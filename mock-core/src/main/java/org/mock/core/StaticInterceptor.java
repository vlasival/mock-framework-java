package org.mock.core;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

import org.mock.behavior.BehaviorRegistry;
import org.mock.tools.DefaultValueProvider;
import org.mock.tools.MethodCall;

public class StaticInterceptor {

    @RuntimeType
    public static Object intercept(@Origin Method method,
                                   @AllArguments Object[] args,
                                   @SuperCall Callable<?> zuper) throws Exception {
        // Если включен режим stubbing, фиксируем вызов
        if (MockFramework.stubbingMode.get()) {
            MockFramework.currentInvocation.set(new MethodCall(method, args));
            return DefaultValueProvider.getDefaultValue(method.getReturnType());
        }
        // Проверяем, задано ли stub-правило для данного вызова
        Object stub = BehaviorRegistry.findInstanceRule(method, args);
        if (stub != null) {
            return stub;
        }
        // Если stub не задан, вызываем оригинальный метод
        return zuper.call();
    }
}
