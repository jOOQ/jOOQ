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
import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
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
// ...
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
// ...
// ...
import static org.jooq.impl.DSL.*;
import static org.jooq.impl.Names.*;
import static org.jooq.impl.SQLDataType.*;

import java.util.Set;

import org.jooq.Condition;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.SQLDialect;

/**
 * @author Lukas Eder
 */
final class BoolOr extends DefaultAggregateFunction<Boolean> {
    private static final Set<SQLDialect> EMULATE          = SQLDialect.supportedBy(CUBRID, DERBY, FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, SQLITE);

    private final Condition              condition;

    BoolOr(Condition condition) {
        super(N_BOOL_OR, BOOLEAN, DSL.field(condition));

        this.condition = condition;
    }

    @Override
    final void acceptFunctionName(Context<?> ctx) {
        switch (ctx.family()) {















            default:
                super.acceptFunctionName(ctx);
                break;
        }
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (EMULATE.contains(ctx.dialect()))
            ctx.visit(DSL.field(fo(DSL.max(DSL.when(condition, one()).otherwise(zero()))).eq(one())));
        else
            super.accept(ctx);
    }
}
