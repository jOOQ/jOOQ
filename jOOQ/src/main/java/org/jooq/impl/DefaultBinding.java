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
// ...
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
// ...
import static org.jooq.SQLDialect.HSQLDB;
// ...
// ...
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.impl.DSL.cast;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.keyword;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.using;
import static org.jooq.impl.DefaultExecuteContext.localTargetConnection;
import static org.jooq.impl.Keywords.K_ARRAY;
import static org.jooq.impl.Keywords.K_AS;
import static org.jooq.impl.Keywords.K_BLOB;
import static org.jooq.impl.Keywords.K_CAST;
import static org.jooq.impl.Keywords.K_DATE;
import static org.jooq.impl.Keywords.K_DATETIME;
import static org.jooq.impl.Keywords.K_DATETIMEOFFSET;
import static org.jooq.impl.Keywords.K_FALSE;
import static org.jooq.impl.Keywords.K_HOUR_TO_SECOND;
import static org.jooq.impl.Keywords.K_NULL;
import static org.jooq.impl.Keywords.K_TIME;
import static org.jooq.impl.Keywords.K_TIMESTAMP;
import static org.jooq.impl.Keywords.K_TIMESTAMP_WITH_TIME_ZONE;
import static org.jooq.impl.Keywords.K_TIME_WITH_TIME_ZONE;
import static org.jooq.impl.Keywords.K_TRUE;
import static org.jooq.impl.Keywords.K_YEAR_TO_DAY;
import static org.jooq.impl.Keywords.K_YEAR_TO_FRACTION;
import static org.jooq.impl.SQLDataType.DOUBLE;
import static org.jooq.impl.Tools.attachRecords;
import static org.jooq.impl.Tools.convertBytesToHex;
import static org.jooq.impl.Tools.getMappedUDTName;
import static org.jooq.impl.Tools.needsBackslashEscaping;
import static org.jooq.impl.Tools.quotesNeedsBackslashEscaping;
import static org.jooq.tools.jdbc.JDBCUtils.safeClose;
import static org.jooq.tools.jdbc.JDBCUtils.safeFree;
import static org.jooq.tools.jdbc.JDBCUtils.wasNull;
import static org.jooq.tools.reflect.Reflect.on;
import static org.jooq.util.postgres.PostgresUtils.toPGArrayString;
import static org.jooq.util.postgres.PostgresUtils.toPGInterval;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// ...
import org.jooq.Attachable;
import org.jooq.Binding;
import org.jooq.BindingGetResultSetContext;
import org.jooq.BindingGetSQLInputContext;
import org.jooq.BindingGetStatementContext;
import org.jooq.BindingRegisterContext;
import org.jooq.BindingSQLContext;
import org.jooq.BindingSetSQLOutputContext;
import org.jooq.BindingSetStatementContext;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.Converter;
import org.jooq.Converters;
import org.jooq.DataType;
import org.jooq.EnumType;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.RenderContext;
import org.jooq.Result;
import org.jooq.Row;
import org.jooq.SQLDialect;
import org.jooq.Schema;
import org.jooq.Scope;
import org.jooq.TableRecord;
import org.jooq.UDTRecord;
import org.jooq.exception.ControlFlowSignal;
import org.jooq.exception.DataTypeException;
import org.jooq.exception.MappingException;
import org.jooq.exception.SQLDialectNotSupportedException;
import org.jooq.tools.Convert;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.StringUtils;
import org.jooq.tools.jdbc.JDBCUtils;
import org.jooq.tools.jdbc.MockArray;
import org.jooq.tools.jdbc.MockResultSet;
import org.jooq.types.DayToSecond;
import org.jooq.types.Interval;
import org.jooq.types.UByte;
import org.jooq.types.UInteger;
import org.jooq.types.ULong;
import org.jooq.types.UShort;
import org.jooq.types.YearToMonth;
import org.jooq.util.postgres.PostgresUtils;

/**
 * @author Lukas Eder
 */
public class DefaultBinding<T, U> implements Binding<T, U> {

    static final JooqLogger     log              = JooqLogger.getLogger(DefaultBinding.class);

    /**
     * Generated UID
     */
    private static final long   serialVersionUID = -198499389344950496L;

    final AbstractBinding<T, U> delegate;

    public final static <T, U> Binding<T, U> binding(Converter<T, U> converter) {
        return binding(converter, false);
    }

    static final <T> Binding<T, T> binding(Class<T> type, boolean isLob) {
        return binding(Converters.identity(type), isLob);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    static final <T, U> Binding<T, U> binding(Converter<T, U> converter, boolean isLob) {
        Class<?> type = converter.fromType();

        // Concrete types
        if (type == BigDecimal.class)
            return new DefaultBigDecimalBinding(converter, isLob);
        else if (type == BigInteger.class)
            return new DefaultBigIntegerBinding(converter, isLob);
        else if (type == Blob.class)
            return new DefaultBlobBinding(converter, isLob);
        else if (type == Boolean.class)
            return new DefaultBooleanBinding(converter, isLob);
        else if (type == Byte.class || type == byte.class)
            return new DefaultByteBinding(converter, isLob);
        else if (type == byte[].class)
            return new DefaultBytesBinding(converter, isLob);
        else if (type == Clob.class)
            return new DefaultClobBinding(converter, isLob);
        else if (type == Date.class)
            return new DefaultDateBinding(converter, isLob);
        else if (type == DayToSecond.class)
            return new DefaultDayToSecondBinding(converter, isLob);
        else if (type == Double.class || type == double.class)
            return new DefaultDoubleBinding(converter, isLob);
        else if (type == Float.class || type == float.class)
            return new DefaultFloatBinding(converter, isLob);
        else if (type == Integer.class || type == int.class)
            return new DefaultIntegerBinding(converter, isLob);

        else if (type == LocalDate.class) {
            DateToLocalDateConverter c1 = new DateToLocalDateConverter();
            Converter<LocalDate, U> c2 = (Converter<LocalDate, U>) converter;
            Converter<Date, U> c3 = Converters.of(c1, c2);
            return (Binding<T, U>) new DelegatingBinding<LocalDate, Date, U>(c1, c2, new DefaultDateBinding<U>(c3, isLob), isLob);
        }
        else if (type == LocalDateTime.class) {
            TimestampToLocalDateTimeConverter c1 = new TimestampToLocalDateTimeConverter();
            Converter<LocalDateTime, U> c2 = (Converter<LocalDateTime, U>) converter;
            Converter<Timestamp, U> c3 = Converters.of(c1, c2);
            return (Binding<T, U>) new DelegatingBinding<LocalDateTime, Timestamp, U>(c1, c2, new DefaultTimestampBinding<U>(c3, isLob), isLob);
        }
        else if (type == LocalTime.class) {
            TimeToLocalTimeConverter c1 = new TimeToLocalTimeConverter();
            Converter<LocalTime, U> c2 = (Converter<LocalTime, U>) converter;
            Converter<Time, U> c3 = Converters.of(c1, c2);
            return (Binding<T, U>) new DelegatingBinding<LocalTime, Time, U>(c1, c2, new DefaultTimeBinding<U>(c3, isLob), isLob);
        }

        else if (type == Long.class || type == long.class)
            return new DefaultLongBinding(converter, isLob);

        else if (type == OffsetDateTime.class)
            return new DefaultOffsetDateTimeBinding(converter, isLob);
        else if (type == OffsetTime.class)
            return new DefaultOffsetTimeBinding(converter, isLob);

        else if (type == Short.class || type == short.class)
            return new DefaultShortBinding(converter, isLob);
        else if (type == String.class)
            return new DefaultStringBinding(converter, isLob);
        else if (type == Time.class)
            return new DefaultTimeBinding(converter, isLob);
        else if (type == Timestamp.class)
            return new DefaultTimestampBinding(converter, isLob);
        else if (type == UByte.class)
            return new DefaultUByteBinding(converter, isLob);
        else if (type == UInteger.class)
            return new DefaultUIntegerBinding(converter, isLob);
        else if (type == ULong.class)
            return new DefaultULongBinding(converter, isLob);
        else if (type == UShort.class)
            return new DefaultUShortBinding(converter, isLob);
        else if (type == UUID.class)
            return new DefaultUUIDBinding(converter, isLob);
        else if (type == YearToMonth.class)
            return new DefaultYearToMonthBinding(converter, isLob);

        // Subtypes of array types etc.
        // The type byte[] is handled earlier. byte[][] can be handled here
        else if (type.isArray())
            return new DefaultArrayBinding(converter, isLob);






        else if (EnumType.class.isAssignableFrom(type))
            return new DefaultEnumTypeBinding(converter, isLob);
        else if (Record.class.isAssignableFrom(type))
            return new DefaultRecordBinding(converter, isLob);
        else if (Result.class.isAssignableFrom(type))
            return new DefaultResultBinding(converter, isLob);

        // Undefined types
        else
            return new DefaultOtherBinding(converter, isLob);

    }

    /**
     * @deprecated - 3.11 - [#6631] - Use {@link #binding(Converter)} instead.
     */
    @Deprecated
    public DefaultBinding(Converter<T, U> converter) {
        this(converter, false);
    }

    /**
     * @deprecated - 3.11 - [#6631] - Use {@link #binding(Converter)} instead.
     */
    @Deprecated
    DefaultBinding(Converter<T, U> converter, boolean isLob) {
        this.delegate = (AbstractBinding<T, U>) binding(converter, isLob);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    static final <T, X, U> Binding<T, U> newBinding(final Converter<X, U> converter, final DataType<T> type, final Binding<T, X> binding) {
        final Binding<T, U> theBinding;


        if (converter == null && binding == null) {
            theBinding = (Binding) type.getBinding();
        }
        else if (converter == null) {
            theBinding = (Binding) binding;
        }
        else if (binding == null) {
            theBinding = (Binding) binding(converter, type.isLob());
        }
        else {
            theBinding = new Binding<T, U>() {

                /**
                 * Generated UID
                 */
                private static final long serialVersionUID = 8912340791845209886L;

                final Converter<T, U>     theConverter     = Converters.of(binding.converter(), converter);

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

    static final Map<String, Class<?>> typeMap(Class<?> type, Configuration configuration) {
        return typeMap(type, configuration, new HashMap<String, Class<?>>());
    }

    @SuppressWarnings("unchecked")
    static final Map<String, Class<?>> typeMap(Class<?> type, Configuration configuration, Map<String, Class<?>> result) {
        try {
            if (UDTRecord.class.isAssignableFrom(type)) {
                Class<UDTRecord<?>> t = (Class<UDTRecord<?>>) type;
                result.put(getMappedUDTName(configuration, t), t);
                UDTRecord<?> r = t.newInstance();
                for (Field<?> field : r.getUDT().fields())
                    typeMap(field.getType(), configuration, result);
            }









        }
        catch (Exception e) {
            throw new MappingException("Error while collecting type map", e);
        }

        return result;
    }

    private static final long parse(Class<? extends java.util.Date> type, String date) throws SQLException {

        // Try reading a plain number first
        try {
            return Long.valueOf(date);
        }

        // If that fails, try reading a formatted date
        catch (NumberFormatException e) {

            if (type == Timestamp.class)
                return Timestamp.valueOf(date).getTime();

            // Dates may come with " 00:00:00". This is safely trimming time information
            if (type == Date.class)
                return Date.valueOf(date.split(" ")[0]).getTime();

            if (type == Time.class)
                return Time.valueOf(date).getTime();

            throw new SQLException("Could not parse date " + date, e);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Implementation of Binding API for backwards compatibility
    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public Converter<T, U> converter() {
        return delegate.converter;
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
        return "DefaultBinding [type=" + delegate.type + ", converter=" + delegate.converter + "]";
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Type-specific subclasses API
    // -----------------------------------------------------------------------------------------------------------------

    abstract static class AbstractBinding<T, U> implements Binding<T, U> {

        /**
         * Generated UID
         */
        private static final long                serialVersionUID                    = -7965247586545864991L;
        private static final EnumSet<SQLDialect> NEEDS_PRECISION_SCALE_ON_BIGDECIMAL = EnumSet.of(CUBRID, DERBY, FIREBIRD, HSQLDB);




        final Class<T>                           type;
        final Converter<T, U>                    converter;

        // TODO: This type boolean should not be passed standalone to the
        // constructor. Find a better design
        final boolean                            isLob;

        AbstractBinding(Converter<T, U> converter, boolean isLob) {
            this.type = converter.fromType();
            this.converter = converter;
            this.isLob = isLob;
        }

        @Override
        public final Converter<T, U> converter() {
            return converter;
        }

        private final boolean shouldCast(BindingSQLContext<U> ctx, T converted) {

            // In default mode, casting is only done when parameters are NOT inlined
            if (ctx.render().paramType() != INLINED) {

                // Generated enums should not be cast...
                if (!(converted instanceof EnumType)) {
                    switch (ctx.family()) {

                        // These dialects can hardly detect the type of a bound constant.




                        case DERBY:
                        case FIREBIRD:

                        // These dialects have some trouble, when they mostly get it right.
                        case H2:
                        case HSQLDB:

                        // [#1261] There are only a few corner-cases, where this is
                        // really needed. Check back on related CUBRID bugs
                        case CUBRID:

                        // [#1029] Postgres and [#632] Sybase need explicit casting
                        // in very rare cases.





                        case POSTGRES: {
                            return true;
                        }
                    }
                }
            }

            // [#566] JDBC doesn't explicitly support interval data types. To be on
            // the safe side, always cast these types in those dialects that support
            // them
            if (Interval.class.isAssignableFrom(type)) {
                switch (ctx.family()) {



                    case POSTGRES:
                        return true;
                }
            }

            return false;
        }

        /**
         * Render the bind variable including a cast, if necessary
         */
        private final void sqlCast(BindingSQLContext<U> ctx, T converted) throws SQLException {
            DataType<T> dataType = DefaultDataType.getDataType(ctx.dialect(), type);
            DataType<T> sqlDataType = dataType.getSQLDataType();
            SQLDialect family = ctx.family();

            // [#822] Some RDBMS need precision / scale information on BigDecimals
            if (converted != null && type == BigDecimal.class && NEEDS_PRECISION_SCALE_ON_BIGDECIMAL.contains(family)) {

                // Add precision / scale on BigDecimals
                int scale = ((BigDecimal) converted).scale();
                int precision = ((BigDecimal) converted).precision();

                // [#5323] BigDecimal precision is always 1 for BigDecimals smaller than 1.0
                if (scale >= precision)
                    precision = scale + 1;

                sqlCast(ctx, converted, dataType, 0, precision, scale);
            }

            // [#1028] Most databases don't know an OTHER type (except H2, HSQLDB).
            else if (SQLDataType.OTHER == sqlDataType)

                // If the bind value is set, it can be used to derive the cast type
                if (converted != null)
                    sqlCast(ctx, converted, DefaultDataType.getDataType(family, converted.getClass()), 0, 0, 0);








                // Derby and DB2 must have a type associated with NULL. Use VARCHAR
                // as a workaround. That's probably not correct in all cases, though
                else
                    sqlCast(ctx, converted, DefaultDataType.getDataType(family, String.class), 0, 0, 0);

            // [#1029] Postgres generally doesn't need the casting. Only in the
            // above case where the type is OTHER
            // [#1125] Also with temporal data types, casting is needed some times
            // [#4338] ... specifically when using JSR-310 types
            // [#1130] TODO type can be null for ARRAY types, etc.
            else if (POSTGRES == family && (sqlDataType == null || !sqlDataType.isTemporal()))
                sql(ctx, converted);

            // [#1727] VARCHAR types should be cast to their actual lengths in some
            // dialects
            else if ((sqlDataType == SQLDataType.VARCHAR || sqlDataType == SQLDataType.CHAR) && FIREBIRD == family)
                sqlCast(ctx, converted, dataType, getValueLength((String) converted), 0, 0);







            // In all other cases, the bind variable can be cast normally
            else {
                sqlCast(ctx, converted, dataType, dataType.length(), dataType.precision(), dataType.scale());
            }
        }

        private static final int getValueLength(String string) {
            if (string == null) {
                return 1;
            }

            else {
                int length = string.length();

                // If non 7-bit ASCII characters are present, multiply the length by
                // 4 to be sure that even UTF-32 collations will fit. But don't use
                // larger numbers than Derby's upper limit 32672
                for (int i = 0; i < length; i++) {
                    if (string.charAt(i) > 127) {
                        return Math.min(32672, 4 * length);
                    }
                }

                return Math.min(32672, length);
            }
        }

        private final void sqlCast(BindingSQLContext<U> ctx, T converted, DataType<?> dataType, int length, int precision, int scale) throws SQLException {
            ctx.render().visit(K_CAST).sql('(');
            sql(ctx, converted);
            ctx.render().sql(' ').visit(K_AS).sql(' ')
                        .sql(dataType.length(length).precision(precision, scale).getCastTypeName(ctx.configuration()))
                        .sql(')');
        }

        @Override
        public final void sql(BindingSQLContext<U> ctx) throws SQLException {
            T converted = converter().to(ctx.value());

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
            if (shouldCast(ctx, converted)) {
                sqlCast(ctx, converted);
            }

            // Most RDBMS can infer types for bind values
            else {
                sql(ctx, converted);
            }
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
        static final String escape(Object val, Context<?> context) {
            String result = val.toString();

            if (needsBackslashEscaping(context.configuration()))
                result = StringUtils.replace(result, "\\", "\\\\");

            if (quotesNeedsBackslashEscaping(context.configuration()))
                result = StringUtils.replace(result, "'", "\\'");
            else
                result = StringUtils.replace(result, "'", "''");

            return result;
        }

        @Override
        public final void register(BindingRegisterContext<U> ctx) throws SQLException {
            if (!FALSE.equals(ctx.settings().isExecuteLogging()))
                if (log.isTraceEnabled())
                    log.trace("Registering variable " + ctx.index(), "" + type);

            register0(ctx);
        }

        @Override
        public final void set(BindingSetStatementContext<U> ctx) throws SQLException {
            T value = converter().to(ctx.value());

            if (!FALSE.equals(ctx.settings().isExecuteLogging()))
                if (log.isTraceEnabled())
                    if (value != null && value.getClass().isArray() && value.getClass() != byte[].class)
                        log.trace("Binding variable " + ctx.index(), Arrays.asList((Object[]) value) + " (" + type + ")");
                    else
                        log.trace("Binding variable " + ctx.index(), value + " (" + type + ")");

            if (value == null)
                setNull0(ctx);
            else
                set0(ctx, value);
        }

        @Override
        public final void set(BindingSetSQLOutputContext<U> ctx) throws SQLException {
            T value = converter().to(ctx.value());

            if (value == null)
                ctx.output().writeObject(null);
            else
                set0(ctx, value);
        }

        @Override
        public final void get(BindingGetResultSetContext<U> ctx) throws SQLException {
            ctx.value(attach(converter().from(get0(ctx)), ctx.configuration()));
        }

        @Override
        public final void get(BindingGetStatementContext<U> ctx) throws SQLException {
            ctx.value(attach(converter().from(get0(ctx)), ctx.configuration()));
        }

        @Override
        public final void get(BindingGetSQLInputContext<U> ctx) throws SQLException {
            ctx.value(attach(converter().from(get0(ctx)), ctx.configuration()));
        }

        private static final <U> U attach(U value, Configuration configuration) {

            // [#4372] Attach records if possible / required
            if (value instanceof Attachable && attachRecords(configuration))
                ((Attachable) value).attach(configuration);

            return value;
        }

        final Class<T> type() {
            return type;
        }

        /* non-final */ void setNull0(BindingSetStatementContext<U> ctx) throws SQLException {
            ctx.statement().setNull(ctx.index(), sqltype(ctx.configuration()));
        }

        /* non-final */ void register0(BindingRegisterContext<U> ctx) throws SQLException {
            ctx.statement().registerOutParameter(ctx.index(), sqltype(ctx.configuration()));
        }

        @SuppressWarnings("unused")
        /* non-final */ void sqlInline0(BindingSQLContext<U> ctx, T value) throws SQLException {

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
        abstract int sqltype(Configuration configuration) throws SQLException;

        // -----------------------------------------------------------------------------------------------------------------
        // Object API
        // -----------------------------------------------------------------------------------------------------------------

        @Override
        public String toString() {
            return "AbstractBinding [type=" + type + ", converter=" + converter + "]";
        }
    }

    static final class DelegatingBinding<X, T, U> extends AbstractBinding<X, U> {

        /**
         * Generated UID
         */
        private static final long           serialVersionUID = 9222783475194108822L;

        private final Converter<T, X>       delegatingConverter;
        private final AbstractBinding<T, U> delegatingBinding;

        DelegatingBinding(
            Converter<T, X> delegatingConverter,
            Converter<X, U> originalConverter,
            AbstractBinding<T, U> delegatingBinding,
            boolean isLob
        ) {
            super(originalConverter, isLob);

            this.delegatingConverter = delegatingConverter;
            this.delegatingBinding = delegatingBinding;
        }

        @Override
        final void sqlInline0(BindingSQLContext<U> ctx, X value) throws SQLException {
            delegatingBinding.sqlInline0(ctx, delegatingConverter.to(value));
        }

        @Override
        final void sqlBind0(BindingSQLContext<U> ctx, X value) throws SQLException {
            delegatingBinding.sqlBind0(ctx, delegatingConverter.to(value));
        }

        @Override
        final void set0(BindingSetStatementContext<U> ctx, X value) throws SQLException {
            delegatingBinding.set0(ctx, delegatingConverter.to(value));
        }

        @Override
        final void setNull0(BindingSetStatementContext<U> ctx) throws SQLException {
            delegatingBinding.setNull0(ctx);
        }

        @Override
        final void set0(BindingSetSQLOutputContext<U> ctx, X value) throws SQLException {
            delegatingBinding.set0(ctx, delegatingConverter.to(value));
        }

        @Override
        final X get0(BindingGetResultSetContext<U> ctx) throws SQLException {
            return delegatingConverter.from(delegatingBinding.get0(ctx));
        }

        @Override
        final X get0(BindingGetStatementContext<U> ctx) throws SQLException {
            return delegatingConverter.from(delegatingBinding.get0(ctx));
        }

        @Override
        final X get0(BindingGetSQLInputContext<U> ctx) throws SQLException {
            return delegatingConverter.from(delegatingBinding.get0(ctx));
        }

        @Override
        final int sqltype(Configuration configuration) throws SQLException {
            return delegatingBinding.sqltype(configuration);
        }
    }

    static final class DefaultArrayBinding<U> extends AbstractBinding<Object[], U> {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = 6875984626674331432L;

        DefaultArrayBinding(Converter<Object[], U> converter, boolean isLob) {
            super(converter, isLob);
        }

        @SuppressWarnings("unchecked")
        @Override
        final void sqlInline0(BindingSQLContext<U> ctx, Object[] value) throws SQLException {
            String separator = "";

            // H2 renders arrays as rows
            if (ctx.family() == H2) {
                ctx.render().sql('(');

                for (Object o : value) {
                    ctx.render().sql(separator);
                    binding((Class<Object>) type.getComponentType(), isLob).sql(new DefaultBindingSQLContext<Object>(ctx.configuration(), ctx.data(), ctx.render(), o));
                    separator = ", ";
                }

                ctx.render().sql(')');
            }

            else if (ctx.family() == POSTGRES) {
                ctx.render().visit(cast(inline(PostgresUtils.toPGArrayString(value)), type));
            }

            // By default, render HSQLDB / POSTGRES syntax
            else {
                ctx.render().visit(K_ARRAY);
                ctx.render().sql('[');

                for (Object o : value) {
                    ctx.render().sql(separator);
                    binding((Class<Object>) type.getComponentType(), isLob).sql(new DefaultBindingSQLContext<Object>(ctx.configuration(), ctx.data(), ctx.render(), o));
                    separator = ", ";
                }

                ctx.render().sql(']');

                // [#3214] Some PostgreSQL array type literals need explicit casting
                if (ctx.family() == POSTGRES && EnumType.class.isAssignableFrom(type.getComponentType()))
                    DefaultEnumTypeBinding.pgRenderEnumCast(ctx.render(), type);
            }
        }

        @Override
        final void sqlBind0(BindingSQLContext<U> ctx, Object[] value) throws SQLException {
            super.sqlBind0(ctx, value);

            // In Postgres, some additional casting must be done in some cases...
            if (ctx.family() == SQLDialect.POSTGRES) {

                // Postgres needs explicit casting for enum (array) types
                if (EnumType.class.isAssignableFrom(type.getComponentType()))
                    DefaultEnumTypeBinding.pgRenderEnumCast(ctx.render(), type);

                // ... and also for other array types
                else
                    ctx.render().sql("::")
                                .sql(DefaultDataType.getDataType(ctx.family(), type).getCastTypeName(ctx.render().configuration()));
            }
        }

        @SuppressWarnings({ "rawtypes", "unchecked" })
        @Override
        final void set0(BindingSetStatementContext<U> ctx, Object[] value) throws SQLException {
            switch (ctx.family()) {
                case POSTGRES: {
                    ctx.statement().setString(ctx.index(), toPGArrayString(value));
                    break;
                }
                case HSQLDB: {
                    Object[] a = value;
                    Class<?> t = type;

                    // [#2325] [#5823] Cannot bind UUID[] type in HSQLDB.
                    // See also: https://sourceforge.net/p/hsqldb/bugs/1466
                    if (t == UUID[].class) {
                        a = Convert.convertArray(a, byte[][].class);
                        t = byte[][].class;
                    }

                    ctx.statement().setArray(ctx.index(), new MockArray(ctx.family(), a, t));
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
            ctx.output().writeArray(new MockArray(ctx.family(), value, type()));
        }

        @Override
        final Object[] get0(BindingGetResultSetContext<U> ctx) throws SQLException {
            switch (ctx.family()) {
                case POSTGRES:
                    return pgGetArray(ctx, ctx.resultSet(), type, ctx.index());

                default:
                    // Note: due to a HSQLDB bug, it is not recommended to call rs.getObject() here:
                    // See https://sourceforge.net/tracker/?func=detail&aid=3181365&group_id=23316&atid=378131
                    return convertArray(ctx.resultSet().getArray(ctx.index()), type());
            }
        }

        @Override
        final Object[] get0(BindingGetStatementContext<U> ctx) throws SQLException {
            return convertArray(ctx.statement().getObject(ctx.index()), type());
        }

        @Override
        final Object[] get0(BindingGetSQLInputContext<U> ctx) throws SQLException {
            Array array = ctx.input().readArray();
            return array == null ? null : (Object[]) array.getArray();
        }

        @Override
        final int sqltype(Configuration configuration) {
            return Types.ARRAY;
        }

        /**
         * Workarounds for the unimplemented Postgres JDBC driver features
         */
        @SuppressWarnings("unchecked")
        private static final <T> T pgGetArray(Scope ctx, ResultSet rs, Class<T> type, int index) throws SQLException {

            // Get the JDBC Array and check for null. If null, that's OK
            Array array = null;
            try {

                array = rs.getArray(index);
                if (array == null) {
                    return null;
                }

                // Try fetching a Java Object[]. That's gonna work for non-UDT types
                try {

                    // [#5633] Special treatment for this type.
                    // [#5586] [#5613] TODO: Improve PostgreSQL array deserialisation.
                    if (byte[][].class == type)
                        throw new ControlFlowSignal("GOTO the next array deserialisation strategy");
                    else
                        return (T) convertArray(array, (Class<? extends Object[]>) type);
                }

                // This might be a UDT (not implemented exception...)
                catch (Exception e) {
                    List<Object> result = new ArrayList<Object>();
                    ResultSet arrayRs = null;

                    // Try fetching the array as a JDBC ResultSet
                    try {
                        arrayRs = array.getResultSet();
                        Binding<T, T> binding = binding((Class<T>) type.getComponentType(), false);
                        DefaultBindingGetResultSetContext<T> out = new DefaultBindingGetResultSetContext<T>(ctx.configuration(), ctx.data(), arrayRs, 2);

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

                    finally {
                        safeClose(arrayRs);
                    }

                    return (T) convertArray(result.toArray(), (Class<? extends Object[]>) type);
                }
            }

            finally {
                safeFree(array);
            }
        }

        private static final Object[] convertArray(Object array, Class<? extends Object[]> type) throws SQLException {
            if (array instanceof Object[])
                return Convert.convert(array, type);
            else if (array instanceof Array)
                return convertArray((Array) array, type);

            return null;
        }

        private static final Object[] convertArray(Array array, Class<? extends Object[]> type) throws SQLException {
            if (array != null)
                return Convert.convert(array.getArray(), type);

            return null;
        }
    }





















































































































































































    static final class DefaultBigDecimalBinding<U> extends AbstractBinding<BigDecimal, U> {

        /**
         * Generated UID
         */
        private static final long                serialVersionUID = -8912971184035434281L;
        private static final EnumSet<SQLDialect> BIND_AS_STRING   = EnumSet.of(SQLITE);

        DefaultBigDecimalBinding(Converter<BigDecimal, U> converter, boolean isLob) {
            super(converter, isLob);
        }

        @Override
        final void sqlInline0(BindingSQLContext<U> ctx, BigDecimal value) {
            ctx.render().sql(value.toString());
        }

        @Override
        final void set0(BindingSetStatementContext<U> ctx, BigDecimal value) throws SQLException {
            if (BIND_AS_STRING.contains(ctx.family()))
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
        final int sqltype(Configuration configuration) {
            return Types.DECIMAL;
        }
    }

    static final class DefaultBigIntegerBinding<U> extends AbstractBinding<BigInteger, U> {

        /**
         * Generated UID
         */
        private static final long                serialVersionUID = -3857031689661809421L;
        private static final EnumSet<SQLDialect> BIND_AS_STRING   = EnumSet.of(SQLITE);

        DefaultBigIntegerBinding(Converter<BigInteger, U> converter, boolean isLob) {
            super(converter, isLob);
        }

        @Override
        final void sqlInline0(BindingSQLContext<U> ctx, BigInteger value) {
            ctx.render().sql(value.toString());
        }

        @Override
        final void set0(BindingSetStatementContext<U> ctx, BigInteger value) throws SQLException {
            if (BIND_AS_STRING.contains(ctx.family()))
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
            if (ctx.configuration().dialect() == SQLDialect.SQLITE)
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
        final int sqltype(Configuration configuration) {
            return Types.DECIMAL;
        }
    }

    static final class DefaultBlobBinding<U> extends AbstractBinding<Blob, U> {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = -4605427469676162501L;

        DefaultBlobBinding(Converter<Blob, U> converter, boolean isLob) {
            super(converter, isLob);
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
        final int sqltype(Configuration configuration) {
            switch (configuration.family()) {
                // [#1225] [#1227] TODO Put this logic into DataType
                // Some dialects have trouble binding binary data as BLOB
                // Same logic in DefaultBytesBinding

                case POSTGRES:





                    return Types.BINARY;

                default:
                    return Types.BLOB;
            }
        }
    }

    static final class DefaultBooleanBinding<U> extends AbstractBinding<Boolean, U> {

        /**
         * Generated UID
         */
        private static final long                serialVersionUID   = -533762957890251203L;
        private static final EnumSet<SQLDialect> BIND_AS_1_0        = EnumSet.of(FIREBIRD, SQLITE);




        DefaultBooleanBinding(Converter<Boolean, U> converter, boolean isLob) {
            super(converter, isLob);
        }

        @Override
        final void sqlInline0(BindingSQLContext<U> ctx, Boolean value) {

            // [#1153] Some dialects don't support boolean literals TRUE and FALSE
            if (BIND_AS_1_0.contains(ctx.family()))
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










            ctx.statement().setBoolean(ctx.index(), value);
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
            return wasNull(ctx.resultSet(), Boolean.valueOf(ctx.resultSet().getBoolean(ctx.index())));
        }

        @Override
        final Boolean get0(BindingGetStatementContext<U> ctx) throws SQLException {
            return wasNull(ctx.statement(), Boolean.valueOf(ctx.statement().getBoolean(ctx.index())));
        }

        @Override
        final Boolean get0(BindingGetSQLInputContext<U> ctx) throws SQLException {
            return wasNull(ctx.input(), Boolean.valueOf(ctx.input().readBoolean()));
        }

        @Override
        final int sqltype(Configuration configuration) {
            return Types.BOOLEAN;
        }
    }

    static final class DefaultByteBinding<U> extends AbstractBinding<Byte, U> {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = -7328193812163714614L;

        DefaultByteBinding(Converter<Byte, U> converter, boolean isLob) {
            super(converter, isLob);
        }

        @Override
        final void sqlInline0(BindingSQLContext<U> ctx, Byte value) {
            ctx.render().sql(value.toString());
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
            return wasNull(ctx.resultSet(), Byte.valueOf(ctx.resultSet().getByte(ctx.index())));
        }

        @Override
        final Byte get0(BindingGetStatementContext<U> ctx) throws SQLException {
            return wasNull(ctx.statement(), Byte.valueOf(ctx.statement().getByte(ctx.index())));
        }

        @Override
        final Byte get0(BindingGetSQLInputContext<U> ctx) throws SQLException {
            return wasNull(ctx.input(), Byte.valueOf(ctx.input().readByte()));
        }

        @Override
        final int sqltype(Configuration configuration) {
            return Types.TINYINT;
        }
    }

    static final class DefaultBytesBinding<U> extends AbstractBinding<byte[], U> {

        /**
         * Generated UID
         */
        private static final long                serialVersionUID = -727202499908007757L;
        private static final EnumSet<SQLDialect> INLINE_AS_X_APOS = EnumSet.of(DERBY, H2, HSQLDB, MARIADB, MYSQL, SQLITE);




        DefaultBytesBinding(Converter<byte[], U> converter, boolean isLob) {
            super(converter, isLob);
        }

        @Override
        final void sqlInline0(BindingSQLContext<U> ctx, byte[] value) {
            // [#1154] Binary data cannot always be inlined













            if (INLINE_AS_X_APOS.contains(ctx.family()))
                ctx.render()
                   .sql("X'")
                   .sql(convertBytesToHex(value))
                   .sql('\'');







            else if (ctx.family() == POSTGRES)
                ctx.render()
                   .sql("E'")
                   .sql(PostgresUtils.toPGString(value))
                   .sql("'::bytea");

            // This default behaviour is used in debug logging for dialects
            // that do not support inlining binary data
            else
                ctx.render()
                   .sql("X'")
                   .sql(convertBytesToHex(value))
                   .sql('\'');
        }

        @Override
        final void set0(BindingSetStatementContext<U> ctx, byte[] value) throws SQLException {
            ctx.statement().setBytes(ctx.index(), value);
        }

        @Override
        final void set0(BindingSetSQLOutputContext<U> ctx, byte[] value) throws SQLException {
























            ctx.output().writeBytes(value);
        }

        @Override
        final byte[] get0(BindingGetResultSetContext<U> ctx) throws SQLException {
            return ctx.resultSet().getBytes(ctx.index());
        }

        @Override
        final byte[] get0(BindingGetStatementContext<U> ctx) throws SQLException {
            return ctx.statement().getBytes(ctx.index());
        }

        @Override
        final byte[] get0(BindingGetSQLInputContext<U> ctx) throws SQLException {
















            return ctx.input().readBytes();
        }

        @Override
        final int sqltype(Configuration configuration) {
            switch (configuration.family()) {
                // [#1225] [#1227] TODO Put this logic into DataType
                // Some dialects have trouble binding binary data as BLOB
                // Same logic in DefaultBlobBinding

                case POSTGRES:





                    return Types.BINARY;

                default:
                    return Types.BLOB;
            }
        }
    }

    static final class DefaultClobBinding<U> extends AbstractBinding<Clob, U> {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = -3890447233590678873L;

        DefaultClobBinding(Converter<Clob, U> converter, boolean isLob) {
            super(converter, isLob);
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
        final int sqltype(Configuration configuration) {
            return Types.CLOB;
        }
    }

    static final class DefaultDateBinding<U> extends AbstractBinding<Date, U> {

        /**
         * Generated UID
         */
        private static final long                serialVersionUID         = -4797649501187223237L;
        private static final EnumSet<SQLDialect> INLINE_AS_STRING_LITERAL = EnumSet.of(SQLITE);

        DefaultDateBinding(Converter<Date, U> converter, boolean isLob) {
            super(converter, isLob);
        }

        @Override
        final void sqlInline0(BindingSQLContext<U> ctx, Date value) {
            // [#1156] DATE / TIME inlining is very vendor-specific

            // The SQLite JDBC driver does not implement the escape syntax
            // [#1253] Sybase does not implement date literals
            if (INLINE_AS_STRING_LITERAL.contains(ctx.family()))
                ctx.render().sql('\'').sql(escape(value, ctx.render())).sql('\'');













            // [#1253] Derby doesn't support the standard literal
            else if (ctx.family() == DERBY)
                ctx.render().visit(K_DATE).sql("('").sql(escape(value, ctx.render())).sql("')");

            // [#3648] Circumvent a MySQL bug related to date literals
            else if (ctx.family() == MYSQL)
                ctx.render().sql("{d '").sql(escape(value, ctx.render())).sql("'}");

            // Most dialects implement SQL standard date literals
            else
                ctx.render().visit(K_DATE).sql(" '").sql(escape(value, ctx.render())).sql('\'');
        }

        @Override
        final void sqlBind0(BindingSQLContext<U> ctx, Date value) throws SQLException {











            super.sqlBind0(ctx, value);
        }

        @Override
        final void register0(BindingRegisterContext<U> ctx) throws SQLException {








            super.register0(ctx);
        }

        @Override
        final void set0(BindingSetStatementContext<U> ctx, Date value) throws SQLException {
            if (ctx.family() == SQLITE)
                ctx.statement().setString(ctx.index(), value.toString());







            else
                ctx.statement().setDate(ctx.index(), value);
        }

        @Override
        final void set0(BindingSetSQLOutputContext<U> ctx, Date value) throws SQLException {








            ctx.output().writeDate(value);
        }

        @Override
        final Date get0(BindingGetResultSetContext<U> ctx) throws SQLException {
            SQLDialect family = ctx.family();

            // SQLite's type affinity needs special care...
            if (family == SQLITE) {
                String date = ctx.resultSet().getString(ctx.index());
                return date == null ? null : new Date(parse(Date.class, date));
            }









            else {
                return ctx.resultSet().getDate(ctx.index());
            }
        }

        @Override
        final Date get0(BindingGetStatementContext<U> ctx) throws SQLException {










            return ctx.statement().getDate(ctx.index());
        }

        @Override
        final Date get0(BindingGetSQLInputContext<U> ctx) throws SQLException {










            return ctx.input().readDate();
        }

        @Override
        final int sqltype(Configuration configuration) {
            return Types.DATE;
        }
    }

    static final class DefaultDayToSecondBinding<U> extends AbstractBinding<DayToSecond, U> {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = 4378118707359663541L;

        DefaultDayToSecondBinding(Converter<DayToSecond, U> converter, boolean isLob) {
            super(converter, isLob);
        }

        @Override
        final void set0(BindingSetStatementContext<U> ctx, DayToSecond value) throws SQLException {

            // [#566] Interval data types are best bound as Strings
            if (ctx.family() == POSTGRES)
                ctx.statement().setObject(ctx.index(), toPGInterval(value));
            else
                ctx.statement().setString(ctx.index(), value.toString());
        }

        @Override
        final void set0(BindingSetSQLOutputContext<U> ctx, DayToSecond value) throws SQLException {
            ctx.output().writeString(value.toString());
        }

        @Override
        final DayToSecond get0(BindingGetResultSetContext<U> ctx) throws SQLException {
            if (ctx.family() == POSTGRES) {
                Object object = ctx.resultSet().getObject(ctx.index());
                return object == null ? null : PostgresUtils.toDayToSecond(object);
            }
            else {
                String string = ctx.resultSet().getString(ctx.index());
                return string == null ? null : DayToSecond.valueOf(string);
            }
        }

        @Override
        final DayToSecond get0(BindingGetStatementContext<U> ctx) throws SQLException {
            if (ctx.family() == POSTGRES) {
                Object object = ctx.statement().getObject(ctx.index());
                return object == null ? null : PostgresUtils.toDayToSecond(object);
            }
            else {
                String string = ctx.statement().getString(ctx.index());
                return string == null ? null : DayToSecond.valueOf(string);
            }
        }

        @Override
        final DayToSecond get0(BindingGetSQLInputContext<U> ctx) throws SQLException {
            String string = ctx.input().readString();
            return string == null ? null : DayToSecond.valueOf(string);
        }

        @Override
        final int sqltype(Configuration configuration) {
            return Types.VARCHAR;
        }
    }

    static final class DefaultDoubleBinding<U> extends AbstractBinding<Double, U> {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = -615723070592774059L;

        DefaultDoubleBinding(Converter<Double, U> converter, boolean isLob) {
            super(converter, isLob);
        }

        @Override
        final void sqlInline0(BindingSQLContext<U> ctx, Double value) {

            // [#5249] [#6912] Special inlining of special floating point values
            if (value.isNaN())
                if (ctx.family() == POSTGRES)
                    ctx.render().visit(inline("NaN")).sql("::float8");
                else
                    ctx.render().visit(K_CAST).sql('(').visit(inline("NaN")).sql(' ').visit(K_AS).sql(' ').visit(keyword(DOUBLE.getCastTypeName(ctx.configuration()))).sql(')');
            else
                ctx.render().sql(value.toString());
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
            return wasNull(ctx.resultSet(), Double.valueOf(ctx.resultSet().getDouble(ctx.index())));
        }

        @Override
        final Double get0(BindingGetStatementContext<U> ctx) throws SQLException {
            return wasNull(ctx.statement(), Double.valueOf(ctx.statement().getDouble(ctx.index())));
        }

        @Override
        final Double get0(BindingGetSQLInputContext<U> ctx) throws SQLException {
            return wasNull(ctx.input(), Double.valueOf(ctx.input().readDouble()));
        }

        @Override
        final int sqltype(Configuration configuration) {
            return Types.DOUBLE;
        }
    }

    static final class DefaultEnumTypeBinding<U> extends AbstractBinding<EnumType, U> {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = -5858761464381778695L;

        DefaultEnumTypeBinding(Converter<EnumType, U> converter, boolean isLob) {
            super(converter, isLob);
        }

        @Override
        final void sqlInline0(BindingSQLContext<U> ctx, EnumType value) throws SQLException {
            String literal = value.getLiteral();

            if (literal == null)
                binding(String.class, isLob).sql(new DefaultBindingSQLContext<String>(ctx.configuration(), ctx.data(), ctx.render(), literal));
            else
                binding(String.class, isLob).sql(new DefaultBindingSQLContext<String>(ctx.configuration(), ctx.data(), ctx.render(), literal));
        }

        @Override
        final void sqlBind0(BindingSQLContext<U> ctx, EnumType value) throws SQLException {
            super.sqlBind0(ctx, value);

            // Postgres needs explicit casting for enum (array) types
            if (ctx.family() == SQLDialect.POSTGRES)
                pgRenderEnumCast(ctx.render(), type);
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
            return getEnumType(type(), ctx.resultSet().getString(ctx.index()));
        }

        @Override
        final EnumType get0(BindingGetStatementContext<U> ctx) throws SQLException {
            return getEnumType(type(), ctx.statement().getString(ctx.index()));
        }

        @Override
        final EnumType get0(BindingGetSQLInputContext<U> ctx) throws SQLException {
            return getEnumType(type(), ctx.input().readString());
        }

        @Override
        final int sqltype(Configuration configuration) {
            return Types.VARCHAR;
        }

        static final void pgRenderEnumCast(RenderContext render, Class<?> type) {

            @SuppressWarnings("unchecked")
            Class<? extends EnumType> enumType = (Class<? extends EnumType>) (
                type.isArray() ? type.getComponentType() : type);

            // [#968] Don't cast "synthetic" enum types (note, val can be null!)
            // [#4427] Java Enum agnostic implementation will work for Scala also
            EnumType[] enums = Tools.enums(enumType);

            if (enums == null || enums.length == 0)
                throw new IllegalArgumentException("Not a valid EnumType : " + type);

            Schema schema = enums[0].getSchema();
            if (schema != null) {
                render.sql("::");

                schema = using(render.configuration()).map(schema);
                if (schema != null && TRUE.equals(render.configuration().settings().isRenderSchema())) {
                    render.visit(schema);
                    render.sql('.');
                }

                render.visit(name(enums[0].getName()));
            }

            if (type.isArray())
                render.sql("[]");
        }

        @SuppressWarnings("unchecked")
        static final <E extends EnumType> E getEnumType(Class<? extends E> type, String literal) {
            try {
                EnumType[] list = Tools.enums(type);

                for (EnumType e : list)
                    if (e.getLiteral().equals(literal))
                        return (E) e;
            }
            catch (Exception e) {
                throw new DataTypeException("Unknown enum literal found : " + literal);
            }

            return null;
        }
    }

    static final class DefaultFloatBinding<U> extends AbstractBinding<Float, U> {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = -2468794191255859536L;

        DefaultFloatBinding(Converter<Float, U> converter, boolean isLob) {
            super(converter, isLob);
        }

        @Override
        final void sqlInline0(BindingSQLContext<U> ctx, Float value) {

            // [#5249] [#6912] Special inlining of special floating point values
            if (value.isNaN())
                if (ctx.family() == POSTGRES)
                    ctx.render().visit(inline("NaN")).sql("::float4");
                else
                    ctx.render().visit(K_CAST).sql('(').visit(inline("NaN")).sql(' ').visit(K_AS).sql(' ').visit(keyword(DOUBLE.getCastTypeName(ctx.configuration()))).sql(')');
            else
                ctx.render().sql(value.toString());
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
            return wasNull(ctx.resultSet(), Float.valueOf(ctx.resultSet().getFloat(ctx.index())));
        }

        @Override
        final Float get0(BindingGetStatementContext<U> ctx) throws SQLException {
            return wasNull(ctx.statement(), Float.valueOf(ctx.statement().getFloat(ctx.index())));
        }

        @Override
        final Float get0(BindingGetSQLInputContext<U> ctx) throws SQLException {
            return wasNull(ctx.input(), Float.valueOf(ctx.input().readFloat()));
        }

        @Override
        final int sqltype(Configuration configuration) {
            return Types.FLOAT;
        }
    }

    static final class DefaultIntegerBinding<U> extends AbstractBinding<Integer, U> {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = 1356528214897599147L;

        DefaultIntegerBinding(Converter<Integer, U> converter, boolean isLob) {
            super(converter, isLob);
        }

        @Override
        final void sqlInline0(BindingSQLContext<U> ctx, Integer value) {
            ctx.render().sql(value.toString());
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
            return wasNull(ctx.resultSet(), Integer.valueOf(ctx.resultSet().getInt(ctx.index())));
        }

        @Override
        final Integer get0(BindingGetStatementContext<U> ctx) throws SQLException {
            return wasNull(ctx.statement(), Integer.valueOf(ctx.statement().getInt(ctx.index())));
        }

        @Override
        final Integer get0(BindingGetSQLInputContext<U> ctx) throws SQLException {
            return wasNull(ctx.input(), Integer.valueOf(ctx.input().readInt()));
        }

        @Override
        final int sqltype(Configuration configuration) {
            return Types.INTEGER;
        }
    }

    static final class DefaultLongBinding<U> extends AbstractBinding<Long, U> {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = 7360911614219750448L;

        DefaultLongBinding(Converter<Long, U> converter, boolean isLob) {
            super(converter, isLob);
        }

        @Override
        final void sqlInline0(BindingSQLContext<U> ctx, Long value) {
            ctx.render().sql(value.toString());
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
            return wasNull(ctx.resultSet(), Long.valueOf(ctx.resultSet().getLong(ctx.index())));
        }

        @Override
        final Long get0(BindingGetStatementContext<U> ctx) throws SQLException {
            return wasNull(ctx.statement(), Long.valueOf(ctx.statement().getLong(ctx.index())));
        }

        @Override
        final Long get0(BindingGetSQLInputContext<U> ctx) throws SQLException {
            return wasNull(ctx.input(), Long.valueOf(ctx.input().readLong()));
        }

        @Override
        final int sqltype(Configuration configuration) {
            return Types.BIGINT;
        }
    }



    private static final Pattern LENIENT_OFFSET_PATTERN = Pattern.compile(
        "(?:(\\d{4}-\\d{2}-\\d{2})[T ])?(\\d{2}:\\d{2}(:\\d{2})?(?:\\.\\d+)?)(?: +)?(([+-])(\\d)?(\\d)(:\\d{2})?)?");

    private static final OffsetTime offsetTime(String string) {
        return string == null ? null : OffsetTime.parse(preparse(string, false));
    }

    private static final OffsetDateTime offsetDateTime(String string) {
        return string == null ? null : OffsetDateTime.parse(preparse(string, true));
    }

    private static final String preparse(String formatted, boolean includeDate) {
        Matcher m = LENIENT_OFFSET_PATTERN.matcher(formatted);

        if (m.find()) {
            StringBuilder sb = new StringBuilder();
            String group1 = m.group(1);

            if (includeDate && group1 != null) {
                sb.append(group1);

                // [#4338] SQL supports the alternative ISO 8601 date format, where a
                // whitespace character separates date and time. java.time does not
                sb.append('T');
            }

            sb.append(m.group(2));

            if (m.group(3) == null)
                sb.append(":00");

            if (m.group(4) != null) {
                sb.append(m.group(5));

                String group6 = m.group(6);
                String group8 = m.group(8);

                // [#4965] Oracle might return a single-digit hour offset (and some spare space)
                sb.append(group6 == null ? "0" : group6);
                sb.append(m.group(7));

                // [#4338] [#5180] [#5776] PostgreSQL is more lenient regarding the offset format
                sb.append(group8 == null ? ":00" : group8);
            }
            else {
                sb.append("+00:00");
            }

            return sb.toString();
        }

        // Probably a bug, let OffsetDateTime or OffsetTime report it
        else {
            return formatted;
        }
    }

    static final class DefaultOffsetDateTimeBinding<U> extends AbstractBinding<OffsetDateTime, U> {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = 2775682497765456476L;

        DefaultOffsetDateTimeBinding(Converter<OffsetDateTime, U> converter, boolean isLob) {
            super(converter, isLob);
        }

        @Override
        final void sqlInline0(BindingSQLContext<U> ctx, OffsetDateTime value) {

            // [#5806] H2 doesn't support TIMESTAMP WITH TIME ZONE literals, see
            if (ctx.family() == H2)
                ctx.render().visit(K_CAST).sql("('").sql(escape(format(value), ctx.render())).sql("' ")
                            .visit(K_AS).sql(' ').visit(K_TIMESTAMP_WITH_TIME_ZONE).sql(')');











            // Some dialects implement SQL standard time literals
            else
                ctx.render().visit(K_TIMESTAMP_WITH_TIME_ZONE).sql(" '").sql(escape(format(value), ctx.render())).sql('\'');
        }

        @Override
        final void set0(BindingSetStatementContext<U> ctx, OffsetDateTime value) throws SQLException {
            ctx.statement().setString(ctx.index(), format(value));
        }

        @Override
        final void set0(BindingSetSQLOutputContext<U> ctx, OffsetDateTime value) throws SQLException {
            // [#6630] TODO support this type
            throw new UnsupportedOperationException("Type " + type + " is not supported");
        }

        @Override
        final OffsetDateTime get0(BindingGetResultSetContext<U> ctx) throws SQLException {
            return offsetDateTime(ctx.resultSet().getString(ctx.index()));
        }

        @Override
        final OffsetDateTime get0(BindingGetStatementContext<U> ctx) throws SQLException {
            return offsetDateTime(ctx.statement().getString(ctx.index()));
        }

        @Override
        final OffsetDateTime get0(BindingGetSQLInputContext<U> ctx) throws SQLException {
            // [#6630] TODO support this type
            throw new UnsupportedOperationException("Type " + type + " is not supported");
        }

        @Override
        final int sqltype(Configuration configuration) {

            // [#5779] Few JDBC drivers support the JDBC 4.2 TIME[STAMP]_WITH_TIMEZONE types.
            return Types.VARCHAR;
        }

        private static final String format(OffsetDateTime val) {

            // Remove the ISO standard T character, as some databases don't like that
            String format = formatISO(val);

            // Replace the ISO standard Z character for UTC, as some databases don't like that
            return StringUtils.replace(format.substring(0, 10) + ' ' + format.substring(11), "Z", "+00:00");
        }

        private static final String formatISO(OffsetDateTime val) {
            return val.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        }
    }

    static final class DefaultOffsetTimeBinding<U> extends AbstractBinding<OffsetTime, U> {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = 8991629916239335071L;

        DefaultOffsetTimeBinding(Converter<OffsetTime, U> converter, boolean isLob) {
            super(converter, isLob);
        }

        @Override
        final void sqlInline0(BindingSQLContext<U> ctx, OffsetTime value) {








            // Some dialects implement SQL standard time literals
            ctx.render().visit(K_TIME_WITH_TIME_ZONE).sql(" '").sql(escape(format(value), ctx.render())).sql('\'');
        }

        @Override
        final void set0(BindingSetStatementContext<U> ctx, OffsetTime value) throws SQLException {
            String string = format(value);







            ctx.statement().setString(ctx.index(), string);
        }

        @Override
        final void set0(BindingSetSQLOutputContext<U> ctx, OffsetTime value) throws SQLException {
            // [#6630] TODO support this type
            throw new UnsupportedOperationException("Type " + type + " is not supported");
        }

        @Override
        final OffsetTime get0(BindingGetResultSetContext<U> ctx) throws SQLException {
            return offsetTime(ctx.resultSet().getString(ctx.index()));
        }

        @Override
        final OffsetTime get0(BindingGetStatementContext<U> ctx) throws SQLException {
            return offsetTime(ctx.statement().getString(ctx.index()));
        }

        @Override
        final OffsetTime get0(BindingGetSQLInputContext<U> ctx) throws SQLException {
            // [#6630] TODO support this type
            throw new UnsupportedOperationException("Type " + type + " is not supported");
        }

        @Override
        final int sqltype(Configuration configuration) {

            // [#5779] Few JDBC drivers support the JDBC 4.2 TIME[STAMP]_WITH_TIMEZONE types.
            return Types.VARCHAR;
        }

        private static final String format(OffsetTime val) {

            // Replace the ISO standard Z character for UTC, as some databases don't like that
            return StringUtils.replace(val.format(DateTimeFormatter.ISO_OFFSET_TIME), "Z", "+00:00");
        }
    }



    static final class DefaultOtherBinding<U> extends AbstractBinding<Object, U> {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = -650741489151706822L;

        DefaultOtherBinding(Converter<Object, U> converter, boolean isLob) {
            super(converter, isLob);
        }

        @SuppressWarnings({ "unchecked", "rawtypes" })
        @Override
        final void set0(BindingSetStatementContext<U> ctx, Object value) throws SQLException {
            ((AbstractBinding) binding(value.getClass(), isLob)).set0(ctx, value);
        }

        @Override
        final void setNull0(BindingSetStatementContext<U> ctx) throws SQLException {













            // [#729] In the absence of the correct JDBC type, try setObject
            ctx.statement().setObject(ctx.index(), null);
        }

        @Override
        final void set0(BindingSetSQLOutputContext<U> ctx, Object value) throws SQLException {
            throw new UnsupportedOperationException("Type " + type + " is not supported");
        }

        @Override
        final Object get0(BindingGetResultSetContext<U> ctx) throws SQLException {
            return unlob(ctx.resultSet().getObject(ctx.index()));
        }

        @Override
        final Object get0(BindingGetStatementContext<U> ctx) throws SQLException {
            return unlob(ctx.statement().getObject(ctx.index()));
        }

        @Override
        final Object get0(BindingGetSQLInputContext<U> ctx) throws SQLException {
            return unlob(ctx.input().readObject());
        }

        @Override
        final int sqltype(Configuration configuration) {
            return Types.OTHER;
        }

        /**
         * [#2534] Extract <code>byte[]</code> or <code>String</code> data from a
         * LOB, if the argument is a lob.
         */
        private static final Object unlob(Object object) throws SQLException {
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
    }

    static final class DefaultRecordBinding<U> extends AbstractBinding<Record, U> {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = 2547994476924120818L;

        DefaultRecordBinding(Converter<Record, U> converter, boolean isLob) {
            super(converter, isLob);
        }

        @Override
        void sqlBind0(BindingSQLContext<U> ctx, Record value) throws SQLException {
            super.sqlBind0(ctx, value);

            if (ctx.family() == POSTGRES && value != null)
                pgRenderRecordCast(ctx.render(), value);
        }

        @Override
        final void sqlInline0(BindingSQLContext<U> ctx, Record value) throws SQLException {
            if (ctx.family() == POSTGRES) {
                ctx.render().visit(inline(PostgresUtils.toPGString(value)));
                pgRenderRecordCast(ctx.render(), value);
            }
            else
                ctx.render().sql("[UDT]");
        }

        @SuppressWarnings({ "rawtypes", "unchecked" })
        @Override
        final void register0(BindingRegisterContext<U> ctx) throws SQLException {







            super.register0(ctx);
        }

        @Override
        final void set0(BindingSetStatementContext<U> ctx, Record value) throws SQLException {
            if (ctx.family() == POSTGRES && value != null)
                ctx.statement().setString(ctx.index(), PostgresUtils.toPGString(value));
            else
                ctx.statement().setObject(ctx.index(), value);
        }

        @SuppressWarnings({ "rawtypes", "unchecked" })
        @Override
        final void setNull0(BindingSetStatementContext<U> ctx) throws SQLException {








            super.setNull0(ctx);
        }

        @Override
        final void set0(BindingSetSQLOutputContext<U> ctx, Record value) throws SQLException {
            if (value instanceof UDTRecord)
                ctx.output().writeObject((UDTRecord<?>) value);
            else
                throw new UnsupportedOperationException("Type " + type + " is not supported");
        }

        @Override
        final Record get0(BindingGetResultSetContext<U> ctx) throws SQLException {
            switch (ctx.family()) {
                case POSTGRES:
                    return pgNewRecord(type, null, ctx.resultSet().getObject(ctx.index()));

                default:
                    return (Record) ctx.resultSet().getObject(ctx.index(), typeMap(type, ctx.configuration()));
            }
        }

        @Override
        final Record get0(BindingGetStatementContext<U> ctx) throws SQLException {
            switch (ctx.family()) {
                case POSTGRES:
                    return pgNewRecord(type, null, ctx.statement().getObject(ctx.index()));

                default:
                    return (Record) ctx.statement().getObject(ctx.index(), typeMap(type, ctx.configuration()));
            }
        }

        @Override
        final Record get0(BindingGetSQLInputContext<U> ctx) throws SQLException {
            return (Record) ctx.input().readObject();
        }

        @Override
        final int sqltype(Configuration configuration) {
            return Types.STRUCT;
        }

        // -------------------------------------------------------------------------
        // XXX: The following section has been added for Postgres UDT support. The
        // official Postgres JDBC driver does not implement SQLData and similar
        // interfaces. Instead, a string representation of a UDT has to be parsed
        // -------------------------------------------------------------------------

        static final void pgRenderRecordCast(RenderContext render, Record value) {
            if (value instanceof UDTRecord)
                render.sql("::").visit(((UDTRecord<?>) value).getUDT().getQualifiedName());
            else if (value instanceof TableRecord)
                render.sql("::").visit(((TableRecord<?>) value).getTable().getQualifiedName());
        }

        private static final <T> T pgFromString(Class<T> type, String string) {
            return pgFromString(Converters.identity(type), string);
        }

        @SuppressWarnings("unchecked")
        private static final <T> T pgFromString(Converter<?, T> converter, String string) {
            Class<T> type = converter.toType();

            if (string == null)
                return null;
            else if (type == Blob.class)
                ; // Not supported
            else if (type == Boolean.class)
                return (T) Convert.convert(string, Boolean.class);
            else if (type == BigInteger.class)
                return (T) new BigInteger(string);
            else if (type == BigDecimal.class)
                return (T) new BigDecimal(string);
            else if (type == Byte.class)
                return (T) Byte.valueOf(string);
            else if (type == byte[].class)
                return (T) PostgresUtils.toBytes(string);
            else if (type == Clob.class)
                ; // Not supported
            else if (type == Date.class)
                return (T) Date.valueOf(string);
            else if (type == Double.class)
                return (T) Double.valueOf(string);
            else if (type == Float.class)
                return (T) Float.valueOf(string);
            else if (type == Integer.class)
                return (T) Integer.valueOf(string);
            else if (type == Long.class)
                return (T) Long.valueOf(string);
            else if (type == Short.class)
                return (T) Short.valueOf(string);
            else if (type == String.class)
                return (T) string;
            else if (type == Time.class)
                return (T) Time.valueOf(string);
            else if (type == Timestamp.class)
                return (T) Timestamp.valueOf(string);

            else if (type == LocalTime.class)
                return (T) LocalTime.parse(string);
            else if (type == LocalDate.class)
                return (T) LocalDate.parse(string);
            else if (type == LocalDateTime.class)
                return (T) LocalDateTime.parse(string);
            else if (type == OffsetTime.class)
                return (T) offsetTime(string);
            else if (type == OffsetDateTime.class)
                return (T) offsetDateTime(string);

            else if (type == UByte.class)
                return (T) UByte.valueOf(string);
            else if (type == UShort.class)
                return (T) UShort.valueOf(string);
            else if (type == UInteger.class)
                return (T) UInteger.valueOf(string);
            else if (type == ULong.class)
                return (T) ULong.valueOf(string);
            else if (type == UUID.class)
                return (T) UUID.fromString(string);
            else if (type.isArray())
                return (T) pgNewArray(type, string);




            else if (EnumType.class.isAssignableFrom(type))
                return (T) DefaultEnumTypeBinding.getEnumType((Class<EnumType>) type, string);
            else if (Record.class.isAssignableFrom(type))
                return (T) pgNewRecord(type, null, string);
            else if (type == Object.class)
                return (T) string;

            // [#4964] [#6058] Recurse only if we have a meaningful converter, not the identity converter,
            //                 which would cause a StackOverflowError, here!
            else if (type != converter.fromType()) {
                Converter<Object, T> c = (Converter<Object, T>) converter;
                return c.from(pgFromString(c.fromType(), string));
            }

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
        static final Record pgNewRecord(Class<?> type, Field<?>[] fields, final Object object) {
            if (object == null)
                return null;

            final List<String> values = PostgresUtils.toPGObject(object.toString());

            // [#6404] In the event of an unknown record type, derive the record length from actual values.
            //         Unfortunately, few column types can be derived from this information. Some possibilities
            //         for future jOOQ versions include:
            //         - Integers
            //         - Numbers
            //         - Binary data (starts with \x)
            //         - Temporal data
            //         - Everything else: VARCHAR
            if (fields == null && type == Record.class)
                fields = Tools.fields(values.size(), SQLDataType.VARCHAR);

            return Tools.newRecord(true, (Class<Record>) type, fields)
                        .operate(new RecordOperation<Record, RuntimeException>() {

                    @Override
                    public Record operate(Record record) {
                        Row row = record.fieldsRow();

                        for (int i = 0; i < row.size(); i++)
                            pgSetValue(record, row.field(i), values.get(i));

                        return record;
                    }
                });
        }

        private static final <T> void pgSetValue(Record record, Field<T> field, String value) {
            record.set(field, pgFromString(field.getConverter(), value));
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
        private static final Object[] pgNewArray(Class<?> type, String string) {
            if (string == null) {
                return null;
            }

            try {
                Class<?> component = type.getComponentType();
                List<String> values = PostgresUtils.toPGArray(string);

                if (values.isEmpty()) {
                    return (Object[]) java.lang.reflect.Array.newInstance(component, 0);
                }
                else {
                    Object[] result = (Object[]) java.lang.reflect.Array.newInstance(component, values.size());

                    for (int i = 0; i < values.size(); i++)
                        result[i] = pgFromString(type.getComponentType(), values.get(i));

                    return result;
                }
            }
            catch (Exception e) {
                throw new DataTypeException("Error while creating array", e);
            }
        }
    }

    static final class DefaultResultBinding<U> extends AbstractBinding<Result<?>, U> {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = -2148875780733374224L;

        DefaultResultBinding(Converter<Result<?>, U> converter, boolean isLob) {
            super(converter, isLob);
        }

        @Override
        final void set0(BindingSetStatementContext<U> ctx, Result<?> value) throws SQLException {
            throw new UnsupportedOperationException("Cannot bind a value of type Result to a PreparedStatement");
        }

        @Override
        final void set0(BindingSetSQLOutputContext<U> ctx, Result<?> value) throws SQLException {
            throw new UnsupportedOperationException("Cannot bind a value of type Result to a SQLOutput");
        }

        @Override
        final Result<?> get0(BindingGetResultSetContext<U> ctx) throws SQLException {
            ResultSet nested = (ResultSet) ctx.resultSet().getObject(ctx.index());
            return DSL.using(ctx.configuration()).fetch(nested);
        }

        @Override
        final Result<?> get0(BindingGetStatementContext<U> ctx) throws SQLException {
            ResultSet nested = (ResultSet) ctx.statement().getObject(ctx.index());
            return DSL.using(ctx.configuration()).fetch(nested);
        }

        @Override
        final Result<?> get0(BindingGetSQLInputContext<U> ctx) throws SQLException {
            throw new UnsupportedOperationException("Cannot get a value of type Result from a SQLInput");
        }

        @Override
        final int sqltype(Configuration configuration) {
            switch (configuration.family()) {



                case H2:
                    return -10; // OracleTypes.CURSOR;

                case POSTGRES:
                default:
                    return Types.OTHER;
            }
        }
    }

    static final class DefaultShortBinding<U> extends AbstractBinding<Short, U> {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = 8935720621737085226L;

        DefaultShortBinding(Converter<Short, U> converter, boolean isLob) {
            super(converter, isLob);
        }

        @Override
        final void sqlInline0(BindingSQLContext<U> ctx, Short value) {
            ctx.render().sql(value.toString());
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
            return wasNull(ctx.resultSet(), Short.valueOf(ctx.resultSet().getShort(ctx.index())));
        }

        @Override
        final Short get0(BindingGetStatementContext<U> ctx) throws SQLException {
            return wasNull(ctx.statement(), Short.valueOf(ctx.statement().getShort(ctx.index())));
        }

        @Override
        final Short get0(BindingGetSQLInputContext<U> ctx) throws SQLException {
            return wasNull(ctx.input(), Short.valueOf(ctx.input().readShort()));
        }

        @Override
        final int sqltype(Configuration configuration) {
            return Types.SMALLINT;
        }
    }

    static final class DefaultStringBinding<U> extends AbstractBinding<String, U> {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = 4232459541239942932L;

        DefaultStringBinding(Converter<String, U> converter, boolean isLob) {
            super(converter, isLob);
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
            return ctx.resultSet().getString(ctx.index());
        }

        @Override
        final String get0(BindingGetStatementContext<U> ctx) throws SQLException {
            return ctx.statement().getString(ctx.index());
        }

        @Override
        final String get0(BindingGetSQLInputContext<U> ctx) throws SQLException {
            return ctx.input().readString();
        }

        @Override
        final int sqltype(Configuration configuration) {
            return Types.VARCHAR;
        }
    }

    static final class DefaultTimeBinding<U> extends AbstractBinding<Time, U> {

        /**
         * Generated UID
         */
        private static final long                serialVersionUID         = -2563220967846617288L;
        private static final EnumSet<SQLDialect> INLINE_AS_STRING_LITERAL = EnumSet.of(SQLITE);

        DefaultTimeBinding(Converter<Time, U> converter, boolean isLob) {
            super(converter, isLob);
        }

        @Override
        final void sqlInline0(BindingSQLContext<U> ctx, Time value) {

            // The SQLite JDBC driver does not implement the escape syntax
            // [#1253] Sybase does not implement time literals
            if (INLINE_AS_STRING_LITERAL.contains(ctx.family()))
                ctx.render().sql('\'').sql(new SimpleDateFormat("HH:mm:ss").format(value)).sql('\'');








            // [#1253] Derby doesn't support the standard literal
            else if (ctx.family() == DERBY)
                ctx.render().visit(K_TIME).sql("('").sql(escape(value, ctx.render())).sql("')");

            // [#3648] Circumvent a MySQL bug related to date literals
            else if (ctx.family() == MYSQL)
                ctx.render().sql("{t '").sql(escape(value, ctx.render())).sql("'}");












            // Most dialects implement SQL standard time literals
            else
                ctx.render().visit(K_TIME).sql(" '").sql(escape(value, ctx.render())).sql('\'');
        }

        @Override
        final void set0(BindingSetStatementContext<U> ctx, Time value) throws SQLException {
            if (ctx.family() == SQLITE)
                ctx.statement().setString(ctx.index(), value.toString());
            else
                ctx.statement().setTime(ctx.index(), value);
        }

        @Override
        final void set0(BindingSetSQLOutputContext<U> ctx, Time value) throws SQLException {
            ctx.output().writeTime(value);
        }

        @Override
        final Time get0(BindingGetResultSetContext<U> ctx) throws SQLException {

            // SQLite's type affinity needs special care...
            if (ctx.family() == SQLDialect.SQLITE) {
                String time = ctx.resultSet().getString(ctx.index());
                return time == null ? null : new Time(parse(Time.class, time));
            }

            else {
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
        final int sqltype(Configuration configuration) {
            return Types.TIME;
        }
    }

    static final class DefaultTimestampBinding<U> extends AbstractBinding<Timestamp, U> {

        /**
         * Generated UID
         */
        private static final long                serialVersionUID         = 289387167549159015L;
        private static final EnumSet<SQLDialect> INLINE_AS_STRING_LITERAL = EnumSet.of(SQLITE);

        DefaultTimestampBinding(Converter<Timestamp, U> converter, boolean isLob) {
            super(converter, isLob);
        }

        @Override
        final void sqlInline0(BindingSQLContext<U> ctx, Timestamp value) {

            // The SQLite JDBC driver does not implement the escape syntax
            // [#1253] Sybase does not implement timestamp literals
            if (INLINE_AS_STRING_LITERAL.contains(ctx.family()))
                ctx.render().sql('\'').sql(escape(value, ctx.render())).sql('\'');













            // [#1253] Derby doesn't support the standard literal
            else if (ctx.family() == DERBY)
                ctx.render().visit(K_TIMESTAMP).sql("('").sql(escape(value, ctx.render())).sql("')");

            // CUBRID timestamps have no fractional seconds
            else if (ctx.family() == CUBRID)
                ctx.render().visit(K_DATETIME).sql(" '").sql(escape(value, ctx.render())).sql('\'');

            // [#3648] Circumvent a MySQL bug related to date literals
            else if (ctx.family() == MYSQL)
                ctx.render().sql("{ts '").sql(escape(value, ctx.render())).sql("'}");

            // Most dialects implement SQL standard timestamp literals
            else
                ctx.render().visit(K_TIMESTAMP).sql(" '").sql(escape(value, ctx.render())).sql('\'');
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
        final int sqltype(Configuration configuration) {
            return Types.TIMESTAMP;
        }
    }

    static final class DefaultUByteBinding<U> extends AbstractBinding<UByte, U> {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = -101167998250685198L;

        DefaultUByteBinding(Converter<UByte, U> converter, boolean isLob) {
            super(converter, isLob);
        }

        @Override
        final void sqlInline0(BindingSQLContext<U> ctx, UByte value) {
            ctx.render().sql(value.toString());
        }

        @Override
        final void set0(BindingSetStatementContext<U> ctx, UByte value) throws SQLException {
            ctx.statement().setShort(ctx.index(), value.shortValue());
        }

        @Override
        final void set0(BindingSetSQLOutputContext<U> ctx, UByte value) throws SQLException {
            ctx.output().writeShort(value.shortValue());
        }

        @Override
        final UByte get0(BindingGetResultSetContext<U> ctx) throws SQLException {
            return Convert.convert(ctx.resultSet().getString(ctx.index()), UByte.class);
        }

        @Override
        final UByte get0(BindingGetStatementContext<U> ctx) throws SQLException {
            String string = ctx.statement().getString(ctx.index());
            return string == null ? null : UByte.valueOf(string);
        }

        @Override
        final UByte get0(BindingGetSQLInputContext<U> ctx) throws SQLException {
            String string = ctx.input().readString();
            return string == null ? null : UByte.valueOf(string);
        }

        @Override
        final int sqltype(Configuration configuration) {
            return Types.SMALLINT;
        }
    }

    static final class DefaultUIntegerBinding<U> extends AbstractBinding<UInteger, U> {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = 1437279656720185207L;

        DefaultUIntegerBinding(Converter<UInteger, U> converter, boolean isLob) {
            super(converter, isLob);
        }

        @Override
        final void sqlInline0(BindingSQLContext<U> ctx, UInteger value) {
            ctx.render().sql(value.toString());
        }

        @Override
        final void set0(BindingSetStatementContext<U> ctx, UInteger value) throws SQLException {





            ctx.statement().setLong(ctx.index(), value.longValue());
        }

        @Override
        final void set0(BindingSetSQLOutputContext<U> ctx, UInteger value) throws SQLException {
            ctx.output().writeLong(value.longValue());
        }

        @Override
        final UInteger get0(BindingGetResultSetContext<U> ctx) throws SQLException {
            return Convert.convert(ctx.resultSet().getString(ctx.index()), UInteger.class);
        }

        @Override
        final UInteger get0(BindingGetStatementContext<U> ctx) throws SQLException {
            String string = ctx.statement().getString(ctx.index());
            return string == null ? null : UInteger.valueOf(string);
        }

        @Override
        final UInteger get0(BindingGetSQLInputContext<U> ctx) throws SQLException {
            String string = ctx.input().readString();
            return string == null ? null : UInteger.valueOf(string);
        }

        @Override
        final int sqltype(Configuration configuration) {
            return Types.BIGINT;
        }
    }

    static final class DefaultULongBinding<U> extends AbstractBinding<ULong, U> {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = 4891128447530113299L;

        DefaultULongBinding(Converter<ULong, U> converter, boolean isLob) {
            super(converter, isLob);
        }

        @Override
        final void sqlInline0(BindingSQLContext<U> ctx, ULong value) {
            ctx.render().sql(value.toString());
        }

        @Override
        final void set0(BindingSetStatementContext<U> ctx, ULong value) throws SQLException {





            ctx.statement().setBigDecimal(ctx.index(), new BigDecimal(value.toString()));
        }

        @Override
        final void set0(BindingSetSQLOutputContext<U> ctx, ULong value) throws SQLException {
            ctx.output().writeBigDecimal(new BigDecimal(value.toString()));
        }

        @Override
        final ULong get0(BindingGetResultSetContext<U> ctx) throws SQLException {
            return Convert.convert(ctx.resultSet().getString(ctx.index()), ULong.class);
        }

        @Override
        final ULong get0(BindingGetStatementContext<U> ctx) throws SQLException {
            String string = ctx.statement().getString(ctx.index());
            return string == null ? null : ULong.valueOf(string);
        }

        @Override
        final ULong get0(BindingGetSQLInputContext<U> ctx) throws SQLException {
            String string = ctx.input().readString();
            return string == null ? null : ULong.valueOf(string);
        }

        @Override
        final int sqltype(Configuration configuration) {
            return Types.DECIMAL;
        }
    }

    static final class DefaultUShortBinding<U> extends AbstractBinding<UShort, U> {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = 2539811197808516971L;

        DefaultUShortBinding(Converter<UShort, U> converter, boolean isLob) {
            super(converter, isLob);
        }

        @Override
        final void sqlInline0(BindingSQLContext<U> ctx, UShort value) {
            ctx.render().sql(value.toString());
        }

        @Override
        final void set0(BindingSetStatementContext<U> ctx, UShort value) throws SQLException {
            ctx.statement().setInt(ctx.index(), value.intValue());
        }

        @Override
        final void set0(BindingSetSQLOutputContext<U> ctx, UShort value) throws SQLException {
            ctx.output().writeInt(value.intValue());
        }

        @Override
        final UShort get0(BindingGetResultSetContext<U> ctx) throws SQLException {
            return Convert.convert(ctx.resultSet().getString(ctx.index()), UShort.class);
        }

        @Override
        final UShort get0(BindingGetStatementContext<U> ctx) throws SQLException {
            String string = ctx.statement().getString(ctx.index());
            return string == null ? null : UShort.valueOf(string);
        }

        @Override
        final UShort get0(BindingGetSQLInputContext<U> ctx) throws SQLException {
            String string = ctx.input().readString();
            return string == null ? null : UShort.valueOf(string);
        }

        @Override
        final int sqltype(Configuration configuration) {
            return Types.INTEGER;
        }
    }

    static final class DefaultUUIDBinding<U> extends AbstractBinding<UUID, U> {

        /**
         * Generated UUID
         */
        private static final long serialVersionUID = -6616291625634347383L;

        DefaultUUIDBinding(Converter<UUID, U> converter, boolean isLob) {
            super(converter, isLob);
        }

        @Override
        final void set0(BindingSetStatementContext<U> ctx, UUID value) throws SQLException {
            switch (ctx.family()) {

                // [#1624] Some JDBC drivers natively support the
                // java.util.UUID data type
                case H2:
                case POSTGRES: {
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

                // [#1624] Some JDBC drivers natively support the
                // java.util.UUID data type
                case H2:
                case POSTGRES:
                    return (UUID) ctx.resultSet().getObject(ctx.index());








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
        final int sqltype(Configuration configuration) {
            return Types.VARCHAR;
        }
    }

    static final class DefaultYearToMonthBinding<U> extends AbstractBinding<YearToMonth, U> {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = 6417965474063152673L;

        DefaultYearToMonthBinding(Converter<YearToMonth, U> converter, boolean isLob) {
            super(converter, isLob);
        }

        @Override
        final void set0(BindingSetStatementContext<U> ctx, YearToMonth value) throws SQLException {

            // [#566] Interval data types are best bound as Strings
            if (ctx.family() == POSTGRES)
                ctx.statement().setObject(ctx.index(), toPGInterval(value));
            else
                ctx.statement().setString(ctx.index(), value.toString());
        }

        @Override
        final void set0(BindingSetSQLOutputContext<U> ctx, YearToMonth value) throws SQLException {
            ctx.output().writeString(value.toString());
        }

        @Override
        final YearToMonth get0(BindingGetResultSetContext<U> ctx) throws SQLException {
            if (ctx.family() == POSTGRES) {
                Object object = ctx.resultSet().getObject(ctx.index());
                return object == null ? null : PostgresUtils.toYearToMonth(object);
            }
            else {
                String string = ctx.resultSet().getString(ctx.index());
                return string == null ? null : YearToMonth.valueOf(string);
            }
        }

        @Override
        final YearToMonth get0(BindingGetStatementContext<U> ctx) throws SQLException {
            if (ctx.family() == POSTGRES) {
                Object object = ctx.statement().getObject(ctx.index());
                return object == null ? null : PostgresUtils.toYearToMonth(object);
            }
            else {
                String string = ctx.statement().getString(ctx.index());
                return string == null ? null : YearToMonth.valueOf(string);
            }
        }

        @Override
        final YearToMonth get0(BindingGetSQLInputContext<U> ctx) throws SQLException {
            String string = ctx.input().readString();
            return string == null ? null : YearToMonth.valueOf(string);
        }

        @Override
        final int sqltype(Configuration configuration) {
            return Types.VARCHAR;
        }
    }
}

