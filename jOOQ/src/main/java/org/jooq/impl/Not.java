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
import org.jooq.Function1;
import org.jooq.Record;
import org.jooq.conf.*;
import org.jooq.impl.*;
import org.jooq.impl.QOM.*;
import org.jooq.tools.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;


/**
 * The <code>NOT</code> statement.
 */
@SuppressWarnings({ "unused" })
final class Not
extends
    AbstractCondition
implements
    QOM.Not
{

    final Condition condition;

    Not(
        Condition condition
    ) {

        this.condition = condition;
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    private static final Clause[] CLAUSES = { Clause.CONDITION, Clause.CONDITION_NOT };

    @Override
    boolean isNullable() {
        return !(condition instanceof AbstractCondition) || ((AbstractCondition) condition).isNullable();
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {






            default:
                ctx.visit(K_NOT).sql(" (").visit(condition).sql(')');
                break;
        }
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }












    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Condition $arg1() {
        return condition;
    }

    @Override
    public final QOM.Not $arg1(Condition newValue) {
        return constructor().apply(newValue);
    }

    @Override
    public final Function1<? super Condition, ? extends QOM.Not> constructor() {
        return (a1) -> new Not(a1);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.Not) { QOM.Not o = (QOM.Not) that;
            return
                StringUtils.equals($condition(), o.$condition())
            ;
        }
        else
            return super.equals(that);
    }
}
