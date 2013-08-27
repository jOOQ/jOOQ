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

import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.SQLDialect.ORACLE;

import java.util.Collection;

import org.jooq.api.annotation.State;
import org.jooq.api.annotation.Transition;

/**
 * This type is used for the {@link Select}'s DSL API when selecting generic
 * {@link Record} types.
 * <p>
 * Example: <code><pre>
 * -- get all authors' first and last names, and the number
 * -- of books they've written in German, if they have written
 * -- more than five books in German in the last three years
 * -- (from 2011), and sort those authors by last names
 * -- limiting results to the second and third row
 *
 *   SELECT T_AUTHOR.FIRST_NAME, T_AUTHOR.LAST_NAME, COUNT(*)
 *     FROM T_AUTHOR
 *     JOIN T_BOOK ON T_AUTHOR.ID = T_BOOK.AUTHOR_ID
 *    WHERE T_BOOK.LANGUAGE = 'DE'
 *      AND T_BOOK.PUBLISHED > '2008-01-01'
 * GROUP BY T_AUTHOR.FIRST_NAME, T_AUTHOR.LAST_NAME
 *   HAVING COUNT(*) > 5
 * ORDER BY T_AUTHOR.LAST_NAME ASC NULLS FIRST
 *    LIMIT 2
 *   OFFSET 1
 *      FOR UPDATE
 *       OF FIRST_NAME, LAST_NAME
 *       NO WAIT
 * </pre></code> Its equivalent in jOOQ <code><pre>
 * create.select(TAuthor.FIRST_NAME, TAuthor.LAST_NAME, create.count())
 *       .from(T_AUTHOR)
 *       .join(T_BOOK).on(TBook.AUTHOR_ID.equal(TAuthor.ID))
 *       .where(TBook.LANGUAGE.equal("DE"))
 *       .and(TBook.PUBLISHED.greaterThan(parseDate('2008-01-01')))
 *       .groupBy(TAuthor.FIRST_NAME, TAuthor.LAST_NAME)
 *       .having(create.count().greaterThan(5))
 *       .orderBy(TAuthor.LAST_NAME.asc().nullsFirst())
 *       .limit(2)
 *       .offset(1)
 *       .forUpdate()
 *       .of(TAuthor.FIRST_NAME, TAuthor.LAST_NAME)
 *       .noWait();
 * </pre></code> Refer to the manual for more details
 *
 * @author Lukas Eder
 */
@State
public interface SelectOrderByStep<R extends Record> extends SelectLimitStep<R> {

    /**
     * Add an <code>ORDER BY</code> clause to the query
     */
    @Support
    @Transition(
        name = "ORDER BY",
        args = "Field+"
    )
    SelectLimitStep<R> orderBy(Field<?>... fields);

    /**
     * Add an <code>ORDER BY</code> clause to the query
     */
    @Support
    @Transition(
        name = "ORDER BY",
        args = "SortField+"
    )
    SelectLimitStep<R> orderBy(SortField<?>... fields);

    /**
     * Add an <code>ORDER BY</code> clause to the query
     */
    @Support
    @Transition(
        name = "ORDER BY",
        args = "SortField+"
    )
    SelectLimitStep<R> orderBy(Collection<SortField<?>> fields);

    /**
     * Add an <code>ORDER BY</code> clause to the query
     * <p>
     * Indexes start at <code>1</code> in SQL!
     * <p>
     * Note, you can use <code>orderBy(DSL.val(1).desc())</code> or
     * <code>orderBy(DSL.literal(1).desc())</code> to apply descending
     * ordering
     */
    @Support
    @Transition(
        name = "ORDER BY",
        args = "Integer+"
    )
    SelectLimitStep<R> orderBy(int... fieldIndexes);

    /**
     * Add an <code>ORDER SIBLINGS BY</code> clause to the query
     * <p>
     * This clause can be used only along with Oracle's <code>CONNECT BY</code>
     * clause, to indicate that the hierarchical ordering should be preserved
     * and elements of each hierarchy should be ordered among themselves.
     */
    @Support({ CUBRID, ORACLE })
    @Transition(
        name = "ORDER SIBLINGS BY",
        args = "Field+"
    )
    SelectLimitStep<R> orderSiblingsBy(Field<?>... fields);

    /**
     * Add an <code>ORDER SIBLINGS BY</code> clause to the query
     * <p>
     * This clause can be used only along with Oracle's <code>CONNECT BY</code>
     * clause, to indicate that the hierarchical ordering should be preserved
     * and elements of each hierarchy should be ordered among themselves.
     */
    @Support({ CUBRID, ORACLE })
    @Transition(
        name = "ORDER SIBLINGS BY",
        args = "SortField+"
    )
    SelectLimitStep<R> orderSiblingsBy(SortField<?>... fields);

    /**
     * Add an <code>ORDER SIBLINGS BY</code> clause to the query
     * <p>
     * This clause can be used only along with Oracle's <code>CONNECT BY</code>
     * clause, to indicate that the hierarchical ordering should be preserved
     * and elements of each hierarchy should be ordered among themselves.
     */
    @Support({ CUBRID, ORACLE })
    @Transition(
        name = "ORDER SIBLINGS BY",
        args = "SortField+"
    )
    SelectLimitStep<R> orderSiblingsBy(Collection<SortField<?>> fields);

    /**
     * Add an <code>ORDER SIBLINGS BY</code> clause to the query
     * <p>
     * This clause can be used only along with Oracle's <code>CONNECT BY</code>
     * clause, to indicate that the hierarchical ordering should be preserved
     * and elements of each hierarchy should be ordered among themselves.
     * <p>
     * Indexes start at <code>1</code> in SQL!
     * <p>
     * Note, you can use <code>orderSiblingsBy(DSL.val(1).desc())</code> or
     * <code>orderBy(DSL.literal(1).desc())</code> to apply descending
     * ordering
     */
    @Support({ CUBRID, ORACLE })
    @Transition(
        name = "ORDER SIBLINGS BY",
        args = "Integer+"
    )
    SelectLimitStep<R> orderSiblingsBy(int... fieldIndexes);
}
