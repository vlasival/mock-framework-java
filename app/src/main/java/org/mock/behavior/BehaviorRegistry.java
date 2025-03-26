package org.mock.behavior;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mock.matchers.ArgumentMatcher;
import org.mock.matchers.MatcherContext;
import org.mock.tools.DefaultValueProvider;
import org.mock.tools.MethodSignature;

/**
 * Реестр правил поведения для mock-объектов.
 * Хранит связи между вызовами методов и их реакциями.
 */
public class BehaviorRegistry {
    private static final Map<MethodSignature, BehaviorRule> rules = new HashMap<>();

    public static void registerRule(Method method, Object[] args, boolean isStatic, Class<?> clazz) {
        List<ArgumentMatcher<?>> matchers = MatcherContext.getMatchers();
        MethodSignature signature = new MethodSignature(method, args, matchers, isStatic, clazz);
        rules.put(signature, createBehaviorRule(method, args));
        MatcherContext.clearMatchers();
    }

    private static BehaviorRule createBehaviorRule(Method method, Object[] args) {
        // Здесь можно добавить кастомную логику создания правил
        return new BehaviorRule() {
            @Override
            public Object execute() throws Throwable {
                // Заглушка для примера
                return DefaultValueProvider.getDefaultValue(method.getReturnType());
            }
        };
    }

    public static BehaviorRule findRule(Method method, Object[] args, boolean isStatic, Class<?> clazz) {
        return rules.entrySet().stream()
                .filter(entry -> {
                    MethodSignature signature = entry.getKey();
                    return signature.getMethod().equals(method) &&
                            signature.matches(args) &&
                            signature.isStatic() == isStatic &&
                            (clazz == null || clazz.equals(signature.getClazz()));
                })
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
    }
}