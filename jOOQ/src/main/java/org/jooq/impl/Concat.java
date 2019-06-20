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
import static org.jooq.impl.ExpressionOperator.ADD;
import static org.jooq.impl.ExpressionOperator.BIT_AND;
import static org.jooq.impl.ExpressionOperator.CONCAT;
import static org.jooq.impl.Tools.castAllIfNeeded;

import org.jooq.Context;
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
final class Concat extends AbstractField<String> {

    /**
     * Generated UID
     */
    private static final long       serialVersionUID = -7273879239726265322L;

    private final        Field<?>[] arguments;

    Concat(Field<?>... arguments) {
        super(DSL.name("concat"), SQLDataType.VARCHAR);

        this.arguments = arguments;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final void accept(Context<?> ctx) {

        // [#461] Type cast the concat expression, if this isn't a VARCHAR field
        Field<String>[] cast = castAllIfNeeded(arguments, String.class);

        // If there is only one argument, return it immediately
        if (cast.length == 1) {
            ctx.visit(cast[0]);
            return;
        }

        Field<String> first = cast[0];
        Field<String>[] others = new Field[cast.length - 1];
        System.arraycopy(cast, 1, others, 0, others.length);

        switch (ctx.family()) {





            case MARIADB:
            case MYSQL:
                ctx.visit(function("concat", SQLDataType.VARCHAR, cast));
                break;













            default:
                ctx.visit(new Expression<String>(CONCAT, first, others));
                break;
        }
    }
}
