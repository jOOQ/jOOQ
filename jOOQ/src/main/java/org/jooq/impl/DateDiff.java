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
import static org.jooq.DatePart.HOUR;
import static org.jooq.DatePart.MICROSECOND;
import static org.jooq.DatePart.MILLISECOND;
import static org.jooq.DatePart.QUARTER;
import static org.jooq.DatePart.YEAR;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.impl.DSL.epoch;
import static org.jooq.impl.DSL.function;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.keyword;
import static org.jooq.impl.DSL.month;
import static org.jooq.impl.DSL.quarter;
import static org.jooq.impl.DSL.year;
import static org.jooq.impl.Names.N_DATEDIFF;
import static org.jooq.impl.Names.N_DAYS_BETWEEN;
import static org.jooq.impl.Names.N_STRFTIME;
import static org.jooq.impl.Names.N_TIMESTAMPDIFF;
import static org.jooq.impl.SQLDataType.TIMESTAMP;
import static org.jooq.impl.Tools.castIfNeeded;

import org.jooq.Context;
import org.jooq.DatePart;
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
final class DateDiff<T> extends AbstractField<Integer> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -4813228000332771961L;

    private final DatePart    part;
    private final Field<T>    date1;
    private final Field<T>    date2;

    DateDiff(DatePart part, Field<T> date1, Field<T> date2) {
        super(N_DATEDIFF, SQLDataType.INTEGER);

        this.part = part;
        this.date1 = date1;
        this.date2 = date2;
    }

    @Override
    public final void accept(Context<?> ctx) {
        DatePart p = part == null ? DAY : part;

        switch (ctx.family()) {




            case MARIADB:
            case MYSQL:
                ctx.visit(N_DATEDIFF).sql('(').visit(date1).sql(", ").visit(date2).sql(')');
                return;

            case DERBY:
                ctx.sql("{fn ").visit(N_TIMESTAMPDIFF).sql('(').visit(keyword("sql_tsi_day")).sql(", ").visit(date2).sql(", ").visit(date1).sql(") }");
                return;









            case FIREBIRD:
            case H2:
            case HSQLDB:
                switch (p) {
                    case MILLENNIUM:
                    case CENTURY:
                    case DECADE:
                        ctx.visit(DSL.extract(date1, p).sub(DSL.extract(date2, p)));
                        return;

                    case QUARTER:
                        if (ctx.family() == FIREBIRD) {
                            ctx.visit(new DateDiff<>(YEAR, date1, date2).times(inline(4)).plus(quarter(date1).minus(quarter(date2))));
                            return;
                        }

                    case HOUR:
                    case MINUTE:
                    case SECOND:
                    case MILLISECOND:
                    case MICROSECOND:
                    case NANOSECOND:
                        if (ctx.family() == HSQLDB) {
                            ctx.visit(N_DATEDIFF).sql('(').visit(p.toKeyword()).sql(", ").visit(date2.cast(TIMESTAMP)).sql(", ").visit(date1.cast(TIMESTAMP)).sql(')');
                            return;
                        }
                }

                ctx.visit(N_DATEDIFF).sql('(').visit(p.toKeyword()).sql(", ").visit(date2).sql(", ").visit(date1).sql(')');
                return;







            case SQLITE:
                ctx.sql('(').visit(N_STRFTIME).sql("('%s', ").visit(date1).sql(") - ").visit(N_STRFTIME).sql("('%s', ").visit(date2).sql(")) / 86400");
                break;





            case POSTGRES:
                switch (p) {
                    case DAY:
                        // [#4481] Parentheses are important in case this expression is
                        //         placed in the context of other arithmetic
                        ctx.sql('(').visit(date1).sql(" - ").visit(date2).sql(')');
                        return;

                    case MILLENNIUM:
                    case CENTURY:
                    case DECADE:
                    case YEAR:
                        ctx.visit(DSL.extract(date1, p).minus(DSL.extract(date2, p)));
                        return;

                    case QUARTER:
                    case MONTH:
                        ctx.visit(year(date1).minus(year(date2)).times(p == QUARTER ? inline(4) : inline(12))
                            .plus(p == QUARTER ? quarter(date1).minus(quarter(date2)) : month(date1).minus(month(date2))));
                        return;

                    case HOUR:
                    case MINUTE:
                        ctx.visit(epoch(date1).minus(epoch(date2)).div(p == HOUR ? inline(3600) : inline(60)));
                        return;

                    case SECOND:
                        ctx.visit(epoch(date1).minus(epoch(date2)));
                        return;

                    case MILLISECOND:
                    case MICROSECOND:
                    case NANOSECOND:
                        ctx.visit(epoch(date1).minus(epoch(date2)).times(p == MILLISECOND ? inline(1000) : p == MICROSECOND ? inline(1000000) : inline(1000000000)));
                        return;
                }

                break;




            case CUBRID:

                // [#4481] Parentheses are important in case this expression is
                //         placed in the context of other arithmetic
                ctx.sql('(').visit(date1).sql(" - ").visit(date2).sql(')');
                return;















        }

        ctx.visit(castIfNeeded(date1.sub(date2), Integer.class));
    }
}
