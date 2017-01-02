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
 */
package org.jooq.impl;

import static org.jooq.impl.DSL.function;
import static org.jooq.impl.DSL.inline;

import org.jooq.Configuration;
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
final class BitCount extends AbstractFunction<Integer> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 7624782102883057433L;

    BitCount(Field<?> field) {
        super("bit_count", SQLDataType.INTEGER, field);
    }

    @Override
    final Field<Integer> getFunction0(Configuration configuration) {
        final Field<?> field = getArguments()[0];

        switch (configuration.family()) {
            case MARIADB:
            case MYSQL:
                return function("bit_count", getDataType(), getArguments());














            // Better solutions very welcome! See also:
            // See also http://stackoverflow.com/questions/7946349/how-to-simulate-the-mysql-bit-count-function-in-sybase-sql-anywhere
            case H2:
            case HSQLDB: {
                if (field.getType() == Byte.class) {
                    @SuppressWarnings("unchecked")
                    Field<Byte> f = (Field<Byte>) field;

                    return DSL.bitAnd(f, inline((byte) 0x01))                          .add(
                           DSL.bitAnd(f, inline((byte) 0x02)).div(inline((byte) 0x02))).add(
                           DSL.bitAnd(f, inline((byte) 0x04)).div(inline((byte) 0x04))).add(
                           DSL.bitAnd(f, inline((byte) 0x08)).div(inline((byte) 0x08))).add(
                           DSL.bitAnd(f, inline((byte) 0x10)).div(inline((byte) 0x10))).add(
                           DSL.bitAnd(f, inline((byte) 0x20)).div(inline((byte) 0x20))).add(
                           DSL.bitAnd(f, inline((byte) 0x40)).div(inline((byte) 0x40))).add(
                           DSL.bitAnd(f, inline((byte) 0x80)).div(inline((byte) 0x80))).cast(Integer.class);
                }
                else if (field.getType() == Short.class) {
                    @SuppressWarnings("unchecked")
                    Field<Short> f = (Field<Short>) field;

                    return DSL.bitAnd(f, inline((short) 0x0001))                             .add(
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
                           DSL.bitAnd(f, inline((short) 0x8000)).div(inline((short) 0x8000))).cast(Integer.class);
                }
                else if (field.getType() == Integer.class) {
                    @SuppressWarnings("unchecked")
                    Field<Integer> f = (Field<Integer>) field;

                    return DSL.bitAnd(f, inline(0x00000001))                         .add(
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
                           DSL.bitAnd(f, inline(0x80000000)).div(inline(0x80000000)));
                }
                else if (field.getType() == Long.class) {
                    @SuppressWarnings("unchecked")
                    Field<Long> f = (Field<Long>) field;

                    return DSL.bitAnd(f, inline(0x0000000000000001L))                                  .add(
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
                           DSL.bitAnd(f, inline(0x8000000000000000L)).div(inline(0x0000000000000000L))).cast(Integer.class);
                }
                else {
                    // Currently not supported
                    return function("bit_count", getDataType(), getArguments());
                }
            }

            default: {
                if (field.getType() == Byte.class) {
                    @SuppressWarnings("unchecked")
                    Field<Byte> f = (Field<Byte>) field;

                    byte i = 0;
                    return         DSL.bitAnd(f, inline((byte) 0x01))               .add(
                           DSL.shr(DSL.bitAnd(f, inline((byte) 0x02)), inline(++i))).add(
                           DSL.shr(DSL.bitAnd(f, inline((byte) 0x04)), inline(++i))).add(
                           DSL.shr(DSL.bitAnd(f, inline((byte) 0x08)), inline(++i))).add(
                           DSL.shr(DSL.bitAnd(f, inline((byte) 0x10)), inline(++i))).add(
                           DSL.shr(DSL.bitAnd(f, inline((byte) 0x20)), inline(++i))).add(
                           DSL.shr(DSL.bitAnd(f, inline((byte) 0x40)), inline(++i))).add(
                           DSL.shr(DSL.bitAnd(f, inline((byte) 0x80)), inline(++i))).cast(Integer.class);
                }
                else if (field.getType() == Short.class) {
                    @SuppressWarnings("unchecked")
                    Field<Short> f = (Field<Short>) field;

                    short i = 0;
                    return         DSL.bitAnd(f, inline((short) 0x0001))               .add(
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
                           DSL.shr(DSL.bitAnd(f, inline((short) 0x8000)), inline(++i))).cast(Integer.class);
                }
                else if (field.getType() == Integer.class) {
                    @SuppressWarnings("unchecked")
                    Field<Integer> f = (Field<Integer>) field;

                    int i = 0;
                    return         DSL.bitAnd(f, inline(0x00000001))               .add(
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
                           DSL.shr(DSL.bitAnd(f, inline(0x80000000)), inline(++i)));
                }
                else if (field.getType() == Long.class) {
                    @SuppressWarnings("unchecked")
                    Field<Long> f = (Field<Long>) field;

                    long i = 0;
                    return         DSL.bitAnd(f, inline(0x0000000000000001L))               .add(
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
                           DSL.shr(DSL.bitAnd(f, inline(0x8000000000000000L)), inline(++i))).cast(Integer.class);
                }
                else {
                    // Currently not supported
                    return function("bit_count", getDataType(), getArguments());
                }
            }
        }
    }
}
