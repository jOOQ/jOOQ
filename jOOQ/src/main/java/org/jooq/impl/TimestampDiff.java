/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
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

import static org.jooq.impl.Factory.field;
import static org.jooq.impl.Factory.function;
import static org.jooq.impl.Factory.literal;

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

    @Override
    final Field<DayToSecond> getFunction0(Configuration configuration) {
        switch (configuration.getDialect()) {

            // Sybase ASE's datediff incredibly overflows on 3 days' worth of
            // microseconds. That's why the days have to be leveled at first
            case ASE:
                Field<Double> days = function("datediff", SQLDataType.DOUBLE, literal("day"), timestamp2, timestamp1);
                Field<Double> milli = function("datediff", SQLDataType.DOUBLE, literal("ms"), timestamp2.add(days), timestamp1);
                return (Field) days.add(milli.div(literal(new DayToSecond(1).getTotalMilli())));

            // CUBRID's datetime operations operate on a millisecond level
            case CUBRID:
                return (Field) timestamp1.sub(timestamp2).div(literal(new DayToSecond(1).getTotalMilli()));

            // Fun with DB2 dates. Find some info here:
            // http://www.ibm.com/developerworks/data/library/techarticle/0211yip/0211yip3.html
            case DB2:
                return (Field) function("days", SQLDataType.INTEGER, timestamp1).sub(
                               function("days", SQLDataType.INTEGER, timestamp2)).add(
                               function("midnight_seconds", SQLDataType.INTEGER, timestamp1).sub(
                               function("midnight_seconds", SQLDataType.INTEGER, timestamp2)).div(literal(new DayToSecond(1).getTotalSeconds())));

            case DERBY:
                return (Field) field("{fn {timestampdiff}({sql_tsi_second}, {0}, {1}) }", SQLDataType.INTEGER, timestamp2, timestamp1).div(literal(new DayToSecond(1).getTotalSeconds()));

            case H2:
            case HSQLDB:
                return function("datediff", getDataType(), literal("'ms'"), timestamp2, timestamp1).div(literal(new DayToSecond(1).getTotalMilli()));

            // MySQL's datetime operations operate on a microsecond level
            case MYSQL:
                return function("timestampdiff", getDataType(), literal("microsecond"), timestamp2, timestamp1).div(literal(new DayToSecond(1).getTotalMicro()));

            case SQLSERVER:
            case SYBASE:
                return function("datediff", getDataType(), literal("ms"), timestamp2, timestamp1).div(literal(new DayToSecond(1).getTotalMilli()));

            case ORACLE:
            case POSTGRES:

             // TODO [#585] This cast shouldn't be necessary
            return timestamp1.sub(timestamp2).cast(DayToSecond.class);
        }

        return null;
    }
}
