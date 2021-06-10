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

import static org.jooq.SQLDialect.*;

import org.jetbrains.annotations.NotNull;

/**
 * This type is part of the jOOQ DSL to create {@link Select}, {@link Insert},
 * {@link Update}, {@link Delete}, {@link Merge} statements prefixed with a
 * <code>WITH</code> clause and with {@link CommonTableExpression}s.
 * <p>
 * Example:
 * <code><pre>
 * DSL.with("table", "col1", "col2")
 *    .as(
 *        select(one(), two())
 *    )
 *    .select()
 *    .from("table")
 * </pre></code>
 *
 * @author Lukas Eder
 */
public interface WithAsStep8 {

    /**
     * Associate a subselect with a common table expression's table and column
     * names.
     */
    @NotNull @CheckReturnValue
    @Support({ FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE })
    WithStep as(ResultQuery<? extends Record8<?, ?, ?, ?, ?, ?, ?, ?>> query);

    /**
     * Associate a materialized subselect with a common table expression's table
     * and column names.
     * <p>
     * This adds the PostgreSQL 12 <code>MATERIALIZED</code> hint to the common
     * table expression definition, or silently ignores it, if the hint is not
     * supported.
     */
    @NotNull @CheckReturnValue
    @Support({ FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE })
    WithStep asMaterialized(ResultQuery<? extends Record8<?, ?, ?, ?, ?, ?, ?, ?>> query);

    /**
     * Associate a non-materialized subselect with a common table expression's
     * table and column names.
     * <p>
     * This adds the PostgreSQL 12 <code>NOT MATERIALIZED</code> hint to the
     * common table expression definition, or silently ignores it, if the hint
     * is not supported.
     */
    @NotNull @CheckReturnValue
    @Support({ FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE })
    WithStep asNotMaterialized(ResultQuery<? extends Record8<?, ?, ?, ?, ?, ?, ?, ?>> query);
}
