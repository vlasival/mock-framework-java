package org.mock.core;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Отвечает за cоздание mock-объектов.
 * Определяет, является ли тип интерфейсом или классом.
 * Для интерфейсов использует Proxy (Java Reflection).
 * Для классов использует ByteBuddy для создания подклассов.
 */
public class MockFramework {
    /**
     * Создает mock-объект для заданного типа.
     */
    public static <T> T createMock(Class<T> type) {
        if (type.isInterface()) {
            return createProxyForInterface(type);
        } else {
            return createProxyForClass(type);
        }
    }

    private static <T> T createProxyForInterface(Class<T> interfaceType) {
        return (T) Proxy.newProxyInstance(
                interfaceType.getClassLoader(),
                new Class<?>[]{interfaceType},
                new MethodInvocationHandler()
        );
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