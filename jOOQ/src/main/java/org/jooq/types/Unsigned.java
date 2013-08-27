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
package org.jooq.types;

import java.math.BigInteger;

/**
 * A utility class for static access to unsigned number functionality.
 * <p>
 * It essentially contains factory methods for unsigned number wrappers. In
 * future versions, it will also contain some arithmetic methods, handling
 * regular arithmetic and bitwise operations
 *
 * @author Lukas Eder
 */
public final class Unsigned {

    /**
     * Create an <code>unsigned byte</code>
     *
     * @throws NumberFormatException If <code>value</code> does not contain a
     *             parsable <code>unsigned byte</code>.
     * @see UByte#valueOf(String)
     */
    public static UByte ubyte(String value) throws NumberFormatException {
        return value == null ? null : UByte.valueOf(value);
    }

    /**
     * Create an <code>unsigned byte</code> by masking it with <code>0xFF</code>
     * i.e. <code>(byte) -1</code> becomes <code>(ubyte) 255</code>
     *
     * @see UByte#valueOf(byte)
     */
    public static UByte ubyte(byte value) {
        return UByte.valueOf(value);
    }

    /**
     * Create an <code>unsigned byte</code>
     *
     * @throws NumberFormatException If <code>value</code> is not in the range
     *             of an <code>unsigned byte</code>
     * @see UByte#valueOf(short)
     */
    public static UByte ubyte(short value) throws NumberFormatException {
        return UByte.valueOf(value);
    }

    /**
     * Create an <code>unsigned byte</code>
     *
     * @throws NumberFormatException If <code>value</code> is not in the range
     *             of an <code>unsigned byte</code>
     * @see UByte#valueOf(short)
     */
    public static UByte ubyte(int value) throws NumberFormatException {
        return UByte.valueOf(value);
    }

    /**
     * Create an <code>unsigned byte</code>
     *
     * @throws NumberFormatException If <code>value</code> is not in the range
     *             of an <code>unsigned byte</code>
     * @see UByte#valueOf(short)
     */
    public static UByte ubyte(long value) throws NumberFormatException {
        return UByte.valueOf(value);
    }

    /**
     * Create an <code>unsigned short</code>
     *
     * @throws NumberFormatException If <code>value</code> does not contain a
     *             parsable <code>unsigned short</code>.
     * @see UShort#valueOf(String)
     */
    public static UShort ushort(String value) throws NumberFormatException {
        return value == null ? null : UShort.valueOf(value);
    }

    /**
     * Create an <code>unsigned short</code> by masking it with
     * <code>0xFFFF</code> i.e. <code>(short) -1</code> becomes
     * <code>(ushort) 65535</code>
     *
     * @see UShort#valueOf(short)
     */
    public static UShort ushort(short value) {
        return UShort.valueOf(value);
    }

    /**
     * Create an <code>unsigned short</code>
     *
     * @throws NumberFormatException If <code>value</code> is not in the range
     *             of an <code>unsigned short</code>
     * @see UShort#valueOf(int)
     */
    public static UShort ushort(int value) throws NumberFormatException {
        return UShort.valueOf(value);
    }

    /**
     * Create an <code>unsigned int</code>
     *
     * @throws NumberFormatException If <code>value</code> does not contain a
     *             parsable <code>unsigned int</code>.
     * @see UInteger#valueOf(String)
     */
    public static UInteger uint(String value) throws NumberFormatException {
        return value == null ? null : UInteger.valueOf(value);
    }

    /**
     * Create an <code>unsigned int</code> by masking it with
     * <code>0xFFFFFFFF</code> i.e. <code>(int) -1</code> becomes
     * <code>(uint) 4294967295</code>
     *
     * @see UInteger#valueOf(int)
     */
    public static UInteger uint(int value) {
        return UInteger.valueOf(value);
    }

    /**
     * Create an <code>unsigned int</code>
     *
     * @throws NumberFormatException If <code>value</code> is not in the range
     *             of an <code>unsigned int</code>
     * @see UInteger#valueOf(long)
     */
    public static UInteger uint(long value) throws NumberFormatException {
        return UInteger.valueOf(value);
    }

    /**
     * Create an <code>unsigned long</code>
     *
     * @throws NumberFormatException If <code>value</code> does not contain a
     *             parsable <code>unsigned long</code>.
     * @see ULong#valueOf(String)
     */
    public static ULong ulong(String value) throws NumberFormatException {
        return value == null ? null : ULong.valueOf(value);
    }

    /**
     * Create an <code>unsigned long</code> by masking it with
     * <code>0xFFFFFFFFFFFFFFFF</code> i.e. <code>(long) -1</code> becomes
     * <code>(uint) 18446744073709551615</code>
     *
     * @see ULong#valueOf(long)
     */
    public static ULong ulong(long value) {
        return ULong.valueOf(value);
    }

    /**
     * Create an <code>unsigned long</code>
     *
     * @throws NumberFormatException If <code>value</code> is not in the range
     *             of an <code>unsigned long</code>
     * @see ULong#valueOf(BigInteger)
     */
    public static ULong ulong(BigInteger value) throws NumberFormatException {
        return ULong.valueOf(value);
    }

    /**
     * No instances
     */
    private Unsigned() {}
}
