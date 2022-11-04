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
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
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
import static org.jooq.impl.DSL.NULL;
import static org.jooq.impl.Keywords.K_CASE;
import static org.jooq.impl.Keywords.K_ELSE;
import static org.jooq.impl.Keywords.K_END;
import static org.jooq.impl.Keywords.K_NULL;
import static org.jooq.impl.Keywords.K_SWITCH;
import static org.jooq.impl.Keywords.K_THEN;
import static org.jooq.impl.Keywords.K_TRUE;
import static org.jooq.impl.Keywords.K_WHEN;
import static org.jooq.impl.Names.NQ_CASE;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_FORCE_CASE_ELSE_NULL;

import java.util.Map;

import org.jooq.CaseConditionStep;
import org.jooq.CaseWhenStep;
import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
// ...
import org.jooq.QueryPart;
// ...
// ...
import org.jooq.impl.QOM.Tuple2;

/**
 * @author Lukas Eder
 */
final class CaseSimple<V, T>
extends
    AbstractCaseSimple<V, T, CaseSimple<V, T>>
implements
    CaseWhenStep<V, T>,
    QOM.CaseSimple<V, T>
{

    CaseSimple(Field<V> value, Field<V> compareValue, Field<T> result) {
        super(NQ_CASE, value, compareValue, result);
    }

    CaseSimple(Field<V> value, Map<? extends Field<V>, ? extends Field<T>> map) {
        super(NQ_CASE, value, map);
    }

    CaseSimple(Field<V> value, DataType<T> type) {
        super(NQ_CASE, value, type);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

    @Override
    public final Field<T> otherwise(T result) {
        return else_(result);
    }

    @Override
    public final Field<T> otherwise(Field<T> result) {
        return else_(result);
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (when.isEmpty()) {
            if (else_ != null)
                ctx.visit(else_);
            else
                ctx.visit(NULL(getDataType()));
        }
        else {
            switch (ctx.family()) {










                // The DERBY dialect doesn't support the simple CASE clause
                case DERBY:
                    acceptSearched(ctx);
                    break;

                default:
                    acceptNative(ctx);
                    break;
            }
        }
    }













































    private final void acceptSearched(Context<?> ctx) {

        CaseConditionStep<T> w = null;
        for (Tuple2<Field<V>, Field<T>> e : when)
            if (w == null)
                w = DSL.when(value.eq(e.$1()), e.$2());
            else
                w = w.when(value.eq(e.$1()), e.$2());

        if (w != null)
            if (else_ != null)
                ctx.visit(w.else_(else_));
            else
                ctx.visit(w);
    }

    private final void acceptNative(Context<?> ctx) {
        ctx.visit(K_CASE);

        ctx.sql(' ')
           .visit(value)
           .formatIndentStart();

        for (Tuple2<Field<V>, Field<T>> e : when)
            ctx.formatSeparator()
               .visit(K_WHEN).sql(' ')
               .visit(e.$1()).sql(' ')
               .visit(K_THEN).sql(' ')
               .visit(e.$2());

        if (else_ != null)
            ctx.formatSeparator()
               .visit(K_ELSE).sql(' ')
               .visit(else_);
        else if (TRUE.equals(ctx.data(DATA_FORCE_CASE_ELSE_NULL)))
            ctx.formatSeparator()
               .visit(K_ELSE).sql(' ').visit(K_NULL);

        ctx.formatIndentEnd()
           .formatSeparator()
           .visit(K_END);
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    final CaseSimple<V, T> construct(Field<V> v, DataType<T> t) {
        return new CaseSimple<>(v, t);
    }














}
