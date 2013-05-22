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
