/**
 * Copyright (c) 2009-2014, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
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
package org.jooq;

// ...
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.HSQLDB;
// ...
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
// ...
// ...
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...

import org.jooq.api.annotation.State;
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
@State
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
     * added to the <code>FROM</code> clause using {@link Table#join(TableLike)}
     *
     * @see Table#join(TableLike)
     */
    @Support
    SelectOnStep<R> join(TableLike<?> table);

    /**
     * Convenience method to <code>INNER JOIN</code> a table to the last table
     * added to the <code>FROM</code> clause using {@link Table#join(String)}
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String)
     * @see Table#join(String)
     */
    @Support
    SelectOnStep<R> join(String sql);

    /**
     * Convenience method to <code>INNER JOIN</code> a table to the last table
     * added to the <code>FROM</code> clause using
     * {@link Table#join(String, Object...)}
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, Object...)
     * @see Table#join(String, Object...)
     */
    @Support
    SelectOnStep<R> join(String sql, Object... bindings);

    /**
     * Convenience method to <code>INNER JOIN</code> a table to the last table
     * added to the <code>FROM</code> clause using
     * {@link Table#join(String, QueryPart...)}
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, QueryPart...)
     * @see Table#join(String, QueryPart...)
     */
    @Support
    SelectOnStep<R> join(String sql, QueryPart... parts);

    /**
     * Convenience method to <code>CROSS JOIN</code> a table to the last table
     * added to the <code>FROM</code> clause using
     * {@link Table#crossJoin(TableLike)}
     * <p>
     * If this syntax is unavailable, it is simulated with a regular
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
     * If this syntax is unavailable, it is simulated with a regular
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
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    SelectJoinStep<R> crossJoin(String sql);

    /**
     * Convenience method to <code>CROSS JOIN</code> a table to the last table
     * added to the <code>FROM</code> clause using
     * {@link Table#crossJoin(String, Object...)}
     * <p>
     * If this syntax is unavailable, it is simulated with a regular
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
     * @see Table#crossJoin(String, Object...)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    SelectJoinStep<R> crossJoin(String sql, Object... bindings);

    /**
     * Convenience method to <code>CROSS JOIN</code> a table to the last table
     * added to the <code>FROM</code> clause using
     * {@link Table#crossJoin(String, QueryPart...)}
     * <p>
     * If this syntax is unavailable, it is simulated with a regular
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
     * @see Table#crossJoin(String, QueryPart...)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    SelectJoinStep<R> crossJoin(String sql, QueryPart... parts);

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
     * @see DSL#table(String)
     * @see Table#leftOuterJoin(String)
     */
    @Support
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
     * @see Table#leftOuterJoin(String, Object...)
     */
    @Support
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
     * @see Table#leftOuterJoin(String, QueryPart...)
     */
    @Support
    SelectJoinPartitionByStep<R> leftOuterJoin(String sql, QueryPart... parts);

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
     * @see DSL#table(String)
     * @see Table#rightOuterJoin(String)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
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
     * @see Table#rightOuterJoin(String, Object...)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
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
     * @see Table#rightOuterJoin(String, QueryPart...)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    SelectJoinPartitionByStep<R> rightOuterJoin(String sql, QueryPart... parts);

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
     * @see DSL#table(String)
     * @see Table#fullOuterJoin(String)
     */
    @Support({ FIREBIRD, HSQLDB, POSTGRES })
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
     * @see Table#fullOuterJoin(String, Object...)
     */
    @Support({ FIREBIRD, HSQLDB, POSTGRES })
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
     * @see Table#fullOuterJoin(String, QueryPart...)
     */
    @Support({ FIREBIRD, HSQLDB, POSTGRES })
    SelectOnStep<R> fullOuterJoin(String sql, QueryPart... parts);

    /**
     * Convenience method to <code>NATURAL JOIN</code> a table to the last table
     * added to the <code>FROM</code> clause using
     * {@link Table#naturalJoin(TableLike)}
     * <p>
     * Natural joins are supported by most RDBMS. If they aren't supported, they
     * are simulated if jOOQ has enough information.
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
     * are simulated if jOOQ has enough information.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String)
     * @see Table#naturalJoin(String)
     */
    @Support
    SelectJoinStep<R> naturalJoin(String sql);

    /**
     * Convenience method to <code>NATURAL JOIN</code> a table to the last table
     * added to the <code>FROM</code> clause using
     * {@link Table#naturalJoin(String, Object...)}
     * <p>
     * Natural joins are supported by most RDBMS. If they aren't supported, they
     * are simulated if jOOQ has enough information.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, Object...)
     * @see Table#naturalJoin(String, Object...)
     */
    @Support
    SelectJoinStep<R> naturalJoin(String sql, Object... bindings);

    /**
     * Convenience method to <code>NATURAL JOIN</code> a table to the last table
     * added to the <code>FROM</code> clause using
     * {@link Table#naturalJoin(String, QueryPart...)}
     * <p>
     * Natural joins are supported by most RDBMS. If they aren't supported, they
     * are simulated if jOOQ has enough information.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, QueryPart...)
     * @see Table#naturalJoin(String, QueryPart...)
     */
    @Support
    SelectJoinStep<R> naturalJoin(String sql, QueryPart... parts);

    /**
     * Convenience method to <code>NATURAL LEFT OUTER JOIN</code> a table to the
     * last table added to the <code>FROM</code> clause using
     * {@link Table#naturalLeftOuterJoin(TableLike)}
     * <p>
     * Natural joins are supported by most RDBMS. If they aren't supported, they
     * are simulated if jOOQ has enough information.
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
     * are simulated if jOOQ has enough information.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String)
     * @see Table#naturalLeftOuterJoin(String)
     */
    @Support
    SelectJoinStep<R> naturalLeftOuterJoin(String sql);

    /**
     * Convenience method to <code>NATURAL LEFT OUTER JOIN</code> a table to the
     * last table added to the <code>FROM</code> clause using
     * {@link Table#naturalLeftOuterJoin(String, Object...)}
     * <p>
     * Natural joins are supported by most RDBMS. If they aren't supported, they
     * are simulated if jOOQ has enough information.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, Object...)
     * @see Table#naturalLeftOuterJoin(String, Object...)
     */
    @Support
    SelectJoinStep<R> naturalLeftOuterJoin(String sql, Object... bindings);

    /**
     * Convenience method to <code>NATURAL LEFT OUTER JOIN</code> a table to the
     * last table added to the <code>FROM</code> clause using
     * {@link Table#naturalLeftOuterJoin(String, QueryPart...)}
     * <p>
     * Natural joins are supported by most RDBMS. If they aren't supported, they
     * are simulated if jOOQ has enough information.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, QueryPart...)
     * @see Table#naturalLeftOuterJoin(String, QueryPart...)
     */
    @Support
    SelectJoinStep<R> naturalLeftOuterJoin(String sql, QueryPart... parts);

    /**
     * Convenience method to <code>NATURAL RIGHT OUTER JOIN</code> a table to
     * the last table added to the <code>FROM</code> clause using
     * {@link Table#naturalRightOuterJoin(TableLike)}
     * <p>
     * Natural joins are supported by most RDBMS. If they aren't supported, they
     * are simulated if jOOQ has enough information.
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
     * are simulated if jOOQ has enough information.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String)
     * @see Table#naturalRightOuterJoin(String)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    SelectJoinStep<R> naturalRightOuterJoin(String sql);

    /**
     * Convenience method to <code>NATURAL RIGHT OUTER JOIN</code> a table to
     * the last table added to the <code>FROM</code> clause using
     * {@link Table#naturalRightOuterJoin(String, Object...)}
     * <p>
     * Natural joins are supported by most RDBMS. If they aren't supported, they
     * are simulated if jOOQ has enough information.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, Object...)
     * @see Table#naturalRightOuterJoin(String, Object...)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    SelectJoinStep<R> naturalRightOuterJoin(String sql, Object... bindings);

    /**
     * Convenience method to <code>NATURAL RIGHT OUTER JOIN</code> a table to
     * the last table added to the <code>FROM</code> clause using
     * {@link Table#naturalRightOuterJoin(String, QueryPart...)}
     * <p>
     * Natural joins are supported by most RDBMS. If they aren't supported, they
     * are simulated if jOOQ has enough information.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, QueryPart...)
     * @see Table#naturalRightOuterJoin(String, QueryPart...)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    SelectJoinStep<R> naturalRightOuterJoin(String sql, QueryPart... parts);

    // -------------------------------------------------------------------------
    // XXX: APPLY clauses on tables
    // -------------------------------------------------------------------------

    /* [pro] xx

    xxx
     x xxxxxxxxxxx xxxxxxxxxxxx x xxxxx xx xxxx xxxxxx
     x
     x xxxx xxxxxxxxxxxxxxxxxxxxxxxxxxx
     xx
    xxxxxxxxxx xxxxxxxxxx xxxxxxxxxx xxxxxx xx
    xxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxx xxxxxxx

    xxx
     x xxxxxxxxxxx xxxxxxxxxxxx x xxxxx xx xxxx xxxxxx
     x xxx
     x xxxxxxxxxxxx xxxx xxxxxxxxx xxxxx xxx xxxx xxxx xxxxxxxx xxx xxxx
     x xxxxxxxxx xxxxxx xxxxxxxxxx xxx xxx xxxx xxxxxx xxx xxxxxxxxxxx xx
     x xxxxxxxxx xxx xxxxxxxxxx xx xxxx xx xxxxxxxx xxx xxxx xxxxxxxxx xxxxxx
     x xxxxxx xxxxxxxx xxxx xxxxxxxxxxxx xxxx xxx xxxxxxxx
     x
     x xxxx xxxxxxxxxxxxxxxxx
     x xxxx xxxxxxxxxxxxxxxxxxxxxxxx
     xx
    xxxxxxxxxx xxxxxxxxxx xxxxxxxxxx xxxxxx xx
    xxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxx xxxxx

    xxx
     x xxxxxxxxxxx xxxxxxxxxxxx x xxxxx xx xxxx xxxxxx
     x xxx
     x xxxxxxxxxxxx xxxx xxxxxxxxx xxxxx xxx xxxx xxxx xxxxxxxx xxx xxxx
     x xxxxxxxxx xxxxxx xxxxxxxxxx xxx xxx xxxx xxxxxx xxx xxxxxxxxxxx xx
     x xxxxxxxxx xxx xxxxxxxxxx xx xxxx xx xxxxxxxx xxx xxxx xxxxxxxxx xxxxxx
     x xxxxxx xxxxxxxx xxxx xxxxxxxxxxxx xxxx xxx xxxxxxxx
     x
     x xxxx xxxxxxxxxxxxxxxxx xxxxxxxxxx
     x xxxx xxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxx
     xx
    xxxxxxxxxx xxxxxxxxxx xxxxxxxxxx xxxxxx xx
    xxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxx xxxx xxxxxxxxx xxxxxxxxxx

    xxx
     x xxxxxxxxxxx xxxxxxxxxxxx x xxxxx xx xxxx xxxxxx
     x xxx
     x xxxxxxxxxxxx xxxx xxxxxxxxx xxxxx xxx xxxx xxxx xxxxxxxx xxx xxxx
     x xxxxxxxxx xxxxxx xxxxxxxxxx xxx xxx xxxx xxxxxx xxx xxxxxxxxxxx xx
     x xxxxxxxxx xxx xxxxxxxxxx xx xxxx xx xxxxxxxx xxx xxxx xxxxxxxxx xxxxxx
     x xxxxxx xxxxxxxx xxxx xxxxxxxxxxxx xxxx xxx xxxxxxxx
     x
     x xxxx xxxxxxxxxxxxxxxxx xxxxxxxxxxxxx
     x xxxx xxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxx
     xx
    xxxxxxxxxx xxxxxxxxxx xxxxxxxxxx xxxxxx xx
    xxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxx xxxx xxxxxxxxxxxx xxxxxxx

    xxx
     x xxxxxxxxxxx xxxxxxxxxxxx x xxxxx xx xxxx xxxxxx
     x
     x xxxx xxxxxxxxxxxxxxxxxxxxxxxxxxx
     xx
    xxxxxxxxxx xxxxxxxxxx xxxxxxxxxx xxxxxx xx
    xxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxx xxxxxxx

    xxx
     x xxxxxxxxxxx xxxxxxxxxxxx x xxxxx xx xxxx xxxxxx
     x xxx
     x xxxxxxxxxxxx xxxx xxxxxxxxx xxxxx xxx xxxx xxxx xxxxxxxx xxx xxxx
     x xxxxxxxxx xxxxxx xxxxxxxxxx xxx xxx xxxx xxxxxx xxx xxxxxxxxxxx xx
     x xxxxxxxxx xxx xxxxxxxxxx xx xxxx xx xxxxxxxx xxx xxxx xxxxxxxxx xxxxxx
     x xxxxxx xxxxxxxx xxxx xxxxxxxxxxxx xxxx xxx xxxxxxxx
     x
     x xxxx xxxxxxxxxxxxxxxxx
     x xxxx xxxxxxxxxxxxxxxxxxxxxxxx
     xx
    xxxxxxxxxx xxxxxxxxxx xxxxxxxxxx xxxxxx xx
    xxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxx xxxxx

    xxx
     x xxxxxxxxxxx xxxxxxxxxxxx x xxxxx xx xxxx xxxxxx
     x xxx
     x xxxxxxxxxxxx xxxx xxxxxxxxx xxxxx xxx xxxx xxxx xxxxxxxx xxx xxxx
     x xxxxxxxxx xxxxxx xxxxxxxxxx xxx xxx xxxx xxxxxx xxx xxxxxxxxxxx xx
     x xxxxxxxxx xxx xxxxxxxxxx xx xxxx xx xxxxxxxx xxx xxxx xxxxxxxxx xxxxxx
     x xxxxxx xxxxxxxx xxxx xxxxxxxxxxxx xxxx xxx xxxxxxxx
     x
     x xxxx xxxxxxxxxxxxxxxxx xxxxxxxxxx
     x xxxx xxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxx
     xx
    xxxxxxxxxx xxxxxxxxxx xxxxxxxxxx xxxxxx xx
    xxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxx xxxx xxxxxxxxx xxxxxxxxxx

    xxx
     x xxxxxxxxxxx xxxxxxxxxxxx x xxxxx xx xxxx xxxxxx
     x xxx
     x xxxxxxxxxxxx xxxx xxxxxxxxx xxxxx xxx xxxx xxxx xxxxxxxx xxx xxxx
     x xxxxxxxxx xxxxxx xxxxxxxxxx xxx xxx xxxx xxxxxx xxx xxxxxxxxxxx xx
     x xxxxxxxxx xxx xxxxxxxxxx xx xxxx xx xxxxxxxx xxx xxxx xxxxxxxxx xxxxxx
     x xxxxxx xxxxxxxx xxxx xxxxxxxxxxxx xxxx xxx xxxxxxxx
     x
     x xxxx xxxxxxxxxxxxxxxxx xxxxxxxxxxxxx
     x xxxx xxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxx
     xx
    xxxxxxxxxx xxxxxxxxxx xxxxxxxxxx xxxxxx xx
    xxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxx xxxx xxxxxxxxxxxx xxxxxxx

    xx [/pro] */

}
