/**
 * Copyright (c) 2009-2016, Data Geekery GmbH (http://www.datageekery.com)
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

import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.keyword;
import static org.jooq.impl.DSL.sql;

import java.sql.Date;

import org.jooq.Configuration;
import org.jooq.DatePart;
import org.jooq.Field;
import org.jooq.QueryPart;

/**
 * @author Lukas Eder
 */
final class DateAdd<T extends java.util.Date> extends AbstractFunction<T> {

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

                return DSL.field("{date_add}({0}, {interval} {1} {2})", getDataType(), date, interval, keyword(keyword));
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

                return DSL.field("{fn {timestampadd}({0}, {1}, {2}) }", getDataType(), keyword(keyword), interval, date);
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

                return DSL.field("{dateadd}({0}, {1}, {2})", getDataType(), keyword(keyword), interval, date);
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

                return DSL.field("{dateadd}({0}, {1}, {2})", getDataType(), inline(keyword), interval, date);
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
                    return DSL.field("({0} + ({1} || {2})::interval)::date", getDataType(), date, interval, inline(keyword));
                else
                    return DSL.field("({0} + ({1} || {2})::interval)", getDataType(), date, interval, inline(keyword));
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

                return DSL.field("{datetime}({0}, '+' || {1} || {2})", getDataType(), date, interval, inline(keyword));
            }












































































































        }

        return null;
    }

    private final void throwUnsupported() {
        throw new UnsupportedOperationException("Unknown date part : " + datePart);
    }
}
