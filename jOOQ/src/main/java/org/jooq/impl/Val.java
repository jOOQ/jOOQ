/**
 * Copyright (c) 2009-2011, Lukas Eder, lukas.eder@gmail.com
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
import static org.jooq.SQLDialect.DB2;
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.INGRES;
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.SYBASE;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jooq.ArrayRecord;
import org.jooq.Attachable;
import org.jooq.BindContext;
import org.jooq.DataType;
import org.jooq.EnumType;
import org.jooq.MasterDataType;
import org.jooq.Param;
import org.jooq.RenderContext;
import org.jooq.SQLDialect;
import org.jooq.UDTRecord;
import org.jooq.tools.StringUtils;

/**
 * @author Lukas Eder
 */
class Val<T> extends AbstractField<T> implements Param<T>, BindingProvider {

    private static final long serialVersionUID = 6807729087019209084L;
    private final String      paramName;
    private T                 value;

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
    // Field API
    // ------------------------------------------------------------------------

    @Override
    public final List<Attachable> getAttachables() {
        return Collections.emptyList();
    }

    @Override
    public final void toSQL(RenderContext context) {

        // Casting is only done when parameters are NOT inlined
        if (!context.inline()) {

            // Generated enums should not be cast...
            // The exception's exception
            if (!(getValue() instanceof EnumType) && !(getValue() instanceof MasterDataType)) {
                switch (context.getDialect()) {

                    // These dialects can hardly detect the type of a bound constant.
                    case DB2:
                    case DERBY:

                    // These dialects have some trouble, when they mostly get it right.
                    case H2:
                    case HSQLDB:

                    // [#722] TODO This is probably not entirely right.
                    case INGRES:

                    // [#1029] Postgres and [#632] Sybase need explicit casting 
                    // in very rare cases.
                    case POSTGRES:
                    case SYBASE: {
                        toSQLCast(context);
                        return;
                    }
                }
            }
        }

        // Most RDBMS can handle constants as typeless literals
        toSQL(context, getValue(), getType());
    }

    /**
     * Render the bind variable including a cast, if necessary
     */
    private void toSQLCast(RenderContext context) {

        // [#822] Some RDBMS need precision / scale information on BigDecimals
        if (getType() == BigDecimal.class && asList(DB2, DERBY, HSQLDB).contains(context.getDialect())) {

            // Add precision / scale on BigDecimals
            int scale = ((BigDecimal) getValue()).scale();
            int precision = scale + ((BigDecimal) getValue()).precision();

            toSQLCast(context, getDataType(context), precision, scale);
        }

        // [#1028] Most databases don't know an OTHER type (except H2, HSQLDB).
        else if (SQLDataType.OTHER == getDataType(context).getSQLDataType()) {

            // If the bind value is set, it can be used to derive the cast type
            if (value != null) {
                toSQLCast(context, FieldTypeHelper.getDataType(context.getDialect(), value.getClass()), 0, 0);
            }

            // [#632] [#722] Current integration tests show that Ingres and
            // Sybase can do without casting in most cases.
            else if (asList(INGRES, SYBASE).contains(context.getDialect())) {
                context.sql(getBindVariable(context));
            }

            // Derby and DB2 must have a type associated with NULL. Use VARCHAR
            // as a workaround. That's probably not correct in all cases, though
            else {
                toSQLCast(context, FieldTypeHelper.getDataType(context.getDialect(), String.class), 0, 0);
            }
        }

        // [#1029] Postgres generally doesn't need the casting. Only in the
        // above case where the type is OTHER
        else if (context.getDialect() == POSTGRES) {
            toSQL(context, getValue(), getType());
        }

        // In all other cases, the bind variable can be cast normally
        else {
            toSQLCast(context, getDataType(context), 0, 0);
        }
    }

    private void toSQLCast(RenderContext context, DataType<?> type, int precision, int scale) {
        context.sql("cast(")
               .sql(getBindVariable(context))
               .sql(" as ")
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
    private void toSQL(RenderContext context, Object val, Class<?> type) {
        if (context.inline()) {
            if (val == null) {
                context.sql("null");
            }
            else if (type == Boolean.class) {
                context.sql(val.toString());
            }
            else if (type == byte[].class) {
                context.sql("'")
                       .sql(Arrays.toString((byte[]) val).replace("'", "''"))
                       .sql("'");
            }
            else if (Number.class.isAssignableFrom(type)) {
                context.sql(val.toString());
            }
            else if (type.isArray()) {
                context.sql("ARRAY")
                       .sql(Arrays.toString((Object[]) val));
            }
            else if (ArrayRecord.class.isAssignableFrom(type)) {
                context.sql(val.toString());
            }
            else if (EnumType.class.isAssignableFrom(type)) {
                toSQL(context, ((EnumType) val).getLiteral());
            }
            else if (MasterDataType.class.isAssignableFrom(type)) {
                toSQL(context, ((MasterDataType<?>) val).getPrimaryKey());
            }
            else if (UDTRecord.class.isAssignableFrom(type)) {
                context.sql("[UDT]");
            }

            // Known fall-through types:
            // - Blob, Clob (both not supported by jOOQ)
            // - String
            // - java.util.Date subtypes
            else {
                context.sql("'")
                       .sql(val.toString().replace("'", "''"))
                       .sql("'");
            }
        }

        // In Postgres, some additional casting must be done in some cases...
        // TODO: Improve this implementation with [#215] (cast support)
        else if (context.getDialect() == SQLDialect.POSTGRES) {

            // Postgres needs explicit casting for array types
            if (type.isArray() && byte[].class != type) {
                context.sql(getBindVariable(context));
                context.sql("::");
                context.sql(FieldTypeHelper.getDataType(context.getDialect(), type).getCastTypeName(context));
            }

            // ... and also for enum types
            else if (EnumType.class.isAssignableFrom(type)) {
                context.sql(getBindVariable(context));

                // [#968] Don't cast "synthetic" enum types
                if (!StringUtils.isBlank(((EnumType) val).getName())) {
                    context.sql("::");
                    context.literal(((EnumType) val).getName());
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

    @Override
    public final void bind(BindContext context) {
        context.bindValue(getValue(), getType());
    }

    @Override
    public final boolean isNullLiteral() {
        return getValue() == null;
    }

    // ------------------------------------------------------------------------
    // Param API
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

    // ------------------------------------------------------------------------
    // BindingProvider API
    // ------------------------------------------------------------------------

    @Override
    public final List<Param<?>> getBindings() {
        return Arrays.<Param<?>>asList(this);
    }
}
