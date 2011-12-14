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

import static org.jooq.impl.Factory.val;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.jooq.ArrayRecord;
import org.jooq.Attachable;
import org.jooq.AttachableInternal;
import org.jooq.Configuration;
import org.jooq.Cursor;
import org.jooq.Field;
import org.jooq.FieldProvider;
import org.jooq.NamedQueryPart;
import org.jooq.QueryPart;
import org.jooq.QueryPartInternal;
import org.jooq.Record;
import org.jooq.RenderContext;
import org.jooq.Schema;
import org.jooq.SchemaMapping;
import org.jooq.Table;
import org.jooq.Type;
import org.jooq.exception.DataAccessException;
import org.jooq.tools.StringUtils;

/**
 * General jooq utilities
 *
 * @author Lukas Eder
 */
final class Util {

    /**
     * Indicating whether JPA (<code>javax.persistence</code>) is on the
     * classpath.
     */
    private static Boolean isJPAAvailable;

    /**
     * Create a new Oracle-style VARRAY {@link ArrayRecord}
     */
    static <R extends ArrayRecord<?>> R newArrayRecord(Class<R> type, Configuration configuration) {
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
    static <R extends Record> R newRecord(Type<R> type) {
        return newRecord(type, null);
    }

    /**
     * Create a new record
     */
    static <R extends Record> R newRecord(Type<R> type, Configuration configuration) {
        return newRecord(type.getRecordType(), type, configuration);
    }

    /**
     * Create a new record
     */
    @SuppressWarnings("unchecked")
    static <R extends Record> R newRecord(Class<R> type, FieldProvider provider, Configuration configuration) {
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
     * Create SQL
     */
    static void toSQLReference(RenderContext context, String sql, Object[] bindings) {

        // Replace bind variables by their associated bind values
        if (context.inline()) {

            // [#724] When bindings is null, this is probably due to API-misuse
            // The user probably meant new Object[] { null }
            if (bindings == null) {
                toSQLReference(context, sql, new Object[] { null });
            }
            else {

                // TODO: Skip ? inside of string literals, e.g.
                // insert into x values ('Hello? Anybody out there?');
                String[] split = sql.split("\\?");

                for (int i = 0; i < split.length; i++) {
                    context.sql(split[i]);

                    if (i < bindings.length) {
                        context.sql(val(bindings[i]));
                    }
                }
            }
        }

        // If not inlining, just append the plain SQL the way it is
        else {
            context.sql(sql);
        }
    }

    /**
     * Create SQL wrapped in parentheses
     *
     * @see #toSQLReference(RenderContext, String, Object[])
     */
    static void toSQLReferenceWithParentheses(RenderContext context, String sql, Object[] bindings) {
        context.sql("(");
        toSQLReference(context, sql, bindings);
        context.sql(")");
    }

    /**
     * Render a list of names of the <code>NamedQueryParts</code> contained in
     * this list.
     */
    static void toSQLNames(RenderContext context, Collection<? extends NamedQueryPart> list) {
        String separator = "";

        for (NamedQueryPart part : list) {
            context.sql(separator).literal(part.getName());

            separator = ", ";
        }
    }

    /**
     * Combine a field with an array of fields
     */
    static Field<?>[] combine(Field<?> field, Field<?>... fields) {
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
    static Field<?>[] combine(Field<?> field1, Field<?> field2, Field<?>... fields) {
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
    static Field<?>[] combine(Field<?> field1, Field<?> field2, Field<?> field3, Field<?>... fields) {
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
    static DataAccessException translate(String task, String sql, SQLException e) {
        String message = task + "; SQL [" + sql + "]; " + e.getMessage();
        return new DataAccessException(message, e);
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

    /**
     * Check if JPA classes can be loaded. This is only done once per JVM!
     */
    static boolean isJPAAvailable() {
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
        // An entity usually has @Column annotations, too
        if (type.getAnnotation(Entity.class) != null) {
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
     * Map a {@link Schema} according to the configured {@link SchemaMapping}
     */
    static Schema getMappedSchema(Configuration configuration, Schema schema) {
        if (configuration.getSchemaMapping() != null) {
            return configuration.getSchemaMapping().map(schema);
        }
        else {
            return schema;
        }
    }

    /**
     * Map a {@link Table} according to the configured {@link SchemaMapping}
     */
    static Table<?> getMappedTable(Configuration configuration, Table<?> table) {
        if (configuration.getSchemaMapping() != null) {
            return configuration.getSchemaMapping().map(table);
        }
        else {
            return table;
        }
    }

    /**
     * Wrap a piece of SQL code in parentheses, if not wrapped already
     */
    static String wrapInParentheses(String sql) {
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
    static AttachableInternal internal(Attachable part) {
        return part.internalAPI(AttachableInternal.class);
    }

    /**
     * Expose the internal API of a {@link QueryPart}
     */
    static QueryPartInternal internal(QueryPart part) {
        return part.internalAPI(QueryPartInternal.class);
    }
}