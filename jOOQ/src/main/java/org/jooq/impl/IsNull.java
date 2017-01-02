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

import static org.jooq.Clause.CONDITION;
import static org.jooq.Clause.CONDITION_IS_NOT_NULL;
import static org.jooq.Clause.CONDITION_IS_NULL;

import org.jooq.Clause;
import org.jooq.Context;
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
final class IsNull extends AbstractCondition {

    private static final long     serialVersionUID = -747240442279619486L;
    private static final Clause[] CLAUSES_NULL     = { CONDITION, CONDITION_IS_NULL };
    private static final Clause[] CLAUSES_NULL_NOT = { CONDITION, CONDITION_IS_NOT_NULL };

    private final Field<?>        field;
    private final boolean         isNull;

    IsNull(Field<?> field, boolean isNull) {
        this.field = field;
        this.isNull = isNull;
    }

    @Override
    public final void accept(Context<?> ctx) {
        ctx.visit(field).sql(' ').keyword(isNull ? "is null" : "is not null");
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return isNull ? CLAUSES_NULL : CLAUSES_NULL_NOT;
    }
}
