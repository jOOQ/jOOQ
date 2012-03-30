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

import static org.jooq.RenderContext.CastMode.NEVER;
import static org.jooq.impl.Factory.val;

import java.util.Collections;
import java.util.List;

import org.jooq.Attachable;
import org.jooq.BindContext;
import org.jooq.Field;
import org.jooq.Param;
import org.jooq.RenderContext;
import org.jooq.RenderContext.CastMode;
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
    private boolean           rendersParams;;

    @Override
    public final List<Attachable> getAttachables() {
        return Collections.emptyList();
    }

    @Override
    public final void toSQL(RenderContext context) {
        boolean inline = context.inline();
        CastMode castMode = context.castMode();

        switch (context.getDialect()) {

            // True LIMIT / OFFSET support provided by the following dialects
            // -----------------------------------------------------------------
            case MYSQL:    // No break
            case H2:       // No break
            case HSQLDB:   // No break
            case POSTGRES: // No break
            case SQLITE: {
                context.castMode(NEVER)
                       .formatSeparator()
                       .keyword("limit ")
                       .sql(numberOfRows)
                       .keyword(" offset ")
                       .sql(offsetOrZero)
                       .castMode(castMode);

                break;
            }

            // LIMIT [offset], [limit] supported by CUBRID
            // -------------------------------------------
            case CUBRID: {
                context.castMode(NEVER)
                       .formatSeparator()
                       .keyword("limit ")
                       .sql(offsetOrZero)
                       .sql(", ")
                       .sql(numberOfRows)
                       .castMode(castMode);

                break;
            }

            case DERBY: {

                // Casts are not supported here...
                context.castMode(NEVER)
                       .formatSeparator()
                       .keyword("offset ")
                       .sql(offsetOrZero)
                       .keyword(" rows fetch next ")
                       .sql(numberOfRows)
                       .keyword(" rows only")
                       .castMode(castMode);

                break;
            }

            case INGRES: {

                // INGRES doesn't allow bind variables in the
                // OFFSET m FETCH FIRST n ROWS ONLY clause
                context.inline(true)
                       .formatSeparator()
                       .keyword("offset ")
                       .sql(offsetOrZero)
                       .keyword(" fetch first ")
                       .sql(numberOfRows)
                       .keyword(" rows only")
                       .inline(inline);

                break;
            }

            // Nice TOP .. START AT support
            // ----------------------------
            case SYBASE: {
                context.inline(true)
                       .keyword("top ")
                       .sql(numberOfRows)
                       .keyword(" start at ")
                       .sql(offsetPlusOne)
                       .inline(inline);

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
                context.inline(true)
                       .formatSeparator()
                       .keyword("fetch first ")
                       .sql(numberOfRows)
                       .keyword(" rows only")
                       .inline(inline);

                break;
            }

            case ASE:
            case SQLSERVER: {
                if (offset != null) {
                    throw new DataAccessException("Offsets in TOP clause not supported");
                }

                // SQL Server and Sybase don't allow bind variables in the TOP n clause
                context.inline(true)
                       .keyword("top ")
                       .sql(numberOfRows)
                       .inline(inline);

                break;
            }

            // A default implementation is necessary for hashCode() and toString()
            default: {
                context.castMode(NEVER)
                       .formatSeparator()
                       .keyword("limit ")
                       .sql(numberOfRows)
                       .keyword(" offset ")
                       .sql(offsetOrZero)
                       .castMode(castMode);

                break;
            }
        }
    }

    @Override
    public final void bind(BindContext context) {
        switch (context.getDialect()) {

            // OFFSET .. LIMIT support provided by the following dialects
            // ----------------------------------------------------------
            case DERBY: {
                context.bind(offsetOrZero);
                context.bind(numberOfRows);
                break;
            }

            // LIMIT .. OFFSET support provided by the following dialects
            // ----------------------------------------------------------
            case MYSQL:
            case HSQLDB:
            case H2:
            case POSTGRES:
            case SQLITE: {
                context.bind(numberOfRows);
                context.bind(offsetOrZero);
                break;
            }

            // LIMIT [offset], [limit] supported by CUBRID
            // -------------------------------------------
            case CUBRID: {
                context.bind(offsetOrZero);
                context.bind(numberOfRows);
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
                    context.bind(getLowerRownum());
                    context.bind(getUpperRownum());
                }

                break;
            }

            // These dialects don't allow bind variables in their TOP clauses
            // --------------------------------------------------------------
            case DB2:
            case SQLSERVER: {

                // TOP clauses without bind variables
                if (offset == null && !rendersParams) {
                }

                // With simulated OFFSETs, no break, fall through
                else {
                    context.bind(getLowerRownum());
                    context.bind(getUpperRownum());
                }

                break;
            }

            // Oracle knows no LIMIT or TOP clause, limits are always bound
            // ------------------------------------------------------------
            case ORACLE: {
                context.bind(getLowerRownum());
                context.bind(getUpperRownum());
                break;
            }
        }
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
