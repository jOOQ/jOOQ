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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.jooq.Configuration;

import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;

/**
 * An SPI that can be used to provide <code>jakarta.persistence</code> style
 * annotations to the {@link DefaultRecordMapper}.
 * <p>
 * Starting with jOOQ 3.20 and <a href=
 * "https://github.com/jOOQ/jOOQ/issues/16500">https://github.com/jOOQ/jOOQ/issues/16500</a>,
 * the {@link jakarta.persistence.Column} (and similar) annotation support has
 * been moved out of the core library to the <code>jOOQ-jpa-extensions</code>
 * module, to allow for:
 * <p>
 * <ul>
 * <li>Removal of the dependency on the <code>jakarta.persistence</code> module
 * from the core library</li>
 * <li>Maintenance of backwards compatibility for those users using the
 * annotation</li>
 * </ul>
 * <p>
 * Implementations can be provided to
 * {@link Configuration#annotatedPojoMemberProvider()}.
 * <p>
 * This SPI is internal and should not be implemented, as it might change
 * incompatibly in the future. Instead, use the default implementation provided
 * by the <code>jOOQ-jpa-extensions</code> module.
 *
 * @author Lukas Eder
 */
@Internal
public interface AnnotatedPojoMemberProvider {

    /**
     * Check whether a type has any annotated members.
     *
     * @param type The POJO type.
     * @return Whether it has annotated members.
     */
    boolean hasAnnotations(Class<?> type);

    /**
     * Get a POJO's annotated members for a given column name.
     * <p>
     * If multiple members match for a given type and column name, then:
     * <ul>
     * <li>The {@link DefaultRecordMapper} will project the column value onto
     * all members and all {@link #getSetters(Class, String)}.</li>
     * <li>The {@link DefaultRecordUnmapper} will read the column value from the
     * first member, giving preference to
     * {@link #getGetters(Class, String)}.</li>
     * </ul>
     *
     * @param type The POJO type.
     * @param name The column name.
     * @return The list of members that match.
     */
    @NotNull
    List<Field> getMembers(Class<?> type, String name);

    /**
     * Get a POJO's annotated getters for a given column name.
     * <p>
     * If multiple getters match for a given type and column name, then the
     * {@link DefaultRecordUnmapper} will read the column value from the first
     * getter.
     *
     * @param type The POJO type.
     * @param name The column name.
     * @return The list of getters that match.
     */
    @NotNull
    List<Method> getGetters(Class<?> type, String name);

    /**
     * Get a POJO's annotated setters for a given column name.
     * <p>
     * If multiple setters match for a given type and column name, then the
     * {@link DefaultRecordMapper} will write the column value to all the
     * setters.
     *
     * @param type The POJO type.
     * @param name The column name.
     * @return The list of setters that match.
     */
    @NotNull
    List<Method> getSetters(Class<?> type, String name);
}
