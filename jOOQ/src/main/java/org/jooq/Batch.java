/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
 *
 * For more information, please visit: https://www.jooq.org/legal/licensing
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
 *
 *
 *
 */
package org.jooq;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;

import org.jooq.exception.DataAccessException;

import org.jetbrains.annotations.Blocking;
import org.jetbrains.annotations.NotNull;

/**
 * A wrapper for a JDBC batch operation. It has two modes:
 * <p>
 * <ol>
 * <li><b>Execute several queries without bind values</b><pre><code>
 * create.batch(query1,
 *              query2,
 *              query3)
 *       .execute();
 * </code></pre></li>
 * <li><b>Execute one query several times with bind values</b><pre><code>
 * create.batch(query)
 *       .bind(valueA1, valueA2)
 *       .bind(valueB1, valueB2)
 *       .execute();
 * </code></pre></li>
 * </ol>
 *
 * @author Lukas Eder
 * @see Statement#executeBatch()
 */
public interface Batch extends Serializable, Publisher<Integer> {

    /**
     * Execute the batch operation.
     *
     * @see Statement#executeBatch()
     * @throws DataAccessException if something went wrong executing the query
     */
    @Blocking
    int @NotNull [] execute() throws DataAccessException;

    /**
     * Execute the batch operation.
     * <p>
     * This is equivalent to {@link #execute()}, except that it can fetch a
     * larger update count if the underlying JDBC driver implements
     * {@link PreparedStatement#executeLargeBatch()}.
     *
     * @see Statement#executeLargeBatch()
     * @throws DataAccessException if something went wrong executing the query
     */
    @Blocking
    long @NotNull [] executeLarge() throws DataAccessException;

    /**
     * Execute the batch operation in a new {@link CompletionStage}.
     * <p>
     * The result is asynchronously completed by a task running in an
     * {@link Executor} provided by the underlying
     * {@link Configuration#executorProvider()}.
     *
     * @see Statement#executeBatch()
     */
    @NotNull
    CompletionStage<int[]> executeAsync();

    /**
     * Execute the query in a new {@link CompletionStage} that is asynchronously
     * completed by a task running in the given executor.
     */
    @NotNull
    CompletionStage<int[]> executeAsync(Executor executor);

    /**
     * Execute the batch operation in a new {@link CompletionStage}.
     * <p>
     * This is equivalent to {@link #executeAsync()}, except that it can fetch a
     * larger update count if the underlying JDBC driver implements
     * {@link PreparedStatement#executeLargeBatch()}.
     * <p>
     * The result is asynchronously completed by a task running in an
     * {@link Executor} provided by the underlying
     * {@link Configuration#executorProvider()}.
     *
     * @see Statement#executeBatch()
     */
    @NotNull
    CompletionStage<long[]> executeLargeAsync();

    /**
     * Execute the query in a new {@link CompletionStage} that is asynchronously
     * completed by a task running in the given executor.
     * <p>
     * This is equivalent to {@link #executeAsync(Executor)}, except that it can
     * fetch a larger update count if the underlying JDBC driver implements
     * {@link PreparedStatement#executeLargeBatch()}.
     */
    @NotNull
    CompletionStage<long[]> executeLargeAsync(Executor executor);

    /**
     * Turn this publisher into a {@link Long} returning publisher to support large update counts.
     */
    @NotNull
    Publisher<Long> largePublisher();

    /**
     * Get the number of executed queries in this batch operation
     */
    int size();
}
