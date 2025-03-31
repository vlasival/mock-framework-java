package org.mock;

import org.mock.core.MockFramework;
import org.mock.core.OngoingStubbing;

public class Example {

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

    public static void main(String[] args) {
        // Очистка реестра правил (если необходимо)
        org.mock.behavior.BehaviorRegistry.clear();

        // Мок для интерфейса
        MyService myService = MockFramework.mock(MyService.class);
        // Захватываем вызов для задания поведения
        MockFramework.startStubbing(null);
        myService.greet("John");
        OngoingStubbing<String> stubInterface = MockFramework.when(null);
        MockFramework.stopStubbing();
        stubInterface.thenReturn("Hello, John!");
        System.out.println("Интерфейс: " + myService.greet("John"));

        // Мок для класса с использованием ByteBuddy
        Calculator calculator = MockFramework.mock(Calculator.class);
        MockFramework.startStubbing(null);
        calculator.add(2, 3);
        OngoingStubbing<Integer> stubClass = MockFramework.when(null);
        MockFramework.stopStubbing();
        stubClass.thenReturn(42);
        System.out.println("Класс: " + calculator.add(2, 3));

        // Демонстрация thenThrow() для класса
        MockFramework.startStubbing(null);
        calculator.add(5, 5);
        OngoingStubbing<Integer> stubThrow = MockFramework.when(null);
        MockFramework.stopStubbing();
        stubThrow.thenThrow(new RuntimeException("Ошибка в методе add"));
        try {
            calculator.add(5, 5);
        } catch (Exception e) {
            System.out.println("Поймано исключение: " + e.getMessage());
        }
    }
}
