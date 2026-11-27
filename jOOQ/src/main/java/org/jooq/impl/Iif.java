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
 * The <code>IIF</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class Iif<T>
extends
    AbstractField<T>
implements
    QOM.Iif<T>
{

    final Condition condition;
    final Field<T>  ifTrue;
    final Field<T>  ifFalse;

    Iif(
        Condition condition,
        Field<T> ifTrue,
        Field<T> ifFalse
    ) {
        super(
            N_IIF,
            allNotNull((DataType) dataType(ifTrue), ifTrue, ifFalse)
        );

        this.condition = condition;
        this.ifTrue = nullSafeNotNull(ifTrue, (DataType) OTHER);
        this.ifFalse = nullSafeNotNull(ifFalse, (DataType) OTHER);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {








            case CLICKHOUSE:
            case MARIADB:
            case MYSQL: {
                ctx.visit(N_IF).sql('(').visit(condition).sql(", ").visit(ifTrue).sql(", ").visit(ifFalse).sql(')');
                break;
            }









            default:
                ctx.visit(DSL.when(condition, ifTrue).otherwise(ifFalse));
                break;
        }
    }















    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Condition $arg1() {
        return condition;
    }

    @Override
    public final Field<T> $arg2() {
        return ifTrue;
    }

    @Override
    public final Field<T> $arg3() {
        return ifFalse;
    }

    @Override
    public final QOM.Iif<T> $arg1(Condition newValue) {
        return $constructor().apply(newValue, $arg2(), $arg3());
    }

    @Override
    public final QOM.Iif<T> $arg2(Field<T> newValue) {
        return $constructor().apply($arg1(), newValue, $arg3());
    }

    @Override
    public final QOM.Iif<T> $arg3(Field<T> newValue) {
        return $constructor().apply($arg1(), $arg2(), newValue);
    }

    @Override
    public final Function3<? super Condition, ? super Field<T>, ? super Field<T>, ? extends QOM.Iif<T>> $constructor() {
        return (a1, a2, a3) -> new Iif<>(a1, a2, a3);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.Iif<?> o) {
            return
                Objects.equals($condition(), o.$condition()) &&
                Objects.equals($ifTrue(), o.$ifTrue()) &&
                Objects.equals($ifFalse(), o.$ifFalse())
            ;
        }
        else
            return super.equals(that);
    }
}

