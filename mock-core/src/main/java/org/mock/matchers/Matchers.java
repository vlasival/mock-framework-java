package org.mock.matchers;

public class Matchers {

    /**
     * Для объектов: матчирует любое значение.
     */
    public static <T> Matcher<T> any() {
        Matcher<T> matcher = argument -> true;
        MatcherContext.registerMatcher(matcher);
        return matcher;
    }

    /**
     * Для объектов: матчирует значение по equals.
     */
    public static <T> Matcher<T> eq(T value) {
        Matcher<T> matcher = argument -> {
            if (value == null) {
                return argument == null;
            }
            return value.equals(argument);
        };
        MatcherContext.registerMatcher(matcher);
        return matcher;
    }

    /**
     * Для int: матчирует любое значение. Возвращает 0, но регистрирует
     * Matcher<Integer>.
     */
    public static int anyInt() {
        Matcher<Integer> matcher = arg -> true;
        MatcherContext.registerMatcher(matcher);
        return 0;
    }

    /**
     * Для int: матчирует равенство с заданным значением. Возвращает это значение,
     * но регистрирует Matcher<Integer>.
     */
    public static int eqInt(int value) {
        Matcher<Integer> matcher = arg -> arg == value;
        MatcherContext.registerMatcher(matcher);
        return value;
    }

    /**
     * Для строк: матчирует любое значение.
     * Регистрирует Matcher<String> и возвращает пустую строку, чтобы удовлетворить
     * сигнатуре метода.
     */
    public static String anyString() {
        Matcher<String> matcher = argument -> true;
        MatcherContext.registerMatcher(matcher);
        return "";
    }

    /**
     * Для строк: матчирует, что значение начинается с заданного префикса.
     * Регистрирует Matcher<String> и возвращает переданный префикс (или любое
     * строковое значение),
     * чтобы удовлетворить сигнатуре метода.
     */
    public static String startsWith(String prefix) {
        Matcher<String> matcher = argument -> argument != null && argument.startsWith(prefix);
        MatcherContext.registerMatcher(matcher);
        return prefix;
    }

    /**
     * Матчер, проверяющий, что числовое значение больше заданного.
     * Подходит для любых числовых типов (с autoboxing).
     */
    public static Matcher<Number> greaterThan(Number value) {
        return argument -> argument.doubleValue() > value.doubleValue();
    }

    /**
     * Матчер, проверяющий, что числовое значение меньше заданного.
     */
    public static Matcher<Number> lessThan(Number value) {
        return argument -> argument.doubleValue() < value.doubleValue();
    }

    /**
     * Матчер для строк, проверяющий, что строка содержит подстроку.
     */
    public static Matcher<String> contains(String substring) {
        return argument -> argument != null && argument.contains(substring);
    }

}
