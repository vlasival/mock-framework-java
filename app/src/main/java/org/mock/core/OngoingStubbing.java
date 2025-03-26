package org.mock.core;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

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
        if (Modifier.isStatic(method.getModifiers())) {
            BehaviorRegistry.registerStaticRule(
                    method.getDeclaringClass(),
                    new MethodSignature(method, args),
                    new ReturnBehavior<>(value));
        } else {
            BehaviorRegistry.registerInstanceRule(
                    new MethodSignature(method, args),
                    new ReturnBehavior<>(value));
        }
    }

    public void thenThrow(Throwable exception) {
        if (Modifier.isStatic(method.getModifiers())) {
            BehaviorRegistry.registerStaticRule(
                    method.getDeclaringClass(),
                    new MethodSignature(method, args),
                    new ThrowBehavior(exception));
        } else {
            BehaviorRegistry.registerInstanceRule(
                    new MethodSignature(method, args),
                    new ThrowBehavior(exception));
        }
    }
}