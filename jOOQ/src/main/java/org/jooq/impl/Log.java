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
 * The <code>LN</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unused" })
final class Log
extends
    AbstractField<BigDecimal>
implements
    MLog
{

    final Field<? extends Number> value;
    final Field<? extends Number> base;

    Log(
        Field<? extends Number> value
    ) {
        super(
            N_LN,
            allNotNull(NUMERIC, value)
        );

        this.value = nullSafeNotNull(value, INTEGER);
        this.base = null;
    }

    Log(
        Field<? extends Number> value,
        Field<? extends Number> base
    ) {
        super(
            N_LN,
            allNotNull(NUMERIC, value, base)
        );

        this.value = nullSafeNotNull(value, INTEGER);
        this.base = nullSafeNotNull(base, INTEGER);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    @Override
    public final void accept(Context<?> ctx) {
        if (base == null) {
            switch (ctx.family()) {






















                default:
                    ctx.visit(function(N_LN, NUMERIC, value));
                    return;
            }
        }
        else {
            switch (ctx.family()) {


























                case DERBY:
                case HSQLDB:
                case IGNITE:
                    ctx.visit(idiv(DSL.ln(value), DSL.ln(base)));
                    return;

                default:
                    ctx.visit(function(N_LOG, NUMERIC, base, value));
                    return;
            }
        }
    }
















    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<? extends Number> $value() {
        return value;
    }

    @Override
    public final Field<? extends Number> $base() {
        return base;
    }

    @Override
    public final MLog $value(MField<? extends Number> newValue) {
        return constructor().apply(newValue, $base());
    }

    @Override
    public final MLog $base(MField<? extends Number> newValue) {
        return constructor().apply($value(), newValue);
    }

    public final Function2<? super MField<? extends Number>, ? super MField<? extends Number>, ? extends MLog> constructor() {
        return (a1, a2) -> new Log((Field<? extends Number>) a1, (Field<? extends Number>) a2);
    }

    @Override
    public final MQueryPart replace(Function1<? super MQueryPart, ? extends MQueryPart> replacement) {
        return QOM.replace(
            this,
            $value(),
            $base(),
            constructor()::apply,
            replacement
        );
    }

    @Override
    public final <R> R traverse(
        R init,
        Predicate<? super R> abort,
        Predicate<? super MQueryPart> recurse,
        BiFunction<? super R, ? super MQueryPart, ? extends R> accumulate
    ) {
        return QOM.traverse(
            init, abort, recurse, accumulate, this,
            $value(),
            $base()
        );
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof Log) {
            return
                StringUtils.equals($value(), ((Log) that).$value()) &&
                StringUtils.equals($base(), ((Log) that).$base())
            ;
        }
        else
            return super.equals(that);
    }
}
