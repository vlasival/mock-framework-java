package org.mock.core;

import java.util.function.Supplier;

import org.mock.tools.MethodCall;

public class MockBehavior {
    // Для обычных методов
    public static <T> OngoingStubbing<T> when(T methodCall) {
        MethodCall call = MethodInvocationHandler.getLastInstanceCall();
        if (call == null) {
            throw new RuntimeException("No method call recorded. " +
                    "Ensure you're calling a method on a mock object.");
        }
        return new OngoingStubbing<>(call.getMethod(), call.getArgs());
    }

    // Для статических методов
    public static <T> OngoingStubbing<T> when(Supplier<T> methodCall) {
        MethodCall call = MethodInvocationHandler.getLastStaticCall();
        if (call == null) {
            throw new RuntimeException("No static method call recorded. " +
                    "Ensure you're calling a method on a mock object.");
        }
        return new OngoingStubbing<>(call.getMethod(), call.getArgs());
    }
}