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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.regex.Matcher;

import org.jooq.ArrayRecord;
import org.jooq.Configuration;
import org.jooq.Cursor;
import org.jooq.EnumType;
import org.jooq.Field;
import org.jooq.FieldProvider;
import org.jooq.MasterDataType;
import org.jooq.NamedTypeProviderQueryPart;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.SQLDialectNotSupportedException;

/**
 * General jooq utilities
 *
 * @author Lukas Eder
 */
final class JooqUtil {

    private static final JooqLogger log = JooqLogger.getLogger(JooqUtil.class);

    /**
     * Create a new Oracle-style VARRAY {@link ArrayRecord}
     */
    static <R extends ArrayRecord<?>> R newArrayRecord(Class<R> type, Configuration configuration) {
       try{
           return type.getConstructor(Configuration.class).newInstance(configuration);
       } catch (Exception e) {
           throw new IllegalStateException(
               "ArrayRecord type does not provide a constructor with signature ArrayRecord(FieldProvider) : " + type +
               ". Exception : " + e.getMessage());

       }
    }

    /**
     * Create a new record
     */
    static <R extends Record> R newRecord(Class<R> type) {
        return newRecord(type, null);
    }

    /**
     * Create a new record
     */
    static <R extends Record> R newRecord(Class<R> type, FieldProvider provider) {
        return newRecord(type, provider, null);
    }

    /**
     * Create a new record
     */
    @SuppressWarnings("unchecked")
    static <R extends Record> R newRecord(Class<R> type, FieldProvider provider, Configuration configuration) {
        try {
            // An ad-hoc type resulting from a JOIN or arbitrary SELECT
            if (type == RecordImpl.class) {
                return (R) new RecordImpl(provider, configuration);
            }

            // Any generated UpdatableRecord
            if (UpdatableRecordImpl.class.isAssignableFrom(type)) {
                return type.getConstructor(Configuration.class).newInstance(configuration);
            }

            // Any generated TableRecord
            return type.newInstance();
        }
        catch (Exception e) {
            throw new IllegalStateException("Could not construct new record. Please report this issue", e);
        }
    }

    /**
     * Delimit literals (such as schema, table, column names) for greater
     * compatibility.
     */
    static String toSQLLiteral(Configuration configuration, String literal) {
        switch (configuration.getDialect()) {
            case MYSQL:
                return "`" + literal + "`";

            case DB2:
            case DERBY:
            case H2:
            case HSQLDB:
            case INGRES:
            case ORACLE:
            case POSTGRES:
                return "\"" + literal + "\"";

            // SQLite should support all sorts of delimiters, but it seems
            // quite buggy
            case SQLITE:
                return literal;

            case SQLSERVER:
            case SYBASE:
                return "[" + literal + "]";

            default:
                return literal;
        }
    }

    /**
     * Create SQL
     */
    static String toSQLReference(Configuration configuration, String sql, Object[] bindings, boolean inlineParameters) {
        String result = sql;

        if (inlineParameters) {

            // [#724] When bindings is null, this is probably due to API-misuse
            // The user probably meant new Object[] { null }
            if (bindings == null) {
                result = toSQLReference(configuration, sql, new Object[] { null }, inlineParameters);
            }
            else {
                for (Object binding : bindings) {
                    result = result.replaceFirst(
                        "\\?",
                        Matcher.quoteReplacement(FieldTypeHelper.toSQL(configuration, binding, inlineParameters)));
                }
            }
        }

        return result;
    }

    /**
     * Create SQL wrapped in parentheses
     *
     * @see #toSQLReference(SQLDialect, String, Object[], boolean)
     */
    static String toSQLReferenceWithParentheses(Configuration configuration, String sql, Object[] bindings, boolean inlineParameters) {
        return "(" + toSQLReference(configuration, sql, bindings, inlineParameters) + ")";
    }

    /**
     * Bind a value to a variable
     */
    @SuppressWarnings("unchecked")
    static void bind(Configuration configuration, PreparedStatement stmt, int index, Class<?> type, Object value) throws SQLException {
        SQLDialect dialect = configuration.getDialect();

        if (log.isTraceEnabled()) {
            if (value != null && value.getClass().isArray() && value.getClass() != byte[].class) {
                log.trace("Binding variable " + index, Arrays.asList((Object[])value) + " (" + type + ")");
            }
            else {
                log.trace("Binding variable " + index, value + " (" + type + ")");
            }
        }

        if (value == null) {
            int sqlType = FieldTypeHelper.getDataType(dialect, type).getSQLType();

            // Treat Oracle-style ARRAY types specially
            if (ArrayRecord.class.isAssignableFrom(type)) {
                String typeName = newArrayRecord((Class<ArrayRecord<?>>) type, configuration).getName();
                stmt.setNull(index, sqlType, typeName);
            }

            // All other types can be set to null if the JDBC type is known
            else if (sqlType != Types.OTHER) {
                stmt.setNull(index, sqlType);
            }

            // [#725] For SQL Server, unknown types should be set to null explicitly, too
            else if (configuration.getDialect() == SQLDialect.SQLSERVER) {
                stmt.setNull(index, sqlType);
            }

            // [#729] In the absence of the correct JDBC type, try setObject
            // [#730] TODO: Handle this case for Sybase
            else {
                stmt.setObject(index, null);
            }
        }
        else if (type == Blob.class) {
            stmt.setBlob(index, (Blob) value);
        }
        else if (type == Boolean.class) {
            stmt.setBoolean(index, (Boolean) value);
        }
        else if (type == BigDecimal.class) {
            stmt.setBigDecimal(index, (BigDecimal) value);
        }
        else if (type == BigInteger.class) {
            stmt.setBigDecimal(index, new BigDecimal((BigInteger) value));
        }
        else if (type == Byte.class) {
            stmt.setByte(index, (Byte) value);
        }
        else if (type == byte[].class) {
            stmt.setBytes(index, (byte[]) value);
        }
        else if (type == Clob.class) {
            stmt.setClob(index, (Clob) value);
        }
        else if (type == Date.class) {
            stmt.setDate(index, (Date) value);
        }
        else if (type == Double.class) {
            stmt.setDouble(index, (Double) value);
        }
        else if (type == Float.class) {
            stmt.setFloat(index, (Float) value);
        }
        else if (type == Integer.class) {
            stmt.setInt(index, (Integer) value);
        }
        else if (type == Long.class) {
            stmt.setLong(index, (Long) value);
        }
        else if (type == Short.class) {
            stmt.setShort(index, (Short) value);
        }
        else if (type == String.class) {
            stmt.setString(index, (String) value);
        }
        else if (type == Time.class) {
            stmt.setTime(index, (Time) value);
        }
        else if (type == Timestamp.class) {
            stmt.setTimestamp(index, (Timestamp) value);
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
                    stmt.setString(index, sb.toString());
                    break;
                }
                case HSQLDB:
                    stmt.setArray(index, new DefaultArray(dialect, (Object[]) value, type));
                    break;
                case H2:
                    stmt.setObject(index, value);
                    break;
                default:
                    throw new SQLDialectNotSupportedException("Cannot bind ARRAY types in dialect " + dialect);
            }
        }
        else if (ArrayRecord.class.isAssignableFrom(type)) {
            stmt.setArray(index, ((ArrayRecord<?>) value).createArray());
        }
        else if (EnumType.class.isAssignableFrom(type)) {
            stmt.setString(index, ((EnumType) value).getLiteral());
        }
        else if (MasterDataType.class.isAssignableFrom(type)) {
            Object primaryKey = ((MasterDataType<?>) value).getPrimaryKey();
            bind(configuration, stmt, index, primaryKey.getClass(), primaryKey);
        }
        else {
            stmt.setObject(index, value);
        }
    }

    /**
     * Bind a value to a variable
     */
    static void bind(Configuration configuration, PreparedStatement stmt, int initialIndex, NamedTypeProviderQueryPart<?> field, Object value)
        throws SQLException {
        bind(configuration, stmt, initialIndex, field.getType(), value);
    }

    /**
     * Bind a value to a variable
     */
    static int bind(Configuration configuration, PreparedStatement stmt, int initialIndex, Object... bindings) throws SQLException {

        // [#724] When bindings is null, this is probably due to API-misuse
        // The user probably meant new Object[] { null }
        if (bindings == null) {
            return bind(configuration, stmt, initialIndex, new Object[] { null });
        }
        else {
            for (Object binding : bindings) {
                Class<?> type = (binding == null) ? Object.class : binding.getClass();
                bind(configuration, stmt, initialIndex++, type, binding);
            }
        }

        return initialIndex;
    }

    /**
     * Combine a field with an array of fields
     */
    static Field<?>[] combine(Field<?> field, Field<?>... fields) {
        Field<?>[] result = new Field<?>[fields.length + 1];
        result[0] = field;
        System.arraycopy(fields, 0, result, 1, fields.length);
        return result;
    }

    /**
     * Combine a field with an array of fields
     */
    static Field<?>[] combine(Field<?> field1, Field<?> field2, Field<?>... fields) {
        Field<?>[] result = new Field<?>[fields.length + 2];
        result[0] = field1;
        result[1] = field2;
        System.arraycopy(fields, 0, result, 2, fields.length);
        return result;
    }

    /**
     * Combine a field with an array of fields
     */
    static Field<?>[] combine(Field<?> field1, Field<?> field2, Field<?> field3, Field<?>... fields) {
        Field<?>[] result = new Field<?>[fields.length + 3];
        result[0] = field1;
        result[1] = field2;
        result[2] = field3;
        System.arraycopy(fields, 0, result, 3, fields.length);
        return result;
    }

    /**
     * Safely close a statement
     */
    static void safeClose(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            }
            catch (Exception ignore) {}
        }
    }

    /**
     * Safely close a result set
     */
    static void safeClose(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            }
            catch (Exception ignore) {}
        }
    }

    /**
     * Safely close a cursor
     */
    static void safeClose(Cursor<?> cursor) {
        if (cursor != null) {
            try {
                cursor.close();
            }
            catch (Exception ignore) {}
        }
    }

    /**
     * Safely close a result set and / or a statement
     */
    static void safeClose(ResultSet resultSet, PreparedStatement statement) {
        safeClose(resultSet);
        safeClose(statement);
    }
}
