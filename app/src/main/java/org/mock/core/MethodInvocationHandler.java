package org.mock.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.mock.behavior.BehaviorRegistry;
import org.mock.behavior.BehaviorRule;
import org.mock.tools.DefaultValueProvider;
import org.mock.tools.MethodCall;

/**
 * Отвечает за перехват вызовов методов mock-объектов.
 */
public class MethodInvocationHandler implements InvocationHandler {
    private static final ThreadLocal<MethodCall> lastInstanceCall = new ThreadLocal<>();
    private static final ThreadLocal<MethodCall> lastStaticCall = new ThreadLocal<>();

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // Сохраняем информацию о вызове обычного метода
        MethodCall call = new MethodCall(method, args);
        lastInstanceCall.set(call);

        // Ищем правило в реестре
        BehaviorRule rule = BehaviorRegistry.findInstanceRule(method, args);
        if (rule != null) {
            return rule.execute();
        }

        // Значение по умолчанию
        return DefaultValueProvider.getDefaultValue(method.getReturnType());
    }

    // Для статических методов (вызывается из перехватчика)
    public static Object handleStaticCall(Method method, Object[] args) throws Throwable {
        // Сохраняем информацию о вызове статического метода
        MethodCall call = new MethodCall(method, args);
        lastStaticCall.set(call);

        // Ищем правило в реестре
        BehaviorRule rule = BehaviorRegistry.findStaticRule(method.getDeclaringClass(), method, args);
        if (rule != null) {
            return rule.execute();
        }

        // Значение по умолчанию
        return DefaultValueProvider.getDefaultValue(method.getReturnType());
    }

    // Для доступа к последнему вызову обычного метода
    public static MethodCall getLastInstanceCall() {
        return lastInstanceCall.get();
    }

    // Для доступа к последнему вызову статического метода
    public static MethodCall getLastStaticCall() {
        return lastStaticCall.get();
    }
}