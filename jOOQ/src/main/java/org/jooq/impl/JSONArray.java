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

import static org.jooq.SQLDialect.H2;
import static org.jooq.impl.DSL.jsonArrayAgg;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.unquotedName;
import static org.jooq.impl.DSL.values;
import static org.jooq.impl.JSONNullClause.ABSENT_ON_NULL;
import static org.jooq.impl.JSONNullClause.NULL_ON_NULL;
import static org.jooq.impl.JSONObject.acceptJSONNullClause;
import static org.jooq.impl.Keywords.K_JSON_ARRAY;
import static org.jooq.impl.Keywords.K_NULL;
import static org.jooq.impl.Keywords.K_ON;
import static org.jooq.impl.Names.N_JSON_ARRAY;

import java.util.Collection;

import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.JSONArrayNullStep;
import org.jooq.Row1;
import org.jooq.Table;


/**
 * The JSON array constructor.
 *
 * @author Lukas Eder
 */
final class JSONArray<J> extends AbstractField<J> implements JSONArrayNullStep<J> {

    /**
     * Generated UID
     */
    private static final long             serialVersionUID = 1772007627336725780L;
    private final QueryPartList<Field<?>> args;
    private final JSONNullClause          nullClause;

    JSONArray(DataType<J> type, Collection<? extends Field<?>> args) {
        this(type, args, null);
    }

    JSONArray(DataType<J> type, Collection<? extends Field<?>> args, JSONNullClause nullClause) {
        super(N_JSON_ARRAY, type);

        this.args = new QueryPartList<>(args);
        this.nullClause = nullClause;
    }

    // -------------------------------------------------------------------------
    // XXX: DSL API
    // -------------------------------------------------------------------------

    @Override
    public final JSONArray<J> nullOnNull() {
        return new JSONArray<>(getDataType(), args, NULL_ON_NULL);
    }

    @Override
    public final JSONArray<J> absentOnNull() {
        return new JSONArray<>(getDataType(), args, ABSENT_ON_NULL);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void accept(Context<?> ctx) {
        switch (ctx.family()) {





            case POSTGRES:
                if (nullClause == ABSENT_ON_NULL) {
                    Row1[] rows = new Row1[args.size()];
                    for (int i = 0; i < rows.length; i++)
                        rows[i] = row(args.get(i));
                    Table<?> t = values(rows).as("t", "a");
                    Field<?> a = t.field("a");
                    ctx.visit(DSL.field(select(jsonArrayAgg(a)).from(t).where(a.isNotNull())));
                }
                else {
                    ctx.visit(unquotedName("json_build_array")).sql('(').visit(args).sql(')');
                }

                break;

            default:
                ctx.visit(K_JSON_ARRAY).sql('(').visit(args);

                // Workaround for https://github.com/h2database/h2database/issues/2496
                if (ctx.family() == H2 && args.isEmpty())
                    ctx.visit(K_NULL).sql(' ').visit(K_ON).sql(' ').visit(K_NULL);
                else
                    acceptJSONNullClause(ctx, nullClause);

                ctx.sql(')');
                break;
        }
    }
}
