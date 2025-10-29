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

// ...
// ...
// ...
import static org.jooq.SQLDialect.CLICKHOUSE;
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
// ...
import static org.jooq.SQLDialect.DUCKDB;
// ...
import static org.jooq.SQLDialect.H2;
// ...
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.MARIADB;
// ...
import static org.jooq.SQLDialect.MYSQL;
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
// ...
import static org.jooq.SQLDialect.TRINO;
import static org.jooq.SQLDialect.YUGABYTEDB;

import java.util.Collection;

import org.jetbrains.annotations.NotNull;

/**
 * An ordered-set aggregate function.
 * <p>
 * An ordered-set aggregate function is an aggregate function with a mandatory
 * Oracle-specific <code>WITHIN GROUP (ORDER BY …)</code> clause. An example is
 * <code>LISTAGG</code>: <pre><code>
 * SELECT   LISTAGG(TITLE, ', ')
 *          WITHIN GROUP (ORDER BY TITLE)
 * FROM     T_BOOK
 * GROUP BY AUTHOR_ID
 * </code></pre> The above function groups books by author and aggregates titles
 * into a concatenated string.
 * <p>
 * Ordered-set aggregate functions can be further converted into window functions
 * using the <code>OVER(PARTITION BY …)</code> clause. For example: <pre><code>
 * SELECT LISTAGG(TITLE, ', ')
 *        WITHIN GROUP (ORDER BY TITLE)
 *        OVER (PARTITION BY AUTHOR_ID)
 * FROM   T_BOOK
 * </code></pre>
 *
 * @author Lukas Eder
 */
public interface OrderedAggregateFunction<T> {

    /**
     * Add an <code>WITHIN GROUP (ORDER BY …)</code> clause to the ordered
     * aggregate function.
     */
    @NotNull
    @Support({ CLICKHOUSE, CUBRID, DUCKDB, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    AggregateFilterStep<T> withinGroupOrderBy(OrderField<?>... fields);

    /**
     * Add an <code>WITHIN GROUP (ORDER BY …)</code> clause to the ordered
     * aggregate function.
     */
    @NotNull
    @Support({ CLICKHOUSE, CUBRID, DUCKDB, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    AggregateFilterStep<T> withinGroupOrderBy(Collection<? extends OrderField<?>> fields);

    /**
     * Add an <code>ORDER BY …</code> clause to the ordered aggregate function.
     */
    @NotNull
    @Support({ CLICKHOUSE, CUBRID, DUCKDB, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    AggregateFilterStep<T> orderBy(OrderField<?>... fields);

    /**
     * Add an <code>ORDER BY …</code> clause to the ordered aggregate function.
     */
    @NotNull
    @Support({ CLICKHOUSE, CUBRID, DUCKDB, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    AggregateFilterStep<T> orderBy(Collection<? extends OrderField<?>> fields);
}
