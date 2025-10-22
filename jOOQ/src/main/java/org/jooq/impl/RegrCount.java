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
 * The <code>REGR COUNT</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unused" })
final class RegrCount
extends
    AbstractAggregateFunction<BigDecimal, RegrCount>
implements
    QOM.RegrCount
{

    RegrCount(
        Field<? extends Number> y,
        Field<? extends Number> x
    ) {
        super(
            false,
            N_REGR_COUNT,
            NUMERIC,
            nullSafeNotNull(y, INTEGER),
            nullSafeNotNull(x, INTEGER)
        );
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    private static final Set<SQLDialect> NO_SUPPORT_NATIVE        = SQLDialect.supportedUntil(CLICKHOUSE, CUBRID, DERBY, HSQLDB, IGNITE, MARIADB, MYSQL, SQLITE, TRINO);





    @Override
    public final void accept(Context<?> ctx) {
        if (NO_SUPPORT_NATIVE.contains(ctx.dialect()))
            acceptEmulation(ctx);




        else
            super.accept(ctx);
    }

    private final void acceptEmulation(Context<?> ctx) {
        ctx.visit(fo(DSL.count(getArguments().get(0).plus(getArguments().get(1)))));
    }



    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    @Override
    public final Field<? extends Number> $y() {
        return (Field<? extends Number>) getArgument(0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Field<? extends Number> $x() {
        return (Field<? extends Number>) getArgument(1);
    }

    @Override
    public final QOM.RegrCount $y(Field<? extends Number> newValue) {
        return $constructor().apply(newValue, $x());
    }

    @Override
    public final QOM.RegrCount $x(Field<? extends Number> newValue) {
        return $constructor().apply($y(), newValue);
    }

    public final Function2<? super Field<? extends Number>, ? super Field<? extends Number>, ? extends QOM.RegrCount> $constructor() {
        return (a1, a2) -> new RegrCount(a1, a2);
    }

    @Override
    final RegrCount copy2(Function<RegrCount, RegrCount> function) {
        return function.apply((RegrCount) $constructor().apply($y(), $x()));
    }
















    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.RegrCount o) {
            return
                Objects.equals($y(), o.$y()) &&
                Objects.equals($x(), o.$x())
            ;
        }
        else
            return super.equals(that);
    }
}
