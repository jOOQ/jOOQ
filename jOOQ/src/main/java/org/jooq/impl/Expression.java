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

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.regex.Pattern;

import org.jooq.Configuration;
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

final class Expression<T> extends AbstractFunction<T> {

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
    private final ExpressionOperator         operator;

    Expression(ExpressionOperator operator, Field<T> lhs, Field<?>... rhs) {
        super(operator.toSQL(), lhs.getDataType(), Tools.combine(lhs, rhs));

        this.operator = operator;
        this.lhs = lhs;
        this.rhs = new QueryPartList<Field<?>>(rhs);
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
    final Field<T> getFunction0(Configuration configuration) {
        SQLDialect family = configuration.family();

        // ---------------------------------------------------------------------
        // XXX: Bitwise operators
        // ---------------------------------------------------------------------

        // DB2, H2 and HSQLDB know functions, instead of operators
        if (BIT_AND == operator && SUPPORT_BIT_AND.contains(family))
            return function("bitand", getDataType(), getArguments());
        else if (BIT_AND == operator && FIREBIRD == family)
            return function("bin_and", getDataType(), getArguments());
        else if (BIT_XOR == operator && SUPPORT_BIT_OR_XOR.contains(family))
            return function("bitxor", getDataType(), getArguments());
        else if (BIT_XOR == operator && FIREBIRD == family)
            return function("bin_xor", getDataType(), getArguments());
        else if (BIT_OR == operator && SUPPORT_BIT_OR_XOR.contains(family))
            return function("bitor", getDataType(), getArguments());
        else if (BIT_OR == operator && FIREBIRD == family)
            return function("bin_or", getDataType(), getArguments());







        // ~(a & b) & (a | b)
        else if (BIT_XOR == operator && EMULATE_BIT_XOR.contains(family))
            return (Field<T>) DSL.bitAnd(
                DSL.bitNot(DSL.bitAnd(lhsAsNumber(), rhsAsNumber())),
                DSL.bitOr(lhsAsNumber(), rhsAsNumber()));








        // Many dialects don't support shifts. Use multiplication/division instead
        else if (SHL == operator && EMULATE_SHR_SHL.contains(family))
            return lhs.mul((Field<? extends Number>) DSL.power(two(), rhsAsNumber()).cast(lhs));

        // [#3962] This emulation is expensive. If this is emulated, BitCount should
        // use division instead of SHR directly
        else if (SHR == operator && EMULATE_SHR_SHL.contains(family))
            return lhs.div((Field<? extends Number>) DSL.power(two(), rhsAsNumber()).cast(lhs));

        // Some dialects support shifts as functions
        else if (SHL == operator && FIREBIRD == family)
            return function("bin_shl", getDataType(), getArguments());
        else if (SHR == operator && FIREBIRD == family)
            return function("bin_shr", getDataType(), getArguments());

        // These operators are not supported in any dialect
        else if (BIT_NAND == operator)
            return (Field<T>) DSL.bitNot(DSL.bitAnd(lhsAsNumber(), rhsAsNumber()));
        else if (BIT_NOR == operator)
            return (Field<T>) DSL.bitNot(DSL.bitOr(lhsAsNumber(), rhsAsNumber()));
        else if (BIT_XNOR == operator)
            return (Field<T>) DSL.bitNot(DSL.bitXor(lhsAsNumber(), rhsAsNumber()));

        // ---------------------------------------------------------------------
        // XXX: Date time arithmetic operators
        // ---------------------------------------------------------------------

        // [#585] Date time arithmetic for numeric or interval RHS
        else if ((ADD == operator || SUBTRACT == operator) &&
             lhs.getDataType().isDateTime() &&
            (rhs.get(0).getDataType().isNumeric() ||
             rhs.get(0).getDataType().isInterval()))
            return new DateExpression<T>(lhs, operator, rhs.get(0));

        // ---------------------------------------------------------------------
        // XXX: Other operators
        // ---------------------------------------------------------------------

        // Use the default operator expression for all other cases
        else
            return new DefaultExpression<T>(lhs, operator, rhs);
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
    private static class DateExpression<T> extends AbstractFunction<T> {

        /**
         * Generated UID
         */
        private static final long        serialVersionUID = 3160679741902222262L;

        private final Field<T>           lhs;
        private final ExpressionOperator operator;
        private final Field<?>           rhs;

        DateExpression(Field<T> lhs, ExpressionOperator operator, Field<?> rhs) {
            super(operator.toSQL(), lhs.getDataType());

            this.lhs = lhs;
            this.operator = operator;
            this.rhs = rhs;
        }

        @Override
        final Field<T> getFunction0(Configuration configuration) {
            if (rhs.getDataType().isInterval())
                return getIntervalExpression(configuration);
            else
                return getNumberExpression(configuration);
        }

        private final Field<T> getYTSExpression(Configuration configuration) {
            YearToSecond yts = rhsAsYTS();

            return new DateExpression<T>(
                new DateExpression<T>(lhs, operator, val(yts.getYearToMonth())),
                operator,
                val(yts.getDayToSecond())
            );
        }

        @SuppressWarnings({ "unchecked", "rawtypes" })
        private final Field<T> getIntervalExpression(Configuration configuration) {
            SQLDialect dialect = configuration.dialect();
            SQLDialect family = dialect.family();

            int sign = (operator == ADD) ? 1 : -1;
            switch (family) {



                case CUBRID:
                case MARIADB:
                case MYSQL: {
                    if (rhs.getType() == YearToSecond.class)
                        return getYTSExpression(configuration);

                    Interval interval = rhsAsInterval();

                    if (operator == SUBTRACT)
                        interval = interval.neg();

                    if (rhs.getType() == YearToMonth.class)
                        return DSL.field("{date_add}({0}, {interval} {1} {year_month})", getDataType(), lhs, Tools.field(interval, SQLDataType.VARCHAR));
                    else if (dialect == CUBRID)
                        return DSL.field("{date_add}({0}, {interval} {1} {day_millisecond})", getDataType(), lhs, Tools.field(interval, SQLDataType.VARCHAR));

                    // [#6820] Workaround for bugs:
                    //         https://bugs.mysql.com/bug.php?id=88573
                    //         https://jira.mariadb.org/browse/MDEV-14452
                    else
                        return DSL.field("{date_add}({0}, {interval} {1} {day_microsecond})", getDataType(), lhs,
                            Tools.field(TRUNC_TO_MICROS.matcher("" + interval).replaceAll("$1"), SQLDataType.VARCHAR)
                        );
                }

                case DERBY:
                case HSQLDB: {
                    if (rhs.getType() == YearToSecond.class)
                        return getYTSExpression(configuration);

                    Field<T> result;

                    if (rhs.getType() == YearToMonth.class)
                        result = DSL.field("{fn {timestampadd}({sql_tsi_month}, {0}, {1}) }",
                            getDataType(), val(sign * rhsAsYTM().intValue()), lhs);
                    else
                        result = DSL.field("{fn {timestampadd}({sql_tsi_second}, {0}, {fn {timestampadd}({sql_tsi_milli_second}, {1}, {2}) }) }",
                            getDataType(),
                            val(sign * (long) rhsAsDTS().getTotalSeconds()),
                            val(sign * (long) rhsAsDTS().getMilli()),
                            lhs);

                    // [#1883] TIMESTAMPADD returns TIMESTAMP columns. If this
                    // is a DATE column, cast it to DATE
                    return castNonTimestamps(configuration, result);
                }

                case FIREBIRD: {
                    if (rhs.getType() == YearToSecond.class)
                        return getYTSExpression(configuration);
                    else if (rhs.getType() == YearToMonth.class)
                        return DSL.field("{dateadd}({month}, {0}, {1})", getDataType(), val(sign * rhsAsYTM().intValue()), lhs);
                    else
                        return DSL.field("{dateadd}({millisecond}, {0}, {1})", getDataType(), val(sign * (long) rhsAsDTS().getTotalMilli()), lhs);
                }

                case H2: {
                    if (rhs.getType() == YearToSecond.class)
                        return getYTSExpression(configuration);
                    else if (rhs.getType() == YearToMonth.class)
                        return DSL.field("{dateadd}('month', {0}, {1})", getDataType(), val(sign * rhsAsYTM().intValue()), lhs);
                    else
                        return DSL.field("{dateadd}('ms', {0}, {1})", getDataType(), val(sign * (long) rhsAsDTS().getTotalMilli()), lhs);
                }

                case SQLITE: {
                    if (rhs.getType() == YearToSecond.class)
                        return getYTSExpression(configuration);

                    boolean ytm = rhs.getType() == YearToMonth.class;
                    Field<?> interval = val(ytm ? rhsAsYTM().intValue() : rhsAsDTS().getTotalSeconds());

                    if (sign < 0)
                        interval = interval.neg();

                    interval = interval.concat(inline(ytm ? " months" : " seconds"));
                    return DSL.field("{datetime}({0}, {1})", getDataType(), lhs, interval);
                }





















































































































































                case POSTGRES:
                default:
                    return new DefaultExpression<T>(lhs, operator, new QueryPartList<Field<?>>(Arrays.asList(rhs)));
            }
        }

        /**
         * Cast a field to its actual type if it is not a <code>TIMESTAMP</code>
         * field
         */
        private final Field<T> castNonTimestamps(Configuration configuration, Field<T> result) {
            if (getDataType().getType() != Timestamp.class)
                return DSL.field("{cast}({0} {as} " + getDataType().getCastTypeName(configuration) + ")", getDataType(), result);

            return result;
        }

        /**
         * Return the expression to be rendered when the RHS is a number type
         */
        @SuppressWarnings({ "unchecked", "rawtypes" })
        private final Field<T> getNumberExpression(Configuration configuration) {
            switch (configuration.family()) {




















                case FIREBIRD: {
                    if (operator == ADD)
                        return DSL.field("{dateadd}(day, {0}, {1})", getDataType(), rhsAsNumber(), lhs);
                    else
                        return DSL.field("{dateadd}(day, {0}, {1})", getDataType(), rhsAsNumber().neg(), lhs);
                }




                case HSQLDB: {
                    if (operator == ADD)
                        return lhs.add(DSL.field("{0} day", rhsAsNumber()));
                    else
                        return lhs.sub(DSL.field("{0} day", rhsAsNumber()));
                }

                case DERBY: {
                    Field<T> result;

                    if (operator == ADD)
                        result = DSL.field("{fn {timestampadd}({sql_tsi_day}, {0}, {1}) }", getDataType(), rhsAsNumber(), lhs);
                    else
                        result = DSL.field("{fn {timestampadd}({sql_tsi_day}, {0}, {1}) }", getDataType(), rhsAsNumber().neg(), lhs);

                    // [#1883] TIMESTAMPADD returns TIMESTAMP columns. If this
                    // is a DATE column, cast it to DATE
                    return castNonTimestamps(configuration, result);
                }




                case CUBRID:
                case MARIADB:
                case MYSQL: {
                    if (operator == ADD)
                        return DSL.field("{date_add}({0}, {interval} {1} {day})", getDataType(), lhs, rhsAsNumber());
                    else
                        return DSL.field("{date_add}({0}, {interval} {1} {day})", getDataType(), lhs, rhsAsNumber().neg());
                }




















                case POSTGRES: {

                    // This seems to be the most reliable way to avoid issues
                    // with incompatible data types and timezones
                    // ? + CAST (? || ' days' as interval)
                    if (operator == ADD)
                        return new DateAdd(lhs, rhsAsNumber(), DatePart.DAY);
                    else
                        return new DateAdd(lhs, rhsAsNumber().neg(), DatePart.DAY);
                }

                case SQLITE:
                    if (operator == ADD)
                        return DSL.field("{datetime}({0}, {1})", getDataType(), lhs, rhsAsNumber().concat(inline(" day")));
                    else
                        return DSL.field("{datetime}({0}, {1})", getDataType(), lhs, rhsAsNumber().neg().concat(inline(" day")));











                case H2:
                default:
                    return new DefaultExpression<T>(lhs, operator, new QueryPartList<Field<?>>(Arrays.asList(rhs)));
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
