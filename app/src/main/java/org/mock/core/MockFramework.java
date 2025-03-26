package org.mock.core;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * Отвечает за cоздание mock-объектов.
 * Определяет, является ли тип интерфейсом или классом.
 * Для интерфейсов использует Proxy (Java Reflection).
 * Для классов использует ByteBuddy для создания подклассов.
 */
public class MockFramework {
    private static final Map<Class<?>, Object> staticMocks = new HashMap<>();

    public static <T> T mock(Class<T> type) {
        return createMock(type);
    }

    public static <T> void mockStatic(Class<T> type) {
        if (staticMocks.containsKey(type)) {
            return;
        }

        try {
            Class<?> subclass = new ByteBuddy()
                    .redefine(type)
                    .method(ElementMatchers.isStatic())
                    .intercept(MethodDelegation.to(new StaticMethodInterceptor()))
                    .make()
                    .load(type.getClassLoader())
                    .getLoaded();

            staticMocks.put(type, subclass.getDeclaredConstructor().newInstance());
        } catch (Exception e) {
            throw new RuntimeException("Failed to mock static class", e);
        }
    }

    private static <T> T createMock(Class<T> type) {
        if (type.isInterface()) {
            return createProxyForInterface(type);
        } else {
            return createProxyForClass(type);
        }
    }

    private static <T> T createProxyForInterface(Class<T> interfaceType) {
        return (T) Proxy.newProxyInstance(
                interfaceType.getClassLoader(),
                new Class<?>[] { interfaceType },
                new MethodInvocationHandler());
    }

    private static <T> T createProxyForClass(Class<T> classType) {
        try {
            return new ByteBuddy()
                    .subclass(classType)
                    .method(ElementMatchers.any())
                    .intercept(MethodDelegation.to(new MethodInvocationHandler()))
                    .make()
                    .load(classType.getClassLoader())
                    .getLoaded()
                    .newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}