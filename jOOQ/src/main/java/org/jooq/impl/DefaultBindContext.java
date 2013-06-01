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

import static java.util.Arrays.asList;
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.SQLITE;
import static org.jooq.SQLDialect.SQLSERVER;
import static org.jooq.SQLDialect.SYBASE;
import static org.jooq.impl.DefaultExecuteContext.localConnection;
import static org.jooq.tools.reflect.Reflect.on;
import static org.jooq.util.postgres.PostgresUtils.toPGArrayString;
import static org.jooq.util.postgres.PostgresUtils.toPGInterval;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.UUID;

import org.jooq.ArrayRecord;
import org.jooq.BindContext;
import org.jooq.Configuration;
import org.jooq.Converter;
import org.jooq.EnumType;
import org.jooq.SQLDialect;
import org.jooq.UDTRecord;
import org.jooq.exception.SQLDialectNotSupportedException;
import org.jooq.tools.Convert;
import org.jooq.tools.JooqLogger;
import org.jooq.types.DayToSecond;
import org.jooq.types.UByte;
import org.jooq.types.UInteger;
import org.jooq.types.ULong;
import org.jooq.types.UShort;
import org.jooq.types.YearToMonth;

/**
 * @author Lukas Eder
 */
class DefaultBindContext extends AbstractBindContext {

    private static final JooqLogger log              = JooqLogger.getLogger(DefaultBindContext.class);

    private final PreparedStatement stmt;

    DefaultBindContext(Configuration configuration, PreparedStatement stmt) {
        super(configuration);

        this.stmt = stmt;
    }

    @Override
    public final PreparedStatement statement() {
        return stmt;
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected final BindContext bindValue0(Object value, Class<?> type) throws SQLException {
        SQLDialect dialect = configuration.dialect();

        // [#650] Check first, if we have a converter for the supplied type
        Converter<?, ?> converter = DataTypes.converter(type);
        if (converter != null) {
            value = ((Converter) converter).to(value);
            type = converter.fromType();
        }

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
            int sqlType = DefaultDataType.getDataType(dialect, type).getSQLType();

            // Oracle-style ARRAY types need to be bound with their type name
            if (ArrayRecord.class.isAssignableFrom(type)) {
                String typeName = Utils.newArrayRecord((Class<ArrayRecord<?>>) type, configuration).getName();
                stmt.setNull(nextIndex(), sqlType, typeName);
            }

            // [#1126] Oracle's UDTs need to be bound with their type name
            else if (UDTRecord.class.isAssignableFrom(type)) {
                String typeName = Utils.newRecord((Class<UDTRecord<?>>) type).getUDT().getName();
                stmt.setNull(nextIndex(), sqlType, typeName);
            }

            // [#1225] [#1227] TODO Put this logic into DataType
            // Some dialects have trouble binding binary data as BLOB
            else if (asList(POSTGRES, SYBASE).contains(configuration.dialect()) && sqlType == Types.BLOB) {
                stmt.setNull(nextIndex(), Types.BINARY);
            }

            // All other types can be set to null if the JDBC type is known
            else if (sqlType != Types.OTHER) {
                stmt.setNull(nextIndex(), sqlType);
            }

            // [#725] For SQL Server, unknown types should be set to null
            // explicitly, too
            else if (configuration.dialect().family() == SQLSERVER) {
                stmt.setNull(nextIndex(), sqlType);
            }

            // [#730] For Sybase, unknown types can be set to null using varchar
            else if (configuration.dialect() == SYBASE) {
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
            if (dialect == SQLITE) {
                stmt.setString(nextIndex(), value.toString());
            }
            else {
                stmt.setBigDecimal(nextIndex(), (BigDecimal) value);
            }
        }
        else if (type == BigInteger.class) {
            if (dialect == SQLITE) {
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

        // There is potential for trouble when binding date time as such
        // -------------------------------------------------------------
        else if (type == Date.class) {
            if (dialect == SQLITE) {
                stmt.setString(nextIndex(), ((Date) value).toString());
            }
            else {
                stmt.setDate(nextIndex(), (Date) value);
            }
        }
        else if (type == Time.class) {
            if (dialect == SQLITE) {
                stmt.setString(nextIndex(), ((Time) value).toString());
            }
            else {
                stmt.setTime(nextIndex(), (Time) value);
            }
        }
        else if (type == Timestamp.class) {
            if (dialect == SQLITE) {
                stmt.setString(nextIndex(), ((Timestamp) value).toString());
            }
            else {
                stmt.setTimestamp(nextIndex(), (Timestamp) value);
            }
        }

        // [#566] Interval data types are best bound as Strings
        else if (type == YearToMonth.class) {
            if (dialect == POSTGRES) {
                stmt.setObject(nextIndex(), toPGInterval((YearToMonth) value));
            }
            else {
                stmt.setString(nextIndex(), value.toString());
            }
        }
        else if (type == DayToSecond.class) {
            if (dialect == POSTGRES) {
                stmt.setObject(nextIndex(), toPGInterval((DayToSecond) value));
            }
            else {
                stmt.setString(nextIndex(), value.toString());
            }
        }
        else if (type == UByte.class) {
            stmt.setShort(nextIndex(), ((UByte) value).shortValue());
        }
        else if (type == UShort.class) {
            stmt.setInt(nextIndex(), ((UShort) value).intValue());
        }
        else if (type == UInteger.class) {
            stmt.setLong(nextIndex(), ((UInteger) value).longValue());
        }
        else if (type == ULong.class) {
            stmt.setBigDecimal(nextIndex(), new BigDecimal(value.toString()));
        }
        else if (type == UUID.class) {
            switch (dialect.family()) {

                // [#1624] Some JDBC drivers natively support the
                // java.util.UUID data type
                case H2:
                case POSTGRES: {
                    stmt.setObject(nextIndex(), value);
                    break;
                }

                // Other SQL dialects deal with UUIDs as if they were CHAR(36)
                // even if they explicitly support them (UNIQUEIDENTIFIER)
                case SQLSERVER:
                case SYBASE:

                // Most databases don't have such a type. In this case, jOOQ
                // simulates the type
                default: {
                    stmt.setString(nextIndex(), value.toString());
                    break;
                }
            }
        }

        // The type byte[] is handled earlier. byte[][] can be handled here
        else if (type.isArray()) {
            switch (dialect) {
                case POSTGRES: {
                    stmt.setString(nextIndex(), toPGArrayString((Object[]) value));
                    break;
                }
                case HSQLDB: {
                    Object[] a = (Object[]) value;
                    Class<?> t = type;

                    // [#2325] Some array types are not natively supported by HSQLDB
                    // More integration tests are probably needed...
                    if (type == UUID[].class) {
                        a = Convert.convertArray(a, String[].class);
                        t = String[].class;
                    }

                    stmt.setArray(nextIndex(), new DefaultArray(dialect, a, t));
                    break;
                }
                case H2: {
                    stmt.setObject(nextIndex(), value);
                    break;
                }
                default:
                    throw new SQLDialectNotSupportedException("Cannot bind ARRAY types in dialect " + dialect);
            }
        }
        else if (ArrayRecord.class.isAssignableFrom(type)) {
            ArrayRecord<?> arrayRecord = (ArrayRecord<?>) value;
            stmt.setArray(nextIndex(), on(localConnection()).call("createARRAY", arrayRecord.getName(), arrayRecord.get()).<Array>get());
        }
        else if (EnumType.class.isAssignableFrom(type)) {
            stmt.setString(nextIndex(), ((EnumType) value).getLiteral());
        }
        else {
            stmt.setObject(nextIndex(), value);
        }

        return this;
    }
}
