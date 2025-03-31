package org.mock;

public class Calculator {
    int add(int a, int b) {
        return a + b;
    }

    String greet(String name) {
        return "Hello, " + name;
    }

    boolean isPositive(int number) {
        return number > 0;
    }
}