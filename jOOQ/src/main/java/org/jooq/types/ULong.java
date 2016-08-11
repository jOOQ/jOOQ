/**
 * Copyright (c) 2009-2016, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
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
package org.jooq.types;

import java.math.BigInteger;

/**
 * The <code>unsigned long</code> type
 *
 * @author Lukas Eder
 */
public final class ULong extends UNumber implements Comparable<ULong> {

    /**
     * Generated UID
     */
    private static final long      serialVersionUID = -6821055240959745390L;

    /**
     * A constant holding the minimum value an <code>unsigned long</code> can
     * have, 0.
     */
    public static final BigInteger MIN_VALUE        = BigInteger.ZERO;

    /**
     * A constant holding the maximum value an <code>unsigned long</code> can
     * have, 2<sup>64</sup>-1.
     */
    public static final BigInteger MAX_VALUE        = new BigInteger("18446744073709551615");

    /**
     * A constant holding the maximum value + 1 an <code>signed long</code> can
     * have, 2<sup>63</sup>.
     */
    public static final BigInteger MAX_VALUE_LONG   = new BigInteger("9223372036854775808");

    /**
     * The value modelling the content of this <code>unsigned long</code>
     */
    private final BigInteger       value;

    /**
     * Create an <code>unsigned long</code>
     *
     * @throws NumberFormatException If <code>value</code> does not contain a
     *             parsable <code>unsigned long</code>.
     */
    public static ULong valueOf(String value) throws NumberFormatException {
        return new ULong(value);
    }

    /**
     * Create an <code>unsigned long</code> by masking it with
     * <code>0xFFFFFFFFFFFFFFFF</code> i.e. <code>(long) -1</code> becomes
     * <code>(uint) 18446744073709551615</code>
     */
    public static ULong valueOf(long value) {
        return new ULong(value);
    }

    /**
     * Create an <code>unsigned long</code>
     *
     * @throws NumberFormatException If <code>value</code> is not in the range
     *             of an <code>unsigned long</code>
     */
    public static ULong valueOf(BigInteger value) throws NumberFormatException {
        return new ULong(value);
    }

    /**
     * Create an <code>unsigned long</code>
     *
     * @throws NumberFormatException If <code>value</code> is not in the range
     *             of an <code>unsigned long</code>
     */
    private ULong(BigInteger value) throws NumberFormatException {
        this.value = value;
        rangeCheck();
    }

    /**
     * Create an <code>unsigned long</code> by masking it with
     * <code>0xFFFFFFFFFFFFFFFF</code> i.e. <code>(long) -1</code> becomes
     * <code>(uint) 18446744073709551615</code>
     */
    private ULong(long value) {
        if (value >= 0) {
            this.value = BigInteger.valueOf(value);
        }
        else {
            this.value = BigInteger.valueOf(value & Long.MAX_VALUE).add(MAX_VALUE_LONG);
        }
    }

    /**
     * Create an <code>unsigned long</code>
     *
     * @throws NumberFormatException If <code>value</code> does not contain a
     *             parsable <code>unsigned long</code>.
     */
    private ULong(String value) throws NumberFormatException {
        this.value = new BigInteger(value);
        rangeCheck();
    }

    private void rangeCheck() throws NumberFormatException {
        if (value.compareTo(MIN_VALUE) < 0 || value.compareTo(MAX_VALUE) > 0) {
            throw new NumberFormatException("Value is out of range : " + value);
        }
    }

    @Override
    public int intValue() {
        return value.intValue();
    }

    @Override
    public long longValue() {
        return value.longValue();
    }

    @Override
    public float floatValue() {
        return value.floatValue();
    }

    @Override
    public double doubleValue() {
        return value.doubleValue();
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof ULong) {
            return value.equals(((ULong) obj).value);
        }

        return false;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public int compareTo(ULong o) {
        return value.compareTo(o.value);
    }
}
