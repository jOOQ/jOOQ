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
 * The <code>BIT SET</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class BitSet<T extends Number>
extends
    AbstractField<T>
implements
    QOM.BitSet<T>
{

    final Field<T>                value;
    final Field<? extends Number> bit;
    final Field<T>                newValue;

    BitSet(
        Field<T> value,
        Field<? extends Number> bit
    ) {
        super(
            N_BIT_SET,
            allNotNull((DataType) dataType(INTEGER, value, false), value, bit)
        );

        this.value = nullSafeNotNull(value, INTEGER);
        this.bit = nullSafeNotNull(bit, INTEGER);
        this.newValue = null;
    }

    BitSet(
        Field<T> value,
        Field<? extends Number> bit,
        Field<T> newValue
    ) {
        super(
            N_BIT_SET,
            allNotNull((DataType) dataType(INTEGER, value, false), value, bit, newValue)
        );

        this.value = nullSafeNotNull(value, INTEGER);
        this.bit = nullSafeNotNull(bit, INTEGER);
        this.newValue = nullSafeNotNull(newValue, INTEGER);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

    @Override
    final boolean parenthesised(Context<?> ctx) {
        switch (ctx.family()) {


























            case CUBRID:
            case FIREBIRD:
            case H2:
            case HSQLDB:
            case MARIADB:
            case MYSQL:
            case POSTGRES:
            case SQLITE:
            case TRINO:
            case YUGABYTEDB:
                return false;

            default:
                return true;
        }
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {






















































            case CUBRID:
            case FIREBIRD:
            case H2:
            case HSQLDB:
            case MARIADB:
            case MYSQL:
            case POSTGRES:
            case SQLITE:
            case TRINO:
            case YUGABYTEDB: {
                if (newValue == null)
                    ctx.visit(value.bitOr((Field) one().shl(bit)));
                else
                    ctx.visit(case_(newValue).when((Field) zero(), value.bitAnd((Field) one().shl(bit).bitNot())).when((Field) one(), value.bitOr((Field) one().shl(bit))));
                break;
            }

            default:
                if (newValue != null)
                    ctx.visit(function(N_BIT_SET, getDataType(), value, bit, newValue));
                else
                    ctx.visit(function(N_BIT_SET, getDataType(), value, bit));
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
        return bit;
    }

    @Override
    public final Field<T> $arg3() {
        return newValue;
    }

    @Override
    public final QOM.BitSet<T> $arg1(Field<T> newValue) {
        return $constructor().apply(newValue, $arg2(), $arg3());
    }

    @Override
    public final QOM.BitSet<T> $arg2(Field<? extends Number> newValue) {
        return $constructor().apply($arg1(), newValue, $arg3());
    }

    @Override
    public final QOM.BitSet<T> $arg3(Field<T> newValue) {
        return $constructor().apply($arg1(), $arg2(), newValue);
    }

    @Override
    public final Function3<? super Field<T>, ? super Field<? extends Number>, ? super Field<T>, ? extends QOM.BitSet<T>> $constructor() {
        return (a1, a2, a3) -> new BitSet<>(a1, a2, a3);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.BitSet<?> o) {
            return
                StringUtils.equals($value(), o.$value()) &&
                StringUtils.equals($bit(), o.$bit()) &&
                StringUtils.equals($newValue(), o.$newValue())
            ;
        }
        else
            return super.equals(that);
    }
}
