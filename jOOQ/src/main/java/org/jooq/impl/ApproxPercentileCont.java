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
 * The <code>APPROX PERCENTILE CONT</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unused" })
final class ApproxPercentileCont
extends
    AbstractAggregateFunction<BigDecimal, QOM.ApproxPercentileCont>
implements
    QOM.ApproxPercentileCont
{

    ApproxPercentileCont(
        Field<? extends Number> percentile
    ) {
        super(
            false,
            N_APPROX_PERCENTILE_CONT,
            NUMERIC,
            nullSafeNotNull(percentile, INTEGER)
        );
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {




            case CLICKHOUSE:
            case TRINO: {
                ctx.visit(ofo(new ApproxPercentileDisc($percentile())));
                break;
            }

            default:
                super.accept(ctx);
                break;
        }
    }

    @Override
    final void acceptFunctionName(Context<?> ctx) {
        switch (ctx.family()) {















            case H2:
            case MARIADB:
            case POSTGRES:
            case YUGABYTEDB:
                ctx.visit(N_PERCENTILE_CONT);
                break;

            default:
                super.acceptFunctionName(ctx);
                break;
        }
    }

    @Override
    final boolean applyFilterToArgument(Context<?> ctx, Field<?> arg, int i) {
        return false;
    }

    @Override
    final boolean applyFilterToWithinGroup(Context<?> ctx) {
        return true;
    }



    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    @Override
    public final Field<? extends Number> $percentile() {
        return (Field<? extends Number>) getArgument(0);
    }

    @Override
    public final QOM.ApproxPercentileCont $percentile(Field<? extends Number> newValue) {
        return copyAggregateSpecification().apply($constructor().apply(newValue));
    }

    public final Function1<? super Field<? extends Number>, ? extends QOM.ApproxPercentileCont> $constructor() {
        return (a1) -> new ApproxPercentileCont(a1);
    }

    @Override
    final QOM.ApproxPercentileCont copyAggregateFunction(Function<? super QOM.ApproxPercentileCont, ? extends QOM.ApproxPercentileCont> function) {
        return function.apply($constructor().apply($percentile()));
    }
























    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.ApproxPercentileCont o) {
            return
                Objects.equals($percentile(), o.$percentile())
            ;
        }
        else
            return super.equals(that);
    }
}
