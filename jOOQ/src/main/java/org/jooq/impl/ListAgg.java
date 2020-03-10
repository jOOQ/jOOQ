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
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.MARIADB;
// ...
import static org.jooq.SQLDialect.MYSQL;
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
import static org.jooq.impl.Keywords.F_CONCAT;
import static org.jooq.impl.Keywords.F_SUBSTR;
import static org.jooq.impl.Keywords.F_XMLAGG;
import static org.jooq.impl.Keywords.F_XMLSERIALIZE;
import static org.jooq.impl.Keywords.F_XMLTEXT;
import static org.jooq.impl.Keywords.K_AS;
import static org.jooq.impl.Keywords.K_DISTINCT;
import static org.jooq.impl.Keywords.K_ORDER_BY;
import static org.jooq.impl.Keywords.K_SEPARATOR;
import static org.jooq.impl.Names.N_GROUP_CONCAT;
import static org.jooq.impl.Names.N_LIST;
import static org.jooq.impl.Names.N_LISTAGG;
import static org.jooq.impl.Names.N_STRING_AGG;
import static org.jooq.impl.Tools.castIfNeeded;

import java.util.Arrays;
import java.util.Set;

import org.jooq.Context;
import org.jooq.Field;
// ...
import org.jooq.SQLDialect;
// ...

/**
 * @author Lukas Eder
 */
final class ListAgg extends DefaultAggregateFunction<String> {

    /**
     * Generated UID
     */
    private static final long            serialVersionUID             = -1760389929938136896L;
    private static final Set<SQLDialect> SUPPORT_GROUP_CONCAT         = SQLDialect.supportedBy(CUBRID, H2, HSQLDB, MARIADB, MYSQL, SQLITE);
    private static final Set<SQLDialect> SUPPORT_STRING_AGG           = SQLDialect.supportedBy(POSTGRES);





    ListAgg(boolean distinct, Field<?> arg) {
        super(distinct, N_LISTAGG, SQLDataType.VARCHAR, arg);
    }

    ListAgg(boolean distinct, Field<?> arg, Field<String> separator) {
        super(distinct, N_LISTAGG, SQLDataType.VARCHAR, arg, separator);
    }

    // -------------------------------------------------------------------------
    // XXX QueryPart API
    // -------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        if (SUPPORT_GROUP_CONCAT.contains(ctx.dialect())) {
            acceptGroupConcat(ctx);
        }
        else if (SUPPORT_STRING_AGG.contains(ctx.dialect())) {
            acceptStringAgg(ctx);
            acceptFilterClause(ctx);
            acceptOverClause(ctx);
        }





        else {
            super.accept(ctx);
        }
    }

    /**
     * [#1273] <code>LIST_AGG</code> emulation for MySQL
     */
    private final void acceptGroupConcat(Context<?> ctx) {
        ctx.visit(N_GROUP_CONCAT).sql('(');
        acceptArguments1(ctx, new QueryPartList<>(Arrays.asList(arguments.get(0))));

        if (!Tools.isEmpty(withinGroupOrderBy))
            ctx.sql(' ').visit(K_ORDER_BY).sql(' ')
               .visit(withinGroupOrderBy);

        if (arguments.size() > 1)
            if (ctx.family() == SQLITE)
                ctx.sql(", ").visit(arguments.get(1));
            else
                ctx.sql(' ').visit(K_SEPARATOR).sql(' ')
                   .visit(arguments.get(1));

        ctx.sql(')');
    }

    /**
     * [#1275] <code>LIST_AGG</code> emulation for Postgres, Sybase
     */
    private final void acceptStringAgg(Context<?> ctx) {
        switch (ctx.family()) {






            default:
                ctx.visit(N_STRING_AGG);
                break;
        }

        ctx.sql('(');

        if (distinct)
            ctx.visit(K_DISTINCT).sql(' ');

        // The explicit cast is needed in Postgres
        ctx.visit(castIfNeeded((Field<?>) arguments.get(0), String.class));

        if (arguments.size() > 1)
            ctx.sql(", ").visit(arguments.get(1));
        else
            ctx.sql(", ''");

        if (!Tools.isEmpty(withinGroupOrderBy))
            ctx.sql(' ').visit(K_ORDER_BY).sql(' ')
               .visit(withinGroupOrderBy);

        ctx.sql(')');
    }

















































}
