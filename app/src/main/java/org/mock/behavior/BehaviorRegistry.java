package org.mock.behavior;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.mock.tools.MethodSignature;

public class BehaviorRegistry {
    private static final Map<MethodSignature, BehaviorRule> rules = new HashMap<>();

    public static void registerRule(MethodSignature signature, BehaviorRule rule) {
        rules.put(signature, rule);
    }

    public static BehaviorRule findRule(Method method, Object[] args) {
        MethodSignature signature = new MethodSignature(method, args);
        return rules.get(signature);
    }
}