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

        this.array = nullSafeNotNull(array, ((DataType) OTHER).getArrayDataType());
        this.index = nullSafeNotNull(index, INTEGER);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {
















            case HSQLDB:
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
            ctx.sql('(').visit(array).sql(')').sql('[').visit(index).sql(']');
        }
    }
















    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<T[]> $array() {
        return array;
    }

    @Override
    public final Field<Integer> $index() {
        return index;
    }

    @Override
    public final QOM.ArrayGet<T> $array(Field<T[]> newValue) {
        return constructor().apply(newValue, $index());
    }

    @Override
    public final QOM.ArrayGet<T> $index(Field<Integer> newValue) {
        return constructor().apply($array(), newValue);
    }

    public final Function2<? super Field<T[]>, ? super Field<Integer>, ? extends QOM.ArrayGet<T>> constructor() {
        return (a1, a2) -> new ArrayGet<>(a1, a2);
    }

    @Override
    public final QueryPart $replace(
        Predicate<? super QueryPart> recurse,
        Function1<? super QueryPart, ? extends QueryPart> replacement
    ) {
        return QOM.replace(
            this,
            $array(),
            $index(),
            (a1, a2) -> constructor().apply(a1, a2),
            recurse,
            replacement
        );
    }

    @Override
    public final <R> R $traverse(Traverser<?, R> traverser) {
        return QOM.traverse(traverser, this,
            $array(),
            $index()
        );
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.ArrayGet) { QOM.ArrayGet<?> o = (QOM.ArrayGet<?>) that;
            return
                StringUtils.equals($array(), o.$array()) &&
                StringUtils.equals($index(), o.$index())
            ;
        }
        else
            return super.equals(that);
    }
}
