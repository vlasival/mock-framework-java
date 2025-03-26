package org.mock.behavior;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.mock.tools.MethodSignature;

public class BehaviorRegistry {
    // Для обычных методов
    private static final Map<MethodSignature, BehaviorRule> instanceRules = new HashMap<>();
    // Для статических методов
    private static final Map<Class<?>, Map<MethodSignature, BehaviorRule>> staticRules = new HashMap<>();

    // Регистрация правил для обычных методов
    public static void registerInstanceRule(MethodSignature signature, BehaviorRule rule) {
        instanceRules.put(signature, rule);
    }

    // Регистрация правил для статических методов
    public static void registerStaticRule(Class<?> clazz, MethodSignature signature, BehaviorRule rule) {
        staticRules.computeIfAbsent(clazz, k -> new HashMap<>()).put(signature, rule);
    }

    // Поиск правил для обычных методов
    public static BehaviorRule findInstanceRule(Method method, Object[] args) {
        return instanceRules.get(new MethodSignature(method, args));
    }

    // Поиск правил для статических методов
    public static BehaviorRule findStaticRule(Class<?> clazz, Method method, Object[] args) {
        Map<MethodSignature, BehaviorRule> rules = staticRules.get(clazz);
        return rules != null ? rules.get(new MethodSignature(method, args)) : null;
    }

    // Сброс всех правил
    public static void reset() {
        instanceRules.clear();
        staticRules.clear();
    }
}