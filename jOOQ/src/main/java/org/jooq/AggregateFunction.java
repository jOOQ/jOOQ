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

import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.SQLDialect.DB2;
import static org.jooq.SQLDialect.ORACLE;
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.SQLSERVER;
import static org.jooq.SQLDialect.SYBASE;

import java.util.Collection;

import org.jooq.api.annotation.State;
import org.jooq.api.annotation.Transition;

/**
 * An aggregate function is a special field that is usually used in a
 * <code>GROUP BY</code> context. It is also the base for window function
 * construction.
 *
 * @author Lukas Eder
 */
@State(
    aliases = {
        "StatisticalFunction",
        "OrderedAggregateFunction",
        "LinearRegressionFunction"
    },
    terminal = true
)
public interface AggregateFunction<T> extends Field<T>, WindowOverStep<T> {

    /**
     * Turn this aggregate function into a window function.
     * <p>
     * An example: <code><pre>
     * MAX(ID) OVER (PARTITION BY 1)
     * </code></pre>
     * <p>
     * Window functions are supported in CUBRID, DB2, Postgres, Oracle, SQL
     * Server and Sybase.
     */
    @Override
    @Support({ CUBRID, DB2, POSTGRES, ORACLE, SQLSERVER, SYBASE })
    @Transition(
        name = "OVER"
    )
    WindowPartitionByStep<T> over();

    /**
     * Restrict this aggregate function to <code>FIRST</code> values
     * <p>
     * An example: <code><pre>
     * MAX(ID) KEEP (DENSE_RANK FIRST ORDER BY 1)
     * </pre></code>
     * <p>
     * This clause is only available on
     * <code>MIN, MAX, SUM, AVG, COUNT, VARIANCE, or STDDEV</code> functions.
     */
    @Support(ORACLE)
    @Transition(
        name = "KEEP DENSE_RANK FIRST ORDER BY",
        args = "Field+"
    )
    WindowBeforeOverStep<T> keepDenseRankFirstOrderBy(Field<?>... fields);

    /**
     * Restrict this aggregate function to <code>FIRST</code> values
     * <p>
     * An example: <code><pre>
     * MAX(ID) KEEP (DENSE_RANK FIRST ORDER BY 1)
     * </pre></code>
     * <p>
     * This clause is only available on
     * <code>MIN, MAX, SUM, AVG, COUNT, VARIANCE, or STDDEV</code> functions.
     */
    @Support(ORACLE)
    @Transition(
        name = "KEEP DENSE_RANK FIRST ORDER BY",
        args = "SortField+"
    )
    WindowBeforeOverStep<T> keepDenseRankFirstOrderBy(SortField<?>... fields);

    /**
     * Restrict this aggregate function to <code>FIRST</code> values
     * <p>
     * An example: <code><pre>
     * MAX(ID) KEEP (DENSE_RANK FIRST ORDER BY 1)
     * </pre></code>
     * <p>
     * This clause is only available on
     * <code>MIN, MAX, SUM, AVG, COUNT, VARIANCE, or STDDEV</code> functions.
     */
    @Support(ORACLE)
    @Transition(
        name = "KEEP DENSE_RANK FIRST ORDER BY",
        args = "SortField+"
    )
    WindowBeforeOverStep<T> keepDenseRankFirstOrderBy(Collection<SortField<?>> fields);

    /**
     * Restrict this aggregate function to <code>FIRST</code> values
     * <p>
     * An example: <code><pre>
     * MAX(ID) KEEP (DENSE_RANK LAST ORDER BY 1)
     * </pre></code>
     * <p>
     * This clause is only available on
     * <code>MIN, MAX, SUM, AVG, COUNT, VARIANCE, or STDDEV</code> functions.
     */
    @Support(ORACLE)
    @Transition(
        name = "KEEP DENSE_RANK LAST ORDER BY",
        args = "Field+"
    )
    WindowBeforeOverStep<T> keepDenseRankLastOrderBy(Field<?>... fields);

    /**
     * Restrict this aggregate function to <code>FIRST</code> values
     * <p>
     * An example: <code><pre>
     * MAX(ID) KEEP (DENSE_RANK LAST ORDER BY 1)
     * </pre></code>
     * <p>
     * This clause is only available on
     * <code>MIN, MAX, SUM, AVG, COUNT, VARIANCE, or STDDEV</code> functions.
     */
    @Support(ORACLE)
    @Transition(
        name = "KEEP DENSE_RANK LAST ORDER BY",
        args = "SortField+"
    )
    WindowBeforeOverStep<T> keepDenseRankLastOrderBy(SortField<?>... fields);

    /**
     * Restrict this aggregate function to <code>FIRST</code> values
     * <p>
     * An example: <code><pre>
     * MAX(ID) KEEP (DENSE_RANK LAST ORDER BY 1)
     * </pre></code>
     * <p>
     * This clause is only available on
     * <code>MIN, MAX, SUM, AVG, COUNT, VARIANCE, or STDDEV</code> functions.
     */
    @Support(ORACLE)
    @Transition(
        name = "KEEP DENSE_RANK LAST ORDER BY",
        args = "SortField+"
    )
    WindowBeforeOverStep<T> keepDenseRankLastOrderBy(Collection<SortField<?>> fields);
}
