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


/**
 * The <code>POWER</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unused" })
final class Power
extends
    AbstractField<BigDecimal>
implements
    QOM.Power
{

    final Field<? extends Number> base;
    final Field<? extends Number> exponent;

    Power(
        Field<? extends Number> base,
        Field<? extends Number> exponent
    ) {
        super(
            N_POWER,
            allNotNull(NUMERIC, base, exponent)
        );

        this.base = nullSafeNotNull(base, INTEGER);
        this.exponent = nullSafeNotNull(exponent, INTEGER);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

    @Override
    final boolean parenthesised(Context<?> ctx) {
        switch (ctx.family()) {





            case CLICKHOUSE:
                return true;

            case DERBY:
                return false;






            default:
                return true;
        }
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {













            case CLICKHOUSE:
                ctx.visit(function(N_POW, getDataType(), base, exponent));
                break;

            case DERBY:
                ctx.visit(DSL.exp(imul(DSL.ln(base), exponent)));
                break;







            default:
                ctx.visit(function(N_POWER, getDataType(), base, exponent));
                break;
        }
    }










    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<? extends Number> $arg1() {
        return base;
    }

    @Override
    public final Field<? extends Number> $arg2() {
        return exponent;
    }

    @Override
    public final QOM.Power $arg1(Field<? extends Number> newValue) {
        return $constructor().apply(newValue, $arg2());
    }

    @Override
    public final QOM.Power $arg2(Field<? extends Number> newValue) {
        return $constructor().apply($arg1(), newValue);
    }

    @Override
    public final Function2<? super Field<? extends Number>, ? super Field<? extends Number>, ? extends QOM.Power> $constructor() {
        return (a1, a2) -> new Power(a1, a2);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.Power o) {
            return
                Objects.equals($base(), o.$base()) &&
                Objects.equals($exponent(), o.$exponent())
            ;
        }
        else
            return super.equals(that);
    }
}
