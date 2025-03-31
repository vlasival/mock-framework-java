package org.mock.core;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import org.mock.behavior.BehaviorRegistry;

/**
 * Отвечает за cоздание mock-объектов.
 * Определяет, является ли тип интерфейсом или классом.
 * Для интерфейсов использует Proxy (Java Reflection).
 * Для классов использует ByteBuddy для создания подклассов.
 */
public class MockFramework {

    private static final Map<Class<?>, Object> staticMocks = new HashMap<>();

    // Существующий метод для обычных объектов
    public static <T> T mock(Class<T> type) {
        return createMock(type);
    }

    // Новый метод для статических методов
    public static <T> void mockStatic(Class<T> type) {
        if (staticMocks.containsKey(type)) {
            System.out.println("Класс уже замокан");
            return;
        }

        try {
            Class<?> subclass = new ByteBuddy()
                    .redefine(type) // Модифицируем существующий класс
                    .method(ElementMatchers.isStatic()) // Перехватываем статические методы
                    .intercept(MethodDelegation.to(new StaticMethodInterceptor())) // Перенаправляем вызовы
                    .make()
                    .load(type.getClassLoader())
                    .getLoaded();

            staticMocks.put(type, subclass.getDeclaredConstructor().newInstance());
        } catch (Exception e) {
            throw new RuntimeException("Не удалось замокать статический класс", e);
        }
    }

    // Сброс всех моков (важно для тестов)
    public static void resetMocks() {
        staticMocks.clear();
        BehaviorRegistry.reset();
    }

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
                new Class<?>[] { interfaceType },
                new MethodInvocationHandler());
    }

    private static <T> T createProxyForClass(Class<T> classType) {
        try {
            return new ByteBuddy()
                    .subclass(classType)
                    .method(ElementMatchers.not(ElementMatchers.isDeclaredBy(Object.class)))
                    .intercept(MethodDelegation.to(new MethodInvocationHandler()))
                    .make()
                    .load(classType.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
                    .getLoaded()
                    .getDeclaredConstructor()
                    .newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}