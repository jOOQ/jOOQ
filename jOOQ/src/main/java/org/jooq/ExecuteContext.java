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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
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
 *
 *
 *
 */
package org.jooq;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.stream.Collector;

import org.jooq.conf.Settings;
import org.jooq.conf.StatementType;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;

import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A context object for {@link Query} execution passed to registered
 * {@link ExecuteListener}'s.
 * <p>
 * This type implements {@link Scope} and thus has a lifecycle defined by the
 * query execution.
 * <p>
 * The {@link #data()} map contents are maintained for the entirety of the
 * execution, and are passed along to child {@link Scope} types, including e.g.
 * <ul>
 * <li>{@link BindingScope}: When passing bind values or reading results.</li>
 * </ul>
 *
 * @author Lukas Eder
 * @see ExecuteListener
 */
public interface ExecuteContext extends Scope {

    /**
     * The connection to be used in this execute context.
     * <p>
     * This returns a proxy to the {@link Configuration#connectionProvider()}
     * 's supplied connection. This proxy takes care of two things:
     * <ul>
     * <li>It takes care of properly implementing
     * {@link Settings#getStatementType()}</li>
     * <li>It takes care of properly returning a connection to
     * {@link ConnectionProvider#release(Connection)}, once jOOQ can release the
     * connection</li>
     * </ul>
     */
    Connection connection();

    /**
     * The type of database interaction that is being executed.
     *
     * @see ExecuteType
     */
    @NotNull
    ExecuteType type();

    /**
     * The jOOQ {@link Query} that is being executed or <code>null</code> if the
     * query is unknown, if it is a batch query, or if there was no jOOQ
     * <code>Query</code>.
     *
     * @see #routine()
     * @see #batchQueries()
     */
    @Nullable
    Query query();

    /**
     * The jOOQ {@link Query} objects that are being executed in batch mode, or
     * empty if the query is unknown or if there was no jOOQ <code>Query</code>.
     * <p>
     * If a single <code>Query</code> is executed in non-batch mode, this will
     * return an array of length <code>1</code>, containing that
     * <code>Query</code>
     *
     * @see #query()
     * @see #routine()
     * @return The executed <code>Query</code> object(s). This is never
     *         <code>null</code>
     */
    @NotNull
    Query @NotNull [] batchQueries();

    /**
     * The jOOQ {@link Routine} that is being executed or <code>null</code> if
     * the query is unknown or if there was no jOOQ <code>Routine</code>.
     *
     * @see #routine()
     */
    @Nullable
    Routine<?> routine();

    /**
     * The SQL that is being executed or <code>null</code> if the SQL statement
     * is unknown or if there was no SQL statement.
     */
    @Nullable
    String sql();

    /**
     * Override the SQL statement that is being executed.
     * <p>
     * This may have no effect, if called at the wrong moment.
     *
     * @see ExecuteListener#renderEnd(ExecuteContext)
     * @see ExecuteListener#prepareStart(ExecuteContext)
     */
    void sql(String sql);

    /**
     * The generated SQL statements that are being executed in batch mode, or
     * empty if the query is unknown or if there was no SQL statement.
     * <p>
     * If a single <code>Query</code> is executed in non-batch mode, this will
     * return an array of length <code>1</code>, containing that
     * <code>Query</code>
     *
     * @see #query()
     * @see #routine()
     * @return The generated SQL statement(s). This is never <code>null</code>
     */
    @NotNull
    String @NotNull [] batchSQL();

    /**
     * Override the {@link Connection} that is being used for execution.
     * <p>
     * This may have no effect, if called at the wrong moment.
     *
     * @see ExecuteListener#start(ExecuteContext)
     */
    void connectionProvider(ConnectionProvider connectionProvider);

    /**
     * The {@link PreparedStatement} that is being executed or <code>null</code>
     * if the statement is unknown or if there was no statement.
     * <p>
     * This can be any of the following: <br>
     * <br>
     * <ul>
     * <li>A <code>java.sql.PreparedStatement</code> from your JDBC driver when
     * a jOOQ <code>Query</code> is being executed as
     * {@link StatementType#PREPARED_STATEMENT}</li>
     * <li>A <code>java.sql.Statement</code> from your JDBC driver wrapped in a
     * <code>java.sql.PreparedStatement</code> when your jOOQ <code>Query</code>
     * is being executed as {@link StatementType#STATIC_STATEMENT}</li>
     * <li>A <code>java.sql.CallableStatement</code> when you are executing a
     * jOOQ <code>Routine</code></li>
     * </ul>
     */
    @Nullable
    PreparedStatement statement();

    /**
     * Override the {@link PreparedStatement} that is being executed.
     * <p>
     * This may have no effect, if called at the wrong moment.
     *
     * @see ExecuteListener#prepareEnd(ExecuteContext)
     * @see ExecuteListener#bindStart(ExecuteContext)
     */
    void statement(PreparedStatement statement);

    /**
     * The number of times this particular statement has been executed.
     * <p>
     * Statements that are prepared by jOOQ can be executed multiple times
     * without being closed if {@link Query#keepStatement(boolean)} is
     * activated.
     * <p>
     * This value will increment as soon as the statement is about to be
     * executed (at the {@link ExecuteListener#executeStart(ExecuteContext)}
     * event).
     *
     * @see ExecuteListener#executeStart(ExecuteContext)
     */
    int statementExecutionCount();

    /**
     * The {@link ResultSet} that is being fetched or <code>null</code> if the
     * result set is unknown or if no result set is being fetched.
     */
    @Nullable
    ResultSet resultSet();

    /**
     * Override the {@link ResultSet} that is being fetched.
     * <p>
     * This may have no effect, if called at the wrong moment.
     *
     * @see ExecuteListener#executeEnd(ExecuteContext)
     * @see ExecuteListener#fetchStart(ExecuteContext)
     */
    void resultSet(ResultSet resultSet);

    /**
     * The 0-based record nesting level for {@link #record()}, relevant when nested
     * result events are triggered via
     * {@link ExecuteListener#resultStart(ExecuteContext)} and
     * {@link ExecuteListener#resultEnd(ExecuteContext)}, e.g. in the presence
     * of {@link DSL#multiset(Select)}.
     */
    int recordLevel();

    /**
     * The last record that was fetched from the result set, or
     * <code>null</code> if no record has been fetched.
     */
    @Nullable
    Record record();

    /**
     * Set the last record that was fetched from the result set.
     * <p>
     * Users shouldn't call this method, it is used by jOOQ internally.
     */
    @Internal
    void record(Record record);

    /**
     * The number of rows that were affected by the last statement.
     * <p>
     * This returns <code>-1</code>:
     * <ul>
     * <li>if the number of affected rows is not yet available (e.g. prior to
     * the {@link ExecuteListener#executeEnd(ExecuteContext)} event).</li>
     * <li>if affected rows are not applicable for the given statement
     * (statements that do not produce a JDBC
     * {@link Statement#getUpdateCount()}.</li>
     * </ul>
     */
    int rows();

    /**
     * Set the number of rows that were affected by the last statement.
     * <p>
     * Users shouldn't call this method, it is used by jOOQ internally.
     */
    @Internal
    void rows(int rows);

    /**
     * The number of rows that were affected by the last statement executed in
     * batch mode.
     * <p>
     * If a single <code>Query</code> is executed in non-batch mode, this will
     * return an array of length <code>1</code>, containing {@link #rows()}
     * <p>
     * This returns <code>-1</code> values if the number of affected rows is not
     * yet available, or if affected rows are not applicable for a given
     * statement.
     *
     * @see #rows()
     * @return The affected rows. This is never <code>null</code>
     */
    int @NotNull [] batchRows();

    /**
     * The 0-based result nesting level for {@link #result()}, relevant when nested
     * result events are triggered via
     * {@link ExecuteListener#resultStart(ExecuteContext)} and
     * {@link ExecuteListener#resultEnd(ExecuteContext)}, e.g. in the presence
     * of {@link DSL#multiset(Select)}.
     */
    int resultLevel();

    /**
     * The last result that was fetched from the result set, or
     * <code>null</code> if no result has been fetched, including when results
     * do not need to be buffered in a {@link Result} type, such as all
     * {@link ResultQuery#collect(Collector)} or {@link ResultQuery#iterator()}
     * based fetches.
     */
    @Nullable
    Result<?> result();

    /**
     * Set the last result that was fetched from the result set.
     * <p>
     * Users shouldn't call this method, it is used by jOOQ internally.
     */
    @Internal
    void result(Result<?> result);

    /**
     * The {@link RuntimeException} being thrown.
     */
    @Nullable
    RuntimeException exception();

    /**
     * Override the {@link RuntimeException} being thrown.
     * <p>
     * This may have no effect, if called at the wrong moment.
     * <p>
     * If <code>null</code> is being passed, jOOQ will internally translate the
     * "unavailable" exception to an unspecified {@link DataAccessException}.
     */
    void exception(RuntimeException e);

    /**
     * The {@link SQLException} that was thrown by the database.
     */
    @Nullable
    SQLException sqlException();

    /**
     * Override the {@link SQLException} being thrown.
     * <p>
     * Any <code>SQLException</code> will be wrapped by jOOQ using an unchecked
     * {@link DataAccessException}. To have jOOQ throw your own custom
     * {@link RuntimeException}, use {@link #exception(RuntimeException)}
     * instead. This may have no effect, if called at the wrong moment.
     * <p>
     * If <code>null</code> is being passed, jOOQ will internally translate the
     * "unavailable" exception to an unspecified {@link DataAccessException}.
     */
    void sqlException(SQLException e);

    /**
     * The {@link SQLWarning} that was emitted by the database.
     * <p>
     * Note that fetching of warnings can be disabled using
     * {@link Settings#isFetchWarnings()}, in case of which this property will
     * be <code>null</code>.
     */
    @Nullable
    SQLWarning sqlWarning();

    /**
     * Override the {@link SQLWarning} being emitted.
     */
    void sqlWarning(SQLWarning e);

    /**
     * Any server output collected from this statement when
     * <code>{@link Settings#getFetchServerOutputSize()} &gt; 0</code>.
     *
     * @return The server output. This is never <code>null</code>.
     */
    @NotNull
    String @NotNull [] serverOutput();

    /**
     * Any server output collected from this statement when
     * <code>{@link Settings#getFetchServerOutputSize()} &gt; 0</code>.
     */
    void serverOutput(String[] output);
}
