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
 */
package org.jooq.impl;

import java.util.ArrayList;
import java.util.List;

import org.jooq.CaseConditionStep;
import org.jooq.Clause;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.QueryPart;
import org.jooq.Record1;
import org.jooq.Select;

/**
 * @author Lukas Eder
 */
final class CaseConditionStepImpl<T> extends AbstractFunction<T> implements CaseConditionStep<T> {

    /**
     * Generated UID
     */
    private static final long     serialVersionUID = -1735676153683257465L;

    private final List<Condition> conditions;
    private final List<Field<T>>  results;
    private Field<T>              otherwise;

    CaseConditionStepImpl(Condition condition, Field<T> result) {
        super("case", result.getDataType());

        this.conditions = new ArrayList<Condition>();
        this.results = new ArrayList<Field<T>>();

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
        return otherwise(Tools.field(result));
    }

    @Override
    public final Field<T> otherwise(Field<T> result) {
        this.otherwise = result;

        return this;
    }

    @Override
    public final Field<T> otherwise(Select<? extends Record1<T>> result) {
        return otherwise(DSL.field(result));
    }

    @Override
    final QueryPart getFunction0(Configuration configuration) {
        switch (configuration.dialect().family()) {





            default:
                return new Native();
        }
    }

    private abstract class Base extends AbstractQueryPart {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = 6146002888421945901L;

        @Override
        public final Clause[] clauses(Context<?> ctx) {
            return null;
        }
    }






































    private class Native extends Base {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = 7850713333675233736L;

        @Override
        public final void accept(Context<?> ctx) {
            ctx.formatIndentLockStart()
               .keyword("case")
               .formatIndentLockStart();

            int size = conditions.size();
            for (int i = 0; i < size; i++) {
                if (i > 0) {
                    ctx.formatNewLine();
                }

                ctx.sql(' ').keyword("when").sql(' ').visit(conditions.get(i))
                   .sql(' ').keyword("then").sql(' ').visit(results.get(i));
            }

            if (otherwise != null) {
                ctx.formatNewLine()
                   .sql(' ').keyword("else").sql(' ').visit(otherwise);
            }

            ctx.formatIndentLockEnd();

            if (size > 1 || otherwise != null) {
                ctx.formatSeparator();
            }
            else {
                ctx.sql(' ');
            }

            ctx.keyword("end")
               .formatIndentLockEnd();
        }
    }
}
