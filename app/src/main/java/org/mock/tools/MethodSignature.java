package org.mock.tools;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

public class MethodSignature {
    private final Method method;
    private final Object[] args;

    public MethodSignature(Method method, Object[] args) {
        this.method = method;
        this.args = args != null ? args : new Object[0];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodSignature that = (MethodSignature) o;
        return method.equals(that.method) && 
               Arrays.deepEquals(args, that.args);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, Arrays.deepHashCode(args));
    }
}