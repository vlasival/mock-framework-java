package org.mock.core;

import java.util.function.Supplier;

import org.mock.tools.MethodCall;

public class MockBehavior {
    // Для обычных методов
    public static <T> OngoingStubbing<T> when(T methodCall) {
        MethodCall call = MethodInvocationHandler.getLastInstanceCall();
        return new OngoingStubbing<>(call.getMethod(), call.getArgs());
    }

    // Для статических методов
    public static <T> OngoingStubbing<T> when(Supplier<T> methodCall) {
        MethodCall call = MethodInvocationHandler.getLastStaticCall();
        return new OngoingStubbing<>(call.getMethod(), call.getArgs());
    }
}