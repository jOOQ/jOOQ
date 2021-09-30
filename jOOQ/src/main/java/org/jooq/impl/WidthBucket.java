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
 * The <code>WIDTH BUCKET</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class WidthBucket<T extends Number>
extends
    AbstractField<T>
implements
    MWidthBucket<T>
{

    final Field<T>       field;
    final Field<T>       low;
    final Field<T>       high;
    final Field<Integer> buckets;

    WidthBucket(
        Field<T> field,
        Field<T> low,
        Field<T> high,
        Field<Integer> buckets
    ) {
        super(
            N_WIDTH_BUCKET,
            allNotNull((DataType) dataType(INTEGER, field, false), field, low, high, buckets)
        );

        this.field = nullSafeNotNull(field, INTEGER);
        this.low = nullSafeNotNull(low, INTEGER);
        this.high = nullSafeNotNull(high, INTEGER);
        this.buckets = nullSafeNotNull(buckets, INTEGER);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    @Override
    public void accept(Context<?> ctx) {
        switch (ctx.family()) {





            case POSTGRES:
            case YUGABYTE:
                ctx.visit(N_WIDTH_BUCKET).sql('(').visit(field).sql(", ").visit(low).sql(", ").visit(high).sql(", ").visit(buckets).sql(')');
                break;

            default:
                ctx.visit(
                    DSL.when(field.lt(low), zero())
                       .when(field.ge(high), iadd(buckets, one()))
                       .otherwise((Field<Integer>) iadd(
                           DSL.floor(idiv(
                               imul(isub(field, low), buckets),
                               isub(high, low)
                           )),
                           one()
                       ))
                );
                break;
        }
    }


















    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<T> $field() {
        return field;
    }

    @Override
    public final Field<T> $low() {
        return low;
    }

    @Override
    public final Field<T> $high() {
        return high;
    }

    @Override
    public final Field<Integer> $buckets() {
        return buckets;
    }

    @Override
    public final MWidthBucket<T> $field(MField<T> newValue) {
        return constructor().apply(newValue, $low(), $high(), $buckets());
    }

    @Override
    public final MWidthBucket<T> $low(MField<T> newValue) {
        return constructor().apply($field(), newValue, $high(), $buckets());
    }

    @Override
    public final MWidthBucket<T> $high(MField<T> newValue) {
        return constructor().apply($field(), $low(), newValue, $buckets());
    }

    @Override
    public final MWidthBucket<T> $buckets(MField<Integer> newValue) {
        return constructor().apply($field(), $low(), $high(), newValue);
    }

    public final Function4<? super MField<T>, ? super MField<T>, ? super MField<T>, ? super MField<Integer>, ? extends MWidthBucket<T>> constructor() {
        return (a1, a2, a3, a4) -> new WidthBucket<>((Field<T>) a1, (Field<T>) a2, (Field<T>) a3, (Field<Integer>) a4);
    }

    @Override
    public final MQueryPart replace(
        Predicate<? super MQueryPart> recurse,
        Function1<? super MQueryPart, ? extends MQueryPart> replacement
    ) {
        return QOM.replace(
            this,
            $field(),
            $low(),
            $high(),
            $buckets(),
            constructor()::apply,
            recurse,
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
            $field(),
            $low(),
            $high(),
            $buckets()
        );
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof WidthBucket) {
            return
                StringUtils.equals($field(), ((WidthBucket) that).$field()) &&
                StringUtils.equals($low(), ((WidthBucket) that).$low()) &&
                StringUtils.equals($high(), ((WidthBucket) that).$high()) &&
                StringUtils.equals($buckets(), ((WidthBucket) that).$buckets())
            ;
        }
        else
            return super.equals(that);
    }
}
