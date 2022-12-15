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
import org.jooq.impl.*;
import org.jooq.impl.QOM.*;
import org.jooq.tools.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;


/**
 * The <code>ARRAY GET</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class ArrayGet<T>
extends
    AbstractField<T>
implements
    QOM.ArrayGet<T>
{

    final Field<T[]>     array;
    final Field<Integer> index;

    ArrayGet(
        Field<T[]> array,
        Field<Integer> index
    ) {
        super(
            N_ARRAY_GET,
            allNotNull((DataType<T>) StringUtils.defaultIfNull(array.getDataType().getArrayComponentDataType(), OTHER), array, index)
        );

        this.array = nullSafeNotNull(array, ((DataType) OTHER).array());
        this.index = nullSafeNotNull(index, INTEGER);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {
















            case HSQLDB:
                if (Boolean.TRUE.equals(ctx.data(DATA_STORE_ASSIGNMENT)))
                    ctx.visit(new Standard());
                else
                    ctx.visit(when(cardinality(array).ge(index), new Standard()));

                break;

            default:
                ctx.visit(new Standard());
                break;
        }
    }

    private class Standard extends AbstractField<T> implements UTransient {

        Standard() {
            super(ArrayGet.this.getQualifiedName(), ArrayGet.this.getDataType());
        }

        @Override
        public void accept(Context<?> ctx) {

            // [#13808] When using an array element reference as a store assignment
            //          target, the parentheses must not be rendered
            if (array instanceof TableField || Boolean.TRUE.equals(ctx.data(DATA_STORE_ASSIGNMENT)))
                ctx.visit(array).sql('[').visit(index).sql(']');

            // [#12480] For expressions the parens might be required
            else
                ctx.sql('(').visit(array).sql(')').sql('[').visit(index).sql(']');
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
    public final Field<Integer> $arg2() {
        return index;
    }

    @Override
    public final QOM.ArrayGet<T> $arg1(Field<T[]> newValue) {
        return $constructor().apply(newValue, $arg2());
    }

    @Override
    public final QOM.ArrayGet<T> $arg2(Field<Integer> newValue) {
        return $constructor().apply($arg1(), newValue);
    }

    @Override
    public final Function2<? super Field<T[]>, ? super Field<Integer>, ? extends QOM.ArrayGet<T>> $constructor() {
        return (a1, a2) -> new ArrayGet<>(a1, a2);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.ArrayGet<?> o) {
            return
                StringUtils.equals($array(), o.$array()) &&
                StringUtils.equals($index(), o.$index())
            ;
        }
        else
            return super.equals(that);
    }
}
