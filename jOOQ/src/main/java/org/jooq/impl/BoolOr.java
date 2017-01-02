/*
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
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
final class BoolOr extends Function<Boolean> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 7292087943334025737L;

    private final Condition   condition;

    BoolOr(Condition condition) {
        super("bool_or", SQLDataType.BOOLEAN, DSL.field(condition));

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
                        c.visit(DSL.max(DSL.when(condition, one()).otherwise(zero())));
                        toSQLOverClause(c);
                    }
                });

                ctx.visit(DSL.when(max.eq(one()), inline(true)).otherwise(inline(false)));
                break;
        }
    }
}
