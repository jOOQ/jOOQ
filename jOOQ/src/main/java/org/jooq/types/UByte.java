/*
 * Copyright (c) 2011-2017, Data Geekery GmbH (http://www.datageekery.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jooq.types;

import java.io.ObjectStreamException;
import java.math.BigInteger;

/**
 * The <code>unsigned byte</code> type
 *
 * @author Lukas Eder
 * @author Ed Schaller
 */
public final class UByte extends UNumber implements Comparable<UByte> {

    /**
     * Generated UID
     */
    private static final long    serialVersionUID = -6821055240959745390L;

    /**
     * Cached values
     */
    private static final UByte[] VALUES           = mkValues();

    /**
     * A constant holding the minimum value an <code>unsigned byte</code> can
     * have, 0.
     */
    public static final short    MIN_VALUE        = 0x00;

    /**
     * A constant holding the maximum value an <code>unsigned byte</code> can
     * have, 2<sup>8</sup>-1.
     */
    public static final short    MAX_VALUE        = 0xff;

    /**
     * The value modelling the content of this <code>unsigned byte</code>
     */
    private final short          value;

    /**
     * Generate a cached value for each byte value.
     *
     * @return Array of cached values for UByte.
     */
    private static final UByte[] mkValues() {
        UByte[] ret = new UByte[256];

        for (int i = Byte.MIN_VALUE; i <= Byte.MAX_VALUE; i++)
            ret[i & MAX_VALUE] = new UByte((byte) i);
        return ret;
    }

    /**
     * Get an instance of an <code>unsigned byte</code>
     *
     * @throws NumberFormatException If <code>value</code> does not contain a
     *             parsable <code>unsigned byte</code>.
     */
    public static UByte valueOf(String value) throws NumberFormatException {
        return valueOfUnchecked(rangeCheck(Short.parseShort(value)));
    }

    /**
     * Get an instance of an <code>unsigned byte</code> by masking it with
     * <code>0xFF</code> i.e. <code>(byte) -1</code> becomes
     * <code>(ubyte) 255</code>
     */
    public static UByte valueOf(byte value) {
        return valueOfUnchecked((short) (value & MAX_VALUE));
    }

    /**
     * Get the value of a short without checking the value.
     */
    private static UByte valueOfUnchecked(short value) throws NumberFormatException {
        return VALUES[value & MAX_VALUE];
    }

    /**
     * Get an instance of an <code>unsigned byte</code>
     *
     * @throws NumberFormatException If <code>value</code> is not in the range
     *             of an <code>unsigned byte</code>
     */
    public static UByte valueOf(short value) throws NumberFormatException {
        return valueOfUnchecked(rangeCheck(value));
    }

    /**
     * Get an instance of an <code>unsigned byte</code>
     *
     * @throws NumberFormatException If <code>value</code> is not in the range
     *             of an <code>unsigned byte</code>
     */
    public static UByte valueOf(int value) throws NumberFormatException {
        return valueOfUnchecked(rangeCheck(value));
    }

    /**
     * Get an instance of an <code>unsigned byte</code>
     *
     * @throws NumberFormatException If <code>value</code> is not in the range
     *             of an <code>unsigned byte</code>
     */
    public static UByte valueOf(long value) throws NumberFormatException {
        return valueOfUnchecked(rangeCheck(value));
    }

    /**
     * Create an <code>unsigned byte</code>
     *
     * @throws NumberFormatException If <code>value</code> is not in the range
     *             of an <code>unsigned byte</code>
     */
    private UByte(long value) throws NumberFormatException {
        this.value = rangeCheck(value);
    }

    /**
     * Create an <code>unsigned byte</code>
     *
     * @throws NumberFormatException If <code>value</code> is not in the range
     *             of an <code>unsigned byte</code>
     */
    private UByte(int value) throws NumberFormatException {
        this.value = rangeCheck(value);
    }

    /**
     * Create an <code>unsigned byte</code>
     *
     * @throws NumberFormatException If <code>value</code> is not in the range
     *             of an <code>unsigned byte</code>
     */
    private UByte(short value) throws NumberFormatException {
        this.value = rangeCheck(value);
    }

    /**
     * Create an <code>unsigned byte</code> by masking it with <code>0xFF</code>
     * i.e. <code>(byte) -1</code> becomes <code>(ubyte) 255</code>
     */
    private UByte(byte value) {
        this.value = (short) (value & MAX_VALUE);
    }

    /**
     * Create an <code>unsigned byte</code>
     *
     * @throws NumberFormatException If <code>value</code> does not contain a
     *             parsable <code>unsigned byte</code>.
     */
    private UByte(String value) throws NumberFormatException {
        this.value = rangeCheck(Short.parseShort(value));
    }

    /**
     * Throw exception if value out of range (short version)
     *
     * @param value Value to check
     * @return value if it is in range
     * @throws NumberFormatException if value is out of range
     */
    private static short rangeCheck(short value) throws NumberFormatException {
        if (value < MIN_VALUE || value > MAX_VALUE) {
            throw new NumberFormatException("Value is out of range : " + value);
        }
        return value;
    }

    /**
     * Throw exception if value out of range (int version)
     *
     * @param value Value to check
     * @return value if it is in range
     * @throws NumberFormatException if value is out of range
     */
    private static short rangeCheck(int value) throws NumberFormatException {
        if (value < MIN_VALUE || value > MAX_VALUE) {
            throw new NumberFormatException("Value is out of range : " + value);
        }
        return (short) value;
    }

    /**
     * Throw exception if value out of range (long version)
     *
     * @param value Value to check
     * @return value if it is in range
     * @throws NumberFormatException if value is out of range
     */
    private static short rangeCheck(long value) throws NumberFormatException {
        if (value < MIN_VALUE || value > MAX_VALUE) {
            throw new NumberFormatException("Value is out of range : " + value);
        }
        return (short) value;
    }

    /**
     * Replace version read through deserialization with cached version. Note
     * that this does not use the {@link #valueOfUnchecked(short)} as we have no
     * guarantee that the value from the stream is valid.
     *
     * @return cached instance of this object's value
     * @throws ObjectStreamException
     */
    private Object readResolve() throws ObjectStreamException {
        return valueOf(value);
    }

    @Override
    public int intValue() {
        return value;
    }

    @Override
    public long longValue() {
        return value;
    }

    @Override
    public float floatValue() {
        return value;
    }

    @Override
    public double doubleValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return Short.valueOf(value).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof UByte) {
            return value == ((UByte) obj).value;
        }

        return false;
    }

    @Override
    public String toString() {
        return Short.valueOf(value).toString();
    }

    @Override
    public int compareTo(UByte o) {
        return (value < o.value ? -1 : (value == o.value ? 0 : 1));
    }

    @Override
    public BigInteger toBigInteger() {
        return BigInteger.valueOf(value);
    }
}
