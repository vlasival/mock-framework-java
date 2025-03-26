package org.mock.tools;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;

import org.mock.matchers.ArgumentMatcher;

/**
 * Сигнатура метода, используется для сравнения вызовов методов.
 */
public class MethodSignature {
    private final Method method;
    private final List<ArgumentMatcher<?>> matchers;
    private final boolean isStatic;
    private final Class<?> clazz;

    public MethodSignature(Method method, Object[] args, List<ArgumentMatcher<?>> matchers, boolean isStatic, Class<?> clazz) {
        this.method = method;
        this.matchers = matchers;
        this.isStatic = isStatic;
        this.clazz = clazz;
    }

    public boolean matches(Object[] args) {
        if (method.getParameterCount() != args.length) return false;
        for (int i = 0; i < args.length; i++) {
            if (matchers != null && i < matchers.size()) {
                if (!matchers.get(i).matches(args[i])) return false;
            } else {
                if (!Objects.equals(args[i], method.getParameters()[i])) return false;
            }
        }
        return true;
    }

    // Геттеры
    public Method getMethod() { return method; }
    public boolean isStatic() { return isStatic; }
    public Class<?> getClazz() { return clazz; }
}