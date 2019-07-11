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

import static org.jooq.impl.Keywords.K_CASE;
import static org.jooq.impl.Keywords.K_ELSE;
import static org.jooq.impl.Keywords.K_END;
import static org.jooq.impl.Keywords.K_SWITCH;
import static org.jooq.impl.Keywords.K_THEN;
import static org.jooq.impl.Keywords.K_TRUE;
import static org.jooq.impl.Keywords.K_WHEN;

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

    /**
     * Generated UID
     */
    private static final long     serialVersionUID = -1735676153683257465L;

    private final List<Condition> conditions;
    private final List<Field<T>>  results;
    private Field<T>              else_;

    CaseConditionStepImpl(Condition condition, Field<T> result) {
        super(DSL.name("case"), result.getDataType());

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
                ctx.visit(new Native());
                break;
        }
    }







































    private class Native extends AbstractQueryPart {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = 7850713333675233736L;

        @Override
        public final void accept(Context<?> ctx) {
            ctx.visit(K_CASE)
               .formatIndentStart()
               .formatSeparator();

            int size = conditions.size();
            for (int i = 0; i < size; i++) {
                if (i > 0)
                    ctx.formatSeparator();

                ctx.visit(K_WHEN).sql(' ').visit(conditions.get(i)).sql(' ')
                   .visit(K_THEN).sql(' ').visit(results.get(i));
            }

            if (else_ != null)
                ctx.formatSeparator()
                   .visit(K_ELSE).sql(' ').visit(else_);

            ctx.formatIndentEnd()
               .formatSeparator()
               .visit(K_END);
        }
    }
}
