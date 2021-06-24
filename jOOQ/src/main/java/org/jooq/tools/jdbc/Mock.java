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
package org.jooq.tools.jdbc;

import static org.jooq.impl.DSL.using;

import java.sql.SQLException;

import org.jooq.Record;
import org.jooq.Result;

/**
 * Various utilities related to {@link MockDataProvider}.
 * <p>
 * <strong>Disclaimer: The general idea of mocking a JDBC connection with this
 * jOOQ API is to provide quick workarounds, injection points, etc. using a very
 * simple JDBC abstraction. It is NOT RECOMMENDED to emulate an entire database
 * (including complex state transitions, transactions, locking, etc.) using this
 * mock API. Once you have this requirement, please consider using an actual
 * database instead for integration testing (e.g. using
 * <a href="https://www.testcontainers.org">https://www.testcontainers.org</a>),
 * rather than implementing your test database inside of a
 * MockDataProvider.</strong>
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
    public static final MockDataProvider of(MockResult... result) {
        return ctx -> result;
    }

    /**
     * Create a new {@link MockDataProvider} that always throws the same
     * exception for all queries.
     */
    public static final MockDataProvider of(SQLException exception) {
        return of(new MockResult(exception));
    }

    /**
     * Wrap a record in a result.
     */
    static final Result<?> result(Record data) {
        Result<Record> result = using(data.configuration()).newResult(data.fields());
        result.add(data);

        return result;
    }

    /**
     * No instances
     */
    private Mock() {}
}
