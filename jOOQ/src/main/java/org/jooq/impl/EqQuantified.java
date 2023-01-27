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
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
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
final class EqQuantified<T>
extends
    AbstractCondition
implements
    QOM.EqQuantified<T>
{

    final Field<T>                                        arg1;
    final org.jooq.QuantifiedSelect<? extends Record1<T>> arg2;

    EqQuantified(
        Field<T> arg1,
        org.jooq.QuantifiedSelect<? extends Record1<T>> arg2
    ) {

        this.arg1 = nullSafeNotNull(arg1, (DataType) OTHER);
        this.arg2 = arg2;
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    private static final Set<SQLDialect> NO_SUPPORT_QUANTIFIED_LIKE       = SQLDialect.supportedBy(CUBRID, DERBY, FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, SQLITE);
    private static final Set<SQLDialect> NO_SUPPORT_QUANTIFIED_SIMILAR_TO = SQLDialect.supportedBy(CUBRID, DERBY, FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB);
    private static final Set<SQLDialect> SUPPORTS_QUANTIFIED_ARRAYS       = SQLDialect.supportedBy(POSTGRES);

    @Override
    public final void accept(Context<?> ctx) {







        EqQuantified.acceptCompareCondition(ctx, this, arg1, org.jooq.Comparator.EQUALS, arg2);
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
        org.jooq.QuantifiedSelect<? extends Record1<T>> arg2
    ) {
        acceptCompareCondition(ctx, condition, arg1, op, arg2, null);
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
        org.jooq.QuantifiedSelect<? extends Record1<T>> arg2,
        Character escape
    ) {
        SelectQueryImpl<?> s;

        if (arg1.getDataType().isEmbeddable()) {
            ctx.visit(row(embeddedFields(arg1)).compare(op, arg2));
        }
        else if ((op == org.jooq.Comparator.EQUALS || op == org.jooq.Comparator.NOT_EQUALS)
                && (arg2 instanceof QOM.QuantifiedSelect)
                && (s = Transformations.subqueryWithLimit(((QOM.QuantifiedSelect<?>) arg2).$query())) != null
                && Transformations.transformInConditionSubqueryWithLimitToDerivedTable(ctx.configuration())) {






        }










        else {
            boolean quantifiedArrayParam = arg2 instanceof QOM.QuantifiedArray<?> a ? a.$arg2() instanceof Param : false;
            boolean quantifiedArray = arg2 instanceof QOM.QuantifiedArray<?> a ? a.$arg2() instanceof Array : false;
            boolean emulateOperator;

            switch (op) {
                case LIKE:
                case NOT_LIKE:
                case LIKE_IGNORE_CASE:
                case NOT_LIKE_IGNORE_CASE:
                    emulateOperator = escape != null || NO_SUPPORT_QUANTIFIED_LIKE.contains(ctx.dialect());
                    break;
                case SIMILAR_TO:
                case NOT_SIMILAR_TO:
                    emulateOperator = escape != null || NO_SUPPORT_QUANTIFIED_SIMILAR_TO.contains(ctx.dialect());
                    break;
                default:
                    emulateOperator = false;
                    break;
            }

            // [#9224] Special case when a SQL dialect actually supports quantified
            //         arrays, such as x = any(?::int[]) in PostgreSQL
            if (quantifiedArrayParam && SUPPORTS_QUANTIFIED_ARRAYS.contains(ctx.dialect()) && !emulateOperator) {
                accept1(ctx, arg1, op, arg2);
            }
            else if (quantifiedArrayParam || quantifiedArray) {
                QOM.QuantifiedArray<?> a = (org.jooq.impl.QOM.QuantifiedArray<?>) arg2;
                ctx.visit(DSL.condition(
                    a.$quantifier() == Quantifier.ALL ? org.jooq.Operator.AND : org.jooq.Operator.OR,
                    a.$array() instanceof Array
                        ? map(((Array) a.$array()).$elements(), v -> comparisonCondition(arg1, op, (Field<String>) v, escape))
                        : map(((Param<? extends Object[]>) a.$array()).getValue(), v -> v instanceof Field ? comparisonCondition(arg1, op, (Field<String>) v, escape) : comparisonCondition(arg1, op, v, escape))
                ));
            }
            else if (emulateOperator) {
                Field<String> pattern = DSL.field(name("pattern"), VARCHAR);
                Condition c;
                Field<Boolean> lhs;

                switch (op) {
                    case NOT_LIKE:
                    case NOT_SIMILAR_TO:
                    case NOT_LIKE_IGNORE_CASE:
                        c = comparisonCondition(arg1, inverse(op), pattern, escape);
                        lhs = inline(false);
                        break;
                    case LIKE:
                    case SIMILAR_TO:
                    case LIKE_IGNORE_CASE:
                        c = comparisonCondition(arg1, op, pattern, escape);
                        lhs = inline(true);
                        break;
                    default:
                        throw new IllegalStateException();
                }

                Table<?> t;
                Quantifier q;

                if (arg2 instanceof QuantifiedArray<?> a) {
                    t = new ArrayTable(a.$array()).asTable("t", "pattern");
                    q = a.$quantifier();
                }
                else {
                    QOM.QuantifiedSelect<?> qs = (QOM.QuantifiedSelect<?>) arg2;
                    t = new AliasedSelect<>(qs.$query(), true, true, false, name("pattern")).as("t");
                    q = qs.$quantifier();
                }

                ctx.visit(lhs.eq(quantify(q, select(c).from(t))));
            }
            else
                accept1(ctx, arg1, op, arg2);
        }
    }

    private static final void accept1(
        Context<?> ctx,
        Field<?> arg1,
        org.jooq.Comparator op,
        org.jooq.QuantifiedSelect<?> arg2
    ) {
        switch (ctx.family()) {








            default:
                ctx.visit(arg1)
                   .sql(' ')
                   .visit(op.toKeyword())
                   .sql(' ')
                   .visit(arg2);

                break;
        }
    }

    private static final org.jooq.Comparator inverse(org.jooq.Comparator operator) {
        switch (operator) {
            case IN:                   return org.jooq.Comparator.NOT_IN;
            case NOT_IN:               return org.jooq.Comparator.IN;
            case EQUALS:               return org.jooq.Comparator.NOT_EQUALS;
            case NOT_EQUALS:           return org.jooq.Comparator.EQUALS;
            case LESS:                 return org.jooq.Comparator.GREATER_OR_EQUAL;
            case LESS_OR_EQUAL:        return org.jooq.Comparator.GREATER;
            case GREATER:              return org.jooq.Comparator.LESS_OR_EQUAL;
            case GREATER_OR_EQUAL:     return org.jooq.Comparator.LESS;
            case IS_DISTINCT_FROM:     return org.jooq.Comparator.IS_NOT_DISTINCT_FROM;
            case IS_NOT_DISTINCT_FROM: return org.jooq.Comparator.IS_DISTINCT_FROM;
            case LIKE:                 return org.jooq.Comparator.NOT_LIKE;
            case NOT_LIKE:             return org.jooq.Comparator.LIKE;
            case SIMILAR_TO:           return org.jooq.Comparator.NOT_SIMILAR_TO;
            case NOT_SIMILAR_TO:       return org.jooq.Comparator.SIMILAR_TO;
            case LIKE_IGNORE_CASE:     return org.jooq.Comparator.NOT_LIKE_IGNORE_CASE;
            case NOT_LIKE_IGNORE_CASE: return org.jooq.Comparator.LIKE_IGNORE_CASE;
            default:                   throw new IllegalStateException();
        }
    }

    private static final Condition comparisonCondition(
        Field<?> arg1,
        org.jooq.Comparator op,
        Field<String> arg2,
        Character escape
    ) {
        switch (op) {
            case LIKE:
                return escape != null ? arg1.like(arg2, escape) : arg1.like(arg2);

            case NOT_LIKE:
                return escape != null ? arg1.notLike(arg2, escape) : arg1.notLike(arg2);

            case SIMILAR_TO:
                return escape != null ? arg1.similarTo(arg2, escape) : arg1.similarTo(arg2);

            case NOT_SIMILAR_TO:
                return escape != null ? arg1.notSimilarTo(arg2, escape) : arg1.notSimilarTo(arg2);

            case LIKE_IGNORE_CASE:
                return escape != null ? arg1.likeIgnoreCase(arg2, escape) : arg1.likeIgnoreCase(arg2);

            case NOT_LIKE_IGNORE_CASE:
                return escape != null ? arg1.notLikeIgnoreCase(arg2, escape) : arg1.notLikeIgnoreCase(arg2);

            default:
                return ((Field) arg1).compare(op, arg2);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static final Condition comparisonCondition(
        Field<?> arg1,
        org.jooq.Comparator op,
        Object arg2,
        Character escape
    ) {
        switch (op) {
            case LIKE:
                return escape != null ? arg1.like(Convert.convert(arg2, String.class), escape) : arg1.like(Convert.convert(arg2, String.class));

            case NOT_LIKE:
                return escape != null ? arg1.notLike(Convert.convert(arg2, String.class), escape) : arg1.notLike(Convert.convert(arg2, String.class));

            case SIMILAR_TO:
                return escape != null ? arg1.similarTo(Convert.convert(arg2, String.class), escape) : arg1.similarTo(Convert.convert(arg2, String.class));

            case NOT_SIMILAR_TO:
                return escape != null ? arg1.notSimilarTo(Convert.convert(arg2, String.class), escape) : arg1.notSimilarTo(Convert.convert(arg2, String.class));

            case LIKE_IGNORE_CASE:
                return escape != null ? arg1.likeIgnoreCase(Convert.convert(arg2, String.class), escape) : arg1.likeIgnoreCase(Convert.convert(arg2, String.class));

            case NOT_LIKE_IGNORE_CASE:
                return escape != null ? arg1.notLikeIgnoreCase(Convert.convert(arg2, String.class), escape) : arg1.notLikeIgnoreCase(Convert.convert(arg2, String.class));

            default:
                return ((Field) arg1).compare(op, arg2);
        }
    }












    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<T> $arg1() {
        return arg1;
    }

    @Override
    public final org.jooq.QuantifiedSelect<? extends Record1<T>> $arg2() {
        return arg2;
    }

    @Override
    public final QOM.EqQuantified<T> $arg1(Field<T> newValue) {
        return $constructor().apply(newValue, $arg2());
    }

    @Override
    public final QOM.EqQuantified<T> $arg2(org.jooq.QuantifiedSelect<? extends Record1<T>> newValue) {
        return $constructor().apply($arg1(), newValue);
    }

    @Override
    public final Function2<? super Field<T>, ? super org.jooq.QuantifiedSelect<? extends Record1<T>>, ? extends QOM.EqQuantified<T>> $constructor() {
        return (a1, a2) -> new EqQuantified<>(a1, a2);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.EqQuantified<?> o) {
            return
                StringUtils.equals($arg1(), o.$arg1()) &&
                StringUtils.equals($arg2(), o.$arg2())
            ;
        }
        else
            return super.equals(that);
    }
}
