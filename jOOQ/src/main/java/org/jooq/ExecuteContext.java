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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.jooq.conf.Settings;
import org.jooq.conf.StatementType;
import org.jooq.exception.DataAccessException;

/**
 * A context object for {@link Query} execution passed to registered
 * {@link ExecuteListener}'s.
 * <p>
 * Expect most of this context's objects to be <code>nullable</code>!
 *
 * @author Lukas Eder
 * @see ExecuteListener
 */
public interface ExecuteContext {

    /**
     * Get all custom data from this <code>ExecuteContext</code>.
     * <p>
     * This is custom data that was previously set to the execute context using
     * {@link #data(Object, Object)}. Use custom data if you want to pass data
     * between events received by an {@link ExecuteListener}.
     * <p>
     * Unlike {@link Configuration#data()}, these data's lifecycle only
     * matches that of a single query execution.
     *
     * @return The custom data. This is never <code>null</code>
     * @see ExecuteListener
     */
    Map<Object, Object> data();

    /**
     * Get some custom data from this <code>ExecuteContext</code>.
     * <p>
     * This is custom data that was previously set to the execute context using
     * {@link #data(Object, Object)}. Use custom data if you want to pass
     * data between events received by an {@link ExecuteListener}.
     * <p>
     * Unlike {@link Configuration#data()}, these data's lifecycle only
     * matches that of a single query execution.
     *
     * @param key A key to identify the custom data
     * @return The custom data or <code>null</code> if no such data is contained
     *         in this <code>ExecuteContext</code>
     * @see ExecuteListener
     */
    Object data(Object key);

    /**
     * Set some custom data to this <code>ExecuteContext</code>.
     * <p>
     * This is custom data that was previously set to the execute context using
     * {@link #data(Object, Object)}. Use custom data if you want to pass
     * data between events received by an {@link ExecuteListener}.
     * <p>
     * Unlike {@link Configuration#data()}, these data's lifecycle only
     * matches that of a single query execution.
     *
     * @param key A key to identify the custom data
     * @param value The custom data or <code>null</code> to unset the custom
     *            data
     * @return The previously set custom data or <code>null</code> if no data
     *         was previously set for the given key
     * @see ExecuteListener
     */
    Object data(Object key, Object value);

    /**
     * The configuration wrapped by this context.
     */
    Configuration configuration();

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
    ExecuteType type();

    /**
     * The jOOQ {@link Query} that is being executed or <code>null</code> if the
     * query is unknown, if it is a batch query, or if there was no jOOQ
     * <code>Query</code>.
     *
     * @see #routine()
     * @see #batchQueries()
     */
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
    Query[] batchQueries();

    /**
     * The jOOQ {@link Routine} that is being executed or <code>null</code> if
     * the query is unknown or if there was no jOOQ <code>Routine</code>.
     *
     * @see #routine()
     */
    Routine<?> routine();

    /**
     * The SQL that is being executed or <code>null</code> if the SQL statement
     * is unknown or if there was no SQL statement.
     */
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
    String[] batchSQL();

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
     * This can be any of the following: <br/>
     * <br/>
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
     * The {@link ResultSet} that is being fetched or <code>null</code> if the
     * result set is unknown or if no result set is being fetched.
     */
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
     * The last record that was fetched from the result set, or
     * <code>null</code> if no record has been fetched.
     */
    Record record();

    /**
     * Calling this has no effect. It is used by jOOQ internally.
     */
    void record(Record record);

    /**
     * The last result that was fetched from the result set, or
     * <code>null</code> if no result has been fetched.
     */
    Result<?> result();

    /**
     * Calling this has no effect. It is used by jOOQ internally.
     */
    void result(Result<?> result);

    /**
     * The {@link RuntimeException} being thrown.
     */
    RuntimeException exception();

    /**
     * Override the {@link RuntimeException} being thrown.
     * <p>
     * This may have no effect, if called at the wrong moment.
     */
    void exception(RuntimeException e);

    /**
     * The {@link SQLException} that was thrown by the database.
     */
    SQLException sqlException();

    /**
     * Override the {@link SQLException} being thrown.
     * <p>
     * Any <code>SQLException</code> will be wrapped by jOOQ using an unchecked
     * {@link DataAccessException}. To have jOOQ throw your own custom
     * {@link RuntimeException}, use {@link #exception(RuntimeException)}
     * instead. This may have no effect, if called at the wrong moment.
     */
    void sqlException(SQLException e);
}
