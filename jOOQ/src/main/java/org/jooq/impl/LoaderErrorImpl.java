/*
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */
package org.jooq.impl;

import org.jooq.LoaderError;
import org.jooq.Query;
import org.jooq.exception.DataAccessException;

/**
 * @author Lukas Eder
 */
final class LoaderErrorImpl implements LoaderError {

    private final DataAccessException exception;
    private final int                 rowIndex;
    private final String[]            row;
    private final Query               query;

    LoaderErrorImpl(DataAccessException exception, Object[] row, int rowIndex, Query query) {
        this.exception = exception;
        this.row = strings(row);
        this.rowIndex = rowIndex;
        this.query = query;
    }

    private static String[] strings(Object[] row) {
        if (row == null)
            return null;

        String[] result = new String[row.length];
        for (int i = 0; i < result.length; i++)
            result[i] = row[i] == null ? null : row[i].toString();

        return result;
    }

    @Override
    public DataAccessException exception() {
        return exception;
    }

    @Override
    public int rowIndex() {
        return rowIndex;
    }

    @Override
    public String[] row() {
        return row;
    }

    @Override
    public Query query() {
        return query;
    }
}
