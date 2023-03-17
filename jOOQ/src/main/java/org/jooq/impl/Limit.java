/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
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
 * For more information, please visit: https://www.jooq.org/legal/licensing
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
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.POSTGRES;
// ...
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
import static org.jooq.impl.Tools.isScalarSubquery;

import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Param;
// ...
import org.jooq.RenderContext.CastMode;
import org.jooq.conf.ParamType;
import org.jooq.impl.QOM.UTransient;
import org.jooq.impl.Tools.BooleanDataKey;


/**
 * @author Lukas Eder
 */
final class Limit extends AbstractQueryPart implements UTransient {

    private static final Lazy<Param<Integer>> ZERO          = Lazy.of(() -> zero());
    private static final Lazy<Param<Integer>> ONE           = Lazy.of(() -> one());
    private static final Lazy<Param<Integer>> MAX           = Lazy.of(() -> DSL.inline(Integer.MAX_VALUE));

    Field<? extends Number>                   limit;
    private Field<? extends Number>           limitOrMax    = MAX.get();
    Field<? extends Number>                   offset;
    private Field<? extends Number>           offsetOrZero  = ZERO.get();
    private Field<? extends Number>           offsetPlusOne = ONE.get();
    boolean                                   withTies;
    boolean                                   percent;

    @Override
    public final void accept(Context<?> ctx) {
        ParamType paramType = ctx.paramType();
        CastMode castMode = ctx.castMode();

        switch (ctx.family()) {

            // True LIMIT / OFFSET support provided by the following dialects
            // -----------------------------------------------------------------



























            // LIMIT [offset], [limit] supported by CUBRID
            // -------------------------------------------
            case CUBRID: {
                ctx.castMode(NEVER)
                   .formatSeparator()
                   .visit(K_LIMIT)
                   .sql(' ').visit(offsetOrZero)
                   .sql(", ").visit(limitOrMax)
                   .castMode(castMode);

                break;
            }

            case FIREBIRD: {











                acceptStandard(ctx, castMode);
                break;
            }

            case H2: {








                acceptStandard(ctx, castMode);
                break;
            }

            case POSTGRES: {





                acceptStandard(ctx, castMode);
                break;
            }


            case DERBY:
            case TRINO: {
                acceptStandard(ctx, castMode);
                break;
            }






































































            case MARIADB: {






                acceptStandard(ctx, castMode);
                break;
            }





            case MYSQL:
            case SQLITE: {

                // [#4785] OFFSET cannot be without LIMIT
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
               .visit(K_FETCH_NEXT).sql(' ').visit(limit);

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
               .sql(' ').visit(limit);

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
           .sql(' ').visit(limitOrMax);

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
        return limit == null;
    }

    /**
     * Whether this limit has a limit of one
     */
    final boolean limitOne() {
        return !limitZero()
            && !withTies()
            && !percent()
            && Long.valueOf(1L).equals(getLimit());
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
        return iadd(offsetOrZero, limitOrMax);
    }

    /**
     * Whether this LIMIT clause is applicable. If <code>false</code>, then no
     * LIMIT clause should be rendered.
     */
    final boolean isApplicable() {
        return offset != null || limit != null;
    }

    /**
     * Whether the LIMIT or OFFSET clause is an expression as opposed to a bind
     * variable.
     */
    final boolean isExpression() {
        return isApplicable()
            && !((limit == null || limit instanceof Param) && (offset == null || offset instanceof Param));
    }

    /**
     * Whether the LIMIT or OFFSET clause is a subquery.
     */
    final boolean isSubquery() {
        return isApplicable()
            && (isScalarSubquery(limit) || isScalarSubquery(offset));
    }

    final void setOffset(Number offset) {
        this.offset = val(offset.longValue(), BIGINT);
        this.offsetOrZero = this.offset;
        this.offsetPlusOne = val(offset.longValue() + 1L, BIGINT);
    }

    final void setOffset(Field<? extends Number> offset) {
        if (offset instanceof NoField)
            return;

        this.offset = offset;
        this.offsetOrZero = offset == null ? ZERO.get() : offset;
    }

    final void setLimit(Number l) {
        this.limit = val(l.longValue(), SQLDataType.BIGINT);
        this.limitOrMax = this.limit;
    }

    final void setLimit(Field<? extends Number> l) {
        if (l instanceof NoField)
            return;

        this.limit = l;
        this.limitOrMax = l == null ? MAX.get() : l;
    }

    final Long getLimit() {
        Field<?> l = limit != null ? limit : limitOrMax;

        if (l instanceof Param<?> p)
            return Convert.convert(p.getValue(), long.class);
        else
            return Convert.convert(MAX.get().getValue(), long.class);
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

    final Limit from(Limit other) {

        // [#9017] Take the lower number of two LIMIT clauses, maintaining
        //         inline flags and parameter names
        // [#5695] TODO: What to do if LIMIT is an expression?
        if (other.limit != null)
            if (this.limit == null)
                this.setLimit(other.limit);
            else
                this.setLimit(((Val<? extends Number>) other.limit).copy(Math.min(getLimit(), other.getLimit())));

        if (other.offset != null)
            this.setOffset(other.offset);

        this.setPercent(other.percent);
        this.setWithTies(other.withTies);

        return this;
    }

    final void clear() {
        offset = null;
        limit = null;
        withTies = false;
        percent = false;
    }
}
