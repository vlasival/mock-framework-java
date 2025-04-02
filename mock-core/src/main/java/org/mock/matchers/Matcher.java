package org.mock.matchers;

@FunctionalInterface
public interface Matcher<T> {
    boolean matches(T argument);
}
