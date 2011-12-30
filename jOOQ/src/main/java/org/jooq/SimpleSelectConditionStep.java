/**
 * Copyright (c) 2009-2011, Lukas Eder, lukas.eder@gmail.com
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

import org.jooq.impl.Factory;

/**
 * This type is used for the {@link Select}'s DSL API when selecting specific
 * {@link Record} types.
 * <p>
 * Example: <code><pre>
 * create.selectFrom(T_AUTHOR)
 *       .where(TBook.LANGUAGE.equal("DE"))
 *       .and(TBook.PUBLISHED.greaterThan(parseDate('2008-01-01')))
 *       .orderBy(TAuthor.LAST_NAME.asc().nullsFirst())
 *       .limit(2)
 *       .offset(1)
 *       .forUpdate()
 *       .of(TAuthor.FIRST_NAME, TAuthor.LAST_NAME)
 *       .noWait();
 * </pre></code> Refer to the manual for more details
 *
 * @param <R> The record type being returned by this query
 * @author Lukas Eder
 */
public interface SimpleSelectConditionStep<R extends Record> extends SimpleSelectOrderByStep<R> {

    /**
     * Combine the currently assembled conditions with another one using the
     * {@link Operator#AND} operator and proceed to the next step.
     */
    @Support
    SimpleSelectConditionStep<R> and(Condition condition);

    /**
     * Combine the currently assembled conditions with another one using the
     * {@link Operator#AND} operator and proceed to the next step.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see Factory#condition(String)
     */
    @Support
    SimpleSelectConditionStep<R> and(String sql);

    /**
     * Combine the currently assembled conditions with another one using the
     * {@link Operator#AND} operator and proceed to the next step.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see Factory#condition(String, Object...)
     */
    @Support
    SimpleSelectConditionStep<R> and(String sql, Object... bindings);

    /**
     * Combine the currently assembled conditions with a negated other one using
     * the {@link Operator#AND} operator and proceed to the next step.
     */
    @Support
    SimpleSelectConditionStep<R> andNot(Condition condition);

    /**
     * Combine the currently assembled conditions with an EXISTS clause using
     * the {@link Operator#AND} operator and proceed to the next step.
     */
    @Support
    SimpleSelectConditionStep<R> andExists(Select<?> select);

    /**
     * Combine the currently assembled conditions with a NOT EXISTS clause using
     * the {@link Operator#AND} operator and proceed to the next step.
     */
    @Support
    SimpleSelectConditionStep<R> andNotExists(Select<?> select);

    /**
     * Combine the currently assembled conditions with another one using the
     * {@link Operator#OR} operator and proceed to the next step.
     */
    @Support
    SimpleSelectConditionStep<R> or(Condition condition);

    /**
     * Combine the currently assembled conditions with another one using the
     * {@link Operator#OR} operator and proceed to the next step.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see Factory#condition(String)
     */
    @Support
    SimpleSelectConditionStep<R> or(String sql);

    /**
     * Combine the currently assembled conditions with another one using the
     * {@link Operator#OR} operator and proceed to the next step.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see Factory#condition(String, Object...)
     */
    @Support
    SimpleSelectConditionStep<R> or(String sql, Object... bindings);

    /**
     * Combine the currently assembled conditions with a negated other one using
     * the {@link Operator#OR} operator and proceed to the next step.
     */
    @Support
    SimpleSelectConditionStep<R> orNot(Condition condition);

    /**
     * Combine the currently assembled conditions with an EXISTS clause using
     * the {@link Operator#OR} operator and proceed to the next step.
     */
    @Support
    SimpleSelectConditionStep<R> orExists(Select<?> select);

    /**
     * Combine the currently assembled conditions with a NOT EXISTS clause using
     * the {@link Operator#OR} operator and proceed to the next step.
     */
    @Support
    SimpleSelectConditionStep<R> orNotExists(Select<?> select);
}
