package org.mock.core;

import java.lang.reflect.Method;

import org.mock.behavior.BehaviorRegistry;
import org.mock.behavior.BehaviorRule;

public class OngoingStubbing<T> {

    private final MethodInvocationHandler handler;
    private final Method method;
    private final Object[] args;

    public OngoingStubbing(MethodInvocationHandler handler, Method method, Object[] args) {
        this.handler = handler;
        this.method = method;
        this.args = args;
    }

    /**
     * Задает возвращаемое значение для ранее зафиксированного вызова.
     */
    public void thenReturn(T value) {
        BehaviorRule rule = () -> value;
        BehaviorRegistry.registerInstanceRule(method, args, rule);
    }

    /**
     * Задает выброс исключения для ранее зафиксированного вызова.
     */
    public void thenThrow(Throwable t) {
        BehaviorRule rule = () -> {
            throw t;
        };
        BehaviorRegistry.registerInstanceRule(method, args, rule);
    }
}
