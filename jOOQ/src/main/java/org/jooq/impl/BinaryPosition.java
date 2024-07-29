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
 * Apache-2.0 and offer limited warranties, support, maintenance, and commercial
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
import org.jooq.conf.ParamType;
import org.jooq.tools.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;



/**
 * The <code>BINARY POSITION</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class BinaryPosition
extends
    AbstractField<Integer>
implements
    QOM.BinaryPosition
{

    final Field<byte[]>           in;
    final Field<byte[]>           search;
    final Field<? extends Number> startIndex;

    BinaryPosition(
        Field<byte[]> in,
        Field<byte[]> search
    ) {
        super(
            N_BINARY_POSITION,
            allNotNull(INTEGER, in, search)
        );

        this.in = nullSafeNotNull(in, VARBINARY);
        this.search = nullSafeNotNull(search, VARBINARY);
        this.startIndex = null;
    }

    BinaryPosition(
        Field<byte[]> in,
        Field<byte[]> search,
        Field<? extends Number> startIndex
    ) {
        super(
            N_BINARY_POSITION,
            allNotNull(INTEGER, in, search, startIndex)
        );

        this.in = nullSafeNotNull(in, VARBINARY);
        this.search = nullSafeNotNull(search, VARBINARY);
        this.startIndex = nullSafeNotNull(startIndex, INTEGER);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    @Override
    public final void accept(Context<?> ctx) {







        Position.accept0(ctx, getDataType(), in, search, startIndex, DSL::binaryPosition, DSL::binarySubstring);
    }

















    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<byte[]> $arg1() {
        return in;
    }

    @Override
    public final Field<byte[]> $arg2() {
        return search;
    }

    @Override
    public final Field<? extends Number> $arg3() {
        return startIndex;
    }

    @Override
    public final QOM.BinaryPosition $arg1(Field<byte[]> newValue) {
        return $constructor().apply(newValue, $arg2(), $arg3());
    }

    @Override
    public final QOM.BinaryPosition $arg2(Field<byte[]> newValue) {
        return $constructor().apply($arg1(), newValue, $arg3());
    }

    @Override
    public final QOM.BinaryPosition $arg3(Field<? extends Number> newValue) {
        return $constructor().apply($arg1(), $arg2(), newValue);
    }

    @Override
    public final Function3<? super Field<byte[]>, ? super Field<byte[]>, ? super Field<? extends Number>, ? extends QOM.BinaryPosition> $constructor() {
        return (a1, a2, a3) -> new BinaryPosition(a1, a2, a3);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.BinaryPosition o) {
            return
                StringUtils.equals($in(), o.$in()) &&
                StringUtils.equals($search(), o.$search()) &&
                StringUtils.equals($startIndex(), o.$startIndex())
            ;
        }
        else
            return super.equals(that);
    }
}
