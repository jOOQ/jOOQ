/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
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
 * Apache-2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: https://www.jooq.org/legal/licensing
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
import static org.jooq.impl.Keywords.K_FALSE;
import static org.jooq.impl.TrueCondition.NO_SUPPORT_BOOLEAN;

import org.jooq.Clause;
import org.jooq.Context;
import org.jooq.False;
import org.jooq.impl.QOM.UEmpty;

/**
 * @author Lukas Eder
 */
final class FalseCondition
extends
    AbstractCondition
implements
    False,
    QOM.False,
    UEmpty,
    SimpleQueryPart
{

    private static final Clause[] CLAUSES  = { CONDITION, CONDITION_COMPARISON };
    static final FalseCondition   INSTANCE = new FalseCondition(false);
    static final FalseCondition   OPTIONAL = new FalseCondition(true);
    final boolean                 optional;

    @Override
    final boolean isNullable() {
        return false;
    }

    @Override
    final boolean parenthesised(Context<?> ctx) {
        return !NO_SUPPORT_BOOLEAN.contains(ctx.dialect());
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (NO_SUPPORT_BOOLEAN.contains(ctx.dialect()))
            ctx.sql("1 = 0");




        else
            ctx.visit(K_FALSE);
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }

    private FalseCondition(boolean optional) {
        this.optional = optional;
    }
}
