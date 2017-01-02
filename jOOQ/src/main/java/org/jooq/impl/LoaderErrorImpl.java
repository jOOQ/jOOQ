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
