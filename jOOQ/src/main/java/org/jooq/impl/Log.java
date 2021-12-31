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
    QOM.Log
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
    public final QOM.Log $value(Field<? extends Number> newValue) {
        return constructor().apply(newValue, $base());
    }

    @Override
    public final QOM.Log $base(Field<? extends Number> newValue) {
        return constructor().apply($value(), newValue);
    }

    public final Function2<? super Field<? extends Number>, ? super Field<? extends Number>, ? extends QOM.Log> constructor() {
        return (a1, a2) -> new Log(a1, a2);
    }
























    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.Log) { QOM.Log o = (QOM.Log) that;
            return
                StringUtils.equals($value(), o.$value()) &&
                StringUtils.equals($base(), o.$base())
            ;
        }
        else
            return super.equals(that);
    }
}
