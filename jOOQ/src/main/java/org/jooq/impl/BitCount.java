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
 * The <code>BIT COUNT</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unused" })
final class BitCount
extends
    AbstractField<Integer>
implements
    QOM.BitCount
{

    final Field<? extends Number> value;

    BitCount(
        Field<? extends Number> value
    ) {
        super(
            N_BIT_COUNT,
            allNotNull(INTEGER, value)
        );

        this.value = nullSafeNotNull(value, INTEGER);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {






















            // [#3962] The databases listed here are the same ones that need to
            // emulate the SHR(a, b) function via a / POWER(2, b). BitCount is
            // expensive enough already, we shouldn't also introduce POWER()
            // Better solutions very welcome! See also:
            // See also http://stackoverflow.com/questions/7946349/how-to-simulate-the-mysql-bit-count-function-in-sybase-sql-anywhere







            case H2:
            case HSQLDB: {
                bitAndDivEmulation(ctx);
                return;
            }









            case CUBRID:
            case DERBY:
            case FIREBIRD:
            case IGNITE:
            case POSTGRES:
            case SQLITE:
            case TRINO:
            case YUGABYTEDB: {
                bitAndShrEmulation(ctx);
                return;
            }

            case CLICKHOUSE:
                ctx.visit(N_bitCount).sql('(').visit(value).sql(')');
                return;

            default:
                acceptNative(ctx);
                return;

        }
    }

    private final void acceptNative(Context<?> ctx) {
        ctx.visit(N_BIT_COUNT).sql('(').visit(value).sql(')');
    }

    private final void bitAndDivEmulation(Context<?> ctx) {
        if (value.getDataType().getFromType() == Byte.class) {
            @SuppressWarnings("unchecked")
            Field<Byte> f = (Field<Byte>) value;

            ctx.visit(
                DSL.bitAnd(f, inline((byte) 0x01))                          .add(
                DSL.bitAnd(f, inline((byte) 0x02)).div(inline((byte) 0x02))).add(
                DSL.bitAnd(f, inline((byte) 0x04)).div(inline((byte) 0x04))).add(
                DSL.bitAnd(f, inline((byte) 0x08)).div(inline((byte) 0x08))).add(
                DSL.bitAnd(f, inline((byte) 0x10)).div(inline((byte) 0x10))).add(
                DSL.bitAnd(f, inline((byte) 0x20)).div(inline((byte) 0x20))).add(
                DSL.bitAnd(f, inline((byte) 0x40)).div(inline((byte) 0x40))).add(
                DSL.bitAnd(f, inline((byte) 0x80)).div(inline((byte) 0x80))).cast(Integer.class));
        }
        else if (value.getDataType().getFromType() == Short.class) {
            @SuppressWarnings("unchecked")
            Field<Short> f = (Field<Short>) value;

            ctx.visit(
                DSL.bitAnd(f, inline((short) 0x0001))                             .add(
                DSL.bitAnd(f, inline((short) 0x0002)).div(inline((short) 0x0002))).add(
                DSL.bitAnd(f, inline((short) 0x0004)).div(inline((short) 0x0004))).add(
                DSL.bitAnd(f, inline((short) 0x0008)).div(inline((short) 0x0008))).add(
                DSL.bitAnd(f, inline((short) 0x0010)).div(inline((short) 0x0010))).add(
                DSL.bitAnd(f, inline((short) 0x0020)).div(inline((short) 0x0020))).add(
                DSL.bitAnd(f, inline((short) 0x0040)).div(inline((short) 0x0040))).add(
                DSL.bitAnd(f, inline((short) 0x0080)).div(inline((short) 0x0080))).add(
                DSL.bitAnd(f, inline((short) 0x0100)).div(inline((short) 0x0100))).add(
                DSL.bitAnd(f, inline((short) 0x0200)).div(inline((short) 0x0200))).add(
                DSL.bitAnd(f, inline((short) 0x0400)).div(inline((short) 0x0400))).add(
                DSL.bitAnd(f, inline((short) 0x0800)).div(inline((short) 0x0800))).add(
                DSL.bitAnd(f, inline((short) 0x1000)).div(inline((short) 0x1000))).add(
                DSL.bitAnd(f, inline((short) 0x2000)).div(inline((short) 0x2000))).add(
                DSL.bitAnd(f, inline((short) 0x4000)).div(inline((short) 0x4000))).add(
                DSL.bitAnd(f, inline((short) 0x8000)).div(inline((short) 0x8000))).cast(Integer.class));
        }
        else if (value.getDataType().getFromType() == Integer.class) {
            @SuppressWarnings("unchecked")
            Field<Integer> f = (Field<Integer>) value;

            ctx.visit(
                DSL.bitAnd(f, inline(0x00000001))                         .add(
                DSL.bitAnd(f, inline(0x00000002)).div(inline(0x00000002))).add(
                DSL.bitAnd(f, inline(0x00000004)).div(inline(0x00000004))).add(
                DSL.bitAnd(f, inline(0x00000008)).div(inline(0x00000008))).add(
                DSL.bitAnd(f, inline(0x00000010)).div(inline(0x00000010))).add(
                DSL.bitAnd(f, inline(0x00000020)).div(inline(0x00000020))).add(
                DSL.bitAnd(f, inline(0x00000040)).div(inline(0x00000040))).add(
                DSL.bitAnd(f, inline(0x00000080)).div(inline(0x00000080))).add(
                DSL.bitAnd(f, inline(0x00000100)).div(inline(0x00000100))).add(
                DSL.bitAnd(f, inline(0x00000200)).div(inline(0x00000200))).add(
                DSL.bitAnd(f, inline(0x00000400)).div(inline(0x00000400))).add(
                DSL.bitAnd(f, inline(0x00000800)).div(inline(0x00000800))).add(
                DSL.bitAnd(f, inline(0x00001000)).div(inline(0x00001000))).add(
                DSL.bitAnd(f, inline(0x00002000)).div(inline(0x00002000))).add(
                DSL.bitAnd(f, inline(0x00004000)).div(inline(0x00004000))).add(
                DSL.bitAnd(f, inline(0x00008000)).div(inline(0x00008000))).add(
                DSL.bitAnd(f, inline(0x00010000)).div(inline(0x00010000))).add(
                DSL.bitAnd(f, inline(0x00020000)).div(inline(0x00020000))).add(
                DSL.bitAnd(f, inline(0x00040000)).div(inline(0x00040000))).add(
                DSL.bitAnd(f, inline(0x00080000)).div(inline(0x00080000))).add(
                DSL.bitAnd(f, inline(0x00100000)).div(inline(0x00100000))).add(
                DSL.bitAnd(f, inline(0x00200000)).div(inline(0x00200000))).add(
                DSL.bitAnd(f, inline(0x00400000)).div(inline(0x00400000))).add(
                DSL.bitAnd(f, inline(0x00800000)).div(inline(0x00800000))).add(
                DSL.bitAnd(f, inline(0x01000000)).div(inline(0x01000000))).add(
                DSL.bitAnd(f, inline(0x02000000)).div(inline(0x02000000))).add(
                DSL.bitAnd(f, inline(0x04000000)).div(inline(0x04000000))).add(
                DSL.bitAnd(f, inline(0x08000000)).div(inline(0x08000000))).add(
                DSL.bitAnd(f, inline(0x10000000)).div(inline(0x10000000))).add(
                DSL.bitAnd(f, inline(0x20000000)).div(inline(0x20000000))).add(
                DSL.bitAnd(f, inline(0x40000000)).div(inline(0x40000000))).add(
                DSL.bitAnd(f, inline(0x80000000)).div(inline(0x80000000))));
        }
        else if (value.getDataType().getFromType() == Long.class) {
            @SuppressWarnings("unchecked")
            Field<Long> f = (Field<Long>) value;

            ctx.visit(
                DSL.bitAnd(f, inline(0x0000000000000001L))                                  .add(
                DSL.bitAnd(f, inline(0x0000000000000002L)).div(inline(0x0000000000000002L))).add(
                DSL.bitAnd(f, inline(0x0000000000000004L)).div(inline(0x0000000000000004L))).add(
                DSL.bitAnd(f, inline(0x0000000000000008L)).div(inline(0x0000000000000008L))).add(
                DSL.bitAnd(f, inline(0x0000000000000010L)).div(inline(0x0000000000000010L))).add(
                DSL.bitAnd(f, inline(0x0000000000000020L)).div(inline(0x0000000000000020L))).add(
                DSL.bitAnd(f, inline(0x0000000000000040L)).div(inline(0x0000000000000040L))).add(
                DSL.bitAnd(f, inline(0x0000000000000080L)).div(inline(0x0000000000000080L))).add(
                DSL.bitAnd(f, inline(0x0000000000000100L)).div(inline(0x0000000000000100L))).add(
                DSL.bitAnd(f, inline(0x0000000000000200L)).div(inline(0x0000000000000200L))).add(
                DSL.bitAnd(f, inline(0x0000000000000400L)).div(inline(0x0000000000000400L))).add(
                DSL.bitAnd(f, inline(0x0000000000000800L)).div(inline(0x0000000000000800L))).add(
                DSL.bitAnd(f, inline(0x0000000000001000L)).div(inline(0x0000000000001000L))).add(
                DSL.bitAnd(f, inline(0x0000000000002000L)).div(inline(0x0000000000002000L))).add(
                DSL.bitAnd(f, inline(0x0000000000004000L)).div(inline(0x0000000000004000L))).add(
                DSL.bitAnd(f, inline(0x0000000000008000L)).div(inline(0x0000000000008000L))).add(
                DSL.bitAnd(f, inline(0x0000000000010000L)).div(inline(0x0000000000010000L))).add(
                DSL.bitAnd(f, inline(0x0000000000020000L)).div(inline(0x0000000000020000L))).add(
                DSL.bitAnd(f, inline(0x0000000000040000L)).div(inline(0x0000000000040000L))).add(
                DSL.bitAnd(f, inline(0x0000000000080000L)).div(inline(0x0000000000080000L))).add(
                DSL.bitAnd(f, inline(0x0000000000100000L)).div(inline(0x0000000000100000L))).add(
                DSL.bitAnd(f, inline(0x0000000000200000L)).div(inline(0x0000000000200000L))).add(
                DSL.bitAnd(f, inline(0x0000000000400000L)).div(inline(0x0000000000400000L))).add(
                DSL.bitAnd(f, inline(0x0000000000800000L)).div(inline(0x0000000000800000L))).add(
                DSL.bitAnd(f, inline(0x0000000001000000L)).div(inline(0x0000000001000000L))).add(
                DSL.bitAnd(f, inline(0x0000000002000000L)).div(inline(0x0000000002000000L))).add(
                DSL.bitAnd(f, inline(0x0000000004000000L)).div(inline(0x0000000004000000L))).add(
                DSL.bitAnd(f, inline(0x0000000008000000L)).div(inline(0x0000000008000000L))).add(
                DSL.bitAnd(f, inline(0x0000000010000000L)).div(inline(0x0000000010000000L))).add(
                DSL.bitAnd(f, inline(0x0000000020000000L)).div(inline(0x0000000020000000L))).add(
                DSL.bitAnd(f, inline(0x0000000040000000L)).div(inline(0x0000000040000000L))).add(
                DSL.bitAnd(f, inline(0x0000000080000000L)).div(inline(0x0000000080000000L))).add(
                DSL.bitAnd(f, inline(0x0000000100000000L)).div(inline(0x0000000100000000L))).add(
                DSL.bitAnd(f, inline(0x0000000200000000L)).div(inline(0x0000000200000000L))).add(
                DSL.bitAnd(f, inline(0x0000000400000000L)).div(inline(0x0000000400000000L))).add(
                DSL.bitAnd(f, inline(0x0000000800000000L)).div(inline(0x0000000800000000L))).add(
                DSL.bitAnd(f, inline(0x0000001000000000L)).div(inline(0x0000001000000000L))).add(
                DSL.bitAnd(f, inline(0x0000002000000000L)).div(inline(0x0000002000000000L))).add(
                DSL.bitAnd(f, inline(0x0000004000000000L)).div(inline(0x0000004000000000L))).add(
                DSL.bitAnd(f, inline(0x0000008000000000L)).div(inline(0x0000008000000000L))).add(
                DSL.bitAnd(f, inline(0x0000010000000000L)).div(inline(0x0000010000000000L))).add(
                DSL.bitAnd(f, inline(0x0000020000000000L)).div(inline(0x0000020000000000L))).add(
                DSL.bitAnd(f, inline(0x0000040000000000L)).div(inline(0x0000040000000000L))).add(
                DSL.bitAnd(f, inline(0x0000080000000000L)).div(inline(0x0000080000000000L))).add(
                DSL.bitAnd(f, inline(0x0000100000000000L)).div(inline(0x0000100000000000L))).add(
                DSL.bitAnd(f, inline(0x0000200000000000L)).div(inline(0x0000200000000000L))).add(
                DSL.bitAnd(f, inline(0x0000400000000000L)).div(inline(0x0000400000000000L))).add(
                DSL.bitAnd(f, inline(0x0000800000000000L)).div(inline(0x0000800000000000L))).add(
                DSL.bitAnd(f, inline(0x0001000000000000L)).div(inline(0x0001000000000000L))).add(
                DSL.bitAnd(f, inline(0x0002000000000000L)).div(inline(0x0002000000000000L))).add(
                DSL.bitAnd(f, inline(0x0004000000000000L)).div(inline(0x0004000000000000L))).add(
                DSL.bitAnd(f, inline(0x0008000000000000L)).div(inline(0x0008000000000000L))).add(
                DSL.bitAnd(f, inline(0x0010000000000000L)).div(inline(0x0010000000000000L))).add(
                DSL.bitAnd(f, inline(0x0020000000000000L)).div(inline(0x0020000000000000L))).add(
                DSL.bitAnd(f, inline(0x0040000000000000L)).div(inline(0x0040000000000000L))).add(
                DSL.bitAnd(f, inline(0x0080000000000000L)).div(inline(0x0080000000000000L))).add(
                DSL.bitAnd(f, inline(0x0100000000000000L)).div(inline(0x0100000000000000L))).add(
                DSL.bitAnd(f, inline(0x0200000000000000L)).div(inline(0x0200000000000000L))).add(
                DSL.bitAnd(f, inline(0x0400000000000000L)).div(inline(0x0400000000000000L))).add(
                DSL.bitAnd(f, inline(0x0800000000000000L)).div(inline(0x0800000000000000L))).add(
                DSL.bitAnd(f, inline(0x1000000000000000L)).div(inline(0x1000000000000000L))).add(
                DSL.bitAnd(f, inline(0x2000000000000000L)).div(inline(0x2000000000000000L))).add(
                DSL.bitAnd(f, inline(0x4000000000000000L)).div(inline(0x4000000000000000L))).add(
                DSL.bitAnd(f, inline(0x8000000000000000L)).div(inline(0x0000000000000000L))).cast(Integer.class));
        }
        else {
            // Currently not supported
            acceptNative(ctx);
        }
    }

    private final void bitAndShrEmulation(Context<?> ctx) {
        if (value.getDataType().getFromType() == Byte.class) {
            @SuppressWarnings("unchecked")
            Field<Byte> f = (Field<Byte>) value;

            byte i = 0;
            ctx.visit(
                        DSL.bitAnd(f, inline((byte) 0x01))               .add(
                DSL.shr(DSL.bitAnd(f, inline((byte) 0x02)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline((byte) 0x04)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline((byte) 0x08)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline((byte) 0x10)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline((byte) 0x20)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline((byte) 0x40)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline((byte) 0x80)), inline(++i))).cast(Integer.class));
        }
        else if (value.getDataType().getFromType() == Short.class) {
            @SuppressWarnings("unchecked")
            Field<Short> f = (Field<Short>) value;

            short i = 0;
            ctx.visit(
                        DSL.bitAnd(f, inline((short) 0x0001))               .add(
                DSL.shr(DSL.bitAnd(f, inline((short) 0x0002)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline((short) 0x0004)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline((short) 0x0008)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline((short) 0x0010)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline((short) 0x0020)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline((short) 0x0040)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline((short) 0x0080)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline((short) 0x0100)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline((short) 0x0200)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline((short) 0x0400)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline((short) 0x0800)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline((short) 0x1000)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline((short) 0x2000)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline((short) 0x4000)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline((short) 0x8000)), inline(++i))).cast(Integer.class));
        }
        else if (value.getDataType().getFromType() == Integer.class) {
            @SuppressWarnings("unchecked")
            Field<Integer> f = (Field<Integer>) value;

            int i = 0;
            ctx.visit(
                        DSL.bitAnd(f, inline(0x00000001))               .add(
                DSL.shr(DSL.bitAnd(f, inline(0x00000002)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x00000004)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x00000008)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x00000010)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x00000020)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x00000040)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x00000080)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x00000100)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x00000200)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x00000400)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x00000800)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x00001000)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x00002000)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x00004000)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x00008000)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x00010000)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x00020000)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x00040000)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x00080000)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x00100000)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x00200000)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x00400000)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x00800000)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x01000000)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x02000000)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x04000000)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x08000000)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x10000000)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x20000000)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x40000000)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x80000000)), inline(++i))));
        }
        else if (value.getDataType().getFromType() == Long.class) {
            @SuppressWarnings("unchecked")
            Field<Long> f = (Field<Long>) value;

            long i = 0;
            ctx.visit(
                DSL.bitAnd(f, inline(0x0000000000000001L))               .add(
                DSL.shr(DSL.bitAnd(f, inline(0x0000000000000002L)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x0000000000000004L)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x0000000000000008L)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x0000000000000010L)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x0000000000000020L)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x0000000000000040L)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x0000000000000080L)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x0000000000000100L)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x0000000000000200L)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x0000000000000400L)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x0000000000000800L)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x0000000000001000L)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x0000000000002000L)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x0000000000004000L)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x0000000000008000L)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x0000000000010000L)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x0000000000020000L)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x0000000000040000L)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x0000000000080000L)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x0000000000100000L)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x0000000000200000L)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x0000000000400000L)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x0000000000800000L)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x0000000001000000L)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x0000000002000000L)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x0000000004000000L)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x0000000008000000L)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x0000000010000000L)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x0000000020000000L)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x0000000040000000L)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x0000000080000000L)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x0000000100000000L)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x0000000200000000L)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x0000000400000000L)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x0000000800000000L)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x0000001000000000L)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x0000002000000000L)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x0000004000000000L)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x0000008000000000L)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x0000010000000000L)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x0000020000000000L)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x0000040000000000L)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x0000080000000000L)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x0000100000000000L)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x0000200000000000L)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x0000400000000000L)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x0000800000000000L)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x0001000000000000L)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x0002000000000000L)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x0004000000000000L)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x0008000000000000L)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x0010000000000000L)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x0020000000000000L)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x0040000000000000L)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x0080000000000000L)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x0100000000000000L)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x0200000000000000L)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x0400000000000000L)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x0800000000000000L)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x1000000000000000L)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x2000000000000000L)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x4000000000000000L)), inline(++i))).add(
                DSL.shr(DSL.bitAnd(f, inline(0x8000000000000000L)), inline(++i))).cast(Integer.class));
        }
        else {
            // Currently not supported
            acceptNative(ctx);
        }
    }














    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<? extends Number> $arg1() {
        return value;
    }

    @Override
    public final QOM.BitCount $arg1(Field<? extends Number> newValue) {
        return $constructor().apply(newValue);
    }

    @Override
    public final Function1<? super Field<? extends Number>, ? extends QOM.BitCount> $constructor() {
        return (a1) -> new BitCount(a1);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.BitCount o) {
            return
                Objects.equals($value(), o.$value())
            ;
        }
        else
            return super.equals(that);
    }
}
