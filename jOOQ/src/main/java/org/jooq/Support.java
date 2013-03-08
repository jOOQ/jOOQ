/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
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
