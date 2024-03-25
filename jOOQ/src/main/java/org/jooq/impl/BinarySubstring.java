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
import org.jooq.tools.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;


/**
 * The <code>BINARY SUBSTRING</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class BinarySubstring
extends
    AbstractField<byte[]>
implements
    QOM.BinarySubstring
{

    final Field<byte[]>           string;
    final Field<? extends Number> startingPosition;
    final Field<? extends Number> length;

    BinarySubstring(
        Field<byte[]> string,
        Field<? extends Number> startingPosition
    ) {
        super(
            N_BINARY_SUBSTRING,
            allNotNull(VARBINARY, string, startingPosition)
        );

        this.string = nullSafeNotNull(string, VARBINARY);
        this.startingPosition = nullSafeNotNull(startingPosition, INTEGER);
        this.length = null;
    }

    BinarySubstring(
        Field<byte[]> string,
        Field<? extends Number> startingPosition,
        Field<? extends Number> length
    ) {
        super(
            N_BINARY_SUBSTRING,
            allNotNull(VARBINARY, string, startingPosition, length)
        );

        this.string = nullSafeNotNull(string, VARBINARY);
        this.startingPosition = nullSafeNotNull(startingPosition, INTEGER);
        this.length = nullSafeNotNull(length, INTEGER);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

    @Override
    final boolean parenthesised(Context<?> ctx) {
        return true;
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {






            default:
                if (length != null)
                    ctx.visit(function(N_SUBSTRING, getDataType(), string, startingPosition, length));
                else
                    ctx.visit(function(N_SUBSTRING, getDataType(), string, startingPosition));
                break;
        }
    }















    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<byte[]> $arg1() {
        return string;
    }

    @Override
    public final Field<? extends Number> $arg2() {
        return startingPosition;
    }

    @Override
    public final Field<? extends Number> $arg3() {
        return length;
    }

    @Override
    public final QOM.BinarySubstring $arg1(Field<byte[]> newValue) {
        return $constructor().apply(newValue, $arg2(), $arg3());
    }

    @Override
    public final QOM.BinarySubstring $arg2(Field<? extends Number> newValue) {
        return $constructor().apply($arg1(), newValue, $arg3());
    }

    @Override
    public final QOM.BinarySubstring $arg3(Field<? extends Number> newValue) {
        return $constructor().apply($arg1(), $arg2(), newValue);
    }

    @Override
    public final Function3<? super Field<byte[]>, ? super Field<? extends Number>, ? super Field<? extends Number>, ? extends QOM.BinarySubstring> $constructor() {
        return (a1, a2, a3) -> new BinarySubstring(a1, a2, a3);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.BinarySubstring o) {
            return
                StringUtils.equals($string(), o.$string()) &&
                StringUtils.equals($startingPosition(), o.$startingPosition()) &&
                StringUtils.equals($length(), o.$length())
            ;
        }
        else
            return super.equals(that);
    }
}
