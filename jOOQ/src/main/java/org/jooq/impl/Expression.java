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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
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

import static org.jooq.DatePart.MONTH;
import static org.jooq.DatePart.SECOND;
// ...
// ...
import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.SQLDialect.DERBY;
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.YUGABYTEDB;
import static org.jooq.conf.RenderOptionalKeyword.ON;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.inlined;
import static org.jooq.impl.DSL.keyword;
import static org.jooq.impl.DSL.val;
import static org.jooq.impl.ExpressionOperator.ADD;
import static org.jooq.impl.ExpressionOperator.MULTIPLY;
import static org.jooq.impl.ExpressionOperator.SUBTRACT;
import static org.jooq.impl.Keywords.K_AS;
import static org.jooq.impl.Keywords.K_CAST;
import static org.jooq.impl.Keywords.K_DAY;
import static org.jooq.impl.Keywords.K_DAY_MICROSECOND;
import static org.jooq.impl.Keywords.K_DAY_MILLISECOND;
import static org.jooq.impl.Keywords.K_DAY_TO_SECOND;
import static org.jooq.impl.Keywords.K_INTERVAL;
import static org.jooq.impl.Keywords.K_MILLISECOND;
import static org.jooq.impl.Keywords.K_MONTH;
import static org.jooq.impl.Keywords.K_SECOND;
import static org.jooq.impl.Keywords.K_TO;
import static org.jooq.impl.Keywords.K_YEAR_MONTH;
import static org.jooq.impl.Keywords.K_YEAR_TO_MONTH;
import static org.jooq.impl.Names.N_ADD_DAYS;
import static org.jooq.impl.Names.N_ADD_MONTHS;
import static org.jooq.impl.Names.N_ADD_SECONDS;
import static org.jooq.impl.Names.N_DATEADD;
import static org.jooq.impl.Names.N_DATE_ADD;
import static org.jooq.impl.Names.N_SQL_TSI_FRAC_SECOND;
import static org.jooq.impl.Names.N_SQL_TSI_MILLI_SECOND;
import static org.jooq.impl.Names.N_SQL_TSI_MONTH;
import static org.jooq.impl.Names.N_SQL_TSI_SECOND;
import static org.jooq.impl.Names.N_STRFTIME;
import static org.jooq.impl.Names.N_TIMESTAMPADD;
import static org.jooq.impl.Names.N_TIMESTAMP_ADD;
import static org.jooq.impl.Names.N_TIMESTAMP_SUB;
import static org.jooq.impl.SQLDataType.INTEGER;
import static org.jooq.impl.SQLDataType.TIMESTAMP;
import static org.jooq.impl.Tools.castIfNeeded;

import java.sql.Timestamp;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Pattern;

import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.DatePart;
import org.jooq.Field;
import org.jooq.Function2;
import org.jooq.Param;
// ...
import org.jooq.QueryPart;
import org.jooq.SQLDialect;
import org.jooq.Typed;
import org.jooq.conf.TransformUnneededArithmeticExpressions;
import org.jooq.exception.DataTypeException;
import org.jooq.impl.QOM.UOperator2;
import org.jooq.impl.QOM.UTransient;
import org.jooq.types.DayToSecond;
import org.jooq.types.Interval;
import org.jooq.types.YearToMonth;
import org.jooq.types.YearToSecond;

final class Expression<T> extends AbstractTransformable<T> implements UOperator2<Field<T>, Field<?>, Field<T>> {
    static final Set<SQLDialect>     HASH_OP_FOR_BIT_XOR    = SQLDialect.supportedBy(POSTGRES, YUGABYTEDB);
    static final Set<SQLDialect>     SUPPORT_YEAR_TO_SECOND = SQLDialect.supportedBy(POSTGRES, YUGABYTEDB);

    private final ExpressionOperator operator;
    private final boolean            internal;
    private final Field<T>           lhs;
    private final Field<?>           rhs;

    Expression(ExpressionOperator operator, boolean internal, Field<T> lhs, Field<?> rhs) {
        super(DSL.name(operator.toSQL()), lhs.getDataType());

        this.operator = operator;
        this.internal = internal;
        this.lhs = lhs;
        this.rhs = rhs;
    }

    static final DataType<?> getExpressionDataType(AbstractField<?> l, ExpressionOperator operator, AbstractField<?> r) {
        DataType<?> lt = l.getExpressionDataType();
        DataType<?> rt = r.getExpressionDataType();

        switch (operator) {
            case MULTIPLY:
            case DIVIDE:
                return rt.isInterval() ? rt : lt;

            case ADD:
                return lt.isInterval() ? rt : lt;
        }

        return lt;
    }

    @Override
    final DataType<?> getExpressionDataType() {

        // [#11959] Workaround for lack of proper data type information for interval based expressions
        return getExpressionDataType((AbstractField<?>) lhs, operator, (AbstractField<?>) rhs);
    }

    @Override
    final void accept0(Context<?> ctx) {
        SQLDialect family = ctx.family();








        // ---------------------------------------------------------------------
        // XXX: Date time arithmetic operators
        // ---------------------------------------------------------------------

        // [#585] Date time arithmetic for numeric or interval RHS
        if ((ADD == operator || SUBTRACT == operator) &&
             lhs.getDataType().isDateTime() &&
            (rhs.getDataType().isNumeric() ||
             rhs.getDataType().isInterval()))
            ctx.visit(new DateExpression<>(lhs, operator, rhs));

        // ---------------------------------------------------------------------
        // XXX: Other operators
        // ---------------------------------------------------------------------

        // Use the default operator expression for all other cases
        else
            ctx.sql('(').visit(lhs).sql(' ').sql(operator.toSQL()).sql(' ').visit(rhs).sql(')');
    }




















    @Override
    public final Field<T> transform(TransformUnneededArithmeticExpressions transform) {
        return transform((UOperator2<Field<T>, Field<T>, Field<T>>) (UOperator2) this, lhs, operator, (Field<T>) rhs, internal, transform);
    }

    static final <T> Field<T> transform(
        UOperator2<Field<T>, Field<T>, Field<T>> expression,
        Field<T> lhs,
        ExpressionOperator operator,
        Field<T> rhs,
        boolean internal,
        TransformUnneededArithmeticExpressions transform
    ) {






















































        return (Field<T>) expression;
    }































    /**
     * In some expressions, the lhs can be safely assumed to be a single number
     */
    @SuppressWarnings("unchecked")
    private final Field<Number> lhsAsNumber() {
        return (Field<Number>) lhs;
    }

    /**
     * In some expressions, the rhs can be safely assumed to be a single number
     */
    @SuppressWarnings("unchecked")
    private final Field<Number> rhsAsNumber() {
        return (Field<Number>) rhs;
    }

    // E.g. +2 00:00:00.000000000
    private static final Pattern TRUNC_TO_MICROS = Pattern.compile("([^.]*\\.\\d{0,6})\\d{0,3}");

    /**
     * Return the expression to be rendered when the RHS is an interval type
     */
    private static class DateExpression<T> extends AbstractField<T> implements UTransient {

        private final Field<T>           lhs;
        private final ExpressionOperator operator;
        private final Field<?>           rhs;

        DateExpression(Field<T> lhs, ExpressionOperator operator, Field<?> rhs) {
            super(DSL.name(operator.toSQL()), lhs.getDataType());

            this.lhs = lhs;
            this.operator = operator;
            this.rhs = rhs;
        }

        private final <U> Field<U> p(U u) {
            Param<U> result = val(u);

            if (((Param<?>) rhs).isInline())
                ((AbstractParamX<?>) result).setInline0(true);

            return result;
        }

        @Override
        public final void accept(Context<?> ctx) {
            if (rhs.getDataType().getFromType() == YearToSecond.class && !SUPPORT_YEAR_TO_SECOND.contains(ctx.dialect()))
                acceptYTSExpression(ctx);
            else if (rhs.getDataType().isInterval())
                acceptIntervalExpression(ctx);
            else
                acceptNumberExpression(ctx);
        }

        private final void acceptYTSExpression(Context<?> ctx) {
            if (rhs instanceof Param) {
                YearToSecond yts = rhsAsYTS();

                ctx.visit(new DateExpression<>(
                    new DateExpression<>(lhs, operator, p(yts.getYearToMonth())),
                    operator,
                    p(yts.getDayToSecond())
                ));
            }
            else {
                acceptIntervalExpression(ctx);
            }
        }

        @SuppressWarnings({ "unchecked", "rawtypes" })
        private final void acceptIntervalExpression(Context<?> ctx) {
            SQLDialect family = ctx.family();

            int sign = (operator == ADD) ? 1 : -1;
            switch (family) {


                case CUBRID:
                case MARIADB:
                case MYSQL: {
                    Interval interval = rhsAsInterval();

                    if (operator == SUBTRACT)
                        interval = interval.neg();

                    if (rhs.getDataType().getFromType() == YearToMonth.class)
                        ctx.visit(N_DATE_ADD).sql('(').visit(lhs).sql(", ").visit(K_INTERVAL).sql(' ')
                           .visit(Tools.field(interval, SQLDataType.VARCHAR)).sql(' ').visit(K_YEAR_MONTH).sql(')');
                    else if (family == CUBRID)
                        ctx.visit(N_DATE_ADD).sql('(').visit(lhs).sql(", ").visit(K_INTERVAL).sql(' ')
                           .visit(Tools.field(interval, SQLDataType.VARCHAR)).sql(' ').visit(K_DAY_MILLISECOND).sql(')');

                    // [#6820] Workaround for bugs:
                    //         https://bugs.mysql.com/bug.php?id=88573
                    //         https://jira.mariadb.org/browse/MDEV-14452
                    else
                        ctx.visit(N_DATE_ADD).sql('(').visit(lhs).sql(", ").visit(K_INTERVAL).sql(' ')
                           .visit(Tools.field(TRUNC_TO_MICROS.matcher("" + interval).replaceAll("$1"), SQLDataType.VARCHAR)).sql(' ').visit(K_DAY_MICROSECOND).sql(')');

                    break;
                }

                case DERBY: {
                    boolean needsCast = getDataType().getType() != Timestamp.class;
                    if (needsCast)
                        ctx.visit(K_CAST).sql('(');

                    if (rhs.getDataType().getFromType() == YearToMonth.class)
                        ctx.sql("{fn ").visit(N_TIMESTAMPADD).sql('(').visit(N_SQL_TSI_MONTH).sql(", ")
                            .visit(p(sign * rhsAsYTM().intValue())).sql(", ").visit(lhs).sql(") }");
                    else
                        ctx.sql("{fn ").visit(N_TIMESTAMPADD).sql('(').visit(N_SQL_TSI_SECOND).sql(", ")
                            .visit(p(sign * (long) rhsAsDTS().getTotalSeconds())).sql(", {fn ")
                            .visit(N_TIMESTAMPADD).sql('(').visit(ctx.family() == DERBY ? N_SQL_TSI_FRAC_SECOND : N_SQL_TSI_MILLI_SECOND).sql(", ")
                            .visit(p(sign * (long) rhsAsDTS().getMilli() * (ctx.family() == DERBY ? 1000000L : 1L))).sql(", ").visit(lhs).sql(") }) }");

                    // [#1883] TIMESTAMPADD returns TIMESTAMP columns. If this
                    // is a DATE column, cast it to DATE
                    if (needsCast)
                        ctx.sql(' ').visit(K_AS).sql(' ').visit(keyword(getDataType().getCastTypeName(ctx.configuration()))).sql(')');

                    break;
                }

                case FIREBIRD: {
                    if (rhs.getDataType().getFromType() == YearToMonth.class)
                        ctx.visit(N_DATEADD).sql('(').visit(K_MONTH).sql(", ").visit(p(sign * rhsAsYTM().intValue())).sql(", ").visit(lhs).sql(')');

                    // [#10448] Firebird only supports adding integers
                    else if (rhsAsDTS().getMilli() > 0)
                        ctx.visit(N_DATEADD).sql('(').visit(K_MILLISECOND).sql(", ").visit(p(sign * (long) rhsAsDTS().getMilli())).sql(", ")
                           .visit(N_DATEADD).sql('(').visit(K_SECOND).sql(", ").visit(p(sign * (long) rhsAsDTS().getTotalSeconds())).sql(", ").visit(lhs).sql(')')
                           .sql(')');
                    else
                        ctx.visit(N_DATEADD).sql('(').visit(K_SECOND).sql(", ").visit(p(sign * (long) rhsAsDTS().getTotalSeconds())).sql(", ").visit(lhs).sql(')');

                    break;
                }

                case SQLITE: {
                    boolean ytm = rhs.getDataType().getFromType() == YearToMonth.class;
                    Field<?> interval = p(ytm ? rhsAsYTM().intValue() : rhsAsDTS().getTotalSeconds());

                    if (sign < 0)
                        interval = interval.neg();

                    interval = interval.concat(inline(ytm ? " months" : " seconds"));
                    ctx.visit(N_STRFTIME).sql("('%Y-%m-%d %H:%M:%f', ").visit(lhs).sql(", ").visit(interval).sql(')');
                    break;
                }






































































































































































































                default:
                    ctx.sql('(').visit(lhs).sql(' ').sql(operator.toSQL()).sql(' ').visit(rhs).sql(')');
                    break;
            }
        }

        /**
         * Return the expression to be rendered when the RHS is a number type
         */
        @SuppressWarnings({ "unchecked", "rawtypes" })
        private final void acceptNumberExpression(Context<?> ctx) {
            switch (ctx.family()) {


































                case FIREBIRD: {
                    if (operator == ADD)
                        ctx.visit(N_DATEADD).sql('(').visit(K_DAY).sql(", ").visit(rhsAsNumber()).sql(", ").visit(lhs).sql(')');
                    else
                        ctx.visit(N_DATEADD).sql('(').visit(K_DAY).sql(", ").visit(rhsAsNumber().neg()).sql(", ").visit(lhs).sql(')');

                    break;
                }


                case HSQLDB: {
                    if (operator == ADD)
                        ctx.visit(lhs.add(DSL.field("({0}) day", rhsAsNumber())));
                    else
                        ctx.visit(lhs.sub(DSL.field("({0}) day", rhsAsNumber())));

                    break;
                }

                case DERBY: {
                    boolean needsCast = getDataType().getType() != Timestamp.class;
                    if (needsCast)
                        ctx.visit(K_CAST).sql('(');

                    if (operator == ADD)
                        ctx.sql("{fn ").visit(N_TIMESTAMPADD).sql('(').visit(keyword("sql_tsi_day")).sql(", ").visit(rhsAsNumber()).sql(", ").visit(lhs).sql(") }");
                    else
                        ctx.sql("{fn ").visit(N_TIMESTAMPADD).sql('(').visit(keyword("sql_tsi_day")).sql(", ").visit(rhsAsNumber().neg()).sql(", ").visit(lhs).sql(") }");

                    // [#1883] TIMESTAMPADD returns TIMESTAMP columns. If this
                    // is a DATE column, cast it to DATE
                    if (needsCast)
                        ctx.sql(' ').visit(K_AS).sql(' ').visit(keyword(getDataType().getCastTypeName(ctx.configuration()))).sql(')');

                    break;
                }



                case CUBRID:
                case MARIADB:
                case MYSQL: {
                    if (operator == ADD)
                        ctx.visit(N_DATE_ADD).sql('(').visit(lhs).sql(", ").visit(K_INTERVAL).sql(' ').visit(rhsAsNumber()).sql(' ').visit(K_DAY).sql(')');
                    else
                        ctx.visit(N_DATE_ADD).sql('(').visit(lhs).sql(", ").visit(K_INTERVAL).sql(' ').visit(rhsAsNumber().neg()).sql(' ').visit(K_DAY).sql(')');

                    break;
                }

























                case POSTGRES:
                case YUGABYTEDB: {

                    // This seems to be the most reliable way to avoid issues
                    // with incompatible data types and timezones
                    // ? + CAST (? || ' days' as interval)
                    if (operator == ADD)
                        ctx.visit(new DateAdd(lhs, rhsAsNumber(), DatePart.DAY));
                    else
                        ctx.visit(new DateAdd(lhs, rhsAsNumber().neg(), DatePart.DAY));

                    break;
                }

                case SQLITE:
                    if (operator == ADD)
                        ctx.visit(N_STRFTIME).sql("('%Y-%m-%d %H:%M:%f', ").visit(lhs).sql(", ").visit(rhsAsNumber().concat(inline(" day"))).sql(')');
                    else
                        ctx.visit(N_STRFTIME).sql("('%Y-%m-%d %H:%M:%f', ").visit(lhs).sql(", ").visit(rhsAsNumber().neg().concat(inline(" day"))).sql(')');

                    break;











                // These dialects can add / subtract days using +/- operators
                default:
                    ctx.sql('(').visit(lhs).sql(' ').sql(operator.toSQL()).sql(' ').visit(rhs).sql(')');
                    break;
            }
        }

        @SuppressWarnings("unchecked")
        private final YearToSecond rhsAsYTS() {
            try {
                return ((Param<YearToSecond>) rhs).getValue();
            }
            catch (ClassCastException e) {
                throw new DataTypeException("Cannot perform datetime arithmetic with a non-numeric, non-interval data type on the right hand side of the expression: " + rhs, e);
            }
        }

        @SuppressWarnings("unchecked")
        private final YearToMonth rhsAsYTM() {
            try {
                return ((Param<YearToMonth>) rhs).getValue();
            }
            catch (ClassCastException e) {
                throw new DataTypeException("Cannot perform datetime arithmetic with a non-numeric, non-interval data type on the right hand side of the expression: " + rhs, e);
            }
        }

        @SuppressWarnings("unchecked")
        private final DayToSecond rhsAsDTS() {
            try {
                return ((Param<DayToSecond>) rhs).getValue();
            }
            catch (ClassCastException e) {
                throw new DataTypeException("Cannot perform datetime arithmetic with a non-numeric, non-interval data type on the right hand side of the expression: " + rhs, e);
            }
        }

        @SuppressWarnings("unchecked")
        private final Interval rhsAsInterval() {
            try {
                return ((Param<Interval>) rhs).getValue();
            }
            catch (ClassCastException e) {
                throw new DataTypeException("Cannot perform datetime arithmetic with a non-numeric, non-interval data type on the right hand side of the expression: " + rhs, e);
            }
        }

        /**
         * In some expressions, the rhs can be safely assumed to be a single number
         */
        @SuppressWarnings("unchecked")
        private final Field<Number> rhsAsNumber() {
            return (Field<Number>) rhs;
        }
    }

    static final record Expr<Q extends QueryPart>(Q lhs, Q rhs) {}

    @SuppressWarnings("unchecked")
    static final <Q1 extends QueryPart, Q2 extends UOperator2<Q1, Q1, Q2>> void acceptAssociative(
        Context<?> ctx,
        Q2 exp,
        QueryPart operator,
        Consumer<? super Context<?>> formatSeparator
    ) {
        Class<Q2> expType = (Class<Q2>) exp.getClass();

        // [#16725] Run this associative operand flattening logic only if there are any nested expressions
        if (!ON.equals(ctx.settings().getRenderOptionalAssociativityParentheses())
            && (exp.$arg1().getClass() == expType || exp.$arg2().getClass() == expType)
        ) {
            Expr<Q1> e = new Expr<>(exp.$arg1(), exp.$arg2());
            List<Q1> elements = new ArrayList<>();

            // Effectively Deque<Q1|Expr<Q1>>
            Deque<Object> queue = new ArrayDeque<>();
            queue.push(e);

            // [#14356] Iterative breadth first tree traversal trading stack space
            //          for heap space to prevent StackOverflowError if tree is
            //          10000+ elements deep
            for (Object o; (o = queue.pollFirst()) != null;) {
                if (o instanceof Expr) {
                    Expr<Q1> p = (Expr<Q1>) o;

                    // [#10665] Associativity is only given for two operands of the same data type
                    // [#12896] ... and if the feature is enabled
                    boolean a =
                        p.lhs instanceof Typed && p.rhs instanceof Typed
                      ? ((Typed<?>) p.lhs).getDataType().equals(((Typed<?>) p.rhs).getDataType())
                      : true;

                    // [#14356] Delay processing of RHS to emulate depth first traversal.
                    if (a && expType.isInstance(p.rhs))
                        queue.push(new Expr<>(((Q2) p.rhs).$arg1(), ((Q2) p.rhs).$arg2()));
                    else
                        queue.push(p.rhs);

                    if (a && expType.isInstance(p.lhs))
                        queue.push(new Expr<>(((Q2) p.lhs).$arg1(), ((Q2) p.lhs).$arg2()));
                    else
                        elements.add(p.lhs);
                }
                else
                    elements.add((Q1) o);
            }

            for (int i = 0; i < elements.size(); i++) {
                if (i > 0) {
                    formatSeparator.accept(ctx);
                    ctx.visit(operator).sql(' ');
                }

                ctx.visit(elements.get(i));
            }
        }
        else {
            ctx.visit(exp.$arg1());
            formatSeparator.accept(ctx);
            ctx.visit(operator).sql(' ');
            ctx.visit(exp.$arg2());
        }
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<T> $arg1() {
        return lhs;
    }

    @Override
    public final Field<?> $arg2() {
        return rhs;
    }

    @Override
    public final Expression<T> $arg1(Field<T> newValue) {
        return $constructor().apply(newValue, $arg2());
    }

    @Override
    public final Expression<T> $arg2(Field<?> newValue) {
        return $constructor().apply($arg1(), newValue);
    }

    @Override
    public final Function2<? super Field<T>, ? super Field<?>, ? extends Expression<T>> $constructor() {
        return (a1, a2) -> new Expression<>(operator, internal, a1, a2);
    }
}
