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
import static org.jooq.impl.Factory.literal;
import static org.jooq.impl.SQLDataType.BOOLEAN;
import static org.jooq.impl.SQLDataType.DOUBLE;
import static org.jooq.impl.SQLDataType.FLOAT;
import static org.jooq.impl.SQLDataType.REAL;
import static org.jooq.impl.SQLDataType.VARCHAR;

import java.util.List;

import org.jooq.Attachable;
import org.jooq.BindContext;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.RenderContext;
import org.jooq.SQLDialect;

/**
 * @author Lukas Eder
 */
class Cast<T> extends AbstractField<T> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -6776617606751542856L;

    private final Field<?>    field;

    public Cast(Field<?> field, DataType<T> type) {
        super("cast", type);

        this.field = field;
    }

    @Override
    public final List<Attachable> getAttachables() {
        return getAttachables(field);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final void toSQL(RenderContext context) {
        if (context.getDialect() == SQLDialect.DERBY) {

            // [#857] Interestingly, Derby does not allow for casting numeric
            // types directly to VARCHAR. An intermediary cast to CHAR is needed
            if (field.getDataType().isNumeric() &&
                VARCHAR.equals(getSQLDataType())) {

                context.sql("trim(cast(")
                       .sql("cast(")
                       .sql(field)
                       .sql(" as char(38))")
                       .sql(" as ")
                       .sql(getDataType(context).getCastTypeName(context))
                       .sql("))");

                return;
            }

            // [#888] ... neither does casting character types to FLOAT (and similar)
            else if (field.getDataType().isString() &&
                     asList(FLOAT, DOUBLE, REAL).contains(getSQLDataType())) {

                context.sql("cast(")
                       .sql("cast(")
                       .sql(field)
                       .sql(" as decimal)")
                       .sql(" as ")
                       .sql(getDataType(context).getCastTypeName(context))
                       .sql(")");

                return;
            }

            // [#859] ... neither does casting numeric types to BOOLEAN
            else if (field.getDataType().isNumeric() &&
                     BOOLEAN.equals(getSQLDataType())) {

                context.sql(asDecodeNumberToBoolean());
                return;
            }

            // [#859] ... neither does casting character types to BOOLEAN
            else if (field.getDataType().isString() &&
                     BOOLEAN.equals(getSQLDataType())) {

                context.sql(asDecodeVarcharToBoolean());
                return;
            }
        }

        // Default rendering, if no special case has applied yet
        context.sql("cast(")
               .sql(field)
               .sql(" as ")
               .sql(getDataType(context).getCastTypeName(context))
               .sql(")");
    }

    @SuppressWarnings("unchecked")
    private Field<Boolean> asDecodeNumberToBoolean() {

        // [#859] 0 => false, null => null, all else is true
        return Factory.decode().value((Field<Integer>) field)
                               .when(literal(0), literal(false))
                               .when(literal((Integer) null), literal((Boolean) null))
                               .otherwise(literal(true));
    }

    @SuppressWarnings("unchecked")
    private Field<Boolean> asDecodeVarcharToBoolean() {
        Field<String> s = (Field<String>) field;

        // [#859] '0', 'f', 'false' => false, null => null, all else is true
        return Factory.decode().when(s.equal(literal("'0'")), literal(false))
                               .when(Factory.lower(s).equal(literal("'false'")), literal(false))
                               .when(Factory.lower(s).equal(literal("'f'")), literal(false))
                               .when(s.isNull(), literal((Boolean) null))
                               .otherwise(literal(true));
    }

    @Override
    public final void bind(BindContext context) {
        if (context.getDialect() == SQLDialect.DERBY) {

            // [#859] casting numeric types to BOOLEAN
            if (field.getDataType().isNumeric() &&
                BOOLEAN.equals(getSQLDataType())) {

                context.bind(asDecodeNumberToBoolean());
                return;
            }

            // [#859] casting character types to BOOLEAN
            else if (field.getDataType().isString() &&
                     BOOLEAN.equals(getSQLDataType())) {

                context.bind(asDecodeVarcharToBoolean());
                return;
            }
        }

        context.bind(field);
    }

    @Override
    public final boolean isNullLiteral() {
        return field.isNullLiteral();
    }
}
