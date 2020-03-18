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
import static org.jooq.impl.Names.N_GREATEST;

import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
final class Greatest<T> extends AbstractField<T> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -7273879239726265322L;
    private final Field<?>[]  args;

    Greatest(DataType<T> type, Field<?>... args) {
        super(N_GREATEST, type);

        this.args = args;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final void accept(Context<?> ctx) {

        // In any dialect, a single argument is always the greatest
        if (args.length == 1) {
            ctx.visit(args[0]);
            return;
        }

        switch (ctx.family()) {
            // This implementation has O(2^n) complexity. Better implementations
            // are very welcome
            // [#1049] TODO Fix this!









            case DERBY: {
                Field<T> first = (Field<T>) args[0];
                Field<T> other = (Field<T>) args[1];

                if (args.length > 2) {
                    Field<?>[] remaining = new Field[args.length - 2];
                    System.arraycopy(args, 2, remaining, 0, remaining.length);

                    ctx.visit(DSL
                        .when(first.gt(other), DSL.greatest(first, remaining))
                        .otherwise(DSL.greatest(other, remaining)));
                }
                else {
                    ctx.visit(DSL
                        .when(first.gt(other), first)
                        .otherwise(other));
                }

                return;
            }

            case FIREBIRD:
                ctx.visit(function("maxvalue", getDataType(), args));
                return;

            case SQLITE:
                ctx.visit(function("max", getDataType(), args));
                return;

            default:
                ctx.visit(function("greatest", getDataType(), args));
                return;
        }
    }
}
