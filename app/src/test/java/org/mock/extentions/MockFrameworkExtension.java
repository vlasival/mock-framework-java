package org.mock.extentions;

import org.junit.jupiter.api.extension.*;
import org.mock.annotation.Mock;
import org.mock.core.MockFramework;

import java.lang.reflect.Field;

/**
 * Интеграция с JUnit 5 для автоматического создания моков через аннотации.
 */
public class MockFrameworkExtension implements BeforeEachCallback, ParameterResolver {
    @Override
    public void beforeEach(ExtensionContext context) {
        Object testInstance = context.getRequiredTestInstance();
        for (Field field : testInstance.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Mock.class)) {
                field.setAccessible(true);
                try {
                    field.set(testInstance, MockFramework.mock(field.getType()));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return parameterContext.getParameter().isAnnotationPresent(Mock.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return MockFramework.mock(parameterContext.getParameter().getType());
    }
}