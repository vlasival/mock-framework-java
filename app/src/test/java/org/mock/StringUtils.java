package org.mock;

class StringUtils {
    public static String reverse(String input) {
        return new StringBuilder(input).reverse().toString();
    }

    public static int multiply(int a, int b) {
        return a * b;
    }
}