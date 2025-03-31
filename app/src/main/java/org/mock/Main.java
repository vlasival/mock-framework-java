package org.mock;

import org.mock.core.MockBehavior;
import org.mock.core.MockFramework;

public class Main {
    public static void main(String[] args) {
        Calculator mockCalculator = MockFramework.createMock(Calculator.class);

        MockBehavior.when(mockCalculator.add(2, 3)).thenReturn(5);

        System.out.println(mockCalculator.add(2, 3));
        System.out.println(mockCalculator.add(99, 99));
    }
}
