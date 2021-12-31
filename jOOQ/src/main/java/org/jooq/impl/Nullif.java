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
 * The <code>NULLIF</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class Nullif<T>
extends
    AbstractField<T>
implements
    QOM.Nullif<T>
{

    final Field<T> value;
    final Field<T> other;

    Nullif(
        Field<T> value,
        Field<T> other
    ) {
        super(
            N_NULLIF,
            nullable((DataType) dataType(value), value, other)
        );

        this.value = nullSafeNotNull(value, (DataType) OTHER);
        this.other = nullSafeNotNull(other, (DataType) OTHER);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {













            default:
                ctx.visit(function(N_NULLIF, getDataType(), value, other));
                break;
        }
    }














    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<T> $value() {
        return value;
    }

    @Override
    public final Field<T> $other() {
        return other;
    }

    @Override
    public final QOM.Nullif<T> $value(Field<T> newValue) {
        return constructor().apply(newValue, $other());
    }

    @Override
    public final QOM.Nullif<T> $other(Field<T> newValue) {
        return constructor().apply($value(), newValue);
    }

    public final Function2<? super Field<T>, ? super Field<T>, ? extends QOM.Nullif<T>> constructor() {
        return (a1, a2) -> new Nullif<>(a1, a2);
    }
























    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.Nullif) { QOM.Nullif<?> o = (QOM.Nullif<?>) that;
            return
                StringUtils.equals($value(), o.$value()) &&
                StringUtils.equals($other(), o.$other())
            ;
        }
        else
            return super.equals(that);
    }
}
