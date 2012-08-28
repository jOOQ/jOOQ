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

import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.impl.Factory.getNewFactory;
import static org.jooq.impl.Util.getDriverConnection;
import static org.jooq.tools.reflect.Reflect.on;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
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
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.jooq.ArrayRecord;
import org.jooq.Configuration;
import org.jooq.Converter;
import org.jooq.DataType;
import org.jooq.EnumType;
import org.jooq.ExecuteContext;
import org.jooq.Field;
import org.jooq.MasterDataType;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.UDTRecord;
import org.jooq.exception.SQLDialectNotSupportedException;
import org.jooq.tools.Convert;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.unsigned.UByte;
import org.jooq.tools.unsigned.UInteger;
import org.jooq.tools.unsigned.ULong;
import org.jooq.tools.unsigned.UNumber;
import org.jooq.tools.unsigned.UShort;
import org.jooq.types.DayToSecond;
import org.jooq.types.YearToMonth;
import org.jooq.util.ase.ASEDataType;
import org.jooq.util.cubrid.CUBRIDDataType;
import org.jooq.util.db2.DB2DataType;
import org.jooq.util.derby.DerbyDataType;
import org.jooq.util.h2.H2DataType;
import org.jooq.util.hsqldb.HSQLDBDataType;
import org.jooq.util.ingres.IngresDataType;
import org.jooq.util.mysql.MySQLDataType;
import org.jooq.util.oracle.OracleDataType;
import org.jooq.util.postgres.PostgresDataType;
import org.jooq.util.postgres.PostgresUtils;
import org.jooq.util.sqlite.SQLiteDataType;
import org.jooq.util.sqlserver.SQLServerDataType;
import org.jooq.util.sybase.SybaseDataType;

/**
 * Utility methods related to the treatment of fields and their types
 * <p>
 * This class is for JOOQ INTERNAL USE only. Do not reference directly
 *
 * @author Lukas Eder
 */
public final class FieldTypeHelper {

    private static final int        LONG_PRECISION    = String.valueOf(Long.MAX_VALUE).length();
    private static final int        INTEGER_PRECISION = String.valueOf(Integer.MAX_VALUE).length();
    private static final int        SHORT_PRECISION   = String.valueOf(Short.MAX_VALUE).length();
    private static final int        BYTE_PRECISION    = String.valueOf(Byte.MAX_VALUE).length();

    private static final JooqLogger log               = JooqLogger.getLogger(FieldTypeHelper.class);

    @SuppressWarnings("unchecked")
    public static <T> T getFromSQLInput(Configuration configuration, SQLInput stream, Field<T> field) throws SQLException {
        Class<? extends T> type = field.getType();
        DataType<T> dataType = field.getDataType();

        if (type == Blob.class) {
            return (T) stream.readBlob();
        }
        else if (type == Boolean.class) {
            return (T) checkWasNull(stream, Boolean.valueOf(stream.readBoolean()));
        }
        else if (type == BigInteger.class) {
            BigDecimal result = stream.readBigDecimal();
            return (T) (result == null ? null : result.toBigInteger());
        }
        else if (type == BigDecimal.class) {
            return (T) stream.readBigDecimal();
        }
        else if (type == Byte.class) {
            return (T) checkWasNull(stream, Byte.valueOf(stream.readByte()));
        }
        else if (type == byte[].class) {

            // [#1327] Oracle cannot deserialise BLOBs as byte[] from SQLInput
            if (dataType.isLob()) {
                Blob blob = null;
                try {
                    blob = stream.readBlob();
                    return (T) (blob == null ? null : blob.getBytes(1, (int) blob.length()));
                }
                finally {
                    Util.safeFree(blob);
                }
            }
            else {
                return (T) stream.readBytes();
            }
        }
        else if (type == Clob.class) {
            return (T) stream.readClob();
        }
        else if (type == Date.class) {
            return (T) stream.readDate();
        }
        else if (type == Double.class) {
            return (T) checkWasNull(stream, Double.valueOf(stream.readDouble()));
        }
        else if (type == Float.class) {
            return (T) checkWasNull(stream, Float.valueOf(stream.readFloat()));
        }
        else if (type == Integer.class) {
            return (T) checkWasNull(stream, Integer.valueOf(stream.readInt()));
        }
        else if (type == Long.class) {
            return (T) checkWasNull(stream, Long.valueOf(stream.readLong()));
        }
        else if (type == Short.class) {
            return (T) checkWasNull(stream, Short.valueOf(stream.readShort()));
        }
        else if (type == String.class) {
            return (T) stream.readString();
        }
        else if (type == Time.class) {
            return (T) stream.readTime();
        }
        else if (type == Timestamp.class) {
            return (T) stream.readTimestamp();
        }
        else if (type == YearToMonth.class) {
            String string = stream.readString();
            return (T) (string == null ? null : YearToMonth.valueOf(string));
        }
        else if (type == DayToSecond.class) {
            String string = stream.readString();
            return (T) (string == null ? null : DayToSecond.valueOf(string));
        }
        else if (type == UByte.class) {
            String string = stream.readString();
            return (T) (string == null ? null : UByte.valueOf(string));
        }
        else if (type == UShort.class) {
            String string = stream.readString();
            return (T) (string == null ? null : UShort.valueOf(string));
        }
        else if (type == UInteger.class) {
            String string = stream.readString();
            return (T) (string == null ? null : UInteger.valueOf(string));
        }
        else if (type == ULong.class) {
            String string = stream.readString();
            return (T) (string == null ? null : ULong.valueOf(string));
        }

        // The type byte[] is handled earlier. byte[][] can be handled here
        else if (type.isArray()) {
            Array result = stream.readArray();
            return (T) (result == null ? null : result.getArray());
        }
        else if (ArrayRecord.class.isAssignableFrom(type)) {
            return (T) getArrayRecord(configuration, stream.readArray(), (Class<? extends ArrayRecord<?>>) type);
        }
        else if (EnumType.class.isAssignableFrom(type)) {
            return getEnumType(type, stream.readString());
        }
        else if (MasterDataType.class.isAssignableFrom(type)) {
            return (T) getMasterDataType(type, stream.readObject());
        }
        else if (UDTRecord.class.isAssignableFrom(type)) {
            return (T) stream.readObject();
        }
        else {
            return (T) stream.readObject();
        }
    }

    public static <T> void writeToSQLOutput(SQLOutput stream, Field<T> field, T value) throws SQLException {
        Class<? extends T> type = field.getType();

        writeToSQLOutput(stream, type, field.getDataType(), value);
    }

    private static <T> void writeToSQLOutput(SQLOutput stream, Class<? extends T> type, DataType<T> dataType, T value) throws SQLException {
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
            if (dataType.isLob()) {
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
            if (dataType.isLob()) {
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
        else if (ArrayRecord.class.isAssignableFrom(type)) {

            // [#1544] We can safely assume that localConfiguration has been
            // set on DefaultBindContext, prior to serialising arrays to SQLOut
            Connection connection = getDriverConnection(DefaultBindContext.LOCAL_CONFIGURATION.get());
            ArrayRecord<?> arrayRecord = (ArrayRecord<?>) value;
            stream.writeArray(on(connection).call("createARRAY", arrayRecord.getName(), arrayRecord.get()).<Array>get());
        }
        else if (EnumType.class.isAssignableFrom(type)) {
            stream.writeString(((EnumType) value).getLiteral());
        }
        else if (MasterDataType.class.isAssignableFrom(type)) {
            Object key = ((MasterDataType<?>) value).getPrimaryKey();
            writeToSQLOutput(stream, key.getClass(), key);
        }
        else if (UDTRecord.class.isAssignableFrom(type)) {
            stream.writeObject((UDTRecord<?>) value);
        }
        else {
            throw new UnsupportedOperationException("Type " + type + " is not supported");
        }
    }

    /**
     * @deprecated - 2.3.0 - Do not reuse this method
     */
    @Deprecated
    public static <T> void writeToSQLOutput(SQLOutput stream, Class<? extends T> type, T value) throws SQLException {
        writeToSQLOutput(stream, type, null, value);
    }

    static <T, U> U getFromResultSet(ExecuteContext ctx, Field<U> field, int index)
        throws SQLException {

        @SuppressWarnings("unchecked")
        Converter<T, U> converter = (Converter<T, U>) DataTypes.converter(field.getType());

        if (converter != null) {
            return converter.from(getFromResultSet(ctx, converter.fromType(), index));
        }
        else {
            return getFromResultSet(ctx, field.getType(), index);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T getFromResultSet(ExecuteContext ctx, Class<? extends T> type, int index)
        throws SQLException {

        ResultSet rs = ctx.resultSet();

        if (type == Blob.class) {
            return (T) rs.getBlob(index);
        }
        else if (type == Boolean.class) {
            return (T) checkWasNull(rs, Boolean.valueOf(rs.getBoolean(index)));
        }
        else if (type == BigInteger.class) {
        	// The SQLite JDBC driver doesn't support BigDecimals
            if (ctx.getDialect() == SQLDialect.SQLITE) {
                return Convert.convert(rs.getString(index), (Class<? extends T>) BigInteger.class);
            }
            else {
                BigDecimal result = rs.getBigDecimal(index);
                return (T) (result == null ? null : result.toBigInteger());
            }
        }
        else if (type == BigDecimal.class) {
            // The SQLite JDBC driver doesn't support BigDecimals
            if (ctx.getDialect() == SQLDialect.SQLITE) {
                return Convert.convert(rs.getString(index), (Class<? extends T>) BigDecimal.class);
            }
            else {
                return (T) rs.getBigDecimal(index);
            }
        }
        else if (type == Byte.class) {
            return (T) checkWasNull(rs, Byte.valueOf(rs.getByte(index)));
        }
        else if (type == byte[].class) {
            return (T) rs.getBytes(index);
        }
        else if (type == Clob.class) {
            return (T) rs.getClob(index);
        }
        else if (type == Date.class) {
            return (T) getDate(ctx.getDialect(), rs, index);
        }
        else if (type == Double.class) {
            return (T) checkWasNull(rs, Double.valueOf(rs.getDouble(index)));
        }
        else if (type == Float.class) {
            return (T) checkWasNull(rs, Float.valueOf(rs.getFloat(index)));
        }
        else if (type == Integer.class) {
            return (T) checkWasNull(rs, Integer.valueOf(rs.getInt(index)));
        }
        else if (type == Long.class) {
            return (T) checkWasNull(rs, Long.valueOf(rs.getLong(index)));
        }
        else if (type == Short.class) {
            return (T) checkWasNull(rs, Short.valueOf(rs.getShort(index)));
        }
        else if (type == String.class) {
            return (T) rs.getString(index);
        }
        else if (type == Time.class) {
            return (T) getTime(ctx.getDialect(), rs, index);
        }
        else if (type == Timestamp.class) {
            return (T) getTimestamp(ctx.getDialect(), rs, index);
        }
        else if (type == YearToMonth.class) {
            if (ctx.getDialect() == POSTGRES) {
                Object object = rs.getObject(index);
                return (T) (object == null ? null : PostgresUtils.toYearToMonth(object));
            }
            else {
                String string = rs.getString(index);
                return (T) (string == null ? null : YearToMonth.valueOf(string));
            }
        }
        else if (type == DayToSecond.class) {
            if (ctx.getDialect() == POSTGRES) {
                Object object = rs.getObject(index);
                return (T) (object == null ? null : PostgresUtils.toDayToSecond(object));
            }
            else {
                String string = rs.getString(index);
                return (T) (string == null ? null : DayToSecond.valueOf(string));
            }
        }
        else if (type == UByte.class) {
            String string = rs.getString(index);
            return (T) (string == null ? null : UByte.valueOf(string));
        }
        else if (type == UShort.class) {
            String string = rs.getString(index);
            return (T) (string == null ? null : UShort.valueOf(string));
        }
        else if (type == UInteger.class) {
            String string = rs.getString(index);
            return (T) (string == null ? null : UInteger.valueOf(string));
        }
        else if (type == ULong.class) {
            String string = rs.getString(index);
            return (T) (string == null ? null : ULong.valueOf(string));
        }

        // The type byte[] is handled earlier. byte[][] can be handled here
        else if (type.isArray()) {
            switch (ctx.getDialect()) {
                case POSTGRES: {
                    return pgGetArray(ctx, type, index);
                }

                default:
                    // Note: due to a HSQLDB bug, it is not recommended to call rs.getObject() here:
                    // See https://sourceforge.net/tracker/?func=detail&aid=3181365&group_id=23316&atid=378131
                    return (T) convertArray(rs.getArray(index), (Class<? extends Object[]>) type);
            }
        }
        else if (ArrayRecord.class.isAssignableFrom(type)) {
            return (T) getArrayRecord(ctx, rs.getArray(index), (Class<? extends ArrayRecord<?>>) type);
        }
        else if (EnumType.class.isAssignableFrom(type)) {
            return getEnumType(type, rs.getString(index));
        }
        else if (MasterDataType.class.isAssignableFrom(type)) {
            return (T) getMasterDataType(type, rs.getObject(index));
        }
        else if (UDTRecord.class.isAssignableFrom(type)) {
            switch (ctx.getDialect()) {
                case POSTGRES:
                    return (T) pgNewUDTRecord(type, rs.getObject(index));
            }

            return (T) rs.getObject(index, DataTypes.udtRecords());
        }
        else if (Result.class.isAssignableFrom(type)) {
            ResultSet nested = (ResultSet) rs.getObject(index);
            return (T) getNewFactory(ctx).fetch(nested);
        }
        else {
            return (T) rs.getObject(index);
        }
    }

    private static ArrayRecord<?> getArrayRecord(Configuration configuration, Array array, Class<? extends ArrayRecord<?>> type)
        throws SQLException {

        if (array == null) {
            return null;
        }
        else {
            // TODO: [#523] Use array record meta data instead
            ArrayRecord<?> record = Util.newArrayRecord(type, configuration);
            record.set(array);
            return record;
        }
    }

    private static Object[] convertArray(Object array, Class<? extends Object[]> type) throws SQLException {
        if (array instanceof Object[]) {
            return Convert.convert(array, type);
        }
        else if (array instanceof Array) {
            return convertArray((Array) array, type);
        }

        return null;
    }

    private static Object[] convertArray(Array array, Class<? extends Object[]> type) throws SQLException {
        if (array != null) {
            return Convert.convert(array.getArray(), type);
        }

        return null;
    }

    private static Date getDate(SQLDialect dialect, ResultSet rs, int index) throws SQLException {

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

    private static Time getTime(SQLDialect dialect, ResultSet rs, int index) throws SQLException {

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

    private static Timestamp getTimestamp(SQLDialect dialect, ResultSet rs, int index) throws SQLException {

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

    private static long parse(String pattern, String date) throws SQLException {
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

    /**
     * @deprecated - 2.3.0 - Do not reuse this method
     */
    @Deprecated
    public static Map<String, Class<?>> getTypeMapping(Class<?> udtType) throws SQLException {
        try {
            return ((UDTRecord<?>) udtType.newInstance()).getUDT().getTypeMapping();
        } catch (Exception e) {
            throw new SQLException("Cannot retrieve type mapping for " + udtType, e);
        }
    }

    private static <T> T checkWasNull(SQLInput stream, T value) throws SQLException {
        return stream.wasNull() ? null : value;
    }

    private static <T> T checkWasNull(ResultSet rs, T value) throws SQLException {
        return rs.wasNull() ? null : value;
    }

    private static <T> T checkWasNull(CallableStatement statement, T value) throws SQLException {
        return statement.wasNull() ? null : value;
    }

    @SuppressWarnings("unchecked")
    private static <T> T getEnumType(Class<? extends T> type, String literal) throws SQLException {
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


    static MasterDataType<?> getMasterDataType(Class<?> type, Object primaryKey) throws SQLException {
        try {
            Object[] values = (Object[]) type.getMethod("values").invoke(type);

            for (Object value : values) {
                MasterDataType<?> result = (MasterDataType<?>) value;

                if (String.valueOf(primaryKey).equals(String.valueOf(result.getPrimaryKey()))) {
                    return result;
                }
            }
        }
        catch (Exception e) {
            throw new SQLException("Unknown enum literal found : " + primaryKey);
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getFromStatement(ExecuteContext ctx, Class<? extends T> type, int index) throws SQLException {
        CallableStatement stmt = (CallableStatement) ctx.statement();

        if (type == Blob.class) {
            return (T) stmt.getBlob(index);
        }
        else if (type == Boolean.class) {
            return (T) checkWasNull(stmt, Boolean.valueOf(stmt.getBoolean(index)));
        }
        else if (type == BigInteger.class) {
            BigDecimal result = stmt.getBigDecimal(index);
            return (T) (result == null ? null : result.toBigInteger());
        }
        else if (type == BigDecimal.class) {
            return (T) stmt.getBigDecimal(index);
        }
        else if (type == Byte.class) {
            return (T) checkWasNull(stmt, Byte.valueOf(stmt.getByte(index)));
        }
        else if (type == byte[].class) {
            return (T) stmt.getBytes(index);
        }
        else if (type == Clob.class) {
            return (T) stmt.getClob(index);
        }
        else if (type == Date.class) {
            return (T) stmt.getDate(index);
        }
        else if (type == Double.class) {
            return (T) checkWasNull(stmt, Double.valueOf(stmt.getDouble(index)));
        }
        else if (type == Float.class) {
            return (T) checkWasNull(stmt, Float.valueOf(stmt.getFloat(index)));
        }
        else if (type == Integer.class) {
            return (T) checkWasNull(stmt, Integer.valueOf(stmt.getInt(index)));
        }
        else if (type == Long.class) {
            return (T) checkWasNull(stmt, Long.valueOf(stmt.getLong(index)));
        }
        else if (type == Short.class) {
            return (T) checkWasNull(stmt, Short.valueOf(stmt.getShort(index)));
        }
        else if (type == String.class) {
            return (T) stmt.getString(index);
        }
        else if (type == Time.class) {
            return (T) stmt.getTime(index);
        }
        else if (type == Timestamp.class) {
            return (T) stmt.getTimestamp(index);
        }
        else if (type == YearToMonth.class) {
            if (ctx.getDialect() == POSTGRES) {
                Object object = stmt.getObject(index);
                return (T) (object == null ? null : PostgresUtils.toYearToMonth(object));
            }
            else {
                String string = stmt.getString(index);
                return (T) (string == null ? null : YearToMonth.valueOf(string));
            }
        }
        else if (type == DayToSecond.class) {
            if (ctx.getDialect() == POSTGRES) {
                Object object = stmt.getObject(index);
                return (T) (object == null ? null : PostgresUtils.toDayToSecond(object));
            }
            else {
                String string = stmt.getString(index);
                return (T) (string == null ? null : DayToSecond.valueOf(string));
            }
        }
        else if (type == UByte.class) {
            String string = stmt.getString(index);
            return (T) (string == null ? null : UByte.valueOf(string));
        }
        else if (type == UShort.class) {
            String string = stmt.getString(index);
            return (T) (string == null ? null : UShort.valueOf(string));
        }
        else if (type == UInteger.class) {
            String string = stmt.getString(index);
            return (T) (string == null ? null : UInteger.valueOf(string));
        }
        else if (type == ULong.class) {
            String string = stmt.getString(index);
            return (T) (string == null ? null : ULong.valueOf(string));
        }

        // The type byte[] is handled earlier. byte[][] can be handled here
        else if (type.isArray()) {
            return (T) convertArray(stmt.getObject(index), (Class<? extends Object[]>)type);
        }
        else if (ArrayRecord.class.isAssignableFrom(type)) {
            return (T) getArrayRecord(ctx, stmt.getArray(index), (Class<? extends ArrayRecord<?>>) type);
        }
        else if (EnumType.class.isAssignableFrom(type)) {
            return getEnumType(type, stmt.getString(index));
        }
        else if (MasterDataType.class.isAssignableFrom(type)) {
            return (T) getMasterDataType(type, stmt.getString(index));
        }
        else if (UDTRecord.class.isAssignableFrom(type)) {
            switch (ctx.getDialect()) {
                case POSTGRES:
                    return (T) pgNewUDTRecord(type, stmt.getObject(index));
            }

            return (T) stmt.getObject(index, DataTypes.udtRecords());
        }
        else if (Result.class.isAssignableFrom(type)) {
            ResultSet nested = (ResultSet) stmt.getObject(index);
            return (T) getNewFactory(ctx).fetch(nested);
        }
        else {
            return (T) stmt.getObject(index);
        }
    }

    public static Class<?> getClass(int sqlType, int precision, int scale) {
        switch (sqlType) {
            case Types.BLOB:
            case Types.BINARY:
            case Types.LONGVARBINARY:
            case Types.VARBINARY:
                return byte[].class;

            case Types.BOOLEAN:
            case Types.BIT:
                return Boolean.class;

            case Types.TINYINT:
                return Byte.class;

            case Types.SMALLINT:
                return Short.class;

            case Types.INTEGER:
                return Integer.class;

            case Types.BIGINT:
                return Long.class;

            case Types.REAL:
                return Float.class;

            case Types.DOUBLE:
            case Types.FLOAT:
                return Double.class;

            case Types.DECIMAL:
            case Types.NUMERIC: {

                // Integer numbers
                if (scale == 0 && precision != 0) {
                    if (precision < BYTE_PRECISION) {
                        return Byte.class;
                    }
                    if (precision < SHORT_PRECISION) {
                        return Short.class;
                    }
                    if (precision < INTEGER_PRECISION) {
                        return Integer.class;
                    }
                    if (precision < LONG_PRECISION) {
                        return Long.class;
                    }

                    // Default integer number
                    return BigInteger.class;
                }

                // Real numbers should not be represented as float or double
                else {
                    return BigDecimal.class;
                }
            }

            case Types.CLOB:
            case Types.CHAR:
            case Types.LONGNVARCHAR:
            case Types.LONGVARCHAR:
            case Types.NCHAR:
            case Types.NCLOB:
            case Types.NVARCHAR:
            case Types.VARCHAR:
                return String.class;

            case Types.DATE:
                return Date.class;

            case Types.TIME:
                return Time.class;

            case Types.TIMESTAMP:
                return Timestamp.class;

            default:
                return Object.class;
        }
    }

    public static <T> DataType<T> getDataType(SQLDialect dialect, Class<? extends T> type) {
        switch (dialect) {
            case ASE:
                return ASEDataType.getDataType(type);
            case CUBRID:
                return CUBRIDDataType.getDataType(type);
            case DB2:
                return DB2DataType.getDataType(type);
            case DERBY:
                return DerbyDataType.getDataType(type);
            case H2:
                return H2DataType.getDataType(type);
            case HSQLDB:
                return HSQLDBDataType.getDataType(type);
            case INGRES:
                return IngresDataType.getDataType(type);
            case MYSQL:
                return MySQLDataType.getDataType(type);
            case ORACLE:
                return OracleDataType.getDataType(type);
            case POSTGRES:
                return PostgresDataType.getDataType(type);
            case SQLITE:
                return SQLiteDataType.getDataType(type);
            case SQLSERVER:
                return SQLServerDataType.getDataType(type);
            case SYBASE:
                return SybaseDataType.getDataType(type);

                // Default behaviour is needed for hashCode() and toString();
            default:
               return SQLDataType.getDataType(null, type);
        }
    }

    /**
     * @return The type name without all special characters and white spaces
     */
    public static String normalise(String typeName) {
        return typeName.toUpperCase().replaceAll("\"|\\.|\\s|\\(\\w+(,\\w+)*\\)|(NOT\\s*NULL)?", "");
    }

    // -------------------------------------------------------------------------
    // The following section has been added for Postgres UDT support. The
    // official Postgres JDBC driver does not implement SQLData and similar
    // interfaces. Instead, a string representation of a UDT has to be parsed
    // -------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    private static <T> T pgFromString(Class<? extends T> type, String string) throws SQLException {
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
        else if (type.isArray()) {
            return (T) pgNewArray(type, string);
        }
        else if (ArrayRecord.class.isAssignableFrom(type)) {
            // Not supported
        }
        else if (EnumType.class.isAssignableFrom(type)) {
            return getEnumType(type, string);
        }
        else if (MasterDataType.class.isAssignableFrom(type)) {
            return (T) getMasterDataType(type, string);
        }
        else if (UDTRecord.class.isAssignableFrom(type)) {
            return (T) pgNewUDTRecord(type, string);
        }

        throw new UnsupportedOperationException("Class " + type + " is not supported");
    }

    private static java.util.Date pgParseDate(String string, SimpleDateFormat f) throws SQLException {
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
    private static UDTRecord<?> pgNewUDTRecord(Class<?> type, Object object) throws SQLException {
        if (object == null) {
            return null;
        }

        @SuppressWarnings({ "unchecked", "rawtypes" })
        UDTRecord<?> record = (UDTRecord<?>) Util.newRecord((Class) type);
        List<String> values = PostgresUtils.toPGObject(object.toString());

        List<Field<?>> fields = record.getFields();
        for (int i = 0; i < fields.size(); i++) {
            pgSetValue(record, fields.get(i), values.get(i));
        }

        return record;
    }

    /**
     * Workarounds for the unimplemented Postgres JDBC driver features
     */
    @SuppressWarnings("unchecked")
    private static <T> T pgGetArray(ExecuteContext ctx, Class<? extends T> type, int index)
        throws SQLException {

        ResultSet rs = ctx.resultSet();

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
                ctx.resultSet(array.getResultSet());
                while (ctx.resultSet().next()) {
                    result.add(getFromResultSet(ctx, type.getComponentType(), 2));
                }
            }

            // That might fail too, then we don't know any further...
            catch (Exception fatal) {
                log.error("Cannot parse Postgres array: " + rs.getString(index));
                log.error(fatal);
                return null;
            }

            finally {
                ctx.resultSet(rs);
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
    private static Object[] pgNewArray(Class<?> type, String string) throws SQLException {
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

    private static <T> void pgSetValue(UDTRecord<?> record, Field<T> field, String value)
        throws SQLException {
        record.setValue(field, pgFromString(field.getType(), value));
    }

    private FieldTypeHelper() {}

    /**
     * Convert a type name (using precision and scale) into a Java class
     */
    public static DataType<?> getDialectDataType(SQLDialect dialect, String t, int p, int s) throws SQLDialectNotSupportedException {
        DataType<?> result = AbstractDataType.getDataType(dialect, normalise(t));

        if (result.getType() == BigDecimal.class) {
            result = AbstractDataType.getDataType(dialect, getClass(Types.NUMERIC, p, s));
        }

        return result;
    }

    /**
     * Convert a type name (using precision and scale) into a Java class
     */
    public static Class<?> getDialectJavaType(SQLDialect dialect, String t, int p, int s) throws SQLDialectNotSupportedException {
        return getDialectDataType(dialect, t, p, s).getType(p, s);
    }
}
