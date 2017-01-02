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

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;

import org.jooq.conf.ParamType;
import org.jooq.conf.Settings;
import org.jooq.conf.StatementType;
import org.jooq.exception.DataAccessException;
import org.jooq.exception.DataTypeException;
import org.jooq.impl.DSL;

/**
 * Any query
 *
 * @author Lukas Eder
 */
public interface Query extends QueryPart, Attachable , AutoCloseable  {

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
     * Retrieve the SQL code rendered by this Query.
     * <p>
     * Use this method, when you want to use jOOQ for object oriented query
     * creation, but execute the query with some other technology, such as
     * <ul>
     * <li>JDBC</li>
     * <li>Spring Templates</li>
     * <li>JPA native queries</li>
     * <li>etc...</li>
     * </ul>
     * <p>
     * Note, this is the same as calling {@link #getSQL(boolean)}. The boolean
     * parameter will depend on your {@link DSLContext}'s {@link Settings}:
     * <table border="1">
     * <tr>
     * <th><code>StatementType</code></th>
     * <th>boolean parameter</th>
     * <th>effect</th>
     * </tr>
     * <tr>
     * <td> {@link StatementType#PREPARED_STATEMENT}</td>
     * <td><code>false</code> (default)</td>
     * <td>This will render bind variables to be used with a JDBC
     * {@link PreparedStatement}. You can extract bind values from this
     * <code>Query</code> using {@link #getBindValues()}</td>
     * </tr>
     * <tr>
     * <td> {@link StatementType#STATIC_STATEMENT}</td>
     * <td><code>true</code></td>
     * <td>This will inline all bind variables in a statement to be used with a
     * JDBC {@link Statement}</td>
     * </tr>
     * </table>
     * <p>
     * [#1520] Note that the query actually being executed might not contain any
     * bind variables, in case the number of bind variables exceeds your SQL
     * dialect's maximum number of supported bind variables. This is not
     * reflected by this method, which will only use the {@link Settings} to
     * decide whether to render bind values.
     *
     * @see #getSQL(boolean)
     */
    String getSQL();

    /**
     * Retrieve the SQL code rendered by this Query.
     * <p>
     * [#1520] Note that the query actually being executed might not contain any
     * bind variables, in case the number of bind variables exceeds your SQL
     * dialect's maximum number of supported bind variables. This is not
     * reflected by this method, which will only use <code>inline</code>
     * argument to decide whether to render bind values.
     * <p>
     * See {@link #getSQL()} for more details.
     *
     * @param inline Whether to inline bind variables. This overrides values in
     *            {@link Settings#getStatementType()}
     * @return The generated SQL
     * @deprecated - [#2414] - 3.1.0 - Use {@link #getSQL(ParamType)} instead
     */
    @Deprecated
    String getSQL(boolean inline);

    /**
     * Retrieve the SQL code rendered by this Query.
     * <p>
     * [#1520] Note that the query actually being executed might not contain any
     * bind variables, in case the number of bind variables exceeds your SQL
     * dialect's maximum number of supported bind variables. This is not
     * reflected by this method, which will only use <code>paramType</code>
     * argument to decide whether to render bind values.
     * <p>
     * See {@link #getSQL()} for more details.
     *
     * @param paramType How to render parameters. This overrides values in
     *            {@link Settings#getStatementType()}
     * @return The generated SQL
     */
    String getSQL(ParamType paramType);

    /**
     * Retrieve the bind values that will be bound by this Query. This
     * <code>List</code> cannot be modified. To modify bind values, use
     * {@link #getParams()} instead.
     * <p>
     * Unlike {@link #getParams()}, which returns also inlined parameters, this
     * returns only actual bind values that will render an actual bind value as
     * a question mark <code>"?"</code>
     *
     * @see DSLContext#extractBindValues(QueryPart)
     */
    List<Object> getBindValues();

    /**
     * Get a <code>Map</code> of named parameters. The <code>Map</code> itself
     * cannot be modified, but the {@link Param} elements allow for modifying
     * bind values on an existing {@link Query}.
     * <p>
     * Bind values created with {@link DSL#val(Object)} will have their bind
     * index as name.
     *
     * @see Param
     * @see DSL#param(String, Object)
     * @see DSLContext#extractParams(QueryPart)
     */
    Map<String, Param<?>> getParams();

    /**
     * Get a named parameter from the {@link Query}, provided its name.
     * <p>
     * Bind values created with {@link DSL#val(Object)} will have their bind
     * index as name.
     *
     * @see Param
     * @see DSL#param(String, Object)
     * @see DSLContext#extractParam(QueryPart, String)
     */
    Param<?> getParam(String name);

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
     *
     * @param param The named parameter name. If this is a number, then this is
     *            the same as calling {@link #bind(int, Object)}
     * @param value The new bind value.
     * @throws IllegalArgumentException if there is no parameter by the given
     *             parameter name or index.
     * @throws DataTypeException if <code>value</code> cannot be converted into
     *             the parameter's data type
     */
    Query bind(String param, Object value) throws IllegalArgumentException, DataTypeException;

    /**
     * Bind a new value to an indexed parameter.
     * <p>
     * [#1886] If the bind value at <code>index</code> is inlined (
     * {@link Param#isInline()}) or if this query was created with
     * {@link StatementType#STATIC_STATEMENT} and there is an underlying
     * <code>PreparedStatement</code> kept open because of
     * {@link #keepStatement(boolean)}, the underlying
     * <code>PreparedStatement</code> will be closed automatically in order for
     * new bind values to have an effect.
     *
     * @param index The parameter index, starting with 1
     * @param value The new bind value.
     * @throws IllegalArgumentException if there is no parameter by the given
     *             parameter index.
     * @throws DataTypeException if <code>value</code> cannot be converted into
     *             the parameter's data type
     */
    Query bind(int index, Object value) throws IllegalArgumentException, DataTypeException;

    // ------------------------------------------------------------------------
    // JDBC methods
    // ------------------------------------------------------------------------

    /**
     * Specify the query timeout for the underlying JDBC {@link Statement}.
     *
     * @see Statement#setQueryTimeout(int)
     */
    Query queryTimeout(int timeout);

    /**
     * Keep the query's underlying statement open after execution.
     * <p>
     * This indicates to jOOQ that the query's underlying {@link Statement} or
     * {@link PreparedStatement} should be kept open after execution. If it is
     * kept open, client code is responsible for properly closing it using
     * {@link #close()}
     *
     * @param keepStatement Whether to keep the underlying statement open
     */
    Query keepStatement(boolean keepStatement);

    /**
     * Close the underlying statement.
     * <p>
     * This closes the query's underlying {@link Statement} or
     * {@link PreparedStatement} if a previous call to
     * {@link #keepStatement(boolean)} indicated that jOOQ should keep
     * statements open after query execution. If there is no underlying open
     * statement, this call is simply ignored.
     *
     * @throws DataAccessException If something went wrong closing the statement
     * @see Statement#close()
     */

    @Override

    void close() throws DataAccessException;

    /**
     * Cancel the underlying statement.
     * <p>
     * This cancels the query's underlying {@link Statement} or
     * {@link PreparedStatement}. If there is no underlying open and running
     * statement, this call is simply ignored.
     *
     * @throws DataAccessException If something went wrong cancelling the
     *             statement
     * @see Statement#cancel()
     */
    void cancel() throws DataAccessException;

}
