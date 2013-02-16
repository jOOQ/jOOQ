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

import java.sql.Statement;

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
