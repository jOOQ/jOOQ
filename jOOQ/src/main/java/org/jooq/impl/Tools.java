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
 *
 *
 *
 */
package org.jooq.impl;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.lang.Character.isJavaIdentifierPart;
import static java.util.Collections.singletonList;
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.HSQLDB;
// ...
import static org.jooq.SQLDialect.MARIADB;
// ...
import static org.jooq.SQLDialect.MYSQL;
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
import static org.jooq.SQLDialect.SQLITE;
// ...
import static org.jooq.conf.BackslashEscaping.DEFAULT;
import static org.jooq.conf.BackslashEscaping.ON;
import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.conf.ParamType.NAMED;
import static org.jooq.conf.ParamType.NAMED_OR_INLINED;
import static org.jooq.conf.SettingsTools.getBackslashEscaping;
import static org.jooq.conf.SettingsTools.reflectionCaching;
import static org.jooq.conf.SettingsTools.updatablePrimaryKeys;
import static org.jooq.conf.ThrowExceptions.THROW_FIRST;
import static org.jooq.conf.ThrowExceptions.THROW_NONE;
import static org.jooq.impl.DDLStatementType.ALTER_INDEX;
import static org.jooq.impl.DDLStatementType.ALTER_SEQUENCE;
import static org.jooq.impl.DDLStatementType.ALTER_TABLE;
import static org.jooq.impl.DDLStatementType.ALTER_VIEW;
import static org.jooq.impl.DDLStatementType.CREATE_INDEX;
import static org.jooq.impl.DDLStatementType.CREATE_SCHEMA;
import static org.jooq.impl.DDLStatementType.CREATE_SEQUENCE;
import static org.jooq.impl.DDLStatementType.CREATE_TABLE;
import static org.jooq.impl.DDLStatementType.CREATE_VIEW;
import static org.jooq.impl.DDLStatementType.DROP_INDEX;
import static org.jooq.impl.DDLStatementType.DROP_SCHEMA;
import static org.jooq.impl.DDLStatementType.DROP_SEQUENCE;
import static org.jooq.impl.DDLStatementType.DROP_TABLE;
import static org.jooq.impl.DDLStatementType.DROP_VIEW;
import static org.jooq.impl.DSL.asterisk;
import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.escape;
import static org.jooq.impl.DSL.getDataType;
import static org.jooq.impl.DSL.keyword;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.nullSafe;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.val;
import static org.jooq.impl.DefaultExecuteContext.localConnection;
import static org.jooq.impl.Identifiers.QUOTES;
import static org.jooq.impl.Identifiers.QUOTE_END_DELIMITER;
import static org.jooq.impl.Identifiers.QUOTE_END_DELIMITER_ESCAPED;
import static org.jooq.impl.Identifiers.QUOTE_START_DELIMITER;
import static org.jooq.impl.Keywords.K_AS;
import static org.jooq.impl.Keywords.K_ATOMIC;
import static org.jooq.impl.Keywords.K_AUTOINCREMENT;
import static org.jooq.impl.Keywords.K_AUTO_INCREMENT;
import static org.jooq.impl.Keywords.K_BEGIN;
import static org.jooq.impl.Keywords.K_BEGIN_CATCH;
import static org.jooq.impl.Keywords.K_BEGIN_TRY;
import static org.jooq.impl.Keywords.K_CHARACTER_SET;
import static org.jooq.impl.Keywords.K_COLLATE;
import static org.jooq.impl.Keywords.K_DECLARE;
import static org.jooq.impl.Keywords.K_DEFAULT;
import static org.jooq.impl.Keywords.K_DO;
import static org.jooq.impl.Keywords.K_ELSE;
import static org.jooq.impl.Keywords.K_ELSIF;
import static org.jooq.impl.Keywords.K_END;
import static org.jooq.impl.Keywords.K_END_CATCH;
import static org.jooq.impl.Keywords.K_END_IF;
import static org.jooq.impl.Keywords.K_END_TRY;
import static org.jooq.impl.Keywords.K_ENUM;
import static org.jooq.impl.Keywords.K_EXCEPTION;
import static org.jooq.impl.Keywords.K_EXEC;
import static org.jooq.impl.Keywords.K_EXECUTE_BLOCK;
import static org.jooq.impl.Keywords.K_EXECUTE_IMMEDIATE;
import static org.jooq.impl.Keywords.K_EXECUTE_STATEMENT;
import static org.jooq.impl.Keywords.K_GENERATED_BY_DEFAULT_AS_IDENTITY;
import static org.jooq.impl.Keywords.K_IDENTITY;
import static org.jooq.impl.Keywords.K_IF;
import static org.jooq.impl.Keywords.K_INT;
import static org.jooq.impl.Keywords.K_LIKE;
import static org.jooq.impl.Keywords.K_NOT;
import static org.jooq.impl.Keywords.K_NOT_NULL;
import static org.jooq.impl.Keywords.K_NULL;
import static org.jooq.impl.Keywords.K_NVARCHAR;
import static org.jooq.impl.Keywords.K_PRIMARY_KEY;
import static org.jooq.impl.Keywords.K_RAISE;
import static org.jooq.impl.Keywords.K_RAISERROR;
import static org.jooq.impl.Keywords.K_SERIAL;
import static org.jooq.impl.Keywords.K_SERIAL8;
import static org.jooq.impl.Keywords.K_SQLSTATE;
import static org.jooq.impl.Keywords.K_START_WITH;
import static org.jooq.impl.Keywords.K_THEN;
import static org.jooq.impl.Keywords.K_THROW;
import static org.jooq.impl.Keywords.K_WHEN;
import static org.jooq.impl.SQLDataType.VARCHAR;
import static org.jooq.impl.Tools.DataCacheKey.DATA_REFLECTION_CACHE_GET_ANNOTATED_GETTER;
import static org.jooq.impl.Tools.DataCacheKey.DATA_REFLECTION_CACHE_GET_ANNOTATED_MEMBERS;
import static org.jooq.impl.Tools.DataCacheKey.DATA_REFLECTION_CACHE_GET_ANNOTATED_SETTERS;
import static org.jooq.impl.Tools.DataCacheKey.DATA_REFLECTION_CACHE_GET_MATCHING_GETTER;
import static org.jooq.impl.Tools.DataCacheKey.DATA_REFLECTION_CACHE_GET_MATCHING_MEMBERS;
import static org.jooq.impl.Tools.DataCacheKey.DATA_REFLECTION_CACHE_GET_MATCHING_SETTERS;
import static org.jooq.impl.Tools.DataCacheKey.DATA_REFLECTION_CACHE_HAS_COLUMN_ANNOTATIONS;
import static org.jooq.impl.Tools.DataKey.DATA_BLOCK_NESTING;
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
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
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
import org.jooq.Asterisk;
import org.jooq.Attachable;
import org.jooq.BindContext;
import org.jooq.Catalog;
import org.jooq.Clause;
import org.jooq.CommonTableExpression;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.Cursor;
import org.jooq.DSLContext;
import org.jooq.DataType;
import org.jooq.EmbeddableRecord;
import org.jooq.EnumType;
import org.jooq.ExecuteContext;
import org.jooq.ExecuteListener;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.OrderField;
import org.jooq.Param;
// ...
import org.jooq.QualifiedAsterisk;
import org.jooq.Query;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.RecordType;
import org.jooq.RenderContext;
import org.jooq.RenderContext.CastMode;
import org.jooq.Result;
import org.jooq.ResultOrRows;
import org.jooq.Results;
import org.jooq.Row;
import org.jooq.RowN;
import org.jooq.SQLDialect;
import org.jooq.Schema;
import org.jooq.Select;
import org.jooq.SelectFieldOrAsterisk;
import org.jooq.SortField;
import org.jooq.Table;
import org.jooq.TableRecord;
import org.jooq.UDT;
import org.jooq.UDTRecord;
import org.jooq.UpdatableRecord;
import org.jooq.conf.BackslashEscaping;
import org.jooq.conf.Settings;
import org.jooq.conf.SettingsTools;
import org.jooq.conf.ThrowExceptions;
import org.jooq.exception.DataAccessException;
import org.jooq.exception.DataTypeException;
import org.jooq.exception.MappingException;
import org.jooq.exception.NoDataFoundException;
import org.jooq.exception.TooManyRowsException;
import org.jooq.impl.ResultsImpl.ResultOrRowsImpl;
import org.jooq.impl.Tools.Cache.CachedOperation;
import org.jooq.tools.Ints;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.StringUtils;
import org.jooq.tools.jdbc.JDBCUtils;
import org.jooq.tools.reflect.Reflect;
import org.jooq.tools.reflect.ReflectException;
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

    static final JooqLogger                 log                           = JooqLogger.getLogger(Tools.class);

    // ------------------------------------------------------------------------
    // Empty arrays for use with Collection.toArray()
    // ------------------------------------------------------------------------

    static final byte[]                     EMPTY_BYTE                     = {};
    static final Class<?>[]                 EMPTY_CLASS                    = {};
    static final Clause[]                   EMPTY_CLAUSE                   = {};
    static final Collection<?>[]            EMPTY_COLLECTION               = {};
    static final CommonTableExpression<?>[] EMPTY_COMMON_TABLE_EXPRESSION  = {};
    static final ExecuteListener[]          EMPTY_EXECUTE_LISTENER         = {};
    static final Field<?>[]                 EMPTY_FIELD                    = {};
    static final int[]                      EMPTY_INT                      = {};
    static final Name[]                     EMPTY_NAME                     = {};
    static final Param<?>[]                 EMPTY_PARAM                    = {};
    static final OrderField<?>[]            EMPTY_ORDERFIELD               = {};
    static final Query[]                    EMPTY_QUERY                    = {};
    static final QueryPart[]                EMPTY_QUERYPART                = {};
    static final Record[]                   EMPTY_RECORD                   = {};
    static final RowN[]                     EMPTY_ROWN                     = {};
    static final SelectFieldOrAsterisk[]    EMPTY_SELECT_FIELD_OR_ASTERISK = {};
    static final SortField<?>[]             EMPTY_SORTFIELD                = {};
    static final String[]                   EMPTY_STRING                   = {};
    static final Table<?>[]                 EMPTY_TABLE                    = {};
    static final TableRecord<?>[]           EMPTY_TABLE_RECORD             = {};
    static final UpdatableRecord<?>[]       EMPTY_UPDATABLE_RECORD         = {};

    // ------------------------------------------------------------------------
    // Some constants for use with Context.data()
    // ------------------------------------------------------------------------

    /**
     * Keys for {@link Configuration#data()}, which may be referenced frequently
     * and represent a {@code boolean} value and are thus stored in an
     * {@link EnumSet} for speedier access.
     */
    enum BooleanDataKey {

        /**
         * [#1537] This constant is used internally by jOOQ to omit the RETURNING
         * clause in {@link DSLContext#batchStore(UpdatableRecord...)} calls for
         * {@link SQLDialect#POSTGRES}.
         */
        DATA_OMIT_RETURNING_CLAUSE,

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
         * <li>{@link SQLDialect#ACCESS} : 768</li>
         * <li>{@link SQLDialect#ASE} : 2000</li>
         * <li>{@link SQLDialect#INGRES} : 1024</li>
         * <li>{@link SQLDialect#ORACLE} : 32767</li>
         * <li>{@link SQLDialect#POSTGRES} : 32767</li>
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
        @SuppressWarnings("javadoc")
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
        @SuppressWarnings("javadoc")
        DATA_WRAP_DERIVED_TABLES_IN_PARENTHESES,


























        /**
         * [#1629] The {@link Connection#getAutoCommit()} flag value before starting
         * a new transaction.
         */
        DATA_DEFAULT_TRANSACTION_PROVIDER_AUTOCOMMIT,

        /**
         * [#2080] When emulating OFFSET pagination in certain databases, synthetic
         * aliases are generated that must be referenced also in
         * <code>ORDER BY</code> clauses, in lieu of their corresponding original
         * aliases.
         * [#8898] Oracle doesn't support aliases in RETURNING clauses.
         */
        DATA_UNALIAS_ALIASED_EXPRESSIONS,

        /**
         * [#7139] No data must be selected in the <code>SELECT</code> statement.
         */
        DATA_SELECT_NO_DATA,

        /**
         * [#3381] Omit the {@link Clause#SELECT_INTO}, as it is being emulated.
         */
        @SuppressWarnings("javadoc")
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
         * [#2995] An <code>INSERT INTO t SELECT</code> statement. Without any
         * explicit column list, the <code>SELECT</code> statement must not be
         * wrapped in parentheses (which would be interpreted as the column
         * list's parentheses).
         */
        DATA_INSERT_SELECT_WITHOUT_INSERT_COLUMN_LIST,

        /**
         * [#3579] [#6431] [#7222] There are nested set operations in the current
         * {@link Select} scope.
         */
        DATA_NESTED_SET_OPERATIONS,

        /**
         * [#5191] Whether INSERT RETURNING is being emulated for bulk insertions.
         */
        DATA_EMULATE_BULK_INSERT_RETURNING,









        /**
         * [#1535] We're currently generating the window specification of a ranking function.
         */
        DATA_RANKING_FUNCTION,

    }

    /**
     * Keys for {@link Configuration#data()}, which may be referenced frequently
     * and are thus stored in an {@link EnumMap} for speedier access.
     */
    enum DataKey {

        /**
         * The level of anonymous block nesting, in case we're generating a block.
         */
        DATA_BLOCK_NESTING,

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
         * [#3381] The table to be used for the {@link Clause#SELECT_INTO} clause.
         */
        @SuppressWarnings("javadoc")
        DATA_SELECT_INTO_TABLE,

        /**
         * [#1206] The collected Semi / Anti JOIN predicates.
         */
        DATA_COLLECTED_SEMI_ANTI_JOIN,



































        /**
         * [#6583] The target table on which a DML operation operates on.
         */
        DATA_DML_TARGET_TABLE,

        /**
         * [#8479] There is a WHERE clause to be emulated for ON DUPLICATE KEY
         */
        DATA_ON_DUPLICATE_KEY_WHERE,

        /**
         * [#3607] [#8522] CTEs that need to be added to the top level CTE section.
         */
        DATA_TOP_LEVEL_CTE
    }

    /**
     * Keys for {@link Configuration#data()}, which may be referenced very
     * infrequently and are thus stored in an ordinary {@link HashMap} for a
     * more optimal memory layout.
     */
    enum DataExtendedKey {









    }

    /**
     * [#2965] These are {@link ConcurrentHashMap}s containing caches for
     * reflection information.
     * <p>
     * <code>new String()</code> is used to allow for synchronizing on these
     * objects.
     */
    enum DataCacheKey {
        DATA_REFLECTION_CACHE_GET_ANNOTATED_GETTER("org.jooq.configuration.reflection-cache.get-annotated-getter"),
        DATA_REFLECTION_CACHE_GET_ANNOTATED_MEMBERS("org.jooq.configuration.reflection-cache.get-annotated-members"),
        DATA_REFLECTION_CACHE_GET_ANNOTATED_SETTERS("org.jooq.configuration.reflection-cache.get-annotated-setters"),
        DATA_REFLECTION_CACHE_GET_MATCHING_GETTER("org.jooq.configuration.reflection-cache.get-matching-getter"),
        DATA_REFLECTION_CACHE_GET_MATCHING_MEMBERS("org.jooq.configuration.reflection-cache.get-matching-members"),
        DATA_REFLECTION_CACHE_GET_MATCHING_SETTERS("org.jooq.configuration.reflection-cache.get-matching-setters"),
        DATA_REFLECTION_CACHE_HAS_COLUMN_ANNOTATIONS("org.jooq.configuration.reflection-cache.has-column-annotations"),
        DATA_CACHE_RECORD_MAPPERS("org.jooq.configuration.cache.record-mappers");

        final String key;

        private DataCacheKey(String key) {
            this.key = key;
        }
    }

    // ------------------------------------------------------------------------
    // Other constants
    // ------------------------------------------------------------------------

    /**
     * The default escape character for <code>[a] LIKE [b] ESCAPE [...]</code>
     * clauses.
     */
    static final char                        ESCAPE                         = '!';

    /**
     * A lock for the initialisation of other static members
     */
    private static final Object              initLock                       = new Object();

    /**
     * Indicating whether JPA (<code>javax.persistence</code>) is on the
     * classpath.
     */
    private static volatile Boolean          isJPAAvailable;

    /**
     * Indicating whether Kotlin (<code>kotlin.*</code>) is on the classpath.
     */
    private static volatile Boolean          isKotlinAvailable;
    private static volatile Reflect          ktJvmClassMapping;
    private static volatile Reflect          ktKClasses;
    private static volatile Reflect          ktKClass;

    /**
     * [#3696] The maximum number of consumed exceptions in
     * {@link #consumeExceptions(Configuration, PreparedStatement, SQLException)}
     * helps prevent infinite loops and {@link OutOfMemoryError}.
     */
    private static int                       maxConsumedExceptions          = 256;
    private static int                       maxConsumedResults             = 65536;

    /**
     * A pattern for the dash line syntax
     */
    private static final Pattern             DASH_PATTERN                   = Pattern.compile("(-+)");

    /**
     * A pattern for the pipe line syntax
     */
    private static final Pattern             PIPE_PATTERN                   = Pattern.compile("(?<=\\|)([^|]+)(?=\\|)");

    /**
     * A pattern for the dash line syntax
     */
    private static final Pattern             PLUS_PATTERN                   = Pattern.compile("\\+(-+)(?=\\+)");

    /**
     * All characters that are matched by Java's interpretation of \s.
     * <p>
     * For a more accurate set of whitespaces, refer to
     * http://stackoverflow.com/a/4731164/521799. In the event of SQL
     * processing, it is probably safe to ignore most of those alternative
     * Unicode whitespaces.
     */
    private static final char[]              WHITESPACE_CHARACTERS          = " \t\n\u000B\f\r".toCharArray();

    /**
     * Acceptable prefixes for JDBC escape syntax.
     */
    private static final char[][]            JDBC_ESCAPE_PREFIXES           = {
        "{fn ".toCharArray(),
        "{d ".toCharArray(),
        "{t ".toCharArray(),
        "{ts ".toCharArray()
    };

    private static final char[]              TOKEN_SINGLE_LINE_COMMENT      = { '-', '-' };
    private static final char[]              TOKEN_HASH                     = { '#' };
    private static final char[]              TOKEN_MULTI_LINE_COMMENT_OPEN  = { '/', '*' };
    private static final char[]              TOKEN_MULTI_LINE_COMMENT_CLOSE = { '*', '/' };
    private static final char[]              TOKEN_APOS                     = { '\'' };
    private static final char[]              TOKEN_ESCAPED_APOS             = { '\'', '\'' };

    /**
     * "Suffixes" that are placed behind a "?" character to form an operator,
     * rather than a JDBC bind variable. This is particularly useful to prevent
     * parsing PostgreSQL operators as bind variables, as can be seen here:
     * <a href=
     * "https://www.postgresql.org/docs/9.5/static/functions-json.html">https://www.postgresql.org/docs/current/static/functions-json.html</a>,
     * <a href=
     * "https://www.postgresql.org/docs/current/static/ltree.html">https://www.postgresql.org/docs/current/static/ltree.html</a>,
     * <a href=
     * "https://www.postgresql.org/docs/current/static/functions-geometry.html">https://www.postgresql.org/docs/current/static/functions-geometry.html</a>.
     * <p>
     * [#5307] Known PostgreSQL JSON operators:
     * <ul>
     * <li>?|</li>
     * <li>?&</li>
     * </ul>
     * <p>
     * [#7035] Known PostgreSQL LTREE operators:
     * <ul>
     * <li>? (we cannot handle this one)</li>
     * <li>?@&gt;</li>
     * <li>?&lt;@</li>
     * <li>?~</li>
     * <li>?@</li>
     * </ul>
     * <p>
     * [#7037] Known PostgreSQL Geometry operators:
     * <ul>
     * <li>?#</li>
     * <li>?-</li>
     * <li>?|</li>
     * </ul>
     */
    private static final char[][]            NON_BIND_VARIABLE_SUFFIXES     = {
        { '?' },
        { '|' },
        { '&' },
        { '@' },
        { '<' },
        { '~' },
        { '#' },
        { '-' }
    };

    /**
     * All hexadecimal digits accessible through array index, e.g.
     * <code>HEX_DIGITS[15] == 'f'</code>.
     */
    private static final char[]   HEX_DIGITS                                   = "0123456789abcdef".toCharArray();

    private static final EnumSet<SQLDialect> REQUIRES_BACKSLASH_ESCAPING       = EnumSet.of(MARIADB, MYSQL);
    private static final EnumSet<SQLDialect> NO_SUPPORT_NULL                   = EnumSet.of(DERBY, FIREBIRD, HSQLDB);
    private static final EnumSet<SQLDialect> NO_SUPPORT_BINARY_TYPE_LENGTH     = EnumSet.of(POSTGRES);
    private static final EnumSet<SQLDialect> NO_SUPPORT_CAST_TYPE_IN_DDL       = EnumSet.of(MARIADB, MYSQL);
    private static final EnumSet<SQLDialect> DEFAULT_BEFORE_NULL               = EnumSet.of(FIREBIRD, HSQLDB);
    private static final EnumSet<SQLDialect> SUPPORT_MYSQL_SYNTAX              = EnumSet.of(MARIADB, MYSQL);

    // ------------------------------------------------------------------------
    // XXX: Record constructors and related methods
    // ------------------------------------------------------------------------

    /**
     * Turn a {@link Result} into a list of {@link Row}
     */
    static final List<Row> rows(Result<?> result) {
        List<Row> rows = new ArrayList<>(result.size());

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

            return new RecordDelegate<>(configuration, record);
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
        return attachable.configuration();
    }

    /**
     * Get an attachable's configuration or a new {@link DefaultConfiguration}
     * if <code>null</code>.
     */
    static final Configuration configuration(Attachable attachable) {
        return configuration(attachable.configuration());
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

    static final <T> SortField<T> sortField(OrderField<T> field) {
        if (field instanceof SortField)
            return (SortField<T>) field;
        else if (field instanceof Field)
            return ((Field<T>) field).sortDefault();
        else
            throw new IllegalArgumentException("Field not supported : " + field);
    }

    static final SortField<?>[] sortFields(OrderField<?>[] fields) {
        if (fields == null)
            return null;

        if (fields instanceof SortField<?>[])
            return (SortField<?>[]) fields;

        SortField<?>[] result = new SortField[fields.length];
        for (int i = 0; i < fields.length; i++)
            result[i] = sortField(fields[i]);

        return result;
    }

    static final List<SortField<?>> sortFields(Collection<? extends OrderField<?>> fields) {
        if (fields == null)
            return null;

        int size = fields.size();
        List<SortField<?>> result = new ArrayList<>(size);
        for (OrderField<?> field : fields)
            result.add(sortField(field));

        return result;
    }

    static final Name[] fieldNames(int length) {
        Name[] result = new Name[length];

        for (int i = 0; i < length; i++)
            result[i] = name("v" + i);

        return result;
    }

    static final String[] fieldNameStrings(int length) {
        String[] result = new String[length];

        for (int i = 0; i < length; i++)
            result[i] = "v" + i;

        return result;
    }

    static final Name[] fieldNames(Field<?>[] fields) {
        if (fields == null)
            return null;

        Name[] result = new Name[fields.length];

        for (int i = 0; i < fields.length; i++)
            result[i] = fields[i].getUnqualifiedName();

        return result;
    }

    static final String[] fieldNameStrings(Field<?>[] fields) {
        if (fields == null)
            return null;

        String[] result = new String[fields.length];

        for (int i = 0; i < fields.length; i++)
            result[i] = fields[i].getName();

        return result;
    }

    static final Field<?>[] fields(int length) {
        return fields(length, SQLDataType.OTHER);
    }

    @SuppressWarnings("unchecked")
    static final <T> Field<T>[] fields(int length, DataType<T> type) {
        Field<T>[] result = new Field[length];
        Name[] names = fieldNames(length);

        for (int i = 0; i < length; i++)
            result[i] = DSL.field(name(names[i]), type);

        return result;
    }

    static final Field<?>[] unqualified(Field<?>[] fields) {
        if (fields == null)
            return null;

        Field<?>[] result = new Field[fields.length];

        for (int i = 0; i < fields.length; i++)
            result[i] = DSL.field(fields[i].getUnqualifiedName(), fields[i].getDataType());

        return result;
    }

    static final Name[] unqualifiedNames(Field<?>[] fields) {
        if (fields == null)
            return null;

        Name[] result = new Name[fields.length];

        for (int i = 0; i < fields.length; i++)
            result[i] = fields[i].getUnqualifiedName();

        return result;
    }

    static final Field<?>[] aliasedFields(Field<?>[] fields, Name[] aliases) {
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

    static final Field<?>[] fieldsByName(Name tableName, Name[] fieldNames) {
        if (fieldNames == null)
            return null;

        Field<?>[] result = new Field[fieldNames.length];

        if (tableName == null)
            for (int i = 0; i < fieldNames.length; i++)
                result[i] = DSL.field(fieldNames[i]);
        else
            for (int i = 0; i < fieldNames.length; i++)
                result[i] = DSL.field(name(tableName, fieldNames[i]));

        return result;
    }

    static final Field<?>[] fieldsByName(String tableName, String[] fieldNames) {
        if (fieldNames == null)
            return null;

        Field<?>[] result = new Field[fieldNames.length];

        if (StringUtils.isEmpty(tableName))
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

    static final List<Name> names(Collection<?> names) {
        if (names == null)
            return null;

        List<Name> result = new ArrayList<>(names.size());

        for (Object o : names)
            result.add(o instanceof Name ? (Name) o : DSL.name(String.valueOf(o)));

        return result;
    }

    private static final IllegalArgumentException fieldExpected(Object value) {
        return new IllegalArgumentException("Cannot interpret argument of type " + value.getClass() + " as a Field: " + value);
    }

    /**
     * [#461] [#473] [#2597] [#8234] Some internals need a cast only if necessary.
     */
    @SuppressWarnings("unchecked")
    static <T> Field<T>[] castAllIfNeeded(Field<?>[] fields, Class<T> type) {
        Field<T>[] castFields = new Field[fields.length];

        for (int i = 0; i < fields.length; i++)
            castFields[i] = castIfNeeded(fields[i], type);

        return castFields;
    }

    /**
     * [#461] [#473] [#2597] [#8234] Some internals need a cast only if necessary.
     */
    @SuppressWarnings("unchecked")
    static final <T> Field<T> castIfNeeded(Field<?> field, Class<T> type) {
        if (field.getType().equals(type))
            return (Field<T>) field;
        else
            return field.cast(type);
    }

    /**
     * [#461] [#473] [#2597] [#8234] Some internals need a cast only if necessary.
     */
    @SuppressWarnings("unchecked")
    static final <T> Field<T> castIfNeeded(Field<?> field, DataType<T> type) {
        if (field.getDataType().equals(type))
            return (Field<T>) field;
        else
            return field.cast(type);
    }

    /**
     * [#461] [#473] [#2597] [#8234] Some internals need a cast only if necessary.
     */
    @SuppressWarnings("unchecked")
    static final <T> Field<T> castIfNeeded(Field<?> field, Field<T> type) {
        if (field.getDataType().equals(type.getDataType()))
            return (Field<T>) field;
        else
            return field.cast(type);
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
        if (value instanceof Field<?>)
            return (Field<T>) value;

        // [#6362] Single-column selects can be considered fields, too
        else if (value instanceof Select && ((Select<?>) value).getSelect().size() == 1)
            return DSL.field((Select<Record1<T>>) value);

        // [#4771] Any other QueryPart type is not supported here
        else if (value instanceof QueryPart)
            throw fieldExpected(value);

        else
            return val(value);
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

    static final Param<Instant> field(Instant value) {
        return val((Object) value, SQLDataType.INSTANT);
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
        if (value instanceof Field<?>)
            return (Field<T>) value;

        // [#4771] Any other QueryPart type is not supported here
        else if (value instanceof QueryPart)
            throw fieldExpected(value);

        else
            return val(value, field);
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
        if (value instanceof Field<?>)
            return (Field<T>) value;

        // [#4771] Any other QueryPart type is not supported here
        else if (value instanceof QueryPart)
            throw fieldExpected(value);

        else
            return val(value, type);
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
        if (value instanceof Field<?>)
            return (Field<T>) value;

        // [#4771] Any other QueryPart type is not supported here
        else if (value instanceof QueryPart)
            throw fieldExpected(value);

        else
            return val(value, type);
    }

    /**
     * Be sure that a given set of objects are fields.
     *
     * @param values The argument objects
     * @return The argument objects themselves, if they are {@link Field}s, or a bind
     *         values created from the argument objects.
     */
    static final <T> List<Field<T>> fields(T[] values) {
        if (values == null)
            return new ArrayList<>();

        List<Field<T>> result = new ArrayList<>(values.length);

        for (int i = 0; i < values.length; i++)
            result.add(field(values[i]));

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
    static final <T> List<Field<T>> fields(Object[] values, Field<T> field) {
        if (values == null || field == null)
            return new ArrayList<>();

        List<Field<T>> result = new ArrayList<>(values.length);

        for (int i = 0; i < values.length; i++)
            result.add(field(values[i], field));

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
        if (values == null || fields == null)
            return new ArrayList<>();

        int length = Math.min(values.length, fields.length);
        List<Field<?>> result = new ArrayList<>(length);

        for (int i = 0; i < length; i++)
            result.add(field(values[i], fields[i]));

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
    static final Field<?>[] fieldsArray(Object[] values, Field<?>[] fields) {
        if (values == null || fields == null)
            return EMPTY_FIELD;

        int length = Math.min(values.length, fields.length);
        Field<?>[] result = new Field[length];

        for (int i = 0; i < length; i++)
            result[i] = field(values[i], fields[i]);

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
    static final <T> List<Field<T>> fields(Object[] values, Class<T> type) {
        if (values == null || type == null)
            return new ArrayList<>();

        List<Field<T>> result = new ArrayList<>(values.length);

        for (int i = 0; i < values.length; i++)
            result.add(field(values[i], type));

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
        if (values == null || types == null)
            return new ArrayList<>();

        int length = Math.min(values.length, types.length);
        List<Field<?>> result = new ArrayList<>(length);

        for (int i = 0; i < length; i++)
            result.add(field(values[i], types[i]));

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
    static final <T> List<Field<T>> fields(Object[] values, DataType<T> type) {
        if (values == null || type == null)
            return new ArrayList<>();

        List<Field<T>> result = new ArrayList<>(values.length);

        for (Object value : values)
            result.add(field(value, type));

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
    static final <T> List<Field<T>> fields(Collection<?> values, DataType<T> type) {
        if (values == null || type == null)
            return new ArrayList<>();

        List<Field<T>> result = new ArrayList<>(values.size());

        for (Object value : values)
            result.add(field(value, type));

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
        if (values == null || types == null)
            return new ArrayList<>();

        int length = Math.min(values.length, types.length);
        List<Field<?>> result = new ArrayList<>(length);

        for (int i = 0; i < length; i++)
            result.add(field(values[i], types[i]));

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
    static final Field<?>[] fieldsArray(Object[] values, DataType<?>[] types) {
        if (values == null || types == null)
            return EMPTY_FIELD;

        int length = Math.min(values.length, types.length);
        Field<?>[] result = new Field[length];

        for (int i = 0; i < length; i++)
            result[i] = field(values[i], types[i]);

        return result;
    }

    static final <T> List<Field<T>> inline(T[] values) {
        if (values == null)
            return new ArrayList<>();

        List<Field<T>> result = new ArrayList<>(values.length);

        for (int i = 0; i < values.length; i++)
            result.add(DSL.inline(values[i]));

        return result;
    }

    @SuppressWarnings("unchecked")
    static final Field<Integer>[] inline(int[] fieldIndexes) {
        if (fieldIndexes == null)
            return (Field<Integer>[]) EMPTY_FIELD;

        Field<Integer>[] result = new Field[fieldIndexes.length];

        for (int i = 0; i < fieldIndexes.length; i++)
            result[i] = DSL.inline(fieldIndexes[i]);

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
     * Reverse an array.
     */

    @SafeVarargs

    static final <T> T[] reverse(T... array) {
        if (array == null)
            return null;

        for (int i = 0; i < array.length / 2; i++) {
            T tmp = array[i];
            array[i] = array[array.length - i - 1];
            array[array.length - i - 1] = tmp;
        }

        return array;
    }

    /**
     * Reverse iterate over an array.
     */

    @SafeVarargs

    static final <T> Iterable<T> reverseIterable(final T... array) {
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return reverseIterator(array);
            }
        };
    }

    /**
     * Reverse iterate over an array.
     */

    @SafeVarargs

    static final <T> Iterator<T> reverseIterator(final T... array) {
        return new Iterator<T>() {
            int index = array.length;

            @Override
            public boolean hasNext() {
                return index > 0;
            }

            @Override
            public T next() {
                return array[--index];
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("remove");
            }
        };
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
        Map<Field<?>, Object> result = new LinkedHashMap<>();
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

            if (iterator.hasNext())
                return iterator.next();
            else
                return null;
        }
    }

    /**
     * Sets the statement's fetch size to the given value or
     * {@link org.jooq.conf.Settings#getFetchSize()} if {@code 0}.
     * <p>
     * This method should not be called before {@link ExecuteContext#statement(PreparedStatement)}.
     */
    static final void setFetchSize(ExecuteContext ctx, int fetchSize) throws SQLException {
        // [#1263] [#4753] Allow for negative fetch sizes to support some non-standard
        // MySQL feature, where Integer.MIN_VALUE is used
        int f = SettingsTools.getFetchSize(fetchSize, ctx.settings());
        if (f != 0) {
            if (log.isDebugEnabled())
                log.debug("Setting fetch size", f);

            PreparedStatement statement = ctx.statement();
            if (statement != null)
                statement.setFetchSize(f);
        }
    }

    /**
     * Get the only element from a list or <code>null</code>, or throw an
     * exception.
     *
     * @param list The list
     * @return The only element from the list or <code>null</code>
     * @throws TooManyRowsException Thrown if the list contains more than one
     *             element
     * @deprecated - [#8881] - Do not reuse this method as it doesn't properly
     *             manage the {@link ExecuteListener#exception(ExecuteContext)}
     *             lifecycle event. Use {@link #fetchOne(Cursor)} instead.
     */
    @Deprecated
    static final <R extends Record> R filterOne(List<R> list) throws TooManyRowsException {
        int size = list.size();

        if (size == 0)
            return null;
        else if (size == 1)
            return list.get(0);
        else
            throw new TooManyRowsException("Too many rows selected : " + size);
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
        return fetchOne(cursor, false);
    }

    /**
     * Get the only element from a cursor or <code>null</code>, or throw an
     * exception.
     * <p>
     * [#2373] This method will always close the argument cursor, as it is
     * supposed to be completely consumed by this method.
     *
     * @param cursor The cursor
     * @param hasLimit1 Whether a LIMIT clause is present that guarantees at
     *            most one row
     * @return The only element from the cursor or <code>null</code>
     * @throws TooManyRowsException Thrown if the cursor returns more than one
     *             element
     */
    static final <R extends Record> R fetchOne(Cursor<R> cursor, boolean hasLimit1) throws TooManyRowsException {
        try {

            // [#7001] Fetching at most two rows rather than at most one row
            //         (and then checking of additional rows) improves debug logs
            // [#7430] Avoid fetching the second row (additional overhead) if
            //         there is a guarantee of at most one row
            Result<R> result = cursor.fetchNext(hasLimit1 ? 1 : 2);
            int size = result.size();

            if (size == 0)
                return null;
            else if (size == 1)
                return result.get(0);
            else
                throw exception((CursorImpl<R>) cursor, new TooManyRowsException("Cursor returned more than one result"));
        }
        finally {
            cursor.close();
        }
    }

    /**
     * Get the only element from a cursor, or throw an exception.
     * <p>
     * [#2373] This method will always close the argument cursor, as it is
     * supposed to be completely consumed by this method.
     *
     * @param cursor The cursor
     * @return The only element from the cursor
     * @throws NoDataFoundException Thrown if the cursor did not return any rows
     * @throws TooManyRowsException Thrown if the cursor returns more than one
     *             element
     */
    static final <R extends Record> R fetchSingle(Cursor<R> cursor) throws NoDataFoundException, TooManyRowsException {
        return fetchSingle(cursor, false);
    }

    /**
     * Get the only element from a cursor, or throw an exception.
     * <p>
     * [#2373] This method will always close the argument cursor, as it is
     * supposed to be completely consumed by this method.
     *
     * @param cursor The cursor
     * @param hasLimit1 Whether a LIMIT clause is present that guarantees at
     *            most one row
     * @return The only element from the cursor
     * @throws NoDataFoundException Thrown if the cursor did not return any rows
     * @throws TooManyRowsException Thrown if the cursor returns more than one
     *             element
     */
    static final <R extends Record> R fetchSingle(Cursor<R> cursor, boolean hasLimit1) throws NoDataFoundException, TooManyRowsException {
        try {

            // [#7001] Fetching at most two rows rather than at most one row
            //         (and then checking of additional rows) improves debug logs
            // [#7430] Avoid fetching the second row (additional overhead) if
            //         there is a guarantee of at most one row
            Result<R> result = cursor.fetchNext(hasLimit1 ? 1 : 2);
            int size = result.size();

            if (size == 0)
                throw exception((CursorImpl<R>) cursor, new NoDataFoundException("Cursor returned no rows"));
            else if (size == 1)
                return result.get(0);
            else
                throw exception((CursorImpl<R>) cursor, new TooManyRowsException("Cursor returned more than one result"));
        }
        finally {
            cursor.close();
        }
    }

    private static final RuntimeException exception(CursorImpl<?> cursor, RuntimeException e) {

        // [#8877] Make sure these exceptions pass through ExecuteListeners as well
        cursor.ctx.exception(e);
        cursor.listener.exception(cursor.ctx);
        return cursor.ctx.exception();
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

        SQLDialect dialect = render.dialect();
        SQLDialect family = dialect.family();
        boolean mysql = SUPPORT_MYSQL_SYNTAX.contains(family);
        char[][][] quotes = QUOTES.get(family);

        // [#3630] Depending on this setting, we need to consider backslashes as escape characters within string literals.
        boolean needsBackslashEscaping = needsBackslashEscaping(ctx.configuration());

        characterLoop:
        for (int i = 0; i < sqlChars.length; i++) {

            // [#1797] Skip content inside of single-line comments, e.g.
            // select 1 x -- what's this ?'?
            // from t_book -- what's that ?'?
            // where id = ?
            if (peek(sqlChars, i, TOKEN_SINGLE_LINE_COMMENT) ||

            // [#4182] MySQL also supports # as a comment character, and requires
            // -- to be followed by a whitespace, although the latter is also not
            // handled correctly by the MySQL JDBC driver (yet). See
            // http://bugs.mysql.com/bug.php?id=76623
                (mysql && peek(sqlChars, i, TOKEN_HASH))) {

                // Consume the complete comment
                for (; i < sqlChars.length && sqlChars[i] != '\r' && sqlChars[i] != '\n'; render.sql(sqlChars[i++]));

                // Consume the newline character
                if (i < sqlChars.length) render.sql(sqlChars[i]);
            }

            // [#1797] Skip content inside of multi-line comments, e.g.
            // select 1 x /* what's this ?'?
            // I don't know ?'? */
            // from t_book where id = ?
            else if (peek(sqlChars, i, TOKEN_MULTI_LINE_COMMENT_OPEN)) {

                int nestedMultilineCommentLevel = 1;

                // Consume the complete comment
                for (;;) {
                    render.sql(sqlChars[i++]);

                    if (peek(sqlChars, i, TOKEN_MULTI_LINE_COMMENT_OPEN))
                        nestedMultilineCommentLevel++;
                    else if (peek(sqlChars, i, TOKEN_MULTI_LINE_COMMENT_CLOSE))
                        nestedMultilineCommentLevel--;

                    if (nestedMultilineCommentLevel == 0)
                        break;
                }

                // Consume the comment delimiter
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
                    if (sqlChars[i] == '\\' && needsBackslashEscaping)
                        render.sql(sqlChars[i++]);

                    // Consume an escaped apostrophe
                    else if (peek(sqlChars, i, TOKEN_ESCAPED_APOS))
                        render.sql(sqlChars[i++]);

                    // Break on the terminal string literal delimiter
                    else if (peek(sqlChars, i, TOKEN_APOS))
                        break;

                    // Consume string literal content
                    render.sql(sqlChars[i++]);
                }

                // Consume the terminal string literal delimiter
                render.sql(sqlChars[i]);
            }

            // [#6704] PostgreSQL supports additional quoted string literals, which we must skip: E'...'
            else if ((sqlChars[i] == 'e' || sqlChars[i] == 'E')
                        && (                                                            ctx.family() == POSTGRES)
                        && i + 1 < sqlChars.length
                        && sqlChars[i + 1] == '\'') {

                // Consume the initial string literal delimiters
                render.sql(sqlChars[i++]);
                render.sql(sqlChars[i++]);

                // Consume the whole string literal
                for (;;) {

                    // [#3000] [#3630] Consume backslash-escaped characters if needed
                    if (sqlChars[i] == '\\')
                        render.sql(sqlChars[i++]);

                    // Consume an escaped apostrophe
                    else if (peek(sqlChars, i, TOKEN_ESCAPED_APOS))
                        render.sql(sqlChars[i++]);

                    // Break on the terminal string literal delimiter
                    else if (peek(sqlChars, i, TOKEN_APOS))
                        break;

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
                for (int d = 0; d < quotes[QUOTE_START_DELIMITER][delimiter].length; d++)
                    render.sql(sqlChars[i++]);

                // Consume the whole identifier
                identifierLoop:
                for (;;) {

                    // Consume an escaped quote
                    if (peek(sqlChars, i, quotes[QUOTE_END_DELIMITER_ESCAPED][delimiter])) {
                        for (int d = 0; d < quotes[QUOTE_END_DELIMITER_ESCAPED][delimiter].length; d++)
                            render.sql(sqlChars[i++]);

                        continue identifierLoop;
                    }

                    // Break on the terminal identifier delimiter
                    else if (peek(sqlChars, i, quotes[QUOTE_END_DELIMITER][delimiter]))
                        break identifierLoop;

                    // Consume identifier content
                    render.sql(sqlChars[i++]);
                }

                // Consume the terminal identifier delimiter
                for (int d = 0; d < quotes[QUOTE_END_DELIMITER][delimiter].length; d++) {
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
                    for (char[] suffix : NON_BIND_VARIABLE_SUFFIXES) {
                        if (peek(sqlChars, i + 1, suffix)) {
                            for (int j = i; i - j <= suffix.length; i++)
                                render.sql(sqlChars[i]);

                            render.sql(sqlChars[i]);
                            continue characterLoop;
                        }
                    }
                }

                    // [#4131] Consume the named bind variable
                if (sqlChars[i] == ':')
                    while (i + 1 < sqlChars.length && isJavaIdentifierPart(sqlChars[i + 1]))
                        i++;

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

                if (bind != null)
                    bind.visit(substitute);
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

                    // Try getting the {numbered placeholder}
                    Integer index = Ints.tryParse(sql, start, end);
                    if (index != null) {
                        QueryPart substitute = substitutes.get(index);
                        render.visit(substitute);

                        if (bind != null) {
                            bind.visit(substitute);
                        }
                    } else {
                        // Then we're dealing with a {keyword}
                        render.visit(DSL.keyword(sql.substring(start, end)));
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
        return escaping == ON || (escaping == DEFAULT && REQUIRES_BACKSLASH_ESCAPING.contains(configuration.family()));
    }

    /**
     * Peek for a string at a given <code>index</code> of a <code>char[]</code>
     *
     * @param sqlChars The char array to peek into
     * @param index The index within the char array to peek for a string
     * @param peek The string to peek for
     */
    static final boolean peek(char[] sqlChars, int index, char[] peek) {
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
     *            {@link #WHITESPACE_CHARACTERS}, or in Java Regex "\s".
     */
    static final boolean peek(char[] sqlChars, int index, char[] peek, boolean anyWhitespace) {

        peekArrayLoop:
        for (int i = 0; i < peek.length; i++) {
            if (index + i >= sqlChars.length)
                return false;

            if (sqlChars[index + i] != peek[i]) {

                // [#3430] In some cases, we don't care about the type of whitespace.
                if (anyWhitespace && peek[i] == ' ')
                    for (int j = 0; j < WHITESPACE_CHARACTERS.length; j++)
                        if (sqlChars[index + i] == WHITESPACE_CHARACTERS[j])
                            continue peekArrayLoop;

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
    static final boolean peekAny(char[] sqlChars, int index, char[][] peekAny) {
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
     *            {@link #WHITESPACE_CHARACTERS}, or in Java Regex "\s".
     */
    static final boolean peekAny(char[] sqlChars, int index, char[][] peekAny, boolean anyWhitespace) {
        for (char[] peek : peekAny)
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
            List<QueryPart> result = new ArrayList<>(substitutes.length);

            for (Object substitute : substitutes) {

                // [#1432] Distinguish between QueryParts and other objects
                if (substitute instanceof QueryPart) {
                    result.add((QueryPart) substitute);
                }
                else {
                    @SuppressWarnings("unchecked")
                    Class<Object> type = (Class<Object>) (substitute != null ? substitute.getClass() : Object.class);
                    result.add(new Val<>(substitute, DSL.getDataType(type)));
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
            context.sql(separator).visit(field.getUnqualifiedName());

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
            context.sql(separator).visit(table.getUnqualifiedName());

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
        if (e != null)
            return new DataAccessException("SQL [" + sql + "]; " + e.getMessage(), e);
        else
            return new DataAccessException("SQL [" + sql + "]; Unspecified SQLException");
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
        if (statement != null)
            consumeWarnings(ctx, listener);

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

                // [#4277] We must release the connection on the ExecuteContext's
                //         ConnectionProvider, as the ctx.configuration().connectionProvider()
                //         is replaced by a ExecuteContextConnectionProvider instance.
                if (connection != null && ((DefaultExecuteContext) ctx).connectionProvider != null)
                    ((DefaultExecuteContext) ctx).connectionProvider.release(connection);
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

        if (mapped != null && !"".equals(mapped.getName()))
            sb.append(mapped.getName()).append('.');






        sb.append(record.getUDT().getName());
        return sb.toString();
    }





















































    static final DSLContext CTX    = DSL.using(new DefaultConfiguration());
    static final ParserImpl PARSER = (ParserImpl) CTX.parser();

    /**
     * Return a non-negative hash code for a {@link QueryPart}, taking into
     * account FindBugs' <code>RV_ABSOLUTE_VALUE_OF_HASHCODE</code> pattern
     */
    static final int hash(QueryPart part) {

        // [#6025] Prevent unstable alias generation for derived tables due to
        //         inlined bind variables in hashCode() calculation
        // [#6175] TODO: Speed this up with a faster way to calculate a hash code
        return 0x7FFFFFF & CTX.render(part).hashCode();
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
            return castIfNeeded(field, String.class);
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
        if (isVal(field))
            return ((Param<T>) field).getValue();
        else
            return null;
    }

































    /**
     * Add primary key conditions to a query
     */
    @SuppressWarnings("deprecation")
    static final void addConditions(org.jooq.ConditionProvider query, Record record, Field<?>... keys) {
        for (Field<?> field : keys)
            addCondition(query, record, field);
    }

    /**
     * Add a field condition to a query
     */
    @SuppressWarnings("deprecation")
    static final <T> void addCondition(org.jooq.ConditionProvider provider, Record record, Field<T> field) {

        // [#2764] If primary keys are allowed to be changed, the
        if (updatablePrimaryKeys(settings(record)))
            provider.addConditions(condition(field, record.original(field)));
        else
            provider.addConditions(condition(field, record.get(field)));
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

            ThreadLocal<Object> tl = new ThreadLocal<>();
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
        static final <V> V run(Configuration configuration, CachedOperation<V> operation, DataCacheKey type, Object key) {

            // If no configuration is provided take the default configuration that loads the default Settings
            if (configuration == null)
                configuration = new DefaultConfiguration();

            // Shortcut caching when the relevant Settings flag isn't set.
            if (!reflectionCaching(configuration.settings()))
                return operation.call();

            Map<Object, Object> cache = (Map<Object, Object>) configuration.data(type);
            if (cache == null) {
                synchronized (type) {
                    cache = (Map<Object, Object>) configuration.data(type);

                    if (cache == null) {
                        cache = new ConcurrentHashMap<>();
                        configuration.data(type, cache);
                    }
                }
            }

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
        static final Object key(Object key1, Object key2) {
            return new Key2(key1, key2);
        }

        /**
         * A 2-value key for caching.
         */
        private static class Key2 implements Serializable {

            /**
             * Generated UID.
             */
            private static final long serialVersionUID = 5822370287443922993L;
            private final Object      key1;
            private final Object      key2;

            Key2(Object key1, Object key2) {
                this.key1 = key1;
                this.key2 = key2;
            }

            @Override
            public int hashCode() {
                final int prime = 31;
                int result = 1;
                result = prime * result + ((key1 == null) ? 0 : key1.hashCode());
                result = prime * result + ((key2 == null) ? 0 : key2.hashCode());
                return result;
            }

            @Override
            public boolean equals(Object obj) {
                if (this == obj)
                    return true;
                if (obj == null)
                    return false;
                if (getClass() != obj.getClass())
                    return false;
                Key2 other = (Key2) obj;
                if (key1 == null) {
                    if (other.key1 != null)
                        return false;
                }
                else if (!key1.equals(other.key1))
                    return false;
                if (key2 == null) {
                    if (other.key2 != null)
                        return false;
                }
                else if (!key2.equals(other.key2))
                    return false;
                return true;
            }

            @Override
            public String toString() {
                return "[" + key1 + ", " + key2 + "]";
            }
        }
    }

    // ------------------------------------------------------------------------
    // XXX: Reflection utilities used for POJO mapping
    // ------------------------------------------------------------------------

    /**
     * Check if JPA classes can be loaded. This is only done once per JVM!
     */
    static final boolean isJPAAvailable() {
        if (isJPAAvailable == null) {
            synchronized (initLock) {
                if (isJPAAvailable == null) {
                    try {
                        Class.forName(Column.class.getName());
                        isJPAAvailable = true;
                    }
                    catch (Throwable e) {
                        isJPAAvailable = false;
                    }
                }
            }
        }

        return isJPAAvailable;
    }

    static final boolean isKotlinAvailable() {
        if (isKotlinAvailable == null) {
            synchronized (initLock) {
                if (isKotlinAvailable == null) {
                    try {
                        if (ktJvmClassMapping() != null) {
                            if (ktKClasses() != null) {
                                isKotlinAvailable = true;
                            }
                            else {
                                isKotlinAvailable = false;
                                log.info("Kotlin is available, but not kotlin-reflect. Add the kotlin-reflect dependency to better use Kotlin features like data classes");
                            }
                        }
                        else {
                            isKotlinAvailable = false;
                        }
                    }
                    catch (ReflectException e) {
                        isKotlinAvailable = false;
                    }
                }
            }
        }

        return isKotlinAvailable;
    }

    static final Reflect ktJvmClassMapping() {
        if (ktJvmClassMapping == null) {
            synchronized (initLock) {
                if (ktJvmClassMapping == null) {
                    try {
                        ktJvmClassMapping = Reflect.on("kotlin.jvm.JvmClassMappingKt");
                    }
                    catch (ReflectException ignore) {}
                }
            }
        }

        return ktJvmClassMapping;
    }

    static final Reflect ktKClasses() {
        if (ktKClasses == null) {
            synchronized (initLock) {
                if (ktKClasses == null) {
                    try {
                        ktKClasses = Reflect.on("kotlin.reflect.full.KClasses");
                    }
                    catch (ReflectException ignore) {}
                }
            }
        }

        return ktKClasses;
    }

    static final Reflect ktKClass() {
        if (ktKClass == null) {
            synchronized (initLock) {
                if (ktKClass == null) {
                    try {
                        ktKClass = Reflect.on("kotlin.reflect.KClass");
                    }
                    catch (ReflectException ignore) {}
                }
            }
        }

        return ktKClass;
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
                List<java.lang.reflect.Field> result = new ArrayList<>();

                for (java.lang.reflect.Field member : getInstanceMembers(type)) {
                    Column column = member.getAnnotation(Column.class);

                    if (column != null) {
                        if (namesMatch(name, column.name()))
                            result.add(accessible(member));
                    }

                    else {
                        Id id = member.getAnnotation(Id.class);

                        if (id != null)
                            if (namesMatch(name, member.getName()))
                                result.add(accessible(member));
                    }
                }

                return result;
            }
        }, DATA_REFLECTION_CACHE_GET_ANNOTATED_MEMBERS, Cache.key(type, name));
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
                List<java.lang.reflect.Field> result = new ArrayList<>();

                // [#1942] Caching these values before the field-loop significantly
                // accerates POJO mapping
                String camelCaseLC = StringUtils.toCamelCaseLC(name);

                for (java.lang.reflect.Field member : getInstanceMembers(type))
                    if (name.equals(member.getName()))
                        result.add(accessible(member));
                    else if (camelCaseLC.equals(member.getName()))
                        result.add(accessible(member));

                return result;
            }

        }, DATA_REFLECTION_CACHE_GET_MATCHING_MEMBERS, Cache.key(type, name));
    }

    /**
     * Get all setter methods annotated with a given column name
     */
    static final List<Method> getAnnotatedSetters(final Configuration configuration, final Class<?> type, final String name) {
        return Cache.run(configuration, new CachedOperation<List<Method>>() {

            @Override
            public List<Method> call() {
                Set<SourceMethod> set = new LinkedHashSet<>();

                for (Method method : getInstanceMethods(type)) {
                    Column column = method.getAnnotation(Column.class);

                    if (column != null && namesMatch(name, column.name())) {

                        // Annotated setter
                        if (method.getParameterTypes().length == 1) {
                            set.add(new SourceMethod(accessible(method)));
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

                                    // [#7953] [#8496] Search the hierarchy for a matching setter
                                    Method setter = getInstanceMethod(type, "set" + suffix, new Class[] { method.getReturnType() });

                                    // Setter annotation is more relevant
                                    if (setter.getAnnotation(Column.class) == null)
                                        set.add(new SourceMethod(accessible(setter)));
                                }
                                catch (NoSuchMethodException ignore) {}
                            }
                        }
                    }
                }

                return SourceMethod.methods(set);
            }

        }, DATA_REFLECTION_CACHE_GET_ANNOTATED_SETTERS, Cache.key(type, name));
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
                                    if (getter.getAnnotation(Column.class) == null)
                                        return accessible(getter);
                                }
                                catch (NoSuchMethodException ignore) {}

                                try {
                                    Method getter = type.getMethod("is" + m.substring(3));

                                    // Getter annotation is more relevant
                                    if (getter.getAnnotation(Column.class) == null)
                                        return accessible(getter);
                                }
                                catch (NoSuchMethodException ignore) {}
                            }
                        }
                    }
                }

                return null;
            }

        }, DATA_REFLECTION_CACHE_GET_ANNOTATED_GETTER, Cache.key(type, name));
    }

    /**
     * Get all setter methods matching a given column name
     */
    static final List<Method> getMatchingSetters(final Configuration configuration, final Class<?> type, final String name) {
        return Cache.run(configuration, new CachedOperation<List<Method>>() {

            @Override
            public List<Method> call() {

                // [#8460] Prevent duplicate methods in the call hierarchy
                Set<SourceMethod> set = new LinkedHashSet<>();

                // [#1942] Caching these values before the method-loop significantly
                // accerates POJO mapping
                String camelCase = StringUtils.toCamelCase(name);
                String camelCaseLC = StringUtils.toLC(camelCase);

                for (Method method : getInstanceMethods(type)) {
                    Class<?>[] parameterTypes = method.getParameterTypes();

                    if (parameterTypes.length == 1)
                        if (name.equals(method.getName()))
                            set.add(new SourceMethod(accessible(method)));
                        else if (camelCaseLC.equals(method.getName()))
                            set.add(new SourceMethod(accessible(method)));
                        else if (("set" + name).equals(method.getName()))
                            set.add(new SourceMethod(accessible(method)));
                        else if (("set" + camelCase).equals(method.getName()))
                            set.add(new SourceMethod(accessible(method)));
                }

                return SourceMethod.methods(set);
            }

        }, DATA_REFLECTION_CACHE_GET_MATCHING_SETTERS, Cache.key(type, name));
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

                for (Method method : getInstanceMethods(type))
                    if (method.getParameterTypes().length == 0)
                        if (name.equals(method.getName()))
                            return accessible(method);
                        else if (camelCaseLC.equals(method.getName()))
                            return accessible(method);
                        else if (("get" + name).equals(method.getName()))
                            return accessible(method);
                        else if (("get" + camelCase).equals(method.getName()))
                            return accessible(method);
                        else if (("is" + name).equals(method.getName()))
                            return accessible(method);
                        else if (("is" + camelCase).equals(method.getName()))
                            return accessible(method);

                return null;
            }

        }, DATA_REFLECTION_CACHE_GET_MATCHING_GETTER, Cache.key(type, name));
    }

    /**
     * A wrapper class that re-implements {@link Method#equals(Object)} and
     * {@link Method#hashCode()} based only on the "source signature" (name,
     * parameter types), instead of the "binary signature" (declaring class,
     * name, return type, parameter types).
     */
    private static final class SourceMethod {
        final Method method;

        SourceMethod(Method method) {
            this.method = method;
        }

        static List<Method> methods(Collection<? extends SourceMethod> methods) {
            List<Method> result = new ArrayList<>(methods.size());

            for (SourceMethod s : methods)
                result.add(s.method);

            return result;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((method == null) ? 0 : method.getName().hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof SourceMethod) {
                Method other = ((SourceMethod) obj).method;

                if (method.getName().equals(other.getName())) {
                    Class<?>[] p1 = method.getParameterTypes();
                    Class<?>[] p2 = other.getParameterTypes();

                    return Arrays.equals(p1, p2);
                }
            }

            return false;
        }

        @Override
        public String toString() {
            return method.toString();
        }
    }

    private static final Method getInstanceMethod(Class<?> type, String name, Class<?>[] parameters) throws NoSuchMethodException {

        // first priority: find a public method with exact signature match in class hierarchy
        try {
            return type.getMethod(name, parameters);
        }

        // second priority: find a private method with exact signature match on declaring class
        catch (NoSuchMethodException e) {
            do {
                try {
                    return type.getDeclaredMethod(name, parameters);
                }
                catch (NoSuchMethodException ignore) {}

                type = type.getSuperclass();
            }
            while (type != null);

            throw new NoSuchMethodException();
        }
    }

    /**
     * All the public and declared methods of a type.
     * <p>
     * This method returns each method only once. Public methods are returned
     * first in the resulting set while declared methods are returned
     * afterwards, from lowest to highest type in the type hierarchy.
     */
    private static final Set<Method> getInstanceMethods(Class<?> type) {
        Set<Method> result = new LinkedHashSet<>();

        for (Method method : type.getMethods())
            if ((method.getModifiers() & Modifier.STATIC) == 0)
                result.add(method);

        do {
            for (Method method : type.getDeclaredMethods())
                if ((method.getModifiers() & Modifier.STATIC) == 0)
                    result.add(method);

            type = type.getSuperclass();
        }
        while (type != null);

        return result;
    }

    private static final List<java.lang.reflect.Field> getInstanceMembers(Class<?> type) {
        List<java.lang.reflect.Field> result = new ArrayList<>();

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

        if (name.startsWith("is") && name.length() > 2)
            name = name.substring(2, 3).toLowerCase() + name.substring(3);
        else if (name.startsWith("get") && name.length() > 3)
            name = name.substring(3, 4).toLowerCase() + name.substring(4);
        else if (name.startsWith("set") && name.length() > 3)
            name = name.substring(3, 4).toLowerCase() + name.substring(4);

        return name;
    }

    // ------------------------------------------------------------------------
    // XXX: JDBC helper methods
    // ------------------------------------------------------------------------

    /**
     * [#3011] [#3054] [#6390] [#6413] Consume additional exceptions if there
     * are any and append them to the <code>previous</code> exception's
     * {@link SQLException#getNextException()} list.
     */
    static final void consumeExceptions(Configuration configuration, PreparedStatement stmt, SQLException previous) {

        // [#6413] Don't consume any additional exceptions if we're throwing only the first.
        ThrowExceptions exceptions = configuration.settings().getThrowExceptions();
        if (exceptions == THROW_FIRST)
            return;























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
     * [#5666] Handle the complexity of each dialect's understanding of
     * correctly calling {@link Statement#execute()}.
     */
    static final SQLException executeStatementAndGetFirstResultSet(ExecuteContext ctx, int skipUpdateCounts) throws SQLException {
        PreparedStatement stmt = ctx.statement();

        try {















































































                             if (stmt.execute()) {
                ctx.resultSet(stmt.getResultSet());
            }

            else {
                ctx.resultSet(null);
                ctx.rows(stmt.getUpdateCount());
            }

            return null;
        }

        // [#3011] [#3054] [#6390] [#6413] Consume additional exceptions if there are any
        catch (SQLException e) {
            if (ctx.settings().getThrowExceptions() != THROW_NONE) {
                consumeExceptions(ctx.configuration(), ctx.statement(), e);
                throw e;
            }
            else {
                return e;
            }
        }
    }











    /**
     * [#3681] Consume all {@link ResultSet}s from a JDBC {@link Statement}.
     */
    static final void consumeResultSets(ExecuteContext ctx, ExecuteListener listener, Results results, Intern intern, SQLException prev) throws SQLException {
        boolean anyResults = false;
        int i = 0;
        int rows = (ctx.resultSet() == null) ? ctx.rows() : 0;

        for (i = 0; i < maxConsumedResults; i++) {
            try {
                if (ctx.resultSet() != null) {
                    anyResults = true;

                    Field<?>[] fields = new MetaDataFieldProvider(ctx.configuration(), ctx.resultSet().getMetaData()).getFields();
                    Cursor<Record> c = new CursorImpl<>(ctx, listener, fields, intern != null ? intern.internIndexes(fields) : null, true, false);
                    results.resultsOrRows().add(new ResultOrRowsImpl(c.fetch()));
                }
                else if (prev == null) {
                    if (rows != -1)
                        results.resultsOrRows().add(new ResultOrRowsImpl(rows));
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

                prev = null;
            }

            // [#3011] [#3054] [#6390] [#6413] Consume additional exceptions if there are any
            catch (SQLException e) {
                prev = e;

                if (ctx.settings().getThrowExceptions() == THROW_NONE) {
                    ctx.sqlException(e);
                    results.resultsOrRows().add(new ResultOrRowsImpl(Tools.translate(ctx.sql(), e)));
                }
                else {
                    consumeExceptions(ctx.configuration(), ctx.statement(), e);
                    throw e;
                }
            }
        }

        if (i == maxConsumedResults)
            log.warn("Maximum consumed results reached: " + maxConsumedResults + ". This is probably a bug. Please report to https://github.com/jOOQ/jOOQ/issues/new");

        // Call this only when there was at least one ResultSet.
        // Otherwise, this call is not supported by ojdbc or CUBRID [#4440]
        if (anyResults && ctx.family() != CUBRID)
            ctx.statement().getMoreResults(Statement.CLOSE_ALL_RESULTS);

        // [#6413] For consistency reasons, any exceptions that have been placed in ResultOrRow elements must
        //         be linked, just as if they were collected using ThrowExceptions == THROW_ALL
        if (ctx.settings().getThrowExceptions() == THROW_NONE) {
            SQLException s1 = null;

            for (ResultOrRows r : results.resultsOrRows()) {
                DataAccessException d = r.exception();

                if (d != null && d.getCause() instanceof SQLException) {
                    SQLException s2 = (SQLException) d.getCause();

                    if (s1 != null)
                        s1.setNextException(s2);

                    s1 = s2;
                }
            }
        }
    }

    private static final Pattern NEW_LINES = Pattern.compile("[\\r\\n]+");

    static final List<String[]> parseTXT(String string, String nullLiteral) {
        String[] strings = NEW_LINES.split(string);

        if (strings.length < 2)
            throw new DataAccessException("String must contain at least two lines");


        // [#2235] Distinguish between jOOQ's Result.format() and others
        boolean formattedJOOQ = (string.charAt(0) == '+');

        // [#6832] Distinguish between Oracle's format and others
        boolean formattedOracle = (string.charAt(0) == '-');

        // In jOOQ's Result.format(), that's line number one:
        // 1: +----+------+----+
        // 2: |ABC |XYZ   |HEHE|
        // 3: +----+------+----+
        // 4: |Data|{null}|1234|
        // 5: +----+------+----+
        //    012345678901234567
        // resulting in
        // [{1,5} {6,12} {13,17}]
        if (formattedJOOQ)
            return parseTXTLines(nullLiteral, strings, PLUS_PATTERN, 0, 1, 3, strings.length - 1);

        // In Oracle's format (e.g. when coming out of DBMS_XPLAN), that's line number one:
        // 1: ------------------
        // 2: |ABC |XYZ   |HEHE|
        // 3: ------------------
        // 4: |Data|{null}|1234|
        // 5: ------------------
        //    012345678901234567
        // resulting in
        // [{1,5} {6,12} {13,17}]
        else if (formattedOracle)
            return parseTXTLines(nullLiteral, strings, PIPE_PATTERN, 1, 1, 3, strings.length - 1);

        // In H2 format, that's line number two:
        // 1: ABC    XYZ     HEHE
        // 2: -----  ------- ----
        // 3: Data   {null}  1234
        //    0123456789012345678
        // resulting in
        // [{0,5} {7,14} {15,19}]
        else
            return parseTXTLines(nullLiteral, strings, DASH_PATTERN, 1, 0, 2, strings.length);
    }

    private static final List<String[]> parseTXTLines(
            String nullLiteral,
            String[] strings,
            Pattern pattern,
            int matchLine,
            int headerLine,
            int dataLineStart,
            int dataLineEnd) {

        List<int[]> positions = new ArrayList<>();
        Matcher m = pattern.matcher(strings[matchLine]);

        while (m.find()) {
            positions.add(new int[] { m.start(1), m.end(1) });
        }

        // Parse header line and data lines into string arrays
        List<String[]> result = new ArrayList<>();
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
        List<String[]> result = new ArrayList<>();

        Matcher mRow = P_PARSE_HTML_ROW.matcher(string);
        while (mRow.find()) {
            String row = mRow.group(1);
            List<String> col = new ArrayList<>();

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
                    result.add(fieldNameStrings(col.size()));
            }

            result.add(col.toArray(EMPTY_STRING));
        }

        return result;
    }

    /**
     * Generate the <code>BEGIN</code> part of an anonymous procedural block.
     */
    static final void begin(Context<?> ctx) {
        switch (ctx.family()) {









            case FIREBIRD: {
                ctx.visit(K_EXECUTE_BLOCK).formatSeparator()
                   .visit(K_AS).formatSeparator()
                   .visit(K_BEGIN).formatIndentStart().formatSeparator();
                break;
            }

            case MARIADB: {
                ctx.visit(K_BEGIN).sql(' ').visit(K_NOT).sql(' ').visit(K_ATOMIC).formatIndentStart().formatSeparator();
                break;
            }




            case POSTGRES: {
                if (increment(ctx.data(), DATA_BLOCK_NESTING))
                    ctx.visit(K_DO).sql(" $$").formatSeparator();

                ctx.visit(K_BEGIN).formatIndentStart().formatSeparator();
                break;
            }
        }
    }

    /**
     * Generate the <code>END</code> part of an anonymous procedural block.
     */
    static final void end(Context<?> ctx) {
        switch (ctx.family()) {









            case FIREBIRD:
            case MARIADB: {
                ctx.formatIndentEnd().formatSeparator()
                   .visit(K_END);
                break;
            }



            case POSTGRES: {
                ctx.formatIndentEnd().formatSeparator()
                   .visit(K_END);

                if (decrement(ctx.data(), DATA_BLOCK_NESTING))
                    ctx.sql(" $$");

                break;
            }
        }
    }

    /**
     * Wrap a statement in an <code>EXECUTE IMMEDIATE</code> statement.
     */
    static final void beginExecuteImmediate(Context<?> ctx) {
        switch (ctx.family()) {








            case FIREBIRD: {
                ctx.visit(K_EXECUTE_STATEMENT).sql(" '").stringLiteral(true).formatIndentStart().formatSeparator();
                break;
            }
        }
    }

    /**
     * Wrap a statement in an <code>EXECUTE IMMEDIATE</code> statement.
     */
    static final void endExecuteImmediate(Context<?> ctx) {
        ctx.formatIndentEnd().formatSeparator().stringLiteral(false).sql("';");
    }

    /**
     * Wrap a <code>DROP .. IF EXISTS</code> statement with
     * <code>BEGIN EXECUTE IMMEDIATE '...' EXCEPTION WHEN ... END;</code>, if
     * <code>IF EXISTS</code> is not supported.
     */
    static final void beginTryCatch(Context<?> ctx, DDLStatementType type) {
        beginTryCatch(ctx, type, null, null);
    }

    static final void beginTryCatch(Context<?> ctx, DDLStatementType type, Boolean container, Boolean element) {
        switch (ctx.family()) {








































































            case FIREBIRD: {
                begin(ctx);
                beginExecuteImmediate(ctx);
                break;
            }

            case MARIADB: {
                List<String> sqlstates = new ArrayList<>();

//                if (type == CREATE_SCHEMA)
//                    sqlstates.add("42710");
//                else if (type == CREATE_SEQUENCE)
//                    sqlstates.add("42710");
//                else if (type == CREATE_VIEW)
//                    sqlstates.add("42710");
//                else
//                    if (type == ALTER_TABLE) {
//                    if (TRUE.equals(container))
//                        sqlstates.add("42704");
//
//                    if (TRUE.equals(element))
//                        sqlstates.add("42703");
//                    else if (FALSE.equals(element))
//                        sqlstates.add("42711");
//                }
//                else
                    sqlstates.add("42S02");

                begin(ctx);
                for (String sqlstate : sqlstates)
                    ctx.visit(keyword("declare continue handler for sqlstate")).sql(' ').visit(DSL.inline(sqlstate)).sql(' ').visit(K_BEGIN).sql(' ').visit(K_END).sql(';').formatSeparator();

                break;
            }




            case POSTGRES: {
                begin(ctx);
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
    static final void endTryCatch(Context<?> ctx, DDLStatementType type) {
        endTryCatch(ctx, type, null, null);
    }

    static final void endTryCatch(Context<?> ctx, DDLStatementType type, Boolean container, Boolean element) {
        switch (ctx.family()) {











































































































































            case FIREBIRD: {
                endExecuteImmediate(ctx);
                ctx.formatSeparator()
                   .visit(K_WHEN).sql(" sqlcode -607 ").visit(K_DO).formatIndentStart().formatSeparator()
                   .visit(K_BEGIN).sql(' ').visit(K_END).formatIndentEnd();
                end(ctx);
                break;
            }

            case MARIADB: {
                ctx.sql(';');
                end(ctx);
                break;
            }




            case POSTGRES: {
                ctx.sql(';').formatIndentEnd().formatSeparator()
                   .visit(K_EXCEPTION).formatIndentStart().formatSeparator()
                   .visit(K_WHEN).sql(' ').visit(K_SQLSTATE).sql(" '42P07' ").visit(K_THEN).sql(' ').visit(K_NULL).sql(';').formatIndentEnd();
                end(ctx);
                break;
            }

            default:
                break;
        }
    }

    static final void toSQLDDLTypeDeclarationForAddition(Context<?> ctx, DataType<?> type) {
        toSQLDDLTypeDeclaration(ctx, type);
        toSQLDDLTypeDeclarationIdentityBeforeNull(ctx, type);

        // [#5356] Some dialects require the DEFAULT clause prior to the
        //         NULL constraints clause
        if (DEFAULT_BEFORE_NULL.contains(ctx.family()))
            toSQLDDLTypeDeclarationDefault(ctx, type);

        if (!type.nullable())
            ctx.sql(' ').visit(K_NOT_NULL);

            // Some databases default to NOT NULL, so explicitly setting columns to NULL is mostly required here
            // [#3400] [#4321] [#7392] ... but not in Derby, Firebird, HSQLDB
        else if (!NO_SUPPORT_NULL.contains(ctx.family()))
            ctx.sql(' ').visit(K_NULL);

        if (!DEFAULT_BEFORE_NULL.contains(ctx.family()))
            toSQLDDLTypeDeclarationDefault(ctx, type);

        toSQLDDLTypeDeclarationIdentityAfterNull(ctx, type);
    }

    /**
     * If a type is an identity type, some dialects require the relevant
     * keywords before the [ NOT ] NULL constraint.
     */
    static final void toSQLDDLTypeDeclarationIdentityBeforeNull(Context<?> ctx, DataType<?> type) {
        if (type.identity()) {
            switch (ctx.family()) {









                case CUBRID:    ctx.sql(' ').visit(K_AUTO_INCREMENT); break;
                case DERBY:     ctx.sql(' ').visit(K_GENERATED_BY_DEFAULT_AS_IDENTITY); break;
                case HSQLDB:    ctx.sql(' ').visit(K_GENERATED_BY_DEFAULT_AS_IDENTITY).sql('(').visit(K_START_WITH).sql(" 1)"); break;
                case SQLITE:    ctx.sql(' ').visit(K_PRIMARY_KEY).sql(' ').visit(K_AUTOINCREMENT); break;
            }
        }
    }

    /**
     * If a type is an identity type, some dialects require the relevant
     * keywords after the [ NOT ] NULL constraint.
     */
    static final void toSQLDDLTypeDeclarationIdentityAfterNull(Context<?> ctx, DataType<?> type) {
        if (type.identity()) {

            // [#5062] H2's (and others') AUTO_INCREMENT flag is syntactically located *after* NULL flags.
            switch (ctx.family()) {







                case H2:
                case MARIADB:
                case MYSQL:  ctx.sql(' ').visit(K_AUTO_INCREMENT); break;
            }
        }
    }

    private static final void toSQLDDLTypeDeclarationDefault(Context<?> ctx, DataType<?> type) {
        if (type.defaulted())
            ctx.sql(' ').visit(K_DEFAULT).sql(' ').visit(type.defaultValue());
    }

    static final void toSQLDDLTypeDeclaration(Context<?> ctx, DataType<?> type) {
        DataType<?> elementType = (type instanceof ArrayDataType)
            ? ((ArrayDataType<?>) type).elementType
            : type;

        // In some databases, identity is a type, not a flag.
        if (type.identity()) {
            switch (ctx.family()) {






                case POSTGRES: ctx.visit(type.getType() == Long.class ? K_SERIAL8 : K_SERIAL); return;
            }
        }

        // [#5299] MySQL enum types
        if (EnumType.class.isAssignableFrom(type.getType())) {

            @SuppressWarnings("unchecked")
            DataType<EnumType> enumType = (DataType<EnumType>) type;

            switch (ctx.family()) {





                case H2:
                case MARIADB:
                case MYSQL: {
                    ctx.visit(K_ENUM).sql('(');

                    String separator = "";
                    for (Object e : enumConstants(enumType)) {
                        ctx.sql(separator).visit(DSL.inline(((EnumType) e).getLiteral()));
                        separator = ", ";
                    }

                    ctx.sql(')');
                    return;
                }

                // [#7597] In PostgreSQL, the enum type reference should be used



                case POSTGRES:
                    break;

                default: {
                    type = emulateEnumType(enumType, enumConstants(enumType));
                    break;
                }
            }
        }

        // [#5807] These databases cannot use the DataType.getCastTypeName() (which is simply char in this case)
        if (type.getType() == UUID.class && NO_SUPPORT_CAST_TYPE_IN_DDL.contains(ctx.family())) {
            toSQLDDLTypeDeclaration(ctx, VARCHAR(36));
            return;
        }

        String typeName = type.getTypeName(ctx.configuration());

        // [#8070] Make sure VARCHAR(n) ARRAY types are generated as such in HSQLDB
        if (type.hasLength() || elementType.hasLength()) {

            // [#6289] [#7191] Some databases don't support lengths on binary types
            if (type.isBinary() && NO_SUPPORT_BINARY_TYPE_LENGTH.contains(ctx.family()))
                ctx.sql(typeName);
            else if (type.length() > 0)
                ctx.sql(typeName).sql('(').sql(type.length()).sql(')');

            // [#6745] The DataType.getCastTypeName() cannot be used in some dialects, for DDL
            else if (NO_SUPPORT_CAST_TYPE_IN_DDL.contains(ctx.family()))
                ctx.sql(SQLDataType.CLOB.getTypeName(ctx.configuration()));

            // Some databases don't allow for length-less VARCHAR, VARBINARY types
            else {
                String castTypeName = type.getCastTypeName(ctx.configuration());

                if (!typeName.equals(castTypeName))
                    ctx.sql(castTypeName);
                else
                    ctx.sql(typeName);
            }
        }
        else if (type.hasPrecision() && type.precision() > 0) {
            if (type.hasScale())
                ctx.sql(typeName).sql('(').sql(type.precision()).sql(", ").sql(type.scale()).sql(')');
            else
                ctx.sql(typeName).sql('(').sql(type.precision()).sql(')');
        }

        // [#6841] SQLite usually recognises int/integer as both meaning the same thing, but not in the
        //         context of an autoincrement column, in case of which explicit "integer" types are required.
        else if (type.identity() && ctx.family() == SQLITE && type.isNumeric()) {
            ctx.sql("integer");
        }
        else {
            ctx.sql(typeName);
        }

        // [#8041] Character sets are vendor-specific storage clauses, which we might need to ignore
        if (type.characterSet() != null && ctx.configuration().data("org.jooq.meta.extensions.ddl.ignore-storage-clauses") == null)
            ctx.sql(' ').visit(K_CHARACTER_SET).sql(' ').visit(type.characterSet());

        // [#8011] Collations are vendor-specific storage clauses, which we might need to ignore
        if (type.collation() != null && ctx.configuration().data("org.jooq.meta.extensions.ddl.ignore-storage-clauses") == null)
            ctx.sql(' ').visit(K_COLLATE).sql(' ').visit(type.collation());
    }

    private static Object[] enumConstants(DataType<? extends EnumType> type) {
        Object[] enums = type.getType().getEnumConstants();

        if (enums == null)
            throw new DataTypeException("EnumType must be a Java enum");

        return enums;
    }

    static final DataType<String> emulateEnumType(DataType<? extends EnumType> type) {
        return emulateEnumType(type, enumConstants(type));
    }

    static final DataType<String> emulateEnumType(DataType<? extends EnumType> type, Object[] enums) {
        int length = 0;
        for (Object e : enums)
            if (((EnumType) e).getLiteral() != null)
                length = Math.max(length, ((EnumType) e).getLiteral().length());

        return VARCHAR(length).nullability(type.nullability()).defaultValue((Field) type.defaultValue());
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


    static String[] enumLiterals(Class<? extends EnumType> type) {
        EnumType[] values = enums(type);
        String[] result = new String[values.length];

        for (int i = 0; i < values.length; i++)
            result[i] = values[i].getLiteral();

        return result;
    }

    static <E extends EnumType> E[] enums(Class<? extends E> type) {

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
                return (E[]) companionClass.getMethod("values").invoke(companion);
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

        Set<String> names = new HashSet<>();
        for (Field<?> field : fields)
            if (!names.add(field.getName()))
                return true;

        return false;
    }

    @SuppressWarnings("serial")
    static final QueryPartList<SelectFieldOrAsterisk> qualify(final Table<?> table, SelectFieldList<SelectFieldOrAsterisk> fields) {
        QueryPartList<SelectFieldOrAsterisk> result = new QueryPartList<SelectFieldOrAsterisk>() {
            @Override
            protected void toSQLEmptyList(Context<?> context) {
                table.asterisk();
            }

            @Override
            public boolean declaresFields() {
                return true;
            }
        };

        for (SelectFieldOrAsterisk field : fields)
            result.add(qualify(table, field));

        return result;
    }

    static final SelectFieldOrAsterisk qualify(Table<?> table, SelectFieldOrAsterisk field) {
        if (field instanceof Field)
            return qualify(table, (Field<?>) field);
        else if (field instanceof Asterisk)
            return table.asterisk();
        else if (field instanceof QualifiedAsterisk)
            return table.asterisk();
        else
            throw new IllegalArgumentException("Unsupported field : " + field);
    }

    static final <T> Field<T> qualify(Table<?> table, Field<T> field) {
        Field<T> result = table.field(field);

        if (result != null)
            return result;

        Name[] part = table.getQualifiedName().parts();
        Name[] name = new Name[part.length + 1];
        System.arraycopy(part, 0, name, 0, part.length);
        name[part.length] = field.getUnqualifiedName();

        return DSL.field(DSL.name(name), field.getDataType());
    }

    static final <R extends Record> Table<R> aliased(Table<R> table) {
        if (table instanceof TableImpl)
            return ((TableImpl<R>) table).getAliasedTable();
        else if (table instanceof TableAlias)
            return ((TableAlias<R>) table).getAliasedTable();
        else
            return null;
    }

    static final <R extends Record> Alias<Table<R>> alias(Table<R> table) {
        if (table instanceof TableImpl)
            return ((TableImpl<R>) table).alias;
        else if (table instanceof TableAlias)
            return ((TableAlias<R>) table).alias;
        else
            return null;
    }

    /**
     * Whether a counter is currently at the top level or not.
     *
     * @see #increment(Map, DataKey)
     * @see #decrement(Map, DataKey)
     */
    static final boolean toplevel(Map<Object, Object> data, DataKey key) {
        Integer updateCounts = (Integer) data.get(key);

        if (updateCounts == null)
            throw new IllegalStateException();
        else
            return updateCounts == 1;
    }

    /**
     * Increment a counter and return true if the counter was zero prior to
     * incrementing.
     */
    static final boolean increment(Map<Object, Object> data, DataKey key) {
        boolean result = true;
        Integer updateCounts = (Integer) data.get(key);

        if (updateCounts == null)
            updateCounts = 0;
        else
            result = false;

        data.put(key, updateCounts + 1);
        return result;
    }

    /**
     * Decrement a counter and return true if the counter is zero after
     * decrementing.
     */
    static final boolean decrement(Map<Object, Object> data, DataKey key) {
        boolean result = false;
        Integer updateCounts = (Integer) data.get(key);

        if (updateCounts == null || updateCounts == 0)
            throw new IllegalStateException("Unmatching increment / decrement on key: " + key);
        else if (updateCounts == 1)
            result = true;

        data.put(key, updateCounts - 1);
        return result;
    }

    static Field<?> tableField(Table<?> table, Object field) {
        if (field instanceof Field<?>)
            return (Field<?>) field;
        else if (field instanceof Name)
            return table.field((Name) field);
        else if (field instanceof String)
            return table.field((String) field);
        else
            throw new IllegalArgumentException("Field type not supported: " + field);
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
        char[] hex = HEX_DIGITS;
        for (int i = 0; i < len; i++) {
            int c = value[i] & 0xff;
            buff[i + i] = hex[c >> 4];
            buff[i + i + 1] = hex[c & 0xf];
        }
        return new String(buff);
    }

    /**
     * Convert a byte array to a hex encoded string.
     *
     * @param value the byte array
     * @return the hex encoded string
     */
    static final String convertBytesToHex(byte[] value) {
        return convertBytesToHex(value, value.length);
    }

    static final boolean isNotEmpty(Collection<?> collection) {
        return collection != null && !collection.isEmpty();
    }

    static final boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    static final boolean isNotEmpty(Object[] array) {
        return array != null && array.length > 0;
    }

    static final boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    static final boolean isEmbeddable(Field<?> field) {
        return field instanceof EmbeddableTableField
            || field instanceof Val && ((Val<?>) field).value instanceof EmbeddableRecord;
    }

    @SuppressWarnings("unchecked")
    static final Field<?>[] embeddedFields(Field<?> field) {
        return field instanceof EmbeddableTableField
             ? ((EmbeddableTableField<?, ?>) field).fields
             : field instanceof Val && ((Val<?>) field).value instanceof EmbeddableRecord
             ? ((EmbeddableRecord<?>) ((Val<?>) field).value).valuesRow().fields()

             // It's an embeddable type, but it is null
             : field instanceof Val && EmbeddableRecord.class.isAssignableFrom(field.getType())
             ? newInstance((Class<? extends EmbeddableRecord<?>>) field.getType()).valuesRow().fields()
             : null;
    }

    private static final EmbeddableRecord<?> newInstance(Class<? extends EmbeddableRecord<?>> type) {
        try {
            return type.getConstructor().newInstance();
        }
        catch (Exception e) {
            throw new MappingException("Cannot create EmbeddableRecord type", e);
        }
    }

    /**
     * Flatten out an {@link EmbeddableTableField}.
     */
    static final <E extends Field<?>> Iterable<E> flatten(final E field) {
        return new Iterable<E>() {
            @Override
            public Iterator<E> iterator() {
                Iterator<E> it = singletonList(field).iterator();

                if (field instanceof EmbeddableTableField)
                    return new FlatteningIterator<E>(it) {
                        @SuppressWarnings("unchecked")
                        @Override
                        List<E> flatten(E e) {
                            return (List<E>) Arrays.asList(((EmbeddableTableField<?, ?>) e).fields);
                        }
                    };
                else
                    return it;
            }
        };
    }

    /**
     * Flatten out {@link EmbeddableTableField} elements contained in an
     * ordinary iterable.
     */
    static final <E extends Field<?>> Iterable<E> flattenCollection(final Iterable<E> iterable) {
        return new Iterable<E>() {
            @Override
            public Iterator<E> iterator() {
                return new FlatteningIterator<E>(iterable.iterator()) {
                    @SuppressWarnings("unchecked")
                    @Override
                    List<E> flatten(E e) {
                        if (e instanceof EmbeddableTableField)
                            return (List<E>) Arrays.asList(((EmbeddableTableField<?, ?>) e).fields);

                        return null;
                    }
                };
            }
        };
    }

    /**
     * Flatten out {@link EmbeddableTableField} elements contained in an
     * entry set iterable.
     */
    static final <E extends Entry<Field<?>, Field<?>>> Iterable<E> flattenEntrySet(final Iterable<E> iterable) {
        return new Iterable<E>() {
            @Override
            public Iterator<E> iterator() {
                return new FlatteningIterator<E>(iterable.iterator()) {
                    @SuppressWarnings("unchecked")
                    @Override
                    List<E> flatten(E e) {
                        if (e.getKey() instanceof EmbeddableTableField) {
                            List<E> result = new ArrayList<>();
                            Field<?>[] keys = embeddedFields(e.getKey());
                            Field<?>[] values = embeddedFields(e.getValue());

                            for (int i = 0; i < keys.length; i++)
                                result.add((E) new SimpleImmutableEntry<Field<?>, Field<?>>(
                                    keys[i], values[i]
                                ));

                            return result;
                        }

                        return null;
                    }
                };
            }
        };
    }

    /**
     * A base implementation for {@link EmbeddableTableField} flattening
     * iterators with a default implementation for {@link Iterator#remove()} for
     * convenience in the Java 6 build.
     */
    static abstract class FlatteningIterator<E> implements Iterator<E> {
        private final Iterator<E> delegate;
        private Iterator<E> flatten;
        private E next;

        FlatteningIterator(Iterator<E> delegate) {
            this.delegate = delegate;
        }

        abstract List<E> flatten(E e);

        private final void move() {
            if (next == null) {
                if (flatten != null) {
                    if (flatten.hasNext()) {
                        next = flatten.next();
                        return;
                    }
                    else {
                        flatten = null;
                    }
                }

                if (delegate.hasNext()) {
                    next = delegate.next();

                    List<E> flattened = flatten(next);
                    if (flattened == null)
                        return;

                    next = null;
                    flatten = flattened.iterator();
                    move();
                    return;
                }
            }
        }

        @Override
        public final boolean hasNext() {
            move();
            return next != null;
        }

        @Override
        public final E next() {
            move();

            if (next == null)
                throw new NoSuchElementException();

            E result = next;
            next = null;
            return result;
        }

        @Override
        public final void remove() {
            throw new UnsupportedOperationException("remove");
        }
    }
}
