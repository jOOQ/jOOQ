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

import static org.jooq.DatePart.YEAR;
import static org.jooq.impl.DSL.function;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.keyword;
import static org.jooq.impl.Keywords.K_CAST;
import static org.jooq.impl.Names.N_DATETIME_TRUNC;
import static org.jooq.impl.Names.N_DATE_TRUNC;
import static org.jooq.impl.Names.N_TRUNC;
import static org.jooq.impl.SQLDataType.VARCHAR;
import static org.jooq.impl.Tools.castIfNeeded;

import java.sql.Date;

import org.jooq.Context;
import org.jooq.DatePart;
import org.jooq.Field;
import org.jooq.impl.QOM.UNotYetImplemented;

/**
 * @author Lukas Eder
 */
final class TruncDate<T> extends AbstractField<T> implements UNotYetImplemented {

    private final Field<T>    date;
    private final DatePart    part;

    TruncDate(Field<T> date, DatePart part) {
        super(N_TRUNC, date.getDataType());

        this.date = date;
        this.part = part;
    }

    @Override
    public final void accept(Context<?> ctx) {
        String keyword = null;
        String format = null;

        familySwitch:
        switch (ctx.family()) {

            // [http://jira.cubrid.org/browse/ENGINE-120] This currently doesn't work for all date parts in CUBRID
            case CUBRID:
            case HSQLDB: {
                switch (part) {
                    case YEAR:   keyword = "YY"; break;
                    case MONTH:  keyword = "MM"; break;
                    case DAY:    keyword = "DD"; break;
                    case HOUR:   keyword = "HH"; break;
                    case MINUTE: keyword = "MI"; break;
                    case SECOND: keyword = "SS"; break;
                    default: acceptDefaultEmulation(ctx); break familySwitch;
                }

                ctx.visit(N_TRUNC).sql('(').visit(date).sql(", ").visit(inline(keyword)).sql(')');
                break;
            }

            case H2: {
                switch (part) {
                    case YEAR:   format = "yyyy";                break;
                    case MONTH:  format = "yyyy-MM";             break;
                    case DAY:    format = "yyyy-MM-dd";          break;
                    case HOUR:   format = "yyyy-MM-dd HH";       break;
                    case MINUTE: format = "yyyy-MM-dd HH:mm";    break;
                    case SECOND: format = "yyyy-MM-dd HH:mm:ss"; break;
                    default: acceptDefaultEmulation(ctx); break familySwitch;
                }

                ctx.visit(DSL.keyword("parsedatetime")).sql('(')
                   .visit(DSL.keyword("formatdatetime")).sql('(').visit(date).sql(", ").visit(inline(format)).sql("), ").visit(inline(format)).sql(')');
                break;
            }

// These don't work yet and need better integration-testing:
// ---------------------------------------------------------
//            case MARIADB:
//            case MYSQL: {
//                switch (part) {
//                    case YEAR:   return DSL.field("{str_to_date}({date_format}({0}, '%Y-00-00 00:00:00.0'), '%Y-%m-%d %H:%i:%s.0')", getDataType(), date);
//                    case MONTH:  return DSL.field("{str_to_date}({date_format}({0}, '%Y-%m-00 00:00:00.0'), '%Y-%m-%d %H:%i:%s.0')", getDataType(), date);
//                    case DAY:    return DSL.field("{str_to_date}({date_format}({0}, '%Y-%m-%d 00:00:00.0'), '%Y-%m-%d %H:%i:%s.0')", getDataType(), date);
//                    case HOUR:   return DSL.field("{str_to_date}({date_format}({0}, '%Y-%m-%d %H:00:00.0'), '%Y-%m-%d %H:%i:%s.0')", getDataType(), date);
//                    case MINUTE: return DSL.field("{str_to_date}({date_format}({0}, '%Y-%m-%d %H:%i:00.0'), '%Y-%m-%d %H:%i:%s.0')", getDataType(), date);
//                    case SECOND: return DSL.field("{str_to_date}({date_format}({0}, '%Y-%m-%d %H:%i:%s.0'), '%Y-%m-%d %H:%i:%s.0')", getDataType(), date);
//                    default: throwUnsupported();
//                }
//            }





            case POSTGRES:
            case YUGABYTEDB: {
                ctx.visit(N_DATE_TRUNC).sql('(').visit(inline(part.toKeyword().toString())).sql(", ").visit(date).sql(')');
                break;
            }

// These don't work yet and need better integration-testing:
// ---------------------------------------------------------
//            case SQLITE: {
//                switch (part) {
//                    case YEAR:   return DSL.field("{strftime}({0}, '%Y-00-00 00:00:00.0')", getDataType(), date);
//                    case MONTH:  return DSL.field("{strftime}({0}, '%Y-%m-00 00:00:00.0')", getDataType(), date);
//                    case DAY:    return DSL.field("{strftime}({0}, '%Y-%m-%d 00:00:00.0')", getDataType(), date);
//                    case HOUR:   return DSL.field("{strftime}({0}, '%Y-%m-%d %H:00:00.0')", getDataType(), date);
//                    case MINUTE: return DSL.field("{strftime}({0}, '%Y-%m-%d %H:%i:00.0')", getDataType(), date);
//                    case SECOND: return DSL.field("{strftime}({0}, '%Y-%m-%d %H:%i:%s.0')", getDataType(), date);
//                    default: throwUnsupported();
//                }
//            }






























































































            default:
                ctx.visit(N_TRUNC).sql('(').visit(date).sql(", ").visit(inline(keyword)).sql(')');
                break;
        }
    }

    private final void acceptDefaultEmulation(Context<?> ctx) {
        switch (part) {
            case DECADE:
                ctx.visit(padYear(ctx, 3, DSL.extract(date, part)).concat(dateOrTimestampLiteral("0-01-01")).cast(getDataType()));
                break;

            case CENTURY:
                ctx.visit(padYear(ctx, 2, DSL.extract(date, part).minus(inline(1))).concat(dateOrTimestampLiteral("01-01-01")).cast(getDataType()));
                break;

            case MILLENNIUM:
                ctx.visit(DSL.extract(date, part).minus(inline(1)).cast(VARCHAR(10)).concat(dateOrTimestampLiteral("001-01-01")).cast(getDataType()));
                break;

            case QUARTER:
                ctx.visit(padYear(ctx, 4, DSL.extract(date, YEAR)).concat(inline("-")).concat(DSL.extract(date, part).minus(inline(1)).times(inline(3)).plus(inline(1)).cast(VARCHAR(10))).concat(dateOrTimestampLiteral("-01")).cast(getDataType()));
                break;

            default:
                throwUnsupported();
                break;
        }
    }

    private final Field<String> padYear(Context<?> ctx, int length, Field<Integer> year) {
        switch (ctx.family()) {

            case HSQLDB:
                return DSL.lpad(year.cast(VARCHAR(10)), inline(length), inline("0"));

            default:
                return year.cast(VARCHAR(10));
        }
    }

    private final Field<String> dateOrTimestampLiteral(String string) {
        return DSL.inline(getDataType().isDate() ? string : string + " 00:00:00");
    }

    private final void throwUnsupported() {
        throw new UnsupportedOperationException("Unknown date part : " + part);
    }
}
