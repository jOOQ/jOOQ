/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: https://www.jooq.org/legal/licensing
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
package org.jooq.impl;

import static org.jooq.impl.Tools.anyMatch;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import org.jooq.exception.ConfigurationException;

/**
 * Class maintaining backwards compatible behaviour, without formal declaration
 * of <code>jakarta.persistence</code> dependency in module info.
 *
 * @author Lukas Eder
 */
final class LegacyAnnotatedPojoMemberProvider implements AnnotatedPojoMemberProvider {

    @Override
    public List<Field> getMembers(Class<?> type, String name) {
        return Collections.emptyList();
    }

    @Override
    public List<Method> getGetters(Class<?> type, String name) {
        return Collections.emptyList();
    }

    @Override
    public List<Method> getSetters(Class<?> type, String name) {
        return Collections.emptyList();
    }

    @Override
    public boolean hasAnnotations(Class<?> type) {
        if (hasPersistenceAnnotations().test(type)
            || anyMatch(type.getMethods(), hasPersistenceAnnotations())
            || anyMatch(type.getDeclaredMethods(), hasPersistenceAnnotations())
            || anyMatch(type.getFields(), hasPersistenceAnnotations())
            || anyMatch(type.getDeclaredFields(), hasPersistenceAnnotations())
        )
            throw new ConfigurationException("""
                No explicit AnnotatedPojoMemberProvider is configured.

                Jakarta Persistence annotations are present on POJO {pojo}
                without any explicit AnnotatedPojoMemberProvider configuration.

                Starting from jOOQ 3.20, the Configuration.annotatedPojoMemberProvider() SPI is required for the
                DefaultRecordMapper to map a Record to a POJO that is annotated with Jakarta Persistence annotations.
                This LegacyAnnotatedPojoMemberProvider detects potential regressions in the mapping logic
                place using reflection. This implementation is due for removal in a future version of jOOQ.

                If you wish to continue working with the Jakarta Persistence annotations, use the
                jOOQ-jpa-extensions module and its DefaultAnnotatedPojoMemberProvider implementation
                """.replace("{pojo}", type.getName()));

        return false;
    }

    private static final ThrowingPredicate<? super AnnotatedElement, RuntimeException> hasPersistenceAnnotations() {
        return a -> anyMatch(a.getAnnotations(), isPersistenceAnnotation());
    }

    private static final ThrowingPredicate<? super Annotation, RuntimeException> isPersistenceAnnotation() {
        return a -> {
            String name = a.annotationType().getName();

            return name.startsWith("javax.persistence.")
                || name.startsWith("jakarta.persistence.");
        };
    }
}
