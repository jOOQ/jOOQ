/*
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
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: http://www.jooq.org/licenses
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
package org.jooq.impl;

import static java.lang.Boolean.FALSE;
import static java.lang.Character.isJavaIdentifierPart;
import static java.util.Arrays.asList;
// ...
import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
import static org.jooq.conf.BackslashEscaping.DEFAULT;
import static org.jooq.conf.BackslashEscaping.ON;
import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.conf.ParamType.NAMED;
import static org.jooq.conf.ParamType.NAMED_OR_INLINED;
import static org.jooq.conf.SettingsTools.getBackslashEscaping;
import static org.jooq.conf.SettingsTools.reflectionCaching;
import static org.jooq.conf.SettingsTools.updatablePrimaryKeys;
import static org.jooq.impl.DDLStatementType.CREATE_INDEX;
import static org.jooq.impl.DDLStatementType.CREATE_SEQUENCE;
import static org.jooq.impl.DDLStatementType.CREATE_TABLE;
import static org.jooq.impl.DDLStatementType.CREATE_VIEW;
import static org.jooq.impl.DDLStatementType.DROP_INDEX;
import static org.jooq.impl.DDLStatementType.DROP_SEQUENCE;
import static org.jooq.impl.DDLStatementType.DROP_TABLE;
import static org.jooq.impl.DDLStatementType.DROP_VIEW;
import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.escape;
import static org.jooq.impl.DSL.getDataType;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.nullSafe;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.val;
import static org.jooq.impl.DefaultExecuteContext.localConnection;
import static org.jooq.impl.Identifiers.QUOTES;
import static org.jooq.impl.Identifiers.QUOTE_END_DELIMITER;
import static org.jooq.impl.Identifiers.QUOTE_END_DELIMITER_ESCAPED;
import static org.jooq.impl.Identifiers.QUOTE_START_DELIMITER;
import static org.jooq.tools.reflect.Reflect.accessible;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinPool.ManagedBlocker;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

// ...
import org.jooq.Attachable;
import org.jooq.AttachableInternal;
import org.jooq.BindContext;
import org.jooq.Catalog;
import org.jooq.Clause;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.Cursor;
import org.jooq.DSLContext;
import org.jooq.DataType;
import org.jooq.EnumType;
import org.jooq.ExecuteContext;
import org.jooq.ExecuteListener;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.Param;
import org.jooq.Query;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.RecordType;
import org.jooq.RenderContext;
import org.jooq.RenderContext.CastMode;
import org.jooq.Result;
import org.jooq.Results;
import org.jooq.Row;
import org.jooq.SQLDialect;
import org.jooq.Schema;
import org.jooq.Select;
import org.jooq.SelectField;
import org.jooq.Sequence;
import org.jooq.SortField;
import org.jooq.Table;
import org.jooq.TableRecord;
import org.jooq.UDT;
import org.jooq.UDTRecord;
import org.jooq.UpdatableRecord;
import org.jooq.conf.BackslashEscaping;
import org.jooq.conf.Settings;
import org.jooq.exception.DataAccessException;
import org.jooq.exception.MappingException;
import org.jooq.exception.TooManyRowsException;
import org.jooq.impl.Tools.Cache.CachedOperation;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.StringUtils;
import org.jooq.tools.jdbc.JDBCUtils;
import org.jooq.tools.reflect.Reflect;
import org.jooq.types.UByte;
import org.jooq.types.UInteger;
import org.jooq.types.ULong;
import org.jooq.types.UShort;

/**
 * General internal jOOQ utilities
 *
 * @author Lukas Eder
 */
final class Tools {

    static final JooqLogger           log                    = JooqLogger.getLogger(Tools.class);

    // ------------------------------------------------------------------------
    // Empty arrays for use with Collection.toArray()
    // ------------------------------------------------------------------------

    static final Class<?>[]           EMPTY_CLASS            = {};
    static final Clause[]             EMPTY_CLAUSE           = {};
    static final Collection<?>[]      EMPTY_COLLECTION       = {};
    static final ExecuteListener[]    EMPTY_EXECUTE_LISTENER = {};
    static final Field<?>[]           EMPTY_FIELD            = {};
    static final int[]                EMPTY_INT              = {};
    static final Param<?>[]           EMPTY_PARAM            = {};
    static final Query[]              EMPTY_QUERY            = {};
    static final QueryPart[]          EMPTY_QUERYPART        = {};
    static final Record[]             EMPTY_RECORD           = {};
    static final String[]             EMPTY_STRING           = {};
    static final TableRecord<?>[]     EMPTY_TABLE_RECORD     = {};
    static final UpdatableRecord<?>[] EMPTY_UPDATABLE_RECORD = {};

    // ------------------------------------------------------------------------
    // Some constants for use with Context.data()
    // ------------------------------------------------------------------------

    enum DataKey {

        /**
         * [#1537] This constant is used internally by jOOQ to omit the RETURNING
         * clause in {@link DSLContext#batchStore(UpdatableRecord...)} calls for
         * {@link SQLDialect#POSTGRES}.
         */
        DATA_OMIT_RETURNING_CLAUSE,

        /**
         * [#1905] This constant is used internally by jOOQ to indicate to
         * subqueries that they're being rendered in the context of a row value
         * expression predicate.
         * <p>
         * This is particularly useful for H2, which pretends that ARRAYs and RVEs
         * are the same
         */
        DATA_ROW_VALUE_EXPRESSION_PREDICATE_SUBQUERY,

        /**
         * [#1296] This constant is used internally by jOOQ to indicate that
         * {@link ResultSet} rows must be locked to emulate a
         * <code>FOR UPDATE</code> clause.
         */
        DATA_LOCK_ROWS_FOR_UPDATE,

        /**
         * [#1520] Count the number of bind values, and potentially enforce a static
         * statement.
         */
        DATA_COUNT_BIND_VALUES,

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
        DATA_FORCE_STATIC_STATEMENT,

        /**
         * [#2665] Omit the emission of clause events by {@link QueryPart}s.
         * <p>
         * Some {@link QueryPart}s may contain further {@link QueryPart}s for whom
         * {@link Clause} emission should be avoided. For example
         * {@link Clause#FIELD_REFERENCE} may contain a
         * {@link Clause#TABLE_REFERENCE}.
         */
        DATA_OMIT_CLAUSE_EVENT_EMISSION,

        /**
         * [#2665] Wrap derived tables in parentheses.
         * <p>
         * Before allowing for hooking into the SQL transformation SPI, new
         * {@link RenderContext} instances could be created to "try" to render a
         * given SQL subclause before inserting it into the real SQL string. This
         * practice should no longer be pursued, as such "sub-renderers" will emit /
         * divert {@link Clause} events.
         */
        DATA_WRAP_DERIVED_TABLES_IN_PARENTHESES,

        /**
         * [#2790] A locally scoped data map.
         * <p>
         * Sometimes, it is useful to have some information only available while
         * visiting QueryParts in the same context of the current subquery, e.g.
         * when communicating between SELECT and WINDOW clauses, as is required to
         * emulate #531.
         */
        DATA_LOCALLY_SCOPED_DATA_MAP,

        /**
         * [#531] The local window definitions.
         * <p>
         * The window definitions declared in the <code>WINDOW</code> clause are
         * needed in the <code>SELECT</code> clause when emulating them by inlining
         * window specifications.
         */
        DATA_WINDOW_DEFINITIONS,











        /**
         * [#1629] The {@link Connection#getAutoCommit()} flag value before starting
         * a new transaction.
         */
        DATA_DEFAULT_TRANSACTION_PROVIDER_AUTOCOMMIT,

        /**
         * [#1629] The {@link Connection#getAutoCommit()} flag value before starting
         * a new transaction.
         */
        DATA_DEFAULT_TRANSACTION_PROVIDER_SAVEPOINTS,

        /**
         * [#1629] The {@link DefaultConnectionProvider} instance to be used during
         * the transaction.
         */
        DATA_DEFAULT_TRANSACTION_PROVIDER_CONNECTION,

        /**
         * [#2080] When emulating OFFSET pagination in certain databases, synthetic
         * aliases are generated that must be referenced also in
         * <code>ORDER BY</code> clauses, in lieu of their corresponding original
         * aliases.
         */
        DATA_OVERRIDE_ALIASES_IN_ORDER_BY,

        /**
         * [#2080] When emulating OFFSET pagination in certain databases, synthetic
         * aliases are generated that must be referenced also in
         * <code>ORDER BY</code> clauses, in lieu of their corresponding original
         * aliases.
         */
        DATA_UNALIAS_ALIASES_IN_ORDER_BY,

        /**
         * [#3381] The table to be used for the {@link Clause#SELECT_INTO} clause.
         */
        DATA_SELECT_INTO_TABLE,

        /**
         * [#3381] Omit the {@link Clause#SELECT_INTO}, as it is being emulated.
         */
        DATA_OMIT_INTO_CLAUSE,

        /**
         * [#1658] Specify whether the trailing LIMIT clause needs to be rendered.
         */
        DATA_RENDER_TRAILING_LIMIT_IF_APPLICABLE,

        /**
         * [#3886] Whether a list has already been indented.
         */
        DATA_LIST_ALREADY_INDENTED,

        /**
         * [#3338] [#5086] Whether a constraint is being referenced (rather than
         * declared).
         */
        DATA_CONSTRAINT_REFERENCE,

        /**
         * [#1206] Whether to collect Semi / Anti JOIN.
         */
        DATA_COLLECT_SEMI_ANTI_JOIN,

        /**
         * [#1206] The collected Semi / Anti JOIN predicates.
         */
        DATA_COLLECTED_SEMI_ANTI_JOIN,

        /**
         * [#2995] An <code>INSERT INTO t SELECT</code> statement. Without any
         * explicit column list, the <code>SELECT</code> statement must not be
         * wrapped in parentheses (which would be interpreted as the column
         * list's parentheses).
         */
        DATA_INSERT_SELECT_WITHOUT_INSERT_COLUMN_LIST,
    }

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

    /**
     * "Suffixes" that are placed behind a "?" character to form an operator,
     * rather than a JDBC bind variable. This is particularly useful to prevent
     * parsing PostgreSQL operators as bind variables, as can be seen here:
     * <a href=
     * "https://www.postgresql.org/docs/9.5/static/functions-json.html">https://www.postgresql.org/docs/9.5/static/functions-json.html</a>
     * <p>
     * Known PostgreSQL JSON operators:
     * <ul>
     * <li>?|</li>
     * <li>?&</li>
     * </ul>
     */
    private static final String[] NON_BIND_VARIABLE_SUFFIXES                   = {
        "?",
        "|",
        "&"
    };

    // ------------------------------------------------------------------------
    // XXX: Record constructors and related methods
    // ------------------------------------------------------------------------

    /**
     * Turn a {@link Result} into a list of {@link Row}
     */
    static final List<Row> rows(Result<?> result) {
        List<Row> rows = new ArrayList<Row>();

        for (Record record : result)
            rows.add(record.valuesRow());

        return rows;
    }


















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
     * Create a new record.
     */
    static final <R extends Record> RecordDelegate<R> newRecord(boolean fetched, Class<R> type, Field<?>[] fields, Configuration configuration) {
        return newRecord(fetched, recordFactory(type, fields), configuration);
    }

    /**
     * Create a new record.
     */
    static final <R extends Record> RecordDelegate<R> newRecord(boolean fetched, RecordFactory<R> factory, Configuration configuration) {
        try {
            R record = factory.newInstance();

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
     * Create a new record factory.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    static final <R extends Record> RecordFactory<R> recordFactory(final Class<R> type, final Field<?>[] fields) {

        // An ad-hoc type resulting from a JOIN or arbitrary SELECT
        if (type == RecordImpl.class || type == Record.class) {
            final RowImpl row = new RowImpl(fields);

            return new RecordFactory<R>() {
                @Override
                public R newInstance() {
                    return (R) new RecordImpl(row);
                }
            };
        }

        // Any generated record
        else {
            try {

                // [#919] Allow for accessing non-public constructors
                final Constructor<R> constructor = Reflect.accessible(type.getDeclaredConstructor());

                return new RecordFactory<R>() {
                    @Override
                    public R newInstance() {
                        try {
                            return constructor.newInstance();
                        }
                        catch (Exception e) {
                            throw new IllegalStateException("Could not construct new record", e);
                        }
                    }
                };
            }
            catch (Exception e) {
                throw new IllegalStateException("Could not construct new record", e);
            }
        }
    }

    /**
     * [#2700] [#3582] If a POJO attribute is NULL, but the column is NOT NULL
     * then we should let the database apply DEFAULT values
     */
    static final void resetChangedOnNotNull(Record record) {
        int size = record.size();

        for (int i = 0; i < size; i++)
            if (record.get(i) == null)
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
    static final Configuration configuration(Attachable attachable) {
        return configuration(attachable instanceof AttachableInternal
            ? ((AttachableInternal) attachable).configuration()
            : null);
    }

    /**
     * Get a configuration or a new {@link DefaultConfiguration} if
     * <code>null</code>.
     */
    static final Configuration configuration(Configuration configuration) {
        return configuration != null ? configuration : new DefaultConfiguration();
    }

    /**
     * Get a configuration's settings or default settings if the configuration
     * is <code>null</code>.
     */
    static final Settings settings(Attachable attachable) {
        return configuration(attachable).settings();
    }

    /**
     * Get a configuration's settings or default settings if the configuration
     * is <code>null</code>.
     */
    static final Settings settings(Configuration configuration) {
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
        return fields == null ? null : fields.toArray(EMPTY_FIELD);
    }

    // ------------------------------------------------------------------------
    // XXX: Data-type related methods
    // ------------------------------------------------------------------------

    /**
     * Useful conversion method
     */
    static final Class<?>[] types(Field<?>[] fields) {
        return types(dataTypes(fields));
    }

    /**
     * Useful conversion method
     */
    static final Class<?>[] types(DataType<?>[] types) {
        if (types == null)
            return null;

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
    static final Class<?>[] types(Object[] values) {
        if (values == null)
            return null;

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
    static final DataType<?>[] dataTypes(Field<?>[] fields) {
        if (fields == null)
            return null;

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
    static final DataType<?>[] dataTypes(Class<?>[] types) {
        if (types == null)
            return null;

        DataType<?>[] result = new DataType<?>[types.length];

        for (int i = 0; i < types.length; i++)
            if (types[i] != null)
                result[i] = getDataType(types[i]);
            else
                result[i] = getDataType(Object.class);

        return result;
    }

    /**
     * Useful conversion method
     */
    static final DataType<?>[] dataTypes(Object[] values) {
        return dataTypes(types(values));
    }

    // ------------------------------------------------------------------------
    // XXX: General utility methods
    // ------------------------------------------------------------------------

    static final SortField<?>[] sortFields(Field<?>[] fields) {
        if (fields == null)
            return null;

        SortField<?>[] result = new SortField[fields.length];
        for (int i = 0; i < fields.length; i++)
            result[i] = fields[i].asc();

        return result;
    }

    static final String[] fieldNames(int length) {
        String[] result = new String[length];

        for (int i = 0; i < length; i++)
            result[i] = "v" + i;

        return result;
    }

    static final String[] fieldNames(Field<?>[] fields) {
        if (fields == null)
            return null;

        String[] result = new String[fields.length];

        for (int i = 0; i < fields.length; i++)
            result[i] = fields[i].getName();

        return result;
    }

    static final Field<?>[] fields(int length) {
        Field<?>[] result = new Field[length];
        String[] names = fieldNames(length);

        for (int i = 0; i < length; i++)
            result[i] = DSL.field(name(names[i]));

        return result;
    }

    static final Field<?>[] aliasedFields(Field<?>[] fields, String[] aliases) {
        if (fields == null)
            return null;

        Field<?>[] result = new Field[fields.length];

        for (int i = 0; i < fields.length; i++)
            result[i] = fields[i].as(aliases[i]);

        return result;
    }

    static final Field<?>[] fieldsByName(Collection<String> fieldNames) {
        return fieldsByName(null, fieldNames.toArray(EMPTY_STRING));
    }

    static final Field<?>[] fieldsByName(String[] fieldNames) {
        return fieldsByName(null, fieldNames);
    }

    static final Field<?>[] fieldsByName(String tableName, Collection<String> fieldNames) {
        return fieldsByName(tableName, fieldNames.toArray(EMPTY_STRING));
    }

    static final Field<?>[] fieldsByName(String tableName, String[] fieldNames) {
        if (fieldNames == null)
            return null;

        Field<?>[] result = new Field[fieldNames.length];

        if (tableName == null)
            for (int i = 0; i < fieldNames.length; i++)
                result[i] = DSL.field(name(fieldNames[i]));
        else
            for (int i = 0; i < fieldNames.length; i++)
                result[i] = DSL.field(name(tableName, fieldNames[i]));

        return result;
    }

    static final Field<?>[] fieldsByName(Name[] names) {
        if (names == null)
            return null;

        Field<?>[] result = new Field[names.length];

        for (int i = 0; i < names.length; i++)
            result[i] = DSL.field(names[i]);

        return result;
    }

    static final Name[] names(String[] names) {
        if (names == null)
            return null;

        Name[] result = new Name[names.length];

        for (int i = 0; i < names.length; i++)
            result[i] = DSL.name(names[i]);

        return result;
    }

    private static final IllegalArgumentException fieldExpected(Object value) {
        return new IllegalArgumentException("Cannot interpret argument of type " + value.getClass() + " as a Field: " + value);
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

        // [#4771] Any other QueryPart type is not supported here
        else if (value instanceof QueryPart) {
            throw fieldExpected(value);
        }
        else {
            return val(value);
        }
    }

    // The following overloads help performance by avoiding runtime data type lookups
    // ------------------------------------------------------------------------------

    static final Param<Byte> field(byte value) {
        return val((Object) value, SQLDataType.TINYINT);
    }

    static final Param<Byte> field(Byte value) {
        return val((Object) value, SQLDataType.TINYINT);
    }

    static final Param<UByte> field(UByte value) {
        return val((Object) value, SQLDataType.TINYINTUNSIGNED);
    }

    static final Param<Short> field(short value) {
        return val((Object) value, SQLDataType.SMALLINT);
    }

    static final Param<Short> field(Short value) {
        return val((Object) value, SQLDataType.SMALLINT);
    }

    static final Param<UShort> field(UShort value) {
        return val((Object) value, SQLDataType.SMALLINTUNSIGNED);
    }

    static final Param<Integer> field(int value) {
        return val((Object) value, SQLDataType.INTEGER);
    }

    static final Param<Integer> field(Integer value) {
        return val((Object) value, SQLDataType.INTEGER);
    }

    static final Param<UInteger> field(UInteger value) {
        return val((Object) value, SQLDataType.INTEGERUNSIGNED);
    }

    static final Param<Long> field(long value) {
        return val((Object) value, SQLDataType.BIGINT);
    }

    static final Param<Long> field(Long value) {
        return val((Object) value, SQLDataType.BIGINT);
    }

    static final Param<ULong> field(ULong value) {
        return val((Object) value, SQLDataType.BIGINTUNSIGNED);
    }

    static final Param<Float> field(float value) {
        return val((Object) value, SQLDataType.REAL);
    }

    static final Param<Float> field(Float value) {
        return val((Object) value, SQLDataType.REAL);
    }

    static final Param<Double> field(double value) {
        return val((Object) value, SQLDataType.DOUBLE);
    }

    static final Param<Double> field(Double value) {
        return val((Object) value, SQLDataType.DOUBLE);
    }

    static final Param<Boolean> field(boolean value) {
        return val((Object) value, SQLDataType.BOOLEAN);
    }

    static final Param<Boolean> field(Boolean value) {
        return val((Object) value, SQLDataType.BOOLEAN);
    }

    static final Param<BigDecimal> field(BigDecimal value) {
        return val((Object) value, SQLDataType.DECIMAL);
    }

    static final Param<BigInteger> field(BigInteger value) {
        return val((Object) value, SQLDataType.DECIMAL_INTEGER);
    }

    static final Param<byte[]> field(byte[] value) {
        return val((Object) value, SQLDataType.VARBINARY);
    }

    static final Param<String> field(String value) {
        return val((Object) value, SQLDataType.VARCHAR);
    }

    static final Param<Date> field(Date value) {
        return val((Object) value, SQLDataType.DATE);
    }

    static final Param<Time> field(Time value) {
        return val((Object) value, SQLDataType.TIME);
    }

    static final Param<Timestamp> field(Timestamp value) {
        return val((Object) value, SQLDataType.TIMESTAMP);
    }


    static final Param<LocalDate> field(LocalDate value) {
        return val((Object) value, SQLDataType.LOCALDATE);
    }

    static final Param<LocalTime> field(LocalTime value) {
        return val((Object) value, SQLDataType.LOCALTIME);
    }

    static final Param<LocalDateTime> field(LocalDateTime value) {
        return val((Object) value, SQLDataType.LOCALDATETIME);
    }

    static final Param<OffsetTime> field(OffsetTime value) {
        return val((Object) value, SQLDataType.OFFSETTIME);
    }

    static final Param<OffsetDateTime> field(OffsetDateTime value) {
        return val((Object) value, SQLDataType.OFFSETDATETIME);
    }


    static final Param<UUID> field(UUID value) {
        return val((Object) value, SQLDataType.UUID);
    }

    /**
     * @deprecated - This method is probably called by mistake (ambiguous static import).
     */
    @Deprecated
    static final Field<Object> field(Name name) {
        return DSL.field(name);
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

        // [#4771] Any other QueryPart type is not supported here
        else if (value instanceof QueryPart) {
            throw fieldExpected(value);
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

        // [#4771] Any other QueryPart type is not supported here
        else if (value instanceof QueryPart) {
            throw fieldExpected(value);
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

        // [#4771] Any other QueryPart type is not supported here
        else if (value instanceof QueryPart) {
            throw fieldExpected(value);
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
    static final <T> List<Field<T>> fields(T[] values) {
        List<Field<T>> result = new ArrayList<Field<T>>();

        if (values != null)
            for (T value : values)
                result.add(field(value));

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

    static final List<Field<?>> fields(Collection<? extends SelectField<?>> fields) {
        List<Field<?>> result = new ArrayList<Field<?>>();

        if (fields != null) {
            for (SelectField<?> field : fields) {
                result.add(DSL.field(field));
            }
        }

        return result;
    }

    static final List<Field<?>> fields(SelectField<?>... fields) {
        return fields == null ? fields(Collections.<SelectField<?>>emptyList()) : fields(Arrays.asList(fields));
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
            result.add(DSL.field(name(field.getName())));

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
     * {@link Row#indexOf(Name)} doesn't return any index.
     */
    static final int indexOrFail(Row row, Name fieldName) {
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
     * A utility method that fails with an exception if
     * {@link RecordType#indexOf(Name)} doesn't return any index.
     */
    static final int indexOrFail(RecordType<?> row, Name fieldName) {
        int result = row.indexOf(fieldName);

        if (result < 0)
            throw new IllegalArgumentException("Field (" + fieldName + ") is not contained in RecordType " + row);

        return result;
    }

    /**
     * Create a new array
     */

    @SafeVarargs

    static final <T> T[] array(T... array) {
        return array;
    }

    /**
     * Use this rather than {@link Arrays#asList(Object...)} for
     * <code>null</code>-safety
     */

    @SafeVarargs

    static final <T> List<T> list(T... array) {
        return array == null ? Collections.<T>emptyList() : Arrays.asList(array);
    }

    /**
     * Turn a {@link Record} into a {@link Map}
     */
    static final Map<Field<?>, Object> mapOfChangedValues(Record record) {
        Map<Field<?>, Object> result = new LinkedHashMap<Field<?>, Object>();
        int size = record.size();

        for (int i = 0; i < size; i++)
            if (record.changed(i))
                result.put(record.field(i), record.get(i));

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
     * @throws TooManyRowsException Thrown if the list contains more than one
     *             element
     */
    static final <R extends Record> R filterOne(List<R> list) throws TooManyRowsException {
        int size = list.size();

        if (size == 1) {
            return list.get(0);
        }
        else if (size > 1) {
            throw new TooManyRowsException("Too many rows selected : " + size);
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
     * @throws TooManyRowsException Thrown if the cursor returns more than one
     *             element
     */
    static final <R extends Record> R fetchOne(Cursor<R> cursor) throws TooManyRowsException {
        try {
            R record = cursor.fetchOne();

            if (cursor.hasNext()) {
                throw new TooManyRowsException("Cursor returned more than one result");
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
        boolean mysql = asList(MARIADB, MYSQL).contains(family);
        String[][] quotes = QUOTES.get(family);

        // [#3630] Depending on this setting, we need to consider backslashes as escape characters within string literals.
        boolean needsBackslashEscaping = needsBackslashEscaping(ctx.configuration());

        characterLoop:
        for (int i = 0; i < sqlChars.length; i++) {

            // [#1797] Skip content inside of single-line comments, e.g.
            // select 1 x -- what's this ?'?
            // from t_book -- what's that ?'?
            // where id = ?
            if (peek(sqlChars, i, "--") ||

            // [#4182] MySQL also supports # as a comment character, and requires
            // -- to be followed by a whitespace, although the latter is also not
            // handled correctly by the MySQL JDBC driver (yet). See
    		// http://bugs.mysql.com/bug.php?id=76623
                (mysql && peek(sqlChars, i, "#"))) {

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

                    // [#3000] [#3630] Consume backslash-escaped characters if needed
                    if (sqlChars[i] == '\\' && needsBackslashEscaping) {
                        render.sql(sqlChars[i++]);
                    }

                    // Consume an escaped apostrophe
                    else if (peek(sqlChars, i, "''")) {
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
            else if (substituteIndex < substitutes.size() &&
                    ((sqlChars[i] == '?')

                  // [#4131] Named bind variables of the form :identifier
                  //         Watch out for the PostgreSQL cast operator ::
                  || (sqlChars[i] == ':'
                          && i + 1 < sqlChars.length && isJavaIdentifierPart(sqlChars[i + 1])
                          &&(i - 1 < 0               || sqlChars[i - 1] != ':')))) {

                // [#5307] Consume PostgreSQL style operators. These aren't bind variables!
                if (sqlChars[i] == '?' && i + 1 < sqlChars.length) {
                    for (String suffix : NON_BIND_VARIABLE_SUFFIXES) {
                        if (peek(sqlChars, i + 1, suffix)) {
                            for (int j = i; i - j <= suffix.length(); i++)
                                render.sql(sqlChars[i]);

                            render.sql(sqlChars[i]);
                            continue characterLoop;
                        }
                    }
                }

                    // [#4131] Consume the named bind variable
                if (sqlChars[i] == ':')
                    while (++i < sqlChars.length && isJavaIdentifierPart(sqlChars[i]));

                QueryPart substitute = substitutes.get(substituteIndex++);

                if (render.paramType() == INLINED || render.paramType() == NAMED || render.paramType() == NAMED_OR_INLINED) {
                    render.visit(substitute);
                }
                else {
                    CastMode previous = render.castMode();
                    render.castMode(CastMode.NEVER)
                          .visit(substitute)
                          .castMode(previous);
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
     * Whether backslash escaping is needed in inlined string literals.
     */
    static final boolean needsBackslashEscaping(Configuration configuration) {
        BackslashEscaping escaping = getBackslashEscaping(configuration.settings());
        return escaping == ON || (escaping == DEFAULT && EnumSet.of(MARIADB, MYSQL).contains(configuration.dialect().family()));
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

        PreparedStatement statement = ctx.statement();
        if (statement != null) {
            consumeWarnings(ctx, listener);
        }

        // [#385] Close statements only if not requested to keep open
        if (!keepStatement) {
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
        setValue(target, targetField, source.get(sourceField));
    }

    /**
     * Type-safely set a value to a record
     */
    static final <T> void setValue(Record target, Field<T> targetField, Object value) {
        target.set(targetField, targetField.getDataType().convert(value));
    }

    /**
     * [#2591] Type-safely copy a value from one record to another, preserving flags.
     */
    static final <T> void copyValue(AbstractRecord target, Field<T> targetField, Record source, Field<?> sourceField) {
        DataType<T> targetType = targetField.getDataType();

        int targetIndex = indexOrFail(target.fieldsRow(), targetField);
        int sourceIndex = indexOrFail(source.fieldsRow(), sourceField);

        target.values[targetIndex] = targetType.convert(source.get(sourceIndex));
        target.originals[targetIndex] = targetType.convert(source.original(sourceIndex));
        target.changed.set(targetIndex, source.changed(sourceIndex));
    }

    /**
     * Map a {@link Catalog} according to the configured {@link org.jooq.SchemaMapping}
     */
    @SuppressWarnings("deprecation")
    static final Catalog getMappedCatalog(Configuration configuration, Catalog catalog) {
        if (configuration != null) {
            org.jooq.SchemaMapping mapping = configuration.schemaMapping();

            if (mapping != null)
                return mapping.map(catalog);
        }

        return catalog;
    }

    /**
     * Map a {@link Schema} according to the configured {@link org.jooq.SchemaMapping}
     */
    @SuppressWarnings("deprecation")
    static final Schema getMappedSchema(Configuration configuration, Schema schema) {
        if (configuration != null) {
            org.jooq.SchemaMapping mapping = configuration.schemaMapping();

            if (mapping != null)
                return mapping.map(schema);
        }

        return schema;
    }

    /**
     * Map a {@link Table} according to the configured {@link org.jooq.SchemaMapping}
     */
    @SuppressWarnings("deprecation")
    static final <R extends Record> Table<R> getMappedTable(Configuration configuration, Table<R> table) {
        if (configuration != null) {
            org.jooq.SchemaMapping mapping = configuration.schemaMapping();

            if (mapping != null)
                return mapping.map(table);
        }

        return table;
    }

    /**
     * Map an {@link ArrayRecord} according to the configured {@link org.jooq.SchemaMapping}
     */
    @SuppressWarnings("unchecked")
    static final String getMappedUDTName(Configuration configuration, Class<? extends UDTRecord<?>> type) {
        return getMappedUDTName(configuration, Tools.newRecord(false, (Class<UDTRecord<?>>) type).<RuntimeException>operate(null));
    }

    /**
     * Map an {@link ArrayRecord} according to the configured {@link org.jooq.SchemaMapping}
     */
    static final String getMappedUDTName(Configuration configuration, UDTRecord<?> record) {
        UDT<?> udt = record.getUDT();
        Schema mapped = getMappedSchema(configuration, udt.getSchema());
        StringBuilder sb = new StringBuilder();

        if (mapped != null)
            sb.append(mapped.getName()).append('.');

        sb.append(record.getUDT().getName());
        return sb.toString();
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
            provider.addConditions(condition(field, record.get(field)));
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
     * This API acts as a "guard" to prevent the same code from being executed
     * recursively within the same thread.
     */
    static class ThreadGuard {

        /**
         * The type of guard.
         */
        static enum Guard {
            RECORD_TOSTRING;

            ThreadLocal<Object> tl = new ThreadLocal<Object>();
        }

        /**
         * A guarded operation.
         */
        static interface GuardedOperation<V> {

            /**
             * This callback is executed only once on the current stack.
             */
            V unguarded();

            /**
             * This callback is executed if {@link #unguarded()} has already been executed on the current stack.
             */
            V guarded();
        }

        /**
         * A default implementation for {@link GuardedOperation#guarded()}.
         */
        abstract static class AbstractGuardedOperation<V> implements GuardedOperation<V> {
            @Override
            public V guarded() {
                return null;
            }
        }

        /**
         * Run an operation using a guard.
         */
        static final <V> V run(Guard guard, GuardedOperation<V> operation) {
            boolean unguarded = (guard.tl.get() == null);
            if (unguarded)
                guard.tl.set(Guard.class);

            try {
                if (unguarded)
                    return operation.unguarded();
                else
                    return operation.guarded();
            }
            finally {
                if (unguarded)
                    guard.tl.remove();
            }
        }
    }

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
        static final <V> V run(Configuration configuration, CachedOperation<V> operation, String type, Serializable... keys) {

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
        private static final Object key(final Serializable... key) {
            if (key == null || key.length == 0)
                return key;

            if (key.length == 1)
                return key[0];

            return new Key(key);
        }

        /**
         * A multi-value key for caching.
         */
        private static class Key implements Serializable {

            /**
             * Generated UID.
             */
            private static final long    serialVersionUID = 5822370287443922993L;
            private final Serializable[] key;

            Key(Serializable[] key) {
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
                if (!isJPAAvailable())
                    return false;

                // An @Entity or @Table usually has @Column annotations, too
                if (type.getAnnotation(Entity.class) != null)
                    return true;

                if (type.getAnnotation(javax.persistence.Table.class) != null)
                    return true;

                for (java.lang.reflect.Field member : getInstanceMembers(type)) {
                    if (member.getAnnotation(Column.class) != null)
                        return true;

                    if (member.getAnnotation(Id.class) != null)
                        return true;
                }

                for (Method method : getInstanceMethods(type))
                    if (method.getAnnotation(Column.class) != null)
                        return true;

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
                    Column column = member.getAnnotation(Column.class);

                    if (column != null) {
                        if (namesMatch(name, column.name())) {
                            result.add(accessible(member));
                        }
                    }

                    else {
                        Id id = member.getAnnotation(Id.class);

                        if (id != null) {
                            if (namesMatch(name, member.getName())) {
                                result.add(accessible(member));
                            }
                        }
                    }
                }

                return result;
            }
        }, DATA_REFLECTION_CACHE_GET_ANNOTATED_MEMBERS, type, name);
    }

    private static final boolean namesMatch(String name, String annotation) {

        // [#4128] JPA @Column.name() properties are case-insensitive, unless
        // the names are quoted using double quotes.
        return annotation.startsWith("\"")
            ? ('"' + name + '"').equals(annotation)
            : name.equalsIgnoreCase(annotation);
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
                    Column column = method.getAnnotation(Column.class);

                    if (column != null && namesMatch(name, column.name())) {

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
                    Column column = method.getAnnotation(Column.class);

                    if (column != null && namesMatch(name, column.name())) {

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

        for (java.lang.reflect.Field field : type.getFields())
            if ((field.getModifiers() & Modifier.STATIC) == 0)
                result.add(field);

        do {
            for (java.lang.reflect.Field field : type.getDeclaredFields())
                if ((field.getModifiers() & Modifier.STATIC) == 0)
                    result.add(field);

            type = type.getSuperclass();
        }
        while (type != null);

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





















    }

    /**
     * [#3076] Consume warnings from a {@link Statement} and notify listeners.
     */
    static final void consumeWarnings(ExecuteContext ctx, ExecuteListener listener) {

        // [#3558] In some databases (e.g. MySQL), the call to PreparedStatement.getWarnings() issues
        // a separate SHOW WARNINGS query. Users may want to avoid this query, explicitly
        if (!Boolean.FALSE.equals(ctx.settings().isFetchWarnings())) {
            try {
                ctx.sqlWarning(ctx.statement().getWarnings());
            }

            // [#3741] In MySQL, calling SHOW WARNINGS on open streaming result sets can cause issues.
            // while this has been resolved, we should expect the above call to cause other issues, too
            catch (SQLException e) {
                ctx.sqlWarning(new SQLWarning("Could not fetch SQLWarning", e));
            }
        }

        if (ctx.sqlWarning() != null)
            listener.warning(ctx);
    }

    /**
     * [#3681] Consume all {@link ResultSet}s from a JDBC {@link Statement}.
     */
    static final void consumeResultSets(ExecuteContext ctx, ExecuteListener listener, Results results, Intern intern) throws SQLException {
        boolean anyResults = false;
        int i = 0;
        int rows = (ctx.resultSet() == null) ? ctx.statement().getUpdateCount() : 0;

        for (i = 0; i < maxConsumedResults; i++) {
            if (ctx.resultSet() != null) {
                anyResults = true;

                Field<?>[] fields = new MetaDataFieldProvider(ctx.configuration(), ctx.resultSet().getMetaData()).getFields();
                Cursor<Record> c = new CursorImpl<Record>(ctx, listener, fields, intern != null ? intern.internIndexes(fields) : null, true, false);
                results.resultsOrRows().add(new ResultsImpl.ResultOrRowsImpl(c.fetch()));
            }
            else {
                if (rows != -1)
                    results.resultsOrRows().add(new ResultsImpl.ResultOrRowsImpl(rows));
                else
                    break;
            }

            if (ctx.statement().getMoreResults())
                ctx.resultSet(ctx.statement().getResultSet());
            else if ((rows = ctx.statement().getUpdateCount()) != -1)
                ctx.resultSet(null);
            else
                break;
        }

        if (i == maxConsumedResults)
            log.warn("Maximum consumed results reached: " + maxConsumedResults + ". This is probably a bug. Please report to https://github.com/jOOQ/jOOQ/issues/new");

        // Call this only when there was at least one ResultSet.
        // Otherwise, this call is not supported by ojdbc or CUBRID [#4440]
        if (anyResults && ctx.family() != CUBRID)
            ctx.statement().getMoreResults(Statement.CLOSE_ALL_RESULTS);
    }

    static final List<String[]> parseTXT(String string, String nullLiteral) {
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

    private static final List<String[]> parseTXTLines(
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

    private static final void parseTXTLine(List<int[]> positions, List<String[]> result, String string, String nullLiteral) {
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

    private static final Pattern P_PARSE_HTML_ROW      = Pattern.compile("<tr>(.*?)</tr>");
    private static final Pattern P_PARSE_HTML_COL_HEAD = Pattern.compile("<th>(.*?)</th>");
    private static final Pattern P_PARSE_HTML_COL_BODY = Pattern.compile("<td>(.*?)</td>");

    static final List<String[]> parseHTML(String string) {
        List<String[]> result = new ArrayList<String[]>();

        Matcher mRow = P_PARSE_HTML_ROW.matcher(string);
        while (mRow.find()) {
            String row = mRow.group(1);
            List<String> col = new ArrayList<String>();

            // Header was not yet emitted
            if (result.isEmpty()) {
                Matcher mColHead = P_PARSE_HTML_COL_HEAD.matcher(row);

                while (mColHead.find())
                    col.add(mColHead.group(1));
            }

            if (col.isEmpty()) {
                Matcher mColBody = P_PARSE_HTML_COL_BODY.matcher(row);

                while (mColBody.find())
                    col.add(mColBody.group(1));

                if (result.isEmpty())
                    result.add(fieldNames(col.size()));
            }

            result.add(col.toArray(EMPTY_STRING));
        }

        return result;
    }

    /**
     * Wrap a <code>DROP .. IF EXISTS</code> statement with
     * <code>BEGIN EXECUTE IMMEDIATE '...' EXCEPTION WHEN ... END;</code>, if
     * <code>IF EXISTS</code> is not supported.
     */
    static final void executeImmediateBegin(Context<?> ctx, DDLStatementType type) {
        switch (ctx.family()) {












































            case FIREBIRD: {
                ctx.keyword("execute block").formatSeparator()
                   .keyword("as").formatSeparator()
                   .keyword("begin").formatIndentStart().formatSeparator()
                   .keyword("execute statement").sql(" '").stringLiteral(true).formatIndentStart().formatSeparator();

                break;
            }

            default:
                break;
        }
    }

    /**
     * Wrap a <code>DROP .. IF EXISTS</code> statement with
     * <code>BEGIN EXECUTE IMMEDIATE '...' EXCEPTION WHEN ... END;</code>, if
     * <code>IF EXISTS</code> is not supported.
     */
    static final void executeImmediateEnd(Context<?> ctx, DDLStatementType type) {
        boolean drop = asList(DROP_INDEX, DROP_SEQUENCE, DROP_TABLE, DROP_VIEW).contains(type);

        switch (ctx.family()) {

























































            case FIREBIRD: {
                ctx.formatIndentEnd().formatSeparator().stringLiteral(false).sql("';").formatSeparator()
                   .keyword("when").sql(" sqlcode -607 ").keyword("do").formatIndentStart().formatSeparator()
                   .keyword("begin end").formatIndentEnd().formatIndentEnd().formatSeparator()
                   .keyword("end");

                break;
            }

            default:
                break;
        }
    }

    static final void executeImmediateIfExistsBegin(Context<?> ctx, DDLStatementType type, QueryPart object) {
        switch (ctx.family()) {


















































































            default:
                executeImmediateBegin(ctx, type);
                break;
        }
    }

    static final void executeImmediateIfExistsEnd(Context<?> ctx, DDLStatementType type, QueryPart object) {
        switch (ctx.family()) {











            default:
                executeImmediateEnd(ctx, type);
                break;
        }
    }

    static final void toSQLDDLTypeDeclaration(Context<?> ctx, DataType<?> type) {
        String typeName = type.getTypeName(ctx.configuration());

        // In some databases, identity is a type, not a flag.
        if (type.identity()) {
            switch (ctx.family()) {





                case POSTGRES: ctx.keyword(type.getType() == Long.class ? "serial8" : "serial"); return;
            }
        }

        if (type.hasLength()) {
            if (type.length() > 0) {
                ctx.keyword(typeName).sql('(').sql(type.length()).sql(')');
            }

            // Some databases don't allow for length-less VARCHAR, VARBINARY types
            else {
                String castTypeName = type.getCastTypeName(ctx.configuration());

                if (!typeName.equals(castTypeName))
                    ctx.keyword(castTypeName);
                else
                    ctx.keyword(typeName);
            }
        }
        else if (type.hasPrecision() && type.precision() > 0) {
            if (type.hasScale())
                ctx.keyword(typeName).sql('(').sql(type.precision()).sql(", ").sql(type.scale()).sql(')');
            else
                ctx.keyword(typeName).sql('(').sql(type.precision()).sql(')');
        }
        else {
            ctx.keyword(typeName);
        }

        if (type.identity()) {
            switch (ctx.family()) {








                case CUBRID:    ctx.sql(' ').keyword("auto_increment"); break;
                case DERBY:     ctx.sql(' ').keyword("generated by default as identity"); break;
                case HSQLDB:    ctx.sql(' ').keyword("generated by default as identity").sql('(').keyword("start with").sql(" 1)"); break;
            }
        }
    }

    // -------------------------------------------------------------------------
    // XXX: ForkJoinPool ManagedBlock implementation
    // -------------------------------------------------------------------------



    static <T> Supplier<T> blocking(Supplier<T> supplier) {
        return blocking(supplier, false);
    }

    static <T> Supplier<T> blocking(Supplier<T> supplier, boolean threadLocal) {

        // [#5377] In ThreadLocal contexts (e.g. when using ThreadLocalTransactionprovider),
        //         no ManagedBlocker is needed as we're guaranteed by API contract to always
        //         remain on the same thread.

        return threadLocal ? supplier : new Supplier<T>() {
            volatile T asyncResult;

            @Override
            public T get() {
                try {
                    ForkJoinPool.managedBlock(new ManagedBlocker() {
                        @Override
                        public boolean block() {
                            asyncResult = supplier.get();
                            return true;
                        }

                        @Override
                        public boolean isReleasable() {
                            return asyncResult != null;
                        }
                    });
                }
                catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                return asyncResult;
            }
        };
    }


    static <E extends EnumType> EnumType[] enums(Class<? extends E> type) {

        // Java implementation
        if (Enum.class.isAssignableFrom(type)) {
            return type.getEnumConstants();
        }

        // [#4427] Scala implementation
        else {
            try {

                // There's probably a better way to do this:
                // http://stackoverflow.com/q/36068089/521799
                Class<?> companionClass = Thread.currentThread().getContextClassLoader().loadClass(type.getName() + "$");
                java.lang.reflect.Field module = companionClass.getField("MODULE$");
                Object companion = module.get(companionClass);
                return (EnumType[]) companionClass.getMethod("values").invoke(companion);
            }
            catch (Exception e) {
                throw new MappingException("Error while looking up Scala enum", e);
            }
        }
    }

    /**
     * Whether a Java type is suitable for {@link Types#TIME}.
     */
    static final boolean isTime(Class<?> t) {
        return t == Time.class  || t == LocalTime.class ;
    }

    /**
     * Whether a Java type is suitable for {@link Types#TIMESTAMP}.
     */
    static final boolean isTimestamp(Class<?> t) {
        return t == Timestamp.class  || t == LocalDateTime.class ;
    }

    /**
     * Whether a Java type is suitable for {@link Types#DATE}.
     */
    static final boolean isDate(Class<?> t) {
        return t == Date.class  || t == LocalDate.class ;
    }

    static final boolean hasAmbiguousNames(Collection<? extends Field<?>> fields) {
        if (fields == null)
            return false;

        Set<String> names = new HashSet<String>();
        for (Field<?> field : fields)
            if (!names.add(field.getName()))
                return true;

        return false;
    }
}
