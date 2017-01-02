/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
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
 * For more information, please visit: http://www.jooq.org/licenses
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

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.jooq.exception.SQLDialectNotSupportedException;

/**
 * A formal declaration of whether any API element is supported by a given
 * {@link SQLDialect}
 * <p>
 * The annotation is mainly used in three modes:
 * <ul>
 * <li>The annotation is absent on a method. This means that the applied
 * <code>SQLDialect</code> is irrelevant for that method. This is mostly the
 * case for jOOQ's general API (creating factories, rendering SQL, binding
 * variables, etc.) as well as utility methods.</li>
 * <li>The annotation is present but "empty" on a method, i.e. it specifies no
 * <code>SQLDialect</code>. This means that all of jOOQ's dialects are supported
 * by this API method. This is typically the case with jOOQ's SQL construction
 * API for very common clauses, such as the creation of <code>SELECT</code>,
 * <code>UPDATE</code>, <code>INSERT</code>, <code>DELETE</code>.</li>
 * <li>The annotation is present and specifies one or more dialects. A method
 * annotated in such a way should be used only along with any of the dialects
 * specified by the annotation. This is typically the case with jOOQ's SQL
 * construction API for less common clauses, such as the creation of
 * <code>MERGE</code>, etc.
 * <p>
 * There listed dialects can be either a:
 * <ul>
 * <li>A dialect family, in case of which all versions of the family support the
 * feature. E.g. when {@link SQLDialect#POSTGRES} is referenced, then
 * {@link SQLDialect#POSTGRES_9_3}, {@link SQLDialect#POSTGRES_9_4}, etc.
 * support the feature as well</li>
 * <li>A dialect version, in case of which all versions larger or equal than the
 * referenced version support the feature. E.g. when
 * {@link SQLDialect#POSTGRES_9_4} is referenced, then
 * {@link SQLDialect#POSTGRES_9_5} would support the feature as well, but not
 * {@link SQLDialect#POSTGRES_9_3}</li>
 * </ul>
 * </li>
 * </ul>
 * <p>
 * Future versions of jOOQ may use these annotations for throwing
 * {@link SQLDialectNotSupportedException} where appropriate, or preprocessing
 * jOOQ source code in order to mark unsupported API as {@link Deprecated}
 *
 * @author Lukas Eder
 */
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RUNTIME)
@Documented
@Inherited
public @interface Support {

    /**
     * A {@link SQLDialect} array containing all dialects that are supported by
     * the API method annotated with this annotation.
     */
    SQLDialect[] value() default {};
}
