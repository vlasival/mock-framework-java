package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mock.core.MockingFramework;
import org.mock.junit.MockingExtension;
import org.mock.junit.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mock.core.MockingFramework.when;

@ExtendWith(MockingExtension.class)
class ApplicationTest {

    @Mock
    private SomeService service;

    @Test
    void testMocking() {
        when(service.someMethod()).thenReturn("Mocked Response");

        assertEquals("Mocked Response", service.someMethod());
    }

    interface SomeService {
        String someMethod();
    }
}
