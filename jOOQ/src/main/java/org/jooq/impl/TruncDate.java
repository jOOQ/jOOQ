/**
 * Copyright (c) 2009-2014, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */
package org.jooq.impl;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.inline;

import java.sql.Date;

import org.jooq.Configuration;
import org.jooq.DatePart;
import org.jooq.Field;
import org.jooq.QueryPart;

/**
 * @author Lukas Eder
 */
class TruncDate<T extends java.util.Date> extends AbstractFunction<T> {

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

        switch (configuration.dialect().family()) {

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

                return field("{trunc}({0}, {1})", getDataType(), date, inline(keyword));
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

                return field("{parsedatetime}({formatdatetime}({0}, {1}), {1})", getDataType(), date, inline(format));
            }

// These don't work yet and need better integration-testing:
// ---------------------------------------------------------
//            case MARIADB:
//            case MYSQL: {
//                switch (part) {
//                    case YEAR:   return field("{str_to_date}({date_format}({0}, '%Y-00-00 00:00:00.0'), '%Y-%m-%d %H:%i:%s.0')", getDataType(), date);
//                    case MONTH:  return field("{str_to_date}({date_format}({0}, '%Y-%m-00 00:00:00.0'), '%Y-%m-%d %H:%i:%s.0')", getDataType(), date);
//                    case DAY:    return field("{str_to_date}({date_format}({0}, '%Y-%m-%d 00:00:00.0'), '%Y-%m-%d %H:%i:%s.0')", getDataType(), date);
//                    case HOUR:   return field("{str_to_date}({date_format}({0}, '%Y-%m-%d %H:00:00.0'), '%Y-%m-%d %H:%i:%s.0')", getDataType(), date);
//                    case MINUTE: return field("{str_to_date}({date_format}({0}, '%Y-%m-%d %H:%i:00.0'), '%Y-%m-%d %H:%i:%s.0')", getDataType(), date);
//                    case SECOND: return field("{str_to_date}({date_format}({0}, '%Y-%m-%d %H:%i:%s.0'), '%Y-%m-%d %H:%i:%s.0')", getDataType(), date);
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

                return field("{date_trunc}({0}, {1})", getDataType(), inline(keyword), date);
            }

// These don't work yet and need better integration-testing:
// ---------------------------------------------------------
//            case SQLITE: {
//                switch (part) {
//                    case YEAR:   return field("{strftime}({0}, '%Y-00-00 00:00:00.0')", getDataType(), date);
//                    case MONTH:  return field("{strftime}({0}, '%Y-%m-00 00:00:00.0')", getDataType(), date);
//                    case DAY:    return field("{strftime}({0}, '%Y-%m-%d 00:00:00.0')", getDataType(), date);
//                    case HOUR:   return field("{strftime}({0}, '%Y-%m-%d %H:00:00.0')", getDataType(), date);
//                    case MINUTE: return field("{strftime}({0}, '%Y-%m-%d %H:%i:00.0')", getDataType(), date);
//                    case SECOND: return field("{strftime}({0}, '%Y-%m-%d %H:%i:%s.0')", getDataType(), date);
//                    default: throwUnsupported();
//                }
//            }

            /* [pro] xx
            xxxx xxxxxxxxx x
                xxxxxx xxxxxx x
                    xxxx xxxxx   xxxxxxx x xxxxxxx   xxxxxx
                    xxxx xxxxxx  xxxxxxx x xxxxxxxx  xxxxxx
                    xxxx xxxx    xxxxxxx x xxxxx     xxxxxx
                    xxxx xxxxx   xxxxxxx x xxxxx     xxxxxx
                    xxxx xxxxxxx xxxxxxx x xxxxx     xxxxxx
                    xxxx xxxxxxx xxxxxx xxxxxxxxxxxxxxxxx xxx xxxxxxxx xxxx xx xxxxxxxxxx xxxxxx
                    xxxxxxxx xxxxxxxxxxxxxxxxxxx
                x

                xxxxxx xxxxxxxxxxxxxxxxxxx xxxxxx xxxxxxxxxxxxxx xxxxx xxxxxxxxxxxxxxxxx
            x

            xxxx xxxx x
                xxxxxx xxxxxx x
                    xxxx xxxxx   xxxxxxx x xxxxxxx   xxxxxx
                    xxxx xxxxxx  xxxxxxx x xxxxx     xxxxxx
                    xxxx xxxx    xxxxxxx x xxxxx     xxxxxx
                    xxxx xxxxx   xxxxxxx x xxxxx     xxxxxx
                    xxxx xxxxxxx xxxxxxx x xxxxx     xxxxxx
                    xxxx xxxxxxx xxxxxxx x xxxxx     xxxxxx
                    xxxxxxxx xxxxxxxxxxxxxxxxxxx
                x

                xxxxxx xxxxxxxxxxxxxxxxxxx xxxxxx xxxxxxxxxxxxxx xxxxx xxxxxxxxxxxxxxxxx
            x

            xxxx xxxxxxx x
                xxxxxx xxxxxx x
                    xxxx xxxxx   xxxxxxx x xxxxxxx   xxxxxx
                    xxxx xxxxxx  xxxxxxx x xxxxx     xxxxxx
                    xxxx xxxx    xxxxxxx x xxxxx     xxxxxx
                    xxxx xxxxx   xxxxxxx x xxxxx     xxxxxx
                    xxxx xxxxxxx xxxxxxx x xxxxx     xxxxxx
                    xxxx xxxxxxx xxxxxx xxxxxxxxxxxxxxxxxxxxxx
                    xxxxxxxx xxxxxxxxxxxxxxxxxxx
                x

                xxxxxx xxxxxxxxxxxxxxxxxxx xxxxxx xxxxxxxxxxxxxx xxxxx xxxxxxxxxxxxxxxxx
            x

xx xxxxx xxxxx xxxx xxx xxx xxxx xxxxxx xxxxxxxxxxxxxxxxxxxx
xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xx            xxxx xxxxxxxxxx
xx            xxxx xxxxxxx x
xx                xxxxxx xxxxxx x
xx                    xxxx xxxxx   xxxxxx xxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxx xx xxxxx xxxx xxxxxxxxxxxxxx xxxxxx
xx                    xxxx xxxxxx  xxxxxx xxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxx xx xxxxx xxxx xxxxxxxxxxxxxx xxxxxx
xx                    xxxx xxxx    xxxxxx xxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxx xx xxxxx xxxx xxxxxxxxxxxxxx xxxxxx
xx                    xxxx xxxxx   xxxxxx xxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxx xx xxxxx xxxx xxxxxxxxxxxxxx xxxxxx
xx                    xxxx xxxxxxx xxxxxx xxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxx xx xxxxx xxxx xxxxxxxxxxxxxx xxxxxx
xx                    xxxx xxxxxxx xxxxxx xxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxx xx xxxxx xxxx xxxxxxxxxxxxxx xxxxxx
xx                    xxxxxxxx xxxxxxxxxxxxxxxxxxx
xx                x
xx            x

            xx [/pro] */

            default:
                return field("{trunc}({0}, {1})", getDataType(), date, inline(part.name()));
        }
    }

    private final void throwUnsupported() {
        throw new UnsupportedOperationException("Unknown date part : " + part);
    }
}
