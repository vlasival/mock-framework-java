package org.example;

import static org.mock.core.MockFramework.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mock.annotation.Mock;
import org.mock.core.MockFramework;
import org.mock.junit.MockingExtension;

@ExtendWith(MockingExtension.class)
class CoverTest {

    public interface Service {
        String getData();
        String echo(String input);
        int getValue();
        int sum(int a, int b);
    }

    @Mock
    private Service service;

    // Класс для мокирования
    public static class Processor {
        public String process(String input) {
            return "Processed: " + input;
        }

        public int multiply(int a, int b) {
            return a * b;
        }
    }

    @Mock
    private Processor processor;

    @Test
    void testServiceGetData() {
        MockFramework.startMocking();
        when(service.getData()).thenReturn("Test Data");
        MockFramework.stopMocking();

        assertEquals("Test Data", service.getData());
    }

    @Test
    void testServiceEcho() {
        MockFramework.startMocking();
        when(service.echo("Hello")).thenReturn("Echo: Hello");
        MockFramework.stopMocking();

        assertEquals("Echo: Hello", service.echo("Hello"));
        assertNull(service.echo("World"));
    }

    @Test
    void testServiceGetValueDefault() {
        assertEquals(0, service.getValue());
    }

    @Test
    void testServiceSum() {
        MockFramework.startMocking();
        when(service.sum(2, 3)).thenReturn(10);
        MockFramework.stopMocking();

        assertEquals(10, service.sum(2, 3));
        assertEquals(0, service.sum(4, 5));
    }

    @Test
    void testProcessorProcess() {
        MockFramework.startMocking();
        when(processor.process("input")).thenReturn("Mocked Process");
        MockFramework.stopMocking();

        assertEquals("Mocked Process", processor.process("input"));
        assertNull(processor.process("other"));
    }

    @Test
    void testProcessorMultiply() {
        MockFramework.startMocking();
        when(processor.multiply(3, 5)).thenReturn(20);
        MockFramework.stopMocking();

        assertEquals(20, processor.multiply(3, 5));
        assertEquals(0, processor.multiply(4, 4));
    }

    @Test
    void testMultipleStubsForSameMethod() {
        MockFramework.startMocking();
        when(service.sum(1, 2)).thenReturn(100);
        when(service.sum(1, 2)).thenReturn(200);
        MockFramework.stopMocking();

        assertEquals(200, service.sum(1, 2));
    }

    @Test
    void testThenThrowInService() {
        MockFramework.startMocking();
        when(service.echo("fail")).thenThrow(new RuntimeException("Failure"));
        MockFramework.stopMocking();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.echo("fail"));
        assertEquals("Failure", exception.getMessage());
    }

    @Test
    void testSequentialCalls() {
        MockFramework.startMocking();
        when(service.getData()).thenReturn("First Call");
        MockFramework.stopMocking();

        assertEquals("First Call", service.getData());
    }
}
