package org.mock.core;

import java.lang.reflect.Method;

import org.mock.behavior.BehaviorRegistry;
import org.mock.behavior.ReturnBehavior;
import org.mock.behavior.ThrowBehavior;
import org.mock.tools.MethodSignature;

public class OngoingStubbing<T> {
    private final Method method;
    private final Object[] args;

    public OngoingStubbing(Method method, Object[] args) {
        this.method = method;
        this.args = args;
    }

    public void thenReturn(T value) {
        MethodSignature signature = new MethodSignature(method, args);
        BehaviorRegistry.registerRule(signature, new ReturnBehavior<>(value));
    }

    public void thenThrow(Throwable exception) {
        MethodSignature signature = new MethodSignature(method, args);
        BehaviorRegistry.registerRule(signature, new ThrowBehavior(exception));
    }
}