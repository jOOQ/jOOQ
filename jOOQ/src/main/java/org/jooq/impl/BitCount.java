/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 * 
 * This work is dual-licensed Open Source, under LGPL and jOOQ EULA
 * =============================================================================
 * You may freely choose which license applies to you. For more information 
 * about licensing, please visit http://www.jooq.org/licenses
 * 
 * LGPL:  
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 * 
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 * 
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details.
 * 
 * You should have received a copy of the jOOQ End User License Agreement
 * along with this library.
 * If not, see http://www.jooq.org/eula
 */
package org.jooq.impl;

import static org.jooq.impl.DSL.bitAnd;
import static org.jooq.impl.DSL.function;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.shr;

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

        switch (configuration.dialect()) {
            case MARIADB:
            case MYSQL:
                return function("bit_count", getDataType(), getArguments());

            // Warning, some severe madness lies ahead. Better solutions very welcome!
            // See also http://stackoverflow.com/questions/7946349/how-to-simulate-the-mysql-bit-count-function-in-sybase-sql-anywhere
            default: {
                if (field.getType() == Byte.class) {
                    @SuppressWarnings("unchecked")
                    Field<Byte> f = (Field<Byte>) field;

                    byte i = 0;
                    return     bitAnd(f, inline((byte) 0x01)).add(
                           shr(bitAnd(f, inline((byte) 0x02)), inline(++i))).add(
                           shr(bitAnd(f, inline((byte) 0x04)), inline(++i))).add(
                           shr(bitAnd(f, inline((byte) 0x08)), inline(++i))).add(
                           shr(bitAnd(f, inline((byte) 0x10)), inline(++i))).add(
                           shr(bitAnd(f, inline((byte) 0x20)), inline(++i))).add(
                           shr(bitAnd(f, inline((byte) 0x40)), inline(++i))).add(
                           shr(bitAnd(f, inline((byte) 0x80)), inline(++i))).cast(Integer.class);
                }
                else if (field.getType() == Short.class) {
                    @SuppressWarnings("unchecked")
                    Field<Short> f = (Field<Short>) field;

                    short i = 0;
                    return     bitAnd(f, inline((short) 0x0001)).add(
                           shr(bitAnd(f, inline((short) 0x0002)), inline(++i))).add(
                           shr(bitAnd(f, inline((short) 0x0004)), inline(++i))).add(
                           shr(bitAnd(f, inline((short) 0x0008)), inline(++i))).add(
                           shr(bitAnd(f, inline((short) 0x0010)), inline(++i))).add(
                           shr(bitAnd(f, inline((short) 0x0020)), inline(++i))).add(
                           shr(bitAnd(f, inline((short) 0x0040)), inline(++i))).add(
                           shr(bitAnd(f, inline((short) 0x0080)), inline(++i))).add(
                           shr(bitAnd(f, inline((short) 0x0100)), inline(++i))).add(
                           shr(bitAnd(f, inline((short) 0x0200)), inline(++i))).add(
                           shr(bitAnd(f, inline((short) 0x0400)), inline(++i))).add(
                           shr(bitAnd(f, inline((short) 0x0800)), inline(++i))).add(
                           shr(bitAnd(f, inline((short) 0x1000)), inline(++i))).add(
                           shr(bitAnd(f, inline((short) 0x2000)), inline(++i))).add(
                           shr(bitAnd(f, inline((short) 0x4000)), inline(++i))).add(
                           shr(bitAnd(f, inline((short) 0x8000)), inline(++i))).cast(Integer.class);
                }
                else if (field.getType() == Integer.class) {
                    @SuppressWarnings("unchecked")
                    Field<Integer> f = (Field<Integer>) field;

                    int i = 0;
                    return     bitAnd(f, inline(0x00000001)).add(
                           shr(bitAnd(f, inline(0x00000002)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x00000004)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x00000008)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x00000010)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x00000020)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x00000040)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x00000080)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x00000100)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x00000200)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x00000400)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x00000800)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x00001000)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x00002000)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x00004000)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x00008000)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x00010000)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x00020000)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x00040000)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x00080000)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x00100000)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x00200000)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x00400000)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x00800000)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x01000000)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x02000000)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x04000000)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x08000000)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x10000000)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x20000000)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x40000000)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x80000000)), inline(++i)));
                }
                else if (field.getType() == Long.class) {
                    @SuppressWarnings("unchecked")
                    Field<Long> f = (Field<Long>) field;

                    long i = 0;
                    return bitAnd(f, inline(0x0000000000000001L)).add(
                           shr(bitAnd(f, inline(0x0000000000000002L)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x0000000000000004L)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x0000000000000008L)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x0000000000000010L)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x0000000000000020L)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x0000000000000040L)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x0000000000000080L)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x0000000000000100L)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x0000000000000200L)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x0000000000000400L)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x0000000000000800L)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x0000000000001000L)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x0000000000002000L)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x0000000000004000L)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x0000000000008000L)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x0000000000010000L)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x0000000000020000L)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x0000000000040000L)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x0000000000080000L)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x0000000000100000L)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x0000000000200000L)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x0000000000400000L)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x0000000000800000L)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x0000000001000000L)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x0000000002000000L)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x0000000004000000L)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x0000000008000000L)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x0000000010000000L)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x0000000020000000L)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x0000000040000000L)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x0000000080000000L)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x0000000100000000L)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x0000000200000000L)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x0000000400000000L)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x0000000800000000L)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x0000001000000000L)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x0000002000000000L)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x0000004000000000L)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x0000008000000000L)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x0000010000000000L)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x0000020000000000L)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x0000040000000000L)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x0000080000000000L)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x0000100000000000L)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x0000200000000000L)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x0000400000000000L)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x0000800000000000L)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x0001000000000000L)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x0002000000000000L)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x0004000000000000L)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x0008000000000000L)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x0010000000000000L)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x0020000000000000L)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x0040000000000000L)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x0080000000000000L)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x0100000000000000L)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x0200000000000000L)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x0400000000000000L)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x0800000000000000L)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x1000000000000000L)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x2000000000000000L)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x4000000000000000L)), inline(++i))).add(
                           shr(bitAnd(f, inline(0x8000000000000000L)), inline(++i))).cast(Integer.class);
                }
                else {
                    // Currently not supported
                    return function("bit_count", getDataType(), getArguments());
                }
            }
        }
    }
}
