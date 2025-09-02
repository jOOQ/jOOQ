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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
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

import static org.jooq.impl.DSL.*;
import static org.jooq.impl.Internal.*;
import static org.jooq.impl.Keywords.*;
import static org.jooq.impl.Names.*;
import static org.jooq.impl.SQLDataType.*;
import static org.jooq.impl.Tools.*;
import static org.jooq.impl.Tools.BooleanDataKey.*;
import static org.jooq.impl.Tools.ExtendedDataKey.*;
import static org.jooq.impl.Tools.SimpleDataKey.*;
import static org.jooq.SQLDialect.*;

import org.jooq.*;
import org.jooq.Function1;
import org.jooq.Record;
import org.jooq.conf.ParamType;
import org.jooq.tools.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;



/**
 * The <code>AND</code> statement.
 */
@SuppressWarnings({ "unused" })
final class And
extends
    AbstractCondition
implements
    QOM.And
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
    final boolean parenthesised(Context<?> ctx) {
        return true;
    }

    @Override
    public final void accept(Context<?> ctx) {














        {
            ctx.sqlIndentStart('(');
            Expression.acceptAssociative(
                ctx,
                this,
                Operator.AND.toKeyword(),
                Context::formatSeparator
            );
            ctx.sqlIndentEnd(')');
        }
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
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Condition $arg1() {
        return arg1;
    }

    @Override
    public final Condition $arg2() {
        return arg2;
    }

    @Override
    public final QOM.And $arg1(Condition newValue) {
        return new And(newValue, $arg2());
    }

    @Override
    public final QOM.And $arg2(Condition newValue) {
        return new And($arg1(), newValue);
    }

    @Override
    public final Function2<? super Condition, ? super Condition, ? extends QOM.And> $constructor() {
        return (a1, a2) -> new And(a1, a2);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.And o) {
            return
                Objects.equals($arg1(), o.$arg1()) &&
                Objects.equals($arg2(), o.$arg2())
            ;
        }
        else
            return super.equals(that);
    }
}
