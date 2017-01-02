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

import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.one;
import static org.jooq.impl.DSL.zero;

import org.jooq.Condition;
import org.jooq.Context;
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
final class BoolAnd extends Function<Boolean> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 7292087943334025737L;

    private final Condition   condition;

    BoolAnd(Condition condition) {
        super("bool_and", SQLDataType.BOOLEAN, DSL.field(condition));

        this.condition = condition;
    }

    @SuppressWarnings("serial")
    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {
            case POSTGRES:
                super.accept(ctx);
                break;

            default:
                final Field<Integer> max = DSL.field("{0}", Integer.class, new CustomQueryPart() {
                    @Override
                    public void accept(Context<?> c) {
                        c.visit(DSL.max(DSL.when(condition, zero()).otherwise(one())));
                        toSQLOverClause(c);
                    }
                });

                ctx.visit(DSL.when(max.eq(zero()), inline(true)).otherwise(inline(false)));
                break;
        }
    }
}
