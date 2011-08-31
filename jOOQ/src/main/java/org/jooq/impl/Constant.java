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

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import org.jooq.Attachable;
import org.jooq.BindContext;
import org.jooq.DataType;
import org.jooq.EnumType;
import org.jooq.MasterDataType;
import org.jooq.RenderContext;

/**
 * @author Lukas Eder
 */
class Constant<T> extends AbstractField<T> {

    private static final long serialVersionUID = 6807729087019209084L;
    private final T           value;

    Constant(T value, DataType<T> type) {
        super("" + value, type);

        this.value = value;
    }

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
            if (!(value instanceof EnumType) && !(value instanceof MasterDataType)) {
                switch (context.getDialect()) {

                    // These dialects can hardly detect the type of a bound constant.
                    case DB2:
                    case DERBY:

                    // These dialects have some trouble, when they mostly get it right.
                    case H2:
                    case HSQLDB:

                    // [#722] TODO This is probably not entirely right.
                    case INGRES:

                    // [#632] Sybase needs explicit casting in very rare cases.
                    case SYBASE: {
                        toSQLCast(context);
                        return;
                    }
                }
            }
        }

        // Most RDBMS can handle constants as typeless literals
        FieldTypeHelper.toSQL(context, value, this);
    }

    /**
     * Render the bind variable including a cast, if necessary
     */
    private void toSQLCast(RenderContext context) {
        switch (context.getDialect()) {

            // [#822] Some RDBMS need precision / scale information on BigDecimals
            case DB2:
            case DERBY:
            case HSQLDB: {

                // Add precision / scale on BigDecimals
                if (getType() == BigDecimal.class) {
                    int scale = ((BigDecimal) value).scale();
                    int precision = scale + ((BigDecimal) value).precision();

                    context.sql("cast(? as ")
                           .sql(getDataType(context).getCastTypeName(context, precision, scale))
                           .sql(")");
                    break;
                }

                // No break, fall through
                else {
                }
            }

            // These dialects don't need precision / scale info on BigDecimals
            case H2:
            case INGRES:
            case SYBASE: {
                context.sql("cast(? as ")
                       .sql(getDataType(context).getCastTypeName(context))
                       .sql(")");
            }
        }
    }

    @Override
    public final void bind(BindContext context) throws SQLException {
        context.bindValue(value, getType());
    }

    @Override
    public final boolean isNullLiteral() {
        return value == null;
    }
}
