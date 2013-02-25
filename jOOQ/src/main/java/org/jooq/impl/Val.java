/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
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

import static java.util.Arrays.asList;
import static org.jooq.SQLDialect.ASE;
import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.SQLDialect.DB2;
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.INGRES;
import static org.jooq.SQLDialect.MYSQL;
import static org.jooq.SQLDialect.ORACLE;
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.SQLITE;
import static org.jooq.SQLDialect.SQLSERVER;
import static org.jooq.SQLDialect.SYBASE;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Arrays;

import org.jooq.ArrayRecord;
import org.jooq.BindContext;
import org.jooq.Converter;
import org.jooq.DataType;
import org.jooq.EnumType;
import org.jooq.Param;
import org.jooq.RenderContext;
import org.jooq.SQLDialect;
import org.jooq.UDTRecord;
import org.jooq.tools.StringUtils;
import org.jooq.types.Interval;

/**
 * @author Lukas Eder
 */
class Val<T> extends AbstractField<T> implements Param<T> {

    private static final long serialVersionUID = 6807729087019209084L;

    private final String      paramName;
    private T                 value;
    private boolean           inline;

    Val(T value, DataType<T> type) {
        this(value, type, null);
    }

    Val(T value, DataType<T> type, String paramName) {
        super(name(value, paramName), type);

        this.paramName = paramName;
        this.value = value;
    }

    private static String name(Object value, String paramName) {
        return paramName == null ? String.valueOf(value) : paramName;
    }

    // ------------------------------------------------------------------------
    // XXX: Field API
    // ------------------------------------------------------------------------

    @Override
    public final void toSQL(RenderContext context) {

        // Casting can be enforced or prevented
        switch (context.castMode()) {
            case NEVER:
                toSQL(context, getValue(), getType());
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
                    toSQL(context, getValue(), getType());
                }

                return;
        }

        // See if we "should" cast, to stay on the safe side
        if (shouldCast(context)) {
            toSQLCast(context);
        }

        // Most RDBMS can infer types for bind values
        else {
            toSQL(context, getValue(), getType());
        }
    }

    @SuppressWarnings("deprecation")
    private boolean shouldCast(RenderContext context) {

        // In default mode, casting is only done when parameters are NOT inlined
        if (!isInline(context)) {

            // Generated enums should not be cast...
            if (!(getValue() instanceof EnumType) && !(getValue() instanceof org.jooq.MasterDataType)) {
                switch (context.getDialect()) {

                    // These dialects can hardly detect the type of a bound constant.
                    case DB2:
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
                    case POSTGRES:
                    case SYBASE: {
                        return true;
                    }
                }
            }
        }

        // [#566] JDBC doesn't explicitly support interval data types. To be on
        // the safe side, always cast these types in those dialects that support
        // them
        if (getDataType().isInterval()) {
            switch (context.getDialect()) {
                case ORACLE:
                case POSTGRES:
                    return true;
            }
        }

        return false;
    }

    /**
     * Render the bind variable including a cast, if necessary
     */
    private void toSQLCast(RenderContext context) {
        SQLDataType<T> type = getDataType(context).getSQLDataType();
        SQLDialect dialect = context.getDialect();

        // [#822] Some RDBMS need precision / scale information on BigDecimals
        if (getValue() != null && getType() == BigDecimal.class && asList(CUBRID, DB2, DERBY, FIREBIRD, HSQLDB).contains(dialect)) {

            // Add precision / scale on BigDecimals
            int scale = ((BigDecimal) getValue()).scale();
            int precision = scale + ((BigDecimal) getValue()).precision();

            // Firebird's max precision is 18
            if (dialect == FIREBIRD) {
                precision = Math.min(precision, 18);
            }

            toSQLCast(context, getDataType(context), precision, scale);
        }

        // [#1028] Most databases don't know an OTHER type (except H2, HSQLDB).
        else if (SQLDataType.OTHER == type) {

            // If the bind value is set, it can be used to derive the cast type
            if (value != null) {
                toSQLCast(context, FieldTypeHelper.getDataType(dialect, value.getClass()), 0, 0);
            }

            // [#632] [#722] Current integration tests show that Ingres and
            // Sybase can do without casting in most cases.
            else if (asList(INGRES, SYBASE).contains(dialect)) {
                context.sql(getBindVariable(context));
            }

            // Derby and DB2 must have a type associated with NULL. Use VARCHAR
            // as a workaround. That's probably not correct in all cases, though
            else {
                toSQLCast(context, FieldTypeHelper.getDataType(dialect, String.class), 0, 0);
            }
        }

        // [#1029] Postgres generally doesn't need the casting. Only in the
        // above case where the type is OTHER
        // [#1125] Also with temporal data types, casting is needed some times
        // [#1130] TODO type can be null for ARRAY types, etc.
        else if (dialect == POSTGRES && (type == null || !type.isTemporal())) {
            toSQL(context, getValue(), getType());
        }

        // [#1727] VARCHAR types should be cast to their actual lengths in some
        // dialects
        else if ((type == SQLDataType.VARCHAR || type == SQLDataType.CHAR) && asList(FIREBIRD).contains(dialect)) {
            toSQLCast(context, getDataType(context), getValueLength());
        }

        // In all other cases, the bind variable can be cast normally
        else {
            toSQLCast(context, getDataType(context), 0, 0);
        }
    }

    private int getValueLength() {
        String string = (String) getValue();
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

    private void toSQLCast(RenderContext context, DataType<?> type, int length) {
        context.keyword("cast(");
        toSQL(context, getValue(), getType());
        context.keyword(" as ")
               .sql(type.getCastTypeName(context, length))
               .sql(")");
    }

    private void toSQLCast(RenderContext context, DataType<?> type, int precision, int scale) {
        context.keyword("cast(");
        toSQL(context, getValue(), getType());
        context.keyword(" as ")
               .sql(type.getCastTypeName(context, precision, scale))
               .sql(")");
    }

    /**
     * Get a bind variable, depending on value of
     * {@link RenderContext#namedParams()}
     */
    private final String getBindVariable(RenderContext context) {
        if (context.namedParams()) {
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
    private void toSQL(RenderContext context, Object val) {
        if (val == null) {
            toSQL(context, val, Object.class);
        }
        else {
            toSQL(context, val, val.getClass());
        }
    }

    /**
     * Inlining abstraction
     */
    @SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
    private void toSQL(RenderContext context, Object val, Class<?> type) {
        SQLDialect dialect = context.getDialect();

        // [#650] Check first, if we have a converter for the supplied type
        Converter<?, ?> converter = DataTypes.converter(type);
        if (converter != null) {
            val = ((Converter) converter).to(val);
            type = converter.fromType();
        }

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
                if (asList(ASE, DB2, FIREBIRD, ORACLE, SQLSERVER, SQLITE, SYBASE).contains(dialect)) {
                    context.sql(((Boolean) val) ? "1" : "0");
                }
                else {
                    context.keyword(((Boolean) val).toString());
                }
            }

            // [#1154] Binary data cannot always be inlined
            else if (type == byte[].class) {
                byte[] binary = (byte[]) val;

                if (asList(ASE, SQLSERVER, SYBASE).contains(dialect)) {
                    context.sql("0x")
                           .sql(Utils.convertBytesToHex(binary));
                }
                else if (dialect == DB2) {
                    context.keyword("blob")
                           .sql("(X'")
                           .sql(Utils.convertBytesToHex(binary))
                           .sql("')");
                }
                else if (asList(DERBY, H2, HSQLDB, INGRES, MYSQL, SQLITE).contains(dialect)) {
                    context.sql("X'")
                           .sql(Utils.convertBytesToHex(binary))
                           .sql("'");
                }
                else if (asList(ORACLE).contains(dialect)) {
                    context.keyword("hextoraw('")
                           .sql(Utils.convertBytesToHex(binary))
                           .sql("')");
                }
                else if (dialect == POSTGRES) {
                    context.sql("E'")
                           .sql(Utils.convertBytesToPostgresOctal(binary))
                           .keyword("'::bytea");
                }

                // This default behaviour is used in debug logging for dialects
                // that do not support inlining binary data
                else {
                    context.sql("X'")
                           .sql(Utils.convertBytesToHex(binary))
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
                if (asList(ASE, SQLITE, SQLSERVER, SYBASE).contains(dialect)) {
                    context.sql("'").sql(escape(val)).sql("'");
                }

                // [#1253] Derby doesn't support the standard literal
                else if (dialect == DERBY) {
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
                if (asList(ASE, SQLITE, SQLSERVER, SYBASE).contains(dialect)) {
                    context.sql("'").sql(escape(val)).sql("'");
                }

                // [#1253] Derby doesn't support the standard literal
                else if (dialect == DERBY) {
                    context.keyword("timestamp('").sql(escape(val)).sql("')");
                }

                // CUBRID timestamps have no fractional seconds
                else if (dialect == CUBRID) {
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
                if (asList(ASE, SQLITE, SQLSERVER, SYBASE).contains(dialect)) {
                    context.sql("'").sql(escape(val)).sql("'");
                }

                // [#1253] Derby doesn't support the standard literal
                else if (dialect == DERBY) {
                    context.keyword("time('").sql(escape(val)).sql("')");
                }

                // [#1253] Oracle doesn't know time literals
                else if (dialect == ORACLE) {
                    context.keyword("timestamp '1970-01-01 ").sql(escape(val)).sql("'");
                }

                // Most dialects implement SQL standard time literals
                else {
                    context.keyword("time '").sql(escape(val)).sql("'");
                }
            }
            else if (type.isArray()) {

                // H2 renders arrays as rows
                if (dialect == H2) {
                    context.sql(Arrays.toString((Object[]) val).replaceAll("\\[([^]]*)\\]", "($1)"));
                }

                // By default, render HSQLDB / POSTGRES syntax
                else {
                    context.keyword("ARRAY")
                           .sql(Arrays.toString((Object[]) val));
                }
            }
            else if (ArrayRecord.class.isAssignableFrom(type)) {
                context.sql(val.toString());
            }
            else if (EnumType.class.isAssignableFrom(type)) {
                toSQL(context, ((EnumType) val).getLiteral());
            }
            else if (org.jooq.MasterDataType.class.isAssignableFrom(type)) {
                toSQL(context, ((org.jooq.MasterDataType<?>) val).getPrimaryKey());
            }
            else if (UDTRecord.class.isAssignableFrom(type)) {
                context.sql("[UDT]");
            }

            // Known fall-through types:
            // - Blob, Clob (both not supported by jOOQ)
            // - String
            else {
                context.sql("'")
                       .sql(escape(val))
                       .sql("'");
            }
        }

        // In Postgres, some additional casting must be done in some cases...
        // TODO: Improve this implementation with [#215] (cast support)
        else if (dialect == SQLDialect.POSTGRES) {

            // Postgres needs explicit casting for array types
            if (type.isArray() && byte[].class != type) {
                context.sql(getBindVariable(context));
                context.sql("::");
                context.keyword(FieldTypeHelper.getDataType(dialect, type).getCastTypeName(context));
            }

            // ... and also for enum types
            else if (EnumType.class.isAssignableFrom(type)) {
                context.sql(getBindVariable(context));

                // [#968] Don't cast "synthetic" enum types (note, val can be null!)
                String name = ((EnumType) type.getEnumConstants()[0]).getName();
                if (!StringUtils.isBlank(name)) {
                    context.sql("::");
                    context.literal(name);
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
    private String escape(Object val) {
        return val.toString().replace("'", "''");
    }

    @Override
    public final void bind(BindContext context) {

        // [#1302] Bind value only if it was not explicitly forced to be inlined
        if (!isInline()) {
            context.bindValue(getValue(), getType());
        }
    }

    @Override
    public final boolean isNullLiteral() {
        return getValue() == null;
    }

    // ------------------------------------------------------------------------
    // XXX: Param API
    // ------------------------------------------------------------------------

    @Override
    public final void setValue(T value) {
        setConverted(value);
    }

    @Override
    public final void setConverted(Object value) {
        this.value = getDataType().convert(value);
    }

    @Override
    public final T getValue() {
        return value;
    }

    @Override
    public final String getParamName() {
        return paramName;
    }

    @Override
    public final void setInline(boolean inline) {
        this.inline = inline;
    }

    @Override
    public final boolean isInline() {
        return inline;
    }

    private final boolean isInline(RenderContext context) {
        return isInline() || context.inline();
    }
}
