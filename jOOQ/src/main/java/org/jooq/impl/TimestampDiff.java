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

import static org.jooq.impl.DSL.function;
import static org.jooq.impl.SQLDataType.INTEGER;

import java.sql.Timestamp;

import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.types.DayToSecond;

/**
 * @author Lukas Eder
 */
final class TimestampDiff extends AbstractFunction<DayToSecond> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -4813228000332771961L;

    private final Field<Timestamp> timestamp1;
    private final Field<Timestamp> timestamp2;

    TimestampDiff(Field<Timestamp> timestamp1, Field<Timestamp> timestamp2) {
        super("timestampdiff", SQLDataType.INTERVALDAYTOSECOND, timestamp1, timestamp2);

        this.timestamp1 = timestamp1;
        this.timestamp2 = timestamp2;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    final Field<DayToSecond> getFunction0(Configuration configuration) {
        switch (configuration.family()) {































            case POSTGRES:

                // [#4481] Parentheses are important in case this expression is
                //         placed in the context of other arithmetic
                return DSL.field("({0} - {1})", getDataType(), timestamp1, timestamp2);

            // CUBRID's datetime operations operate on a millisecond level
            case CUBRID:
                return (Field) timestamp1.sub(timestamp2);

            case DERBY:
                return (Field) DSL.field("1000 * {fn {timestampdiff}({sql_tsi_second}, {0}, {1}) }", INTEGER, timestamp2, timestamp1);





            case FIREBIRD:
                return DSL.field("{datediff}(millisecond, {0}, {1})", getDataType(), timestamp2, timestamp1);

            case H2:
            case HSQLDB:
                return DSL.field("{datediff}('ms', {0}, {1})", getDataType(), timestamp2, timestamp1);

            // MySQL's datetime operations operate on a microsecond level
            case MARIADB:
            case MYSQL:
                return DSL.field("{timestampdiff}(microsecond, {0}, {1}) / 1000", getDataType(), timestamp2, timestamp1);

            case SQLITE:
                return DSL.field("({strftime}('%s', {0}) - {strftime}('%s', {1})) * 1000", getDataType(), timestamp1, timestamp2);





        }

        // Default implementation for equals() and hashCode()
        return timestamp1.sub(timestamp2).cast(DayToSecond.class);
    }
}
