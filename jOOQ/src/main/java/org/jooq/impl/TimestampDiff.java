/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.jooq.impl;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.function;
import static org.jooq.impl.SQLDataType.INTEGER;

import java.sql.Timestamp;

import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.exception.SQLDialectNotSupportedException;
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

            case INGRES:
                throw new SQLDialectNotSupportedException("Date time arithmetic not supported in Ingres. Contributions welcome!");

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
        }

        // Default implementation for equals() and hashCode()
        return timestamp1.sub(timestamp2).cast(DayToSecond.class);
    }
}
