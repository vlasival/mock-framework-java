package org.mock.matchers;

public class AnyMatcher<T> implements ArgumentMatcher<T> {
    private final Class<T> type;

    public AnyMatcher(Class<T> type) {
        this.type = type;
    }

    @Override
    public boolean matches(Object argument) {
        return type.isInstance(argument);
    }
}