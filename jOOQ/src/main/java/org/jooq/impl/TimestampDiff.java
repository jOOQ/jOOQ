/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is triple-licensed under ASL 2.0, AGPL 3.0, and jOOQ EULA
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   ASL 2.0 or jOOQ EULA.
 * - If you're using this work with at least one commercial database, you may
 *   choose AGPL 3.0 or jOOQ EULA.
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
 * AGPL 3.0
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 *
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details: http://www.jooq.org/eula
 */
package org.jooq.impl;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.function;
import static org.jooq.impl.SQLDataType.INTEGER;

import java.sql.Timestamp;

import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.types.DayToSecond;

/**
 * @author Lukas Eder
 */
class TimestampDiff extends AbstractFunction<DayToSecond> {

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
        switch (configuration.dialect().family()) {

            // Sybase ASE's datediff incredibly overflows on 3 days' worth of
            // microseconds. That's why the days have to be leveled at first
            case ASE:

                // The difference in number of days
                Field<Integer> days = field("{datediff}(day, {0}, {1})", INTEGER, timestamp2, timestamp1);

                // The intra-day difference in number of milliseconds
                Field<Integer> milli = field("{datediff}(ms, {0}, {1})", INTEGER, timestamp2.add(days), timestamp1);
                return (Field) days.mul(86400000).add(milli);

            // CUBRID's datetime operations operate on a millisecond level
            case CUBRID:
                return (Field) timestamp1.sub(timestamp2);

            // Fun with DB2 dates. Find some info here:
            // http://www.ibm.com/developerworks/data/library/techarticle/0211yip/0211yip3.html
            case DB2:
                return (Field) function("days", INTEGER, timestamp1).sub(
                               function("days", INTEGER, timestamp2)).mul(86400000).add(
                               function("midnight_seconds", INTEGER, timestamp1).sub(
                               function("midnight_seconds", INTEGER, timestamp2)).mul(1000));

            case DERBY:
                return (Field) field("1000 * {fn {timestampdiff}({sql_tsi_second}, {0}, {1}) }", INTEGER, timestamp2, timestamp1);

            case FIREBIRD:
                return field("{datediff}(millisecond, {0}, {1})", getDataType(), timestamp2, timestamp1);

            case H2:
            case HSQLDB:
                return field("{datediff}('ms', {0}, {1})", getDataType(), timestamp2, timestamp1);

            // MySQL's datetime operations operate on a microsecond level
            case MARIADB:
            case MYSQL:
                return field("{timestampdiff}(microsecond, {0}, {1}) / 1000", getDataType(), timestamp2, timestamp1);

            case SQLSERVER:
            case SYBASE:
                return field("{datediff}(ms, {0}, {1})", getDataType(), timestamp2, timestamp1);

            case SQLITE:
                return field("({strftime}('%s', {0}) - {strftime}('%s', {1})) * 1000", getDataType(), timestamp1, timestamp2);

            case ORACLE:
            case POSTGRES:
                return field("{0} - {1}", getDataType(), timestamp1, timestamp2);

            // Fall through to default
            case INGRES:
        }

        // Default implementation for equals() and hashCode()
        return timestamp1.sub(timestamp2).cast(DayToSecond.class);
    }
}
