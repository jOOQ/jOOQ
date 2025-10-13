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
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
// ...
// ...
// ...
import static org.jooq.impl.DSL.arrayAgg;
import static org.jooq.impl.DSL.arrayAggDistinct;
import static org.jooq.impl.DSL.function;
import static org.jooq.impl.DSL.groupConcat;
import static org.jooq.impl.DSL.groupConcatDistinct;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.jsonArray;
import static org.jooq.impl.JSONEntryImpl.jsonCast;
import static org.jooq.impl.JSONEntryImpl.jsonCastMapper;
import static org.jooq.impl.JSONEntryImpl.jsonMerge;
import static org.jooq.impl.Keywords.K_AS;
import static org.jooq.impl.Keywords.K_DISTINCT;
import static org.jooq.impl.Keywords.K_IS_NOT_NULL;
import static org.jooq.impl.Keywords.K_NESTED;
import static org.jooq.impl.Keywords.K_PATH;
import static org.jooq.impl.Keywords.K_REPLACE;
import static org.jooq.impl.Names.N_ARRAY_AGG;
import static org.jooq.impl.Names.N_CAST;
import static org.jooq.impl.Names.N_FILTER;
import static org.jooq.impl.Names.N_GROUP_CONCAT;
import static org.jooq.impl.Names.N_JSONB_AGG;
import static org.jooq.impl.Names.N_JSON_AGG;
import static org.jooq.impl.Names.N_JSON_ARRAYAGG;
import static org.jooq.impl.Names.N_JSON_GROUP_ARRAY;
import static org.jooq.impl.Names.N_JSON_QUOTE;
import static org.jooq.impl.Names.N_JSON_STRIP_NULLS;
import static org.jooq.impl.Names.N_JSON_TRANSFORM;
import static org.jooq.impl.Names.N_TO_JSON;
import static org.jooq.impl.Names.N_toJSONString;
import static org.jooq.impl.QOM.JSONOnNull.ABSENT_ON_NULL;
import static org.jooq.impl.QOM.JSONOnNull.NULL_ON_NULL;
import static org.jooq.impl.QueryPartListView.wrap;
import static org.jooq.impl.SQLDataType.BLOB;
import static org.jooq.impl.SQLDataType.INTEGER;
import static org.jooq.impl.SQLDataType.JSON;
import static org.jooq.impl.SQLDataType.JSONB;
import static org.jooq.impl.SQLDataType.VARCHAR;

import java.util.Collection;
import java.util.Set;
import java.util.function.Function;

import org.jooq.AggregateFilterStep;
import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Function1;
import org.jooq.JSONArrayAggOrderByStep;
import org.jooq.OrderField;
// ...
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.Scope;
import org.jooq.Select;
import org.jooq.SelectGroupByStep;
import org.jooq.impl.QOM.JSONOnNull;


/**
 * The JSON array constructor.
 *
 * @author Lukas Eder
 */
final class JSONArrayAgg<J>
extends
    AbstractAggregateFunction<J>
implements
    JSONArrayAggOrderByStep<J>,
    QOM.JSONArrayAgg<J>
{

    static final Set<SQLDialect> EMULATE_WITH_GROUP_CONCAT = SQLDialect.supportedBy(MARIADB, MYSQL);







    private JSONOnNull           onNull;
    private DataType<?>          returning;

    JSONArrayAgg(DataType<J> type, Field<?> arg, boolean distinct) {
        super(distinct, N_JSON_ARRAYAGG, type, arg);
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {
            case MARIADB:
            case MYSQL: {
                // Workaround for https://jira.mariadb.org/browse/MDEV-21912,
                // https://jira.mariadb.org/browse/MDEV-21914, and other issues
                ctx.visit(jsonMerge(ctx, "[]", groupConcatEmulation(ctx)));
                break;
            }
































































            case POSTGRES:
            case YUGABYTEDB:
                ctx.visit(getDataType() == JSON ? N_JSON_AGG : N_JSONB_AGG).sql('(');
                acceptDistinct(ctx);
                ctx.visit(arguments.get(0));
                acceptOrderBy(ctx);
                ctx.sql(')');

                if (onNull == ABSENT_ON_NULL)
                    acceptFilterClause(ctx, f(arguments.get(0).isNotNull()));
                else
                    acceptFilterClause(ctx);

                acceptOverClause(ctx);
                break;

            case DUCKDB: {
                Field<?> agg = arrayAggEmulation(distinct, arguments.get(0), withinGroupOrderBy);

                ctx.visit(N_TO_JSON).sql('(').visit(agg).sql(')');

                break;
            }

            case CLICKHOUSE: {
                Field<?> agg = arrayAggEmulation(distinct, arguments.get(0), withinGroupOrderBy);

                ctx.visit(N_toJSONString).sql('(').visit(
                    arguments.get(0).getDataType().isJSON()
                       ? agg.cast(JSON.array())
                       : agg
                ).sql(')');

                break;
            }

            case SQLITE:
                ctx.visit(N_JSON_GROUP_ARRAY).sql('(');
                acceptDistinct(ctx);
                ctx.visit(arguments.get(0));
                acceptOrderBy(ctx);
                ctx.sql(')');

                if (onNull == ABSENT_ON_NULL)
                    acceptFilterClause(ctx, f(arguments.get(0).isNotNull()));
                else
                    acceptFilterClause(ctx);

                acceptOverClause(ctx);
                break;

            case TRINO: {
                boolean noAggregateFilter = onNull == JSONOnNull.ABSENT_ON_NULL && !supportsFilter(ctx);

                ctx.visit(N_CAST).sql('(');

                if (noAggregateFilter)
                    ctx.visit(N_FILTER).sql('(');

                ctx.visit(N_ARRAY_AGG).sql('(');
                acceptDistinct(ctx);
                ctx.visit(jsonCast(ctx, arguments.get(0)));
                acceptOrderBy(ctx);
                ctx.sql(')');

                if (onNull == ABSENT_ON_NULL)
                    acceptFilterClause(ctx, f(arguments.get(0).isNotNull()));
                else
                    acceptFilterClause(ctx);

                acceptOverClause(ctx);

                if (noAggregateFilter)
                    ctx.sql(", v -> v ").visit(K_IS_NOT_NULL).sql(')');

                ctx.sql(' ').visit(K_AS).sql(' ').visit(JSON);
                ctx.sql(')');
                break;
            }

            default:
                acceptStandard(ctx);
                break;
        }
    }

    @SuppressWarnings("unchecked")
    private final Field<?> groupConcatEmulation(Context<?> ctx) {
        Field<?> arg1 = arguments.get(0);

        if (arg1.getDataType().isString()) {
            switch (ctx.family()) {
                case MARIADB:
                case MYSQL:
                    arg1 = function(N_JSON_QUOTE, getDataType(), arg1);
                    break;






            }
        }













        Field<?> arg2 = arg1;
        return DSL.concat(
            inline('['),
            CustomField.of(N_GROUP_CONCAT, VARCHAR, c1 -> {
                c1.visit(groupConcatEmulationWithoutArrayWrappers(
                    distinct,
                    CustomField.of(Names.N_FIELD, VARCHAR, c2 -> acceptArguments2(c2, QueryPartListView.wrap(arg2))),
                    withinGroupOrderBy
                ));
                acceptFilterClause(ctx);
                acceptOverClause(c1);
            }),
            inline(']')
        );
    }

    static final Field<?> groupConcatEmulationWithoutArrayWrappers(boolean distinct, Field<?> field, SortFieldList orderBy) {
        return Tools.apply(
            distinct ? groupConcatDistinct(field) : groupConcat(field),
            agg -> Tools.isEmpty(orderBy) ? agg : agg.orderBy(orderBy)
        );
    }

    final Field<?> arrayAggEmulation(boolean d, Field<?> field, SortFieldList orderBy) {
        return fo(
            Tools.apply(
                d ? arrayAggDistinct(field) : arrayAgg(field),
                agg -> (AggregateFilterStep<?>) (Tools.isEmpty(orderBy) ? agg : agg.orderBy(orderBy))
            ),
            onNull == JSONOnNull.ABSENT_ON_NULL ? f(field.isNotNull()) : DSL.noCondition()
        );
    }

















    private final void acceptStandard(Context<?> ctx) {
        acceptStandard(ctx, null, onNull);
    }

    private final void acceptStandard(Context<?> ctx, Function<? super Field<?>, ? extends Field<?>> mapper, JSONOnNull onNull0) {
        ctx.visit(N_JSON_ARRAYAGG).sql('(');
        acceptDistinct(ctx);
        acceptArguments3(ctx, mapper == null ? arguments : wrap(arguments).map(mapper), jsonCastMapper(ctx));
        acceptOrderBy(ctx);

        JSONNull jsonNull = new JSONNull(onNull0);
        if (jsonNull.rendersContent(ctx))
            ctx.sql(' ').visit(jsonNull);

        JSONReturning jsonReturning = new JSONReturning(returning);
        if (jsonReturning.rendersContent(ctx))
            ctx.sql(' ').visit(jsonReturning);

        ctx.sql(')');

        acceptFilterClause(ctx);
        acceptOverClause(ctx);
    }

    private final void acceptDistinct(Context<?> ctx) {
        if (distinct)
            ctx.visit(K_DISTINCT).sql(' ');
    }

    @Override
    public final JSONArrayAgg<J> nullOnNull() {
        onNull = NULL_ON_NULL;
        return this;
    }

    @Override
    public final JSONArrayAgg<J> absentOnNull() {
        onNull = ABSENT_ON_NULL;
        return this;
    }

    @Override
    public final JSONArrayAgg<J> returning(DataType<?> r) {
        this.returning = r;
        return this;
    }

    @Override
    public final JSONArrayAgg<J> orderBy(OrderField<?>... fields) {
        return (JSONArrayAgg<J>) super.orderBy(fields);
    }

    @Override
    public final JSONArrayAgg<J> orderBy(Collection<? extends OrderField<?>> fields) {
        return (JSONArrayAgg<J>) super.orderBy(fields);
    }

    static final <R extends Record> Select<R> patchOracleArrayAggBug(Scope scope, SelectGroupByStep<R> select) {

















        return select;
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<?> $arg1() {
        return getArguments().get(0);
    }

    @Override
    public final JSONOnNull $onNull() {
        return onNull;
    }

    @Override
    public final DataType<?> $returning() {
        return returning;
    }

    @Override
    public final Function1<? super Field<?>, ? extends QOM.JSONArrayAgg<J>> $constructor() {
        return f -> {
            JSONArrayAgg<J> r = new JSONArrayAgg<J>(getDataType(), f, distinct);
            r.onNull = onNull;
            r.returning = returning;
            return r;
        };
    }
}
