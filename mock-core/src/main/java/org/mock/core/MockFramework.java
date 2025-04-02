package org.mock.core;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import org.mock.matchers.MatcherContext;
import org.mock.tools.DefaultValueProvider;
import org.mock.tools.MethodCall;

/**
 * Отвечает за cоздание mock-объектов.
 * Определяет, является ли тип интерфейсом или классом.
 * Для интерфейсов использует Proxy (Java Reflection).
 * Для классов использует ByteBuddy для создания подклассов.
 */
public class MockFramework {

    static final ThreadLocal<MethodCall> currentInvocation = new ThreadLocal<>();
    static final ThreadLocal<Boolean> stubbingMode = ThreadLocal.withInitial(() -> false);

    /**
     * Создает mock-объект для заданного класса или интерфейса.
     * Для интерфейсов используется стандартный Proxy, для классов – ByteBuddy.
     */
    @SuppressWarnings("unchecked")
    public static <T> T mock(Class<T> clazz) throws Exception {
        if (clazz.isInterface()) {
            MethodInvocationHandler handler = new MethodInvocationHandler();
            return (T) Proxy.newProxyInstance(
                    clazz.getClassLoader(),
                    new Class[] { clazz },
                    handler);
        } else {
            Constructor<?> constructor = findConstructor(clazz);
            Object[] args = generateDefaultArgs(constructor.getParameterTypes());

            MethodInvocationHandler handler = new MethodInvocationHandler();
            return (T) new ByteBuddy()
                    .subclass(clazz)
                    .method(ElementMatchers.any())
                    .intercept(InvocationHandlerAdapter.of(handler))
                    .make()
                    .load(clazz.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
                    .getLoaded()
                    .getDeclaredConstructor(constructor.getParameterTypes())
                    .newInstance(args);
        }
    }

    /**
     * Метод для поиска конструктора класса.
     * Если конструктор без аргументов не найден, возвращает первый доступный.
     */
    private static Constructor<?> findConstructor(Class<?> clazz) throws NoSuchMethodException {
        try {
            return clazz.getDeclaredConstructor(); // Ищем конструктор без аргументов
        } catch (NoSuchMethodException e) {
            // Если такого конструктора нет, берём первый доступный
            Constructor<?>[] constructors = clazz.getDeclaredConstructors();
            if (constructors.length > 0) {
                constructors[0].setAccessible(true);
                return constructors[0];
            }
            throw new NoSuchMethodException("Нет доступного конструктора у класса " + clazz.getName());
        }
    }

    /**
     * Создаёт дефолтные значения для аргументов конструктора.
     */
    private static Object[] generateDefaultArgs(Class<?>[] paramTypes) {
        Object[] args = new Object[paramTypes.length];
        for (int i = 0; i < paramTypes.length; i++) {
            args[i] = DefaultValueProvider.getDefaultValue(paramTypes[i]);
        }
        return args;
    }

    /**
     * Захватывает вызов метода для последующего задания поведения.
     */
    public static <T> OngoingStubbing<T> when(T methodCall) {
        MethodCall data = currentInvocation.get();
        if (data == null) {
            throw new IllegalStateException("Не зафиксирован вызов метода для stubbing.");
        }
        currentInvocation.remove();
        return new OngoingStubbing<>(data.getMethod(), data.getArgs());
    }

    /**
     * Режим для захвата вызова метода.
     */
    public static void startMocking() {
        stubbingMode.set(true);
    }

    public static void stopMocking() {
        stubbingMode.set(false);
    }

    public static void recordCall(Method method, Object[] args) {
        // Для каждого аргумента проверяем, есть ли matcher в MatcherContext.
        Object[] processedArgs = new Object[args == null ? 0 : args.length];
        for (int i = 0; i < processedArgs.length; i++) {
            Object matcher = MatcherContext.pollMatcher();
            processedArgs[i] = (matcher != null) ? matcher : args[i];
        }
        currentInvocation.set(new MethodCall(method, processedArgs));
    }
}