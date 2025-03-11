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
    
    private static final ThreadLocal<MethodCall> lastMethodCall = new ThreadLocal<>();

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MethodCall call = new MethodCall(method, args);
        lastMethodCall.set(call);
        
        BehaviorRule rule = BehaviorRegistry.findRule(method, args);
        if (rule != null) {
            return rule.execute();
        }
        
        return DefaultValueProvider.getDefaultValue(method.getReturnType());
    }

    public static MethodCall getLastMethodCall() {
        return lastMethodCall.get();
    }
}