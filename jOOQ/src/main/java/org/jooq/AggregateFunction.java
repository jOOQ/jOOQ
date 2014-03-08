/**
 * Copyright (c) 2009-2014, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
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
     * MAX(id) OVER (PARTITION BY 1)
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
     * Turn this aggregate function into a window function referencing a window
     * name.
     * <p>
     * An example: <code><pre>
     * MAX(id) OVER my_window
     * </code></pre>
     * <p>
     * Window functions are supported in CUBRID, DB2, Postgres, Oracle, SQL
     * Server and Sybase. If the <code>WINDOW</code> clause is not supported
     * (see {@link SelectWindowStep#window(WindowDefinition...)}, then
     * referenced windows will be inlined.
     */
    @Override
    @Support({ CUBRID, DB2, POSTGRES, ORACLE, SQLSERVER, SYBASE })
    @Transition(
        name = "OVER"
    )
    WindowFinalStep<T> over(Name name);

    /**
     * Turn this aggregate function into a window function referencing a window
     * name.
     * <p>
     * An example: <code><pre>
     * MAX(id) OVER my_window
     * </code></pre>
     * <p>
     * Window functions are supported in CUBRID, DB2, Postgres, Oracle, SQL
     * Server and Sybase. If the <code>WINDOW</code> clause is not supported
     * (see {@link SelectWindowStep#window(WindowDefinition...)}, then
     * referenced windows will be inlined.
     */
    @Override
    @Support({ CUBRID, DB2, POSTGRES, ORACLE, SQLSERVER, SYBASE })
    @Transition(
        name = "OVER"
    )
    WindowFinalStep<T> over(String name);

    /**
     * Turn this aggregate function into a window function.
     * <p>
     * An example: <code><pre>
     * MAX(id) OVER (PARTITION BY 1)
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
    WindowFinalStep<T> over(WindowSpecification specification);

    /**
     * Turn this aggregate function into a window function referencing a window
     * definition.
     * <p>
     * An example: <code><pre>
     * MAX(id) OVER my_window
     * </code></pre>
     * <p>
     * Window functions are supported in CUBRID, DB2, Postgres, Oracle, SQL
     * Server and Sybase. If the <code>WINDOW</code> clause is not supported
     * (see {@link SelectWindowStep#window(WindowDefinition...)}, then
     * referenced windows will be inlined.
     */
    @Override
    @Support({ CUBRID, DB2, POSTGRES, ORACLE, SQLSERVER, SYBASE })
    @Transition(
        name = "OVER"
    )
    WindowFinalStep<T> over(WindowDefinition definition);

    /* [pro] */
    /**
     * Restrict this aggregate function to <code>FIRST</code> values
     * <p>
     * An example: <code><pre>
     * MAX(id) KEEP (DENSE_RANK FIRST ORDER BY 1)
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
     * MAX(id) KEEP (DENSE_RANK FIRST ORDER BY 1)
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
     * MAX(id) KEEP (DENSE_RANK FIRST ORDER BY 1)
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
    WindowBeforeOverStep<T> keepDenseRankFirstOrderBy(Collection<? extends SortField<?>> fields);

    /**
     * Restrict this aggregate function to <code>FIRST</code> values
     * <p>
     * An example: <code><pre>
     * MAX(id) KEEP (DENSE_RANK LAST ORDER BY 1)
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
     * MAX(id) KEEP (DENSE_RANK LAST ORDER BY 1)
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
     * MAX(id) KEEP (DENSE_RANK LAST ORDER BY 1)
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
    WindowBeforeOverStep<T> keepDenseRankLastOrderBy(Collection<? extends SortField<?>> fields);
    /* [/pro] */
}
