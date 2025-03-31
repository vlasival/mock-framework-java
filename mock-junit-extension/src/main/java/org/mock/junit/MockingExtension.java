package org.mock.junit;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mock.annotation.Mock;
import org.mock.core.MockFramework;

import java.lang.reflect.Field;

public class MockingExtension implements BeforeEachCallback {
    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Object testInstance = context.getRequiredTestInstance();
        for (Field field : testInstance.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Mock.class)) {
                field.setAccessible(true);
                Object mockInstance = MockingFramework.mock(field.getType());
                field.set(testInstance, mockInstance);
            }
        }
    }
}
