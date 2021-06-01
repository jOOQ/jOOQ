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

import static org.jooq.impl.DSL.function;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.isoDayOfWeek;
import static org.jooq.impl.DSL.keyword;
import static org.jooq.impl.DSL.one;
import static org.jooq.impl.Internal.iadd;
import static org.jooq.impl.Internal.idiv;
import static org.jooq.impl.Internal.imul;
import static org.jooq.impl.Keywords.K_DATE;
import static org.jooq.impl.Keywords.K_DAY;
import static org.jooq.impl.Keywords.K_FROM;
import static org.jooq.impl.Keywords.K_HOUR;
import static org.jooq.impl.Keywords.K_INT;
import static org.jooq.impl.Keywords.K_MINUTE;
import static org.jooq.impl.Keywords.K_MONTH;
import static org.jooq.impl.Keywords.K_SECOND;
import static org.jooq.impl.Keywords.K_YEAR;
import static org.jooq.impl.Names.N_DATEDIFF;
import static org.jooq.impl.Names.N_DATEPART;
import static org.jooq.impl.Names.N_DAYOFWEEK;
import static org.jooq.impl.Names.N_DAYOFYEAR;
import static org.jooq.impl.Names.N_EXTRACT;
import static org.jooq.impl.Names.N_SECONDS_BETWEEN;
import static org.jooq.impl.Names.N_STRFTIME;
import static org.jooq.impl.Names.N_TO_CHAR;
import static org.jooq.impl.Names.N_TO_NUMBER;
import static org.jooq.impl.Names.N_TRUNC;
import static org.jooq.impl.Names.N_WEEKDAY;
import static org.jooq.impl.SQLDataType.INTEGER;
import static org.jooq.impl.SQLDataType.VARCHAR;
import static org.jooq.impl.Tools.castIfNeeded;

import java.sql.Date;
import java.sql.Timestamp;

import org.jooq.Context;
import org.jooq.DatePart;
import org.jooq.Field;
import org.jooq.Keyword;

/**
 * @author Lukas Eder
 */
final class Extract extends AbstractField<Integer> {

    private final Field<?> field;
    private final DatePart datePart;

    Extract(Field<?> field, DatePart datePart) {
        super(N_EXTRACT, INTEGER);

        this.field = field;
        this.datePart = datePart;
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {
            case SQLITE:
                switch (datePart) {
                    case YEAR:
                        ctx.visit(function(N_STRFTIME, VARCHAR, inline("%Y"), field).cast(INTEGER));
                        return;
                    case MONTH:
                        ctx.visit(function(N_STRFTIME, VARCHAR, inline("%m"), field).cast(INTEGER));
                        return;
                    case DAY:
                        ctx.visit(function(N_STRFTIME, VARCHAR, inline("%d"), field).cast(INTEGER));
                        return;
                    case HOUR:
                        ctx.visit(function(N_STRFTIME, VARCHAR, inline("%H"), field).cast(INTEGER));
                        return;
                    case MINUTE:
                        ctx.visit(function(N_STRFTIME, VARCHAR, inline("%M"), field).cast(INTEGER));
                        return;
                    case SECOND:
                        ctx.visit(function(N_STRFTIME, VARCHAR, inline("%S"), field).cast(INTEGER));
                        return;

                    // See: https://www.sqlite.org/lang_datefunc.html
                    case EPOCH:
                        ctx.visit(function(N_STRFTIME, VARCHAR, inline("%s"), field).cast(INTEGER));
                        return;
                    case ISO_DAY_OF_WEEK:
                        ctx.visit(dowSun0ToISO(function("strftime", INTEGER, inline("%w"), field).cast(INTEGER)));
                        return;
                    case DAY_OF_WEEK:
                        ctx.visit(function(N_STRFTIME, VARCHAR, inline("%w"), field).cast(INTEGER).plus(one()));
                        return;
                    case DAY_OF_YEAR:
                        ctx.visit(function(N_STRFTIME, VARCHAR, inline("%j"), field).cast(INTEGER));
                        return;
                }
                break;















































            case DERBY:
                switch (datePart) {
                    case YEAR:
                        ctx.visit(K_YEAR).sql('(').visit(field).sql(')');
                        return;
                    case MONTH:
                        ctx.visit(K_MONTH).sql('(').visit(field).sql(')');
                        return;
                    case DAY:
                        ctx.visit(K_DAY).sql('(').visit(field).sql(')');
                        return;
                    case HOUR:
                        ctx.visit(K_HOUR).sql('(').visit(field).sql(')');
                        return;
                    case MINUTE:
                        ctx.visit(K_MINUTE).sql('(').visit(field).sql(')');
                        return;
                    case SECOND:
                        ctx.visit(K_SECOND).sql('(').visit(field).sql(')');
                        return;
                }
                break;





























































































































            case MARIADB:
            case MYSQL:
                switch (datePart) {
                    case DAY_OF_WEEK:
                        ctx.visit(N_DAYOFWEEK).sql('(').visit(field).sql(')');
                        return;
                    case DAY_OF_YEAR:
                        ctx.visit(N_DAYOFYEAR).sql('(').visit(field).sql(')');
                        return;
                    case EPOCH:
                        ctx.visit(keyword("unix_timestamp")).sql('(').visit(field).sql(')');
                        return;
                    case ISO_DAY_OF_WEEK:
                        ctx.visit(N_WEEKDAY).sql('(').visit(field).sql(") + 1");
                        return;
                    case QUARTER:
                        ctx.visit(datePart.toName()).sql('(').visit(field).sql(')');
                        return;
                }
                break;














            case POSTGRES:
                switch (datePart) {
                    case DAY_OF_WEEK:
                        ctx.sql('(');
                        acceptNativeFunction(ctx, keyword("dow"));
                        ctx.sql(" + 1)");
                        return;
                    case DAY_OF_YEAR:
                        acceptNativeFunction(ctx, keyword("doy"));
                        return;
                    case ISO_DAY_OF_WEEK:
                        acceptNativeFunction(ctx, keyword("isodow"));
                        return;
                    case MILLISECOND:
                        acceptNativeFunction(ctx, keyword("milliseconds"));
                        return;
                    case MICROSECOND:
                        acceptNativeFunction(ctx, keyword("microseconds"));
                        return;
                    case CENTURY:
                    case DECADE:
                    case MILLENNIUM:
                    case QUARTER:
                    case TIMEZONE:
                        acceptNativeFunction(ctx);
                        return;
                }
                break;

            case HSQLDB:
                switch (datePart) {
                    case EPOCH:
                        ctx.visit(keyword("unix_timestamp")).sql('(').visit(field).sql(')');
                        return;
                    case ISO_DAY_OF_WEEK:
                        ctx.visit(dowSun1ToISO(DSL.field("{extract}({day_of_week from} {0})", INTEGER, field)));
                        return;
                    case QUARTER:
                    case WEEK:
                        ctx.visit(datePart.toName()).sql('(').visit(field).sql(')');
                        return;
                }
                break;

            case H2:
                switch (datePart) {
                    case QUARTER:
                        ctx.visit(datePart.toName()).sql('(').visit(field).sql(')');
                        return;
                    case WEEK:
                        ctx.visit(keyword("iso_week")).sql('(').visit(field).sql(')');
                        return;
                }
                break;
        }

        acceptDefaultEmulation(ctx);
    }

    private final static Field<Integer> dowISOToSun1(Field<Integer> dow) {
        return iadd(dow.mod(inline(7)), one());
    }

    private final static Field<Integer> dowSun1ToISO(Field<Integer> dow) {
        return iadd(iadd(dow, inline(5)).mod(inline(7)), one());
    }

    private final static Field<Integer> dowSun0ToISO(Field<Integer> dow) {
        return iadd(iadd(dow, inline(6)).mod(inline(7)), one());
    }

    private final void acceptDefaultEmulation(Context<?> ctx) {
        switch (datePart) {
            case DECADE:
                ctx.visit(DSL.floor(idiv(DSL.year(field), inline(10))));
                break;

            case CENTURY:
                ctx.visit(DSL.floor(idiv(
                    imul(
                        DSL.sign(DSL.year(field)),
                        iadd(DSL.abs(DSL.year(field)), inline(99))
                    ),
                    inline(100)
                )));
                break;

            case MILLENNIUM:
                ctx.visit(DSL.floor(idiv(
                    imul(
                        DSL.sign(DSL.year(field)),
                        iadd(DSL.abs(DSL.year(field)), inline(999))
                    ),
                    inline(1000)
                )));
                break;

            case QUARTER:
                ctx.visit(DSL.floor(idiv(
                    iadd(DSL.month(field), inline(2)),
                    inline(3)
                )));
                break;

            case TIMEZONE:
                ctx.visit(iadd(
                    imul(DSL.extract(field, DatePart.TIMEZONE_HOUR), inline(3600)),
                    imul(DSL.extract(field, DatePart.TIMEZONE_MINUTE), inline(60))
                ));
                break;

            default:
                acceptNativeFunction(ctx);
                break;
        }
    }

    private final void acceptNativeFunction(Context<?> ctx) {
        acceptNativeFunction(ctx, datePart.toKeyword());
    }

    private final void acceptNativeFunction(Context<?> ctx, Keyword keyword) {
        ctx.visit(N_EXTRACT).sql('(').visit(keyword).sql(' ').visit(K_FROM).sql(' ').visit(field).sql(')');
    }
}
