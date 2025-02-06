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
// ...
// ...
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.HSQLDB;
// ...
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.YUGABYTEDB;

import org.jooq.impl.QOM;
import org.jooq.impl.QOM.UnmodifiableList;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus.Experimental;

/**
 * A derived column list.
 * <p>
 * Thist type models a table name and an optional "derived column list", which
 * can be used to name both tables and columns in one go, e.g. when aliasing a
 * derived table or a {@link CommonTableExpression}.
 * <p>
 * <strong>Example:</strong>
 * <p>
 * <pre><code>
 * // Assuming import static org.jooq.impl.DSL.*;
 *
 * Table&lt;?&gt; t = name("t").fields("v").as(select(one()));
 * //           ^^^^^^^^^^^^^^^^^^^^^ -- DerivedColumnList
 *
 * using(configuration)
 *    .select()
 *    .from(t)
 *    .fetch();
 * </code></pre>
 * <p>
 * Instances can be created using {@link Name#fields(String...)} and overloads.
 *
 * @author Lukas Eder
 */
public interface DerivedColumnList extends QueryPart {

    /**
     * Specify a subselect to refer to by the <code>DerivedColumnList</code> to
     * form a common table expression.
     * <p>
     * A common table expression renders itself differently, depending on
     * {@link Context#declareCTE()}. There are two rendering modes:
     * <ul>
     * <li>Declaration: The common table expression renders its CTE name
     * (<code>this</code>) along with the <code>AS (query)</code> clause. This
     * typically happens in <code>WITH</code> clauses.</li>
     * <li>Reference: The common table expression renders its alias identifier.
     * This happens everywhere else.</li>
     * </ul>
     */
    @NotNull
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB })
    <R extends Record> CommonTableExpression<R> as(ResultQuery<R> query);

    /**
     * Specify a materialized subselect to refer to by the
     * <code>DerivedColumnList</code> to form a common table expression.
     * <p>
     * This adds the PostgreSQL 12 <code>MATERIALIZED</code> hint to the common
     * table expression definition, or silently ignores it, if the hint is not
     * supported.
     * <p>
     * A common table expression renders itself differently, depending on
     * {@link Context#declareCTE()}. There are two rendering modes:
     * <ul>
     * <li>Declaration: The common table expression renders its CTE name
     * (<code>this</code>) along with the <code>AS (query)</code> clause. This
     * typically happens in <code>WITH</code> clauses.</li>
     * <li>Reference: The common table expression renders its alias identifier.
     * This happens everywhere else.</li>
     * </ul>
     */
    @NotNull
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB })
    <R extends Record> CommonTableExpression<R> asMaterialized(ResultQuery<R> query);

    /**
     * Specify a non-materialized subselect to refer to by the
     * <code>DerivedColumnList</code> to form a common table expression.
     * <p>
     * This adds the PostgreSQL 12 <code>NOT MATERIALIZED</code> hint to the
     * common table expression definition, or silently ignores it, if the hint
     * is not supported.
     * <p>
     * A common table expression renders itself differently, depending on
     * {@link Context#declareCTE()}. There are two rendering modes:
     * <ul>
     * <li>Declaration: The common table expression renders its CTE name
     * (<code>this</code>) along with the <code>AS (query)</code> clause. This
     * typically happens in <code>WITH</code> clauses.</li>
     * <li>Reference: The common table expression renders its alias identifier.
     * This happens everywhere else.</li>
     * </ul>
     */
    @NotNull
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB })
    <R extends Record> CommonTableExpression<R> asNotMaterialized(ResultQuery<R> query);

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    /**
     * Experimental query object model accessor method, see also {@link QOM}.
     * Subject to change in future jOOQ versions, use at your own risk.
     */
    @Experimental
    @NotNull Name $tableName();

    /**
     * Experimental query object model accessor method, see also {@link QOM}.
     * Subject to change in future jOOQ versions, use at your own risk.
     */
    @Experimental
    @NotNull UnmodifiableList<? extends Name> $columnNames();
}
