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
import static org.jooq.impl.AbstractCondition.unwrapNot;
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
import static org.jooq.impl.Names.N_CASE;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_FORCE_CASE_ELSE_NULL;

import java.util.ArrayList;
import java.util.List;

import org.jooq.CaseConditionStep;
import org.jooq.Condition;
import org.jooq.Context;
import org.jooq.Field;
// ...
import org.jooq.Record1;
import org.jooq.Select;

/**
 * @author Lukas Eder
 */
final class CaseConditionStepImpl<T> extends AbstractField<T> implements CaseConditionStep<T> {

    private final List<Condition> conditions;
    private final List<Field<T>>  results;
    private Field<T>              else_;

    CaseConditionStepImpl(Condition condition, Field<T> result) {
        super(N_CASE, result.getDataType());

        this.conditions = new ArrayList<>();
        this.results = new ArrayList<>();

        when(condition, result);
    }

    @Override
    public final CaseConditionStep<T> when(Condition condition, T result) {
        return when(condition, Tools.field(result));
    }

    @Override
    public final CaseConditionStep<T> when(Condition condition, Field<T> result) {
        conditions.add(condition);
        results.add(result);

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

        int size = conditions.size();
        for (int i = 0; i < size; i++) {
            Condition c = conditions.get(i);










            ctx.formatSeparator()
               .visit(K_WHEN).sql(' ').visit(c).sql(' ')
               .visit(K_THEN).sql(' ').visit(results.get(i));
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
}
