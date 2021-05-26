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

import static org.jooq.Clause.CONDITION;
import static org.jooq.Clause.CONDITION_COMPARISON;
// ...
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.val;

import org.jooq.Clause;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.Field;

/**
 * Abstraction for various "contains" operations
 *
 * @author Lukas Eder
 */
final class Contains<T> extends AbstractCondition {
    private static final Clause[] CLAUSES = { CONDITION, CONDITION_COMPARISON };

    private final Field<T>        lhs;
    private final Field<T>        rhs;
    private final T               value;
    private final boolean         leftWildcard;
    private final boolean         rightWildcard;

    Contains(Field<T> field, T value, boolean leftWildcard, boolean rightWildcard) {
        this.lhs = field;
        this.rhs = null;
        this.value = value;
        this.leftWildcard = leftWildcard;
        this.rightWildcard = rightWildcard;
    }

    Contains(Field<T> field, Field<T> rhs, boolean leftWildcard, boolean rightWildcard) {
        this.lhs = field;
        this.rhs = rhs;
        this.value = null;
        this.leftWildcard = leftWildcard;
        this.rightWildcard = rightWildcard;
    }

    @Override
    public final void accept(Context<?> ctx) {
        // [#1107] Some dialects support "contains" operations for ARRAYs
        // [#5929] Check both sides of the operation for array types
        if (lhs.getDataType().isArray()
            || (rhs != null && rhs.getDataType().isArray())
            || (rhs == null && value != null && value.getClass().isArray()))
            ctx.visit(new PostgresArrayContains());

        // "contains" operations on Strings
        else {
            switch (ctx.family()) {















                default:
                    Field<?>[] array = new Field[1 + (leftWildcard ? 1 : 0) + (rightWildcard ? 1 : 0)];

                    int i = 0;
                    if (leftWildcard)
                        array[i++] = inline("%");

                    array[i++] = Tools.escapeForLike(rhs == null ? Tools.field(value, lhs) : rhs, ctx.configuration());

                    if (rightWildcard)
                        array[i++] = inline("%");

                    ctx.visit(lhs.like(DSL.concat(array), Tools.ESCAPE));
                    break;
            }
        }
    }

    /**
     * The Postgres array contains operator
     */
    private class PostgresArrayContains extends AbstractCondition {

        @Override
        public final void accept(Context<?> ctx) {
            ctx.visit(lhs).sql(" @> ").visit(rhs());
        }

        @Override
        public final Clause[] clauses(Context<?> ctx) {
            return CLAUSES;
        }

        private final Field<T> rhs() {
            return (rhs == null) ? val(value, lhs) : rhs;
        }
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }
}
