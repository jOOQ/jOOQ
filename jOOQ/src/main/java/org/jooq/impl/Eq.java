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
import java.util.function.*;
import java.util.stream.*;


/**
 * The <code>EQ</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class Eq<T>
extends
    AbstractCondition
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







        Eq.acceptCompareCondition(ctx, this, arg1, arg2, RowN::eq, RowN::eq, c -> c.visit(arg1).sql(" = ").visit(arg2));
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
    static final Expression.Expr<Field<?>> expr(Condition condition) {
        if (condition instanceof Eq)
            return new Expression.Expr<>(((Eq<?>) condition).arg1, org.jooq.Comparator.EQUALS.toKeyword(), ((Eq<?>) condition).arg2);
        else if (condition instanceof Ne)
            return new Expression.Expr<>(((Ne<?>) condition).arg1, org.jooq.Comparator.NOT_EQUALS.toKeyword(), ((Ne<?>) condition).arg2);
        else if (condition instanceof Gt)
            return new Expression.Expr<>(((Gt<?>) condition).arg1, org.jooq.Comparator.GREATER.toKeyword(), ((Gt<?>) condition).arg2);
        else if (condition instanceof Ge)
            return new Expression.Expr<>(((Ge<?>) condition).arg1, org.jooq.Comparator.GREATER_OR_EQUAL.toKeyword(), ((Ge<?>) condition).arg2);
        else if (condition instanceof Lt)
            return new Expression.Expr<>(((Lt<?>) condition).arg1, org.jooq.Comparator.LESS.toKeyword(), ((Lt<?>) condition).arg2);
        else if (condition instanceof Le)
            return new Expression.Expr<>(((Le<?>) condition).arg1, org.jooq.Comparator.LESS_OR_EQUAL.toKeyword(), ((Le<?>) condition).arg2);
        else
            return null;
    }

    /**
     * @deprecated - [#12425] After the QOM refactoring, this should be improved
     */
    @Deprecated
    static final Comparator comparator(Condition condition) {
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

    /**
     * @deprecated - [#12425] After the QOM refactoring, this should be improved
     */
    @Deprecated
    static final <T> Condition condition(Expression.Expr<? extends Field<T>> expr) {
        if (expr.op().equals(org.jooq.Comparator.EQUALS.toKeyword()))
            return expr.lhs().eq(expr.rhs());
        else if (expr.op().equals(org.jooq.Comparator.NOT_EQUALS.toKeyword()))
            return expr.lhs().ne(expr.rhs());
        else if (expr.op().equals(org.jooq.Comparator.LESS.toKeyword()))
            return expr.lhs().lt(expr.rhs());
        else if (expr.op().equals(org.jooq.Comparator.LESS_OR_EQUAL.toKeyword()))
            return expr.lhs().le(expr.rhs());
        else if (expr.op().equals(org.jooq.Comparator.GREATER.toKeyword()))
            return expr.lhs().gt(expr.rhs());
        else if (expr.op().equals(org.jooq.Comparator.GREATER_OR_EQUAL.toKeyword()))
            return expr.lhs().ge(expr.rhs());
        else
            return null;
    }












    // -------------------------------------------------------------------------
    // The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof Eq) {
            return
                StringUtils.equals(arg1, ((Eq) that).arg1) &&
                StringUtils.equals(arg2, ((Eq) that).arg2)
            ;
        }
        else
            return super.equals(that);
    }
}
