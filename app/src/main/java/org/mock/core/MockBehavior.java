package org.mock.core;

import org.mock.tools.MethodCall;

public class MockBehavior {
    public static <T> OngoingStubbing<T> when(T methodCall) {
        MethodCall call = MethodInvocationHandler.getLastMethodCall();
        return new OngoingStubbing<>(call.getMethod(), call.getArgs());
    }
}