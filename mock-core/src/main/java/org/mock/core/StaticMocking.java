package org.mock.core;

import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

public class StaticMocking {
    static {
        // Убедитесь, что ByteBuddyAgent загружен
        ByteBuddyAgent.install();
    }

    /**
     * Метод для создания mock-объекта для статического метода.
     * Использует ByteBuddy для создания прокси-объекта.
     */
    public static <T> T mockStatic(Class<T> clazz) throws Exception {
        return new ByteBuddy()
                .redefine(clazz)
                .method(ElementMatchers.any())
                .intercept(MethodDelegation.to(StaticMethodAdvice.class))
                .make()
                .load(clazz.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent())
                .getLoaded()
                .getDeclaredConstructor()
                .newInstance();
    }
}
