/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
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
 * For more information, please visit: https://www.jooq.org/legal/licensing
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
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.nCopies;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.joining;
import static org.jooq.ContextConverter.scoped;
// ...
// ...
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.DUCKDB;
// ...
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
// ...
// ...
import static org.jooq.SQLDialect.HSQLDB;
// ...
// ...
// ...
import static org.jooq.SQLDialect.MARIADB;
// ...
import static org.jooq.SQLDialect.MYSQL;
// ...
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.TRINO;
import static org.jooq.SQLDialect.YUGABYTEDB;
import static org.jooq.conf.BackslashEscaping.DEFAULT;
import static org.jooq.conf.BackslashEscaping.ON;
import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.conf.ParamType.NAMED;
import static org.jooq.conf.ParamType.NAMED_OR_INLINED;
import static org.jooq.conf.RenderDefaultNullability.IMPLICIT_NULL;
import static org.jooq.conf.RenderQuotedNames.EXPLICIT_DEFAULT_QUOTED;
import static org.jooq.conf.SettingsTools.getBackslashEscaping;
import static org.jooq.conf.SettingsTools.updatablePrimaryKeys;
import static org.jooq.conf.ThrowExceptions.THROW_FIRST;
import static org.jooq.conf.ThrowExceptions.THROW_NONE;
import static org.jooq.exception.DataAccessException.sqlStateClass;
import static org.jooq.impl.CacheType.REFLECTION_CACHE_GET_ANNOTATED_GETTER;
import static org.jooq.impl.CacheType.REFLECTION_CACHE_GET_ANNOTATED_MEMBERS;
import static org.jooq.impl.CacheType.REFLECTION_CACHE_GET_ANNOTATED_SETTERS;
import static org.jooq.impl.CacheType.REFLECTION_CACHE_GET_MATCHING_GETTER;
import static org.jooq.impl.CacheType.REFLECTION_CACHE_GET_MATCHING_MEMBERS;
import static org.jooq.impl.CacheType.REFLECTION_CACHE_GET_MATCHING_SETTERS;
import static org.jooq.impl.CacheType.REFLECTION_CACHE_HAS_COLUMN_ANNOTATIONS;
import static org.jooq.impl.Convert.convert;
import static org.jooq.impl.DDLStatementType.ALTER_SCHEMA;
import static org.jooq.impl.DDLStatementType.ALTER_TABLE;
import static org.jooq.impl.DDLStatementType.ALTER_VIEW;
import static org.jooq.impl.DDLStatementType.CREATE_DATABASE;
import static org.jooq.impl.DDLStatementType.CREATE_DOMAIN;
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
import static org.jooq.impl.DSL.all;
import static org.jooq.impl.DSL.any;
import static org.jooq.impl.DSL.asterisk;
import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.escape;
import static org.jooq.impl.DSL.getDataType;
import static org.jooq.impl.DSL.keyword;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.noCondition;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.unquotedName;
import static org.jooq.impl.DSL.val;
import static org.jooq.impl.DefaultExecuteContext.localConnection;
import static org.jooq.impl.DefaultParseContext.SUPPORTS_HASH_COMMENT_SYNTAX;
import static org.jooq.impl.DerivedTable.NO_SUPPORT_CORRELATED_DERIVED_TABLE;
import static org.jooq.impl.Identifiers.QUOTES;
import static org.jooq.impl.Identifiers.QUOTE_END_DELIMITER;
import static org.jooq.impl.Identifiers.QUOTE_END_DELIMITER_ESCAPED;
import static org.jooq.impl.Identifiers.QUOTE_START_DELIMITER;
import static org.jooq.impl.Keywords.K_ALWAYS;
import static org.jooq.impl.Keywords.K_AS;
import static org.jooq.impl.Keywords.K_ATOMIC;
import static org.jooq.impl.Keywords.K_AUTOINCREMENT;
import static org.jooq.impl.Keywords.K_AUTO_INCREMENT;
import static org.jooq.impl.Keywords.K_BEGIN;
import static org.jooq.impl.Keywords.K_BEGIN_CATCH;
import static org.jooq.impl.Keywords.K_BEGIN_TRY;
import static org.jooq.impl.Keywords.K_BY;
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
import static org.jooq.impl.Keywords.K_ERROR;
import static org.jooq.impl.Keywords.K_EXCEPTION;
import static org.jooq.impl.Keywords.K_EXEC;
import static org.jooq.impl.Keywords.K_EXECUTE_BLOCK;
import static org.jooq.impl.Keywords.K_EXECUTE_IMMEDIATE;
import static org.jooq.impl.Keywords.K_EXECUTE_STATEMENT;
import static org.jooq.impl.Keywords.K_GENERATED;
import static org.jooq.impl.Keywords.K_IDENTITY;
import static org.jooq.impl.Keywords.K_IF;
import static org.jooq.impl.Keywords.K_INT;
import static org.jooq.impl.Keywords.K_LIKE;
import static org.jooq.impl.Keywords.K_NOT;
import static org.jooq.impl.Keywords.K_NOT_NULL;
import static org.jooq.impl.Keywords.K_NULL;
import static org.jooq.impl.Keywords.K_NVARCHAR;
import static org.jooq.impl.Keywords.K_PERSISTED;
import static org.jooq.impl.Keywords.K_PRIMARY_KEY;
import static org.jooq.impl.Keywords.K_RAISE;
import static org.jooq.impl.Keywords.K_RAISERROR;
import static org.jooq.impl.Keywords.K_SERIAL;
import static org.jooq.impl.Keywords.K_SERIAL4;
import static org.jooq.impl.Keywords.K_SERIAL8;
import static org.jooq.impl.Keywords.K_SQLSTATE;
import static org.jooq.impl.Keywords.K_START_WITH;
import static org.jooq.impl.Keywords.K_STORED;
import static org.jooq.impl.Keywords.K_THEN;
import static org.jooq.impl.Keywords.K_THROW;
import static org.jooq.impl.Keywords.K_VIRTUAL;
import static org.jooq.impl.Keywords.K_WHEN;
import static org.jooq.impl.QOM.GenerationOption.STORED;
import static org.jooq.impl.QOM.GenerationOption.VIRTUAL;
import static org.jooq.impl.SQLDataType.BLOB;
import static org.jooq.impl.SQLDataType.CLOB;
import static org.jooq.impl.SQLDataType.INTEGER;
import static org.jooq.impl.SQLDataType.JSON;
import static org.jooq.impl.SQLDataType.JSONB;
import static org.jooq.impl.SQLDataType.OTHER;
import static org.jooq.impl.SQLDataType.SMALLINT;
import static org.jooq.impl.SQLDataType.VARCHAR;
import static org.jooq.impl.SQLDataType.XML;
import static org.jooq.impl.SubqueryCharacteristics.DERIVED_TABLE;
import static org.jooq.impl.SubqueryCharacteristics.PREDICAND;
import static org.jooq.impl.SubqueryCharacteristics.SET_OPERATION;
import static org.jooq.impl.Tools.executeImmediate;
import static org.jooq.impl.Tools.SimpleDataKey.DATA_BLOCK_NESTING;
import static org.jooq.tools.StringUtils.defaultIfNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
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
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinPool.ManagedBlocker;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

// ...
// ...
// ...
import org.jooq.Asterisk;
import org.jooq.Attachable;
import org.jooq.BindContext;
import org.jooq.Catalog;
import org.jooq.Check;
import org.jooq.Clause;
import org.jooq.CommonTableExpression;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.ContextConverter;
import org.jooq.Converter;
import org.jooq.ConverterContext;
import org.jooq.ConverterProvider;
import org.jooq.Converters;
import org.jooq.Cursor;
import org.jooq.DSLContext;
import org.jooq.DataType;
import org.jooq.EmbeddableRecord;
import org.jooq.EnumType;
import org.jooq.ExecuteContext;
import org.jooq.ExecuteListener;
import org.jooq.Field;
import org.jooq.FieldOrRow;
import org.jooq.FieldOrRowOrSelect;
import org.jooq.Fields;
import org.jooq.ForeignKey;
import org.jooq.Function1;
import org.jooq.Function2;
import org.jooq.Function3;
import org.jooq.Generator;
import org.jooq.JSON;
import org.jooq.JSONB;
import org.jooq.JSONEntry;
import org.jooq.JoinType;
import org.jooq.Name;
import org.jooq.OrderField;
import org.jooq.Param;
// ...
import org.jooq.QualifiedAsterisk;
import org.jooq.QualifiedRecord;
import org.jooq.QuantifiedSelect;
import org.jooq.Query;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.RecordQualifier;
import org.jooq.RenderContext;
import org.jooq.RenderContext.CastMode;
import org.jooq.Result;
import org.jooq.ResultOrRows;
import org.jooq.ResultQuery;
import org.jooq.Results;
import org.jooq.Row;
import org.jooq.SQLDialect;
import org.jooq.Schema;
import org.jooq.Scope;
import org.jooq.Select;
import org.jooq.SelectFieldOrAsterisk;
import org.jooq.SortField;
import org.jooq.Source;
import org.jooq.Table;
import org.jooq.TableElement;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.UDT;
import org.jooq.UpdatableRecord;
import org.jooq.WindowSpecification;
import org.jooq.XML;
import org.jooq.conf.BackslashEscaping;
import org.jooq.conf.NestedCollectionEmulation;
import org.jooq.conf.ParamType;
import org.jooq.conf.ParseNameCase;
import org.jooq.conf.RenderDefaultNullability;
import org.jooq.conf.RenderMapping;
import org.jooq.conf.RenderQuotedNames;
import org.jooq.conf.Settings;
import org.jooq.conf.SettingsTools;
import org.jooq.conf.ThrowExceptions;
import org.jooq.exception.DataAccessException;
import org.jooq.exception.DataException;
import org.jooq.exception.DataTypeException;
import org.jooq.exception.DetachedException;
import org.jooq.exception.ExceptionTools;
import org.jooq.exception.IntegrityConstraintViolationException;
import org.jooq.exception.MappingException;
import org.jooq.exception.NoDataFoundException;
import org.jooq.exception.SQLStateClass;
import org.jooq.exception.TemplatingException;
import org.jooq.exception.TooManyRowsException;
import org.jooq.impl.QOM.Quantifier;
import org.jooq.impl.QOM.UEmpty;
import org.jooq.impl.ResultsImpl.ResultOrRowsImpl;
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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import io.r2dbc.spi.R2dbcException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

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

    static final byte[]                     EMPTY_BYTE                    = {};
    static final Catalog[]                  EMPTY_CATALOG                 = {};
    static final Check<?>[]                 EMPTY_CHECK                   = {};
    static final Clause[]                   EMPTY_CLAUSE                  = {};
    static final Collection<?>[]            EMPTY_COLLECTION              = {};
    static final CommonTableExpression<?>[] EMPTY_COMMON_TABLE_EXPRESSION = {};
    static final ExecuteListener[]          EMPTY_EXECUTE_LISTENER        = {};
    static final Field<?>[]                 EMPTY_FIELD                   = {};
    static final FieldOrRow[]               EMPTY_FIELD_OR_ROW            = {};
    static final int[]                      EMPTY_INT                     = {};
    static final JSONEntry<?>[]             EMPTY_JSONENTRY               = {};
    static final Name[]                     EMPTY_NAME                    = {};
    static final Object[]                   EMPTY_OBJECT                  = {};
    static final Param<?>[]                 EMPTY_PARAM                   = {};
    static final OrderField<?>[]            EMPTY_ORDERFIELD              = {};
    static final Query[]                    EMPTY_QUERY                   = {};
    static final QueryPart[]                EMPTY_QUERYPART               = {};
    static final Record[]                   EMPTY_RECORD                  = {};
    static final Row[]                      EMPTY_ROW                     = {};
    static final Schema[]                   EMTPY_SCHEMA                  = {};
    static final SortField<?>[]             EMPTY_SORTFIELD               = {};
    static final Source[]                   EMPTY_SOURCE                  = {};
    static final String[]                   EMPTY_STRING                  = {};
    static final Table<?>[]                 EMPTY_TABLE                   = {};
    static final TableField<?, ?>[]         EMPTY_TABLE_FIELD             = {};
    static final TableRecord<?>[]           EMPTY_TABLE_RECORD            = {};
    static final UpdatableRecord<?>[]       EMPTY_UPDATABLE_RECORD        = {};

    // ------------------------------------------------------------------------
    // Some constants for use with Context.data()
    // ------------------------------------------------------------------------

    static final class DataKeyScopeStackPart extends AbstractQueryPart implements UEmpty {

        static final DataKeyScopeStackPart INSTANCE = new DataKeyScopeStackPart();

        private DataKeyScopeStackPart() {}

        @Override
        public final void accept(Context<?> ctx) {}

        @Override
        public boolean equals(Object that) {
            return this == that;
        }

        @Override
        public int hashCode() {
            return 0;
        }
    }

    /**
     * A common super types for {@link BooleanDataKey}, {@link SimpleDataKey} and {@link ExtendedDataKey}
     */
    sealed interface DataKey {

        /**
         * Whether this data key resets itself to {@link #resetValue()} when
         * entering in a new scope of depth {@link #resetThreshold()}.
         */
        boolean resetInSubqueryScope();

        /**
         * The value to reset itself to.
         */
        Object resetValue();

        /**
         * The depth after which the key resets itself.
         */
        int resetThreshold();
    }

    /**
     * Keys for {@link Configuration#data()}, which may be referenced frequently
     * and represent a {@code boolean} value and are thus stored in an
     * {@link EnumSet} for speedier access.
     */
    enum BooleanDataKey implements DataKey {

        /**
         * [#13468] The WHERE clause in a SELECT is mandatory for the current
         * scope.
         */
        DATA_MANDATORY_WHERE_CLAUSE(true, null, 1),

        /**
         * [#1520] Count the number of bind values, and potentially enforce a
         * static statement.
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
         * [#1905] This constant is used internally by jOOQ to indicate to
         * subqueries that they're being rendered in the context of a row value
         * expression predicate.
         */
        DATA_ROW_VALUE_EXPRESSION_PREDICATE_SUBQUERY,















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
        DATA_OMIT_INTO_CLAUSE,

        /**
         * [#1658] Specify whether the trailing LIMIT clause needs to be rendered.
         */
        DATA_RENDER_TRAILING_LIMIT_IF_APPLICABLE,

        /**
         * [#13509] In some cases, it may be desirable to enforce appending a
         * <code>LIMIT</code> clause, when there's an <code>ORDER BY</code>
         * clause, e.g. to prevent the optimiser from removing the seemingly
         * unnecessary sort.
         */
        DATA_FORCE_LIMIT_WITH_ORDER_BY,

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
         * [#11486] An <code>INSERT … SELECT</code> statement.
         */
        DATA_INSERT_SELECT,

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
         * [#9925] In some cases the <code>AS</code> keyword is required for
         * aliasing, e.g. XML.
         */
        DATA_AS_REQUIRED(true, null, 0),

        /**
         * [#12030] MULTISET conditions need to render the MULTISET emulation
         * differently to implement MULTISET semantics (ORDER agnostic)
         */
        DATA_MULTISET_CONDITION,

        /**
         * [#12021] MULTISET content may need to be rendered differently (e.g.
         * nested <code>ROW</code> types).
         */
        DATA_MULTISET_CONTENT,

        /**
         * [#12072] In some cases, it's recommended to generate an explicit
         * <code>ELSE NULL</code> clause in a <code>CASE</code> expression.
         */
        DATA_FORCE_CASE_ELSE_NULL,

        /**
         * [#12092] Whether the @@group_concat_max_len value has already been
         * set.
         */
        DATA_GROUP_CONCAT_MAX_LEN_SET,

        /**
         * [#11543] Whether the @@innodb_lock_wait_timeout value has already
         * been set.
         */
        DATA_LOCK_WAIT_TIMEOUT_SET,

        /**
         * [#13573] We're parsing the <code>ON CONFLICT</code> clause, in which
         * the <code>VALUES()</code> function or <code>EXCLUDED</code> pseudo
         * table have a special semantics.
         */
        DATA_PARSE_ON_CONFLICT,

        /**
         * [#13808] We're in a store assignment context (e.g.
         * <code>UPDATE</code> or assignment statement).
         */
        DATA_STORE_ASSIGNMENT,

        /**
         * [#14985] We're in a context where implicit joins are being rendered,
         * not the query itself.
         */
        DATA_RENDER_IMPLICIT_JOIN,

        ;

        private final boolean resetInSubqueryScope;
        private final Object  resetValue;
        private final int     resetThreshold;

        private BooleanDataKey() {
            this(false, null, 0);
        }

        private BooleanDataKey(boolean resetInSubqueryScope, Object resetValue, int resetThreshold) {
            this.resetInSubqueryScope = resetInSubqueryScope;
            this.resetValue = resetValue;
            this.resetThreshold = resetThreshold;
        }

        @Override
        public final boolean resetInSubqueryScope() {
            return resetInSubqueryScope;
        }

        @Override
        public final Object resetValue() {
            return resetValue;
        }

        @Override
        public final int resetThreshold() {
            return resetThreshold;
        }
    }

    /**
     * Keys for {@link Configuration#data()}, which may be referenced frequently
     * and are thus stored in an {@link EnumMap} for speedier access.
     */
    enum SimpleDataKey implements DataKey {

        /**
         * The level of anonymous block nesting, in case we're generating a block.
         */
        DATA_BLOCK_NESTING,

        /**
         * [#2744] Currently rendering the data change delta table syntax.
         * <p>
         * In some dialects, a <code>FINAL TABLE (INSERT …)</code> clause exists, which
         * corresponds to the PostgreSQL <code>INSERT … RETURNING</code> clause.
         */
        DATA_RENDERING_DATA_CHANGE_DELTA_TABLE,

        /**
         * [#531] The local window definitions.
         * <p>
         * The window definitions declared in the <code>WINDOW</code> clause are
         * needed in the <code>SELECT</code> clause when emulating them by inlining
         * window specifications.
         */
        DATA_WINDOW_DEFINITIONS(true, null, 0),

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
        DATA_SELECT_INTO_TABLE,

        /**
         * [#1206] The collected Semi / Anti JOIN predicates.
         */
        DATA_COLLECTED_SEMI_ANTI_JOIN,

        /**
         * [#5764] Sometimes, it is necessary to prepend some SQL to the
         * generated SQL.
         * <p>
         * This needs to be done e.g. to emulate inline table valued parameters
         * in SQL Server:
         * <p>
         * <pre><code>
         * -- With TVP bind variable:
         * SELECT * FROM func (?)
         *
         * -- Inlining TVP bind variable:
         * DECLARE @t TABLE_TYPE;
         * INSERT INTO @t VALUES (?),(?),...,(?);
         * SELECT * FROM func (@t)
         * </code></pre>
         */
        DATA_PREPEND_SQL,

        /**
         * [#12092] Sometimes, it is necessary to append some SQL to the
         * generated SQL.
         * <p>
         * This needs to be done e.g. to make sure the
         * MySQL @@group_concat_max_len setting is set to an appropriate value,
         * and reset to the previous value again.
         * <p>
         * <pre><code>
         * SET @t = @@group_concat_max_len;
         * SET @@group_concat_max_len = 4294967295;
         * SELECT group_concat(...);
         * SET @@group_concat_max_len = @t;
         * </code></pre>
         */
        DATA_APPEND_SQL,




























        /**
         * [#6583] The target table on which a DML operation operates on.
         */
        DATA_DML_TARGET_TABLE,

        /**
         * [#6583] [#14742] The target table on which a DML operation operates on.
         */
        DATA_DML_USING_TABLES,

        /**
         * [#8479] There is a WHERE clause to be emulated for ON DUPLICATE KEY
         */
        DATA_ON_DUPLICATE_KEY_WHERE,

        /**
         * [#3607] [#8522] CTEs that need to be added to the top level CTE
         * section.
         */
        DATA_TOP_LEVEL_CTE,

        /**
         * [#10540] Aliases to be applied to the current <code>SELECT</code>
         * statement.
         */
        DATA_SELECT_ALIASES,

        ;

        private final boolean resetInSubqueryScope;
        private final Object  resetValue;
        private final int     resetThreshold;

        private SimpleDataKey() {
            this(false, null, 0);
        }

        private SimpleDataKey(boolean resetInSubqueryScope, Object resetValue, int resetThreshold) {
            this.resetInSubqueryScope = resetInSubqueryScope;
            this.resetValue = resetValue;
            this.resetThreshold = resetThreshold;
        }

        @Override
        public final boolean resetInSubqueryScope() {
            return resetInSubqueryScope;
        }

        @Override
        public final Object resetValue() {
            return resetValue;
        }

        @Override
        public final int resetThreshold() {
            return resetThreshold;
        }
    }

    /**
     * Keys for {@link Configuration#data()}, which may be referenced very
     * infrequently and are thus stored in an ordinary {@link HashMap} for a
     * more optimal memory layout.
     */
    enum ExtendedDataKey implements DataKey {

        /**
         * [#4498] [#7552] The original INSERT ON DUPLICATE KEY UPDATE query
         * that produced a MERGE statement for its emulation.
         */
        DATA_INSERT_ON_DUPLICATE_KEY_UPDATE,































































        /**
         * [#9017] We've already transformed ROWNUM expressions to LIMIT.
         */
        DATA_TRANSFORM_ROWNUM_TO_LIMIT,

        /**
         * [#1535] [#11851] The window function object that uses a
         * {@link WindowSpecification}.
         */
        DATA_WINDOW_FUNCTION,

        /**
         * [#8893] Whether {@link TableField} should be qualified with their
         * tables when rendering in the current scope.
         */
        DATA_RENDER_TABLE(true, null, 0),

        ;

        private final boolean resetInSubqueryScope;
        private final Object  resetValue;
        private final int     resetThreshold;

        private ExtendedDataKey() {
            this(false, null, 0);
        }

        private ExtendedDataKey(boolean resetInSubqueryScope, Object resetValue, int resetThreshold) {
            this.resetInSubqueryScope = resetInSubqueryScope;
            this.resetValue = resetValue;
            this.resetThreshold = resetThreshold;
        }

        @Override
        public final boolean resetInSubqueryScope() {
            return resetInSubqueryScope;
        }

        @Override
        public final Object resetValue() {
            return resetValue;
        }

        @Override
        public final int resetThreshold() {
            return resetThreshold;
        }
    }

    static final DataKey[] DATAKEY_RESET_IN_SUBQUERY_SCOPE;

    static {
        DATAKEY_RESET_IN_SUBQUERY_SCOPE = Stream
            .concat(
                Stream.concat(Stream.of(BooleanDataKey.values()), Stream.of(SimpleDataKey.values())),
                Stream.of(ExtendedDataKey.values()))
            .filter(t -> t.resetInSubqueryScope())
            .toArray(DataKey[]::new);
    }

    // ------------------------------------------------------------------------
    // Other constants
    // ------------------------------------------------------------------------

    /**
     * The default escape character for <code>[a] LIKE [b] ESCAPE […]</code>
     * clauses.
     */
    static final char                    ESCAPE                             = '!';

    /**
     * A lock for the initialisation of other static members
     */
    private static final Object          initLock                           = new Object();

    /**
     * Indicating whether JPA (<code>jakarta.persistence</code>) is on the
     * classpath.
     */
    private static volatile JPANamespace jpaNamespace;

    /**
     * Indicating whether Kotlin (<code>kotlin.*</code>) is on the classpath.
     */
    private static volatile Boolean      isKotlinAvailable;
    private static volatile Reflect      ktJvmClassMapping;
    private static volatile Reflect      ktKClasses;
    private static volatile Reflect      ktKClass;
    private static volatile Reflect      ktKTypeParameter;

    /**
     * [#3696] The maximum number of consumed exceptions in
     * {@link #consumeExceptions(Configuration, PreparedStatement, SQLException)}
     * helps prevent infinite loops and {@link OutOfMemoryError}.
     */
    static int                           maxConsumedExceptions              = 256;
    static int                           maxConsumedResults                 = 65536;

    /**
     * A pattern for the dash line syntax
     */
    private static final Pattern         DASH_PATTERN                       = Pattern.compile("(-+)");

    /**
     * A pattern for the pipe line syntax
     */
    private static final Pattern         PIPE_PATTERN                       = Pattern.compile("(?<=\\|)([^|]+)(?=\\|)");

    /**
     * A pattern for the dash line syntax
     */
    private static final Pattern         PLUS_PATTERN                       = Pattern.compile("\\+(-+)(?=\\+)");

    /**
     * All characters that are matched by Java's interpretation of \s.
     * <p>
     * For a more accurate set of whitespaces, refer to
     * http://stackoverflow.com/a/4731164/521799. In the event of SQL
     * processing, it is probably safe to ignore most of those alternative
     * Unicode whitespaces.
     */
    private static final char[]          WHITESPACE_CHARACTERS              = " \t\n\u000B\f\r".toCharArray();

    /**
     * Acceptable prefixes for JDBC escape syntax.
     */
    private static final char[][]        JDBC_ESCAPE_PREFIXES                = {
        "{fn ".toCharArray(),
        "{d ".toCharArray(),
        "{t ".toCharArray(),
        "{ts ".toCharArray()
    };

    private static final char[]          TOKEN_SINGLE_LINE_COMMENT          = { '-', '-' };
    private static final char[]          TOKEN_SINGLE_LINE_COMMENT_C        = { '/', '/' };
    private static final char[]          TOKEN_HASH                         = { '#' };
    private static final char[]          TOKEN_MULTI_LINE_COMMENT_OPEN      = { '/', '*' };
    private static final char[]          TOKEN_MULTI_LINE_COMMENT_CLOSE     = { '*', '/' };
    private static final char[]          TOKEN_APOS                         = { '\'' };
    private static final char[]          TOKEN_ESCAPED_APOS                 = { '\'', '\'' };

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
    private static final char[][]        NON_BIND_VARIABLE_SUFFIXES         = {
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
     * "Suffixes" that are placed behind a "?" character to form a JDBC bind
     * variable, rather than an operator.
     * <p>
     * [#11442] The above NON_BIND_VARIABLE_SUFFIXES leads to false positives,
     * such as <code>"?&lt;&gt;"</code>, which is a non-equality operator, not
     * an operator on its own.
     */
    private static final char[][]        BIND_VARIABLE_SUFFIXES             = {
        { '<', '>' }
    };

    /**
     * All hexadecimal digits accessible through array index, e.g.
     * <code>HEX_DIGITS[15] == 'f'</code>.
     */
    private static final char[]          HEX_DIGITS                         = "0123456789ABCDEF".toCharArray();
    private static final byte[]          HEX_LOOKUP                         = {
        /* 0x00 */ 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
        /* 0x10 */ 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
        /* 0x20 */ 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
        /* 0x30 */ 0,  1,  2,  3,  4,  5,  6,  7,  8,  9,  0,  0,  0,  0,  0,  0,
        /* 0x40 */ 0, 10, 11, 12, 13, 14, 15,  0,  0,  0,  0,  0,  0,  0,  0,  0,
        /* 0x50 */ 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
        /* 0x60 */ 0, 10, 11, 12, 13, 14, 15,  0,  0,  0,  0,  0,  0,  0,  0,  0,
    };

    static final Set<SQLDialect>         REQUIRES_BACKSLASH_ESCAPING        = SQLDialect.supportedBy(MARIADB, MYSQL);
    static final Set<SQLDialect>         NO_SUPPORT_NULL                    = SQLDialect.supportedBy(DERBY, FIREBIRD, H2, HSQLDB, TRINO);
    static final Set<SQLDialect>         NO_SUPPORT_NOT_NULL                = SQLDialect.supportedBy(TRINO);
    static final Set<SQLDialect>         NO_SUPPORT_BINARY_TYPE_LENGTH      = SQLDialect.supportedBy(POSTGRES, TRINO, YUGABYTEDB);
    static final Set<SQLDialect>         NO_SUPPORT_CAST_TYPE_IN_DDL        = SQLDialect.supportedBy(MARIADB, MYSQL);
    static final Set<SQLDialect>         SUPPORT_NON_BIND_VARIABLE_SUFFIXES = SQLDialect.supportedBy(POSTGRES, YUGABYTEDB);
    static final Set<SQLDialect>         SUPPORT_POSTGRES_LITERALS          = SQLDialect.supportedBy(POSTGRES, YUGABYTEDB);
    static final Set<SQLDialect>         DEFAULT_BEFORE_NULL                = SQLDialect.supportedBy(FIREBIRD, HSQLDB);
    static final Set<SQLDialect>         NO_SUPPORT_TIMESTAMP_PRECISION     = SQLDialect.supportedBy(DERBY);
    static final Set<SQLDialect>         DEFAULT_TIMESTAMP_NOT_NULL         = SQLDialect.supportedBy(MARIADB);

















    // ------------------------------------------------------------------------
    // XXX: Record constructors and related methods
    // ------------------------------------------------------------------------

    /**
     * Turn a {@link Result} into a list of {@link Row}
     */
    static final List<Row> rows(Result<?> result) {
        return map(result, r -> r.valuesRow());
    }



























































































































    /**
     * Create a new record
     */
    static final <R extends Record> RecordDelegate<R> newRecord(boolean fetched, Class<R> type) {
        return newRecord(fetched, type, null);
    }

    /**
     * Create a new record.
     */
    static final <R extends Record> RecordDelegate<R> newRecord(boolean fetched, Class<R> type, AbstractRow<R> fields) {
        return newRecord(fetched, type, fields, null);
    }

    /**
     * Create a new {@link Table} or {@link UDT} record.
     */
    static final <R extends Record> RecordDelegate<R> newRecord(boolean fetched, RecordQualifier<R> type) {
        return newRecord(fetched, type, null);
    }

    /**
     * Create a new {@link Table} or {@link UDT} record.
     */
    static final <R extends Record> RecordDelegate<R> newRecord(boolean fetched, RecordQualifier<R> type, Configuration configuration) {
        return newRecord(fetched, type.getRecordType(), (AbstractRow<R>) type.fieldsRow(), configuration);
    }

    /**
     * Create a new record.
     */
    static final <R extends Record> RecordDelegate<R> newRecord(boolean fetched, Class<? extends R> type, AbstractRow<? extends R> fields, Configuration configuration) {
        return newRecord(fetched, recordFactory(type, fields), configuration);
    }

    /**
     * Create a new record.
     */
    static final <R extends Record> RecordDelegate<R> newRecord(boolean fetched, Supplier<R> factory, Configuration configuration) {
        return new RecordDelegate<>(configuration, factory, fetched);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    static final <R extends Record> AbstractRow<R> row0(FieldsImpl<R> fields) {
        switch (fields.size()) {


            case 1: return new RowImpl1(fields);
            case 2: return new RowImpl2(fields);
            case 3: return new RowImpl3(fields);
            case 4: return new RowImpl4(fields);
            case 5: return new RowImpl5(fields);
            case 6: return new RowImpl6(fields);
            case 7: return new RowImpl7(fields);
            case 8: return new RowImpl8(fields);
            case 9: return new RowImpl9(fields);
            case 10: return new RowImpl10(fields);
            case 11: return new RowImpl11(fields);
            case 12: return new RowImpl12(fields);
            case 13: return new RowImpl13(fields);
            case 14: return new RowImpl14(fields);
            case 15: return new RowImpl15(fields);
            case 16: return new RowImpl16(fields);
            case 17: return new RowImpl17(fields);
            case 18: return new RowImpl18(fields);
            case 19: return new RowImpl19(fields);
            case 20: return new RowImpl20(fields);
            case 21: return new RowImpl21(fields);
            case 22: return new RowImpl22(fields);



            default: return (AbstractRow<R>) new RowImplN(fields);
        }
    }

    static final AbstractRow<?> row0(Collection<? extends Field<?>> fields) {
        return row0(fields.toArray(EMPTY_FIELD));
    }

    static final AbstractRow<?> row0(Field<?>... fields) {
        return row0(new FieldsImpl<>(fields));
    }

    static final Class<? extends AbstractRecord> recordType(int length) {
        switch (length) {


            case 1: return RecordImpl1.class;
            case 2: return RecordImpl2.class;
            case 3: return RecordImpl3.class;
            case 4: return RecordImpl4.class;
            case 5: return RecordImpl5.class;
            case 6: return RecordImpl6.class;
            case 7: return RecordImpl7.class;
            case 8: return RecordImpl8.class;
            case 9: return RecordImpl9.class;
            case 10: return RecordImpl10.class;
            case 11: return RecordImpl11.class;
            case 12: return RecordImpl12.class;
            case 13: return RecordImpl13.class;
            case 14: return RecordImpl14.class;
            case 15: return RecordImpl15.class;
            case 16: return RecordImpl16.class;
            case 17: return RecordImpl17.class;
            case 18: return RecordImpl18.class;
            case 19: return RecordImpl19.class;
            case 20: return RecordImpl20.class;
            case 21: return RecordImpl21.class;
            case 22: return RecordImpl22.class;



            default: return RecordImplN.class;
        }
    }

    /**
     * Create a new record factory.
     */
    @SuppressWarnings({ "unchecked" })
    static final <R extends Record> Supplier<R> recordFactory(Class<? extends R> type, AbstractRow<? extends R> row) {

        // An ad-hoc type resulting from a JOIN or arbitrary SELECT
        if (type == AbstractRecord.class || type == Record.class || InternalRecord.class.isAssignableFrom(type)) {
            switch (row.size()) {


                case 1: return () -> (R) new RecordImpl1<>(row);
                case 2: return () -> (R) new RecordImpl2<>(row);
                case 3: return () -> (R) new RecordImpl3<>(row);
                case 4: return () -> (R) new RecordImpl4<>(row);
                case 5: return () -> (R) new RecordImpl5<>(row);
                case 6: return () -> (R) new RecordImpl6<>(row);
                case 7: return () -> (R) new RecordImpl7<>(row);
                case 8: return () -> (R) new RecordImpl8<>(row);
                case 9: return () -> (R) new RecordImpl9<>(row);
                case 10: return () -> (R) new RecordImpl10<>(row);
                case 11: return () -> (R) new RecordImpl11<>(row);
                case 12: return () -> (R) new RecordImpl12<>(row);
                case 13: return () -> (R) new RecordImpl13<>(row);
                case 14: return () -> (R) new RecordImpl14<>(row);
                case 15: return () -> (R) new RecordImpl15<>(row);
                case 16: return () -> (R) new RecordImpl16<>(row);
                case 17: return () -> (R) new RecordImpl17<>(row);
                case 18: return () -> (R) new RecordImpl18<>(row);
                case 19: return () -> (R) new RecordImpl19<>(row);
                case 20: return () -> (R) new RecordImpl20<>(row);
                case 21: return () -> (R) new RecordImpl21<>(row);
                case 22: return () -> (R) new RecordImpl22<>(row);



                default: return () -> (R) new RecordImplN(row);
            }
        }

        // Any generated record
        else {
            try {

                // [#919] Allow for accessing non-public constructors
                final Constructor<? extends R> constructor = Reflect.accessible(type.getDeclaredConstructor());

                return () -> {
                    try {
                        return constructor.newInstance();
                    }
                    catch (Exception e) {
                        throw new IllegalStateException("Could not construct new record", e);
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
     * Get an attachable's configuration or a new {@link DefaultConfiguration}
     * if <code>null</code>.
     */
    static final Configuration configurationOrThrow(Attachable attachable) {
        if (attachable.configuration() == null)
            throw new DetachedException("No configuration attached: " + attachable);
        else
            return configuration(attachable.configuration());
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
     * Get a configuration or a new {@link DefaultConfiguration} if
     * <code>null</code>.
     */
    static final Configuration configuration(Scope scope) {
        return configuration(scope != null ? scope.configuration() : null);
    }

    /**
     * Get a converter from a {@link ConverterProvider} or <code>null</code> if
     * no converter could be provided.
     */
    static final <T, U> ContextConverter<T, U> converter(Configuration configuration, T instance, Class<T> tType, Class<U> uType) {
        Converter<T, U> result = configuration(configuration).converterProvider().provide(tType, uType);

        if (result == null)
            result = CONFIG.get().converterProvider().provide(tType, uType);

        // [#11823] [#12208] The new ad-hoc conversion API tries to avoid the Class<U> literal
        //                   meaning there are perfectly reasonable API usages when using MULTISET
        //                   where we can't decide on a converter prior to having an actual result
        //                   type - so, let's try again if we have the result value.
        if (result == null && tType == Converters.UnknownType.class)
            result = converter(configuration, instance, (Class<T>) (instance == null ? Object.class : instance.getClass()), uType);

        return result == null ? null : scoped(result);
    }

    /**
     * Get a converter from a {@link ConverterProvider} or <code>null</code> if
     * no converter could be provided.
     */
    static final <T, U> ContextConverter<T, U> converterOrFail(Configuration configuration, T instance, Class<T> tType, Class<U> uType) {
        ContextConverter<T, U> result = converter(configuration, instance, tType, uType);

        if (result == null)
            throw new DataTypeException("No Converter found for types " + tType.getName() + " and " + uType.getName());

        return result;
    }

    /**
     * Get a converter from a {@link ConverterProvider}.
     */
    static final <T, U> ContextConverter<T, U> converterOrFail(Attachable attachable, T instance, Class<T> tType, Class<U> uType) {
        return converterOrFail(configuration(attachable), instance, tType, uType);
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

    static final <T> T attach(Attachable attachable, Configuration configuration, Supplier<T> supplier) {
        Configuration previous = attachable.configuration();

        try {
            attachable.attach(configuration);
            return supplier.get();
        }
        finally {
            attachable.attach(previous);
        }
    }

    static final boolean attachRecords(Configuration configuration) {
        if (configuration != null) {
            Settings settings = configuration.settings();

            if (settings != null)
                return !FALSE.equals(settings.isAttachRecords());
        }

        return true;
    }

    static final Field<?>[] fieldArray(Collection<? extends Field<?>> fields) {
        return fields == null ? null : fields.toArray(EMPTY_FIELD);
    }

    // ------------------------------------------------------------------------
    // XXX: Data-type related methods
    // ------------------------------------------------------------------------

    static final DataType<?>[] dataTypes(Class<?>[] types) {
        return map(types, t -> t != null ? getDataType(t) : getDataType(Object.class), DataType[]::new);
    }

    // ------------------------------------------------------------------------
    // XXX: General utility methods
    // ------------------------------------------------------------------------

    private static final int      FIELD_NAME_CACHE_SIZE = 128;
    private static final String[] FIELD_NAME_STRINGS;
    private static final Name[]   FIELD_NAMES;

    static {
        FIELD_NAME_STRINGS = IntStream.range(0, FIELD_NAME_CACHE_SIZE).mapToObj(Tools::fieldNameString0).toArray(String[]::new);
        FIELD_NAMES = IntStream.range(0, FIELD_NAME_CACHE_SIZE).mapToObj(i -> name(FIELD_NAME_STRINGS[i])).toArray(Name[]::new);
    }

    static final <T> SortField<T> sortField(OrderField<T> field) {
        if (field instanceof SortField<T> s)
            return s;
        else if (field instanceof Field<T> f)
            return f.sortDefault();
        else
            throw new IllegalArgumentException("Field not supported : " + field);
    }

    static final SortField<?>[] sortFields(OrderField<?>[] fields) {
        if (fields instanceof SortField<?>[] s)
            return s;
        else
            return map(fields, o -> sortField(o), SortField[]::new);
    }

    static final List<SortField<?>> sortFields(Collection<? extends OrderField<?>> fields) {
        return Tools.map(fields, (OrderField<?> o) -> sortField(o));
    }

    // TODO: Check if these field names are ever really needed, or if we can just use the C field names
    private static final String fieldNameString0(int index) {
        return "v" + index;
    }

    static final String fieldNameString(int index) {
        return index < FIELD_NAME_CACHE_SIZE ? FIELD_NAME_STRINGS[index] : fieldNameString0(index);
    }

    static final Name fieldName(int index) {
        return index < FIELD_NAME_CACHE_SIZE ? FIELD_NAMES[index] : name(fieldNameString0(index));
    }

    static final Name[] fieldNames(int length) {
        Name[] result = new Name[length];

        for (int i = 0; i < length; i++)
            result[i] = fieldName(i);

        return result;
    }

    static final String[] fieldNameStrings(int length) {
        String[] result = new String[length];

        for (int i = 0; i < length; i++)
            result[i] = fieldNameString(i);

        return result;
    }

    static final Field<?>[] fields(int length) {
        return fields(length, SQLDataType.OTHER);
    }

    @SuppressWarnings("unchecked")
    static final <T> Field<T>[] fields(int length, DataType<T> type) {
        Field<T>[] result = new Field[length];

        for (int i = 0; i < length; i++)
            result[i] = DSL.field(fieldName(i), type);

        return result;
    }

    static final boolean reference(Field<?> field) {
        return field instanceof TableField
            || field instanceof SQLField && ((SQLField<?>) field).delegate.isName;
    }

    static final <T> Field<T> unqualified(Field<T> field) {
        return DSL.field(field.getUnqualifiedName(), field.getDataType());
    }

    static final <T> SortField<T> unqualified(SortField<T> field) {
        return field.$field(unqualified(field.$field()));
    }

    static final List<Field<?>> unaliasedFields(Collection<? extends Field<?>> fields) {
        return map(fields, (f, i) -> DSL.field(fieldName(i), f.getDataType()).as(f));
    }

    static final <R extends Record, O extends Record> ReferenceImpl<R, O> aliasedKey(ForeignKey<R, O> key, Table<R> child, Table<O> parent) {

        // [#10603] [#5050] TODO: Solve aliasing constraints more generically
        // [#8762] We can't dereference child.fields() or parent.fields() here yet, because this method is being called by
        //         the TableImpl constructor, meaning the fields are not initialised yet.
        return new ReferenceImpl<>(
            child,
            key.getQualifiedName(),
            Tools.fieldsByName(child, key.getFieldsArray()),
            key.getKey(),
            Tools.fieldsByName(parent, key.getKeyFieldsArray()),
            key.enforced()
        );
    }

    static final List<Field<?>> aliasedFields(Collection<? extends Field<?>> fields) {
        return map(fields, (f, i) -> f.as(fieldName(i)));
    }

    static final Field<?>[] fieldsByName(String[] fieldNames) {
        return fieldsByName(null, fieldNames);
    }

    static final Field<?>[] fieldsByName(Name tableName, int length) {
        Field<?>[] result = new Field[length];

        if (tableName == null)
            for (int i = 0; i < length; i++)
                result[i] = DSL.field(fieldName(i));
        else
            for (int i = 0; i < length; i++)
                result[i] = DSL.field(name(tableName, fieldName(i)));

        return result;
    }

    @SuppressWarnings("unchecked")
    static final <R extends Record> TableField<R, ?>[] fieldsByName(Table<R> tableName, Field<?>[] fieldNames) {
        if (tableName == null)
            return map(fieldNames, n -> (TableField<R, ?>) DSL.field(n.getUnqualifiedName(), n.getDataType()), TableField[]::new);
        else
            return map(fieldNames, n -> (TableField<R, ?>) DSL.field(tableName.getQualifiedName().append(n.getUnqualifiedName()), n.getDataType()), TableField[]::new);
    }

    static final Field<?>[] fieldsByName(Name tableName, Name[] fieldNames) {
        if (tableName == null)
            return map(fieldNames, n -> DSL.field(n), Field[]::new);
        else
            return map(fieldNames, n -> DSL.field(name(tableName, n)), Field[]::new);
    }

    static final Field<?>[] fieldsByName(String tableName, String[] fieldNames) {
        if (StringUtils.isEmpty(tableName))
            return map(fieldNames, n -> DSL.field(name(n)), Field[]::new);
        else
            return map(fieldNames, n -> DSL.field(name(tableName, n)), Field[]::new);
    }

    static final Field<?>[] fieldsByName(Name[] names) {
        return map(names, n -> DSL.field(n), Field[]::new);
    }

    static final Name[] names(String[] names) {
        return map(names, n -> DSL.name(n), Name[]::new);
    }

    static final List<Name> names(Collection<?> names) {
        return map(names, n -> n instanceof Name name ? name : DSL.name(String.valueOf(n)));
    }

    static final String sanitiseName(Configuration configuration, String name) {
        switch (configuration.family()) {






            default:
                return name;
        }
    }

    static final List<JSONEntry<?>> jsonEntries(Field<?>[] entries) {
        return Tools.map(entries, f -> DSL.jsonEntry(f));
    }

    private static final IllegalArgumentException fieldExpected(Object value) {
        return new IllegalArgumentException("Cannot interpret argument of type " + value.getClass() + " as a Field: " + value);
    }

    /**
     * [#461] [#473] [#2597] [#8234] Some internals need a cast only if necessary.
     */
    @SuppressWarnings("unchecked")
    static final <T> Field<T>[] castAllIfNeeded(Field<?>[] fields, Class<T> type) {
        Field<T>[] castFields = new Field[fields.length];

        for (int i = 0; i < fields.length; i++)
            castFields[i] = castIfNeeded(fields[i], type);

        return castFields;
    }

    /**
     * [#461] [#473] [#2597] [#8234] Some internals need a cast only if necessary.
     */
    @SuppressWarnings("unchecked")
    static final <T> Field<T>[] castAllIfNeeded(Field<?>[] fields, DataType<T> type) {
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

    static final Param<JSON> field(JSON value) {
        return val((Object) value, SQLDataType.JSON);
    }

    static final Param<JSONB> field(JSONB value) {
        return val((Object) value, SQLDataType.JSONB);
    }

    static final Param<XML> field(XML value) {
        return val((Object) value, SQLDataType.XML);
    }

    /**
     * @deprecated - This method is probably called by mistake (ambiguous static import).
     */
    @Deprecated
    static final Field<Object> field(Name name) {
        return DSL.field(name);
    }

    @SuppressWarnings("unchecked")
    private static final <T> Field<T> field(
        Object value,
        boolean defaultInferred,
        Function<? super Object, ? extends Param<T>> defaultValue
    ) {

        // [#14694] Inferred data types may have to be refined lazily, here.
        //          For example, when wrapping row(1, 2), then the integers may
        //          still require a converter to be applied to them, when the
        //          row is passed to the INSERT's valuesOfRows() method.
        if (value instanceof Val<?> p1) {
            if (p1.inferredDataType && !defaultInferred) {
                Val<T> p2 = (Val<T>) defaultValue.apply(p1.getValue());
                p2.setInline0(p1.isInline());
                return p2;
            }
            else
                return (Field<T>) p1;
        }

        // Fields can be mixed with constant values
        else if (value instanceof Field<?>)
            return (Field<T>) value;

        // [#6362] [#8220] Single-column selects can be considered fields, too
        else if (value instanceof Select && Tools.degree((Select<?>) value) == 1)
            return DSL.field((Select<Record1<T>>) value);

        // [#13251] Rows can be mixed with values in ROW constructors
        else if (value instanceof AbstractRow<?> r)
            return (Field<T>) r.rf();

        // [#15008] Tables can be mixed with values in ROW constructors
        else if (value instanceof AbstractTable<?> t)
            return (Field<T>) t.tf();

        // [#4771] Any other QueryPart type is not supported here
        else if (value instanceof QueryPart)
            throw fieldExpected(value);

        else
            return defaultValue.apply(value);
    }

    @SuppressWarnings("unchecked")
    static final <T> Field<T> field(T value) {
        return field(value, true, v -> DSL.val0((T) v, true));
    }

    static final <T> Field<T> field(Object value, Field<T> field) {
        return field(value, false, v -> val(v, field));
    }

    static final <T> Field<T> field(Object value, Class<T> type) {
        return field(value, false, v -> val(v, type));
    }

    static final <T> Field<T> field(Object value, DataType<T> type) {
        return field(value, false, v -> val(v, type));
    }

    static final <T> List<Field<T>> fields(T[] values) {
        return asList(fieldsArray(values));
    }

    @SuppressWarnings("unchecked")
    static final <T> Field<T>[] fieldsArray(T[] values) {
        return map(values, v -> field(v), Field[]::new);
    }

    static final <T> List<Field<T>> fields(Object[] values, Field<T> field) {
        if (field == null)
            return new ArrayList<>();
        else
            return map(values, v -> field(v, field));
    }

    static final List<Field<?>> fields(Object[] values, Field<?>[] fields) {
        return asList(fieldsArray(values, fields));
    }

    static final Field<?>[] fieldsArray(Object[] values, Field<?>[] fields) {
        return map(values, (v, i) -> field(v, fields[i]), Field[]::new);
    }

    static final <T> List<Field<T>> fields(Object[] values, DataType<T> type) {
        return asList(fieldsArray(values, type));
    }

    static final <T> List<Field<T>> fields(Collection<?> values, DataType<T> type) {
        return map(values, v -> field(v, type));
    }

    @SuppressWarnings("unchecked")
    static final <T> Field<T>[] fieldsArray(Object[] values, DataType<T> type) {
        return map(values, v -> field(v, type), Field[]::new);
    }

    static final List<Field<?>> fields(Object[] values, DataType<?>[] types) {
        return asList(fieldsArray(values, types));
    }

    static final Field<?>[] fieldsArray(Object[] values, DataType<?>[] types) {
        return map(values, (v, i) -> field(v, types[i]), Field[]::new);
    }

    static final IllegalArgumentException indexFail(Fields row, Field<?> field) {
        return new IllegalArgumentException("Field (" + field + ") is not contained in Row " + row);
    }

    static final int indexOrFail(Fields row, Field<?> field) {
        int result = row.indexOf(field);

        if (result < 0)
            throw indexFail(row, field);

        return result;
    }

    static final IllegalArgumentException indexFail(Fields row, String fieldName) {
        throw new IllegalArgumentException("Field (" + fieldName + ") is not contained in Row " + row);
    }

    static final int indexOrFail(Fields row, String fieldName) {
        int result = row.indexOf(fieldName);

        if (result < 0)
            throw indexFail(row, fieldName);

        return result;
    }

    static final IllegalArgumentException indexFail(Fields row, Name fieldName) {
        throw new IllegalArgumentException("Field (" + fieldName + ") is not contained in Row " + row);
    }

    static final int indexOrFail(Fields row, Name fieldName) {
        int result = row.indexOf(fieldName);

        if (result < 0)
            throw indexFail(row, fieldName);

        return result;
    }

    static final IllegalArgumentException indexFail(Fields row, int fieldIndex) {
        throw new IllegalArgumentException("Field (" + fieldIndex + ") is not contained in Row " + row);
    }

    static final int indexOrFail(Fields row, int fieldIndex) {
        Field<?> result = row.field(fieldIndex);

        if (result == null)
            throw indexFail(row, fieldIndex);

        return fieldIndex;
    }

    private static final <T> List<T> newListWithCapacity(Iterable<?> it) {
        return it instanceof Collection<?> c ? new ArrayList<>(c.size()) : new ArrayList<>();
    }

    static final <T, R, X extends Throwable> R apply(@Nullable T t, ThrowingFunction<? super @NotNull T, ? extends R, ? extends X> f) throws X {
        return t == null ? null : f.apply(t);
    }

    static final <T, R, X extends Throwable> R applyOrElse(@Nullable T t, ThrowingFunction<? super @NotNull T, ? extends R, ? extends X> f, ThrowingSupplier<? extends R, ? extends X> s) throws X {
        return t == null ? s.get() : f.apply(t);
    }

    static final <T, X extends Throwable> T orElse(@Nullable T t, ThrowingSupplier<? extends T, ? extends X> s) throws X {
        return t == null ? s.get() : t;
    }

    static final <T> T let(@Nullable T t, Consumer<? super @NotNull T> consumer) {
        if (t != null)
            consumer.accept(t);

        return t;
    }

    static final <T, E extends Exception> boolean allMatch(T[] array, ThrowingPredicate<? super T, E> test) throws E {
        return !anyMatch(array, test.negate());
    }

    static final <T, E extends Exception> boolean allMatch(T[] array, ThrowingIntPredicate<? super T, E> test) throws E {
        return !anyMatch(array, test.negate());
    }

    static final <T, E extends Exception> boolean allMatch(Iterable<? extends T> it, ThrowingPredicate<? super T, E> test) throws E {
        return !anyMatch(it, test.negate());
    }

    static final <T, E extends Exception> boolean allMatch(Iterable<? extends T> it, ThrowingIntPredicate<? super T, E> test) throws E {
        return !anyMatch(it, test.negate());
    }

    static final <T, E extends Exception> boolean anyMatch(T[] array, ThrowingPredicate<? super T, E> test) throws E {
        return findAny(array, test, t -> TRUE) != null;
    }

    static final <T, E extends Exception> boolean anyMatch(T[] array, ThrowingIntPredicate<? super T, E> test) throws E {
        return findAny(array, test, t -> TRUE) != null;
    }

    static final <T, E extends Exception> boolean anyMatch(Iterable<? extends T> it, ThrowingPredicate<? super T, E> test) throws E {
        return findAny(it, test, t -> TRUE) != null;
    }

    static final <T, E extends Exception> boolean anyMatch(Iterable<? extends T> it, ThrowingIntPredicate<? super T, E> test) throws E {
        return findAny(it, test, t -> TRUE) != null;
    }

    static final <T, E extends Exception> T findAny(T[] array, ThrowingPredicate<? super T, E> test) throws E {
        return findAny(array, test, t -> t);
    }

    static final <T, E extends Exception> T findAny(T[] array, ThrowingIntPredicate<? super T, E> test) throws E {
        return findAny(array, test, t -> t);
    }

    static final <T, E extends Exception> T findAny(Iterable<? extends T> it, ThrowingPredicate<? super T, E> test) throws E {
        return findAny(it, test, t -> t);
    }

    static final <T, E extends Exception> T findAny(Iterable<? extends T> it, ThrowingIntPredicate<? super T, E> test) throws E {
        return findAny(it, test, t -> t);
    }

    static final <T, U, E extends Exception> U findAny(T[] array, ThrowingPredicate<? super T, E> test, ThrowingFunction<? super T, ? extends U, E> function) throws E {
        if (array != null)
            for (T t : array)
                if (test.test(t))
                    return function.apply(t);

        return null;
    }

    static final <T, U, E extends Exception> U findAny(T[] array, ThrowingIntPredicate<? super T, E> test, ThrowingFunction<? super T, ? extends U, E> function) throws E {
        if (array != null) {
            int i = 0;

            for (T t : array)
                if (test.test(t, i++))
                    return function.apply(t);
        }

        return null;
    }

    static final <T, U, E extends Exception> U findAny(Iterable<? extends T> it, ThrowingPredicate<? super T, E> test, ThrowingFunction<? super T, ? extends U, E> function) throws E {
        if (it != null)
            for (T t : it)
                if (test.test(t))
                    return function.apply(t);

        return null;
    }

    static final <T, U, E extends Exception> U findAny(Iterable<? extends T> it, ThrowingIntPredicate<? super T, E> test, ThrowingFunction<? super T, ? extends U, E> function) throws E {
        if (it != null) {
            int i = 0;

            for (T t : it)
                if (test.test(t, i++))
                    return function.apply(t);
        }

        return null;
    }

    static final Condition allNull(Field<?>[] fields) {
        return DSL.and(map(fields, Field::isNull));
    }

    static final Condition allNotNull(Field<?>[] fields) {
        return DSL.and(map(fields, Field::isNotNull));
    }

    static final <T> List<List<T>> chunks(List<T> list, int size) {
        int l;

        if (size <= 0 || size == Integer.MAX_VALUE || (l = list.size()) <= size)
            return asList(list);

        List<List<T>> result = new ArrayList<>();
        int prev = 0, next = size;
        while (prev < l) {
            result.add(list.subList(prev, Math.min(next, l)));
            prev = next;
            next += size;
        }

        return result;
    }

    /**
     * "sneaky-throw" a checked exception or throwable.
     */
    static final void throwChecked(Throwable t) {
        Tools.<RuntimeException>throwChecked0(t);
    }

    /**
     * "sneaky-throw" a checked exception or throwable.
     */
    @SuppressWarnings("unchecked")
    static final <E extends Throwable> void throwChecked0(Throwable throwable) throws E {
        throw (E) throwable;
    }

    static final <T, R> Function<T, R> checkedFunction(ThrowingFunction<T, R, Throwable> function) {
        return t -> {
            try {
                return function.apply(t);
            }
            catch (Throwable e) {
                throwChecked(e);
                throw new IllegalStateException(e);
            }
        };
    }

    /**
     * Like <code>Stream.of(array).map(mapper).toArray(constructor)</code> but
     * without the entire stream pipeline.
     */
    static final <T, U, E extends Exception> U[] map(T[] array, ThrowingFunction<? super T, ? extends U, E> mapper, IntFunction<U[]> constructor) throws E {
        if (array == null)
            return constructor.apply(0);

        U[] result = constructor.apply(array.length);
        for (int i = 0; i < array.length; i++)
            result[i] = mapper.apply(array[i]);

        return result;
    }

    /**
     * Like <code>Stream.of(array).map(mapper).toArray(constructor)</code> but
     * without the entire stream pipeline.
     */
    static final <U, E extends Exception> U[] map(int[] array, ThrowingIntFunction<? extends U, E> mapper, IntFunction<U[]> constructor) throws E {
        if (array == null)
            return constructor.apply(0);

        U[] result = constructor.apply(array.length);
        for (int i = 0; i < array.length; i++)
            result[i] = mapper.apply(array[i]);

        return result;
    }

    /**
     * Like <code>Stream.of(array).map(mapper).toArray(constructor)</code> but
     * without the entire stream pipeline.
     */
    static final <T, U, E extends Exception> U[] map(Collection<? extends T> collection, ThrowingFunction<? super T, ? extends U, E> mapper, IntFunction<U[]> constructor) throws E {
        if (collection == null)
            return constructor.apply(0);

        U[] result = constructor.apply(collection.size());
        int i = 0;
        for (T t : collection)
            result[i++] = mapper.apply(t);

        return result;
    }

    /**
     * Like
     * <code>Stream.of(array).zipWithIndex().map(mapper).toArray(constructor)</code>
     * but without the entire stream pipeline.
     */
    static final <T, U, E extends Exception> U[] map(T[] array, ThrowingObjIntFunction<? super T, ? extends U, E> mapper, IntFunction<U[]> constructor) throws E {
        if (array == null)
            return constructor.apply(0);

        U[] result = constructor.apply(array.length);
        for (int i = 0; i < array.length; i++)
            result[i] = mapper.apply(array[i], i);

        return result;
    }

    /**
     * Like
     * <code>Stream.of(array).zipWithIndex().map(mapper).toArray(constructor)</code>
     * but without the entire stream pipeline.
     */
    static final <U, E extends Exception> U[] map(int[] array, ThrowingIntIntFunction<? extends U, E> mapper, IntFunction<U[]> constructor) throws E {
        if (array == null)
            return constructor.apply(0);

        U[] result = constructor.apply(array.length);
        for (int i = 0; i < array.length; i++)
            result[i] = mapper.apply(array[i], i);

        return result;
    }

    /**
     * Like
     * <code>Stream.of(array).zipWithIndex().map(mapper).toArray(constructor)</code>
     * but without the entire stream pipeline.
     */
    static final <T, U, E extends Exception> U[] map(Collection<? extends T> collection, ThrowingObjIntFunction<? super T, ? extends U, E> mapper, IntFunction<U[]> constructor) throws E {
        if (collection == null)
            return constructor.apply(0);

        U[] result = constructor.apply(collection.size());
        int i = 0;
        for (T t : collection)
            result[i] = mapper.apply(t, i++);

        return result;
    }

    /**
     * Like <code>Stream.of(array).map(mapper).toList()</code> but
     * without the entire stream pipeline.
     */
    static final <T, U, E extends Exception> List<U> map(T[] array, ThrowingFunction<? super T, ? extends U, E> mapper) throws E {
        if (array == null)
            return emptyList();

        List<U> result = new ArrayList<>(array.length);
        for (T t : array)
            result.add(mapper.apply(t));

        return result;
    }

    /**
     * Like <code>Stream.of(array).map(mapper).toList()</code> but
     * without the entire stream pipeline.
     */
    static final <U, E extends Exception> List<U> map(int[] array, ThrowingIntFunction<? extends U, E> mapper) throws E {
        if (array == null)
            return emptyList();

        List<U> result = new ArrayList<>(array.length);
        for (int t : array)
            result.add(mapper.apply(t));

        return result;
    }

    /**
     * Like <code>Stream.of(array).map(mapper).toList()</code> but
     * without the entire stream pipeline.
     */
    static final <T, U, E extends Exception> List<U> map(Iterable<? extends T> it, ThrowingFunction<? super T, ? extends U, E> mapper) throws E {
        if (it == null)
            return emptyList();

        List<U> result = newListWithCapacity(it);
        for (T t : it)
            result.add(mapper.apply(t));

        return result;
    }

    /**
     * Like <code>Stream.of(array).map(mapper).toList()</code> but
     * without the entire stream pipeline.
     */
    static final <T, U, E extends Exception> List<U> flatMap(Iterable<? extends T> it, ThrowingFunction<? super T, ? extends List<? extends U>, E> mapper) throws E {
        if (it == null)
            return emptyList();

        List<U> result = newListWithCapacity(it);
        for (T t : it)
            result.addAll(mapper.apply(t));

        return result;
    }

    /**
     * Like <code>Stream.of(array).zipWithIndex().map(mapper).toList()</code>
     * but without the entire stream pipeline.
     */
    static final <T, U, E extends Exception> List<U> map(T[] array, ThrowingObjIntFunction<? super T, ? extends U, E> mapper) throws E {
        if (array == null)
            return emptyList();

        List<U> result = new ArrayList<>(array.length);
        for (int i = 0; i < array.length; i++)
            result.add(mapper.apply(array[i], i));

        return result;
    }

    /**
     * Like <code>Stream.of(array).zipWithIndex().map(mapper).toList()</code>
     * but without the entire stream pipeline.
     */
    static final <U, E extends Exception> List<U> map(int[] array, ThrowingIntIntFunction<? extends U, E> mapper) throws E {
        if (array == null)
            return emptyList();

        List<U> result = new ArrayList<>(array.length);
        for (int i = 0; i < array.length; i++)
            result.add(mapper.apply(array[i], i));

        return result;
    }

    /**
     * Like <code>Stream.of(array).map(mapper).toList()</code> but
     * without the entire stream pipeline.
     */
    static final <T, U, E extends Exception> List<U> map(Iterable<? extends T> it, ThrowingObjIntFunction<? super T, ? extends U, E> mapper) throws E {
        if (it == null)
            return emptyList();

        List<U> result = newListWithCapacity(it);
        int i = 0;
        for (T t : it)
            result.add(mapper.apply(t, i++));

        return result;
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

    static final <T, U> Iterator<U> iterator(Iterator<? extends T> iterator, Function<? super T, ? extends U> mapper) {
        return new Iterator<U>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public U next() {
                return mapper.apply(iterator.next());
            }

            @Override
            public void remove() {
                iterator.remove();
            }
        };
    }

    /**
     * Reverse iterate over an array.
     */
    @SafeVarargs
    static final <T> Iterable<T> reverseIterable(final T... array) {
        return reverseIterable(Arrays.asList(array));
    }

    /**
     * Reverse iterate over an array.
     */
    @SafeVarargs
    static final <T> Iterator<T> reverseIterator(T... array) {
        return reverseIterator(Arrays.asList(array));
    }

    /**
     * Reverse iterate over a list.
     */
    static final <T> Iterable<T> reverseIterable(List<T> list) {
        return () -> reverseIterator(list);
    }

    /**
     * Reverse iterate over a list.
     */
    static final <T> Iterator<T> reverseIterator(List<T> list) {
        return new Iterator<T>() {
            final ListIterator<T> li = list.listIterator(list.size());

            @Override
            public boolean hasNext() {
                return li.hasPrevious();
            }

            @Override
            public T next() {
                return li.previous();
            }

            @Override
            public void remove() {
                li.remove();
            }
        };
    }

    /**
     * Use this rather than {@link Arrays#asList(Object...)} for
     * <code>null</code>-safety
     */
    @SafeVarargs
    static final <T> List<T> list(T... array) {
        return array == null ? emptyList() : asList(array);
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
        if (iterable == null)
            return null;

        Iterator<? extends T> it = iterable.iterator();
        if (it.hasNext())
            return it.next();
        else
            return null;
    }

    static final <T> T last(Collection<T> collection) {
        if (collection.isEmpty())
            return null;
        else if (collection instanceof List<T> l)
            return l.get(collection.size() - 1);

        T last = null;
        for (Iterator<T> it = collection.iterator(); it.hasNext(); last = it.next());
        return last;
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
                throw exception(cursor, new TooManyRowsException("Cursor returned more than one result"));
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
                throw exception(cursor, new NoDataFoundException("Cursor returned no rows"));
            else if (size == 1)
                return result.get(0);
            else
                throw exception(cursor, new TooManyRowsException("Cursor returned more than one result"));
        }
        finally {
            cursor.close();
        }
    }

    private static final RuntimeException exception(Cursor<?> cursor, RuntimeException e) {

        // [#8877] Make sure these exceptions pass through ExecuteListeners as well
        if (cursor instanceof CursorImpl<?> c) {
            c.ctx.exception(e);
            c.listener.exception(c.ctx);
            return c.ctx.exception();
        }
        else
            return e;
    }

    @SuppressWarnings("unchecked")
    static final <Q extends QueryPart> void visitAutoAliased(
        Context<?> ctx,
        Q q,
        Predicate<? super Context<?>> declaring,
        BiConsumer<? super Context<?>, ? super Q> visit
    ) {
        Q alternative;

        if (declaring.test(ctx) && q instanceof AutoAlias && (alternative = ((AutoAlias<Q>) q).autoAlias(ctx, q)) != null)
            visit.accept(ctx, alternative);
        else
            visit.accept(ctx, q);
    }

    static final void visitSubquery(
        Context<?> ctx,
        QueryPart query
    ) {
        visitSubquery(ctx, query, 0, true);
    }

    static final void visitSubquery(
        Context<?> ctx,
        QueryPart query,
        int characteristics
    ) {
        visitSubquery(ctx, query, characteristics, true);
    }

    static final void visitSubquery(
        Context<?> ctx,
        QueryPart query,
        int characteristics,
        boolean parentheses
    ) {





        if (parentheses)
            ctx.sql('(');

        boolean previousPredicandSubquery = ctx.predicandSubquery();
        boolean previousDerivedTableSubquery = ctx.derivedTableSubquery();
        boolean previousSetOperationSubquery = ctx.setOperationSubquery();

        ctx.subquery(true)
           .predicandSubquery((characteristics & PREDICAND) != 0)
           .derivedTableSubquery((characteristics & DERIVED_TABLE) != 0)
           .setOperationSubquery((characteristics & SET_OPERATION) != 0)
           .formatIndentStart()
           .formatNewLine()
           .visit(query)
           .formatIndentEnd()
           .formatNewLine()
           .setOperationSubquery(previousSetOperationSubquery)
           .derivedTableSubquery(previousDerivedTableSubquery)
           .predicandSubquery(previousPredicandSubquery)
           .subquery(false);

        if (parentheses)
            ctx.sql(')');
    }

    /**
     * Visit each query part from a collection, given a context.
     */
    static final <C extends Context<?>> C visitAll(C ctx, Collection<? extends QueryPart> parts) {
        if (parts != null)
            for (QueryPart part : parts)
                ctx.visit(part);

        return ctx;
    }

    /**
     * Visit each query part from an array, given a context.
     */
    static final <C extends Context<?>> C visitAll(C ctx, QueryPart[] parts) {
        if (parts != null)
            for (QueryPart part : parts)
                ctx.visit(part);

        return ctx;
    }

    /**
     * Apply {@link RenderMapping} to a qualified {@link Name}, assuming the
     * {@link Name#qualifier()} describes the schema, if available.
     */
    static final <C extends Context<?>> C visitMappedSchema(C ctx, Name qualifiedName) {
        if (qualifiedName.qualified()) {
            Schema s = getMappedSchema(ctx, new SchemaImpl(qualifiedName.qualifier()));

            if (s != null)
                ctx.visit(s).sql('.');

            ctx.visit(qualifiedName.unqualifiedName());
        }
        else
            ctx.visit(qualifiedName);

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
        if (TRUE.equals(ctx.settings().isRenderPlainSQLTemplatesAsRaw())) {
            ctx.sql(sql);
            return;
        }

        RenderContext render = ctx instanceof RenderContext r ? r : null;
        BindContext   bind   = ctx instanceof BindContext b   ? b : null;

        int substituteIndex = 0;
        char[] sqlChars = sql.toCharArray();

        // [#1593] Create a dummy renderer if we're in bind mode
        if (render == null) render = new DefaultRenderContext(bind.configuration(), ctx.executeContext());
        SQLDialect family = render.family();
        boolean mysql = SUPPORTS_HASH_COMMENT_SYNTAX.contains(render.dialect());
        char[][][] quotes = QUOTES.get(family);

        // [#3630] Depending on this setting, we need to consider backslashes as escape characters within string literals.
        boolean needsBackslashEscaping = needsBackslashEscaping(ctx.configuration());

        characterLoop:
        for (int i = 0; i < sqlChars.length; i++) {

            // [#1797] [#9651] Skip content inside of single-line comments, e.g.
            // select 1 x -- what's this ?'?
            // from t_book -- what's that ?'?
            // where id = ?
            if (peek(sqlChars, i, TOKEN_SINGLE_LINE_COMMENT) ||
                peek(sqlChars, i, TOKEN_SINGLE_LINE_COMMENT_C) ||

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
                do {
                    render.sql(sqlChars[i++]);

                    if (peek(sqlChars, i, TOKEN_MULTI_LINE_COMMENT_OPEN))
                        nestedMultilineCommentLevel++;
                    else if (peek(sqlChars, i, TOKEN_MULTI_LINE_COMMENT_CLOSE))
                        nestedMultilineCommentLevel--;

                }
                while (nestedMultilineCommentLevel != 0);

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

                    // [#9648] The "string literal" might not be one, if we're inside
                    //         of some vendor specific comment syntax
                    if (i >= sqlChars.length)
                        break characterLoop;

                    // [#3000] [#3630] Consume backslash-escaped characters if needed
                    else if (sqlChars[i] == '\\' && needsBackslashEscaping)
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
                        && SUPPORT_POSTGRES_LITERALS.contains(ctx.dialect())
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


                    // [#9648] The "identifier" might not be one, if we're inside
                    //         of some vendor specific comment syntax
                    if (i >= sqlChars.length)
                        break characterLoop;

                    // Consume an escaped quote
                    else if (peek(sqlChars, i, quotes[QUOTE_END_DELIMITER_ESCAPED][delimiter])) {
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
                if (sqlChars[i] == '?' && i + 1 < sqlChars.length && SUPPORT_NON_BIND_VARIABLE_SUFFIXES.contains(ctx.dialect())) {

                    nonBindSuffixLoop:
                    for (char[] candidate : NON_BIND_VARIABLE_SUFFIXES) {
                        if (peek(sqlChars, i + 1, candidate)) {
                            for (char[] exclude : BIND_VARIABLE_SUFFIXES)
                                if (peek(sqlChars, i + 1, exclude))
                                    continue nonBindSuffixLoop;

                            for (int j = i; i - j <= candidate.length; i++)
                                render.sql(sqlChars[i]);

                            // [#13489] The operator could be the last thing in the template
                            if (i < sqlChars.length)
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
                        if (index < 0 || index >= substitutes.size())
                            throw new TemplatingException("No substitute QueryPart provided for placeholder {" + index + "} in plain SQL template: " + sql);

                        QueryPart substitute = substitutes.get(index);
                        render.visit(substitute);

                        if (bind != null)
                            bind.visit(substitute);
                    }
                    else {
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
        return escaping == ON || (escaping == DEFAULT && REQUIRES_BACKSLASH_ESCAPING.contains(configuration.dialect()));
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
                    for (char c : WHITESPACE_CHARACTERS)
                        if (sqlChars[index + i] == c)
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
                if (substitute instanceof QueryPart q) {
                    result.add(q);
                }
                else {
                    @SuppressWarnings("unchecked")
                    Class<Object> type = (Class<Object>) (substitute != null ? substitute.getClass() : Object.class);
                    result.add(new Val<>(substitute, DSL.getDataType(type), true));
                }
            }

            return result;
        }
    }

    @SuppressWarnings("unchecked")
    static final <T> T[] combine(T value, T[] array) {
        T[] result = (T[]) java.lang.reflect.Array.newInstance(array.getClass().getComponentType(), array.length + 1);
        result[0] = value;
        System.arraycopy(array, 0, result, 1, array.length);
        return result;
    }

    static final <T> T[] combine(T[] array, T value) {
        T[] result = Arrays.copyOf(array, array.length + 1);
        result[array.length] = value;
        return result;
    }

    static final <T> T[] combine(T[] a1, T[] a2) {
        T[] result = Arrays.copyOf(a1, a1.length + a2.length);
        System.arraycopy(a2, 0, result, a1.length, a2.length);
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
     * Translate a {@link R2dbcException} to a {@link DataAccessException}
     */
    static final RuntimeException translate(String sql, Throwable t) {
        if (t instanceof R2dbcException e)
            return translate(sql, e);
        else if (t instanceof SQLException e)
            return translate(sql, e);
        else if (t instanceof RuntimeException e)
            return translate(sql, e);
        else if (t != null)
            return new DataAccessException("SQL [" + sql + "]; Unspecified Throwable", t);
        else
            return new DataAccessException("SQL [" + sql + "]; Unspecified Throwable");
    }

    /**
     * Translate a {@link R2dbcException} to a {@link DataAccessException}
     */
    static final DataAccessException translate(String sql, R2dbcException e) {
        if (e != null)
            return translate(sql, e, sqlStateClass(e));
        else
            return new DataAccessException("SQL [" + sql + "]; Unspecified R2dbcException");
    }

    /**
     * Translate a {@link SQLException} to a {@link DataAccessException}
     */
    static final DataAccessException translate(String sql, SQLException e) {
        if (e != null)
            return translate(sql, e, sqlStateClass(e));
        else
            return new DataAccessException("SQL [" + sql + "]; Unspecified SQLException");
    }

    private static final DataAccessException translate(String sql, Exception e, SQLStateClass sqlState) {
        switch (sqlState) {
            case C22_DATA_EXCEPTION:
                return new DataException("SQL [" + sql + "]; " + e.getMessage(), e);
            case C23_INTEGRITY_CONSTRAINT_VIOLATION:
                return new IntegrityConstraintViolationException("SQL [" + sql + "]; " + e.getMessage(), e);
            default:
                return new DataAccessException("SQL [" + sql + "]; " + e.getMessage(), e);
        }
    }

    /**
     * Translate a {@link RuntimeException} to a {@link DataAccessException}
     */
    static final RuntimeException translate(String sql, RuntimeException e) {
        if (e != null)
            return e;
        else
            return new DataAccessException("SQL [" + sql + "]; Unspecified RuntimeException");
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
     * Type-safely copy a value from one record to another
     */
    static final <T> void setValue(AbstractRecord target, Field<T> targetField, int targetIndex, Record source, int sourceIndex) {
        setValue(target, targetField, targetIndex, source.get(sourceIndex));
    }

    /**
     * Type-safely set a value to a record
     */
    static final <T> void setValue(Record target, Field<T> targetField, Object value) {
        target.set(targetField, targetField.getDataType().convert(value));
    }

    /**
     * Type-safely set a value to a record
     */
    static final <T> void setValue(AbstractRecord target, Field<T> targetField, int targetIndex, Object value) {
        target.set(targetField, targetIndex, targetField.getDataType().convert(value));
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
    static final Catalog getMappedCatalog(Scope scope, Catalog catalog) {
        if (scope != null)
            return scope.configuration().schemaMapping().map(catalog);

        return catalog;
    }

    /**
     * Map a {@link Schema} according to the configured {@link org.jooq.SchemaMapping}
     */
    static final Schema getMappedSchema(Scope scope, Schema schema) {
        if (scope != null)
            return scope.configuration().schemaMapping().map(schema);

        return schema;
    }

    /**
     * Map a {@link Table} according to the configured {@link org.jooq.SchemaMapping}
     */
    static final <R extends Record> Table<R> getMappedTable(Scope scope, Table<R> table) {
        if (scope != null)
            return scope.configuration().schemaMapping().map(table);

        return table;
    }

    /**
     * Map an {@link QualifiedRecord} according to the configured
     * {@link org.jooq.SchemaMapping}
     */
    @SuppressWarnings("unchecked")
    static final String getMappedUDTName(Scope scope, Class<? extends QualifiedRecord<?>> type) {
        return getMappedUDTName(scope, Tools.newRecord(false, (Class<QualifiedRecord<?>>) type).operate(null));
    }

    /**
     * Map an {@link QualifiedRecord} according to the configured
     * {@link org.jooq.SchemaMapping}
     */
    static final String getMappedUDTName(Scope scope, QualifiedRecord<?> record) {
        RecordQualifier<?> udt = record.getQualifier();
        Schema mapped = getMappedSchema(scope, udt.getSchema());
        StringBuilder sb = new StringBuilder();

        if (mapped != null && !"".equals(mapped.getName()))
            sb.append(mapped.getName()).append('.');






        sb.append(record.getQualifier().getName());





        return sb.toString();
    }



































































































    static final RecordQualifier<?> getRecordQualifier(DataType<?> t) {
        return getRecordQualifier(t.getType());
    }

    static final RecordQualifier<?> getRecordQualifier(Class<?> t) {
        try {
            return ((QualifiedRecord<?>) t.getDeclaredConstructor().newInstance()).getQualifier();
        }
        catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }



























    static final Lazy<Configuration> CONFIG          = Lazy.of(() -> new DefaultConfiguration());
    static final Lazy<Configuration> CONFIG_UNQUOTED = Lazy.of(() -> {
        DefaultConfiguration c = new DefaultConfiguration();
        c.settings().setRenderQuotedNames(RenderQuotedNames.NEVER);
        return c;
    });

    static final Lazy<DSLContext>    CTX             = Lazy.of(() -> DSL.using(CONFIG.get()));

    /**
     * A possibly inefficient but stable way to generate an alias for any
     * {@link QueryPart}.
     * <p>
     * Stability is important to profit from execution plan caching. Equal query
     * parts must produce the same alias every time.
     */
    static final String autoAlias(QueryPart part) {
        return "alias_" + Internal.hash(part);
    }

    /**
     * A possibly inefficient but stable way to generate an alias for any
     * {@link QueryPart}.
     * <p>
     * Stability is important to profit from execution plan caching. Equal query
     * parts must produce the same alias every time.
     */
    static final Name autoAliasName(QueryPart part) {
        return DSL.name(autoAlias(part));
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
        if (nullSafeDataType(field).isString()) {







            {
                return escape((Field<String>) field, ESCAPE);
            }
        }
        else {
            return castIfNeeded(field, String.class);
        }
    }

    static final boolean isParam(Field<?> field) {
        return field instanceof Param;
    }

    static final boolean isParamOrCastParam(Field<?> field) {
        return field instanceof Param
            || field instanceof Cast && isParamOrCastParam(((Cast<?>) field).$field());
    }

    static final boolean isVal(Field<?> field) {
        return field instanceof Val
            || field instanceof ConvertedVal && ((ConvertedVal<?>) field).delegate instanceof Val;
    }

    static final boolean isWindow(QueryPart part) {
        return part instanceof AbstractWindowFunction && ((AbstractWindowFunction<?>) part).isWindow();
    }

    static final boolean isSimple(Context<?> ctx, QueryPart part) {
        return part instanceof SimpleQueryPart
            || part instanceof SimpleCheckQueryPart && ((SimpleCheckQueryPart) part).isSimple(ctx);
    }

    static final boolean isSimple(Context<?> ctx, QueryPart... parts) {
        for (QueryPart part : parts)
            if (!isSimple(ctx, part))
                return false;

        return true;
    }

    static final boolean isRendersSeparator(QueryPart part) {
        return part instanceof SeparatedQueryPart && ((SeparatedQueryPart) part).rendersSeparator();
    }

    static final boolean isPossiblyNullable(Field<?> f) {
        return f instanceof AbstractField && ((AbstractField<?>) f).isPossiblyNullable();
    }

    static final Val<?> extractVal(Field<?> field) {
        return field instanceof Val<?> v
             ? v
             : field instanceof ConvertedVal<?> v
             ? (Val<?>) v.delegate
             : null;
    }

    static final boolean hasDefaultConverter(Field<?> field) {
        return field.getConverter() instanceof IdentityConverter;
    }

    static final <T> T extractParamValue(Field<T> field) {
        if (isParam(field))
            return ((Param<T>) field).getValue();
        else
            return null;
    }



































    @SuppressWarnings({ "unchecked", "rawtypes" })
    static final <R extends Record> SelectQueryImpl<R> selectQueryImpl(QueryPart part) {
        if (part instanceof SelectQueryImpl s)
            return s;
        else if (part instanceof SelectImpl s)
            return (SelectQueryImpl<R>) s.getDelegate();
        else if (part instanceof ScalarSubquery<?> s)
            return selectQueryImpl(s.query);
        else if (part instanceof QuantifiedSelectImpl<?> s)
            return selectQueryImpl(s.query);
        else
            return null;
    }

    static final AbstractResultQuery<?> abstractResultQuery(Query query) {
        if (query instanceof AbstractResultQuery<?> q)
            return q;
        else if (query instanceof AbstractDelegatingQuery<?, ?> q)
            return abstractResultQuery(q.getDelegate());
        else
            return null;
    }

    static final InsertQueryImpl<?> insertQueryImpl(Query query) {
        AbstractDMLQuery<?> result = abstractDMLQuery(query);

        if (result instanceof InsertQueryImpl<?> q)
            return q;
        else
            return null;
    }

    static final UpdateQueryImpl<?> updateQueryImpl(Query query) {
        AbstractDMLQuery<?> result = abstractDMLQuery(query);

        if (result instanceof UpdateQueryImpl<?> q)
            return q;
        else
            return null;
    }

    static final DeleteQueryImpl<?> deleteQueryImpl(Query query) {
        AbstractDMLQuery<?> result = abstractDMLQuery(query);

        if (result instanceof DeleteQueryImpl<?> q)
            return q;
        else
            return null;
    }

    static final AbstractDMLQuery<?> abstractDMLQuery(Query query) {
        if (query instanceof AbstractDMLQuery<?> q)
            return q;
        else if (query instanceof AbstractDelegatingDMLQuery<?, ?> q)
            return abstractDMLQuery(q.getDelegate());
        else if (query instanceof AbstractDMLQueryAsResultQuery<?, ?> q)
            return q.getDelegate();
        else
            return null;
    }

    static final int degree(ResultQuery<?> query) {







        return query.types().length;
    }

    static final List<DataType<?>> dataTypes(ResultQuery<?> query) {











        return asList(query.dataTypes());
    }

    static final DataType<?> scalarType(ResultQuery<?> query) {
        List<DataType<?>> list = dataTypes(query);

        if (list.size() != 1)
            throw new IllegalStateException("Only single-column selects have a scalar type");

        return list.get(0);
    }

    /**
     * Add primary key conditions to a query
     */
    static final void addConditions(org.jooq.ConditionProvider query, Record record, Field<?>... keys) {
        for (Field<?> field : keys)
            addCondition(query, record, field);
    }

    /**
     * Add a field condition to a query
     */
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
    // XXX: Reflection utilities used for POJO mapping
    // ------------------------------------------------------------------------

    /**
     * Check if JPA classes can be loaded. This is only done once per JVM!
     */
    static final JPANamespace jpaNamespace() {
        if (jpaNamespace == null) {
            synchronized (initLock) {
                if (jpaNamespace == null) {
                    try {
                        Class.forName(Column.class.getName());
                        jpaNamespace = JPANamespace.JAKARTA;
                    }
                    catch (Throwable e) {
                        try {

                            // [#14180] Break the maven-bundle-plugin class analyser, to prevent
                            //          adding a package import to MANIFEST.MF for this lookup
                            Class.forName(new String("javax.persistence.") + new String("Column"));
                            jpaNamespace = JPANamespace.JAVAX;
                            JooqLogger.getLogger(Tools.class, "isJPAAvailable", 1).info("javax.persistence.Column was found on the classpath instead of jakarta.persistence.Column. jOOQ 3.16 requires you to upgrade to Jakarta EE if you wish to use JPA annotations in your DefaultRecordMapper");
                        }
                        catch (Throwable ignore) {
                            jpaNamespace = JPANamespace.NONE;
                        }
                    }
                }
            }
        }

        return jpaNamespace;
    }

    enum JPANamespace {
        JAVAX, JAKARTA, NONE
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
                        ktJvmClassMapping = Reflect.onClass("kotlin.jvm.JvmClassMappingKt");
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
                        ktKClasses = Reflect.onClass("kotlin.reflect.full.KClasses");
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
                        ktKClass = Reflect.onClass("kotlin.reflect.KClass");
                    }
                    catch (ReflectException ignore) {}
                }
            }
        }

        return ktKClass;
    }

    static final Reflect ktKTypeParameter() {
        if (ktKTypeParameter == null) {
            synchronized (initLock) {
                if (ktKTypeParameter == null) {
                    try {
                        ktKTypeParameter = Reflect.onClass("kotlin.reflect.KTypeParameter");
                    }
                    catch (ReflectException ignore) {}
                }
            }
        }

        return ktKTypeParameter;
    }

    /**
     * Check whether <code>type</code> has any {@link Column} annotated members
     * or methods
     */
    static final boolean hasColumnAnnotations(final Configuration configuration, final Class<?> type) {
        return Cache.run(configuration, () -> {
            switch (Tools.jpaNamespace()) {
                case JAVAX:
                    if (anyMatch(type.getAnnotations(), isJavaxPersistenceAnnotation()))
                        JooqLogger.getLogger(Tools.class, "hasColumnAnnotations", 1).warn("Type " + type + " is annotated with javax.persistence annotation for usage in DefaultRecordMapper, but starting from jOOQ 3.16, only JakartaEE annotations are supported.");

                    if (anyMatch(map(type.getMethods(), m -> anyMatch(m.getAnnotations(), isJavaxPersistenceAnnotation())), b -> b))
                        JooqLogger.getLogger(Tools.class, "hasColumnAnnotations", 1).warn("Type " + type + " has methods annotated with javax.persistence annotation for usage in DefaultRecordMapper, but starting from jOOQ 3.16, only JakartaEE annotations are supported.");

                    if (anyMatch(map(type.getDeclaredMethods(), m -> anyMatch(m.getAnnotations(), isJavaxPersistenceAnnotation())), b -> b))
                        JooqLogger.getLogger(Tools.class, "hasColumnAnnotations", 1).warn("Type " + type + " has methods annotated with javax.persistence annotation for usage in DefaultRecordMapper, but starting from jOOQ 3.16, only JakartaEE annotations are supported.");

                    if (anyMatch(map(type.getFields(), f -> anyMatch(f.getAnnotations(), isJavaxPersistenceAnnotation())), b -> b))
                        JooqLogger.getLogger(Tools.class, "hasColumnAnnotations", 1).warn("Type " + type + " has fields annotated with javax.persistence annotation for usage in DefaultRecordMapper, but starting from jOOQ 3.16, only JakartaEE annotations are supported.");

                    if (anyMatch(map(type.getDeclaredFields(), f -> anyMatch(f.getAnnotations(), isJavaxPersistenceAnnotation())), b -> b))
                        JooqLogger.getLogger(Tools.class, "hasColumnAnnotations", 1).warn("Type " + type + " has fields annotated with javax.persistence annotation for usage in DefaultRecordMapper, but starting from jOOQ 3.16, only JakartaEE annotations are supported.");

                    return false;

                case JAKARTA:

                    // An @Entity or @Table usually has @Column annotations, too
                    if (type.getAnnotation(Entity.class) != null)
                        return true;

                    if (type.getAnnotation(jakarta.persistence.Table.class) != null)
                        return true;

                    if (anyMatch(getInstanceMembers(type), m ->
                            m.getAnnotation(Column.class) != null
                         || m.getAnnotation(Id.class) != null))
                        return true;
                    else
                        return anyMatch(getInstanceMethods(type), m -> m.getAnnotation(Column.class) != null);

                case NONE:
                default:
                    return false;
            }

        }, REFLECTION_CACHE_HAS_COLUMN_ANNOTATIONS, () -> type);
    }

    private static final ThrowingPredicate<? super Annotation, RuntimeException> isJavaxPersistenceAnnotation() {
        return a -> a.annotationType().getName().startsWith("javax.persistence.");
    }

    static final <T extends AccessibleObject> T accessible(T object, boolean makeAccessible) {
        return makeAccessible ? Reflect.accessible(object) : object;
    }

    /**
     * Get all members annotated with a given column name
     */
    static final List<java.lang.reflect.Field> getAnnotatedMembers(
        final Configuration configuration,
        final Class<?> type,
        final String name,
        final boolean makeAccessible
    ) {
        return Cache.run(configuration, () -> {
            List<java.lang.reflect.Field> result = new ArrayList<>();

            for (java.lang.reflect.Field member : getInstanceMembers(type)) {
                Column column = member.getAnnotation(Column.class);

                if (column != null) {
                    if (namesMatch(name, column.name()))
                        result.add(accessible(member, makeAccessible));
                }

                else {
                    Id id = member.getAnnotation(Id.class);

                    if (id != null)
                        if (namesMatch(name, member.getName()))
                            result.add(accessible(member, makeAccessible));
                }
            }

            return result;
        }, REFLECTION_CACHE_GET_ANNOTATED_MEMBERS, () -> Cache.key(type, name));
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
    static final List<java.lang.reflect.Field> getMatchingMembers(
        final Configuration configuration,
        final Class<?> type,
        final String name,
        final boolean makeAccessible
    ) {
        return Cache.run(configuration, () -> {
            List<java.lang.reflect.Field> result = new ArrayList<>();

            // [#1942] Caching these values before the field-loop significantly
            // accerates POJO mapping
            String camelCaseLC = StringUtils.toCamelCaseLC(name);

            for (java.lang.reflect.Field member : getInstanceMembers(type))
                if (name.equals(member.getName()))
                    result.add(accessible(member, makeAccessible));
                else if (camelCaseLC.equals(member.getName()))
                    result.add(accessible(member, makeAccessible));

            return result;
        }, REFLECTION_CACHE_GET_MATCHING_MEMBERS, () -> Cache.key(type, name));
    }

    /**
     * Get all setter methods annotated with a given column name
     */
    static final List<Method> getAnnotatedSetters(
        final Configuration configuration,
        final Class<?> type,
        final String name,
        final boolean makeAccessible
    ) {
        return Cache.run(configuration, () -> {
            Set<SourceMethod> set = new LinkedHashSet<>();

            for (Method method : getInstanceMethods(type)) {
                Column column = method.getAnnotation(Column.class);

                if (column != null && namesMatch(name, column.name())) {

                    // Annotated setter
                    if (method.getParameterTypes().length == 1) {
                        set.add(new SourceMethod(accessible(method, makeAccessible)));
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
                                    set.add(new SourceMethod(accessible(setter, makeAccessible)));
                            }
                            catch (NoSuchMethodException ignore) {}
                        }
                    }
                }
            }

            return SourceMethod.methods(set);
        }, REFLECTION_CACHE_GET_ANNOTATED_SETTERS, () -> Cache.key(type, name));
    }

    /**
     * Get the first getter method annotated with a given column name
     */
    static final Method getAnnotatedGetter(
        final Configuration configuration,
        final Class<?> type,
        final String name,
        final boolean makeAccessible
    ) {
        return Cache.run(configuration, () -> {
            for (Method method : getInstanceMethods(type)) {
                Column column = method.getAnnotation(Column.class);

                if (column != null && namesMatch(name, column.name())) {

                    // Annotated getter
                    if (method.getParameterTypes().length == 0) {
                        return accessible(method, makeAccessible);
                    }

                    // Annotated setter with matching getter
                    else if (method.getParameterTypes().length == 1) {
                        String m = method.getName();

                        if (m.startsWith("set")) {
                            try {
                                Method getter1 = type.getMethod("get" + m.substring(3));

                                // Getter annotation is more relevant
                                if (getter1.getAnnotation(Column.class) == null)
                                    return accessible(getter1, makeAccessible);
                            }
                            catch (NoSuchMethodException ignore1) {}

                            try {
                                Method getter2 = type.getMethod("is" + m.substring(3));

                                // Getter annotation is more relevant
                                if (getter2.getAnnotation(Column.class) == null)
                                    return accessible(getter2, makeAccessible);
                            }
                            catch (NoSuchMethodException ignore2) {}
                        }
                    }
                }
            }

            return null;
        }, REFLECTION_CACHE_GET_ANNOTATED_GETTER, () -> Cache.key(type, name));
    }

    /**
     * Get all setter methods matching a given column name
     */
    static final List<Method> getMatchingSetters(
        final Configuration configuration,
        final Class<?> type,
        final String name,
        final boolean makeAccessible
    ) {
        return Cache.run(configuration, () -> {

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
                        set.add(new SourceMethod(accessible(method, makeAccessible)));
                    else if (camelCaseLC.equals(method.getName()))
                        set.add(new SourceMethod(accessible(method, makeAccessible)));
                    else if (("set" + name).equals(method.getName()))
                        set.add(new SourceMethod(accessible(method, makeAccessible)));
                    else if (("set" + camelCase).equals(method.getName()))
                        set.add(new SourceMethod(accessible(method, makeAccessible)));
            }

            return SourceMethod.methods(set);
        }, REFLECTION_CACHE_GET_MATCHING_SETTERS, () -> Cache.key(type, name));
    }


    /**
     * Get the first getter method matching a given column name
     */
    static final Method getMatchingGetter(
        final Configuration configuration,
        final Class<?> type,
        final String name,
        final boolean makeAccessible
    ) {
        return Cache.run(configuration, () -> {
            // [#1942] Caching these values before the method-loop significantly
            // accerates POJO mapping
            String camelCase = StringUtils.toCamelCase(name);
            String camelCaseLC = StringUtils.toLC(camelCase);

            for (Method method : getInstanceMethods(type))
                if (method.getParameterTypes().length == 0)
                    if (name.equals(method.getName()))
                        return accessible(method, makeAccessible);
                    else if (camelCaseLC.equals(method.getName()))
                        return accessible(method, makeAccessible);
                    else if (("get" + name).equals(method.getName()))
                        return accessible(method, makeAccessible);
                    else if (("get" + camelCase).equals(method.getName()))
                        return accessible(method, makeAccessible);
                    else if (("is" + name).equals(method.getName()))
                        return accessible(method, makeAccessible);
                    else if (("is" + camelCase).equals(method.getName()))
                        return accessible(method, makeAccessible);

            return null;
        }, REFLECTION_CACHE_GET_MATCHING_GETTER, () -> Cache.key(type, name));
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
            return map(methods, s -> s.method);
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
            if (obj instanceof SourceMethod s) {
                Method other = s.method;

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

        do
            for (Method method : type.getDeclaredMethods())
                if ((method.getModifiers() & Modifier.STATIC) == 0)
                    result.add(method);
        while ((type = type.getSuperclass()) != null);

        return result;
    }

    private static final List<java.lang.reflect.Field> getInstanceMembers(Class<?> type) {
        List<java.lang.reflect.Field> result = new ArrayList<>();

        for (java.lang.reflect.Field field : type.getFields())
            if ((field.getModifiers() & Modifier.STATIC) == 0)
                result.add(field);

        do
            for (java.lang.reflect.Field field : type.getDeclaredFields())
                if ((field.getModifiers() & Modifier.STATIC) == 0)
                    result.add(field);
        while ((type = type.getSuperclass()) != null);

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
     * correctly calling {@link PreparedStatement#execute()}}.
     */
    static final SQLException executeStatementAndGetFirstResultSet(ExecuteContext ctx, int skipUpdateCounts) throws SQLException {
        PreparedStatement stmt = ctx.statement();

        try {













































            // [#5764] [#11243]
            // Statement batches (or triggers) may produce unexpected update
            // counts, which we have to fetch first, prior to accessing the
            // first ResultSet. Unexpected result sets could be produced as
            // well, but it's much harder to skip them.
            if (skipUpdateCounts > 0) {

                fetchLoop:
                for (int i = 0; i < maxConsumedResults; i++) {
                    boolean result = (i == 0)
                        ? stmt.execute()
                        : stmt.getMoreResults();

                    // The first ResultSet
                    if (result) {
                        ctx.resultSet(stmt.getResultSet());
                        break fetchLoop;
                    }

                    // Some DML statement whose results we want to skip
                    else {
                        int updateCount = stmt.getUpdateCount();

                        // Store the first update count, in case
                        if (i == 0) {
                            ctx.resultSet(null);
                            ctx.rows(updateCount);
                        }

                        if (updateCount == -1 || skipUpdateCounts-- == 0)
                            break fetchLoop;
                    }
                }
            }

            // [#1232] Avoid executeQuery() in order to handle queries that may
            // not return a ResultSet, e.g. SQLite's pragma foreign_key_list(table)
            else if (stmt.execute()) {
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
        int i;
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
            log.warn("Maximum consumed results reached: " + maxConsumedResults + ". This is probably a bug. Please report to https://jooq.org/bug");

        // Call this only when there was at least one ResultSet.
        if (anyResults) {







            ctx.statement().getMoreResults(Statement.CLOSE_ALL_RESULTS);
        }

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

        for (int j = dataLineStart; j < dataLineEnd; j++)
            parseTXTLine(positions, result, strings[j], nullLiteral);

        return result;
    }

    private static final void parseTXTLine(List<int[]> positions, List<String[]> result, String string, String nullLiteral) {
        String[] fields = new String[positions.size()];
        result.add(fields);
        int length = string.length();

        for (int i = 0; i < fields.length; i++) {
            int[] position = positions.get(i);

            if (position[0] < length)
                fields[i] = string.substring(position[0], Math.min(position[1], length)).trim();
            else
                fields[i] = null;

            if (StringUtils.equals(fields[i], nullLiteral))
                fields[i] = null;
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
     * Wrap a runnable in a <code>BEGIN / END</code> anonymous block.
     */
    static final void begin(Context<?> ctx, Consumer<? super Context<?>> runnable) {
        begin(ctx);
        runnable.accept(ctx);
        end(ctx);
    }

    /**
     * Generate the <code>BEGIN</code> part of an anonymous procedural block.
     */
    private static final void begin(Context<?> ctx) {
        switch (ctx.family()) {












            case FIREBIRD:
                ctx.visit(K_EXECUTE_BLOCK).formatSeparator()
                   .visit(K_AS).formatSeparator()
                   .visit(K_BEGIN).formatIndentStart().formatSeparator();
                break;

            case MARIADB:
                ctx.visit(K_BEGIN).sql(' ').visit(K_NOT).sql(' ').visit(K_ATOMIC).formatIndentStart().formatSeparator();
                break;


            case POSTGRES:
            case YUGABYTEDB:
                if (increment(ctx.data(), DATA_BLOCK_NESTING))
                    ctx.visit(K_DO).sql(" $").sql(ctx.settings().getRenderDollarQuotedStringToken()).sql('$').formatSeparator();

                ctx.visit(K_BEGIN).formatIndentStart().formatSeparator();
                break;
        }
    }

    /**
     * Generate the <code>END</code> part of an anonymous procedural block.
     */
    private static final void end(Context<?> ctx) {
        switch (ctx.family()) {










            case FIREBIRD:
            case MARIADB:
                ctx.formatIndentEnd().formatSeparator()
                   .visit(K_END);
                break;


            case POSTGRES:
            case YUGABYTEDB:
                ctx.formatIndentEnd().formatSeparator()
                   .visit(K_END);

                if (decrement(ctx.data(), DATA_BLOCK_NESTING))
                    ctx.sql(" $").sql(ctx.settings().getRenderDollarQuotedStringToken()).sql('$');

                break;
        }
    }

    /**
     * Wrap a statement in an <code>EXECUTE IMMEDIATE</code> statement.
     */
    static final void executeImmediateIf(boolean wrap, Context<?> ctx, Consumer<? super Context<?>> runnable) {
        if (wrap) {
            executeImmediate(ctx, runnable);
        }
        else {
            runnable.accept(ctx);
            ctx.sql(';');
        }
    }

    /**
     * Wrap a statement in an <code>EXECUTE IMMEDIATE</code> statement.
     */
    static final void executeImmediate(Context<?> ctx, Consumer<? super Context<?>> runnable) {
        beginExecuteImmediate(ctx);
        runnable.accept(ctx);
        endExecuteImmediate(ctx);
    }

    /**
     * Wrap a statement in an <code>EXECUTE IMMEDIATE</code> statement.
     */
    static final void beginExecuteImmediate(Context<?> ctx) {
        switch (ctx.family()) {








            case FIREBIRD:
                ctx.visit(K_EXECUTE_STATEMENT).sql(" '").stringLiteral(true).formatIndentStart().formatSeparator();
                break;
        }
    }

    /**
     * Wrap a statement in an <code>EXECUTE IMMEDIATE</code> statement.
     */
    static final void endExecuteImmediate(Context<?> ctx) {
        ctx.formatIndentEnd().formatSeparator().stringLiteral(false).sql("';");
    }

    /**
     * Wrap a <code>DROP … IF EXISTS</code> statement with
     * <code>BEGIN EXECUTE IMMEDIATE '…' EXCEPTION WHEN … END;</code>, if
     * <code>IF EXISTS</code> is not supported.
     */
    static final void tryCatch(Context<?> ctx, DDLStatementType type, Consumer<? super Context<?>> runnable) {
        tryCatch(ctx, type, null, null, runnable);
    }

    static final void tryCatch(Context<?> ctx, DDLStatementType type, Boolean container, Boolean element, Consumer<? super Context<?>> runnable) {
        switch (ctx.family()) {











































































































































































































































































































            case FIREBIRD: {
                begin(ctx, c -> {
                    executeImmediate(c, runnable);

                    c.formatSeparator()
                     .visit(K_WHEN).sql(" sqlcode -607 ").visit(K_DO).formatIndentStart().formatSeparator()
                     .visit(K_BEGIN).sql(' ').visit(K_END).formatIndentEnd();
                });
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

                begin(ctx, c -> {
                    for (String sqlstate : sqlstates)
                        c.visit(keyword("declare continue handler for sqlstate")).sql(' ').visit(DSL.inline(sqlstate)).sql(' ').visit(K_BEGIN).sql(' ').visit(K_END).sql(';').formatSeparator();

                    runnable.accept(c);
                    c.sql(';');
                });
                break;
            }


            case POSTGRES:
            case YUGABYTEDB: {
                begin(ctx, c -> {
                    String sqlstate;

                    switch (type) {
                        case ALTER_DATABASE: sqlstate = "3D000"; break;
                        case ALTER_DOMAIN  : sqlstate = "42704"; break;
                        case CREATE_DOMAIN : sqlstate = "42710"; break;
                        default            : sqlstate = "42P07"; break;
                    }

                    runnable.accept(c);

                    c.sql(';').formatIndentEnd().formatSeparator()
                     .visit(K_EXCEPTION).formatIndentStart().formatSeparator()
                     .visit(K_WHEN).sql(' ').visit(K_SQLSTATE).sql(' ').visit(DSL.inline(sqlstate)).sql(' ').visit(K_THEN).sql(' ').visit(K_NULL).sql(';').formatIndentEnd();
                });
                break;
            }

            default:
                runnable.accept(ctx);
                break;
        }
    }

    static final void toSQLDDLTypeDeclarationForAddition(Context<?> ctx, DataType<?> type) {
        boolean qualify = ctx.qualify();
        toSQLDDLTypeDeclaration(ctx, type);

        // [#15048] While qualified type declarations are supported, we can't
        //          have qualified field references elsewhere, e.g. in computed
        //          column declarations.
        ctx.qualify(false);
        toSQLDDLTypeDeclarationIdentityBeforeNull(ctx, type);




        // [#5356] Some dialects require the DEFAULT clause prior to the
        //         NULL constraints clause
        if (DEFAULT_BEFORE_NULL.contains(ctx.dialect()))
            toSQLDDLTypeDeclarationDefault(ctx, type);






        toSQLDDLTypeDeclarationForAdditionNullability(ctx, type);

        if (!DEFAULT_BEFORE_NULL.contains(ctx.dialect()))
            toSQLDDLTypeDeclarationDefault(ctx, type);

        toSQLDDLTypeDeclarationIdentityAfterNull(ctx, type);




        ctx.qualify(qualify);
    }

    private static final void toSQLDDLTypeDeclarationForAdditionNullability(Context<?> ctx, DataType<?> type) {
        switch (type.nullability()) {
            case NOT_NULL:

                // [#11485] Some dialects don't support NOT NULL constraints!
                if (!NO_SUPPORT_NOT_NULL.contains(ctx.dialect()))
                    ctx.sql(' ').visit(K_NOT_NULL);

                break;

            case NULL:

                // [#3400] [#4321] [#7392] [#10819] E.g. Derby, Firebird, HSQLDB do not support explicit nullability.
                if (!NO_SUPPORT_NULL.contains(ctx.dialect()))
                    ctx.sql(' ').visit(K_NULL);

                break;

            // [#10937] The behaviour has been re-defined via Settings.renderDefaultNullability
            case DEFAULT:
                RenderDefaultNullability nullability = StringUtils.defaultIfNull(ctx.settings().getRenderDefaultNullability(), IMPLICIT_NULL);

                switch (nullability) {
                    case EXPLICIT_NULL:
                        if (!NO_SUPPORT_NULL.contains(ctx.dialect()))
                            ctx.sql(' ').visit(K_NULL);

                        break;

                    case IMPLICIT_DEFAULT:
                        break;

                    case IMPLICIT_NULL:






                        // [#10937] MariaDB applies NOT NULL DEFAULT CURRENT_TIMESTAMP on
                        //          TIMESTAMP columns in the absence of explicit nullability
                        if (DEFAULT_TIMESTAMP_NOT_NULL.contains(ctx.dialect()) && type.isTimestamp())
                            ctx.sql(' ').visit(K_NULL);

                        break;

                    default:
                        throw new UnsupportedOperationException("Nullability not supported: " + nullability);
                }
                break;

            default:
                throw new UnsupportedOperationException("Nullability not supported: " + type.nullability());
        }
    }

    private static final Set<SQLDialect> REQUIRE_IDENTITY_AFTER_NULL = SQLDialect.supportedBy(H2, MARIADB, MYSQL);
    private static final Set<SQLDialect> SUPPORT_PG_IDENTITY         = SQLDialect.supportedBy(POSTGRES);

    /**
     * If a type is an identity type, some dialects require the relevant
     * keywords before the [ NOT ] NULL constraint.
     */
    static final void toSQLDDLTypeDeclarationIdentityBeforeNull(Context<?> ctx, DataType<?> type) {
        if (REQUIRE_IDENTITY_AFTER_NULL.contains(ctx.dialect()))
            return;

        if (type.identity()) {
            switch (ctx.family()) {









                case CUBRID:    ctx.sql(' ').visit(K_AUTO_INCREMENT); break;

                case HSQLDB:    ctx.sql(' ').visit(K_GENERATED).sql(' ').visit(K_BY).sql(' ').visit(K_DEFAULT).sql(' ').visit(K_AS).sql(' ').visit(K_IDENTITY).sql('(').visit(K_START_WITH).sql(" 1)"); break;
                case SQLITE:    ctx.sql(' ').visit(K_PRIMARY_KEY).sql(' ').visit(K_AUTOINCREMENT); break;
                case POSTGRES:
                    if (SUPPORT_PG_IDENTITY.contains(ctx.dialect()))
                        ctx.sql(' ').visit(K_GENERATED).sql(' ').visit(K_BY).sql(' ').visit(K_DEFAULT).sql(' ').visit(K_AS).sql(' ').visit(K_IDENTITY);

                    break;


                case DERBY:
                case FIREBIRD:
                case YUGABYTEDB: ctx.sql(' ').visit(K_GENERATED).sql(' ').visit(K_BY).sql(' ').visit(K_DEFAULT).sql(' ').visit(K_AS).sql(' ').visit(K_IDENTITY); break;
            }
        }
    }

    /**
     * If a type is an identity type, some dialects require the relevant
     * keywords after the [ NOT ] NULL constraint.
     */
    static final void toSQLDDLTypeDeclarationIdentityAfterNull(Context<?> ctx, DataType<?> type) {
        if (!REQUIRE_IDENTITY_AFTER_NULL.contains(ctx.dialect()))
            return;

        if (type.identity()) {

            // [#5062] H2's (and others') AUTO_INCREMENT flag is syntactically located *after* NULL flags.
            switch (ctx.family()) {



                case H2:     ctx.sql(' ').visit(K_GENERATED).sql(' ').visit(K_BY).sql(' ').visit(K_DEFAULT).sql(' ').visit(K_AS).sql(' ').visit(K_IDENTITY); break;






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
        // [#10376] TODO: Move some of this logic into DataType






        toSQLDDLTypeDeclaration0(ctx, type);
    }


    static final void toSQLDDLTypeDeclaration0(Context<?> ctx, DataType<?> type) {
        DataType<?> elementType = type.getArrayBaseDataType();

        // In some databases, identity is a type, not a flag.
        if (type.identity()) {
            switch (ctx.family()) {






























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
                    for (EnumType e : enumConstants(enumType)) {
                        ctx.sql(separator).visit(DSL.inline(e.getLiteral()));
                        separator = ", ";
                    }

                    ctx.sql(')');
                    return;
                }

                // [#7597] In PostgreSQL, the enum type reference should be used


                case POSTGRES:
                case YUGABYTEDB: {

                    // [#7597] but only if the EnumType.getSchema() value is present
                    //         i.e. when it is a known, stored enum type
                    if (!storedEnumType(enumType))
                        type = emulateEnumType(enumType);

                    break;
                }

                default: {
                    type = emulateEnumType(enumType);
                    break;
                }
            }
        }

        // [#5807] These databases cannot use the DataType.getCastTypeName() (which is simply char in this case)
        if (type.getType() == UUID.class && NO_SUPPORT_CAST_TYPE_IN_DDL.contains(ctx.dialect())) {
            toSQLDDLTypeDeclaration(ctx, VARCHAR(36));
            return;
        }

        // [#12019] [#12117] If dateAsTimestamp=true is active, we must declare a DATE instead.
        if (type.isTimestamp() && (type.getBinding() instanceof DateAsTimestampBinding || type.getBinding() instanceof LocalDateAsLocalDateTimeBinding))
            type = SQLDataType.DATE;

        String typeName = type.getTypeName(ctx.configuration());







        // [#8070] Make sure VARCHAR(n) ARRAY types are generated as such in HSQLDB
        if (type.hasLength() || elementType.hasLength()) {

            // [#6289] [#7191] Some databases don't support lengths on binary types
            if (type.isBinary() && NO_SUPPORT_BINARY_TYPE_LENGTH.contains(ctx.dialect()))
                ctx.sql(typeName);




            else if (type.length() > 0)
                ctx.sql(typeName).sql('(').sql(type.length()).sql(')');

            // [#6745] [#9473] The DataType.getCastTypeName() cannot be used in some dialects, for DDL
            else if (NO_SUPPORT_CAST_TYPE_IN_DDL.contains(ctx.dialect()))
                if (type.isBinary())
                    ctx.sql(BLOB.getTypeName(ctx.configuration()));
                else
                    ctx.sql(CLOB.getTypeName(ctx.configuration()));

            // Some databases don't allow for length-less VARCHAR, VARBINARY types
            else {
                String castTypeName = type.getCastTypeName(ctx.configuration());

                if (!typeName.equals(castTypeName))
                    ctx.sql(castTypeName);
                else
                    ctx.sql(typeName);
            }
        }
        else if (type.hasPrecision() && type.precision() > 0 && (!type.isTimestamp() || !NO_SUPPORT_TIMESTAMP_PRECISION.contains(ctx.dialect()))) {

            // [#6745] [#9473] The DataType.getCastTypeName() cannot be used in some dialects, for DDL
            if (NO_SUPPORT_CAST_TYPE_IN_DDL.contains(ctx.dialect()))
                if (type.hasScale())
                    ctx.sql(typeName).sql('(').sql(type.precision()).sql(", ").sql(type.scale()).sql(')');
                else
                    ctx.sql(typeName).sql('(').sql(type.precision()).sql(')');




            else
                ctx.sql(type.getCastTypeName(ctx.configuration()));
        }

        // [#6841] SQLite usually recognises int/integer as both meaning the same thing, but not in the
        //         context of an autoincrement column, in case of which explicit "integer" types are required.
        else if (type.identity() && ctx.family() == SQLITE && type.isNumeric())
            ctx.sql("integer");














        // [#15048] User defined types may need quoting, etc.
        else if (type.isOther() && !(type instanceof BuiltInDataType))
            ctx.visit(type.getQualifiedName());
        else
            ctx.sql(typeName);

        // [#8041] Character sets are vendor-specific storage clauses, which we might need to ignore
        if (type.characterSet() != null && ctx.configuration().data("org.jooq.ddl.ignore-storage-clauses") == null)
            ctx.sql(' ').visit(K_CHARACTER_SET).sql(' ').visit(type.characterSet());

        // [#8011] Collations are vendor-specific storage clauses, which we might need to ignore
        if (type.collation() != null && ctx.configuration().data("org.jooq.ddl.ignore-storage-clauses") == null)
            ctx.sql(' ').visit(K_COLLATE).sql(' ').visit(type.collation());
    }

    static final boolean storedEnumType(DataType<EnumType> enumType) {
        return enumConstants(enumType)[0].getSchema() != null;
    }

    private static final EnumType[] enumConstants(DataType<? extends EnumType> type) {
        EnumType[] enums = type.getType().getEnumConstants();

        if (enums == null)
            throw new DataTypeException("EnumType must be a Java enum");

        return enums;
    }

    static final DataType<String> emulateEnumType(DataType<? extends EnumType> type) {
        return emulateEnumType(type, enumConstants(type));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static final DataType<String> emulateEnumType(DataType<? extends EnumType> type, EnumType[] enums) {
        int length = 0;
        for (EnumType e : enums)
            length = Math.max(length, e.getLiteral().length());

        return VARCHAR(length).nullability(type.nullability()).defaultValue((Field) type.defaultValue());
    }

    static final <C extends Context<? extends C>> C prependInline(C ctx, String prepend, Field<?> inline, String append) {
        if (inline instanceof Param<?> p)
            return ctx.visit(DSL.inline(prepend + p.getValue() + append));
        else
            return ctx.visit(DSL.inline(prepend).concat(inline).concat(DSL.inline(append)), ParamType.INLINED);
    }

    static final <C extends Context<? extends C>> C prependSQL(C ctx, Query... queries) {
        return preOrAppendSQL(SimpleDataKey.DATA_PREPEND_SQL, ctx, queries);
    }

    static final <C extends Context<? extends C>> C appendSQL(C ctx, Query... queries) {
        return preOrAppendSQL(SimpleDataKey.DATA_APPEND_SQL, ctx, queries);
    }

    private static final <C extends Context<? extends C>> C preOrAppendSQL(SimpleDataKey key, C ctx, Query... queries) {
        ctx.data().compute(key, (k, v) -> {
            String sql = ctx.dsl().renderInlined(ctx.dsl().queries(queries));

            if (v == null)
                return sql;
            else
                return v + sql;
        });

        return ctx;
    }

    // -------------------------------------------------------------------------
    // XXX: ForkJoinPool ManagedBlock implementation
    // -------------------------------------------------------------------------

    static final <T> Supplier<T> blocking(Supplier<T> supplier) {
        return blocking(supplier, false);
    }

    static final <T> Supplier<T> blocking(Supplier<T> supplier, boolean threadLocal) {

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

    @SuppressWarnings("unchecked")
    static final <E extends EnumType> E[] enums(Class<? extends E> type) {

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
        return t == Time.class || t == LocalTime.class;
    }

    /**
     * Whether a Java type is suitable for {@link Types#TIMESTAMP}.
     */
    static final boolean isTimestamp(Class<?> t) {
        return t == Timestamp.class || t == LocalDateTime.class;
    }

    /**
     * Whether a Java type is suitable for {@link Types#DATE}.
     */
    static final boolean isDate(Class<?> t) {
        return t == Date.class || t == LocalDate.class;
    }

    static final boolean hasAmbiguousNames(Collection<? extends Field<?>> fields) {
        if (fields == null)
            return false;

        Set<String> names = new HashSet<>();
        return anyMatch(fields, f -> !names.add(f.getName()));
    }

    static final SelectFieldOrAsterisk qualify(Table<?> table, SelectFieldOrAsterisk field) {
        if (field instanceof Field<?> f)
            return qualify(table, f);
        else if (field instanceof Asterisk)
            return table.asterisk();
        else if (field instanceof QualifiedAsterisk)
            return table.asterisk();
        // [#11812] TODO: handle field instanceof Row
        else
            throw new UnsupportedOperationException("Unsupported field : " + field);
    }

    static final Field<?>[] qualify(Table<?> table, Field<?>[] fields) {
        return map(fields, f -> qualify(table, f), Field<?>[]::new);
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

    static final <T> Field<T> field(OrderField<T> orderField) {
        if (orderField instanceof Field<T> f)
            return f;
        else
            return ((SortField<T>) orderField).$field();
    }

    static final Field<?>[] fields(OrderField<?>[] orderFields) {
        return map(orderFields, f -> field(f), Field[]::new);
    }

    static final List<Field<?>> fields(Collection<? extends OrderField<?>> orderFields) {
        return map(orderFields, (OrderField<?> f) -> field(f));
    }

    static final <T> Field<T> unalias(Field<T> field) {
        Field<T> result = aliased(field);
        return result != null ? result : field;
    }

    @SuppressWarnings("unchecked")
    static final <T> Field<T> unaliasTable(Field<T> field) {
        if (field instanceof TableField<?, ?> tf) {
            Table<?> t = aliased(tf.getTable());

            // [#14671] Use only the Field::getName for lookups to avoid:
            //          - StackOverflowError
            //          - Otherwise Field or Name related lookup efforts
            if (t != null)
                return (Field<T>) t.field(field.getName());
        }

        return field;
    }

    static final <T> Field<T> aliased(Field<T> field) {
        if (field instanceof FieldAlias<T> f)
            return f.getAliasedField();
        else
            return null;
    }

    static final <R extends Record> Table<R> unalias(Table<R> table) {
        Table<R> result = aliased(table);
        return result != null ? result : table;
    }

    static final TableElement uncollate(TableElement field) {
        if (field instanceof QOM.Collated c)
            return uncollate(c.$field());
        else
            return field;
    }

    static final boolean isScalarSubquery(Field<?> field) {
        // TODO: Replace other instanceof checks by this one
        return uncoerce(field) instanceof ScalarSubquery;
    }

    static final Field<?> uncoerce(Field<?> field) {
        return field instanceof Coerce<?> f ? f.field : field;
    }

    static final <R extends Record> Table<R> unwrap(Table<R> table) {
        return unwrap(table, true);
    }

    static final <R extends Record> Table<R> unwrap(Table<R> table, boolean unalias) {
        Table<R> r = table;

        if (table instanceof AbstractDelegatingTable<R> t)
            return unwrap(t.delegate);
        else if (unalias && (r = unalias(table)) != table)
            return unwrap(r);
        else
            return r;
    }

    static final <R extends Record> Table<R> aliased(Table<R> table) {
        if (table instanceof TableImpl<R> t)
            return t.getAliasedTable();
        else if (table instanceof TableAlias<R> t)
            return t.getAliasedTable();
        else
            return null;
    }

    static final <R extends Record> Alias<Table<R>> alias(Table<R> table) {
        if (table instanceof TableImpl<R> t)
            return t.alias;
        else if (table instanceof TableAlias<R> t)
            return t.alias;
        else
            return null;
    }

    /**
     * Whether a counter is currently at the top level or not.
     *
     * @see #increment(Map, SimpleDataKey)
     * @see #decrement(Map, SimpleDataKey)
     */
    static final boolean toplevel(Map<Object, Object> data, SimpleDataKey key) {
        Integer updateCounts = (Integer) data.get(key);

        if (updateCounts == null)
            throw new IllegalStateException();
        else
            return updateCounts.intValue() == 1;
    }

    /**
     * Increment a counter, run a runnable, and decrement the counter again.
     */
    static final void increment(Map<Object, Object> data, SimpleDataKey key, Runnable runnable) {
        increment(data, key);
        runnable.run();
        decrement(data, key);
    }

    /**
     * Increment a counter and return true if the counter was zero prior to
     * incrementing.
     */
    static final boolean increment(Map<Object, Object> data, SimpleDataKey key) {
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
    static final boolean decrement(Map<Object, Object> data, SimpleDataKey key) {
        boolean result = false;
        Integer updateCounts = (Integer) data.get(key);

        if (updateCounts == null || updateCounts == 0)
            throw new IllegalStateException("Unmatching increment / decrement on key: " + key);
        else if (updateCounts == 1)
            result = true;

        data.put(key, updateCounts - 1);
        return result;
    }

    /**
     * Look up a field in a table, or create a new qualified field from the table.
     */
    static final Field<?> tableField(Table<?> table, Object field) {
        if (field instanceof Field<?> f)
            return f;
        else if (field instanceof Name n) {
            if (table.fieldsRow().size() == 0)
                return DSL.field(table.getQualifiedName().append(n.unqualifiedName())) ;
            else
                return table.field(n);
        }
        else if (field instanceof String s) {
            if (table.fieldsRow().size() == 0)
                return DSL.field(table.getQualifiedName().append(s));
            else
                return table.field(s);
        }
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
    static final String convertBytesToHex(byte[] value, int offset, int len) {
        len = Math.min(value.length - offset, len);
        char[] buff = new char[2 * len];
        char[] hex = HEX_DIGITS;

        for (int i = 0; i < len; i++) {
            int c = value[i + offset] & 0xff;
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
        return convertBytesToHex(value, 0, value.length);
    }

    /**
     * Convert a hex encoded string to a byte array.
     *
     * @param value the hex encoded string
     * @param len the number of bytes to encode
     * @return the byte array
     */
    static final byte[] convertHexToBytes(String value, int offset, int len) {
        len = Math.min(value.length() / 2 - offset, len);
        byte[] buff = new byte[len];
        byte[] lookup = HEX_LOOKUP;
        int max = lookup.length;

        for (int i = 0; i < len; i++) {
            int pos = (i + offset) * 2;
            char c1 = value.charAt(pos);
            char c2 = value.charAt(pos + 1);
            byte v1 = c1 < max ? lookup[c1] : 0;
            byte v2 = c2 < max ? lookup[c2] : 0;

            buff[i] = (byte) ((v1 << 4) + v2);
        }

        return buff;
    }

    /**
     * Convert a hex encoded string to a byte array.
     *
     * @param value the hex encoded string
     * @return the byte array
     */
    static final byte[] convertHexToBytes(String value) {
        return convertHexToBytes(value, 0, value.length());
    }

    static final boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    static final boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    static final boolean isNotEmpty(Iterable<?> it) {
        return !isEmpty(it);
    }

    static final boolean isEmpty(Iterable<?> it) {
        if (it == null)
            return true;
        else if (it instanceof Collection<?> c)
            return isEmpty(c);

        Iterator<?> i = it.iterator();
        return !i.hasNext();
    }

    static final boolean isNotEmpty(Object[] array) {
        return !isEmpty(array);
    }

    static final boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    static final boolean nonReplacingEmbeddable(Field<?> field) {
        return field instanceof EmbeddableTableField && !((EmbeddableTableField<?, ?>) field).replacesFields;
    }

    @SuppressWarnings("unchecked")
    static final Class<? extends AbstractRecord> embeddedRecordType(Field<?> field) {
        return field instanceof EmbeddableTableField<?, ?> e
             ? (Class<AbstractRecord>) e.recordType
             : field instanceof Val && ((Val<?>) field).value instanceof EmbeddableRecord
             ? ((AbstractRecord) ((Val<?>) field).value).getClass()
             : field.getDataType().isEmbeddable()
             ? ((Field<AbstractRecord>) field).getType()
             : null;
    }

    @SuppressWarnings("unchecked")
    static final Field<?>[] embeddedFields(Field<?> field) {
        return field instanceof EmbeddableTableField<?, ?> e
             ? e.fields
             : field instanceof Val && ((Val<?>) field).value instanceof EmbeddableRecord
             ? ((EmbeddableRecord<?>) ((Val<?>) field).value).valuesRow().fields()
             : field instanceof ScalarSubquery<?> s
             ? embeddedFields(s)
             : field.getDataType().isEmbeddable()
             ? newInstance(((Field<EmbeddableRecord<?>>) field).getType()).valuesRow().fields()
             : null;
    }

    static final Row embeddedFieldsRow(Row row) {
        if (hasEmbeddedFields(row.fields()))
            return row(map(flattenCollection(Arrays.asList(row.fields())), f -> f));
        else
            return row;
    }

    static final Field<?>[] embeddedFields(ScalarSubquery<?> field) {

        // Split a scalar subquery of degree N into N scalar subqueries of degree 1
        // In a few cases, there's a better solution that prevents the N subqueries,
        // but this is a good default that works in many cases.
        // [#8353] [#10522] [#10523] TODO: Factor out some of this logic and
        // reuse it for the emulation of UPDATE .. SET row = (SELECT ..)
        List<Field<?>> select = field.query.getSelect();
        List<Field<?>> result = collect(flattenCollection(select));
        Name tableName = name("t");
        Name[] fieldNames = fieldNames(result.size());
        Table<?> t = new AliasedSelect<>(field.query, true, true, false, fieldNames).as("t");
        for (int i = 0; i < result.size(); i++)
            result.set(i, DSL.field(DSL.select(DSL.field(tableName.append(fieldNames[i]), result.get(i).getDataType())).from(t)));

        return result.toArray(EMPTY_FIELD);
    }

    private static final EmbeddableRecord<?> newInstance(Class<? extends EmbeddableRecord<?>> type) {
        try {
            return type.getDeclaredConstructor().newInstance();
        }
        catch (Exception e) {
            throw new MappingException("Cannot create EmbeddableRecord type", e);
        }
    }

    static final boolean hasEmbeddedFields(Field<?>[] fields) {
        return anyMatch(fields, f -> f.getDataType().isEmbeddable());
    }

    static final boolean hasEmbeddedFields(Iterable<? extends Field<?>> fields) {
        return anyMatch(fields, f -> f.getDataType().isEmbeddable());
    }

    static final <E> List<E> collect(Iterable<E> iterable) {
        if (iterable instanceof List<E> l)
            return l;

        List<E> result = new ArrayList<>();
        for (E e : iterable)
            result.add(e);

        return result;
    }

    static final <E> Iterator<E> filter(Iterator<E> iterator, Predicate<? super E> predicate) {
        return filter(iterator, (e, i) -> predicate.test(e));
    }

    static final <E> Iterator<E> filter(Iterator<E> iterator, ObjIntPredicate<? super E> predicate) {
        return new Iterator<E>() {
            boolean uptodate;
            E       next;
            int     index;

            private void move() {
                if (!uptodate) {
                    uptodate = true;

                    while (iterator.hasNext()) {
                        next = iterator.next();

                        if (predicate.test(next, index++))
                            return;
                    }

                    uptodate = false;
                }
            }

            @Override
            public boolean hasNext() {
                move();
                return uptodate;
            }

            @Override
            public E next() {
                move();
                if (!uptodate)
                    throw new NoSuchElementException();
                uptodate = false;
                return next;
            }
        };
    }

    static final <E> Iterable<E> filter(Iterable<E> iterable, Predicate<? super E> predicate) {
        return () -> filter(iterable.iterator(), predicate);
    }

    static final <E> Iterable<E> filter(Iterable<E> iterable, ObjIntPredicate<? super E> predicate) {
        return () -> filter(iterable.iterator(), predicate);
    }

    static final <E> Iterable<E> filter(E[] array, Predicate<? super E> predicate) {
        return filter(asList(array), predicate);
    }

    static final <E> Iterable<E> filter(E[] array, ObjIntPredicate<? super E> predicate) {
        return filter(asList(array), predicate);
    }

    static final Iterable<Field<?>> flattenFieldOrRow(FieldOrRow fr) {
        if (fr instanceof Field<?> f)
            return flatten(f);
        else
            return asList(((Row) fr).fields());
    }

    static final <C extends Collection<Field<?>>> C flattenFieldOrRows(Collection<? extends FieldOrRow> frs, C c) {
        for (FieldOrRow fr : frs)
            for (Field<?> f : flattenFieldOrRow(fr))
                c.add(f);

        return c;
    }

    /**
     * Flatten out an {@link EmbeddableTableField}.
     */
    @SuppressWarnings("unchecked")
    static final <E extends Field<?>> Iterable<E> flatten(E field) {

        // [#2530] [#6124] [#10481] TODO: Refactor and optimise these flattening algorithms
        Iterator<E> it1 = singletonList(field).iterator();

        // [#11729] Workaround for https://bugs.eclipse.org/bugs/show_bug.cgi?id=572873
        Iterator<E> it2 = field.getDataType().isEmbeddable()
            ? new FlatteningIterator<E>(it1, (e, duplicates) -> (List<E>) Arrays.asList(embeddedFields(field)))
            : it1;

        return () -> it2;
    }

    /**
     * Flatten out {@link EmbeddableTableField} elements contained in an
     * ordinary iterable.
     */
    static final Iterable<Field<?>> flattenCollection(Iterable<? extends Field<?>> iterable) {
        return flattenCollection(iterable, false, false);
    }

    /**
     * Flatten out {@link EmbeddableTableField} elements contained in an
     * ordinary iterable.
     */
    static final Iterable<Field<?>> flattenCollection(
        Iterable<? extends Field<?>> iterable,
        boolean removeDuplicates,
        boolean flattenRowFields
    ) {
        // [#2530] [#6124] [#10481] TODO: Refactor and optimise these flattening algorithms
        return () -> new FlatteningIterator<>(iterable.iterator(), (e, duplicates) -> {













            // TODO [#10525] Should embedded records be emulated as RowField?
            if (flattenRowFields) {
                if (e instanceof AbstractRowAsField<?> r) {
                    List<Field<?>> result = new ArrayList<>();

                    for (Field<?> field : flattenCollection(asList(r.fields0().fields()), removeDuplicates, flattenRowFields))
                        if (duplicates.test(field))
                            result.add(field);

                    return result;
                }
            }

            return Tools.flatten(e);
        });
    }

    /**
     * Flatten out {@link EmbeddableTableField} elements contained in an entry
     * set iterable, making sure no duplicate keys resulting from overlapping
     * embeddables will be produced.
     */
    static final Iterable<Entry<FieldOrRow, FieldOrRowOrSelect>> flattenEntrySet(
        final Iterable<Entry<FieldOrRow, FieldOrRowOrSelect>> iterable,
        final boolean removeDuplicates
    ) {
        // [#2530] [#6124] [#10481] TODO: Refactor and optimise these flattening algorithms
        // [#11729] Workaround for https://bugs.eclipse.org/bugs/show_bug.cgi?id=572873
        return () -> new FlatteningIterator<>(iterable.iterator(), (e, duplicates) -> {

            // [#9879] [#13325] TODO: Support also UPDATE .. SET ROW = ...
            if (e.getKey() instanceof EmbeddableTableField<?, ?> key) {
                List<Entry<FieldOrRow, FieldOrRowOrSelect>> result = new ArrayList<>();
                Field<?>[] keys = embeddedFields(key);
                Field<?>[] values = embeddedFields((Field<?>) e.getValue());

                for (int i = 0; i < keys.length; i++)



                        result.add(new SimpleImmutableEntry<FieldOrRow, FieldOrRowOrSelect>(
                            keys[i], values[i]
                        ));

                return result;
            }

            return null;
        });
    }

    static final <T> Set<T> lazy(Set<T> set) {
        return set == null ? new HashSet<>() : set;
    }

    static final <T> List<T> lazy(List<T> list) {
        return list == null ? new ArrayList<>() : list;
    }

    static final <T> List<T> lazy(List<T> list, int size) {
        List<T> result = lazy(list);

        if (result.size() < size)
            result.addAll(nCopies(size - result.size(), null));

        return result;
    }

    static final <T> Supplier<T> cached(Supplier<T> s) {
        return new Supplier<T>() {

            // The assumption is that race conditions for the assignment are
            // acceptable because the computation is idempotent, so memory
            // barriers or synchronization isn't necessary.
            transient T cached;

            @Override
            public T get() {
                if (cached == null)
                    cached = s.get();

                return cached;
            }
        };
    }

    /**
     * A base implementation for {@link EmbeddableTableField} flattening
     * iterators with a default implementation for {@link Iterator#remove()} for
     * convenience in the Java 6 build.
     */
    static final class FlatteningIterator<E> implements Iterator<E> {
        private final Iterator<? extends E>                                           delegate;
        private final BiFunction<? super E, Predicate<Object>, ? extends Iterable<E>> flattener;
        private final Predicate<Object>                                               checkDuplicates;
        private Iterator<E>                                                           flatten;
        private E                                                                     next;
        private Set<Object>                                                           duplicates;

        FlatteningIterator(Iterator<? extends E> delegate, BiFunction<? super E, Predicate<Object>, ? extends Iterable<E>> flattener) {
            this.delegate = delegate;
            this.flattener = flattener;
            this.checkDuplicates = e -> (duplicates = lazy(duplicates)).add(e);
        }

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

                    Iterable<E> flattened = flattener.apply(next, checkDuplicates);
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

    static final boolean anyQuoted(Settings settings, Name... names) {
        RenderQuotedNames renderQuotedNames = SettingsTools.getRenderQuotedNames(settings);

        switch (renderQuotedNames) {
            case ALWAYS:
                return true;
            case NEVER:
                return false;
            case EXPLICIT_DEFAULT_QUOTED:
            case EXPLICIT_DEFAULT_UNQUOTED:
                for (Name name : names) {
                    Name n = name.unqualifiedName();

                    switch (n.quoted()) {
                        case QUOTED:
                            return true;
                        case DEFAULT:
                            if (renderQuotedNames == EXPLICIT_DEFAULT_QUOTED)
                                return true;
                            else
                                break;
                    }
                }

                break;
        }

        return false;
    }

    static final String asString(Name name) {
        if (!name.qualified())
            return name.first();

        StringBuilder sb = new StringBuilder();
        Name[] parts = name.parts();
        for (int i = 0; i < parts.length; i++) {
            sb.append(parts[i].first());
            if (i < parts.length - 1)
                sb.append('.');
        }
        return sb.toString();
    }

    /**
     * Normalise a name case depending on the dialect and the setting for
     * {@link ParseNameCase}.
     */
    static final String normaliseNameCase(Configuration configuration, String name, boolean quoted, Locale locale) {
        switch (parseNameCase(configuration)) {
            case LOWER_IF_UNQUOTED:
                if (quoted)
                    return name;

                // no-break

            case LOWER:
                return name.toLowerCase(locale);

            case UPPER_IF_UNQUOTED:
                if (quoted)
                    return name;

                // no-break
            case UPPER:
                return name.toUpperCase(locale);

            case AS_IS:
            case DEFAULT:
            default:
                return name;
        }
    }

    /**
     * Get the {@link ParseNameCase}, looking up the default value from the
     * parse dialect.
     */
    private static final ParseNameCase parseNameCase(Configuration configuration) {
        ParseNameCase result = defaultIfNull(configuration.settings().getParseNameCase(), ParseNameCase.DEFAULT);

        if (result == ParseNameCase.DEFAULT) {
            switch (defaultIfNull(configuration.settings().getParseDialect(), configuration.family()).family()) {









                case POSTGRES:
                case YUGABYTEDB:
                    return ParseNameCase.LOWER_IF_UNQUOTED;







                case DUCKDB:
                case MARIADB:
                case MYSQL:
                case SQLITE:
                case TRINO:
                    return ParseNameCase.AS_IS;

                default:
                    // The SQL standard uses upper case identifiers if unquoted
                    return ParseNameCase.UPPER_IF_UNQUOTED;
            }
        }

        return result;
    }

    static final NestedCollectionEmulation emulateMultiset(Configuration configuration) {
        NestedCollectionEmulation result = defaultIfNull(configuration.settings().getEmulateMultiset(), NestedCollectionEmulation.DEFAULT);

        if (result == NestedCollectionEmulation.DEFAULT) {
            switch (configuration.family()) {



                case POSTGRES:
                case YUGABYTEDB:
                    return NestedCollectionEmulation.JSONB;


                case H2:
                case MARIADB:
                case MYSQL:
                case SQLITE:
                case TRINO:
                    return NestedCollectionEmulation.JSON;




















                default:
                    return NestedCollectionEmulation.NATIVE;
            }
        }

        return result;
    }

    static final DataType<?> dataType(Field<?> field) {
        return dataType(OTHER, field, false);
    }

    @SuppressWarnings("unchecked")
    static final <T> DataType<T> dataType(DataType<T> defaultType, Field<?> field, boolean preferDefault) {
        return field == null
             ? defaultType
             : preferDefault && field.getType() != defaultType.getType()
             ? defaultType.nullable(field.getDataType().nullable())
             : (DataType<T>) field.getDataType();
    }

    static final <T> DataType<T> allNotNull(DataType<T> defaultType) {
        return defaultType.notNull();
    }

    static final <T> DataType<T> allNotNull(DataType<T> defaultType, Field<?> f1) {
        return dataType(defaultType, f1, true);
    }

    static final <T> DataType<T> allNotNull(DataType<T> defaultType, Field<?> f1, Field<?> f2) {
        DataType<T> result = dataType(defaultType, f1, true);

        if (result.nullable())
            return result;
        else if (dataType(f2).nullable())
            return result.null_();
        else
            return result;
    }

    static final <T> DataType<T> allNotNull(DataType<T> defaultType, Field<?> f1, Field<?> f2, Field<?> f3) {
        DataType<T> result = dataType(defaultType, f1, true);

        if (result.nullable())
            return result;
        else if (dataType(f2).nullable())
            return result.null_();
        else if (dataType(f3).nullable())
            return result.null_();
        else
            return result;
    }

    static final <T> DataType<T> allNotNull(DataType<T> defaultType, Field<?>... fields) {
        DataType<T> result = dataType(defaultType, isEmpty(fields) ? null : fields[0], true);

        if (result.nullable())
            return result;
        else
            for (Field<?> field : fields)
                if (dataType(field).nullable())
                    return result.null_();

        return result;
    }

    static final <T> DataType<T> anyNotNull(DataType<T> defaultType, Field<?> f1) {
        return dataType(defaultType, f1, false);
    }

    static final <T> DataType<T> anyNotNull(DataType<T> defaultType, Field<?> f1, Field<?> f2) {
        DataType<T> result = dataType(defaultType, f1, false);

        if (!result.nullable())
            return result;
        else if (!dataType(f2).nullable())
            return result.notNull();
        else
            return result;
    }

    static final <T> DataType<T> anyNotNull(DataType<T> defaultType, Field<?> f1, Field<?> f2, Field<?> f3) {
        DataType<T> result = dataType(defaultType, f1, false);

        if (!result.nullable())
            return result;
        else if (!dataType(f2).nullable())
            return result.notNull();
        else if (!dataType(f3).nullable())
            return result.notNull();
        else
            return result;
    }

    static final <T> DataType<T> anyNotNull(DataType<T> defaultType, Field<?>... fields) {
        DataType<T> result = dataType(defaultType, isEmpty(fields) ? null : fields[0], false);

        if (!result.nullable())
            return result;
        else
            for (Field<?> field : fields)
                if (!dataType(field).nullable())
                    return result.notNull();

        return result;
    }

    static final <T> DataType<T> nullable(DataType<T> defaultType, Field<?> f1) {
        return dataType(defaultType, f1, false).null_();
    }

    static final <T> DataType<T> nullable(DataType<T> defaultType, Field<?> f1, Field<?> f2) {
        return dataType(defaultType, f1, false).null_();
    }

    static final <T> DataType<T> nullable(DataType<T> defaultType, Field<?> f1, Field<?> f2, Field<?> f3) {
        return dataType(defaultType, f1, false).null_();
    }

    static final <T> DataType<T> nullable(DataType<T> defaultType, Field<?>... fields) {
        return dataType(defaultType, isEmpty(fields) ? null : fields[0], false).null_();
    }

    static final <T> Field<T> nullSafe(Field<T> field) {
        return field == null ? DSL.val((T) null) : field;
    }

    @SuppressWarnings("unchecked")
    static final <T> Field<T> nullSafe(Field<T> field, DataType<?> type) {
        return field == null
             ? (Field<T>) DSL.val((T) null, type)
             : field instanceof Condition c
             ? (Field<T>) DSL.field(c)
             : convertVal(field, type);
    }

    /**
     * In very rare cases, we want {@link #nullSafe(Field, DataType)} behaviour
     * but only for <code>null</code> values.
     */
    static final Field<?> nullSafeNoConvertVal(Object field, DataType<?> type) {
        return field == null
             ? DSL.val(null, type)
             : field instanceof Condition c
             ? DSL.field(c)
             : field(field);
    }

    @SuppressWarnings("unchecked")
    static final <T> Field<T> convertVal(Field<T> field, DataType<?> type) {
        return isVal(field)
             ? (Field<T>) extractVal(field).convertTo(type)
             : field;
    }

    static final Field<?>[] nullSafe(Field<?>... fields) {
        if (fields == null)
            return EMPTY_FIELD;

        Field<?>[] result = new Field<?>[fields.length];
        for (int i = 0; i < fields.length; i++)
            result[i] = nullSafe(fields[i]);

        return result;
    }

    static final Field<?>[] nullSafe(Field<?>[] fields, DataType<?> type) {
        if (fields == null)
            return EMPTY_FIELD;

        Field<?>[] result = new Field<?>[fields.length];
        for (int i = 0; i < fields.length; i++)
            result[i] = nullSafe(fields[i], type);

        return result;
    }

    static final List<Field<?>> nullSafeList(Field<?>... fields) {
        if (fields == null)
            return asList(EMPTY_FIELD);
        else
            return map(fields, f -> nullSafe(f));
    }

    static final List<Field<?>> nullSafeList(Field<?>[] fields, DataType<?> type) {
        if (fields == null)
            return asList(EMPTY_FIELD);
        else
            return map(fields, f -> nullSafe(f, type));
    }

    @SuppressWarnings("unchecked")
    static final <T> DataType<T> nullSafeDataType(Field<T> field) {
        return (DataType<T>) (field == null ? SQLDataType.OTHER : field.getDataType());
    }

    @SuppressWarnings("unchecked")
    static final <T> DataType<T> nullSafeDataType(Field<?>[] values) {
        return (DataType<T>) (isEmpty(values) ? SQLDataType.OTHER : values[0].getDataType());
    }

    static final <T> Field<T> nullSafeNotNull(Field<T> field, DataType<?> type) {
        return nullableIf(false, nullSafe(field, type));
    }

    static final Field<?> nullSafeNoConvertValNotNull(Field<?> field, DataType<?> type) {
        return nullableIf(false, nullSafeNoConvertVal(field, type));
    }

    static final <T> Field<T> nullableIf(boolean nullable, Field<T> field) {
        return isVal(field)
             ? extractVal(field).convertTo(field.getDataType().nullable(nullable))
             : field;
    }

    // TODO: In a new expression tree model, we'll support generic visitors of some sort
    // ---------------------------------------------------------------------------------

    static final boolean containsDeclaredTable(Table<?> in, Table<?> search) {
        return traverseJoins(in, false, r -> r, search(search, t -> t));
    }

    static final boolean containsDeclaredTable(Iterable<? extends Table<?>> in, Table<?> search) {
        return traverseJoins(in, false, r -> r, search(search, t -> t));
    }

    private static final BiFunction<Boolean, Table<?>, Boolean> search(Table<?> table, Function<? super Table<?>, ? extends Table<?>> f) {
        Table<?> unaliased = f.apply(table);
        return (r, t) -> r || unaliased.equals(f.apply(t));
    }

    static final boolean containsTable(Table<?> in, Table<?> search, boolean unalias) {

        // [#6304] [#7626] [#14668] Improved alias discovery
        return traverseJoins(in, false, r -> r, search(search, t -> unwrap(t, unalias)));
    }

    static final boolean containsTable(Iterable<? extends Table<?>> in, Table<?> search, boolean unalias) {

        // [#6304] [#7626] [#14668] Improved alias discovery
        return traverseJoins(in, false, r -> r, search(search, t -> unwrap(t, unalias)));
    }

    static final boolean containsUnaliasedTable(Table<?> in, Table<?> search) {

        // [#6304] [#7626] [#14668] Improved alias discovery
        return traverseJoins(in, false, r -> r, search(search, Tools::unwrap));
    }

    static final boolean containsUnaliasedTable(Iterable<? extends Table<?>> in, Table<?> search) {

        // [#6304] [#7626] [#14668] Improved alias discovery
        return traverseJoins(in, false, r -> r, search(search, Tools::unwrap));
    }

    static final void traverseJoins(Iterable<? extends Table<?>> i, Consumer<? super Table<?>> consumer) {
        for (Table<?> t : i)
            traverseJoins(t, consumer);
    }

    static final void traverseJoins(Table<?> t, Consumer<? super Table<?>> consumer) {
        traverseJoins(t, null, null, (result, x) -> { consumer.accept(x); return result; });
    }

    static final <T> T traverseJoins(
        Iterable<? extends Table<?>> i,
        T result,
        Predicate<? super T> abort,
        BiFunction<? super T, ? super Table<?>, ? extends T> function
    ) {
        for (Table<?> t : i)
            if (abort != null && abort.test(result))
                return result;
            else
                result = traverseJoins(t, result, abort, function);

        return result;
    }

    static final <T> T traverseJoins(
        Table<?> t,
        T result,
        Predicate<? super T> abort,
        BiFunction<? super T, ? super Table<?>, ? extends T> function
    ) {
        return traverseJoins(t, result, abort, null, null, null, function);
    }

    static final <T> T traverseJoins(
        Iterable<? extends Table<?>> i,
        T result,
        Predicate<? super T> abort,
        Predicate<? super JoinTable<?>> recurseLhs,
        Predicate<? super JoinTable<?>> recurseRhs,
        BiFunction<? super T, ? super JoinType, ? extends T> joinTypeFunction,
        BiFunction<? super T, ? super Table<?>, ? extends T> tableFunction
    ) {
        for (Table<?> t : i)
            if (abort != null && abort.test(result))
                return result;
            else
                result = traverseJoins(t, result, abort, recurseLhs, recurseRhs, joinTypeFunction, tableFunction);

        return result;
    }

    static final <T> T traverseJoins(
        Table<?> t,
        T result,
        Predicate<? super T> abort,
        Predicate<? super JoinTable<?>> recurseLhs,
        Predicate<? super JoinTable<?>> recurseRhs,
        BiFunction<? super T, ? super JoinType, ? extends T> joinTypeFunction,
        BiFunction<? super T, ? super Table<?>, ? extends T> tableFunction
    ) {
        if (abort != null && abort.test(result))
            return result;

        if (t instanceof JoinTable<?> j) {
            if (recurseLhs == null || recurseLhs.test(j)) {
                result = traverseJoins(j.lhs, result, abort, recurseLhs, recurseRhs, joinTypeFunction, tableFunction);

                if (abort != null && abort.test(result))
                    return result;
            }

            if (joinTypeFunction != null) {
                result = joinTypeFunction.apply(result, j.type);

                if (abort != null && abort.test(result))
                    return result;
            }

            if (recurseRhs == null || recurseRhs.test(j))
                result = traverseJoins(j.rhs, result, abort, recurseLhs, recurseRhs, joinTypeFunction, tableFunction);
        }
        else if (tableFunction != null)
            result = tableFunction.apply(result, t);

        return result;
    }

    static final String capitalize(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    static final String decapitalize(String string) {
        return string.substring(0, 1).toLowerCase() + string.substring(1);
    }

    static final String pascalCase(String snakeCase) {
        return Stream.of(snakeCase.toLowerCase().split("_")).map(Tools::capitalize).collect(joining());
    }

    static final String camelCase(String snakeCase) {
        return decapitalize(pascalCase(snakeCase));
    }

    static final String characterLiteral(char character) {
        return "'" + ("" + character).replace("\\", "\\\\").replace("'", "\\'") + "'";
    }

    static final String stringLiteral(String string) {
        return "\"" + string.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n\" + \n\"") + "\"";
    }

    /**
     * Access to the JDK 9 {@link Matcher#replaceAll(Function)} function.
     */
    static final String replaceAll(String string, Matcher matcher, Function<MatchResult, String> replacer) {

        if (true)
            return matcher.replaceAll(replacer);


        // Java 8 version
        boolean find = matcher.find();
        if (find) {
            StringBuffer sb = new StringBuffer();

            do
                matcher.appendReplacement(sb, replacer.apply(matcher));
            while (find = matcher.find());

            matcher.appendTail(sb);
            return sb.toString();
        }

        return string;
    }

    static final int asInt(long value) {
        return asInt(value, () -> new DataTypeException("Long value too large for int downcast: " + value));
    }

    static final <E extends Exception> int asInt(long value, Supplier<E> e) throws E {
        if (value > Integer.MAX_VALUE)
            throw e.get();
        else
            return (int) value;
    }

    /**
     * Used to work around bugs in JDBC drivers, e.g.
     * https://github.com/h2database/h2database/issues/3236
     */
    static final <T, E extends Exception> T ignoreNPE(ThrowingSupplier<? extends T, ? extends E> supplier, Supplier<? extends T> ifNPE) throws E {
        try {
            return supplier.get();
        }
        catch (NullPointerException e) {
            return ifNPE.get();
        }
        catch (Exception e) {
            if (ExceptionTools.getCause(e, NullPointerException.class) != null)
                return ifNPE.get();
            else
                throw e;
        }
    }

    static final <T> DataType<T> removeGenerator(Configuration configuration, DataType<T> dataType) {









        return dataType;
    }

    static final ConverterContext converterContext(Attachable attachable) {
        return new DefaultConverterContext(configuration(attachable));
    }

    static final ConverterContext converterContext(Configuration configuration) {
        return new DefaultConverterContext(configuration(configuration));
    }

    /**
     * Wrap an expression in a derived table to allow for simplifying
     * referencing it.
     */
    static final <T1, R> Field<R> derivedTable(
        Context<?> ctx,
        Field<T1> f1,
        Function1<? super Field<T1>, ? extends Field<R>> f
    ) {
        return derivedTableIf(ctx, true, f1, f);
    }

    /**
     * Wrap expressions in a derived table to allow for simplifying referencing
     * them.
     */
    static final <T1, T2, R> Field<R> derivedTable(
        Context<?> ctx,
        Field<T1> f1,
        Field<T2> f2,
        Function2<? super Field<T1>, ? super Field<T2>, ? extends Field<R>> f
    ) {
        return derivedTableIf(ctx, true, f1, f2, f);
    }

    /**
     * Wrap expressions in a derived table to allow for simplifying referencing
     * them.
     */
    static final <T1, T2, T3, R> Field<R> derivedTable(
        Context<?> ctx,
        Field<T1> f1,
        Field<T2> f2,
        Field<T3> f3,
        Function3<? super Field<T1>, ? super Field<T2>, ? super Field<T3>, ? extends Field<R>> f
    ) {
        return derivedTableIf(ctx, true, f1, f2, f3, f);
    }

    /**
     * Wrap an expression in a derived table to allow for simplifying
     * referencing it.
     */
    static final <T1, R> Field<R> derivedTableIf(
        Context<?> ctx,
        boolean condition,
        Field<T1> f1,
        Function1<? super Field<T1>, ? extends Field<R>> f
    ) {
        if (condition && derivedTableEnabled(ctx) && !isSimple(ctx, f1))
            return DSL.field(select(f.apply(DSL.field(name("f1"), f1.getDataType()))).from(select(f1.as("f1")).asTable("t")));
        else
            return f.apply(f1);
    }

    /**
     * Wrap expressions in a derived table to allow for simplifying referencing
     * them.
     */
    static final <T1, T2, R> Field<R> derivedTableIf(
        Context<?> ctx,
        boolean condition,
        Field<T1> f1,
        Field<T2> f2,
        Function2<? super Field<T1>, ? super Field<T2>, ? extends Field<R>> f
    ) {
        if (condition && derivedTableEnabled(ctx) && !isSimple(ctx, f1) && !isSimple(ctx, f2))
            return DSL.field(select(f.apply(DSL.field(name("f1"), f1.getDataType()), DSL.field(name("f2"), f2.getDataType()))).from(select(f1.as("f1"), f2.as("f2")).asTable("t")));
        else
            return f.apply(f1, f2);
    }

    /**
     * Wrap expressions in a derived table to allow for simplifying referencing
     * them.
     */
    static final <T1, T2, T3, R> Field<R> derivedTableIf(
        Context<?> ctx,
        boolean condition,
        Field<T1> f1,
        Field<T2> f2,
        Field<T3> f3,
        Function3<? super Field<T1>, ? super Field<T2>, ? super Field<T3>, ? extends Field<R>> f
    ) {
        if (condition && derivedTableEnabled(ctx) && !isSimple(ctx, f1) && !isSimple(ctx, f2) && !isSimple(ctx, f3))
            return DSL.field(select(f.apply(DSL.field(name("f1"), f1.getDataType()), DSL.field(name("f2"), f2.getDataType()), DSL.field(name("f3"), f3.getDataType()))).from(select(f1.as("f1"), f2.as("f2"), f3.as("f3")).asTable("t")));
        else
            return f.apply(f1, f2, f3);
    }

    private static boolean derivedTableEnabled(Context<?> ctx) {
        return !FALSE.equals(ctx.settings().isRenderVariablesInDerivedTablesForEmulations())
            && !NO_SUPPORT_CORRELATED_DERIVED_TABLE.contains(ctx.dialect());
    }

    @SuppressWarnings("removal")
    static final DataType<?> componentDataType(Object[] array) {
        if (!isEmpty(array) && array[0] instanceof Field<?> f) {
            return f.getDataType();
        }
        else
            return DSL.getDataType(array.getClass().getComponentType());
    }

    static final Object[] mostSpecificArray(Object[] array) {
        if (isEmpty(array))
            return array;

        Class<?> type = null;
        for (Object o : array)
            if (o != null)
                if (type == null)
                    type = o.getClass();
                else if (type != o.getClass())
                    return array;

        if (type == null)
            return array;
        else
            return Convert.convertArray(array, type);
    }

    static final <R extends Record> QuantifiedSelect<R> quantify(Quantifier q, Select<R> select) {
        switch (q) {
            case ANY:
                return any(select);
            case ALL:
                return all(select);
            default:
                throw new IllegalArgumentException("Unsupported quantifier: " + q);
        }
    }
}
