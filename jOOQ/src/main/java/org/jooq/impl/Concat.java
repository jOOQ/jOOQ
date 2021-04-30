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

import static org.jooq.impl.DSL.function;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.ExpressionOperator.ADD;
import static org.jooq.impl.ExpressionOperator.BIT_AND;
import static org.jooq.impl.ExpressionOperator.CONCAT;
import static org.jooq.impl.Names.N_CONCAT;
import static org.jooq.impl.Tools.castAllIfNeeded;

import org.jooq.Context;
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
final class Concat extends AbstractField<String> {

    private final        Field<?>[] arguments;

    Concat(Field<?>... arguments) {
        super(N_CONCAT, SQLDataType.VARCHAR);

        this.arguments = arguments;
    }

    @Override
    public final void accept(Context<?> ctx) {

        // [#461] Type cast the concat expression, if this isn't a VARCHAR field
        Field<String>[] cast = castAllIfNeeded(arguments, String.class);

        if (Boolean.TRUE.equals(ctx.settings().isRenderCoalesceToEmptyStringInConcat()) && ctx.configuration().commercial(() -> "Auto-coalescing of CONCAT arguments is available in the jOOQ 3.15 Professional Edition and jOOQ Enterprise Edition, see https://github.com/jOOQ/jOOQ/issues/11757")) {





        }

        // If there is only one argument, return it immediately
        if (cast.length == 1) {
            ctx.visit(cast[0]);
            return;
        }

        ExpressionOperator op = CONCAT;
        switch (ctx.family()) {


            case MARIADB:
            case MYSQL:
                ctx.visit(function("concat", SQLDataType.VARCHAR, cast));
                return;













        }

        Field<?> expression = new Expression<>(op, false, cast[0], cast[1]);
        for (int i = 2; i < cast.length; i++)
            expression = new Expression<>(op, false, expression, cast[i]);

        ctx.visit(expression);
    }
}
