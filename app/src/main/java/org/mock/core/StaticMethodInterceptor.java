package org.mock.core;

import java.lang.reflect.Method;

import net.bytebuddy.asm.Advice.Origin;
import net.bytebuddy.asm.MemberSubstitution.AllArguments;

public class StaticMethodInterceptor {
    public static Object intercept(@Origin Method method, @AllArguments Object[] args) throws Throwable {
        return MethodInvocationHandler.handleStaticCall(method, args);
    }
}