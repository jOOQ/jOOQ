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

import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.impl.DSL.groupConcat;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.jsonArray;
import static org.jooq.impl.DSL.jsonObject;
import static org.jooq.impl.DSL.key;
import static org.jooq.impl.DSL.when;
import static org.jooq.impl.JSONEntryImpl.jsonCast;
import static org.jooq.impl.Keywords.K_AS;
import static org.jooq.impl.Keywords.K_IS_NOT_NULL;
import static org.jooq.impl.Keywords.K_NESTED;
import static org.jooq.impl.Keywords.K_PATH;
import static org.jooq.impl.Keywords.K_REPLACE;
import static org.jooq.impl.Keywords.K_ROW;
import static org.jooq.impl.Names.N_ARRAY_AGG;
import static org.jooq.impl.Names.N_CAST;
import static org.jooq.impl.Names.N_FIELD;
import static org.jooq.impl.Names.N_JSONB_OBJECT_AGG;
import static org.jooq.impl.Names.N_JSON_GROUP_OBJECT;
import static org.jooq.impl.Names.N_JSON_OBJECT;
import static org.jooq.impl.Names.N_JSON_OBJECTAGG;
import static org.jooq.impl.Names.N_JSON_OBJECT_AGG;
import static org.jooq.impl.Names.N_JSON_STRIP_NULLS;
import static org.jooq.impl.Names.N_JSON_TRANSFORM;
import static org.jooq.impl.Names.N_MAP;
import static org.jooq.impl.Names.N_MAP_FILTER;
import static org.jooq.impl.Names.N_MAP_FROM_ENTRIES;
import static org.jooq.impl.Names.N_OBJECT_AGG;
import static org.jooq.impl.Names.N_T;
import static org.jooq.impl.Names.N_TO_JSON;
import static org.jooq.impl.QOM.JSONOnNull.ABSENT_ON_NULL;
import static org.jooq.impl.QOM.JSONOnNull.NULL_ON_NULL;
import static org.jooq.impl.QueryPartListView.wrap;
import static org.jooq.impl.SQLDataType.BLOB;
import static org.jooq.impl.SQLDataType.JSON;
import static org.jooq.impl.SQLDataType.VARCHAR;

import java.util.function.Function;

import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Function1;
import org.jooq.JSON;
import org.jooq.JSONEntry;
import org.jooq.JSONObjectAggNullStep;
// ...
import org.jooq.QueryPart;
// ...
import org.jooq.impl.QOM.JSONOnNull;


/**
 * The JSON object constructor.
 *
 * @author Lukas Eder
 */
final class JSONObjectAgg<J>
extends
    AbstractAggregateFunction<J, QOM.JSONObjectAgg<J>>
implements
    JSONObjectAggNullStep<J>,
    QOM.JSONObjectAgg<J>
{

    private final JSONEntry<?> entry;
    private JSONOnNull         onNull;
    private DataType<?>        returning;

    JSONObjectAgg(DataType<J> type, JSONEntry<?> entry) {
        super(false, N_JSON_OBJECTAGG, type, entry.key(), entry.value());

        this.entry = entry;
    }

    @Override
    public void accept(Context<?> ctx) {
        switch (ctx.family()) {













































            case POSTGRES:
            case YUGABYTEDB:
                acceptPostgres(ctx);
                break;

            // [#10089] These dialects support non-standard JSON_OBJECTAGG without ABSENT ON NULL support
            case MARIADB:
            case MYSQL:

                // [#11238] FILTER cannot be emulated with the standard syntax
                if (onNull == ABSENT_ON_NULL || filter.hasWhere())
                    acceptGroupConcat(ctx);




                else
                    acceptStandard(ctx);

                break;

            case SQLITE:
                acceptSQLite(ctx);
                break;

            case DUCKDB:
                acceptDuckDB(ctx);
                break;

            case TRINO:
                acceptTrino(ctx);
                break;

            default:
                acceptStandard(ctx);
                break;
        }
    }

    private final void acceptDuckDB(Context<?> ctx) {
        ctx.visit(N_TO_JSON).sql('(');
        ctx.visit(N_MAP_FROM_ENTRIES).sql('(');
        ctx.visit(N_ARRAY_AGG).sql('(');
        ctx.visit(K_ROW).sql('(').visit(entry.key()).sql(", ").visit(jsonCast(ctx, entry.value())).sql(')');
        ctx.sql(')');

        if (onNull == ABSENT_ON_NULL)
            acceptFilterClause(ctx, f(entry.value().isNotNull()));
        else
            acceptFilterClause(ctx);

        acceptOverClause(ctx);

        ctx.sql(')');
        ctx.sql(')');
    }

    private final void acceptTrino(Context<?> ctx) {
        ctx.visit(N_CAST).sql('(');

        boolean noAggregateFilter = onNull == JSONOnNull.ABSENT_ON_NULL && !supportsFilter(ctx);
        if (noAggregateFilter)
            ctx.visit(N_MAP_FILTER).sql('(');

        ctx.visit(N_MAP).sql('(');
        acceptTrinoArrayAgg(ctx, entry.key(), entry.value());
        ctx.sql(", ");
        acceptTrinoArrayAgg(ctx, entry.value(), entry.value());
        ctx.sql(')');

        if (noAggregateFilter)
            ctx.sql(", (k, v) -> v ").visit(K_IS_NOT_NULL).sql(')');

        ctx.sql(' ').visit(K_AS).sql(' ').visit(JSON);
        ctx.sql(')');
    }

    private final void acceptTrinoArrayAgg(Context<?> ctx, Field<?> f1, Field<?> f2) {
        ctx.visit(N_ARRAY_AGG).sql('(');
        ctx.visit(jsonCast(ctx, f1));
        ctx.sql(")");

        if (onNull == ABSENT_ON_NULL)
            acceptFilterClause(ctx, f(f2.isNotNull()));
        else
            acceptFilterClause(ctx);

        acceptOverClause(ctx);
    }

    private final void acceptPostgres(Context<?> ctx) {
        ctx.visit(getDataType() == JSON ? N_JSON_OBJECT_AGG : N_JSONB_OBJECT_AGG).sql('(');
        ctx.visit(entry);
        ctx.sql(')');

        if (onNull == ABSENT_ON_NULL)
            acceptFilterClause(ctx, f(entry.value().isNotNull()));
        else
            acceptFilterClause(ctx);

        acceptOverClause(ctx);
    }

    private final void acceptSQLite(Context<?> ctx) {
        ctx.visit(N_JSON_GROUP_OBJECT).sql('(');
        ctx.visit(entry);
        ctx.sql(')');

        if (onNull == ABSENT_ON_NULL)
            acceptFilterClause(ctx, f(entry.value().isNotNull()));
        else
            acceptFilterClause(ctx);

        acceptOverClause(ctx);
    }





















































    private final void acceptGroupConcat(Context<?> ctx) {
        ctx.sql('(').visit(groupConcatEmulation(ctx)).sql(')');
    }

    private final Field<?> groupConcatEmulation(Context<?> ctx) {
        final Field<String> listagg = CustomField.of(Names.N_GROUP_CONCAT, VARCHAR, c1 -> {
            Field<JSON> o1 = jsonObject(entry.key(), entry.value());

            if (onNull == ABSENT_ON_NULL)
                o1 = when(entry.value().isNull(), inline((JSON) null)).else_(o1);

            Field<JSON> o2 = o1;
            c1.visit(groupConcat(DSL.concat(
                CustomField.of(N_FIELD, VARCHAR, c2 -> acceptArguments2(c2, QueryPartListView.wrap(
                    DSL.regexpReplaceAll(o2.cast(VARCHAR), inline("^\\{(.*)\\}$"), inline(RegexpReplace.replacement(ctx, 1)))
                )))
            )));

            acceptFilterClause(c1);
            acceptOverClause(c1);
        });

        Field<String> result = DSL.concat(inline('{'), listagg, inline('}'));

        switch (ctx.family()) {







            default:
                return result;
        }
    }















    private final void acceptStandard(Context<?> ctx) {
        acceptStandard(ctx, null, onNull);
    }

    private final void acceptStandard(Context<?> ctx, Function<? super Field<?>, ? extends Field<?>> mapper, JSONOnNull onNull0) {
        JSONEntry<?> entry0 = mapper == null ? entry : key(entry.key()).value(mapper.apply(entry.value()));

        ctx.visit(N_JSON_OBJECTAGG).sql('(').visit(entry0);

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

    @Override
    public final JSONObjectAgg<J> nullOnNull() {
        onNull = NULL_ON_NULL;
        return this;
    }

    @Override
    public final JSONObjectAgg<J> absentOnNull() {
        onNull = ABSENT_ON_NULL;
        return this;
    }

    @Override
    public final JSONObjectAgg<J> returning(DataType<?> r) {
        this.returning = r;
        return this;
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final JSONEntry<?> $arg1() {
        return entry;
    }

    @Override
    public final QOM.JSONObjectAgg<J> $arg1(JSONEntry<?> newArg1) {
        return copyAggregateSpecification().apply($constructor().apply(newArg1));
    }

    @Override
    public final JSONOnNull $onNull() {
        return onNull;
    }

    @Override
    public final QOM.JSONObjectAgg<J> $onNull(JSONOnNull newOnNull) {
        return copy(c -> {
            JSONObjectAgg<J> q = (JSONObjectAgg<J>) c;

            q.onNull = newOnNull;
        });
    }

    @Override
    public final DataType<?> $returning() {
        return returning;
    }

    @Override
    public final QOM.JSONObjectAgg<J> $returning(DataType<?> newReturning) {
        return copy(c -> {
            JSONObjectAgg<J> q = (JSONObjectAgg<J>) c;

            q.returning = newReturning;
        });
    }

    @Override
    public final Function1<? super JSONEntry<?>, ? extends QOM.JSONObjectAgg<J>> $constructor() {
        return e -> {
            JSONObjectAgg<J> r = new JSONObjectAgg<J>(getDataType(), e);
            r.onNull = onNull;
            r.returning = returning;
            return r;
        };
    }

    @Override
    final QOM.JSONObjectAgg<J> copyAggregateFunction(Function<? super QOM.JSONObjectAgg<J>, ? extends QOM.JSONObjectAgg<J>> function) {
        return function.apply($constructor().apply(entry));
    }






















}
