package org.mock.core;

import net.bytebuddy.asm.Advice;

import java.lang.reflect.Method;

import org.mock.behavior.BehaviorRegistry;
import org.mock.tools.MethodCall;

public class StaticMethodAdvice {

    @Advice.OnMethodEnter(skipOn = Advice.OnNonDefaultValue.class)
    public static Object onEnter(@Advice.Origin Method method,
            @Advice.AllArguments Object[] args) {
        // Если мы находимся в режиме stubbing, записываем вызов
        if (MockFramework.stubbingMode.get()) {
            MockFramework.currentInvocation.set(new MethodCall(method, args));
        }

        Object stub = BehaviorRegistry.findInstanceRule(method, args);
        if (stub != null) {
            return stub;
        }

        return null;
    }

    @Advice.OnMethodExit
    public static void onExit(@Advice.Enter Object stubValue,
            @Advice.Return(readOnly = false) Object returned) {
                
        if (stubValue != null) {
            returned = stubValue;
        }
    }
}
