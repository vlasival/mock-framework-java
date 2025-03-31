package org.mock.core;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.mock.tools.DefaultValueProvider;

/**
 * Отвечает за cоздание mock-объектов.
 * Определяет, является ли тип интерфейсом или классом.
 * Для интерфейсов использует Proxy (Java Reflection).
 * Для классов использует ByteBuddy для создания подклассов.
 */
public class MockFramework {
    // ThreadLocal для хранения информации о последнем вызове метода в режиме
    // stubbing
    static final ThreadLocal<InvocationData> currentInvocation = new ThreadLocal<>();
    static final ThreadLocal<Boolean> stubbingMode = ThreadLocal.withInitial(() -> false);
    private static final ThreadLocal<MethodInvocationHandler> handlerRef = new ThreadLocal<>();

    /**
     * Создает mock-объект для заданного класса или интерфейса.
     * Для интерфейсов используется стандартный Proxy, для классов – ByteBuddy.
     */
    @SuppressWarnings("unchecked")
    public static <T> T mock(Class<T> clazz) throws Exception {
        if (clazz.isInterface()) {
            MethodInvocationHandler handler = new MethodInvocationHandler();
            handlerRef.set(handler);
            return (T) Proxy.newProxyInstance(
                    clazz.getClassLoader(),
                    new Class[] { clazz },
                    handler);
        } else {
            Constructor<?> constructor = findConstructor(clazz);
            Object[] args = generateDefaultArgs(constructor.getParameterTypes());

            MethodInvocationHandler handler = new MethodInvocationHandler();
            ByteBuddyInterceptor interceptor = new ByteBuddyInterceptor(handler);
            handlerRef.set(handler);
            return (T) new ByteBuddy()
                    .subclass(clazz)
                    .method(ElementMatchers.any())
                    .intercept(MethodDelegation.to(interceptor))
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
     * Возвращает дефолтное значение для типа параметра.
     */
    private static Object getDefaultValue(Class<?> type) {
        if (type.isPrimitive()) {
            if (type == boolean.class)
                return false;
            if (type == byte.class)
                return (byte) 0;
            if (type == short.class)
                return (short) 0;
            if (type == int.class)
                return 0;
            if (type == long.class)
                return 0L;
            if (type == float.class)
                return 0.0f;
            if (type == double.class)
                return 0.0;
            if (type == char.class)
                return '\u0000';
        }
        return null;
    }

    /**
     * Захватывает вызов метода для последующего задания поведения.
     * Пример использования:
     * when(mock.someMethod()).thenReturn(42);
     */
    public static <T> OngoingStubbing<T> when(T methodCallResult) {
        stubbingMode.set(true);
        try {
            InvocationData data = currentInvocation.get();
            if (data == null) {
                throw new IllegalStateException("Не зафиксирован вызов метода для stubbing.");
            }
            currentInvocation.remove();
            return new OngoingStubbing<>(data.handler, data.method, data.args);
        } finally {
            stubbingMode.set(false);
        }
    }

    /**
     * Режим для захвата вызова метода.
     */
    private static <T> T startStubbing(T dummy) {
        stubbingMode.set(true);
        return capture(dummy);
    }

    private static void stopStubbing() {
        stubbingMode.set(false);
    }

    /**
     * Вспомогательный метод для захвата вызова.
     */
    static <T> T capture(T dummy) {
        return dummy;
    }

    /**
     * Данные о захваченном вызове.
     */
    static class InvocationData {
        final MethodInvocationHandler handler;
        final Method method;
        final Object[] args;

        InvocationData(MethodInvocationHandler handler, Method method, Object[] args) {
            this.handler = handler;
            this.method = method;
            this.args = args;
        }
    }
}