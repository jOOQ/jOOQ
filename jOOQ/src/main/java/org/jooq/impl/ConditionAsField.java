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
 * The <code>FIELD</code> statement.
 */
@SuppressWarnings({ "unused" })
final class ConditionAsField
extends
    AbstractField<Boolean>
implements
    QOM.ConditionAsField
{

    final Condition condition;

    ConditionAsField(
        Condition condition
    ) {
        super(
            N_FIELD,
            allNotNull(BOOLEAN)
        );

        this.condition = condition;
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {















            case CUBRID:
            case FIREBIRD: {
                // [#10179] Avoid 3VL when not necessary
                if (condition instanceof AbstractCondition && !((AbstractCondition) condition).isNullable())
                    ctx.visit(DSL.when(condition, inline(true))
                                 .else_(inline(false)));

                // [#3206] Implement 3VL if necessary or unknown
                else
                    ctx.visit(DSL.when(condition, inline(true))
                                 .when(not(condition), inline(false)));
                break;
            }

            default:
                ctx.sql('(').visit(condition).sql(')');
                break;
        }
    }












    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Condition $condition() {
        return condition;
    }

    @Override
    public final QOM.ConditionAsField $condition(Condition newValue) {
        return new ConditionAsField(newValue);
    }

    public final Function1<? super Condition, ? extends Field<Boolean>> constructor() {
        return (a1) -> DSL.field(a1);
    }






















    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.ConditionAsField) { QOM.ConditionAsField o = (QOM.ConditionAsField) that;
            return
                StringUtils.equals($condition(), o.$condition())
            ;
        }
        else
            return super.equals(that);
    }
}
