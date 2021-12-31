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
import org.jooq.Function1;
import org.jooq.Record;
import org.jooq.conf.*;
import org.jooq.impl.*;
import org.jooq.impl.QOM.*;
import org.jooq.tools.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.math.BigDecimal;


/**
 * The <code>CORR</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unused" })
final class Corr
extends
    AbstractAggregateFunction<BigDecimal>
implements
    QOM.Corr
{

    Corr(
        Field<? extends Number> y,
        Field<? extends Number> x
    ) {
        super(
            false,
            N_CORR,
            NUMERIC,
            nullSafeNotNull(y, INTEGER),
            nullSafeNotNull(x, INTEGER)
        );
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    private static final Set<SQLDialect> NO_SUPPORT_NATIVE        = SQLDialect.supportedUntil(CUBRID, DERBY, H2, HSQLDB, IGNITE, MARIADB, MYSQL, SQLITE);





    @Override
    public final void accept(Context<?> ctx) {
        if (NO_SUPPORT_NATIVE.contains(ctx.dialect()))
            acceptEmulation(ctx);




        else
            super.accept(ctx);
    }

    @SuppressWarnings("unchecked")
    private final void acceptEmulation(Context<?> ctx) {
        Field<? extends Number> x = (Field) getArguments().get(0);
        Field<? extends Number> y = (Field) getArguments().get(1);

        ctx.visit(fo(covarPop(x, y)).div(fon(DSL.stddevPop(x(x, y))).times(fo(DSL.stddevPop(y(x, y))))));
    }



    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    @Override
    public final Field<? extends Number> $y() {
        return (Field<? extends Number>) getArguments().get(0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Field<? extends Number> $x() {
        return (Field<? extends Number>) getArguments().get(1);
    }

    @Override
    public final QOM.Corr $y(Field<? extends Number> newValue) {
        return constructor().apply(newValue, $x());
    }

    @Override
    public final QOM.Corr $x(Field<? extends Number> newValue) {
        return constructor().apply($y(), newValue);
    }

    public final Function2<? super Field<? extends Number>, ? super Field<? extends Number>, ? extends QOM.Corr> constructor() {
        return (a1, a2) -> new Corr(a1, a2);
    }

























    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.Corr) { QOM.Corr o = (QOM.Corr) that;
            return
                StringUtils.equals($y(), o.$y()) &&
                StringUtils.equals($x(), o.$x())
            ;
        }
        else
            return super.equals(that);
    }
}
