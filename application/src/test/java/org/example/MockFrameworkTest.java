package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mock.annotation.Mock;
import org.mock.core.MockFramework;
import org.mock.core.OngoingStubbing;

import static org.junit.jupiter.api.Assertions.*;

public class MockFrameworkTest {

    @Mock
    private MyService myService;

    @Mock
    private Calculator calculator;

    /**
     * Простая реализация автоматической инъекции мок‑объектов.
     */
    @BeforeEach
    public void initMocks() throws Exception {
        for (java.lang.reflect.Field field : this.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Mock.class)) {
                field.setAccessible(true);
                Object mockInstance = MockFramework.mock(field.getType());
                field.set(this, mockInstance);
            }
        }
    }

    // Интерфейс для демонстрации мокирования интерфейса
    public interface MyService {
        String greet(String name);
    }

    // Класс для демонстрации мокирования класса
    public static class Calculator {
        public int add(int a, int b) {
            return a + b;
        }
    }

    @Test
    public void testInterfaceMock() {
        // Настройка поведения для метода интерфейса
        myService.greet("Alice");
        OngoingStubbing<String> stub = MockFramework.when(null);
        stub.thenReturn("Hello, Alice!");

        // Проверка корректного возвращаемого значения
        assertEquals("Hello, Alice!", myService.greet("Alice"));
    }

    @Test
    public void testClassMock() {
        // Настройка поведения для метода класса
        calculator.add(3, 4);
        OngoingStubbing<Integer> stub = MockFramework.when(null);
        stub.thenReturn(100);

        // Проверка корректного возвращаемого значения
        assertEquals(100, calculator.add(3, 4));
    }

    @Test
    public void testThenThrow() {
        // Настройка поведения для выброса исключения
        calculator.add(10, 20);
        OngoingStubbing<Integer> stub = MockFramework.when(null);
        stub.thenThrow(new RuntimeException("Ошибка!"));

        // Проверка выброса исключения с нужным сообщением
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            calculator.add(10, 20);
        });
        assertEquals("Ошибка!", exception.getMessage());
    }
}
