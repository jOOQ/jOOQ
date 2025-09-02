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
 * The <code>DATE ADD</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class DateAdd<T>
extends
    AbstractField<T>
implements
    QOM.DateAdd<T>
{

    final Field<T>                date;
    final Field<? extends Number> interval;
    final DatePart                datePart;

    DateAdd(
        Field<T> date,
        Field<? extends Number> interval
    ) {
        super(
            N_DATE_ADD,
            allNotNull((DataType) dataType(date), date, interval)
        );

        this.date = nullSafeNotNull(date, (DataType) OTHER);
        this.interval = nullSafeNotNull(interval, INTEGER);
        this.datePart = null;
    }

    DateAdd(
        Field<T> date,
        Field<? extends Number> interval,
        DatePart datePart
    ) {
        super(
            N_DATE_ADD,
            allNotNull((DataType) dataType(date), date, interval)
        );

        this.date = nullSafeNotNull(date, (DataType) OTHER);
        this.interval = nullSafeNotNull(interval, INTEGER);
        this.datePart = datePart;
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    @Override
    public final void accept(Context<?> ctx) {
        if (datePart == null)
            ctx.visit(date.add(interval));
        else
            accept0(ctx);
    }

    private final void accept0(Context<?> ctx) {
        Keyword keyword = null;
        Name    name    = null;
        String  string  = null;

        switch (ctx.family()) {



            case CUBRID:
            case MARIADB:
            case MYSQL: {
                ctx.visit(N_DATE_ADD).sql('(').visit(date).sql(", ").visit(K_INTERVAL).sql(' ').visit(interval).sql(' ').visit(standardKeyword()).sql(')');
                break;
            }

            case DUCKDB: {
                if (getDataType().isDate())
                    ctx.visit(K_CAST).sql('(');

                ctx.visit(N_DATE_ADD).sql('(').visit(date).sql(", ").visit(K_INTERVAL).sql(' ').visit(inline(1)).sql(' ').visit(standardKeyword()).sql(" * ").visit(interval).sql(')');

                if (getDataType().isDate())
                    ctx.sql(' ').visit(K_AS).sql(' ').visit(K_DATE).sql(')');

                break;
            }

            case DERBY:
            case HSQLDB: {
                switch (datePart) {
                    case YEAR:   keyword = DSL.keyword("sql_tsi_year");   break;
                    case MONTH:  keyword = DSL.keyword("sql_tsi_month");  break;
                    case DAY:    keyword = DSL.keyword("sql_tsi_day");    break;
                    case HOUR:   keyword = DSL.keyword("sql_tsi_hour");   break;
                    case MINUTE: keyword = DSL.keyword("sql_tsi_minute"); break;
                    case SECOND: keyword = DSL.keyword("sql_tsi_second"); break;
                    default: throw unsupported();
                }

                ctx.sql("{fn ").visit(N_TIMESTAMPADD).sql('(').visit(keyword).sql(", ").visit(interval).sql(", ").visit(date).sql(") }");
                break;
            }

            case H2: {
                switch (datePart) {
                    case YEAR:   string = "year";   break;
                    case MONTH:  string = "month";  break;
                    case DAY:    string = "day";    break;
                    case HOUR:   string = "hour";   break;
                    case MINUTE: string = "minute"; break;
                    case SECOND: string = "second"; break;
                    default: throw unsupported();
                }

                ctx.visit(N_DATEADD).sql('(').visit(inline(string)).sql(", ").visit(interval).sql(", ").visit(date).sql(')');
                break;
            }






























            case POSTGRES:
            case YUGABYTEDB: {
                switch (datePart) {
                    case YEAR:   string = "1 year";   break;
                    case MONTH:  string = "1 month";  break;
                    case DAY:    string = "1 day";    break;
                    case HOUR:   string = "1 hour";   break;
                    case MINUTE: string = "1 minute"; break;
                    case SECOND: string = "1 second"; break;
                    default: throw unsupported();
                }

                // [#10258] [#11954]
                if (((AbstractField<?>) interval).getExpressionDataType().isInterval())
                    ctx.sql('(').visit(date).sql(" + ").visit(interval).sql(')');

                else if (getDataType().isDate())

                    // [#10258] Special case for DATE + INTEGER arithmetic
                    if (datePart == DatePart.DAY)
                        ctx.sql('(').visit(date).sql(" + ").visit(interval).sql(')');

                    // [#3824] Ensure that the output for DATE arithmetic will also be of type DATE, not TIMESTAMP
                    else
                        ctx.sql("cast((").visit(date).sql(" + ").visit(interval).sql(" * ").visit(K_INTERVAL).sql(' ').visit(inline(string)).sql(") as date)");

                else
                    ctx.sql('(').visit(date).sql(" + ").visit(interval).sql(" * ").visit(K_INTERVAL).sql(' ').visit(inline(string)).sql(")");

                break;
            }

            case SQLITE: {
                switch (datePart) {
                    case YEAR:   string = " year";   break;
                    case MONTH:  string = " month";  break;
                    case DAY:    string = " day";    break;
                    case HOUR:   string = " hour";   break;
                    case MINUTE: string = " minute"; break;
                    case SECOND: string = " second"; break;
                    default: throw unsupported();
                }

                ctx.visit(N_STRFTIME).sql("('%Y-%m-%d %H:%M:%f', ").visit(date).sql(", ").visit(interval.concat(inline(string))).sql(')');
                break;
            }

            case TRINO: {
                ctx.visit(N_DATE_ADD).sql('(').visit(inline(datePart.toString().toLowerCase())).sql(", ").visit(interval).sql(", ").visit(date).sql(')');
                break;
            }






































































































































            default: {
                ctx.visit(N_DATEADD).sql('(').visit(standardKeyword()).sql(", ").visit(interval).sql(", ").visit(date).sql(')');
                break;
            }
        }
    }

    private final Keyword standardKeyword() {
        switch (datePart) {
            case YEAR:
            case MONTH:
            case DAY:
            case HOUR:
            case MINUTE:
            case SECOND:
                return datePart.toKeyword();

            default:
                throw unsupported();
        }
    }

    private final UnsupportedOperationException unsupported() {
        return new UnsupportedOperationException("Unknown date part : " + datePart);
    }

















    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<T> $arg1() {
        return date;
    }

    @Override
    public final Field<? extends Number> $arg2() {
        return interval;
    }

    @Override
    public final DatePart $arg3() {
        return datePart;
    }

    @Override
    public final QOM.DateAdd<T> $arg1(Field<T> newValue) {
        return $constructor().apply(newValue, $arg2(), $arg3());
    }

    @Override
    public final QOM.DateAdd<T> $arg2(Field<? extends Number> newValue) {
        return $constructor().apply($arg1(), newValue, $arg3());
    }

    @Override
    public final QOM.DateAdd<T> $arg3(DatePart newValue) {
        return $constructor().apply($arg1(), $arg2(), newValue);
    }

    @Override
    public final Function3<? super Field<T>, ? super Field<? extends Number>, ? super DatePart, ? extends QOM.DateAdd<T>> $constructor() {
        return (a1, a2, a3) -> new DateAdd<>(a1, a2, a3);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.DateAdd<?> o) {
            return
                Objects.equals($date(), o.$date()) &&
                Objects.equals($interval(), o.$interval()) &&
                Objects.equals($datePart(), o.$datePart())
            ;
        }
        else
            return super.equals(that);
    }
}
