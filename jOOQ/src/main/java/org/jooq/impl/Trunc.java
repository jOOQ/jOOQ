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
 * Apache-2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
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
 * The <code>TRUNC</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class Trunc<T extends Number>
extends
    AbstractField<T>
implements
    QOM.Trunc<T>
{

    final Field<T>       value;
    final Field<Integer> decimals;

    Trunc(
        Field<T> value,
        Field<Integer> decimals
    ) {
        super(
            N_TRUNC,
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


            case DERBY: {
                Field<?> power;

                // [#1334] if possible, calculate the power in Java to prevent
                // inaccurate arithmetics in the Derby database
                Integer decimalsVal = extractParamValue(decimals);
                if (decimalsVal != null)
                    power = inline(java.math.BigDecimal.TEN.pow(decimalsVal, java.math.MathContext.DECIMAL128));
                else
                    power = DSL.power(inline(java.math.BigDecimal.TEN), decimals);

                ctx.visit(DSL.decode()
                    .when(value.sign().greaterOrEqual(zero()), idiv(imul(value, power).floor(), power))
                    .otherwise(idiv(imul(value, power).ceil(), power)));
                break;
            }



            case H2:
            case MARIADB:
            case MYSQL:
                ctx.visit(N_TRUNCATE).sql('(').visit(value).sql(", ").visit(decimals).sql(')');
                break;

            // Postgres TRUNC() only takes NUMERIC arguments, no
            // DOUBLE PRECISION ones


            case POSTGRES:
            case YUGABYTEDB:
                ctx.visit(castIfNeeded(
                    DSL.function(N_TRUNC, NUMERIC,
                        castIfNeeded(value, NUMERIC),
                        decimals
                    ),
                    value.getDataType()
                ));
                break;





















            default:
                ctx.visit(N_TRUNC).sql('(').visit(value).sql(", ").visit(decimals).sql(')');
                break;
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
    public final QOM.Trunc<T> $arg1(Field<T> newValue) {
        return $constructor().apply(newValue, $arg2());
    }

    @Override
    public final QOM.Trunc<T> $arg2(Field<Integer> newValue) {
        return $constructor().apply($arg1(), newValue);
    }

    @Override
    public final Function2<? super Field<T>, ? super Field<Integer>, ? extends QOM.Trunc<T>> $constructor() {
        return (a1, a2) -> new Trunc<>(a1, a2);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.Trunc<?> o) {
            return
                Objects.equals($value(), o.$value()) &&
                Objects.equals($decimals(), o.$decimals())
            ;
        }
        else
            return super.equals(that);
    }
}
