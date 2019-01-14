/*
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

import static java.lang.Boolean.TRUE;
import static org.jooq.RenderContext.CastMode.NEVER;
// ...
import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.impl.DSL.one;
import static org.jooq.impl.DSL.val;
import static org.jooq.impl.DSL.zero;
import static org.jooq.impl.Keywords.K_FETCH_FIRST;
import static org.jooq.impl.Keywords.K_FETCH_NEXT;
import static org.jooq.impl.Keywords.K_FIRST;
import static org.jooq.impl.Keywords.K_LIMIT;
import static org.jooq.impl.Keywords.K_OFFSET;
import static org.jooq.impl.Keywords.K_PERCENT;
import static org.jooq.impl.Keywords.K_ROWS;
import static org.jooq.impl.Keywords.K_ROWS_ONLY;
import static org.jooq.impl.Keywords.K_ROWS_WITH_TIES;
import static org.jooq.impl.Keywords.K_SKIP;
import static org.jooq.impl.Keywords.K_START_AT;
import static org.jooq.impl.Keywords.K_TO;
import static org.jooq.impl.Keywords.K_TOP;
import static org.jooq.impl.Keywords.K_WITH_TIES;
import static org.jooq.impl.SQLDataType.BIGINT;

import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Param;
import org.jooq.RenderContext.CastMode;
import org.jooq.conf.ParamType;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.Tools.BooleanDataKey;

/**
 * @author Lukas Eder
 */
final class Limit extends AbstractQueryPart {

    /**
     * Generated UID
     */
    private static final long           serialVersionUID  = 2053741242981425602L;
    private static final Field<Integer> ZERO              = zero();
    private static final Field<Integer> ONE               = one();
    private static final Param<Integer> MAX               = DSL.inline(Integer.MAX_VALUE);

    private Field<?>                    numberOfRows;
    private Field<?>                    numberOfRowsOrMax = MAX;
    private Field<?>                    offset;
    private Field<?>                    offsetOrZero      = ZERO;
    private Field<?>                    offsetPlusOne     = ONE;
    private boolean                     rendersParams;
    private boolean                     withTies;




    @Override
    public final void accept(Context<?> ctx) {
        ParamType paramType = ctx.paramType();
        CastMode castMode = ctx.castMode();

        switch (ctx.dialect()) {

            // True LIMIT / OFFSET support provided by the following dialects
            // -----------------------------------------------------------------



























            // LIMIT [offset], [limit] supported by CUBRID
            // -------------------------------------------
            case CUBRID: {
                ctx.castMode(NEVER)
                   .formatSeparator()
                   .visit(K_LIMIT)
                   .sql(' ').visit(offsetOrZero)
                   .sql(", ").visit(numberOfRowsOrMax)
                   .castMode(castMode);

                break;
            }

            // ROWS .. TO ..
            // -------------
            case FIREBIRD:
            case FIREBIRD_2_5:
            case FIREBIRD_3_0: {
                ctx.castMode(NEVER)
                   .formatSeparator()
                   .visit(K_ROWS)
                   .sql(' ').visit(getLowerRownum().add(ONE))
                   .sql(' ').visit(K_TO)
                   .sql(' ').visit(getUpperRownum())
                   .castMode(castMode);

                break;
            }



















            case DERBY: {

                // Casts are not supported here...
                ctx.castMode(NEVER);




                    ctx.formatSeparator()
                       .visit(K_OFFSET)
                       .sql(' ').visit(offsetOrZero)
                       .sql(' ').visit(K_ROWS);

                if (!limitZero()) {
                    ctx.formatSeparator()
                       .visit(K_FETCH_NEXT).sql(' ').visit(numberOfRows);






                    ctx.sql(' ').visit(withTies ? K_ROWS_WITH_TIES : K_ROWS_ONLY);
                }

                ctx.castMode(castMode);
                break;
            }























































































            // [#4785] OFFSET cannot be without LIMIT
            case H2:
            case MARIADB:
            case MYSQL_5_7:
            case MYSQL_8_0:
            case MYSQL:
            case SQLITE: {
                ctx.castMode(NEVER)
                       .formatSeparator()
                       .visit(K_LIMIT)
                       .sql(' ').visit(numberOfRowsOrMax);

                if (!offsetZero())
                    ctx.formatSeparator()
                           .visit(K_OFFSET)
                           .sql(' ').visit(offsetOrZero);

                ctx.castMode(castMode);

                break;
            }

            // [#4785] OFFSET can be without LIMIT



            case HSQLDB:
            case POSTGRES:
            case POSTGRES_9_3:
            case POSTGRES_9_4:
            case POSTGRES_9_5:
            case POSTGRES_10:
            case POSTGRES_11:
                // No break

            // A default implementation is necessary for hashCode() and toString()
            default: {
                ctx.castMode(NEVER);

                if (!limitZero())
                    ctx.formatSeparator()
                           .visit(K_LIMIT)
                           .sql(' ').visit(numberOfRows);

                if (!offsetZero())
                    ctx.formatSeparator()
                           .visit(K_OFFSET)
                           .sql(' ').visit(offsetOrZero);

                ctx.castMode(castMode);
                break;
            }
        }
    }






















    /**
     * Whether this limit has a limit of zero
     */
    final boolean limitZero() {
        return numberOfRows == null;
    }

    /**
     * Whether this limit has a limit of one
     */
    final boolean limitOne() {
        return !limitZero()
            && !withTies()

            && numberOfRows instanceof Param
            && Long.valueOf(1L).equals(((Param<?>) numberOfRows).getValue());
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
    final Field<?> getLowerRownum() {
        return offsetOrZero;
    }

    /**
     * The upper bound, such that ROW_NUMBER() &lt;= getUpperRownum()
     */
    final Field<?> getUpperRownum() {
        return offsetOrZero.add(numberOfRowsOrMax);
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

    final void setOffset(Number offset) {
        if (offset.longValue() != 0L) {
            this.offset = val(offset.longValue(), BIGINT);
            this.offsetOrZero = this.offset;
            this.offsetPlusOne = val(offset.longValue() + 1L, BIGINT);
        }
    }

    final void setOffset(Param<? extends Number> offset) {
        this.offset = offset;
        this.offsetOrZero = offset;
        this.rendersParams = rendersParams |= offset.isInline();
    }

    final void setNumberOfRows(Number numberOfRows) {
        this.numberOfRows = val(numberOfRows.longValue(), SQLDataType.BIGINT);
        this.numberOfRowsOrMax = this.numberOfRows;
    }

    final void setNumberOfRows(Param<? extends Number> numberOfRows) {
        this.numberOfRows = numberOfRows;
        this.numberOfRowsOrMax = numberOfRows;
        this.rendersParams |= numberOfRows.isInline();
    }













    final void setWithTies(boolean withTies) {
        this.withTies = withTies;
    }

    final boolean withTies() {
        return withTies;
    }
}
