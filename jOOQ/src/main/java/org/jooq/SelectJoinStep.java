/**
 * Copyright (c) 2009-2016, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
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
package org.jooq;

// ...
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
// ...
import static org.jooq.SQLDialect.HSQLDB;
// ...
// ...
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
// ...
// ...
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.POSTGRES_9_3;
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
// ...

import org.jooq.impl.DSL;

/**
 * This type is used for the {@link Select}'s DSL API when selecting generic
 * {@link Record} types.
 * <p>
 * Example: <code><pre>
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
 *      AND T_BOOK.PUBLISHED > '2008-01-01'
 * GROUP BY T_AUTHOR.FIRST_NAME, T_AUTHOR.LAST_NAME
 *   HAVING COUNT(*) > 5
 * ORDER BY T_AUTHOR.LAST_NAME ASC NULLS FIRST
 *    LIMIT 2
 *   OFFSET 1
 *      FOR UPDATE
 *       OF FIRST_NAME, LAST_NAME
 *       NO WAIT
 * </pre></code> Its equivalent in jOOQ <code><pre>
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
 * </pre></code> Refer to the manual for more details
 *
 * @author Lukas Eder
 */
public interface SelectJoinStep<R extends Record> extends SelectWhereStep<R> {

    /**
     * Convenience method to join a table to the last table added to the
     * <code>FROM</code> clause using {@link Table#join(TableLike, JoinType)}
     * <p>
     * Depending on the <code>JoinType</code>, a subsequent
     * {@link SelectOnStep#on(Condition...)} or
     * {@link SelectOnStep#using(Field...)} clause is required. If it is
     * required but omitted, the JOIN clause will be ignored
     */
    @Support
    SelectOptionalOnStep<R> join(TableLike<?> table, JoinType type);

    /**
     * Convenience method to <code>INNER JOIN</code> a table to the last table
     * added to the <code>FROM</code> clause using {@link Table#join(TableLike)}.
     * <p>
     * A synonym for {@link #innerJoin(TableLike)}.
     *
     * @see Table#join(TableLike)
     * @see #innerJoin(TableLike)
     */
    @Support
    SelectOnStep<R> join(TableLike<?> table);

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
    @Support
    @PlainSQL
    SelectOnStep<R> join(Name name);

    /**
     * Convenience method to <code>INNER JOIN</code> a table to the last table
     * added to the <code>FROM</code> clause using {@link Table#join(TableLike)}.
     *
     * @see Table#innerJoin(TableLike)
     */
    @Support
    SelectOnStep<R> innerJoin(TableLike<?> table);

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
    @Support
    SelectOnStep<R> innerJoin(Name name);

    /**
     * Convenience method to <code>CROSS JOIN</code> a table to the last table
     * added to the <code>FROM</code> clause using
     * {@link Table#crossJoin(TableLike)}
     * <p>
     * If this syntax is unavailable, it is emulated with a regular
     * <code>INNER JOIN</code>. The following two constructs are equivalent:
     * <code><pre>
     * A cross join B
     * A join B on 1 = 1
     * </pre></code>
     *
     * @see Table#crossJoin(TableLike)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    SelectJoinStep<R> crossJoin(TableLike<?> table);

    /**
     * Convenience method to <code>CROSS JOIN</code> a table to the last table
     * added to the <code>FROM</code> clause using
     * {@link Table#crossJoin(String)}
     * <p>
     * If this syntax is unavailable, it is emulated with a regular
     * <code>INNER JOIN</code>. The following two constructs are equivalent:
     * <code><pre>
     * A cross join B
     * A join B on 1 = 1
     * </pre></code>
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
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    @PlainSQL
    SelectJoinStep<R> crossJoin(SQL sql);

    /**
     * Convenience method to <code>CROSS JOIN</code> a table to the last table
     * added to the <code>FROM</code> clause using
     * {@link Table#crossJoin(String)}
     * <p>
     * If this syntax is unavailable, it is emulated with a regular
     * <code>INNER JOIN</code>. The following two constructs are equivalent:
     * <code><pre>
     * A cross join B
     * A join B on 1 = 1
     * </pre></code>
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
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    @PlainSQL
    SelectJoinStep<R> crossJoin(String sql);

    /**
     * Convenience method to <code>CROSS JOIN</code> a table to the last table
     * added to the <code>FROM</code> clause using
     * {@link Table#crossJoin(String, Object...)}
     * <p>
     * If this syntax is unavailable, it is emulated with a regular
     * <code>INNER JOIN</code>. The following two constructs are equivalent:
     * <code><pre>
     * A cross join B
     * A join B on 1 = 1
     * </pre></code>
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
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    @PlainSQL
    SelectJoinStep<R> crossJoin(String sql, Object... bindings);

    /**
     * Convenience method to <code>CROSS JOIN</code> a table to the last table
     * added to the <code>FROM</code> clause using
     * {@link Table#crossJoin(String, QueryPart...)}
     * <p>
     * If this syntax is unavailable, it is emulated with a regular
     * <code>INNER JOIN</code>. The following two constructs are equivalent:
     * <code><pre>
     * A cross join B
     * A join B on 1 = 1
     * </pre></code>
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
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    @PlainSQL
    SelectJoinStep<R> crossJoin(String sql, QueryPart... parts);

    /**
     * Convenience method to <code>CROSS JOIN</code> a table to the last table
     * added to the <code>FROM</code> clause using
     * {@link Table#crossJoin(Name)}
     * <p>
     * If this syntax is unavailable, it is emulated with a regular
     * <code>INNER JOIN</code>. The following two constructs are equivalent:
     * <code><pre>
     * A cross join B
     * A join B on 1 = 1
     * </pre></code>
     *
     * @see DSL#table(Name)
     * @see Table#crossJoin(Name)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
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
    @Support
    SelectJoinPartitionByStep<R> leftJoin(TableLike<?> table);

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
    @Support
    SelectJoinPartitionByStep<R> leftJoin(Name name);

    /**
     * Convenience method to <code>LEFT OUTER JOIN</code> a table to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#leftOuterJoin(TableLike)}
     *
     * @see Table#leftOuterJoin(TableLike)
     */
    @Support
    SelectJoinPartitionByStep<R> leftOuterJoin(TableLike<?> table);

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
    @Support
    SelectJoinPartitionByStep<R> leftOuterJoin(Name name);

    /**
     * Convenience method to <code>RIGHT OUTER JOIN</code> a table to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#rightOuterJoin(TableLike)}.
     * <p>
     * A synonym for {@link #rightOuterJoin(TableLike)}.
     * <p>
     * This is only possible where the underlying RDBMS supports it
     *
     * @see Table#rightOuterJoin(TableLike)
     * @see #rightOuterJoin(TableLike)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    SelectJoinPartitionByStep<R> rightJoin(TableLike<?> table);

    /**
     * Convenience method to <code>RIGHT OUTER JOIN</code> a table to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#rightOuterJoin(String)}.
     * <p>
     * A synonym for {@link #rightOuterJoin(String)}.
     * <p>
     * This is only possible where the underlying RDBMS supports it
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
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    @PlainSQL
    SelectJoinPartitionByStep<R> rightJoin(SQL sql);

    /**
     * Convenience method to <code>RIGHT OUTER JOIN</code> a table to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#rightOuterJoin(String)}.
     * <p>
     * A synonym for {@link #rightOuterJoin(String)}.
     * <p>
     * This is only possible where the underlying RDBMS supports it
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
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    @PlainSQL
    SelectJoinPartitionByStep<R> rightJoin(String sql);

    /**
     * Convenience method to <code>RIGHT OUTER JOIN</code> a table to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#rightOuterJoin(String, Object...)}.
     * <p>
     * A synonym for {@link #rightOuterJoin(String, Object...)}.
     * <p>
     * This is only possible where the underlying RDBMS supports it
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
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    @PlainSQL
    SelectJoinPartitionByStep<R> rightJoin(String sql, Object... bindings);

    /**
     * Convenience method to <code>RIGHT OUTER JOIN</code> a table to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#rightOuterJoin(String, QueryPart...)}.
     * <p>
     * A synonym for {@link #rightOuterJoin(String, QueryPart...)}.
     * <p>
     * This is only possible where the underlying RDBMS supports it
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
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    @PlainSQL
    SelectJoinPartitionByStep<R> rightJoin(String sql, QueryPart... parts);

    /**
     * Convenience method to <code>RIGHT OUTER JOIN</code> a table to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#rightOuterJoin(Name)}.
     * <p>
     * A synonym for {@link #rightOuterJoin(Name)}.
     * <p>
     * This is only possible where the underlying RDBMS supports it
     *
     * @see DSL#table(Name)
     * @see Table#rightOuterJoin(Name)
     * @see #rightOuterJoin(Name)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    SelectJoinPartitionByStep<R> rightJoin(Name name);

    /**
     * Convenience method to <code>RIGHT OUTER JOIN</code> a table to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#rightOuterJoin(TableLike)}
     * <p>
     * This is only possible where the underlying RDBMS supports it
     *
     * @see Table#rightOuterJoin(TableLike)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    SelectJoinPartitionByStep<R> rightOuterJoin(TableLike<?> table);

    /**
     * Convenience method to <code>RIGHT OUTER JOIN</code> a table to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#rightOuterJoin(String)}
     * <p>
     * This is only possible where the underlying RDBMS supports it
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
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    @PlainSQL
    SelectJoinPartitionByStep<R> rightOuterJoin(SQL sql);

    /**
     * Convenience method to <code>RIGHT OUTER JOIN</code> a table to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#rightOuterJoin(String)}
     * <p>
     * This is only possible where the underlying RDBMS supports it
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
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    @PlainSQL
    SelectJoinPartitionByStep<R> rightOuterJoin(String sql);

    /**
     * Convenience method to <code>RIGHT OUTER JOIN</code> a table to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#rightOuterJoin(String, Object...)}
     * <p>
     * This is only possible where the underlying RDBMS supports it
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
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    @PlainSQL
    SelectJoinPartitionByStep<R> rightOuterJoin(String sql, Object... bindings);

    /**
     * Convenience method to <code>RIGHT OUTER JOIN</code> a table to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#rightOuterJoin(String, QueryPart...)}
     * <p>
     * This is only possible where the underlying RDBMS supports it
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
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    @PlainSQL
    SelectJoinPartitionByStep<R> rightOuterJoin(String sql, QueryPart... parts);

    /**
     * Convenience method to <code>RIGHT OUTER JOIN</code> a table to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#rightOuterJoin(Name)}
     * <p>
     * This is only possible where the underlying RDBMS supports it
     *
     * @see DSL#table(Name)
     * @see Table#rightOuterJoin(Name)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    SelectJoinPartitionByStep<R> rightOuterJoin(Name name);

    /**
     * Convenience method to <code>FULL OUTER JOIN</code> a table to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#fullOuterJoin(TableLike)}
     * <p>
     * This is only possible where the underlying RDBMS supports it
     *
     * @see Table#fullOuterJoin(TableLike)
     */
    @Support({ FIREBIRD, HSQLDB, POSTGRES })
    SelectOnStep<R> fullOuterJoin(TableLike<?> table);

    /**
     * Convenience method to <code>FULL OUTER JOIN</code> a table to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#fullOuterJoin(String)}
     * <p>
     * This is only possible where the underlying RDBMS supports it
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
    @Support({ FIREBIRD, HSQLDB, POSTGRES })
    @PlainSQL
    SelectOnStep<R> fullOuterJoin(SQL sql);

    /**
     * Convenience method to <code>FULL OUTER JOIN</code> a table to the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#fullOuterJoin(String)}
     * <p>
     * This is only possible where the underlying RDBMS supports it
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
    @Support({ FIREBIRD, HSQLDB, POSTGRES })
    @PlainSQL
    SelectOnStep<R> fullOuterJoin(String sql);

    /**
     * Convenience method to <code>FULL OUTER JOIN</code> a tableto the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#fullOuterJoin(String, Object...)}
     * <p>
     * This is only possible where the underlying RDBMS supports it
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
    @Support({ FIREBIRD, HSQLDB, POSTGRES })
    @PlainSQL
    SelectOnStep<R> fullOuterJoin(String sql, Object... bindings);

    /**
     * Convenience method to <code>FULL OUTER JOIN</code> a tableto the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#fullOuterJoin(String, QueryPart...)}
     * <p>
     * This is only possible where the underlying RDBMS supports it
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
    @Support({ FIREBIRD, HSQLDB, POSTGRES })
    @PlainSQL
    SelectOnStep<R> fullOuterJoin(String sql, QueryPart... parts);

    /**
     * Convenience method to <code>FULL OUTER JOIN</code> a tableto the last
     * table added to the <code>FROM</code> clause using
     * {@link Table#fullOuterJoin(Name)}
     * <p>
     * This is only possible where the underlying RDBMS supports it
     *
     * @see DSL#table(Name)
     * @see Table#fullOuterJoin(Name)
     */
    @Support({ FIREBIRD, HSQLDB, POSTGRES })
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
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
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
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
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
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
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
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
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
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
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
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    SelectJoinStep<R> naturalRightOuterJoin(Name name);

    // -------------------------------------------------------------------------
    // XXX: SEMI and ANTI JOIN
    // -------------------------------------------------------------------------

    /**
     * A synthetic <code>LEFT SEMI JOIN</code> clause that translates to an
     * equivalent <code>EXISTS</code> predicate.
     * <p>
     * The following two SQL snippets are semantically equivalent:
     * <code><pre>
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
     * </pre></code>
     *
     * @see Table#leftSemiJoin(TableLike)
     */
    @Support
    SelectOnStep<R> leftSemiJoin(TableLike<?> table);

    /**
     * A synthetic <code>LEFT ANTI JOIN</code> clause that translates to an
     * equivalent <code>NOT EXISTS</code> predicate.
     * <p>
     * The following two SQL snippets are semantically equivalent:
     * <code><pre>
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
     * </pre></code>
     *
     * @see Table#leftAntiJoin(TableLike)
     */
    @Support
    SelectOnStep<R> leftAntiJoin(TableLike<?> table);

    // -------------------------------------------------------------------------
    // XXX: APPLY clauses on tables
    // -------------------------------------------------------------------------

    /**
     * <code>CROSS APPLY</code> a table to this table.
     *
     * @see Table#crossApply(TableLike)
     */
    @Support({ POSTGRES_9_3 })
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
    @Support({ POSTGRES_9_3 })
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
    @Support({ POSTGRES_9_3 })
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
    @Support({ POSTGRES_9_3 })
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
    @Support({ POSTGRES_9_3 })
    @PlainSQL
    SelectJoinStep<R> crossApply(String sql, QueryPart... parts);

    /**
     * <code>CROSS APPLY</code> a table to this table.
     *
     * @see DSL#table(Name)
     * @see Table#crossApply(Name)
     */
    @Support({ POSTGRES_9_3 })
    SelectJoinStep<R> crossApply(Name name);

    /**
     * <code>OUTER APPLY</code> a table to this table.
     *
     * @see Table#outerApply(TableLike)
     */
    @Support({ POSTGRES_9_3 })
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
    @Support({ POSTGRES_9_3 })
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
    @Support({ POSTGRES_9_3 })
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
    @Support({ POSTGRES_9_3 })
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
    @Support({ POSTGRES_9_3 })
    @PlainSQL
    SelectJoinStep<R> outerApply(String sql, QueryPart... parts);

    /**
     * <code>OUTER APPLY</code> a table to this table.
     *
     * @see DSL#table(Name)
     * @see Table#outerApply(Name)
     */
    @Support({ POSTGRES_9_3 })
    SelectJoinStep<R> outerApply(Name name);

    /**
     * <code>STRAIGHT_JOIN</code> a table to this table.
     *
     * @see Table#straightJoin(TableLike)
     */
    @Support({ MYSQL })
    SelectOnStep<R> straightJoin(TableLike<?> table);

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
    @Support({ MYSQL })
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
    @Support({ MYSQL })
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
    @Support({ MYSQL })
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
    @Support({ MYSQL })
    @PlainSQL
    SelectOnStep<R> straightJoin(String sql, QueryPart... parts);

    /**
     * <code>STRAIGHT_JOIN</code> a table to this table.
     *
     * @see DSL#table(Name)
     * @see Table#straightJoin(Name)
     */
    @Support({ MYSQL })
    SelectOnStep<R> straightJoin(Name name);
}
