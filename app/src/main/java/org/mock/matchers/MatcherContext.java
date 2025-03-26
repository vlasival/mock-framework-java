package org.mock.matchers;

import java.util.ArrayList;
import java.util.List;

public class MatcherContext {
    private static final ThreadLocal<List<ArgumentMatcher<?>>> matchers = 
        ThreadLocal.withInitial(ArrayList::new);

    public static void addMatcher(ArgumentMatcher<?> matcher) {
        matchers.get().add(matcher);
    }

    public static List<ArgumentMatcher<?>> getMatchers() {
        return matchers.get();
    }

    public static void clearMatchers() {
        matchers.get().clear();
    }
}