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

import java.sql.PreparedStatement;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;

import org.jooq.conf.StatementType;
import org.jooq.exception.DataAccessException;
import org.jooq.exception.DataTypeException;
import org.jooq.impl.DSL;

import org.jetbrains.annotations.Blocking;
import org.jetbrains.annotations.NotNull;

/**
 * Any query.
 * <p>
 * Instances can be created using {@link DSL#query(String)} and overloads,
 * or by creating a subtype.
 *
 * @author Lukas Eder
 */
public interface Query extends Statement, AttachableQueryPart {

    /**
     * Execute the query, if it has been created with a proper configuration.
     *
     * @return A result value, depending on the concrete implementation of
     *         {@link Query}:
     *         <ul>
     *         <li> {@link Delete} : the number of deleted records</li>
     *         <li> {@link Insert} : the number of inserted records</li>
     *         <li> {@link Merge} : the result may have no meaning</li>
     *         <li> {@link Select} : the number of resulting records</li>
     *         <li> {@link Truncate} : the result may have no meaning</li>
     *         <li> {@link Update} : the number of updated records</li>
     *         </ul>
     * @throws DataAccessException If anything goes wrong in the database
     */
    @Blocking
    int execute() throws DataAccessException;

    /**
     * Execute the query in a new {@link CompletionStage}.
     * <p>
     * The result is asynchronously completed by a task running in an
     * {@link Executor} provided by the underlying
     * {@link Configuration#executorProvider()}.
     *
     * @return A result value, depending on the concrete implementation of
     *         {@link Query}:
     *         <ul>
     *         <li>{@link Delete} : the number of deleted records</li>
     *         <li>{@link Insert} : the number of inserted records</li>
     *         <li>{@link Merge} : the result may have no meaning</li>
     *         <li>{@link Select} : the number of resulting records</li>
     *         <li>{@link Truncate} : the result may have no meaning</li>
     *         <li>{@link Update} : the number of updated records</li>
     *         </ul>
     */
    @NotNull
    CompletionStage<Integer> executeAsync();

    /**
     * Execute the query in a new {@link CompletionStage} that is asynchronously
     * completed by a task running in the given executor.
     *
     * @return A result value, depending on the concrete implementation of
     *         {@link Query}:
     *         <ul>
     *         <li> {@link Delete} : the number of deleted records</li>
     *         <li> {@link Insert} : the number of inserted records</li>
     *         <li> {@link Merge} : the result may have no meaning</li>
     *         <li> {@link Select} : the number of resulting records</li>
     *         <li> {@link Truncate} : the result may have no meaning</li>
     *         <li> {@link Update} : the number of updated records</li>
     *         </ul>
     */
    @NotNull
    CompletionStage<Integer> executeAsync(Executor executor);

    /**
     * Whether this query is executable in its current state.
     * <p>
     * DML queries may be incomplete in structure and thus not executable.
     * Calling {@link #execute()} on such queries has no effect, but beware that
     * {@link #getSQL()} may not render valid SQL!
     */
    boolean isExecutable();

    /**
     * Bind a new value to a named parameter.
     * <p>
     * [#1886] If the bind value with name <code>param</code> is inlined (
     * {@link Param#isInline()}) or if this query was created with
     * {@link StatementType#STATIC_STATEMENT} and there is an underlying
     * <code>PreparedStatement</code> kept open because of
     * {@link #keepStatement(boolean)}, the underlying
     * <code>PreparedStatement</code> will be closed automatically in order for
     * new bind values to have an effect.
     * <h3>Performance consideration</h3>
     * <p>
     * Historically, the bind value is
     * inserted into the mutable {@link Query}, which means that the
     * {@link Query} needs to be traversed in order to find the bind value and
     * change it.
     * <p>
     * As such, it is usually better to supply bind values directly with the
     * input of an expression, e.g.:
     * <ul>
     * <li>Directly with the {@link DSL} method, such as
     * {@link DSL#log(Field, Field)}, for example.</li>
     * <li>With the plain SQL template constructor, e.g.
     * {@link DSL#field(String, Object...)}</li>
     * <li>With the parser method, e.g.
     * {@link Parser#parseField(String, Object...)}</li>
     * </ul>
     *
     * @param param The named parameter name. If this is a number, then this is
     *            the same as calling {@link #bind(int, Object)}
     * @param value The new bind value.
     * @throws IllegalArgumentException if there is no parameter by the given
     *             parameter name or index.
     * @throws DataTypeException if <code>value</code> cannot be converted into
     *             the parameter's data type
     */
    @NotNull
    Query bind(String param, Object value) throws IllegalArgumentException, DataTypeException;

    /**
     * Bind a new value to an indexed parameter.
     * <h3>Bind index order</h3> The 1-based parameter index describes a
     * parameter in <em>rendering order</em>, not in input order. For example,
     * if a query contains a {@link DSL#log(Field, Field)} call, where the first
     * argument is the <code>value</code> and the second argument is the
     * <code>base</code>, this may produce different dialect specific
     * renderings:
     * <ul>
     * <li>Db2: <code>ln(value) / ln(base)</code></li>
     * <li>Oracle: <code>log(base, value)</code></li>
     * <li>SQL Server: <code>log(value, base)</code></li>
     * </ul>
     * <p>
     * Some bind values may even be repeated by a dialect specific emulation,
     * leading to duplication and index-shifting.
     * <h3>Performance consideration</h3>
     * <p>
     * Historically, the bind value is
     * inserted into the mutable {@link Query}, which means that the
     * {@link Query} needs to be traversed in order to find the bind value and
     * change it.
     * <p>
     * As such, it is usually better to supply bind values directly with the
     * input of an expression, e.g.:
     * <ul>
     * <li>Directly with the {@link DSL} method, such as
     * {@link DSL#log(Field, Field)}, for example.</li>
     * <li>With the plain SQL template constructor, e.g.
     * {@link DSL#field(String, Object...)}</li>
     * <li>With the parser method, e.g.
     * {@link Parser#parseField(String, Object...)}</li>
     * </ul>
     * <h3>Inlined values</h3>
     * <p>
     * [#1886] If the bind value at <code>index</code> is inlined (
     * {@link Param#isInline()}) or if this query was created with
     * {@link StatementType#STATIC_STATEMENT} and there is an underlying
     * <code>PreparedStatement</code> kept open because of
     * {@link #keepStatement(boolean)}, the underlying
     * <code>PreparedStatement</code> will be closed automatically in order for
     * new bind values to have an effect.
     *
     * @param index The parameter index in rendering order, starting with 1
     * @param value The new bind value.
     * @throws IllegalArgumentException if there is no parameter by the given
     *             parameter index.
     * @throws DataTypeException if <code>value</code> cannot be converted into
     *             the parameter's data type
     */
    @NotNull
    Query bind(int index, Object value) throws IllegalArgumentException, DataTypeException;

    // ------------------------------------------------------------------------
    // JDBC methods
    // ------------------------------------------------------------------------

    /**
     * Specify whether any JDBC {@link java.sql.Statement} created by this query
     * should be {@link java.sql.Statement#setPoolable(boolean)}.
     * <p>
     * If this method is not called on jOOQ types, then jOOQ will not specify
     * the flag on JDBC either, resulting in JDBC's default behaviour.
     *
     * @see java.sql.Statement#setPoolable(boolean)
     */
    @NotNull
    Query poolable(boolean poolable);

    /**
     * Specify the query timeout in number of seconds for the underlying JDBC
     * {@link Statement}.
     *
     * @see java.sql.Statement#setQueryTimeout(int)
     */
    @NotNull
    Query queryTimeout(int seconds);

    /**
     * Keep the query's underlying statement open after execution.
     * <p>
     * This indicates to jOOQ that the query's underlying {@link Statement} or
     * {@link PreparedStatement} should be kept open after execution. If it is
     * kept open, client code is responsible for properly closing it using
     * {@link CloseableQuery#close()}, e.g. via a
     * <code>try-with-resources</code> statement.
     *
     * @param keepStatement Whether to keep the underlying statement open
     */
    @NotNull
    CloseableQuery keepStatement(boolean keepStatement);

    /**
     * Cancel the underlying statement.
     * <p>
     * This cancels the query's underlying {@link Statement} or
     * {@link PreparedStatement}. If there is no underlying open and running
     * statement, this call is simply ignored.
     *
     * @throws DataAccessException If something went wrong cancelling the
     *             statement
     * @see java.sql.Statement#cancel()
     */
    void cancel() throws DataAccessException;

}
