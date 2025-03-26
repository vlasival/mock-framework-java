package org.mock.matchers;

import java.util.function.Predicate;

/**
 * Фабрика матчеров для гибкой настройки аргументов методов.
 */
public class Matchers {
    public static <T> T eq(T value) {
        registerMatcher(new EqMatcher<>(value));
        return value;
    }

    public static <T> T any(Class<T> type) {
        registerMatcher(new AnyMatcher<>(type));
        return null;
    }

    public static <T> T argThat(Predicate<T> predicate) {
        registerMatcher(new PredicateMatcher<>(predicate));
        return null;
    }

    public static <T> T isNull() {
        registerMatcher(new NullMatcher<>());
        return null;
    }

    public static <T> T notNull() {
        registerMatcher(new NotNullMatcher<>());
        return null;
    }

    public static String matches(String regex) {
        registerMatcher(new RegexMatcher(regex));
        return null;
    }

    private static void registerMatcher(ArgumentMatcher<?> matcher) {
        MatcherContext.addMatcher(matcher);
    }
}
