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

import static java.lang.Boolean.TRUE;
import static java.lang.Integer.toOctalString;
import static java.util.Arrays.asList;
// ...
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.HSQLDB;
// ...
// ...
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
// ...
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.using;
import static org.jooq.impl.DefaultExecuteContext.localTargetConnection;
import static org.jooq.impl.Utils.needsBackslashEscaping;
import static org.jooq.tools.StringUtils.leftPad;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

// ...
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
import org.jooq.RenderContext;
import org.jooq.Result;
import org.jooq.Row;
import org.jooq.SQLDialect;
import org.jooq.Schema;
import org.jooq.Scope;
import org.jooq.UDTRecord;
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
            theBinding = (Binding) new DefaultBinding<T, T>(new IdentityConverter<T>(type.getType()), type.isLob());
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
                    /* [pro] xx
                    xxxx xxxx
                    xxxx xxxxxxxxx
                    xx [/pro] */
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
                    /* [pro] xx
                    xxxx xxxxxxx
                    xx [/pro] */
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
                /* [pro] xx
                xxxx xxxxxxx
                xx [/pro] */
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
            int precision = scale + ((BigDecimal) converted).precision();

            // Firebird's max precision is 18
            if (family == FIREBIRD) {
                precision = Math.min(precision, 18);
            }

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
        // [#1130] TODO type can be null for ARRAY types, etc.
        else if (family == POSTGRES && (sqlDataType == null || !sqlDataType.isTemporal())) {
            toSQL(ctx, converted);
        }

        // [#1727] VARCHAR types should be cast to their actual lengths in some
        // dialects
        else if ((sqlDataType == SQLDataType.VARCHAR || sqlDataType == SQLDataType.CHAR) && asList(FIREBIRD).contains(family)) {
            toSQLCast(ctx, converted, dataType, getValueLength(converted), 0, 0);
        }

        /* [pro] xx
        xx xxxxxxx xxxx xxxx xxxxx xxxxxx xxx xx xxxx xx xxx xxxxxx xx xxxx xxxxxxxx
        xxxx xx xxxxxxxxxxxx xx xxxxxxxxxxxxxxxxxxxxxxx xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx x
            xxxxxxxxxxxxxx xxxxxxxxxx xxxxxxxxx xx xx xxx
        x
        xx [/pro] */

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
        ctx.render().keyword("cast").sql("(");
        toSQL(ctx, converted);
        ctx.render().sql(" ").keyword("as").sql(" ")
               .sql(dataType.length(length).precision(precision, scale).getCastTypeName(ctx.configuration()))
               .sql(")");
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
                /* [pro] xx
                xxxx xx xxxxxxx xx xxxxxxxxx x
                    xxxxxxxxxxxxxxxxxxxxx xxxx x xxxxx x xxxxxxx
                x
                xx [/pro] */
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
                /* [pro] xx
                xxxx xx xxxxxxx xx xxxx x
                    xxxxxxxxxxxxxxxxxxxxxx
                          xxxxxxxxxxx
                          xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                          xxxxxxxxxxx
                x
                xx [/pro] */
                else if (asList(DERBY, H2, HSQLDB, MARIADB, MYSQL, SQLITE).contains(family)) {
                    render.sql("X'")
                          .sql(convertBytesToHex(binary))
                          .sql("'");
                }
                else if (asList().contains(family)) {
                    render.keyword("hextoraw('")
                          .sql(convertBytesToHex(binary))
                          .sql("')");
                }
                else if (family == POSTGRES) {
                    render.sql("E'")
                          .sql(convertBytesToPostgresOctal(binary))
                          .keyword("'::bytea");
                }

                // This default behaviour is used in debug logging for dialects
                // that do not support inlining binary data
                else {
                    render.sql("X'")
                          .sql(convertBytesToHex(binary))
                          .sql("'");
                }
            }

            // Interval extends Number, so let Interval come first!
            else if (Interval.class.isAssignableFrom(type)) {
                render.sql("'")
                      .sql(escape(val, render))
                      .sql("'");
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
                    render.sql("'").sql(escape(val, render)).sql("'");
                }

                /* [pro] xx
                xxxx xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx x
                    xxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxx
                x

                xxxx xx xxxxxxx xx xxxxxxxxx x
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxx xxxxxxxxxxxxxxxx xx xxxxxx
                x
                xx [/pro] */

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
                    render.keyword("date '").sql(escape(val, render)).sql("'");
                }
            }
            else if (type == Timestamp.class) {

                // The SQLite JDBC driver does not implement the escape syntax
                // [#1253] SQL Server and Sybase do not implement timestamp literals
                if (asList(SQLITE).contains(family)) {
                    render.sql("'").sql(escape(val, render)).sql("'");
                }

                /* [pro] xx
                xxxx xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx x
                    xxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxx
                x

                xxxx xx xxxxxxx xx xxxxxxxxx x
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxx xxxxxxxxxxxxxxxx xx xxxxxxxxxxx
                x
                xx [/pro] */

                // [#1253] Derby doesn't support the standard literal
                else if (family == DERBY) {
                    render.keyword("timestamp('").sql(escape(val, render)).sql("')");
                }

                // CUBRID timestamps have no fractional seconds
                else if (family == CUBRID) {
                    render.keyword("datetime '").sql(escape(val, render)).sql("'");
                }

                // [#3648] Circumvent a MySQL bug related to date literals
                else if (family == MYSQL) {
                    render.keyword("{ts '").sql(escape(val, render)).sql("'}");
                }

                // Most dialects implement SQL standard timestamp literals
                else {
                    render.keyword("timestamp '").sql(escape(val, render)).sql("'");
                }
            }
            else if (type == Time.class) {

                // The SQLite JDBC driver does not implement the escape syntax
                // [#1253] SQL Server and Sybase do not implement time literals
                if (asList(SQLITE).contains(family)) {
                    render.sql("'").sql(new SimpleDateFormat("HH:mm:ss").format((Time) val)).sql("'");
                }

                /* [pro] xx
                xxxx xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx x
                    xxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxx
                x

                xxxx xx xxxxxxx xx xxxxxxxxx x
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxx xxxxxxxxxxxxxxxx xx xxxxxxxxx
                x
                xx [/pro] */

                // [#1253] Derby doesn't support the standard literal
                else if (family == DERBY) {
                    render.keyword("time").sql("('").sql(escape(val, render)).sql("')");
                }

                // [#3648] Circumvent a MySQL bug related to date literals
                else if (family == MYSQL) {
                    render.keyword("{t '").sql(escape(val, render)).sql("'}");
                }
                /* [pro] xx
                xx xxxxxxx xxxxxx xxxxxxx xxxx xxxx xxxxxxxx
                xxxx xx xxxxxxx xx xxxxxxx x
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxx xxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxx
                x

                xx [/pro] */
                // Most dialects implement SQL standard time literals
                else {
                    render.keyword("time").sql(" '").sql(escape(val, render)).sql("'");
                }
            }
            else if (type.isArray()) {
                String separator = "";

                // H2 renders arrays as rows
                if (family == H2) {
                    render.sql("(");

                    for (Object o : ((Object[]) val)) {
                        render.sql(separator);
                        new DefaultBinding<Object, Object>(new IdentityConverter(type.getComponentType()), isLob).sql(new DefaultBindingSQLContext<Object>(ctx.configuration(), ctx.render(), o));
                        separator = ", ";
                    }

                    render.sql(")");
                }

                // By default, render HSQLDB / POSTGRES syntax
                else {
                    render.keyword("ARRAY");
                    render.sql("[");

                    for (Object o : ((Object[]) val)) {
                        render.sql(separator);
                        new DefaultBinding<Object, Object>(new IdentityConverter(type.getComponentType()), isLob).sql(new DefaultBindingSQLContext<Object>(ctx.configuration(), ctx.render(), o));
                        separator = ", ";
                    }

                    render.sql("]");

                    // [#3214] Some PostgreSQL array type literals need explicit casting
                    if (family == POSTGRES && EnumType.class.isAssignableFrom(type.getComponentType())) {
                        render.sql("::")
                               .keyword(DefaultDataType.getDataType(family, type).getCastTypeName(render.configuration()));
                    }
                }
            }
            /* [pro] xx
            xxxx xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx x
                xxxxxxxxxxxxxxxxxxxxxx xxxxxxxx xxxxxx
            x
            xx [/pro] */
            else if (EnumType.class.isAssignableFrom(type)) {
                String literal = ((EnumType) val).getLiteral();

                if (literal == null) {
                    new DefaultBinding<Object, Object>(new IdentityConverter(String.class), isLob).sql(new DefaultBindingSQLContext<Object>(ctx.configuration(), ctx.render(), literal));
                }
                else {
                    new DefaultBinding<Object, Object>(new IdentityConverter(String.class), isLob).sql(new DefaultBindingSQLContext<Object>(ctx.configuration(), ctx.render(), literal));
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
                render.sql("'")
                       .sql(escape(val, render), true)
                       .sql("'");
            }
        }

        // In Postgres, some additional casting must be done in some cases...
        else if (family == SQLDialect.POSTGRES) {

            // Postgres needs explicit casting for array types
            if (type.isArray() && byte[].class != type) {
                render.sql(ctx.variable());
                render.sql("::");
                render.keyword(DefaultDataType.getDataType(family, type).getCastTypeName(render.configuration()));
            }

            // ... and also for enum types
            else if (EnumType.class.isAssignableFrom(type)) {
                render.sql(ctx.variable());

                // [#968] Don't cast "synthetic" enum types (note, val can be null!)
                EnumType e = (EnumType) type.getEnumConstants()[0];
                Schema schema = e.getSchema();

                if (schema != null) {
                    render.sql("::");

                    schema = using(render.configuration()).map(schema);
                    if (schema != null && TRUE.equals(render.configuration().settings().isRenderSchema())) {
                        render.visit(schema);
                        render.sql(".");
                    }

                    render.visit(name(e.getName()));
                }
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

    /**
     * Postgres uses octals instead of hex encoding
     */
    private static final String convertBytesToPostgresOctal(byte[] binary) {
        StringBuilder sb = new StringBuilder();

        for (byte b : binary) {
            sb.append("\\\\");
            sb.append(leftPad(toOctalString(b), 3, '0'));
        }

        return sb.toString();
    }

    @Override
    public void register(BindingRegisterContext<U> ctx) throws SQLException {
        Configuration configuration = ctx.configuration();
        int sqlType = DefaultDataType.getDataType(ctx.dialect(), type).getSQLType();

        switch (configuration.dialect().family()) {
            /* [pro] xx

            xx xxx xxxx xxxx xxxxxxx xxxxx xxxxxx xxxxx xx xxxx
            xx xxxx xxx xxxx xxxx
            xxxx xxxxxxx x
                xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx x
                    xxxxxxxxxxxx xxxxxx x xxxxx
                        xxxxxxxxxxxxxxxxx xxxxxxxx xxxxxxx xxxxxxxxxxxxxx xxxxx
                        xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxx
                x

                xxxx xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx x
                    xxxxxxxxxxxxxx xxxxxx x xxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxx xxxxxxxxxxxxxxxx xxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxx xxxxxxxxxxxxxxxxxx
                x

                xx xxx xxxxxxx xxxxxxxxx xx xxx xx xxxxxxxx x xxxx
                xx xxxxxxx
                xxxx x
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxx
                x

                xxxxxx
            x

            xx [/pro] */
            default: {
                ctx.statement().registerOutParameter(ctx.index(), sqlType);
                break;
            }
        }
    }

    @Override
    public void set(BindingSetStatementContext<U> ctx) throws SQLException {
        Configuration configuration = ctx.configuration();
        SQLDialect dialect = ctx.dialect();
        T value = converter.to(ctx.value());

        if (log.isTraceEnabled()) {
            if (value != null && value.getClass().isArray() && value.getClass() != byte[].class) {
                log.trace("Binding variable " + ctx.index(), Arrays.asList((Object[]) value) + " (" + type + ")");
            }
            else {
                log.trace("Binding variable " + ctx.index(), value + " (" + type + ")");
            }
        }

        // Setting null onto a prepared statement is subtly different for every
        // SQL dialect. See the following section for details
        if (value == null) {
            int sqlType = DefaultDataType.getDataType(dialect, type).getSQLType();

            /* [pro] xx
            xx xxxxxxxxxxxx xxxxx xxxxx xxxx xx xx xxxxx xxxx xxxxx xxxx xxxx
            xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx x
                xxxxxx xxxxxxxx x xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxx
                xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxx xxxxxxxxxx
            x

            xxxx
            xx [/pro] */
            // [#1126] Oracle's UDTs need to be bound with their type name
            if (UDTRecord.class.isAssignableFrom(type)) {
                String typeName = Utils.newRecord(false, (Class<UDTRecord<?>>) type)
                                       .<RuntimeException>operate(null)
                                       .getUDT()
                                       .getName();
                ctx.statement().setNull(ctx.index(), sqlType, typeName);
            }

            // [#1225] [#1227] TODO Put this logic into DataType
            // Some dialects have trouble binding binary data as BLOB
            else if (asList(POSTGRES).contains(configuration.dialect()) && sqlType == Types.BLOB) {
                ctx.statement().setNull(ctx.index(), Types.BINARY);
            }

            /* [pro] xx
            xxxx xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xx xxxxxxx x

                xx xxxx xxxxxxxxxx xxxx xx xxxx xxxxxx xxxx xxx xxx xxxxxxxxx xxxxxx
                xx xxxxxxxxxxx xxxxx xxxxxxx xxx xxxxxxx
                xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                xxxxxx xxxxxxxxx x
                    xxxx xxxxxxxxxxxxx
                    xxxx xxxxxxxxxxxxxxxx
                    xxxx xxxxxxxxxxxxxxxxxxxx
                    xxxx xxxxxxxxxxx
                        xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxx
                        xxxxxx

                    xxxxxxxx
                        xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxx
                        xxxxxx
                x
            x

            xx xxxxxxx xxxxxxx xxx xxx xxxxxx xxxxxx xxxx xxx xxxx xxxxxxxx xxx xxx xxxx xxxxxx
            xx xxxxxx xxx xxx xxxxxxx xxxxx
            xxxx xx xxxxxxxx xx xxxxxxxxxxxxx xx xxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx x
                xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxx
            x

            xx [/pro] */
            // All other types can be set to null if the JDBC type is known
            else if (sqlType != Types.OTHER) {
                ctx.statement().setNull(ctx.index(), sqlType);
            }

            /* [pro] xx
            xx xxxxxx xxx xxx xxxxxxx xxxxxxx xxxxx xxxxxx xx xxx xx xxxx
            xx xxxxxxxxxxx xxx
            xxxx xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xx xxxxxxxxxx x
                xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxx
            x

            xx xxxxxx xxx xxxxxxx xxxxxxx xxxxx xxx xx xxx xx xxxx xxxxx xxxxxxx
            xxxx xx xxxxxxxxxxxxxxxxxxxxxxxx xx xxxxxxx x
                xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxx
            x

            xx [/pro] */
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
                /* [pro] xx
                xx xx xxxxxx xxxxxx xxxxxx xx xxxxx xxxxx xxxxx xx xxxxxxxxx xx xxxxxxxxxx xx xxxxxxx xxxxxxx
                xx xxxxxxxxxxxxxxxxx xx xxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxx xxxxx x x x xxx
                xxxx
                xx [/pro] */
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
                /* [pro] xx
                xx xxxxxxxxxxxxxxxxx xx xxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxx
                xxxx
                xx [/pro] */
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
                if (dialect == SQLITE) {
                    ctx.statement().setString(ctx.index(), ((Date) value).toString());
                }
                else {
                    ctx.statement().setDate(ctx.index(), (Date) value);
                }
            }
            else if (actualType == Time.class) {
                if (dialect == SQLITE) {
                    ctx.statement().setString(ctx.index(), ((Time) value).toString());
                }
                else {
                    ctx.statement().setTime(ctx.index(), (Time) value);
                }
            }
            else if (actualType == Timestamp.class) {
                if (dialect == SQLITE) {
                    ctx.statement().setString(ctx.index(), ((Timestamp) value).toString());
                }
                else {
                    ctx.statement().setTimestamp(ctx.index(), (Timestamp) value);
                }
            }

            // [#566] Interval data types are best bound as Strings
            else if (actualType == YearToMonth.class) {
                if (dialect == POSTGRES) {
                    ctx.statement().setObject(ctx.index(), toPGInterval((YearToMonth) value));
                }
                else {
                    ctx.statement().setString(ctx.index(), value.toString());
                }
            }
            else if (actualType == DayToSecond.class) {
                if (dialect == POSTGRES) {
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
                /* [pro] xx
                xx xxxxxxxxxxxxxxxxx xx xxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxx
                xxxx
                xx [/pro] */
                ctx.statement().setLong(ctx.index(), ((UInteger) value).longValue());
            }
            else if (actualType == ULong.class) {
                /* [pro] xx
                xx xxxxxxxxxxxxxxxxx xx xxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxx
                xxxx
                xx [/pro] */
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

                    /* [pro] xx
                    xx xxxxx xxx xxxxxxxx xxxx xxxx xxxxx xx xx xxxx xxxx xxxxxxxx
                    xx xxxx xx xxxx xxxxxxxxxx xxxxxxx xxxx xxxxxxxxxxxxxxxxxx
                    xxxx xxxxxxxxxx
                    xxxx xxxxxxx

                    xx [/pro] */
                    // Most databases don't have such a type. In this case, jOOQ
                    // simulates the type
                    default: {
                        ctx.statement().setString(ctx.index(), value.toString());
                        break;
                    }
                }
            }

            // The type byte[] is handled earlier. byte[][] can be handled here
            else if (actualType.isArray()) {
                switch (dialect) {
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
            /* [pro] xx
            xxxx xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx x
                xxxxxxxxxxxxxx xxxxxxxxxxx x xxxxxxxxxxxxxxxx xxxxxx
                xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
            x
            xx [/pro] */
            else if (EnumType.class.isAssignableFrom(actualType)) {
                ctx.statement().setString(ctx.index(), ((EnumType) value).getLiteral());
            }
            else {
                ctx.statement().setObject(ctx.index(), value);
            }
        }
    }

    @Override
    public void set(BindingSetSQLOutputContext<U> ctx) throws SQLException {
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
            ctx.output().writeDate((Date) value);
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
        /* [pro] xx
        xxxx xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx x

            xx xxxxxxx xx xxx xxxxxx xxxxxx xxxx xxxxxxxxxxxxxxxxxx xxx xxxx
            xx xxx xx xxxxxxxxxxxxxxxxxxx xxxxx xx xxxxxxxxxxx xxxxxx xx xxxxxx
            xxxxxxxxxxxxxx xxxxxxxxxxx x xxxxxxxxxxxxxxxx xxxxxx
            xxxxxxxx xxxxx x xxxxxxxxxxxxxxxxxx

            xx xxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxx xxxxxxxxxxxxxxxxxx x
                xxxxxxxx xxxxxxxxx x xxx xxxxxxxxxxxxxxxxxxxxx

                xxx xxxx x x xx x x xxxxxxxxxxxxxxxxx xxxx
                    xxxxxxxxxxxx x xxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

                xxxxx x xxxxxxxxxx
            x
            xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxx
        x
        xx [/pro] */
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
            result = (T) getDate(ctx.configuration().dialect(), ctx.resultSet(), ctx.index());
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
        else if (type == Long.class) {
            result = (T) wasNull(ctx.resultSet(), Long.valueOf(ctx.resultSet().getLong(ctx.index())));
        }
        else if (type == Short.class) {
            result = (T) wasNull(ctx.resultSet(), Short.valueOf(ctx.resultSet().getShort(ctx.index())));
        }
        else if (type == String.class) {
            result = (T) ctx.resultSet().getString(ctx.index());
        }
        else if (type == Time.class) {
            result = (T) getTime(ctx.configuration().dialect(), ctx.resultSet(), ctx.index());
        }
        else if (type == Timestamp.class) {
            result = (T) getTimestamp(ctx.configuration().dialect(), ctx.resultSet(), ctx.index());
        }
        else if (type == YearToMonth.class) {
            if (ctx.configuration().dialect() == POSTGRES) {
                Object object = ctx.resultSet().getObject(ctx.index());
                result = (T) (object == null ? null : PostgresUtils.toYearToMonth(object));
            }
            else {
                String string = ctx.resultSet().getString(ctx.index());
                result = (T) (string == null ? null : YearToMonth.valueOf(string));
            }
        }
        else if (type == DayToSecond.class) {
            if (ctx.configuration().dialect() == POSTGRES) {
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
            switch (ctx.configuration().dialect().family()) {

                // [#1624] Some JDBC drivers natively support the
                // java.util.UUID data type
                case H2:
                case POSTGRES: {
                    result = (T) ctx.resultSet().getObject(ctx.index());
                    break;
                }

                /* [pro] xx
                xx xxxxx xxx xxxxxxxx xxxx xxxx xxxxx xx xx xxxx xxxx xxxxxxxx
                xx xxxx xx xxxx xxxxxxxxxx xxxxxxx xxxx xxxxxxxxxxxxxxxxxx
                xxxx xxxxxxxxxx
                xxxx xxxxxxx

                xx [/pro] */
                // Most databases don't have such a type. In this case, jOOQ
                // simulates the type
                default: {
                    result = (T) Convert.convert(ctx.resultSet().getString(ctx.index()), UUID.class);
                    break;
                }
            }
        }

        // The type byte[] is handled earlier. byte[][] can be handled here
        else if (type.isArray()) {
            switch (ctx.configuration().dialect()) {
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
        /* [pro] xx
        xxxx xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx x
            xxxxxx x xxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxx xxxxxxx xxxxxxxxxxxxxxxx xxxxxx
        x
        xx [/pro] */
        else if (EnumType.class.isAssignableFrom(type)) {
            result = getEnumType(type, ctx.resultSet().getString(ctx.index()));
        }
        else if (UDTRecord.class.isAssignableFrom(type)) {
            switch (ctx.configuration().dialect()) {
                case POSTGRES:
                    result = (T) pgNewUDTRecord(type, ctx.resultSet().getObject(ctx.index()));
                    break;

                default:
                    result = (T) ctx.resultSet().getObject(ctx.index(), DataTypes.udtRecords());
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

        ctx.value(converter.from(result));
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
            if (ctx.configuration().dialect() == POSTGRES) {
                Object object = ctx.statement().getObject(ctx.index());
                result = (T) (object == null ? null : PostgresUtils.toYearToMonth(object));
            }
            else {
                String string = ctx.statement().getString(ctx.index());
                result = (T) (string == null ? null : YearToMonth.valueOf(string));
            }
        }
        else if (type == DayToSecond.class) {
            if (ctx.configuration().dialect() == POSTGRES) {
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
            switch (ctx.configuration().dialect().family()) {

                // [#1624] Some JDBC drivers natively support the
                // java.util.UUID data type
                case H2:
                case POSTGRES: {
                    result = (T) ctx.statement().getObject(ctx.index());
                    break;
                }

                /* [pro] xx
                xx xxxxx xxx xxxxxxxx xxxx xxxx xxxxx xx xx xxxx xxxx xxxxxxxx
                xx xxxx xx xxxx xxxxxxxxxx xxxxxxx xxxx xxxxxxxxxxxxxxxxxx
                xxxx xxxxxxxxxx
                xxxx xxxxxxx

                xx [/pro] */
                // Most databases don't have such a type. In this case, jOOQ
                // simulates the type
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
        /* [pro] xx
        xxxx xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx x
            xxxxxx x xxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxx xxxxxxx xxxxxxxxxxxxxxxx xxxxxx
        x
        xx [/pro] */
        else if (EnumType.class.isAssignableFrom(type)) {
            result = getEnumType(type, ctx.statement().getString(ctx.index()));
        }
        else if (UDTRecord.class.isAssignableFrom(type)) {
            switch (ctx.configuration().dialect()) {
                case POSTGRES:
                    result = (T) pgNewUDTRecord(type, ctx.statement().getObject(ctx.index()));
                    break;

                default:
                    result = (T) ctx.statement().getObject(ctx.index(), DataTypes.udtRecords());
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

        ctx.value(converter.from(result));
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
        /* [pro] xx
        xxxx xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx x
            xxxxxx x xxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxx xxxxxxx xxxxxxxxxxxxxxxx xxxxxx
        x
        xx [/pro] */
        else if (EnumType.class.isAssignableFrom(type)) {
            result = getEnumType(type, ctx.input().readString());
        }
        else if (UDTRecord.class.isAssignableFrom(type)) {
            result = (T) ctx.input().readObject();
        }
        else {
            result = (T) unlob(ctx.input().readObject());
        }

        ctx.value(converter.from(result));
    }




    /* [pro] xx
    xxxxxxx xxxxxx xxxxx xxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxx xxxxxx xxxxxxx xxxxxxx xxxxxxxxxxxxxxx xxxxx xxxxxx xxxxxxxxxxxx x
        xx xxxxxx xx xxxxx x
            xxxxxx xxxxx
        x
        xxxx x
            xx xxxxx xxxxxx xxx xxxxx xxxxxx xxxx xxxx xxxxxxx
            xxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxx
        x
    x

    xxxxxx xxxxx xxx xxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxx xxxxxxx xxxxx xxxxxxx xxxxxx xxxxxxxxxxxx x
        xx xxxxxxx xx xxxxx x
            xxxxxxxxxxxxxxxx xxxxxx
        x
        xxxx x
            xx xxxxxx xxxxx xxxxxx xxxx xx xxxxxx xx xxxx xxxxx xx xxxxxx
            xx xxxxxx xxxxx xxxx xxxx xx xxxx xx xxxxxxx xxxx xx xxxxxx xx
            xx xxxxxxxxx xxxxxxx xxx xxxxxx xxxxxxx
            xxxxxxxx xxxxx x xxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
            xxx xxxxxxxxx x xxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxx

            xxx xxxx x x xx x x xxxxxxxxxxxxx xxxx
                xxxxxxxxxxxx x xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

            xxxxxxxxxxxxxxxxxxxxxx
            xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        x

        xxxxxx xxxxxxx
    x

    xx [/pro] */
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
        /* [pro] xx
        xxxx xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx x
            xx xxx xxxxxxxxx
        x
        xx [/pro] */
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
    private static final <T> T pgGetArray(Scope ctx, ResultSet rs, Class<T> type, int index) throws SQLException {

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
                while (rs.next()) {
                    DefaultBindingGetResultSetContext<T> out = new DefaultBindingGetResultSetContext<T>(ctx.configuration(), rs, 2);
                    new DefaultBinding<T, T>(new IdentityConverter<T>((Class<T>) type.getComponentType()), false).get(out);
                    result.add(out.value());
                }
            }

            // That might fail too, then we don't know any further...
            catch (Exception fatal) {
                log.error("Cannot parse Postgres array: " + rs.getString(index));
                log.error(fatal);
                return null;
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
}

