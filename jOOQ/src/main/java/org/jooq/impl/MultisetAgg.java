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
import static org.jooq.impl.DSL.jsonArray;
import static org.jooq.impl.DSL.jsonArrayAgg;
import static org.jooq.impl.DSL.jsonObject;
import static org.jooq.impl.DSL.jsonbArray;
import static org.jooq.impl.DSL.jsonbArrayAgg;
import static org.jooq.impl.DSL.jsonbObject;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectFrom;
import static org.jooq.impl.DSL.xmlagg;
import static org.jooq.impl.DSL.xmlelement;
import static org.jooq.impl.Keywords.K_MULTISET;
import static org.jooq.impl.Names.N_ARRAY_AGG;
import static org.jooq.impl.Names.N_MULTISET_AGG;
import static org.jooq.impl.Names.N_RECORD;
import static org.jooq.impl.Names.N_RESULT;
import static org.jooq.impl.Tools.emulateMultiset;
import static org.jooq.impl.Tools.map;
import static org.jooq.impl.Tools.visitSubquery;

import org.jooq.AggregateFunction;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.Row;
import org.jooq.SelectField;
import org.jooq.Table;

/**
 * @author Lukas Eder
 */
final class MultisetAgg<R extends Record> extends DefaultAggregateFunction<Result<R>> {

    private final AbstractRow<R> row;

    MultisetAgg(boolean distinct, SelectField<R> row) {
        super(distinct, N_MULTISET_AGG, new MultisetDataType<>((AbstractRow<R>) row, null), (((AbstractRow<R>) row).fields()));

        this.row = (AbstractRow<R>) row;
    }

    @Override
    public final void accept(Context<?> ctx) {












        switch (emulateMultiset(ctx.configuration())) {
            case JSON: {
                ctx.visit(ofo((AbstractAggregateFunction<?>) jsonArrayAgg(jsonObject(row.fields()))));
                break;
            }

            case JSONB: {
                ctx.visit(ofo((AbstractAggregateFunction<?>) jsonbArrayAgg(jsonObject(row.fields()))));
                break;
            }

            case XML: {
                ctx.visit(xmlelement(N_RESULT,
                    ofo((AbstractAggregateFunction<?>)
                        xmlagg(xmlelement(N_RECORD,
                            map(row.fields(), f -> xmlelement(f.getUnqualifiedName(), f))
                        ))
                    )
                ));
                break;
            }

            case NATIVE:
                ctx.visit(N_MULTISET_AGG).sql('(');
                acceptArguments1(ctx, new QueryPartListView<>(arguments.get(0)));
                acceptOrderBy(ctx);
                ctx.sql(')');
                acceptFilterClause(ctx);
                acceptOverClause(ctx);
                break;
        }
    }
}
