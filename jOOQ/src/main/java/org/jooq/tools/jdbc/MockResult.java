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

import java.sql.Statement;

import org.jooq.Record;
import org.jooq.Result;

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
    public final int       rows;

    /**
     * The result data associated with this execution result.
     * <p>
     * This object describes the result data (including meta data). If the given
     * query execution did not provide any results, this may be
     * <code>null</code>. Note, that this can also be used to provide a result
     * for {@link Statement#getGeneratedKeys()}
     */
    public final Result<?> data;

    /**
     * Create a new <code>MockResult</code>.
     * <p>
     * This is a convenience constructor creating a <code>MockResult</code> with exactly one record.
     *
     * @param data The single record in this result.
     */
    public MockResult(Record data) {
        this(1, Mock.result(data));
    }

    /**
     * Create a new <code>MockResult</code>.
     *
     * @param rows The number of affected rows
     * @param data The result data
     */
    public MockResult(int rows, Result<?> data) {
        this.rows = rows;
        this.data = data;
    }

    @Override
    public String toString() {
        return (data != null) ? data.toString() : ("" + rows);
    }
}
