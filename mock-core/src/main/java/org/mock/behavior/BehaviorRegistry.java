package org.mock.behavior;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class BehaviorRegistry {
    private static final Map<String, BehaviorRule> instanceRules = new HashMap<>();

    public static void registerInstanceRule(Method method, Object[] args, BehaviorRule rule) {
        instanceRules.put(getKey(method, args), rule);
    }

    public static BehaviorRule findInstanceRule(Method method, Object[] args) {
        return instanceRules.get(getKey(method, args));
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

    public static void clear() {
        instanceRules.clear();
    }
}
