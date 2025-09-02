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



/**
 * The <code>ROUND</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class Round<T extends Number>
extends
    AbstractField<T>
implements
    QOM.Round<T>
{

    final Field<T>       value;
    final Field<Integer> decimals;

    Round(
        Field<T> value
    ) {
        super(
            N_ROUND,
            allNotNull((DataType) dataType(INTEGER, value, false), value)
        );

        this.value = nullSafeNotNull(value, INTEGER);
        this.decimals = null;
    }

    Round(
        Field<T> value,
        Field<Integer> decimals
    ) {
        super(
            N_ROUND,
            allNotNull((DataType) dataType(INTEGER, value, false), value, decimals)
        );

        this.value = nullSafeNotNull(value, INTEGER);
        this.decimals = nullSafeNotNull(decimals, INTEGER);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {

            // evaluate "round" if unavailable
            case DERBY: {
                if (decimals == null) {
                    ctx.visit(DSL
                        .when(isub(value, DSL.floor(value))
                        .lessThan(inline((T) Double.valueOf(0.5))), DSL.floor(value))
                        .otherwise(DSL.ceil(value)));

                    return;
                }
                else if (decimals instanceof Param<Integer> p) {
                    Integer decimalsValue = p.getValue();
                    Field<?> factor = DSL.val(java.math.BigDecimal.ONE.movePointRight(decimalsValue));
                    Field<T> mul = imul(value, factor);

                    ctx.visit(DSL
                        .when(isub(mul, DSL.floor(mul))
                        .lessThan(inline((T) Double.valueOf(0.5))), idiv(DSL.floor(mul), factor))
                        .otherwise(idiv(DSL.ceil(mul), factor)));

                    return;
                }
                // fall-through
            }



























            // There's no function round(double precision, integer) in Postgres
            case POSTGRES:
            case YUGABYTEDB:
                if (decimals == null)
                    ctx.visit(function(N_ROUND, getDataType(), value));
                else
                    ctx.visit(function(N_ROUND, getDataType(), castIfNeeded(value, NUMERIC), decimals));

                return;

            default:
                if (decimals == null)
                    ctx.visit(function(N_ROUND, getDataType(), value));
                else
                    ctx.visit(function(N_ROUND, getDataType(), value, decimals));

                return;
        }
    }
















    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<T> $arg1() {
        return value;
    }

    @Override
    public final Field<Integer> $arg2() {
        return decimals;
    }

    @Override
    public final QOM.Round<T> $arg1(Field<T> newValue) {
        return $constructor().apply(newValue, $arg2());
    }

    @Override
    public final QOM.Round<T> $arg2(Field<Integer> newValue) {
        return $constructor().apply($arg1(), newValue);
    }

    @Override
    public final Function2<? super Field<T>, ? super Field<Integer>, ? extends QOM.Round<T>> $constructor() {
        return (a1, a2) -> new Round<>(a1, a2);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.Round<?> o) {
            return
                Objects.equals($value(), o.$value()) &&
                Objects.equals($decimals(), o.$decimals())
            ;
        }
        else
            return super.equals(that);
    }
}
