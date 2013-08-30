/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is triple-licensed under ASL 2.0, AGPL 3.0, and jOOQ EULA
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   ASL 2.0 or jOOQ EULA.
 * - If you're using this work with at least one commercial database, you may
 *   choose AGPL 3.0 or jOOQ EULA.
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * AGPL 3.0
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 *
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details: http://www.jooq.org/eula
 */
package org.jooq;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
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
 * <code>MERGE</code>, etc.</li>
 * </ul>
 * <p>
 * Future versions of jOOQ may use these annotations for throwing
 * {@link SQLDialectNotSupportedException} where appropriate, or preprocessing
 * jOOQ source code in order to mark unsupported API as {@link Deprecated}
 *
 * @author Lukas Eder
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RUNTIME)
@Documented
public @interface Support {

    /**
     * A {@link SQLDialect} array containing all dialects that are supported by
     * the API method annotated with this annotation.
     */
    SQLDialect[] value() default {};
}
