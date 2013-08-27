/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 * 
 * This work is dual-licensed Open Source, under LGPL and jOOQ EULA
 * =============================================================================
 * You may freely choose which license applies to you. For more information 
 * about licensing, please visit http://www.jooq.org/licenses
 * 
 * LGPL:  
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 * 
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 * 
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details.
 * 
 * You should have received a copy of the jOOQ End User License Agreement
 * along with this library.
 * If not, see http://www.jooq.org/eula
 */
package org.jooq;

import java.util.List;

import org.jooq.api.annotation.State;
import org.jooq.exception.DataAccessException;

/**
 * A {@link Query} that can provide a {@link Result} after execution
 *
 * @param <R> The record type being returned by this query
 * @author Lukas Eder
 */
@State(
    terminal = true
)
public interface Select<R extends Record> extends ResultQuery<R>, TableLike<R>, FieldLike {

    /**
     * Combine with other selects
     */
    @Support
    Select<R> union(Select<? extends R> select);

    /**
     * Combine with other selects
     */
    @Support
    Select<R> unionAll(Select<? extends R> select);

    /**
     * Combine with other selects
     */
    @Support
    Select<R> except(Select<? extends R> select);

    /**
     * Combine with other selects
     */
    @Support
    Select<R> intersect(Select<? extends R> select);

    /**
     * All fields selected in this query
     */
    List<Field<?>> getSelect();

    /**
     * Execute this query in the context of its attached executor and return
     * a <code>COUNT(*)</code> value.
     * <p>
     * This wraps a pre-existing <code>SELECT</code> query in another one to
     * calculate the <code>COUNT(*)</code> value, without modifying the original
     * <code>SELECT</code>. An example: <code><pre>
     * -- Original query:
     * SELECT id, title FROM book WHERE title LIKE '%a%'
     *
     * -- Wrapped query:
     * SELECT count(*) FROM (
     *   SELECT id, title FROM book WHERE title LIKE '%a%'
     * )
     * </pre></code> This is particularly useful for those databases that do not
     * support the <code>COUNT(*) OVER()</code> window function to calculate
     * total results in paged queries.
     *
     * @return The <code>COUNT(*)</code> result
     * @throws DataAccessException if something went wrong executing the query
     */
    int fetchCount() throws DataAccessException;
}
