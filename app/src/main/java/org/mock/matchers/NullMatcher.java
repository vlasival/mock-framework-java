package org.mock.matchers;

public class NullMatcher<T> implements ArgumentMatcher<T> {
    @Override
    public boolean matches(Object argument) {
        return argument == null;
    }
}