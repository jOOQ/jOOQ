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
import org.jooq.Record;
import org.jooq.conf.*;
import org.jooq.impl.*;
import org.jooq.tools.*;

import java.util.*;


/**
 * The <code>BIT XOR AGG</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class BitXorAgg<T extends Number>
extends
    DefaultAggregateFunction<T>
{

    BitXorAgg(
        Field<T> value
    ) {
        super(
            false,
            N_BIT_XOR_AGG,
            Tools.nullSafeDataType(value),
            nullSafeNotNull(value, INTEGER)
        );
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    public static final Set<SQLDialect> NO_SUPPORT_NATIVE = SQLDialect.supportedUntil(CUBRID, DERBY, FIREBIRD, H2, HSQLDB, IGNITE, POSTGRES, SQLITE);

    @Override
    public final void accept(Context<?> ctx) {
        if (NO_SUPPORT_NATIVE.contains(ctx.dialect())) {
            Field<?> field = getArguments().get(0);

            // TODO: Is 2's complement implemented correctly?
            if (field.getType() == Byte.class) {
                Field<Byte> f = (Field<Byte>) field;
                Field<Byte> b0 = inline((byte) 0);
                Field<Byte> b2 = inline((byte) 2);

                ctx.visit(
                          when(o(DSL.count().filterWhere(bitCheck(f, (byte) 0x01))).mod(b2).eq(one()), inline((byte) 0x01)).else_(b0)
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, (byte) 0x02))).mod(b2).eq(one()), inline((byte) 0x02)).else_(b0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, (byte) 0x04))).mod(b2).eq(one()), inline((byte) 0x04)).else_(b0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, (byte) 0x08))).mod(b2).eq(one()), inline((byte) 0x08)).else_(b0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, (byte) 0x10))).mod(b2).eq(one()), inline((byte) 0x10)).else_(b0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, (byte) 0x20))).mod(b2).eq(one()), inline((byte) 0x20)).else_(b0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, (byte) 0x40))).mod(b2).eq(one()), inline((byte) 0x40)).else_(b0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, (byte) 0x80))).mod(b2).eq(one()), inline((byte) 0x80)).else_(b0))
                );
            }
            else if (field.getType() == Short.class) {
                Field<Short> f = (Field<Short>) field;
                Field<Short> s0 = inline((short) 0);
                Field<Short> s2 = inline((short) 2);

                ctx.visit(
                          when(o(DSL.count().filterWhere(bitCheck(f, (short) 0x0001))).mod(s2).eq(one()), inline((short) 0x0001)).else_(s0)
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, (short) 0x0002))).mod(s2).eq(one()), inline((short) 0x0002)).else_(s0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, (short) 0x0004))).mod(s2).eq(one()), inline((short) 0x0004)).else_(s0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, (short) 0x0008))).mod(s2).eq(one()), inline((short) 0x0008)).else_(s0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, (short) 0x0010))).mod(s2).eq(one()), inline((short) 0x0010)).else_(s0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, (short) 0x0020))).mod(s2).eq(one()), inline((short) 0x0020)).else_(s0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, (short) 0x0040))).mod(s2).eq(one()), inline((short) 0x0040)).else_(s0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, (short) 0x0080))).mod(s2).eq(one()), inline((short) 0x0080)).else_(s0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, (short) 0x0100))).mod(s2).eq(one()), inline((short) 0x0100)).else_(s0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, (short) 0x0200))).mod(s2).eq(one()), inline((short) 0x0200)).else_(s0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, (short) 0x0400))).mod(s2).eq(one()), inline((short) 0x0400)).else_(s0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, (short) 0x0800))).mod(s2).eq(one()), inline((short) 0x0800)).else_(s0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, (short) 0x1000))).mod(s2).eq(one()), inline((short) 0x1000)).else_(s0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, (short) 0x2000))).mod(s2).eq(one()), inline((short) 0x2000)).else_(s0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, (short) 0x4000))).mod(s2).eq(one()), inline((short) 0x4000)).else_(s0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, (short) 0x8000))).mod(s2).eq(one()), inline((short) 0x8000)).else_(s0))
                );
            }
            else if (field.getType() == Integer.class) {
                Field<Integer> f = (Field<Integer>) field;
                Field<Integer> i0 = inline(0);
                Field<Integer> i2 = inline(2);

                ctx.visit(
                          when(o(DSL.count().filterWhere(bitCheck(f, 0x00000001))).mod(i2).eq(one()), inline(0x00000001)).else_(i0)
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x00000002))).mod(i2).eq(one()), inline(0x00000002)).else_(i0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x00000004))).mod(i2).eq(one()), inline(0x00000004)).else_(i0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x00000008))).mod(i2).eq(one()), inline(0x00000008)).else_(i0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x00000010))).mod(i2).eq(one()), inline(0x00000010)).else_(i0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x00000020))).mod(i2).eq(one()), inline(0x00000020)).else_(i0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x00000040))).mod(i2).eq(one()), inline(0x00000040)).else_(i0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x00000080))).mod(i2).eq(one()), inline(0x00000080)).else_(i0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x00000100))).mod(i2).eq(one()), inline(0x00000100)).else_(i0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x00000200))).mod(i2).eq(one()), inline(0x00000200)).else_(i0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x00000400))).mod(i2).eq(one()), inline(0x00000400)).else_(i0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x00000800))).mod(i2).eq(one()), inline(0x00000800)).else_(i0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x00001000))).mod(i2).eq(one()), inline(0x00001000)).else_(i0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x00002000))).mod(i2).eq(one()), inline(0x00002000)).else_(i0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x00004000))).mod(i2).eq(one()), inline(0x00004000)).else_(i0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x00008000))).mod(i2).eq(one()), inline(0x00008000)).else_(i0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x00010000))).mod(i2).eq(one()), inline(0x00010000)).else_(i0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x00020000))).mod(i2).eq(one()), inline(0x00020000)).else_(i0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x00040000))).mod(i2).eq(one()), inline(0x00040000)).else_(i0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x00080000))).mod(i2).eq(one()), inline(0x00080000)).else_(i0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x00100000))).mod(i2).eq(one()), inline(0x00100000)).else_(i0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x00200000))).mod(i2).eq(one()), inline(0x00200000)).else_(i0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x00400000))).mod(i2).eq(one()), inline(0x00400000)).else_(i0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x00800000))).mod(i2).eq(one()), inline(0x00800000)).else_(i0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x01000000))).mod(i2).eq(one()), inline(0x01000000)).else_(i0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x02000000))).mod(i2).eq(one()), inline(0x02000000)).else_(i0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x04000000))).mod(i2).eq(one()), inline(0x04000000)).else_(i0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x08000000))).mod(i2).eq(one()), inline(0x08000000)).else_(i0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x10000000))).mod(i2).eq(one()), inline(0x10000000)).else_(i0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x20000000))).mod(i2).eq(one()), inline(0x20000000)).else_(i0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x40000000))).mod(i2).eq(one()), inline(0x40000000)).else_(i0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x80000000))).mod(i2).eq(one()), inline(0x80000000)).else_(i0))
                );
            }
            else if (field.getType() == Long.class) {
                Field<Long> f = (Field<Long>) field;
                Field<Long> l0 = inline(0L);
                Field<Long> l2 = inline(2L);

                ctx.visit(
                          when(o(DSL.count().filterWhere(bitCheck(f, 0x0000000000000001L))).mod(l2).eq(one()), inline(0x0000000000000001L)).else_(l0)
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x0000000000000002L))).mod(l2).eq(one()), inline(0x0000000000000002L)).else_(l0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x0000000000000004L))).mod(l2).eq(one()), inline(0x0000000000000004L)).else_(l0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x0000000000000008L))).mod(l2).eq(one()), inline(0x0000000000000008L)).else_(l0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x0000000000000010L))).mod(l2).eq(one()), inline(0x0000000000000010L)).else_(l0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x0000000000000020L))).mod(l2).eq(one()), inline(0x0000000000000020L)).else_(l0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x0000000000000040L))).mod(l2).eq(one()), inline(0x0000000000000040L)).else_(l0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x0000000000000080L))).mod(l2).eq(one()), inline(0x0000000000000080L)).else_(l0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x0000000000000100L))).mod(l2).eq(one()), inline(0x0000000000000100L)).else_(l0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x0000000000000200L))).mod(l2).eq(one()), inline(0x0000000000000200L)).else_(l0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x0000000000000400L))).mod(l2).eq(one()), inline(0x0000000000000400L)).else_(l0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x0000000000000800L))).mod(l2).eq(one()), inline(0x0000000000000800L)).else_(l0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x0000000000001000L))).mod(l2).eq(one()), inline(0x0000000000001000L)).else_(l0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x0000000000002000L))).mod(l2).eq(one()), inline(0x0000000000002000L)).else_(l0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x0000000000004000L))).mod(l2).eq(one()), inline(0x0000000000004000L)).else_(l0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x0000000000008000L))).mod(l2).eq(one()), inline(0x0000000000008000L)).else_(l0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x0000000000010000L))).mod(l2).eq(one()), inline(0x0000000000010000L)).else_(l0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x0000000000020000L))).mod(l2).eq(one()), inline(0x0000000000020000L)).else_(l0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x0000000000040000L))).mod(l2).eq(one()), inline(0x0000000000040000L)).else_(l0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x0000000000080000L))).mod(l2).eq(one()), inline(0x0000000000080000L)).else_(l0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x0000000000100000L))).mod(l2).eq(one()), inline(0x0000000000100000L)).else_(l0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x0000000000200000L))).mod(l2).eq(one()), inline(0x0000000000200000L)).else_(l0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x0000000000400000L))).mod(l2).eq(one()), inline(0x0000000000400000L)).else_(l0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x0000000000800000L))).mod(l2).eq(one()), inline(0x0000000000800000L)).else_(l0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x0000000001000000L))).mod(l2).eq(one()), inline(0x0000000001000000L)).else_(l0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x0000000002000000L))).mod(l2).eq(one()), inline(0x0000000002000000L)).else_(l0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x0000000004000000L))).mod(l2).eq(one()), inline(0x0000000004000000L)).else_(l0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x0000000008000000L))).mod(l2).eq(one()), inline(0x0000000008000000L)).else_(l0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x0000000010000000L))).mod(l2).eq(one()), inline(0x0000000010000000L)).else_(l0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x0000000020000000L))).mod(l2).eq(one()), inline(0x0000000020000000L)).else_(l0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x0000000040000000L))).mod(l2).eq(one()), inline(0x0000000040000000L)).else_(l0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x0000000080000000L))).mod(l2).eq(one()), inline(0x0000000080000000L)).else_(l0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x0000000100000000L))).mod(l2).eq(one()), inline(0x0000000100000000L)).else_(l0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x0000000200000000L))).mod(l2).eq(one()), inline(0x0000000200000000L)).else_(l0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x0000000400000000L))).mod(l2).eq(one()), inline(0x0000000400000000L)).else_(l0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x0000000800000000L))).mod(l2).eq(one()), inline(0x0000000800000000L)).else_(l0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x0000001000000000L))).mod(l2).eq(one()), inline(0x0000001000000000L)).else_(l0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x0000002000000000L))).mod(l2).eq(one()), inline(0x0000002000000000L)).else_(l0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x0000004000000000L))).mod(l2).eq(one()), inline(0x0000004000000000L)).else_(l0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x0000008000000000L))).mod(l2).eq(one()), inline(0x0000008000000000L)).else_(l0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x0000010000000000L))).mod(l2).eq(one()), inline(0x0000010000000000L)).else_(l0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x0000020000000000L))).mod(l2).eq(one()), inline(0x0000020000000000L)).else_(l0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x0000040000000000L))).mod(l2).eq(one()), inline(0x0000040000000000L)).else_(l0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x0000080000000000L))).mod(l2).eq(one()), inline(0x0000080000000000L)).else_(l0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x0000100000000000L))).mod(l2).eq(one()), inline(0x0000100000000000L)).else_(l0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x0000200000000000L))).mod(l2).eq(one()), inline(0x0000200000000000L)).else_(l0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x0000400000000000L))).mod(l2).eq(one()), inline(0x0000400000000000L)).else_(l0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x0000800000000000L))).mod(l2).eq(one()), inline(0x0000800000000000L)).else_(l0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x0001000000000000L))).mod(l2).eq(one()), inline(0x0001000000000000L)).else_(l0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x0002000000000000L))).mod(l2).eq(one()), inline(0x0002000000000000L)).else_(l0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x0004000000000000L))).mod(l2).eq(one()), inline(0x0004000000000000L)).else_(l0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x0008000000000000L))).mod(l2).eq(one()), inline(0x0008000000000000L)).else_(l0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x0010000000000000L))).mod(l2).eq(one()), inline(0x0010000000000000L)).else_(l0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x0020000000000000L))).mod(l2).eq(one()), inline(0x0020000000000000L)).else_(l0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x0040000000000000L))).mod(l2).eq(one()), inline(0x0040000000000000L)).else_(l0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x0080000000000000L))).mod(l2).eq(one()), inline(0x0080000000000000L)).else_(l0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x0100000000000000L))).mod(l2).eq(one()), inline(0x0100000000000000L)).else_(l0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x0200000000000000L))).mod(l2).eq(one()), inline(0x0200000000000000L)).else_(l0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x0400000000000000L))).mod(l2).eq(one()), inline(0x0400000000000000L)).else_(l0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x0800000000000000L))).mod(l2).eq(one()), inline(0x0800000000000000L)).else_(l0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x1000000000000000L))).mod(l2).eq(one()), inline(0x1000000000000000L)).else_(l0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x2000000000000000L))).mod(l2).eq(one()), inline(0x2000000000000000L)).else_(l0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x4000000000000000L))).mod(l2).eq(one()), inline(0x4000000000000000L)).else_(l0))
                    .plus(when(o(DSL.count().filterWhere(bitCheck(f, 0x8000000000000000L))).mod(l2).eq(one()), inline(0x8000000000000000L)).else_(l0))
                );
            }
            // Currently not supported
            else
                super.accept(ctx);
        }
        else
            super.accept(ctx);
    }

    private final <N extends Number> Condition bitCheck(Field<N> field, N mask) {

        // [#11956] TODO: Potentially refactor this to use GETBIT()
        return f(DSL.bitAnd(field, inline(mask)).eq(inline(mask)));
    }

    @Override
    final void acceptFunctionName(Context<?> ctx) {
        switch (ctx.family()) {





            case H2:
            case IGNITE:
            case MARIADB:
            case MYSQL:
            case POSTGRES:
                ctx.visit(N_BIT_XOR);
                break;

            default:
                super.acceptFunctionName(ctx);
                break;
        }
    }


}
