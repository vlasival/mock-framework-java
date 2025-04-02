package org.mock.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.mock.behavior.BehaviorRegistry;
import org.mock.behavior.BehaviorRule;
import org.mock.tools.DefaultValueProvider;

public class MethodInvocationHandler implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // Если мы находимся в режиме stubbing, сохраняем информацию о вызове
        if (MockFramework.stubbingMode.get()) {
            MockFramework.recordCall(method, args);
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

        BehaviorRule rule = BehaviorRegistry.findInstanceRule(method, args);
        if (rule != null) {
            return rule.execute();
        }

        return DefaultValueProvider.getDefaultValue(method.getReturnType());
    }

}
