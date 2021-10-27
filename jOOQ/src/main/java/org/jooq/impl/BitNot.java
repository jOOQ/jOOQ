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
 * The <code>BIT NOT</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class BitNot<T extends Number>
extends
    AbstractField<T>
implements
    QOM.BitNot<T>
{

    final Field<T> arg1;

    BitNot(
        Field<T> arg1
    ) {
        super(
            N_BIT_NOT,
            allNotNull((DataType) dataType(INTEGER, arg1, false), arg1)
        );

        this.arg1 = nullSafeNotNull(arg1, INTEGER);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {








            case HSQLDB:
                ctx.visit(isub(isub(zero(), arg1), one()));
                break;





            case H2:
                ctx.visit(function(N_BITNOT, getDataType(), arg1));
                break;

            case FIREBIRD:
                ctx.visit(function(N_BIN_NOT, getDataType(), arg1));
                break;

            default:
                ctx.sql("~(").visit(arg1).sql(')');
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
    public final QOM.BitNot<T> $arg1(Field<T> newValue) {
        return constructor().apply(newValue);
    }

    @Override
    public final Function1<? super Field<T>, ? extends QOM.BitNot<T>> constructor() {
        return (a1) -> new BitNot<>(a1);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.BitNot) { QOM.BitNot<?> o = (QOM.BitNot<?>) that;
            return
                StringUtils.equals($arg1(), o.$arg1())
            ;
        }
        else
            return super.equals(that);
    }
}
