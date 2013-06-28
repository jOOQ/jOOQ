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

import org.jooq.Configuration;
import org.jooq.DatePart;
import org.jooq.Field;
import org.jooq.exception.SQLDialectNotSupportedException;

/**
 * @author Lukas Eder
 */
class Extract extends AbstractFunction<Integer> {

    private static final long serialVersionUID = 3748640920856031034L;

    private final Field<?>    field;
    private final DatePart    datePart;

    Extract(Field<?> field, DatePart datePart) {
        super("extract", SQLDataType.INTEGER, field);

        this.field = field;
        this.datePart = datePart;
    }

    @Override
    final Field<Integer> getFunction0(Configuration configuration) {
        switch (configuration.dialect().family()) {
            case SQLITE:
                switch (datePart) {
                    case YEAR:
                        return field("{strftime}('%Y', {0})", SQLDataType.INTEGER, field);
                    case MONTH:
                        return field("{strftime}('%m', {0})", SQLDataType.INTEGER, field);
                    case DAY:
                        return field("{strftime}('%d', {0})", SQLDataType.INTEGER, field);
                    case HOUR:
                        return field("{strftime}('%H', {0})", SQLDataType.INTEGER, field);
                    case MINUTE:
                        return field("{strftime}('%M', {0})", SQLDataType.INTEGER, field);
                    case SECOND:
                        return field("{strftime}('%S', {0})", SQLDataType.INTEGER, field);
                    default:
                        throw new SQLDialectNotSupportedException("DatePart not supported: " + datePart);
                }

            case DERBY:
            case DB2:
                switch (datePart) {
                    case YEAR:
                        return function("year", SQLDataType.INTEGER, field);
                    case MONTH:
                        return function("month", SQLDataType.INTEGER, field);
                    case DAY:
                        return function("day", SQLDataType.INTEGER, field);
                    case HOUR:
                        return function("hour", SQLDataType.INTEGER, field);
                    case MINUTE:
                        return function("minute", SQLDataType.INTEGER, field);
                    case SECOND:
                        return function("second", SQLDataType.INTEGER, field);
                    default:
                        throw new SQLDialectNotSupportedException("DatePart not supported: " + datePart);
                }

            case ORACLE:
                switch (datePart) {
                    case YEAR:
                        return field("{to_char}({0}, 'YYYY')", SQLDataType.INTEGER, field);
                    case MONTH:
                        return field("{to_char}({0}, 'MM')", SQLDataType.INTEGER, field);
                    case DAY:
                        return field("{to_char}({0}, 'DD')", SQLDataType.INTEGER, field);
                    case HOUR:
                        return field("{to_char}({0}, 'HH24')", SQLDataType.INTEGER, field);
                    case MINUTE:
                        return field("{to_char}({0}, 'MI')", SQLDataType.INTEGER, field);
                    case SECOND:
                        return field("{to_char}({0}, 'SS')", SQLDataType.INTEGER, field);
                    default:
                        throw new SQLDialectNotSupportedException("DatePart not supported: " + datePart);
                }

            case ASE:
            case SQLSERVER:
            case SYBASE:
                switch (datePart) {
                    case YEAR:
                        return field("{datepart}(yy, {0})", SQLDataType.INTEGER, field);
                    case MONTH:
                        return field("{datepart}(mm, {0})", SQLDataType.INTEGER, field);
                    case DAY:
                        return field("{datepart}(dd, {0})", SQLDataType.INTEGER, field);
                    case HOUR:
                        return field("{datepart}(hh, {0})", SQLDataType.INTEGER, field);
                    case MINUTE:
                        return field("{datepart}(mi, {0})", SQLDataType.INTEGER, field);
                    case SECOND:
                        return field("{datepart}(ss, {0})", SQLDataType.INTEGER, field);
                    default:
                        throw new SQLDialectNotSupportedException("DatePart not supported: " + datePart);
                }

            case INGRES:
            case MARIADB:
            case MYSQL:
            case POSTGRES:
            case HSQLDB:
            case H2:

            // A default implementation is necessary for hashCode() and toString()
            default:
                return field("{extract}({" + datePart.toSQL() + " from} {0})", SQLDataType.INTEGER, field);
        }
    }
}
