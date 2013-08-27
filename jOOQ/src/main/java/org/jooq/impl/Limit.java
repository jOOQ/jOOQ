/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 * 
 * This work is dual-licensed Open Source, under LGPL and jOOQ EULA
 * =============================================================================
 * You may freely choose which license applies to you. For more information 
 * about licensing, please visit http://www.jooq.org/licenses
 * 
 * LGPL:  
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 * 
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 * 
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details.
 * 
 * You should have received a copy of the jOOQ End User License Agreement
 * along with this library.
 * If not, see http://www.jooq.org/eula
 */
package org.jooq.impl;

import static org.jooq.RenderContext.CastMode.NEVER;
import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.val;

import org.jooq.BindContext;
import org.jooq.Clause;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Param;
import org.jooq.RenderContext;
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
    private Field<Integer>    offsetOrZero     = val(0);
    private Field<Integer>    offsetPlusOne    = val(1);
    private boolean           rendersParams;

    @Override
    public final void toSQL(RenderContext context) {
        ParamType paramType = context.paramType();
        CastMode castMode = context.castMode();

        switch (context.configuration().dialect()) {

            // True LIMIT / OFFSET support provided by the following dialects
            // -----------------------------------------------------------------
            case MARIADB:
            case MYSQL:    // No break
            case H2:       // No break
            case HSQLDB:   // No break
            case POSTGRES: // No break
            case SQLITE: {
                context.castMode(NEVER)
                       .formatSeparator()
                       .keyword("limit")
                       .sql(" ").visit(numberOfRows)
                       .sql(" ").keyword("offset")
                       .sql(" ").visit(offsetOrZero)
                       .castMode(castMode);

                break;
            }

            // LIMIT [offset], [limit] supported by CUBRID
            // -------------------------------------------
            case CUBRID: {
                context.castMode(NEVER)
                       .formatSeparator()
                       .keyword("limit")
                       .sql(" ").visit(offsetOrZero)
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
                       .sql(" ").visit(getLowerRownum().add(inline(1)))
                       .sql(" ").keyword("to")
                       .sql(" ").visit(getUpperRownum())
                       .castMode(castMode);

                break;
            }

            case DERBY:
            case SQLSERVER:
            case SQLSERVER2012: {

                // Casts are not supported here...
                context.castMode(NEVER)
                       .formatSeparator()
                       .keyword("offset")
                       .sql(" ").visit(offsetOrZero)
                       .sql(" ").keyword("rows fetch next")
                       .sql(" ").visit(numberOfRows)
                       .sql(" ").keyword("rows only")
                       .castMode(castMode);

                break;
            }

            case INGRES: {

                // INGRES doesn't allow bind variables in the
                // OFFSET m FETCH FIRST n ROWS ONLY clause
                context.paramType(INLINED)
                       .formatSeparator()
                       .keyword("offset")
                       .sql(" ").visit(offsetOrZero)
                       .sql(" ").keyword("fetch first")
                       .sql(" ").visit(numberOfRows)
                       .sql(" ").keyword("rows only")
                       .paramType(paramType);

                break;
            }

            // Nice TOP .. START AT support
            // ----------------------------
            case SYBASE: {
                context.paramType(INLINED)
                       .keyword("top")
                       .sql(" ").visit(numberOfRows)
                       .sql(" ").keyword("start at")
                       .sql(" ").visit(offsetPlusOne)
                       .paramType(paramType);

                break;
            }

            // Only "TOP" support provided by the following dialects.
            // "OFFSET" support is simulated with nested selects
            // -----------------------------------------------------------------
            case DB2: {
                if (offset != null) {
                    throw new DataAccessException("DB2 does not support offsets in FETCH FIRST ROWS ONLY clause");
                }

                // DB2 doesn't allow bind variables here. Casting is not needed.
                context.paramType(INLINED)
                       .formatSeparator()
                       .keyword("fetch first")
                       .sql(" ").visit(numberOfRows)
                       .sql(" ").keyword("rows only")
                       .paramType(paramType);

                break;
            }

            case ASE:
            case SQLSERVER2008: {
                if (offset != null) {
                    throw new DataAccessException("Offsets in TOP clause not supported");
                }

                // SQL Server and Sybase don't allow bind variables in the TOP n clause
                context.paramType(INLINED)
                       .keyword("top")
                       .sql(" ").visit(numberOfRows)
                       .paramType(paramType);

                break;
            }

            // A default implementation is necessary for hashCode() and toString()
            default: {
                context.castMode(NEVER)
                       .formatSeparator()
                       .keyword("limit")
                       .sql(" ").visit(numberOfRows)
                       .sql(" ").keyword("offset")
                       .sql(" ").visit(offsetOrZero)
                       .castMode(castMode);

                break;
            }
        }
    }

    @Override
    public final void bind(BindContext context) {
        switch (context.configuration().dialect()) {

            // OFFSET .. LIMIT support provided by the following dialects
            // ----------------------------------------------------------
            case DERBY:
            case SQLSERVER:
            case SQLSERVER2012: {
                context.visit(offsetOrZero);
                context.visit(numberOfRows);
                break;
            }

            // LIMIT .. OFFSET support provided by the following dialects
            // ----------------------------------------------------------
            case MARIADB:
            case MYSQL:
            case HSQLDB:
            case H2:
            case POSTGRES:
            case SQLITE: {
                context.visit(numberOfRows);
                context.visit(offsetOrZero);
                break;
            }

            // LIMIT [offset], [limit] supported by CUBRID
            // -------------------------------------------
            case CUBRID: {
                context.visit(offsetOrZero);
                context.visit(numberOfRows);
                break;
            }

            // No bind variables in the FIRST .. SKIP clause
            // ---------------------------------------------
            case FIREBIRD: {
                context.visit(getLowerRownum());
                context.visit(getUpperRownum());
                break;
            }

            // These dialects don't support bind variables at all
            case ASE:
            case INGRES: {
                break;
            }

            // No bind variables in the TOP .. START AT clause
            // -----------------------------------------------
            case SYBASE: {

                // TOP .. START AT clauses without bind variables
                if (!rendersParams) {
                }

                // With simulated OFFSETs, no break, fall through
                else {
                    context.visit(getLowerRownum());
                    context.visit(getUpperRownum());
                }

                break;
            }

            // These dialects don't allow bind variables in their TOP clauses
            // --------------------------------------------------------------
            case DB2:
            case SQLSERVER2008: {

                // TOP clauses without bind variables
                if (offset == null && !rendersParams) {
                }

                // With simulated OFFSETs, no break, fall through
                else {
                    context.visit(getLowerRownum());
                    context.visit(getUpperRownum());
                }

                break;
            }

            // Oracle knows no LIMIT or TOP clause, limits are always bound
            // ------------------------------------------------------------
            case ORACLE:
            case ORACLE10G:
            case ORACLE11G:
            case ORACLE12C: {

                // [#1020] With the ROWNUM filtering improvement, the upper
                // limit is bound before the lower limit
                context.visit(getUpperRownum());
                context.visit(getLowerRownum());
                break;
            }

            // [#2057] Bind the same values as rendered in toSQL() by default
            default: {
                context.visit(numberOfRows);
                context.visit(offsetOrZero);
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
     * The upper bound, such that ROW_NUMBER() <= getUpperRownum()
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
