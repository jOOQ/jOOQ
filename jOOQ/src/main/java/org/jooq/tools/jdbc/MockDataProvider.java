/**
 * Copyright (c) 2009-2016, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
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
 *
 *
 *
 */
package org.jooq.tools.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.jooq.DSLContext;
import org.jooq.Query;
import org.jooq.Record;
import org.jooq.ResultQuery;

/**
 * A data provider for mock query executions.
 * <p>
 * Supply this data provider to your {@link MockConnection} in order to globally
 * provide data for SQL statements.
 * <p>
 * The general idea of mocking a JDBC connection with this jOOQ API is to
 * provide quick workarounds, injection points, etc. using a <em>very
 * simple</em> JDBC abstraction (see the {@link #execute(MockExecuteContext)}
 * method). It is <em>NOT RECOMMENDED</em> to emulate an entire database
 * (including complex state transitions, transactions, locking, etc.) using this
 * mock API. Once you have this requirement, please consider using an actual
 * database instead for integration testing, rather than implementing your test
 * database inside of a {@link MockDataProvider}.
 * <p>
 * See {@link #execute(MockExecuteContext)} for more details.
 *
 * @author Lukas Eder
 * @see MockConnection
 */

@FunctionalInterface

public interface MockDataProvider {

    /**
     * Execution callback for a JDBC query execution.
     * <p>
     * This callback will be called by {@link MockStatement} upon the various
     * statement execution methods. These include:
     * <p>
     * <ul>
     * <li>{@link Statement#execute(String)}</li>
     * <li>{@link Statement#execute(String, int)}</li>
     * <li>{@link Statement#execute(String, int[])}</li>
     * <li>{@link Statement#execute(String, String[])}</li>
     * <li>{@link Statement#executeBatch()}</li>
     * <li>{@link Statement#executeQuery(String)}</li>
     * <li>{@link Statement#executeUpdate(String)}</li>
     * <li>{@link Statement#executeUpdate(String, int)}</li>
     * <li>{@link Statement#executeUpdate(String, int[])}</li>
     * <li>{@link Statement#executeUpdate(String, String[])}</li>
     * <li>{@link PreparedStatement#execute()}</li>
     * <li>{@link PreparedStatement#executeQuery()}</li>
     * <li>{@link PreparedStatement#executeUpdate()}</li>
     * </ul>
     * <p>
     * The various execution modes are unified into this simple method.
     * Implementations should adhere to this contract:
     * <p>
     * <ul>
     * <li><code>MockStatement</code> does not distinguish between "static" and
     * "prepared" statements. However, a non-empty
     * {@link MockExecuteContext#bindings()} is a strong indicator for a
     * {@link PreparedStatement}.</li>
     * <li><code>MockStatement</code> does not distinguish between "batch" and
     * "single" statements. However...
     * <ul>
     * <li>A {@link MockExecuteContext#batchSQL()} with more than one SQL string
     * is a strong indicator for a "multi-batch statement", as understood by
     * jOOQ's {@link DSLContext#batch(Query...)}.</li>
     * <li>A {@link MockExecuteContext#batchBindings()} with more than one bind
     * variable array is a strong indicator for a "single-batch statement", as
     * understood by jOOQ's {@link DSLContext#batch(Query)}.</li>
     * </ul>
     * </li>
     * <li>It is recommended to return as many <code>MockResult</code> objects
     * as batch executions. In other words, you should guarantee that:
     * <p>
     * <code><pre>
     * int multiSize = context.getBatchSQL().length;
     * int singleSize = context.getBatchBindings().length;
     * assertEquals(result.length, Math.max(multiSize, singleSize))
     * </pre></code>
     * <p>
     * This holds true also for non-batch executions (where both sizes are equal
     * to <code>1</code>)</li>
     * <li>You may also return more than one result for non-batch executions.
     * This is useful for procedure calls with several result sets.
     * <ul>
     * <li>In JDBC, such additional result sets can be obtained with
     * {@link Statement#getMoreResults()}.</li>
     * <li>In jOOQ, such additional result sets can be obtained with
     * {@link ResultQuery#fetchMany()}</li>
     * </ul>
     * </li>
     * <li>If generated keys ({@link Statement#RETURN_GENERATED_KEYS}) are
     * requested from this execution, you can also add {@link MockResult#data}
     * to your result, in addition to the affected {@link MockResult#rows}. The
     * relevant flag is passed from <code>MockStatement</code> to any of these
     * properties:
     * <ul>
     * <li>{@link MockExecuteContext#autoGeneratedKeys()}</li>
     * <li>{@link MockExecuteContext#columnIndexes()}</li>
     * <li>{@link MockExecuteContext#columnNames()}</li>
     * </ul>
     * </li>
     * <li>OUT parameters from stored procedures are generated from the first
     * {@link MockResult}'s first {@link Record}. If OUT parameters are
     * requested, implementors must ensure the presence of such a
     * <code>Record</code>.</li>
     * </ul>
     *
     * @param ctx The execution context.
     * @return The execution results. This should be non-null and non-empty, as
     *         every execution is expected to return at least one result.
     * @throws SQLException A <code>SQLException</code> that is passed through
     *             to jOOQ.
     */
    MockResult[] execute(MockExecuteContext ctx) throws SQLException;
}
