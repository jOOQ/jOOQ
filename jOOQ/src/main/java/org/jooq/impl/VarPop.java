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
 * The <code>VAR POP</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unused" })
final class VarPop
extends
    AbstractAggregateFunction<BigDecimal>
implements
    QOM.VarPop
{

    VarPop(
        Field<? extends Number> field
    ) {
        super(
            false,
            N_VAR_POP,
            NUMERIC,
            nullSafeNotNull(field, INTEGER)
        );
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    private static final Set<SQLDialect> NO_SUPPORT_NATIVE = SQLDialect.supportedUntil(DERBY, IGNITE, SQLITE);

    @SuppressWarnings("unchecked")
    @Override
    public void accept(Context<?> ctx) {
        if (NO_SUPPORT_NATIVE.contains(ctx.dialect())) {
            Field<? extends Number> x = (Field) getArguments().get(0);

            ctx.visit(fo(DSL.avg(DSL.square(x).cast(d(ctx)))).minus(DSL.square(fo(DSL.avg(x.cast(d(ctx)))))));
        }
        else
            super.accept(ctx);
    }

    @Override
    void acceptFunctionName(Context<?> ctx) {
        switch (ctx.family()) {












            default:
                super.acceptFunctionName(ctx);
                break;
        }
    }



    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    @Override
    public final Field<? extends Number> $field() {
        return (Field<? extends Number>) getArguments().get(0);
    }

    @Override
    public final QOM.VarPop $field(Field<? extends Number> newValue) {
        return constructor().apply(newValue);
    }

    public final Function1<? super Field<? extends Number>, ? extends QOM.VarPop> constructor() {
        return (a1) -> new VarPop(a1);
    }























    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.VarPop) { QOM.VarPop o = (QOM.VarPop) that;
            return
                StringUtils.equals($field(), o.$field())
            ;
        }
        else
            return super.equals(that);
    }
}
