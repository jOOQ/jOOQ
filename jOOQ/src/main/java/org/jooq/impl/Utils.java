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

import static java.lang.Boolean.FALSE;
import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.impl.DSL.escape;
import static org.jooq.impl.DSL.getDataType;
import static org.jooq.impl.DSL.nullSafe;
import static org.jooq.impl.DSL.val;
import static org.jooq.impl.DefaultExecuteContext.localConnection;
import static org.jooq.tools.jdbc.JDBCUtils.safeFree;
import static org.jooq.tools.jdbc.JDBCUtils.wasNull;
import static org.jooq.tools.reflect.Reflect.accessible;
import static org.jooq.tools.reflect.Reflect.on;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.jooq.ArrayRecord;
import org.jooq.Attachable;
import org.jooq.AttachableInternal;
import org.jooq.BindContext;
import org.jooq.Configuration;
import org.jooq.Converter;
import org.jooq.Cursor;
import org.jooq.DSLContext;
import org.jooq.DataType;
import org.jooq.EnumType;
import org.jooq.ExecuteContext;
import org.jooq.ExecuteListener;
import org.jooq.Field;
import org.jooq.Param;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.RenderContext;
import org.jooq.Result;
import org.jooq.Row;
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
import org.jooq.tools.JooqLogger;
import org.jooq.tools.StringUtils;
import org.jooq.tools.jdbc.JDBCUtils;
import org.jooq.tools.reflect.Reflect;
import org.jooq.types.DayToSecond;
import org.jooq.types.UByte;
import org.jooq.types.UInteger;
import org.jooq.types.ULong;
import org.jooq.types.UNumber;
import org.jooq.types.UShort;
import org.jooq.types.YearToMonth;
import org.jooq.util.postgres.PostgresUtils;

/**
 * General internal jOOQ utilities
 *
 * @author Lukas Eder
 */
final class Utils {

    static final JooqLogger      log                                          = JooqLogger.getLogger(Utils.class);

    // ------------------------------------------------------------------------
    // Some constants for use with Context.data()
    // ------------------------------------------------------------------------

    /**
     * [#1537] This constant is used internally by jOOQ to omit the RETURNING
     * clause in {@link DSLContext#batchStore(UpdatableRecord...)} calls for
     * {@link SQLDialect#POSTGRES}.
     */
    static final String          DATA_OMIT_RETURNING_CLAUSE                   = "org.jooq.configuration.omit-returning-clause";

    /**
     * [#1905] This constant is used internally by jOOQ to indicate to
     * subqueries that they're being rendered in the context of a row value
     * expression predicate.
     * <p>
     * This is particularly useful for H2, which pretends that ARRAYs and RVEs
     * are the same
     */
    static final String          DATA_ROW_VALUE_EXPRESSION_PREDICATE_SUBQUERY = "org.jooq.configuration.row-value-expression-subquery";

    /**
     * [#1296] This constant is used internally by jOOQ to indicate that
     * {@link ResultSet} rows must be locked to simulate a
     * <code>FOR UPDATE</code> clause.
     */
    static final String          DATA_LOCK_ROWS_FOR_UPDATE                    = "org.jooq.configuration.lock-rows-for-update";

    /**
     * [#1520] Count the number of bind values, and potentially enforce a static
     * statement.
     */
    static final String          DATA_COUNT_BIND_VALUES                       = "org.jooq.configuration.count-bind-values";

    /**
     * [#1520] Enforce executing static statements.
     * <p>
     * Some SQL dialects support only a limited amount of bind variables. This
     * flag is set when static statements have too many bind variables. Known
     * values are:
     * <ul>
     * <li>{@link SQLDialect#ASE} : 2000</li>
     * <li>{@link SQLDialect#INGRES} : 1024</li>
     * <li>{@link SQLDialect#SQLITE} : 999</li>
     * <li>{@link SQLDialect#SQLSERVER} : 2100</li>
     * </ul>
     */
    static final String          DATA_FORCE_STATIC_STATEMENT                  = "org.jooq.configuration.force-static-statement";

    // ------------------------------------------------------------------------
    // Other constants
    // ------------------------------------------------------------------------

    /**
     * The default escape character for <code>[a] LIKE [b] ESCAPE [...]</code>
     * clauses.
     */
    static final char            ESCAPE                                       = '!';

    /**
     * Indicating whether JPA (<code>javax.persistence</code>) is on the
     * classpath.
     */
    private static Boolean       isJPAAvailable;

    /**
     * A pattern for the dash line syntax
     */
    private static final Pattern DASH_PATTERN                                 = Pattern.compile("(-+)");                                ;

    /**
     * A pattern for the dash line syntax
     */
    private static final Pattern PLUS_PATTERN                                 = Pattern.compile("\\+(-+)(?=\\+)");                                ;

    /**
     * A pattern for the JDBC escape syntax
     */
    private static final Pattern JDBC_ESCAPE_PATTERN                          = Pattern.compile("\\{(fn|d|t|ts)\\b.*");

    // ------------------------------------------------------------------------
    // XXX: Record constructors and related methods
    // ------------------------------------------------------------------------

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
    static final <R extends Record> R newRecord(Class<R> type, Field<?>[] fields) {
        return newRecord(type, fields, null);
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
        return newRecord(type.getRecordType(), type.fields(), configuration);
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
        return newRecord(type.getRecordType(), type.fields(), configuration);
    }

    /**
     * Create a new record
     */
    static final <R extends Record> R newRecord(Class<R> type, Collection<? extends Field<?>> fields, Configuration configuration) {
        return newRecord(type, fieldArray(fields), configuration);
    }

    /**
     * Create a new record
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    static final <R extends Record> R newRecord(Class<R> type, Field<?>[] fields, Configuration configuration) {
        try {
            R result;

            // An ad-hoc type resulting from a JOIN or arbitrary SELECT
            if (type == RecordImpl.class || type == Record.class) {
                result = (R) new RecordImpl(fields);
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

    /**
     * Get an attachable's configuration or a new {@link DefaultConfiguration}
     * if <code>null</code>.
     */
    static Configuration configuration(AttachableInternal attachable) {
        return configuration(attachable.configuration());
    }

    /**
     * Get an configuration or a new {@link DefaultConfiguration} if
     * <code>null</code>.
     */
    static Configuration configuration(Configuration configuration) {
        return configuration != null ? configuration : new DefaultConfiguration();
    }

    private static final boolean attachRecords(Configuration configuration) {
        if (configuration != null) {
            Settings settings = configuration.settings();

            if (settings != null) {
                return !FALSE.equals(settings.isAttachRecords());
            }
        }

        return true;
    }

    static final Field<?>[] fieldArray(Collection<? extends Field<?>> fields) {
        return fields == null ? null : fields.toArray(new Field[fields.size()]);
    }

    // ------------------------------------------------------------------------
    // XXX: Data-type related methods
    // ------------------------------------------------------------------------

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
     * Create a new array
     */
    static final <T> T[] array(T... array) {
        return array;
    }

    /**
     * Use this rather than {@link Arrays#asList(Object...)} for
     * <code>null</code>-safety
     */
    static final <T> List<T> list(T... array) {
        return array == null ? Collections.<T>emptyList() : Arrays.asList(array);
    }

    /**
     * Turn a {@link Record} into a {@link Map}
     */
    static final Map<Field<?>, Object> map(Record record) {
        Map<Field<?>, Object> result = new LinkedHashMap<Field<?>, Object>();
        int size = record.size();

        for (int i = 0; i < size; i++) {
            result.put(record.field(i), record.getValue(i));
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
     * exception.
     * <p>
     * [#2373] This method will always close the argument cursor, as it is
     * supposed to be completely consumed by this method.
     *
     * @param cursor The cursor
     * @return The only element from the cursor or <code>null</code>
     * @throws InvalidResultException Thrown if the cursor returns more than one
     *             element
     */
    static final <R extends Record> R fetchOne(Cursor<R> cursor) throws InvalidResultException {
        try {
            R record = cursor.fetchOne();

            if (cursor.hasNext()) {
                throw new InvalidResultException("Cursor returned more than one result");
            }

            return record;
        }
        finally {
            cursor.close();
        }
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
        if (render == null) render = new DefaultRenderContext(bind.configuration());

        for (int i = 0; i < sqlChars.length; i++) {

            // [#1797] Skip content inside of single-line comments, e.g.
            // select 1 x -- what's this ?'?
            // from t_book -- what's that ?'?
            // where id = ?
            if (peek(sqlChars, i, "--")) {

                // Consume the complete comment
                for (; i < sqlChars.length && sqlChars[i] != '\r' && sqlChars[i] != '\n'; render.sql(sqlChars[i++]));

                // Consume the newline character
                if (i < sqlChars.length) render.sql(sqlChars[i]);
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

                if (render.paramType() == INLINED) {
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
                    result.add(new Val<Object>(substitute, DSL.getDataType(type)));
                }
            }

            return result;
        }
    }

    /**
     * Render a list of names of the <code>NamedQueryParts</code> contained in
     * this list.
     */
    static final void fieldNames(RenderContext context, Fields<?> fields) {
        fieldNames(context, list(fields.fields));
    }

    /**
     * Render a list of names of the <code>NamedQueryParts</code> contained in
     * this list.
     */
    static final void fieldNames(RenderContext context, Field<?>... fields) {
        fieldNames(context, list(fields));
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
    static final void tableNames(RenderContext context, Table<?>... list) {
        tableNames(context, list(list));
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

    @SuppressWarnings("unchecked")
    static final <T> T[] combine(T[] array, T value) {
        T[] result = (T[]) java.lang.reflect.Array.newInstance(array.getClass().getComponentType(), array.length + 1);

        System.arraycopy(array, 0, result, 0, array.length);
        result[array.length] = value;

        return result;
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
        safeClose(listener, ctx, keepStatement, true);
    }

    /**
     * Safely close a statement
     */
    static final void safeClose(ExecuteListener listener, ExecuteContext ctx, boolean keepStatement, boolean keepResultSet) {
        // [#2523] Set JDBC objects to null, to prevent repeated closing
    	JDBCUtils.safeClose(ctx.resultSet());
        ctx.resultSet(null);

        // [#385] Close statements only if not requested to keep open
        if (!keepStatement) {
            JDBCUtils.safeClose(ctx.statement());
            ctx.statement(null);
        }

        // [#1868] [#2373] Terminate ExecuteListener lifecycle, if needed
        if (keepResultSet)
            listener.end(ctx);

        // [#1326] Clean up any potentially remaining temporary lobs
        DefaultExecuteContext.clean();
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
        org.jooq.SchemaMapping mapping = configuration.schemaMapping();

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
    static final <R extends Record> Table<R> getMappedTable(Configuration configuration, Table<R> table) {
        org.jooq.SchemaMapping mapping = configuration.schemaMapping();

        if (mapping != null) {
            return mapping.map(table);
        }
        else {
            return table;
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

    /**
     * Extract the configuration from an attachable.
     */
    static final Configuration getConfiguration(Attachable attachable) {
        if (attachable instanceof AttachableInternal) {
            return ((AttachableInternal) attachable).configuration();
        }

        return null;
    }

    // ------------------------------------------------------------------------
    // XXX: Reflection utilities used for POJO mapping
    // ------------------------------------------------------------------------

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

            if (parameterTypes.length == 1) {
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

    // ------------------------------------------------------------------------
    // XXX: JDBC helper methods
    // ------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    static final <T> T getFromSQLInput(Configuration configuration, SQLInput stream, Field<T> field) throws SQLException {
        Class<T> type = field.getType();
        DataType<T> dataType = field.getDataType();

        if (type == Blob.class) {
            return (T) stream.readBlob();
        }
        else if (type == Boolean.class) {
            return (T) wasNull(stream, Boolean.valueOf(stream.readBoolean()));
        }
        else if (type == BigInteger.class) {
            BigDecimal result = stream.readBigDecimal();
            return (T) (result == null ? null : result.toBigInteger());
        }
        else if (type == BigDecimal.class) {
            return (T) stream.readBigDecimal();
        }
        else if (type == Byte.class) {
            return (T) wasNull(stream, Byte.valueOf(stream.readByte()));
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
                    safeFree(blob);
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
            return (T) wasNull(stream, Double.valueOf(stream.readDouble()));
        }
        else if (type == Float.class) {
            return (T) wasNull(stream, Float.valueOf(stream.readFloat()));
        }
        else if (type == Integer.class) {
            return (T) wasNull(stream, Integer.valueOf(stream.readInt()));
        }
        else if (type == Long.class) {
            return (T) wasNull(stream, Long.valueOf(stream.readLong()));
        }
        else if (type == Short.class) {
            return (T) wasNull(stream, Short.valueOf(stream.readShort()));
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
        else if (type == UUID.class) {
            return (T) Convert.convert(stream.readString(), UUID.class);
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
        else if (UDTRecord.class.isAssignableFrom(type)) {
            return (T) stream.readObject();
        }
        else {
            return (T) stream.readObject();
        }
    }

    static final <T> void writeToSQLOutput(SQLOutput stream, Field<T> field, T value) throws SQLException {
        Class<T> type = field.getType();
        DataType<T> dataType = field.getDataType();

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
        else if (type == UUID.class) {
            stream.writeString(value.toString());
        }
        else if (ArrayRecord.class.isAssignableFrom(type)) {

            // [#1544] We can safely assume that localConfiguration has been
            // set on DefaultBindContext, prior to serialising arrays to SQLOut
            ArrayRecord<?> arrayRecord = (ArrayRecord<?>) value;
            stream.writeArray(on(localConnection()).call("createARRAY", arrayRecord.getName(), arrayRecord.get()).<Array>get());
        }
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

    static final <T, U> U getFromResultSet(ExecuteContext ctx, Field<U> field, int index) throws SQLException {

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
    private static final <T> T getFromResultSet(ExecuteContext ctx, Class<T> type, int index) throws SQLException {

        ResultSet rs = ctx.resultSet();

        if (type == Blob.class) {
            return (T) rs.getBlob(index);
        }
        else if (type == Boolean.class) {
            return (T) wasNull(rs, Boolean.valueOf(rs.getBoolean(index)));
        }
        else if (type == BigInteger.class) {
            // The SQLite JDBC driver doesn't support BigDecimals
            if (ctx.configuration().dialect() == SQLDialect.SQLITE) {
                return Convert.convert(rs.getString(index), (Class<T>) BigInteger.class);
            }
            else {
                BigDecimal result = rs.getBigDecimal(index);
                return (T) (result == null ? null : result.toBigInteger());
            }
        }
        else if (type == BigDecimal.class) {
            // The SQLite JDBC driver doesn't support BigDecimals
            if (ctx.configuration().dialect() == SQLDialect.SQLITE) {
                return Convert.convert(rs.getString(index), (Class<T>) BigDecimal.class);
            }
            else {
                return (T) rs.getBigDecimal(index);
            }
        }
        else if (type == Byte.class) {
            return (T) wasNull(rs, Byte.valueOf(rs.getByte(index)));
        }
        else if (type == byte[].class) {
            return (T) rs.getBytes(index);
        }
        else if (type == Clob.class) {
            return (T) rs.getClob(index);
        }
        else if (type == Date.class) {
            return (T) getDate(ctx.configuration().dialect(), rs, index);
        }
        else if (type == Double.class) {
            return (T) wasNull(rs, Double.valueOf(rs.getDouble(index)));
        }
        else if (type == Float.class) {
            return (T) wasNull(rs, Float.valueOf(rs.getFloat(index)));
        }
        else if (type == Integer.class) {
            return (T) wasNull(rs, Integer.valueOf(rs.getInt(index)));
        }
        else if (type == Long.class) {
            return (T) wasNull(rs, Long.valueOf(rs.getLong(index)));
        }
        else if (type == Short.class) {
            return (T) wasNull(rs, Short.valueOf(rs.getShort(index)));
        }
        else if (type == String.class) {
            return (T) rs.getString(index);
        }
        else if (type == Time.class) {
            return (T) getTime(ctx.configuration().dialect(), rs, index);
        }
        else if (type == Timestamp.class) {
            return (T) getTimestamp(ctx.configuration().dialect(), rs, index);
        }
        else if (type == YearToMonth.class) {
            if (ctx.configuration().dialect() == POSTGRES) {
                Object object = rs.getObject(index);
                return (T) (object == null ? null : PostgresUtils.toYearToMonth(object));
            }
            else {
                String string = rs.getString(index);
                return (T) (string == null ? null : YearToMonth.valueOf(string));
            }
        }
        else if (type == DayToSecond.class) {
            if (ctx.configuration().dialect() == POSTGRES) {
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
        else if (type == UUID.class) {
            switch (ctx.configuration().dialect().family()) {

                // [#1624] Some JDBC drivers natively support the
                // java.util.UUID data type
                case H2:
                case POSTGRES: {
                    return (T) rs.getObject(index);
                }

                // Other SQL dialects deal with UUIDs as if they were CHAR(36)
                // even if they explicitly support them (UNIQUEIDENTIFIER)
                case SQLSERVER:
                case SYBASE:

                // Most databases don't have such a type. In this case, jOOQ
                // simulates the type
                default: {
                    return (T) Convert.convert(rs.getString(index), UUID.class);
                }
            }
        }

        // The type byte[] is handled earlier. byte[][] can be handled here
        else if (type.isArray()) {
            switch (ctx.configuration().dialect()) {
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
            return (T) getArrayRecord(ctx.configuration(), rs.getArray(index), (Class<? extends ArrayRecord<?>>) type);
        }
        else if (EnumType.class.isAssignableFrom(type)) {
            return getEnumType(type, rs.getString(index));
        }
        else if (UDTRecord.class.isAssignableFrom(type)) {
            switch (ctx.configuration().dialect()) {
                case POSTGRES:
                    return (T) pgNewUDTRecord(type, rs.getObject(index));
            }

            return (T) rs.getObject(index, DataTypes.udtRecords());
        }
        else if (Result.class.isAssignableFrom(type)) {
            ResultSet nested = (ResultSet) rs.getObject(index);
            return (T) DSL.using(ctx.configuration()).fetch(nested);
        }
        else {
            return (T) rs.getObject(index);
        }
    }

    private static final ArrayRecord<?> getArrayRecord(Configuration configuration, Array array, Class<? extends ArrayRecord<?>> type) throws SQLException {
        if (array == null) {
            return null;
        }
        else {
            // TODO: [#523] Use array record meta data instead
            ArrayRecord<?> record = Utils.newArrayRecord(type, configuration);
            record.set(array);
            return record;
        }
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

    @SuppressWarnings("unchecked")
    static final <T> T getFromStatement(ExecuteContext ctx, Class<T> type, int index) throws SQLException {
        CallableStatement stmt = (CallableStatement) ctx.statement();

        if (type == Blob.class) {
            return (T) stmt.getBlob(index);
        }
        else if (type == Boolean.class) {
            return (T) wasNull(stmt, Boolean.valueOf(stmt.getBoolean(index)));
        }
        else if (type == BigInteger.class) {
            BigDecimal result = stmt.getBigDecimal(index);
            return (T) (result == null ? null : result.toBigInteger());
        }
        else if (type == BigDecimal.class) {
            return (T) stmt.getBigDecimal(index);
        }
        else if (type == Byte.class) {
            return (T) wasNull(stmt, Byte.valueOf(stmt.getByte(index)));
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
            return (T) wasNull(stmt, Double.valueOf(stmt.getDouble(index)));
        }
        else if (type == Float.class) {
            return (T) wasNull(stmt, Float.valueOf(stmt.getFloat(index)));
        }
        else if (type == Integer.class) {
            return (T) wasNull(stmt, Integer.valueOf(stmt.getInt(index)));
        }
        else if (type == Long.class) {
            return (T) wasNull(stmt, Long.valueOf(stmt.getLong(index)));
        }
        else if (type == Short.class) {
            return (T) wasNull(stmt, Short.valueOf(stmt.getShort(index)));
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
            if (ctx.configuration().dialect() == POSTGRES) {
                Object object = stmt.getObject(index);
                return (T) (object == null ? null : PostgresUtils.toYearToMonth(object));
            }
            else {
                String string = stmt.getString(index);
                return (T) (string == null ? null : YearToMonth.valueOf(string));
            }
        }
        else if (type == DayToSecond.class) {
            if (ctx.configuration().dialect() == POSTGRES) {
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
        else if (type == UUID.class) {
            switch (ctx.configuration().dialect().family()) {

                // [#1624] Some JDBC drivers natively support the
                // java.util.UUID data type
                case H2:
                case POSTGRES: {
                    return (T) stmt.getObject(index);
                }

                // Other SQL dialects deal with UUIDs as if they were CHAR(36)
                // even if they explicitly support them (UNIQUEIDENTIFIER)
                case SQLSERVER:
                case SYBASE:

                // Most databases don't have such a type. In this case, jOOQ
                // simulates the type
                default: {
                    return (T) Convert.convert(stmt.getString(index), UUID.class);
                }
            }
        }

        // The type byte[] is handled earlier. byte[][] can be handled here
        else if (type.isArray()) {
            return (T) convertArray(stmt.getObject(index), (Class<? extends Object[]>)type);
        }
        else if (ArrayRecord.class.isAssignableFrom(type)) {
            return (T) getArrayRecord(ctx.configuration(), stmt.getArray(index), (Class<? extends ArrayRecord<?>>) type);
        }
        else if (EnumType.class.isAssignableFrom(type)) {
            return getEnumType(type, stmt.getString(index));
        }
        else if (UDTRecord.class.isAssignableFrom(type)) {
            switch (ctx.configuration().dialect()) {
                case POSTGRES:
                    return (T) pgNewUDTRecord(type, stmt.getObject(index));
            }

            return (T) stmt.getObject(index, DataTypes.udtRecords());
        }
        else if (Result.class.isAssignableFrom(type)) {
            ResultSet nested = (ResultSet) stmt.getObject(index);
            return (T) DSL.using(ctx.configuration()).fetch(nested);
        }
        else {
            return (T) stmt.getObject(index);
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
        else if (ArrayRecord.class.isAssignableFrom(type)) {
            // Not supported
        }
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
    private static final UDTRecord<?> pgNewUDTRecord(Class<?> type, Object object) throws SQLException {
        if (object == null) {
            return null;
        }

        @SuppressWarnings({ "unchecked", "rawtypes" })
        UDTRecord<?> record = (UDTRecord<?>) Utils.newRecord((Class) type);
        List<String> values = PostgresUtils.toPGObject(object.toString());

        Row row = record.fieldsRow();
        for (int i = 0; i < row.size(); i++) {
            pgSetValue(record, row.field(i), values.get(i));
        }

        return record;
    }

    /**
     * Workarounds for the unimplemented Postgres JDBC driver features
     */
    @SuppressWarnings("unchecked")
    private static final <T> T pgGetArray(ExecuteContext ctx, Class<T> type, int index) throws SQLException {

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

    static List<String[]> parseTXT(String string, String nullLiteral) {
        String[] strings = string.split("[\\r\\n]+");

        if (strings.length < 2) {
            throw new DataAccessException("String must contain at least two lines");
        }

        // [#2235] Distinguish between jOOQ's Result.format() and H2's format
        boolean formatted = (string.charAt(0) == '+');

        // In jOOQ's Result.format(), that's line number one:
        // 1: +----+------+----+
        // 2: |ABC |XYZ   |HEHE|
        // 3: +----+------+----+
        // 4: |Data|{null}|1234|
        // 5: +----+------+----+
        //    012345678901234567
        // resulting in
        // [{1,5} {6,12} {13,17}]
        if (formatted) {
            return parseTXTLines(nullLiteral, strings, PLUS_PATTERN, 0, 1, 3, strings.length - 1);
        }

        // In H2 format, that's line number two:
        // 1: ABC    XYZ     HEHE
        // 2: -----  ------- ----
        // 3: Data   {null}  1234
        //    0123456789012345678
        // resulting in
        // [{0,5} {7,14} {15,19}]
        else {
            return parseTXTLines(nullLiteral, strings, DASH_PATTERN, 1, 0, 2, strings.length);
        }
    }

    private static List<String[]> parseTXTLines(
            String nullLiteral,
            String[] strings,
            Pattern pattern,
            int matchLine,
            int headerLine,
            int dataLineStart,
            int dataLineEnd) {

        List<int[]> positions = new ArrayList<int[]>();
        Matcher m = pattern.matcher(strings[matchLine]);

        while (m.find()) {
            positions.add(new int[] { m.start(1), m.end(1) });
        }

        // Parse header line and data lines into string arrays
        List<String[]> result = new ArrayList<String[]>();
        parseTXTLine(positions, result, strings[headerLine], nullLiteral);

        for (int j = dataLineStart; j < dataLineEnd; j++) {
            parseTXTLine(positions, result, strings[j], nullLiteral);
        }
        return result;
    }

    private static void parseTXTLine(List<int[]> positions, List<String[]> result, String string, String nullLiteral) {
        String[] fields = new String[positions.size()];
        result.add(fields);
        int length = string.length();

        for (int i = 0; i < fields.length; i++) {
            int[] position = positions.get(i);

            if (position[0] < length) {
                fields[i] = string.substring(position[0], Math.min(position[1], length)).trim();
            }
            else {
                fields[i] = null;
            }

            if (StringUtils.equals(fields[i], nullLiteral)) {
                fields[i] = null;
            }
        }
    }
}