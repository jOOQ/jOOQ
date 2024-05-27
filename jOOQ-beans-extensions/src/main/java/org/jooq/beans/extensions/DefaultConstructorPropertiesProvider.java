package org.jooq.beans.extensions;

import java.beans.ConstructorProperties;
import java.lang.reflect.Constructor;

import org.jooq.ConstructorPropertiesProvider;

/**
 * The default {@link ConstructorPropertiesProvider} implementation that looks
 * up constructor properties from {@link ConstructorProperties}.
 *
 * @author Lukas Eder
 */
public class DefaultConstructorPropertiesProvider implements ConstructorPropertiesProvider {

    @Override
    public String[] properties(Constructor<?> constructor) {
        ConstructorProperties cp = constructor.getAnnotation(ConstructorProperties.class);

        return cp != null ? cp.value() : null;
    }
}
