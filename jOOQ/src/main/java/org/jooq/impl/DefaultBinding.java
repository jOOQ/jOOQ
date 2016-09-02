/**
 * Copyright (c) 2009-2016, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
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

import static java.lang.Boolean.TRUE;
import static java.util.Arrays.asList;
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
// ...
import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.impl.DSL.cast;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.using;
import static org.jooq.impl.DefaultExecuteContext.localTargetConnection;
import static org.jooq.impl.Tools.attachRecords;
import static org.jooq.impl.Tools.getMappedUDTName;
import static org.jooq.impl.Tools.needsBackslashEscaping;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import org.jooq.UDTRecord;
import org.jooq.exception.DataTypeException;
import org.jooq.exception.MappingException;
import org.jooq.exception.SQLDialectNotSupportedException;
import org.jooq.tools.Convert;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.jdbc.JDBCUtils;
import org.jooq.tools.jdbc.MockArray;
import org.jooq.types.DayToSecond;
import org.jooq.types.Interval;
import org.jooq.types.UByte;
import org.jooq.types.UInteger;
import org.jooq.types.ULong;
import org.jooq.types.UNumber;
import org.jooq.types.UShort;
import org.jooq.types.YearToMonth;
import org.jooq.util.postgres.PostgresUtils;

/**
 * @author Lukas Eder
 */
public class DefaultBinding<T, U> implements Binding<T, U> {

    static final JooqLogger     log              = JooqLogger.getLogger(DefaultBinding.class);
    private static final char[] HEX              = "0123456789abcdef".toCharArray();

    /**
     * Generated UID
     */
    private static final long   serialVersionUID = -198499389344950496L;

    final Class<T>              type;
    final Converter<T, U>       converter;

    @Deprecated
    // TODO: This type boolean should not be passed standalone to the
    // constructor. Find a better design
    final boolean               isLob;

    public DefaultBinding(Converter<T, U> converter) {
        this(converter, false);
    }

    DefaultBinding(Converter<T, U> converter, boolean isLob) {
        this.type = converter.fromType();
        this.converter = converter;
        this.isLob = isLob;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    static <T, X, U> Binding<T, U> newBinding(final Converter<X, U> converter, final DataType<T> type, final Binding<T, X> binding) {
        final Binding<T, U> theBinding;


        if (converter == null && binding == null) {
            theBinding = (Binding) type.getBinding();
        }
        else if (converter == null) {
            theBinding = (Binding) binding;
        }
        else if (binding == null) {
            theBinding = (Binding) new DefaultBinding<X, U>(converter, type.isLob());
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

    @Override
    public Converter<T, U> converter() {
        return converter;
    }

    @Override
    public void sql(BindingSQLContext<U> ctx) {
        T converted = converter.to(ctx.value());

        // Casting can be enforced or prevented
        switch (ctx.render().castMode()) {
            case NEVER:
                toSQL(ctx, converted);
                return;

            case ALWAYS:
                toSQLCast(ctx, converted);
                return;
        }

        // See if we "should" cast, to stay on the safe side
        if (shouldCast(ctx, converted)) {
            toSQLCast(ctx, converted);
        }

        // Most RDBMS can infer types for bind values
        else {
            toSQL(ctx, converted);
        }
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


        if (type == OffsetTime.class || type == OffsetDateTime.class) {
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
    private final void toSQLCast(BindingSQLContext<U> ctx, T converted) {
        DataType<T> dataType = DefaultDataType.getDataType(ctx.dialect(), type);
        DataType<T> sqlDataType = dataType.getSQLDataType();
        SQLDialect family = ctx.family();

        // [#822] Some RDBMS need precision / scale information on BigDecimals
        if (converted != null && type == BigDecimal.class && asList(CUBRID, DERBY, FIREBIRD, HSQLDB).contains(family)) {

            // Add precision / scale on BigDecimals
            int scale = ((BigDecimal) converted).scale();
            int precision = ((BigDecimal) converted).precision();

            // [#5323] BigDecimal precision is always 1 for BigDecimals smaller than 1.0
            if (scale >= precision)
                precision = scale + 1;

            toSQLCast(ctx, converted, dataType, 0, precision, scale);
        }

        // [#1028] Most databases don't know an OTHER type (except H2, HSQLDB).
        else if (SQLDataType.OTHER == sqlDataType) {

            // If the bind value is set, it can be used to derive the cast type
            if (converted != null) {
                toSQLCast(ctx, converted, DefaultDataType.getDataType(family, converted.getClass()), 0, 0, 0);
            }

            // [#632] [#722] Current integration tests show that Ingres and
            // Sybase can do without casting in most cases.
            else if (asList().contains(family)) {
                ctx.render().sql(ctx.variable());
            }

            // Derby and DB2 must have a type associated with NULL. Use VARCHAR
            // as a workaround. That's probably not correct in all cases, though
            else {
                toSQLCast(ctx, converted, DefaultDataType.getDataType(family, String.class), 0, 0, 0);
            }
        }

        // [#1029] Postgres generally doesn't need the casting. Only in the
        // above case where the type is OTHER
        // [#1125] Also with temporal data types, casting is needed some times
        // [#4338] ... specifically when using JSR-310 types
        // [#1130] TODO type can be null for ARRAY types, etc.
        else if (asList(POSTGRES).contains(family) && (sqlDataType == null || !sqlDataType.isTemporal())) {
            toSQL(ctx, converted);
        }

        // [#1727] VARCHAR types should be cast to their actual lengths in some
        // dialects
        else if ((sqlDataType == SQLDataType.VARCHAR || sqlDataType == SQLDataType.CHAR) && asList(FIREBIRD).contains(family)) {
            toSQLCast(ctx, converted, dataType, getValueLength(converted), 0, 0);
        }








        // In all other cases, the bind variable can be cast normally
        else {
            toSQLCast(ctx, converted, dataType, dataType.length(), dataType.precision(), dataType.scale());
        }
    }

    private final int getValueLength(T value) {
        String string = (String) value;
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

    private final void toSQLCast(BindingSQLContext<U> ctx, T converted, DataType<?> dataType, int length, int precision, int scale) {
        ctx.render().keyword("cast").sql('(');
        toSQL(ctx, converted);
        ctx.render().sql(' ').keyword("as").sql(' ')
               .sql(dataType.length(length).precision(precision, scale).getCastTypeName(ctx.configuration()))
               .sql(')');
    }

    /**
     * Inlining abstraction
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private final void toSQL(BindingSQLContext<U> ctx, Object val) {
        SQLDialect family = ctx.family();
        RenderContext render = ctx.render();

        if (render.paramType() == INLINED) {
            // [#2223] Some type-casts in this section may seem unnecessary, e.g.
            // ((Boolean) val).toString(). They have been put in place to avoid
            // accidental type confusions where type != val.getClass(), and thus
            // SQL injection may occur

            if (val == null) {
                render.keyword("null");
            }
            else if (type == Boolean.class) {

                // [#1153] Some dialects don't support boolean literals TRUE and FALSE
                if (asList(FIREBIRD, SQLITE).contains(family)) {
                    render.sql(((Boolean) val) ? "1" : "0");
                }





                else {
                    render.keyword(((Boolean) val).toString());
                }
            }

            // [#1154] Binary data cannot always be inlined
            else if (type == byte[].class) {
                byte[] binary = (byte[]) val;

                if (asList().contains(family)) {
                    render.sql("0x")
                          .sql(convertBytesToHex(binary));
                }








                else if (asList(DERBY, H2, HSQLDB, MARIADB, MYSQL, SQLITE).contains(family)) {
                    render.sql("X'")
                          .sql(convertBytesToHex(binary))
                          .sql('\'');
                }
                else if (asList().contains(family)) {
                    render.keyword("hextoraw('")
                          .sql(convertBytesToHex(binary))
                          .sql("')");
                }
                else if (family == POSTGRES) {
                    render.sql("E'")
                          .sql(PostgresUtils.toPGString(binary))
                          .keyword("'::bytea");
                }

                // This default behaviour is used in debug logging for dialects
                // that do not support inlining binary data
                else {
                    render.sql("X'")
                          .sql(convertBytesToHex(binary))
                          .sql('\'');
                }
            }

            // Interval extends Number, so let Interval come first!
            else if (Interval.class.isAssignableFrom(type)) {
                render.sql('\'')
                      .sql(escape(val, render))
                      .sql('\'');
            }

            // [#5249] Special inlining of special floating point values
            else if (Double.class.isAssignableFrom(type) && ((Double) val).isNaN()) {
                if (POSTGRES == family)
                    render.visit(inline("NaN")).sql("::").keyword("float8");
                else
                    render.sql(((Number) val).toString());
            }

            // [#5249] Special inlining of special floating point values
            else if (Float.class.isAssignableFrom(type) && ((Float) val).isNaN()) {
                if (POSTGRES == family)
                    render.visit(inline("NaN")).sql("::").keyword("float4");
                else
                    render.sql(((Number) val).toString());
            }

            else if (Number.class.isAssignableFrom(type)) {
                render.sql(((Number) val).toString());
            }

            // [#1156] Date/Time data types should be inlined using JDBC
            // escape syntax
            else if (type == Date.class) {

                // The SQLite JDBC driver does not implement the escape syntax
                // [#1253] SQL Server and Sybase do not implement date literals
                if (asList(SQLITE).contains(family)) {
                    render.sql('\'').sql(escape(val, render)).sql('\'');
                }












                // [#1253] Derby doesn't support the standard literal
                else if (family == DERBY) {
                    render.keyword("date('").sql(escape(val, render)).sql("')");
                }

                // [#3648] Circumvent a MySQL bug related to date literals
                else if (family == MYSQL) {
                    render.keyword("{d '").sql(escape(val, render)).sql("'}");
                }

                // Most dialects implement SQL standard date literals
                else {
                    render.keyword("date '").sql(escape(val, render)).sql('\'');
                }
            }
            else if (type == Timestamp.class) {

                // The SQLite JDBC driver does not implement the escape syntax
                // [#1253] SQL Server and Sybase do not implement timestamp literals
                if (asList(SQLITE).contains(family)) {
                    render.sql('\'').sql(escape(val, render)).sql('\'');
                }











                // [#1253] Derby doesn't support the standard literal
                else if (family == DERBY) {
                    render.keyword("timestamp('").sql(escape(val, render)).sql("')");
                }

                // CUBRID timestamps have no fractional seconds
                else if (family == CUBRID) {
                    render.keyword("datetime '").sql(escape(val, render)).sql('\'');
                }

                // [#3648] Circumvent a MySQL bug related to date literals
                else if (family == MYSQL) {
                    render.keyword("{ts '").sql(escape(val, render)).sql("'}");
                }

                // Most dialects implement SQL standard timestamp literals
                else {
                    render.keyword("timestamp '").sql(escape(val, render)).sql('\'');
                }
            }
            else if (type == Time.class) {

                // The SQLite JDBC driver does not implement the escape syntax
                // [#1253] SQL Server and Sybase do not implement time literals
                if (asList(SQLITE).contains(family)) {
                    render.sql('\'').sql(new SimpleDateFormat("HH:mm:ss").format((Time) val)).sql('\'');
                }











                // [#1253] Derby doesn't support the standard literal
                else if (family == DERBY) {
                    render.keyword("time").sql("('").sql(escape(val, render)).sql("')");
                }

                // [#3648] Circumvent a MySQL bug related to date literals
                else if (family == MYSQL) {
                    render.keyword("{t '").sql(escape(val, render)).sql("'}");
                }







                // Most dialects implement SQL standard time literals
                else {
                    render.keyword("time").sql(" '").sql(escape(val, render)).sql('\'');
                }
            }
            else if (type.isArray()) {
                String separator = "";

                // H2 renders arrays as rows
                if (family == H2) {
                    render.sql('(');

                    for (Object o : ((Object[]) val)) {
                        render.sql(separator);
                        new DefaultBinding<Object, Object>(Converters.identity((Class) type.getComponentType()), isLob).sql(new DefaultBindingSQLContext<Object>(ctx.configuration(), ctx.data(), ctx.render(), o));
                        separator = ", ";
                    }

                    render.sql(')');
                }

                else if (family == POSTGRES) {
                    render.visit(cast(inline(PostgresUtils.toPGArrayString((Object[]) val)), type));
                }

                // By default, render HSQLDB / POSTGRES syntax
                else {
                    render.keyword("ARRAY");
                    render.sql('[');

                    for (Object o : ((Object[]) val)) {
                        render.sql(separator);
                        new DefaultBinding<Object, Object>(Converters.identity((Class) type.getComponentType()), isLob).sql(new DefaultBindingSQLContext<Object>(ctx.configuration(), ctx.data(), ctx.render(), o));
                        separator = ", ";
                    }

                    render.sql(']');

                    // [#3214] Some PostgreSQL array type literals need explicit casting
                    if (family == POSTGRES && EnumType.class.isAssignableFrom(type.getComponentType())) {
                        pgRenderEnumCast(render, type);
                    }
                }
            }





            else if (EnumType.class.isAssignableFrom(type)) {
                String literal = ((EnumType) val).getLiteral();

                if (literal == null) {
                    new DefaultBinding<Object, Object>(Converters.identity((Class) String.class), isLob).sql(new DefaultBindingSQLContext<Object>(ctx.configuration(), ctx.data(), ctx.render(), literal));
                }
                else {
                    new DefaultBinding<Object, Object>(Converters.identity((Class) String.class), isLob).sql(new DefaultBindingSQLContext<Object>(ctx.configuration(), ctx.data(), ctx.render(), literal));
                }
            }
            else if (UDTRecord.class.isAssignableFrom(type)) {
                render.sql("[UDT]");
            }

            // Known fall-through types:
            // - Blob, Clob (both not supported by jOOQ)
            // - String
            // - UUID
            else {
                render.sql('\'')
                      .sql(escape(val, render), true)
                      .sql('\'');
            }
        }

        // In Postgres, some additional casting must be done in some cases...
        else if (family == SQLDialect.POSTGRES) {

            // Postgres needs explicit casting for enum (array) types
            if (EnumType.class.isAssignableFrom(type) ||
                (type.isArray() && EnumType.class.isAssignableFrom(type.getComponentType()))) {
                render.sql(ctx.variable());
                pgRenderEnumCast(render, type);
            }

            // ... and also for other array types
            else if (type.isArray() && byte[].class != type) {
                render.sql(ctx.variable());
                render.sql("::");
                render.keyword(DefaultDataType.getDataType(family, type).getCastTypeName(render.configuration()));
            }

            else {
                render.sql(ctx.variable());
            }
        }














        else {
            render.sql(ctx.variable());
        }
    }

    /**
     * Escape a string literal by replacing <code>'</code> by <code>''</code>, and possibly also backslashes.
     */
    private final String escape(Object val, Context<?> context) {
        String result = val.toString();

        if (needsBackslashEscaping(context.configuration()))
            result = result.replace("\\", "\\\\");

        return result.replace("'", "''");
    }

    /**
     * Convert a byte array to a hex encoded string.
     *
     * @param value the byte array
     * @return the hex encoded string
     */
    private static final String convertBytesToHex(byte[] value) {
        return convertBytesToHex(value, value.length);
    }

    /**
     * Convert a byte array to a hex encoded string.
     *
     * @param value the byte array
     * @param len the number of bytes to encode
     * @return the hex encoded string
     */
    private static final String convertBytesToHex(byte[] value, int len) {
        char[] buff = new char[len + len];
        char[] hex = HEX;
        for (int i = 0; i < len; i++) {
            int c = value[i] & 0xff;
            buff[i + i] = hex[c >> 4];
            buff[i + i + 1] = hex[c & 0xf];
        }
        return new String(buff);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void register(BindingRegisterContext<U> ctx) throws SQLException {
        Configuration configuration = ctx.configuration();
        int sqlType = DefaultDataType.getDataType(ctx.dialect(), type).getSQLType();

        switch (configuration.family()) {


































            default: {
                ctx.statement().registerOutParameter(ctx.index(), sqlType);
                break;
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void set(BindingSetStatementContext<U> ctx) throws SQLException {
        Configuration configuration = ctx.configuration();
        SQLDialect dialect = ctx.dialect();
        T value = converter.to(ctx.value());

        if (log.isTraceEnabled())
            if (value != null && value.getClass().isArray() && value.getClass() != byte[].class)
                log.trace("Binding variable " + ctx.index(), Arrays.asList((Object[]) value) + " (" + type + ")");
            else
                log.trace("Binding variable " + ctx.index(), value + " (" + type + ")");

        // Setting null onto a prepared statement is subtly different for every
        // SQL dialect. See the following section for details
        if (value == null) {
            int sqlType = DefaultDataType.getDataType(dialect, type).getSQLType();









            // [#1126] Oracle's UDTs need to be bound with their type name
            if (UDTRecord.class.isAssignableFrom(type)) {
                ctx.statement().setNull(ctx.index(), sqlType, Tools.getMappedUDTName(configuration, (Class<UDTRecord<?>>) type));
            }

            // [#1225] [#1227] TODO Put this logic into DataType
            // Some dialects have trouble binding binary data as BLOB
            else if (asList(POSTGRES).contains(configuration.family()) && sqlType == Types.BLOB) {
                ctx.statement().setNull(ctx.index(), Types.BINARY);
            }




























            // All other types can be set to null if the JDBC type is known
            else if (sqlType != Types.OTHER) {
                ctx.statement().setNull(ctx.index(), sqlType);
            }














            // [#729] In the absence of the correct JDBC type, try setObject
            else {
                ctx.statement().setObject(ctx.index(), null);
            }
        }
        else {
            Class<?> actualType = type;

            // Try to infer the bind value type from the actual bind value if possible.
            if (actualType == Object.class) {
                actualType = value.getClass();
            }

            if (actualType == Blob.class) {
                ctx.statement().setBlob(ctx.index(), (Blob) value);
            }
            else if (actualType == Boolean.class) {








                    ctx.statement().setBoolean(ctx.index(), (Boolean) value);
            }
            else if (actualType == BigDecimal.class) {
                if (asList(SQLITE).contains(dialect.family())) {
                    ctx.statement().setString(ctx.index(), value.toString());
                }
                else {
                    ctx.statement().setBigDecimal(ctx.index(), (BigDecimal) value);
                }
            }
            else if (actualType == BigInteger.class) {
                if (asList(SQLITE).contains(dialect.family())) {
                    ctx.statement().setString(ctx.index(), value.toString());
                }
                else {
                    ctx.statement().setBigDecimal(ctx.index(), new BigDecimal((BigInteger) value));
                }
            }
            else if (actualType == Byte.class) {
                ctx.statement().setByte(ctx.index(), (Byte) value);
            }
            else if (actualType == byte[].class) {
                ctx.statement().setBytes(ctx.index(), (byte[]) value);
            }
            else if (actualType == Clob.class) {
                ctx.statement().setClob(ctx.index(), (Clob) value);
            }
            else if (actualType == Double.class) {
                ctx.statement().setDouble(ctx.index(), (Double) value);
            }
            else if (actualType == Float.class) {
                ctx.statement().setFloat(ctx.index(), (Float) value);
            }
            else if (actualType == Integer.class) {
                ctx.statement().setInt(ctx.index(), (Integer) value);
            }
            else if (actualType == Long.class) {





                ctx.statement().setLong(ctx.index(), (Long) value);
            }
            else if (actualType == Short.class) {
                ctx.statement().setShort(ctx.index(), (Short) value);
            }
            else if (actualType == String.class) {
                ctx.statement().setString(ctx.index(), (String) value);
            }

            // There is potential for trouble when binding date time as such
            // -------------------------------------------------------------
            else if (actualType == Date.class) {
                Date date = (Date) value;

                if (dialect == SQLITE) {
                    ctx.statement().setString(ctx.index(), date.toString());
                }








                else {
                    ctx.statement().setDate(ctx.index(), date);
                }
            }
            else if (actualType == Time.class) {
                Time time = (Time) value;

                if (dialect == SQLITE) {
                    ctx.statement().setString(ctx.index(), time.toString());
                }
                else {
                    ctx.statement().setTime(ctx.index(), time);
                }
            }
            else if (actualType == Timestamp.class) {
                Timestamp timestamp = (Timestamp) value;

                if (dialect == SQLITE) {
                    ctx.statement().setString(ctx.index(), timestamp.toString());
                }
                else {
                    ctx.statement().setTimestamp(ctx.index(), timestamp);
                }
            }


            else if (actualType == LocalDate.class) {
                ctx.statement().setDate(ctx.index(), Date.valueOf((LocalDate) value));
            }
            else if (actualType == LocalTime.class) {
                ctx.statement().setTime(ctx.index(), Time.valueOf((LocalTime) value));
            }
            else if (actualType == LocalDateTime.class) {
                ctx.statement().setTimestamp(ctx.index(), Timestamp.valueOf((LocalDateTime) value));
            }
            else if (actualType == OffsetTime.class) {
                ctx.statement().setString(ctx.index(), value.toString());
            }
            else if (actualType == OffsetDateTime.class) {
                ctx.statement().setString(ctx.index(), value.toString());
            }


            // [#566] Interval data types are best bound as Strings
            else if (actualType == YearToMonth.class) {
                if (dialect.family() == POSTGRES) {
                    ctx.statement().setObject(ctx.index(), toPGInterval((YearToMonth) value));
                }
                else {
                    ctx.statement().setString(ctx.index(), value.toString());
                }
            }
            else if (actualType == DayToSecond.class) {
                if (dialect.family() == POSTGRES) {
                    ctx.statement().setObject(ctx.index(), toPGInterval((DayToSecond) value));
                }
                else {
                    ctx.statement().setString(ctx.index(), value.toString());
                }
            }
            else if (actualType == UByte.class) {
                ctx.statement().setShort(ctx.index(), ((UByte) value).shortValue());
            }
            else if (actualType == UShort.class) {
                ctx.statement().setInt(ctx.index(), ((UShort) value).intValue());
            }
            else if (actualType == UInteger.class) {





                ctx.statement().setLong(ctx.index(), ((UInteger) value).longValue());
            }
            else if (actualType == ULong.class) {





                ctx.statement().setBigDecimal(ctx.index(), new BigDecimal(value.toString()));
            }
            else if (actualType == UUID.class) {
                switch (dialect.family()) {

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

            // The type byte[] is handled earlier. byte[][] can be handled here
            else if (actualType.isArray()) {
                switch (dialect.family()) {
                    case POSTGRES: {
                        ctx.statement().setString(ctx.index(), toPGArrayString((Object[]) value));
                        break;
                    }
                    case HSQLDB: {
                        Object[] a = (Object[]) value;
                        Class<?> t = actualType;

                        // [#2325] Some array types are not natively supported by HSQLDB
                        // More integration tests are probably needed...
                        if (actualType == UUID[].class) {
                            a = Convert.convertArray(a, String[].class);
                            t = String[].class;
                        }

                        ctx.statement().setArray(ctx.index(), new MockArray(dialect, a, t));
                        break;
                    }
                    case H2: {
                        ctx.statement().setObject(ctx.index(), value);
                        break;
                    }
                    default:
                        throw new SQLDialectNotSupportedException("Cannot bind ARRAY types in dialect " + dialect);
                }
            }






            else if (EnumType.class.isAssignableFrom(actualType)) {
                ctx.statement().setString(ctx.index(), ((EnumType) value).getLiteral());
            }
            else {
                ctx.statement().setObject(ctx.index(), value);
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void set(BindingSetSQLOutputContext<U> ctx) throws SQLException {
        Configuration configuration = ctx.configuration();
        T value = converter.to(ctx.value());

        if (value == null) {
            ctx.output().writeObject(null);
        }
        else if (type == Blob.class) {
            ctx.output().writeBlob((Blob) value);
        }
        else if (type == Boolean.class) {
            ctx.output().writeBoolean((Boolean) value);
        }
        else if (type == BigInteger.class) {
            ctx.output().writeBigDecimal(new BigDecimal((BigInteger) value));
        }
        else if (type == BigDecimal.class) {
            ctx.output().writeBigDecimal((BigDecimal) value);
        }
        else if (type == Byte.class) {
            ctx.output().writeByte((Byte) value);
        }
        else if (type == byte[].class) {

            // [#1327] Oracle cannot serialise BLOBs as byte[] to SQLOutput
            // Use reflection to avoid dependency on OJDBC
            if (isLob) {
                Blob blob = null;

                try {
                    blob = on("oracle.sql.BLOB").call("createTemporary",
                               on(ctx.output()).call("getSTRUCT")
                                         .call("getJavaSqlConnection").get(),
                               false,
                               on("oracle.sql.BLOB").get("DURATION_SESSION")).get();

                    blob.setBytes(1, (byte[]) value);
                    ctx.output().writeBlob(blob);
                }
                finally {
                    DefaultExecuteContext.register(blob);
                }
            }
            else {
                ctx.output().writeBytes((byte[]) value);
            }
        }
        else if (type == Clob.class) {
            ctx.output().writeClob((Clob) value);
        }
        else if (type == Date.class) {
            Date date = (Date) value;









            ctx.output().writeDate(date);
        }
        else if (type == Double.class) {
            ctx.output().writeDouble((Double) value);
        }
        else if (type == Float.class) {
            ctx.output().writeFloat((Float) value);
        }
        else if (type == Integer.class) {
            ctx.output().writeInt((Integer) value);
        }
        else if (type == Long.class) {
            ctx.output().writeLong((Long) value);
        }
        else if (type == Short.class) {
            ctx.output().writeShort((Short) value);
        }
        else if (type == String.class) {

            // [#1327] Oracle cannot serialise CLOBs as String to SQLOutput
            // Use reflection to avoid dependency on OJDBC
            if (isLob) {
                Clob clob = null;

                try {
                    clob = on("oracle.sql.CLOB").call("createTemporary",
                               on(ctx.output()).call("getSTRUCT")
                                         .call("getJavaSqlConnection").get(),
                               false,
                               on("oracle.sql.CLOB").get("DURATION_SESSION")).get();

                    clob.setString(1, (String) value);
                    ctx.output().writeClob(clob);
                }
                finally {
                    DefaultExecuteContext.register(clob);
                }
            }
            else {
                ctx.output().writeString((String) value);
            }
        }
        else if (type == Time.class) {
            ctx.output().writeTime((Time) value);
        }
        else if (type == Timestamp.class) {
            ctx.output().writeTimestamp((Timestamp) value);
        }
        else if (type == YearToMonth.class) {
            ctx.output().writeString(value.toString());
        }
        else if (type == DayToSecond.class) {
            ctx.output().writeString(value.toString());
        }
//        else if (type.isArray()) {
//            stream.writeArray(value);
//        }
        else if (UNumber.class.isAssignableFrom(type)) {
            ctx.output().writeString(value.toString());
        }
        else if (type == UUID.class) {
            ctx.output().writeString(value.toString());
        }





        else if (EnumType.class.isAssignableFrom(type)) {
            ctx.output().writeString(((EnumType) value).getLiteral());
        }
        else if (UDTRecord.class.isAssignableFrom(type)) {
            ctx.output().writeObject((UDTRecord<?>) value);
        }
        else {
            throw new UnsupportedOperationException("Type " + type + " is not supported");
        }
    }





























    @SuppressWarnings("unchecked")
    @Override
    public void get(BindingGetResultSetContext<U> ctx) throws SQLException {
        T result = null;

        if (type == Blob.class) {
            result = (T) ctx.resultSet().getBlob(ctx.index());
        }
        else if (type == Boolean.class) {
            result = (T) wasNull(ctx.resultSet(), Boolean.valueOf(ctx.resultSet().getBoolean(ctx.index())));
        }
        else if (type == BigInteger.class) {
            // The SQLite JDBC driver doesn't support BigDecimals
            if (ctx.configuration().dialect() == SQLDialect.SQLITE) {
                result = Convert.convert(ctx.resultSet().getString(ctx.index()), (Class<T>) BigInteger.class);
            }
            else {
                BigDecimal b = ctx.resultSet().getBigDecimal(ctx.index());
                result = (T) (b == null ? null : b.toBigInteger());
            }
        }
        else if (type == BigDecimal.class) {
            // The SQLite JDBC driver doesn't support BigDecimals
            if (ctx.configuration().dialect() == SQLDialect.SQLITE) {
                result = Convert.convert(ctx.resultSet().getString(ctx.index()), (Class<T>) BigDecimal.class);
            }
            else {
                result = (T) ctx.resultSet().getBigDecimal(ctx.index());
            }
        }
        else if (type == Byte.class) {
            result = (T) wasNull(ctx.resultSet(), Byte.valueOf(ctx.resultSet().getByte(ctx.index())));
        }
        else if (type == byte[].class) {
            result = (T) ctx.resultSet().getBytes(ctx.index());
        }
        else if (type == Clob.class) {
            result = (T) ctx.resultSet().getClob(ctx.index());
        }
        else if (type == Date.class) {
            result = (T) getDate(ctx.family(), ctx.resultSet(), ctx.index());
        }
        else if (type == Double.class) {
            result = (T) wasNull(ctx.resultSet(), Double.valueOf(ctx.resultSet().getDouble(ctx.index())));
        }
        else if (type == Float.class) {
            result = (T) wasNull(ctx.resultSet(), Float.valueOf(ctx.resultSet().getFloat(ctx.index())));
        }
        else if (type == Integer.class) {
            result = (T) wasNull(ctx.resultSet(), Integer.valueOf(ctx.resultSet().getInt(ctx.index())));
        }

        else if (type == LocalDate.class) {
            result = (T) localDate(getDate(ctx.family(), ctx.resultSet(), ctx.index()));
        }
        else if (type == LocalTime.class) {
            result = (T) localTime(getTime(ctx.family(), ctx.resultSet(), ctx.index()));
        }
        else if (type == LocalDateTime.class) {
            result = (T) localDateTime(getTimestamp(ctx.family(), ctx.resultSet(), ctx.index()));
        }

        else if (type == Long.class) {
            result = (T) wasNull(ctx.resultSet(), Long.valueOf(ctx.resultSet().getLong(ctx.index())));
        }

        else if (type == OffsetTime.class) {
            result = (T) offsetTime(ctx.resultSet().getString(ctx.index()));
        }
        else if (type == OffsetDateTime.class) {
            result = (T) offsetDateTime(ctx.resultSet().getString(ctx.index()));
        }

        else if (type == Short.class) {
            result = (T) wasNull(ctx.resultSet(), Short.valueOf(ctx.resultSet().getShort(ctx.index())));
        }
        else if (type == String.class) {
            result = (T) ctx.resultSet().getString(ctx.index());
        }
        else if (type == Time.class) {
            result = (T) getTime(ctx.family(), ctx.resultSet(), ctx.index());
        }
        else if (type == Timestamp.class) {
            result = (T) getTimestamp(ctx.family(), ctx.resultSet(), ctx.index());
        }
        else if (type == YearToMonth.class) {
            if (ctx.family() == POSTGRES) {
                Object object = ctx.resultSet().getObject(ctx.index());
                result = (T) (object == null ? null : PostgresUtils.toYearToMonth(object));
            }
            else {
                String string = ctx.resultSet().getString(ctx.index());
                result = (T) (string == null ? null : YearToMonth.valueOf(string));
            }
        }
        else if (type == DayToSecond.class) {
            if (ctx.family() == POSTGRES) {
                Object object = ctx.resultSet().getObject(ctx.index());
                result = (T) (object == null ? null : PostgresUtils.toDayToSecond(object));
            }
            else {
                String string = ctx.resultSet().getString(ctx.index());
                result = (T) (string == null ? null : DayToSecond.valueOf(string));
            }
        }
        else if (type == UByte.class) {
            result = (T) Convert.convert(ctx.resultSet().getString(ctx.index()), UByte.class);
        }
        else if (type == UShort.class) {
            result = (T) Convert.convert(ctx.resultSet().getString(ctx.index()), UShort.class);
        }
        else if (type == UInteger.class) {
            result = (T) Convert.convert(ctx.resultSet().getString(ctx.index()), UInteger.class);
        }
        else if (type == ULong.class) {
            result = (T) Convert.convert(ctx.resultSet().getString(ctx.index()), ULong.class);
        }
        else if (type == UUID.class) {
            switch (ctx.family()) {

                // [#1624] Some JDBC drivers natively support the
                // java.util.UUID data type
                case H2:
                case POSTGRES: {
                    result = (T) ctx.resultSet().getObject(ctx.index());
                    break;
                }








                // Most databases don't have such a type. In this case, jOOQ
                // emulates the type
                default: {
                    result = (T) Convert.convert(ctx.resultSet().getString(ctx.index()), UUID.class);
                    break;
                }
            }
        }

        // The type byte[] is handled earlier. byte[][] can be handled here
        else if (type.isArray()) {
            switch (ctx.family()) {
                case POSTGRES: {
                    result = pgGetArray(ctx, ctx.resultSet(), type, ctx.index());
                    break;
                }

                default:
                    // Note: due to a HSQLDB bug, it is not recommended to call rs.getObject() here:
                    // See https://sourceforge.net/tracker/?func=detail&aid=3181365&group_id=23316&atid=378131
                    result = (T) convertArray(ctx.resultSet().getArray(ctx.index()), (Class<? extends Object[]>) type);
                    break;
            }
        }





        else if (EnumType.class.isAssignableFrom(type)) {
            result = (T) getEnumType((Class<EnumType>) type, ctx.resultSet().getString(ctx.index()));
        }
        else if (Record.class.isAssignableFrom(type)) {
            switch (ctx.family()) {
                case POSTGRES:
                    result = (T) pgNewRecord(type, null, ctx.resultSet().getObject(ctx.index()));
                    break;

                default:
                    result = (T) ctx.resultSet().getObject(ctx.index(), typeMap(type, ctx.configuration()));
                    break;
            }
        }
        else if (Result.class.isAssignableFrom(type)) {
            ResultSet nested = (ResultSet) ctx.resultSet().getObject(ctx.index());
            result = (T) DSL.using(ctx.configuration()).fetch(nested);
        }
        else {
            result = (T) unlob(ctx.resultSet().getObject(ctx.index()));
        }

        // [#4372] Attach records if possible / required
        if (result instanceof Attachable && attachRecords(ctx.configuration()))
            ((Attachable) result).attach(ctx.configuration());

        ctx.value(converter.from(result));
    }


    private final LocalDate localDate(Date date) {
        return date == null ? null : date.toLocalDate();
    }

    private final LocalTime localTime(Time time) {
        return time == null ? null : time.toLocalTime();
    }

    private final LocalDateTime localDateTime(Timestamp timestamp) {
        return timestamp == null ? null : timestamp.toLocalDateTime();
    }

    private final OffsetTime offsetTime(String string) {
        if (string == null)
            return null;

        // [#4338] [#5180] PostgreSQL is more lenient regarding the offset format
        if (string.lastIndexOf('+') == string.length() - 3 || string.lastIndexOf('-') == string.length() - 3)
            string = string + ":00";

        return OffsetTime.parse(string);
    }

    private final OffsetDateTime offsetDateTime(String string) {
        if (string == null)
            return null;

        // [#4338] [#5180] PostgreSQL is more lenient regarding the offset format
        if (string.lastIndexOf('+') == string.length() - 3 || string.lastIndexOf('-') == string.length() - 3)
            string = string + ":00";

        // [#4338] SQL supports the alternative ISO 8601 date format, where a
        // whitespace character separates date and time. java.time does not
        if (string.charAt(10) == ' ')
            string = string.substring(0, 10) + "T" + string.substring(11);

        return OffsetDateTime.parse(string);
    }


    @SuppressWarnings("unchecked")
    @Override
    public void get(BindingGetStatementContext<U> ctx) throws SQLException {
        T result = null;

        if (type == Blob.class) {
            result = (T) ctx.statement().getBlob(ctx.index());
        }
        else if (type == Boolean.class) {
            result = (T) wasNull(ctx.statement(), Boolean.valueOf(ctx.statement().getBoolean(ctx.index())));
        }
        else if (type == BigInteger.class) {
            BigDecimal d = ctx.statement().getBigDecimal(ctx.index());
            result = (T) (d == null ? null : d.toBigInteger());
        }
        else if (type == BigDecimal.class) {
            result = (T) ctx.statement().getBigDecimal(ctx.index());
        }
        else if (type == Byte.class) {
            result = (T) wasNull(ctx.statement(), Byte.valueOf(ctx.statement().getByte(ctx.index())));
        }
        else if (type == byte[].class) {
            result = (T) ctx.statement().getBytes(ctx.index());
        }
        else if (type == Clob.class) {
            result = (T) ctx.statement().getClob(ctx.index());
        }
        else if (type == Date.class) {









            result = (T) ctx.statement().getDate(ctx.index());
        }
        else if (type == Double.class) {
            result = (T) wasNull(ctx.statement(), Double.valueOf(ctx.statement().getDouble(ctx.index())));
        }
        else if (type == Float.class) {
            result = (T) wasNull(ctx.statement(), Float.valueOf(ctx.statement().getFloat(ctx.index())));
        }
        else if (type == Integer.class) {
            result = (T) wasNull(ctx.statement(), Integer.valueOf(ctx.statement().getInt(ctx.index())));
        }
        else if (type == Long.class) {
            result = (T) wasNull(ctx.statement(), Long.valueOf(ctx.statement().getLong(ctx.index())));
        }
        else if (type == Short.class) {
            result = (T) wasNull(ctx.statement(), Short.valueOf(ctx.statement().getShort(ctx.index())));
        }
        else if (type == String.class) {
            result = (T) ctx.statement().getString(ctx.index());
        }
        else if (type == Time.class) {
            result = (T) ctx.statement().getTime(ctx.index());
        }
        else if (type == Timestamp.class) {
            result = (T) ctx.statement().getTimestamp(ctx.index());
        }
        else if (type == YearToMonth.class) {
            if (ctx.family() == POSTGRES) {
                Object object = ctx.statement().getObject(ctx.index());
                result = (T) (object == null ? null : PostgresUtils.toYearToMonth(object));
            }
            else {
                String string = ctx.statement().getString(ctx.index());
                result = (T) (string == null ? null : YearToMonth.valueOf(string));
            }
        }
        else if (type == DayToSecond.class) {
            if (ctx.family() == POSTGRES) {
                Object object = ctx.statement().getObject(ctx.index());
                result = (T) (object == null ? null : PostgresUtils.toDayToSecond(object));
            }
            else {
                String string = ctx.statement().getString(ctx.index());
                result = (T) (string == null ? null : DayToSecond.valueOf(string));
            }
        }
        else if (type == UByte.class) {
            String string = ctx.statement().getString(ctx.index());
            result = (T) (string == null ? null : UByte.valueOf(string));
        }
        else if (type == UShort.class) {
            String string = ctx.statement().getString(ctx.index());
            result = (T) (string == null ? null : UShort.valueOf(string));
        }
        else if (type == UInteger.class) {
            String string = ctx.statement().getString(ctx.index());
            result = (T) (string == null ? null : UInteger.valueOf(string));
        }
        else if (type == ULong.class) {
            String string = ctx.statement().getString(ctx.index());
            result = (T) (string == null ? null : ULong.valueOf(string));
        }
        else if (type == UUID.class) {
            switch (ctx.family()) {

                // [#1624] Some JDBC drivers natively support the
                // java.util.UUID data type
                case H2:
                case POSTGRES: {
                    result = (T) ctx.statement().getObject(ctx.index());
                    break;
                }








                // Most databases don't have such a type. In this case, jOOQ
                // emulates the type
                default: {
                    result = (T) Convert.convert(ctx.statement().getString(ctx.index()), UUID.class);
                    break;
                }
            }
        }

        // The type byte[] is handled earlier. byte[][] can be handled here
        else if (type.isArray()) {
            result = (T) convertArray(ctx.statement().getObject(ctx.index()), (Class<? extends Object[]>)type);
        }





        else if (EnumType.class.isAssignableFrom(type)) {
            result = (T) getEnumType((Class<EnumType>) type, ctx.statement().getString(ctx.index()));
        }
        else if (Record.class.isAssignableFrom(type)) {
            switch (ctx.family()) {
                case POSTGRES:
                    result = (T) pgNewRecord(type, null, ctx.statement().getObject(ctx.index()));
                    break;

                default:
                    result = (T) ctx.statement().getObject(ctx.index(), typeMap(type, ctx.configuration()));
                    break;
            }
        }
        else if (Result.class.isAssignableFrom(type)) {
            ResultSet nested = (ResultSet) ctx.statement().getObject(ctx.index());
            result = (T) DSL.using(ctx.configuration()).fetch(nested);
        }
        else {
            result = (T) ctx.statement().getObject(ctx.index());
        }

        // [#4372] Attach records if possible / required
        if (result instanceof Attachable && attachRecords(ctx.configuration()))
            ((Attachable) result).attach(ctx.configuration());

        ctx.value(converter.from(result));
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

    @SuppressWarnings("unchecked")
    @Override
    public void get(BindingGetSQLInputContext<U> ctx) throws SQLException {
        T result = null;

        if (type == Blob.class) {
            result = (T) ctx.input().readBlob();
        }
        else if (type == Boolean.class) {
            result = (T) wasNull(ctx.input(), Boolean.valueOf(ctx.input().readBoolean()));
        }
        else if (type == BigInteger.class) {
            BigDecimal d = ctx.input().readBigDecimal();
            result = (T) (d == null ? null : d.toBigInteger());
        }
        else if (type == BigDecimal.class) {
            result = (T) ctx.input().readBigDecimal();
        }
        else if (type == Byte.class) {
            result = (T) wasNull(ctx.input(), Byte.valueOf(ctx.input().readByte()));
        }
        else if (type == byte[].class) {

            // [#1327] Oracle cannot deserialise BLOBs as byte[] from SQLInput
            if (isLob) {
                Blob blob = null;
                try {
                    blob = ctx.input().readBlob();
                    result = (T) (blob == null ? null : blob.getBytes(1, (int) blob.length()));
                }
                finally {
                    safeFree(blob);
                }
            }
            else {
                result = (T) ctx.input().readBytes();
            }
        }
        else if (type == Clob.class) {
            result = (T) ctx.input().readClob();
        }
        else if (type == Date.class) {









            result = (T) ctx.input().readDate();
        }
        else if (type == Double.class) {
            result = (T) wasNull(ctx.input(), Double.valueOf(ctx.input().readDouble()));
        }
        else if (type == Float.class) {
            result = (T) wasNull(ctx.input(), Float.valueOf(ctx.input().readFloat()));
        }
        else if (type == Integer.class) {
            result = (T) wasNull(ctx.input(), Integer.valueOf(ctx.input().readInt()));
        }
        else if (type == Long.class) {
            result = (T) wasNull(ctx.input(), Long.valueOf(ctx.input().readLong()));
        }
        else if (type == Short.class) {
            result = (T) wasNull(ctx.input(), Short.valueOf(ctx.input().readShort()));
        }
        else if (type == String.class) {
            result = (T) ctx.input().readString();
        }
        else if (type == Time.class) {
            result = (T) ctx.input().readTime();
        }
        else if (type == Timestamp.class) {
            result = (T) ctx.input().readTimestamp();
        }
        else if (type == YearToMonth.class) {
            String string = ctx.input().readString();
            result = (T) (string == null ? null : YearToMonth.valueOf(string));
        }
        else if (type == DayToSecond.class) {
            String string = ctx.input().readString();
            result = (T) (string == null ? null : DayToSecond.valueOf(string));
        }
        else if (type == UByte.class) {
            String string = ctx.input().readString();
            result = (T) (string == null ? null : UByte.valueOf(string));
        }
        else if (type == UShort.class) {
            String string = ctx.input().readString();
            result = (T) (string == null ? null : UShort.valueOf(string));
        }
        else if (type == UInteger.class) {
            String string = ctx.input().readString();
            result = (T) (string == null ? null : UInteger.valueOf(string));
        }
        else if (type == ULong.class) {
            String string = ctx.input().readString();
            result = (T) (string == null ? null : ULong.valueOf(string));
        }
        else if (type == UUID.class) {
            result = (T) Convert.convert(ctx.input().readString(), UUID.class);
        }

        // The type byte[] is handled earlier. byte[][] can be handled here
        else if (type.isArray()) {
            Array array = ctx.input().readArray();
            result = (T) (array == null ? null : array.getArray());
        }





        else if (EnumType.class.isAssignableFrom(type)) {
            result = (T) getEnumType((Class<EnumType>) type, ctx.input().readString());
        }
        else if (UDTRecord.class.isAssignableFrom(type)) {
            result = (T) ctx.input().readObject();
        }
        else {
            result = (T) unlob(ctx.input().readObject());
        }

        ctx.value(converter.from(result));
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

    @SuppressWarnings("unchecked")
    private static final <E extends EnumType> E getEnumType(Class<? extends E> type, String literal) {
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

    private static final Date getDate(SQLDialect family, ResultSet rs, int index) throws SQLException {

        // SQLite's type affinity needs special care...
        if (family == SQLDialect.SQLITE) {
            String date = rs.getString(index);
            return date == null ? null : new Date(parse(Date.class, date));
        }









        else {
            return rs.getDate(index);
        }
    }

    private static final Time getTime(SQLDialect family, ResultSet rs, int index) throws SQLException {

        // SQLite's type affinity needs special care...
        if (family == SQLDialect.SQLITE) {
            String time = rs.getString(index);
            return time == null ? null : new Time(parse(Time.class, time));
        }

        else {
            return rs.getTime(index);
        }
    }

    private static final Timestamp getTimestamp(SQLDialect family, ResultSet rs, int index) throws SQLException {

        // SQLite's type affinity needs special care...
        if (family == SQLDialect.SQLITE) {
            String timestamp = rs.getString(index);
            return timestamp == null ? null : new Timestamp(parse(Timestamp.class, timestamp));
        }

        else {
            return rs.getTimestamp(index);
        }
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

    // -------------------------------------------------------------------------
    // XXX: The following section has been added for Postgres UDT support. The
    // official Postgres JDBC driver does not implement SQLData and similar
    // interfaces. Instead, a string representation of a UDT has to be parsed
    // -------------------------------------------------------------------------

    private static final <T> T pgFromString(Class<T> type, String string) {
        return pgFromString(Converters.identity(type), string);
    }

    @SuppressWarnings("unchecked")
    private static final <T> T pgFromString(Converter<?, T> converter, String string) {
        Class<T> type = converter.toType();

        if (string == null) {
            return null;
        }
        else if (type == Blob.class) {
            // Not supported
        }
        else if (type == Boolean.class) {
            return (T) Convert.convert(string, Boolean.class);
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
            return (T) Date.valueOf(string);
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
            return (T) Time.valueOf(string);
        }
        else if (type == Timestamp.class) {
            return (T) Timestamp.valueOf(string);
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





        else if (EnumType.class.isAssignableFrom(type)) {
            return (T) getEnumType((Class<EnumType>) type, string);
        }
        else if (Record.class.isAssignableFrom(type)) {
            return (T) pgNewRecord(type, null, string);
        }
        else if (type == Object.class) {
            return (T) string;
        }
        else {
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
        if (object == null) {
            return null;
        }

        return Tools.newRecord(true, (Class<Record>) type, fields)
                    .operate(new RecordOperation<Record, RuntimeException>() {

                @Override
                public Record operate(Record record) {
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
                return (T) convertArray(array, (Class<? extends Object[]>) type);
            }

            // This might be a UDT (not implemented exception...)
            catch (Exception e) {
                List<Object> result = new ArrayList<Object>();
                ResultSet arrayRs = null;

                // Try fetching the array as a JDBC ResultSet
                try {
                    arrayRs = array.getResultSet();

                    while (arrayRs.next()) {
                        DefaultBindingGetResultSetContext<T> out = new DefaultBindingGetResultSetContext<T>(ctx.configuration(), ctx.data(), arrayRs, 2);
                        new DefaultBinding<T, T>(Converters.identity((Class<T>) type.getComponentType()), false).get(out);
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
            throw new DataTypeException("Error while creating array", e);
        }
    }

    static final <T> void pgSetValue(Record record, Field<T> field, String value) {
        record.set(field, pgFromString(field.getConverter(), value));
    }

    private static final void pgRenderEnumCast(RenderContext render, Class<?> type) {

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
}

