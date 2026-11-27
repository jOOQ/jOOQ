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
 * The <code>ARRAY POSITION</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class ArrayPosition<T>
extends
    AbstractField<Integer>
implements
    QOM.ArrayPosition<T>
{

    final Field<T[]> array;
    final Field<T>   value;

    ArrayPosition(
        Field<T[]> array,
        Field<T> value
    ) {
        super(
            N_ARRAY_POSITION,
            allNotNull(INTEGER, array, value)
        );

        this.array = nullSafeNotNull(array, ((DataType) OTHER).array());
        this.value = nullSafeNotNull(value, (DataType) OTHER);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

    @Override
    final boolean parenthesised(Context<?> ctx) {
        switch (ctx.family()) {



            case CLICKHOUSE:
            case H2:
            case HSQLDB:
            case TRINO:
                return false;

            default:
                return true;
        }
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {






            case CLICKHOUSE:
                ctx.visit(DSL.nullif(function(N_indexOf, getDataType(), array, value), zero()));
                break;


            case TRINO:
                ctx.visit(DSL.nullif(function(N_ARRAY_POSITION, getDataType(), array, value), zero()));
                break;

            case HSQLDB: {
                ctx.visit(N_NULLIF).sql('(').visit(N_POSITION_ARRAY).sql('(').visit(value).sql(' ').visit(K_IN).sql(' ').visit(array).sql("), ").visit(zero()).sql(')');
                break;
            }








            case H2:
                ctx.visit(unnestEmulation());
                break;

            default:
                ctx.visit(function(N_ARRAY_POSITION, getDataType(), array, value));
                break;
        }
    }














    private final Field<Integer> unnestEmulation() {
        Field<Integer> o = DSL.field(N_O, INTEGER);

        return DSL.field(
            DSL.select(DSL.min(o))
               .from(unnest(array).withOrdinality().as(N_T, N_E, N_O))
               .where(DSL.field(N_E, value.getDataType()).eq(value))
        );
    }














    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<T[]> $arg1() {
        return array;
    }

    @Override
    public final Field<T> $arg2() {
        return value;
    }

    @Override
    public final QOM.ArrayPosition<T> $arg1(Field<T[]> newValue) {
        return $constructor().apply(newValue, $arg2());
    }

    @Override
    public final QOM.ArrayPosition<T> $arg2(Field<T> newValue) {
        return $constructor().apply($arg1(), newValue);
    }

    @Override
    public final Function2<? super Field<T[]>, ? super Field<T>, ? extends QOM.ArrayPosition<T>> $constructor() {
        return (a1, a2) -> new ArrayPosition<>(a1, a2);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.ArrayPosition<?> o) {
            return
                Objects.equals($array(), o.$array()) &&
                Objects.equals($value(), o.$value())
            ;
        }
        else
            return super.equals(that);
    }
}

