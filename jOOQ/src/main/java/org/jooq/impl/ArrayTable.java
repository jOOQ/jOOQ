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
import java.util.Collections;
import java.util.List;

import org.jooq.Attachable;
import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.SQLDialectNotSupportedException;
import org.jooq.Table;
import org.jooq.util.h2.H2DataType;

/**
 * @author Lukas Eder
 */
class ArrayTable<R extends Record> extends AbstractTable<R> {

    /**
     * Generated UID
     */
    private static final long    serialVersionUID = 2380426377794577041L;

    private final Field<?>       array;
    private final FieldList      field;

    ArrayTable(Field<?> array) {
        super("array_table");

        Class<?> arrayType;
        if (array.getDataType().getType().isArray()) {
            arrayType = array.getDataType().getType().getComponentType();
        }
        else {
            // [#523] TODO use ArrayRecord meta data instead
            arrayType = Object.class;
        }

        this.array = array;
        this.field = new FieldList();
        this.field.add(create().field("COLUMN_VALUE", arrayType));
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Class<? extends R> getRecordType() {
        return (Class<? extends R>) RecordImpl.class;
    }

    @Override
    public final Table<R> as(String alias) {
        return new TableAlias<R>(this, alias);
    }

    @Override
    public final String toSQLReference(Configuration configuration, boolean inlineParameters) {
        StringBuilder sb = new StringBuilder();

        switch (configuration.getDialect()) {
            case ORACLE: {
                sb.append("table(");
                sb.append(internal(array).toSQLReference(configuration, inlineParameters));
                sb.append(")");
                break;
            }

            case H2: {
                sb.append("table(COLUMN_VALUE ");

                // If the array type is unknown (e.g. because it's returned from a stored function
                // Then the best choice for arbitrary types is varchar
                if (array.getDataType().getType() == Object[].class) {
                    sb.append(H2DataType.VARCHAR.getTypeName());
                }
                else {
                    sb.append(array.getDataType().getTypeName());
                }

                sb.append(" = ");
                sb.append(internal(array).toSQLReference(configuration, inlineParameters));
                sb.append(")");
                break;
            }

            case HSQLDB:
            case POSTGRES: {

                // [#756] TODO: Handle aliasing correctly
                sb.append("unnest(");
                sb.append(internal(array).toSQLReference(configuration, inlineParameters));
                sb.append(") ARRAY_TABLE(COLUMN_VALUE)");

                break;
            }

            default:
                throw new SQLDialectNotSupportedException("ARRAY TABLE is not supported for " + configuration.getDialect());
        }

        return sb.toString();
    }

    @Override
    public final int bindReference(Configuration configuration, PreparedStatement stmt, int initialIndex)
        throws SQLException {

        switch (configuration.getDialect()) {
            case ORACLE:
            case H2:
            case HSQLDB:
            case POSTGRES:
                return internal(array).bindReference(configuration, stmt, initialIndex);

            default:
                break;
        }

        throw new SQLDialectNotSupportedException("ARRAY TABLE is not supported for " + configuration.getDialect());
    }

    @Override
    protected final FieldList getFieldList() {
        return field;
    }

    @Override
    protected final List<Attachable> getAttachables0() {
        return Collections.emptyList();
    }
}
