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
package org.jooq.tools.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.jooq.DSLContext;
import org.jooq.Query;
import org.jooq.ResultQuery;

/**
 * A data provider for mock query executions.
 * <p>
 * Supply this data provider to your {@link MockConnection} in order to globally
 * provide data for SQL statements.
 * <p>
 * See {@link #execute(MockExecuteContext)} for details.
 *
 * @author Lukas Eder
 * @see MockConnection
 */
public interface MockDataProvider {

    /**
     * Execution callback for a JDBC query execution.
     * <p>
     * This callback will be called by {@link MockStatement} upon the various
     * statement execution methods. These include:
     * <p>
     * <ul>
     * <li> {@link Statement#execute(String)}</li>
     * <li> {@link Statement#execute(String, int)}</li>
     * <li> {@link Statement#execute(String, int[])}</li>
     * <li> {@link Statement#execute(String, String[])}</li>
     * <li> {@link Statement#executeBatch()}</li>
     * <li> {@link Statement#executeQuery(String)}</li>
     * <li> {@link Statement#executeUpdate(String)}</li>
     * <li> {@link Statement#executeUpdate(String, int)}</li>
     * <li> {@link Statement#executeUpdate(String, int[])}</li>
     * <li> {@link Statement#executeUpdate(String, String[])}</li>
     * <li> {@link PreparedStatement#execute()}</li>
     * <li> {@link PreparedStatement#executeQuery()}</li>
     * <li> {@link PreparedStatement#executeUpdate()}</li>
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
     * <li>A {@link MockExecuteContext#batchSQL()} with more than one SQL
     * string is a strong indicator for a "multi-batch statement", as understood
     * by jOOQ's {@link DSLContext#batch(Query...)}.</li>
     * <li>A {@link MockExecuteContext#batchBindings()} with more than one
     * bind variable array is a strong indicator for a "single-batch statement",
     * as understood by jOOQ's {@link DSLContext#batch(Query)}.</li>
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
     * <li> {@link MockExecuteContext#autoGeneratedKeys()}</li>
     * <li> {@link MockExecuteContext#columnIndexes()}</li>
     * <li> {@link MockExecuteContext#columnNames()}</li>
     * </ul>
     * </li>
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
