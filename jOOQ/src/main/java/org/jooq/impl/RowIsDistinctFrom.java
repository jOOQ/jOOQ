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
package org.jooq.impl;

import static org.jooq.Comparator.IS_DISTINCT_FROM;
import static org.jooq.Comparator.IS_NOT_DISTINCT_FROM;
// ...
// ...
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.DERBY;
// ...
// ...
import static org.jooq.SQLDialect.HSQLDB;
// ...
// ...
import static org.jooq.SQLDialect.MARIADB;
// ...
import static org.jooq.SQLDialect.MYSQL;
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
// ...
// ...
import static org.jooq.impl.DSL.exists;
import static org.jooq.impl.DSL.notExists;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.Keywords.K_IS;
import static org.jooq.impl.Keywords.K_NOT;
import static org.jooq.impl.Tools.visitSubquery;

import java.util.Set;

import org.jooq.Clause;
import org.jooq.Context;
import org.jooq.Record;
import org.jooq.Row;
import org.jooq.SQLDialect;
import org.jooq.Select;

/**
 * @author Lukas Eder
 */
final class RowIsDistinctFrom extends AbstractCondition {
    private static final Set<SQLDialect> EMULATE_DISTINCT            = SQLDialect.supportedBy(CUBRID, DERBY);

    // An emulation may be required only for the version where a subquery is used
    // E.g. in HSQLDB: https://sourceforge.net/p/hsqldb/bugs/1579/
    // Or in PostgreSQL: https://twitter.com/pg_xocolatl/status/1260344255035379714
    private static final Set<SQLDialect> EMULATE_DISTINCT_SELECT     = SQLDialect.supportedBy(HSQLDB, POSTGRES);
    private static final Set<SQLDialect> SUPPORT_DISTINCT_WITH_ARROW = SQLDialect.supportedBy(MARIADB, MYSQL);

    private final Row                    lhs;
    private final Row                    rhsRow;
    private final Select<?>              rhsSelect;
    private final boolean                not;

    RowIsDistinctFrom(Row lhs, Row rhs, boolean not) {
        this.lhs = lhs;
        this.rhsRow = rhs;
        this.rhsSelect = null;
        this.not = not;
    }

    RowIsDistinctFrom(Row lhs, Select<?> rhs, boolean not) {
        this.lhs = lhs;
        this.rhsRow = null;
        this.rhsSelect = rhs;
        this.not = not;
    }

    @Override
    final boolean isNullable() {
        return false;
    }

    @Override
    public final void accept(Context<?> ctx) {

        // [#3511]         These dialects need to emulate the IS DISTINCT FROM predicate,
        //                 optimally using INTERSECT...
        // [#7222] [#7224] Make sure the columns are aliased
        // [#10178]        Special treatment for DISTINCT with subqueries
        if (EMULATE_DISTINCT.contains(ctx.dialect()) || rhsSelect != null && EMULATE_DISTINCT_SELECT.contains(ctx.dialect())) {
            Select<Record> intersect = select(lhs.fields()).intersect(rhsSelect != null ? rhsSelect : select(rhsRow.fields()));
            ctx.visit(not ? exists(intersect) : notExists(intersect));
        }

        // MySQL knows the <=> operator
        else if (SUPPORT_DISTINCT_WITH_ARROW.contains(ctx.dialect())) {
            if (!not)
                ctx.visit(K_NOT).sql('(');

            ctx.visit(lhs).sql(" <=> ");

            if (rhsRow != null)
                ctx.visit(rhsRow);
            else
                visitSubquery(ctx, rhsSelect);

            if (!not)
                ctx.sql(')');
        }

        // SQLite knows the IS / IS NOT predicate
        else if (SQLITE == ctx.family()) {
            ctx.visit(lhs).sql(' ').visit(K_IS).sql(' ');

            if (!not)
                ctx.visit(K_NOT).sql(' ');

            if (rhsRow != null)
                ctx.visit(rhsRow);
            else
                visitSubquery(ctx, rhsSelect);
        }















        // These dialects natively support the IS DISTINCT FROM predicate:
        // H2, Postgres
        else {
            ctx.visit(rhsRow != null
                ? new RowCondition(lhs, rhsRow, not ? IS_NOT_DISTINCT_FROM : IS_DISTINCT_FROM)
                : new RowSubqueryCondition(lhs, rhsSelect, not ? IS_NOT_DISTINCT_FROM : IS_DISTINCT_FROM)
            );
        }
    }

    @Override // Avoid AbstractCondition implementation
    public final Clause[] clauses(Context<?> ctx) {
        return null;
    }
}
