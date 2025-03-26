package org.mock.core;

import java.lang.reflect.Method;

import org.mock.behavior.BehaviorRegistry;
import org.mock.behavior.BehaviorRule;
import org.mock.tools.DefaultValueProvider;

import net.bytebuddy.asm.Advice.AllArguments;
import net.bytebuddy.asm.Advice.Origin;

public class StaticMethodInterceptor {
    public static Object intercept(@Origin Method method, @AllArguments Object[] args) throws Throwable {
        BehaviorRule rule = BehaviorRegistry.findRule(
                method,
                args,
                true,
                method.getDeclaringClass());

        if (rule != null) {
            return rule.execute();
        }

        return DefaultValueProvider.getDefaultValue(method.getReturnType());
    }
}