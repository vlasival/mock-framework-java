package org.mock.matchers;

import java.util.ArrayList;
import java.util.List;

public class MatcherContext {
    private static final ThreadLocal<List<Object>> matchers = ThreadLocal.withInitial(ArrayList::new);

    public static void registerMatcher(Object matcher) {
        matchers.get().add(matcher);
    }

    public static Object pollMatcher() {
        List<Object> list = matchers.get();
        if (!list.isEmpty()) {
            return list.remove(0);
        }
        return null;
    }

    public static void clear() {
        matchers.get().clear();
    }
}
