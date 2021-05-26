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

import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.not;
import static org.jooq.impl.DSL.select;

import org.jooq.Condition;
import org.jooq.Context;

/**
 * @author Lukas Eder
 */
final class ConditionAsField extends AbstractField<Boolean> {
    final Condition           condition;

    ConditionAsField(Condition condition) {
        super(DSL.name(condition.toString()), SQLDataType.BOOLEAN);

        this.condition = condition;
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {














            case CUBRID:
            case FIREBIRD:
                acceptCase(ctx);
                break;

            // Other dialects can inline predicates in column expression contexts
            default:
                ctx.sql('(').visit(condition).sql(')');
                break;
        }
    }

    private final void acceptCase(Context<?> ctx) {

        // [#10179] Avoid 3VL when not necessary
        if (condition instanceof AbstractCondition && !((AbstractCondition) condition).isNullable())
            ctx.visit(DSL.when(condition, inline(true))
                         .else_(inline(false)));

        // [#3206] Implement 3VL if necessary or unknown
        else
            ctx.visit(DSL.when(condition, inline(true))
                         .when(not(condition), inline(false)));
    }
}
