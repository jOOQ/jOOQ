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
        final Field<Integer> negatives = DSL.when(f.lt(zero()), inline(-1));

        Field<BigDecimal> negativesSum = CustomField.of("sum", NUMERIC, c -> {
            c.visit(distinct
                ? DSL.sumDistinct(negatives)
                : DSL.sum(negatives));

            acceptFilterClause(c);
            acceptOverClause(c);
        });

        Field<BigDecimal> zerosSum = CustomField.of("sum", NUMERIC, c -> {
            c.visit(DSL.sum(choose(f).when(zero(), one())));

            acceptFilterClause(c);
            acceptOverClause(c);
        });

        Field<BigDecimal> logarithmsSum = CustomField.of("sum", NUMERIC, c -> {
            Field<Integer> abs = DSL.abs(DSL.nullif(f, zero()));
            Field<BigDecimal> ln =





                DSL.ln(abs);

            c.visit(distinct
                ? DSL.sumDistinct(ln)
                : DSL.sum(ln));

            acceptFilterClause(c);
            acceptOverClause(c);
        });

        ctx.visit(imul(
             when(zerosSum.gt(inline(BigDecimal.ZERO)), zero())
            .when(negativesSum.mod(inline(2)).lt(inline(BigDecimal.ZERO)), inline(-1))
            .otherwise(one()),
            DSL.exp(logarithmsSum)
        ));
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
