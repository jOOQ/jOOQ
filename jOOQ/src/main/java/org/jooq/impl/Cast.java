/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
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
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.SQLDataType.BOOLEAN;
import static org.jooq.impl.SQLDataType.DOUBLE;
import static org.jooq.impl.SQLDataType.FLOAT;
import static org.jooq.impl.SQLDataType.REAL;
import static org.jooq.impl.SQLDataType.VARCHAR;

import org.jooq.BindContext;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.RenderContext;
import org.jooq.RenderContext.CastMode;
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

    private final DataType<T> getSQLDataType() {
        return getDataType().getSQLDataType();
    }

    @SuppressWarnings("unchecked")
    @Override
    public final void toSQL(RenderContext context) {
        // Avoid casting bind values inside an explicit cast...
        CastMode castMode = context.castMode();

        if (context.configuration().dialect() == SQLDialect.DERBY) {

            // [#857] Interestingly, Derby does not allow for casting numeric
            // types directly to VARCHAR. An intermediary cast to CHAR is needed
            if (field.getDataType().isNumeric() &&
                VARCHAR.equals(getSQLDataType())) {

                context.keyword("trim(cast(")
                       .keyword("cast(")
                       .castMode(CastMode.NEVER)
                       .sql(field)
                       .castMode(castMode)
                       .keyword(" as char(38))")
                       .keyword(" as ")
                       .keyword(getDataType(context.configuration()).getCastTypeName(context.configuration()))
                       .sql("))");

                return;
            }

            // [#888] ... neither does casting character types to FLOAT (and similar)
            else if (field.getDataType().isString() &&
                     asList(FLOAT, DOUBLE, REAL).contains(getSQLDataType())) {

                context.keyword("cast(")
                       .keyword("cast(")
                       .castMode(CastMode.NEVER)
                       .sql(field)
                       .castMode(castMode)
                       .keyword(" as decimal)")
                       .keyword(" as ")
                       .keyword(getDataType(context.configuration()).getCastTypeName(context.configuration()))
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
        context.keyword("cast(")
               .castMode(CastMode.NEVER)
               .sql(field)
               .castMode(castMode)
               .keyword(" as ")
               .keyword(getDataType(context.configuration()).getCastTypeName(context.configuration()))
               .sql(")");
    }

    @SuppressWarnings("unchecked")
    private Field<Boolean> asDecodeNumberToBoolean() {

        // [#859] 0 => false, null => null, all else is true
        return DSL.decode().value((Field<Integer>) field)
                               .when(inline(0), inline(false))
                               .when(inline((Integer) null), inline((Boolean) null))
                               .otherwise(inline(true));
    }

    @SuppressWarnings("unchecked")
    private Field<Boolean> asDecodeVarcharToBoolean() {
        Field<String> s = (Field<String>) field;

        // [#859] '0', 'f', 'false' => false, null => null, all else is true
        return DSL.decode().when(s.equal(inline("0")), inline(false))
                               .when(DSL.lower(s).equal(inline("false")), inline(false))
                               .when(DSL.lower(s).equal(inline("f")), inline(false))
                               .when(s.isNull(), inline((Boolean) null))
                               .otherwise(inline(true));
    }

    @Override
    public final void bind(BindContext context) {
        if (context.configuration().dialect() == SQLDialect.DERBY) {

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
}
