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

import static java.lang.Boolean.FALSE;
import static java.lang.Integer.toOctalString;
import static org.jooq.impl.Factory.escape;
import static org.jooq.impl.Factory.getDataType;
import static org.jooq.impl.Factory.nullSafe;
import static org.jooq.impl.Factory.val;
import static org.jooq.tools.StringUtils.leftPad;
import static org.jooq.tools.reflect.Reflect.accessible;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.jooq.ArrayRecord;
import org.jooq.BindContext;
import org.jooq.Configuration;
import org.jooq.Cursor;
import org.jooq.DataType;
import org.jooq.ExecuteContext;
import org.jooq.ExecuteListener;
import org.jooq.Field;
import org.jooq.FieldProvider;
import org.jooq.Param;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.RenderContext;
import org.jooq.SQLDialect;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.UDT;
import org.jooq.UDTRecord;
import org.jooq.UpdatableRecord;
import org.jooq.conf.Settings;
import org.jooq.exception.DataAccessException;
import org.jooq.exception.InvalidResultException;
import org.jooq.tools.Convert;
import org.jooq.tools.LoggerListener;
import org.jooq.tools.StopWatchListener;
import org.jooq.tools.StringUtils;
import org.jooq.tools.reflect.Reflect;

/**
 * General jOOQ utilities
 *
 * @author Lukas Eder
 */
final class Utils {

    // ------------------------------------------------------------------------
    // Some constants for use with Configuration.setData()
    // ------------------------------------------------------------------------

    /**
     * [#1537] This constant is used internally by jOOQ to omit the RETURNING
     * clause in {@link Executor#batchStore(UpdatableRecord...)} calls for
     * {@link SQLDialect#POSTGRES}
     */
    static final String          DATA_OMIT_RETURNING_CLAUSE = "org.jooq.configuration.omit-returning-clause";

    // ------------------------------------------------------------------------
    // Other constants
    // ------------------------------------------------------------------------

    /**
     * The default escape character for <code>[a] LIKE [b] ESCAPE [...]</code>
     * clauses.
     */
    static final char            ESCAPE                     = '!';

    /**
     * Indicating whether JPA (<code>javax.persistence</code>) is on the
     * classpath.
     */
    private static Boolean       isJPAAvailable;

    /**
     * A pattern for the JDBC escape syntax
     */
    private static final Pattern JDBC_ESCAPE_PATTERN        = Pattern.compile("\\{(fn|d|t|ts)\\b.*");

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
    static final <R extends Record> R newRecord(Table<R> type) {
        return newRecord(type, null);
    }

    /**
     * Create a new record
     */
    static final <R extends Record> R newRecord(Table<R> type, Configuration configuration) {
        return newRecord(type.getRecordType(), type, configuration);
    }

    /**
     * Create a new UDT record
     */
    static final <R extends UDTRecord<R>> R newRecord(UDT<R> type) {
        return newRecord(type, null);
    }

    /**
     * Create a new UDT record
     */
    static final <R extends UDTRecord<R>> R newRecord(UDT<R> type, Configuration configuration) {
        return newRecord(type.getRecordType(), type, configuration);
    }

    /**
     * Create a new record
     */
    @SuppressWarnings({ "unchecked" })
    static final <R extends Record> R newRecord(Class<R> type, FieldProvider provider, Configuration configuration) {
        try {
            R result;

            // An ad-hoc type resulting from a JOIN or arbitrary SELECT
            if (type == RecordImpl.class || type == Record.class) {
                result = (R) new RecordImpl(provider);
            }

            // Any generated record
            else {

                // [#919] Allow for accessing non-public constructors
                result = Reflect.accessible(type.getDeclaredConstructor()).newInstance();
            }

            // [#1684] TODO: Do not attach configuration if settings say no
            if (attachRecords(configuration)) {
                result.attach(configuration);
            }

            return result;
        }
        catch (Exception e) {
            throw new IllegalStateException("Could not construct new record", e);
        }
    }

    private static final boolean attachRecords(Configuration configuration) {
        if (configuration != null) {
            Settings settings = configuration.getSettings();

            if (settings != null) {
                return !FALSE.equals(settings.isAttachRecords());
            }
        }

        return true;
    }

    /**
     * Use this rather than {@link Arrays#asList(Object...)} for
     * <code>null</code>-safety
     */
    static final <T> List<T> list(T... array) {
        return array == null ? Collections.<T>emptyList() : Arrays.asList(array);
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
     * [#1005] Convert values from the <code>VALUES</code> clause to appropriate
     * values as specified by the <code>INTO</code> clause's column list.
     */
    static final Object[] convert(Class<?>[] types, Object[] values) {
        if (values != null) {
            Object[] result = new Object[values.length];

            for (int i = 0; i < values.length; i++) {

                // TODO [#1008] Should fields be cast? Check this with
                // appropriate integration tests
                if (values[i] instanceof Field<?>) {
                    result[i] = values[i];
                }
                else {
                    result[i] = Convert.convert(values[i], types[i]);
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

    // ------------------------------------------------------------------------
    // XXX: General utility methods
    // ------------------------------------------------------------------------

    /**
     * Be sure that a given object is a field.
     *
     * @param value The argument object
     * @return The argument object itself, if it is a {@link Field}, or a bind
     *         value created from the argument object.
     */
    @SuppressWarnings("unchecked")
    static final <T> Field<T> field(T value) {

        // Fields can be mixed with constant values
        if (value instanceof Field<?>) {
            return (Field<T>) value;
        }
        else {
            return val(value);
        }
    }

    /**
     * Be sure that a given object is a field.
     *
     * @param value The argument object
     * @param field The field to take the bind value type from
     * @return The argument object itself, if it is a {@link Field}, or a bind
     *         value created from the argument object.
     */
    @SuppressWarnings("unchecked")
    static final <T> Field<T> field(Object value, Field<T> field) {

        // Fields can be mixed with constant values
        if (value instanceof Field<?>) {
            return (Field<T>) value;
        }
        else {
            return val(value, field);
        }
    }

    /**
     * Be sure that a given object is a field.
     *
     * @param value The argument object
     * @param type The type to take the bind value type from
     * @return The argument object itself, if it is a {@link Field}, or a bind
     *         value created from the argument object.
     */
    @SuppressWarnings("unchecked")
    static final <T> Field<T> field(Object value, Class<T> type) {

        // Fields can be mixed with constant values
        if (value instanceof Field<?>) {
            return (Field<T>) value;
        }
        else {
            return val(value, type);
        }
    }

    /**
     * Be sure that a given object is a field.
     *
     * @param value The argument object
     * @param type The type to take the bind value type from
     * @return The argument object itself, if it is a {@link Field}, or a bind
     *         value created from the argument object.
     */
    @SuppressWarnings("unchecked")
    static final <T> Field<T> field(Object value, DataType<T> type) {

        // Fields can be mixed with constant values
        if (value instanceof Field<?>) {
            return (Field<T>) value;
        }
        else {
            return val(value, type);
        }
    }

    /**
     * Be sure that a given set of objects are fields.
     *
     * @param values The argument objects
     * @return The argument objects themselves, if they are {@link Field}s, or a bind
     *         values created from the argument objects.
     */
    static final List<Field<?>> fields(Object[] values) {
        List<Field<?>> result = new ArrayList<Field<?>>();

        if (values != null) {
            for (Object value : values) {
                result.add(field(value));
            }
        }

        return result;
    }

    /**
     * Be sure that a given set of objects are fields.
     *
     * @param values The argument objects
     * @param field The field to take the bind value types from
     * @return The argument objects themselves, if they are {@link Field}s, or a bind
     *         values created from the argument objects.
     */
    static final List<Field<?>> fields(Object[] values, Field<?> field) {
        List<Field<?>> result = new ArrayList<Field<?>>();

        if (values != null && field != null) {
            for (int i = 0; i < values.length; i++) {
                result.add(field(values[i], field));
            }
        }

        return result;
    }

    /**
     * Be sure that a given set of objects are fields.
     *
     * @param values The argument objects
     * @param fields The fields to take the bind value types from
     * @return The argument objects themselves, if they are {@link Field}s, or a bind
     *         values created from the argument objects.
     */
    static final List<Field<?>> fields(Object[] values, Field<?>[] fields) {
        List<Field<?>> result = new ArrayList<Field<?>>();

        if (values != null && fields != null) {
            for (int i = 0; i < values.length && i < fields.length; i++) {
                result.add(field(values[i], fields[i]));
            }
        }

        return result;
    }

    /**
     * Be sure that a given set of objects are fields.
     *
     * @param values The argument objects
     * @param type The type to take the bind value types from
     * @return The argument objects themselves, if they are {@link Field}s, or a bind
     *         values created from the argument objects.
     */
    static final List<Field<?>> fields(Object[] values, Class<?> type) {
        List<Field<?>> result = new ArrayList<Field<?>>();

        if (values != null && type != null) {
            for (int i = 0; i < values.length; i++) {
                result.add(field(values[i], type));
            }
        }

        return result;
    }

    /**
     * Be sure that a given set of objects are fields.
     *
     * @param values The argument objects
     * @param types The types to take the bind value types from
     * @return The argument objects themselves, if they are {@link Field}s, or a bind
     *         values created from the argument objects.
     */
    static final List<Field<?>> fields(Object[] values, Class<?>[] types) {
        List<Field<?>> result = new ArrayList<Field<?>>();

        if (values != null && types != null) {
            for (int i = 0; i < values.length && i < types.length; i++) {
                result.add(field(values[i], types[i]));
            }
        }

        return result;
    }

    /**
     * Be sure that a given set of objects are fields.
     *
     * @param values The argument objects
     * @param type The type to take the bind value types from
     * @return The argument objects themselves, if they are {@link Field}s, or a bind
     *         values created from the argument objects.
     */
    static final List<Field<?>> fields(Object[] values, DataType<?> type) {
        List<Field<?>> result = new ArrayList<Field<?>>();

        if (values != null && type != null) {
            for (int i = 0; i < values.length; i++) {
                result.add(field(values[i], type));
            }
        }

        return result;
    }

    /**
     * Be sure that a given set of objects are fields.
     *
     * @param values The argument objects
     * @param types The types to take the bind value types from
     * @return The argument objects themselves, if they are {@link Field}s, or a bind
     *         values created from the argument objects.
     */
    static final List<Field<?>> fields(Object[] values, DataType<?>[] types) {
        List<Field<?>> result = new ArrayList<Field<?>>();

        if (values != null && types != null) {
            for (int i = 0; i < values.length && i < types.length; i++) {
                result.add(field(values[i], types[i]));
            }
        }

        return result;
    }

    /**
     * Turn a {@link Record} into a {@link Map}
     */
    static final Map<Field<?>, Object> map(Record record) {
        Map<Field<?>, Object> result = new LinkedHashMap<Field<?>, Object>();
        int size = record.size();

        for (int i = 0; i < size; i++) {
            result.put(record.getField(i), record.getValue(i));
        }

        return result;
    }

    /**
     * Extract the first item from an iterable or <code>null</code>, if there is
     * no such item, or if iterable itself is <code>null</code>
     */
    static final <T> T first(Iterable<? extends T> iterable) {
        if (iterable == null) {
            return null;
        }
        else {
            Iterator<? extends T> iterator = iterable.iterator();

            if (iterator.hasNext()) {
                return iterator.next();
            }
            else {
                return null;
            }
        }
    }

    /**
     * Get the only element from a list or <code>null</code>, or throw an
     * exception
     *
     * @param list The list
     * @return The only element from the list or <code>null</code>
     * @throws InvalidResultException Thrown if the list contains more than one
     *             element
     */
    static final <R extends Record> R filterOne(List<R> list) throws InvalidResultException {
        int size = list.size();

        if (size == 1) {
            return list.get(0);
        }
        else if (size > 1) {
            throw new InvalidResultException("Too many rows selected : " + size);
        }

        return null;
    }

    /**
     * Get the only element from a cursor or <code>null</code>, or throw an
     * exception
     *
     * @param cursor The cursor
     * @return The only element from the cursor or <code>null</code>
     * @throws InvalidResultException Thrown if the cursor returns more than one
     *             element
     */
    static final <R extends Record> R fetchOne(Cursor<R> cursor) throws InvalidResultException {
        R record = cursor.fetchOne();

        if (cursor.hasNext()) {
            throw new InvalidResultException("Cursor returned more than one result");
        }

        return record;
    }

    /**
     * Render and bind a list of {@link QueryPart} to plain SQL
     * <p>
     * This will perform two actions:
     * <ul>
     * <li>When {@link RenderContext} is provided, it will render plain SQL to
     * the context, substituting {numbered placeholders} and bind values if
     * {@link RenderContext#inline()} is set</li>
     * <li>When {@link BindContext} is provided, it will bind the list of
     * {@link QueryPart} according to the {numbered placeholders} and bind
     * values in the sql string</li>
     * </ul>
     */
    static final void renderAndBind(RenderContext render, BindContext bind, String sql, List<QueryPart> substitutes) {
        int substituteIndex = 0;
        char[] sqlChars = sql.toCharArray();

        // [#1593] Create a dummy renderer if we're in bind mode
        if (render == null) render = new DefaultRenderContext(bind);

        for (int i = 0; i < sqlChars.length; i++) {

            // [#1797] Skip content inside of single-line comments, e.g.
            // select 1 x -- what's this ?'?
            // from t_book -- what's that ?'?
            // where id = ?
            if (peek(sqlChars, i, "--")) {

                // Consume the complete comment
                for (; i < sqlChars.length && sqlChars[i] != '\r' && sqlChars[i] != '\n'; render.sql(sqlChars[i++]));

                // Consume the newline character
                render.sql(sqlChars[i]);
            }

            // [#1797] Skip content inside of multi-line comments, e.g.
            // select 1 x /* what's this ?'?
            // I don't know ?'? */
            // from t_book where id = ?
            else if (peek(sqlChars, i, "/*")) {

                // Consume the complete comment
                for (; !peek(sqlChars, i, "*/"); render.sql(sqlChars[i++]));

                // Consume the comment delimiter
                render.sql(sqlChars[i++]);
                render.sql(sqlChars[i]);
            }

            // [#1031] [#1032] Skip ? inside of string literals, e.g.
            // insert into x values ('Hello? Anybody out there?');
            else if (sqlChars[i] == '\'') {

                // Consume the initial string literal delimiter
                render.sql(sqlChars[i++]);

                // Consume the whole string literal
                for (;;) {

                    // Consume an escaped apostrophe
                    if (peek(sqlChars, i, "''")) {
                        render.sql(sqlChars[i++]);
                    }

                    // Break on the terminal string literal delimiter
                    else if (peek(sqlChars, i, "'")) {
                        break;
                    }

                    // Consume string literal content
                    render.sql(sqlChars[i++]);
                }

                // Consume the terminal string literal delimiter
                render.sql(sqlChars[i]);
            }

            // Inline bind variables only outside of string literals
            else if (sqlChars[i] == '?' && substituteIndex < substitutes.size()) {
                QueryPart substitute = substitutes.get(substituteIndex++);

                if (render.inline()) {
                    render.sql(substitute);
                }
                else {
                    render.sql(sqlChars[i]);
                }

                if (bind != null) {
                    bind.bind(substitute);
                }
            }

            // [#1432] Inline substitues for {numbered placeholders} outside of string literals
            else if (sqlChars[i] == '{') {

                // [#1461] Be careful not to match any JDBC escape syntax
                if (JDBC_ESCAPE_PATTERN.matcher(sql.substring(i)).matches()) {
                    render.sql(sqlChars[i]);
                }

                // Consume the whole token
                else {
                    int start = ++i;
                    for (; i < sqlChars.length && sqlChars[i] != '}'; i++);
                    int end = i;

                    String token = sql.substring(start, end);

                    // Try getting the {numbered placeholder}
                    try {
                        QueryPart substitute = substitutes.get(Integer.valueOf(token));
                        render.sql(substitute);

                        if (bind != null) {
                            bind.bind(substitute);
                        }
                    }

                    // If the above failed, then we're dealing with a {keyword}
                    catch (NumberFormatException e) {
                        render.keyword(token);
                    }
                }
            }

            // Any other character
            else {
                render.sql(sqlChars[i]);
            }
        }
    }

    /**
     * Peek for a string at a given <code>index</code> of a <code>char[]</code>
     *
     * @param sqlChars The char array to peek into
     * @param index The index within the char array to peek for a string
     * @param peek The string to peek for
     */
    static final boolean peek(char[] sqlChars, int index, String peek) {
        char[] peekArray = peek.toCharArray();

        for (int i = 0; i < peekArray.length; i++) {
            if (index + i >= sqlChars.length) {
                return false;
            }
            if (sqlChars[index + i] != peekArray[i]) {
                return false;
            }
        }

        return true;
    }

    /**
     * Create {@link QueryPart} objects from bind values or substitutes
     */
    static final List<QueryPart> queryParts(Object... substitutes) {
        // [#724] When bindings is null, this is probably due to API-misuse
        // The user probably meant new Object[] { null }
        if (substitutes == null) {
            return queryParts(new Object[] { null });
        }
        else {
            List<QueryPart> result = new ArrayList<QueryPart>();

            for (Object substitute : substitutes) {

                // [#1432] Distinguish between QueryParts and other objects
                if (substitute instanceof QueryPart) {
                    result.add((QueryPart) substitute);
                }
                else {
                    @SuppressWarnings("unchecked")
                    Class<Object> type = (Class<Object>) (substitute != null ? substitute.getClass() : Object.class);
                    result.add(new Val<Object>(substitute, Factory.getDataType(type)));
                }
            }

            return result;
        }
    }

    /**
     * Render a list of names of the <code>NamedQueryParts</code> contained in
     * this list.
     */
    static final void fieldNames(RenderContext context, Collection<? extends Field<?>> list) {
        String separator = "";

        for (Field<?> field : list) {
            context.sql(separator).literal(field.getName());

            separator = ", ";
        }
    }

    /**
     * Render a list of names of the <code>NamedQueryParts</code> contained in
     * this list.
     */
    static final void tableNames(RenderContext context, Collection<? extends Table<?>> list) {
        String separator = "";

        for (Table<?> table : list) {
            context.sql(separator).literal(table.getName());

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
    static final DataAccessException translate(String sql, SQLException e) {
        String message = "SQL [" + sql + "]; " + e.getMessage();
        return new DataAccessException(message, e);
    }

    // -------------------------------------------------------------------------
    // This section is merged from org.jooq.tools.jdbc.JDBCUtils
    // vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv

    /**
     * Safely close a statement
     */
    static final void safeClose(ExecuteListener listener, ExecuteContext ctx) {
        safeClose(listener, ctx, false);
    }

    /**
     * Safely close a statement
     */
    static final void safeClose(ExecuteListener listener, ExecuteContext ctx, boolean keepStatement) {
        safeClose(ctx.resultSet());
        if (!keepStatement)
            safeClose(ctx.statement());

        // [#1868] TODO: This needs to be called in fetchLazy(), too
        listener.end(ctx);

        // [#1326] Clean up any potentially remaining temporary lobs
        DefaultExecuteContext.clean();
    }

    /**
     * Safely close a statement
     * <p>
     * This method will silently ignore if <code>statement</code> is
     * <code>null</code>, or if {@link Statement#close()} throws an exception.
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
     * <p>
     * This method will silently ignore if <code>resultSet</code> is
     * <code>null</code>, or if {@link ResultSet#close()} throws an exception.
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
     * Safely close a result set and / or a statement
     * <p>
     * This method will silently ignore if <code>resultSet</code> or
     * <code>statement</code> is <code>null</code>, or if
     * {@link ResultSet#close()} or {@link Statement#close()} throws an
     * exception.
     */
    static final void safeClose(ResultSet resultSet, PreparedStatement statement) {
        safeClose(resultSet);
        safeClose(statement);
    }

    /**
     * Safely free a blob
     * <p>
     * This method will silently ignore if <code>blob</code> is
     * <code>null</code>, or if {@link Blob#free()} throws an exception.
     */
    static final void safeFree(Blob blob) {
        if (blob != null) {
            try {
                blob.free();
            }
            catch (Exception ignore) {}
        }
    }

    /**
     * Safely free a clob
     * <p>
     * This method will silently ignore if <code>clob</code> is
     * <code>null</code>, or if {@link Clob#free()} throws an exception.
     */
    static final void safeFree(Clob clob) {
        if (clob != null) {
            try {
                clob.free();
            }
            catch (Exception ignore) {}
        }
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    // This section is merged from org.jooq.tools.jdbc.JDBCUtils
    // -------------------------------------------------------------------------

    /**
     * Extract an underlying connection
     */
    static final Connection getDriverConnection(Configuration configuration) {
        if (configuration != null) {
            Connection connection = configuration.getConnection();

            if (connection != null) {

                // If the connection is wrapped by jOOQ, extract the underlying
                // connection
                if (connection.getClass() == DataSourceConnection.class) {
                    connection = ((DataSourceConnection) connection).getDelegate();
                }

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
                    result.add(accessible(member));
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

        // [#1942] Caching these values before the field-loop significantly
        // accerates POJO mapping
        String camelCaseLC = StringUtils.toCamelCaseLC(name);

        for (java.lang.reflect.Field member : getInstanceMembers(type)) {
            if (name.equals(member.getName())) {
                result.add(accessible(member));
            }
            else if (camelCaseLC.equals(member.getName())) {
                result.add(accessible(member));
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
                    result.add(accessible(method));
                }

                // Annotated getter with matching setter
                else if (method.getParameterTypes().length == 0) {
                    String m = method.getName();

                    if (m.startsWith("get") || m.startsWith("is")) {
                        try {
                            Method setter = type.getMethod("set" + m.substring(3), method.getReturnType());

                            // Setter annotation is more relevant
                            if (setter.getAnnotation(Column.class) == null) {
                                result.add(accessible(setter));
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
                    return accessible(method);
                }

                // Annotated setter with matching getter
                else if (method.getParameterTypes().length == 1) {
                    String m = method.getName();

                    if (m.startsWith("set")) {
                        try {
                            Method getter = type.getMethod("get" + m.substring(3));

                            // Getter annotation is more relevant
                            if (getter.getAnnotation(Column.class) == null) {
                                return accessible(getter);
                            }
                        }
                        catch (NoSuchMethodException ignore) {}

                        try {
                            Method getter = type.getMethod("is" + m.substring(3));

                            // Getter annotation is more relevant
                            if (getter.getAnnotation(Column.class) == null) {
                                return accessible(getter);
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

        // [#1942] Caching these values before the method-loop significantly
        // accerates POJO mapping
        String camelCase = StringUtils.toCamelCase(name);
        String camelCaseLC = StringUtils.toLC(camelCase);

        for (Method method : getInstanceMethods(type)) {
            Class<?>[] parameterTypes = method.getParameterTypes();

            // [#1510] [#1819] Avoid potentially overloaded setters that were
            // generated for foreign key records
            if (parameterTypes.length == 1 && !Record.class.isAssignableFrom(parameterTypes[0])) {
                if (name.equals(method.getName())) {
                    result.add(accessible(method));
                }
                else if (camelCaseLC.equals(method.getName())) {
                    result.add(accessible(method));
                }
                else if (("set" + name).equals(method.getName())) {
                    result.add(accessible(method));
                }
                else if (("set" + camelCase).equals(method.getName())) {
                    result.add(accessible(method));
                }
            }
        }

        return result;
    }


    /**
     * Get the first getter method matching a given column name
     */
    static final Method getMatchingGetter(Class<?> type, String name) {

        // [#1942] Caching these values before the method-loop significantly
        // accerates POJO mapping
        String camelCase = StringUtils.toCamelCase(name);
        String camelCaseLC = StringUtils.toLC(camelCase);

        for (Method method : getInstanceMethods(type)) {
            if (method.getParameterTypes().length == 0) {
                if (name.equals(method.getName())) {
                    return accessible(method);
                }
                else if (camelCaseLC.equals(method.getName())) {
                    return accessible(method);
                }
                else if (("get" + name).equals(method.getName())) {
                    return accessible(method);
                }
                else if (("get" + camelCase).equals(method.getName())) {
                    return accessible(method);
                }
                else if (("is" + name).equals(method.getName())) {
                    return accessible(method);
                }
                else if (("is" + camelCase).equals(method.getName())) {
                    return accessible(method);
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
     * Get a property name associated with a getter/setter method name.
     */
    static final String getPropertyName(String methodName) {
        String name = methodName;

        if (name.startsWith("is") && name.length() > 2) {
            name = name.substring(2, 3).toLowerCase() + name.substring(3);
        }
        else if (name.startsWith("get") && name.length() > 3) {
            name = name.substring(3, 4).toLowerCase() + name.substring(4);
        }
        else if (name.startsWith("set") && name.length() > 3) {
            name = name.substring(3, 4).toLowerCase() + name.substring(4);
        }

        return name;
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
        org.jooq.SchemaMapping mapping = configuration.getSchemaMapping();

        if (mapping != null) {
            return mapping.map(schema);
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
        org.jooq.SchemaMapping mapping = configuration.getSchemaMapping();

        if (mapping != null) {
            return mapping.map(table);
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

            // [#1572] Loading classes like this is needed for class loading to
            // work with OSGi. [#1578] The current implementation of loading
            // ExecuteListeners will be reworked in jOOQ 3.0, though
            Class<?> type = Thread.currentThread().getContextClassLoader().loadClass(name);
            return (ExecuteListener) Reflect.accessible(type.getDeclaredConstructor()).newInstance();
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

    /**
     * Utility method to check whether a field is a {@link Param}
     */
    static final boolean isVal(Field<?> field) {
        return field instanceof Param;
    }

    /**
     * Utility method to extract a value from a field
     */
    static final <T> T extractVal(Field<T> field) {
        if (isVal(field)) {
            return ((Param<T>) field).getValue();
        }
        else {
            return null;
        }
    }

    /**
     * Add primary key conditions to a query
     */
    @SuppressWarnings("deprecation")
    static final void addConditions(org.jooq.ConditionProvider query, Record record, Field<?>... keys) {
        for (Field<?> field : keys) {
            addCondition(query, record, field);
        }
    }

    /**
     * Add a field condition to a query
     */
    @SuppressWarnings("deprecation")
    static final <T> void addCondition(org.jooq.ConditionProvider provider, Record record, Field<T> field) {
        provider.addConditions(field.equal(record.getValue(field)));
    }
}
