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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import org.jooq.ConstructorPropertiesProvider;
import org.jooq.tools.JooqLogger;

/**
 * Class maintaining backwards compatible behaviour, without formal declaration
 * of <code>jakarta.persistence</code> dependency in module info.
 *
 * @author Lukas Eder
 */
final class LegacyAnnotatedPojoMemberProvider implements AnnotatedPojoMemberProvider {

    static final JooqLogger log = JooqLogger.getLogger(AnnotatedPojoMemberProvider.class, 1);

    // [#16500] TODO: Implement these, or throw an exception

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
        return false;
    }
}
