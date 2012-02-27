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
 * . Neither the name "jOOU" nor the names of its contributors may be
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
package org.jooq.tools.unsigned;

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
     * @see UByte#UByte(String)
     */
    public static UByte ubyte(String value) throws NumberFormatException {
        return value == null ? null : new UByte(value);
    }

    /**
     * Create an <code>unsigned byte</code> by masking it with <code>0xFF</code>
     * i.e. <code>(byte) -1</code> becomes <code>(ubyte) 255</code>
     *
     * @see UByte#UByte(byte)
     */
    public static UByte ubyte(byte value) {
        return new UByte(value);
    }

    /**
     * Create an <code>unsigned byte</code>
     *
     * @throws NumberFormatException If <code>value</code> is not in the range
     *             of an <code>unsigned byte</code>
     * @see UByte#UByte(short)
     */
    public static UByte ubyte(short value) throws NumberFormatException {
        return new UByte(value);
    }

    /**
     * Create an <code>unsigned short</code>
     *
     * @throws NumberFormatException If <code>value</code> does not contain a
     *             parsable <code>unsigned short</code>.
     * @see UShort#UShort(String)
     */
    public static UShort ushort(String value) throws NumberFormatException {
        return value == null ? null : new UShort(value);
    }

    /**
     * Create an <code>unsigned short</code> by masking it with
     * <code>0xFFFF</code> i.e. <code>(short) -1</code> becomes
     * <code>(ushort) 65535</code>
     *
     * @see UShort#UShort(short)
     */
    public static UShort ushort(short value) {
        return new UShort(value);
    }

    /**
     * Create an <code>unsigned short</code>
     *
     * @throws NumberFormatException If <code>value</code> is not in the range
     *             of an <code>unsigned short</code>
     * @see UShort#UShort(int)
     */
    public static UShort ushort(int value) throws NumberFormatException {
        return new UShort(value);
    }

    /**
     * Create an <code>unsigned int</code>
     *
     * @throws NumberFormatException If <code>value</code> does not contain a
     *             parsable <code>unsigned int</code>.
     * @see UInteger#UInteger(String)
     */
    public static UInteger uint(String value) throws NumberFormatException {
        return value == null ? null : new UInteger(value);
    }

    /**
     * Create an <code>unsigned int</code> by masking it with
     * <code>0xFFFFFFFF</code> i.e. <code>(int) -1</code> becomes
     * <code>(uint) 4294967295</code>
     *
     * @see UInteger#UInteger(int)
     */
    public static UInteger uint(int value) {
        return new UInteger(value);
    }

    /**
     * Create an <code>unsigned int</code>
     *
     * @throws NumberFormatException If <code>value</code> is not in the range
     *             of an <code>unsigned int</code>
     * @see UInteger#UInteger(long)
     */
    public static UInteger uint(long value) throws NumberFormatException {
        return new UInteger(value);
    }

    /**
     * Create an <code>unsigned long</code>
     *
     * @throws NumberFormatException If <code>value</code> does not contain a
     *             parsable <code>unsigned long</code>.
     * @see ULong#ULong(String)
     */
    public static ULong ulong(String value) throws NumberFormatException {
        return value == null ? null : new ULong(value);
    }

    /**
     * Create an <code>unsigned long</code> by masking it with
     * <code>0xFFFFFFFFFFFFFFFF</code> i.e. <code>(long) -1</code> becomes
     * <code>(uint) 18446744073709551615</code>
     *
     * @see ULong#ULong(long)
     */
    public static ULong ulong(long value) {
        return new ULong(value);
    }

    /**
     * Create an <code>unsigned long</code>
     *
     * @throws NumberFormatException If <code>value</code> is not in the range
     *             of an <code>unsigned long</code>
     * @see ULong#ULong(BigInteger)
     */
    public static ULong ulong(BigInteger value) throws NumberFormatException {
        return new ULong(value);
    }

    /**
     * No instances
     */
    private Unsigned() {}
}
