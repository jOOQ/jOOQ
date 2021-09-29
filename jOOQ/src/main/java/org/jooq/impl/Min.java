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
// ...
import org.jooq.tools.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;


/**
 * The <code>MIN</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class Min<T>
extends
    AbstractAggregateFunction<T>
implements
    MMin<T>
{

    Min(
        Field<T> field,
        boolean distinct
    ) {
        super(
            distinct,
            N_MIN,
            Tools.nullSafeDataType(field),
            nullSafeNotNull(field, (DataType) OTHER)
        );
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        super.accept(ctx);
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    @Override
    public final Field<T> $field() {
        return (Field<T>) getArguments().get(0);
    }

    @Override
    public final MMin<T> $field(MField<T> newValue) {
        return constructor().apply(newValue, $distinct());
    }

    @Override
    public final MMin<T> $distinct(boolean newValue) {
        return constructor().apply($field(), newValue);
    }

    public final Function2<? super MField<T>, ? super Boolean, ? extends MMin<T>> constructor() {
        return (a1, a2) -> new Min<>((Field<T>) a1, a2);
    }

    @Override
    public final MQueryPart replace(Function1<? super MQueryPart, ? extends MQueryPart> replacement) {
        return QOM.replace(
            this,
            $field(),
            $distinct(),
            constructor()::apply,
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
        return super.traverse(
            QOM.traverse(
                init, abort, recurse, accumulate, this,
                $field()
            ), abort, recurse, accumulate
        );
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof Min) {
            return
                StringUtils.equals($field(), ((Min) that).$field()) &&
                $distinct() == ((Min) that).$distinct()
            ;
        }
        else
            return super.equals(that);
    }
}
