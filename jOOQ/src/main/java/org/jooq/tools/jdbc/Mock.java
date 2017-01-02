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
package org.jooq.tools.jdbc;

import static org.jooq.impl.DSL.using;

import org.jooq.AttachableInternal;
import org.jooq.Configuration;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.DefaultConfiguration;

/**
 * Various utilities related to {@link MockDataProvider}.
 *
 * @author Lukas Eder
 */
public final class Mock {

    /**
     * Create a new {@link MockDataProvider} that always returns a single record
     * for all queries.
     */
    public static final MockDataProvider of(int rows) {
        return of(new MockResult(rows, null));
    }

    /**
     * Create a new {@link MockDataProvider} that always returns a single record
     * for all queries.
     */
    public static final MockDataProvider of(Record record) {
        return of(result(record));
    }

    /**
     * Create a new {@link MockDataProvider} that always returns the same result
     * for all queries.
     */
    public static final MockDataProvider of(Result<?> result) {
        return of(new MockResult(result.size(), result));
    }

    /**
     * Create a new {@link MockDataProvider} that always returns the same mock
     * results for all queries.
     */
    public static final MockDataProvider of(final MockResult... result) {
        return new MockDataProvider() {
            @Override
            public MockResult[] execute(MockExecuteContext ctx) {
                return result;
            }
        };
    }

    /**
     * Wrap a record in a result.
     */
    static final Result<?> result(Record data) {
        Configuration configuration = data instanceof AttachableInternal
            ? ((AttachableInternal) data).configuration()
            : new DefaultConfiguration();

        Result<Record> result = using(configuration).newResult(data.fields());
        result.add(data);

        return result;
    }

    /**
     * No instances
     */
    private Mock() {}
}
