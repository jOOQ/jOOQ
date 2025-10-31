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
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.DUCKDB;
// ...
import static org.jooq.SQLDialect.H2;
// ...
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.IGNITE;
// ...
// ...
import static org.jooq.SQLDialect.MARIADB;
// ...
// ...
import static org.jooq.SQLDialect.MYSQL;
// ...
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...
// ...
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
// ...
import static org.jooq.SQLDialect.TRINO;
// ...
import static org.jooq.SQLDialect.YUGABYTEDB;

import org.jetbrains.annotations.NotNull;


/**
 * This type is used for the {@link Select}'s DSL API when selecting generic
 * {@link Record} types.
 * <p>
 * Example: <pre><code>
 * -- get all authors' first and last names, and the number
 * -- of books they've written in German, if they have written
 * -- more than five books in German in the last three years
 * -- (from 2011), and sort those authors by last names
 * -- limiting results to the second and third row
 *
 *   SELECT T_AUTHOR.FIRST_NAME, T_AUTHOR.LAST_NAME, COUNT(*)
 *     FROM T_AUTHOR
 *     JOIN T_BOOK ON T_AUTHOR.ID = T_BOOK.AUTHOR_ID
 *    WHERE T_BOOK.LANGUAGE = 'DE'
 *      AND T_BOOK.PUBLISHED &gt; '2008-01-01'
 * GROUP BY T_AUTHOR.FIRST_NAME, T_AUTHOR.LAST_NAME
 *   HAVING COUNT(*) &gt; 5
 * ORDER BY T_AUTHOR.LAST_NAME ASC NULLS FIRST
 *    LIMIT 2
 *   OFFSET 1
 *      FOR UPDATE
 *       OF FIRST_NAME, LAST_NAME
 *       NO WAIT
 * </code></pre> Its equivalent in jOOQ <pre><code>
 * create.select(TAuthor.FIRST_NAME, TAuthor.LAST_NAME, create.count())
 *       .from(T_AUTHOR)
 *       .join(T_BOOK).on(TBook.AUTHOR_ID.equal(TAuthor.ID))
 *       .where(TBook.LANGUAGE.equal("DE"))
 *       .and(TBook.PUBLISHED.greaterThan(parseDate('2008-01-01')))
 *       .groupBy(TAuthor.FIRST_NAME, TAuthor.LAST_NAME)
 *       .having(create.count().greaterThan(5))
 *       .orderBy(TAuthor.LAST_NAME.asc().nullsFirst())
 *       .limit(2)
 *       .offset(1)
 *       .forUpdate()
 *       .of(TAuthor.FIRST_NAME, TAuthor.LAST_NAME)
 *       .noWait();
 * </code></pre> Refer to the manual for more details
 * <p>
 * <h3>Referencing <code>XYZ*Step</code> types directly from client code</h3>
 * <p>
 * It is usually not recommended to reference any <code>XYZ*Step</code> types
 * directly from client code, or assign them to local variables. When writing
 * dynamic SQL, creating a statement's components dynamically, and passing them
 * to the DSL API statically is usually a better choice. See the manual's
 * section about dynamic SQL for details: <a href=
 * "https://www.jooq.org/doc/latest/manual/sql-building/dynamic-sql">https://www.jooq.org/doc/latest/manual/sql-building/dynamic-sql</a>.
 * <p>
 * Drawbacks of referencing the <code>XYZ*Step</code> types directly:
 * <ul>
 * <li>They're operating on mutable implementations (as of jOOQ 3.x)</li>
 * <li>They're less composable and not easy to get right when dynamic SQL gets
 * complex</li>
 * <li>They're less readable</li>
 * <li>They might have binary incompatible changes between minor releases</li>
 * </ul>
 *
 * @author Lukas Eder
 */
public interface SelectUnionStep<R extends Record> extends SelectCorrelatedSubqueryStep<R> {

    /**
     * Apply the <code>UNION</code> set operation.
     * <p>
     * In SQL, a <code>UNION</code> is <code>DISTINCT</code> by default,
     * meaning, duplicates are removed from the result set. So, this is the same
     * as {@link #unionDistinct(Select)}. If duplicate removal isn't required,
     * or already guaranteed by the data model, it is recommended to use
     * {@link #unionAll(Select)}, instead.
     */
    @Override
    @NotNull @CheckReturnValue
    @Support
    SelectOrderByStep<R> union(Select<? extends R> select);

    /**
     * Apply the <code>UNION DISTINCT</code> set operation.
     * <p>
     * In SQL, a <code>UNION</code> is <code>DISTINCT</code> by default.
     * However, it is often useful to make this explicit to express intent when
     * distinct removal is really desired.
     */
    @Override
    @NotNull @CheckReturnValue
    @Support
    SelectOrderByStep<R> unionDistinct(Select<? extends R> select);

    /**
     * Apply the <code>UNION ALL</code> set operation.
     */
    @Override
    @NotNull @CheckReturnValue
    @Support
    SelectOrderByStep<R> unionAll(Select<? extends R> select);

    /**
     * Apply the <code>EXCEPT</code> (or <code>MINUS</code>) set operation.
     * <p>
     * In SQL, an <code>EXCEPT</code> is <code>DISTINCT</code> by default,
     * meaning, duplicates are removed from the result set. So, this is the same
     * as {@link #exceptDistinct(Select)}. If duplicate removal isn't required,
     * or already guaranteed by the data model, it is recommended to use
     * {@link #exceptAll(Select)}, instead, if the underlying RDBMS supports it.
     */
    @Override
    @NotNull @CheckReturnValue
    @Support({ CLICKHOUSE, CUBRID, DERBY, DUCKDB, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    SelectOrderByStep<R> except(Select<? extends R> select);

    /**
     * Apply the <code>EXCEPT</code> (or <code>MINUS</code>) set operation.
     * <p>
     * In SQL, an <code>EXCEPT</code> is <code>DISTINCT</code> by default.
     * However, it is often useful to make this explicit to express intent when
     * distinct removal is really desired.
     */
    @Override
    @NotNull @CheckReturnValue
    @Support({ CLICKHOUSE, CUBRID, DERBY, DUCKDB, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    SelectOrderByStep<R> exceptDistinct(Select<? extends R> select);

    /**
     * Apply the <code>EXCEPT ALL</code> set operation.
     */
    @Override
    @NotNull @CheckReturnValue
    @Support({ CLICKHOUSE, CUBRID, DERBY, DUCKDB, HSQLDB, MARIADB, MYSQL, POSTGRES, TRINO, YUGABYTEDB })
    SelectOrderByStep<R> exceptAll(Select<? extends R> select);

    /**
     * Apply the <code>INTERSECT</code> set operation.
     * <p>
     * In SQL, an <code>INTERSECT</code> is <code>DISTINCT</code> by default,
     * meaning, duplicates are removed from the result set. So, this is the same
     * as {@link #intersectDistinct(Select)}. If duplicate removal isn't
     * required, or already guaranteed by the data model, it is recommended to
     * use {@link #intersectAll(Select)}, instead, if the underlying RDBMS
     * supports it. Apply the <code>INTERSECT</code> set operation.
     */
    @Override
    @NotNull @CheckReturnValue
    @Support({ CLICKHOUSE, CUBRID, DERBY, DUCKDB, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    SelectOrderByStep<R> intersect(Select<? extends R> select);

    /**
     * Apply the <code>INTERSECT</code> set operation.
     * <p>
     * In SQL, an <code>INTERSECT</code> is <code>DISTINCT</code> by default,
     * meaning, duplicates are removed from the result set. So, this is the same
     * as {@link #intersectDistinct(Select)}. If duplicate removal isn't
     * required, or already guaranteed by the data model, it is recommended to
     * use {@link #intersectAll(Select)}, instead, if the underlying RDBMS
     * supports it. Apply the <code>INTERSECT</code> set operation.
     */
    @Override
    @NotNull @CheckReturnValue
    @Support({ CLICKHOUSE, CUBRID, DERBY, DUCKDB, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    SelectOrderByStep<R> intersectDistinct(Select<? extends R> select);

    /**
     * Apply the <code>INTERSECT ALL</code> set operation.
     */
    @Override
    @NotNull @CheckReturnValue
    @Support({ CLICKHOUSE, CUBRID, DERBY, DUCKDB, HSQLDB, MARIADB, MYSQL, POSTGRES, TRINO, YUGABYTEDB })
    SelectOrderByStep<R> intersectAll(Select<? extends R> select);
}
