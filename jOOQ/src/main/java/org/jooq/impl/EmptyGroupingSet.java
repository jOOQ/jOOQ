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

import static java.util.Arrays.asList;
// ...
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
// ...
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.IGNITE;
// ...
// ...
import static org.jooq.SQLDialect.MARIADB;
// ...
import static org.jooq.SQLDialect.MYSQL;
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...
// ...
import static org.jooq.SQLDialect.SQLITE;
// ...
import static org.jooq.SQLDialect.YUGABYTEDB;
import static org.jooq.impl.DSL.groupingSets;
import static org.jooq.impl.DSL.one;
import static org.jooq.impl.SQLDataType.OTHER;
import static org.jooq.impl.Tools.EMPTY_FIELD;

import java.util.EnumSet;
import java.util.Set;

import org.jooq.Context;
// ...
import org.jooq.SQLDialect;

/**
 * @author Lukas Eder
 */
final class EmptyGroupingSet extends AbstractField<Object> implements QOM.EmptyGroupingSet {

    static final Set<SQLDialect> EMULATE_EMPTY_GROUP_BY_CONSTANT = SQLDialect.supportedUntil(DERBY, HSQLDB, IGNITE);
    static final Set<SQLDialect> EMULATE_EMPTY_GROUP_BY_OTHER    = SQLDialect.supportedUntil(FIREBIRD, MARIADB, MYSQL, SQLITE, YUGABYTEDB);
















    static final EmptyGroupingSet INSTANCE = new EmptyGroupingSet();

    private EmptyGroupingSet() {
        super(DSL.name("()"), OTHER);
    }

    @Override
    public final void accept(Context<?> ctx) {

        // [#4292] Some dialects accept constant expressions in GROUP BY
        // Note that dialects may consider constants as indexed field
        // references, as in the ORDER BY clause!
        if (EMULATE_EMPTY_GROUP_BY_CONSTANT.contains(ctx.dialect()))
            ctx.sql('0');

        // [#4447] CUBRID can't handle subqueries in GROUP BY
        else if (ctx.family() == CUBRID)
            ctx.sql("1 + 0");











        // [#4292] Some dialects don't support empty GROUP BY () clauses
        else if (EMULATE_EMPTY_GROUP_BY_OTHER.contains(ctx.dialect()))
            ctx.sql('(').visit(DSL.select(one())).sql(')');

        // Few dialects support the SQL standard "grand total" (i.e. empty grouping set)
        else

        ctx.sql("()");
    }
}
