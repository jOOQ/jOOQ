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
import java.util.Set;



/**
 * The <code>ROW EQ</code> statement.
 */
@SuppressWarnings({ "unused" })
final class RowEq<T extends Row>
extends
    AbstractCondition
implements
    QOM.RowEq<T>
{

    final T arg1;
    final T arg2;

    RowEq(
        T arg1,
        T arg2
    ) {

        this.arg1 = (T) ((AbstractRow) arg1).convertTo(arg2);
        this.arg2 = (T) ((AbstractRow) arg2).convertTo(arg1);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------







    private static final Set<SQLDialect> EMULATE_EQ_AND_NE  = SQLDialect.supportedBy(DERBY, FIREBIRD);
    private static final Set<SQLDialect> EMULATE_RANGES     = SQLDialect.supportedBy(CUBRID, DERBY, FIREBIRD);

    @Override
    public final void accept(Context<?> ctx) {







        RowEq.acceptCompareCondition(ctx, arg1, org.jooq.Comparator.EQUALS, arg2);
    }

    static final <T extends Row> void acceptCompareCondition(
        Context<?> ctx,
        T arg1,
        org.jooq.Comparator op,
        T arg2
    ) {
        ctx.visit(new RowCondition(arg1, arg2, op));
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static final Condition emulate(
        Row r1,
        Row r2,
        org.jooq.Comparator comp,
        org.jooq.Comparator last
    ) {
        Condition result = r1.field(r1.size() - 1).compare(last, (Field) r2.field(r1.size() - 1));

        for (int i = r1.size() - 2; i >= 0; i--) {
            Field e1 = r1.field(i);
            Field e2 = r2.field(i);
            result = e1.compare(comp, e2).or(e1.eq(e2).and(result));
        }

        return result;
    }

    /**
     * @deprecated - [#12425] After the QOM refactoring, this should be improved
     */
    @Deprecated
    static final org.jooq.Comparator comparator(Condition condition) {
        if (condition instanceof RowEq)
            return org.jooq.Comparator.EQUALS;
        else if (condition instanceof RowNe)
            return org.jooq.Comparator.NOT_EQUALS;
        else if (condition instanceof RowGt)
            return org.jooq.Comparator.GREATER;
        else if (condition instanceof RowGe)
            return org.jooq.Comparator.GREATER_OR_EQUAL;
        else if (condition instanceof RowLt)
            return org.jooq.Comparator.LESS;
        else if (condition instanceof RowLe)
            return org.jooq.Comparator.LESS_OR_EQUAL;
        else
            return null;
    }












    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final T $arg1() {
        return arg1;
    }

    @Override
    public final T $arg2() {
        return arg2;
    }

    @Override
    public final QOM.RowEq<T> $arg1(T newValue) {
        return $constructor().apply(newValue, $arg2());
    }

    @Override
    public final QOM.RowEq<T> $arg2(T newValue) {
        return $constructor().apply($arg1(), newValue);
    }

    @Override
    public final Function2<? super T, ? super T, ? extends QOM.RowEq<T>> $constructor() {
        return (a1, a2) -> new RowEq<>(a1, a2);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.RowEq<?> o) {
            return
                StringUtils.equals($arg1(), o.$arg1()) &&
                StringUtils.equals($arg2(), o.$arg2())
            ;
        }
        else
            return super.equals(that);
    }
}
