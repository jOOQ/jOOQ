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
 * The <code>BINARY OVERLAY</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class BinaryOverlay
extends
    AbstractField<byte[]>
implements
    QOM.BinaryOverlay
{

    final Field<byte[]>           in;
    final Field<byte[]>           placing;
    final Field<? extends Number> startIndex;
    final Field<? extends Number> length;

    BinaryOverlay(
        Field<byte[]> in,
        Field<byte[]> placing,
        Field<? extends Number> startIndex
    ) {
        super(
            N_BINARY_OVERLAY,
            allNotNull(VARBINARY, in, placing, startIndex)
        );

        this.in = nullSafeNotNull(in, VARBINARY);
        this.placing = nullSafeNotNull(placing, VARBINARY);
        this.startIndex = nullSafeNotNull(startIndex, INTEGER);
        this.length = null;
    }

    BinaryOverlay(
        Field<byte[]> in,
        Field<byte[]> placing,
        Field<? extends Number> startIndex,
        Field<? extends Number> length
    ) {
        super(
            N_BINARY_OVERLAY,
            allNotNull(VARBINARY, in, placing, startIndex, length)
        );

        this.in = nullSafeNotNull(in, VARBINARY);
        this.placing = nullSafeNotNull(placing, VARBINARY);
        this.startIndex = nullSafeNotNull(startIndex, INTEGER);
        this.length = nullSafeNotNull(length, INTEGER);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    @Override
    public final void accept(Context<?> ctx) {







        Overlay.accept0(ctx,
            in, placing, startIndex, length,
            DSL::binaryLength, DSL::binaryConcat, DSL::binarySubstring, DSL::binarySubstring
        );
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
        return placing;
    }

    @Override
    public final Field<? extends Number> $arg3() {
        return startIndex;
    }

    @Override
    public final Field<? extends Number> $arg4() {
        return length;
    }

    @Override
    public final QOM.BinaryOverlay $arg1(Field<byte[]> newValue) {
        return $constructor().apply(newValue, $arg2(), $arg3(), $arg4());
    }

    @Override
    public final QOM.BinaryOverlay $arg2(Field<byte[]> newValue) {
        return $constructor().apply($arg1(), newValue, $arg3(), $arg4());
    }

    @Override
    public final QOM.BinaryOverlay $arg3(Field<? extends Number> newValue) {
        return $constructor().apply($arg1(), $arg2(), newValue, $arg4());
    }

    @Override
    public final QOM.BinaryOverlay $arg4(Field<? extends Number> newValue) {
        return $constructor().apply($arg1(), $arg2(), $arg3(), newValue);
    }

    @Override
    public final Function4<? super Field<byte[]>, ? super Field<byte[]>, ? super Field<? extends Number>, ? super Field<? extends Number>, ? extends QOM.BinaryOverlay> $constructor() {
        return (a1, a2, a3, a4) -> new BinaryOverlay(a1, a2, a3, a4);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.BinaryOverlay o) {
            return
                Objects.equals($in(), o.$in()) &&
                Objects.equals($placing(), o.$placing()) &&
                Objects.equals($startIndex(), o.$startIndex()) &&
                Objects.equals($length(), o.$length())
            ;
        }
        else
            return super.equals(that);
    }
}
