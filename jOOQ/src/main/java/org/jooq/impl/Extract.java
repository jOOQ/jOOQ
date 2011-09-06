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

import java.sql.SQLException;
import java.util.List;

import org.jooq.Attachable;
import org.jooq.BindContext;
import org.jooq.Configuration;
import org.jooq.DatePart;
import org.jooq.Field;
import org.jooq.RenderContext;
import org.jooq.SQLDialectNotSupportedException;

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
            case INGRES:   // No break
            case MYSQL:    // No break
            case POSTGRES: // No break
            case HSQLDB:   // No break
            case H2:
                return new SQLExtract();

            case SQLITE:
                switch (datePart) {
                    case YEAR:
                        return new Function<Integer>("strftime", SQLDataType.INTEGER, create(configuration).val("%Y"), field);
                    case MONTH:
                        return new Function<Integer>("strftime", SQLDataType.INTEGER, create(configuration).val("%m"), field);
                    case DAY:
                        return new Function<Integer>("strftime", SQLDataType.INTEGER, create(configuration).val("%d"), field);
                    case HOUR:
                        return new Function<Integer>("strftime", SQLDataType.INTEGER, create(configuration).val("%H"), field);
                    case MINUTE:
                        return new Function<Integer>("strftime", SQLDataType.INTEGER, create(configuration).val("%M"), field);
                    case SECOND:
                        return new Function<Integer>("strftime", SQLDataType.INTEGER, create(configuration).val("%S"), field);
                    default:
                        throw new SQLDialectNotSupportedException("DatePart not supported: " + datePart);
                }

            case DERBY:    // No break
            case DB2:
                switch (datePart) {
                    case YEAR:
                        return new Function<Integer>("year", SQLDataType.INTEGER, field);
                    case MONTH:
                        return new Function<Integer>("month", SQLDataType.INTEGER, field);
                    case DAY:
                        return new Function<Integer>("day", SQLDataType.INTEGER, field);
                    case HOUR:
                        return new Function<Integer>("hour", SQLDataType.INTEGER, field);
                    case MINUTE:
                        return new Function<Integer>("minute", SQLDataType.INTEGER, field);
                    case SECOND:
                        return new Function<Integer>("second", SQLDataType.INTEGER, field);
                    default:
                        throw new SQLDialectNotSupportedException("DatePart not supported: " + datePart);
                }

            case ORACLE:
                switch (datePart) {
                    case YEAR:
                        return new Function<Integer>("to_char", SQLDataType.INTEGER, field, create(configuration).val("YYYY"));
                    case MONTH:
                        return new Function<Integer>("to_char", SQLDataType.INTEGER, field, create(configuration).val("MM"));
                    case DAY:
                        return new Function<Integer>("to_char", SQLDataType.INTEGER, field, create(configuration).val("DD"));
                    case HOUR:
                        return new Function<Integer>("to_char", SQLDataType.INTEGER, field, create(configuration).val("HH24"));
                    case MINUTE:
                        return new Function<Integer>("to_char", SQLDataType.INTEGER, field, create(configuration).val("MI"));
                    case SECOND:
                        return new Function<Integer>("to_char", SQLDataType.INTEGER, field, create(configuration).val("SS"));
                    default:
                        throw new SQLDialectNotSupportedException("DatePart not supported: " + datePart);
                }

            case ASE:
            case SQLSERVER:
            case SYBASE:
                switch (datePart) {
                    case YEAR:
                        return new Function<Integer>("datepart", SQLDataType.INTEGER, create(configuration).field("yy"), field);
                    case MONTH:
                        return new Function<Integer>("datepart", SQLDataType.INTEGER, create(configuration).field("mm"), field);
                    case DAY:
                        return new Function<Integer>("datepart", SQLDataType.INTEGER, create(configuration).field("dd"), field);
                    case HOUR:
                        return new Function<Integer>("datepart", SQLDataType.INTEGER, create(configuration).field("hh"), field);
                    case MINUTE:
                        return new Function<Integer>("datepart", SQLDataType.INTEGER, create(configuration).field("mi"), field);
                    case SECOND:
                        return new Function<Integer>("datepart", SQLDataType.INTEGER, create(configuration).field("ss"), field);
                    default:
                        throw new SQLDialectNotSupportedException("DatePart not supported: " + datePart);
                }

            default:
                throw new SQLDialectNotSupportedException("extract not supported");
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
        public final void bind(BindContext context) throws SQLException {
            context.bind(field);
        }
    }
}
