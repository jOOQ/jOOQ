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
 * The <code>PERCENTILE CONT</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unused" })
final class PercentileCont
extends
    AbstractAggregateFunction<BigDecimal, QOM.PercentileCont>
implements
    QOM.PercentileCont
{

    PercentileCont(
        Field<? extends Number> percentile
    ) {
        super(
            false,
            N_PERCENTILE_CONT,
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























            case CLICKHOUSE: {
                SortField<?> sort = withinGroupOrderBy.$first();
                Field<?> p = getArgument(0);

                if (sort != null) {
                    ctx.visit(N_quantile).sql('(').visit(sort.$sortOrder() == SortOrder.DESC ? inline(1).minus(p) : p).sql(')')
                       .sql('(').visit(sort.$field()).sql(')');
                }
                else
                    ctx.visit(N_quantile).sql('(').visit(p).sql(')');

                acceptFilterClause(ctx);
                acceptOverClause(ctx);
                break;
            }

            default:
                super.accept(ctx);
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
    public final QOM.PercentileCont $percentile(Field<? extends Number> newValue) {
        return copyAggregateSpecification().apply($constructor().apply(newValue));
    }

    public final Function1<? super Field<? extends Number>, ? extends QOM.PercentileCont> $constructor() {
        return (a1) -> new PercentileCont(a1);
    }

    @Override
    final QOM.PercentileCont copyAggregateFunction(Function<? super QOM.PercentileCont, ? extends QOM.PercentileCont> function) {
        return function.apply($constructor().apply($percentile()));
    }
























    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.PercentileCont o) {
            return
                Objects.equals($percentile(), o.$percentile())
            ;
        }
        else
            return super.equals(that);
    }
}
