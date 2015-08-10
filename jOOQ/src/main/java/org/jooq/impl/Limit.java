/**
 * Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)
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

import static org.jooq.RenderContext.CastMode.NEVER;
import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.val;

import org.jooq.Clause;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Param;
import org.jooq.RenderContext.CastMode;
import org.jooq.conf.ParamType;
import org.jooq.exception.DataAccessException;

/**
 * @author Lukas Eder
 */
class Limit extends AbstractQueryPart {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 2053741242981425602L;

    private Field<Integer>    numberOfRows;
    private Field<Integer>    offset;
    private Field<Integer>    offsetOrZero     = inline(0);
    private Field<Integer>    offsetPlusOne    = inline(1);
    private boolean           rendersParams;

    @Override
    public final void accept(Context<?> context) {
        ParamType paramType = context.paramType();
        CastMode castMode = context.castMode();

        switch (context.dialect()) {

            // True LIMIT / OFFSET support provided by the following dialects
            // -----------------------------------------------------------------
            /* [pro] */
            case VERTICA: {

                // Prevent [Vertica][VJDBC](2013) ERROR: n of LIMIT n clause is not supported for expressions
                // It appears that Vertica doesn't support expressions in LIMIT .. OFFSET
                // clauses if they're placed in derived tables.
                if (context.subquery())
                    context.paramType(INLINED);

                context.castMode(NEVER)
                       .formatSeparator()
                       .keyword("limit")
                       .sql(' ').visit(numberOfRows);

                if (!offsetZero())
                    context.sql(' ').keyword("offset")
                           .sql(' ').visit(offsetOrZero);

                context.castMode(castMode);

                if (context.subquery())
                    context.paramType(paramType);

                break;
            }
            /* [/pro] */

            case MARIADB:
            case MYSQL:
            case H2:
            case HSQLDB:
            case POSTGRES:
            case POSTGRES_9_3:
            case POSTGRES_9_4:
            case POSTGRES_9_5:
            case SQLITE: {
                context.castMode(NEVER)
                       .formatSeparator()
                       .keyword("limit")
                       .sql(' ').visit(numberOfRows);

                if (!offsetZero())
                    context.sql(' ').keyword("offset")
                           .sql(' ').visit(offsetOrZero);

                context.castMode(castMode);

                break;
            }

            // LIMIT [offset], [limit] supported by CUBRID
            // -------------------------------------------
            case CUBRID: {
                context.castMode(NEVER)
                       .formatSeparator()
                       .keyword("limit")
                       .sql(' ').visit(offsetOrZero)
                       .sql(", ").visit(numberOfRows)
                       .castMode(castMode);

                break;
            }

            // ROWS .. TO ..
            // -------------
            case FIREBIRD: {
                context.castMode(NEVER)
                       .formatSeparator()
                       .keyword("rows")
                       .sql(' ').visit(getLowerRownum().add(inline(1)))
                       .sql(' ').keyword("to")
                       .sql(' ').visit(getUpperRownum())
                       .castMode(castMode);

                break;
            }

            /* [pro] */
            case SQLSERVER:
            case SQLSERVER2012:
            case SQLSERVER2014:
            /* [/pro] */
            case DERBY: {

                // Casts are not supported here...
                context.castMode(NEVER)
                       .formatSeparator()
                       .keyword("offset")
                       .sql(' ').visit(offsetOrZero)
                       .sql(' ').keyword("rows fetch next")
                       .sql(' ').visit(numberOfRows)
                       .sql(' ').keyword("rows only")
                       .castMode(castMode);

                break;
            }

            /* [pro] */
            case INGRES: {

                // INGRES doesn't allow bind variables in the
                // OFFSET m FETCH FIRST n ROWS ONLY clause
                context.paramType(INLINED)
                       .formatSeparator()
                       .keyword("offset")
                       .sql(' ').visit(offsetOrZero)
                       .sql(' ').keyword("fetch first")
                       .sql(' ').visit(numberOfRows)
                       .sql(' ').keyword("rows only")
                       .paramType(paramType);

                break;
            }

            // Nice SKIP .. FIRST support
            // ----------------------------
            case INFORMIX: {
                context.paramType(INLINED);

                if (!offsetZero())
                    context.keyword("skip")
                           .sql(' ').visit(offsetOrZero)
                           .sql(' ');

                context.keyword("first")
                       .sql(' ').visit(numberOfRows)
                       .paramType(paramType);

                break;
            }

            // Nice TOP .. START AT support
            // ----------------------------
            case SYBASE: {
                context.paramType(INLINED)
                       .keyword("top")
                       .sql(' ').visit(numberOfRows);

                if (!offsetZero())
                    context.sql(' ').keyword("start at")
                           .sql(' ').visit(offsetPlusOne);

                context.paramType(paramType);

                break;
            }

            // Only "TOP" support provided by the following dialects.
            // "OFFSET" support is emulated with nested selects
            // -----------------------------------------------------------------
            case DB2:
            case DB2_9:
            case DB2_10: {
                if (offset != null) {
                    throw new DataAccessException("DB2 does not support offsets in FETCH FIRST ROWS ONLY clause");
                }

                // DB2 doesn't allow bind variables here. Casting is not needed.
                context.paramType(INLINED)
                       .formatSeparator()
                       .keyword("fetch first")
                       .sql(' ').visit(numberOfRows)
                       .sql(' ').keyword("rows only")
                       .paramType(paramType);

                break;
            }

            case ACCESS:
            case ACCESS2013:
            case ASE:
            case SQLSERVER2008: {
                if (offset != null) {
                    throw new DataAccessException("Offsets in TOP clause not supported");
                }

                // SQL Server and Sybase don't allow bind variables in the TOP n clause
                context.paramType(INLINED)
                       .keyword("top")
                       .sql(' ').visit(numberOfRows)
                       .paramType(paramType);

                break;
            }

            /* [/pro] */
            // A default implementation is necessary for hashCode() and toString()
            default: {
                context.castMode(NEVER)
                       .formatSeparator()
                       .keyword("limit")
                       .sql(' ').visit(numberOfRows);


                if (!offsetZero())
                    context.sql(' ').keyword("offset")
                           .sql(' ').visit(offsetOrZero);

                context.castMode(castMode);

                break;
            }
        }
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return null;
    }

    /**
     * Whether this limit has an offset of zero
     */
    final boolean offsetZero() {
        return offset == null;
    }

    /**
     * The lower bound, such that ROW_NUMBER() > getLowerRownum()
     */
    final Field<Integer> getLowerRownum() {
        return offsetOrZero;
    }

    /**
     * The upper bound, such that ROW_NUMBER() &lt;= getUpperRownum()
     */
    final Field<Integer> getUpperRownum() {
        return offsetOrZero.add(numberOfRows);
    }

    /**
     * Whether this LIMIT clause is applicable. If <code>false</code>, then no
     * LIMIT clause should be rendered.
     */
    final boolean isApplicable() {
        return offset != null || numberOfRows != null;
    }

    /**
     * Whether this LIMIT clause renders {@link Param} objects. This indicates
     * to the <code>SELECT</code> statement, that it may need to prefer
     * <code>ROW_NUMBER()</code> filtering over a <code>TOP</code> clause to
     * allow for named parameters in the query, as the <code>TOP</code> clause
     * may not accept bind variables.
     */
    final boolean rendersParams() {
        return rendersParams;
    }

    final void setOffset(int offset) {
        if (offset != 0) {
            this.offset = val(offset);
            this.offsetOrZero = this.offset;
            this.offsetPlusOne = val(offset + 1);
        }
    }

    final void setOffset(Param<Integer> offset) {
        this.offset = offset;
        this.offsetOrZero = offset;
        this.rendersParams = true;
    }

    final void setNumberOfRows(int numberOfRows) {
        this.numberOfRows = val(numberOfRows);
    }

    final void setNumberOfRows(Param<Integer> numberOfRows) {
        this.numberOfRows = numberOfRows;
        this.rendersParams = true;
    }
}
