package org.mock.matchers;

import java.util.function.Predicate;

public class PredicateMatcher<T> implements ArgumentMatcher<T> {
    private final Predicate<T> predicate;

    public PredicateMatcher(Predicate<T> predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean matches(Object argument) {
        return predicate.test((T) argument);
    }
}