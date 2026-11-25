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
 * The <code>ARRAY SLICE</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class ArraySlice<T>
extends
    AbstractField<T[]>
implements
    QOM.ArraySlice<T>
{

    final Field<T[]>     array;
    final Field<Integer> start;
    final Field<Integer> end;

    ArraySlice(
        Field<T[]> array,
        Field<Integer> start,
        Field<Integer> end
    ) {
        super(
            N_ARRAY_SLICE,
            allNotNull((DataType) dataType(((DataType) OTHER).array(), array, false), array, start, end)
        );

        this.array = nullSafeNotNull(array, ((DataType) OTHER).array());
        this.start = nullSafeNotNull(start, INTEGER);
        this.end = nullSafeNotNull(end, INTEGER);
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
            case POSTGRES:
            case TRINO:
            case YUGABYTEDB:
                return false;

            default:
                return true;
        }
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {







            case POSTGRES:
            case YUGABYTEDB: {
                ctx.sql('(').visit(array).sql(")[").visit(start).sql(':').visit(end).sql(']');
                break;
            }








            case CLICKHOUSE:
                ctx.visit(function(N_arraySlice, getDataType(), array, start, sliceLength()));
                break;


            case TRINO:
                ctx.visit(function(N_SLICE, getDataType(), array, start, sliceLength()));
                break;


            case H2:
            case HSQLDB:
                ctx.visit(unnestEmulation());
                break;

            default:
                ctx.visit(function(N_ARRAY_SLICE, getDataType(), array, start, end));
                break;
        }
    }















    private final Field<Integer> sliceLength() {
        return DSL.greatest(iadd(isub(end, start), one()), zero());
    }

    private final Field<?> unnestEmulation() {
        Field<Integer> o = DSL.field(N_O, INTEGER);
        Field<T[]> result = DSL.coalesce(
            DSL.field(
                DSL.select(arrayAgg((Field<T>) DSL.field(N_E)).orderBy(o))
                   .from(unnest(array).withOrdinality().as(N_T, N_E, N_O))
                   .where(o.between(start, end))
            ),
            DSL.cast(array(), getDataType())
        );

        // The emulation would produce an empty array, not NULL, for NULL arguments
        Condition c = DSL.and(notNull(array), notNull(start), notNull(end));
        return c instanceof NoCondition ? result : DSL.when(c, result);
    }

    private static final Condition notNull(Field<?> f) {
        return isVal0(f, v -> v.getValue() != null) ? noCondition() : f.isNotNull();
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<T[]> $arg1() {
        return array;
    }

    @Override
    public final Field<Integer> $arg2() {
        return start;
    }

    @Override
    public final Field<Integer> $arg3() {
        return end;
    }

    @Override
    public final QOM.ArraySlice<T> $arg1(Field<T[]> newValue) {
        return $constructor().apply(newValue, $arg2(), $arg3());
    }

    @Override
    public final QOM.ArraySlice<T> $arg2(Field<Integer> newValue) {
        return $constructor().apply($arg1(), newValue, $arg3());
    }

    @Override
    public final QOM.ArraySlice<T> $arg3(Field<Integer> newValue) {
        return $constructor().apply($arg1(), $arg2(), newValue);
    }

    @Override
    public final Function3<? super Field<T[]>, ? super Field<Integer>, ? super Field<Integer>, ? extends QOM.ArraySlice<T>> $constructor() {
        return (a1, a2, a3) -> new ArraySlice<>(a1, a2, a3);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.ArraySlice<?> o) {
            return
                Objects.equals($array(), o.$array()) &&
                Objects.equals($start(), o.$start()) &&
                Objects.equals($end(), o.$end())
            ;
        }
        else
            return super.equals(that);
    }
}

