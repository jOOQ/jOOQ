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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
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

import static org.jooq.impl.DSL.*;
import static org.jooq.impl.Internal.*;
import static org.jooq.impl.Keywords.*;
import static org.jooq.impl.Names.*;
import static org.jooq.impl.SQLDataType.*;
import static org.jooq.impl.Tools.*;
import static org.jooq.impl.Tools.BooleanDataKey.*;
import static org.jooq.impl.Tools.ExtendedDataKey.*;
import static org.jooq.impl.Tools.SimpleDataKey.*;
import static org.jooq.SQLDialect.*;

import org.jooq.*;
import org.jooq.Function1;
import org.jooq.Record;
import org.jooq.conf.ParamType;
import org.jooq.tools.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import java.math.BigDecimal;
import java.util.function.Function;


/**
 * The <code>PRODUCT</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unused" })
final class Product
extends
    AbstractAggregateFunction<BigDecimal, QOM.Product>
implements
    QOM.Product
{

    Product(
        Field<? extends Number> field,
        boolean distinct
    ) {
        super(
            distinct,
            N_PRODUCT,
            NUMERIC,
            nullSafeNotNull(field, INTEGER)
        );
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {












            case DUCKDB:
                acceptNative(ctx);
                break;

            default:
                acceptEmulation(ctx);
                break;
        }
    }

    private final void acceptNative(Context<?> ctx) {
        Name name;

        switch (ctx.family()) {





            default:
                name = N_PRODUCT;
                break;
        }

        ctx.visit(CustomField.of(name, NUMERIC, c -> {
            c.visit(distinct
                ? aggregateDistinct(name, NUMERIC, arguments.toArray(EMPTY_FIELD))
                : aggregate(name, NUMERIC, arguments.toArray(EMPTY_FIELD)));

            acceptFilterClause(c);
            acceptOverClause(c);
        }));
    }

    private final void acceptEmulation(Context<?> ctx) {

        @SuppressWarnings({ "unchecked" })
        final Field<Integer> f = (Field) arguments.get(0);

        // [#20591] Apply this function's FILTER and OVER clauses to the
        //          auxiliary aggregate functions, rather than rendering them
        //          verbatim, in order to profit from FILTER emulations
        final Field<BigDecimal> zerosSum = fo(DSL.sum(choose(f).when(zero(), one())));
        final Condition negative;
        final Field<BigDecimal> logarithmsSum;

        // [#20608] DISTINCT must be applied to the argument values, not to
        //          derived values, which may collapse distinct arguments:
        //          - All negative values map to the same sign marker
        //          - x and -x map to the same LN(ABS(x)) value
        if (distinct) {
            final Field<Integer> negatives = fo(DSL.countDistinct(DSL.when(f.lt(zero()), f)));
            final Field<BigDecimal> positiveLogarithmsSum = fo(DSL.sumDistinct(DSL.when(f.gt(zero()), ln(ctx, f))));
            final Field<BigDecimal> negativeLogarithmsSum = fo(DSL.sumDistinct(DSL.when(f.lt(zero()), ln(ctx, f.neg()))));

            negative = negatives.mod(inline(2)).eq(inline(1));

            // NULL only if both sums are NULL, i.e. if there are no non-zero, non-NULL values
            logarithmsSum = DSL.coalesce(
                positiveLogarithmsSum.plus(negativeLogarithmsSum),
                positiveLogarithmsSum,
                negativeLogarithmsSum
            );
        }
        else {
            final Field<BigDecimal> negativesSum = fo(DSL.sum(DSL.when(f.lt(zero()), inline(-1))));

            negative = negativesSum.mod(inline(2)).lt(inline(BigDecimal.ZERO));
            logarithmsSum = fo(DSL.sum(ln(ctx, DSL.abs(DSL.nullif(f, zero())))));
        }

        ctx.visit(imul(
             when(zerosSum.gt(inline(BigDecimal.ZERO)), zero())
            .when(negative, inline(-1))
            .otherwise(one()),
            DSL.exp(logarithmsSum)
        ));
    }

    private static final Field<BigDecimal> ln(Context<?> ctx, Field<Integer> f) {
        return





            DSL.ln(f);
    }



    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    @Override
    public final Field<? extends Number> $field() {
        return (Field<? extends Number>) getArgument(0);
    }

    @Override
    public final QOM.Product $field(Field<? extends Number> newValue) {
        return copyAggregateSpecification().apply($constructor().apply(newValue, $distinct()));
    }

    @Override
    public final QOM.Product $distinct(boolean newValue) {
        return copyAggregateSpecification().apply($constructor().apply($field(), newValue));
    }

    public final Function2<? super Field<? extends Number>, ? super Boolean, ? extends QOM.Product> $constructor() {
        return (a1, a2) -> new Product(a1, a2);
    }

    @Override
    final QOM.Product copyAggregateFunction(Function<? super QOM.Product, ? extends QOM.Product> function) {
        return function.apply($constructor().apply($field(), $distinct()));
    }























    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.Product o) {
            return
                Objects.equals($field(), o.$field()) &&
                $distinct() == o.$distinct()
            ;
        }
        else
            return super.equals(that);
    }
}

