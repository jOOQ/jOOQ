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
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
// ...
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
import static org.jooq.conf.ParamType.NAMED;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.using;
import static org.jooq.tools.StringUtils.leftPad;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

// ...
import org.jooq.BindContext;
import org.jooq.Context;
import org.jooq.Converter;
import org.jooq.DataType;
import org.jooq.EnumType;
import org.jooq.RenderContext;
import org.jooq.SQLDialect;
import org.jooq.Schema;
import org.jooq.UDTRecord;
import org.jooq.tools.StringUtils;
import org.jooq.types.Interval;

/**
 * @author Lukas Eder
 */
class Val<T> extends AbstractParam<T> {

    private static final long   serialVersionUID = 6807729087019209084L;
    private static final char[] HEX              = "0123456789abcdef".toCharArray();

    Val(T value, DataType<T> type) {
        super(value, type);
    }

    Val(T value, DataType<T> type, String paramName) {
        super(value, type, paramName);
    }

    // ------------------------------------------------------------------------
    // XXX: Field API
    // ------------------------------------------------------------------------

    @Override
    public void accept(Context<?> ctx) {
        if (ctx instanceof RenderContext)
            toSQL((RenderContext) ctx);
        else
            bind((BindContext) ctx);
    }

    @Override
    public final void toSQL(RenderContext context) {

        // Casting can be enforced or prevented
        switch (context.castMode()) {
            case NEVER:
                toSQL(context, value, getConverter());
                return;

            case ALWAYS:
                toSQLCast(context);
                return;

            case SOME:

                // This dialect must cast
                if (context.cast()) {
                    toSQLCast(context);
                }

                // In some cases, we should still cast
                else if (shouldCast(context)) {
                    toSQLCast(context);
                }
                else {
                    toSQL(context, value, getConverter());
                }

                return;
        }

        // See if we "should" cast, to stay on the safe side
        if (shouldCast(context)) {
            toSQLCast(context);
        }

        // Most RDBMS can infer types for bind values
        else {
            toSQL(context, value, getConverter());
        }
    }

    private final boolean shouldCast(RenderContext context) {

        // In default mode, casting is only done when parameters are NOT inlined
        if (!isInline(context)) {

            // Generated enums should not be cast...
            if (!(value instanceof EnumType)) {
                switch (context.configuration().dialect().family()) {

                    // These dialects can hardly detect the type of a bound constant.
                    /* [pro] xx
                    xxxx xxxx
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
        if (getDataType().isInterval()) {
            switch (context.configuration().dialect().family()) {
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
    private final void toSQLCast(RenderContext context) {
        DataType<T> dataType = getDataType(context.configuration());
        DataType<T> type = dataType.getSQLDataType();
        SQLDialect family = context.configuration().dialect().family();

        // [#822] Some RDBMS need precision / scale information on BigDecimals
        if (value != null && getType() == BigDecimal.class && asList(CUBRID, DERBY, FIREBIRD, HSQLDB).contains(family)) {

            // Add precision / scale on BigDecimals
            int scale = ((BigDecimal) value).scale();
            int precision = scale + ((BigDecimal) value).precision();

            // Firebird's max precision is 18
            if (family == FIREBIRD) {
                precision = Math.min(precision, 18);
            }

            toSQLCast(context, dataType, 0, precision, scale);
        }

        // [#1028] Most databases don't know an OTHER type (except H2, HSQLDB).
        else if (SQLDataType.OTHER == type) {

            // If the bind value is set, it can be used to derive the cast type
            if (value != null) {
                toSQLCast(context, DefaultDataType.getDataType(family, value.getClass()), 0, 0, 0);
            }

            // [#632] [#722] Current integration tests show that Ingres and
            // Sybase can do without casting in most cases.
            else if (asList().contains(family)) {
                context.sql(getBindVariable(context));
            }

            // Derby and DB2 must have a type associated with NULL. Use VARCHAR
            // as a workaround. That's probably not correct in all cases, though
            else {
                toSQLCast(context, DefaultDataType.getDataType(family, String.class), 0, 0, 0);
            }
        }

        // [#1029] Postgres generally doesn't need the casting. Only in the
        // above case where the type is OTHER
        // [#1125] Also with temporal data types, casting is needed some times
        // [#1130] TODO type can be null for ARRAY types, etc.
        else if (family == POSTGRES && (type == null || !type.isTemporal())) {
            toSQL(context, value, getConverter());
        }

        // [#1727] VARCHAR types should be cast to their actual lengths in some
        // dialects
        else if ((type == SQLDataType.VARCHAR || type == SQLDataType.CHAR) && asList(FIREBIRD).contains(family)) {
            toSQLCast(context, dataType, getValueLength(), 0, 0);
        }

        /* [pro] xx
        xx xxxxxxx xxxx xxxx xxxxx xxxxxx xxx xx xxxx xx xxx xxxxxx xx xxxx xxxxxxxx
        xxxx xx xxxxx xx xxxxxxxxxxxxxxxxxxxxxxx xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx x
            xxxxxxxxxxxxxxxxxx xxxxxxxxx xx xx xxx
        x
        xx [/pro] */

        // In all other cases, the bind variable can be cast normally
        else {
            toSQLCast(context, dataType, dataType.length(), dataType.precision(), dataType.scale());
        }
    }

    private final int getValueLength() {
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

    private final void toSQLCast(RenderContext context, DataType<?> type, int length, int precision, int scale) {
        context.keyword("cast").sql("(");
        toSQL(context, value, getConverter());
        context.sql(" ").keyword("as").sql(" ")
               .sql(type.length(length).precision(precision, scale).getCastTypeName(context.configuration()))
               .sql(")");
    }

    /**
     * Get a bind variable, depending on value of
     * {@link RenderContext#namedParams()}
     */
    private final String getBindVariable(RenderContext context) {
        if (context.paramType() == NAMED) {
            int index = context.nextIndex();

            if (StringUtils.isBlank(getParamName())) {
                return ":" + index;
            }
            else {
                return ":" + getName();
            }
        }
        else {
            return "?";
        }
    }

    /**
     * Inlining abstraction
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private final void toSQL(RenderContext context, Object val, Converter<?, T> converter) {
        SQLDialect family = context.configuration().dialect().family();

        // [#650] [#3108] Check first, if we have a converter for the supplied type
        Class<?> type = converter.fromType();
        val = ((Converter) converter).to(val);

        if (isInline(context)) {
            // [#2223] Some type-casts in this section may seem unnecessary, e.g.
            // ((Boolean) val).toString(). They have been put in place to avoid
            // accidental type confusions where type != val.getClass(), and thus
            // SQL injection may occur

            if (val == null) {
                context.keyword("null");
            }
            else if (type == Boolean.class) {

                // [#1153] Some dialects don't support boolean literals
                // TRUE and FALSE
                if (asList(FIREBIRD, SQLITE).contains(family)) {
                    context.sql(((Boolean) val) ? "1" : "0");
                }
                else {
                    context.keyword(((Boolean) val).toString());
                }
            }

            // [#1154] Binary data cannot always be inlined
            else if (type == byte[].class) {
                byte[] binary = (byte[]) val;

                if (asList().contains(family)) {
                    context.sql("0x")
                           .sql(convertBytesToHex(binary));
                }
                /* [pro] xx
                xxxx xx xxxxxxx xx xxxx x
                    xxxxxxxxxxxxxxxxxxxxxxx
                           xxxxxxxxxxx
                           xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                           xxxxxxxxxxx
                x
                xx [/pro] */
                else if (asList(DERBY, H2, HSQLDB, MARIADB, MYSQL, SQLITE).contains(family)) {
                    context.sql("X'")
                           .sql(convertBytesToHex(binary))
                           .sql("'");
                }
                else if (asList().contains(family)) {
                    context.keyword("hextoraw('")
                           .sql(convertBytesToHex(binary))
                           .sql("')");
                }
                else if (family == POSTGRES) {
                    context.sql("E'")
                           .sql(convertBytesToPostgresOctal(binary))
                           .keyword("'::bytea");
                }

                // This default behaviour is used in debug logging for dialects
                // that do not support inlining binary data
                else {
                    context.sql("X'")
                           .sql(convertBytesToHex(binary))
                           .sql("'");
                }
            }

            // Interval extends Number, so let Interval come first!
            else if (Interval.class.isAssignableFrom(type)) {
                context.sql("'")
                       .sql(escape(val))
                       .sql("'");
            }

            else if (Number.class.isAssignableFrom(type)) {
                context.sql(((Number) val).toString());
            }

            // [#1156] Date/Time data types should be inlined using JDBC
            // escape syntax
            else if (type == Date.class) {

                // The SQLite JDBC driver does not implement the escape syntax
                // [#1253] SQL Server and Sybase do not implement date literals
                if (asList(SQLITE).contains(family)) {
                    context.sql("'").sql(escape(val)).sql("'");
                }

                /* [pro] xx
                xxxx xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx x
                    xxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxx
                x
                xx [/pro] */

                // [#1253] Derby doesn't support the standard literal
                else if (family == DERBY) {
                    context.keyword("date('").sql(escape(val)).sql("')");
                }

                // Most dialects implement SQL standard date literals
                else {
                    context.keyword("date '").sql(escape(val)).sql("'");
                }
            }
            else if (type == Timestamp.class) {

                // The SQLite JDBC driver does not implement the escape syntax
                // [#1253] SQL Server and Sybase do not implement timestamp literals
                if (asList(SQLITE).contains(family)) {
                    context.sql("'").sql(escape(val)).sql("'");
                }

                /* [pro] xx
                xxxx xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx x
                    xxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxx
                x
                xx [/pro] */

                // [#1253] Derby doesn't support the standard literal
                else if (family == DERBY) {
                    context.keyword("timestamp('").sql(escape(val)).sql("')");
                }

                // CUBRID timestamps have no fractional seconds
                else if (family == CUBRID) {
                    context.keyword("datetime '").sql(escape(val)).sql("'");
                }

                // Most dialects implement SQL standard timestamp literals
                else {
                    context.keyword("timestamp '").sql(escape(val)).sql("'");
                }
            }
            else if (type == Time.class) {

                // The SQLite JDBC driver does not implement the escape syntax
                // [#1253] SQL Server and Sybase do not implement time literals
                if (asList(SQLITE).contains(family)) {
                    context.sql("'").sql(new SimpleDateFormat("HH:mm:ss").format((Time) val)).sql("'");
                }

                /* [pro] xx
                xxxx xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx x
                    xxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                x
                xx [/pro] */

                // [#1253] Derby doesn't support the standard literal
                else if (family == DERBY) {
                    context.keyword("time").sql("('").sql(escape(val)).sql("')");
                }

                /* [pro] xx
                xx xxxxxxx xxxxxx xxxxxxx xxxx xxxx xxxxxxxx
                xxxx xx xxxxxxx xx xxxxxxx x
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                x

                xx [/pro] */
                // Most dialects implement SQL standard time literals
                else {
                    context.keyword("time").sql(" '").sql(escape(val)).sql("'");
                }
            }
            else if (type.isArray()) {
                String separator = "";

                // H2 renders arrays as rows
                if (family == H2) {
                    context.sql("(");

                    for (Object o : ((Object[]) val)) {
                        context.sql(separator);
                        toSQL(context, o, new IdentityConverter(type.getComponentType()));
                        separator = ", ";
                    }

                    context.sql(")");
                }

                // By default, render HSQLDB / POSTGRES syntax
                else {
                    context.keyword("ARRAY");
                    context.sql("[");

                    for (Object o : ((Object[]) val)) {
                        context.sql(separator);
                        toSQL(context, o, new IdentityConverter(type.getComponentType()));
                        separator = ", ";
                    }

                    context.sql("]");

                    // [#3214] Some PostgreSQL array type literals need explicit casting
                    if (family == POSTGRES && EnumType.class.isAssignableFrom(type.getComponentType())) {
                        context.sql("::")
                               .keyword(DefaultDataType.getDataType(family, type).getCastTypeName(context.configuration()));
                    }
                }
            }
            /* [pro] xx
            xxxx xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx x
                xxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxx
            x
            xx [/pro] */
            else if (EnumType.class.isAssignableFrom(type)) {
                String literal = ((EnumType) val).getLiteral();

                if (literal == null) {
                    toSQL(context, val, new IdentityConverter(String.class));
                }
                else {
                    toSQL(context, val, new IdentityConverter(String.class));
                }
            }
            else if (UDTRecord.class.isAssignableFrom(type)) {
                context.sql("[UDT]");
            }

            // Known fall-through types:
            // - Blob, Clob (both not supported by jOOQ)
            // - String
            // - UUID
            else {
                context.sql("'")
                       .sql(escape(val), true)
                       .sql("'");
            }
        }

        // In Postgres, some additional casting must be done in some cases...
        else if (family == SQLDialect.POSTGRES) {

            // Postgres needs explicit casting for array types
            if (type.isArray() && byte[].class != type) {
                context.sql(getBindVariable(context));
                context.sql("::");
                context.keyword(DefaultDataType.getDataType(family, type).getCastTypeName(context.configuration()));
            }

            // ... and also for enum types
            else if (EnumType.class.isAssignableFrom(type)) {
                context.sql(getBindVariable(context));

                // [#968] Don't cast "synthetic" enum types (note, val can be null!)
                EnumType e = (EnumType) type.getEnumConstants()[0];
                Schema schema = e.getSchema();

                if (schema != null) {
                    context.sql("::");

                    schema = using(context.configuration()).map(schema);
                    if (schema != null && TRUE.equals(context.configuration().settings().isRenderSchema())) {
                        context.visit(schema);
                        context.sql(".");
                    }

                    context.visit(name(e.getName()));
                }
            }

            else {
                context.sql(getBindVariable(context));
            }
        }

        else {
            context.sql(getBindVariable(context));
        }
    }

    /**
     * Escape a string literal by replacing <code>'</code> by <code>''</code>
     */
    private final String escape(Object val) {
        return val.toString().replace("'", "''");
    }

    @Override
    public final void bind(BindContext context) {

        // [#1302] Bind value only if it was not explicitly forced to be inlined
        if (!isInline()) {
            context.bindValue(value, this);
        }
    }

    // ------------------------------------------------------------------------
    // XXX: Param API
    // ------------------------------------------------------------------------

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
}
