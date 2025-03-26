package org.mock.matchers;

import java.util.regex.Pattern;

public class RegexMatcher implements ArgumentMatcher<String> {
    private final Pattern pattern;

    public RegexMatcher(String regex) {
        this.pattern = Pattern.compile(regex);
    }

    @Override
    public boolean matches(Object argument) {
        return argument != null && pattern.matcher((String) argument).matches();
    }
}