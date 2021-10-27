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
 * The <code>EQ</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class Eq<T>
extends
    AbstractCondition
implements
    QOM.Eq<T>
{

    final Field<T> arg1;
    final Field<T> arg2;

    Eq(
        Field<T> arg1,
        Field<T> arg2
    ) {

        this.arg1 = nullableIf(false, Tools.nullSafe(arg1, arg2.getDataType()));
        this.arg2 = nullableIf(false, Tools.nullSafe(arg2, arg1.getDataType()));
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    static final Clause[] CLAUSES = { Clause.CONDITION, Clause.CONDITION_COMPARISON };

    @Override
    public final void accept(Context<?> ctx) {







        Eq.acceptCompareCondition(ctx, this, arg1, org.jooq.Comparator.EQUALS, arg2, RowN::eq, RowN::eq, c -> c.visit(arg1).sql(" = ").visit(arg2));
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return Eq.CLAUSES;
    }

    /**
     * @deprecated - [#12425] After the QOM refactoring, this should be improved
     */
    @Deprecated
    static final <T> void acceptCompareCondition(
        Context<?> ctx,
        AbstractCondition condition,
        Field<T> arg1,
        org.jooq.Comparator op,
        Field<T> arg2,
        BiFunction<RowN, Select<?>, Condition> compareRowSubquery,
        BiFunction<RowN, RowN, Condition> compareRowRow,
        Consumer<? super Context<?>> acceptDefault
    ) {
        boolean field1Embeddable = arg1.getDataType().isEmbeddable();
        SelectQueryImpl<?> s;

        if (field1Embeddable && arg2 instanceof ScalarSubquery)
            ctx.visit(compareRowSubquery.apply(row(embeddedFields(arg1)), ((ScalarSubquery<?>) arg2).query));
        else if (field1Embeddable && arg2.getDataType().isEmbeddable())
            ctx.visit(compareRowRow.apply(row(embeddedFields(arg1)), row(embeddedFields(arg2))));
        else if ((op == org.jooq.Comparator.IN || op == org.jooq.Comparator.NOT_IN)
            && (s = Transformations.subqueryWithLimit(arg2)) != null
            && Transformations.transformInConditionSubqueryWithLimitToDerivedTable(ctx.configuration())) {



        }
        else if (arg1.getDataType().isMultiset()
                && arg2.getDataType().isMultiset()
                && !Boolean.TRUE.equals(ctx.data(DATA_MULTISET_CONDITION)))
            ctx.data(DATA_MULTISET_CONDITION, true, c -> c.visit(condition));
        else
            acceptDefault.accept(ctx);
    }

    /**
     * @deprecated - [#12425] After the QOM refactoring, this should be improved
     */
    @Deprecated
    static final org.jooq.Comparator comparator(Condition condition) {
        if (condition instanceof Eq)
            return org.jooq.Comparator.EQUALS;
        else if (condition instanceof Ne)
            return org.jooq.Comparator.NOT_EQUALS;
        else if (condition instanceof Gt)
            return org.jooq.Comparator.GREATER;
        else if (condition instanceof Ge)
            return org.jooq.Comparator.GREATER_OR_EQUAL;
        else if (condition instanceof Lt)
            return org.jooq.Comparator.LESS;
        else if (condition instanceof Le)
            return org.jooq.Comparator.LESS_OR_EQUAL;
        else
            return null;
    }












    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<T> $arg1() {
        return arg1;
    }

    @Override
    public final Field<T> $arg2() {
        return arg2;
    }

    @Override
    public final QOM.Eq<T> $arg1(Field<T> newValue) {
        return constructor().apply(newValue, $arg2());
    }

    @Override
    public final QOM.Eq<T> $arg2(Field<T> newValue) {
        return constructor().apply($arg1(), newValue);
    }

    @Override
    public final Function2<? super Field<T>, ? super Field<T>, ? extends QOM.Eq<T>> constructor() {
        return (a1, a2) -> new Eq<>(a1, a2);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.Eq) { QOM.Eq<?> o = (QOM.Eq<?>) that;
            return
                StringUtils.equals($arg1(), o.$arg1()) &&
                StringUtils.equals($arg2(), o.$arg2())
            ;
        }
        else
            return super.equals(that);
    }
}
