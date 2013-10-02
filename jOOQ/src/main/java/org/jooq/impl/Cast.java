/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
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

                context.keyword("trim").sql("(")
                           .keyword("cast").sql("(")
                               .keyword("cast").sql("(")
                                   .castMode(CastMode.NEVER)
                                   .visit(field)
                                   .castMode(castMode)
                                   .sql(" ").keyword("as").sql(" char(38))")
                               .sql(" ").keyword("as").sql(" ")
                               .keyword(getDataType(context.configuration()).getCastTypeName(context.configuration()))
                           .sql("))");

                return;
            }

            // [#888] ... neither does casting character types to FLOAT (and similar)
            else if (field.getDataType().isString() &&
                     asList(FLOAT, DOUBLE, REAL).contains(getSQLDataType())) {

                context.keyword("cast").sql("(")
                           .keyword("cast").sql("(")
                               .castMode(CastMode.NEVER)
                               .visit(field)
                               .castMode(castMode)
                               .sql(" ").keyword("as").sql(" ").keyword("decimal")
                           .sql(") ")
                           .keyword("as")
                           .sql(" ")
                           .keyword(getDataType(context.configuration()).getCastTypeName(context.configuration()))
                       .sql(")");

                return;
            }

            // [#859] ... neither does casting numeric types to BOOLEAN
            else if (field.getDataType().isNumeric() &&
                     BOOLEAN.equals(getSQLDataType())) {

                context.visit(asDecodeNumberToBoolean());
                return;
            }

            // [#859] ... neither does casting character types to BOOLEAN
            else if (field.getDataType().isString() &&
                     BOOLEAN.equals(getSQLDataType())) {

                context.visit(asDecodeVarcharToBoolean());
                return;
            }
        }

        // Default rendering, if no special case has applied yet
        context.keyword("cast").sql("(")
                   .castMode(CastMode.NEVER)
                   .visit(field)
                   .castMode(castMode)
                   .sql(" ").keyword("as").sql(" ")
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

                context.visit(asDecodeNumberToBoolean());
                return;
            }

            // [#859] casting character types to BOOLEAN
            else if (field.getDataType().isString() &&
                     BOOLEAN.equals(getSQLDataType())) {

                context.visit(asDecodeVarcharToBoolean());
                return;
            }
        }

        context.visit(field);
    }
}
