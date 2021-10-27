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
 * The <code>BIT XOR</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class BitXor<T extends Number>
extends
    AbstractField<T>
implements
    QOM.BitXor<T>
{

    final Field<T> arg1;
    final Field<T> arg2;

    BitXor(
        Field<T> arg1,
        Field<T> arg2
    ) {
        super(
            N_BIT_XOR,
            allNotNull((DataType) dataType(INTEGER, arg1, false), arg1, arg2)
        );

        this.arg1 = nullSafeNotNull(arg1, INTEGER);
        this.arg2 = nullSafeNotNull(arg2, INTEGER);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {











            case H2:
            case HSQLDB:
                ctx.visit(function(N_BITXOR, getDataType(), arg1, arg2));
                break;

            case FIREBIRD:
                ctx.visit(function(N_BIN_XOR, getDataType(), arg1, arg2));
                break;


            case SQLITE:
                ctx.visit(// ~(a & b) & (a | b)
                DSL.bitAnd(
                    DSL.bitNot(DSL.bitAnd((Field<Number>) arg1, (Field<Number>) arg2)),
                    DSL.bitOr((Field<Number>) arg1, (Field<Number>) arg2)
                ));
                break;

            default:
                ctx.sql('(');
                SQL op = Expression.HASH_OP_FOR_BIT_XOR.contains(ctx.dialect()) ? Operators.OP_NUM : Operators.OP_HAT;
                Expression.<Field<T>, BitXor<T>>acceptAssociative(
                    ctx,
                    this,
                    q -> new Expression.Expr<>(q.arg1, op, q.arg2),
                    c -> c.sql(' ')
                );
                ctx.sql(')');
                break;
        }
    }










    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<T> $arg1() {
        return arg1;
    }

    @Override
    public final Field<T> $arg2() {
        return arg2;
    }

    @Override
    public final QOM.BitXor<T> $arg1(Field<T> newValue) {
        return constructor().apply(newValue, $arg2());
    }

    @Override
    public final QOM.BitXor<T> $arg2(Field<T> newValue) {
        return constructor().apply($arg1(), newValue);
    }

    @Override
    public final Function2<? super Field<T>, ? super Field<T>, ? extends QOM.BitXor<T>> constructor() {
        return (a1, a2) -> new BitXor<>(a1, a2);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.BitXor) { QOM.BitXor<?> o = (QOM.BitXor<?>) that;
            return
                StringUtils.equals($arg1(), o.$arg1()) &&
                StringUtils.equals($arg2(), o.$arg2())
            ;
        }
        else
            return super.equals(that);
    }
}
