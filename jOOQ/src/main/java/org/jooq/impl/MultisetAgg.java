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

import static java.lang.Boolean.TRUE;
// ...
import static org.jooq.impl.DSL.xmlelement;
import static org.jooq.impl.DSL.xmlserializeContent;
import static org.jooq.impl.Multiset.NO_SUPPORT_JSONB_COMPARE;
import static org.jooq.impl.Multiset.NO_SUPPORT_JSON_COMPARE;
import static org.jooq.impl.Multiset.NO_SUPPORT_XML_COMPARE;
import static org.jooq.impl.Multiset.arrayAggEmulation;
import static org.jooq.impl.Multiset.jsonArrayaggEmulation;
import static org.jooq.impl.Multiset.jsonbArrayaggEmulation;
import static org.jooq.impl.Multiset.nResult;
import static org.jooq.impl.Multiset.returningClob;
import static org.jooq.impl.Multiset.xmlaggEmulation;
import static org.jooq.impl.Names.N_MULTISET_AGG;
import static org.jooq.impl.SQLDataType.VARCHAR;
import static org.jooq.impl.Tools.emulateMultiset;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_MULTISET_CONDITION;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_MULTISET_CONTENT;

import java.util.function.Function;

import org.jooq.ArrayAggOrderByStep;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Function1;
import org.jooq.JSON;
import org.jooq.JSONArrayAggOrderByStep;
import org.jooq.JSONB;
import org.jooq.QueryPart;
import org.jooq.Record;
// ...
import org.jooq.Result;
import org.jooq.Row;
import org.jooq.SelectField;
// ...
import org.jooq.XML;
import org.jooq.XMLAggOrderByStep;
import org.jooq.impl.QOM.UnmodifiableList;

import org.jetbrains.annotations.NotNull;

/**
 * @author Lukas Eder
 */
final class MultisetAgg<R extends Record>
extends
    AbstractAggregateFunction<Result<R>, QOM.MultisetAgg<R>>
implements
    QOM.MultisetAgg<R>
{

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
                JSONArrayAggOrderByStep<JSON> order = jsonArrayaggEmulation(ctx, row, true, distinct);

                Field<?> f = multisetCondition
                    ? fo((AbstractAggregateFunction<?, ?>) returningClob(ctx, order.orderBy(row.fields())))
                    : ofo((AbstractAggregateFunction<?, ?>) returningClob(ctx, order));

                if (multisetCondition && NO_SUPPORT_JSON_COMPARE.contains(ctx.dialect()))
                    ctx.visit(f.cast(VARCHAR));
                else
                    ctx.visit(f);

                break;
            }

            case JSONB: {
                JSONArrayAggOrderByStep<JSONB> order = jsonbArrayaggEmulation(ctx, row, true, distinct);

                Field<?> f = multisetCondition
                    ? fo((AbstractAggregateFunction<?, ?>) returningClob(ctx, order.orderBy(row.fields())))
                    : ofo((AbstractAggregateFunction<?, ?>) returningClob(ctx, order));


                if (multisetCondition && NO_SUPPORT_JSONB_COMPARE.contains(ctx.dialect()))
                    ctx.visit(f.cast(VARCHAR));
                else
                    ctx.visit(f);

                break;
            }

            case XML: {
                XMLAggOrderByStep<XML> order = xmlaggEmulation(ctx, row, true);

                Field<XML> f = xmlelement(nResult(ctx),
                    multisetCondition
                        ? fo((AbstractAggregateFunction<?, ?>) order.orderBy(row.fields()))
                        : ofo((AbstractAggregateFunction<?, ?>) order)
                );

                if (multisetCondition && NO_SUPPORT_XML_COMPARE.contains(ctx.dialect()))
                    ctx.visit(xmlserializeContent(f, VARCHAR));
                else
                    ctx.visit(f);

                break;
            }

            case NATIVE: {
                ArrayAggOrderByStep<?> order = arrayAggEmulation(row, true);

                ctx.visit(multisetCondition
                    ? fo(order.orderBy(row.fields()))
                    : ofo((AbstractAggregateFunction<?, ?>) order)
                );

                break;
            }
        }
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Row $row() {
        return row;
    }

    @Override
    public final UnmodifiableList<? extends Field<?>> $arg1() {
        return QOM.unmodifiable(row.fields());
    }

    @Override
    public final QOM.MultisetAgg<R> $arg1(UnmodifiableList<? extends Field<?>> newArg1) {
        return copyAggregateSpecification().apply($constructor().apply(newArg1));
    }

    @Override
    public final Function1<? super UnmodifiableList<? extends Field<?>>, ? extends QOM.MultisetAgg<R>> $constructor() {
        return f -> (QOM.MultisetAgg<R>) new MultisetAgg<>(distinct, Tools.row0(f));
    }

    @Override
    final QOM.MultisetAgg<R> copyAggregateFunction(Function<? super QOM.MultisetAgg<R>, ? extends QOM.MultisetAgg<R>> function) {
        return function.apply(new MultisetAgg<>(distinct, row));
    }










}
