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
 * The <code>SHR</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class Shr<T extends Number>
extends
    AbstractField<T>
implements
    QOM.Shr<T>
{

    final Field<T>                value;
    final Field<? extends Number> count;

    Shr(
        Field<T> value,
        Field<? extends Number> count
    ) {
        super(
            N_SHR,
            allNotNull((DataType) dataType(INTEGER, value, false), value, count)
        );

        this.value = nullSafeNotNull(value, INTEGER);
        this.count = nullSafeNotNull(count, INTEGER);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {






            case FIREBIRD:
                ctx.visit(function(N_BIN_SHR, getDataType(), value, count));
                break;

            case H2:
                ctx.visit(function(N_RSHIFT, getDataType(), value, count));
                break;



















            case HSQLDB:
                ctx.visit(idiv(value, (Field<? extends Number>) castIfNeeded(DSL.power(two(), count), value)));
                break;

            default:
                ctx.sql('(').visit(value).sql(" >> ").visit(count).sql(')');
                break;
        }
    }










    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<T> $arg1() {
        return value;
    }

    @Override
    public final Field<? extends Number> $arg2() {
        return count;
    }

    @Override
    public final QOM.Shr<T> $arg1(Field<T> newValue) {
        return constructor().apply(newValue, $arg2());
    }

    @Override
    public final QOM.Shr<T> $arg2(Field<? extends Number> newValue) {
        return constructor().apply($arg1(), newValue);
    }

    @Override
    public final Function2<? super Field<T>, ? super Field<? extends Number>, ? extends QOM.Shr<T>> constructor() {
        return (a1, a2) -> new Shr<>(a1, a2);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.Shr) { QOM.Shr<?> o = (QOM.Shr<?>) that;
            return
                StringUtils.equals($value(), o.$value()) &&
                StringUtils.equals($count(), o.$count())
            ;
        }
        else
            return super.equals(that);
    }
}
