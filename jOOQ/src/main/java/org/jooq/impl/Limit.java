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
import static org.jooq.SQLDialect.H2;
// ...
import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.impl.DSL.one;
import static org.jooq.impl.DSL.val;
import static org.jooq.impl.DSL.zero;
import static org.jooq.impl.Internal.iadd;
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
// ...
import org.jooq.RenderContext.CastMode;
import org.jooq.conf.ParamType;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.Tools.BooleanDataKey;

/**
 * @author Lukas Eder
 */
final class Limit extends AbstractQueryPart {
    private static final Param<Integer> ZERO              = zero();
    private static final Param<Integer> ONE               = one();
    private static final Param<Integer> MAX               = DSL.inline(Integer.MAX_VALUE);

    Param<?>                            numberOfRows;
    private Param<?>                    numberOfRowsOrMax = MAX;
    Param<?>                            offset;
    private Param<?>                    offsetOrZero      = ZERO;
    private Param<?>                    offsetPlusOne     = ONE;
    boolean                             withTies;
    boolean                             percent;

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




































            case DERBY:
            case FIREBIRD:
            case H2:
            case POSTGRES: {

                // [#8415] For backwards compatibility reasons, we generate standard
                //         OFFSET .. FETCH syntax on H2 only when strictly needed
                if (ctx.family() == H2 && !withTies() && !percent())
                    acceptDefault(ctx, castMode);
                else
                    acceptStandard(ctx, castMode);

                break;
            }
































































































            // [#4785] OFFSET cannot be without LIMIT
            case MARIADB:






            case MYSQL:
            case SQLITE: {
                acceptDefaultLimitMandatory(ctx, castMode);
                break;
            }


















            default: {
                acceptDefault(ctx, castMode);
                break;
            }
        }
    }

    private final void acceptStandard(Context<?> ctx, CastMode castMode) {
        ctx.castMode(NEVER);

        if ( !offsetZero())
            ctx.formatSeparator()
               .visit(K_OFFSET)
               .sql(' ').visit(offsetOrZero)
               .sql(' ').visit(K_ROWS);

        if (!limitZero()) {
            ctx.formatSeparator()
               .visit(K_FETCH_NEXT).sql(' ').visit(numberOfRows);

            if (percent)
                ctx.sql(' ').visit(K_PERCENT);

            ctx.sql(' ').visit(withTies ? K_ROWS_WITH_TIES : K_ROWS_ONLY);
        }

        ctx.castMode(castMode);
    }

    private final void acceptDefault(Context<?> ctx, CastMode castMode) {
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
    }

    private void acceptDefaultLimitMandatory(Context<?> ctx, CastMode castMode) {
        ctx.castMode(NEVER)
           .formatSeparator()
           .visit(K_LIMIT)
           .sql(' ').visit(numberOfRowsOrMax);

        if (!offsetZero())
            ctx.formatSeparator()
               .visit(K_OFFSET)
               .sql(' ').visit(offsetOrZero);

        ctx.castMode(castMode);
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
            && !percent()
            && Long.valueOf(1L).equals(numberOfRows.getValue());
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
        return iadd(offsetOrZero, numberOfRowsOrMax);
    }

    /**
     * Whether this LIMIT clause is applicable. If <code>false</code>, then no
     * LIMIT clause should be rendered.
     */
    final boolean isApplicable() {
        return offset != null || numberOfRows != null;
    }

    final void setOffset(Number offset) {
        this.offset = val(offset.longValue(), BIGINT);
        this.offsetOrZero = this.offset;
        this.offsetPlusOne = val(offset.longValue() + 1L, BIGINT);
    }

    final void setOffset(Param<?> offset) {
        this.offset = offset;
        this.offsetOrZero = offset;
    }

    final void setNumberOfRows(Number numberOfRows) {
        this.numberOfRows = val(numberOfRows.longValue(), SQLDataType.BIGINT);
        this.numberOfRowsOrMax = this.numberOfRows;
    }

    final void setNumberOfRows(Param<?> numberOfRows) {
        this.numberOfRows = numberOfRows;
        this.numberOfRowsOrMax = numberOfRows;
    }

    final Long getNumberOfRows() {
        return Convert.convert((numberOfRows != null ? numberOfRows : numberOfRowsOrMax).getValue(), long.class);
    }

    final void setPercent(boolean percent) {
        this.percent = percent;
    }

    final boolean percent() {
        return percent;
    }

    final void setWithTies(boolean withTies) {
        this.withTies = withTies;
    }

    final boolean withTies() {
        return withTies;
    }

    final Limit from(Limit limit) {

        // [#9017] Take the lower number of two LIMIT clauses, maintaining
        //         inline flags and parameter names
        if (limit.numberOfRows != null)
            if (numberOfRows == null)
                this.setNumberOfRows(limit.numberOfRows);
            else
                this.setNumberOfRows(((Val<?>) limit.numberOfRows).copy(Math.min(getNumberOfRows(), limit.getNumberOfRows())));

        if (limit.offset != null)
            this.setOffset(limit.offset);

        this.setPercent(limit.percent);
        this.setWithTies(limit.withTies);

        return this;
    }

    final void clear() {
        offset = null;
        numberOfRows = null;
        withTies = false;
        percent = false;
    }
}
