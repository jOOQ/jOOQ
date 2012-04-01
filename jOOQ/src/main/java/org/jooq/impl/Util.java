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

import static java.lang.Boolean.FALSE;
import static java.lang.Integer.toOctalString;
import static org.jooq.impl.Factory.escape;
import static org.jooq.impl.Factory.getDataType;
import static org.jooq.impl.Factory.nullSafe;
import static org.jooq.impl.Factory.val;
import static org.jooq.tools.StringUtils.leftPad;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.jooq.ArrayRecord;
import org.jooq.Attachable;
import org.jooq.AttachableInternal;
import org.jooq.Configuration;
import org.jooq.Cursor;
import org.jooq.DataType;
import org.jooq.ExecuteContext;
import org.jooq.ExecuteListener;
import org.jooq.Field;
import org.jooq.FieldProvider;
import org.jooq.NamedQueryPart;
import org.jooq.Param;
import org.jooq.QueryPart;
import org.jooq.QueryPartInternal;
import org.jooq.Record;
import org.jooq.RenderContext;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.Type;
import org.jooq.exception.DataAccessException;
import org.jooq.tools.Convert;
import org.jooq.tools.LoggerListener;
import org.jooq.tools.StopWatchListener;
import org.jooq.tools.StringUtils;

/**
 * General jOOQ utilities
 *
 * @author Lukas Eder
 */
final class Util {

    /**
     * The default escape character for <code>[a] LIKE [b] ESCAPE [...]</code>
     * clauses.
     */
    static final char                          ESCAPE            = '!';

    /**
     * Indicating whether JPA (<code>javax.persistence</code>) is on the
     * classpath.
     */
    private static Boolean                     isJPAAvailable;

    /**
     * A cache for {@link ExecuteListener} classes
     */
    private static final Map<String, Class<?>> EXECUTE_LISTENERS = new ConcurrentHashMap<String, Class<?>>();

    /**
     * Create a new Oracle-style VARRAY {@link ArrayRecord}
     */
    static final <R extends ArrayRecord<?>> R newArrayRecord(Class<R> type, Configuration configuration) {
        try {
            return type.getConstructor(Configuration.class).newInstance(configuration);
        }
        catch (Exception e) {
            throw new IllegalStateException(
                "ArrayRecord type does not provide a constructor with signature ArrayRecord(FieldProvider) : " + type
                    + ". Exception : " + e.getMessage());

        }
    }

    /**
     * Create a new record
     */
    static final <R extends Record> R newRecord(Class<R> type) {
        return newRecord(type, null);
    }

    /**
     * Create a new record
     */
    static final <R extends Record> R newRecord(Class<R> type, FieldProvider provider) {
        return newRecord(type, provider, null);
    }

    /**
     * Create a new record
     */
    static final <R extends Record> R newRecord(Type<R> type) {
        return newRecord(type, null);
    }

    /**
     * Create a new record
     */
    static final <R extends Record> R newRecord(Type<R> type, Configuration configuration) {
        return newRecord(type.getRecordType(), type, configuration);
    }

    /**
     * Create a new record
     */
    @SuppressWarnings("unchecked")
    static final <R extends Record> R newRecord(Class<R> type, FieldProvider provider, Configuration configuration) {
        try {
            R result;

            // An ad-hoc type resulting from a JOIN or arbitrary SELECT
            if (type == RecordImpl.class) {
                result = (R) new RecordImpl(provider);
            }

            // Any generated record
            else {
                Constructor<R> constructor = type.getDeclaredConstructor();

                // [#919] Allow for accessing non-public constructors
                if (!constructor.isAccessible()) {
                    constructor.setAccessible(true);
                }

                result = constructor.newInstance();
            }

            result.attach(configuration);
            return result;
        }
        catch (Exception e) {
            throw new IllegalStateException("Could not construct new record", e);
        }
    }

    /**
     * [#1005] Convert values from the <code>VALUES</code> clause to appropriate
     * values as specified by the <code>INTO</code> clause's column list.
     */
    static final Object[] convert(List<Field<?>> fields, Object[] values) {
        if (values != null) {
            Object[] result = new Object[values.length];

            for (int i = 0; i < values.length; i++) {

                // TODO [#1008] Should fields be cast? Check this with
                // appropriate integration tests
                if (values[i] instanceof Field<?>) {
                    result[i] = values[i];
                }
                else {
                    result[i] = Convert.convert(values[i], fields.get(i).getType());
                }
            }

            return result;
        }
        else {
            return null;
        }
    }

    /**
     * Useful conversion method
     */
    static final Class<?>[] getClasses(Field<?>[] fields) {
        return getClasses(getDataTypes(fields));
    }

    /**
     * Useful conversion method
     */
    static final Class<?>[] getClasses(DataType<?>[] types) {
        if (types == null) {
            return null;
        }

        Class<?>[] result = new Class<?>[types.length];

        for (int i = 0; i < types.length; i++) {
            if (types[i] != null) {
                result[i] = types[i].getType();
            }
            else {
                result[i] = Object.class;
            }
        }

        return result;
    }

    /**
     * Useful conversion method
     */
    static final Class<?>[] getClasses(Object[] values) {
        if (values == null) {
            return null;
        }

        Class<?>[] result = new Class<?>[values.length];

        for (int i = 0; i < values.length; i++) {
            if (values[i] instanceof Field<?>) {
                result[i] = ((Field<?>) values[i]).getType();
            }
            else if (values[i] != null) {
                result[i] = values[i].getClass();
            }
            else {
                result[i] = Object.class;
            }
        }

        return result;
    }

    /**
     * Useful conversion method
     */
    static final DataType<?>[] getDataTypes(Field<?>[] fields) {
        if (fields == null) {
            return null;
        }

        DataType<?>[] result = new DataType<?>[fields.length];

        for (int i = 0; i < fields.length; i++) {
            if (fields[i] != null) {
                result[i] = fields[i].getDataType();
            }
            else {
                result[i] = getDataType(Object.class);
            }
        }

        return result;
    }

    /**
     * Useful conversion method
     */
    static final DataType<?>[] getDataTypes(Class<?>[] types) {
        if (types == null) {
            return null;
        }

        DataType<?>[] result = new DataType<?>[types.length];

        for (int i = 0; i < types.length; i++) {
            if (types[i] != null) {
                result[i] = getDataType(types[i]);
            }
            else {
                result[i] = getDataType(Object.class);
            }
        }

        return result;
    }

    /**
     * Useful conversion method
     */
    static final DataType<?>[] getDataTypes(Object[] values) {
        return getDataTypes(getClasses(values));
    }

    /**
     * Create SQL
     */
    static final void toSQLReference(RenderContext context, String sql, List<Param<?>> bindings) {

        // Replace bind variables by their associated bind values
        if (context.inline()) {

            // [#1031] [#1032] Skip ? inside of string literals, e.g.
            // insert into x values ('Hello? Anybody out there?');
            int bindIndex = 0;
            char[] sqlChars = sql.toCharArray();
            boolean stringLiteral = false;

            for (int i = 0; i < sqlChars.length; i++) {

                // String literal delimiter
                if (sqlChars[i] == '\'') {

                    // Delimiter is actually an escaping apostrophe
                    if (i + 1 < sqlChars.length && sqlChars[i + 1] == '\'') {

                        // Skip subsequent character
                        context.sql(sqlChars[i++]);
                        context.sql(sqlChars[i]);
                    }

                    else {
                        stringLiteral = !stringLiteral;
                        context.sql(sqlChars[i]);
                    }
                }

                // Replace bind variables only outside of string literals
                else if (sqlChars[i] == '?' && !stringLiteral && bindIndex < bindings.size()) {
                    context.sql(bindings.get(bindIndex++));
                }

                // Any other character
                else {
                    context.sql(sqlChars[i]);
                }
            }
        }

        // If not inlining, just append the plain SQL the way it is
        else {
            context.sql(sql);
        }
    }

    /**
     * Create {@link Param} objects from bind values
     */
    static final List<Param<?>> bindings(Object... bindings) {
        // [#724] When bindings is null, this is probably due to API-misuse
        // The user probably meant new Object[] { null }
        if (bindings == null) {
            return bindings(new Object[] { null });
        }
        else {
            List<Param<?>> result = new ArrayList<Param<?>>();

            for (Object binding : bindings) {
                Class<?> type = binding != null ? binding.getClass() : Object.class;
                result.add(new Val<Object>(binding, Factory.getDataType(type)));
            }

            return result;
        }
    }

    /**
     * Create SQL wrapped in parentheses
     *
     * @see #toSQLReference(RenderContext, String, List)
     */
    static final void toSQLReferenceWithParentheses(RenderContext context, String sql, List<Param<?>> bindings) {
        context.sql("(");
        toSQLReference(context, sql, bindings);
        context.sql(")");
    }

    /**
     * Render a list of names of the <code>NamedQueryParts</code> contained in
     * this list.
     */
    static final void toSQLNames(RenderContext context, Collection<? extends NamedQueryPart> list) {
        String separator = "";

        for (NamedQueryPart part : list) {
            context.sql(separator).literal(part.getName());

            separator = ", ";
        }
    }

    /**
     * Combine a field with an array of fields
     */
    static final Field<?>[] combine(Field<?> field, Field<?>... fields) {
        if (fields == null) {
            return new Field[] { field };
        }
        else {
            Field<?>[] result = new Field<?>[fields.length + 1];
            result[0] = field;
            System.arraycopy(fields, 0, result, 1, fields.length);

            return result;
        }
    }

    /**
     * Combine a field with an array of fields
     */
    static final Field<?>[] combine(Field<?> field1, Field<?> field2, Field<?>... fields) {
        if (fields == null) {
            return new Field[] { field1, field2 };
        }
        else {
            Field<?>[] result = new Field<?>[fields.length + 2];
            result[0] = field1;
            result[1] = field2;
            System.arraycopy(fields, 0, result, 2, fields.length);

            return result;
        }
    }

    /**
     * Combine a field with an array of fields
     */
    static final Field<?>[] combine(Field<?> field1, Field<?> field2, Field<?> field3, Field<?>... fields) {
        if (fields == null) {
            return new Field[] { field1, field2, field3 };
        }
        else {
            Field<?>[] result = new Field<?>[fields.length + 3];
            result[0] = field1;
            result[1] = field2;
            result[2] = field3;
            System.arraycopy(fields, 0, result, 3, fields.length);
            return result;
        }
    }

    /**
     * Translate a {@link SQLException} to a {@link DataAccessException}
     */
    static final DataAccessException translate(String task, String sql, SQLException e) {
        String message = task + "; SQL [" + sql + "]; " + e.getMessage();
        return new DataAccessException(message, e);
    }

    /**
     * Safely close a statement
     */
    static final void safeClose(ExecuteListener listener, ExecuteContext ctx) {
        safeClose(ctx.resultSet());
        safeClose(ctx.statement());
        listener.end(ctx);
    }

    /**
     * Safely close a statement
     */
    static final void safeClose(Statement statement) {
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
    static final void safeClose(ResultSet resultSet) {
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
    static final void safeClose(Cursor<?> cursor) {
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
    static final void safeClose(ResultSet resultSet, PreparedStatement statement) {
        safeClose(resultSet);
        safeClose(statement);
    }

    /**
     * Extract an underlying connection
     */
    static final Connection getDriverConnection(Configuration configuration) {
        if (configuration != null) {
            Connection connection = configuration.getConnection();

            if (connection != null) {

                // If the connection is wrapped by jOOQ, extract the underlying
                // connection
                if (connection.getClass() == ConnectionProxy.class) {
                    connection = ((ConnectionProxy) connection).getDelegate();
                }

                // [#1157] TODO: If jOOQ's extended tracing / logging feature
                // allows for further wrapping a connection, this must be
                // treated here...

                return connection;
            }
        }

        throw new DataAccessException("Cannot get a JDBC driver connection from configuration: " + configuration);
    }

    /**
     * Check if JPA classes can be loaded. This is only done once per JVM!
     */
    private static final boolean isJPAAvailable() {
        if (isJPAAvailable == null) {
            try {
                Class.forName(Column.class.getName());
                isJPAAvailable = true;
            }
            catch (Throwable e) {
                isJPAAvailable = false;
            }
        }

        return isJPAAvailable;
    }

    /**
     * Check whether <code>type</code> has any {@link Column} annotated members
     * or methods
     */
    static final boolean hasColumnAnnotations(Class<?> type) {
        if (!isJPAAvailable()) {
            return false;
        }

        // An @Entity or @Table usually has @Column annotations, too
        if (type.getAnnotation(Entity.class) != null ||
            type.getAnnotation(javax.persistence.Table.class) != null) {
            return true;
        }

        for (java.lang.reflect.Field member : getInstanceMembers(type)) {
            if (member.getAnnotation(Column.class) != null) {
                return true;
            }
        }

        for (Method method : getInstanceMethods(type)) {
            if (method.getAnnotation(Column.class) != null) {
                return true;
            }
        }

        return false;
    }

    /**
     * Get all members annotated with a given column name
     */
    static final List<java.lang.reflect.Field> getAnnotatedMembers(Class<?> type, String name) {
        List<java.lang.reflect.Field> result = new ArrayList<java.lang.reflect.Field>();

        for (java.lang.reflect.Field member : getInstanceMembers(type)) {
            Column annotation = member.getAnnotation(Column.class);

            if (annotation != null) {
                if (name.equals(annotation.name())) {
                    result.add(member);
                }
            }
        }

        return result;
    }

    /**
     * Get all members matching a given column name
     */
    static final List<java.lang.reflect.Field> getMatchingMembers(Class<?> type, String name) {
        List<java.lang.reflect.Field> result = new ArrayList<java.lang.reflect.Field>();

        for (java.lang.reflect.Field member : getInstanceMembers(type)) {
            if (name.equals(member.getName())) {
                result.add(member);
            }
            else if (StringUtils.toCamelCaseLC(name).equals(member.getName())) {
                result.add(member);
            }
        }

        return result;
    }

    /**
     * Get all setter methods annotated with a given column name
     */
    static final List<Method> getAnnotatedSetters(Class<?> type, String name) {
        List<Method> result = new ArrayList<Method>();

        for (Method method : getInstanceMethods(type)) {
            Column annotation = method.getAnnotation(Column.class);

            if (annotation != null && name.equals(annotation.name())) {

                // Annotated setter
                if (method.getParameterTypes().length == 1) {
                    result.add(method);
                }

                // Annotated getter with matching setter
                else if (method.getParameterTypes().length == 0) {
                    String m = method.getName();

                    if (m.startsWith("get") || m.startsWith("is")) {
                        try {
                            Method setter = type.getMethod("set" + m.substring(3), method.getReturnType());

                            // Setter annotation is more relevant
                            if (setter.getAnnotation(Column.class) == null) {
                                result.add(setter);
                            }
                        }
                        catch (NoSuchMethodException ignore) {}
                    }
                }
            }
        }

        return result;
    }

    /**
     * Get the first getter method annotated with a given column name
     */
    static final Method getAnnotatedGetter(Class<?> type, String name) {
        for (Method method : getInstanceMethods(type)) {
            Column annotation = method.getAnnotation(Column.class);

            if (annotation != null && name.equals(annotation.name())) {

                // Annotated getter
                if (method.getParameterTypes().length == 0) {
                    return method;
                }

                // Annotated setter with matching getter
                else if (method.getParameterTypes().length == 1) {
                    String m = method.getName();

                    if (m.startsWith("set")) {
                        try {
                            Method getter = type.getMethod("get" + m.substring(3));

                            // Getter annotation is more relevant
                            if (getter.getAnnotation(Column.class) == null) {
                                return getter;
                            }
                        }
                        catch (NoSuchMethodException ignore) {}

                        try {
                            Method getter = type.getMethod("is" + m.substring(3));

                            // Getter annotation is more relevant
                            if (getter.getAnnotation(Column.class) == null) {
                                return getter;
                            }
                        }
                        catch (NoSuchMethodException ignore) {}
                    }
                }
            }
        }

        return null;
    }

    /**
     * Get all setter methods matching a given column name
     */
    static final List<Method> getMatchingSetters(Class<?> type, String name) {
        List<Method> result = new ArrayList<Method>();

        for (Method method : getInstanceMethods(type)) {
            if (method.getParameterTypes().length == 1) {
                if (name.equals(method.getName())) {
                    result.add(method);
                }
                else if (StringUtils.toCamelCaseLC(name).equals(method.getName())) {
                    result.add(method);
                }
                else if (("set" + name).equals(method.getName())) {
                    result.add(method);
                }
                else if (("set" + StringUtils.toCamelCase(name)).equals(method.getName())) {
                    result.add(method);
                }
            }
        }

        return result;
    }


    /**
     * Get the first getter method matching a given column name
     */
    static final Method getMatchingGetter(Class<?> type, String name) {
        for (Method method : getInstanceMethods(type)) {
            if (method.getParameterTypes().length == 0) {
                if (name.equals(method.getName())) {
                    return method;
                }
                else if (StringUtils.toCamelCaseLC(name).equals(method.getName())) {
                    return method;
                }
                else if (("get" + name).equals(method.getName())) {
                    return method;
                }
                else if (("get" + StringUtils.toCamelCase(name)).equals(method.getName())) {
                    return method;
                }
                else if (("is" + name).equals(method.getName())) {
                    return method;
                }
                else if (("is" + StringUtils.toCamelCase(name)).equals(method.getName())) {
                    return method;
                }
            }
        }

        return null;
    }

    private static final List<Method> getInstanceMethods(Class<?> type) {
        List<Method> result = new ArrayList<Method>();

        for (Method method : type.getMethods()) {
            if ((method.getModifiers() & Modifier.STATIC) == 0) {
                result.add(method);
            }
        }

        return result;
    }

    private static final List<java.lang.reflect.Field> getInstanceMembers(Class<?> type) {
        List<java.lang.reflect.Field> result = new ArrayList<java.lang.reflect.Field>();

        for (java.lang.reflect.Field field : type.getFields()) {
            if ((field.getModifiers() & Modifier.STATIC) == 0) {
                result.add(field);
            }
        }

        return result;
    }

    /**
     * Type-safely copy a value from one record to another
     */
    static final <T> void setValue(Record target, Field<T> targetField, Record source, Field<?> sourceField) {
        setValue(target, targetField, source.getValue(sourceField));
    }

    /**
     * Type-safely set a value to a record
     */
    static final <T> void setValue(Record target, Field<T> targetField, Object value) {
        target.setValue(targetField, targetField.getDataType().convert(value));
    }

    /**
     * Map a {@link Schema} according to the configured {@link org.jooq.SchemaMapping}
     */
    @SuppressWarnings("deprecation")
    static final Schema getMappedSchema(Configuration configuration, Schema schema) {
        if (configuration.getSchemaMapping() != null) {
            return configuration.getSchemaMapping().map(schema);
        }
        else {
            return schema;
        }
    }

    /**
     * Map a {@link Table} according to the configured {@link org.jooq.SchemaMapping}
     */
    @SuppressWarnings("deprecation")
    static final Table<?> getMappedTable(Configuration configuration, Table<?> table) {
        if (configuration.getSchemaMapping() != null) {
            return configuration.getSchemaMapping().map(table);
        }
        else {
            return table;
        }
    }

    static final List<ExecuteListener> getListeners(Configuration configuration) {
        List<ExecuteListener> result = new ArrayList<ExecuteListener>();

        if (!FALSE.equals(configuration.getSettings().isExecuteLogging())) {
            result.add(new StopWatchListener());
            result.add(new LoggerListener());
        }

        for (String listener : configuration.getSettings().getExecuteListeners()) {
            result.add(getListener(listener));
        }

        return result;
    }

    private static final ExecuteListener getListener(String name) {
        try {
            Class<?> type = EXECUTE_LISTENERS.get(name);

            if (type == null) {
                type = Class.forName(name);
                EXECUTE_LISTENERS.put(name, type);
            }

            return (ExecuteListener) type.newInstance();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Wrap a piece of SQL code in parentheses, if not wrapped already
     */
    static final String wrapInParentheses(String sql) {
        if (sql.startsWith("(")) {
            return sql;
        }
        else {
            return "(" + sql + ")";
        }
    }

    /**
     * Expose the internal API of an {@link Attachable}
     */
    static final AttachableInternal internal(Attachable part) {
        return part.internalAPI(AttachableInternal.class);
    }

    /**
     * Expose the internal API of a {@link QueryPart}
     */
    static final QueryPartInternal internal(QueryPart part) {
        return part.internalAPI(QueryPartInternal.class);
    }

    /**
     * Return a non-negative hash code for a {@link QueryPart}, taking into
     * account FindBugs' <code>RV_ABSOLUTE_VALUE_OF_HASHCODE</code> pattern
     */
    static final int hash(Object object) {
        return 0x7FFFFFF & object.hashCode();
    }

    // ------------------------------------------------------------------------
    // XXX This section is taken from the H2 Database
    // ------------------------------------------------------------------------

    private static final char[] HEX = "0123456789abcdef".toCharArray();

    /**
     * Convert a byte array to a hex encoded string.
     *
     * @param value the byte array
     * @return the hex encoded string
     */
    static final String convertBytesToHex(byte[] value) {
        return convertBytesToHex(value, value.length);
    }

    /**
     * Convert a byte array to a hex encoded string.
     *
     * @param value the byte array
     * @param len the number of bytes to encode
     * @return the hex encoded string
     */
    static final String convertBytesToHex(byte[] value, int len) {
        char[] buff = new char[len + len];
        char[] hex = HEX;
        for (int i = 0; i < len; i++) {
            int c = value[i] & 0xff;
            buff[i + i] = hex[c >> 4];
            buff[i + i + 1] = hex[c & 0xf];
        }
        return new String(buff);
    }

    /**
     * Postgres uses octals instead of hex encoding
     */
    static final String convertBytesToPostgresOctal(byte[] binary) {
        StringBuilder sb = new StringBuilder();

        for (byte b : binary) {
            sb.append("\\\\");
            sb.append(leftPad(toOctalString(b), 3, '0'));
        }

        return sb.toString();
    }

    /**
     * Utility method to escape strings or "toString" other objects
     */
    static final Field<String> escapeForLike(Object value) {
        if (value != null && value.getClass() == String.class) {
            return val(escape("" + value, ESCAPE));
        }
        else {
            return val("" + value);
        }
    }

    /**
     * Utility method to escape string fields, or cast other fields
     */
    @SuppressWarnings("unchecked")
    static final Field<String> escapeForLike(Field<?> field) {
        if (nullSafe(field).getDataType().isString()) {
            return escape((Field<String>) field, ESCAPE);
        }
        else {
            return field.cast(String.class);
        }
    }
}