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

import static org.jooq.DatePart.DAY;
import static org.jooq.DatePart.EPOCH;
import static org.jooq.DatePart.HOUR;
import static org.jooq.DatePart.MICROSECOND;
import static org.jooq.DatePart.MILLISECOND;
import static org.jooq.DatePart.NANOSECOND;
import static org.jooq.DatePart.QUARTER;
import static org.jooq.DatePart.YEAR;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.impl.DSL.function;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.Keywords.K_DAY;
import static org.jooq.impl.Keywords.K_FROM;
import static org.jooq.impl.Keywords.K_MILLISECOND;
import static org.jooq.impl.Names.N_DATEDIFF;
import static org.jooq.impl.Names.N_DATE_DIFF;
import static org.jooq.impl.Names.N_DAYS;
import static org.jooq.impl.Names.N_DAYS_BETWEEN;
import static org.jooq.impl.Names.N_EXTRACT;
import static org.jooq.impl.Names.N_SQL_TSI_DAY;
import static org.jooq.impl.Names.N_SQL_TSI_FRAC_SECOND;
import static org.jooq.impl.Names.N_SQL_TSI_HOUR;
import static org.jooq.impl.Names.N_SQL_TSI_MINUTE;
import static org.jooq.impl.Names.N_SQL_TSI_SECOND;
import static org.jooq.impl.Names.N_STRFTIME;
import static org.jooq.impl.Names.N_TIMESTAMPDIFF;
import static org.jooq.impl.Names.N_TIMESTAMP_DIFF;
import static org.jooq.impl.SQLDataType.TIMESTAMP;
import static org.jooq.impl.Tools.castIfNeeded;

import java.sql.Date;
import java.sql.Timestamp;

import org.jooq.Context;
import org.jooq.DatePart;
import org.jooq.Field;
import org.jooq.Name;

/**
 * @author Lukas Eder
 */
final class DateDiff<T> extends AbstractField<Integer> {

    private final DatePart    part;
    private final Field<T>    startDate;
    private final Field<T>    endDate;

    DateDiff(DatePart part, Field<T> startDate, Field<T> endDate) {
        super(N_DATEDIFF, SQLDataType.INTEGER);

        this.part = part;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public final void accept(Context<?> ctx) {
        DatePart p = part == null ? DAY : part;

        switch (ctx.family()) {


            case MARIADB:
            case MYSQL:
                switch (p) {
                    case MILLENNIUM:
                    case CENTURY:
                    case DECADE:
                    case YEAR:
                        ctx.visit(partDiff(p));
                        return;

                    case QUARTER:
                    case MONTH:
                        ctx.visit(monthDiff(p));
                        return;

                    case DAY:
                        ctx.visit(N_DATEDIFF).sql('(').visit(endDate).sql(", ").visit(startDate).sql(')');
                        return;

                    case MILLISECOND:
                        ctx.visit(new DateDiff<>(MICROSECOND, startDate, endDate).div(inline(1000)));
                        return;

                    case NANOSECOND:
                        ctx.visit(new DateDiff<>(MICROSECOND, startDate, endDate).times(inline(1000)));
                        return;
                }

                ctx.visit(N_TIMESTAMPDIFF).sql('(').visit(p.toName()).sql(", ").visit(startDate).sql(", ").visit(endDate).sql(')');
                return;

            case DERBY: {
                Name name = N_SQL_TSI_DAY;

                switch (p) {
                    case MILLENNIUM:
                    case CENTURY:
                    case DECADE:
                    case YEAR:
                        ctx.visit(partDiff(p));
                        return;

                    case QUARTER:
                    case MONTH:
                        ctx.visit(monthDiff(p));
                        return;

                    case DAY:        name = N_SQL_TSI_DAY;         break;
                    case HOUR:       name = N_SQL_TSI_HOUR;        break;
                    case MINUTE:     name = N_SQL_TSI_MINUTE;      break;
                    case SECOND:     name = N_SQL_TSI_SECOND;      break;
                    case NANOSECOND: name = N_SQL_TSI_FRAC_SECOND; break;

                    case MILLISECOND:
                        ctx.visit(new DateDiff<>(NANOSECOND, startDate, endDate).div(inline(1000000L)));
                        return;

                    case MICROSECOND:
                        ctx.visit(new DateDiff<>(NANOSECOND, startDate, endDate).div(inline(1000L)));
                        return;
                }

                ctx.sql("{fn ").visit(N_TIMESTAMPDIFF).sql('(').visit(name).sql(", ").visit(startDate).sql(", ").visit(endDate).sql(") }");
                return;
            }







            case FIREBIRD:
            case H2:
            case HSQLDB:
                switch (p) {
                    case MILLENNIUM:
                    case CENTURY:
                    case DECADE:
                        ctx.visit(partDiff(p));
                        return;

                    case QUARTER:
                        if (ctx.family() == FIREBIRD) {
                            ctx.visit(monthDiff(QUARTER));
                            return;
                        }

                        break;

                    case HOUR:
                    case MINUTE:
                    case SECOND:
                    case MILLISECOND:
                    case MICROSECOND:
                    case NANOSECOND:
                        if (ctx.family() == HSQLDB) {
                            ctx.visit(N_DATEDIFF).sql('(').visit(p.toKeyword()).sql(", ").visit(startDate.cast(TIMESTAMP)).sql(", ").visit(endDate.cast(TIMESTAMP)).sql(')');
                            return;
                        }

                        break;
                }

                ctx.visit(N_DATEDIFF).sql('(').visit(p.toKeyword()).sql(", ").visit(startDate).sql(", ").visit(endDate).sql(')');
                return;















            case SQLITE:
                ctx.sql('(').visit(N_STRFTIME).sql("('%s', ").visit(endDate).sql(") - ").visit(N_STRFTIME).sql("('%s', ").visit(startDate).sql(")) / 86400");
                return;






            case CUBRID:
            case POSTGRES:
                switch (p) {
                    case MILLENNIUM:
                    case CENTURY:
                    case DECADE:
                    case YEAR:
                        ctx.visit(partDiff(p));
                        return;

                    case QUARTER:
                    case MONTH:
                        ctx.visit(monthDiff(p));
                        return;

                    case DAY:
                        switch (ctx.family()) {












                            case POSTGRES:
                                if (endDate.getDataType().isDate() && startDate.getDataType().isDate())
                                    ctx.sql('(').visit(endDate).sql(" - ").visit(startDate).sql(')');
                                else
                                    ctx.visit(N_EXTRACT).sql('(').visit(K_DAY).sql(' ').visit(K_FROM).sql(' ').visit(endDate).sql(" - ").visit(startDate).sql(')');

                                return;

                            default:

                                // [#4481] Parentheses are important in case this expression is
                                //         placed in the context of other arithmetic
                                ctx.sql('(').visit(endDate).sql(" - ").visit(startDate).sql(')');
                                return;
                        }

                    case HOUR:
                    case MINUTE:
                        ctx.visit(partDiff(EPOCH).div(p == HOUR ? inline(3600) : inline(60)));
                        return;

                    case SECOND:
                        ctx.visit(partDiff(EPOCH));
                        return;

                    case MILLISECOND:
                    case MICROSECOND:
                    case NANOSECOND:
                        ctx.visit(partDiff(EPOCH).times(p == MILLISECOND ? inline(1000) : p == MICROSECOND ? inline(1000000) : inline(1000000000)));
                        return;
                }

                break;







        }

        ctx.visit(castIfNeeded(endDate.minus(startDate), Integer.class));
    }

    private final Field<Integer> partDiff(DatePart p) {
        return DSL.extract(endDate, p).minus(DSL.extract(startDate, p));
    }

    /**
     * Calculate the difference for {@link DatePart#MONTH} or month-based date
     * parts like {@link DatePart#QUARTER}.
     */
    private final Field<Integer> monthDiff(DatePart p) {
        return partDiff(YEAR).times(p == QUARTER ? inline(4) : inline(12)).plus(partDiff(p));
    }
}
