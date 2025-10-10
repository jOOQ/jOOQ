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
 * The <code>WIDTH BUCKET</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class WidthBucket<T extends Number>
extends
    AbstractField<T>
implements
    QOM.WidthBucket<T>
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
















            case CUBRID:
            case DERBY:
            case DUCKDB:
            case FIREBIRD:
            case H2:
            case HSQLDB:
            case IGNITE:
            case MARIADB:
            case MYSQL:
            case SQLITE:
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

            default:
                ctx.visit(N_WIDTH_BUCKET).sql('(').visit(field).sql(", ").visit(low).sql(", ").visit(high).sql(", ").visit(buckets).sql(')');
                break;
        }
    }


















    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<T> $arg1() {
        return field;
    }

    @Override
    public final Field<T> $arg2() {
        return low;
    }

    @Override
    public final Field<T> $arg3() {
        return high;
    }

    @Override
    public final Field<Integer> $arg4() {
        return buckets;
    }

    @Override
    public final QOM.WidthBucket<T> $arg1(Field<T> newValue) {
        return $constructor().apply(newValue, $arg2(), $arg3(), $arg4());
    }

    @Override
    public final QOM.WidthBucket<T> $arg2(Field<T> newValue) {
        return $constructor().apply($arg1(), newValue, $arg3(), $arg4());
    }

    @Override
    public final QOM.WidthBucket<T> $arg3(Field<T> newValue) {
        return $constructor().apply($arg1(), $arg2(), newValue, $arg4());
    }

    @Override
    public final QOM.WidthBucket<T> $arg4(Field<Integer> newValue) {
        return $constructor().apply($arg1(), $arg2(), $arg3(), newValue);
    }

    @Override
    public final Function4<? super Field<T>, ? super Field<T>, ? super Field<T>, ? super Field<Integer>, ? extends QOM.WidthBucket<T>> $constructor() {
        return (a1, a2, a3, a4) -> new WidthBucket<>(a1, a2, a3, a4);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.WidthBucket<?> o) {
            return
                Objects.equals($field(), o.$field()) &&
                Objects.equals($low(), o.$low()) &&
                Objects.equals($high(), o.$high()) &&
                Objects.equals($buckets(), o.$buckets())
            ;
        }
        else
            return super.equals(that);
    }
}
