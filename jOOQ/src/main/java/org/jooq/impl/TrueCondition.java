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
// ...
// ...
// ...
import static org.jooq.SQLDialect.FIREBIRD;
// ...
// ...
// ...
// ...
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
// ...
// ...
import static org.jooq.impl.Keywords.K_TRUE;

import java.util.Set;

import org.jooq.Clause;
import org.jooq.Context;
import org.jooq.SQLDialect;
import org.jooq.True;
import org.jooq.impl.QOM.UEmpty;

/**
 * @author Lukas Eder
 */
final class TrueCondition
extends
    AbstractCondition
implements
    True,
    QOM.True,
    UEmpty,
    SimpleQueryPart
{

    private static final Clause[] CLAUSES            = { CONDITION, CONDITION_COMPARISON };
    static final Set<SQLDialect>  NO_SUPPORT_BOOLEAN = SQLDialect.supportedBy(FIREBIRD, SQLITE);
    static final TrueCondition    INSTANCE           = new TrueCondition(false);
    static final TrueCondition    OPTIONAL           = new TrueCondition(true);
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
            ctx.sql("1 = 1");




        else
            ctx.visit(K_TRUE);
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }

    private TrueCondition(boolean optional) {
        this.optional = optional;
    }
}
