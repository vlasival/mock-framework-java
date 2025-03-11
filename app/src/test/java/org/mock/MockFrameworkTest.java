package org.mock;

import org.junit.jupiter.api.Test;
import org.mock.core.MockBehavior;
import org.mock.core.MockFramework;

import static org.junit.jupiter.api.Assertions.*;

class MockFrameworkTest {

    @Test
    void testAddMethod() {
        // Создаем mock-объект для интерфейса Calculator
        Calculator mockCalculator = MockFramework.createMock(Calculator.class);

        // Настраиваем поведение: при вызове add(2, 3) должен вернуться результат 5
        MockBehavior.when(mockCalculator.add(2, 3)).thenReturn(5);

        // Проверяем, что mock работает корректно
        assertEquals(5, mockCalculator.add(2, 3)); // Ожидаем 5
        assertEquals(0, mockCalculator.add(99, 99)); // Не настроено → значение по умолчанию (0)
    }

    @Test
    void testGreetMethod() {
        // Создаем mock-объект для интерфейса Calculator
        Calculator mockCalculator = MockFramework.createMock(Calculator.class);

        // Настраиваем поведение: при вызове greet("John") должен вернуться "Hello, John"
        MockBehavior.when(mockCalculator.greet("John")).thenReturn("Hello, John");

        // Проверяем, что mock работает корректно
        assertEquals("Hello, John", mockCalculator.greet("John")); // Ожидаем "Hello, John"
        assertNull(mockCalculator.greet("Alice")); // Не настроено → значение по умолчанию (null)
    }

    @Test
    void testIsPositiveMethod() {
        // Создаем mock-объект для интерфейса Calculator
        Calculator mockCalculator = MockFramework.createMock(Calculator.class);

        // Настраиваем поведение: при вызове isPositive(10) должен вернуться true
        MockBehavior.when(mockCalculator.isPositive(10)).thenReturn(true);

        // Проверяем, что mock работает корректно
        assertTrue(mockCalculator.isPositive(10)); // Ожидаем true
        assertFalse(mockCalculator.isPositive(-5)); // Не настроено → значение по умолчанию (false)
    }

    @Test
    void testThrowException() {
        // Создаем mock-объект для интерфейса Calculator
        Calculator mockCalculator = MockFramework.createMock(Calculator.class);

        // Настраиваем поведение: при вызове add(1, 1) должно выброситься исключение
        MockBehavior.when(mockCalculator.add(1, 1)).thenThrow(new RuntimeException("Error"));

        // Проверяем, что исключение выбрасывается
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            mockCalculator.add(1, 1);
        });
        assertEquals("Error", exception.getMessage());
    }
}