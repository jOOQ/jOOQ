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
package org.jooq;

import org.jetbrains.annotations.*;


// ...
// ...
// ...
import static org.jooq.SQLDialect.DUCKDB;
import static org.jooq.SQLDialect.H2;
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...
import static org.jooq.SQLDialect.SQLITE;
import static org.jooq.SQLDialect.TRINO;
import static org.jooq.SQLDialect.YUGABYTEDB;

import org.jooq.impl.DSL;

/**
 * A step in the construction of {@link DSL#jsonArrayAgg(Field)} or
 * {@link DSL#jsonbArrayAgg(Field)} functions where the <code>NULL</code> clause
 * can be defined.
 *
 * @author Lukas Eder
 */
public interface JSONArrayAggNullStep<T> extends JSONArrayAggReturningStep<T> {

    /**
     * Include <code>NULL</code> values in output JSON.
     */
    @NotNull
    @Support({ DUCKDB, H2, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    JSONArrayAggReturningStep<T> nullOnNull();

    /**
     * Exclude <code>NULL</code> values in output JSON.
     */
    @NotNull
    @Support({ DUCKDB, H2, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    JSONArrayAggReturningStep<T> absentOnNull();
}
