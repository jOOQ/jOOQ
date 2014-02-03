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

            /* [pro] xx
            xxxx xxxxxxxxxx
            xxxx xxxxxxxxxxxxxx
            xx [/pro] */
            case DERBY: {

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

            /* [pro] xx
            xxxx xxxxxxx x

                xx xxxxxx xxxxxxx xxxxx xxxx xxxxxxxxx xx xxx
                xx xxxxxx x xxxxx xxxxx x xxxx xxxx xxxxxx
                xxxxxxxxxxxxxxxxxxxxxxxxxx
                       xxxxxxxxxxxxxxxxxx
                       xxxxxxxxxxxxxxxxxx
                       xxxxxx xxxxxxxxxxxxxxxxxxxxxx
                       xxxxxx xxxxxxxxxxxxxxxxx xxxxxxx
                       xxxxxx xxxxxxxxxxxxxxxxxxxxxx
                       xxxxxx xxxxxxxxxxxxxxxx xxxxxx
                       xxxxxxxxxxxxxxxxxxxxxx

                xxxxxx
            x

            xx xxxx xxx xx xxxxx xx xxxxxxx
            xx xxxxxxxxxxxxxxxxxxxxxxxxxxxx
            xxxx xxxxxxx x
                xxxxxxxxxxxxxxxxxxxxxxxxxx
                       xxxxxxxxxxxxxxx
                       xxxxxx xxxxxxxxxxxxxxxxxxxxxx
                       xxxxxx xxxxxxxxxxxxxxxxx xxxx
                       xxxxxx xxxxxxxxxxxxxxxxxxxxxxx
                       xxxxxxxxxxxxxxxxxxxxxx

                xxxxxx
            x

            xx xxxx xxxxx xxxxxxx xxxxxxxx xx xxx xxxxxxxxx xxxxxxxxx
            xx xxxxxxxx xxxxxxx xx xxxxxxxxx xxxx xxxxxx xxxxxxx
            xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
            xxxx xxxx
            xxxx xxxxxx
            xxxx xxxxxxx x
                xx xxxxxxx xx xxxxx x
                    xxxxx xxx xxxxxxxxxxxxxxxxxxxxxxxx xxxx xxx xxxxxxx xxxxxxx xx xxxxx xxxxx xxxx xxxx xxxxxxxxx
                x

                xx xxx xxxxxxx xxxxx xxxx xxxxxxxxx xxxxx xxxxxxx xx xxx xxxxxxx
                xxxxxxxxxxxxxxxxxxxxxxxxxx
                       xxxxxxxxxxxxxxxxxx
                       xxxxxxxxxxxxxxx xxxxxxx
                       xxxxxx xxxxxxxxxxxxxxxxxxxxxx
                       xxxxxx xxxxxxxxxxxxxxxx xxxxxx
                       xxxxxxxxxxxxxxxxxxxxxx

                xxxxxx
            x

            xxxx xxxxxxx
            xxxx xxxxxxxxxxx
            xxxx xxxx
            xxxx xxxxxxxxxxxxxx x
                xx xxxxxxx xx xxxxx x
                    xxxxx xxx xxxxxxxxxxxxxxxxxxxxxxxxxxxx xx xxx xxxxxx xxx xxxxxxxxxxxx
                x

                xx xxx xxxxxx xxx xxxxxx xxxxx xxxxx xxxx xxxxxxxxx xx xxx xxx x xxxxxx
                xxxxxxxxxxxxxxxxxxxxxxxxxx
                       xxxxxxxxxxxxxxx
                       xxxxxx xxxxxxxxxxxxxxxxxxxxxx
                       xxxxxxxxxxxxxxxxxxxxxx

                xxxxxx
            x

            xx [/pro] */
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
            /* [pro] xx
            xxxx xxxxxxxxxx
            xxxx xxxxxxxxxxxxxx
            xx [/pro] */
            case DERBY: {
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

            /* [pro] xx
            xx xxxxx xxxxxxxx xxxxx xxxxxxx xxxx xxxxxxxxx xx xxx
            xxxx xxxx
            xxxx xxxxxxx x
                xxxxxx
            x

            xx xx xxxx xxxxxxxxx xx xxx xxx xx xxxxx xx xxxxxx
            xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
            xxxx xxxxxxx x

                xx xxx xx xxxxx xx xxxxxxx xxxxxxx xxxx xxxxxxxxx
                xx xxxxxxxxxxxxxxxx x
                x

                xx xxxx xxxxxxxxx xxxxxxxx xx xxxxxx xxxx xxxxxxx
                xxxx x
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                x

                xxxxxx
            x

            xx xxxxx xxxxxxxx xxxxx xxxxx xxxx xxxxxxxxx xx xxxxx xxx xxxxxxx
            xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
            xxxx xxxxxxx
            xxxx xxxxxxxxxxx
            xxxx xxxx
            xxxx xxxxxx
            xxxx xxxxxxx
            xxxx xxxxxxxxxxxxxx x

                xx xxx xxxxxxx xxxxxxx xxxx xxxxxxxxx
                xx xxxxxxx xx xxxx xx xxxxxxxxxxxxxxx x
                x

                xx xxxx xxxxxxxxx xxxxxxxx xx xxxxxx xxxx xxxxxxx
                xxxx x
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                x

                xxxxxx
            x

            xx xxxxxx xxxxx xx xxxxx xx xxx xxxxxxx xxxxxx xxx xxxxxx xxxxx
            xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
            xxxx xxxxxxx
            xxxx xxxxxxxxxx
            xxxx xxxxxxxxxx
            xxxx xxxxxxxxxx x

                xx xxxxxxx xxxx xxx xxxxxx xxxxxxxxx xxxxxxxxxxxx xxx xxxxx
                xx xxxxx xx xxxxx xxxxxx xxx xxxxx xxxxx
                xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                xxxxxx
            x

            xx [/pro] */
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
