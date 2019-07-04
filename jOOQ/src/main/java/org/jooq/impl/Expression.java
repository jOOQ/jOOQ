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

import static org.jooq.DatePart.MONTH;
import static org.jooq.DatePart.SECOND;
// ...
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.HSQLDB;
// ...
// ...
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
// ...
// ...
import static org.jooq.impl.DSL.function;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.keyword;
import static org.jooq.impl.DSL.two;
import static org.jooq.impl.DSL.val;
import static org.jooq.impl.ExpressionOperator.ADD;
import static org.jooq.impl.ExpressionOperator.BIT_AND;
import static org.jooq.impl.ExpressionOperator.BIT_NAND;
import static org.jooq.impl.ExpressionOperator.BIT_NOR;
import static org.jooq.impl.ExpressionOperator.BIT_OR;
import static org.jooq.impl.ExpressionOperator.BIT_XNOR;
import static org.jooq.impl.ExpressionOperator.BIT_XOR;
import static org.jooq.impl.ExpressionOperator.SHL;
import static org.jooq.impl.ExpressionOperator.SHR;
import static org.jooq.impl.ExpressionOperator.SUBTRACT;
import static org.jooq.impl.Keywords.F_DATEADD;
import static org.jooq.impl.Keywords.F_DATE_ADD;
import static org.jooq.impl.Keywords.F_STRFTIME;
import static org.jooq.impl.Keywords.F_TIMESTAMPADD;
import static org.jooq.impl.Keywords.K_AS;
import static org.jooq.impl.Keywords.K_CAST;
import static org.jooq.impl.Keywords.K_DAY;
import static org.jooq.impl.Keywords.K_DAY_MICROSECOND;
import static org.jooq.impl.Keywords.K_DAY_MILLISECOND;
import static org.jooq.impl.Keywords.K_DAY_TO_SECOND;
import static org.jooq.impl.Keywords.K_INTERVAL;
import static org.jooq.impl.Keywords.K_MILLISECOND;
import static org.jooq.impl.Keywords.K_MONTH;
import static org.jooq.impl.Keywords.K_YEAR_MONTH;
import static org.jooq.impl.Keywords.K_YEAR_TO_MONTH;
import static org.jooq.impl.Tools.castIfNeeded;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.regex.Pattern;

import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.DatePart;
import org.jooq.Field;
import org.jooq.Param;
import org.jooq.SQLDialect;
import org.jooq.exception.DataTypeException;
import org.jooq.exception.SQLDialectNotSupportedException;
import org.jooq.types.DayToSecond;
import org.jooq.types.Interval;
import org.jooq.types.YearToMonth;
import org.jooq.types.YearToSecond;

final class Expression<T> extends AbstractField<T> {

    /**
     * Generated UID
     */
    private static final long                serialVersionUID    = -5522799070693019771L;
    private static final EnumSet<SQLDialect> SUPPORT_BIT_AND     = EnumSet.of(H2, HSQLDB);
    private static final EnumSet<SQLDialect> SUPPORT_BIT_OR_XOR  = EnumSet.of(H2, HSQLDB);
    private static final EnumSet<SQLDialect> EMULATE_BIT_XOR     = EnumSet.of(SQLITE);
    private static final EnumSet<SQLDialect> EMULATE_SHR_SHL     = EnumSet.of(H2, HSQLDB);
    private static final EnumSet<SQLDialect> HASH_OP_FOR_BIT_XOR = EnumSet.of(POSTGRES);

    private final Field<T>                   lhs;
    private final QueryPartList<Field<?>>    rhs;
    private final Field<?>[]                 arguments;
    private final ExpressionOperator         operator;

    Expression(ExpressionOperator operator, Field<T> lhs, Field<?>... rhs) {
        super(DSL.name(operator.toSQL()), lhs.getDataType());

        this.operator = operator;
        this.lhs = lhs;
        this.rhs = new QueryPartList<Field<?>>(rhs);
        this.arguments = Tools.combine(lhs, rhs);
    }

    @Override
    public final Field<T> add(Field<?> value) {
        if (operator == ExpressionOperator.ADD) {
            rhs.add(value);
            return this;
        }

        return super.add(value);
    }

    @Override
    public final Field<T> mul(Field<? extends Number> value) {
        if (operator == ExpressionOperator.MULTIPLY) {
            rhs.add(value);
            return this;
        }

        return super.mul(value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final void accept(Context<?> ctx) {
        SQLDialect family = ctx.family();

        // ---------------------------------------------------------------------
        // XXX: Bitwise operators
        // ---------------------------------------------------------------------

        // DB2, H2 and HSQLDB know functions, instead of operators
        if (BIT_AND == operator && SUPPORT_BIT_AND.contains(family))
            ctx.visit(function("bitand", getDataType(), arguments));
        else if (BIT_AND == operator && FIREBIRD == family)
            ctx.visit(function("bin_and", getDataType(), arguments));
        else if (BIT_XOR == operator && SUPPORT_BIT_OR_XOR.contains(family))
            ctx.visit(function("bitxor", getDataType(), arguments));
        else if (BIT_XOR == operator && FIREBIRD == family)
            ctx.visit(function("bin_xor", getDataType(), arguments));
        else if (BIT_OR == operator && SUPPORT_BIT_OR_XOR.contains(family))
            ctx.visit(function("bitor", getDataType(), arguments));
        else if (BIT_OR == operator && FIREBIRD == family)
            ctx.visit(function("bin_or", getDataType(), arguments));







        // ~(a & b) & (a | b)
        else if (BIT_XOR == operator && EMULATE_BIT_XOR.contains(family))
            ctx.visit(DSL.bitAnd(
                DSL.bitNot(DSL.bitAnd(lhsAsNumber(), rhsAsNumber())),
                DSL.bitOr(lhsAsNumber(), rhsAsNumber())));








        // Many dialects don't support shifts. Use multiplication/division instead
        else if (SHL == operator && EMULATE_SHR_SHL.contains(family))
            ctx.visit(lhs.mul((Field<? extends Number>) castIfNeeded(DSL.power(two(), rhsAsNumber()), lhs)));

        // [#3962] This emulation is expensive. If this is emulated, BitCount should
        // use division instead of SHR directly
        else if (SHR == operator && EMULATE_SHR_SHL.contains(family))
            ctx.visit(lhs.div((Field<? extends Number>) castIfNeeded(DSL.power(two(), rhsAsNumber()), lhs)));

        // Some dialects support shifts as functions
        else if (SHL == operator && FIREBIRD == family)
            ctx.visit(function("bin_shl", getDataType(), arguments));
        else if (SHR == operator && FIREBIRD == family)
            ctx.visit(function("bin_shr", getDataType(), arguments));

        // These operators are not supported in any dialect
        else if (BIT_NAND == operator)
            ctx.visit(DSL.bitNot(DSL.bitAnd(lhsAsNumber(), rhsAsNumber())));
        else if (BIT_NOR == operator)
            ctx.visit(DSL.bitNot(DSL.bitOr(lhsAsNumber(), rhsAsNumber())));
        else if (BIT_XNOR == operator)
            ctx.visit(DSL.bitNot(DSL.bitXor(lhsAsNumber(), rhsAsNumber())));

        // ---------------------------------------------------------------------
        // XXX: Date time arithmetic operators
        // ---------------------------------------------------------------------

        // [#585] Date time arithmetic for numeric or interval RHS
        else if ((ADD == operator || SUBTRACT == operator) &&
             lhs.getDataType().isDateTime() &&
            (rhs.get(0).getDataType().isNumeric() ||
             rhs.get(0).getDataType().isInterval()))
            ctx.visit(new DateExpression<T>(lhs, operator, rhs.get(0)));

        // ---------------------------------------------------------------------
        // XXX: Other operators
        // ---------------------------------------------------------------------

        // Use the default operator expression for all other cases
        else
            ctx.visit(new DefaultExpression<T>(lhs, operator, rhs));
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
        return (Field<Number>) rhs.get(0);
    }

    // E.g. +2 00:00:00.000000000
    private static final Pattern TRUNC_TO_MICROS = Pattern.compile("([^.]*\\.\\d{0,6})\\d{0,3}");

    /**
     * Return the expression to be rendered when the RHS is an interval type
     */
    private static class DateExpression<T> extends AbstractField<T> {

        /**
         * Generated UID
         */
        private static final long        serialVersionUID = 3160679741902222262L;

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
                result.setInline(true);

            return result;
        }

        @Override
        public final void accept(Context<?> ctx) {
            if (rhs.getDataType().isInterval())
                acceptIntervalExpression(ctx);
            else
                acceptNumberExpression(ctx);
        }

        private final Field<T> getYTSExpression() {
            YearToSecond yts = rhsAsYTS();

            return new DateExpression<T>(
                new DateExpression<T>(lhs, operator, p(yts.getYearToMonth())),
                operator,
                p(yts.getDayToSecond())
            );
        }

        @SuppressWarnings({ "unchecked", "rawtypes" })
        private final void acceptIntervalExpression(Context<?> ctx) {
            SQLDialect family = ctx.family();

            int sign = (operator == ADD) ? 1 : -1;
            switch (family) {




                case CUBRID:
                case MARIADB:
                case MYSQL: {
                    if (rhs.getType() == YearToSecond.class) {
                        ctx.visit(getYTSExpression());
                        break;
                    }

                    Interval interval = rhsAsInterval();

                    if (operator == SUBTRACT)
                        interval = interval.neg();

                    if (rhs.getType() == YearToMonth.class)
                        ctx.visit(F_DATE_ADD).sql('(').visit(lhs).sql(", ").visit(K_INTERVAL).sql(' ')
                            .visit(Tools.field(interval, SQLDataType.VARCHAR)).sql(' ').visit(K_YEAR_MONTH).sql(')');
                    else if (family == CUBRID)
                        ctx.visit(F_DATE_ADD).sql('(').visit(lhs).sql(", ").visit(K_INTERVAL).sql(' ')
                            .visit(Tools.field(interval, SQLDataType.VARCHAR)).sql(' ').visit(K_DAY_MILLISECOND).sql(')');

                    // [#6820] Workaround for bugs:
                    //         https://bugs.mysql.com/bug.php?id=88573
                    //         https://jira.mariadb.org/browse/MDEV-14452
                    else
                        ctx.visit(F_DATE_ADD).sql('(').visit(lhs).sql(", ").visit(K_INTERVAL).sql(' ')
                            .visit(Tools.field(TRUNC_TO_MICROS.matcher("" + interval).replaceAll("$1"), SQLDataType.VARCHAR)).sql(' ').visit(K_DAY_MICROSECOND).sql(')');
                    break;
                }

                case DERBY:
                case HSQLDB: {
                    if (rhs.getType() == YearToSecond.class) {
                        ctx.visit(getYTSExpression());
                        break;
                    }

                    boolean needsCast = getDataType().getType() != Timestamp.class;
                    if (needsCast)
                        ctx.visit(K_CAST).sql('(');

                    if (rhs.getType() == YearToMonth.class)
                        ctx.sql("{fn ").visit(F_TIMESTAMPADD).sql('(').visit(keyword("sql_tsi_month")).sql(", ")
                            .visit(p(sign * rhsAsYTM().intValue())).sql(", ").visit(lhs).sql(") }");
                    else
                        ctx.sql("{fn ").visit(F_TIMESTAMPADD).sql('(').visit(keyword("sql_tsi_second")).sql(", ")
                            .visit(p(sign * (long) rhsAsDTS().getTotalSeconds())).sql(", {fn ")
                            .visit(F_TIMESTAMPADD).sql('(').visit(keyword("sql_tsi_milli_second")).sql(", ")
                            .visit(p(sign * (long) rhsAsDTS().getMilli())).sql(", ").visit(lhs).sql(") }) }");

                    // [#1883] TIMESTAMPADD returns TIMESTAMP columns. If this
                    // is a DATE column, cast it to DATE
                    if (needsCast)
                        ctx.sql(' ').visit(K_AS).sql(' ').visit(keyword(getDataType().getCastTypeName(ctx.configuration()))).sql(')');

                    break;
                }

                case FIREBIRD: {
                    if (rhs.getType() == YearToSecond.class)
                        ctx.visit(getYTSExpression());
                    else if (rhs.getType() == YearToMonth.class)
                        ctx.visit(F_DATEADD).sql('(').visit(K_MONTH).sql(", ").visit(p(sign * rhsAsYTM().intValue())).sql(", ").visit(lhs).sql(')');
                    else
                        ctx.visit(F_DATEADD).sql('(').visit(K_MILLISECOND).sql(", ").visit(p(sign * (long) rhsAsDTS().getTotalMilli())).sql(", ").visit(lhs).sql(')');
                    break;
                }

                case H2: {
                    if (rhs.getType() == YearToSecond.class)
                        ctx.visit(getYTSExpression());
                    else if (rhs.getType() == YearToMonth.class)
                        ctx.visit(F_DATEADD).sql("('month', ").visit(p(sign * rhsAsYTM().intValue())).sql(", ").visit(lhs).sql(')');
                    else
                        ctx.visit(F_DATEADD).sql("('ms', ").visit(p(sign * (long) rhsAsDTS().getTotalMilli())).sql(", ").visit(lhs).sql(')');
                    break;
                }

                case SQLITE: {
                    if (rhs.getType() == YearToSecond.class) {
                        ctx.visit(getYTSExpression());
                        break;
                    }

                    boolean ytm = rhs.getType() == YearToMonth.class;
                    Field<?> interval = p(ytm ? rhsAsYTM().intValue() : rhsAsDTS().getTotalSeconds());

                    if (sign < 0)
                        interval = interval.neg();

                    interval = interval.concat(inline(ytm ? " months" : " seconds"));
                    ctx.visit(F_STRFTIME).sql("('%Y-%m-%d %H:%M:%f', ").visit(lhs).sql(", ").visit(interval).sql(')');
                    break;
                }




























































































































































                case POSTGRES:
                default:
                    ctx.visit(new DefaultExpression<T>(lhs, operator, new QueryPartList<Field<?>>(Arrays.asList(rhs))));
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
                        ctx.visit(F_DATEADD).sql('(').visit(K_DAY).sql(", ").visit(rhsAsNumber()).sql(", ").visit(lhs).sql(')');
                    else
                        ctx.visit(F_DATEADD).sql('(').visit(K_DAY).sql(", ").visit(rhsAsNumber().neg()).sql(", ").visit(lhs).sql(')');
                    break;
                }




                case HSQLDB: {
                    if (operator == ADD)
                        ctx.visit(lhs.add(DSL.field("{0} day", rhsAsNumber())));
                    else
                        ctx.visit(lhs.sub(DSL.field("{0} day", rhsAsNumber())));
                    break;
                }

                case DERBY: {
                    boolean needsCast = getDataType().getType() != Timestamp.class;
                    if (needsCast)
                        ctx.visit(K_CAST).sql('(');

                    if (operator == ADD)
                        ctx.sql("{fn ").visit(F_TIMESTAMPADD).sql('(').visit(keyword("sql_tsi_day")).sql(", ").visit(rhsAsNumber()).sql(", ").visit(lhs).sql(") }");
                    else
                        ctx.sql("{fn ").visit(F_TIMESTAMPADD).sql('(').visit(keyword("sql_tsi_day")).sql(", ").visit(rhsAsNumber().neg()).sql(", ").visit(lhs).sql(") }");

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
                        ctx.visit(F_DATE_ADD).sql('(').visit(lhs).sql(", ").visit(K_INTERVAL).sql(' ').visit(rhsAsNumber()).sql(' ').visit(K_DAY).sql(')');
                    else
                        ctx.visit(F_DATE_ADD).sql('(').visit(lhs).sql(", ").visit(K_INTERVAL).sql(' ').visit(rhsAsNumber().neg()).sql(' ').visit(K_DAY).sql(')');
                    break;
                }






















                case POSTGRES: {

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
                        ctx.visit(F_STRFTIME).sql("('%Y-%m-%d %H:%M:%f', ").visit(lhs).sql(", ").visit(rhsAsNumber().concat(inline(" day"))).sql(')');
                    else
                        ctx.visit(F_STRFTIME).sql("('%Y-%m-%d %H:%M:%f', ").visit(lhs).sql(", ").visit(rhsAsNumber().neg().concat(inline(" day"))).sql(')');
                    break;












                case H2:
                default:
                    ctx.visit(new DefaultExpression<T>(lhs, operator, new QueryPartList<Field<?>>(Arrays.asList(rhs))));
                    break;
            }
        }

        @SuppressWarnings("unchecked")
        private final YearToSecond rhsAsYTS() {
            try {
                return ((Param<YearToSecond>) rhs).getValue();
            }
            catch (ClassCastException e) {
                throw new DataTypeException("Cannot perform datetime arithmetic with a non-numeric, non-interval data type on the right hand side of the expression: " + rhs);
            }
        }

        @SuppressWarnings("unchecked")
        private final YearToMonth rhsAsYTM() {
            try {
                return ((Param<YearToMonth>) rhs).getValue();
            }
            catch (ClassCastException e) {
                throw new DataTypeException("Cannot perform datetime arithmetic with a non-numeric, non-interval data type on the right hand side of the expression: " + rhs);
            }
        }

        @SuppressWarnings("unchecked")
        private final DayToSecond rhsAsDTS() {
            try {
                return ((Param<DayToSecond>) rhs).getValue();
            }
            catch (ClassCastException e) {
                throw new DataTypeException("Cannot perform datetime arithmetic with a non-numeric, non-interval data type on the right hand side of the expression: " + rhs);
            }
        }

        @SuppressWarnings("unchecked")
        private final Interval rhsAsInterval() {
            try {
                return ((Param<Interval>) rhs).getValue();
            }
            catch (ClassCastException e) {
                throw new DataTypeException("Cannot perform datetime arithmetic with a non-numeric, non-interval data type on the right hand side of the expression: " + rhs);
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

    private static class DefaultExpression<T> extends AbstractField<T> {

        /**
         * Generated UID
         */
        private static final long             serialVersionUID = -5105004317793995419L;

        private final Field<T>                lhs;
        private final ExpressionOperator      operator;
        private final QueryPartList<Field<?>> rhs;

        DefaultExpression(Field<T> lhs, ExpressionOperator operator, QueryPartList<Field<?>> rhs) {
            super(operator.toName(), lhs.getDataType());

            this.lhs = lhs;
            this.operator = operator;
            this.rhs = rhs;
        }

        @Override
        public final void accept(Context<?> ctx) {
            String op = operator.toSQL();

            if (operator == BIT_XOR && HASH_OP_FOR_BIT_XOR.contains(ctx.family()))
                op = "#";

            ctx.sql('(');
            ctx.visit(lhs);

            for (Field<?> field : rhs)
                ctx.sql(' ')
                   .sql(op)
                   .sql(' ')
                   .visit(field);

            ctx.sql(')');
        }
    }
}
