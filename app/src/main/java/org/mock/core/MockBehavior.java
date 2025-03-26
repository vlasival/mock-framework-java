package org.mock.core;

import java.util.function.Supplier;

import org.mock.tools.MethodCall;

/**
 * Точка входа для настройки поведения mock-объектов.
 */
public class MockBehavior {
    public static <T> OngoingStubbing<T> when(T methodCall) {
        MethodCall call = MethodInvocationHandler.getLastMethodCall();
        return new OngoingStubbing<>(call.getMethod(), call.getArgs());
    }

    public static <T> OngoingStaticStubbing<T> whenStatic(Class<?> clazz, Supplier<T> methodCall) {
        MethodCall call = MethodInvocationHandler.getLastMethodCall();
        return new OngoingStaticStubbing<>(clazz, call.getMethod(), call.getArgs());
    }
}