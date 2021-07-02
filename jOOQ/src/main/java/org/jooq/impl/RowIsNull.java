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
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
// ...
// ...
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.selectCount;
import static org.jooq.impl.Keywords.K_IS_NOT_NULL;
import static org.jooq.impl.Keywords.K_IS_NULL;
import static org.jooq.impl.Tools.map;
import static org.jooq.impl.Tools.visitSubquery;

import java.util.Set;

import org.jooq.Clause;
import org.jooq.Condition;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Row;
import org.jooq.SQLDialect;
import org.jooq.Select;
import org.jooq.Table;

/**
 * @author Lukas Eder
 */
final class RowIsNull extends AbstractCondition {

    // Currently not yet supported in SQLite:
    // https://www.sqlite.org/rowvalue.html
    private static final Set<SQLDialect> EMULATE_NULL_ROW   = SQLDialect.supportedBy(CUBRID, DERBY, FIREBIRD, HSQLDB, MARIADB, MYSQL, SQLITE);
    private static final Set<SQLDialect> EMULATE_NULL_QUERY = SQLDialect.supportedBy(CUBRID, DERBY, FIREBIRD, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE);

    private final Row                    row;
    private final Select<?>              select;
    private final boolean                isNull;

    RowIsNull(Row row, boolean isNull) {
        this.row = row;
        this.select = null;
        this.isNull = isNull;
    }

    RowIsNull(Select<?> select, boolean isNull) {
        this.row = null;
        this.select = select;
        this.isNull = isNull;
    }

    @Override
    final boolean isNullable() {
        return false;
    }

    @Override
    public final void accept(Context<?> ctx) {






        if (row != null && EMULATE_NULL_ROW.contains(ctx.dialect()))
            ctx.visit(condition(row.fields()));
        else if (select != null && EMULATE_NULL_QUERY.contains(ctx.dialect())) {

            // [#11011] Avoid the RVE IS NULL emulation for queries of degree 1
            if (select.getSelect().size() == 1) {
                acceptStandard(ctx);
            }
            else {
                Table<?> t = new AliasedSelect<>(select, true, true).as("t");
                ctx.visit(inline(1).eq(selectCount().from(t).where(condition(t.fields()))));
            }
        }
        else
            acceptStandard(ctx);
    }

    private final Condition condition(Field<?>[] fields) {
        return DSL.and(map(fields, f -> isNull ? f.isNull() : f.isNotNull()));
    }

    private final void acceptStandard(Context<?> ctx) {
        if (row != null)
            ctx.visit(row);
        else
            visitSubquery(ctx, select);

        switch (ctx.family()) {






            default:
                ctx.sql(' ')
                   .visit(isNull ? K_IS_NULL : K_IS_NOT_NULL);
                break;
        }
    }

    @Override // Avoid AbstractCondition implementation
    public final Clause[] clauses(Context<?> ctx) {
        return null;
    }
}
