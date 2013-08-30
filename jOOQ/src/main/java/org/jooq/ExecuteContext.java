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
