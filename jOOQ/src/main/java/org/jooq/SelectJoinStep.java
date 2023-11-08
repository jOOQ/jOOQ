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
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
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
// ...
// ...
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.DERBY;
// ...
import static org.jooq.SQLDialect.FIREBIRD;
// ...
import static org.jooq.SQLDialect.H2;
// ...
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.IGNITE;
// ...
// ...
import static org.jooq.SQLDialect.MARIADB;
// ...
import static org.jooq.SQLDialect.MYSQL;
// ...
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
// ...
import static org.jooq.SQLDialect.TRINO;
// ...
import static org.jooq.SQLDialect.YUGABYTEDB;

import org.jooq.impl.DSL;
import org.jooq.impl.QOM.JoinHint;

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
public interface SelectJoinStep<R extends Record> extends SelectWhereStep<R> {

    /**
     * Convenience method to join a table to the last table added to the
     * <code>FROM</code> clause using {@link Table#join(TableLike, JoinType)}
     * <p>
     * Depending on the <code>JoinType</code>, a subsequent
     * {@link SelectOnStep#on(Condition)} or
     * {@link SelectOnStep#using(Field...)} clause is required. If it is
     * required but omitted, the JOIN clause will be ignored
     */
    @NotNull @CheckReturnValue
    @Support
    SelectOptionalOnStep<R> join(TableLike<?> table, JoinType type);

    /**
     * Convenience method to join a table to the last table added to the
     * <code>FROM</code> clause using {@link Table#join(TableLike, JoinType, JoinHint)}
     * <p>
     * Depending on the <code>JoinType</code>, a subsequent
     * {@link SelectOnStep#on(Condition)} or
     * {@link SelectOnStep#using(Field...)} clause is required. If it is
     * required but omitted, the JOIN clause will be ignored
     */
    @NotNull @CheckReturnValue
    @Support
    SelectOptionalOnStep<R> join(TableLike<?> table, JoinType type, JoinHint hint);

    /**
     * Convenience method to <code>INNER JOIN</code> a table to the last table
     * added to the <code>FROM</code> clause using {@link Table#join(TableLike)}.
     * <p>
     * A synonym for {@link #innerJoin(TableLike)}.
     *
     * @see Table#join(TableLike)
     * @see #innerJoin(TableLike)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectOnStep<R> join(TableLike<?> table);

    /**
     * Convenience method to <code>INNER JOIN</code> a table to the last table
     * added to the <code>FROM</code> clause using {@link Table#hashJoin(TableLike)}.
     * <p>
     * A synonym for {@link #innerHashJoin(TableLike)}.
     *
     * @see Table#hashJoin(TableLike)
     * @see #innerHashJoin(TableLike)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectOnStep<R> hashJoin(TableLike<?> table);

    /**
     * Convenience method to <code>INNER JOIN</code> a table to the last table
     * added to the <code>FROM</code> clause using {@link Table#loopJoin(TableLike)}.
     * <p>
     * A synonym for {@link #innerLoopJoin(TableLike)}.
     *
     * @see Table#loopJoin(TableLike)
     * @see #innerLoopJoin(TableLike)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectOnStep<R> loopJoin(TableLike<?> table);

    /**
     * Convenience method to <code>INNER JOIN</code> a table to the last table
     * added to the <code>FROM</code> clause using {@link Table#mergeJoin(TableLike)}.
     * <p>
     * A synonym for {@link #innerMergeJoin(TableLike)}.
     *
     * @see Table#mergeJoin(TableLike)
     * @see #innerMergeJoin(TableLike)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectOnStep<R> mergeJoin(TableLike<?> table);

    /**
     * Convenience method to <code>INNER JOIN</code> a path to the last table
     * added to the <code>FROM</code> clause using {@link Table#join(Path)}.
     * <p>
     * A synonym for {@link #innerJoin(Path)}.
     *
     * @see Table#join(Path)
     * @see #innerJoin(Path)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectOptionalOnStep<R> join(Path<?> path);

    /**
     * Convenience method to <code>INNER JOIN</code> a path to the last table
     * added to the <code>FROM</code> clause using {@link Table#hashJoin(Path)}.
     * <p>
     * A synonym for {@link #innerHashJoin(Path)}.
     *
     * @see Table#hashJoin(Path)
     * @see #innerHashJoin(Path)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectOptionalOnStep<R> hashJoin(Path<?> path);

    /**
     * Convenience method to <code>INNER JOIN</code> a path to the last table
     * added to the <code>FROM</code> clause using {@link Table#loopJoin(Path)}.
     * <p>
     * A synonym for {@link #innerLoopJoin(Path)}.
     *
     * @see Table#loopJoin(Path)
     * @see #innerLoopJoin(Path)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectOptionalOnStep<R> loopJoin(Path<?> path);

    /**
     * Convenience method to <code>INNER JOIN</code> a path to the last table
     * added to the <code>FROM</code> clause using {@link Table#mergeJoin(Path)}.
     * <p>
     * A synonym for {@link #innerMergeJoin(Path)}.
     *
     * @see Table#mergeJoin(Path)
     * @see #innerMergeJoin(Path)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectOptionalOnStep<R> mergeJoin(Path<?> path);

    /**
     * Convenience method to <code>INNER JOIN</code> a table to the last table
     * added to the <code>FROM</code> clause using {@link Table#join(String)}.
     * <p>
     * A synonym for {@link #innerJoin(String)}.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(SQL)
     * @see Table#join(SQL)
     * @see #innerJoin(SQL)
     * @see SQL
     */
    @NotNull @CheckReturnValue
    @Support
    @PlainSQL
    SelectOnStep<R> join(SQL sql);

    /**
     * Convenience method to <code>INNER JOIN</code> a table to the last table
     * added to the <code>FROM</code> clause using {@link Table#join(String)}.
     * <p>
     * A synonym for {@link #innerJoin(String)}.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String)
     * @see Table#join(String)
     * @see #innerJoin(String)
     * @see SQL
     */
    @NotNull @CheckReturnValue
    @Support
    @PlainSQL
    SelectOnStep<R> join(String sql);

    /**
     * Convenience method to <code>INNER JOIN</code> a table to the last table
     * added to the <code>FROM</code> clause using
     * {@link Table#join(String, Object...)}.
     * <p>
     * A synonym for {@link #innerJoin(String, Object...)}.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, Object...)
     * @see DSL#sql(String, Object...)
     * @see Table#join(String, Object...)
     * @see #innerJoin(String, Object...)
     * @see SQL
     */
    @NotNull @CheckReturnValue
    @Support
    @PlainSQL
    SelectOnStep<R> join(String sql, Object... bindings);

    /**
     * Convenience method to <code>INNER JOIN</code> a table to the last table
     * added to the <code>FROM</code> clause using
     * {@link Table#join(String, QueryPart...)}.
     * <p>
     * A synonym for {@link #innerJoin(String, QueryPart...)}.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, QueryPart...)
     * @see DSL#sql(String, QueryPart...)
     * @see Table#join(String, QueryPart...)
     * @see #innerJoin(String, QueryPart...)
     * @see SQL
     */
    @NotNull @CheckReturnValue
    @Support
    @PlainSQL
    SelectOnStep<R> join(String sql, QueryPart... parts);

    /**
     * Convenience method to <code>INNER JOIN</code> a table to the last table
     * added to the <code>FROM</code> clause using
     * {@link Table#join(Name)}.
     * <p>
     * A synonym for {@link #innerJoin(Name)}.
     *
     * @see DSL#table(Name)
     * @see Table#join(Name)
     * @see #innerJoin(Name)
     */
    @NotNull @CheckReturnValue
    @Support
    @PlainSQL
    SelectOnStep<R> join(Name name);

    /**
     * Convenience method to <code>INNER JOIN</code> a table to the last table
     * added to the <code>FROM</code> clause using {@link Table#join(TableLike)}.
     *
     * @see Table#innerJoin(TableLike)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectOnStep<R> innerJoin(TableLike<?> table);

    /**
     * Convenience method to <code>INNER JOIN</code> a table to the last table
     * added to the <code>FROM</code> clause using {@link Table#innerHashJoin(TableLike)}.
     *
     * @see Table#innerHashJoin(TableLike)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectOnStep<R> innerHashJoin(TableLike<?> table);

    /**
     * Convenience method to <code>INNER JOIN</code> a table to the last table
     * added to the <code>FROM</code> clause using {@link Table#innerLoopJoin(TableLike)}.
     *
     * @see Table#innerLoopJoin(TableLike)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectOnStep<R> innerLoopJoin(TableLike<?> table);

    /**
     * Convenience method to <code>INNER JOIN</code> a table to the last table
     * added to the <code>FROM</code> clause using {@link Table#innerMergeJoin(TableLike)}.
     *
     * @see Table#innerMergeJoin(TableLike)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectOnStep<R> innerMergeJoin(TableLike<?> table);

    /**
     * Convenience method to <code>INNER JOIN</code> a path to the last table
     * added to the <code>FROM</code> clause using {@link Table#join(Path)}.
     *
     * @see Table#innerJoin(Path)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectOptionalOnStep<R> innerJoin(Path<?> path);

    /**
     * Convenience method to <code>INNER JOIN</code> a path to the last table
     * added to the <code>FROM</code> clause using {@link Table#innerHashJoin(Path)}.
     *
     * @see Table#innerHashJoin(Path)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectOptionalOnStep<R> innerHashJoin(Path<?> path);

    /**
     * Convenience method to <code>INNER JOIN</code> a path to the last table
     * added to the <code>FROM</code> clause using {@link Table#innerLoopJoin(Path)}.
     *
     * @see Table#innerLoopJoin(Path)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectOptionalOnStep<R> innerLoopJoin(Path<?> path);

    /**
     * Convenience method to <code>INNER JOIN</code> a path to the last table
     * added to the <code>FROM</code> clause using {@link Table#innerMergeJoin(Path)}.
     *
     * @see Table#innerMergeJoin(Path)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectOptionalOnStep<R> innerMergeJoin(Path<?> path);

    /**
     * Convenience method to <code>INNER JOIN</code> a table to the last table
     * added to the <code>FROM</code> clause using {@link Table#join(String)}.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(SQL)
     * @see Table#innerJoin(SQL)
     * @see SQL
     */
    @NotNull @CheckReturnValue
    @Support
    @PlainSQL
    SelectOnStep<R> innerJoin(SQL sql);

    /**
     * Convenience method to <code>INNER JOIN</code> a table to the last table
     * added to the <code>FROM</code> clause using {@link Table#join(String)}.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String)
     * @see Table#innerJoin(String)
     * @see SQL
     */
    @NotNull @CheckReturnValue
    @Support
    @PlainSQL
    SelectOnStep<R> innerJoin(String sql);

    /**
     * Convenience method to <code>INNER JOIN</code> a table to the last table
     * added to the <code>FROM</code> clause using
     * {@link Table#join(String, Object...)}.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, Object...)
     * @see DSL#sql(String, Object...)
     * @see Table#innerJoin(String, Object...)
     * @see SQL
     */
    @NotNull @CheckReturnValue
    @Support
    @PlainSQL
    SelectOnStep<R> innerJoin(String sql, Object... bindings);

    /**
     * Convenience method to <code>INNER JOIN</code> a table to the last table
     * added to the <code>FROM</code> clause using
     * {@link Table#join(String, QueryPart...)}.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, QueryPart...)
     * @see DSL#sql(String, QueryPart...)
     * @see Table#innerJoin(String, QueryPart...)
     * @see SQL
     */
    @NotNull @CheckReturnValue
    @Support
    @PlainSQL
    SelectOnStep<R> innerJoin(String sql, QueryPart... parts);

    /**
     * Convenience method to <code>INNER JOIN</code> a table to the last table
     * added to the <code>FROM</code> clause using
     * {@link Table#join(Name)}.
     *
     * @see DSL#table(Name)
     * @see Table#innerJoin(Name)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectOnStep<R> innerJoin(Name name);

    /**
     * Convenience method to <code>CROSS JOIN</code> a table to the last table
     * added to the <code>FROM</code> clause using
     * {@link Table#crossJoin(TableLike)}
     * <p>
     * If this syntax is unavailable, it is emulated with a regular
     * <code>INNER JOIN</code>. The following two constructs are equivalent:
     * <pre><code>
     * A cross join B
     * A join B on 1 = 1
     * </code></pre>
     *
     * @see Table#crossJoin(TableLike)
     */
    @NotNull @CheckReturnValue
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    SelectJoinStep<R> crossJoin(TableLike<?> table);

    /**
     * Convenience method to <code>CROSS JOIN</code> a table to the last table
     * added to the <code>FROM</code> clause using
     * {@link Table#crossJoin(String)}
     * <p>
     * If this syntax is unavailable, it is emulated with a regular
     * <code>INNER JOIN</code>. The following two constructs are equivalent:
     * <pre><code>
     * A cross join B
     * A join B on 1 = 1
     * </code></pre>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(SQL)
     * @see Table#crossJoin(SQL)
     * @see SQL
     */
    @NotNull @CheckReturnValue
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    @PlainSQL
    SelectJoinStep<R> crossJoin(SQL sql);

    /**
     * Convenience method to <code>CROSS JOIN</code> a table to the last table
     * added to the <code>FROM</code> clause using
     * {@link Table#crossJoin(String)}
     * <p>
     * If this syntax is unavailable, it is emulated with a regular
     * <code>INNER JOIN</code>. The following two constructs are equivalent:
     * <pre><code>
     * A cross join B
     * A join B on 1 = 1
     * </code></pre>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String)
     * @see Table#crossJoin(String)
     * @see SQL
     */
    @NotNull @CheckReturnValue
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    @PlainSQL
    SelectJoinStep<R> crossJoin(String sql);

    /**
     * Convenience method to <code>CROSS JOIN</code> a table to the last table
     * added to the <code>FROM</code> clause using
     * {@link Table#crossJoin(String, Object...)}
     * <p>
     * If this syntax is unavailable, it is emulated with a regular
     * <code>INNER JOIN</code>. The following two constructs are equivalent:
     * <pre><code>
     * A cross join B
     * A join B on 1 = 1
     * </code></pre>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, Object...)
     * @see DSL#sql(String, Object...)
     * @see Table#crossJoin(String, Object...)
     * @see SQL
     */
    @NotNull @CheckReturnValue
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    @PlainSQL
    SelectJoinStep<R> crossJoin(String sql, Object... bindings);

    /**
     * Convenience method to <code>CROSS JOIN</code> a table to the last table
     * added to the <code>FROM</code> clause using
     * {@link Table#crossJoin(String, QueryPart...)}
     * <p>
     * If this syntax is unavailable, it is emulated with a regular
     * <code>INNER JOIN</code>. The following two constructs are equivalent:
     * <pre><code>
     * A cross join B
     * A join B on 1 = 1
     * </code></pre>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, QueryPart...)
     * @see DSL#sql(String, QueryPart...)
     * @see Table#crossJoin(String, QueryPart...)
     * @see SQL
     */
    @NotNull @CheckReturnValue
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    @PlainSQL
    SelectJoinStep<R> crossJoin(String sql, QueryPart... parts);

    /**
     * Convenience method to <code>CROSS JOIN</code> a table to the last table
     * added to the <code>FROM</code> clause using
     * {@link Table#crossJoin(Name)}
     * <p>
     * If this syntax is unavailable, it is emulated with a regular
     * <code>INNER JOIN</code>. The following two constructs are equivalent:
     * <pre><code>
     * A cross join B
     * A join B on 1 = 1
     * </code></pre>
     *
     * @see DSL#table(Name)
     * @see Table#crossJoin(Name)
     */
    @NotNull @CheckReturnValue
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    SelectJoinStep<R> crossJoin(Name name);

    /**
     * Convenience method to <code>LEFT OUTER JOIN</code> a table to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#leftOuterJoin(TableLike)}.
     * <p>
     * A synonym for {@link #leftOuterJoin(TableLike)}.
     *
     * @see Table#leftOuterJoin(TableLike)
     * @see #leftOuterJoin(TableLike)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectJoinPartitionByStep<R> leftJoin(TableLike<?> table);

    /**
     * Convenience method to <code>LEFT OUTER JOIN</code> a table to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#leftOuterHashJoin(TableLike)}.
     * <p>
     * A synonym for {@link #leftOuterHashJoin(TableLike)}.
     *
     * @see Table#leftOuterHashJoin(TableLike)
     * @see #leftOuterHashJoin(TableLike)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectJoinPartitionByStep<R> leftHashJoin(TableLike<?> table);

    /**
     * Convenience method to <code>LEFT OUTER JOIN</code> a table to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#leftOuterLoopJoin(TableLike)}.
     * <p>
     * A synonym for {@link #leftOuterLoopJoin(TableLike)}.
     *
     * @see Table#leftOuterLoopJoin(TableLike)
     * @see #leftOuterLoopJoin(TableLike)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectJoinPartitionByStep<R> leftLoopJoin(TableLike<?> table);

    /**
     * Convenience method to <code>LEFT OUTER JOIN</code> a table to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#leftOuterMergeJoin(TableLike)}.
     * <p>
     * A synonym for {@link #leftOuterMergeJoin(TableLike)}.
     *
     * @see Table#leftOuterMergeJoin(TableLike)
     * @see #leftOuterMergeJoin(TableLike)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectJoinPartitionByStep<R> leftMergeJoin(TableLike<?> table);

    /**
     * Convenience method to <code>LEFT OUTER JOIN</code> a path to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#leftOuterJoin(Path)}.
     * <p>
     * A synonym for {@link #leftOuterJoin(Path)}.
     *
     * @see Table#leftOuterJoin(Path)
     * @see #leftOuterJoin(Path)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectOptionalOnStep<R> leftJoin(Path<?> table);

    /**
     * Convenience method to <code>LEFT OUTER JOIN</code> a path to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#leftOuterHashJoin(Path)}.
     * <p>
     * A synonym for {@link #leftOuterHashJoin(Path)}.
     *
     * @see Table#leftOuterHashJoin(Path)
     * @see #leftOuterHashJoin(Path)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectOptionalOnStep<R> leftHashJoin(Path<?> table);

    /**
     * Convenience method to <code>LEFT OUTER JOIN</code> a path to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#leftOuterLoopJoin(Path)}.
     * <p>
     * A synonym for {@link #leftOuterLoopJoin(Path)}.
     *
     * @see Table#leftOuterLoopJoin(Path)
     * @see #leftOuterLoopJoin(Path)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectOptionalOnStep<R> leftLoopJoin(Path<?> table);

    /**
     * Convenience method to <code>LEFT OUTER JOIN</code> a path to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#leftOuterMergeJoin(Path)}.
     * <p>
     * A synonym for {@link #leftOuterMergeJoin(Path)}.
     *
     * @see Table#leftOuterMergeJoin(Path)
     * @see #leftOuterMergeJoin(Path)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectOptionalOnStep<R> leftMergeJoin(Path<?> table);

    /**
     * Convenience method to <code>LEFT OUTER JOIN</code> a table to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#leftOuterJoin(String)}.
     * <p>
     * A synonym for {@link #leftOuterJoin(String)}.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(SQL)
     * @see Table#leftOuterJoin(SQL)
     * @see #leftOuterJoin(SQL)
     * @see SQL
     */
    @NotNull @CheckReturnValue
    @Support
    @PlainSQL
    SelectJoinPartitionByStep<R> leftJoin(SQL sql);

    /**
     * Convenience method to <code>LEFT OUTER JOIN</code> a table to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#leftOuterJoin(String)}.
     * <p>
     * A synonym for {@link #leftOuterJoin(String)}.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String)
     * @see Table#leftOuterJoin(String)
     * @see #leftOuterJoin(String)
     * @see SQL
     */
    @NotNull @CheckReturnValue
    @Support
    @PlainSQL
    SelectJoinPartitionByStep<R> leftJoin(String sql);

    /**
     * Convenience method to <code>LEFT OUTER JOIN</code> a table to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#leftOuterJoin(String, Object...)}.
     * <p>
     * A synonym for {@link #leftOuterJoin(String, Object...)}.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, Object...)
     * @see DSL#sql(String, Object...)
     * @see Table#leftOuterJoin(String, Object...)
     * @see #leftOuterJoin(String, Object...)
     * @see SQL
     */
    @NotNull @CheckReturnValue
    @Support
    @PlainSQL
    SelectJoinPartitionByStep<R> leftJoin(String sql, Object... bindings);

    /**
     * Convenience method to <code>LEFT OUTER JOIN</code> a table to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#leftOuterJoin(String, QueryPart...)}.
     * <p>
     * A synonym for {@link #leftOuterJoin(String, QueryPart...)}.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, QueryPart...)
     * @see DSL#sql(String, QueryPart...)
     * @see Table#leftOuterJoin(String, QueryPart...)
     * @see #leftOuterJoin(String, QueryPart...)
     * @see SQL
     */
    @NotNull @CheckReturnValue
    @Support
    @PlainSQL
    SelectJoinPartitionByStep<R> leftJoin(String sql, QueryPart... parts);

    /**
     * Convenience method to <code>LEFT OUTER JOIN</code> a table to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#leftOuterJoin(Name)}.
     * <p>
     * A synonym for {@link #leftOuterJoin(Name)}.
     *
     * @see DSL#table(Name)
     * @see Table#leftOuterJoin(Name)
     * @see #leftOuterJoin(Name)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectJoinPartitionByStep<R> leftJoin(Name name);

    /**
     * Convenience method to <code>LEFT OUTER JOIN</code> a table to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#leftOuterJoin(TableLike)}
     *
     * @see Table#leftOuterJoin(TableLike)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectJoinPartitionByStep<R> leftOuterJoin(TableLike<?> table);

    /**
     * Convenience method to <code>LEFT OUTER JOIN</code> a table to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#leftOuterHashJoin(TableLike)}
     *
     * @see Table#leftOuterHashJoin(TableLike)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectJoinPartitionByStep<R> leftOuterHashJoin(TableLike<?> table);

    /**
     * Convenience method to <code>LEFT OUTER JOIN</code> a table to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#leftOuterLoopJoin(TableLike)}
     *
     * @see Table#leftOuterLoopJoin(TableLike)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectJoinPartitionByStep<R> leftOuterLoopJoin(TableLike<?> table);

    /**
     * Convenience method to <code>LEFT OUTER JOIN</code> a table to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#leftOuterMergeJoin(TableLike)}
     *
     * @see Table#leftOuterMergeJoin(TableLike)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectJoinPartitionByStep<R> leftOuterMergeJoin(TableLike<?> table);

    /**
     * Convenience method to <code>LEFT OUTER JOIN</code> a path to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#leftOuterJoin(Path)}
     *
     * @see Table#leftOuterJoin(Path)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectOptionalOnStep<R> leftOuterJoin(Path<?> path);

    /**
     * Convenience method to <code>LEFT OUTER JOIN</code> a path to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#leftOuterHashJoin(Path)}
     *
     * @see Table#leftOuterHashJoin(Path)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectOptionalOnStep<R> leftOuterHashJoin(Path<?> path);

    /**
     * Convenience method to <code>LEFT OUTER JOIN</code> a path to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#leftOuterLoopJoin(Path)}
     *
     * @see Table#leftOuterLoopJoin(Path)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectOptionalOnStep<R> leftOuterLoopJoin(Path<?> path);

    /**
     * Convenience method to <code>LEFT OUTER JOIN</code> a path to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#leftOuterMergeJoin(Path)}
     *
     * @see Table#leftOuterMergeJoin(Path)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectOptionalOnStep<R> leftOuterMergeJoin(Path<?> path);

    /**
     * Convenience method to <code>LEFT OUTER JOIN</code> a table to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#leftOuterJoin(String)}
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(SQL)
     * @see Table#leftOuterJoin(SQL)
     * @see SQL
     */
    @NotNull @CheckReturnValue
    @Support
    @PlainSQL
    SelectJoinPartitionByStep<R> leftOuterJoin(SQL sql);

    /**
     * Convenience method to <code>LEFT OUTER JOIN</code> a table to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#leftOuterJoin(String)}
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String)
     * @see Table#leftOuterJoin(String)
     * @see SQL
     */
    @NotNull @CheckReturnValue
    @Support
    @PlainSQL
    SelectJoinPartitionByStep<R> leftOuterJoin(String sql);

    /**
     * Convenience method to <code>LEFT OUTER JOIN</code> a table to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#leftOuterJoin(String, Object...)}
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, Object...)
     * @see DSL#sql(String, Object...)
     * @see Table#leftOuterJoin(String, Object...)
     * @see SQL
     */
    @NotNull @CheckReturnValue
    @Support
    @PlainSQL
    SelectJoinPartitionByStep<R> leftOuterJoin(String sql, Object... bindings);

    /**
     * Convenience method to <code>LEFT OUTER JOIN</code> a table to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#leftOuterJoin(String, QueryPart...)}
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, QueryPart...)
     * @see DSL#sql(String, QueryPart...)
     * @see Table#leftOuterJoin(String, QueryPart...)
     * @see SQL
     */
    @NotNull @CheckReturnValue
    @Support
    @PlainSQL
    SelectJoinPartitionByStep<R> leftOuterJoin(String sql, QueryPart... parts);

    /**
     * Convenience method to <code>LEFT OUTER JOIN</code> a table to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#leftOuterJoin(Name)}
     *
     * @see DSL#table(Name)
     * @see Table#leftOuterJoin(Name)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectJoinPartitionByStep<R> leftOuterJoin(Name name);

    /**
     * Convenience method to <code>RIGHT OUTER JOIN</code> a table to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#rightOuterJoin(TableLike)}.
     * <p>
     * A synonym for {@link #rightOuterJoin(TableLike)}.
     *
     * @see Table#rightOuterJoin(TableLike)
     * @see #rightOuterJoin(TableLike)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectJoinPartitionByStep<R> rightJoin(TableLike<?> table);

    /**
     * Convenience method to <code>RIGHT OUTER JOIN</code> a table to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#rightOuterHashJoin(TableLike)}.
     * <p>
     * A synonym for {@link #rightOuterHashJoin(TableLike)}.
     *
     * @see Table#rightOuterHashJoin(TableLike)
     * @see #rightOuterHashJoin(TableLike)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectJoinPartitionByStep<R> rightHashJoin(TableLike<?> table);

    /**
     * Convenience method to <code>RIGHT OUTER JOIN</code> a table to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#rightOuterLoopJoin(TableLike)}.
     * <p>
     * A synonym for {@link #rightOuterLoopJoin(TableLike)}.
     *
     * @see Table#rightOuterLoopJoin(TableLike)
     * @see #rightOuterLoopJoin(TableLike)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectJoinPartitionByStep<R> rightLoopJoin(TableLike<?> table);

    /**
     * Convenience method to <code>RIGHT OUTER JOIN</code> a table to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#rightOuterMergeJoin(TableLike)}.
     * <p>
     * A synonym for {@link #rightOuterMergeJoin(TableLike)}.
     *
     * @see Table#rightOuterMergeJoin(TableLike)
     * @see #rightOuterMergeJoin(TableLike)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectJoinPartitionByStep<R> rightMergeJoin(TableLike<?> table);

    /**
     * Convenience method to <code>RIGHT OUTER JOIN</code> a path to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#rightOuterJoin(Path)}.
     * <p>
     * A synonym for {@link #rightOuterJoin(Path)}.
     *
     * @see Table#rightOuterJoin(Path)
     * @see #rightOuterJoin(Path)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectOptionalOnStep<R> rightJoin(Path<?> path);

    /**
     * Convenience method to <code>RIGHT OUTER JOIN</code> a path to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#rightOuterHashJoin(Path)}.
     * <p>
     * A synonym for {@link #rightOuterHashJoin(Path)}.
     *
     * @see Table#rightOuterHashJoin(Path)
     * @see #rightOuterHashJoin(Path)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectOptionalOnStep<R> rightHashJoin(Path<?> path);

    /**
     * Convenience method to <code>RIGHT OUTER JOIN</code> a path to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#rightOuterLoopJoin(Path)}.
     * <p>
     * A synonym for {@link #rightOuterLoopJoin(Path)}.
     *
     * @see Table#rightOuterLoopJoin(Path)
     * @see #rightOuterLoopJoin(Path)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectOptionalOnStep<R> rightLoopJoin(Path<?> path);

    /**
     * Convenience method to <code>RIGHT OUTER JOIN</code> a path to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#rightOuterMergeJoin(Path)}.
     * <p>
     * A synonym for {@link #rightOuterMergeJoin(Path)}.
     *
     * @see Table#rightOuterMergeJoin(Path)
     * @see #rightOuterMergeJoin(Path)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectOptionalOnStep<R> rightMergeJoin(Path<?> path);

    /**
     * Convenience method to <code>RIGHT OUTER JOIN</code> a table to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#rightOuterJoin(String)}.
     * <p>
     * A synonym for {@link #rightOuterJoin(String)}.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(SQL)
     * @see Table#rightOuterJoin(SQL)
     * @see #rightOuterJoin(SQL)
     * @see SQL
     */
    @NotNull @CheckReturnValue
    @Support
    @PlainSQL
    SelectJoinPartitionByStep<R> rightJoin(SQL sql);

    /**
     * Convenience method to <code>RIGHT OUTER JOIN</code> a table to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#rightOuterJoin(String)}.
     * <p>
     * A synonym for {@link #rightOuterJoin(String)}.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String)
     * @see Table#rightOuterJoin(String)
     * @see #rightOuterJoin(String)
     * @see SQL
     */
    @NotNull @CheckReturnValue
    @Support
    @PlainSQL
    SelectJoinPartitionByStep<R> rightJoin(String sql);

    /**
     * Convenience method to <code>RIGHT OUTER JOIN</code> a table to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#rightOuterJoin(String, Object...)}.
     * <p>
     * A synonym for {@link #rightOuterJoin(String, Object...)}.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, Object...)
     * @see DSL#sql(String, Object...)
     * @see Table#rightOuterJoin(String, Object...)
     * @see #rightOuterJoin(String, Object...)
     * @see SQL
     */
    @NotNull @CheckReturnValue
    @Support
    @PlainSQL
    SelectJoinPartitionByStep<R> rightJoin(String sql, Object... bindings);

    /**
     * Convenience method to <code>RIGHT OUTER JOIN</code> a table to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#rightOuterJoin(String, QueryPart...)}.
     * <p>
     * A synonym for {@link #rightOuterJoin(String, QueryPart...)}.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, QueryPart...)
     * @see DSL#sql(String, QueryPart...)
     * @see Table#rightOuterJoin(String, QueryPart...)
     * @see #rightOuterJoin(String, QueryPart...)
     * @see SQL
     */
    @NotNull @CheckReturnValue
    @Support
    @PlainSQL
    SelectJoinPartitionByStep<R> rightJoin(String sql, QueryPart... parts);

    /**
     * Convenience method to <code>RIGHT OUTER JOIN</code> a table to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#rightOuterJoin(Name)}.
     * <p>
     * A synonym for {@link #rightOuterJoin(Name)}.
     *
     * @see DSL#table(Name)
     * @see Table#rightOuterJoin(Name)
     * @see #rightOuterJoin(Name)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectJoinPartitionByStep<R> rightJoin(Name name);

    /**
     * Convenience method to <code>RIGHT OUTER JOIN</code> a table to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#rightOuterJoin(TableLike)}
     *
     * @see Table#rightOuterJoin(TableLike)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectJoinPartitionByStep<R> rightOuterJoin(TableLike<?> table);

    /**
     * Convenience method to <code>RIGHT OUTER JOIN</code> a table to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#rightOuterHashJoin(TableLike)}
     *
     * @see Table#rightOuterHashJoin(TableLike)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectJoinPartitionByStep<R> rightOuterHashJoin(TableLike<?> table);

    /**
     * Convenience method to <code>RIGHT OUTER JOIN</code> a table to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#rightOuterLoopJoin(TableLike)}
     *
     * @see Table#rightOuterLoopJoin(TableLike)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectJoinPartitionByStep<R> rightOuterLoopJoin(TableLike<?> table);

    /**
     * Convenience method to <code>RIGHT OUTER JOIN</code> a table to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#rightOuterMergeJoin(TableLike)}
     *
     * @see Table#rightOuterMergeJoin(TableLike)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectJoinPartitionByStep<R> rightOuterMergeJoin(TableLike<?> table);

    /**
     * Convenience method to <code>RIGHT OUTER JOIN</code> a path to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#rightOuterJoin(Path)}
     *
     * @see Table#rightOuterJoin(Path)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectOptionalOnStep<R> rightOuterJoin(Path<?> path);

    /**
     * Convenience method to <code>RIGHT OUTER JOIN</code> a path to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#rightOuterHashJoin(Path)}
     *
     * @see Table#rightOuterHashJoin(Path)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectOptionalOnStep<R> rightOuterHashJoin(Path<?> path);

    /**
     * Convenience method to <code>RIGHT OUTER JOIN</code> a path to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#rightOuterLoopJoin(Path)}
     *
     * @see Table#rightOuterLoopJoin(Path)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectOptionalOnStep<R> rightOuterLoopJoin(Path<?> path);

    /**
     * Convenience method to <code>RIGHT OUTER JOIN</code> a path to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#rightOuterMergeJoin(Path)}
     *
     * @see Table#rightOuterMergeJoin(Path)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectOptionalOnStep<R> rightOuterMergeJoin(Path<?> path);

    /**
     * Convenience method to <code>RIGHT OUTER JOIN</code> a table to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#rightOuterJoin(String)}
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(SQL)
     * @see Table#rightOuterJoin(SQL)
     * @see SQL
     */
    @NotNull @CheckReturnValue
    @Support
    @PlainSQL
    SelectJoinPartitionByStep<R> rightOuterJoin(SQL sql);

    /**
     * Convenience method to <code>RIGHT OUTER JOIN</code> a table to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#rightOuterJoin(String)}
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String)
     * @see Table#rightOuterJoin(String)
     * @see SQL
     */
    @NotNull @CheckReturnValue
    @Support
    @PlainSQL
    SelectJoinPartitionByStep<R> rightOuterJoin(String sql);

    /**
     * Convenience method to <code>RIGHT OUTER JOIN</code> a table to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#rightOuterJoin(String, Object...)}
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, Object...)
     * @see DSL#sql(String, Object...)
     * @see Table#rightOuterJoin(String, Object...)
     * @see SQL
     */
    @NotNull @CheckReturnValue
    @Support
    @PlainSQL
    SelectJoinPartitionByStep<R> rightOuterJoin(String sql, Object... bindings);

    /**
     * Convenience method to <code>RIGHT OUTER JOIN</code> a table to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#rightOuterJoin(String, QueryPart...)}
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, QueryPart...)
     * @see DSL#sql(String, QueryPart...)
     * @see Table#rightOuterJoin(String, QueryPart...)
     * @see SQL
     */
    @NotNull @CheckReturnValue
    @Support
    @PlainSQL
    SelectJoinPartitionByStep<R> rightOuterJoin(String sql, QueryPart... parts);

    /**
     * Convenience method to <code>RIGHT OUTER JOIN</code> a table to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#rightOuterJoin(Name)}
     *
     * @see DSL#table(Name)
     * @see Table#rightOuterJoin(Name)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectJoinPartitionByStep<R> rightOuterJoin(Name name);

    /**
     * Convenience method to <code>FULL OUTER JOIN</code> a table to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#fullOuterJoin(TableLike)}.
     * <p>
     * A synonym for {@link #fullOuterJoin(TableLike)}.
     */
    @NotNull @CheckReturnValue
    @Support({ FIREBIRD, HSQLDB, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    SelectOnStep<R> fullJoin(TableLike<?> table);

    /**
     * Convenience method to <code>FULL OUTER JOIN</code> a table to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#fullOuterHashJoin(TableLike)}.
     * <p>
     * A synonym for {@link #fullOuterHashJoin(TableLike)}.
     */
    @NotNull @CheckReturnValue
    @Support({ FIREBIRD, HSQLDB, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    SelectOnStep<R> fullHashJoin(TableLike<?> table);

    /**
     * Convenience method to <code>FULL OUTER JOIN</code> a table to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#fullOuterLoopJoin(TableLike)}.
     * <p>
     * A synonym for {@link #fullOuterLoopJoin(TableLike)}.
     */
    @NotNull @CheckReturnValue
    @Support({ FIREBIRD, HSQLDB, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    SelectOnStep<R> fullLoopJoin(TableLike<?> table);

    /**
     * Convenience method to <code>FULL OUTER JOIN</code> a table to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#fullOuterMergeJoin(TableLike)}.
     * <p>
     * A synonym for {@link #fullOuterMergeJoin(TableLike)}.
     */
    @NotNull @CheckReturnValue
    @Support({ FIREBIRD, HSQLDB, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    SelectOnStep<R> fullMergeJoin(TableLike<?> table);

    /**
     * Convenience method to <code>FULL OUTER JOIN</code> a path to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#fullOuterJoin(Path)}.
     * <p>
     * A synonym for {@link #fullOuterJoin(Path)}.
     */
    @NotNull @CheckReturnValue
    @Support({ FIREBIRD, HSQLDB, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    SelectOptionalOnStep<R> fullJoin(Path<?> table);

    /**
     * Convenience method to <code>FULL OUTER JOIN</code> a path to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#fullOuterHashJoin(Path)}.
     * <p>
     * A synonym for {@link #fullOuterHashJoin(Path)}.
     */
    @NotNull @CheckReturnValue
    @Support({ FIREBIRD, HSQLDB, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    SelectOptionalOnStep<R> fullHashJoin(Path<?> table);

    /**
     * Convenience method to <code>FULL OUTER JOIN</code> a path to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#fullOuterLoopJoin(Path)}.
     * <p>
     * A synonym for {@link #fullOuterLoopJoin(Path)}.
     */
    @NotNull @CheckReturnValue
    @Support({ FIREBIRD, HSQLDB, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    SelectOptionalOnStep<R> fullLoopJoin(Path<?> table);

    /**
     * Convenience method to <code>FULL OUTER JOIN</code> a path to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#fullOuterMergeJoin(Path)}.
     * <p>
     * A synonym for {@link #fullOuterMergeJoin(Path)}.
     */
    @NotNull @CheckReturnValue
    @Support({ FIREBIRD, HSQLDB, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    SelectOptionalOnStep<R> fullMergeJoin(Path<?> table);

    /**
     * Convenience method to <code>FULL OUTER JOIN</code> a table to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#fullOuterJoin(String)}.
     * <p>
     * A synonym for {@link #fullOuterJoin(SQL)}.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     */
    @NotNull @CheckReturnValue
    @Support({ FIREBIRD, HSQLDB, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    @PlainSQL
    SelectOnStep<R> fullJoin(SQL sql);

    /**
     * Convenience method to <code>FULL OUTER JOIN</code> a table to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#fullOuterJoin(String)}.
     * <p>
     * A synonym for {@link #fullOuterJoin(String)}.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     */
    @NotNull @CheckReturnValue
    @Support({ FIREBIRD, HSQLDB, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    @PlainSQL
    SelectOnStep<R> fullJoin(String sql);

    /**
     * Convenience method to <code>FULL OUTER JOIN</code> a tableto the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#fullOuterJoin(String, Object...)}.
     * <p>
     * A synonym for {@link #fullOuterJoin(String, Object...)}.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     */
    @NotNull @CheckReturnValue
    @Support({ FIREBIRD, HSQLDB, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    @PlainSQL
    SelectOnStep<R> fullJoin(String sql, Object... bindings);

    /**
     * Convenience method to <code>FULL OUTER JOIN</code> a tableto the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#fullOuterJoin(String, QueryPart...)}.
     * <p>
     * A synonym for {@link #fullOuterJoin(String, QueryPart...)}.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     */
    @NotNull @CheckReturnValue
    @Support({ FIREBIRD, HSQLDB, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    @PlainSQL
    SelectOnStep<R> fullJoin(String sql, QueryPart... parts);

    /**
     * Convenience method to <code>FULL OUTER JOIN</code> a tableto the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#fullOuterJoin(Name)}.
     * <p>
     * A synonym for {@link #fullOuterJoin(Name)}.
     */
    @NotNull @CheckReturnValue
    @Support({ FIREBIRD, HSQLDB, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    SelectOnStep<R> fullJoin(Name name);

    /**
     * Convenience method to <code>FULL OUTER JOIN</code> a table to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#fullOuterJoin(TableLike)}
     *
     * @see Table#fullOuterJoin(TableLike)
     */
    @NotNull @CheckReturnValue
    @Support({ FIREBIRD, HSQLDB, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    SelectOnStep<R> fullOuterJoin(TableLike<?> table);

    /**
     * Convenience method to <code>FULL OUTER JOIN</code> a table to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#fullOuterHashJoin(TableLike)}
     *
     * @see Table#fullOuterHashJoin(TableLike)
     */
    @NotNull @CheckReturnValue
    @Support({ FIREBIRD, HSQLDB, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    SelectOnStep<R> fullOuterHashJoin(TableLike<?> table);

    /**
     * Convenience method to <code>FULL OUTER JOIN</code> a table to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#fullOuterLoopJoin(TableLike)}
     *
     * @see Table#fullOuterLoopJoin(TableLike)
     */
    @NotNull @CheckReturnValue
    @Support({ FIREBIRD, HSQLDB, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    SelectOnStep<R> fullOuterLoopJoin(TableLike<?> table);

    /**
     * Convenience method to <code>FULL OUTER JOIN</code> a table to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#fullOuterMergeJoin(TableLike)}
     *
     * @see Table#fullOuterMergeJoin(TableLike)
     */
    @NotNull @CheckReturnValue
    @Support({ FIREBIRD, HSQLDB, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    SelectOnStep<R> fullOuterMergeJoin(TableLike<?> table);

    /**
     * Convenience method to <code>FULL OUTER JOIN</code> a path to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#fullOuterJoin(Path)}
     *
     * @see Table#fullOuterJoin(Path)
     */
    @NotNull @CheckReturnValue
    @Support({ FIREBIRD, HSQLDB, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    SelectOptionalOnStep<R> fullOuterJoin(Path<?> table);

    /**
     * Convenience method to <code>FULL OUTER JOIN</code> a path to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#fullOuterHashJoin(Path)}
     *
     * @see Table#fullOuterHashJoin(Path)
     */
    @NotNull @CheckReturnValue
    @Support({ FIREBIRD, HSQLDB, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    SelectOptionalOnStep<R> fullOuterHashJoin(Path<?> table);

    /**
     * Convenience method to <code>FULL OUTER JOIN</code> a path to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#fullOuterLoopJoin(Path)}
     *
     * @see Table#fullOuterLoopJoin(Path)
     */
    @NotNull @CheckReturnValue
    @Support({ FIREBIRD, HSQLDB, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    SelectOptionalOnStep<R> fullOuterLoopJoin(Path<?> table);

    /**
     * Convenience method to <code>FULL OUTER JOIN</code> a path to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#fullOuterMergeJoin(Path)}
     *
     * @see Table#fullOuterMergeJoin(Path)
     */
    @NotNull @CheckReturnValue
    @Support({ FIREBIRD, HSQLDB, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    SelectOptionalOnStep<R> fullOuterMergeJoin(Path<?> table);

    /**
     * Convenience method to <code>FULL OUTER JOIN</code> a table to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#fullOuterJoin(String)}
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(SQL)
     * @see Table#fullOuterJoin(SQL)
     * @see SQL
     */
    @NotNull @CheckReturnValue
    @Support({ FIREBIRD, HSQLDB, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    @PlainSQL
    SelectOnStep<R> fullOuterJoin(SQL sql);

    /**
     * Convenience method to <code>FULL OUTER JOIN</code> a table to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#fullOuterJoin(String)}
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String)
     * @see Table#fullOuterJoin(String)
     * @see SQL
     */
    @NotNull @CheckReturnValue
    @Support({ FIREBIRD, HSQLDB, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    @PlainSQL
    SelectOnStep<R> fullOuterJoin(String sql);

    /**
     * Convenience method to <code>FULL OUTER JOIN</code> a tableto the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#fullOuterJoin(String, Object...)}
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, Object...)
     * @see DSL#sql(String, Object...)
     * @see Table#fullOuterJoin(String, Object...)
     * @see SQL
     */
    @NotNull @CheckReturnValue
    @Support({ FIREBIRD, HSQLDB, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    @PlainSQL
    SelectOnStep<R> fullOuterJoin(String sql, Object... bindings);

    /**
     * Convenience method to <code>FULL OUTER JOIN</code> a tableto the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#fullOuterJoin(String, QueryPart...)}
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, QueryPart...)
     * @see DSL#sql(String, QueryPart...)
     * @see Table#fullOuterJoin(String, QueryPart...)
     * @see SQL
     */
    @NotNull @CheckReturnValue
    @Support({ FIREBIRD, HSQLDB, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    @PlainSQL
    SelectOnStep<R> fullOuterJoin(String sql, QueryPart... parts);

    /**
     * Convenience method to <code>FULL OUTER JOIN</code> a tableto the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#fullOuterJoin(Name)}
     *
     * @see DSL#table(Name)
     * @see Table#fullOuterJoin(Name)
     */
    @NotNull @CheckReturnValue
    @Support({ FIREBIRD, HSQLDB, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    SelectOnStep<R> fullOuterJoin(Name name);

    /**
     * Convenience method to <code>NATURAL JOIN</code> a table to the last table
     * added to the <code>FROM</code> clause using
     * {@link Table#naturalJoin(TableLike)}
     * <p>
     * Natural joins are supported by most RDBMS. If they aren't supported, they
     * are emulated if jOOQ has enough information.
     *
     * @see Table#naturalJoin(TableLike)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectJoinStep<R> naturalJoin(TableLike<?> table);

    /**
     * Convenience method to <code>NATURAL JOIN</code> a table to the last table
     * added to the <code>FROM</code> clause using
     * {@link Table#naturalJoin(String)}
     * <p>
     * Natural joins are supported by most RDBMS. If they aren't supported, they
     * are emulated if jOOQ has enough information.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(SQL)
     * @see Table#naturalJoin(SQL)
     * @see SQL
     */
    @NotNull @CheckReturnValue
    @Support
    @PlainSQL
    SelectJoinStep<R> naturalJoin(SQL sql);

    /**
     * Convenience method to <code>NATURAL JOIN</code> a table to the last table
     * added to the <code>FROM</code> clause using
     * {@link Table#naturalJoin(String)}
     * <p>
     * Natural joins are supported by most RDBMS. If they aren't supported, they
     * are emulated if jOOQ has enough information.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String)
     * @see Table#naturalJoin(String)
     * @see SQL
     */
    @NotNull @CheckReturnValue
    @Support
    @PlainSQL
    SelectJoinStep<R> naturalJoin(String sql);

    /**
     * Convenience method to <code>NATURAL JOIN</code> a table to the last table
     * added to the <code>FROM</code> clause using
     * {@link Table#naturalJoin(String, Object...)}
     * <p>
     * Natural joins are supported by most RDBMS. If they aren't supported, they
     * are emulated if jOOQ has enough information.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, Object...)
     * @see DSL#sql(String, Object...)
     * @see Table#naturalJoin(String, Object...)
     * @see SQL
     */
    @NotNull @CheckReturnValue
    @Support
    @PlainSQL
    SelectJoinStep<R> naturalJoin(String sql, Object... bindings);

    /**
     * Convenience method to <code>NATURAL JOIN</code> a table to the last table
     * added to the <code>FROM</code> clause using
     * {@link Table#naturalJoin(String, QueryPart...)}
     * <p>
     * Natural joins are supported by most RDBMS. If they aren't supported, they
     * are emulated if jOOQ has enough information.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, QueryPart...)
     * @see DSL#sql(String, QueryPart...)
     * @see Table#naturalJoin(String, QueryPart...)
     * @see SQL
     */
    @NotNull @CheckReturnValue
    @Support
    @PlainSQL
    SelectJoinStep<R> naturalJoin(String sql, QueryPart... parts);

    /**
     * Convenience method to <code>NATURAL JOIN</code> a table to the last table
     * added to the <code>FROM</code> clause using
     * {@link Table#naturalJoin(Name)}
     * <p>
     * Natural joins are supported by most RDBMS. If they aren't supported, they
     * are emulated if jOOQ has enough information.
     *
     * @see DSL#table(Name)
     * @see Table#naturalJoin(Name)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectJoinStep<R> naturalJoin(Name name);

    /**
     * Convenience method to <code>NATURAL LEFT OUTER JOIN</code> a table to the
     * last table added to the <code>FROM</code> clause using
     * {@link Table#naturalLeftOuterJoin(TableLike)}
     * <p>
     * Natural joins are supported by most RDBMS. If they aren't supported, they
     * are emulated if jOOQ has enough information.
     *
     * @see Table#naturalLeftOuterJoin(TableLike)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectJoinStep<R> naturalLeftOuterJoin(TableLike<?> table);

    /**
     * Convenience method to <code>NATURAL LEFT OUTER JOIN</code> a table to the
     * last table added to the <code>FROM</code> clause using
     * {@link Table#naturalLeftOuterJoin(String)}
     * <p>
     * Natural joins are supported by most RDBMS. If they aren't supported, they
     * are emulated if jOOQ has enough information.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(SQL)
     * @see Table#naturalLeftOuterJoin(SQL)
     * @see SQL
     */
    @NotNull @CheckReturnValue
    @Support
    @PlainSQL
    SelectJoinStep<R> naturalLeftOuterJoin(SQL sql);

    /**
     * Convenience method to <code>NATURAL LEFT OUTER JOIN</code> a table to the
     * last table added to the <code>FROM</code> clause using
     * {@link Table#naturalLeftOuterJoin(String)}
     * <p>
     * Natural joins are supported by most RDBMS. If they aren't supported, they
     * are emulated if jOOQ has enough information.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String)
     * @see Table#naturalLeftOuterJoin(String)
     * @see SQL
     */
    @NotNull @CheckReturnValue
    @Support
    @PlainSQL
    SelectJoinStep<R> naturalLeftOuterJoin(String sql);

    /**
     * Convenience method to <code>NATURAL LEFT OUTER JOIN</code> a table to the
     * last table added to the <code>FROM</code> clause using
     * {@link Table#naturalLeftOuterJoin(String, Object...)}
     * <p>
     * Natural joins are supported by most RDBMS. If they aren't supported, they
     * are emulated if jOOQ has enough information.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, Object...)
     * @see DSL#sql(String, Object...)
     * @see Table#naturalLeftOuterJoin(String, Object...)
     * @see SQL
     */
    @NotNull @CheckReturnValue
    @Support
    @PlainSQL
    SelectJoinStep<R> naturalLeftOuterJoin(String sql, Object... bindings);

    /**
     * Convenience method to <code>NATURAL LEFT OUTER JOIN</code> a table to the
     * last table added to the <code>FROM</code> clause using
     * {@link Table#naturalLeftOuterJoin(String, QueryPart...)}
     * <p>
     * Natural joins are supported by most RDBMS. If they aren't supported, they
     * are emulated if jOOQ has enough information.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, QueryPart...)
     * @see DSL#sql(String, QueryPart...)
     * @see Table#naturalLeftOuterJoin(String, QueryPart...)
     * @see SQL
     */
    @NotNull @CheckReturnValue
    @Support
    @PlainSQL
    SelectJoinStep<R> naturalLeftOuterJoin(String sql, QueryPart... parts);

    /**
     * Convenience method to <code>NATURAL LEFT OUTER JOIN</code> a table to the
     * last table added to the <code>FROM</code> clause using
     * {@link Table#naturalLeftOuterJoin(Name)}
     * <p>
     * Natural joins are supported by most RDBMS. If they aren't supported, they
     * are emulated if jOOQ has enough information.
     *
     * @see DSL#table(Name)
     * @see Table#naturalLeftOuterJoin(Name)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectJoinStep<R> naturalLeftOuterJoin(Name name);

    /**
     * Convenience method to <code>NATURAL RIGHT OUTER JOIN</code> a table to
     * the last table added to the <code>FROM</code> clause using
     * {@link Table#naturalRightOuterJoin(TableLike)}
     * <p>
     * Natural joins are supported by most RDBMS. If they aren't supported, they
     * are emulated if jOOQ has enough information.
     *
     * @see Table#naturalRightOuterJoin(TableLike)
     */
    @NotNull @CheckReturnValue
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    SelectJoinStep<R> naturalRightOuterJoin(TableLike<?> table);

    /**
     * Convenience method to <code>NATURAL RIGHT OUTER JOIN</code> a table to
     * the last table added to the <code>FROM</code> clause using
     * {@link Table#naturalRightOuterJoin(String)}
     * <p>
     * Natural joins are supported by most RDBMS. If they aren't supported, they
     * are emulated if jOOQ has enough information.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(SQL)
     * @see Table#naturalRightOuterJoin(SQL)
     * @see SQL
     */
    @NotNull @CheckReturnValue
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    @PlainSQL
    SelectJoinStep<R> naturalRightOuterJoin(SQL sql);

    /**
     * Convenience method to <code>NATURAL RIGHT OUTER JOIN</code> a table to
     * the last table added to the <code>FROM</code> clause using
     * {@link Table#naturalRightOuterJoin(String)}
     * <p>
     * Natural joins are supported by most RDBMS. If they aren't supported, they
     * are emulated if jOOQ has enough information.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String)
     * @see Table#naturalRightOuterJoin(String)
     * @see SQL
     */
    @NotNull @CheckReturnValue
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    @PlainSQL
    SelectJoinStep<R> naturalRightOuterJoin(String sql);

    /**
     * Convenience method to <code>NATURAL RIGHT OUTER JOIN</code> a table to
     * the last table added to the <code>FROM</code> clause using
     * {@link Table#naturalRightOuterJoin(String, Object...)}
     * <p>
     * Natural joins are supported by most RDBMS. If they aren't supported, they
     * are emulated if jOOQ has enough information.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, Object...)
     * @see DSL#sql(String, Object...)
     * @see Table#naturalRightOuterJoin(String, Object...)
     * @see SQL
     */
    @NotNull @CheckReturnValue
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    @PlainSQL
    SelectJoinStep<R> naturalRightOuterJoin(String sql, Object... bindings);

    /**
     * Convenience method to <code>NATURAL RIGHT OUTER JOIN</code> a table to
     * the last table added to the <code>FROM</code> clause using
     * {@link Table#naturalRightOuterJoin(String, QueryPart...)}
     * <p>
     * Natural joins are supported by most RDBMS. If they aren't supported, they
     * are emulated if jOOQ has enough information.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, QueryPart...)
     * @see DSL#sql(String, QueryPart...)
     * @see Table#naturalRightOuterJoin(String, QueryPart...)
     * @see SQL
     */
    @NotNull @CheckReturnValue
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    @PlainSQL
    SelectJoinStep<R> naturalRightOuterJoin(String sql, QueryPart... parts);

    /**
     * Convenience method to <code>NATURAL RIGHT OUTER JOIN</code> a table to
     * the last table added to the <code>FROM</code> clause using
     * {@link Table#naturalRightOuterJoin(Name)}
     * <p>
     * Natural joins are supported by most RDBMS. If they aren't supported, they
     * are emulated if jOOQ has enough information.
     *
     * @see DSL#table(Name)
     * @see Table#naturalRightOuterJoin(Name)
     */
    @NotNull @CheckReturnValue
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    SelectJoinStep<R> naturalRightOuterJoin(Name name);

    /**
     * Convenience method to <code>NATURAL FULL OUTER JOIN</code> a table to
     * the last table added to the <code>FROM</code> clause using
     * {@link Table#naturalFullOuterJoin(TableLike)}
     * <p>
     * Natural joins are supported by most RDBMS. If they aren't supported, they
     * are emulated if jOOQ has enough information.
     *
     * @see Table#naturalFullOuterJoin(TableLike)
     */
    @NotNull @CheckReturnValue
    @Support({ FIREBIRD, HSQLDB, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    SelectJoinStep<R> naturalFullOuterJoin(TableLike<?> table);

    /**
     * Convenience method to <code>NATURAL FULL OUTER JOIN</code> a table to
     * the last table added to the <code>FROM</code> clause using
     * {@link Table#naturalFullOuterJoin(String)}
     * <p>
     * Natural joins are supported by most RDBMS. If they aren't supported, they
     * are emulated if jOOQ has enough information.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(SQL)
     * @see Table#naturalFullOuterJoin(SQL)
     * @see SQL
     */
    @NotNull @CheckReturnValue
    @Support({ FIREBIRD, HSQLDB, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    @PlainSQL
    SelectJoinStep<R> naturalFullOuterJoin(SQL sql);

    /**
     * Convenience method to <code>NATURAL FULL OUTER JOIN</code> a table to
     * the last table added to the <code>FROM</code> clause using
     * {@link Table#naturalFullOuterJoin(String)}
     * <p>
     * Natural joins are supported by most RDBMS. If they aren't supported, they
     * are emulated if jOOQ has enough information.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String)
     * @see Table#naturalFullOuterJoin(String)
     * @see SQL
     */
    @NotNull @CheckReturnValue
    @Support({ FIREBIRD, HSQLDB, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    @PlainSQL
    SelectJoinStep<R> naturalFullOuterJoin(String sql);

    /**
     * Convenience method to <code>NATURAL FULL OUTER JOIN</code> a table to
     * the last table added to the <code>FROM</code> clause using
     * {@link Table#naturalFullOuterJoin(String, Object...)}
     * <p>
     * Natural joins are supported by most RDBMS. If they aren't supported, they
     * are emulated if jOOQ has enough information.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, Object...)
     * @see DSL#sql(String, Object...)
     * @see Table#naturalFullOuterJoin(String, Object...)
     * @see SQL
     */
    @NotNull @CheckReturnValue
    @Support({ FIREBIRD, HSQLDB, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    @PlainSQL
    SelectJoinStep<R> naturalFullOuterJoin(String sql, Object... bindings);

    /**
     * Convenience method to <code>NATURAL FULL OUTER JOIN</code> a table to
     * the last table added to the <code>FROM</code> clause using
     * {@link Table#naturalFullOuterJoin(String, QueryPart...)}
     * <p>
     * Natural joins are supported by most RDBMS. If they aren't supported, they
     * are emulated if jOOQ has enough information.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, QueryPart...)
     * @see DSL#sql(String, QueryPart...)
     * @see Table#naturalFullOuterJoin(String, QueryPart...)
     * @see SQL
     */
    @NotNull @CheckReturnValue
    @Support({ FIREBIRD, HSQLDB, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    @PlainSQL
    SelectJoinStep<R> naturalFullOuterJoin(String sql, QueryPart... parts);

    /**
     * Convenience method to <code>NATURAL FULL OUTER JOIN</code> a table to
     * the last table added to the <code>FROM</code> clause using
     * {@link Table#naturalFullOuterJoin(Name)}
     * <p>
     * Natural joins are supported by most RDBMS. If they aren't supported, they
     * are emulated if jOOQ has enough information.
     *
     * @see DSL#table(Name)
     * @see Table#naturalFullOuterJoin(Name)
     */
    @NotNull @CheckReturnValue
    @Support({ FIREBIRD, HSQLDB, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    SelectJoinStep<R> naturalFullOuterJoin(Name name);

    // -------------------------------------------------------------------------
    // XXX: SEMI and ANTI JOIN
    // -------------------------------------------------------------------------

    /**
     * A synthetic <code>LEFT SEMI JOIN</code> clause that translates to an
     * equivalent <code>EXISTS</code> predicate.
     * <p>
     * The following two SQL snippets are semantically equivalent:
     * <pre><code>
     * -- Using LEFT SEMI JOIN
     * FROM A
     *     LEFT SEMI JOIN B
     *         ON A.ID = B.ID
     *
     * -- Using WHERE EXISTS
     * FROM A
     * WHERE EXISTS (
     *     SELECT 1 FROM B WHERE A.ID = B.ID
     * )
     * </code></pre>
     * <p>
     * Notice that according to
     * <a href="https://en.wikipedia.org/wiki/Relational_algebra">Relational
     * algebra's</a> understanding of left semi join, the right hand side of the
     * left semi join operator is not projected, i.e. it cannot be accessed from
     * <code>WHERE</code> or <code>SELECT</code> or any other clause than
     * <code>ON</code>.
     *
     * @see Table#leftSemiJoin(TableLike)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectOnStep<R> leftSemiJoin(TableLike<?> table);

    /**
     * A synthetic <code>LEFT SEMI JOIN</code> clause that translates to an
     * equivalent <code>EXISTS</code> predicate.
     * <p>
     * The following two SQL snippets are semantically equivalent:
     * <pre><code>
     * -- Using LEFT SEMI JOIN
     * FROM A
     *     LEFT SEMI JOIN B
     *         ON A.ID = B.ID
     *
     * -- Using WHERE EXISTS
     * FROM A
     * WHERE EXISTS (
     *     SELECT 1 FROM B WHERE A.ID = B.ID
     * )
     * </code></pre>
     * <p>
     * Notice that according to
     * <a href="https://en.wikipedia.org/wiki/Relational_algebra">Relational
     * algebra's</a> understanding of left semi join, the right hand side of the
     * left semi join operator is not projected, i.e. it cannot be accessed from
     * <code>WHERE</code> or <code>SELECT</code> or any other clause than
     * <code>ON</code>.
     *
     * @see Table#leftSemiJoin(Path)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectOptionalOnStep<R> leftSemiJoin(Path<?> path);

    /**
     * A synthetic <code>LEFT ANTI JOIN</code> clause that translates to an
     * equivalent <code>NOT EXISTS</code> predicate.
     * <p>
     * The following two SQL snippets are semantically equivalent:
     * <pre><code>
     * -- Using LEFT ANTI JOIN
     * FROM A
     *     LEFT ANTI JOIN B
     *         ON A.ID = B.ID
     *
     * -- Using WHERE NOT EXISTS
     * FROM A
     * WHERE NOT EXISTS (
     *     SELECT 1 FROM B WHERE A.ID = B.ID
     * )
     * </code></pre>
     * <p>
     * Notice that according to
     * <a href="https://en.wikipedia.org/wiki/Relational_algebra">Relational
     * algebra's</a> understanding of left semi join, the right hand side of the
     * left semi join operator is not projected, i.e. it cannot be accessed from
     * <code>WHERE</code> or <code>SELECT</code> or any other clause than
     * <code>ON</code>.
     *
     * @see Table#leftAntiJoin(TableLike)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectOnStep<R> leftAntiJoin(TableLike<?> table);

    /**
     * A synthetic <code>LEFT ANTI JOIN</code> clause that translates to an
     * equivalent <code>NOT EXISTS</code> predicate.
     * <p>
     * The following two SQL snippets are semantically equivalent:
     * <pre><code>
     * -- Using LEFT ANTI JOIN
     * FROM A
     *     LEFT ANTI JOIN B
     *         ON A.ID = B.ID
     *
     * -- Using WHERE NOT EXISTS
     * FROM A
     * WHERE NOT EXISTS (
     *     SELECT 1 FROM B WHERE A.ID = B.ID
     * )
     * </code></pre>
     * <p>
     * Notice that according to
     * <a href="https://en.wikipedia.org/wiki/Relational_algebra">Relational
     * algebra's</a> understanding of left semi join, the right hand side of the
     * left semi join operator is not projected, i.e. it cannot be accessed from
     * <code>WHERE</code> or <code>SELECT</code> or any other clause than
     * <code>ON</code>.
     *
     * @see Table#leftAntiJoin(Path)
     */
    @NotNull @CheckReturnValue
    @Support
    SelectOptionalOnStep<R> leftAntiJoin(Path<?> path);

    // -------------------------------------------------------------------------
    // XXX: APPLY clauses on tables
    // -------------------------------------------------------------------------

    /**
     * <code>CROSS APPLY</code> a table to this table.
     *
     * @see Table#crossApply(TableLike)
     */
    @NotNull @CheckReturnValue
    @Support({ FIREBIRD, POSTGRES, TRINO, YUGABYTEDB })
    SelectJoinStep<R> crossApply(TableLike<?> table);

    /**
     * <code>CROSS APPLY</code> a table to this table.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(SQL)
     * @see Table#crossApply(SQL)
     * @see SQL
     */
    @NotNull @CheckReturnValue
    @Support({ FIREBIRD, POSTGRES, TRINO, YUGABYTEDB })
    @PlainSQL
    SelectJoinStep<R> crossApply(SQL sql);

    /**
     * <code>CROSS APPLY</code> a table to this table.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String)
     * @see Table#crossApply(String)
     * @see SQL
     */
    @NotNull @CheckReturnValue
    @Support({ FIREBIRD, POSTGRES, TRINO, YUGABYTEDB })
    @PlainSQL
    SelectJoinStep<R> crossApply(String sql);

    /**
     * <code>CROSS APPLY</code> a table to this table.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, Object...)
     * @see DSL#sql(String, Object...)
     * @see Table#crossApply(String, Object...)
     * @see SQL
     */
    @NotNull @CheckReturnValue
    @Support({ FIREBIRD, POSTGRES, TRINO, YUGABYTEDB })
    @PlainSQL
    SelectJoinStep<R> crossApply(String sql, Object... bindings);

    /**
     * <code>CROSS APPLY</code> a table to this table.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, QueryPart...)
     * @see DSL#sql(String, QueryPart...)
     * @see Table#crossApply(String, QueryPart...)
     * @see SQL
     */
    @NotNull @CheckReturnValue
    @Support({ FIREBIRD, POSTGRES, TRINO, YUGABYTEDB })
    @PlainSQL
    SelectJoinStep<R> crossApply(String sql, QueryPart... parts);

    /**
     * <code>CROSS APPLY</code> a table to this table.
     *
     * @see DSL#table(Name)
     * @see Table#crossApply(Name)
     */
    @NotNull @CheckReturnValue
    @Support({ FIREBIRD, POSTGRES, TRINO, YUGABYTEDB })
    SelectJoinStep<R> crossApply(Name name);

    /**
     * <code>OUTER APPLY</code> a table to this table.
     *
     * @see Table#outerApply(TableLike)
     */
    @NotNull @CheckReturnValue
    @Support({ FIREBIRD, POSTGRES, TRINO, YUGABYTEDB })
    SelectJoinStep<R> outerApply(TableLike<?> table);

    /**
     * <code>OUTER APPLY</code> a table to this table.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(SQL)
     * @see Table#outerApply(SQL)
     * @see SQL
     */
    @NotNull @CheckReturnValue
    @Support({ FIREBIRD, POSTGRES, TRINO, YUGABYTEDB })
    @PlainSQL
    SelectJoinStep<R> outerApply(SQL sql);

    /**
     * <code>OUTER APPLY</code> a table to this table.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String)
     * @see Table#outerApply(String)
     * @see SQL
     */
    @NotNull @CheckReturnValue
    @Support({ FIREBIRD, POSTGRES, TRINO, YUGABYTEDB })
    @PlainSQL
    SelectJoinStep<R> outerApply(String sql);

    /**
     * <code>OUTER APPLY</code> a table to this table.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, Object...)
     * @see DSL#sql(String, Object...)
     * @see Table#outerApply(String, Object...)
     * @see SQL
     */
    @NotNull @CheckReturnValue
    @Support({ FIREBIRD, POSTGRES, TRINO, YUGABYTEDB })
    @PlainSQL
    SelectJoinStep<R> outerApply(String sql, Object... bindings);

    /**
     * <code>OUTER APPLY</code> a table to this table.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, QueryPart...)
     * @see DSL#sql(String, QueryPart...)
     * @see Table#outerApply(String, QueryPart...)
     * @see SQL
     */
    @NotNull @CheckReturnValue
    @Support({ FIREBIRD, POSTGRES, TRINO, YUGABYTEDB })
    @PlainSQL
    SelectJoinStep<R> outerApply(String sql, QueryPart... parts);

    /**
     * <code>OUTER APPLY</code> a table to this table.
     *
     * @see DSL#table(Name)
     * @see Table#outerApply(Name)
     */
    @NotNull @CheckReturnValue
    @Support({ FIREBIRD, POSTGRES, TRINO, YUGABYTEDB })
    SelectJoinStep<R> outerApply(Name name);

    /**
     * <code>STRAIGHT_JOIN</code> a table to this table.
     *
     * @see Table#straightJoin(TableLike)
     */
    @NotNull @CheckReturnValue
    @Support({ MARIADB, MYSQL })
    SelectOnStep<R> straightJoin(TableLike<?> table);

    /**
     * <code>STRAIGHT_JOIN</code> a path to this table.
     *
     * @see Table#straightJoin(Path)
     */
    @NotNull @CheckReturnValue
    @Support({ MARIADB, MYSQL })
    SelectOnStep<R> straightJoin(Path<?> table);

    /**
     * <code>STRAIGHT_JOIN</code> a table to this table.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(SQL)
     * @see Table#straightJoin(SQL)
     */
    @NotNull @CheckReturnValue
    @Support({ MARIADB, MYSQL })
    @PlainSQL
    SelectOnStep<R> straightJoin(SQL sql);

    /**
     * <code>STRAIGHT_JOIN</code> a table to this table.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String)
     * @see Table#straightJoin(String)
     */
    @NotNull @CheckReturnValue
    @Support({ MARIADB, MYSQL })
    @PlainSQL
    SelectOnStep<R> straightJoin(String sql);

    /**
     * <code>STRAIGHT_JOIN</code> a table to this table.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, Object...)
     * @see DSL#sql(String, Object...)
     * @see Table#straightJoin(String, Object...)
     */
    @NotNull @CheckReturnValue
    @Support({ MARIADB, MYSQL })
    @PlainSQL
    SelectOnStep<R> straightJoin(String sql, Object... bindings);

    /**
     * <code>STRAIGHT_JOIN</code> a table to this table.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, QueryPart...)
     * @see DSL#sql(String, QueryPart...)
     * @see Table#straightJoin(String, QueryPart...)
     */
    @NotNull @CheckReturnValue
    @Support({ MARIADB, MYSQL })
    @PlainSQL
    SelectOnStep<R> straightJoin(String sql, QueryPart... parts);

    /**
     * <code>STRAIGHT_JOIN</code> a table to this table.
     *
     * @see DSL#table(Name)
     * @see Table#straightJoin(Name)
     */
    @NotNull @CheckReturnValue
    @Support({ MARIADB, MYSQL })
    SelectOnStep<R> straightJoin(Name name);
}
