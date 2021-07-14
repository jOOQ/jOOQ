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

import static java.lang.Boolean.TRUE;
// ...
import static org.jooq.impl.DSL.xmlelement;
import static org.jooq.impl.DSL.xmlserializeContent;
import static org.jooq.impl.Multiset.NO_SUPPORT_JSONB_COMPARE;
import static org.jooq.impl.Multiset.NO_SUPPORT_JSON_COMPARE;
import static org.jooq.impl.Multiset.NO_SUPPORT_XML_COMPARE;
import static org.jooq.impl.Multiset.jsonArrayaggEmulation;
import static org.jooq.impl.Multiset.jsonbArrayaggEmulation;
import static org.jooq.impl.Multiset.returningClob;
import static org.jooq.impl.Multiset.xmlaggEmulation;
import static org.jooq.impl.Names.N_MULTISET_AGG;
import static org.jooq.impl.Names.N_RESULT;
import static org.jooq.impl.SQLDataType.VARCHAR;
import static org.jooq.impl.Tools.emulateMultiset;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_MULTISET_CONDITION;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_MULTISET_CONTENT;

import org.jooq.Context;
import org.jooq.Field;
import org.jooq.JSON;
import org.jooq.JSONArrayAggOrderByStep;
import org.jooq.JSONB;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SelectField;
import org.jooq.XML;
import org.jooq.XMLAggOrderByStep;

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













        if (TRUE.equals(ctx.data(DATA_MULTISET_CONDITION))) {
            ctx.data().remove(DATA_MULTISET_CONDITION);
            ctx.data(DATA_MULTISET_CONTENT, true, c -> accept0(c, true));
            ctx.data(DATA_MULTISET_CONDITION, true);
        }
        else
            ctx.data(DATA_MULTISET_CONTENT, true, c -> accept0(c, false));
    }

    private final void accept0(Context<?> ctx, boolean multisetCondition) {
        switch (emulateMultiset(ctx.configuration())) {
            case JSON: {
                JSONArrayAggOrderByStep<JSON> order = jsonArrayaggEmulation(ctx, row, true);

                Field<?> f = multisetCondition
                    ? fo((AbstractAggregateFunction<?>) returningClob(ctx, order.orderBy(row.fields())))
                    : ofo((AbstractAggregateFunction<?>) returningClob(ctx, order));

                if (multisetCondition && NO_SUPPORT_JSON_COMPARE.contains(ctx.dialect()))
                    ctx.visit(f.cast(VARCHAR));
                else
                    ctx.visit(f);

                break;
            }

            case JSONB: {
                JSONArrayAggOrderByStep<JSONB> order = jsonbArrayaggEmulation(ctx, row, true);

                Field<?> f = multisetCondition
                    ? fo((AbstractAggregateFunction<?>) returningClob(ctx, order.orderBy(row.fields())))
                    : ofo((AbstractAggregateFunction<?>) returningClob(ctx, order));


                if (multisetCondition && NO_SUPPORT_JSONB_COMPARE.contains(ctx.dialect()))
                    ctx.visit(f.cast(VARCHAR));
                else
                    ctx.visit(f);

                break;
            }

            case XML: {
                XMLAggOrderByStep<XML> order = xmlaggEmulation(row, true);

                Field<XML> f = xmlelement(N_RESULT,
                    multisetCondition
                        ? fo((AbstractAggregateFunction<?>) order.orderBy(row.fields()))
                        : ofo((AbstractAggregateFunction<?>) order)
                );

                if (multisetCondition && NO_SUPPORT_XML_COMPARE.contains(ctx.dialect()))
                    ctx.visit(xmlserializeContent(f, VARCHAR));
                else
                    ctx.visit(f);

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
