/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
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

import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.SQLDialect.ORACLE;

import java.util.Collection;

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
public interface SimpleSelectOrderByStep<R extends Record> extends SimpleSelectLimitStep<R> {

    /**
     * Add an <code>ORDER BY</code> clause to the query
     */
    @Support
    SimpleSelectLimitStep<R> orderBy(Field<?>... fields);

    /**
     * Add an <code>ORDER BY</code> clause to the query
     */
    @Support
    SimpleSelectLimitStep<R> orderBy(SortField<?>... fields);

    /**
     * Add an <code>ORDER BY</code> clause to the query
     */
    @Support
    SimpleSelectLimitStep<R> orderBy(Collection<SortField<?>> fields);

    /**
     * Add an <code>ORDER BY</code> clause to the query
     * <p>
     * Indexes start at <code>1</code> in SQL!
     * <p>
     * Note, you can use <code>orderBy(Factory.val(1).desc())</code> or
     * <code>orderBy(Factory.literal(1).desc())</code> to apply descending
     * ordering
     */
    @Support
    SimpleSelectLimitStep<R> orderBy(int... fieldIndexes);

    /**
     * Add an <code>ORDER SIBLINGS BY</code> clause to the query
     * <p>
     * This clause can be used only along with Oracle's <code>CONNECT BY</code>
     * clause, to indicate that the hierarchical ordering should be preserved
     * and elements of each hierarchy should be ordered among themselves.
     */
    @Support({ CUBRID, ORACLE })
    SimpleSelectLimitStep<R> orderSiblingsBy(Field<?>... fields);

    /**
     * Add an <code>ORDER SIBLINGS BY</code> clause to the query
     * <p>
     * This clause can be used only along with Oracle's <code>CONNECT BY</code>
     * clause, to indicate that the hierarchical ordering should be preserved
     * and elements of each hierarchy should be ordered among themselves.
     */
    @Support({ CUBRID, ORACLE })
    SimpleSelectLimitStep<R> orderSiblingsBy(SortField<?>... fields);

    /**
     * Add an <code>ORDER SIBLINGS BY</code> clause to the query
     * <p>
     * This clause can be used only along with Oracle's <code>CONNECT BY</code>
     * clause, to indicate that the hierarchical ordering should be preserved
     * and elements of each hierarchy should be ordered among themselves.
     */
    @Support({ CUBRID, ORACLE })
    SimpleSelectLimitStep<R> orderSiblingsBy(Collection<SortField<?>> fields);

    /**
     * Add an <code>ORDER SIBLINGS BY</code> clause to the query
     * <p>
     * This clause can be used only along with Oracle's <code>CONNECT BY</code>
     * clause, to indicate that the hierarchical ordering should be preserved
     * and elements of each hierarchy should be ordered among themselves.
     * <p>
     * Indexes start at <code>1</code> in SQL!
     * <p>
     * Note, you can use <code>orderSiblingsBy(Factory.val(1).desc())</code> or
     * <code>orderBy(Factory.literal(1).desc())</code> to apply descending
     * ordering
     */
    @Support({ CUBRID, ORACLE })
    SimpleSelectLimitStep<R> orderSiblingsBy(int... fieldIndexes);
}
