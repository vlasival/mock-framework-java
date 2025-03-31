package org.mock.core;

import java.lang.reflect.Method;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;

public class ByteBuddyInterceptor {

    private final MethodInvocationHandler handler;

    public ByteBuddyInterceptor(MethodInvocationHandler handler) {
        this.handler = handler;
    }

    @RuntimeType
    public Object intercept(@This Object proxy,
            @Origin Method method,
            @AllArguments Object[] args) throws Throwable {
        // Исключаем методы Object для избежания неоднозначностей
        if (method.getDeclaringClass().equals(Object.class)) {
            if ("toString".equals(method.getName())) {
                return proxy.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(proxy));
            }
            if ("hashCode".equals(method.getName())) {
                return System.identityHashCode(proxy);
            }
            if ("equals".equals(method.getName())) {
                return proxy == args[0];
            }
        }
        // Если режим stubbing активен, делегируем хэндлеру фиксацию вызова
        if (MockFramework.stubbingMode.get()) {
            MockFramework.currentInvocation.set(new MockFramework.InvocationData(handler, method, args));
            return org.mock.tools.DefaultValueProvider.getDefaultValue(method.getReturnType());
        }
        return handler.invoke(proxy, method, args);
    }
}
