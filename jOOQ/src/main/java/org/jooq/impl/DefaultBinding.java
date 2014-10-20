/**
 * Copyright (c) 2009-2014, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */
package org.jooq.impl;

import static java.util.Arrays.asList;
import static org.jooq.SQLDialect.ACCESS;
import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.SQLDialect.DB2;
import static org.jooq.SQLDialect.ORACLE;
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.SQLITE;
import static org.jooq.SQLDialect.SQLSERVER;
import static org.jooq.SQLDialect.SYBASE;
import static org.jooq.impl.DefaultExecuteContext.localTargetConnection;
import static org.jooq.tools.jdbc.JDBCUtils.safeFree;
import static org.jooq.tools.jdbc.JDBCUtils.wasNull;
import static org.jooq.tools.reflect.Reflect.on;
import static org.jooq.util.postgres.PostgresUtils.toPGArrayString;
import static org.jooq.util.postgres.PostgresUtils.toPGInterval;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import org.jooq.ArrayRecord;
import org.jooq.Binding;
import org.jooq.Configuration;
import org.jooq.Converter;
import org.jooq.EnumType;
import org.jooq.Field;
import org.jooq.RenderContext.CastMode;
import org.jooq.Result;
import org.jooq.Row;
import org.jooq.SQLDialect;
import org.jooq.UDTRecord;
import org.jooq.conf.ParamType;
import org.jooq.exception.SQLDialectNotSupportedException;
import org.jooq.tools.Convert;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.jdbc.JDBCUtils;
import org.jooq.tools.jdbc.MockArray;
import org.jooq.types.DayToSecond;
import org.jooq.types.UByte;
import org.jooq.types.UInteger;
import org.jooq.types.ULong;
import org.jooq.types.UNumber;
import org.jooq.types.UShort;
import org.jooq.types.YearToMonth;
import org.jooq.util.postgres.PostgresUtils;

/**
 * @author Lukas Eder
 */
public class DefaultBinding<T, U> implements Binding<U> {

    static final JooqLogger log = JooqLogger.getLogger(DefaultBinding.class);

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -198499389344950496L;

    final Class<T>            type;
    final Converter<T, U>     converter;

    @Deprecated
    // TODO: This type boolean should not be passed standalone to the
    // constructor. Find a better design
    final boolean             isLob;

    public DefaultBinding(Converter<T, U> converter, boolean isLob) {
        this.type = converter.fromType();
        this.converter = converter;
        this.isLob = isLob;
    }

    @Override
    public String sql(BindingContext ctx, U value, CastMode castMode, ParamType paramType) {
        return null;
    }

    @Override
    public void register(BindingContext ctx, CallableStatement stmt, int index) throws SQLException {
        Configuration configuration = ctx.configuration();
        int sqlType = DefaultDataType.getDataType(ctx.dialect(), type).getSQLType();

        switch (configuration.dialect().family()) {
            /* [pro] */

            // For some user defined types Oracle needs to bind
            // also the type name
            case ORACLE: {
                if (UDTRecord.class.isAssignableFrom(type)) {
                    UDTRecord<?> record = Utils
                        .newRecord(false, (Class<? extends UDTRecord<?>>) type)
                        .<RuntimeException>operate(null);
                    stmt.registerOutParameter(index, Types.STRUCT, record.getSQLTypeName());
                }

                else if (ArrayRecord.class.isAssignableFrom(type)) {
                    ArrayRecord<?> record = Utils.newArrayRecord((Class<? extends ArrayRecord<?>>) type);
                    stmt.registerOutParameter(index, Types.ARRAY, record.getName());
                }

                // The default behaviour is not to register a type
                // mapping
                else {
                    stmt.registerOutParameter(index, sqlType);
                }

                break;
            }

            /* [/pro] */
            default: {
                stmt.registerOutParameter(index, sqlType);
                break;
            }
        }
    }

    @Override
    public void set(BindingContext ctx, PreparedStatement stmt, int index, U object) throws SQLException {
        Configuration configuration = ctx.configuration();
        SQLDialect dialect = ctx.configuration().dialect();
        T value = converter.to(object);

        if (log.isTraceEnabled()) {
            if (value != null && value.getClass().isArray() && value.getClass() != byte[].class) {
                log.trace("Binding variable " + index, Arrays.asList((Object[]) value) + " (" + type + ")");
            }
            else {
                log.trace("Binding variable " + index, value + " (" + type + ")");
            }
        }

        // Setting null onto a prepared statement is subtly different for every
        // SQL dialect. See the following section for details
        if (value == null) {
            int sqlType = DefaultDataType.getDataType(dialect, type).getSQLType();

            /* [pro] */
            // Oracle-style ARRAY types need to be bound with their type name
            if (ArrayRecord.class.isAssignableFrom(type)) {
                String typeName = Utils.newArrayRecord((Class<ArrayRecord<?>>) type).getName();
                stmt.setNull(index, sqlType, typeName);
            }

            else
            /* [/pro] */
            // [#1126] Oracle's UDTs need to be bound with their type name
            if (UDTRecord.class.isAssignableFrom(type)) {
                String typeName = Utils.newRecord(false, (Class<UDTRecord<?>>) type)
                                       .<RuntimeException>operate(null)
                                       .getUDT()
                                       .getName();
                stmt.setNull(index, sqlType, typeName);
            }

            // [#1225] [#1227] TODO Put this logic into DataType
            // Some dialects have trouble binding binary data as BLOB
            else if (asList(POSTGRES, SYBASE).contains(configuration.dialect()) && sqlType == Types.BLOB) {
                stmt.setNull(index, Types.BINARY);
            }

            /* [pro] */
            else if (configuration.dialect().family() == ACCESS) {

                // This incredible mess is only needed with the Sun JDBC-ODBC bridge
                // Apparently, other drivers are better:
                // http://stackoverflow.com/a/19712785/521799
                switch (sqlType) {
                    case Types.BINARY:
                    case Types.VARBINARY:
                    case Types.LONGVARBINARY:
                    case Types.BLOB:
                        stmt.setNull(index, Types.VARCHAR);
                        break;

                    default:
                        stmt.setString(index, null);
                        break;
                }
            }

            // [#2152] [#3039] DB2 and Oracle cannot bind the type BOOLEAN. Let the JDBC driver
            // figure out the correct type.
            else if (sqlType == Types.BOOLEAN && asList(DB2, ORACLE).contains(configuration.dialect().family())) {
                stmt.setObject(index, null);
            }

            /* [/pro] */
            // All other types can be set to null if the JDBC type is known
            else if (sqlType != Types.OTHER) {
                stmt.setNull(index, sqlType);
            }

            /* [pro] */
            // [#725] For SQL Server, unknown types should be set to null
            // explicitly, too
            else if (configuration.dialect().family() == SQLSERVER) {
                stmt.setNull(index, sqlType);
            }

            // [#730] For Sybase, unknown types can be set to null using varchar
            else if (configuration.dialect() == SYBASE) {
                stmt.setNull(index, Types.VARCHAR);
            }

            /* [/pro] */
            // [#729] In the absence of the correct JDBC type, try setObject
            else {
                stmt.setObject(index, null);
            }
        }
        else {
            Class<?> actualType = type;

            // Try to infer the bind value type from the actual bind value if possible.
            if (actualType == Object.class) {
                actualType = value.getClass();
            }

            if (actualType == Blob.class) {
                stmt.setBlob(index, (Blob) value);
            }
            else if (actualType == Boolean.class) {
                /* [pro] */
                // MS Access treats "true" as "-1", which might be truncated in VARCHAR(1) or CHAR(1) columns
                if (dialect.family() == ACCESS)
                    stmt.setInt(index, (Boolean) value ? 1 : 0);
                else
                /* [/pro] */
                    stmt.setBoolean(index, (Boolean) value);
            }
            else if (actualType == BigDecimal.class) {
                if (asList(ACCESS, SQLITE).contains(dialect.family())) {
                    stmt.setString(index, value.toString());
                }
                else {
                    stmt.setBigDecimal(index, (BigDecimal) value);
                }
            }
            else if (actualType == BigInteger.class) {
                if (asList(ACCESS, SQLITE).contains(dialect.family())) {
                    stmt.setString(index, value.toString());
                }
                else {
                    stmt.setBigDecimal(index, new BigDecimal((BigInteger) value));
                }
            }
            else if (actualType == Byte.class) {
                stmt.setByte(index, (Byte) value);
            }
            else if (actualType == byte[].class) {
                stmt.setBytes(index, (byte[]) value);
            }
            else if (actualType == Clob.class) {
                stmt.setClob(index, (Clob) value);
            }
            else if (actualType == Double.class) {
                stmt.setDouble(index, (Double) value);
            }
            else if (actualType == Float.class) {
                stmt.setFloat(index, (Float) value);
            }
            else if (actualType == Integer.class) {
                stmt.setInt(index, (Integer) value);
            }
            else if (actualType == Long.class) {
                /* [pro] */
                if (dialect.family() == ACCESS)
                    stmt.setString(index, value.toString());
                else
                /* [/pro] */
                stmt.setLong(index, (Long) value);
            }
            else if (actualType == Short.class) {
                stmt.setShort(index, (Short) value);
            }
            else if (actualType == String.class) {
                stmt.setString(index, (String) value);
            }

            // There is potential for trouble when binding date time as such
            // -------------------------------------------------------------
            else if (actualType == Date.class) {
                if (dialect == SQLITE) {
                    stmt.setString(index, ((Date) value).toString());
                }
                else {
                    stmt.setDate(index, (Date) value);
                }
            }
            else if (actualType == Time.class) {
                if (dialect == SQLITE) {
                    stmt.setString(index, ((Time) value).toString());
                }
                else {
                    stmt.setTime(index, (Time) value);
                }
            }
            else if (actualType == Timestamp.class) {
                if (dialect == SQLITE) {
                    stmt.setString(index, ((Timestamp) value).toString());
                }
                else {
                    stmt.setTimestamp(index, (Timestamp) value);
                }
            }

            // [#566] Interval data types are best bound as Strings
            else if (actualType == YearToMonth.class) {
                if (dialect == POSTGRES) {
                    stmt.setObject(index, toPGInterval((YearToMonth) value));
                }
                else {
                    stmt.setString(index, value.toString());
                }
            }
            else if (actualType == DayToSecond.class) {
                if (dialect == POSTGRES) {
                    stmt.setObject(index, toPGInterval((DayToSecond) value));
                }
                else {
                    stmt.setString(index, value.toString());
                }
            }
            else if (actualType == UByte.class) {
                stmt.setShort(index, ((UByte) value).shortValue());
            }
            else if (actualType == UShort.class) {
                stmt.setInt(index, ((UShort) value).intValue());
            }
            else if (actualType == UInteger.class) {
                /* [pro] */
                if (dialect.family() == ACCESS)
                    stmt.setString(index, value.toString());
                else
                /* [/pro] */
                stmt.setLong(index, ((UInteger) value).longValue());
            }
            else if (actualType == ULong.class) {
                /* [pro] */
                if (dialect.family() == ACCESS)
                    stmt.setString(index, value.toString());
                else
                /* [/pro] */
                stmt.setBigDecimal(index, new BigDecimal(value.toString()));
            }
            else if (actualType == UUID.class) {
                switch (dialect.family()) {

                    // [#1624] Some JDBC drivers natively support the
                    // java.util.UUID data type
                    case H2:
                    case POSTGRES: {
                        stmt.setObject(index, value);
                        break;
                    }

                    /* [pro] */
                    // Other SQL dialects deal with UUIDs as if they were CHAR(36)
                    // even if they explicitly support them (UNIQUEIDENTIFIER)
                    case SQLSERVER:
                    case SYBASE:

                    /* [/pro] */
                    // Most databases don't have such a type. In this case, jOOQ
                    // simulates the type
                    default: {
                        stmt.setString(index, value.toString());
                        break;
                    }
                }
            }

            // The type byte[] is handled earlier. byte[][] can be handled here
            else if (actualType.isArray()) {
                switch (dialect) {
                    case POSTGRES: {
                        stmt.setString(index, toPGArrayString((Object[]) value));
                        break;
                    }
                    case HSQLDB: {
                        Object[] a = (Object[]) value;
                        Class<?> t = actualType;

                        // [#2325] Some array types are not natively supported by HSQLDB
                        // More integration tests are probably needed...
                        if (actualType == UUID[].class) {
                            a = Convert.convertArray(a, String[].class);
                            t = String[].class;
                        }

                        stmt.setArray(index, new MockArray(dialect, a, t));
                        break;
                    }
                    case H2: {
                        stmt.setObject(index, value);
                        break;
                    }
                    default:
                        throw new SQLDialectNotSupportedException("Cannot bind ARRAY types in dialect " + dialect);
                }
            }
            /* [pro] */
            else if (ArrayRecord.class.isAssignableFrom(actualType)) {
                ArrayRecord<?> arrayRecord = (ArrayRecord<?>) value;
                stmt.setArray(index, on(localTargetConnection()).call("createARRAY", arrayRecord.getName(), arrayRecord.get()).<Array>get());
            }
            /* [/pro] */
            else if (EnumType.class.isAssignableFrom(actualType)) {
                stmt.setString(index, ((EnumType) value).getLiteral());
            }
            else {
                stmt.setObject(index, value);
            }
        }
    }

    @Override
    public void set(BindingContext ctx, SQLOutput stream, U object) throws SQLException {
        T value = converter.to(object);

        if (value == null) {
            stream.writeObject(null);
        }
        else if (type == Blob.class) {
            stream.writeBlob((Blob) value);
        }
        else if (type == Boolean.class) {
            stream.writeBoolean((Boolean) value);
        }
        else if (type == BigInteger.class) {
            stream.writeBigDecimal(new BigDecimal((BigInteger) value));
        }
        else if (type == BigDecimal.class) {
            stream.writeBigDecimal((BigDecimal) value);
        }
        else if (type == Byte.class) {
            stream.writeByte((Byte) value);
        }
        else if (type == byte[].class) {

            // [#1327] Oracle cannot serialise BLOBs as byte[] to SQLOutput
            // Use reflection to avoid dependency on OJDBC
            if (isLob) {
                Blob blob = null;

                try {
                    blob = on("oracle.sql.BLOB").call("createTemporary",
                               on(stream).call("getSTRUCT")
                                         .call("getJavaSqlConnection").get(),
                               false,
                               on("oracle.sql.BLOB").get("DURATION_SESSION")).get();

                    blob.setBytes(1, (byte[]) value);
                    stream.writeBlob(blob);
                }
                finally {
                    DefaultExecuteContext.register(blob);
                }
            }
            else {
                stream.writeBytes((byte[]) value);
            }
        }
        else if (type == Clob.class) {
            stream.writeClob((Clob) value);
        }
        else if (type == Date.class) {
            stream.writeDate((Date) value);
        }
        else if (type == Double.class) {
            stream.writeDouble((Double) value);
        }
        else if (type == Float.class) {
            stream.writeFloat((Float) value);
        }
        else if (type == Integer.class) {
            stream.writeInt((Integer) value);
        }
        else if (type == Long.class) {
            stream.writeLong((Long) value);
        }
        else if (type == Short.class) {
            stream.writeShort((Short) value);
        }
        else if (type == String.class) {

            // [#1327] Oracle cannot serialise CLOBs as String to SQLOutput
            // Use reflection to avoid dependency on OJDBC
            if (isLob) {
                Clob clob = null;

                try {
                    clob = on("oracle.sql.CLOB").call("createTemporary",
                               on(stream).call("getSTRUCT")
                                         .call("getJavaSqlConnection").get(),
                               false,
                               on("oracle.sql.CLOB").get("DURATION_SESSION")).get();

                    clob.setString(1, (String) value);
                    stream.writeClob(clob);
                }
                finally {
                    DefaultExecuteContext.register(clob);
                }
            }
            else {
                stream.writeString((String) value);
            }
        }
        else if (type == Time.class) {
            stream.writeTime((Time) value);
        }
        else if (type == Timestamp.class) {
            stream.writeTimestamp((Timestamp) value);
        }
        else if (type == YearToMonth.class) {
            stream.writeString(value.toString());
        }
        else if (type == DayToSecond.class) {
            stream.writeString(value.toString());
        }
//        else if (type.isArray()) {
//            stream.writeArray(value);
//        }
        else if (UNumber.class.isAssignableFrom(type)) {
            stream.writeString(value.toString());
        }
        else if (type == UUID.class) {
            stream.writeString(value.toString());
        }
        /* [pro] */
        else if (ArrayRecord.class.isAssignableFrom(type)) {

            // [#1544] We can safely assume that localConfiguration has been
            // set on DefaultBindContext, prior to serialising arrays to SQLOut
            ArrayRecord<?> arrayRecord = (ArrayRecord<?>) value;
            Object[] array = arrayRecord.get();

            if (arrayRecord.getDataType() instanceof ConvertedDataType) {
                Object[] converted = new Object[array.length];

                for (int i = 0; i < converted.length; i++)
                    converted[i] = ((ConvertedDataType<Object, Object>) arrayRecord.getDataType()).converter().to(array[i]);

                array = converted;
            }
            stream.writeArray(on(localTargetConnection()).call("createARRAY", arrayRecord.getName(), array).<Array>get());
        }
        /* [/pro] */
        else if (EnumType.class.isAssignableFrom(type)) {
            stream.writeString(((EnumType) value).getLiteral());
        }
        else if (UDTRecord.class.isAssignableFrom(type)) {
            stream.writeObject((UDTRecord<?>) value);
        }
        else {
            throw new UnsupportedOperationException("Type " + type + " is not supported");
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public U get(BindingContext ctx, ResultSet rs, int index) throws SQLException {
        T result = null;

        if (type == Blob.class) {
            result = (T) rs.getBlob(index);
        }
        else if (type == Boolean.class) {
            result = (T) wasNull(rs, Boolean.valueOf(rs.getBoolean(index)));
        }
        else if (type == BigInteger.class) {
            // The SQLite JDBC driver doesn't support BigDecimals
            if (ctx.configuration().dialect() == SQLDialect.SQLITE) {
                result = Convert.convert(rs.getString(index), (Class<T>) BigInteger.class);
            }
            else {
                BigDecimal b = rs.getBigDecimal(index);
                result = (T) (b == null ? null : b.toBigInteger());
            }
        }
        else if (type == BigDecimal.class) {
            // The SQLite JDBC driver doesn't support BigDecimals
            if (ctx.configuration().dialect() == SQLDialect.SQLITE) {
                result = Convert.convert(rs.getString(index), (Class<T>) BigDecimal.class);
            }
            else {
                result = (T) rs.getBigDecimal(index);
            }
        }
        else if (type == Byte.class) {
            result = (T) wasNull(rs, Byte.valueOf(rs.getByte(index)));
        }
        else if (type == byte[].class) {
            result = (T) rs.getBytes(index);
        }
        else if (type == Clob.class) {
            result = (T) rs.getClob(index);
        }
        else if (type == Date.class) {
            result = (T) getDate(ctx.configuration().dialect(), rs, index);
        }
        else if (type == Double.class) {
            result = (T) wasNull(rs, Double.valueOf(rs.getDouble(index)));
        }
        else if (type == Float.class) {
            result = (T) wasNull(rs, Float.valueOf(rs.getFloat(index)));
        }
        else if (type == Integer.class) {
            result = (T) wasNull(rs, Integer.valueOf(rs.getInt(index)));
        }
        else if (type == Long.class) {
            result = (T) wasNull(rs, Long.valueOf(rs.getLong(index)));
        }
        else if (type == Short.class) {
            result = (T) wasNull(rs, Short.valueOf(rs.getShort(index)));
        }
        else if (type == String.class) {
            result = (T) rs.getString(index);
        }
        else if (type == Time.class) {
            result = (T) getTime(ctx.configuration().dialect(), rs, index);
        }
        else if (type == Timestamp.class) {
            result = (T) getTimestamp(ctx.configuration().dialect(), rs, index);
        }
        else if (type == YearToMonth.class) {
            if (ctx.configuration().dialect() == POSTGRES) {
                Object object = rs.getObject(index);
                result = (T) (object == null ? null : PostgresUtils.toYearToMonth(object));
            }
            else {
                String string = rs.getString(index);
                result = (T) (string == null ? null : YearToMonth.valueOf(string));
            }
        }
        else if (type == DayToSecond.class) {
            if (ctx.configuration().dialect() == POSTGRES) {
                Object object = rs.getObject(index);
                result = (T) (object == null ? null : PostgresUtils.toDayToSecond(object));
            }
            else {
                String string = rs.getString(index);
                result = (T) (string == null ? null : DayToSecond.valueOf(string));
            }
        }
        else if (type == UByte.class) {
            result = (T) Convert.convert(rs.getString(index), UByte.class);
        }
        else if (type == UShort.class) {
            result = (T) Convert.convert(rs.getString(index), UShort.class);
        }
        else if (type == UInteger.class) {
            result = (T) Convert.convert(rs.getString(index), UInteger.class);
        }
        else if (type == ULong.class) {
            result = (T) Convert.convert(rs.getString(index), ULong.class);
        }
        else if (type == UUID.class) {
            switch (ctx.configuration().dialect().family()) {

                // [#1624] Some JDBC drivers natively support the
                // java.util.UUID data type
                case H2:
                case POSTGRES: {
                    result = (T) rs.getObject(index);
                    break;
                }

                /* [pro] */
                // Other SQL dialects deal with UUIDs as if they were CHAR(36)
                // even if they explicitly support them (UNIQUEIDENTIFIER)
                case SQLSERVER:
                case SYBASE:

                /* [/pro] */
                // Most databases don't have such a type. In this case, jOOQ
                // simulates the type
                default: {
                    result = (T) Convert.convert(rs.getString(index), UUID.class);
                    break;
                }
            }
        }

        // The type byte[] is handled earlier. byte[][] can be handled here
        else if (type.isArray()) {
            switch (ctx.configuration().dialect()) {
                case POSTGRES: {
                    result = pgGetArray(ctx, rs, type, index);
                    break;
                }

                default:
                    // Note: due to a HSQLDB bug, it is not recommended to call rs.getObject() here:
                    // See https://sourceforge.net/tracker/?func=detail&aid=3181365&group_id=23316&atid=378131
                    result = (T) convertArray(rs.getArray(index), (Class<? extends Object[]>) type);
                    break;
            }
        }
        /* [pro] */
        else if (ArrayRecord.class.isAssignableFrom(type)) {
            result = (T) getArrayRecord(ctx.configuration(), rs.getArray(index), (Class<? extends ArrayRecord<?>>) type);
        }
        /* [/pro] */
        else if (EnumType.class.isAssignableFrom(type)) {
            result = getEnumType(type, rs.getString(index));
        }
        else if (UDTRecord.class.isAssignableFrom(type)) {
            switch (ctx.configuration().dialect()) {
                case POSTGRES:
                    result = (T) pgNewUDTRecord(type, rs.getObject(index));
                    break;

                default:
                    result = (T) rs.getObject(index, DataTypes.udtRecords());
                    break;
            }

        }
        else if (Result.class.isAssignableFrom(type)) {
            ResultSet nested = (ResultSet) rs.getObject(index);
            result = (T) DSL.using(ctx.configuration()).fetch(nested);
        }
        else {
            result = (T) unlob(rs.getObject(index));
        }

        return converter.from(result);
    }

    @SuppressWarnings("unchecked")
    @Override
    public U get(BindingContext ctx, CallableStatement stmt, int index) throws SQLException {
        T result = null;

        if (type == Blob.class) {
            result = (T) stmt.getBlob(index);
        }
        else if (type == Boolean.class) {
            result = (T) wasNull(stmt, Boolean.valueOf(stmt.getBoolean(index)));
        }
        else if (type == BigInteger.class) {
            BigDecimal d = stmt.getBigDecimal(index);
            result = (T) (d == null ? null : d.toBigInteger());
        }
        else if (type == BigDecimal.class) {
            result = (T) stmt.getBigDecimal(index);
        }
        else if (type == Byte.class) {
            result = (T) wasNull(stmt, Byte.valueOf(stmt.getByte(index)));
        }
        else if (type == byte[].class) {
            result = (T) stmt.getBytes(index);
        }
        else if (type == Clob.class) {
            result = (T) stmt.getClob(index);
        }
        else if (type == Date.class) {
            result = (T) stmt.getDate(index);
        }
        else if (type == Double.class) {
            result = (T) wasNull(stmt, Double.valueOf(stmt.getDouble(index)));
        }
        else if (type == Float.class) {
            result = (T) wasNull(stmt, Float.valueOf(stmt.getFloat(index)));
        }
        else if (type == Integer.class) {
            result = (T) wasNull(stmt, Integer.valueOf(stmt.getInt(index)));
        }
        else if (type == Long.class) {
            result = (T) wasNull(stmt, Long.valueOf(stmt.getLong(index)));
        }
        else if (type == Short.class) {
            result = (T) wasNull(stmt, Short.valueOf(stmt.getShort(index)));
        }
        else if (type == String.class) {
            result = (T) stmt.getString(index);
        }
        else if (type == Time.class) {
            result = (T) stmt.getTime(index);
        }
        else if (type == Timestamp.class) {
            result = (T) stmt.getTimestamp(index);
        }
        else if (type == YearToMonth.class) {
            if (ctx.configuration().dialect() == POSTGRES) {
                Object object = stmt.getObject(index);
                result = (T) (object == null ? null : PostgresUtils.toYearToMonth(object));
            }
            else {
                String string = stmt.getString(index);
                result = (T) (string == null ? null : YearToMonth.valueOf(string));
            }
        }
        else if (type == DayToSecond.class) {
            if (ctx.configuration().dialect() == POSTGRES) {
                Object object = stmt.getObject(index);
                result = (T) (object == null ? null : PostgresUtils.toDayToSecond(object));
            }
            else {
                String string = stmt.getString(index);
                result = (T) (string == null ? null : DayToSecond.valueOf(string));
            }
        }
        else if (type == UByte.class) {
            String string = stmt.getString(index);
            result = (T) (string == null ? null : UByte.valueOf(string));
        }
        else if (type == UShort.class) {
            String string = stmt.getString(index);
            result = (T) (string == null ? null : UShort.valueOf(string));
        }
        else if (type == UInteger.class) {
            String string = stmt.getString(index);
            result = (T) (string == null ? null : UInteger.valueOf(string));
        }
        else if (type == ULong.class) {
            String string = stmt.getString(index);
            result = (T) (string == null ? null : ULong.valueOf(string));
        }
        else if (type == UUID.class) {
            switch (ctx.configuration().dialect().family()) {

                // [#1624] Some JDBC drivers natively support the
                // java.util.UUID data type
                case H2:
                case POSTGRES: {
                    result = (T) stmt.getObject(index);
                    break;
                }

                /* [pro] */
                // Other SQL dialects deal with UUIDs as if they were CHAR(36)
                // even if they explicitly support them (UNIQUEIDENTIFIER)
                case SQLSERVER:
                case SYBASE:

                /* [/pro] */
                // Most databases don't have such a type. In this case, jOOQ
                // simulates the type
                default: {
                    result = (T) Convert.convert(stmt.getString(index), UUID.class);
                    break;
                }
            }
        }

        // The type byte[] is handled earlier. byte[][] can be handled here
        else if (type.isArray()) {
            result = (T) convertArray(stmt.getObject(index), (Class<? extends Object[]>)type);
        }
        /* [pro] */
        else if (ArrayRecord.class.isAssignableFrom(type)) {
            result = (T) getArrayRecord(ctx.configuration(), stmt.getArray(index), (Class<? extends ArrayRecord<?>>) type);
        }
        /* [/pro] */
        else if (EnumType.class.isAssignableFrom(type)) {
            result = getEnumType(type, stmt.getString(index));
        }
        else if (UDTRecord.class.isAssignableFrom(type)) {
            switch (ctx.configuration().dialect()) {
                case POSTGRES:
                    result = (T) pgNewUDTRecord(type, stmt.getObject(index));
                    break;

                default:
                    result = (T) stmt.getObject(index, DataTypes.udtRecords());
                    break;
            }
        }
        else if (Result.class.isAssignableFrom(type)) {
            ResultSet nested = (ResultSet) stmt.getObject(index);
            result = (T) DSL.using(ctx.configuration()).fetch(nested);
        }
        else {
            result = (T) stmt.getObject(index);
        }

        return converter.from(result);
    }

    @SuppressWarnings("unchecked")
    @Override
    public U get(BindingContext ctx, SQLInput stream) throws SQLException {
        T result = null;

        if (type == Blob.class) {
            result = (T) stream.readBlob();
        }
        else if (type == Boolean.class) {
            result = (T) wasNull(stream, Boolean.valueOf(stream.readBoolean()));
        }
        else if (type == BigInteger.class) {
            BigDecimal d = stream.readBigDecimal();
            result = (T) (d == null ? null : d.toBigInteger());
        }
        else if (type == BigDecimal.class) {
            result = (T) stream.readBigDecimal();
        }
        else if (type == Byte.class) {
            result = (T) wasNull(stream, Byte.valueOf(stream.readByte()));
        }
        else if (type == byte[].class) {

            // [#1327] Oracle cannot deserialise BLOBs as byte[] from SQLInput
            if (isLob) {
                Blob blob = null;
                try {
                    blob = stream.readBlob();
                    result = (T) (blob == null ? null : blob.getBytes(1, (int) blob.length()));
                }
                finally {
                    safeFree(blob);
                }
            }
            else {
                result = (T) stream.readBytes();
            }
        }
        else if (type == Clob.class) {
            result = (T) stream.readClob();
        }
        else if (type == Date.class) {
            result = (T) stream.readDate();
        }
        else if (type == Double.class) {
            result = (T) wasNull(stream, Double.valueOf(stream.readDouble()));
        }
        else if (type == Float.class) {
            result = (T) wasNull(stream, Float.valueOf(stream.readFloat()));
        }
        else if (type == Integer.class) {
            result = (T) wasNull(stream, Integer.valueOf(stream.readInt()));
        }
        else if (type == Long.class) {
            result = (T) wasNull(stream, Long.valueOf(stream.readLong()));
        }
        else if (type == Short.class) {
            result = (T) wasNull(stream, Short.valueOf(stream.readShort()));
        }
        else if (type == String.class) {
            result = (T) stream.readString();
        }
        else if (type == Time.class) {
            result = (T) stream.readTime();
        }
        else if (type == Timestamp.class) {
            result = (T) stream.readTimestamp();
        }
        else if (type == YearToMonth.class) {
            String string = stream.readString();
            result = (T) (string == null ? null : YearToMonth.valueOf(string));
        }
        else if (type == DayToSecond.class) {
            String string = stream.readString();
            result = (T) (string == null ? null : DayToSecond.valueOf(string));
        }
        else if (type == UByte.class) {
            String string = stream.readString();
            result = (T) (string == null ? null : UByte.valueOf(string));
        }
        else if (type == UShort.class) {
            String string = stream.readString();
            result = (T) (string == null ? null : UShort.valueOf(string));
        }
        else if (type == UInteger.class) {
            String string = stream.readString();
            result = (T) (string == null ? null : UInteger.valueOf(string));
        }
        else if (type == ULong.class) {
            String string = stream.readString();
            result = (T) (string == null ? null : ULong.valueOf(string));
        }
        else if (type == UUID.class) {
            result = (T) Convert.convert(stream.readString(), UUID.class);
        }

        // The type byte[] is handled earlier. byte[][] can be handled here
        else if (type.isArray()) {
            Array array = stream.readArray();
            result = (T) (array == null ? null : array.getArray());
        }
        /* [pro] */
        else if (ArrayRecord.class.isAssignableFrom(type)) {
            result = (T) getArrayRecord(ctx.configuration(), stream.readArray(), (Class<? extends ArrayRecord<?>>) type);
        }
        /* [/pro] */
        else if (EnumType.class.isAssignableFrom(type)) {
            result = getEnumType(type, stream.readString());
        }
        else if (UDTRecord.class.isAssignableFrom(type)) {
            result = (T) stream.readObject();
        }
        else {
            result = (T) unlob(stream.readObject());
        }

        return converter.from(result);
    }




    /* [pro] */
    private static final ArrayRecord<?> getArrayRecord(Configuration configuration, Array array, Class<? extends ArrayRecord<?>> type) throws SQLException {
        if (array == null) {
            return null;
        }
        else {
            // TODO: [#523] Use array record meta data instead
            return set(Utils.newArrayRecord(type), array);
        }
    }

    static final <T> ArrayRecord<T> set(ArrayRecord<T> target, Array source) throws SQLException {
        if (source == null) {
            target.set((T[]) null);
        }
        else {
            // [#1179 #1376 #1377] This is needed to load TABLE OF OBJECT
            // [#884] TODO: This name is used in inlined SQL. It should be
            // correctly escaped and schema mapped!
            Object[] array = (Object[]) source.getArray(DataTypes.udtRecords());
            T[] converted = (T[]) java.lang.reflect.Array.newInstance(target.getDataType().getType(), array.length);

            for (int i = 0; i < array.length; i++)
                converted[i] = target.getDataType().convert(array[i]);

            target.set(converted);
            // target.set(Convert.convert(o, target.getDataType().getArrayType()));
        }

        return target;
    }

    /* [/pro] */
    /**
     * [#2534] Extract <code>byte[]</code> or <code>String</code> data from a
     * LOB, if the argument is a lob.
     */
    private static Object unlob(Object object) throws SQLException {
        if (object instanceof Blob) {
            Blob blob = (Blob) object;

            try {
                return blob.getBytes(1, (int) blob.length());
            }
            finally {
                JDBCUtils.safeFree(blob);
            }
        }
        else if (object instanceof Clob) {
            Clob clob = (Clob) object;

            try {
                return clob.getSubString(1, (int) clob.length());
            }
            finally {
                JDBCUtils.safeFree(clob);
            }
        }

        return object;
    }

    @SuppressWarnings("unchecked")
    private static final <T> T getEnumType(Class<T> type, String literal) throws SQLException {
        try {
            Object[] list = (Object[]) type.getMethod("values").invoke(type);

            for (Object e : list) {
                String l = ((EnumType) e).getLiteral();

                if (l.equals(literal)) {
                    return (T) e;
                }
            }
        }
        catch (Exception e) {
            throw new SQLException("Unknown enum literal found : " + literal);
        }

        return null;
    }

    private static final Object[] convertArray(Object array, Class<? extends Object[]> type) throws SQLException {
        if (array instanceof Object[]) {
            return Convert.convert(array, type);
        }
        else if (array instanceof Array) {
            return convertArray((Array) array, type);
        }

        return null;
    }

    private static final Object[] convertArray(Array array, Class<? extends Object[]> type) throws SQLException {
        if (array != null) {
            return Convert.convert(array.getArray(), type);
        }

        return null;
    }

    private static final Date getDate(SQLDialect dialect, ResultSet rs, int index) throws SQLException {

        // SQLite's type affinity needs special care...
        if (dialect == SQLDialect.SQLITE) {
            String date = rs.getString(index);

            if (date != null) {
                return new Date(parse("yyyy-MM-dd", date));
            }

            return null;
        }

        // Cubrid SQL dates are incorrectly fetched. Reset milliseconds...
        // See http://jira.cubrid.org/browse/APIS-159
        // See https://sourceforge.net/apps/trac/cubridinterface/ticket/140
        else if (dialect == CUBRID) {
            Date date = rs.getDate(index);

            if (date != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(date.getTime());
                cal.set(Calendar.MILLISECOND, 0);
                date = new Date(cal.getTimeInMillis());
            }

            return date;
        }

        else {
            return rs.getDate(index);
        }
    }

    private static final Time getTime(SQLDialect dialect, ResultSet rs, int index) throws SQLException {

        // SQLite's type affinity needs special care...
        if (dialect == SQLDialect.SQLITE) {
            String time = rs.getString(index);

            if (time != null) {
                return new Time(parse("HH:mm:ss", time));
            }

            return null;
        }

        // Cubrid SQL dates are incorrectly fetched. Reset milliseconds...
        // See http://jira.cubrid.org/browse/APIS-159
        // See https://sourceforge.net/apps/trac/cubridinterface/ticket/140
        else if (dialect == CUBRID) {
            Time time = rs.getTime(index);

            if (time != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(time.getTime());
                cal.set(Calendar.MILLISECOND, 0);
                time = new Time(cal.getTimeInMillis());
            }

            return time;
        }

        else {
            return rs.getTime(index);
        }
    }

    private static final Timestamp getTimestamp(SQLDialect dialect, ResultSet rs, int index) throws SQLException {

        // SQLite's type affinity needs special care...
        if (dialect == SQLDialect.SQLITE) {
            String timestamp = rs.getString(index);

            if (timestamp != null) {
                return new Timestamp(parse("yyyy-MM-dd HH:mm:ss", timestamp));
            }

            return null;
        } else {
            return rs.getTimestamp(index);
        }
    }

    private static final long parse(String pattern, String date) throws SQLException {
        try {

            // Try reading a plain number first
            try {
                return Long.valueOf(date);
            }

            // If that fails, try reading a formatted date
            catch (NumberFormatException e) {
                return new SimpleDateFormat(pattern).parse(date).getTime();
            }
        }
        catch (ParseException e) {
            throw new SQLException("Could not parse date " + date, e);
        }
    }

    // -------------------------------------------------------------------------
    // XXX: The following section has been added for Postgres UDT support. The
    // official Postgres JDBC driver does not implement SQLData and similar
    // interfaces. Instead, a string representation of a UDT has to be parsed
    // -------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    private static final <T> T pgFromString(Class<T> type, String string) throws SQLException {
        if (string == null) {
            return null;
        }
        else if (type == Blob.class) {
            // Not supported
        }
        else if (type == Boolean.class) {
            return (T) Boolean.valueOf(string);
        }
        else if (type == BigInteger.class) {
            return (T) new BigInteger(string);
        }
        else if (type == BigDecimal.class) {
            return (T) new BigDecimal(string);
        }
        else if (type == Byte.class) {
            return (T) Byte.valueOf(string);
        }
        else if (type == byte[].class) {
            return (T) PostgresUtils.toBytes(string);
        }
        else if (type == Clob.class) {
            // Not supported
        }
        else if (type == Date.class) {
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
            return (T) new Date(pgParseDate(string, f).getTime());
        }
        else if (type == Double.class) {
            return (T) Double.valueOf(string);
        }
        else if (type == Float.class) {
            return (T) Float.valueOf(string);
        }
        else if (type == Integer.class) {
            return (T) Integer.valueOf(string);
        }
        else if (type == Long.class) {
            return (T) Long.valueOf(string);
        }
        else if (type == Short.class) {
            return (T) Short.valueOf(string);
        }
        else if (type == String.class) {
            return (T) string;
        }
        else if (type == Time.class) {
            SimpleDateFormat f = new SimpleDateFormat("HH:mm:ss");
            return (T) new Time(pgParseDate(string, f).getTime());
        }
        else if (type == Timestamp.class) {
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return (T) new Timestamp(pgParseDate(string, f).getTime());
        }
        else if (type == UByte.class) {
            return (T) UByte.valueOf(string);
        }
        else if (type == UShort.class) {
            return (T) UShort.valueOf(string);
        }
        else if (type == UInteger.class) {
            return (T) UInteger.valueOf(string);
        }
        else if (type == ULong.class) {
            return (T) ULong.valueOf(string);
        }
        else if (type == UUID.class) {
            return (T) UUID.fromString(string);
        }
        else if (type.isArray()) {
            return (T) pgNewArray(type, string);
        }
        /* [pro] */
        else if (ArrayRecord.class.isAssignableFrom(type)) {
            // Not supported
        }
        /* [/pro] */
        else if (EnumType.class.isAssignableFrom(type)) {
            return getEnumType(type, string);
        }
        else if (UDTRecord.class.isAssignableFrom(type)) {
            return (T) pgNewUDTRecord(type, string);
        }

        throw new UnsupportedOperationException("Class " + type + " is not supported");
    }

    private static final java.util.Date pgParseDate(String string, SimpleDateFormat f) throws SQLException {
        try {
            return f.parse(string);
        }
        catch (ParseException e) {
            throw new SQLException(e);
        }
    }

    /**
     * Create a UDT record from a PGobject
     * <p>
     * Unfortunately, this feature is very poorly documented and true UDT
     * support by the PostGreSQL JDBC driver has been postponed for a long time.
     *
     * @param object An object of type PGobject. The actual argument type cannot
     *            be expressed in the method signature, as no explicit
     *            dependency to postgres logic is desired
     * @return The converted {@link UDTRecord}
     */
    @SuppressWarnings("unchecked")
    private static final UDTRecord<?> pgNewUDTRecord(Class<?> type, final Object object) throws SQLException {
        if (object == null) {
            return null;
        }

        return Utils.newRecord(true, (Class<UDTRecord<?>>) type)
                    .operate(new RecordOperation<UDTRecord<?>, SQLException>() {

                @Override
                public UDTRecord<?> operate(UDTRecord<?> record) throws SQLException {
                    List<String> values = PostgresUtils.toPGObject(object.toString());

                    Row row = record.fieldsRow();
                    for (int i = 0; i < row.size(); i++) {
                        pgSetValue(record, row.field(i), values.get(i));
                    }

                    return record;
                }
            });
    }

    /**
     * Workarounds for the unimplemented Postgres JDBC driver features
     */
    @SuppressWarnings("unchecked")
    private static final <T> T pgGetArray(BindingContext ctx, ResultSet rs, Class<T> type, int index) throws SQLException {

        // Get the JDBC Array and check for null. If null, that's OK
        Array array = rs.getArray(index);
        if (array == null) {
            return null;
        }

        // Try fetching a Java Object[]. That's gonna work for non-UDT types
        try {
            return (T) convertArray(rs.getArray(index), (Class<? extends Object[]>) type);
        }

        // This might be a UDT (not implemented exception...)
        catch (Exception e) {
            List<Object> result = new ArrayList<Object>();

            // Try fetching the array as a JDBC ResultSet
            try {
                while (rs.next()) {
                    result.add(
                        new DefaultBinding<T, T>(new IdentityConverter<T>((Class<T>) type.getComponentType()), false).get(ctx, rs, 2)
                    );
                }
            }

            // That might fail too, then we don't know any further...
            catch (Exception fatal) {
                log.error("Cannot parse Postgres array: " + rs.getString(index));
                log.error(fatal);
                return null;
            }

            return (T) convertArray(result.toArray(), (Class<? extends Object[]>) type);
        }
    }

    /**
     * Create an array from a String
     * <p>
     * Unfortunately, this feature is very poorly documented and true UDT
     * support by the PostGreSQL JDBC driver has been postponed for a long time.
     *
     * @param string A String representation of an array
     * @return The converted array
     */
    private static final Object[] pgNewArray(Class<?> type, String string) throws SQLException {
        if (string == null) {
            return null;
        }

        try {
            Class<?> component = type.getComponentType();
            String values = string.replaceAll("^\\{(.*)\\}$", "$1");

            if ("".equals(values)) {
                return (Object[]) java.lang.reflect.Array.newInstance(component, 0);
            }
            else {
                String[] split = values.split(",");
                Object[] result = (Object[]) java.lang.reflect.Array.newInstance(component, split.length);

                for (int i = 0; i < split.length; i++) {
                    result[i] = pgFromString(type.getComponentType(), split[i]);
                }

                return result;
            }
        }
        catch (Exception e) {
            throw new SQLException(e);
        }
    }

    private static final <T> void pgSetValue(UDTRecord<?> record, Field<T> field, String value) throws SQLException {
        record.setValue(field, pgFromString(field.getType(), value));
    }
}

