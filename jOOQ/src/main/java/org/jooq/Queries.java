/*
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
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: http://www.jooq.org/licenses
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
package org.jooq;

import java.sql.Statement;
import java.util.stream.Stream;

import org.jooq.exception.DetachedException;

/**
 * A wrapper for a collection of queries.
 *
 * @author Lukas Eder
 */
public interface Queries extends QueryPart, Iterable<Query> {

    // ------------------------------------------------------------------------
    // Access API
    // ------------------------------------------------------------------------

    /**
     * The wrapped collection of queries.
     */
    Query[] queries();


    /**
     * The wrapped collection of queries.
     *
     * @deprecated - 3.10 - [#6143] - Use {@link #queryStream()} instead.
     */
    @Deprecated
    Stream<Query> stream();

    /**
     * The wrapped collection of queries.
     */
    Stream<Query> queryStream();


    // ------------------------------------------------------------------------
    // Execution API
    // ------------------------------------------------------------------------

    /**
     * Execute all queries one-by-one and return all results.
     * <p>
     * This is a convenience method for executing individual {@link #queries()}.
     * <p>
     * If this {@link Queries} reference is attached to a {@link Configuration},
     * then that <code>configuration</code> is used through
     * {@link DSLContext#fetchMany(ResultQuery)} or
     * {@link DSLContext#execute(Query)}. If this <code>queries</code> reference
     * is unattached, then each individual {@link ResultQuery#fetchMany()} or
     * {@link Query#execute()} method is called.
     *
     * @throws DetachedException If this <code>queries</code> reference is
     *             unattached and at least one of the contained
     *             {@link #queries()} is also unattached.
     */
    Results fetchMany();

    /**
     * Sends the entire batch of queries to the server and executes them using a
     * JDBC {@link Statement#executeBatch()} operation.
     * <p>
     * This {@link Queries} reference must be attached to a
     * {@link Configuration}.
     *
     * @throws DetachedException If this <code>queries</code> reference is
     *             unattached.
     * @see DSLContext#batch(Queries)
     */
    int[] executeBatch();
}
