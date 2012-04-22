/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.jooq.impl;

import static org.jooq.impl.Factory.bitAnd;
import static org.jooq.impl.Factory.function;
import static org.jooq.impl.Factory.literal;
import static org.jooq.impl.Factory.shr;

import org.jooq.Configuration;
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
class BitCount extends AbstractFunction<Integer> {

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

        switch (configuration.getDialect()) {
            case MYSQL:
                return function("bit_count", getDataType(), getArguments());

            // Warning, some severe madness lies ahead. Better solutions very welcome!
            // See also http://stackoverflow.com/questions/7946349/how-to-simulate-the-mysql-bit-count-function-in-sybase-sql-anywhere
            default: {
                if (field.getType() == Byte.class) {
                    @SuppressWarnings("unchecked")
                    Field<Byte> f = (Field<Byte>) field;

                    byte i = 0;
                    return bitAnd(f, literal((byte) 0x01)).add(
                           shr(bitAnd(f, literal((byte) 0x02)), literal(++i))).add(
                           shr(bitAnd(f, literal((byte) 0x04)), literal(++i))).add(
                           shr(bitAnd(f, literal((byte) 0x08)), literal(++i))).add(
                           shr(bitAnd(f, literal((byte) 0x10)), literal(++i))).add(
                           shr(bitAnd(f, literal((byte) 0x20)), literal(++i))).add(
                           shr(bitAnd(f, literal((byte) 0x40)), literal(++i))).add(
                           shr(bitAnd(f, literal((byte) 0x80)), literal(++i))).cast(Integer.class);
                }
                else if (field.getType() == Short.class) {
                    @SuppressWarnings("unchecked")
                    Field<Short> f = (Field<Short>) field;

                    short i = 0;
                    return bitAnd(f, literal((short) 0x0001)).add(
                           shr(bitAnd(f, literal((short) 0x0002)), literal(++i))).add(
                           shr(bitAnd(f, literal((short) 0x0004)), literal(++i))).add(
                           shr(bitAnd(f, literal((short) 0x0008)), literal(++i))).add(
                           shr(bitAnd(f, literal((short) 0x0010)), literal(++i))).add(
                           shr(bitAnd(f, literal((short) 0x0020)), literal(++i))).add(
                           shr(bitAnd(f, literal((short) 0x0040)), literal(++i))).add(
                           shr(bitAnd(f, literal((short) 0x0080)), literal(++i))).add(
                           shr(bitAnd(f, literal((short) 0x0100)), literal(++i))).add(
                           shr(bitAnd(f, literal((short) 0x0200)), literal(++i))).add(
                           shr(bitAnd(f, literal((short) 0x0400)), literal(++i))).add(
                           shr(bitAnd(f, literal((short) 0x0800)), literal(++i))).add(
                           shr(bitAnd(f, literal((short) 0x1000)), literal(++i))).add(
                           shr(bitAnd(f, literal((short) 0x2000)), literal(++i))).add(
                           shr(bitAnd(f, literal((short) 0x4000)), literal(++i))).add(
                           shr(bitAnd(f, literal((short) 0x8000)), literal(++i))).cast(Integer.class);
                }
                else if (field.getType() == Integer.class) {
                    @SuppressWarnings("unchecked")
                    Field<Integer> f = (Field<Integer>) field;

                    int i = 0;
                    return bitAnd(f, literal(0x00000001)).add(
                           shr(bitAnd(f, literal(0x00000002)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x00000004)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x00000008)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x00000010)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x00000020)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x00000040)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x00000080)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x00000100)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x00000200)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x00000400)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x00000800)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x00001000)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x00002000)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x00004000)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x00008000)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x00010000)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x00020000)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x00040000)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x00080000)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x00100000)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x00200000)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x00400000)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x00800000)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x01000000)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x02000000)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x04000000)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x08000000)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x10000000)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x20000000)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x40000000)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x80000000)), literal(++i)));
                }
                else if (field.getType() == Long.class) {
                    @SuppressWarnings("unchecked")
                    Field<Long> f = (Field<Long>) field;

                    long i = 0;
                    return bitAnd(f, literal(0x0000000000000001L)).add(
                           shr(bitAnd(f, literal(0x0000000000000002L)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x0000000000000004L)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x0000000000000008L)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x0000000000000010L)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x0000000000000020L)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x0000000000000040L)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x0000000000000080L)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x0000000000000100L)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x0000000000000200L)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x0000000000000400L)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x0000000000000800L)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x0000000000001000L)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x0000000000002000L)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x0000000000004000L)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x0000000000008000L)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x0000000000010000L)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x0000000000020000L)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x0000000000040000L)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x0000000000080000L)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x0000000000100000L)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x0000000000200000L)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x0000000000400000L)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x0000000000800000L)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x0000000001000000L)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x0000000002000000L)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x0000000004000000L)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x0000000008000000L)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x0000000010000000L)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x0000000020000000L)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x0000000040000000L)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x0000000080000000L)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x0000000100000000L)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x0000000200000000L)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x0000000400000000L)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x0000000800000000L)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x0000001000000000L)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x0000002000000000L)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x0000004000000000L)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x0000008000000000L)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x0000010000000000L)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x0000020000000000L)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x0000040000000000L)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x0000080000000000L)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x0000100000000000L)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x0000200000000000L)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x0000400000000000L)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x0000800000000000L)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x0001000000000000L)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x0002000000000000L)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x0004000000000000L)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x0008000000000000L)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x0010000000000000L)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x0020000000000000L)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x0040000000000000L)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x0080000000000000L)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x0100000000000000L)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x0200000000000000L)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x0400000000000000L)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x0800000000000000L)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x1000000000000000L)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x2000000000000000L)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x4000000000000000L)), literal(++i))).add(
                           shr(bitAnd(f, literal(0x8000000000000000L)), literal(++i))).cast(Integer.class);
                }
                else {
                    // Currently not supported
                    return function("bit_count", getDataType(), getArguments());
                }
            }
        }
    }
}
