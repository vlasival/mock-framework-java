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
        MockFramework.startMocking();
        myService.greet("Alice");
        OngoingStubbing<String> stub = MockFramework.when(null);
        stub.thenReturn("Hello, Alice!");
        MockFramework.stopMocking();

        assertEquals("Hello, Alice!", myService.greet("Alice"));
    }

    @Test
    public void testClassMock() {
        MockFramework.startMocking();
        calculator.add(3, 4);
        OngoingStubbing<Integer> stub = MockFramework.when(null);
        stub.thenReturn(100);
        MockFramework.stopMocking();

        assertEquals(100, calculator.add(3, 4));
    }

    @Test
    public void testThenThrow() {
        MockFramework.startMocking();
        calculator.add(10, 20);
        OngoingStubbing<Integer> stub = MockFramework.when(null);
        stub.thenThrow(new RuntimeException("Ошибка!"));
        MockFramework.stopMocking();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            calculator.add(10, 20);
        });
        assertEquals("Ошибка!", exception.getMessage());
    }
}
