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
// ...
import static org.jooq.impl.DSL.one;
import static org.jooq.impl.DSL.select;
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

import java.util.List;

import org.jooq.CaseConditionStep;
import org.jooq.Condition;
import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Function2;
// ...
import org.jooq.Record1;
// ...
import org.jooq.Select;
import org.jooq.impl.QOM.CaseSearched;
import org.jooq.impl.QOM.Tuple2;
import org.jooq.impl.QOM.UnmodifiableList;

/**
 * @author Lukas Eder
 */
final class CaseConditionStepImpl<T>
extends
    AbstractField<T>
implements
    CaseConditionStep<T>,
    QOM.CaseSearched<T>
{

    private final List<Tuple2<Condition, Field<T>>> when;
    private Field<T>                                 else_;

    CaseConditionStepImpl(DataType<T> type) {
        super(NQ_CASE, type);

        this.when = new QueryPartList<>();
    }

    CaseConditionStepImpl(Condition condition, Field<T> result) {
        this(result.getDataType());

        when(condition, result);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

    @Override
    public final CaseConditionStep<T> when(Condition condition, T result) {
        return when(condition, Tools.field(result));
    }

    @Override
    public final CaseConditionStep<T> when(Condition condition, Field<T> result) {
        when.add(QOM.tuple(condition, result));
        return this;
    }

    @Override
    public final CaseConditionStep<T> when(Condition condition, Select<? extends Record1<T>> result) {
        return when(condition, DSL.field(result));
    }

    @Override
    public final CaseConditionStep<T> when(Field<Boolean> condition, T result) {
        return when(DSL.condition(condition), result);
    }

    @Override
    public final CaseConditionStep<T> when(Field<Boolean> condition, Field<T> result) {
        return when(DSL.condition(condition), result);
    }

    @Override
    public final CaseConditionStep<T> when(Field<Boolean> condition, Select<? extends Record1<T>> result) {
        return when(DSL.condition(condition), result);
    }

    @Override
    public final Field<T> otherwise(T result) {
        return else_(result);
    }

    @Override
    public final Field<T> otherwise(Field<T> result) {
        return else_(result);
    }

    @Override
    public final Field<T> otherwise(Select<? extends Record1<T>> result) {
        return else_(result);
    }

    @Override
    public final Field<T> else_(T result) {
        return else_(Tools.field(result));
    }

    @Override
    public final Field<T> else_(Field<T> result) {
        this.else_ = result;

        return this;
    }

    @Override
    public final Field<T> else_(Select<? extends Record1<T>> result) {
        return else_(DSL.field(result));
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {










            default:
                acceptNative(ctx);
                break;
        }
    }








































    private final void acceptNative(Context<?> ctx) {
        ctx.visit(K_CASE)
           .formatIndentStart();

        for (Tuple2<Condition, Field<T>> e : when) {
            Condition c = e.$1();
















            ctx.formatSeparator()
               .visit(K_WHEN).sql(' ').visit(c).sql(' ')
               .visit(K_THEN).sql(' ').visit(e.$2());
        }

        if (else_ != null)
            ctx.formatSeparator()
               .visit(K_ELSE).sql(' ').visit(else_);
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
    public final Function2<? super UnmodifiableList<? extends Tuple2<Condition, Field<T>>>, ? super Field<T>, ? extends CaseSearched<T>> $constructor() {
        return (w, e) -> {
            CaseConditionStepImpl<T> r = new CaseConditionStepImpl<>(getDataType());
            w.forEach(t -> r.when(t.$1(), t.$2()));
            r.else_(e);
            return r;
        };
    }

    @Override
    public final UnmodifiableList<? extends Tuple2<Condition, Field<T>>> $arg1() {
        return QOM.unmodifiable(when);
    }

    @Override
    public final CaseSearched<T> $arg1(UnmodifiableList<? extends Tuple2<Condition, Field<T>>> w) {
        return $constructor().apply(w, $else());
    }

    @Override
    public final Field<T> $arg2() {
        return else_;
    }

    @Override
    public final CaseSearched<T> $arg2(Field<T> e) {
        return $constructor().apply($when(), e);
    }
}
