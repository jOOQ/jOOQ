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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
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
import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;
import static java.time.temporal.ChronoField.MONTH_OF_YEAR;
import static java.time.temporal.ChronoField.NANO_OF_SECOND;
import static java.time.temporal.ChronoField.SECOND_OF_MINUTE;
import static java.time.temporal.ChronoField.YEAR;
import static java.util.Arrays.asList;
import static java.util.function.Function.identity;
import static java.util.regex.Matcher.quoteReplacement;
import static org.jooq.ContextConverter.scoped;
import static org.jooq.Decfloat.decfloat;
import static org.jooq.Decfloat.decfloatOrNull;
import static org.jooq.Geography.geography;
import static org.jooq.Geometry.geometry;
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.CLICKHOUSE;
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
// ...
import static org.jooq.SQLDialect.DEFAULT;
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.DUCKDB;
// ...
import static org.jooq.SQLDialect.FIREBIRD;
// ...
import static org.jooq.SQLDialect.H2;
// ...
// ...
// ...
import static org.jooq.SQLDialect.HSQLDB;
// ...
// ...
// ...
import static org.jooq.SQLDialect.MYSQL;
// ...
// ...
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...
// ...
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
// ...
import static org.jooq.SQLDialect.TRINO;
// ...
import static org.jooq.SQLDialect.YUGABYTEDB;
import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.impl.Array.NO_SUPPORT_SQUARE_BRACKETS;
import static org.jooq.impl.BlobBinding.readBlob;
import static org.jooq.impl.Convert.convert;
import static org.jooq.impl.Convert.patchIso8601Timestamp;
import static org.jooq.impl.DSL.cast;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.log;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.using;
import static org.jooq.impl.DefaultBinding.DefaultDoubleBinding.REQUIRES_LITERAL_CAST;
import static org.jooq.impl.DefaultBinding.DefaultDoubleBinding.infinity;
import static org.jooq.impl.DefaultBinding.DefaultDoubleBinding.nan;
import static org.jooq.impl.DefaultBinding.DefaultEnumTypeBinding.pgEnumValue;
import static org.jooq.impl.DefaultBinding.DefaultEnumTypeBinding.pgRenderEnumCast;
import static org.jooq.impl.DefaultBinding.DefaultJSONBBinding.EMULATE_AS_BLOB;
import static org.jooq.impl.DefaultBinding.DefaultJSONBinding.patchSnowflakeJSON;
import static org.jooq.impl.DefaultBinding.DefaultResultBinding.readMultisetJSON;
import static org.jooq.impl.DefaultBinding.DefaultResultBinding.readMultisetXML;
import static org.jooq.impl.DefaultBinding.DefaultStringBinding.autoRtrim;
import static org.jooq.impl.DefaultDataType.getDataType;
import static org.jooq.impl.DefaultDataType.unsupportedDatetimePrecision;
import static org.jooq.impl.DefaultExecuteContext.localExecuteContext;
import static org.jooq.impl.DefaultExecuteContext.localTargetConnection;
import static org.jooq.impl.Internal.arrayType;
import static org.jooq.impl.Keywords.K_ARRAY;
import static org.jooq.impl.Keywords.K_AS;
import static org.jooq.impl.Keywords.K_BLOB;
import static org.jooq.impl.Keywords.K_CAST;
import static org.jooq.impl.Keywords.K_DATE;
import static org.jooq.impl.Keywords.K_DATETIME;
import static org.jooq.impl.Keywords.K_DATETIME2;
import static org.jooq.impl.Keywords.K_DATETIMEOFFSET;
import static org.jooq.impl.Keywords.K_FALSE;
import static org.jooq.impl.Keywords.K_FORMAT;
import static org.jooq.impl.Keywords.K_GEOGRAPHY;
import static org.jooq.impl.Keywords.K_GEOMETRY;
import static org.jooq.impl.Keywords.K_HOUR_TO_SECOND;
import static org.jooq.impl.Keywords.K_JSON;
import static org.jooq.impl.Keywords.K_NULL;
import static org.jooq.impl.Keywords.K_TIME;
import static org.jooq.impl.Keywords.K_TIMESTAMP;
import static org.jooq.impl.Keywords.K_TIMESTAMP_WITH_TIME_ZONE;
import static org.jooq.impl.Keywords.K_TIME_WITH_TIME_ZONE;
import static org.jooq.impl.Keywords.K_TRUE;
import static org.jooq.impl.Keywords.K_YEAR_TO_DAY;
import static org.jooq.impl.Keywords.K_YEAR_TO_FRACTION;
import static org.jooq.impl.Names.N_BYTEA;
import static org.jooq.impl.Names.N_JSON_PARSE;
import static org.jooq.impl.Names.N_PARSE_JSON;
import static org.jooq.impl.Names.N_ST_GEOMFROMTEXT;
import static org.jooq.impl.Names.N_ST_GEOMFROMWKB;
import static org.jooq.impl.Names.N_TO_BINARY;
import static org.jooq.impl.R2DBC.isR2dbc;
import static org.jooq.impl.SQLDataType.BIGINT;
import static org.jooq.impl.SQLDataType.BLOB;
import static org.jooq.impl.SQLDataType.BOOLEAN;
import static org.jooq.impl.SQLDataType.CHAR;
import static org.jooq.impl.SQLDataType.DATE;
import static org.jooq.impl.SQLDataType.DECFLOAT;
import static org.jooq.impl.SQLDataType.DECIMAL_INTEGER;
import static org.jooq.impl.SQLDataType.DOUBLE;
import static org.jooq.impl.SQLDataType.INTEGER;
import static org.jooq.impl.SQLDataType.LONGVARCHAR;
import static org.jooq.impl.SQLDataType.NUMERIC;
import static org.jooq.impl.SQLDataType.OTHER;
import static org.jooq.impl.SQLDataType.REAL;
import static org.jooq.impl.SQLDataType.ROWID;
import static org.jooq.impl.SQLDataType.SMALLINT;
import static org.jooq.impl.SQLDataType.TIME;
import static org.jooq.impl.SQLDataType.TIMESTAMP;
import static org.jooq.impl.SQLDataType.VARBINARY;
import static org.jooq.impl.SQLDataType.VARCHAR;
import static org.jooq.impl.Tools.CTX;
import static org.jooq.impl.Tools.apply;
import static org.jooq.impl.Tools.asInt;
import static org.jooq.impl.Tools.attachRecords;
import static org.jooq.impl.Tools.convertBytesToHex;
import static org.jooq.impl.Tools.convertHexToBytes;
import static org.jooq.impl.Tools.emulateMultiset;
import static org.jooq.impl.Tools.enums;
// ...
import static org.jooq.impl.Tools.getMappedTable;
import static org.jooq.impl.Tools.getMappedUDT;
import static org.jooq.impl.Tools.getMappedUDTName;
import static org.jooq.impl.Tools.getRecordQualifier;
import static org.jooq.impl.Tools.isEmpty;
import static org.jooq.impl.Tools.map;
import static org.jooq.impl.Tools.needsBackslashEscaping;
import static org.jooq.impl.Tools.newRecord;
import static org.jooq.impl.Tools.rtrim;
import static org.jooq.impl.Tools.uncoerce;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_MULTISET_CONTENT;
import static org.jooq.impl.Tools.ExtendedDataKey.DATA_OMIT_DATETIME_LITERAL_PREFIX;
import static org.jooq.tools.StringUtils.defaultIfNull;
import static org.jooq.tools.StringUtils.isEmpty;
import static org.jooq.tools.StringUtils.leftPad;
import static org.jooq.tools.jdbc.JDBCUtils.safeFree;
import static org.jooq.tools.jdbc.JDBCUtils.wasNull;
import static org.jooq.tools.reflect.Reflect.onClass;
import static org.jooq.tools.reflect.Reflect.wrapper;
import static org.jooq.util.postgres.PostgresUtils.toDayToSecond;
import static org.jooq.util.postgres.PostgresUtils.toPGArray;
import static org.jooq.util.postgres.PostgresUtils.toPGArrayString;
import static org.jooq.util.postgres.PostgresUtils.toPGInterval;
import static org.jooq.util.postgres.PostgresUtils.toYearToMonth;

import java.io.Serializable;
import java.io.StringReader;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Struct;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Year;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.SignStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// ...
// ...
// ...
import org.jooq.Attachable;
import org.jooq.Binding;
import org.jooq.BindingGetResultSetContext;
import org.jooq.BindingGetSQLInputContext;
import org.jooq.BindingGetStatementContext;
import org.jooq.BindingRegisterContext;
import org.jooq.BindingSQLContext;
import org.jooq.BindingScope;
import org.jooq.BindingSetSQLOutputContext;
import org.jooq.BindingSetStatementContext;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.ContextConverter;
import org.jooq.Converter;
import org.jooq.ConverterContext;
import org.jooq.Converters;
import org.jooq.DSLContext;
import org.jooq.DataType;
import org.jooq.Decfloat;
import org.jooq.EnumType;
import org.jooq.ExecuteScope;
import org.jooq.Field;
import org.jooq.Geography;
import org.jooq.Geometry;
import org.jooq.JSON;
import org.jooq.JSONB;
import org.jooq.Package;
import org.jooq.Param;
// ...
import org.jooq.QualifiedRecord;
import org.jooq.Record;
import org.jooq.RecordQualifier;
import org.jooq.RenderContext;
import org.jooq.Result;
import org.jooq.Row;
import org.jooq.RowId;
import org.jooq.SQLDialect;
import org.jooq.Schema;
import org.jooq.Scope;
import org.jooq.Source;
import org.jooq.Spatial;
import org.jooq.UDT;
import org.jooq.UDTField;
import org.jooq.UDTRecord;
import org.jooq.XML;
import org.jooq.conf.NestedCollectionEmulation;
import org.jooq.exception.ControlFlowSignal;
import org.jooq.exception.DataAccessException;
import org.jooq.exception.DataTypeException;
import org.jooq.exception.MappingException;
import org.jooq.exception.SQLDialectNotSupportedException;
import org.jooq.impl.Cast.CastNative;
import org.jooq.impl.R2DBC.R2DBCPreparedStatement;
import org.jooq.impl.Tools.ExtendedDataKey;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.Longs;
import org.jooq.tools.StringUtils;
import org.jooq.tools.jdbc.JDBCUtils;
import org.jooq.tools.jdbc.MockArray;
import org.jooq.tools.jdbc.MockResultSet;
import org.jooq.tools.json.JSONArray;
import org.jooq.tools.json.JSONObject;
import org.jooq.tools.json.JSONParser;
import org.jooq.tools.json.JSONValue;
import org.jooq.types.DayToSecond;
import org.jooq.types.UByte;
import org.jooq.types.UInteger;
import org.jooq.types.ULong;
import org.jooq.types.UShort;
import org.jooq.types.YearToMonth;
import org.jooq.types.YearToSecond;
import org.jooq.util.postgres.PostgresUtils;

// ...
// ...

// ...
// ...
// ...
// ...
// ...
// ...

/**
 * @author Lukas Eder
 */
public class DefaultBinding<T, U> implements Binding<T, U> {

    static final JooqLogger              log                       = JooqLogger.getLogger(DefaultBinding.class);






    // Taken from org.postgresql.PGStatement 9223372036825200000
    private static final long            PG_DATE_POSITIVE_INFINITY = 9223372036825200000L;
    private static final long            PG_DATE_NEGATIVE_INFINITY = -9223372036832400000L;

    final Binding<T, U>                  delegate;

    /**
     * Get the internal default binding for a {@link Converter}.
     */
    public static final <T, U> Binding<T, U> binding(Converter<T, U> converter) {
        return binding(DefaultDataType.getDataType(DEFAULT, converter.fromType()), converter);
    }

    /**
     * Get the internal default binding for a {@link DataType}.
     */
    public static final <T> Binding<T, T> binding(DataType<T> dataType) {
        return binding(dataType, Converters.identity(dataType.getType()));
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    static final <T, U> Binding<T, U> binding(DataType<? extends T> dataType, Converter<T, U> converter) {
        Class<?> type = converter.fromType();

        // Concrete types
        if (type == BigDecimal.class)
            return new DefaultBigDecimalBinding(dataType, converter);
        else if (type == BigInteger.class)
            return new DefaultBigIntegerBinding(dataType, converter);
        else if (type == Blob.class)
            return new DefaultBlobBinding(dataType, converter);
        else if (type == Boolean.class)
            return new DefaultBooleanBinding(dataType, converter);
        else if (type == Byte.class || type == byte.class)
            return new DefaultByteBinding(dataType, converter);
        else if (type == byte[].class)
            return new DefaultBytesBinding(dataType, converter);
        else if (type == Clob.class)
            return new DefaultClobBinding(dataType, converter);
        else if (type == Date.class)
            return new DefaultDateBinding(dataType, converter);
        else if (type == DayToSecond.class)
            return new DefaultDayToSecondBinding(dataType, converter);
        else if (type == Decfloat.class)
            return new DefaultDecfloatBinding(dataType, converter);
        else if (type == Double.class || type == double.class)
            return new DefaultDoubleBinding(dataType, converter);
        else if (type == Float.class || type == float.class)
            return new DefaultFloatBinding(dataType, converter);
        else if (type == Geometry.class)





            return new CommercialOnlyBinding(dataType, converter);
        else if (type == Geography.class)






            return new CommercialOnlyBinding(dataType, converter);
        else if (type == Integer.class || type == int.class)
            return new DefaultIntegerBinding(dataType, converter);
        else if (type == JSON.class)
            return new DefaultJSONBinding(dataType, converter);
        else if (type == JSONB.class)
            return new DefaultJSONBBinding(dataType, converter);
        else if (type == XML.class)
            return new DefaultXMLBinding(dataType, converter);
        else if (type == LocalDate.class)
            return (Binding<T, U>) new DelegatingBinding<>(
                (DataType<LocalDate>) dataType,
                ContextConverter.ofNullable(Date.class, LocalDate.class,
                    (BiFunction<Date, ConverterContext, LocalDate> & Serializable) (t, x) -> t.toLocalDate(),
                    (BiFunction<LocalDate, ConverterContext, Date> & Serializable) (t, x) -> Date.valueOf(t)
                ),
                (ContextConverter<LocalDate, U>) converter,
                c -> new DefaultDateBinding<>(DATE, c)
            );
        else if (type == LocalDateTime.class)
            return (Binding<T, U>) new DelegatingBinding<>(
                (DataType<LocalDateTime>) dataType,
                ContextConverter.ofNullable(Timestamp.class, LocalDateTime.class,
                    (BiFunction<Timestamp, ConverterContext, LocalDateTime> & Serializable) (t, x) -> t.toLocalDateTime(),
                    (BiFunction<LocalDateTime, ConverterContext, Timestamp> & Serializable) (t, x) -> Timestamp.valueOf(t)
                ),
                (ContextConverter<LocalDateTime, U>) converter,
                c -> new DefaultTimestampBinding<>(TIMESTAMP, c)
            );
        else if (type == LocalTime.class)
            return (Binding<T, U>) new DelegatingBinding<>(
                (DataType<LocalTime>) dataType,
                ContextConverter.ofNullable(Time.class, LocalTime.class,
                    (BiFunction<Time, ConverterContext, LocalTime> & Serializable) (t, x) -> t.toLocalTime(),
                    (BiFunction<LocalTime, ConverterContext, Time> & Serializable) (t, x) -> Time.valueOf(t)
                ),
                (ContextConverter<LocalTime, U>) converter,
                c -> new DefaultTimeBinding<>(TIME, c)
            );
        else if (type == Long.class || type == long.class)
            return new DefaultLongBinding(dataType, converter);
        else if (type == OffsetDateTime.class)
            return new DefaultOffsetDateTimeBinding(dataType, converter);
        else if (type == OffsetTime.class)
            return new DefaultOffsetTimeBinding(dataType, converter);
        else if (type == Instant.class)
            return new DefaultInstantBinding(dataType, converter);
        else if (type == RowId.class)
            return new DefaultRowIdBinding(dataType, converter);
        else if (type == Short.class || type == short.class)
            return new DefaultShortBinding(dataType, converter);
        else if (type == String.class)
            if (dataType.isNString())
                return new DefaultNStringBinding(dataType, converter);
            else
                return new DefaultStringBinding(dataType, converter);
        else if (type == Time.class)
            return new DefaultTimeBinding(dataType, converter);
        else if (type == Timestamp.class)
            return new DefaultTimestampBinding(dataType, converter);
        // [#8022] Support for the "synthetic" timestamp type
        else if (type == java.util.Date.class)
            return new DefaultTimestampBinding(dataType, Converters.of(TimestampToJavaUtilDateConverter.INSTANCE, (Converter<java.util.Date, java.util.Date>) converter));
        else if (type == UByte.class)
            return (Binding<T, U>) new DelegatingBinding<>(
                (DataType<UByte>) dataType,
                ContextConverter.ofNullable(Short.class, UByte.class,
                    (BiFunction<Short, ConverterContext, UByte> & Serializable) (t, x) -> UByte.valueOf(t),
                    (BiFunction<UByte, ConverterContext, Short> & Serializable) (t, x) -> t.shortValue()
                ),
                (ContextConverter<UByte, U>) converter,
                c -> new DefaultShortBinding<>(SMALLINT, c)
            );
        else if (type == UInteger.class)
            return (Binding<T, U>) new DelegatingBinding<>(
                (DataType<UInteger>) dataType,
                ContextConverter.ofNullable(Long.class, UInteger.class,
                    (BiFunction<Long, ConverterContext, UInteger> & Serializable) (t, x) -> UInteger.valueOf(t),
                    (BiFunction<UInteger, ConverterContext, Long> & Serializable) (t, x) -> t.longValue()
                ),
                (ContextConverter<UInteger, U>) converter,
                c -> new DefaultLongBinding<>(BIGINT, c)
            );
        else if (type == ULong.class)
            return (Binding<T, U>) new DelegatingBinding<>(
                (DataType<ULong>) dataType,
                ContextConverter.ofNullable(BigInteger.class, ULong.class,
                    (BiFunction<BigInteger, ConverterContext, ULong> & Serializable) (t, x) -> ULong.valueOf(t),
                    (BiFunction<ULong, ConverterContext, BigInteger> & Serializable) (t, x) -> t.toBigInteger()
                ),
                (ContextConverter<ULong, U>) converter,
                c -> new DefaultBigIntegerBinding<>(DECIMAL_INTEGER, c)
            );
        else if (type == UShort.class)
            return (Binding<T, U>) new DelegatingBinding<>(
                (DataType<UShort>) dataType,
                ContextConverter.ofNullable(Integer.class, UShort.class,
                    (BiFunction<Integer, ConverterContext, UShort> & Serializable) (t, x) -> UShort.valueOf(t),
                    (BiFunction<UShort, ConverterContext, Integer> & Serializable) (t, x) -> t.intValue()
                ),
                (ContextConverter<UShort, U>) converter,
                c -> new DefaultIntegerBinding<>(INTEGER, c)
            );
        else if (type == UUID.class)
            return new DefaultUUIDBinding(dataType, converter);
        else if (type == YearToSecond.class)
            return new DefaultYearToSecondBinding(dataType, converter);
        else if (type == YearToMonth.class)
            return new DefaultYearToMonthBinding(dataType, converter);
        else if (type == Year.class)
            return new DefaultYearBinding(dataType, converter);

        // Subtypes of array types etc.
        // The type byte[] is handled earlier. byte[][] can be handled here
        else if (type.isArray())
            return new DefaultArrayBinding(dataType, converter);








        else if (EnumType.class.isAssignableFrom(type))
            return new DefaultEnumTypeBinding(dataType, converter);
        else if (Record.class.isAssignableFrom(type))
            return new DefaultRecordBinding(dataType, converter);
        else if (Result.class.isAssignableFrom(type))
            return new DefaultResultBinding(dataType, converter);

        // Undefined types
        else
            return new DefaultOtherBinding(dataType, converter);

    }

    /**
     * @deprecated - 3.11 - [#6631] - Use {@link #binding(Converter)} instead.
     */
    @Deprecated(forRemoval = true)
    public DefaultBinding(Converter<T, U> converter) {
        this.delegate = binding(converter);
    }

    public DefaultBinding(Binding<T, U> delegate) {
        this.delegate = delegate;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    static final <T, X, U> Binding<T, U> newBinding(final Converter<X, U> converter, final DataType<? extends T> dataType, final Binding<T, X> binding) {
        final Binding<T, U> theBinding;


        if (converter == null && binding == null) {
            theBinding = (Binding) dataType.getBinding();
        }
        else if (converter == null) {
            theBinding = (Binding) binding;
        }
        else if (binding == null) {
            theBinding = binding(dataType, (ContextConverter<T, U>) scoped(converter));
        }
        else {
            theBinding = new Binding<T, U>() {

                final ContextConverter<T, U> theConverter = Converters.of(binding.converter(), converter);

                @Override
                public Converter<T, U> converter() {
                    return theConverter;
                }

                @Override
                public void sql(BindingSQLContext<U> ctx) throws SQLException {
                    binding.sql(ctx.convert(converter));
                }

                @Override
                public void register(BindingRegisterContext<U> ctx) throws SQLException {
                    binding.register(ctx.convert(converter));
                }

                @Override
                public void set(BindingSetStatementContext<U> ctx) throws SQLException {
                    binding.set(ctx.convert(converter));
                }

                @Override
                public void set(BindingSetSQLOutputContext<U> ctx) throws SQLException {
                    binding.set(ctx.convert(converter));
                }

                @Override
                public void get(BindingGetResultSetContext<U> ctx) throws SQLException {
                    binding.get(ctx.convert(converter));
                }

                @Override
                public void get(BindingGetStatementContext<U> ctx) throws SQLException {
                    binding.get(ctx.convert(converter));
                }

                @Override
                public void get(BindingGetSQLInputContext<U> ctx) throws SQLException {
                    binding.get(ctx.convert(converter));
                }
            };
        }

        return theBinding;
    }

    static final Map<String, Class<?>> typeMap(Class<?> type, Scope scope) {
        return typeMap(type, scope, new HashMap<>());
    }

    @SuppressWarnings("unchecked")
    static final Map<String, Class<?>> typeMap(Class<?> type, Scope scope, Map<String, Class<?>> result) {
        try {
            if (QualifiedRecord.class.isAssignableFrom(type)) {
                Class<QualifiedRecord<?>> t = (Class<QualifiedRecord<?>>) type;

                // [#644] Prevent infinite recursion between fields and subtypes
                if (result.putIfAbsent(getMappedUDTName(scope, t), t) == null) {












                    RecordQualifier<?> q = getRecordQualifier(t);
                    for (Field<?> field : q.fields())
                        typeMap(field.getType(), scope, result);

                    // [#644] Put subtypes into the type map as well
                    if (q instanceof UDT<?> u) {
                        for (UDT<?> s : u.getSubtypes())
                            typeMap(s.getRecordType(), scope, result);
                    }
                }
            }










        }
        catch (Exception e) {
            throw new MappingException("Error while collecting type map", e);
        }

        return result;
    }

    private static final long parse(Class<? extends java.util.Date> type, String date) throws SQLException {

        // Try reading a plain number first
        Long number = Longs.tryParse(date);
        if (number != null)
            return number;

        // If that fails, try reading a formatted date

        // [#7325] In SQLite dates could be stored in both ISO standard formats:
        //         With T (default standard), or without T (optional standard, JDBC standard)
        date = StringUtils.replace(date, "T", " ");

        if (type == Timestamp.class)
            return Timestamp.valueOf(date).getTime();

        // Dates may come with " 00:00:00". This is safely trimming time information
        else if (type == Date.class)
            return Date.valueOf(date.split(" ")[0]).getTime();

        else if (type == Time.class)
            return Time.valueOf(date).getTime();

        throw new SQLException("Could not parse date " + date);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Implementation of Binding API for backwards compatibility
    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public Converter<T, U> converter() {
        return delegate.converter();
    }

    @Override
    public void sql(BindingSQLContext<U> ctx) throws SQLException {
        delegate.sql(ctx);
    }

    @Override
    public void register(BindingRegisterContext<U> ctx) throws SQLException {
        delegate.register(ctx);
    }

    @Override
    public void set(BindingSetStatementContext<U> ctx) throws SQLException {
        delegate.set(ctx);
    }

    @Override
    public void set(BindingSetSQLOutputContext<U> ctx) throws SQLException {
        delegate.set(ctx);
    }

    @Override
    public void get(BindingGetResultSetContext<U> ctx) throws SQLException {
        delegate.get(ctx);
    }

    @Override
    public void get(BindingGetStatementContext<U> ctx) throws SQLException {
        delegate.get(ctx);
    }

    @Override
    public void get(BindingGetSQLInputContext<U> ctx) throws SQLException {
        delegate.get(ctx);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Object API
    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return delegate.toString();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Type-specific subclasses API
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * An internal binding class for default data type binding implementations.
     * <p>
     * This base class can be safely assumed to not leak into custom bindings.
     */
    abstract static class InternalBinding<T, U> implements org.jooq.Binding<T, U> {
        static final Set<SQLDialect> NEEDS_PRECISION_SCALE_ON_BIGDECIMAL = SQLDialect.supportedBy(CUBRID, DERBY, FIREBIRD, H2, HSQLDB);
        static final Set<SQLDialect> REQUIRES_JSON_CAST                  = SQLDialect.supportedBy(POSTGRES, TRINO, YUGABYTEDB);
        static final Set<SQLDialect> NO_SUPPORT_ENUM_CAST                = SQLDialect.supportedBy(POSTGRES, YUGABYTEDB);
        static final Set<SQLDialect> NO_SUPPORT_NVARCHAR                 = SQLDialect.supportedBy(CLICKHOUSE, DERBY, DUCKDB, FIREBIRD, POSTGRES, SQLITE, TRINO, YUGABYTEDB);





        final DataType<T>            dataType;
        final ContextConverter<T, U> converter;
        final boolean                attachable;

        InternalBinding(DataType<T> dataType, Converter<T, U> converter) {
            this.dataType = dataType;
            this.converter = ContextConverter.scoped(converter);

            // [#11099] Caching this per binding seems to have a considerable performance effect.
            //          We must be careful to short circuit instanceof Attachable checks only if we *know*
            //          the type cannot be attachable, i.e. for final declared types only (such as String)
            this.attachable = Attachable.class.isAssignableFrom(converter.toType())
                           || !Modifier.isFinal(converter.toType().getModifiers());
        }

        @Override
        public final ContextConverter<T, U> converter() {
            return converter;
        }

        static final Configuration originalConfiguration(Scope ctx) {
            if (ctx instanceof ExecuteScope es) {
                if (es.executeContext() instanceof DefaultExecuteContext dec) {
                    return dec.originalConfiguration();
                }
            }

            return ctx.configuration();
        }

        static final DSLContext originalScope(Scope ctx) {
            if (ctx instanceof ExecuteScope es) {
                if (es.executeContext() instanceof DefaultExecuteContext dec) {
                    return dec.originalConfiguration().dsl();
                }
            }

            return ctx.dsl();
        }

        private final boolean shouldCast(BindingSQLContext<U> ctx, T converted) {

            // [#10662] A few dialects require casts for NULL literals
            if (ctx.render().paramType() == INLINED) {
                if (converted == null) {
                    switch (ctx.family()) {








                        case DERBY:
                            return true;
                    }
                }

                // [#17803] Some dialects require the cast for syntactic reasons (see DefaultDoubleBinding.REQUIRES_LITERAL_CAST)
                //          Others require it occasionally to prevent wrong type promotions, on literals only.
                if (dataType.isFloat()) {
                    switch (ctx.family()) {



                        case FIREBIRD:

                        // [#10879] We'll need more precise cast types, first!
                        // case MARIADB:
                        // case MYSQL:
                        case POSTGRES:


                        case YUGABYTEDB:
                            return true;
                    }
                }
            }

            // Many dialects require casts for bind values
            else {

                // Generated enums should not be cast...
                if (!(converted instanceof EnumType)) {
                    switch (ctx.family()) {

                        // These dialects can hardly detect the type of a bound constant.


                        case DERBY:
                        case DUCKDB:
                        case FIREBIRD:

                        // These dialects have some trouble, when they mostly get it right.
                        case H2:
                        case HSQLDB:
                        case IGNITE:

                        // [#1261] There are only a few corner-cases, where this is
                        // really needed. Check back on related CUBRID bugs
                        case CUBRID:

                        // [#1029] Postgres and [#632] Sybase need explicit casting
                        // in very rare cases. BigQuery doesn't support NULL BOOLEAN or INT64 bind values








                        case POSTGRES:
                        case YUGABYTEDB: {
                            return true;
                        }
                    }
                }
            }

            // [#566] JDBC doesn't explicitly support interval data types. To be on
            // the safe side, always cast these types in those dialects that support
            // them
            if (dataType.isInterval()) {
                switch (ctx.family()) {




                    case H2:
                    case HSQLDB:
                    case POSTGRES:
                    case YUGABYTEDB:
                        return true;
                }
            }

            // [#7242] [#13252] Other vendor specific types also need a lot of casting
            if (dataType.isJSON() || dataType.isXML()) {
                switch (ctx.family()) {




                    case POSTGRES:
                    case TRINO:
                    case YUGABYTEDB:
                        return true;
                }
            }

            if (dataType.isUUID()) {
                switch (ctx.family()) {



                    case CLICKHOUSE:
                    case HSQLDB:
                    case POSTGRES:
                    case TRINO:
                    case YUGABYTEDB:
                        return true;
                }
            }

            if (dataType.getType() == OffsetDateTime.class ||
                dataType.getType() == Instant.class
            ) {
                switch (ctx.family()) {







                    case TRINO:
                        return true;
                }
            }

            if (dataType.getType() == OffsetTime.class) {
                switch (ctx.family()) {







                    case TRINO:
                        return true;
                }
            }

            if (dataType.getType() == Decfloat.class) {
                switch (ctx.family()) {


                    case FIREBIRD:
                    case H2:
                        return true;
                }
            }

            // [#2902] The xerial driver binds BigDecimal as String, which may produce
            //         wrong results
            if (dataType.isDecimal()) {
                switch (ctx.family()) {
                    case SQLITE:
                        return true;
                }
            }





































            return false;
        }

        /**
         * Render the bind variable including a cast, if necessary
         */
        private final void sqlCast(BindingSQLContext<U> ctx, T converted) throws SQLException {
            DataType<T> sqlDataType = dataType.getSQLDataType();
            SQLDialect family = ctx.family();

            // [#822] Some RDBMS need precision / scale information on BigDecimals
            if (converted != null && dataType.getType() == BigDecimal.class && NEEDS_PRECISION_SCALE_ON_BIGDECIMAL.contains(ctx.dialect())) {

                // Add precision / scale on BigDecimals
                int scale = ((BigDecimal) converted).scale();
                int precision = ((BigDecimal) converted).precision();

                // [#5323] BigDecimal precision is always 1 for BigDecimals smaller than 1.0
                if (scale >= precision)
                    precision = scale + 1;

                sqlCast(ctx, converted, dataType, null, precision, scale);
            }

            // [#7905] The ROWID type cannot be cast to
            else if (ROWID == sqlDataType)
                sql(ctx, converted);

            // [#1028] Most databases don't know an OTHER type (except H2, HSQLDB).
            else if (OTHER == sqlDataType)

                // If the bind value is set, it can be used to derive the cast type
                if (converted != null)
                    sqlCast(ctx, converted, DefaultDataType.getDataType(family, converted.getClass()), null, null, null);

                // [#11511] Specifically when in a parser context, we must not
                // blindly cast bind variables
                else
                    ctx.render().sql(ctx.variable());

            // [#1029] Postgres generally doesn't need the casting. Only in the
            // above case where the type is OTHER
            // [#1125] Also with temporal data types, casting is needed some times
            // [#4338] ... specifically when using JSR-310 types
            // [#1130] TODO type can be null for ARRAY types, etc.
            // [#7351] UUID data types need to be cast too
            // [#7242] JSON(B) data types need to be cast too
            // [#13252] XML data types need to be cast too
            // [#17803] Floating point literals need to be cast as well
            else if (REQUIRES_JSON_CAST.contains(ctx.dialect()) &&
                    (sqlDataType == null ||
                    (!sqlDataType.isTemporal()
                        && sqlDataType != SQLDataType.UUID
                        && !sqlDataType.isXML()



                        && !sqlDataType.isJSON())
                        && !sqlDataType.isFloat()))
                sql(ctx, converted);














            // [#7379] Most databases cannot cast a bind variable to an enum type
            else if (!NO_SUPPORT_ENUM_CAST.contains(ctx.dialect()) && dataType.isEnum())
                sqlCast(
                    ctx,
                    converted,
                    Tools.emulateEnumType((DataType<EnumType>) dataType),
                    dataType.lengthDefined() ? dataType.length() : null,
                    dataType.precisionDefined() ? dataType.precision() : null,
                    dataType.scaleDefined() ? dataType.scale() : null
                );

            // [#17212] Avoid precision on datetime casts when not supported
            else if (dataType.isDateTime() && unsupportedDatetimePrecision(ctx, dataType))
                sqlCast(ctx, converted, dataType, null, null, null);

            // In all other cases, the bind variable can be cast normally
            else
                sqlCast(
                    ctx,
                    converted,
                    dataType,
                    dataType.lengthDefined() ? dataType.length() : null,
                    dataType.precisionDefined() ? dataType.precision() : null,
                    dataType.scaleDefined() ? dataType.scale() : null
                );
        }

        private static final int getValueLength(String string) {
            if (string == null)
                return 1;

            else {
                int length = string.length();

                // If non 7-bit ASCII characters are present, multiply the length by
                // 4 to be sure that even UTF-32 collations will fit. But don't use
                // larger numbers than Derby's upper limit 32672
                for (int i = 0; i < length; i++)
                    if (string.charAt(i) > 127)
                        return Math.min(32672, 4 * length);

                return Math.min(32672, length);
            }
        }

        private final void sqlCast(BindingSQLContext<U> ctx, T converted, DataType<?> t, Integer length, Integer precision, Integer scale) throws SQLException {
            switch (ctx.family()) {













                case TRINO: {
                    if (t.isJSON()) {
                        ctx.render().visit(N_JSON_PARSE).sql('(');
                        sql(ctx, converted);
                        ctx.render().sql(')');
                    }
                    else
                        sqlCast0(ctx, converted, t, length, precision, scale);

                    break;
                }

                default:
                    sqlCast0(ctx, converted, t, length, precision, scale);
                    break;
            }
        }

        private final void sqlCast0(BindingSQLContext<U> ctx, T converted, DataType<?> t, Integer length, Integer precision, Integer scale) throws SQLException {
            ctx.render().visit(K_CAST).sql('(');
            sql(ctx, converted);
            ctx.render().sql(' ').visit(K_AS).sql(' ')
                        .sql(DefaultDataType.set(t, length, precision, scale).getCastTypeName(ctx.configuration()))
                        .sql(')');
        }

        @Override
        public final void sql(BindingSQLContext<U> ctx) throws SQLException {
            T converted = converter().to(ctx.value(), ctx.converterContext());

            // Casting can be enforced or prevented
            switch (ctx.render().castMode()) {
                case NEVER:
                    sql(ctx, converted);
                    return;

                case ALWAYS:
                    sqlCast(ctx, converted);
                    return;
            }

            // See if we "should" cast, to stay on the safe side
            if (shouldCast(ctx, converted))
                sqlCast(ctx, converted);

            // Most RDBMS can infer types for bind values
            else
                sql(ctx, converted);
        }

        private final void sql(BindingSQLContext<U> ctx, T value) throws SQLException {
            if (ctx.render().paramType() == INLINED)
                if (value == null)
                    ctx.render().visit(K_NULL);
                else
                    sqlInline0(ctx, value);
            else
                sqlBind0(ctx, value);
        }

        /**
         * Escape a string literal by replacing <code>'</code> by <code>''</code>, and possibly also backslashes.
         */
        static final String escape(Object val, Context<?> ctx) {
            String result = val.toString();

            if (needsBackslashEscaping(ctx.configuration()))
                result = StringUtils.replace(result, "\\", "\\\\");






            return StringUtils.replace(result, "'", "''");
        }

        @Override
        public final void register(BindingRegisterContext<U> ctx) throws SQLException {
            if (!FALSE.equals(ctx.settings().isExecuteLogging()))
                if (log.isTraceEnabled())
                    log.trace("Registering variable " + ctx.index(), "" + dataType);

            register0(ctx);
        }

        @Override
        public final void set(BindingSetStatementContext<U> ctx) throws SQLException {
            T value = converter().to(ctx.value(), ctx.converterContext());

            if (!FALSE.equals(ctx.settings().isExecuteLogging()))
                if (log.isTraceEnabled())
                    if (value != null && value.getClass().isArray() && value.getClass() != byte[].class)
                        log.trace("Binding variable " + ctx.index(), Arrays.asList((Object[]) value) + " (" + dataType + ")");
                    else
                        log.trace("Binding variable " + ctx.index(), value + " (" + dataType + ")");

            if (value == null)
                setNull0(ctx);
            else
                set0(ctx, value);
        }

        @Override
        public final void set(BindingSetSQLOutputContext<U> ctx) throws SQLException {
            T value = converter().to(ctx.value(), ctx.converterContext());

            if (value == null)
                ctx.output().writeObject(null);
            else
                set0(ctx, value);
        }

        @Override
        public final void get(BindingGetResultSetContext<U> ctx) throws SQLException {
            U value = converter().from(get0(ctx), ctx.converterContext());

            if (attachable)
                value = attach(value, originalConfiguration(ctx));

            ctx.value(value);
        }

        @Override
        public final void get(BindingGetStatementContext<U> ctx) throws SQLException {
            U value = converter().from(get0(ctx), ctx.converterContext());

            if (attachable)
                value = attach(value, originalConfiguration(ctx));

            ctx.value(value);
        }

        @Override
        public final void get(BindingGetSQLInputContext<U> ctx) throws SQLException {
            U value = converter().from(get0(ctx), ctx.converterContext());

            if (attachable)
                value = attach(value, originalConfiguration(ctx));

            ctx.value(value);
        }

        private static final <U> U attach(U value, Configuration configuration) {

            // [#4372] Attach records if possible / required
            if (value instanceof Attachable && attachRecords(configuration))
                ((Attachable) value).attach(configuration);

            return value;
        }

        /* non-final */ void setNull0(BindingSetStatementContext<U> ctx) throws SQLException {
            if (ctx.statement() instanceof R2DBCPreparedStatement s)
                s.setNull(ctx.index(), dataType);
            else
                ctx.statement().setNull(ctx.index(), sqltype(ctx.statement(), ctx.configuration()));
        }

        /* non-final */ void register0(BindingRegisterContext<U> ctx) throws SQLException {
            ctx.statement().registerOutParameter(ctx.index(), sqltype(ctx.statement(), ctx.configuration()));
        }

        @SuppressWarnings("unused")
        /* non-final */ void sqlInline0(BindingSQLContext<U> ctx, T value) throws SQLException {
            sqlInline1(ctx, value);
        }

        @SuppressWarnings("unused")
        final void sqlInline1(BindingSQLContext<U> ctx, Object value) throws SQLException {

            // Known fall-through types:
            // - Blob, Clob
            // - String
            // - UUID
            ctx.render().sql('\'')
                        .sql(escape(value, ctx.render()), true)
                        .sql('\'');
        }

        @SuppressWarnings("unused")
        /* non-final */ void sqlBind0(BindingSQLContext<U> ctx, T value) throws SQLException {
            ctx.render().sql(ctx.variable());
        }

        // abstract void register0(BindingRegisterContext<U> ctx) throws SQLException;
        abstract void set0(BindingSetStatementContext<U> ctx, T value) throws SQLException;
        abstract void set0(BindingSetSQLOutputContext<U> ctx, T value) throws SQLException;
        abstract T get0(BindingGetResultSetContext<U> ctx) throws SQLException;
        abstract T get0(BindingGetStatementContext<U> ctx) throws SQLException;
        abstract T get0(BindingGetSQLInputContext<U> ctx) throws SQLException;
        abstract int sqltype(Statement statement, Configuration configuration) throws SQLException;

        // -----------------------------------------------------------------------------------------------------------------
        // Object API
        // -----------------------------------------------------------------------------------------------------------------

        @Override
        public String toString() {
            return "AbstractBinding [type=" + dataType + ", converter=" + converter + "]";
        }
    }

    static final class DelegatingBinding<X, T, U> extends InternalBinding<X, U> {

        private final ContextConverter<T, X> delegatingConverter;
        private final InternalBinding<T, U> delegatingBinding;

        DelegatingBinding(
            DataType<X> originalDataType,
            ContextConverter<T, X> delegatingConverter,
            ContextConverter<X, U> originalConverter,
            Function<? super ContextConverter<T, U>, ? extends InternalBinding<T, U>> f
        ) {
            super(originalDataType, originalConverter);

            this.delegatingConverter = delegatingConverter;
            this.delegatingBinding = f.apply(Converters.of(delegatingConverter, originalConverter));
        }

        @Override
        final void sqlInline0(BindingSQLContext<U> ctx, X value) throws SQLException {
            delegatingBinding.sqlInline0(ctx, delegatingConverter.to(value, ctx.converterContext()));
        }

        @Override
        final void sqlBind0(BindingSQLContext<U> ctx, X value) throws SQLException {
            delegatingBinding.sqlBind0(ctx, delegatingConverter.to(value, ctx.converterContext()));
        }

        @Override
        final void set0(BindingSetStatementContext<U> ctx, X value) throws SQLException {
            delegatingBinding.set0(ctx, delegatingConverter.to(value, ctx.converterContext()));
        }

        @Override
        final void setNull0(BindingSetStatementContext<U> ctx) throws SQLException {
            delegatingBinding.setNull0(ctx);
        }

        @Override
        final void set0(BindingSetSQLOutputContext<U> ctx, X value) throws SQLException {
            delegatingBinding.set0(ctx, delegatingConverter.to(value, ctx.converterContext()));
        }

        @Override
        final X get0(BindingGetResultSetContext<U> ctx) throws SQLException {
            return delegatingConverter.from(delegatingBinding.get0(ctx), ctx.converterContext());
        }

        @Override
        final X get0(BindingGetStatementContext<U> ctx) throws SQLException {
            return delegatingConverter.from(delegatingBinding.get0(ctx), ctx.converterContext());
        }

        @Override
        final X get0(BindingGetSQLInputContext<U> ctx) throws SQLException {
            return delegatingConverter.from(delegatingBinding.get0(ctx), ctx.converterContext());
        }

        @Override
        final int sqltype(Statement statement, Configuration configuration) throws SQLException {
            return delegatingBinding.sqltype(statement, configuration);
        }
    }

    static final class DefaultArrayBinding<U> extends InternalBinding<Object[], U> {

        private static final Set<SQLDialect> REQUIRES_JSON_CAST  = SQLDialect.supportedBy(DUCKDB);
        private static final Set<SQLDialect> REQUIRES_ARRAY_CAST = SQLDialect.supportedBy(POSTGRES, YUGABYTEDB);








        DefaultArrayBinding(DataType<Object[]> dataType, Converter<Object[], U> converter) {
            super(dataType, converter);
        }

        @SuppressWarnings("unchecked")
        @Override
        final void sqlInline0(BindingSQLContext<U> ctx, Object[] value) throws SQLException {
            String separator = "";

            if (REQUIRES_ARRAY_CAST.contains(ctx.dialect())) {

                // [#8933] In some cases, we cannot derive the cast type from
                //         array type directly
                DataType<?> arrayType =
                    dataType.getType() == Object[].class
                  ? getDataType(ctx.dialect(), deriveArrayTypeFromComponentType(value))
                  : dataType;

                ctx.render().visit(cast(inline(PostgresUtils.toPGArrayString(value)), arrayType));
            }

















            // By default, render HSQLDB syntax
            else {
                boolean squareBrackets = !NO_SUPPORT_SQUARE_BRACKETS.contains(ctx.dialect());

                ctx.render().visit(K_ARRAY);
                ctx.render().sql(squareBrackets ? '[' : '(');

                for (Object o : value) {
                    ctx.render().sql(separator);
                    binding((DataType<Object>) dataType.getArrayComponentDataType()).sql(new DefaultBindingSQLContext<>(ctx.configuration(), ctx.data(), ctx.render(), o));
                    separator = ", ";
                }

                ctx.render().sql(squareBrackets ? ']' : ')');
            }
        }

        private final Class<? extends Object[]> deriveArrayTypeFromComponentType(Object[] value) {
            for (Object o : value)
                if (o != null)
                    return arrayType(o.getClass());

            // PostgreSQL often defaults to using varchar as well, so we can
            // mimick this behaviour (without documenting it).
            return String[].class;
        }

        @Override
        final void sqlBind0(BindingSQLContext<U> ctx, Object[] value) throws SQLException {
            Cast.renderCastIf(ctx.render(),
                c -> {
                    if (REQUIRES_JSON_CAST.contains(ctx.dialect())) {
                        ctx.render().visit(K_CAST).sql('(');
                        super.sqlBind0(ctx, value);
                        ctx.render().sql(' ').visit(K_AS).sql(' ').visit(K_JSON).sql(')');
                    }









                    else
                        super.sqlBind0(ctx, value);
                },
                c -> {
                    if (REQUIRES_JSON_CAST.contains(ctx.dialect()))
                        ctx.render().sql(dataType.getCastTypeName(ctx.render().configuration()));

                    // Postgres needs explicit casting for enum (array) types
                    else if (EnumType.class.isAssignableFrom(dataType.getType().getComponentType()))
                        pgRenderEnumCast(ctx.render(), dataType.getType(), pgEnumValue(dataType.getType()));

                    // ... and also for other array types
                    else
                        ctx.render().sql(dataType.getCastTypeName(ctx.render().configuration()));
                },

                // In Postgres, some additional casting must be done in some cases...
                () -> REQUIRES_ARRAY_CAST.contains(ctx.dialect())



                   || REQUIRES_JSON_CAST.contains(ctx.dialect())
            );
        }

        @Override
        final void setNull0(BindingSetStatementContext<U> ctx) throws SQLException {
            switch (ctx.family()) {






                default:
                    super.setNull0(ctx);
                    break;
            }
        }

        @SuppressWarnings({ "rawtypes", "unchecked" })
        @Override
        final void set0(BindingSetStatementContext<U> ctx, Object[] value) throws SQLException {
            switch (ctx.family()) {









                case POSTGRES: {

                    // [#12485] Passing the array string as OTHER (OID = unspecified) may prevent poor
                    //          decisions by the PostgreSQL optimiser.
                    ctx.statement().setObject(ctx.index(), toPGArrayString(value), Types.OTHER);
                    break;
                }



                case YUGABYTEDB: {
                    ctx.statement().setString(ctx.index(), toPGArrayString(value));
                    break;
                }

                case HSQLDB: {
                    Object[] a = value;
                    Class<?> t = dataType.getType();

                    // [#2325] [#5823] Cannot bind UUID[] type in HSQLDB.
                    // See also: https://sourceforge.net/p/hsqldb/bugs/1466
                    if (t == UUID[].class) {
                        a = (Object[]) Convert.convertArray(a, byte[][].class);
                        t = byte[][].class;
                    }

                    // [#16585] Another HSQLDB bug regarding LocalTime:
                    // See also: https://sourceforge.net/p/hsqldb/bugs/1702/
                    else if (t == LocalTime[].class) {
                        a = (Object[]) Convert.convertArray(a, Time[].class);
                        t = Time[].class;
                    }

                    ctx.statement().setArray(ctx.index(), new MockArray(ctx.family(), a, t));
                    break;
                }

                // [#15732] Use JSON as a workaround to bind array types for now.
                case DUCKDB: {
                    ctx.statement().setString(ctx.index(), JSONValue.toJSONString(value));
                    break;
                }

                case CLICKHOUSE: {
                    Object[] a = value;

                    // [#7539] Work around a JDBC bug: https://github.com/ClickHouse/clickhouse-java/issues/1626
                    if (a instanceof Date[]) {
                        a = (Object[]) Convert.convertArray(a, LocalDate[].class);
                    }

                    ctx.statement().setObject(ctx.index(), a);
                    break;
                }

                case H2: {
                    ctx.statement().setObject(ctx.index(), value);
                    break;
                }

                default:
                    throw new SQLDialectNotSupportedException("Cannot bind ARRAY types in dialect " + ctx.family());
            }
        }

        @SuppressWarnings({ "rawtypes", "unchecked" })
        @Override
        final void set0(BindingSetSQLOutputContext<U> ctx, Object[] value) throws SQLException {
            ctx.output().writeArray(new MockArray(ctx.family(), value, dataType.getType()));
        }

        @Override
        final Object[] get0(BindingGetResultSetContext<U> ctx) throws SQLException {
            switch (ctx.family()) {









































                case POSTGRES:
                case YUGABYTEDB:
                    return pgGetArray(ctx, ctx.resultSet(), dataType, ctx.index());

                case HSQLDB: {

                    // [#13965] Some HSQLDB versions have trouble reading NULL values as arrays
                    //          See also: https://sourceforge.net/p/hsqldb/bugs/1662/
                    if (ctx.resultSet().getObject(ctx.index()) == null)
                        return null;

                    // However, due to a historic HSQLDB bug, we better not rely on rs.getObject() here:
                    // See https://sourceforge.net/p/hsqldb/bugs/1102/
                    else
                        return convertArray(ctx.resultSet().getArray(ctx.index()), dataType.getType());
                }

                default:
                    return convertArray(ctx.resultSet().getArray(ctx.index()), dataType.getType());
            }
        }

        @Override
        final Object[] get0(BindingGetStatementContext<U> ctx) throws SQLException {
            return convertArray(ctx.statement().getObject(ctx.index()), dataType.getType());
        }

        @Override
        final Object[] get0(BindingGetSQLInputContext<U> ctx) throws SQLException {
            Array array = ctx.input().readArray();
            return array == null ? null : (Object[]) array.getArray();
        }

        @Override
        final int sqltype(Statement statement, Configuration configuration) {
            return Types.ARRAY;
        }

        /**
         * Workarounds for the unimplemented Postgres JDBC driver features
         */
        @SuppressWarnings("unchecked")
        private static final <T> T pgGetArray(ExecuteScope ctx, ResultSet rs, DataType<T> dataType, int index) throws SQLException {

            // Get the JDBC Array and check for null. If null, that's OK
            Array array = null;
            try {

                array = rs.getArray(index);
                if (array == null)
                    return null;

                DataType<?> cdt = dataType.getArrayComponentDataType();

                // Try fetching a Java Object[]. That's gonna work for non-UDT types
                try {

                    // [#5586] [#5613] TODO: Improve PostgreSQL array deserialisation.
                    // [#5633] Special treatment for byte[][] types.
                    // [#14010] UDT arrays should skip the Convert utility
                    // [#16581] OffsetTime[] is returned as Time[] by Array::getArray
                    if (cdt.isBinary() || cdt.isUDT() || cdt.getType() == OffsetTime.class)
                        throw new ControlFlowSignal("GOTO the next array deserialisation strategy");
                    else
                        return (T) convertArray(array, (Class<? extends Object[]>) dataType.getType());
                }

                // This might be a UDT (not implemented exception...)
                catch (Exception e) {
                    List<Object> result = new ArrayList<>();

                    // Try fetching the array as a JDBC ResultSet
                    try (ResultSet arrayRs = array.getResultSet()) {
                        Binding<T, T> binding = binding((DataType<T>) dataType.getArrayComponentDataType());
                        DefaultBindingGetResultSetContext<T> out = new DefaultBindingGetResultSetContext<>(ctx.executeContext(), arrayRs, 2);

                        while (arrayRs.next()) {
                            binding.get(out);
                            result.add(out.value());
                        }
                    }

                    // That might fail too, then we don't know any further...
                    catch (Exception fatal) {
                        String string = null;
                        try {
                            string = rs.getString(index);
                        }
                        catch (SQLException ignore) {}

                        log.error("Cannot parse array", string, fatal);
                        return null;
                    }

                    return (T) convertArray(result.toArray(), (Class<? extends Object[]>) dataType.getType());
                }
            }

            finally {
                safeFree(array);
            }
        }

        private static final Object[] convertArray(Object array, Class<? extends Object[]> type) throws SQLException {
            if (array instanceof Object[])
                return Convert.convert(array, type);
            else if (array instanceof Array a)
                return convertArray(a, type);

            return null;
        }

        private static final Object[] convertArray(Array array, Class<? extends Object[]> type) throws SQLException {
            if (array != null)
                return Convert.convert(array.getArray(), type);

            return null;
        }
    }














































































































































































































































    static final class DefaultBigDecimalBinding<U> extends InternalBinding<BigDecimal, U> {
        private static final Set<SQLDialect> BIND_AS_STRING   = SQLDialect.supportedBy(SQLITE);

        DefaultBigDecimalBinding(DataType<BigDecimal> dataType, Converter<BigDecimal, U> converter) {
            super(dataType, converter);
        }

        @Override
        final void sqlInline0(BindingSQLContext<U> ctx, BigDecimal value) {
            switch (ctx.family()) {















                default:
                    ctx.render().sql(value.toString());
                    break;
            }
        }

        @Override
        final void setNull0(BindingSetStatementContext<U> ctx) throws SQLException {





            super.setNull0(ctx);
        }

        @Override
        final void set0(BindingSetStatementContext<U> ctx, BigDecimal value) throws SQLException {
            if (BIND_AS_STRING.contains(ctx.dialect()))
                ctx.statement().setString(ctx.index(), value.toString());
            else
                ctx.statement().setBigDecimal(ctx.index(), value);
        }

        @Override
        final void set0(BindingSetSQLOutputContext<U> ctx, BigDecimal value) throws SQLException {
            ctx.output().writeBigDecimal(value);
        }

        @Override
        final BigDecimal get0(BindingGetResultSetContext<U> ctx) throws SQLException {

            // The SQLite JDBC driver doesn't support BigDecimals
            if (ctx.family() == SQLDialect.SQLITE)
                return Convert.convert(ctx.resultSet().getString(ctx.index()), BigDecimal.class);
            else
                return ctx.resultSet().getBigDecimal(ctx.index());
        }

        @Override
        final BigDecimal get0(BindingGetStatementContext<U> ctx) throws SQLException {
            return ctx.statement().getBigDecimal(ctx.index());
        }

        @Override
        final BigDecimal get0(BindingGetSQLInputContext<U> ctx) throws SQLException {
            return ctx.input().readBigDecimal();
        }

        @Override
        final int sqltype(Statement statement, Configuration configuration) {
            return Types.DECIMAL;
        }
    }

    static final class DefaultBigIntegerBinding<U> extends InternalBinding<BigInteger, U> {
        private static final Set<SQLDialect> BIND_AS_STRING   = SQLDialect.supportedBy(SQLITE);

        DefaultBigIntegerBinding(DataType<BigInteger> dataType, Converter<BigInteger, U> converter) {
            super(dataType, converter);
        }

        @Override
        final void sqlInline0(BindingSQLContext<U> ctx, BigInteger value) {
            ctx.render().sql(value.toString());
        }

        @Override
        final void setNull0(BindingSetStatementContext<U> ctx) throws SQLException {





            super.setNull0(ctx);
        }

        @Override
        final void set0(BindingSetStatementContext<U> ctx, BigInteger value) throws SQLException {
            if (BIND_AS_STRING.contains(ctx.dialect()))
                ctx.statement().setString(ctx.index(), value.toString());
            else
                ctx.statement().setBigDecimal(ctx.index(), new BigDecimal(value));
        }

        @Override
        final void set0(BindingSetSQLOutputContext<U> ctx, BigInteger value) throws SQLException {
            ctx.output().writeBigDecimal(new BigDecimal(value));
        }

        @Override
        final BigInteger get0(BindingGetResultSetContext<U> ctx) throws SQLException {

            // The SQLite JDBC driver doesn't support BigDecimals
            if (ctx.family() == SQLDialect.SQLITE)
                return Convert.convert(ctx.resultSet().getString(ctx.index()), BigInteger.class);

            BigDecimal b = ctx.resultSet().getBigDecimal(ctx.index());
            return (b == null ? null : b.toBigInteger());
        }

        @Override
        final BigInteger get0(BindingGetStatementContext<U> ctx) throws SQLException {
            BigDecimal d = ctx.statement().getBigDecimal(ctx.index());
            return (d == null ? null : d.toBigInteger());
        }

        @Override
        final BigInteger get0(BindingGetSQLInputContext<U> ctx) throws SQLException {
            BigDecimal d = ctx.input().readBigDecimal();
            return (d == null ? null : d.toBigInteger());
        }

        @Override
        final int sqltype(Statement statement, Configuration configuration) {
            return Types.DECIMAL;
        }
    }

    static final class DefaultDecfloatBinding<U> extends InternalBinding<Decfloat, U> {

        DefaultDecfloatBinding(DataType<Decfloat> dataType, Converter<Decfloat, U> converter) {
            super(dataType, converter);
        }

        @Override
        final void sqlInline0(org.jooq.BindingSQLContext<U> ctx, Decfloat value) throws SQLException {

            // [#5249] [#6912] [#8063] [#11701] [#11076] Special inlining of special floating point values
            if (value.isNaN())
                ctx.render().visit(nan(ctx, DECFLOAT));
            else if (value.isPositiveInfinity())
                ctx.render().visit(infinity(ctx, DECFLOAT, false));
            else if (value.isNegativeInfinity())
                ctx.render().visit(infinity(ctx, DECFLOAT, true));
            else if (REQUIRES_LITERAL_CAST.contains(ctx.dialect()))
                ctx.render().visit(field(ctx.render().floatFormat().format(value)).cast(DECFLOAT));
            else
                ctx.render().sql(value.data());

        }

        @Override
        final void set0(BindingSetStatementContext<U> ctx, Decfloat value) throws SQLException {
            ctx.statement().setString(ctx.index(), value.data());
        }

        @Override
        final void set0(BindingSetSQLOutputContext<U> ctx, Decfloat value) throws SQLException {
            ctx.output().writeString(value.data());
        }

        @Override
        final Decfloat get0(BindingGetResultSetContext<U> ctx) throws SQLException {
            return decfloatOrNull(ctx.resultSet().getString(ctx.index()));
        }

        @Override
        final Decfloat get0(BindingGetStatementContext<U> ctx) throws SQLException {
            return decfloatOrNull(ctx.statement().getString(ctx.index()));
        }

        @Override
        final Decfloat get0(BindingGetSQLInputContext<U> ctx) throws SQLException {
            return decfloatOrNull(ctx.input().readString());
        }

        @Override
        final int sqltype(Statement statement, Configuration configuration) {
            return Types.VARCHAR;
        }
    }

    static final class DefaultBlobBinding<U> extends InternalBinding<Blob, U> {

        DefaultBlobBinding(DataType<Blob> dataType, Converter<Blob, U> converter) {
            super(dataType, converter);
        }

        @Override
        final void setNull0(BindingSetStatementContext<U> ctx) throws SQLException {





            super.setNull0(ctx);
        }

        @Override
        final void set0(BindingSetStatementContext<U> ctx, Blob value) throws SQLException {
            ctx.statement().setBlob(ctx.index(), value);
        }

        @Override
        final void set0(BindingSetSQLOutputContext<U> ctx, Blob value) throws SQLException {
            ctx.output().writeBlob(value);
        }

        @Override
        final Blob get0(BindingGetResultSetContext<U> ctx) throws SQLException {
            return ctx.resultSet().getBlob(ctx.index());
        }

        @Override
        final Blob get0(BindingGetStatementContext<U> ctx) throws SQLException {
            return ctx.statement().getBlob(ctx.index());
        }

        @Override
        final Blob get0(BindingGetSQLInputContext<U> ctx) throws SQLException {
            return ctx.input().readBlob();
        }

        @Override
        final int sqltype(Statement statement, Configuration configuration) {
            switch (configuration.family()) {
                // [#1225] [#1227] TODO Put this logic into DataType
                // Some dialects have trouble binding binary data as BLOB
                // Same logic in DefaultBytesBinding





                case POSTGRES:
                case YUGABYTEDB:
                    return Types.BINARY;

                default:
                    return Types.BLOB;
            }
        }
    }

    static final class DefaultBooleanBinding<U> extends InternalBinding<Boolean, U> {
        static final Set<SQLDialect> BIND_AS_1_0        = SQLDialect.supportedUntil(FIREBIRD, SQLITE);





        DefaultBooleanBinding(DataType<Boolean> dataType, Converter<Boolean, U> converter) {
            super(dataType, converter);
        }

        @Override
        final void sqlInline0(BindingSQLContext<U> ctx, Boolean value) {

            // [#1153] Some dialects don't support boolean literals TRUE and FALSE
            if (BIND_AS_1_0.contains(ctx.dialect()))
                ctx.render().sql(value ? "1" : "0");




            else
                ctx.render().visit(value ? K_TRUE : K_FALSE);
        }

        @Override
        final void register0(BindingRegisterContext<U> ctx) throws SQLException {









            super.register0(ctx);
        }

        @Override
        final void set0(BindingSetStatementContext<U> ctx, Boolean value) throws SQLException {
            switch (ctx.family()) {































                default:
                    ctx.statement().setBoolean(ctx.index(), value);
                    break;
            }
        }

        @Override
        final void setNull0(BindingSetStatementContext<U> ctx) throws SQLException {











            super.setNull0(ctx);
        }

        @Override
        final void set0(BindingSetSQLOutputContext<U> ctx, Boolean value) throws SQLException {
            ctx.output().writeBoolean(value);
        }

        @Override
        final Boolean get0(BindingGetResultSetContext<U> ctx) throws SQLException {
            return wasNull(ctx.resultSet(), ctx.resultSet().getBoolean(ctx.index()));
        }

        @Override
        final Boolean get0(BindingGetStatementContext<U> ctx) throws SQLException {
            return wasNull(ctx.statement(), ctx.statement().getBoolean(ctx.index()));
        }

        @Override
        final Boolean get0(BindingGetSQLInputContext<U> ctx) throws SQLException {
            return wasNull(ctx.input(), ctx.input().readBoolean());
        }

        @Override
        final int sqltype(Statement statement, Configuration configuration) throws SQLException {
            switch (configuration.family()) {












                default:
                    return Types.BOOLEAN;
            }
        }
    }

    static final class DefaultByteBinding<U> extends InternalBinding<Byte, U> {

        DefaultByteBinding(DataType<Byte> dataType, Converter<Byte, U> converter) {
            super(dataType, converter);
        }

        @Override
        final void setNull0(BindingSetStatementContext<U> ctx) throws SQLException {





            super.setNull0(ctx);
        }

        @Override
        final void sqlInline0(BindingSQLContext<U> ctx, Byte value) {
            ctx.render().sql(value);
        }

        @Override
        final void set0(BindingSetStatementContext<U> ctx, Byte value) throws SQLException {
            ctx.statement().setByte(ctx.index(), value);
        }

        @Override
        final void set0(BindingSetSQLOutputContext<U> ctx, Byte value) throws SQLException {
            ctx.output().writeByte(value);
        }

        @Override
        final Byte get0(BindingGetResultSetContext<U> ctx) throws SQLException {
            return wasNull(ctx.resultSet(), ctx.resultSet().getByte(ctx.index()));
        }

        @Override
        final Byte get0(BindingGetStatementContext<U> ctx) throws SQLException {
            return wasNull(ctx.statement(), ctx.statement().getByte(ctx.index()));
        }

        @Override
        final Byte get0(BindingGetSQLInputContext<U> ctx) throws SQLException {
            return wasNull(ctx.input(), ctx.input().readByte());
        }

        @Override
        final int sqltype(Statement statement, Configuration configuration) {








            return Types.TINYINT;
        }
    }

    static final class DefaultBytesBinding<U> extends InternalBinding<byte[], U> {

        // [#12956] Starting from H2 2.0, we can't use byte[] for BLOB anymore, if they're
        //          larger than 1MB
        private final BlobBinding blobs;

        DefaultBytesBinding(DataType<byte[]> dataType, Converter<byte[], U> converter) {
            super(dataType, converter);

            this.blobs = new BlobBinding();
        }

        @Override
        final void setNull0(BindingSetStatementContext<U> ctx) throws SQLException {
            switch (ctx.family()) {










                default:
                    super.setNull0(ctx);
                    break;
            }
        }

        @Override
        void sqlBind0(org.jooq.BindingSQLContext<U> ctx, byte[] value) throws SQLException {
            switch (ctx.family()) {








                default:
                    super.sqlBind0(ctx, value);
                    break;
            }
        }

        @Override
        final void sqlInline0(BindingSQLContext<U> ctx, byte[] value) {
            // [#1154] Binary data cannot always be inlined

            switch (ctx.family()) {
                case DUCKDB:
                    ctx.render()
                       .visit(K_CAST)
                       .sql("('")
                       .sql(escapeHexDigitPairs(convertBytesToHex(value)))
                       .sql("' ")
                       .visit(K_AS)
                       .sql(' ')
                       .visit(BLOB)
                       .sql(')');

                    break;

                case DERBY:
                    ctx.render()
                       .visit(K_CAST)
                       .sql("(X'")
                       .sql(convertBytesToHex(value))
                       .sql("' ")
                       .visit(K_AS)
                       .sql(' ')
                       .visit(BLOB)
                       .sql(')');

                    break;








































                case POSTGRES:
                case YUGABYTEDB:
                    Cast.renderCast(ctx.render(),
                        c -> c.sql("E'").sql(PostgresUtils.toPGString(value)).sql("'"),
                        c -> c.visit(N_BYTEA)
                    );
                    break;

                default:
                    ctx.render()
                       .sql("X'")
                       .sql(convertBytesToHex(value))
                       .sql('\'');

                    break;
            }
        }

        final String escapeHexDigitPairs(String hex) {
            StringBuilder sb = new StringBuilder();
            int l = hex.length();

            for (int i = 0; i < l; i += 2)
                sb.append("\\x").append(hex.charAt(i)).append(hex.charAt(i + 1));

            return sb.toString();
        }

        @Override
        final void set0(BindingSetStatementContext<U> ctx, byte[] value) throws SQLException {
            switch (ctx.family()) {





                case H2:
                    blobs.set(new DefaultBindingSetStatementContext<>(ctx.executeContext(), ctx.statement(), ctx.index(), value));
                    break;

                default:
                    ctx.statement().setBytes(ctx.index(), value);
                    break;
            }
        }

        @Override
        final void set0(BindingSetSQLOutputContext<U> ctx, byte[] value) throws SQLException {









            ctx.output().writeBytes(value);
        }

        @Override
        final byte[] get0(BindingGetResultSetContext<U> ctx) throws SQLException {
            switch (ctx.family()) {
                case DUCKDB:
                case H2:
                    DefaultBindingGetResultSetContext<byte[]> x = new DefaultBindingGetResultSetContext<>(ctx.executeContext(), ctx.resultSet(), ctx.index());
                    blobs.get(x);
                    return x.value();

                default:
                    return ctx.resultSet().getBytes(ctx.index());
            }
        }

        @Override
        final byte[] get0(BindingGetStatementContext<U> ctx) throws SQLException {
            switch (ctx.family()) {
                case DUCKDB:
                case H2:
                    DefaultBindingGetStatementContext<byte[]> x = new DefaultBindingGetStatementContext<>(ctx.executeContext(), ctx.statement(), ctx.index());
                    blobs.get(x);
                    return x.value();

                default:
                    return ctx.statement().getBytes(ctx.index());
            }
        }

        @Override
        final byte[] get0(BindingGetSQLInputContext<U> ctx) throws SQLException {









            return ctx.input().readBytes();
        }

        @Override
        final int sqltype(Statement statement, Configuration configuration) {
            switch (configuration.family()) {
                // [#1225] [#1227] TODO Put this logic into DataType
                // Some dialects have trouble binding binary data as BLOB
                // Same logic in DefaultBlobBinding





                case POSTGRES:
                case YUGABYTEDB:
                    return Types.BINARY;

                default:
                    return Types.BLOB;
            }
        }
    }

    static final class DefaultClobBinding<U> extends InternalBinding<Clob, U> {

        DefaultClobBinding(DataType<Clob> dataType, Converter<Clob, U> converter) {
            super(dataType, converter);
        }

        @Override
        final void setNull0(BindingSetStatementContext<U> ctx) throws SQLException {





            super.setNull0(ctx);
        }

        @Override
        final void set0(BindingSetStatementContext<U> ctx, Clob value) throws SQLException {
            ctx.statement().setClob(ctx.index(), value);
        }

        @Override
        final void set0(BindingSetSQLOutputContext<U> ctx, Clob value) throws SQLException {
            ctx.output().writeClob(value);
        }

        @Override
        final Clob get0(BindingGetResultSetContext<U> ctx) throws SQLException {
            return ctx.resultSet().getClob(ctx.index());
        }

        @Override
        final Clob get0(BindingGetStatementContext<U> ctx) throws SQLException {
            return ctx.statement().getClob(ctx.index());
        }

        @Override
        final Clob get0(BindingGetSQLInputContext<U> ctx) throws SQLException {
            return ctx.input().readClob();
        }

        @Override
        final int sqltype(Statement statement, Configuration configuration) {
            return Types.CLOB;
        }
    }

    static final class DefaultDateBinding<U> extends InternalBinding<Date, U> {
        private static final Set<SQLDialect> INLINE_AS_STRING_LITERAL = SQLDialect.supportedBy(SQLITE);

        DefaultDateBinding(DataType<Date> dataType, Converter<Date, U> converter) {
            super(dataType, converter);
        }

        @Override
        final void setNull0(BindingSetStatementContext<U> ctx) throws SQLException {
            switch (ctx.family()) {





                default:
                    super.setNull0(ctx);
                    break;
            }
        }

        @Override
        final void sqlInline0(BindingSQLContext<U> ctx, Date value) {
            // [#1156] DATE / TIME inlining is very vendor-specific

            // The SQLite JDBC driver does not implement the escape syntax
            // [#1253] Sybase does not implement date literals
            if (INLINE_AS_STRING_LITERAL.contains(ctx.dialect()))
                ctx.render().sql('\'').sql(escape(value, ctx.render())).sql('\'');












            // [#1253] Derby doesn't support the standard literal
            else if (ctx.family() == DERBY)
                ctx.render().visit(K_DATE).sql("('").sql(escape(value, ctx.render())).sql("')");







            // [#16498] Special cases where the standard datetime literal prefix needs to be omitted
            //          See: https://bugs.mysql.com/bug.php?id=114450
            else if (ctx.data(DATA_OMIT_DATETIME_LITERAL_PREFIX) != null)
                ctx.render().sql('\'').sql(format(value, ctx.render())).sql('\'');

            // Most dialects implement SQL standard date literals
            else
                ctx.render().visit(K_DATE).sql(" '").sql(format(value, ctx.render())).sql('\'');
        }

        private final String format(Date value, RenderContext render) {



            if (render.family() == POSTGRES)
                if (value.getTime() == PG_DATE_POSITIVE_INFINITY)
                    return "infinity";
                else if (value.getTime() == PG_DATE_NEGATIVE_INFINITY)
                    return "-infinity";

            // [#9968] JDBC's java.sql.Date formats years as YYYY, which is wrong when the
            //         year exceeds 10000. We could use java.time.LocalDate, but that's not
            //         available in jOOQ's Java 6 distribution.
            if (value.getYear() + 1900 >= 10000)
                return (value.getYear() + 1900) +
                            "-" + leftPad("" + (value.getMonth() + 1), 2, '0') +
                            "-" + leftPad("" + value.getDate(), 2, '0');
            else
                return escape(value, render);
        }

        @Override
        final void sqlBind0(BindingSQLContext<U> ctx, Date value) throws SQLException {
            switch (ctx.family()) {










                default:
                    super.sqlBind0(ctx, value);
                    break;
            }
        }

        @Override
        final void register0(BindingRegisterContext<U> ctx) throws SQLException {
            switch (ctx.family()) {







                default:
                    super.register0(ctx);
                    break;
            }
        }

        @Override
        final void set0(BindingSetStatementContext<U> ctx, Date value) throws SQLException {
            switch (ctx.family()) {

                // DuckDB doesn't support setDate() yet: https://github.com/duckdb/duckdb/discussions/7207
                case DUCKDB:

                // SQLite's type affinity needs special care...
                case SQLITE:
                    ctx.statement().setString(ctx.index(), value.toString());
                    break;








                default:
                    ctx.statement().setDate(ctx.index(), value);
                    break;
            }
        }

        @Override
        final void set0(BindingSetSQLOutputContext<U> ctx, Date value) throws SQLException {
            switch (ctx.family()) {








                default:
                    ctx.output().writeDate(value);
                    break;
            }
        }

        @Override
        final Date get0(BindingGetResultSetContext<U> ctx) throws SQLException {
            switch (ctx.family()) {

                // SQLite's type affinity needs special care...
                case SQLITE: {
                    String date = ctx.resultSet().getString(ctx.index());
                    return date == null ? null : new Date(parse(Date.class, date));
                }









                default:
                    return ctx.resultSet().getDate(ctx.index());
            }
        }

        @Override
        final Date get0(BindingGetStatementContext<U> ctx) throws SQLException {
            switch (ctx.family()) {








                default:
                    return ctx.statement().getDate(ctx.index());
            }
        }

        @Override
        final Date get0(BindingGetSQLInputContext<U> ctx) throws SQLException {
            switch (ctx.family()) {








                default:
                    return ctx.input().readDate();
            }
        }

        @Override
        final int sqltype(Statement statement, Configuration configuration) {
            switch (configuration.family()) {






                default:
                    return Types.DATE;
            }
        }
    }

    static final class DefaultDayToSecondBinding<U> extends InternalBinding<DayToSecond, U> {
        private static final Set<SQLDialect> REQUIRE_PG_INTERVAL       = SQLDialect.supportedBy(POSTGRES, YUGABYTEDB);
        private static final Set<SQLDialect> REQUIRE_STANDARD_INTERVAL = SQLDialect.supportedBy(H2, TRINO);

        DefaultDayToSecondBinding(DataType<DayToSecond> dataType, Converter<DayToSecond, U> converter) {
            super(dataType, converter);
        }

        @Override
        void sqlInline0(BindingSQLContext<U> ctx, DayToSecond value) throws SQLException {

            // [#566] Interval data types are best bound as Strings
            if (REQUIRE_PG_INTERVAL.contains(ctx.dialect()))
                ctx.render().visit(inline(toPGInterval(value).toString()));

            // [#11485] Truncate the nanosecond precision to (8) as Trino doesn't accept the usual precision of (9)
            //          https://trino.io/docs/current/functions/datetime.html#extraction-function
            else if (ctx.family() == TRINO)
                ctx.render().sql(renderDTS(ctx, value, i -> apply(i.toString(), s -> s.substring(0, s.length() - 1))));
            else
                super.sqlInline0(ctx, value);
        }

        @Override
        final void set0(BindingSetStatementContext<U> ctx, DayToSecond value) throws SQLException {

            // [#566] Interval data types are best bound as Strings
            if (REQUIRE_PG_INTERVAL.contains(ctx.dialect()))
                ctx.statement().setString(ctx.index(), toPGInterval(value).toString());
            else
                ctx.statement().setString(ctx.index(), renderDTS(ctx, value));
        }

        @Override
        final void set0(BindingSetSQLOutputContext<U> ctx, DayToSecond value) throws SQLException {
            ctx.output().writeString(renderDTS(ctx, value));
        }

        @Override
        final DayToSecond get0(BindingGetResultSetContext<U> ctx) throws SQLException {
            if (REQUIRE_PG_INTERVAL.contains(ctx.dialect()))
                return toDayToSecond(ctx.resultSet().getString(ctx.index()));
            else
                return parseDTS(ctx, ctx.resultSet().getString(ctx.index()));
        }

        @Override
        final DayToSecond get0(BindingGetStatementContext<U> ctx) throws SQLException {
            if (REQUIRE_PG_INTERVAL.contains(ctx.dialect()))
                return toDayToSecond(ctx.statement().getString(ctx.index()));
            else
                return parseDTS(ctx, ctx.statement().getString(ctx.index()));
        }

        @Override
        final DayToSecond get0(BindingGetSQLInputContext<U> ctx) throws SQLException {
            return parseDTS(ctx, ctx.input().readString());
        }

        @SuppressWarnings("unchecked")
        private final DayToSecond parseDTS(Scope scope, String string) {
            if (string == null)
                return null;
            else if (REQUIRE_STANDARD_INTERVAL.contains(scope.dialect()) && string.startsWith("INTERVAL"))
                return ((Param<DayToSecond>) scope.dsl().parser().parseField(string)).getValue();
            else
                return DayToSecond.valueOf(string);
        }

        private final String renderDTS(Scope scope, DayToSecond dts) {
            return renderDTS(scope, dts, Object::toString);
        }

        private final String renderDTS(Scope scope, DayToSecond dts, Function<? super DayToSecond, ? extends String> toString) {
            if (dts == null)
                return null;
            else if (REQUIRE_STANDARD_INTERVAL.contains(scope.dialect()))
                return "INTERVAL '" + toString.apply(dts) + "' DAY TO SECOND";
            else
                return toString.apply(dts);
        }

        @Override
        final int sqltype(Statement statement, Configuration configuration) {
            return Types.VARCHAR;
        }
    }

    static final class DefaultDoubleBinding<U> extends InternalBinding<Double, U> {
        static final Set<SQLDialect> REQUIRES_LITERAL_CAST = SQLDialect.supportedBy(H2);

        DefaultDoubleBinding(DataType<Double> dataType, Converter<Double, U> converter) {
            super(dataType, converter);
        }


























        @SuppressWarnings({ "unchecked", "rawtypes" })
        static final Field<?> nan(BindingSQLContext<?> ctx, DataType<?> type) {
            switch (ctx.family()) {
                case FIREBIRD:
                    if (type.isDecimal())
                        return inline("NaN").cast(type);
                    else
                        return log(inline(1), inline(1));
                case HSQLDB:
                    return inline(0.0).div(field("0.0e0", (DataType) type));
                default:
                    return inline("NaN").cast(type);
            }
        }

        @SuppressWarnings({ "unchecked", "rawtypes" })
        static final Field<?> infinity(BindingSQLContext<?> ctx, DataType<?> type, boolean negative) {
            switch (ctx.family()) {
                case FIREBIRD:
                    if (type.isDecimal())
                        return inline(negative ? "-Infinity" : "Infinity").cast(type);
                    else
                        return log(negative ? inline(0.5) : inline(1.5), inline(1));
                case HSQLDB:
                    return inline(negative ? -1.0 : 1.0).div(field("0.0e0", (DataType) type));
                default:
                    return inline(negative ? "-Infinity" : "Infinity").cast(type);
            }
        }

        static final Double fixInfinity(
            Scope scope,
            ThrowingSupplier<Double, SQLException> doubleSupplier,
            ThrowingSupplier<String, SQLException> stringSupplier
        ) throws SQLException {
            return fixInfinity(scope, doubleSupplier, stringSupplier, () -> Double.POSITIVE_INFINITY, () -> Double.NEGATIVE_INFINITY);
        }

        static final <T> T fixInfinity(
            Scope scope,
            ThrowingSupplier<T, SQLException> doubleSupplier,
            ThrowingSupplier<String, SQLException> stringSupplier,
            Supplier<T> positive,
            Supplier<T> negative
        ) throws SQLException {
            try {
                return doubleSupplier.get();
            }
            catch (SQLException e) {












                throw e;
            }
        }

        @Override
        final void setNull0(BindingSetStatementContext<U> ctx) throws SQLException {





            super.setNull0(ctx);
        }

        @Override
        final void sqlInline0(BindingSQLContext<U> ctx, Double value) {

            // [#5249] [#6912] [#8063] [#11701] [#11076] Special inlining of special floating point values
            if (value.isNaN())
                ctx.render().visit(nan(ctx, DOUBLE));
            else if (value == Double.POSITIVE_INFINITY)
                ctx.render().visit(infinity(ctx, DOUBLE, false));
            else if (value == Double.NEGATIVE_INFINITY)
                ctx.render().visit(infinity(ctx, DOUBLE, true));
            else if (REQUIRES_LITERAL_CAST.contains(ctx.dialect()))
                ctx.render().visit(field(ctx.render().doubleFormat().format(value)).cast(DOUBLE));
            else
                ctx.render().sql(value);
        }

        @Override
        final void set0(BindingSetStatementContext<U> ctx, Double value) throws SQLException {





            ctx.statement().setDouble(ctx.index(), value);
        }

        @Override
        final void set0(BindingSetSQLOutputContext<U> ctx, Double value) throws SQLException {
            ctx.output().writeDouble(value);
        }

        @Override
        final Double get0(BindingGetResultSetContext<U> ctx) throws SQLException {
            return wasNull(ctx.resultSet(), fixInfinity(ctx,
                () -> ctx.resultSet().getDouble(ctx.index()),
                () -> ctx.resultSet().getString(ctx.index())
            ));
        }

        @Override
        final Double get0(BindingGetStatementContext<U> ctx) throws SQLException {
            return wasNull(ctx.statement(), fixInfinity(ctx,
                () -> ctx.statement().getDouble(ctx.index()),
                () -> ctx.statement().getString(ctx.index())
            ));
        }

        @Override
        final Double get0(BindingGetSQLInputContext<U> ctx) throws SQLException {
            return wasNull(ctx.input(), fixInfinity(ctx,
                () -> ctx.input().readDouble(),
                () -> ctx.input().readString()
            ));
        }

        @Override
        final int sqltype(Statement statement, Configuration configuration) {







            return Types.DOUBLE;
        }
    }

    static final class DefaultEnumTypeBinding<U> extends InternalBinding<org.jooq.EnumType, U> {
        private static final Set<SQLDialect> REQUIRE_ENUM_CAST = SQLDialect.supportedBy(POSTGRES, YUGABYTEDB);

        DefaultEnumTypeBinding(DataType<EnumType> dataType, Converter<EnumType, U> converter) {
            super(dataType, converter);
        }

        @Override
        final void sqlInline0(BindingSQLContext<U> ctx, EnumType value) throws SQLException {
            EnumType enumValue = pgEnumValue(dataType.getType());

            Cast.renderCastIf(ctx.render(),
                c -> binding(VARCHAR).sql(new DefaultBindingSQLContext<>(ctx.configuration(), ctx.data(), ctx.render(), value.getLiteral())),
                c -> pgRenderEnumCast(c, dataType.getType(), enumValue),

                // Postgres needs explicit casting for enum (array) types
                () -> REQUIRE_ENUM_CAST.contains(ctx.dialect()) && enumValue.getName() != null
            );
        }

        @Override
        final void sqlBind0(BindingSQLContext<U> ctx, EnumType value) throws SQLException {
            EnumType enumValue = pgEnumValue(dataType.getType());

            Cast.renderCastIf(ctx.render(),
                c -> super.sqlBind0(ctx, value),
                c -> pgRenderEnumCast(c, dataType.getType(), enumValue),

                // Postgres needs explicit casting for enum (array) types
                () -> REQUIRE_ENUM_CAST.contains(ctx.dialect()) && enumValue.getName() != null
            );
        }

        @Override
        final void set0(BindingSetStatementContext<U> ctx, EnumType value) throws SQLException {
            ctx.statement().setString(ctx.index(), value.getLiteral());
        }

        @Override
        final void set0(BindingSetSQLOutputContext<U> ctx, EnumType value) throws SQLException {
            ctx.output().writeString(value.getLiteral());
        }

        @Override
        final EnumType get0(BindingGetResultSetContext<U> ctx) throws SQLException {
            return getEnumType(dataType.getType(), ctx.resultSet().getString(ctx.index()));
        }

        @Override
        final EnumType get0(BindingGetStatementContext<U> ctx) throws SQLException {
            return getEnumType(dataType.getType(), ctx.statement().getString(ctx.index()));
        }

        @Override
        final EnumType get0(BindingGetSQLInputContext<U> ctx) throws SQLException {
            return getEnumType(dataType.getType(), ctx.input().readString());
        }

        @Override
        final int sqltype(Statement statement, Configuration configuration) {
            return Types.VARCHAR;
        }

        static final EnumType pgEnumValue(Class<?> type) {

            @SuppressWarnings("unchecked")
            Class<? extends EnumType> enumType = (Class<? extends EnumType>) (
                type.isArray() ? type.getComponentType() : type);

            // [#968] Don't cast "synthetic" enum types (note, val can be null!)
            // [#4427] Java Enum agnostic implementation will work for Scala also
            EnumType[] enums = Tools.enums(enumType);

            if (enums == null || enums.length == 0)
                throw new IllegalArgumentException("Not a valid EnumType : " + type);

            return enums[0];
        }

        static final void pgRenderEnumCast(Context<?> ctx, Class<?> type, EnumType value) {
            if (value.getName() != null) {
                Schema schema = using(ctx.configuration()).map(value.getSchema());

                if (schema != null && TRUE.equals(ctx.configuration().settings().isRenderSchema())) {
                    ctx.visit(schema);
                    ctx.sql('.');
                }

                ctx.visit(name(value.getName()));

                if (type.isArray())
                    ctx.sql("[]");
            }
        }

        static final <E extends EnumType> E getEnumType(Class<? extends E> type, String literal) {
            try {
                return Tools.findAny(enums(type), e -> e.getLiteral().equals(literal));
            }
            catch (Exception e) {
                throw new DataTypeException("Unknown enum literal found : " + literal);
            }
        }
    }

    static final class DefaultFloatBinding<U> extends InternalBinding<Float, U> {

        DefaultFloatBinding(DataType<Float> dataType, Converter<Float, U> converter) {
            super(dataType, converter);
        }

        static final Float fixInfinity(
            Scope scope,
            ThrowingSupplier<Float, SQLException> doubleSupplier,
            ThrowingSupplier<String, SQLException> stringSupplier
        ) throws SQLException {
            return DefaultDoubleBinding.fixInfinity(scope, doubleSupplier, stringSupplier, () -> Float.POSITIVE_INFINITY, () -> Float.NEGATIVE_INFINITY);
        }

        @Override
        final void setNull0(BindingSetStatementContext<U> ctx) throws SQLException {





            super.setNull0(ctx);
        }

        @Override
        final void sqlInline0(BindingSQLContext<U> ctx, Float value) {

            // [#5249] [#6912] [#8063] [#11701] [#11076] Special inlining of special floating point values
            if (value.isNaN())
                ctx.render().visit(nan(ctx, REAL));
            else if (value == Float.POSITIVE_INFINITY)
                ctx.render().visit(infinity(ctx, REAL, false));
            else if (value == Float.NEGATIVE_INFINITY)
                ctx.render().visit(infinity(ctx, REAL, true));
            else if (REQUIRES_LITERAL_CAST.contains(ctx.dialect()))
                ctx.render().visit(field(ctx.render().floatFormat().format(value)).cast(REAL));
            else
                ctx.render().sql(value);
        }

        @Override
        final void set0(BindingSetStatementContext<U> ctx, Float value) throws SQLException {





            ctx.statement().setFloat(ctx.index(), value);
        }

        @Override
        final void set0(BindingSetSQLOutputContext<U> ctx, Float value) throws SQLException {
            ctx.output().writeFloat(value);
        }

        @Override
        final Float get0(BindingGetResultSetContext<U> ctx) throws SQLException {
            return wasNull(ctx.resultSet(), fixInfinity(ctx,
                () -> ctx.resultSet().getFloat(ctx.index()),
                () -> ctx.resultSet().getString(ctx.index())
            ));
        }

        @Override
        final Float get0(BindingGetStatementContext<U> ctx) throws SQLException {
            return wasNull(ctx.statement(), fixInfinity(ctx,
                () -> ctx.statement().getFloat(ctx.index()),
                () -> ctx.statement().getString(ctx.index())
            ));
        }

        @Override
        final Float get0(BindingGetSQLInputContext<U> ctx) throws SQLException {
            return wasNull(ctx.input(), fixInfinity(ctx,
                () -> ctx.input().readFloat(),
                () -> ctx.input().readString()
            ));
        }

        @Override
        final int sqltype(Statement statement, Configuration configuration) {







            return Types.FLOAT;
        }
    }

    static final class DefaultIntegerBinding<U> extends InternalBinding<Integer, U> {

        DefaultIntegerBinding(DataType<Integer> dataType, Converter<Integer, U> converter) {
            super(dataType, converter);
        }

        @Override
        final void setNull0(BindingSetStatementContext<U> ctx) throws SQLException {





            super.setNull0(ctx);
        }

        @Override
        final void sqlInline0(BindingSQLContext<U> ctx, Integer value) {
            ctx.render().sql(value);
        }

        @Override
        final void set0(BindingSetStatementContext<U> ctx, Integer value) throws SQLException {
            ctx.statement().setInt(ctx.index(), value);
        }

        @Override
        final void set0(BindingSetSQLOutputContext<U> ctx, Integer value) throws SQLException {
            ctx.output().writeInt(value);
        }

        @Override
        final Integer get0(BindingGetResultSetContext<U> ctx) throws SQLException {
            return wasNull(ctx.resultSet(), ctx.resultSet().getInt(ctx.index()));
        }

        @Override
        final Integer get0(BindingGetStatementContext<U> ctx) throws SQLException {
            return wasNull(ctx.statement(), ctx.statement().getInt(ctx.index()));
        }

        @Override
        final Integer get0(BindingGetSQLInputContext<U> ctx) throws SQLException {
            return wasNull(ctx.input(), ctx.input().readInt());
        }

        @Override
        final int sqltype(Statement statement, Configuration configuration) {





            return Types.INTEGER;
        }
    }

    static final class DefaultLongBinding<U> extends InternalBinding<Long, U> {

        DefaultLongBinding(DataType<Long> dataType, Converter<Long, U> converter) {
            super(dataType, converter);
        }

        @Override
        final void setNull0(BindingSetStatementContext<U> ctx) throws SQLException {





            super.setNull0(ctx);
        }

        @Override
        final void sqlInline0(BindingSQLContext<U> ctx, Long value) {
            ctx.render().sql(value);
        }

        @Override
        final void set0(BindingSetStatementContext<U> ctx, Long value) throws SQLException {






            ctx.statement().setLong(ctx.index(), value);
        }

        @Override
        final void set0(BindingSetSQLOutputContext<U> ctx, Long value) throws SQLException {
            ctx.output().writeLong(value);
        }

        @Override
        final Long get0(BindingGetResultSetContext<U> ctx) throws SQLException {
            return wasNull(ctx.resultSet(), ctx.resultSet().getLong(ctx.index()));
        }

        @Override
        final Long get0(BindingGetStatementContext<U> ctx) throws SQLException {
            return wasNull(ctx.statement(), ctx.statement().getLong(ctx.index()));
        }

        @Override
        final Long get0(BindingGetSQLInputContext<U> ctx) throws SQLException {
            return wasNull(ctx.input(), ctx.input().readLong());
        }

        @Override
        final int sqltype(Statement statement, Configuration configuration) {





            return Types.BIGINT;
        }
    }

    /**
     * [#7986] OffsetDateTime.parse() is way too slow and not lenient enough to
     * handle all the possible RDBMS output formats and quirks.
     */
    static final class OffsetDateTimeParser {
        static final OffsetTime offsetTime(String string) {
            if (string == null)
                return null;

            // Out parameter emulation
            int[] position = { 0 };
            return OffsetTime.of(parseLocalTime(string, position), parseOffset(string, position));
        }

        static final OffsetDateTime offsetDateTime(String string) {
            if (string == null)
                return null;

            // Out parameter emulation
            int[] position = { 0 };

            LocalDate d = parseLocalDate(string, position);

            // [#4338] SQL supports the alternative ISO 8601 date format, where a
            // whitespace character separates date and time. java.time does not
            parseAnyChar(string, position, " T");
            LocalTime t = parseLocalTime(string, position);

            ZoneOffset offset = parseOffset(string, position);

            // [#8178] PostgreSQL doesn't support negative years but expects the
            //         AD / BC notation
            return parseBCIf(string, position)
                 ? OffsetDateTime.of(d.withYear(1 - d.getYear()), t, offset)
                 : OffsetDateTime.of(d, t, offset);
        }

        static final LocalDate parseLocalDate(String string, int[] position) {
            int year = parseInt(string, position, 10);

            parseChar(string, position, '-');
            int month = parseInt(string, position, 2);

            parseChar(string, position, '-');
            int day = parseInt(string, position, 2);

            return LocalDate.of(year, month, day);
        }

        static final LocalTime parseLocalTime(String string, int[] position) {
            int hour = parseInt(string, position, 2);

            // [#5895] HSQLDB seems to confuse 00:00:00+02:00 with 24:00:00+02:00
            // https://sourceforge.net/p/hsqldb/bugs/1523/
            if (hour == 24)
                hour = hour % 24;

            parseChar(string, position, ':');
            int minute = parseInt(string, position, 2);
            int second = 0;
            int nano = 0;

            if (parseCharIf(string, position, ':')) {
                second = parseInt(string, position, 2);

                if (parseCharIf(string, position, '.'))
                    nano = parseInt(string, position, 9, true);
            }

            return LocalTime.of(hour, minute, second, nano);
        }

        private static final ZoneOffset parseOffset(String string, int[] position) {
            int offsetHours = 0;
            int offsetMinutes = 0;
            int offsetSeconds = 0;

            if (!parseCharIf(string, position, 'Z')) {

                // [#4965] Oracle might return some spare space
                while (parseCharIf(string, position, ' '))
                    ;

                boolean minus = parseCharIf(string, position, '-');
                boolean plus = !minus && parseCharIf(string, position, '+');

                if (minus || plus) {
                    offsetHours = parseInt(string, position, 1);

                    // [#4965] Oracle might return a single-digit hour offset
                    if (Character.isDigit(string.charAt(position[0])))
                        offsetHours = offsetHours * 10 + parseInt(string, position, 1);

                    // [#4338] [#5180] [#5776] PostgreSQL is more lenient regarding the offset format
                    if (parseCharIf(string, position, ':'))
                        offsetMinutes = parseInt(string, position, 2);

                    // [#8181] In some edge cases, there might also be a seconds offset
                    if (parseCharIf(string, position, ':'))
                        offsetSeconds = parseInt(string, position, 2);

                    if (minus) {
                        offsetHours = -offsetHours;
                        offsetMinutes = -offsetMinutes;
                        offsetSeconds = -offsetSeconds;
                    }
                }
            }

            return ZoneOffset.ofHoursMinutesSeconds(offsetHours, offsetMinutes, offsetSeconds);
        }

        private static final void parseAnyChar(String string, int[] position, String expected) {
            for (int i = 0; i < expected.length(); i++) {
                if (string.charAt(position[0]) == expected.charAt(i)) {
                    position[0] = position[0] + 1;
                    return;
                }
            }

            throw new IllegalArgumentException("Expected any of \"" + expected + "\" at position " + position[0] + " in " + string);
        }

        private static final boolean parseBCIf(String string, int[] position) {
            return parseCharIf(string, position, ' ')
                && parseCharIf(string, position, 'B')
                && parseCharIf(string, position, 'C');
        }

        private static final boolean parseCharIf(String string, int[] position, char expected) {
            boolean result = string.length() > position[0] && string.charAt(position[0]) == expected;

            if (result)
                position[0] = position[0] + 1;

            return result;
        }

        private static final void parseChar(String string, int[] position, char expected) {
            if (!parseCharIf(string, position, expected))
                throw new IllegalArgumentException("Expected '" + expected + "' at position " + position[0] + " in " + string);
        }

        private static final int parseInt(String string, int[] position, int maxLength) {
            return parseInt(string, position, maxLength, false);
        }

        private static final int parseInt(String string, int[] position, int maxLength, boolean rightPad) {
            int result = 0;
            int pos = position[0];
            int length;

            for (length = 0; length < maxLength && (pos + length) < string.length(); length++) {
                char c = string.charAt(pos + length);

                // [#11485] Some RDBMS seem to prepend + to large years, e.g. +10000-01-01
                if (c == '+' && length == 0)
                    continue;

                int digit = c - '0';

                if (digit >= 0 && digit < 10)
                    result = result * 10 + digit;
                else
                    break;
            }

            if (rightPad && length < maxLength && result > 0)
                for (int i = length; i < maxLength; i++)
                    result = result * 10;

            position[0] = pos + length;
            return result;
        }
    }

    static final class DefaultOffsetDateTimeBinding<U> extends InternalBinding<OffsetDateTime, U> {









        DefaultOffsetDateTimeBinding(DataType<OffsetDateTime> dataType, Converter<OffsetDateTime, U> converter) {
            super(dataType, converter);
        }

        @Override
        final void setNull0(BindingSetStatementContext<U> ctx) throws SQLException {





            super.setNull0(ctx);
        }

        @Override
        final void sqlInline0(BindingSQLContext<U> ctx, OffsetDateTime value) {
            SQLDialect family = ctx.family();

            switch (family) {

                // [#5895] HSQLDB derives the specific data type from the literal


                case FIREBIRD:
                case HSQLDB:
                case TRINO:
                    ctx.render().visit(K_TIMESTAMP).sql(" '").sql(escape(format(value, family), ctx.render())).sql('\'');
                    break;

                // [#8735] SQLite renders as ISO formatted string literals
                case SQLITE:
                    ctx.render().sql('\'').sql(escape(format(value, family), ctx.render())).sql('\'');
                    break;









                // Some dialects implement SQL standard time literals
                default:
                    ctx.render().visit(K_TIMESTAMP_WITH_TIME_ZONE).sql(" '").sql(escape(format(value, family), ctx.render())).sql('\'');
                    break;
            }
        }

        @Override
        final void sqlBind0(BindingSQLContext<U> ctx, OffsetDateTime value) throws SQLException {
            switch (ctx.family()) {

                // [#17088] The R2DBC H2 driver binds strings as CLOB, which cannot be converted to TIMESTAMPTZ
                case H2:
                    if (isR2dbc(ctx)) {
                        Cast.renderCast(ctx.render(),
                            c -> super.sqlBind0(ctx, value),
                            c -> c.sql(VARCHAR.getCastTypeName(c.configuration()))
                        );
                    }
                    else
                        super.sqlBind0(ctx, value);

                    break;

                default:
                    super.sqlBind0(ctx, value);
                    break;
            }
        }

        @Override
        final void set0(BindingSetStatementContext<U> ctx, OffsetDateTime value) throws SQLException {
            SQLDialect family = ctx.family();

            if (!FALSE.equals(ctx.settings().isBindOffsetDateTimeType()))
                ctx.statement().setObject(ctx.index(), value);







            else if (family == FIREBIRD)
                ctx.statement().setString(ctx.index(), value.toString());

            else
                ctx.statement().setString(ctx.index(), format(value, family));
        }

        @Override
        final void set0(BindingSetSQLOutputContext<U> ctx, OffsetDateTime value) throws SQLException {

            if (!FALSE.equals(ctx.settings().isBindOffsetDateTimeType()))
                ctx.output().writeObject(value, JDBCType.TIMESTAMP_WITH_TIMEZONE);






            else
                throw new UnsupportedOperationException("Type " + dataType + " is not supported");
        }

        @Override
        final OffsetDateTime get0(BindingGetResultSetContext<U> ctx) throws SQLException {
            if (!FALSE.equals(ctx.settings().isBindOffsetDateTimeType()))
                return ctx.resultSet().getObject(ctx.index(), OffsetDateTime.class);










            else
                return OffsetDateTimeParser.offsetDateTime(ctx.resultSet().getString(ctx.index()));
        }

        @Override
        final OffsetDateTime get0(BindingGetStatementContext<U> ctx) throws SQLException {
            if (!FALSE.equals(ctx.settings().isBindOffsetDateTimeType()))
                return ctx.statement().getObject(ctx.index(), OffsetDateTime.class);






            else
                return OffsetDateTimeParser.offsetDateTime(ctx.statement().getString(ctx.index()));
        }

        @Override
        final OffsetDateTime get0(BindingGetSQLInputContext<U> ctx) throws SQLException {

            if (!FALSE.equals(ctx.settings().isBindOffsetDateTimeType()))
                return ctx.input().readObject(OffsetDateTime.class);















            else
                throw new UnsupportedOperationException("Type " + dataType + " is not supported");
        }

        @Override
        final int sqltype(Statement statement, Configuration configuration) {

            // [#5779] [#9902] Use the JDBC 4.2 TIME[STAMP]_WITH_TIMEZONE types by default
            if (!FALSE.equals(configuration.settings().isBindOffsetDateTimeType()))
                return Types.TIMESTAMP_WITH_TIMEZONE;











            // [#5779] Revert to encoding this type as string.
            else
                return Types.VARCHAR;
        }













        private static final DateTimeFormatter F_TIMESTAMPTZ = new DateTimeFormatterBuilder()
            .appendValue(YEAR, 4, 10, SignStyle.NORMAL)
            .appendLiteral('-')
            .appendValue(MONTH_OF_YEAR, 2)
            .appendLiteral('-')
            .appendValue(DAY_OF_MONTH, 2)
            .appendLiteral(' ')
            .appendValue(HOUR_OF_DAY, 2)
            .appendLiteral(':')
            .appendValue(MINUTE_OF_HOUR, 2)
            .appendLiteral(':')
            .appendValue(SECOND_OF_MINUTE, 2)
            .appendFraction(NANO_OF_SECOND, 0, 9, true)
            .appendOffset("+HH:MM", "+00:00")
            .toFormatter();

        private static final String format(OffsetDateTime val, SQLDialect family) {
            if (family == POSTGRES)
                if (val.toEpochSecond() * 1000 == PG_DATE_POSITIVE_INFINITY)
                    return "infinity";
                else if (val.toEpochSecond() * 1000 == PG_DATE_NEGATIVE_INFINITY)
                    return "-infinity";

            // [#8178] Replace negative dates by AD/BC notation in PostgreSQL
            if (family == POSTGRES && val.getYear() <= 0)
                return formatEra(val);

            // Remove the ISO standard T character, as some databases don't like that
            // Replace the ISO standard Z character for UTC, as some databases don't like that
            return val.format(F_TIMESTAMPTZ);
        }

        private static final String formatISO(OffsetDateTime val) {
            return val.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        }

        private static final DateTimeFormatter ERA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.nnnnnnnnnZZZZZ G", Locale.US);

        private static final String formatEra(OffsetDateTime val) {
            return val.format(ERA);
        }
    }

    static final class DefaultOffsetTimeBinding<U> extends InternalBinding<OffsetTime, U> {

        DefaultOffsetTimeBinding(DataType<OffsetTime> dataType, Converter<OffsetTime, U> converter) {
            super(dataType, converter);
        }

        @Override
        final void setNull0(BindingSetStatementContext<U> ctx) throws SQLException {





            super.setNull0(ctx);
        }

        @Override
        final void sqlInline0(BindingSQLContext<U> ctx, OffsetTime value) {

            switch (ctx.family()) {
                // [#5895] HSQLDB derives the specific data type from the literal
                case FIREBIRD:
                case HSQLDB:
                case TRINO:
                    ctx.render().visit(K_TIME).sql(" '").sql(escape(format(value), ctx.render())).sql('\'');
                    break;

                // [#8735] SQLite renders as ISO formatted string literals
                case SQLITE:
                    ctx.render().sql('\'').sql(escape(format(value), ctx.render())).sql('\'');
                    break;







                // Some dialects implement SQL standard time literals
                default:
                    ctx.render().visit(K_TIME_WITH_TIME_ZONE).sql(" '").sql(escape(format(value), ctx.render())).sql('\'');
                    break;
            }
        }

        @Override
        final void set0(BindingSetStatementContext<U> ctx, OffsetTime value) throws SQLException {
            if (FALSE.equals(ctx.settings().isBindOffsetTimeType())) {
                String string = format(value);







                ctx.statement().setString(ctx.index(), string);
            }

            else if (ctx.family() == FIREBIRD)
                ctx.statement().setString(ctx.index(), value.toString());
            else
                ctx.statement().setObject(ctx.index(), value);
        }

        @Override
        final void set0(BindingSetSQLOutputContext<U> ctx, OffsetTime value) throws SQLException {
            // [#6630] TODO support this type
            throw new UnsupportedOperationException("Type " + dataType + " is not supported");
        }

        @Override
        final OffsetTime get0(BindingGetResultSetContext<U> ctx) throws SQLException {
            if (!FALSE.equals(ctx.settings().isBindOffsetTimeType()))
                return ctx.resultSet().getObject(ctx.index(), OffsetTime.class);
            else
                return OffsetDateTimeParser.offsetTime(ctx.resultSet().getString(ctx.index()));
        }

        @Override
        final OffsetTime get0(BindingGetStatementContext<U> ctx) throws SQLException {
            if (!FALSE.equals(ctx.settings().isBindOffsetTimeType()))
                return ctx.statement().getObject(ctx.index(), OffsetTime.class);
            else
                return OffsetDateTimeParser.offsetTime(ctx.statement().getString(ctx.index()));
        }

        @Override
        final OffsetTime get0(BindingGetSQLInputContext<U> ctx) throws SQLException {
            // [#6630] TODO support this type
            throw new UnsupportedOperationException("Type " + dataType + " is not supported");
        }

        @Override
        final int sqltype(Statement statement, Configuration configuration) {

            // [#5779] [#9902] Use the JDBC 4.2 TIME[STAMP]_WITH_TIMEZONE types by default
            if (!FALSE.equals(configuration.settings().isBindOffsetTimeType()))
                return Types.TIME_WITH_TIMEZONE;








            // [#5779] Revert to encoding this type as string.
            else
                return Types.VARCHAR;
        }

        private static final String format(OffsetTime val) {

            // Replace the ISO standard Z character for UTC, as some databases don't like that
            return StringUtils.replace(val.format(DateTimeFormatter.ISO_OFFSET_TIME), "Z", "+00:00");
        }
    }

    static final class DefaultInstantBinding<U> extends InternalBinding<Instant, U> {

        @SuppressWarnings("unchecked")
        private static final ContextConverter<OffsetDateTime, Instant> CONVERTER = ContextConverter.ofNullable(
            OffsetDateTime.class,
            Instant.class,
            (BiFunction<OffsetDateTime, ConverterContext, Instant> & Serializable) (t, x) -> t.toInstant(),
            (BiFunction<Instant, ConverterContext, OffsetDateTime> & Serializable) (i, x) -> OffsetDateTime.ofInstant(i, ZoneOffset.UTC)
        );

        private final DefaultOffsetDateTimeBinding<U>                  delegate;

        DefaultInstantBinding(DataType<Instant> dataType, Converter<Instant, U> converter) {
            super(dataType, converter);

            delegate = new DefaultOffsetDateTimeBinding<>(SQLDataType.OFFSETDATETIME, Converters.of(CONVERTER, converter()));
        }

        @Override
        final void setNull0(BindingSetStatementContext<U> ctx) throws SQLException {
            delegate.setNull0(ctx);
        }

        @Override
        final void sqlInline0(BindingSQLContext<U> ctx, Instant value) throws SQLException {
            delegate.sqlInline0(ctx, CONVERTER.to(value, ctx.converterContext()));
        }

        @Override
        final void sqlBind0(BindingSQLContext<U> ctx, Instant value) throws SQLException {
            delegate.sqlBind0(ctx, CONVERTER.to(value, ctx.converterContext()));
        }

        @Override
        final void set0(BindingSetStatementContext<U> ctx, Instant value) throws SQLException {
            delegate.set0(ctx, CONVERTER.to(value, ctx.converterContext()));
        }

        @Override
        final void set0(BindingSetSQLOutputContext<U> ctx, Instant value) throws SQLException {
            delegate.set0(ctx, CONVERTER.to(value, ctx.converterContext()));
        }

        @Override
        final Instant get0(BindingGetResultSetContext<U> ctx) throws SQLException {
            return CONVERTER.from(delegate.get0(ctx), ctx.converterContext());
        }

        @Override
        final Instant get0(BindingGetStatementContext<U> ctx) throws SQLException {
            return CONVERTER.from(delegate.get0(ctx), ctx.converterContext());
        }

        @Override
        final Instant get0(BindingGetSQLInputContext<U> ctx) throws SQLException {
            return CONVERTER.from(delegate.get0(ctx), ctx.converterContext());
        }

        @Override
        final int sqltype(Statement statement, Configuration configuration) throws SQLException {
            return delegate.sqltype(statement, configuration);
        }
    }

    static final class CommercialOnlyBinding<U> extends InternalBinding<Object, U> {
        CommercialOnlyBinding(DataType<Object> dataType, Converter<Object, U> converter) {
            super(dataType, converter);
        }

        @Override
        final void set0(BindingSetStatementContext<U> ctx, Object value) throws SQLException {
            ctx.configuration().requireCommercial(() -> "The out of the box binding for " + dataType.getName() + " is available in the commercial jOOQ distribution only. Alternatively, you can implement your own custom binding.");
        }

        @Override
        final void set0(BindingSetSQLOutputContext<U> ctx, Object value) throws SQLException {
            ctx.configuration().requireCommercial(() -> "The out of the box binding for " + dataType.getName() + " is available in the commercial jOOQ distribution only. Alternatively, you can implement your own custom binding.");
        }

        @Override
        final Object get0(BindingGetResultSetContext<U> ctx) throws SQLException {
            ctx.configuration().requireCommercial(() -> "The out of the box binding for " + dataType.getName() + " is available in the commercial jOOQ distribution only. Alternatively, you can implement your own custom binding.");
            return null;
        }

        @Override
        final Object get0(BindingGetStatementContext<U> ctx) throws SQLException {
            ctx.configuration().requireCommercial(() -> "The out of the box binding for " + dataType.getName() + " is available in the commercial jOOQ distribution only. Alternatively, you can implement your own custom binding.");
            return null;
        }

        @Override
        final Object get0(BindingGetSQLInputContext<U> ctx) throws SQLException {
            ctx.configuration().requireCommercial(() -> "The out of the box binding for " + dataType.getName() + " is available in the commercial jOOQ distribution only. Alternatively, you can implement your own custom binding.");
            return null;
        }

        @Override
        final int sqltype(Statement statement, Configuration configuration) throws SQLException {
            return Types.OTHER;
        }
    }

    static final class DefaultOtherBinding<U> extends InternalBinding<Object, U> {

        DefaultOtherBinding(DataType<Object> dataType, Converter<Object, U> converter) {
            super(dataType, converter);
        }

        @SuppressWarnings({ "unchecked", "rawtypes" })
        @Override
        final void set0(BindingSetStatementContext<U> ctx, Object value) throws SQLException {
            InternalBinding b = (InternalBinding) binding(DefaultDataType.getDataType(ctx.dialect(), value.getClass()));

            // [#7370] Prevent a stack overflow error on unsupported data types
            if (b instanceof DefaultOtherBinding)
                ctx.statement().setObject(ctx.index(), value);
            else
                b.set0(ctx, b.dataType.convert(value));
        }

        @Override
        final void setNull0(BindingSetStatementContext<U> ctx) throws SQLException {
            switch (ctx.family()) {



















                // [#729] In the absence of the correct JDBC type, try setObject
                default:
                    ctx.statement().setObject(ctx.index(), null);
                    break;
            }
        }


        @SuppressWarnings({ "unchecked", "rawtypes" })
        @Override
        void sqlInline0(BindingSQLContext<U> ctx, Object value) throws SQLException {
            Binding<?, ?> b = binding(DefaultDataType.getDataType(
                DEFAULT, (Class<Object>) value.getClass(), SQLDataType.OTHER
            ));

            if (b instanceof DefaultOtherBinding )
                super.sqlInline0(ctx, value);
            else if (b instanceof InternalBinding i)
                i.sqlInline0(ctx, value);
            else
                super.sqlInline0(ctx, value);
        }

        @Override
        final void set0(BindingSetSQLOutputContext<U> ctx, Object value) throws SQLException {
            throw new DataTypeException("Type " + dataType + " is not supported");
        }

        @Override
        final Object get0(BindingGetResultSetContext<U> ctx) throws SQLException {
            return unlob(ctx, ctx.resultSet().getObject(ctx.index()));
        }

        @Override
        final Object get0(BindingGetStatementContext<U> ctx) throws SQLException {
            return unlob(ctx, ctx.statement().getObject(ctx.index()));
        }

        @Override
        final Object get0(BindingGetSQLInputContext<U> ctx) throws SQLException {
            return unlob(ctx, ctx.input().readObject());
        }

        @Override
        final int sqltype(Statement statement, Configuration configuration) {
            return Types.OTHER;
        }

        /**
         * [#2534] Extract <code>byte[]</code> or <code>String</code> data from a
         * LOB, if the argument is a lob.
         */
        private static final Object unlob(Scope ctx, Object object) throws SQLException {
            if (object instanceof Blob blob) {
                return readBlob(ctx, blob);
            }
            else if (object instanceof Clob clob) {
                try {
                    return clob.getSubString(1, asInt(clob.length()));
                }
                finally {
                    JDBCUtils.safeFree(clob);
                }
            }

            return object;
        }
    }

    static final class DefaultRowIdBinding<U> extends InternalBinding<RowId, U> {

        DefaultRowIdBinding(DataType<RowId> dataType, Converter<RowId, U> converter) {
            super(dataType, converter);
        }

        @Override
        final void set0(BindingSetStatementContext<U> ctx, RowId value) throws SQLException {
            ctx.statement().setObject(ctx.index(), value.value());
        }

        @Override
        final void set0(BindingSetSQLOutputContext<U> ctx, RowId value) throws SQLException {
            throw new DataTypeException("Type " + dataType + " is not supported");
        }

        @Override
        final RowId get0(BindingGetResultSetContext<U> ctx) throws SQLException {
            return new RowIdImpl(ctx.resultSet().getObject(ctx.index()));
        }

        @Override
        final RowId get0(BindingGetStatementContext<U> ctx) throws SQLException {
            return new RowIdImpl(ctx.statement().getObject(ctx.index()));
        }

        @Override
        final RowId get0(BindingGetSQLInputContext<U> ctx) throws SQLException {
            throw new DataTypeException("Type " + dataType + " is not supported");
        }

        @Override
        final int sqltype(Statement statement, Configuration configuration) {
            return Types.ROWID;
        }
    }

    static final class DefaultRecordBinding<U> extends InternalBinding<Record, U> {







        static final Set<SQLDialect> REQUIRE_RECORD_CAST        = SQLDialect.supportedBy(DUCKDB, POSTGRES, YUGABYTEDB);

        DefaultRecordBinding(DataType<Record> dataType, Converter<Record, U> converter) {
            super(dataType, converter);
        }

        @Override
        final void sqlBind0(BindingSQLContext<U> ctx, Record value) throws SQLException {
            Cast.renderCastIf(ctx.render(),
                c -> super.sqlBind0(ctx, value),
                c -> pgRenderRecordCast(ctx.render()),
                () -> REQUIRE_RECORD_CAST.contains(ctx.dialect())
            );
        }

        @SuppressWarnings({ "rawtypes", "unchecked" })
        @Override
        final void sqlInline0(BindingSQLContext<U> ctx, Record value) throws SQLException {
            Cast.renderCastIf(ctx.render(),
                c -> {
                    if (REQUIRE_RECORD_CAST.contains(ctx.dialect()))
                        ctx.render().visit(inline(PostgresUtils.toPGString(value)));
                    else
                        ctx.render().visit(new QualifiedRecordConstant((QualifiedRecord) value, getRecordQualifier(dataType)));
                },
                c -> pgRenderRecordCast(ctx.render()),
                () -> REQUIRE_RECORD_CAST.contains(ctx.dialect())
            );
        }

        @SuppressWarnings({ "rawtypes", "unchecked" })
        @Override
        final void register0(BindingRegisterContext<U> ctx) throws SQLException {







            super.register0(ctx);
        }

        @Override
        final void set0(BindingSetStatementContext<U> ctx, Record value) throws SQLException {
            if (REQUIRE_RECORD_CAST.contains(ctx.dialect()) && value != null)
                ctx.statement().setString(ctx.index(), PostgresUtils.toPGString(value));
            else
                localExecuteContext(ctx.executeContext(), () -> { ctx.statement().setObject(ctx.index(), value); return null; });
        }

        @SuppressWarnings({ "rawtypes", "unchecked" })
        @Override
        final void setNull0(BindingSetStatementContext<U> ctx) throws SQLException {








            super.setNull0(ctx);
        }

        @Override
        final void set0(BindingSetSQLOutputContext<U> ctx, Record value) throws SQLException {
            if (value instanceof QualifiedRecord<?> q)
                ctx.output().writeObject(q);
            else
                throw new UnsupportedOperationException("Type " + dataType + " is not supported");
        }

        @Override
        final Record get0(BindingGetResultSetContext<U> ctx) throws SQLException {
            boolean skipDegree1 = !TRUE.equals(ctx.settings().isEmulateNestedRecordProjectionsUsingMultisetEmulation());

            switch (ctx.family()) {


                case POSTGRES:
                case YUGABYTEDB: {

                    // [#17979] Native ROW support may be overridden for various reasons
                    if (TRUE.equals(ctx.executeContext().data(DATA_MULTISET_CONTENT)))
                        return readMultiset(ctx, dataType, skipDegree1);
                    else
                        return pgNewRecord(ctx,
                            dataType.getType(),
                            (AbstractRow<Record>) dataType.getRow(),
                            ctx.resultSet().getObject(ctx.index())
                        );
                }

                case CLICKHOUSE:
                case DUCKDB: {
                    Object object = ctx.resultSet().getObject(ctx.index());

                    if (object == null)
                        return null;

                    return readMultiset(ctx, dataType, skipDegree1 && !(object instanceof Struct));
                }

                default:
                    if (UDTRecord.class.isAssignableFrom(dataType.getType()))
                        return localExecuteContext(ctx.executeContext(), () -> (Record) ctx.resultSet().getObject(ctx.index(), typeMap(dataType.getType(), ctx)));
                    else
                        return readMultiset(ctx, dataType, skipDegree1);
            }
        }

        @Override
        final Record get0(BindingGetStatementContext<U> ctx) throws SQLException {
            switch (ctx.family()) {


                case POSTGRES:
                case YUGABYTEDB:
                    return pgNewRecord(ctx, dataType.getType(), (AbstractRow<Record>) dataType.getRow(), ctx.statement().getObject(ctx.index()));

                default:
                    return localExecuteContext(ctx.executeContext(), () -> (Record) ctx.statement().getObject(ctx.index(), typeMap(dataType.getType(), ctx)));
            }
        }

        @Override
        final Record get0(BindingGetSQLInputContext<U> ctx) throws SQLException {
            return (Record) ctx.input().readObject();
        }

        @SuppressWarnings({ "unchecked", "rawtypes" })
        static final <R extends Record> R readMultiset(BindingGetResultSetContext<?> ctx, DataType<R> type, boolean skipDegree1) throws SQLException {
            AbstractRow<R> row = (AbstractRow<R>) type.getRow();
            Result<R> result;

            // [#12930] AbstractRowAsField doesn't unnecessarily nest Row1
            // [#17074] Do this only if requested (e.g. native ROW may be generated despite JSON MULTISET emulation at the top level)
            if (skipDegree1 && row.size() == 1 && emulateMultiset(ctx.configuration()) != NestedCollectionEmulation.NATIVE) {
                result = new ResultImpl<>(ctx.configuration(), row);
                result.add(newRecord(true, ctx.configuration(), (Class<R>) type.getRecordType(), row).operate(r -> {
                    DefaultBindingGetResultSetContext<?> c = new DefaultBindingGetResultSetContext<>(ctx.executeContext(), ctx.resultSet(), ctx.index());
                    r.field(0).getBinding().get((BindingGetResultSetContext) c);
                    r.fromArray(c.value());
                    r.touched(false);
                    return r;
                }));
            }
            else
                result = DefaultResultBinding.readMultiset(ctx, row, type.getType(),
                    s -> s != null && (s.startsWith("[") || s.startsWith("{")) ? "[" + s + "]" : null,
                    s -> s != null && (s.startsWith("<")) ? "<result>" + s + "</result>" : null,
                    s -> s instanceof Struct x
                         ? asList(x)
                         : s instanceof List<?> l
                         ? asList(l)
                         : null
                );

            return isEmpty(result) ? null : result.get(0);
        }

        @Override
        final int sqltype(Statement statement, Configuration configuration) {
            return Types.STRUCT;
        }

        // -------------------------------------------------------------------------
        // XXX: The following section has been added for Postgres UDT support. The
        // official Postgres JDBC driver does not implement SQLData and similar
        // interfaces. Instead, a string representation of a UDT has to be parsed
        // -------------------------------------------------------------------------

        final void pgRenderRecordCast(Context<?> ctx) {
            if (dataType instanceof UDTDataType<?> u) {
                ctx.visit(defaultIfNull(getMappedUDT(ctx, u.udt), u.udt));
            }
            else if (dataType instanceof TableDataType<?> t) {
                ctx.visit(defaultIfNull(getMappedTable(ctx, t.table), t.table));
            }
            else if (dataType.isUDT()) {
                RecordQualifier<?> q = getRecordQualifier(dataType);
                ctx.visit(defaultIfNull(Tools.getMappedQualifier(ctx, q), q));
            }
            else
                ctx.visit(dataType.getQualifiedName());
        }

        @SuppressWarnings("unchecked")
        private static final <T, U> U pgFromString(BindingScope ctx, Field<U> field, String string) {
            ContextConverter<T, U> converter = (ContextConverter<T, U>) field.getConverter();
            Class<?> type = wrapper(converter.fromType());

            if (string == null)
                return converter.from(null, ctx.converterContext());
            else if (type == Blob.class)
                ; // Not supported
            else if (type == Boolean.class)
                return converter.from((T) Convert.convert(string, Boolean.class), ctx.converterContext());
            else if (type == BigInteger.class)
                return converter.from((T) new BigInteger(string), ctx.converterContext());
            else if (type == BigDecimal.class)
                return converter.from((T) new BigDecimal(string), ctx.converterContext());
            else if (type == Byte.class)
                return converter.from((T) Byte.valueOf(string), ctx.converterContext());
            else if (type == byte[].class)
                return converter.from((T) PostgresUtils.toBytes(string), ctx.converterContext());
            else if (type == Clob.class)
                ; // Not supported
            else if (type == Date.class)
                return converter.from((T) Date.valueOf(string), ctx.converterContext());
            else if (type == Decfloat.class)
                return converter.from((T) Decfloat.decfloat(string), ctx.converterContext());
            else if (type == Double.class)
                return converter.from((T) Double.valueOf(string), ctx.converterContext());
            else if (type == Float.class)
                return converter.from((T) Float.valueOf(string), ctx.converterContext());






            else if (type == Integer.class)
                return converter.from((T) Integer.valueOf(string), ctx.converterContext());
            else if (type == Long.class)
                return converter.from((T) Long.valueOf(string), ctx.converterContext());
            else if (type == Short.class)
                return converter.from((T) Short.valueOf(string), ctx.converterContext());
            else if (type == String.class)
                return converter.from((T) string, ctx.converterContext());
            else if (type == Time.class)
                return converter.from((T) Time.valueOf(string), ctx.converterContext());
            else if (type == Timestamp.class)
                return converter.from((T) Timestamp.valueOf(patchIso8601Timestamp(string, false)), ctx.converterContext());
            else if (type == LocalTime.class)
                return converter.from((T) LocalTime.parse(string), ctx.converterContext());
            else if (type == LocalDate.class)
                return converter.from((T) LocalDate.parse(string), ctx.converterContext());
            else if (type == LocalDateTime.class)
                return converter.from((T) LocalDateTime.parse(patchIso8601Timestamp(string, true)), ctx.converterContext());
            else if (type == OffsetTime.class)
                return converter.from((T) OffsetDateTimeParser.offsetTime(string), ctx.converterContext());
            else if (type == OffsetDateTime.class)
                return converter.from((T) OffsetDateTimeParser.offsetDateTime(string), ctx.converterContext());
            else if (type == Instant.class)
                return converter.from((T) OffsetDateTimeParser.offsetDateTime(string).toInstant(), ctx.converterContext());
            else if (type == JSON.class)
                return converter.from((T) JSON.json(string), ctx.converterContext());
            else if (type == JSONB.class)
                return converter.from((T) JSONB.jsonb(string), ctx.converterContext());
            else if (type == UByte.class)
                return converter.from((T) UByte.valueOf(string), ctx.converterContext());
            else if (type == UShort.class)
                return converter.from((T) UShort.valueOf(string), ctx.converterContext());
            else if (type == UInteger.class)
                return converter.from((T) UInteger.valueOf(string), ctx.converterContext());
            else if (type == ULong.class)
                return converter.from((T) ULong.valueOf(string), ctx.converterContext());
            else if (type == UUID.class)
                return converter.from((T) UUID.fromString(string), ctx.converterContext());
            else if (type == XML.class)
                return converter.from((T) XML.xml(string), ctx.converterContext());
            else if (type == Year.class)
                return converter.from((T) Year.parse(string), ctx.converterContext());
            else if (type == YearToMonth.class)
                return converter.from((T) PostgresUtils.toYearToMonth(string), ctx.converterContext());
            else if (type == YearToSecond.class)
                return converter.from((T) PostgresUtils.toYearToSecond(string), ctx.converterContext());
            else if (type == DayToSecond.class)
                return converter.from((T) PostgresUtils.toDayToSecond(string), ctx.converterContext());
            else if (type.isArray())
                return converter.from((T) pgNewArray(ctx, field, type, string), ctx.converterContext());




            else if (EnumType.class.isAssignableFrom(type))
                return converter.from((T) DefaultEnumTypeBinding.getEnumType((Class<EnumType>) type, string), ctx.converterContext());
            else if (Result.class.isAssignableFrom(type))
                if (string.startsWith("<"))
                    return converter.from((T) readMultisetXML(ctx, (AbstractRow<Record>) field.getDataType().getRow(), (Class<Record>) field.getDataType().getRecordType(), string), ctx.converterContext());
                else
                    return converter.from((T) readMultisetJSON(ctx, (AbstractRow<Record>) field.getDataType().getRow(), (Class<Record>) field.getDataType().getRecordType(), string), ctx.converterContext());
            else if (Record.class.isAssignableFrom(type)

            // [#11812] UDTRecords/TableRecords or InternalRecords that don't have an explicit converter
                    && (!InternalRecord.class.isAssignableFrom(type) || type == converter.fromType()))
                return converter.from((T) pgNewRecord(ctx, type, (AbstractRow<?>) field.getDataType().getRow(), string), ctx.converterContext());
            else if (type == Object.class)
                return converter.from((T) string, ctx.converterContext());

            // [#4964] [#6058] Recurse only if we have a meaningful converter, not the identity converter,
            //                 which would cause a StackOverflowError, here!
            else if (type != wrapper(converter.toType()))
                return converter.from((T) pgFromString(ctx, field("converted_field", ConvertedDataType.delegate(field.getDataType())), string), ctx.converterContext());

            throw new UnsupportedOperationException("Class " + type + " is not supported");
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
        static final Record pgNewRecord(BindingScope ctx, Class<?> type, AbstractRow<?> fields, Object object) {
            if (object == null)
                return null;

            // [#15395] The ResultSet::getObject method returned a UDTRecord (i.e. it's a MockResultSet).
            //          As such, we can skip the serialisation / deserialisation logic.
            else if (object instanceof Record r)
                return r;

            String s = object.toString();
            List<String> values = PostgresUtils.toPGObject(s);

            // [#6404] [#7691]
            //   In the event of an unknown record type, derive the record length from actual values.
            //   Unfortunately, few column types can be derived from this information. Some possibilities
            //   for future jOOQ versions include:
            //   - Integers
            //   - Numbers
            //   - Binary data (starts with \x)
            //   - Temporal data
            //   - Everything else: VARCHAR
            if (fields == null && Record.class.isAssignableFrom(type))
                fields = Tools.row0(Tools.fields(values.size(), SQLDataType.VARCHAR));

            return Tools.newRecord(true, originalConfiguration(ctx), (Class<Record>) type, (AbstractRow<Record>) fields)
                        .operate(r -> {
                            Row row = r.fieldsRow();

                            for (int i = 0; i < row.size(); i++)
                                pgSetValue(ctx, r, row.field(i), values.get(i));

                            r.touched(false);
                            return r;
                        });
        }

        private static final <T> void pgSetValue(BindingScope ctx, Record record, Field<T> field, String value) {
            record.set(field, pgFromString(ctx, field, value));
        }

        /**
         * Create an array from a String
         *
         * @param string A String representation of an array
         * @return The converted array
         */
        private static final Object[] pgNewArray(BindingScope ctx, Field<?> field, Class<?> type, String string) {
            if (string == null)
                return null;

            DataType<?> t = field.getDataType();
            try {
                return Tools.map(
                    toPGArray(string),
                    v -> pgFromString(ctx, field("array_element", ConvertedDataType.delegate(t).getArrayComponentDataType()), v),
                    size -> (Object[]) java.lang.reflect.Array.newInstance(type.getComponentType(), size)
                );
            }
            catch (Exception e) {

                // [#11823]
                if (type.getComponentType().getSimpleName().equals("UnknownType"))
                    throw new DataTypeException("Error while creating array for UnknownType. Please provide an explicit Class<U> type to your converter, see https://github.com/jOOQ/jOOQ/issues/11823", e);
                else
                    throw new DataTypeException("Error while creating array", e);
            }
        }
    }

    static final class DefaultResultBinding<U> extends InternalBinding<org.jooq.Result<?>, U> {

        DefaultResultBinding(DataType<Result<?>> dataType, Converter<Result<?>, U> converter) {
            super(dataType, converter);
        }

        @Override
        final void set0(BindingSetStatementContext<U> ctx, Result<?> value) throws SQLException {
            throw new UnsupportedOperationException("Cannot bind a value of type Result to a PreparedStatement");
        }

        @Override
        final void set0(BindingSetSQLOutputContext<U> ctx, Result<?> value) throws SQLException {
            throw new UnsupportedOperationException("Cannot bind a value of type Result to a SQLOutput");
        }

        @SuppressWarnings("unchecked")
        @Override
        final Result<?> get0(BindingGetResultSetContext<U> ctx) throws SQLException {
            Field<?> field = uncoerce(ctx.field());

            if (field.getDataType().isMultiset())
                return readMultiset(ctx, (DataType<Result<Record>>) field.getDataType());
            else
                return ctx.configuration().dsl().fetch(convert(ctx.resultSet().getObject(ctx.index()), ResultSet.class));
        }

        @SuppressWarnings("unchecked")
        static final <R extends Record> Result<R> readMultiset(BindingGetResultSetContext<?> ctx, DataType<Result<R>> type) throws SQLException {
            return readMultiset(ctx,
                (AbstractRow<R>) type.getRow(),
                (Class<R>) type.getRecordType(),
                identity(),
                identity(),
                o -> o instanceof List<?> l
                   ? l
                   : o instanceof Object[] a
                   ? asList(a)
                   : o instanceof Array a
                   ? asList((Object[]) a.getArray())
                   : null
            );
        }

        static final <R extends Record> Result<R> readMultiset(
            BindingGetResultSetContext<?> ctx,
            AbstractRow<R> row,
            Class<R> recordType,
            Function<String, String> jsonStringPatch,
            Function<String, String> xmlStringPatch,
            ThrowingFunction<Object, List<?>, SQLException> nativePatch
        )
        throws SQLException {
            NestedCollectionEmulation emulation = emulateMultiset(ctx.configuration());

            // [#17074] Native capable dialects may render ROW at the top level despite JSON being requested.
            //          This probably hints at a design problem somewhere, which we'll investigate once more
            //          dialects support NATIVE implementations
            switch (ctx.family()) {
                case DUCKDB:
                    if (ctx.resultSet().getObject(ctx.index()) instanceof Struct)
                        emulation = NestedCollectionEmulation.NATIVE;

                    break;
            }

            switch (emulation) {
                case JSON:
                case JSONB:
                    if (emulation == NestedCollectionEmulation.JSONB && EMULATE_AS_BLOB.contains(ctx.dialect())) {
                        byte[] bytes = ctx.resultSet().getBytes(ctx.index());
                        return apply(
                            jsonStringPatch.apply(bytes == null ? null : Source.of(bytes, ctx.configuration().charsetProvider().provide()).readString()),
                            s -> readMultisetJSON(ctx, row, recordType, s)
                        );
                    }
                    else {
                        return apply(
                            jsonStringPatch.apply(ctx.resultSet().getString(ctx.index())),
                            s -> readMultisetJSON(ctx, row, recordType, s)
                        );
                    }

                case XML:
                    return apply(
                        xmlStringPatch.apply(ctx.resultSet().getString(ctx.index())),
                        s -> readMultisetXML(ctx, row, recordType, s)
                    );

                case NATIVE:
                    return apply(
                        nativePatch.apply(ctx.resultSet().getObject(ctx.index())),
                        l -> readMultisetList(ctx, row, recordType, l)
                    );
            }

            throw new UnsupportedOperationException("Multiset emulation not yet supported: " + emulation);
        }

        static final <R extends Record> Result<R> readMultisetList(Scope ctx, AbstractRow<R> row, Class<R> recordType, List<?> l) throws SQLException {
            return new ListHandler<>(originalScope(ctx), row, recordType).read(l);
        }

        static final <R extends Record> Result<R> readMultisetXML(Scope ctx, AbstractRow<R> row, Class<R> recordType, String s) {
            if (s.startsWith("<"))
                return new XMLHandler<>(originalScope(ctx), row, recordType).read(s);
            else
                return readMultisetScalar(ctx, row, recordType, s);
        }

        static final <R extends Record> Result<R> readMultisetJSON(Scope ctx, AbstractRow<R> row, Class<R> recordType, String s) {
            if (s.startsWith("{") || s.startsWith("["))
                return new JSONReader<>(originalScope(ctx), row, recordType, true).read(new StringReader(patchSnowflakeJSON(ctx, s)), true);
            else
                return readMultisetScalar(ctx, row, recordType, s);
        }

        static final <R extends Record> Result<R> readMultisetScalar(Scope ctx, AbstractRow<R> row, Class<R> recordType, String s) {
            Configuration c = originalConfiguration(ctx);
            Result<R> result = new ResultImpl<>(c, row);

            result.add(newRecord(true, c, recordType, row).operate(r -> {
                r.from(asList(s));
                r.touched(false);
                return r;
            }));

            return result;
        }

        @Override
        final Result<?> get0(BindingGetStatementContext<U> ctx) throws SQLException {
            return ctx.configuration().dsl().fetch(convert(ctx.statement().getObject(ctx.index()), ResultSet.class));
        }

        @Override
        final Result<?> get0(BindingGetSQLInputContext<U> ctx) throws SQLException {
            throw new UnsupportedOperationException("Cannot get a value of type Result from a SQLInput");
        }

        @Override
        final int sqltype(Statement statement, Configuration configuration) {
            switch (configuration.family()) {

                case H2:
                    return -10; // OracleTypes.CURSOR;

                default:
                    return Types.OTHER;
            }
        }
    }

    static final class DefaultShortBinding<U> extends InternalBinding<Short, U> {

        DefaultShortBinding(DataType<Short> dataType, Converter<Short, U> converter) {
            super(dataType, converter);
        }

        @Override
        final void setNull0(BindingSetStatementContext<U> ctx) throws SQLException {





            super.setNull0(ctx);
        }

        @Override
        final void sqlInline0(BindingSQLContext<U> ctx, Short value) {
            ctx.render().sql(value);
        }

        @Override
        final void set0(BindingSetStatementContext<U> ctx, Short value) throws SQLException {
            ctx.statement().setShort(ctx.index(), value);
        }

        @Override
        final void set0(BindingSetSQLOutputContext<U> ctx, Short value) throws SQLException {
            ctx.output().writeShort(value);
        }

        @Override
        final Short get0(BindingGetResultSetContext<U> ctx) throws SQLException {
            return wasNull(ctx.resultSet(), ctx.resultSet().getShort(ctx.index()));
        }

        @Override
        final Short get0(BindingGetStatementContext<U> ctx) throws SQLException {
            return wasNull(ctx.statement(), ctx.statement().getShort(ctx.index()));
        }

        @Override
        final Short get0(BindingGetSQLInputContext<U> ctx) throws SQLException {
            return wasNull(ctx.input(), ctx.input().readShort());
        }

        @Override
        final int sqltype(Statement statement, Configuration configuration) {





            return Types.SMALLINT;
        }
    }

    static final class DefaultStringBinding<U> extends InternalBinding<String, U> {

        DefaultStringBinding(DataType<String> dataType, Converter<String, U> converter) {
            super(dataType, converter);
        }

        @Override
        final void setNull0(BindingSetStatementContext<U> ctx) throws SQLException {





            super.setNull0(ctx);
        }

        @Override
        final void sqlInline0(BindingSQLContext<U> ctx, String value) throws SQLException {

            // [#6516] The below heuristics work for UTF-32 and UTF-8
            //         future UTF encodings which may use more bytes per
            //         character are not handled here, yet.
            if (ctx.family() == DERBY)
                sqlInlineWorkaround6516(ctx, value, 8192, "");











            else
                super.sqlInline0(ctx, value);
        }

        private final void sqlInlineWorkaround6516(BindingSQLContext<U> ctx, String value, int limit, String prefix) throws SQLException {
            int l = value.length();

            if (l > limit) {
                ctx.render().sql('(');

                for (int i = 0; i < l; i += limit) {
                    if (i > 0)
                        ctx.render().sql(" || ");

                    ctx.render().sql(prefix).sql("(");
                    super.sqlInline0(ctx, value.substring(i, Math.min(l, i + limit)));
                    ctx.render().sql(')');
                }

                ctx.render().sql(')');
            }
            else
                super.sqlInline0(ctx, value);
        }


        @Override
        final void set0(BindingSetStatementContext<U> ctx, String value) throws SQLException {











            ctx.statement().setString(ctx.index(), value);
        }

        @Override
        final void set0(BindingSetSQLOutputContext<U> ctx, String value) throws SQLException {








            ctx.output().writeString(value);
        }

        @Override
        final String get0(BindingGetResultSetContext<U> ctx) throws SQLException {

















            return autoRtrim(ctx, dataType, ctx.resultSet().getString(ctx.index()));
        }

        @Override
        final String get0(BindingGetStatementContext<U> ctx) throws SQLException {
            return autoRtrim(ctx, dataType, ctx.statement().getString(ctx.index()));
        }

        @Override
        final String get0(BindingGetSQLInputContext<U> ctx) throws SQLException {
            return autoRtrim(ctx, dataType, ctx.input().readString());
        }

        @Override
        final int sqltype(Statement statement, Configuration configuration) {







            return Types.VARCHAR;
        }

        static final String autoRtrim(Scope ctx, DataType<String> type, String string) {
            if (type.hasFixedLength() && !isEmpty(string) && TRUE.equals(ctx.settings().isFetchTrimmedCharValues()))
                return rtrim(string);
            else
                return string;
        }
    }

    static final class DefaultNStringBinding<U> extends InternalBinding<String, U> {






        private final DefaultStringBinding<U> fallback;

        DefaultNStringBinding(DataType<String> dataType, Converter<String, U> converter) {
            super(dataType, converter);

            this.fallback = new DefaultStringBinding<>(dataType, converter);
        }

        @Override
        final void setNull0(BindingSetStatementContext<U> ctx) throws SQLException {





            super.setNull0(ctx);
        }

        @Override
        void sqlInline0(BindingSQLContext<U> ctx, String value) throws SQLException {
            if (NO_SUPPORT_NVARCHAR.contains(ctx.dialect())) {
                fallback.sqlInline0(ctx, value);
            }
            else {
                ctx.render().sql('N');
                super.sqlInline0(ctx, value);
            }
        }

        @Override
        final void set0(BindingSetStatementContext<U> ctx, String value) throws SQLException {
            if (NO_SUPPORT_NVARCHAR.contains(ctx.dialect()))
                fallback.set0(ctx, value);
            else
                ctx.statement().setNString(ctx.index(), value);
        }

        @Override
        final void set0(BindingSetSQLOutputContext<U> ctx, String value) throws SQLException {
            if (NO_SUPPORT_NVARCHAR.contains(ctx.dialect())) {
                fallback.set0(ctx, value);
            }









            ctx.output().writeNString(value);
        }

        @Override
        final String get0(BindingGetResultSetContext<U> ctx) throws SQLException {
            if (NO_SUPPORT_NVARCHAR.contains(ctx.dialect()))
                return fallback.get0(ctx);

            // [#17850] In some cases, NULL values can't be read as NVARCHAR as the
            //          MySQL driver will throw an exception due to a wrong encoding (e.g. ISO 8859-1)
            else if (ctx.family() == MYSQL)
                return ctx.resultSet().getObject(ctx.index()) == null
                    ? null
                    : autoRtrim(ctx, dataType, ctx.resultSet().getNString(ctx.index()));
            else
                return autoRtrim(ctx, dataType, ctx.resultSet().getNString(ctx.index()));
        }

        @Override
        final String get0(BindingGetStatementContext<U> ctx) throws SQLException {
            if (NO_SUPPORT_NVARCHAR.contains(ctx.dialect()))
                return fallback.get0(ctx);
            else
                return autoRtrim(ctx, dataType, ctx.statement().getNString(ctx.index()));
        }

        @Override
        final String get0(BindingGetSQLInputContext<U> ctx) throws SQLException {
            if (NO_SUPPORT_NVARCHAR.contains(ctx.dialect()))
                return fallback.get0(ctx);
            else
                return autoRtrim(ctx, dataType, ctx.input().readNString());
        }

        @Override
        final int sqltype(Statement statement, Configuration configuration) {
            if (NO_SUPPORT_NVARCHAR.contains(configuration.dialect()))
                return fallback.sqltype(statement, configuration);







            else
                return Types.NVARCHAR;
        }
    }

    static final class DefaultTimeBinding<U> extends InternalBinding<Time, U> {

        DefaultTimeBinding(DataType<Time> dataType, Converter<Time, U> converter) {
            super(dataType, converter);
        }

        @Override
        final void setNull0(BindingSetStatementContext<U> ctx) throws SQLException {





            super.setNull0(ctx);
        }

        @Override
        final void sqlInline0(BindingSQLContext<U> ctx, Time value) {
            switch (ctx.family()) {
                // The SQLite JDBC driver does not implement the escape syntax
                // [#1253] Sybase does not implement time literals


                case SQLITE:
                    ctx.render().sql('\'').sql(new SimpleDateFormat("HH:mm:ss").format(value)).sql('\'');
                    break;











                // [#1253] Derby doesn't support the standard literal
                case DERBY:
                    ctx.render().visit(K_TIME).sql("('").sql(escape(value, ctx.render())).sql("')");
                    break;
















                default:









                    // [#16498] Special cases where the standard datetime literal prefix needs to be omitted
                    //          See: https://bugs.mysql.com/bug.php?id=114450
                    if (ctx.data(DATA_OMIT_DATETIME_LITERAL_PREFIX) != null)
                        ctx.render().sql('\'').sql(escape(value, ctx.render())).sql('\'');

                    // Most dialects implement SQL standard time literals
                    else
                        ctx.render().visit(K_TIME).sql(" '").sql(escape(value, ctx.render())).sql('\'');

                    break;
            }
        }

        @Override
        final void set0(BindingSetStatementContext<U> ctx, Time value) throws SQLException {
            switch (ctx.family()) {






                case DUCKDB:
                case SQLITE:
                    ctx.statement().setString(ctx.index(), value.toString());
                    break;

                default:
                    ctx.statement().setTime(ctx.index(), value);
                    break;
            }
        }

        @Override
        final void set0(BindingSetSQLOutputContext<U> ctx, Time value) throws SQLException {
            ctx.output().writeTime(value);
        }

        @Override
        final Time get0(BindingGetResultSetContext<U> ctx) throws SQLException {
            switch (ctx.family()) {







                // ResultSet.getTime() isn't implemented correctly, see: https://github.com/duckdb/duckdb/issues/10682
                case DUCKDB: {
                    String time = ctx.resultSet().getString(ctx.index());
                    return time == null ? null : Convert.convert(time, Time.class);
                }

                // SQLite's type affinity needs special care...
                case SQLITE: {
                    String time = ctx.resultSet().getString(ctx.index());
                    return time == null ? null : new Time(parse(Time.class, time));
                }

                default:
                    return ctx.resultSet().getTime(ctx.index());
            }
        }

        @Override
        final Time get0(BindingGetStatementContext<U> ctx) throws SQLException {
            return ctx.statement().getTime(ctx.index());
        }

        @Override
        final Time get0(BindingGetSQLInputContext<U> ctx) throws SQLException {
            return ctx.input().readTime();
        }

        @Override
        final int sqltype(Statement statement, Configuration configuration) {
            return Types.TIME;
        }
    }

    static final class DefaultTimestampBinding<U> extends InternalBinding<Timestamp, U> {
        private static final Set<SQLDialect> INLINE_AS_STRING_LITERAL = SQLDialect.supportedBy(SQLITE);

        DefaultTimestampBinding(DataType<Timestamp> dataType, Converter<Timestamp, U> converter) {
            super(dataType, converter);
        }

        @Override
        final void setNull0(BindingSetStatementContext<U> ctx) throws SQLException {





            super.setNull0(ctx);
        }

        @Override
        final void sqlInline0(BindingSQLContext<U> ctx, Timestamp value) {

            // The SQLite JDBC driver does not implement the escape syntax
            // [#1253] Sybase does not implement timestamp literals
            if (INLINE_AS_STRING_LITERAL.contains(ctx.dialect()))
                ctx.render().sql('\'').sql(escape(value, ctx.render())).sql('\'');













            // [#7539] Clickhouse Timestamp literals don't support milliseconds
            else if (ctx.family() == CLICKHOUSE)
                ctx.render().visit(K_TIMESTAMP).sql(" '").sql(truncateTimestamp(escape(value, ctx.render()))).sql('\'');

            // [#1253] Derby doesn't support the standard literal
            else if (ctx.family() == DERBY)
                ctx.render().visit(K_TIMESTAMP).sql("('").sql(escape(value, ctx.render())).sql("')");

            // CUBRID timestamps have no fractional seconds
            else if (ctx.family() == CUBRID)
                ctx.render().visit(K_DATETIME).sql(" '").sql(escape(value, ctx.render())).sql('\'');







            // [#16498] Special cases where the standard datetime literal prefix needs to be omitted
            //          See: https://bugs.mysql.com/bug.php?id=114450
            else if (ctx.data(DATA_OMIT_DATETIME_LITERAL_PREFIX) != null)
                ctx.render().sql('\'').sql(format(value, ctx.render())).sql('\'');

            // Most dialects implement SQL standard timestamp literals
            else
                ctx.render().visit(K_TIMESTAMP).sql(" '").sql(format(value, ctx.render())).sql('\'');
        }

        private final String truncateTimestamp(String t) {
            int i = t.indexOf('.');
            return i > 0 ? t.substring(0, i) : t;
        }

        private final String format(Timestamp value, RenderContext render) {
            if (render.family() == POSTGRES)
                if (value.getTime() == PG_DATE_POSITIVE_INFINITY)
                    return "infinity";
                else if (value.getTime() == PG_DATE_NEGATIVE_INFINITY)
                    return "-infinity";

            return escape(value, render);
        }

        @Override
        final void set0(BindingSetStatementContext<U> ctx, Timestamp value) throws SQLException {
            if (ctx.family() == SQLITE)
                ctx.statement().setString(ctx.index(), value.toString());
            else
                ctx.statement().setTimestamp(ctx.index(), value);
        }

        @Override
        final void set0(BindingSetSQLOutputContext<U> ctx, Timestamp value) throws SQLException {
            ctx.output().writeTimestamp(value);
        }

        @Override
        final Timestamp get0(BindingGetResultSetContext<U> ctx) throws SQLException {

            // SQLite's type affinity needs special care...
            if (ctx.family() == SQLDialect.SQLITE) {
                String timestamp = ctx.resultSet().getString(ctx.index());
                return timestamp == null ? null : new Timestamp(parse(Timestamp.class, timestamp));
            }

            else {
                return ctx.resultSet().getTimestamp(ctx.index());
            }
        }

        @Override
        final Timestamp get0(BindingGetStatementContext<U> ctx) throws SQLException {
            return ctx.statement().getTimestamp(ctx.index());
        }

        @Override
        final Timestamp get0(BindingGetSQLInputContext<U> ctx) throws SQLException {
            return ctx.input().readTimestamp();
        }

        @Override
        final int sqltype(Statement statement, Configuration configuration) {
            return Types.TIMESTAMP;
        }
    }

    static final class DefaultUUIDBinding<U> extends InternalBinding<UUID, U> {

        DefaultUUIDBinding(DataType<UUID> dataType, Converter<UUID, U> converter) {
            super(dataType, converter);
        }

        @Override
        final void setNull0(BindingSetStatementContext<U> ctx) throws SQLException {





            super.setNull0(ctx);
        }

        @Override
        final void set0(BindingSetStatementContext<U> ctx, UUID value) throws SQLException {
            switch (ctx.family()) {

                // [#1624] Some JDBC drivers natively support the
                // java.util.UUID data type



                case H2:
                case POSTGRES:
                case YUGABYTEDB: {
                    ctx.statement().setObject(ctx.index(), value);
                    break;
                }









                // Most databases don't have such a type. In this case, jOOQ
                // emulates the type
                default: {
                    ctx.statement().setString(ctx.index(), value.toString());
                    break;
                }
            }
        }

        @Override
        final void set0(BindingSetSQLOutputContext<U> ctx, UUID value) throws SQLException {
            ctx.output().writeString(value.toString());
        }

        @Override
        final UUID get0(BindingGetResultSetContext<U> ctx) throws SQLException {
            switch (ctx.family()) {

                // [#1624] Some JDBC drivers natively support the java.util.UUID data type
                // [#8439] In edge cases (e.g. arrays over domains) the type info may have
                //         been lost between server and JDBC driver, so let's expect PGobject


                case H2:
                case POSTGRES:
                case YUGABYTEDB: {
                    Object o = ctx.resultSet().getObject(ctx.index());

                    if (o == null)
                        return null;
                    else if (o instanceof UUID u)
                        return u;
                    else
                        return Convert.convert(o.toString(), UUID.class);
                }









                // Most databases don't have such a type. In this case, jOOQ
                // emulates the type
                default:
                    return Convert.convert(ctx.resultSet().getString(ctx.index()), UUID.class);
            }
        }

        @Override
        final UUID get0(BindingGetStatementContext<U> ctx) throws SQLException {
            switch (ctx.family()) {

                // [#1624] Some JDBC drivers natively support the
                // java.util.UUID data type



                case H2:
                case POSTGRES:
                case YUGABYTEDB:
                    return (UUID) ctx.statement().getObject(ctx.index());









                // Most databases don't have such a type. In this case, jOOQ
                // emulates the type
                default:
                    return Convert.convert(ctx.statement().getString(ctx.index()), UUID.class);
            }
        }

        @Override
        final UUID get0(BindingGetSQLInputContext<U> ctx) throws SQLException {
            return Convert.convert(ctx.input().readString(), UUID.class);
        }

        @Override
        final int sqltype(Statement statement, Configuration configuration) {
            switch (configuration.family()) {


                case POSTGRES:
                case YUGABYTEDB:
                    return Types.OTHER;

                default:
                    return Types.VARCHAR;
            }
        }
    }



















































































































































































































































































































































































































































































































































































































    static final class DefaultJSONBinding<U> extends InternalBinding<org.jooq.JSON, U> {

        DefaultJSONBinding(DataType<JSON> dataType, Converter<JSON, U> converter) {
            super(dataType, converter);
        }

        @Override
        final void setNull0(BindingSetStatementContext<U> ctx) throws SQLException {





            super.setNull0(ctx);
        }

        @Override
        void sqlInline0(BindingSQLContext<U> ctx, JSON value) throws SQLException {
            super.sqlInline0(ctx, value);

            if (ctx.family() == H2 && value != null)
                ctx.render().sql(' ').visit(K_FORMAT).sql(' ').visit(K_JSON);
        }

        @Override
        void sqlBind0(BindingSQLContext<U> ctx, JSON value) throws SQLException {
            super.sqlBind0(ctx, value);

            if (ctx.family() == H2 && value != null)
                ctx.render().sql(' ').visit(K_FORMAT).sql(' ').visit(K_JSON);
        }

        @Override
        final void set0(BindingSetStatementContext<U> ctx, JSON value) throws SQLException {
            ctx.statement().setString(ctx.index(), value.data());
        }

        @Override
        final void set0(BindingSetSQLOutputContext<U> ctx, JSON value) throws SQLException {
            ctx.output().writeString(value.data());
        }

        @Override
        final JSON get0(BindingGetResultSetContext<U> ctx) throws SQLException {
            String string = patchSnowflakeJSON(ctx, ctx.resultSet().getString(ctx.index()));
            return string == null ? null : JSON.valueOf(string);
        }

        @Override
        final JSON get0(BindingGetStatementContext<U> ctx) throws SQLException {
            String string = patchSnowflakeJSON(ctx, ctx.statement().getString(ctx.index()));
            return string == null ? null : JSON.valueOf(string);
        }

        @Override
        final JSON get0(BindingGetSQLInputContext<U> ctx) throws SQLException {
            String string = patchSnowflakeJSON(ctx, ctx.input().readString());
            return string == null ? null : JSON.valueOf(string);
        }

        @Override
        final int sqltype(Statement statement, Configuration configuration) {
            return Types.VARCHAR;
        }






        static final String patchSnowflakeJSON(Scope ctx, String json) {




















            return json;
        }
    }

    static final class DefaultJSONBBinding<U> extends InternalBinding<org.jooq.JSONB, U> {
        static final Set<SQLDialect> EMULATE_AS_BLOB = SQLDialect.supportedBy(DERBY, FIREBIRD, HSQLDB, SQLITE);

        DefaultJSONBBinding(DataType<JSONB> dataType, Converter<JSONB, U> converter) {
            super(dataType, converter);
        }

        @Override
        final void setNull0(BindingSetStatementContext<U> ctx) throws SQLException {





            super.setNull0(ctx);
        }

        @Override
        void sqlInline0(BindingSQLContext<U> ctx, JSONB value) throws SQLException {
            if (EMULATE_AS_BLOB.contains(ctx.dialect())) {
                bytes(ctx.configuration()).sqlInline0(ctx, bytesConverter(ctx.configuration()).to(value, ctx.converterContext()));
            }
            else {
                super.sqlInline1(ctx, value.data());

                if (ctx.family() == H2)
                    ctx.render().sql(' ').visit(K_FORMAT).sql(' ').visit(K_JSON);
            }
        }

        @Override
        void sqlBind0(BindingSQLContext<U> ctx, JSONB value) throws SQLException {
            super.sqlBind0(ctx, value);

            if (ctx.family() == H2 && value != null)
                ctx.render().sql(' ').visit(K_FORMAT).sql(' ').visit(K_JSON);
        }

        @Override
        final void set0(BindingSetStatementContext<U> ctx, JSONB value) throws SQLException {
            if (EMULATE_AS_BLOB.contains(ctx.dialect()))
                bytes(ctx.configuration()).set0(ctx, bytesConverter(ctx.configuration()).to(value, ctx.converterContext()));
            else
                ctx.statement().setString(ctx.index(), value.data());
        }

        @Override
        final void set0(BindingSetSQLOutputContext<U> ctx, JSONB value) throws SQLException {
            if (EMULATE_AS_BLOB.contains(ctx.dialect()))
                bytes(ctx.configuration()).set0(ctx, bytesConverter(ctx.configuration()).to(value, ctx.converterContext()));
            else
                ctx.output().writeString(value.data());
        }

        @Override
        final JSONB get0(BindingGetResultSetContext<U> ctx) throws SQLException {
            if (EMULATE_AS_BLOB.contains(ctx.dialect()))
                return bytesConverter(ctx.configuration()).from(bytes(ctx.configuration()).get0(ctx), ctx.converterContext());

            String string = patchSnowflakeJSON(ctx, ctx.resultSet().getString(ctx.index()));
            return string == null ? null : JSONB.valueOf(string);
        }

        @Override
        final JSONB get0(BindingGetStatementContext<U> ctx) throws SQLException {
            if (EMULATE_AS_BLOB.contains(ctx.dialect()))
                return bytesConverter(ctx.configuration()).from(bytes(ctx.configuration()).get0(ctx), ctx.converterContext());

            String string = patchSnowflakeJSON(ctx, ctx.statement().getString(ctx.index()));
            return string == null ? null : JSONB.valueOf(string);
        }

        @Override
        final JSONB get0(BindingGetSQLInputContext<U> ctx) throws SQLException {
            if (EMULATE_AS_BLOB.contains(ctx.dialect()))
                return bytesConverter(ctx.configuration()).from(bytes(ctx.configuration()).get0(ctx), ctx.converterContext());

            String string = patchSnowflakeJSON(ctx, ctx.input().readString());
            return string == null ? null : JSONB.valueOf(string);
        }

        @Override
        final int sqltype(Statement statement, Configuration configuration) {
            if (EMULATE_AS_BLOB.contains(configuration.dialect()))
                return bytes(configuration).sqltype(statement, configuration);

            return Types.VARCHAR;
        }

        private final ContextConverter<byte[], JSONB> bytesConverter(final Configuration configuration) {
            return ContextConverter.ofNullable(byte[].class, JSONB.class,
                (t, x) -> JSONB.valueOf(new String(t, configuration.charsetProvider().provide())),
                (u, x) -> u.data().getBytes(configuration.charsetProvider().provide())
            );
        }

        @SuppressWarnings({ "unchecked", "rawtypes" })
        private final DefaultBytesBinding<U> bytes(Configuration configuration) {
            return new DefaultBytesBinding<>(BLOB, (Converter) bytesConverter(configuration));
        }
    }

    static final class DefaultXMLBinding<U> extends InternalBinding<XML, U> {

        DefaultXMLBinding(DataType<XML> dataType, Converter<XML, U> converter) {
            super(dataType, converter);
        }

        @Override
        final void setNull0(BindingSetStatementContext<U> ctx) throws SQLException {





            super.setNull0(ctx);
        }

        @Override
        final void set0(BindingSetStatementContext<U> ctx, XML value) throws SQLException {
            ctx.statement().setString(ctx.index(), value.toString());
        }

        @Override
        final void set0(BindingSetSQLOutputContext<U> ctx, XML value) throws SQLException {
            ctx.output().writeString(value.toString());
        }

        @Override
        final XML get0(BindingGetResultSetContext<U> ctx) throws SQLException {
            String string = ctx.resultSet().getString(ctx.index());
            return string == null ? null : XML.valueOf(string);
        }

        @Override
        final XML get0(BindingGetStatementContext<U> ctx) throws SQLException {
            String string = ctx.statement().getString(ctx.index());
            return string == null ? null : XML.valueOf(string);
        }

        @Override
        final XML get0(BindingGetSQLInputContext<U> ctx) throws SQLException {
            String string = ctx.input().readString();
            return string == null ? null : XML.valueOf(string);
        }

        @Override
        final int sqltype(Statement statement, Configuration configuration) {
            return Types.VARCHAR;
        }
    }

    static final class DefaultYearToSecondBinding<U> extends InternalBinding<YearToSecond, U> {
        private static final Set<SQLDialect> REQUIRE_PG_INTERVAL = SQLDialect.supportedBy(POSTGRES, YUGABYTEDB);

        DefaultYearToSecondBinding(DataType<YearToSecond> dataType, Converter<YearToSecond, U> converter) {
            super(dataType, converter);
        }

        @Override
        final void sqlInline0(BindingSQLContext<U> ctx, YearToSecond value) throws SQLException {

            // [#566] Interval data types are best bound as Strings
            if (REQUIRE_PG_INTERVAL.contains(ctx.dialect()))
                ctx.render().visit(inline(toPGInterval(value).toString()));
            else
                super.sqlInline0(ctx, value);
        }

        @Override
        final void set0(BindingSetStatementContext<U> ctx, YearToSecond value) throws SQLException {

            // [#566] Interval data types are best bound as Strings
            if (REQUIRE_PG_INTERVAL.contains(ctx.dialect()))
                ctx.statement().setString(ctx.index(), toPGInterval(value).toString());
            else
                ctx.statement().setString(ctx.index(), value.toString());
        }

        @Override
        final void set0(BindingSetSQLOutputContext<U> ctx, YearToSecond value) throws SQLException {
            ctx.output().writeString(value.toString());
        }

        @Override
        final YearToSecond get0(BindingGetResultSetContext<U> ctx) throws SQLException {
            if (REQUIRE_PG_INTERVAL.contains(ctx.dialect())) {
                Object object = ctx.resultSet().getObject(ctx.index());
                return object == null ? null : PostgresUtils.toYearToSecond(object);
            }
            else {
                String string = ctx.resultSet().getString(ctx.index());
                return string == null ? null : YearToSecond.valueOf(string);
            }
        }

        @Override
        final YearToSecond get0(BindingGetStatementContext<U> ctx) throws SQLException {
            if (REQUIRE_PG_INTERVAL.contains(ctx.dialect())) {
                Object object = ctx.statement().getObject(ctx.index());
                return object == null ? null : PostgresUtils.toYearToSecond(object);
            }
            else {
                String string = ctx.statement().getString(ctx.index());
                return string == null ? null : YearToSecond.valueOf(string);
            }
        }

        @Override
        final YearToSecond get0(BindingGetSQLInputContext<U> ctx) throws SQLException {
            String string = ctx.input().readString();
            return string == null ? null : YearToSecond.valueOf(string);
        }

        @Override
        final int sqltype(Statement statement, Configuration configuration) {
            return Types.VARCHAR;
        }
    }

    static final class DefaultYearBinding<U> extends InternalBinding<Year, U> {

        DefaultYearBinding(DataType<Year> dataType, Converter<Year, U> converter) {
            super(dataType, converter);
        }

        @Override
        final void sqlInline0(BindingSQLContext<U> ctx, Year value) {
            ctx.render().sql(value.getValue());
        }

        @Override
        final void set0(BindingSetStatementContext<U> ctx, Year value) throws SQLException {
            ctx.statement().setInt(ctx.index(), value.getValue());
        }

        @Override
        final void set0(BindingSetSQLOutputContext<U> ctx, Year value) throws SQLException {
            ctx.output().writeInt(value.getValue());
        }

        @Override
        final Year get0(BindingGetResultSetContext<U> ctx) throws SQLException {
            return wasNull(ctx.resultSet(), Year.of(ctx.resultSet().getInt(ctx.index())));
        }

        @Override
        final Year get0(BindingGetStatementContext<U> ctx) throws SQLException {
            return wasNull(ctx.statement(), Year.of(ctx.statement().getInt(ctx.index())));
        }

        @Override
        final Year get0(BindingGetSQLInputContext<U> ctx) throws SQLException {
            return wasNull(ctx.input(), Year.of(ctx.input().readInt()));
        }

        @Override
        final int sqltype(Statement statement, Configuration configuration) {
            return Types.SMALLINT;
        }
    }

    static final class DefaultYearToMonthBinding<U> extends InternalBinding<YearToMonth, U> {
        private static final Set<SQLDialect> REQUIRE_PG_INTERVAL       = SQLDialect.supportedBy(POSTGRES, YUGABYTEDB);
        private static final Set<SQLDialect> REQUIRE_STANDARD_INTERVAL = SQLDialect.supportedBy(H2, TRINO);

        DefaultYearToMonthBinding(DataType<YearToMonth> dataType, Converter<YearToMonth, U> converter) {
            super(dataType, converter);
        }

        @Override
        final void sqlInline0(BindingSQLContext<U> ctx, YearToMonth value) throws SQLException {

            // [#566] Interval data types are best bound as Strings
            if (REQUIRE_PG_INTERVAL.contains(ctx.dialect()))
                ctx.render().visit(inline(toPGInterval(value).toString()));
            else if (ctx.family() == TRINO)
                ctx.render().sql(renderYTM(ctx, value));
            else
                super.sqlInline0(ctx, value);
        }

        @Override
        final void set0(BindingSetStatementContext<U> ctx, YearToMonth value) throws SQLException {

            // [#566] Interval data types are best bound as Strings
            if (REQUIRE_PG_INTERVAL.contains(ctx.dialect()))
                ctx.statement().setString(ctx.index(), toPGInterval(value).toString());
            else
                ctx.statement().setString(ctx.index(), renderYTM(ctx, value));
        }

        @Override
        final void set0(BindingSetSQLOutputContext<U> ctx, YearToMonth value) throws SQLException {
            ctx.output().writeString(renderYTM(ctx, value));
        }

        @Override
        final YearToMonth get0(BindingGetResultSetContext<U> ctx) throws SQLException {
            if (REQUIRE_PG_INTERVAL.contains(ctx.dialect()))
                return toYearToMonth(ctx.resultSet().getString(ctx.index()));
            else
                return parseYTM(ctx, ctx.resultSet().getString(ctx.index()));
        }

        @Override
        final YearToMonth get0(BindingGetStatementContext<U> ctx) throws SQLException {
            if (REQUIRE_PG_INTERVAL.contains(ctx.dialect()))
                return toYearToMonth(ctx.statement().getString(ctx.index()));
            else
                return parseYTM(ctx, ctx.statement().getString(ctx.index()));
        }

        @Override
        final YearToMonth get0(BindingGetSQLInputContext<U> ctx) throws SQLException {
            return parseYTM(ctx, ctx.input().readString());
        }

        @SuppressWarnings("unchecked")
        private final YearToMonth parseYTM(Scope scope, String string) {
            if (string == null)
                return null;
            else if (REQUIRE_STANDARD_INTERVAL.contains(scope.dialect()) && string.startsWith("INTERVAL"))
                return ((Param<YearToMonth>) scope.dsl().parser().parseField(string)).getValue();
            else
                return YearToMonth.valueOf(string);
        }

        private final String renderYTM(Scope scope, YearToMonth ytm) {
            return renderYTM(scope, ytm, Object::toString);
        }

        private final String renderYTM(Scope scope, YearToMonth ytm, Function<? super YearToMonth, ? extends String> toString) {
            if (ytm == null)
                return null;
            else if (REQUIRE_STANDARD_INTERVAL.contains(scope.dialect()))
                return "INTERVAL '" + toString.apply(ytm) + "' YEAR TO MONTH";
            else
                return toString.apply(ytm);
        }

        @Override
        final int sqltype(Statement statement, Configuration configuration) {
            return Types.VARCHAR;
        }
    }





























































































}

