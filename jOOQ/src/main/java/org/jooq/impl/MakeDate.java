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

import java.sql.Date;


/**
 * The <code>MAKE DATE</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class MakeDate
extends
    AbstractField<Date>
implements
    QOM.MakeDate
{

    final Field<? extends Number> year;
    final Field<? extends Number> month;
    final Field<? extends Number> day;

    MakeDate(
        Field<? extends Number> year,
        Field<? extends Number> month,
        Field<? extends Number> day
    ) {
        super(
            N_MAKE_DATE,
            allNotNull(DATE, year, month, day)
        );

        this.year = nullSafeNotNull(year, INTEGER);
        this.month = nullSafeNotNull(month, INTEGER);
        this.day = nullSafeNotNull(day, INTEGER);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

    @Override
    final boolean parenthesised(Context<?> ctx) {
        switch (ctx.family()) {





            case CLICKHOUSE:
                return true;















            case CUBRID:
            case FIREBIRD:
            case H2:
            case HSQLDB:
            case MARIADB:
            case MYSQL:
            case SQLITE:
            case TRINO:
                return false;

            default:
                return true;
        }
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {















            case CLICKHOUSE:
                ctx.visit(function(N_makeDate, getDataType(), year, month, day));
                break;








            case MARIADB:
            case MYSQL:
                ctx.visit(makedateEmulation());
                break;
















            case SQLITE:
                ctx.visit(printfEmulation());
                break;























            case CUBRID:
            case FIREBIRD:
            case H2:
            case HSQLDB:
            case TRINO:
                ctx.visit(castEmulation());
                break;

            default:
                ctx.visit(function(N_MAKE_DATE, getDataType(), year, month, day));
                break;
        }
    }















    // [#19287] MySQL doesn't have a 3 argument date constructor, but
    //          a 2 argument one with the day of year.
    private final Field<Date> makedateEmulation() {
        return DSL.dateAdd(
            DSL.dateAdd(
                function(N_MAKEDATE, getDataType(), year, one()),
                isub(month, one()),
                DatePart.MONTH
            ),
            isub(day, one()),
            DatePart.DAY
        );
    }

    // [#19287] SQLite's PRINTF() formats NULL as 0, so NULL arguments must be
    //          handled explicitly.
    private final Field<Date> printfEmulation() {
        Field<Date> result = function(N_DATE, getDataType(), function(N_PRINTF, VARCHAR, inline("%04d-%02d-%02d"), year, month, day));
        Condition c = DSL.and(notNull(year), notNull(month), notNull(day));
        return c instanceof NoCondition ? result : DSL.when(c, result);
    }

    private static final Condition notNull(Field<?> f) {
        return isVal0(f, v -> v.getValue() != null) ? noCondition() : f.isNotNull();
    }

    // [#19287] Dialects without any numeric date constructor parse
    //          an ISO 8601 yyyy-mm-dd string instead.
    private final Field<Date> castEmulation() {
        return DSL.cast(isoString(), getDataType());
    }

    private final Field<String> isoString() {
        return DSL.concat(
            pad(year, 4),
            inline("-"),
            pad(month, 2),
            inline("-"),
            pad(day, 2)
        );
    }

    private static final Field<String> pad(Field<? extends Number> f, int length) {
        return DSL.lpad(DSL.cast(f, VARCHAR), length, "0");
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<? extends Number> $arg1() {
        return year;
    }

    @Override
    public final Field<? extends Number> $arg2() {
        return month;
    }

    @Override
    public final Field<? extends Number> $arg3() {
        return day;
    }

    @Override
    public final QOM.MakeDate $arg1(Field<? extends Number> newValue) {
        return $constructor().apply(newValue, $arg2(), $arg3());
    }

    @Override
    public final QOM.MakeDate $arg2(Field<? extends Number> newValue) {
        return $constructor().apply($arg1(), newValue, $arg3());
    }

    @Override
    public final QOM.MakeDate $arg3(Field<? extends Number> newValue) {
        return $constructor().apply($arg1(), $arg2(), newValue);
    }

    @Override
    public final Function3<? super Field<? extends Number>, ? super Field<? extends Number>, ? super Field<? extends Number>, ? extends QOM.MakeDate> $constructor() {
        return (a1, a2, a3) -> new MakeDate(a1, a2, a3);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.MakeDate o) {
            return
                Objects.equals($year(), o.$year()) &&
                Objects.equals($month(), o.$month()) &&
                Objects.equals($day(), o.$day())
            ;
        }
        else
            return super.equals(that);
    }
}

