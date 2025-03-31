package org.mock.core;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

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

    /**
     * Создает mock-объект для заданного класса или интерфейса.
     * Для интерфейсов используется стандартный Proxy, для классов – ByteBuddy.
     */
    @SuppressWarnings("unchecked")
    public static <T> T mock(Class<T> clazz) {
        MethodInvocationHandler handler = new MethodInvocationHandler();
        if (clazz.isInterface()) {
            return (T) Proxy.newProxyInstance(
                    clazz.getClassLoader(),
                    new Class<?>[] { clazz },
                    handler);
        } else {
            try {
                // Исключаем методы, объявленные в Object, чтобы избежать конфликтов
                return new ByteBuddy()
                        .subclass(clazz)
                        .method(ElementMatchers.not(ElementMatchers.isDeclaredBy(Object.class)))
                        .intercept(MethodDelegation.to(new ByteBuddyInterceptor(handler)))
                        .make()
                        .load(clazz.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
                        .getLoaded()
                        .getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Не удалось создать мок для класса", e);
            }
        }
    }

    /**
     * Захватывает вызов метода для последующего задания поведения.
     * Пример использования:
     * when(mock.someMethod()).thenReturn(42);
     */
    public static <T> OngoingStubbing<T> when(T methodCallResult) {
        InvocationData data = currentInvocation.get();
        if (data == null) {
            throw new IllegalStateException("Не зафиксирован вызов метода для stubbing.");
        }
        currentInvocation.remove();
        return new OngoingStubbing<>(data.handler, data.method, data.args);
    }

    /**
     * Режим для захвата вызова метода.
     */
    public static <T> T startStubbing(T dummy) {
        stubbingMode.set(true);
        return capture(dummy);
    }

    public static void stopStubbing() {
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