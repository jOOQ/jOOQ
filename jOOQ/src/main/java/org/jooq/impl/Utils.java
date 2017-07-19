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

import static java.lang.Boolean.FALSE;
import static org.jooq.SQLDialect.ACCESS;
import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.conf.ParamType.NAMED;
import static org.jooq.conf.SettingsTools.reflectionCaching;
import static org.jooq.conf.SettingsTools.updatablePrimaryKeys;
import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.escape;
import static org.jooq.impl.DSL.fieldByName;
import static org.jooq.impl.DSL.getDataType;
import static org.jooq.impl.DSL.nullSafe;
import static org.jooq.impl.DSL.val;
import static org.jooq.impl.DefaultExecuteContext.localConnection;
import static org.jooq.impl.DefaultExecuteContext.localTargetConnection;
import static org.jooq.impl.Identifiers.QUOTES;
import static org.jooq.impl.Identifiers.QUOTE_END_DELIMITER;
import static org.jooq.impl.Identifiers.QUOTE_END_DELIMITER_ESCAPED;
import static org.jooq.impl.Identifiers.QUOTE_START_DELIMITER;
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
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;
import java.sql.Statement;
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
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.jooq.ArrayRecord;
import org.jooq.Attachable;
import org.jooq.AttachableInternal;
import org.jooq.BindContext;
import org.jooq.Clause;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Context;
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
import org.jooq.RecordType;
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
import org.jooq.impl.Utils.Cache.CachedOperation;
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

    /**
     * [#2665] Omit the emission of clause events by {@link QueryPart}s.
     * <p>
     * Some {@link QueryPart}s may contain further {@link QueryPart}s for whom
     * {@link Clause} emission should be avoided. For example
     * {@link Clause#FIELD_REFERENCE} may contain a
     * {@link Clause#TABLE_REFERENCE}.
     */
    static final String          DATA_OMIT_CLAUSE_EVENT_EMISSION              = "org.jooq.configuration.omit-clause-event-emission";

    /**
     * [#2665] Wrap derived tables in parentheses.
     * <p>
     * Before allowing for hooking into the SQL transformation SPI, new
     * {@link RenderContext} instances could be created to "try" to render a
     * given SQL subclause before inserting it into the real SQL string. This
     * practice should no longer be pursued, as such "sub-renderers" will emit /
     * divert {@link Clause} events.
     */
    static final String          DATA_WRAP_DERIVED_TABLES_IN_PARENTHESES      = "org.jooq.configuration.wrap-derived-tables-in-parentheses";

    /**
     * [#2790] A locally scoped data map.
     * <p>
     * Sometimes, it is useful to have some information only available while
     * visiting QueryParts in the same context of the current subquery, e.g.
     * when communicating between SELECT and WINDOW clauses, as is required to
     * emulate #531.
     */
    static final String          DATA_LOCALLY_SCOPED_DATA_MAP                 = "org.jooq.configuration.locally-scoped-data-map";

    /**
     * [#531] The local window definitions.
     * <p>
     * The window definitions declared in the <code>WINDOW</code> clause are
     * needed in the <code>SELECT</code> clause when emulating them by inlining
     * window specifications.
     */
    static final String          DATA_WINDOW_DEFINITIONS                      = "org.jooq.configuration.local-window-definitions";

    /**
     * [#2744] Currently rendering the DB2 FINAL TABLE clause.
     * <p>
     * In DB2, a <code>FINAL TABLE (INSERT ...)</code> clause exists, which
     * corresponds to the PostgreSQL <code>INSERT .. RETURNING</code> clause.
     */
    static final String          DATA_RENDERING_DB2_FINAL_TABLE_CLAUSE        = "org.jooq.configuration.rendering-db2-final-table-clause";

    /**
     * [#1629] The {@link Connection#getAutoCommit()} flag value before starting
     * a new transaction.
     */
    static final String          DATA_DEFAULT_TRANSACTION_PROVIDER_AUTOCOMMIT = "org.jooq.configuration.default-transaction-provider-autocommit";

    /**
     * [#1629] The {@link Connection#getAutoCommit()} flag value before starting
     * a new transaction.
     */
    static final String          DATA_DEFAULT_TRANSACTION_PROVIDER_SAVEPOINTS = "org.jooq.configuration.default-transaction-provider-savepoints";

    /**
     * [#1629] The {@link DefaultConnectionProvider} instance to be used during
     * the transaction.
     */
    static final String          DATA_DEFAULT_TRANSACTION_PROVIDER_CONNECTION = "org.jooq.configuration.default-transaction-provider-connection-provider";

    /**
     * [#2965] These are {@link ConcurrentHashMap}s containing caches for
     * reflection information.
     * <p>
     * <code>new String()</code> is used to allow for synchronizing on these
     * objects.
     */
    static final String          DATA_REFLECTION_CACHE_GET_ANNOTATED_GETTER   = new String("org.jooq.configuration.reflection-cache.get-annotated-getter");
    static final String          DATA_REFLECTION_CACHE_GET_ANNOTATED_MEMBERS  = new String("org.jooq.configuration.reflection-cache.get-annotated-members");
    static final String          DATA_REFLECTION_CACHE_GET_ANNOTATED_SETTERS  = new String("org.jooq.configuration.reflection-cache.get-annotated-setters");
    static final String          DATA_REFLECTION_CACHE_GET_MATCHING_GETTER    = new String("org.jooq.configuration.reflection-cache.get-matching-getter");
    static final String          DATA_REFLECTION_CACHE_GET_MATCHING_MEMBERS   = new String("org.jooq.configuration.reflection-cache.get-matching-members");
    static final String          DATA_REFLECTION_CACHE_GET_MATCHING_SETTERS   = new String("org.jooq.configuration.reflection-cache.get-matching-setters");
    static final String          DATA_REFLECTION_CACHE_HAS_COLUMN_ANNOTATIONS = new String("org.jooq.configuration.reflection-cache.has-column-annotations");

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
     * [#3696] The maximum number of consumed exceptions in
     * {@link #consumeExceptions(Configuration, PreparedStatement, SQLException)}
     * helps prevent infinite loops and {@link OutOfMemoryError}.
     */
    private static int           maxConsumedExceptions                        = 256;
    private static int           maxConsumedResults                           = 65536;

    /**
     * A pattern for the dash line syntax
     */
    private static final Pattern DASH_PATTERN                                 = Pattern.compile("(-+)");

    /**
     * A pattern for the dash line syntax
     */
    private static final Pattern PLUS_PATTERN                                 = Pattern.compile("\\+(-+)(?=\\+)");

    /**
     * All characters that are matched by Java's interpretation of \s.
     * <p>
     * For a more accurate set of whitespaces, refer to
     * http://stackoverflow.com/a/4731164/521799. In the event of SQL
     * processing, it is probably safe to ignore most of those alternative
     * Unicode whitespaces.
     */
    private static final String   WHITESPACE                                  = " \t\n\u000B\f\r";

    /**
     * Acceptable prefixes for JDBC escape syntax.
     */
    private static final String[] JDBC_ESCAPE_PREFIXES                        = {
        "{fn ",
        "{d ",
        "{t ",
        "{ts "
    };

    // ------------------------------------------------------------------------
    // XXX: Record constructors and related methods
    // ------------------------------------------------------------------------

    /* [pro] */
    /**
     * Create a new Oracle-style VARRAY {@link ArrayRecord}
     */
    static final <R extends ArrayRecord<?>> R newArrayRecord(Class<R> type) {
        try {
            return type.newInstance();
        }
        catch (Exception e) {
            throw new IllegalStateException(
                "ArrayRecord type does not provide a constructor with signature ArrayRecord(FieldProvider) : " + type
                    + ". Exception : " + e.getMessage());

        }
    }

    /* [/pro] */
    /**
     * Create a new record
     */
    static final <R extends Record> RecordDelegate<R> newRecord(boolean fetched, Class<R> type) {
        return newRecord(fetched, type, null);
    }

    /**
     * Create a new record
     */
    static final <R extends Record> RecordDelegate<R> newRecord(boolean fetched, Class<R> type, Field<?>[] fields) {
        return newRecord(fetched, type, fields, null);
    }

    /**
     * Create a new record
     */
    static final <R extends Record> RecordDelegate<R> newRecord(boolean fetched, Table<R> type) {
        return newRecord(fetched, type, null);
    }

    /**
     * Create a new record
     */
    @SuppressWarnings("unchecked")
    static final <R extends Record> RecordDelegate<R> newRecord(boolean fetched, Table<R> type, Configuration configuration) {
        return (RecordDelegate<R>) newRecord(fetched, type.getRecordType(), type.fields(), configuration);
    }

    /**
     * Create a new UDT record
     */
    static final <R extends UDTRecord<R>> RecordDelegate<R> newRecord(boolean fetched, UDT<R> type) {
        return newRecord(fetched, type, null);
    }

    /**
     * Create a new UDT record
     */
    static final <R extends UDTRecord<R>> RecordDelegate<R> newRecord(boolean fetched, UDT<R> type, Configuration configuration) {
        return newRecord(fetched, type.getRecordType(), type.fields(), configuration);
    }

    /**
     * Create a new record
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    static final <R extends Record> RecordDelegate<R> newRecord(boolean fetched, Class<R> type, Field<?>[] fields, Configuration configuration) {
        try {
            R record;

            // An ad-hoc type resulting from a JOIN or arbitrary SELECT
            if (type == RecordImpl.class || type == Record.class) {
                record = (R) new RecordImpl(fields);
            }

            // Any generated record
            else {

                // [#919] Allow for accessing non-public constructors
                record = Reflect.accessible(type.getDeclaredConstructor()).newInstance();
            }

            // [#3300] Records that were fetched from the database
            if (record instanceof AbstractRecord)
                ((AbstractRecord) record).fetched = fetched;

            return new RecordDelegate<R>(configuration, record);
        }
        catch (Exception e) {
            throw new IllegalStateException("Could not construct new record", e);
        }
    }

    /**
     * [#2700] [#3582] If a POJO attribute is NULL, but the column is NOT NULL
     * then we should let the database apply DEFAULT values
     */
    static void resetChangedOnNotNull(Record record) {
        int size = record.size();

        for (int i = 0; i < size; i++)
            if (record.getValue(i) == null)
                if (!record.field(i).getDataType().nullable())
                    record.changed(i, false);
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

    /**
     * Get an attachable's configuration or a new {@link DefaultConfiguration}
     * if <code>null</code>.
     */
    static Configuration configuration(Attachable attachable) {
        return configuration(attachable instanceof AttachableInternal
            ? ((AttachableInternal) attachable).configuration()
            : null);
    }

    /**
     * Get a configuration or a new {@link DefaultConfiguration} if
     * <code>null</code>.
     */
    static Configuration configuration(Configuration configuration) {
        return configuration != null ? configuration : new DefaultConfiguration();
    }

    /**
     * Get a configuration's settings or default settings if the configuration
     * is <code>null</code>.
     */
    static Settings settings(Attachable attachable) {
        return configuration(attachable).settings();
    }

    /**
     * Get a configuration's settings or default settings if the configuration
     * is <code>null</code>.
     */
    static Settings settings(Configuration configuration) {
        return configuration(configuration).settings();
    }

    static final boolean attachRecords(Configuration configuration) {
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

    static final String[] fieldNames(int length) {
        String[] result = new String[length];

        for (int i = 0; i < length; i++)
            result[i] = "v" + i;

        return result;
    }

    static final String[] fieldNames(Field<?>[] fields) {
        String[] result = new String[fields.length];

        for (int i = 0; i < fields.length; i++)
            result[i] = fields[i].getName();

        return result;
    }

    static final Field<?>[] fields(int length) {
        Field<?>[] result = new Field[length];
        String[] names = fieldNames(length);

        for (int i = 0; i < length; i++)
            result[i] = fieldByName(names[i]);

        return result;
    }

    static final Field<?>[] aliasedFields(Field<?>[] fields, String[] aliases) {
        Field<?>[] result = new Field[fields.length];

        for (int i = 0; i < fields.length; i++)
            result[i] = fields[i].as(aliases[i]);

        return result;
    }

    static final Field<?>[] fieldsByName(String tableName, String[] fieldNames) {
        Field<?>[] result = new Field[fieldNames.length];

        for (int i = 0; i < fieldNames.length; i++)
            result[i] = fieldByName(tableName, fieldNames[i]);

        return result;
    }

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

    static final <T> List<Field<T>> inline(T[] values) {
        List<Field<T>> result = new ArrayList<Field<T>>();

        if (values != null) {
            for (T value : values) {
                result.add(DSL.inline(value));
            }
        }

        return result;
    }

    /**
     * Return a list of unqualified {@link Field}s.
     */
    static final List<Field<?>> unqualify(List<? extends Field<?>> fields) {
        QueryPartList<Field<?>> result = new QueryPartList<Field<?>>();

        for (Field<?> field : fields)
            result.add(fieldByName(field.getName()));

        return result;
    }

    /**
     * A utility method that fails with an exception if
     * {@link Row#indexOf(Field)} doesn't return any index.
     */
    static final int indexOrFail(Row row, Field<?> field) {
        int result = row.indexOf(field);

        if (result < 0)
            throw new IllegalArgumentException("Field (" + field + ") is not contained in Row " + row);

        return result;
    }

    /**
     * A utility method that fails with an exception if
     * {@link Row#indexOf(String)} doesn't return any index.
     */
    static final int indexOrFail(Row row, String fieldName) {
        int result = row.indexOf(fieldName);

        if (result < 0)
            throw new IllegalArgumentException("Field (" + fieldName + ") is not contained in Row " + row);

        return result;
    }

    /**
     * A utility method that fails with an exception if
     * {@link RecordType#indexOf(Field)} doesn't return any index.
     */
    static final int indexOrFail(RecordType<?> row, Field<?> field) {
        int result = row.indexOf(field);

        if (result < 0)
            throw new IllegalArgumentException("Field (" + field + ") is not contained in RecordType " + row);

        return result;
    }

    /**
     * A utility method that fails with an exception if
     * {@link RecordType#indexOf(String)} doesn't return any index.
     */
    static final int indexOrFail(RecordType<?> row, String fieldName) {
        int result = row.indexOf(fieldName);

        if (result < 0)
            throw new IllegalArgumentException("Field (" + fieldName + ") is not contained in RecordType " + row);

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
     * Visit each query part from a collection, given a context.
     */
    static final <C extends Context<? super C>> C visitAll(C ctx, Collection<? extends QueryPart> parts) {
        if (parts != null) {
            for (QueryPart part : parts) {
                ctx.visit(part);
            }
        }

        return ctx;
    }

    /**
     * Visit each query part from an array, given a context.
     */
    static final <C extends Context<? super C>> C visitAll(C ctx, QueryPart[] parts) {
        if (parts != null) {
            for (QueryPart part : parts) {
                ctx.visit(part);
            }
        }

        return ctx;
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
    @SuppressWarnings("null")
    static final void renderAndBind(Context<?> ctx, String sql, List<QueryPart> substitutes) {
        RenderContext render = (RenderContext) ((ctx instanceof RenderContext) ? ctx : null);
        BindContext   bind   = (BindContext)   ((ctx instanceof BindContext)   ? ctx : null);

        int substituteIndex = 0;
        char[] sqlChars = sql.toCharArray();

        // [#1593] Create a dummy renderer if we're in bind mode
        if (render == null) render = new DefaultRenderContext(bind.configuration());

        SQLDialect dialect = render.configuration().dialect();
        SQLDialect family = dialect.family();
        String[][] quotes = QUOTES.get(family);

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

            // [#3297] Skip ? inside of quoted identifiers, e.g.
            // update x set v = "Column Name with a ? (question mark)"
            else if (peekAny(sqlChars, i, quotes[QUOTE_START_DELIMITER])) {

                // Main identifier delimiter or alternative one?
                int delimiter = 0;
                for (int d = 0; d < quotes[QUOTE_START_DELIMITER].length; d++) {
                    if (peek(sqlChars, i, quotes[QUOTE_START_DELIMITER][d])) {
                        delimiter = d;
                        break;
                    }
                }

                // Consume the initial identifier delimiter
                for (int d = 0; d < quotes[QUOTE_START_DELIMITER][delimiter].length(); d++)
                    render.sql(sqlChars[i++]);

                // Consume the whole identifier
                for (;;) {

                    // Consume an escaped quote
                    if (peek(sqlChars, i, quotes[QUOTE_END_DELIMITER_ESCAPED][delimiter])) {
                        for (int d = 0; d < quotes[QUOTE_END_DELIMITER_ESCAPED][delimiter].length(); d++)
                            render.sql(sqlChars[i++]);
                    }

                    // Break on the terminal identifier delimiter
                    else if (peek(sqlChars, i, quotes[QUOTE_END_DELIMITER][delimiter])) {
                        break;
                    }

                    // Consume identifier content
                    render.sql(sqlChars[i++]);
                }

                // Consume the terminal identifier delimiter
                for (int d = 0; d < quotes[QUOTE_END_DELIMITER][delimiter].length(); d++) {
                    if (d > 0)
                        i++;

                    render.sql(sqlChars[i]);
                }
            }

            // Inline bind variables only outside of string literals
            else if (sqlChars[i] == '?' && substituteIndex < substitutes.size()) {
                QueryPart substitute = substitutes.get(substituteIndex++);

                if (render.paramType() == INLINED || render.paramType() == NAMED) {
                    render.visit(substitute);
                }
                else {
                    render.sql(sqlChars[i]);
                }

                if (bind != null) {
                    bind.visit(substitute);
                }
            }

            // [#1432] Inline substitues for {numbered placeholders} outside of string literals
            else if (sqlChars[i] == '{') {

                // [#1461] Be careful not to match any JDBC escape syntax
                if (peekAny(sqlChars, i, JDBC_ESCAPE_PREFIXES, true)) {
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
                        render.visit(substitute);

                        if (bind != null) {
                            bind.visit(substitute);
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
        return peek(sqlChars, index, peek, false);
    }

    /**
     * Peek for a string at a given <code>index</code> of a <code>char[]</code>
     *
     * @param sqlChars The char array to peek into
     * @param index The index within the char array to peek for a string
     * @param peek The string to peek for
     * @param anyWhitespace A whitespace character in <code>peekAny</code>
     *            represents "any" whitespace character as defined in
     *            {@link #WHITESPACE}, or in Java Regex "\s".
     */
    static final boolean peek(char[] sqlChars, int index, String peek, boolean anyWhitespace) {
        char[] peekArray = peek.toCharArray();

        peekArrayLoop:
        for (int i = 0; i < peekArray.length; i++) {
            if (index + i >= sqlChars.length) {
                return false;
            }
            if (sqlChars[index + i] != peekArray[i]) {

                // [#3430] In some cases, we don't care about the type of whitespace.
                if (anyWhitespace && peekArray[i] == ' ') {
                    for (int j = 0; j < WHITESPACE.length(); j++) {
                        if (sqlChars[index + i] == WHITESPACE.charAt(j)) {
                            continue peekArrayLoop;
                        }
                    }
                }

                return false;
            }
        }

        return true;
    }

    /**
     * Peek for several strings at a given <code>index</code> of a <code>char[]</code>
     *
     * @param sqlChars The char array to peek into
     * @param index The index within the char array to peek for a string
     * @param peekAny The strings to peek for
     */
    static final boolean peekAny(char[] sqlChars, int index, String[] peekAny) {
        return peekAny(sqlChars, index, peekAny, false);
    }

    /**
     * Peek for several strings at a given <code>index</code> of a
     * <code>char[]</code>
     *
     * @param sqlChars The char array to peek into
     * @param index The index within the char array to peek for a string
     * @param peekAny The strings to peek for
     * @param anyWhitespace A whitespace character in <code>peekAny</code>
     *            represents "any" whitespace character as defined in
     *            {@link #WHITESPACE}, or in Java Regex "\s".
     */
    static final boolean peekAny(char[] sqlChars, int index, String[] peekAny, boolean anyWhitespace) {
        for (String peek : peekAny)
            if (peek(sqlChars, index, peek, anyWhitespace))
                return true;

        return false;
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
    static final void fieldNames(Context<?> context, Fields<?> fields) {
        fieldNames(context, list(fields.fields));
    }

    /**
     * Render a list of names of the <code>NamedQueryParts</code> contained in
     * this list.
     */
    static final void fieldNames(Context<?> context, Field<?>... fields) {
        fieldNames(context, list(fields));
    }

    /**
     * Render a list of names of the <code>NamedQueryParts</code> contained in
     * this list.
     */
    static final void fieldNames(Context<?> context, Collection<? extends Field<?>> list) {
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
    static final void tableNames(Context<?> context, Table<?>... list) {
        tableNames(context, list(list));
    }

    /**
     * Render a list of names of the <code>NamedQueryParts</code> contained in
     * this list.
     */
    static final void tableNames(Context<?> context, Collection<? extends Table<?>> list) {
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
            PreparedStatement statement = ctx.statement();

            if (statement != null) {
                JDBCUtils.safeClose(statement);
                ctx.statement(null);
            }

            // [#3234] We must ensure that any connection we may still have will be released,
            // in the event of an exception
            else {
                Connection connection = localConnection();

                if (connection != null) {
                    ctx.configuration().connectionProvider().release(connection);
                }
            }
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
     * [#2591] Type-safely copy a value from one record to another, preserving flags.
     */
    static final <T> void copyValue(AbstractRecord target, Field<T> targetField, Record source, Field<?> sourceField) {
        DataType<T> targetType = targetField.getDataType();

        int targetIndex = indexOrFail(target.fieldsRow(), targetField);
        int sourceIndex = indexOrFail(source.fieldsRow(), sourceField);

        target.values[targetIndex] = targetType.convert(source.getValue(sourceIndex));
        target.originals[targetIndex] = targetType.convert(source.original(sourceIndex));
        target.changed.set(targetIndex, source.changed(sourceIndex));
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
        return escapeForLike(value, new DefaultConfiguration());
    }

    /**
     * Utility method to escape strings or "toString" other objects
     */
    static final Field<String> escapeForLike(Object value, Configuration configuration) {
        if (value != null && value.getClass() == String.class) {

            /* [pro] */
            if (configuration.dialect().family() == ACCESS) {
                return val("[" + value + "]");
            }
            else
            /* [/pro] */
            {
                return val(escape("" + value, ESCAPE));
            }
        }
        else {
            return val("" + value);
        }
    }

    /**
     * Utility method to escape string fields, or cast other fields
     */
    static final Field<String> escapeForLike(Field<?> field) {
        return escapeForLike(field, new DefaultConfiguration());
    }

    /**
     * Utility method to escape string fields, or cast other fields
     */
    @SuppressWarnings("unchecked")
    static final Field<String> escapeForLike(Field<?> field, Configuration configuration) {
        if (nullSafe(field).getDataType().isString()) {

            /* [pro] */
            if (configuration.dialect().family() == ACCESS) {
                return concat(DSL.inline("["), field, DSL.inline("]"));
            }
            else
            /* [/pro] */
            {
                return escape((Field<String>) field, ESCAPE);
            }
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

        // [#2764] If primary keys are allowed to be changed, the
        if (updatablePrimaryKeys(settings(record))) {
            provider.addConditions(condition(field, record.original(field)));
        }
        else {
            provider.addConditions(condition(field, record.getValue(field)));
        }
    }

    /**
     * Create a <code>null</code>-safe condition.
     */
    static final <T> Condition condition(Field<T> field, T value) {
        return (value == null) ? field.isNull() : field.eq(value);
    }

    // ------------------------------------------------------------------------
    // XXX: [#2965] Reflection cache
    // ------------------------------------------------------------------------

    /**
     * [#2965] This is a {@link Configuration}-based cache that can cache reflection information and other things
     */
    static class Cache {

        /**
         * A callback wrapping expensive operations.
         */
        static interface CachedOperation<V> {

            /**
             * An expensive operation.
             */
            V call();
        }

        /**
         * Run a {@link CachedOperation} in the context of a
         * {@link Configuration}.
         *
         * @param configuration The configuration that may cache the outcome of
         *            the {@link CachedOperation}.
         * @param operation The expensive operation.
         * @param type The cache type to be used.
         * @param keys The cache keys.
         * @return The cached value or the outcome of the cached operation.
         */
        @SuppressWarnings("unchecked")
        static final <V> V run(Configuration configuration, CachedOperation<V> operation, String type, Object... keys) {

            // If no configuration is provided take the default configuration that loads the default Settings
            if (configuration == null)
                configuration = new DefaultConfiguration();

            // Shortcut caching when the relevant Settings flag isn't set.
            if (!reflectionCaching(configuration.settings()))
                return operation.call();

            Map<Object, Object> cache = (Map<Object, Object>) configuration.data(type);
            if (cache == null) {

                // String synchronization is OK as all type literals were created using new String()
                synchronized (type) {
                    cache = (Map<Object, Object>) configuration.data(type);

                    if (cache == null) {
                        cache = new ConcurrentHashMap<Object, Object>();
                        configuration.data(type, cache);
                    }
                }
            }

            Object key = key(keys);
            Object result = cache.get(key);

            if (result == null) {

                synchronized (cache) {
                    result = cache.get(key);

                    if (result == null) {
                        result = operation.call();
                        cache.put(key, result == null ? NULL : result);
                    }
                }
            }

            return (V) (result == NULL ? null : result);
        }

        /**
         * A <code>null</code> placeholder to be put in {@link ConcurrentHashMap}.
         */
        private static final Object NULL = new Object();

        /**
         * Create a single-value or multi-value key for caching.
         */
        private static final Object key(final Object... key) {
            if (key == null || key.length == 0)
                return key;

            if (key.length == 1)
                return key[0];

            return new Key(key);
        }

        /**
         * A multi-value key for caching.
         */
        private static class Key {

            private final Object[] key;

            Key(Object[] key) {
                this.key = key;
            }

            @Override
            public int hashCode() {
                return Arrays.hashCode(key);
            }

            @Override
            public boolean equals(Object obj) {
                if (obj instanceof Key)
                    return Arrays.equals(key, ((Key) obj).key);

                return false;
            }

            @Override
            public String toString() {
                return Arrays.asList(key).toString();
            }
        }
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
    static final boolean hasColumnAnnotations(final Configuration configuration, final Class<?> type) {
        return Cache.run(configuration, new CachedOperation<Boolean>() {

            @Override
            public Boolean call() {
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

        }, DATA_REFLECTION_CACHE_HAS_COLUMN_ANNOTATIONS, type);
    }

    /**
     * Get all members annotated with a given column name
     */
    static final List<java.lang.reflect.Field> getAnnotatedMembers(final Configuration configuration, final Class<?> type, final String name) {
        return Cache.run(configuration, new CachedOperation<List<java.lang.reflect.Field>>() {

            @Override
            public List<java.lang.reflect.Field> call() {
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

        }, DATA_REFLECTION_CACHE_GET_ANNOTATED_MEMBERS, type, name);
    }

    /**
     * Get all members matching a given column name
     */
    static final List<java.lang.reflect.Field> getMatchingMembers(final Configuration configuration, final Class<?> type, final String name) {
        return Cache.run(configuration, new CachedOperation<List<java.lang.reflect.Field>>() {

            @Override
            public List<java.lang.reflect.Field> call() {
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

        }, DATA_REFLECTION_CACHE_GET_MATCHING_MEMBERS, type, name);
    }

    /**
     * Get all setter methods annotated with a given column name
     */
    static final List<Method> getAnnotatedSetters(final Configuration configuration, final Class<?> type, final String name) {
        return Cache.run(configuration, new CachedOperation<List<Method>>() {

            @Override
            public List<Method> call() {
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
                            String suffix = m.startsWith("get")
                                          ? m.substring(3)
                                          : m.startsWith("is")
                                          ? m.substring(2)
                                          : null;

                            if (suffix != null) {
                                try {
                                    Method setter = type.getMethod("set" + suffix, method.getReturnType());

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

        }, DATA_REFLECTION_CACHE_GET_ANNOTATED_SETTERS, type, name);
    }

    /**
     * Get the first getter method annotated with a given column name
     */
    static final Method getAnnotatedGetter(final Configuration configuration, final Class<?> type, final String name) {
        return Cache.run(configuration, new CachedOperation<Method>() {

            @Override
            public Method call() {
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

        }, DATA_REFLECTION_CACHE_GET_ANNOTATED_GETTER, type, name);
    }

    /**
     * Get all setter methods matching a given column name
     */
    static final List<Method> getMatchingSetters(final Configuration configuration, final Class<?> type, final String name) {
        return Cache.run(configuration, new CachedOperation<List<Method>>() {

            @Override
            public List<Method> call() {
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

        }, DATA_REFLECTION_CACHE_GET_MATCHING_SETTERS, type, name);
    }


    /**
     * Get the first getter method matching a given column name
     */
    static final Method getMatchingGetter(final Configuration configuration, final Class<?> type, final String name) {
        return Cache.run(configuration, new CachedOperation<Method>() {

            @Override
            public Method call() {
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

        }, DATA_REFLECTION_CACHE_GET_MATCHING_GETTER, type, name);
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

    /**
     * [#3011] [#3054] Consume additional exceptions if there are any and append
     * them to the <code>previous</code> exception's
     * {@link SQLException#getNextException()} list.
     */
    static final void consumeExceptions(Configuration configuration, PreparedStatement stmt, SQLException previous) {
        /* [pro] */
        // So far, this issue has been observed only with SQL Server
        switch (configuration.dialect().family()) {
            case SQLSERVER:
                consumeLoop: for (int i = 0; i < maxConsumedExceptions; i++)
                    try {
                        if (!stmt.getMoreResults() && stmt.getUpdateCount() == -1)
                            break consumeLoop;
                    }
                    catch (SQLException e) {
                        previous.setNextException(e);
                        previous = e;
                    }
        }
        /* [/pro] */
    }

    /**
     * [#3076] Consume warnings from a {@link Statement} and notify listeners.
     */
    static final void consumeWarnings(ExecuteContext ctx, ExecuteListener listener) throws SQLException {
        ctx.sqlWarning(ctx.statement().getWarnings());

        if (ctx.sqlWarning() != null)
            listener.warning(ctx);
    }

    /* [pro] */
    /**
     * [#3681] Consume all {@link ResultSet}s from a JDBC {@link Statement}.
     */
    static final void consumeResultSets(ExecuteContext ctx) throws SQLException {
        int i = 0;
        int rows = (ctx.resultSet() == null) ? ctx.rows() : 0;

        for (i = 0; i < maxConsumedResults; i++) {
            try {
                if (ctx.resultSet() != null) {

                    // [#6390] With [#3681] unavailable in jOOQ 3.4, we just ignore intermediate result sets.
                    ctx.resultSet().close();
                }
                else {
                    if (rows != -1)
                        // [#6390] With [#3681] unavailable in jOOQ 3.4, we just ignore intermediate update counts.
                        ;
                    else
                        break;
                }

                if (ctx.statement().getMoreResults()) {
                    ctx.resultSet(ctx.statement().getResultSet());
                }
                else {
                    rows = ctx.statement().getUpdateCount();
                    ctx.rows(rows);

                    if (rows != -1)
                        ctx.resultSet(null);
                    else
                        break;
                }
            }

            // [#3011] [#3054] [#6390] [#6413] Consume additional exceptions if there are any
            catch (SQLException e) {
                consumeExceptions(ctx.configuration(), ctx.statement(), e);
                throw e;
            }
        }

        if (i == maxConsumedResults)
            log.warn("Maximum consumed results reached: " + maxConsumedResults + ". This is probably a bug. Please report to https://github.com/jOOQ/jOOQ/issues/new");
    }
    /* [/pro] */

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
        /* [pro] */
        else if (ArrayRecord.class.isAssignableFrom(type)) {
            return (T) getArrayRecord(configuration, stream.readArray(), (Class<? extends ArrayRecord<?>>) type);
        }
        /* [/pro] */
        else if (EnumType.class.isAssignableFrom(type)) {
            return getEnumType(type, stream.readString());
        }
        else if (UDTRecord.class.isAssignableFrom(type)) {
            return (T) stream.readObject();
        }
        else {
            return (T) unlob(stream.readObject());
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

    static final <T, U> U getFromResultSet(ExecuteContext ctx, Field<U> field, int index) throws SQLException {

        @SuppressWarnings("unchecked")
        Converter<T, U> converter = (Converter<T, U>) field.getConverter();
        return converter.from(getFromResultSet(ctx, converter.fromType(), index));
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
            return (T) Convert.convert(rs.getString(index), UByte.class);
        }
        else if (type == UShort.class) {
            return (T) Convert.convert(rs.getString(index), UShort.class);
        }
        else if (type == UInteger.class) {
            return (T) Convert.convert(rs.getString(index), UInteger.class);
        }
        else if (type == ULong.class) {
            return (T) Convert.convert(rs.getString(index), ULong.class);
        }
        else if (type == UUID.class) {
            switch (ctx.configuration().dialect().family()) {

                // [#1624] Some JDBC drivers natively support the
                // java.util.UUID data type
                case H2:
                case POSTGRES: {
                    return (T) rs.getObject(index);
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
        /* [pro] */
        else if (ArrayRecord.class.isAssignableFrom(type)) {
            return (T) getArrayRecord(ctx.configuration(), rs.getArray(index), (Class<? extends ArrayRecord<?>>) type);
        }
        /* [/pro] */
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
            return (T) unlob(rs.getObject(index));
        }
    }

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
            Object o;

            // [#1179 #1376 #1377] This is needed to load TABLE OF OBJECT
            // [#884] TODO: This name is used in inlined SQL. It should be
            // correctly escaped and schema mapped!
            o = source.getArray(DataTypes.udtRecords());
            target.set(Convert.convert(o, target.getDataType().getArrayType()));
        }

        return target;
    }

    /* [/pro] */
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

                /* [pro] */
                // Other SQL dialects deal with UUIDs as if they were CHAR(36)
                // even if they explicitly support them (UNIQUEIDENTIFIER)
                case SQLSERVER:
                case SYBASE:

                /* [/pro] */
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
        /* [pro] */
        else if (ArrayRecord.class.isAssignableFrom(type)) {
            return (T) getArrayRecord(ctx.configuration(), stmt.getArray(index), (Class<? extends ArrayRecord<?>>) type);
        }
        /* [/pro] */
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
