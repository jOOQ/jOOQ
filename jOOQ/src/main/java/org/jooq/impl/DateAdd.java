/**
 * Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
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

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.keyword;
import static org.jooq.impl.DSL.sql;

import java.sql.Date;

import org.jooq.Configuration;
import org.jooq.DatePart;
import org.jooq.Field;
import org.jooq.QueryPart;
import org.jooq.exception.SQLDialectNotSupportedException;

/**
 * @author Lukas Eder
 */
class DateAdd<T extends java.util.Date> extends AbstractFunction<T> {

    /**
     * Generated UID
     */
    private static final long             serialVersionUID = -19593015886723235L;

    private final Field<T>                date;
    private final Field<? extends Number> interval;
    private final DatePart                datePart;

    DateAdd(Field<T> date, Field<? extends Number> interval, DatePart datePart) {
        super("dateadd", date.getDataType());

        this.date = date;
        this.interval = interval;
        this.datePart = datePart;
    }

    @Override
    final QueryPart getFunction0(Configuration configuration) {
        String keyword = null;
        String function = null;

        switch (configuration.family()) {
            case CUBRID:
            case MARIADB:
            case MYSQL: {
                switch (datePart) {
                    case YEAR:   keyword = "year";   break;
                    case MONTH:  keyword = "month";  break;
                    case DAY:    keyword = "day";    break;
                    case HOUR:   keyword = "hour";   break;
                    case MINUTE: keyword = "minute"; break;
                    case SECOND: keyword = "second"; break;
                    default: throwUnsupported();
                }

                return field("{date_add}({0}, {interval} {1} {2})", getDataType(), date, interval, keyword(keyword));
            }

            case DERBY:
            case HSQLDB: {
                switch (datePart) {
                    case YEAR:   keyword = "sql_tsi_year";   break;
                    case MONTH:  keyword = "sql_tsi_month";  break;
                    case DAY:    keyword = "sql_tsi_day";    break;
                    case HOUR:   keyword = "sql_tsi_hour";   break;
                    case MINUTE: keyword = "sql_tsi_minute"; break;
                    case SECOND: keyword = "sql_tsi_second"; break;
                    default: throwUnsupported();
                }

                return field("{fn {timestampadd}({0}, {1}, {2}) }", getDataType(), keyword(keyword), interval, date);
            }

            case FIREBIRD: {
                switch (datePart) {
                    case YEAR:   keyword = "year";   break;
                    case MONTH:  keyword = "month";  break;
                    case DAY:    keyword = "day";    break;
                    case HOUR:   keyword = "hour";   break;
                    case MINUTE: keyword = "minute"; break;
                    case SECOND: keyword = "second"; break;
                    default: throwUnsupported();
                }

                return field("{dateadd}({0}, {1}, {2})", getDataType(), keyword(keyword), interval, date);
            }

            case H2: {
                switch (datePart) {
                    case YEAR:   keyword = "year";   break;
                    case MONTH:  keyword = "month";  break;
                    case DAY:    keyword = "day";    break;
                    case HOUR:   keyword = "hour";   break;
                    case MINUTE: keyword = "minute"; break;
                    case SECOND: keyword = "second"; break;
                    default: throwUnsupported();
                }

                return field("{dateadd}({0}, {1}, {2})", getDataType(), inline(keyword), interval, date);
            }

            case POSTGRES: {
                switch (datePart) {
                    case YEAR:   keyword = " year";   break;
                    case MONTH:  keyword = " month";  break;
                    case DAY:    keyword = " day";    break;
                    case HOUR:   keyword = " hour";   break;
                    case MINUTE: keyword = " minute"; break;
                    case SECOND: keyword = " second"; break;
                    default: throwUnsupported();
                }

                // [#3824] Ensure that the output for DATE arithmetic will also
                // be of type DATE, not TIMESTAMP
                if (getDataType().getType() == Date.class)
                    return field("({0} + ({1} || {2})::interval)::date", getDataType(), date, interval, inline(keyword));
                else
                    return field("({0} + ({1} || {2})::interval)", getDataType(), date, interval, inline(keyword));
            }

            case SQLITE: {
                switch (datePart) {
                    case YEAR:   keyword = " year";   break;
                    case MONTH:  keyword = " month";  break;
                    case DAY:    keyword = " day";    break;
                    case HOUR:   keyword = " hour";   break;
                    case MINUTE: keyword = " minute"; break;
                    case SECOND: keyword = " second"; break;
                    default: throwUnsupported();
                }

                return field("{datetime}({0}, '+' || {1} || {2})", getDataType(), date, interval, inline(keyword));
            }

            /* [pro] xx
            xxxx xxxxxxx x
                xxxxxx xxxxxxxxxx x
                    xxxx xxxxx   xxxxxxx x xxxxxxx xxxxxx
                    xxxx xxxxxx  xxxxxxx x xxxx    xxxxxx
                    xxxx xxxx    xxxxxxx x xxxx    xxxxxx
                    xxxx xxxxx   xxxxxxx x xxxx    xxxxxx
                    xxxx xxxxxxx xxxxxxx x xxxx    xxxxxx
                    xxxx xxxxxxx xxxxxxx x xxxx    xxxxxx
                    xxxxxxxx xxxxxxxxxxxxxxxxxxx
                x

                xxxxxx xxxxxxxxxxxxxxxxxxxxx xxxx xxxxxx xxxxxxxxxxxxxx xxxxxxxxxxxxxxxx xxxxxxxxx xxxxxx
            x

            xxxx xxxx
            xxxx xxxxxxx
            xxxx xxxxxxxxxx x
                xxxxxx xxxxxxxxxx x
                    xxxx xxxxx   xxxxxxx x xxxxx xxxxxx
                    xxxx xxxxxx  xxxxxxx x xxxxx xxxxxx
                    xxxx xxxx    xxxxxxx x xxxxx xxxxxx
                    xxxx xxxxx   xxxxxxx x xxxxx xxxxxx
                    xxxx xxxxxxx xxxxxxx x xxxxx xxxxxx
                    xxxx xxxxxxx xxxxxxx x xxxxx xxxxxx
                    xxxxxxxx xxxxxxxxxxxxxxxxxxx
                x

                xxxxxx xxxxxxxxxxxxxxxxxxxxx xxxx xxxxxx xxxxxxxxxxxxxx xxxxxxxxxxxxxxxxx xxxxxxxxx xxxxxx
            x

            xxxx xxxxx x
                xxxxxx xxxxxxxxxx x xxxxx

                xxxxxx xxxxxxxxxx x
                    xxxx xxxxx   xxxxxxx x xxxxxxxxxxxx                        xxxxxx
                    xxxx xxxxxx  xxxxxxx x xxxxxxxxxxxxx                       xxxxxx
                    xxxx xxxx    xxxxxxx x xxxxxxxxxxx                         xxxxxx
                    xxxx xxxxx   xxxxxxx x xxxxxxxxxxxxxx xxxxxxxxxx x xxxxxxx xxxxxx
                    xxxx xxxxxxx xxxxxxx x xxxxxxxxxxxxxx xxxxxxxxxx x xxxxx   xxxxxx
                    xxxx xxxxxxx xxxxxxx x xxxxxxxxxxxxxx                      xxxxxx
                    xxxxxxxx xxxxxxxxxxxxxxxxxxx
                x

                xx xxxxxxxxxxx xx xxxxx
                    xxxxxx xxxxxxxxxxxxxxx xxxxxx xxxxxxxxxxxxxx xxxxxxxxxxxxxxxxx xxxxx xxxxxxxxxx
                xxxx
                    xxxxxx xxxxxxxxxxxxxxx xxx x xxxxxx xxxxxxxxxxxxxx xxxxxxxxxxxxxxxxx xxxxx xxxxxxxxx xxxxxxxxxxxxxxxxx
            x

            xxxx xxxxxxxxx x
                xxxxxx xxxxxxxxxx x
                    xxxx xxxxx   xxxxxxx x xxxxxxx   xxxxxx
                    xxxx xxxxxx  xxxxxxx x xxxxxxxx  xxxxxx
                    xxxx xxxx    xxxxxxx x xxxxxx    xxxxxx
                    xxxx xxxxx   xxxxxxx x xxxxxxx   xxxxxx
                    xxxx xxxxxxx xxxxxxx x xxxxxxxxx xxxxxx
                    xxxx xxxxxxx xxxxxxx x xxxxxxxxx xxxxxx
                    xxxxxxxx xxxxxxxxxxxxxxxxxxx
                x

                xxxxxx xxxxxxxxxxxxxxxxxxx xxxxxxx xxxxx xxxxxxxxx xxxxxxxxxxxxxxxxxxx
            x

            xxxx xxxx x
                xxxxxx xxxxxxxxxx x
                    xxxx xxxxx   xxxxxxx x xxxxxxx   xxxxxx
                    xxxx xxxxxx  xxxxxxx x xxxxxxxx  xxxxxx
                    xxxx xxxx    xxxxxxx x xxxxxx    xxxxxx
                    xxxx xxxxx   xxxxxxx x xxxxxxx   xxxxxx
                    xxxx xxxxxxx xxxxxxx x xxxxxxxxx xxxxxx
                    xxxx xxxxxxx xxxxxxx x xxxxxxxxx xxxxxx
                    xxxxxxxx xxxxxxxxxxxxxxxxxxx
                x

                xxxxxx xxxxxxxxxxxxxxxxxxx xxxxx xxxxxxxxx xxxxxxxxxxxxxxxxxxx
            x

            xxxx xxxxxxx x
                xxxxxx xxxxxxxxxx x

                    xx xxxxxxx xx xxxxxx xxx xxxxxxxxxxxxxxx xxxxxxxx xx xxxxxx
                    xx xxxx xxxxx xxxxxxxxx xx xxxx xxxx xxxxxxxx xxxxxxx xxxxx
                    xxxx xxxxx   xxxxxx xxxxxxxxxxxxxxxxxxxxxxxx xxxxxx xxxxx xxxxxxxxxxxxxxxxxxxxxxxxxx
                    xxxx xxxxxx  xxxxxx xxxxxxxxxxxxxxxxxxxxxxxx xxxxxx xxxxx xxxxxxxxxx
                    xxxx xxxx    xxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxx xxxxxxxxx xxxxxxxxxxxxxxxx
                    xxxx xxxxx   xxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxx xxxxxxxxx xxxxxxxxxxxxxxxxx
                    xxxx xxxxxxx xxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxx xxxxxxxxx xxxxxxxxxxxxxxxxxxx
                    xxxx xxxxxxx xxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxx xxxxxxxxx xxxxxxxxxxxxxxxxxxx
                    xxxxxxxx xxxxxxxxxxxxxxxxxxx
                x
            x

            xxxx xxxxxxx x
                xxxxx xxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxx xxxxxxxxxxx xx xxx xxx xxxxxxxxxxxxxx
            x
            xx [/pro] */
        }

        return null;
    }

    private final void throwUnsupported() {
        throw new UnsupportedOperationException("Unknown date part : " + datePart);
    }
}
