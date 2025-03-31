package org.mock.tools;

import java.lang.reflect.Method;
import java.util.Arrays;

public class MethodCall {
    private final Method method;
    private final Object[] args;

    public MethodCall(Method method, Object[] args) {
        this.method = method;
        this.args = args != null ? args : new Object[0];
    }

    public Method getMethod() {
        return method;
    }

    public Object[] getArgs() {
        return args;
    }

    @Override
    public String toString() {
        return "MethodCall{" +
                "method=" + method +
                ", args=" + Arrays.toString(args) +
                '}';
    }
}
