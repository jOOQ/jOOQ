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

import static org.jooq.SQLDialect.ORACLE;

import java.util.Collection;

import org.jooq.api.annotation.State;
import org.jooq.api.annotation.Transition;

/**
 * An ordered aggregate function.
 * <p>
 * An ordered aggregate function is an aggregate function with a mandatory
 * Oracle-specific <code>WITHIN GROUP (ORDER BY ..)</code> clause. An example is
 * <code>LISTAGG</code>: <code><pre>
 * SELECT   LISTAGG(TITLE, ', ')
 *          WITHIN GROUP (ORDER BY TITLE)
 * FROM     T_BOOK
 * GROUP BY AUTHOR_ID
 * </pre></code> The above function groups books by author and aggregates titles
 * into a concatenated string.
 * <p>
 * Ordered aggregate functions can be further converted into window functions
 * using the <code>OVER(PARTITION BY ..)</code> clause. For example: <code><pre>
 * SELECT LISTAGG(TITLE, ', ')
 *        WITHIN GROUP (ORDER BY TITLE)
 *        OVER (PARTITION BY AUTHOR_ID)
 * FROM   T_BOOK
 * </pre></code>
 *
 * @author Lukas Eder
 */
@State
public interface OrderedAggregateFunction<T> {

    /**
     * Add an <code>WITHIN GROUP (ORDER BY ..)</code> clause to the ordered
     * aggregate function
     */
    @Support(ORACLE)
    @Transition(
        name = "WITHIN GROUP ORDER BY",
        args = "Field+",
        to = "OrderedAggregateFunction"
    )
    AggregateFunction<T> withinGroupOrderBy(Field<?>... fields);

    /**
     * Add an <code>WITHIN GROUP (ORDER BY ..)</code> clause to the ordered
     * aggregate function
     */
    @Support(ORACLE)
    @Transition(
        name = "WITHIN GROUP ORDER BY",
        args = "SortField+",
        to = "OrderedAggregateFunction"
    )
    AggregateFunction<T> withinGroupOrderBy(SortField<?>... fields);

    /**
     * Add an <code>WITHIN GROUP (ORDER BY ..)</code> clause to the ordered
     * aggregate function
     */
    @Support(ORACLE)
    @Transition(
        name = "WITHIN GROUP ORDER BY",
        args = "SortField+",
        to = "OrderedAggregateFunction"
    )
    AggregateFunction<T> withinGroupOrderBy(Collection<SortField<?>> fields);
}
