package org.mock.matchers;

public class EqMatcher<T> implements ArgumentMatcher<T> {
    private final T expected;

    public EqMatcher(T expected) {
        this.expected = expected;
    }

    @Override
    public boolean matches(Object argument) {
        return expected.equals(argument);
    }
}