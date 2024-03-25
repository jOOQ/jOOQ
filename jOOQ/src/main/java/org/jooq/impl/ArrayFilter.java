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
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
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
import org.jooq.conf.*;
import org.jooq.tools.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;


/**
 * The <code>ARRAY FILTER</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class ArrayFilter<T>
extends
    AbstractField<T[]>
implements
    QOM.ArrayFilter<T>
{

    final Field<T[]>          array;
    final Lambda1<T, Boolean> filter;

    ArrayFilter(
        Field<T[]> array,
        Lambda1<T, Boolean> filter
    ) {
        super(
            N_ARRAY_FILTER,
            allNotNull((DataType) dataType(((DataType) OTHER).array(), array, false), array)
        );

        this.array = nullSafeNotNull(array, ((DataType) OTHER).array());
        this.filter = filter;
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

    @Override
    final boolean parenthesised(Context<?> ctx) {
        switch (ctx.family()) {


            case H2:
            case HSQLDB:
            case POSTGRES:
            case YUGABYTEDB:
                return false;

            case DUCKDB:
                return true;

            default:
                return true;
        }
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {








            case H2:
            case HSQLDB:
            case POSTGRES:
            case YUGABYTEDB:
                ctx.visit(DSL.field(
                    select(DSL.coalesce(arrayAgg(filter.$arg1()), when(array.isNotNull(), DSL.cast(array(), getDataType()))))
                    .from(unnest(array).as(N_T, filter.$arg1().getUnqualifiedName()))
                    .where(filter.$result())
                ));
                break;

            case DUCKDB:
                ctx.visit(function(N_ARRAY_FILTER, getDataType(), array, DSL.field("{0}", OTHER, filter)));
                break;

            default:
                ctx.visit(function(N_ARRAY_FILTER, getDataType(), array, DSL.field("{0}", OTHER, filter)));
                break;
        }
    }














    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<T[]> $arg1() {
        return array;
    }

    @Override
    public final Lambda1<T, Boolean> $arg2() {
        return filter;
    }

    @Override
    public final QOM.ArrayFilter<T> $arg1(Field<T[]> newValue) {
        return $constructor().apply(newValue, $arg2());
    }

    @Override
    public final QOM.ArrayFilter<T> $arg2(Lambda1<T, Boolean> newValue) {
        return $constructor().apply($arg1(), newValue);
    }

    @Override
    public final Function2<? super Field<T[]>, ? super Lambda1<T, Boolean>, ? extends QOM.ArrayFilter<T>> $constructor() {
        return (a1, a2) -> new ArrayFilter<>(a1, a2);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.ArrayFilter<?> o) {
            return
                StringUtils.equals($array(), o.$array()) &&
                StringUtils.equals($filter(), o.$filter())
            ;
        }
        else
            return super.equals(that);
    }
}
