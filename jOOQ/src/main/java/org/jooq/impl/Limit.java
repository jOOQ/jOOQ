/**
 * Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
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
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: http://www.jooq.org/licenses
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
package org.jooq.impl;

import static org.jooq.RenderContext.CastMode.NEVER;
import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.one;
import static org.jooq.impl.DSL.val;
import static org.jooq.impl.DSL.zero;

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
    private static final long           serialVersionUID = 2053741242981425602L;
    private static final Field<Integer> ZERO             = zero();
    private static final Field<Integer> ONE              = one();

    private Field<Integer>              numberOfRows;
    private Field<Integer>              offset;
    private Field<Integer>              offsetOrZero     = ZERO;
    private Field<Integer>              offsetPlusOne    = ONE;
    private boolean                     rendersParams;

    @Override
    public final void accept(Context<?> context) {
        ParamType paramType = context.paramType();
        CastMode castMode = context.castMode();

        switch (context.dialect()) {

            // True LIMIT / OFFSET support provided by the following dialects
            // -----------------------------------------------------------------
            /* [pro] xx
            xxxx xxxxxxxx x

                xx xxxxxxx xxxxxxxxxxxxxxxxxxxxxx xxxxxx x xx xxxxx x xxxxxx xx xxx xxxxxxxxx xxx xxxxxxxxxxx
                xx xx xxxxxxx xxxx xxxxxxx xxxxxxx xxxxxxx xxxxxxxxxxx xx xxxxx xx xxxxxx
                xx xxxxxxx xx xxxxxxx xxxxxx xx xxxxxxx xxxxxxx
                xx xxxxxxxxxxxxxxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxx

                xxxxxxxxxxxxxxxxxxxxxxx
                       xxxxxxxxxxxxxxxxxx
                       xxxxxxxxxxxxxxxxx
                       xxxxxx xxxxxxxxxxxxxxxxxxxxxxx

                xx xxxxxxxxxxxxxxx
                    xxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxx
                           xxxxxx xxxxxxxxxxxxxxxxxxxxxxx

                xxxxxxxxxxxxxxxxxxxxxxxxxxx

                xx xxxxxxxxxxxxxxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxx

                xxxxxx
            x
            xx [/pro] */

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
                       .sql(' ').visit(getLowerRownum().add(inline(1, SQLDataType.INTEGER)))
                       .sql(' ').keyword("to")
                       .sql(' ').visit(getUpperRownum())
                       .castMode(castMode);

                break;
            }

            /* [pro] xx
            xxxx xxxxxxxxxx
            xxxx xxxxxxxxxxxxxx
            xxxx xxxxxxxxxxxxxx
            xx [/pro] */
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

            xx xxxx xxxx xx xxxxx xxxxxxx
            xx xxxxxxxxxxxxxxxxxxxxxxxxxxxx
            xxxx xxxxxxxxx x
                xxxxxxxxxxxxxxxxxxxxxxxxxxx

                xx xxxxxxxxxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxx
                           xxxxxx xxxxxxxxxxxxxxxxxxxxxx
                           xxxxxx xxx

                xxxxxxxxxxxxxxxxxxxxxxxx
                       xxxxxx xxxxxxxxxxxxxxxxxxxxxx
                       xxxxxxxxxxxxxxxxxxxxxx

                xxxxxx
            x

            xx xxxx xxx xx xxxxx xx xxxxxxx
            xx xxxxxxxxxxxxxxxxxxxxxxxxxxxx
            xxxx xxxxxxx x
                xxxxxxxxxxxxxxxxxxxxxxxxxx
                       xxxxxxxxxxxxxxx
                       xxxxxx xxxxxxxxxxxxxxxxxxxxxxx

                xx xxxxxxxxxxxxxxx
                    xxxxxxxxxxxxx xxxxxxxxxxxxxxxxx xxxx
                           xxxxxx xxxxxxxxxxxxxxxxxxxxxxxx

                xxxxxxxxxxxxxxxxxxxxxxxxxxxxx

                xxxxxx
            x

            xx xxxx xxxxx xxxxxxx xxxxxxxx xx xxx xxxxxxxxx xxxxxxxxx
            xx xxxxxxxx xxxxxxx xx xxxxxxxx xxxx xxxxxx xxxxxxx
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
            this.offset = val(offset, SQLDataType.INTEGER);
            this.offsetOrZero = this.offset;
            this.offsetPlusOne = val(offset + 1, SQLDataType.INTEGER);
        }
    }

    final void setOffset(Param<Integer> offset) {
        this.offset = offset;
        this.offsetOrZero = offset;
        this.rendersParams = true;
    }

    final void setNumberOfRows(int numberOfRows) {
        this.numberOfRows = val(numberOfRows, SQLDataType.INTEGER);
    }

    final void setNumberOfRows(Param<Integer> numberOfRows) {
        this.numberOfRows = numberOfRows;
        this.rendersParams = true;
    }
}
