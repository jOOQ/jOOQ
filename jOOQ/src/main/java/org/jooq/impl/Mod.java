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
 * The <code>MOD</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class Mod<T extends Number>
extends
    AbstractField<T>
implements
    QOM.Mod<T>
{

    final Field<T>                dividend;
    final Field<? extends Number> divisor;

    Mod(
        Field<T> dividend,
        Field<? extends Number> divisor
    ) {
        super(
            N_MOD,
            allNotNull((DataType) dataType(INTEGER, dividend, false), dividend, divisor)
        );

        this.dividend = nullSafeNotNull(dividend, INTEGER);
        this.divisor = nullSafeNotNull(divisor, INTEGER);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {


















            case SQLITE: {
                ctx.sql('(').visit(dividend).sql(" % ").visit(divisor).sql(')');
                break;
            }

            default:
                ctx.visit(function(N_MOD, getDataType(), dividend, divisor));
                break;
        }
    }










    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<T> $arg1() {
        return dividend;
    }

    @Override
    public final Field<? extends Number> $arg2() {
        return divisor;
    }

    @Override
    public final QOM.Mod<T> $arg1(Field<T> newValue) {
        return constructor().apply(newValue, $arg2());
    }

    @Override
    public final QOM.Mod<T> $arg2(Field<? extends Number> newValue) {
        return constructor().apply($arg1(), newValue);
    }

    @Override
    public final Function2<? super Field<T>, ? super Field<? extends Number>, ? extends QOM.Mod<T>> constructor() {
        return (a1, a2) -> new Mod<>(a1, a2);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.Mod) { QOM.Mod<?> o = (QOM.Mod<?>) that;
            return
                StringUtils.equals($dividend(), o.$dividend()) &&
                StringUtils.equals($divisor(), o.$divisor())
            ;
        }
        else
            return super.equals(that);
    }
}
