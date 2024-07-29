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
package org.jooq;

import java.lang.reflect.Constructor;

import org.jooq.impl.DefaultRecordMapper;

import org.jetbrains.annotations.Nullable;

/**
 * An SPI that can be used to provide <code>java.beans</code> style constructor
 * properties to the {@link DefaultRecordMapper}.
 * <p>
 * Starting with jOOQ 3.20 and <a href=
 * "https://github.com/jOOQ/jOOQ/issues/7585">https://github.com/jOOQ/jOOQ/issues/7585</a>,
 * the {@link java.beans.ConstructorProperties} annotation support has been
 * moved out of the core library to the <code>jOOQ-beans-extensions</code>
 * module, to allow for:
 * <p>
 * <ul>
 * <li>Removal of the dependency on the <code>java.beans</code> JDK module from
 * the core library</li>
 * <li>Maintenance of backwards compatibility for those users using the
 * annotation</li>
 * </ul>
 * <p>
 * Implementations can be provided to
 * {@link Configuration#constructorPropertiesProvider()}.
 *
 * @author Lukas Eder
 */
@FunctionalInterface
public interface ConstructorPropertiesProvider {

    /**
     * Provide the constructor properties on the argument constructor, like
     * {@link java.beans.ConstructorProperties#value()}.
     * <p>
     *
     * @return The constructor properties, if applicable, or <code>null</code>
     *         if the argument {@link Constructor} does not expose any
     *         constructor properties.
     */
    @Nullable
    public String @Nullable [] properties(Constructor<?> constructor);
}
