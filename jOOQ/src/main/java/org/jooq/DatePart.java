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

package org.jooq;

// ...
// ...
// ...
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
// ...
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...

import java.time.temporal.ChronoField;

import org.jooq.impl.DSL;

/**
 * A date part can be used with SQL functions such as extract(). It describes a
 * part of a date / datetime value
 *
 * @author Lukas Eder
 */
public enum DatePart {

    // ------------------------------------------------------------------------
    // XXX: SQL standard date parts
    // ------------------------------------------------------------------------

    /**
     * The year. Corresponds to {@link ChronoField#YEAR} semantics.
     */
    @Support
    YEAR("year"),

    /**
     * The month. Corresponds to {@link ChronoField#MONTH_OF_YEAR} semantics.
     */
    @Support
    MONTH("month"),

    /**
     * The day. Corresponds to {@link ChronoField#DAY_OF_MONTH} semantics.
     */
    @Support
    DAY("day"),

    /**
     * The hour. Corresponds to {@link ChronoField#HOUR_OF_DAY} semantics.
     */
    @Support
    HOUR("hour"),

    /**
     * The minute. Corresponds to {@link ChronoField#MINUTE_OF_HOUR} semantics.
     */
    @Support
    MINUTE("minute"),

    /**
     * The second. Corresponds to {@link ChronoField#SECOND_OF_MINUTE} semantics.
     */
    @Support
    SECOND("second"),

    // ------------------------------------------------------------------------
    // XXX: Vendor-specific date parts
    // ------------------------------------------------------------------------





















    /**
     * The millennium. The year 2000 is in the 2nd millennium, the year 2001 in
     * the 3rd.
     */
    @Support
    MILLENNIUM("millennium"),

    /**
     * The century. The year 2000 is in the 20th century, the year 2001 in the
     * 21st.
     */
    @Support
    CENTURY("century"),

    /**
     * The decade. The year divided by 10.
     */
    @Support
    DECADE("decade"),

    /**
     * The epoch in seconds since 1970-01-01.
     */
    @Support({ H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    EPOCH("epoch"),

    /**
     * The quarter. Jan-Mar = 1, Apr-Jun = 2, Jul-Sep = 3, Oct-Dec = 4.
     */
    @Support
    QUARTER("quarter"),

    /**
     * The week of the year.
     */
    @Support({ H2, HSQLDB, POSTGRES })
    WEEK("week"),

    /**
     * The day of the year. Corresponds to {@link ChronoField#DAY_OF_YEAR}.
     */
    @Support({ H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    DAY_OF_YEAR("day_of_year"),

    /**
     * The day of the week. 1 = Sunday, 2 = Monday, ..., 7 = Saturday.
     * Corresponds to {@link ChronoField#DAY_OF_WEEK}, shifted by one day.
     */
    @Support({ H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    DAY_OF_WEEK("day_of_week"),

    /**
     * The ISO day of the week. 1 = Monday, 2 = Tuesday, ..., 7 = Sunday.
     * Corresponds to {@link ChronoField#DAY_OF_WEEK}.
     */
    @Support({ H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    ISO_DAY_OF_WEEK("iso_day_of_week"),

    /**
     * The timezone offset in seconds. Corresponds to
     * {@link ChronoField#OFFSET_SECONDS}.
     */
    @Support({ H2, HSQLDB, POSTGRES })
    TIMEZONE("timezone"),

    /**
     * The time zone offset's hour part.
     */
    @Support({ H2, HSQLDB, POSTGRES })
    TIMEZONE_HOUR("timezone_hour"),

    /**
     * The time zone offset's minute part.
     */
    @Support({ H2, HSQLDB, POSTGRES })
    TIMEZONE_MINUTE("timezone_minute"),

    ;

    private final String  sql;
    private final Keyword keyword;

    private DatePart(String sql) {
        this.sql = sql;
        this.keyword = DSL.keyword(sql);
    }

    public final String toSQL() {
        return sql;
    }

    public final Keyword toKeyword() {
        return keyword;
    }
}
