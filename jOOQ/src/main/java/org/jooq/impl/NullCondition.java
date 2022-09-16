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
import static org.jooq.SQLDialect.*;
import static org.jooq.impl.DSL.castNull;
import static org.jooq.impl.Keywords.K_NULL;
import static org.jooq.impl.SQLDataType.BOOLEAN;
import static org.jooq.impl.TrueCondition.NO_SUPPORT_BOOLEAN;

import java.util.Set;

import org.jooq.Clause;
import org.jooq.Context;
import org.jooq.False;
import org.jooq.Null;
import org.jooq.SQLDialect;
import org.jooq.impl.QOM.UEmpty;

/**
 * @author Lukas Eder
 */
final class NullCondition extends AbstractCondition implements Null, UEmpty {

    private static final Clause[] CLAUSES  = { CONDITION, CONDITION_COMPARISON };
    static final NullCondition    INSTANCE = new NullCondition();
    static final Set<SQLDialect>  NO_SUPPORT_UNTYPED_NULL = SQLDialect.supportedBy(DERBY, HSQLDB);

    @Override
    final boolean isNullable() {
        return true;
    }

    @Override
    final boolean parenthesised(Context<?> ctx) {
        return !NO_SUPPORT_UNTYPED_NULL.contains(ctx.dialect())
            && !NO_SUPPORT_BOOLEAN.contains(ctx.dialect());
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (NO_SUPPORT_UNTYPED_NULL.contains(ctx.dialect()))
            ctx.visit(castNull(BOOLEAN));
        else if (NO_SUPPORT_BOOLEAN.contains(ctx.dialect()))
            ctx.visit(K_NULL).sql(" = ").visit(K_NULL);




        else
            ctx.visit(K_NULL);
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }

    private NullCondition() {}
}
