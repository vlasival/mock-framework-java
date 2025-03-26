package org.mock.core;

import java.lang.reflect.Method;

import org.mock.behavior.BehaviorRegistry;
import org.mock.behavior.ReturnBehavior;
import org.mock.behavior.ThrowBehavior;

public class OngoingStaticStubbing<T> {
    private final Class<?> clazz;
    private final Method method;
    private final Object[] args;

    public OngoingStaticStubbing(Class<?> clazz, Method method, Object[] args) {
        this.clazz = clazz;
        this.method = method;
        this.args = args;
    }

    public void thenReturn(T value) {
        BehaviorRegistry.registerStaticRule(clazz, method, args, new ReturnBehavior<>(value));
    }

    public void thenThrow(Throwable exception) {
        BehaviorRegistry.registerStaticRule(clazz, method, args, new ThrowBehavior(exception));
    }
}