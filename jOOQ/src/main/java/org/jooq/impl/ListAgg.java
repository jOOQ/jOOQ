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

import static java.lang.Boolean.FALSE;
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
// ...
import static org.jooq.SQLDialect.H2;
// ...
import static org.jooq.SQLDialect.HSQLDB;
// ...
import static org.jooq.SQLDialect.MARIADB;
// ...
import static org.jooq.SQLDialect.MYSQL;
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
// ...
// ...
import static org.jooq.impl.DSL.query;
import static org.jooq.impl.DSL.sql;
import static org.jooq.impl.Keywords.K_DISTINCT;
import static org.jooq.impl.Keywords.K_SEPARATOR;
import static org.jooq.impl.Names.N_GROUP_CONCAT;
import static org.jooq.impl.Names.N_LIST;
import static org.jooq.impl.Names.N_LISTAGG;
import static org.jooq.impl.Names.N_STRING_AGG;
import static org.jooq.impl.Names.N_XMLSERIALIZE;
import static org.jooq.impl.Names.N_XMLTEXT;
import static org.jooq.impl.SQLDataType.VARCHAR;
import static org.jooq.impl.SQLDataType.XML;
import static org.jooq.impl.Tools.appendSQL;
import static org.jooq.impl.Tools.castIfNeeded;
import static org.jooq.impl.Tools.prependSQL;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_GROUP_CONCAT_MAX_LEN_SET;

import java.util.Set;

import org.jooq.Context;
import org.jooq.Field;
// ...
import org.jooq.SQLDialect;

/**
 * @author Lukas Eder
 */
final class ListAgg extends DefaultAggregateFunction<String> {
    private static final Set<SQLDialect> SET_GROUP_CONCAT_MAX_LEN     = SQLDialect.supportedBy(MARIADB, MYSQL);
    private static final Set<SQLDialect> SUPPORT_GROUP_CONCAT         = SQLDialect.supportedBy(CUBRID, H2, HSQLDB, MARIADB, MYSQL, SQLITE);
    private static final Set<SQLDialect> SUPPORT_STRING_AGG           = SQLDialect.supportedBy(POSTGRES);





    ListAgg(boolean distinct, Field<?> arg) {
        super(distinct, N_LISTAGG, VARCHAR, arg);
    }

    ListAgg(boolean distinct, Field<?> arg, Field<String> separator) {
        super(distinct, N_LISTAGG, VARCHAR, arg, separator);
    }

    // -------------------------------------------------------------------------
    // XXX QueryPart API
    // -------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        if (SUPPORT_GROUP_CONCAT.contains(ctx.dialect())) {

            // [#12092] Prevent silent truncation of GROUP_CONCAT max length
            if (SET_GROUP_CONCAT_MAX_LEN.contains(ctx.dialect())
                    && !FALSE.equals(ctx.settings().isRenderGroupConcatMaxLenSessionVariable())
                    && ctx.data(DATA_GROUP_CONCAT_MAX_LEN_SET) == null) {
                ctx.skipUpdateCounts(2).data(DATA_GROUP_CONCAT_MAX_LEN_SET, true);

                prependSQL(ctx,
                    query("{set} @t = @@group_concat_max_len"),
                    query("{set} @@group_concat_max_len = 4294967295")
                );
                acceptGroupConcat(ctx);
                appendSQL(ctx, query("{set} @@group_concat_max_len = @t"));
            }
            else
                acceptGroupConcat(ctx);
        }
        else if (SUPPORT_STRING_AGG.contains(ctx.dialect())) {
            acceptStringAgg(ctx);
            acceptFilterClause(ctx);




            acceptOverClause(ctx);
        }




        else
            super.accept(ctx);
    }

    @Override
    void acceptFunctionName(Context<?> ctx) {





        super.acceptFunctionName(ctx);
    }

    /**
     * [#1273] <code>LIST_AGG</code> emulation for MySQL
     */
    private final void acceptGroupConcat(Context<?> ctx) {
        ctx.visit(N_GROUP_CONCAT).sql('(');
        acceptArguments1(ctx, new QueryPartListView<>(arguments.get(0)));
        acceptOrderBy(ctx);

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




        acceptOrderBy(ctx);
        ctx.sql(')');
    }




































}
