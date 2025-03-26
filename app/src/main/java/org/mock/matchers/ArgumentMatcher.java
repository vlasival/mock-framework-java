package org.mock.matchers;

public interface ArgumentMatcher<T> {
    boolean matches(Object argument);
}