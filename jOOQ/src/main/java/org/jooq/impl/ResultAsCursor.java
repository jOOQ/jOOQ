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
 *
 *
 *
 */
package org.jooq.impl;

import java.sql.ResultSet;
import java.util.Iterator;

import org.jooq.Record;
import org.jooq.Result;

/**
 * @author Lukas Eder
 */
final class ResultAsCursor<R extends Record> extends AbstractCursor<R> {
    private final Result<R>   result;
    private int               index;

    @SuppressWarnings("unchecked")
    ResultAsCursor(Result<R> result) {
        super(result.configuration(), (AbstractRow<R>) result.fieldsRow());

        this.result = result;
    }

    @Override
    public final Iterator<R> iterator() {
        return result.iterator();
    }

    @Override
    public final Result<R> fetchNext(int number) {
        Result<R> r = new ResultImpl<R>(configuration, fields);

        for (int i = 0; i < number && i + index < result.size(); i++)
            r.add(result.get(i + index));

        index += number;
        return r;
    }

    @Override
    public void close() {}

    @Override
    public boolean isClosed() {
        return false;
    }

    @Override
    public final ResultSet resultSet() {
        return result.intoResultSet();
    }
}
