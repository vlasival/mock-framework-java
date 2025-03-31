package org.mock.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.mock.behavior.BehaviorRegistry;
import org.mock.behavior.BehaviorRule;
import org.mock.tools.DefaultValueProvider;
import org.mock.tools.MethodCall;

public class MethodInvocationHandler implements InvocationHandler {

    private static final ThreadLocal<MethodCall> lastInstanceCall = new ThreadLocal<>();
    private static final ThreadLocal<MethodCall> lastStaticCall = new ThreadLocal<>();

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // Если режим stubbing активен, зафиксировать вызов и вернуть значение по
        // умолчанию
        if (MockFramework.stubbingMode.get()) {
            MockFramework.currentInvocation.set(new MockFramework.InvocationData(this, method, args));
            return DefaultValueProvider.getDefaultValue(method.getReturnType());
        }

        // Обработка методов из Object
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
        // Сохраняем информацию о вызове обычного метода
        lastInstanceCall.set(new MethodCall(method, args));

        BehaviorRule rule = BehaviorRegistry.findInstanceRule(method, args);
        if (rule != null) {
            return rule.execute();
        }

        return DefaultValueProvider.getDefaultValue(method.getReturnType());
    }

    // Для статических методов (вызывается из перехватчика ByteBuddy)
    public static Object handleStaticCall(Method method, Object[] args) throws Throwable {
        MethodCall call = new MethodCall(method, args);
        lastStaticCall.set(call);

        BehaviorRule rule = BehaviorRegistry.findStaticRule(method.getDeclaringClass(), method, args);
        if (rule != null) {
            return rule.execute();
        }

        return DefaultValueProvider.getDefaultValue(method.getReturnType());
    }

    public static MethodCall getLastInstanceCall() {
        return lastInstanceCall.get();
    }

    public static MethodCall getLastStaticCall() {
        return lastStaticCall.get();
    }
}
