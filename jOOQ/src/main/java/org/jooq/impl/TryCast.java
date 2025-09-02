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
 * The <code>TRY CAST</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class TryCast<T>
extends
    AbstractField<T>
implements
    QOM.TryCast<T>
{

    final Field<?>    value;
    final DataType<T> dataType;

    TryCast(
        Field<?> value,
        DataType<T> dataType
    ) {
        super(
            N_TRY_CAST,
            dataType
        );

        this.value = nullSafeNotNull(value, OTHER);
        this.dataType = dataType;
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {






            default:
                ctx.visit(new Cast.CastNative<>(value, dataType, true));
                break;
        }
    }














    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<?> $arg1() {
        return value;
    }

    @Override
    public final DataType<T> $arg2() {
        return dataType;
    }

    @Override
    public final QOM.TryCast<T> $arg1(Field<?> newValue) {
        return $constructor().apply(newValue, $arg2());
    }

    @Override
    public final QOM.TryCast<T> $arg2(DataType<T> newValue) {
        return $constructor().apply($arg1(), newValue);
    }

    @Override
    public final Function2<? super Field<?>, ? super DataType<T>, ? extends QOM.TryCast<T>> $constructor() {
        return (a1, a2) -> new TryCast<>(a1, a2);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.TryCast<?> o) {
            return
                Objects.equals($value(), o.$value()) &&
                Objects.equals($dataType(), o.$dataType())
            ;
        }
        else
            return super.equals(that);
    }
}
