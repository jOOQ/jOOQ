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
    final boolean parenthesised(Context<?> ctx) {
        switch (ctx.family()) {











            case DUCKDB:
            case SQLITE:
                return false;

            default:
                return true;
        }
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {



















            case DUCKDB:
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
        return $constructor().apply(newValue, $arg2());
    }

    @Override
    public final QOM.Mod<T> $arg2(Field<? extends Number> newValue) {
        return $constructor().apply($arg1(), newValue);
    }

    @Override
    public final Function2<? super Field<T>, ? super Field<? extends Number>, ? extends QOM.Mod<T>> $constructor() {
        return (a1, a2) -> new Mod<>(a1, a2);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.Mod<?> o) {
            return
                Objects.equals($dividend(), o.$dividend()) &&
                Objects.equals($divisor(), o.$divisor())
            ;
        }
        else
            return super.equals(that);
    }
}
