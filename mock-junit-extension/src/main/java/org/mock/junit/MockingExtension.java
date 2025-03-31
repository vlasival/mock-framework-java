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
        Field[] fields = testInstance.getClass().getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(Mock.class)) {
                field.setAccessible(true);
                Class<?> fieldType = field.getType();
                field.set(testInstance, MockFramework.mock(fieldType));
            }
        }
    }
}
