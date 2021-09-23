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

import static org.jooq.impl.DSL.*;
import static org.jooq.impl.Internal.*;
import static org.jooq.impl.Keywords.*;
import static org.jooq.impl.Names.*;
import static org.jooq.impl.SQLDataType.*;
import static org.jooq.impl.Tools.*;
import static org.jooq.impl.Tools.BooleanDataKey.*;
import static org.jooq.impl.Tools.DataExtendedKey.*;
import static org.jooq.impl.Tools.DataKey.*;
import static org.jooq.SQLDialect.*;

import org.jooq.*;
import org.jooq.Record;
import org.jooq.conf.*;
import org.jooq.impl.*;
import org.jooq.tools.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.math.BigDecimal;


/**
 * The <code>PRODUCT</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unused" })
final class Product
extends
    AbstractAggregateFunction<BigDecimal>
{

    Product(
        Field<? extends Number> field,
        boolean productDistinct
    ) {
        super(
            productDistinct,
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







            default:
                acceptEmulation(ctx);
                break;
        }
    }



















    private final void acceptEmulation(Context<?> ctx) {

        @SuppressWarnings({ "unchecked" })
        final Field<Integer> f = (Field) DSL.field("{0}", arguments.get(0).getDataType(), arguments.get(0));
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


}
