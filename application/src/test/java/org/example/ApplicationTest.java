package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mock.core.MockFramework;
import org.mock.junit.MockingExtension;
import org.mock.annotation.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mock.core.MockFramework.when;

@ExtendWith(MockingExtension.class)
class ApplicationTest {

    @Mock
    private SomeService service;

    @Test
    void testMocking() {
        MockFramework.startStubbing();
        when(service.someMethod()).thenReturn("Mocked Response");
        MockFramework.stopStubbing();
        
        assertEquals("Mocked Response", service.someMethod());
    }

    interface SomeService {
        String someMethod();
    }

    @Mock
    private Calculator calculator;

    @Test
    public void testClassMock() {
        MockFramework.startStubbing();
        when(calculator.add(3, 4)).thenReturn(100);
        MockFramework.stopStubbing();


        assertEquals(100, calculator.add(3, 4));
    }

    public class Calculator {
        public int add(int a, int b) {
            return a + b;
        }
    }
}
