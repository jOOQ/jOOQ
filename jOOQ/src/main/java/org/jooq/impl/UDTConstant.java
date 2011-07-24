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

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.jooq.Attachable;
import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.SQLDialectNotSupportedException;
import org.jooq.UDT;
import org.jooq.UDTRecord;

/**
 * @author Lukas Eder
 */
class UDTConstant<R extends UDTRecord<R>> extends AbstractField<R> {

    private static final long  serialVersionUID = 6807729087019209084L;

    private final UDTRecord<?> record;

    UDTConstant(UDTRecord<R> value) {
        super(value.toString(), value.getUDT().getDataType());

        this.record = value;
    }

    @Override
    public final List<Attachable> getAttachables() {
        return getAttachables(record);
    }

    @Override
    public final String toSQLReference(Configuration configuration, boolean inlineParameters) {
        switch (configuration.getDialect()) {
            // Oracle supports java.sql.SQLData, hence the record can be bound
            // to the CallableStatement directly
            case ORACLE: {
                if (inlineParameters) {
                    return toSQLInline(configuration, inlineParameters);
                } else {
                    return "?";
                }
            }
            case POSTGRES: {
                return toSQLInline(configuration, inlineParameters);
            }
            case DB2: {

                // The subsequent DB2 logic should be refactored into toSQLInline()
                StringBuilder sb = new StringBuilder();
                sb.append(getInlineConstructor(configuration));
                sb.append("()");

                String separator = "..";
                for (Field<?> field : record.getFields()) {
                    Field<?> value = create(configuration).val(record.getValue(field));

                    sb.append(separator);
                    sb.append(field.getName()).append("(");
                    sb.append(internal(value).toSQLReference(configuration, inlineParameters));
                    sb.append(")");
                }

                return sb.toString();
            }
        }

        throw new SQLDialectNotSupportedException("UDTs not supported in dialect " + configuration.getDialect());
    }

    private String toSQLInline(Configuration configuration, boolean inlineParameters) {
        StringBuilder sb = new StringBuilder();
        sb.append(getInlineConstructor(configuration));
        sb.append("(");

        String separator = "";
        for (Field<?> field : record.getFields()) {
            Field<?> value = create(configuration).val(record.getValue(field));

            sb.append(separator);
            sb.append(internal(value).toSQLReference(configuration, inlineParameters));
            separator = ", ";
        }

        sb.append(")");
        return sb.toString();
    }

    private String getInlineConstructor(Configuration configuration) {
        switch (configuration.getDialect()) {
            case POSTGRES:
                return "ROW";

            case ORACLE: // No break
            case DB2: {
                UDT<?> udt = record.getUDT();

                if (getMappedSchema(configuration, udt.getSchema()) != null) {
                    return getMappedSchema(configuration, udt.getSchema()) + "." + udt.getName();
                }
                else {
                    return udt.getName();
                }
            }
        }
        throw new SQLDialectNotSupportedException("UDTs not supported in dialect " + configuration.getDialect());
    }

    @Override
    public int bindReference(Configuration configuration, PreparedStatement stmt, int initialIndex) throws SQLException {
        switch (configuration.getDialect()) {

            // Oracle supports java.sql.SQLData, hence the record can be bound
            // to the CallableStatement directly
            case ORACLE:
                return JooqUtil.bind(configuration, stmt, initialIndex, record);

            // Is the DB2 case correct? Should it be inlined like the Postgres case?
            case DB2:

            // Postgres cannot bind a complete structured type. The type is
            // inlined instead: ROW(.., .., ..)
            case POSTGRES: {
                int result = initialIndex;

                for (Field<?> field : record.getFields()) {
                    Field<?> value = create(configuration).val(record.getValue(field));
                    result = internal(value).bindReference(configuration, stmt, result);
                }

                return result;
            }
        }

        throw new SQLDialectNotSupportedException("UDTs not supported in dialect " + configuration.getDialect());
    }

    @Override
    public final boolean isNullLiteral() {
        return record == null;
    }
}
