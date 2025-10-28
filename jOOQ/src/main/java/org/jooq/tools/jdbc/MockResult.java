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
package org.jooq.tools.jdbc;

import java.sql.SQLException;
import java.sql.Statement;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.Internal;

/**
 * A mock result.
 * <p>
 * This type is used to wrap unified results of DDL and DML query executions.
 * JDBC execution results can be summarised to two properties:
 * <ul>
 * <li> {@link Statement#getUpdateCount()}: The number of affected rows</li>
 * <li> {@link Statement#getResultSet()}: The result set</li>
 * </ul>
 * <p>
 * See {@link MockDataProvider#execute(MockExecuteContext)} for more details
 *
 * @author Lukas Eder
 * @see MockDataProvider
 */
public class MockResult {

    /**
     * The number of affected rows for this execution result.
     * <p>
     * This number corresponds to the value of
     * {@link Statement#getUpdateCount()}. The following values are possible:
     * <ul>
     * <li>Positive numbers: the number of affected rows by a given query
     * execution</li>
     * <li>0: no rows were affected by a given query execution</li>
     * <li>-1: the row count is not applicable</li>
     * </ul>
     */
    public final int          rows;

    /**
     * The number of affected rows for this execution result.
     * <p>
     * This number corresponds to the value of
     * {@link Statement#getLargeUpdateCount()} where supported. The following
     * values are possible:
     * <ul>
     * <li>Positive numbers: the number of affected rows by a given query
     * execution</li>
     * <li>0: no rows were affected by a given query execution</li>
     * <li>-1: the row count is not applicable</li>
     * </ul>
     */
    public final long         largeRows;

    /**
     * The result data associated with this execution result.
     * <p>
     * This object describes the result data (including meta data).
     * <p>
     * If the given query execution did not provide any results (as in
     * <code>{@link Statement#execute(String)} == false</code>), this may be
     * <code>null</code>. This is not the same as producing an <em>empty</em>
     * result, which can only be modelled by an empty {@link Result}, containing
     * column information but no rows.
     * </p>
     * Note, that this can also be used to provide a result for
     * {@link Statement#getGeneratedKeys()}
     */
    public final Result<?>    data;

    /**
     * The exception associated with this execution result.
     * <p>
     * If present, the current result produces an exception.
     */
    public final SQLException exception;

    /**
     * Create a new <code>MockResult</code>.
     * <p>
     * This is a convenience constructor calling
     * <code>MockResult(-1, null)</code>.
     *
     * @see MockDataProvider <code>MockDataProvider</code> for details
     */
    public MockResult() {
        this(-1, null);
    }

    /**
     * Create a new <code>MockResult</code>.
     * <p>
     * This is a convenience constructor calling
     * <code>MockResult(rows, null)</code>.
     *
     * @see MockDataProvider <code>MockDataProvider</code> for details
     */
    public MockResult(int rows) {
        this(rows, null);
    }

    /**
     * Create a new <code>MockResult</code>.
     * <p>
     * This is a convenience constructor calling
     * <code>MockResult(rows, null)</code>.
     *
     * @see MockDataProvider <code>MockDataProvider</code> for details
     */
    public MockResult(long largeRows) {
        this(largeRows, null);
    }

    /**
     * Create a new <code>MockResult</code>.
     * <p>
     * This is a convenience constructor creating a <code>MockResult</code> with
     * exactly one record.
     *
     * @param data The single record in this result. Record instances can be
     *            obtained from queries, instantiated from generated record
     *            classes, or created using
     *            {@link DSLContext#newRecord(org.jooq.Field...)} and other
     *            overloads.
     * @see MockDataProvider <code>MockDataProvider</code> for details
     */
    public MockResult(Record data) {
        this(1, Mock.result(data));
    }

    /**
     * Create a new <code>MockResult</code>.
     *
     * @param rows The number of affected rows
     * @param data The result data. Result instances can be obtained from
     *            queries, or created using
     *            {@link DSLContext#newResult(org.jooq.Field...)} and other
     *            overloads.
     * @see MockDataProvider <code>MockDataProvider</code> for details
     */
    public MockResult(int rows, Result<?> data) {
        this.rows = rows;
        this.largeRows = rows;
        this.data = data;
        this.exception = null;
    }

    /**
     * Create a new <code>MockResult</code>.
     *
     * @param largeRows The number of affected rows
     * @param data The result data. Result instances can be obtained from
     *            queries, or created using
     *            {@link DSLContext#newResult(org.jooq.Field...)} and other
     *            overloads.
     * @see MockDataProvider <code>MockDataProvider</code> for details
     */
    public MockResult(long largeRows, Result<?> data) {
        this.rows = Internal.truncateUpdateCount(largeRows);
        this.largeRows = largeRows;
        this.data = data;
        this.exception = null;
    }

    public MockResult(SQLException exception) {
        this.rows = -1;
        this.largeRows = -1;
        this.data = null;
        this.exception = exception;
    }

    @Override
    public String toString() {
        return (exception != null)
             ? "Exception : " + exception.getMessage()
             : (data != null)
             ? data.toString()
             : ("" + largeRows);
    }
}
