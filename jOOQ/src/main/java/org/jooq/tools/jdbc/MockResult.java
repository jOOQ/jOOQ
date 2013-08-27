/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 * 
 * This work is dual-licensed Open Source, under LGPL and jOOQ EULA
 * =============================================================================
 * You may freely choose which license applies to you. For more information 
 * about licensing, please visit http://www.jooq.org/licenses
 * 
 * LGPL:  
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 * 
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 * 
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details.
 * 
 * You should have received a copy of the jOOQ End User License Agreement
 * along with this library.
 * If not, see http://www.jooq.org/eula
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
