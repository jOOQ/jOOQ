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
package org.jooq.impl;

// ...
// ...
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
import static org.jooq.SQLDialect.FIREBIRD;
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
// ...
// ...
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
// ...
import static org.jooq.SQLDialect.TRINO;
// ...
import static org.jooq.SQLDialect.YUGABYTEDB;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.selectCount;
import static org.jooq.impl.Keywords.K_IS_NULL;
import static org.jooq.impl.SubqueryCharacteristics.PREDICAND;
import static org.jooq.impl.Tools.allNull;
import static org.jooq.impl.Tools.collect;
import static org.jooq.impl.Tools.fieldNames;
import static org.jooq.impl.Tools.fieldsByName;
import static org.jooq.impl.Tools.flattenCollection;
import static org.jooq.impl.Tools.visitSubquery;

import java.util.List;
import java.util.Set;

import org.jooq.Clause;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Function1;
import org.jooq.Name;
import org.jooq.SQLDialect;
import org.jooq.Select;
import org.jooq.Table;

/**
 * @author Lukas Eder
 */
final class SelectIsNull extends AbstractCondition implements QOM.SelectIsNull {

    static final Set<SQLDialect> EMULATE_NULL_QUERY = SQLDialect.supportedBy(CLICKHOUSE, CUBRID, DERBY, DUCKDB, FIREBIRD, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE, TRINO, YUGABYTEDB);

    final Select<?>              select;

    SelectIsNull(Select<?> select) {
        this.select = select;
    }

    @Override
    final boolean isNullable() {
        return false;
    }

    @Override
    public final void accept(Context<?> ctx) {






        if (EMULATE_NULL_QUERY.contains(ctx.dialect())) {

            // [#11011] Avoid the RVE IS NULL emulation for queries of degree 1
            // [#16319] Flatten embeddables to find collection size
            List<Field<?>> f = collect(flattenCollection(select.getSelect()));
            if (f.size() == 1) {
                acceptStandard(ctx);
            }
            else {
                Name[] n = fieldNames(f.size());
                Table<?> t = new AliasedSelect<>(select, true, true, false, n).as("t");
                ctx.visit(inline(1).eq(selectCount().from(t).where(allNull(fieldsByName(n)))));
            }
        }
        else
            acceptStandard(ctx);
    }

    private final void acceptStandard(Context<?> ctx) {
        visitSubquery(ctx, select, PREDICAND);

        switch (ctx.family()) {






            default:
                ctx.sql(' ')
                   .visit(K_IS_NULL);
                break;
        }
    }

    @Override // Avoid AbstractCondition implementation
    public final Clause[] clauses(Context<?> ctx) {
        return null;
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Select<?> $arg1() {
        return select;
    }

    @Override
    public final Function1<? super Select<?>, ? extends QOM.SelectIsNull> $constructor() {
        return r -> new SelectIsNull(r);
    }
}
