package org.mock.core;

import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.matcher.ElementMatchers;

public class StaticMocking {
    static {
        // Устанавливаем агент ByteBuddy (это нужно сделать один раз)
        ByteBuddyAgent.install();
    }

    /**
     * Перезаписывает класс так, чтобы все статические методы перехватывались.
     * После вызова этого метода, вызовы статических методов будут перенаправляться
     * в наш Advice.
     */
    public static void mockStatic(Class<?> clazz) {
        new ByteBuddy()
                .redefine(clazz)
                .visit(Advice.to(StaticMethodAdvice.class)
                        .on(ElementMatchers.isStatic()))
                .make()
                .load(clazz.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());
    }
}
