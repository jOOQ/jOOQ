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

import static org.jooq.impl.DSL.*;
import static org.jooq.impl.Internal.*;
import static org.jooq.impl.Keywords.*;
import static org.jooq.impl.Names.*;
import static org.jooq.impl.SQLDataType.*;
import static org.jooq.impl.Tools.*;
import static org.jooq.impl.Tools.BooleanDataKey.*;
import static org.jooq.impl.Tools.DataExtendedKey.*;
import static org.jooq.impl.Tools.DataKey.*;
import static org.jooq.SQLDialect.*;

import org.jooq.*;
import org.jooq.Record;
import org.jooq.conf.*;
import org.jooq.impl.*;
import org.jooq.tools.*;

import java.util.*;


/**
 * The <code>AND</code> statement.
 */
@SuppressWarnings({ "unused" })
final class And
extends
    AbstractCondition
{

    final Condition arg1;
    final Condition arg2;

    And(
        Condition arg1,
        Condition arg2
    ) {

        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    private static final Clause[] CLAUSES = { Clause.CONDITION, Clause.CONDITION_AND };

    @Override
    public final void accept(Context<?> ctx) {














        {
            ctx.sqlIndentStart('(');
            Expression.<Condition, And>acceptAssociative(
                ctx,
                this,
                q -> new Expression.Expr<>(q.arg1, Operator.AND.toKeyword(), q.arg2),
                Context::formatSeparator
            );
            ctx.sqlIndentEnd(')');
        }
    }

    /**
     * @deprecated - This will be implemented using QOM.replace, instead.
     */
    @Deprecated
    final Condition transform(java.util.function.Function<? super Condition, ? extends Condition> function) {
        Condition t1 = arg1 instanceof And
            ? ((And) arg1).transform(function)
            : arg1 instanceof Or
            ? ((And) arg1).transform(function)
            : function.apply(arg1);
        Condition t2 = arg2 instanceof And
            ? ((And) arg2).transform(function)
            : arg2 instanceof Or
            ? ((Or) arg2).transform(function)
            : function.apply(arg2);

        if (t1 == arg1 && t2 == arg2)
            return this;
        else
            return DSL.and(t1, t2);
    }

    @Override
    final boolean isNullable() {
        return ((AbstractCondition) arg1).isNullable() || ((AbstractCondition) arg2).isNullable();
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }












    // -------------------------------------------------------------------------
    // The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof And) {
            return
                StringUtils.equals(arg1, ((And) that).arg1) &&
                StringUtils.equals(arg2, ((And) that).arg2)
            ;
        }
        else
            return super.equals(that);
    }
}
