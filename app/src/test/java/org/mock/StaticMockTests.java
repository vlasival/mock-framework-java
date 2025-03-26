package org.mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mock.core.MockBehavior;
import org.mock.core.MockFramework;

public class StaticMockTests {

    @BeforeEach
    void resetMocks() {
        MockFramework.resetMocks();
    }

    @Test
    void testStaticMethodMocking() {
        // Мокаем статический метод
        MockFramework.mockStatic(StringUtils.class);

        // Настраиваем поведение
        MockBehavior.when(() -> StringUtils.reverse("hello"))
                .thenReturn("mocked_response");

        // Проверяем
        assertEquals("mocked_response", StringUtils.reverse("hello"));
        assertNull(StringUtils.reverse("unmocked")); // Не настроено → null
    }

    @Test
    void testStaticMethodReturnValue() {
        MockFramework.mockStatic(StringUtils.class);

        MockBehavior.when(() -> StringUtils.multiply(2, 3))
                .thenReturn(100);

        assertEquals(100, StringUtils.multiply(2, 3));
        assertEquals(0, StringUtils.multiply(5, 5)); // Значение по умолчанию для int
    }

    @Test
    void testStaticMethodException() {
        MockFramework.mockStatic(StringUtils.class);

        MockBehavior.when(() -> StringUtils.reverse("error"))
                .thenThrow(new RuntimeException("Test exception"));

        assertThrows(RuntimeException.class, () -> {
            StringUtils.reverse("error");
        });
    }

    @Test
    void testOriginalBehaviorAfterReset() {
        MockFramework.mockStatic(StringUtils.class);
        MockBehavior.when(() -> StringUtils.reverse("hello"))
                .thenReturn("mocked");

        // Сбрасываем моки
        MockFramework.resetMocks();

        // Проверяем оригинальное поведение
        assertEquals("olleh", StringUtils.reverse("hello"));
    }
}