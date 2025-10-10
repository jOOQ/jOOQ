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
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
// ...
import static org.jooq.SQLDialect.TRINO;
// ...
import static org.jooq.SQLDialect.YUGABYTEDB;

import java.util.Collection;
import java.util.List;

import org.jooq.impl.DSL;
import org.jooq.impl.QOM;
import org.jooq.impl.QOM.UnmodifiableList;
import org.jooq.impl.QOM.With;

import org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A <code>SELECT</code> statement.
 * <p>
 * <strong>Example:</strong>
 * <p>
 * <pre><code>
 * // Assuming import static org.jooq.impl.DSL.*;
 *
 * using(configuration)
 *    .select(ACTOR.FIRST_NAME, ACTOR.LAST_NAME)
 *    .from(ACTOR)
 *    .fetch();
 * </code></pre>
 * <p>
 * Instances can be created using {@link DSL#select(SelectFieldOrAsterisk...)},
 * or {@link DSLContext#selectQuery()} and overloads.
 *
 * @param <R> The record type being returned by this query
 * @author Lukas Eder
 */
public interface Select<R extends Record>
extends
    ResultQuery<R>,
    TableLike<R>,
    FieldLike,
    FieldOrRowOrSelect
{

    /**
     * Apply the <code>UNION</code> set operation.
     * <p>
     * In SQL, a <code>UNION</code> is <code>DISTINCT</code> by default,
     * meaning, duplicates are removed from the result set. So, this is the same
     * as {@link #unionDistinct(Select)}. If duplicate removal isn't required,
     * or already guaranteed by the data model, it is recommended to use
     * {@link #unionAll(Select)}, instead.
     *
     * @throws IllegalArgumentException If the argument select has the same
     *             identity as this select. The jOOQ 3.x API is mutable, which
     *             means that calls to the DSL API mutate this instance. Adding
     *             this instance as an set operation argument would lead to a
     *             {@link StackOverflowError} when generating the SQL.
     */
    @NotNull @CheckReturnValue
    @Support
    Select<R> union(Select<? extends R> select);

    /**
     * Apply the <code>UNION DISTINCT</code> set operation.
     * <p>
     * In SQL, a <code>UNION</code> is <code>DISTINCT</code> by default.
     * However, it is often useful to make this explicit to express intent when
     * distinct removal is really desired.
     *
     * @throws IllegalArgumentException If the argument select has the same
     *             identity as this select. The jOOQ 3.x API is mutable, which
     *             means that calls to the DSL API mutate this instance. Adding
     *             this instance as an set operation argument would lead to a
     *             {@link StackOverflowError} when generating the SQL.
     */
    @NotNull @CheckReturnValue
    @Support
    Select<R> unionDistinct(Select<? extends R> select);

    /**
     * Apply the <code>UNION ALL</code> set operation.
     *
     * @throws IllegalArgumentException If the argument select has the same
     *             identity as this select. The jOOQ 3.x API is mutable, which
     *             means that calls to the DSL API mutate this instance. Adding
     *             this instance as an set operation argument would lead to a
     *             {@link StackOverflowError} when generating the SQL.
     */
    @NotNull @CheckReturnValue
    @Support
    Select<R> unionAll(Select<? extends R> select);

    /**
     * Apply the <code>EXCEPT</code> (or <code>MINUS</code>) set operation.
     * <p>
     * In SQL, an <code>EXCEPT</code> is <code>DISTINCT</code> by default,
     * meaning, duplicates are removed from the result set. So, this is the same
     * as {@link #exceptDistinct(Select)}. If duplicate removal isn't required,
     * or already guaranteed by the data model, it is recommended to use
     * {@link #exceptAll(Select)}, instead, if the underlying RDBMS supports it.
     *
     * @throws IllegalArgumentException If the argument select has the same
     *             identity as this select. The jOOQ 3.x API is mutable, which
     *             means that calls to the DSL API mutate this instance. Adding
     *             this instance as an set operation argument would lead to a
     *             {@link StackOverflowError} when generating the SQL.
     */
    @NotNull @CheckReturnValue
    @Support({ CLICKHOUSE, CUBRID, DERBY, DUCKDB, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    Select<R> except(Select<? extends R> select);

    /**
     * Apply the <code>EXCEPT</code> (or <code>MINUS</code>) set operation.
     * <p>
     * In SQL, an <code>EXCEPT</code> is <code>DISTINCT</code> by default.
     * However, it is often useful to make this explicit to express intent when
     * distinct removal is really desired.
     *
     * @throws IllegalArgumentException If the argument select has the same
     *             identity as this select. The jOOQ 3.x API is mutable, which
     *             means that calls to the DSL API mutate this instance. Adding
     *             this instance as an set operation argument would lead to a
     *             {@link StackOverflowError} when generating the SQL.
     */
    @NotNull @CheckReturnValue
    @Support({ CLICKHOUSE, CUBRID, DERBY, DUCKDB, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    Select<R> exceptDistinct(Select<? extends R> select);

    /**
     * Apply the <code>EXCEPT ALL</code> set operation.
     *
     * @throws IllegalArgumentException If the argument select has the same
     *             identity as this select. The jOOQ 3.x API is mutable, which
     *             means that calls to the DSL API mutate this instance. Adding
     *             this instance as an set operation argument would lead to a
     *             {@link StackOverflowError} when generating the SQL.
     */
    @NotNull @CheckReturnValue
    @Support({ CLICKHOUSE, CUBRID, DERBY, DUCKDB, HSQLDB, MARIADB, MYSQL, POSTGRES, TRINO, YUGABYTEDB })
    Select<R> exceptAll(Select<? extends R> select);

    /**
     * Apply the <code>INTERSECT</code> set operation.
     * <p>
     * In SQL, an <code>INTERSECT</code> is <code>DISTINCT</code> by default,
     * meaning, duplicates are removed from the result set. So, this is the same
     * as {@link #intersectDistinct(Select)}. If duplicate removal isn't
     * required, or already guaranteed by the data model, it is recommended to
     * use {@link #intersectAll(Select)}, instead, if the underlying RDBMS
     * supports it. Apply the <code>INTERSECT</code> set operation.
     *
     * @throws IllegalArgumentException If the argument select has the same
     *             identity as this select. The jOOQ 3.x API is mutable, which
     *             means that calls to the DSL API mutate this instance. Adding
     *             this instance as an set operation argument would lead to a
     *             {@link StackOverflowError} when generating the SQL.
     */
    @NotNull @CheckReturnValue
    @Support({ CLICKHOUSE, CUBRID, DERBY, DUCKDB, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    Select<R> intersect(Select<? extends R> select);

    /**
     * Apply the <code>INTERSECT</code> set operation.
     * <p>
     * In SQL, a <code>INTERSECT</code> is <code>DISTINCT</code> by default.
     * However, it is often useful to make this explicit to express intent when
     * distinct removal is really desired.
     *
     * @throws IllegalArgumentException If the argument select has the same
     *             identity as this select. The jOOQ 3.x API is mutable, which
     *             means that calls to the DSL API mutate this instance. Adding
     *             this instance as an set operation argument would lead to a
     *             {@link StackOverflowError} when generating the SQL.
     */
    @NotNull @CheckReturnValue
    @Support({ CLICKHOUSE, CUBRID, DERBY, DUCKDB, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    Select<R> intersectDistinct(Select<? extends R> select);

    /**
     * Apply the <code>INTERSECT ALL</code> set operation.
     *
     * @throws IllegalArgumentException If the argument select has the same
     *             identity as this select. The jOOQ 3.x API is mutable, which
     *             means that calls to the DSL API mutate this instance. Adding
     *             this instance as an set operation argument would lead to a
     *             {@link StackOverflowError} when generating the SQL.
     */
    @NotNull @CheckReturnValue
    @Support({ CLICKHOUSE, CUBRID, DERBY, DUCKDB, HSQLDB, MARIADB, MYSQL, POSTGRES, TRINO, YUGABYTEDB })
    Select<R> intersectAll(Select<? extends R> select);

    /**
     * All fields selected in this query
     */
    @NotNull @CheckReturnValue
    List<Field<?>> getSelect();

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    /**
     * Experimental query object model accessor method, see also {@link QOM}.
     * Subject to change in future jOOQ versions, use at your own risk.
     */
    @Experimental
    @Nullable With $with();

    /**
     * Experimental query object model accessor method, see also {@link QOM}.
     * Subject to change in future jOOQ versions, use at your own risk.
     */
    @Experimental
    @CheckReturnValue
    @NotNull Select<?> $with(With newWith);

    /**
     * Experimental query object model accessor method, see also {@link QOM}.
     * Subject to change in future jOOQ versions, use at your own risk.
     */
    @Experimental
    @NotNull UnmodifiableList<? extends SelectFieldOrAsterisk> $select();

    /**
     * Experimental query object model accessor method, see also {@link QOM}.
     * Subject to change in future jOOQ versions, use at your own risk.
     */
    @Experimental
    @CheckReturnValue
    @NotNull Select<?> $select(Collection<? extends SelectFieldOrAsterisk> newSelect);

    /**
     * Experimental query object model accessor method, see also {@link QOM}.
     * Subject to change in future jOOQ versions, use at your own risk.
     */
    @Experimental
    boolean $distinct();

    /**
     * Experimental query object model accessor method, see also {@link QOM}.
     * Subject to change in future jOOQ versions, use at your own risk.
     */
    @Experimental
    @CheckReturnValue
    @NotNull Select<R> $distinct(boolean newDistinct);

    /**
     * Experimental query object model accessor method, see also {@link QOM}.
     * Subject to change in future jOOQ versions, use at your own risk.
     */
    @Experimental
    @NotNull UnmodifiableList<? extends SelectFieldOrAsterisk> $distinctOn();

    /**
     * Experimental query object model accessor method, see also {@link QOM}.
     * Subject to change in future jOOQ versions, use at your own risk.
     */
    @Experimental
    @CheckReturnValue
    @NotNull Select<R> $distinctOn(Collection<? extends SelectFieldOrAsterisk> newDistinctOn);




















    /**
     * Experimental query object model accessor method, see also {@link QOM}.
     * Subject to change in future jOOQ versions, use at your own risk.
     */
    @Experimental
    @Nullable Table<?> $intoTable();

    /**
     * Experimental query object model accessor method, see also {@link QOM}.
     * Subject to change in future jOOQ versions, use at your own risk.
     */
    @Experimental
    @CheckReturnValue
    @NotNull Select<R> $intoTable(Table<?> newIntoTable);

    /**
     * Experimental query object model accessor method, see also {@link QOM}.
     * Subject to change in future jOOQ versions, use at your own risk.
     */
    @Experimental
    @NotNull UnmodifiableList<? extends Table<?>> $from();

    /**
     * Experimental query object model accessor method, see also {@link QOM}.
     * Subject to change in future jOOQ versions, use at your own risk.
     */
    @Experimental
    @CheckReturnValue
    @NotNull Select<R> $from(Collection<? extends Table<?>> newFrom);

    /**
     * Experimental query object model accessor method, see also {@link QOM}.
     * Subject to change in future jOOQ versions, use at your own risk.
     */
    @Experimental
    @Nullable Condition $where();

    /**
     * Experimental query object model accessor method, see also {@link QOM}.
     * Subject to change in future jOOQ versions, use at your own risk.
     */
    @Experimental
    @CheckReturnValue
    @NotNull Select<R> $where(Condition newWhere);

    /**
     * Experimental query object model accessor method, see also {@link QOM}.
     * Subject to change in future jOOQ versions, use at your own risk.
     */
    @Experimental
    @NotNull UnmodifiableList<? extends GroupField> $groupBy();

    /**
     * Experimental query object model accessor method, see also {@link QOM}.
     * Subject to change in future jOOQ versions, use at your own risk.
     */
    @Experimental
    @CheckReturnValue
    @NotNull Select<R> $groupBy(Collection<? extends GroupField> newGroupBy);

    /**
     * Experimental query object model accessor method, see also {@link QOM}.
     * Subject to change in future jOOQ versions, use at your own risk.
     */
    @Experimental
    boolean $groupByDistinct();

    /**
     * Experimental query object model accessor method, see also {@link QOM}.
     * Subject to change in future jOOQ versions, use at your own risk.
     */
    @Experimental
    @CheckReturnValue
    @NotNull Select<R> $groupByDistinct(boolean newGroupByDistinct);

    /**
     * Experimental query object model accessor method, see also {@link QOM}.
     * Subject to change in future jOOQ versions, use at your own risk.
     */
    @Experimental
    @Nullable Condition $having();

    /**
     * Experimental query object model accessor method, see also {@link QOM}.
     * Subject to change in future jOOQ versions, use at your own risk.
     */
    @Experimental
    @CheckReturnValue
    @NotNull Select<R> $having(Condition newHaving);

    /**
     * Experimental query object model accessor method, see also {@link QOM}.
     * Subject to change in future jOOQ versions, use at your own risk.
     */
    @Experimental
    @NotNull UnmodifiableList<? extends WindowDefinition> $window();

    /**
     * Experimental query object model accessor method, see also {@link QOM}.
     * Subject to change in future jOOQ versions, use at your own risk.
     */
    @Experimental
    @CheckReturnValue
    @NotNull Select<R> $window(Collection<? extends WindowDefinition> newWindow);

    /**
     * Experimental query object model accessor method, see also {@link QOM}.
     * Subject to change in future jOOQ versions, use at your own risk.
     */
    @Experimental
    @Nullable Condition $qualify();

    /**
     * Experimental query object model accessor method, see also {@link QOM}.
     * Subject to change in future jOOQ versions, use at your own risk.
     */
    @Experimental
    @CheckReturnValue
    @NotNull Select<R> $qualify(Condition newQualify);

    /**
     * Experimental query object model accessor method, see also {@link QOM}.
     * Subject to change in future jOOQ versions, use at your own risk.
     */
    @Experimental
    @NotNull UnmodifiableList<? extends SortField<?>> $orderBy();

    /**
     * Experimental query object model accessor method, see also {@link QOM}.
     * Subject to change in future jOOQ versions, use at your own risk.
     */
    @Experimental
    @CheckReturnValue
    @NotNull Select<R> $orderBy(Collection<? extends SortField<?>> newOrderBy);

    /**
     * Experimental query object model accessor method, see also {@link QOM}.
     * Subject to change in future jOOQ versions, use at your own risk.
     */
    @Experimental
    @Nullable Field<? extends Number> $limit();

    /**
     * Experimental query object model accessor method, see also {@link QOM}.
     * Subject to change in future jOOQ versions, use at your own risk.
     */
    @Experimental
    @CheckReturnValue
    @NotNull Select<R> $limit(Field<? extends Number> newLimit);

    /**
     * Experimental query object model accessor method, see also {@link QOM}.
     * Subject to change in future jOOQ versions, use at your own risk.
     */
    @Experimental
    boolean $limitPercent();

    /**
     * Experimental query object model accessor method, see also {@link QOM}.
     * Subject to change in future jOOQ versions, use at your own risk.
     */
    @Experimental
    @CheckReturnValue
    @NotNull Select<R> $limitPercent(boolean newLimitPercent);

    /**
     * Experimental query object model accessor method, see also {@link QOM}.
     * Subject to change in future jOOQ versions, use at your own risk.
     */
    @Experimental
    boolean $limitWithTies();

    /**
     * Experimental query object model accessor method, see also {@link QOM}.
     * Subject to change in future jOOQ versions, use at your own risk.
     */
    @Experimental
    @CheckReturnValue
    @NotNull Select<R> $limitWithTies(boolean newLimitWithTies);

    /**
     * Experimental query object model accessor method, see also {@link QOM}.
     * Subject to change in future jOOQ versions, use at your own risk.
     */
    @Experimental
    @Nullable Field<? extends Number> $offset();

    /**
     * Experimental query object model accessor method, see also {@link QOM}.
     * Subject to change in future jOOQ versions, use at your own risk.
     */
    @Experimental
    @CheckReturnValue
    @NotNull Select<R> $offset(Field<? extends Number> newOffset);

















































}
