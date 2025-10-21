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

import static java.lang.Boolean.FALSE;
import static java.util.Collections.emptyList;
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
// ...
import static org.jooq.SQLDialect.DUCKDB;
import static org.jooq.SQLDialect.H2;
// ...
import static org.jooq.SQLDialect.HSQLDB;
// ...
import static org.jooq.SQLDialect.MARIADB;
// ...
import static org.jooq.SQLDialect.MYSQL;
import static org.jooq.SQLDialect.POSTGRES;
// ...
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
// ...
// ...
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.query;
import static org.jooq.impl.DSL.xmlserializeContent;
import static org.jooq.impl.DSL.xmlserializeDocument;
import static org.jooq.impl.Keywords.K_SEPARATOR;
import static org.jooq.impl.Names.N_GROUP_CONCAT;
import static org.jooq.impl.Names.N_LIST;
import static org.jooq.impl.Names.N_LISTAGG;
import static org.jooq.impl.Names.N_STRING_AGG;
import static org.jooq.impl.Names.N_XMLTEXT;
import static org.jooq.impl.QueryPartListView.wrap;
import static org.jooq.impl.SQLDataType.VARCHAR;
import static org.jooq.impl.SQLDataType.XML;
import static org.jooq.impl.Tools.appendSQL;
import static org.jooq.impl.Tools.castIfNeeded;
import static org.jooq.impl.Tools.prependSQL;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_GROUP_CONCAT_MAX_LEN_SET;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.Function;

import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Function2;
// ...
import org.jooq.QueryPart;
// ...
import org.jooq.SQLDialect;
import org.jooq.SortField;
import org.jooq.XML;
import org.jooq.impl.QOM.FrameUnits;
import org.jooq.impl.QOM.UnmodifiableList;

import org.jetbrains.annotations.NotNull;

/**
 * @author Lukas Eder
 */
final class ListAgg
extends
    AbstractAggregateFunction<String, ListAgg>
implements
    QOM.ListAgg
{

    static final Set<SQLDialect> SET_GROUP_CONCAT_MAX_LEN          = SQLDialect.supportedBy(MARIADB, MYSQL);
    static final Set<SQLDialect> SUPPORT_GROUP_CONCAT              = SQLDialect.supportedBy(CUBRID, H2, HSQLDB, MARIADB, MYSQL, SQLITE);
    static final Set<SQLDialect> SUPPORT_STRING_AGG                = SQLDialect.supportedBy(DUCKDB, POSTGRES);





    static final Field<String>   DEFAULT_SEPARATOR                 = DSL.inline(",");

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

        // [#19255] Some dialects do not support aggregate ORDER BY in window functions
        if (emulateWindowAggregateOrderBy(ctx)) {
            acceptWindowAggregateOrderByEmulation(ctx);
        }
        else if (SUPPORT_GROUP_CONCAT.contains(ctx.dialect())) {

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

    @Override
    final Field<?> applyMap(Context<?> ctx, Field<?> arg) {
        switch (ctx.family()) {
            case TRINO:
                return arg.getDataType().isString() ? arg : arg.cast(VARCHAR);
            default:
                return arg;
        }
    }

    @Override
    final boolean applyFilterToArgument(Context<?> ctx, Field<?> arg, int i) {
        return i == 0;
    }

    @Override
    boolean supportsFilter(Context<?> ctx) {
        switch (ctx.family()) {
            case TRINO:
                return false;
            default:
                return super.supportsFilter(ctx);
        }
    }

    /**
     * [#1273] <code>LIST_AGG</code> emulation for MySQL
     */
    private final void acceptGroupConcat(Context<?> ctx) {
        ctx.visit(N_GROUP_CONCAT).sql('(');
        acceptArguments1(ctx, new QueryPartListView<>(arguments.get(0)));
        acceptOrderBy(ctx);

        if (arguments.size() > 1) {
            if (ctx.family() == SQLITE) {

                // [#16666] SQLite's GROUP_CONCAT(DISTINCT ..) function doesn't support a second argument
                if (!distinct || !DEFAULT_SEPARATOR.equals(arguments.get(1)))
                    ctx.sql(", ").visit(arguments.get(1));
            }
            else
                ctx.sql(' ').visit(K_SEPARATOR).sql(' ')
                   .visit(arguments.get(1));
        }

        ctx.sql(')');

        acceptFilterClause(ctx);
        acceptOverClause(ctx);
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

        // The explicit cast is needed in Postgres
        QueryPartListView<Field<?>> args =  wrap(castIfNeeded((Field<?>) arguments.get(0), String.class));
        acceptArguments1(ctx, args);

        if (arguments.size() > 1)
            ctx.sql(", ").visit(arguments.get(1));
        else
            ctx.sql(", ").visit(inline(""));




        acceptOrderBy(ctx);
        ctx.sql(')');
    }




































    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<?> $arg1() {
        return getArgument(0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Field<String> $arg2() {
        return (Field<String>) getArgument(1);
    }

    @Override
    public final Function2<? super Field<?>, ? super Field<String>, ? extends QOM.ListAgg> $constructor() {
        return (a1, a2) -> new ListAgg(distinct, a1, a2);
    }

    @Override
    final ListAgg copy2(Function<ListAgg, ListAgg> function) {
        return function.apply(new ListAgg(distinct, getArgument(0), (Field) getArgument(1)));
    }
}
