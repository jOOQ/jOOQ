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

import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.impl.DSL.groupConcat;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.jsonObject;
import static org.jooq.impl.DSL.jsonValue;
import static org.jooq.impl.DSL.when;
import static org.jooq.impl.JSONOnNull.ABSENT_ON_NULL;
import static org.jooq.impl.JSONOnNull.NULL_ON_NULL;
import static org.jooq.impl.Names.N_JSONB_OBJECT_AGG;
import static org.jooq.impl.Names.N_JSON_OBJECTAGG;
import static org.jooq.impl.Names.N_JSON_OBJECT_AGG;
import static org.jooq.impl.SQLDataType.JSON;

import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.JSON;
import org.jooq.JSONEntry;
import org.jooq.JSONObjectAggNullStep;


/**
 * The JSON object constructor.
 *
 * @author Lukas Eder
 */
final class JSONObjectAgg<J>
extends AbstractAggregateFunction<J>
implements JSONObjectAggNullStep<J> {

    /**
     * Generated UID
     */
    private static final long  serialVersionUID = 1772007627336725780L;

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
                acceptPostgres(ctx);
                break;

            // [#10089] These dialects support non-standard JSON_OBJECTAGG without ABSENT ON NULL support
            case MARIADB:
            case MYSQL:
                if (onNull == ABSENT_ON_NULL)
                    acceptGroupConcat(ctx);




                else
                    acceptStandard(ctx);

                break;

            default:
                acceptStandard(ctx);
                break;
        }
    }

    private final void acceptPostgres(Context<?> ctx) {
        ctx.visit(getDataType() == JSON ? N_JSON_OBJECT_AGG : N_JSONB_OBJECT_AGG).sql('(');
        ctx.visit(entry);
        ctx.sql(')');

        // TODO: What about a user-defined filter clause?
        if (onNull == ABSENT_ON_NULL)
            acceptFilterClause(ctx, entry.value().isNotNull());

        acceptOverClause(ctx);
    }

    @SuppressWarnings({ "unchecked", "rawtypes", "serial" })
    private final void acceptGroupConcat(Context<?> ctx) {
        Field<?> value;

        if (entry.value().getDataType().isJSON()) {
            value = entry.value();
        }
        else {
            Field<JSON> x = jsonObject(inline("x"), entry.value());

            switch (ctx.family()) {






                default:
                    value = jsonValue(x, inline("$.x"));
                    break;
            }

            if (onNull == ABSENT_ON_NULL)
                value = when(entry.value().isNull(), inline((String) null)).else_((Field) value);
        }

        final Field<?> value1 = value;
        final Field<String> listagg = DSL.field("{0}", String.class, CustomQueryPart.of(c -> {
            c.visit(groupConcat(DSL.concat(
                inline('"'),
                DSL.replace(entry.key(), inline('"'), inline("\\\"")),
                inline("\":"),
                onNull == ABSENT_ON_NULL ? value1 : DSL.coalesce(value1, inline("null"))
            )));
            acceptOverClause(c);
        }));

        ctx.sql('(').visit(DSL.concat(inline('{'), listagg, inline('}'))).sql(')');
    }

    private final void acceptStandard(Context<?> ctx) {
        ctx.visit(N_JSON_OBJECTAGG).sql('(').visit(entry);

        JSONNull jsonNull = new JSONNull(onNull);
        if (jsonNull.rendersContent(ctx))
            ctx.sql(' ').visit(jsonNull);

        JSONReturning jsonReturning = new JSONReturning(returning);
        if (jsonReturning.rendersContent(ctx))
            ctx.sql(' ').visit(jsonReturning);

        ctx.sql(')');
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
}
