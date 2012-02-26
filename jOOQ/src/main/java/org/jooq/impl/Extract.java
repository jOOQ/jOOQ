/**
 * Copyright (c) 2009-2011, Lukas Eder, lukas.eder@gmail.com
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
import static org.jooq.impl.Factory.val;

import java.util.List;

import org.jooq.Attachable;
import org.jooq.BindContext;
import org.jooq.Configuration;
import org.jooq.DatePart;
import org.jooq.Field;
import org.jooq.RenderContext;
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
        switch (configuration.getDialect()) {
            case SQLITE:
                switch (datePart) {
                    case YEAR:
                        return function("strftime", SQLDataType.INTEGER, val("%Y"), field);
                    case MONTH:
                        return function("strftime", SQLDataType.INTEGER, val("%m"), field);
                    case DAY:
                        return function("strftime", SQLDataType.INTEGER, val("%d"), field);
                    case HOUR:
                        return function("strftime", SQLDataType.INTEGER, val("%H"), field);
                    case MINUTE:
                        return function("strftime", SQLDataType.INTEGER, val("%M"), field);
                    case SECOND:
                        return function("strftime", SQLDataType.INTEGER, val("%S"), field);
                    default:
                        throw new SQLDialectNotSupportedException("DatePart not supported: " + datePart);
                }

            case DERBY:    // No break
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
                        return function("to_char", SQLDataType.INTEGER, field, val("YYYY"));
                    case MONTH:
                        return function("to_char", SQLDataType.INTEGER, field, val("MM"));
                    case DAY:
                        return function("to_char", SQLDataType.INTEGER, field, val("DD"));
                    case HOUR:
                        return function("to_char", SQLDataType.INTEGER, field, val("HH24"));
                    case MINUTE:
                        return function("to_char", SQLDataType.INTEGER, field, val("MI"));
                    case SECOND:
                        return function("to_char", SQLDataType.INTEGER, field, val("SS"));
                    default:
                        throw new SQLDialectNotSupportedException("DatePart not supported: " + datePart);
                }

            case ASE:
            case SQLSERVER:
            case SYBASE:
                switch (datePart) {
                    case YEAR:
                        return function("datepart", SQLDataType.INTEGER, field("yy"), field);
                    case MONTH:
                        return function("datepart", SQLDataType.INTEGER, field("mm"), field);
                    case DAY:
                        return function("datepart", SQLDataType.INTEGER, field("dd"), field);
                    case HOUR:
                        return function("datepart", SQLDataType.INTEGER, field("hh"), field);
                    case MINUTE:
                        return function("datepart", SQLDataType.INTEGER, field("mi"), field);
                    case SECOND:
                        return function("datepart", SQLDataType.INTEGER, field("ss"), field);
                    default:
                        throw new SQLDialectNotSupportedException("DatePart not supported: " + datePart);
                }

            case INGRES:   // No break
            case MYSQL:    // No break
            case POSTGRES: // No break
            case HSQLDB:   // No break
            case H2:

            // A default implementation is necessary for hashCode() and toString()
            default:
                return new SQLExtract();
        }
    }

    /**
     * The default implementation according to the SQL standard.
     *
     * @author Lukas Eder
     */
    private class SQLExtract extends AbstractField<Integer> {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = 5979696080332410352L;

        SQLExtract() {
            super("extract", SQLDataType.INTEGER);
        }

        @Override
        public final List<Attachable> getAttachables() {
            return getAttachables(Extract.this.getArguments());
        }

        @Override
        public final void toSQL(RenderContext context) {
            context.sql("extract(")
                   .sql(datePart.toSQL())
                   .sql(" from ")
                   .sql(field)
                   .sql(")");
        }

        @Override
        public final boolean isNullLiteral() {
            return false;
        }

        @Override
        public final void bind(BindContext context) {
            context.bind(field);
        }
    }
}
