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
 * The <code>BIT OR AGG</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class BitOrAgg<T extends Number>
extends
    AbstractAggregateFunction<T>
implements
    QOM.BitOrAgg<T>
{

    BitOrAgg(
        Field<T> value
    ) {
        super(
            false,
            N_BIT_OR_AGG,
            Tools.nullSafeDataType(value),
            nullSafeNotNull(value, INTEGER)
        );
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    public static final Set<SQLDialect> NO_SUPPORT_NATIVE = SQLDialect.supportedUntil(CUBRID, DERBY, FIREBIRD, HSQLDB, SQLITE);

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
                          case_(fo(DSL.max(case_(DSL.bitAnd(f, inline((byte) 0x01))).when(b0, b0).when(inline((byte) 0x01), inline((byte) 0x01))))).when((byte) 0x01, (byte) 0x01).when(b0, b0)
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline((byte) 0x02))).when(b0, b0).when(inline((byte) 0x02), inline((byte) 0x02))))).when((byte) 0x02, (byte) 0x02).when(b0, b0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline((byte) 0x04))).when(b0, b0).when(inline((byte) 0x04), inline((byte) 0x04))))).when((byte) 0x04, (byte) 0x04).when(b0, b0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline((byte) 0x08))).when(b0, b0).when(inline((byte) 0x08), inline((byte) 0x08))))).when((byte) 0x08, (byte) 0x08).when(b0, b0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline((byte) 0x10))).when(b0, b0).when(inline((byte) 0x10), inline((byte) 0x10))))).when((byte) 0x10, (byte) 0x10).when(b0, b0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline((byte) 0x20))).when(b0, b0).when(inline((byte) 0x20), inline((byte) 0x20))))).when((byte) 0x20, (byte) 0x20).when(b0, b0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline((byte) 0x40))).when(b0, b0).when(inline((byte) 0x40), inline((byte) 0x40))))).when((byte) 0x40, (byte) 0x40).when(b0, b0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline((byte) 0x80))).when(b0, b0).when(inline((byte) 0x80), inline((byte) 0x80))))).when((byte) 0x80, (byte) 0x80).when(b0, b0))
                );
            }
            else if (field.getType() == Short.class) {
                Field<Short> f = (Field<Short>) field;
                Field<Short> s0 = inline((short) 0);
                Field<Short> s2 = inline((short) 2);

                ctx.visit(
                          case_(fo(DSL.max(case_(DSL.bitAnd(f, inline((short) 0x0001))).when(s0, s0).when(inline((short) 0x0001), inline((short) 0x0001))))).when((short) 0x0001, (short) 0x0001).when(s0, s0)
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline((short) 0x0002))).when(s0, s0).when(inline((short) 0x0002), inline((short) 0x0002))))).when((short) 0x0002, (short) 0x0002).when(s0, s0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline((short) 0x0004))).when(s0, s0).when(inline((short) 0x0004), inline((short) 0x0004))))).when((short) 0x0004, (short) 0x0004).when(s0, s0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline((short) 0x0008))).when(s0, s0).when(inline((short) 0x0008), inline((short) 0x0008))))).when((short) 0x0008, (short) 0x0008).when(s0, s0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline((short) 0x0010))).when(s0, s0).when(inline((short) 0x0010), inline((short) 0x0010))))).when((short) 0x0010, (short) 0x0010).when(s0, s0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline((short) 0x0020))).when(s0, s0).when(inline((short) 0x0020), inline((short) 0x0020))))).when((short) 0x0020, (short) 0x0020).when(s0, s0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline((short) 0x0040))).when(s0, s0).when(inline((short) 0x0040), inline((short) 0x0040))))).when((short) 0x0040, (short) 0x0040).when(s0, s0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline((short) 0x0080))).when(s0, s0).when(inline((short) 0x0080), inline((short) 0x0080))))).when((short) 0x0080, (short) 0x0080).when(s0, s0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline((short) 0x0100))).when(s0, s0).when(inline((short) 0x0100), inline((short) 0x0100))))).when((short) 0x0100, (short) 0x0100).when(s0, s0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline((short) 0x0200))).when(s0, s0).when(inline((short) 0x0200), inline((short) 0x0200))))).when((short) 0x0200, (short) 0x0200).when(s0, s0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline((short) 0x0400))).when(s0, s0).when(inline((short) 0x0400), inline((short) 0x0400))))).when((short) 0x0400, (short) 0x0400).when(s0, s0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline((short) 0x0800))).when(s0, s0).when(inline((short) 0x0800), inline((short) 0x0800))))).when((short) 0x0800, (short) 0x0800).when(s0, s0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline((short) 0x1000))).when(s0, s0).when(inline((short) 0x1000), inline((short) 0x1000))))).when((short) 0x1000, (short) 0x1000).when(s0, s0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline((short) 0x2000))).when(s0, s0).when(inline((short) 0x2000), inline((short) 0x2000))))).when((short) 0x2000, (short) 0x2000).when(s0, s0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline((short) 0x4000))).when(s0, s0).when(inline((short) 0x4000), inline((short) 0x4000))))).when((short) 0x4000, (short) 0x4000).when(s0, s0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline((short) 0x8000))).when(s0, s0).when(inline((short) 0x8000), inline((short) 0x8000))))).when((short) 0x8000, (short) 0x8000).when(s0, s0))
                );
            }
            else if (field.getType() == Integer.class) {
                Field<Integer> f = (Field<Integer>) field;
                Field<Integer> i0 = inline(0);
                Field<Integer> i2 = inline(2);

                ctx.visit(
                          case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x00000001))).when(i0, i0).when(inline(0x00000001), inline(0x00000001))))).when(0x00000001, 0x00000001).when(i0, i0)
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x00000002))).when(i0, i0).when(inline(0x00000002), inline(0x00000002))))).when(0x00000002, 0x00000002).when(i0, i0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x00000004))).when(i0, i0).when(inline(0x00000004), inline(0x00000004))))).when(0x00000004, 0x00000004).when(i0, i0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x00000008))).when(i0, i0).when(inline(0x00000008), inline(0x00000008))))).when(0x00000008, 0x00000008).when(i0, i0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x00000010))).when(i0, i0).when(inline(0x00000010), inline(0x00000010))))).when(0x00000010, 0x00000010).when(i0, i0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x00000020))).when(i0, i0).when(inline(0x00000020), inline(0x00000020))))).when(0x00000020, 0x00000020).when(i0, i0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x00000040))).when(i0, i0).when(inline(0x00000040), inline(0x00000040))))).when(0x00000040, 0x00000040).when(i0, i0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x00000080))).when(i0, i0).when(inline(0x00000080), inline(0x00000080))))).when(0x00000080, 0x00000080).when(i0, i0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x00000100))).when(i0, i0).when(inline(0x00000100), inline(0x00000100))))).when(0x00000100, 0x00000100).when(i0, i0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x00000200))).when(i0, i0).when(inline(0x00000200), inline(0x00000200))))).when(0x00000200, 0x00000200).when(i0, i0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x00000400))).when(i0, i0).when(inline(0x00000400), inline(0x00000400))))).when(0x00000400, 0x00000400).when(i0, i0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x00000800))).when(i0, i0).when(inline(0x00000800), inline(0x00000800))))).when(0x00000800, 0x00000800).when(i0, i0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x00001000))).when(i0, i0).when(inline(0x00001000), inline(0x00001000))))).when(0x00001000, 0x00001000).when(i0, i0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x00002000))).when(i0, i0).when(inline(0x00002000), inline(0x00002000))))).when(0x00002000, 0x00002000).when(i0, i0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x00004000))).when(i0, i0).when(inline(0x00004000), inline(0x00004000))))).when(0x00004000, 0x00004000).when(i0, i0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x00008000))).when(i0, i0).when(inline(0x00008000), inline(0x00008000))))).when(0x00008000, 0x00008000).when(i0, i0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x00010000))).when(i0, i0).when(inline(0x00010000), inline(0x00010000))))).when(0x00010000, 0x00010000).when(i0, i0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x00020000))).when(i0, i0).when(inline(0x00020000), inline(0x00020000))))).when(0x00020000, 0x00020000).when(i0, i0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x00040000))).when(i0, i0).when(inline(0x00040000), inline(0x00040000))))).when(0x00040000, 0x00040000).when(i0, i0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x00080000))).when(i0, i0).when(inline(0x00080000), inline(0x00080000))))).when(0x00080000, 0x00080000).when(i0, i0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x00100000))).when(i0, i0).when(inline(0x00100000), inline(0x00100000))))).when(0x00100000, 0x00100000).when(i0, i0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x00200000))).when(i0, i0).when(inline(0x00200000), inline(0x00200000))))).when(0x00200000, 0x00200000).when(i0, i0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x00400000))).when(i0, i0).when(inline(0x00400000), inline(0x00400000))))).when(0x00400000, 0x00400000).when(i0, i0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x00800000))).when(i0, i0).when(inline(0x00800000), inline(0x00800000))))).when(0x00800000, 0x00800000).when(i0, i0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x01000000))).when(i0, i0).when(inline(0x01000000), inline(0x01000000))))).when(0x01000000, 0x01000000).when(i0, i0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x02000000))).when(i0, i0).when(inline(0x02000000), inline(0x02000000))))).when(0x02000000, 0x02000000).when(i0, i0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x04000000))).when(i0, i0).when(inline(0x04000000), inline(0x04000000))))).when(0x04000000, 0x04000000).when(i0, i0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x08000000))).when(i0, i0).when(inline(0x08000000), inline(0x08000000))))).when(0x08000000, 0x08000000).when(i0, i0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x10000000))).when(i0, i0).when(inline(0x10000000), inline(0x10000000))))).when(0x10000000, 0x10000000).when(i0, i0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x20000000))).when(i0, i0).when(inline(0x20000000), inline(0x20000000))))).when(0x20000000, 0x20000000).when(i0, i0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x40000000))).when(i0, i0).when(inline(0x40000000), inline(0x40000000))))).when(0x40000000, 0x40000000).when(i0, i0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x80000000))).when(i0, i0).when(inline(0x80000000), inline(0x80000000))))).when(0x80000000, 0x80000000).when(i0, i0))
                );
            }
            else if (field.getType() == Long.class) {
                Field<Long> f = (Field<Long>) field;
                Field<Long> l0 = inline(0L);
                Field<Long> l2 = inline(2L);

                ctx.visit(
                          case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x0000000000000001L))).when(l0, l0).when(inline(0x0000000000000001L), inline(0x0000000000000001L))))).when(0x0000000000000001L, 0x0000000000000001L).when(l0, l0)
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x0000000000000002L))).when(l0, l0).when(inline(0x0000000000000002L), inline(0x0000000000000002L))))).when(0x0000000000000002L, 0x0000000000000002L).when(l0, l0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x0000000000000004L))).when(l0, l0).when(inline(0x0000000000000004L), inline(0x0000000000000004L))))).when(0x0000000000000004L, 0x0000000000000004L).when(l0, l0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x0000000000000008L))).when(l0, l0).when(inline(0x0000000000000008L), inline(0x0000000000000008L))))).when(0x0000000000000008L, 0x0000000000000008L).when(l0, l0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x0000000000000010L))).when(l0, l0).when(inline(0x0000000000000010L), inline(0x0000000000000010L))))).when(0x0000000000000010L, 0x0000000000000010L).when(l0, l0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x0000000000000020L))).when(l0, l0).when(inline(0x0000000000000020L), inline(0x0000000000000020L))))).when(0x0000000000000020L, 0x0000000000000020L).when(l0, l0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x0000000000000040L))).when(l0, l0).when(inline(0x0000000000000040L), inline(0x0000000000000040L))))).when(0x0000000000000040L, 0x0000000000000040L).when(l0, l0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x0000000000000080L))).when(l0, l0).when(inline(0x0000000000000080L), inline(0x0000000000000080L))))).when(0x0000000000000080L, 0x0000000000000080L).when(l0, l0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x0000000000000100L))).when(l0, l0).when(inline(0x0000000000000100L), inline(0x0000000000000100L))))).when(0x0000000000000100L, 0x0000000000000100L).when(l0, l0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x0000000000000200L))).when(l0, l0).when(inline(0x0000000000000200L), inline(0x0000000000000200L))))).when(0x0000000000000200L, 0x0000000000000200L).when(l0, l0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x0000000000000400L))).when(l0, l0).when(inline(0x0000000000000400L), inline(0x0000000000000400L))))).when(0x0000000000000400L, 0x0000000000000400L).when(l0, l0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x0000000000000800L))).when(l0, l0).when(inline(0x0000000000000800L), inline(0x0000000000000800L))))).when(0x0000000000000800L, 0x0000000000000800L).when(l0, l0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x0000000000001000L))).when(l0, l0).when(inline(0x0000000000001000L), inline(0x0000000000001000L))))).when(0x0000000000001000L, 0x0000000000001000L).when(l0, l0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x0000000000002000L))).when(l0, l0).when(inline(0x0000000000002000L), inline(0x0000000000002000L))))).when(0x0000000000002000L, 0x0000000000002000L).when(l0, l0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x0000000000004000L))).when(l0, l0).when(inline(0x0000000000004000L), inline(0x0000000000004000L))))).when(0x0000000000004000L, 0x0000000000004000L).when(l0, l0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x0000000000008000L))).when(l0, l0).when(inline(0x0000000000008000L), inline(0x0000000000008000L))))).when(0x0000000000008000L, 0x0000000000008000L).when(l0, l0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x0000000000010000L))).when(l0, l0).when(inline(0x0000000000010000L), inline(0x0000000000010000L))))).when(0x0000000000010000L, 0x0000000000010000L).when(l0, l0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x0000000000020000L))).when(l0, l0).when(inline(0x0000000000020000L), inline(0x0000000000020000L))))).when(0x0000000000020000L, 0x0000000000020000L).when(l0, l0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x0000000000040000L))).when(l0, l0).when(inline(0x0000000000040000L), inline(0x0000000000040000L))))).when(0x0000000000040000L, 0x0000000000040000L).when(l0, l0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x0000000000080000L))).when(l0, l0).when(inline(0x0000000000080000L), inline(0x0000000000080000L))))).when(0x0000000000080000L, 0x0000000000080000L).when(l0, l0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x0000000000100000L))).when(l0, l0).when(inline(0x0000000000100000L), inline(0x0000000000100000L))))).when(0x0000000000100000L, 0x0000000000100000L).when(l0, l0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x0000000000200000L))).when(l0, l0).when(inline(0x0000000000200000L), inline(0x0000000000200000L))))).when(0x0000000000200000L, 0x0000000000200000L).when(l0, l0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x0000000000400000L))).when(l0, l0).when(inline(0x0000000000400000L), inline(0x0000000000400000L))))).when(0x0000000000400000L, 0x0000000000400000L).when(l0, l0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x0000000000800000L))).when(l0, l0).when(inline(0x0000000000800000L), inline(0x0000000000800000L))))).when(0x0000000000800000L, 0x0000000000800000L).when(l0, l0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x0000000001000000L))).when(l0, l0).when(inline(0x0000000001000000L), inline(0x0000000001000000L))))).when(0x0000000001000000L, 0x0000000001000000L).when(l0, l0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x0000000002000000L))).when(l0, l0).when(inline(0x0000000002000000L), inline(0x0000000002000000L))))).when(0x0000000002000000L, 0x0000000002000000L).when(l0, l0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x0000000004000000L))).when(l0, l0).when(inline(0x0000000004000000L), inline(0x0000000004000000L))))).when(0x0000000004000000L, 0x0000000004000000L).when(l0, l0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x0000000008000000L))).when(l0, l0).when(inline(0x0000000008000000L), inline(0x0000000008000000L))))).when(0x0000000008000000L, 0x0000000008000000L).when(l0, l0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x0000000010000000L))).when(l0, l0).when(inline(0x0000000010000000L), inline(0x0000000010000000L))))).when(0x0000000010000000L, 0x0000000010000000L).when(l0, l0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x0000000020000000L))).when(l0, l0).when(inline(0x0000000020000000L), inline(0x0000000020000000L))))).when(0x0000000020000000L, 0x0000000020000000L).when(l0, l0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x0000000040000000L))).when(l0, l0).when(inline(0x0000000040000000L), inline(0x0000000040000000L))))).when(0x0000000040000000L, 0x0000000040000000L).when(l0, l0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x0000000080000000L))).when(l0, l0).when(inline(0x0000000080000000L), inline(0x0000000080000000L))))).when(0x0000000080000000L, 0x0000000080000000L).when(l0, l0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x0000000100000000L))).when(l0, l0).when(inline(0x0000000100000000L), inline(0x0000000100000000L))))).when(0x0000000100000000L, 0x0000000100000000L).when(l0, l0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x0000000200000000L))).when(l0, l0).when(inline(0x0000000200000000L), inline(0x0000000200000000L))))).when(0x0000000200000000L, 0x0000000200000000L).when(l0, l0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x0000000400000000L))).when(l0, l0).when(inline(0x0000000400000000L), inline(0x0000000400000000L))))).when(0x0000000400000000L, 0x0000000400000000L).when(l0, l0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x0000000800000000L))).when(l0, l0).when(inline(0x0000000800000000L), inline(0x0000000800000000L))))).when(0x0000000800000000L, 0x0000000800000000L).when(l0, l0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x0000001000000000L))).when(l0, l0).when(inline(0x0000001000000000L), inline(0x0000001000000000L))))).when(0x0000001000000000L, 0x0000001000000000L).when(l0, l0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x0000002000000000L))).when(l0, l0).when(inline(0x0000002000000000L), inline(0x0000002000000000L))))).when(0x0000002000000000L, 0x0000002000000000L).when(l0, l0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x0000004000000000L))).when(l0, l0).when(inline(0x0000004000000000L), inline(0x0000004000000000L))))).when(0x0000004000000000L, 0x0000004000000000L).when(l0, l0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x0000008000000000L))).when(l0, l0).when(inline(0x0000008000000000L), inline(0x0000008000000000L))))).when(0x0000008000000000L, 0x0000008000000000L).when(l0, l0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x0000010000000000L))).when(l0, l0).when(inline(0x0000010000000000L), inline(0x0000010000000000L))))).when(0x0000010000000000L, 0x0000010000000000L).when(l0, l0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x0000020000000000L))).when(l0, l0).when(inline(0x0000020000000000L), inline(0x0000020000000000L))))).when(0x0000020000000000L, 0x0000020000000000L).when(l0, l0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x0000040000000000L))).when(l0, l0).when(inline(0x0000040000000000L), inline(0x0000040000000000L))))).when(0x0000040000000000L, 0x0000040000000000L).when(l0, l0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x0000080000000000L))).when(l0, l0).when(inline(0x0000080000000000L), inline(0x0000080000000000L))))).when(0x0000080000000000L, 0x0000080000000000L).when(l0, l0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x0000100000000000L))).when(l0, l0).when(inline(0x0000100000000000L), inline(0x0000100000000000L))))).when(0x0000100000000000L, 0x0000100000000000L).when(l0, l0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x0000200000000000L))).when(l0, l0).when(inline(0x0000200000000000L), inline(0x0000200000000000L))))).when(0x0000200000000000L, 0x0000200000000000L).when(l0, l0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x0000400000000000L))).when(l0, l0).when(inline(0x0000400000000000L), inline(0x0000400000000000L))))).when(0x0000400000000000L, 0x0000400000000000L).when(l0, l0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x0000800000000000L))).when(l0, l0).when(inline(0x0000800000000000L), inline(0x0000800000000000L))))).when(0x0000800000000000L, 0x0000800000000000L).when(l0, l0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x0001000000000000L))).when(l0, l0).when(inline(0x0001000000000000L), inline(0x0001000000000000L))))).when(0x0001000000000000L, 0x0001000000000000L).when(l0, l0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x0002000000000000L))).when(l0, l0).when(inline(0x0002000000000000L), inline(0x0002000000000000L))))).when(0x0002000000000000L, 0x0002000000000000L).when(l0, l0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x0004000000000000L))).when(l0, l0).when(inline(0x0004000000000000L), inline(0x0004000000000000L))))).when(0x0004000000000000L, 0x0004000000000000L).when(l0, l0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x0008000000000000L))).when(l0, l0).when(inline(0x0008000000000000L), inline(0x0008000000000000L))))).when(0x0008000000000000L, 0x0008000000000000L).when(l0, l0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x0010000000000000L))).when(l0, l0).when(inline(0x0010000000000000L), inline(0x0010000000000000L))))).when(0x0010000000000000L, 0x0010000000000000L).when(l0, l0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x0020000000000000L))).when(l0, l0).when(inline(0x0020000000000000L), inline(0x0020000000000000L))))).when(0x0020000000000000L, 0x0020000000000000L).when(l0, l0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x0040000000000000L))).when(l0, l0).when(inline(0x0040000000000000L), inline(0x0040000000000000L))))).when(0x0040000000000000L, 0x0040000000000000L).when(l0, l0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x0080000000000000L))).when(l0, l0).when(inline(0x0080000000000000L), inline(0x0080000000000000L))))).when(0x0080000000000000L, 0x0080000000000000L).when(l0, l0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x0100000000000000L))).when(l0, l0).when(inline(0x0100000000000000L), inline(0x0100000000000000L))))).when(0x0100000000000000L, 0x0100000000000000L).when(l0, l0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x0200000000000000L))).when(l0, l0).when(inline(0x0200000000000000L), inline(0x0200000000000000L))))).when(0x0200000000000000L, 0x0200000000000000L).when(l0, l0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x0400000000000000L))).when(l0, l0).when(inline(0x0400000000000000L), inline(0x0400000000000000L))))).when(0x0400000000000000L, 0x0400000000000000L).when(l0, l0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x0800000000000000L))).when(l0, l0).when(inline(0x0800000000000000L), inline(0x0800000000000000L))))).when(0x0800000000000000L, 0x0800000000000000L).when(l0, l0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x1000000000000000L))).when(l0, l0).when(inline(0x1000000000000000L), inline(0x1000000000000000L))))).when(0x1000000000000000L, 0x1000000000000000L).when(l0, l0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x2000000000000000L))).when(l0, l0).when(inline(0x2000000000000000L), inline(0x2000000000000000L))))).when(0x2000000000000000L, 0x2000000000000000L).when(l0, l0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x4000000000000000L))).when(l0, l0).when(inline(0x4000000000000000L), inline(0x4000000000000000L))))).when(0x4000000000000000L, 0x4000000000000000L).when(l0, l0))
                    .plus(case_(fo(DSL.max(case_(DSL.bitAnd(f, inline(0x8000000000000000L))).when(l0, l0).when(inline(0x8000000000000000L), inline(0x8000000000000000L))))).when(0x8000000000000000L, 0x8000000000000000L).when(l0, l0))
                );
            }
            // Currently not supported
            else
                super.accept(ctx);
        }












        else
            super.accept(ctx);
    }

    @Override
    final void acceptFunctionName(Context<?> ctx) {
        switch (ctx.family()) {





            case IGNITE:
            case MARIADB:
            case MYSQL:
            case POSTGRES:
            case YUGABYTEDB:
                ctx.visit(N_BIT_OR);
                break;

            case H2:





                ctx.visit(N_BIT_OR_AGG);
                break;

            case TRINO:
                ctx.visit(N_BITWISE_OR_AGG);
                break;

            default:
                super.acceptFunctionName(ctx);
                break;
        }
    }



    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    @Override
    public final Field<T> $value() {
        return (Field<T>) getArguments().get(0);
    }

    @Override
    public final QOM.BitOrAgg<T> $value(Field<T> newValue) {
        return $constructor().apply(newValue);
    }

    public final Function1<? super Field<T>, ? extends QOM.BitOrAgg<T>> $constructor() {
        return (a1) -> new BitOrAgg<>(a1);
    }























    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.BitOrAgg<?> o) {
            return
                StringUtils.equals($value(), o.$value())
            ;
        }
        else
            return super.equals(that);
    }
}
