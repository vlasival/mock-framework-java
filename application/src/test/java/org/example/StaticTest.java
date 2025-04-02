package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.mock.core.MockFramework;
import org.mock.core.StaticMocking;

import static org.mock.core.MockFramework.when;

public class StaticTest {
    @Test
    public void testStaticMethodMocking() {
        StaticMocking.mockStatic(MathUtils.class);
        MockFramework.startStubbing();
        when(MathUtils.square(5)).thenReturn(25);
        MockFramework.stopStubbing();

        int result = MathUtils.square(5);
        assertEquals(25, result);
    }

    public class MathUtils {
        public static int square(int x) {
            return x * x;
        }
    }
}
