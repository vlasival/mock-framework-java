package org.mock.behavior;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class BehaviorRegistry {
    // Карты для хранения правил для instance и static вызовов
    private static final Map<String, BehaviorRule> instanceRules = new HashMap<>();
    private static final Map<String, BehaviorRule> staticRules = new HashMap<>();

    public static void registerInstanceRule(Method method, Object[] args, BehaviorRule rule) {
        instanceRules.put(getKey(method, args), rule);
    }

    public static BehaviorRule findInstanceRule(Method method, Object[] args) {
        return instanceRules.get(getKey(method, args));
    }

    public static void registerStaticRule(Class<?> clazz, Method method, Object[] args, BehaviorRule rule) {
        staticRules.put(getKey(method, args), rule);
    }

    public static BehaviorRule findStaticRule(Method method, Object[] args) {
        return staticRules.get(getKey(method, args));
    }

    private static String getKey(Method method, Object[] args) {
        StringBuilder sb = new StringBuilder();
        sb.append(method.toGenericString());
        if (args != null) {
            for (Object arg : args) {
                sb.append("#").append(arg);
            }
        }
        return sb.toString();
    }

    // Для очистки правил между тестами
    public static void clear() {
        instanceRules.clear();
        staticRules.clear();
    }
}
