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
 * The <code>ARRAY REPLACE</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class ArrayReplace<T>
extends
    AbstractField<T[]>
implements
    QOM.ArrayReplace<T>
{

    final Field<T[]> arg1;
    final Field<T>   arg2;
    final Field<T>   arg3;

    ArrayReplace(
        Field<T[]> arg1,
        Field<T> arg2,
        Field<T> arg3
    ) {
        super(
            N_ARRAY_REPLACE,
            allNotNull((DataType) dataType(((DataType) OTHER).array(), arg1, false), arg1, arg2, arg3)
        );

        this.arg1 = nullSafeNotNull(arg1, ((DataType) OTHER).array());
        this.arg2 = nullSafeNotNull(arg2, (DataType) OTHER);
        this.arg3 = nullSafeNotNull(arg3, (DataType) OTHER);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

    @Override
    final boolean parenthesised(Context<?> ctx) {
        switch (ctx.family()) {
            case H2:
            case HSQLDB:
                return false;

            default:
                return true;
        }
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {






            case H2:
            case HSQLDB: {
                Field<T> x = DSL.field(name("x"), arg2.getDataType());
                Field<Long> o = DSL.field(name("o"), BIGINT);

                ctx.visit(DSL.field(
                    select(arrayAgg(when(x.isNotDistinctFrom(arg2), arg3).else_(x)).orderBy(o))
                    .from(unnest(arg1).withOrdinality().as("t", "x", "o"))
                ));
                break;
            }

            default:
                ctx.visit(function(N_ARRAY_REPLACE, getDataType(), arg1, arg2, arg3));
                break;
        }
    }















    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<T[]> $arg1() {
        return arg1;
    }

    @Override
    public final Field<T> $arg2() {
        return arg2;
    }

    @Override
    public final Field<T> $arg3() {
        return arg3;
    }

    @Override
    public final QOM.ArrayReplace<T> $arg1(Field<T[]> newValue) {
        return $constructor().apply(newValue, $arg2(), $arg3());
    }

    @Override
    public final QOM.ArrayReplace<T> $arg2(Field<T> newValue) {
        return $constructor().apply($arg1(), newValue, $arg3());
    }

    @Override
    public final QOM.ArrayReplace<T> $arg3(Field<T> newValue) {
        return $constructor().apply($arg1(), $arg2(), newValue);
    }

    @Override
    public final Function3<? super Field<T[]>, ? super Field<T>, ? super Field<T>, ? extends QOM.ArrayReplace<T>> $constructor() {
        return (a1, a2, a3) -> new ArrayReplace<>(a1, a2, a3);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.ArrayReplace<?> o) {
            return
                StringUtils.equals($arg1(), o.$arg1()) &&
                StringUtils.equals($arg2(), o.$arg2()) &&
                StringUtils.equals($arg3(), o.$arg3())
            ;
        }
        else
            return super.equals(that);
    }
}
