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
package org.jooq;

// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.DERBY;
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
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.YUGABYTE;

import java.util.List;

import org.jooq.impl.DSL;
import org.jooq.impl.QOM.MList;
import org.jooq.impl.QOM.With;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A <code>SELECT</code> statement.
 * <p>
 * <strong>Example:</strong>
 * <p>
 * <code><pre>
 * // Assuming import static org.jooq.impl.DSL.*;
 *
 * using(configuration)
 *    .select(ACTOR.FIRST_NAME, ACTOR.LAST_NAME)
 *    .from(ACTOR)
 *    .fetch();
 * </pre></code>
 * <p>
 * Instances can be created using {@link DSL#select(SelectFieldOrAsterisk...)},
 * or {@link DSLContext#selectQuery()} and overloads.
 *
 * @param <R> The record type being returned by this query
 * @author Lukas Eder
 */
public interface Select<R extends Record> extends ResultQuery<R>, TableLike<R>, FieldLike {

    /**
     * Apply the <code>UNION</code> set operation.
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
     *
     * @throws IllegalArgumentException If the argument select has the same
     *             identity as this select. The jOOQ 3.x API is mutable, which
     *             means that calls to the DSL API mutate this instance. Adding
     *             this instance as an set operation argument would lead to a
     *             {@link StackOverflowError} when generating the SQL.
     */
    @NotNull @CheckReturnValue
    @Support({ CUBRID, DERBY, H2, HSQLDB, IGNITE, MARIADB, POSTGRES, SQLITE, YUGABYTE })
    Select<R> except(Select<? extends R> select);

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
    @Support({ CUBRID, DERBY, HSQLDB, POSTGRES, YUGABYTE })
    Select<R> exceptAll(Select<? extends R> select);

    /**
     * Apply the <code>INTERSECT</code> set operation.
     *
     * @throws IllegalArgumentException If the argument select has the same
     *             identity as this select. The jOOQ 3.x API is mutable, which
     *             means that calls to the DSL API mutate this instance. Adding
     *             this instance as an set operation argument would lead to a
     *             {@link StackOverflowError} when generating the SQL.
     */
    @NotNull @CheckReturnValue
    @Support({ CUBRID, DERBY, H2, HSQLDB, IGNITE, MARIADB, POSTGRES, SQLITE, YUGABYTE })
    Select<R> intersect(Select<? extends R> select);

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
    @Support({ CUBRID, DERBY, HSQLDB, POSTGRES, YUGABYTE })
    Select<R> intersectAll(Select<? extends R> select);

    /**
     * All fields selected in this query
     */
    @NotNull @CheckReturnValue
    List<Field<?>> getSelect();

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Nullable With $with();
    @NotNull MList<? extends SelectFieldOrAsterisk> $select();
    @NotNull Select<?> $select(MList<? extends SelectFieldOrAsterisk> select);
    boolean $distinct();
    @NotNull Select<R> $distinct(boolean distinct);
    @NotNull MList<? extends Table<?>> $from();
    @NotNull Select<R> $from(MList<? extends Table<?>> from);
    @Nullable Condition $where();
    @NotNull Select<R> $where(Condition condition);
    @NotNull MList<? extends GroupField> $groupBy();
    boolean $groupByDistinct();
    @NotNull Select<R> $groupByDistinct(boolean groupByDistinct);
    @Nullable Condition $having();
    @NotNull Select<R> $having(Condition condition);
    @NotNull MList<? extends WindowDefinition> $window();
    @Nullable Condition $qualify();
    @NotNull Select<R> $qualify(Condition condition);
    @NotNull MList<? extends SortField<?>> $orderBy();









}
