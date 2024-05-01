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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


/**
 * The <code>ARRAY OVERLAP</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class ArrayOverlap<T>
extends
    AbstractCondition
implements
    QOM.ArrayOverlap<T>
{

    final Field<T[]> arg1;
    final Field<T[]> arg2;

    ArrayOverlap(
        Field<T[]> arg1,
        Field<T[]> arg2
    ) {

        this.arg1 = nullSafeNotNull(arg1, ((DataType) OTHER).array());
        this.arg2 = nullSafeNotNull(arg2, ((DataType) OTHER).array());
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {






            case H2:
                ctx.visit(exists(
                    select(asterisk()).from(unnest(arg1))
                    .intersect(select(asterisk()).from(unnest(arg2)))
                ));
                break;

            case HSQLDB:
                ctx.visit(exists(
                    select(asterisk()).from(unnest(arg1))
                    .intersectAll(select(asterisk()).from(unnest(arg2)))
                ));
                break;

            default:
                ctx.sql('(').visit(arg1).sql(" && ").visit(arg2).sql(')');
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
    public final Field<T[]> $arg2() {
        return arg2;
    }

    @Override
    public final QOM.ArrayOverlap<T> $arg1(Field<T[]> newValue) {
        return $constructor().apply(newValue, $arg2());
    }

    @Override
    public final QOM.ArrayOverlap<T> $arg2(Field<T[]> newValue) {
        return $constructor().apply($arg1(), newValue);
    }

    @Override
    public final Function2<? super Field<T[]>, ? super Field<T[]>, ? extends QOM.ArrayOverlap<T>> $constructor() {
        return (a1, a2) -> new ArrayOverlap<>(a1, a2);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.ArrayOverlap<?> o) {
            return
                StringUtils.equals($arg1(), o.$arg1()) &&
                StringUtils.equals($arg2(), o.$arg2())
            ;
        }
        else
            return super.equals(that);
    }
}
