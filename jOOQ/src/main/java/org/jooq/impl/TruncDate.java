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
 */
package org.jooq.impl;

import static org.jooq.impl.DSL.inline;

import java.sql.Date;

import org.jooq.Configuration;
import org.jooq.DatePart;
import org.jooq.Field;
import org.jooq.QueryPart;

/**
 * @author Lukas Eder
 */
final class TruncDate<T> extends AbstractFunction<T> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -4617792768119885313L;

    private final Field<T>    date;
    private final DatePart    part;

    TruncDate(Field<T> date, DatePart part) {
        super("trunc", date.getDataType());

        this.date = date;
        this.part = part;
    }

    @Override
    final QueryPart getFunction0(Configuration configuration) {
        String keyword = null;
        String format = null;

        switch (configuration.family()) {

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
                    default: throwUnsupported();
                }

                return DSL.field("{trunc}({0}, {1})", getDataType(), date, inline(keyword));
            }

            case H2: {
                switch (part) {
                    case YEAR:   format = "yyyy";                break;
                    case MONTH:  format = "yyyy-MM";             break;
                    case DAY:    format = "yyyy-MM-dd";          break;
                    case HOUR:   format = "yyyy-MM-dd HH";       break;
                    case MINUTE: format = "yyyy-MM-dd HH:mm";    break;
                    case SECOND: format = "yyyy-MM-dd HH:mm:ss"; break;
                    default: throwUnsupported();
                }

                return DSL.field("{parsedatetime}({formatdatetime}({0}, {1}), {1})", getDataType(), date, inline(format));
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






            case POSTGRES: {
                switch (part) {
                    case YEAR:   keyword = "year";   break;
                    case MONTH:  keyword = "month";  break;
                    case DAY:    keyword = "day";    break;
                    case HOUR:   keyword = "hour";   break;
                    case MINUTE: keyword = "minute"; break;
                    case SECOND: keyword = "second"; break;
                    default: throwUnsupported();
                }

                return DSL.field("{date_trunc}({0}, {1})", getDataType(), inline(keyword), date);
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
                return DSL.field("{trunc}({0}, {1})", getDataType(), date, inline(part.name()));
        }
    }

    private final void throwUnsupported() {
        throw new UnsupportedOperationException("Unknown date part : " + part);
    }
}
