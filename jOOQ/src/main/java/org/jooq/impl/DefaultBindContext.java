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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.Collection;

import org.jooq.ArrayRecord;
import org.jooq.BindContext;
import org.jooq.Configuration;
import org.jooq.EnumType;
import org.jooq.MasterDataType;
import org.jooq.QueryPart;
import org.jooq.QueryPartInternal;
import org.jooq.SQLDialect;
import org.jooq.SQLDialectNotSupportedException;

/**
 * @author Lukas Eder
 */
class DefaultBindContext extends AbstractContext<BindContext> implements BindContext {

    /**
     * Generated UID
     */
    private static final long       serialVersionUID = -5457385919209241505L;
    private static final JooqLogger log              = JooqLogger.getLogger(JooqUtil.class);

    private final PreparedStatement stmt;
    private int                     index;

    DefaultBindContext(Configuration configuration, PreparedStatement stmt) {
        super(configuration);

        this.stmt = stmt;
    }

    DefaultBindContext(BindContext context) {
        this(context, context.statement());

        declareFields(context.declareFields());
        declareTables(context.declareTables());
    }

    @Override
    public final PreparedStatement statement() {
        return stmt;
    }

    @Override
    public final int nextIndex() {
        return ++index;
    }

    @Override
    public final int peekIndex() {
        return index + 1;
    }

    @Override
    public final BindContext bind(QueryPart part) throws SQLException {
        QueryPartInternal internal = part.internalAPI(QueryPartInternal.class);

        // If this is supposed to be a declaration section and the part isn't
        // able to declare anything, then disable declaration temporarily

        // We're declaring fields, but "part" does not declare fields
        if (declareFields() && !internal.declaresFields()) {
            declareFields(false);
            internal.bind(this);
            declareFields(true);
        }

        // We're declaring tables, but "part" does not declare tables
        else if (declareTables() && !internal.declaresTables()) {
            declareTables(false);
            internal.bind(this);
            declareTables(true);
        }

        // We're not declaring, or "part" can declare
        else {
            internal.bind(this);
        }

        return this;
    }

    @Override
    public final BindContext bind(Collection<? extends QueryPart> parts) throws SQLException {
        for (QueryPart part : parts) {
            bind(part);
        }

        return this;
    }

    @Override
    public final BindContext bind(QueryPart[] parts) throws SQLException {
        bind(Arrays.asList(parts));
        return this;
    }

    @Override
    public final BindContext bindValues(Object... values) throws SQLException {

        // [#724] When values is null, this is probably due to API-misuse
        // The user probably meant new Object[] { null }
        if (values == null) {
            bindValues(new Object[] { null });
        }
        else {
            for (Object value : values) {
                Class<?> type = (value == null) ? Object.class : value.getClass();
                bindValue(value, type);
            }
        }

        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final BindContext bindValue(Object value, Class<?> type) throws SQLException {
        SQLDialect dialect = configuration.getDialect();

        if (log.isTraceEnabled()) {
            if (value != null && value.getClass().isArray() && value.getClass() != byte[].class) {
                log.trace("Binding variable " + peekIndex(), Arrays.asList((Object[]) value) + " (" + type + ")");
            }
            else {
                log.trace("Binding variable " + peekIndex(), value + " (" + type + ")");
            }
        }

        // Setting null onto a prepared statement is subtly different for every
        // SQL dialect. See the following section for details
        if (value == null) {
            int sqlType = FieldTypeHelper.getDataType(dialect, type).getSQLType();

            // Treat Oracle-style ARRAY types specially
            if (ArrayRecord.class.isAssignableFrom(type)) {
                String typeName = JooqUtil.newArrayRecord((Class<ArrayRecord<?>>) type, configuration).getName();
                stmt.setNull(nextIndex(), sqlType, typeName);
            }

            // All other types can be set to null if the JDBC type is known
            else if (sqlType != Types.OTHER) {
                stmt.setNull(nextIndex(), sqlType);
            }

            // [#725] For SQL Server, unknown types should be set to null
            // explicitly, too
            else if (configuration.getDialect() == SQLDialect.SQLSERVER) {
                stmt.setNull(nextIndex(), sqlType);
            }

            // [#730] For Sybase, unknown types can be set to null using varchar
            else if (configuration.getDialect() == SQLDialect.SYBASE) {
                stmt.setNull(nextIndex(), Types.VARCHAR);
            }

            // [#729] In the absence of the correct JDBC type, try setObject
            else {
                stmt.setObject(nextIndex(), null);
            }
        }
        else if (type == Blob.class) {
            stmt.setBlob(nextIndex(), (Blob) value);
        }
        else if (type == Boolean.class) {
            stmt.setBoolean(nextIndex(), (Boolean) value);
        }
        else if (type == BigDecimal.class) {
            if (dialect == SQLDialect.SQLITE) {
                stmt.setString(nextIndex(), value.toString());
            }
            else {
                stmt.setBigDecimal(nextIndex(), (BigDecimal) value);
            }
        }
        else if (type == BigInteger.class) {
            if (dialect == SQLDialect.SQLITE) {
                stmt.setString(nextIndex(), value.toString());
            }
            else {
                stmt.setBigDecimal(nextIndex(), new BigDecimal((BigInteger) value));
            }
        }
        else if (type == Byte.class) {
            stmt.setByte(nextIndex(), (Byte) value);
        }
        else if (type == byte[].class) {
            stmt.setBytes(nextIndex(), (byte[]) value);
        }
        else if (type == Clob.class) {
            stmt.setClob(nextIndex(), (Clob) value);
        }
        else if (type == Date.class) {
            stmt.setDate(nextIndex(), (Date) value);
        }
        else if (type == Double.class) {
            stmt.setDouble(nextIndex(), (Double) value);
        }
        else if (type == Float.class) {
            stmt.setFloat(nextIndex(), (Float) value);
        }
        else if (type == Integer.class) {
            stmt.setInt(nextIndex(), (Integer) value);
        }
        else if (type == Long.class) {
            stmt.setLong(nextIndex(), (Long) value);
        }
        else if (type == Short.class) {
            stmt.setShort(nextIndex(), (Short) value);
        }
        else if (type == String.class) {
            stmt.setString(nextIndex(), (String) value);
        }
        else if (type == Time.class) {
            stmt.setTime(nextIndex(), (Time) value);
        }
        else if (type == Timestamp.class) {
            stmt.setTimestamp(nextIndex(), (Timestamp) value);
        }

        // The type byte[] is handled earlier. byte[][] can be handled here
        else if (type.isArray()) {
            switch (dialect) {
                case POSTGRES: {
                    StringBuilder sb = new StringBuilder();
                    sb.append("{");

                    String separator = "";
                    for (Object o : (Object[]) value) {
                        sb.append(separator);

                        // [#753] null must be set as a literal
                        if (o == null) {
                            sb.append(o);
                        }
                        else {
                            sb.append("\"");
                            sb.append(o.toString().replaceAll("\"", "\"\""));
                            sb.append("\"");
                        }

                        separator = ", ";
                    }

                    sb.append("}");
                    stmt.setString(nextIndex(), sb.toString());
                    break;
                }
                case HSQLDB:
                    stmt.setArray(nextIndex(), new DefaultArray(dialect, (Object[]) value, type));
                    break;
                case H2:
                    stmt.setObject(nextIndex(), value);
                    break;
                default:
                    throw new SQLDialectNotSupportedException("Cannot bind ARRAY types in dialect " + dialect);
            }
        }
        else if (ArrayRecord.class.isAssignableFrom(type)) {
            stmt.setArray(nextIndex(), ((ArrayRecord<?>) value).createArray());
        }
        else if (EnumType.class.isAssignableFrom(type)) {
            stmt.setString(nextIndex(), ((EnumType) value).getLiteral());
        }
        else if (MasterDataType.class.isAssignableFrom(type)) {
            Object primaryKey = ((MasterDataType<?>) value).getPrimaryKey();
            bindValue(primaryKey, primaryKey.getClass());
        }
        else {
            stmt.setObject(nextIndex(), value);
        }

        return this;
    }

    // ------------------------------------------------------------------------
    // Object API
    // ------------------------------------------------------------------------

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("binding   [index ");
        sb.append(index);
        sb.append("]");

        toString(sb);
        return sb.toString();
    }
}
