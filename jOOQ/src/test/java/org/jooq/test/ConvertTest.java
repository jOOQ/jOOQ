/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
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
package org.jooq.test;

import static org.jooq.tools.reflect.Reflect.wrapper;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import org.jooq.tools.Convert;

import org.junit.Test;

/**
 * Unit tests for data type conversion
 *
 * @author Lukas Eder
 */
public class ConvertTest {

    @Test
    public void testToObject() {
        testConversion(null, null, Object.class);
        testConversion("a", "a", Object.class);
        testConversion(true, true, Object.class);
        testConversion((byte) 1, (byte) 1, Object.class);
        testConversion((short) 1, (short) 1, Object.class);
        testConversion(1, 1, Object.class);
        testConversion(1L, 1L, Object.class);
        testConversion(1.0d, 1.0d, Object.class);
        testConversion(1.0f, 1.0f, Object.class);
        testConversion(BigInteger.ONE, BigInteger.ONE, Object.class);
        testConversion(BigDecimal.ONE, BigDecimal.ONE, Object.class);
        testConversion(new Date(0), new Date(0), Object.class);
        testConversion(new Time(0), new Time(0), Object.class);
        testConversion(new Timestamp(0), new Timestamp(0), Object.class);
    }

    @Test
    public void testToString() {
        testConversion(null, null, String.class);
        testConversion("a", "a", String.class);
        testConversion("true", true, String.class);
        testConversion("1", (byte) 1, String.class);
        testConversion("1", (short) 1, String.class);
        testConversion("1", 1, String.class);
        testConversion("1", 1L, String.class);
        testConversion("1.0", 1.0d, String.class);
        testConversion("1.0", 1.0f, String.class);
        testConversion("1", BigInteger.ONE, String.class);
        testConversion("1", BigDecimal.ONE, String.class);
        testConversion("1970-01-01", new Date(0), String.class);
        testConversion("01:00:00", new Time(0), String.class);
        testConversion("1970-01-01 01:00:00.0", new Timestamp(0), String.class);
    }

    @Test
    public void testToBoolean() {
        testConversion(false, null, boolean.class);
        testConversion(false, "xmf", boolean.class);
        testConversion(null, null, Boolean.class);
        testConversion(true, "true", Boolean.class);
        testConversion(true, "1", Boolean.class);
        testConversion(true, "yes", Boolean.class);
        testConversion(false, "false", Boolean.class);
        testConversion(false, "0", Boolean.class);
        testConversion(false, "no", Boolean.class);
        testConversion(null, "xmf", Boolean.class);
        testConversion(true, true, Boolean.class);
        testConversion(false, false, Boolean.class);
        testConversion(null, (byte) 2, Boolean.class);
        testConversion(true, (byte) 1, Boolean.class);
        testConversion(false, (byte) 0, Boolean.class);
        testConversion(null, (short) 2, Boolean.class);
        testConversion(true, (short) 1, Boolean.class);
        testConversion(false, (short) 0, Boolean.class);
        testConversion(null, 2, Boolean.class);
        testConversion(true, 1, Boolean.class);
        testConversion(false, 0, Boolean.class);
        testConversion(null, 2L, Boolean.class);
        testConversion(true, 1L, Boolean.class);
        testConversion(false, 0L, Boolean.class);
        testConversion(null, 2.0, Boolean.class);
        testConversion(true, 1.0, Boolean.class);
        testConversion(false, 0.0, Boolean.class);
        testConversion(null, BigInteger.TEN, Boolean.class);
        testConversion(true, BigInteger.ONE, Boolean.class);
        testConversion(false, BigInteger.ZERO, Boolean.class);
        testConversion(null, BigDecimal.TEN, Boolean.class);
        testConversion(true, BigDecimal.ONE, Boolean.class);
        testConversion(false, BigDecimal.ZERO, Boolean.class);
        testConversion(null, new Date(0), Boolean.class);
        testConversion(null, new Time(0), Boolean.class);
        testConversion(null, new Timestamp(0), Boolean.class);
    }

    @Test
    public void testToByte() {
        testConversion(null, null, Byte.class);
        testConversion((byte) 0, null, byte.class);
        testConversion((byte) 1, "1", Byte.class);
        testConversion((byte) 1, true, Byte.class);
        testConversion((byte) 0, false, Byte.class);
        testConversion((byte) 1, (byte) 1, Byte.class);
        testConversion((byte) 1, (short) 1, Byte.class);
        testConversion((byte) 1, 1, Byte.class);
        testConversion((byte) 1, 1L, Byte.class);
        testConversion((byte) 1, 1.0d, Byte.class);
        testConversion((byte) 1, 1.0f, Byte.class);
        testConversion((byte) 1, BigInteger.ONE, Byte.class);
        testConversion((byte) 1, BigDecimal.ONE, Byte.class);
        testConversion(null, new Date(0), Byte.class);
        testConversion(null, new Time(0), Byte.class);
        testConversion(null, new Timestamp(0), Byte.class);
    }

    @Test
    public void testToShort() {
        testConversion(null, null, Short.class);
        testConversion((short) 0, null, short.class);
        testConversion((short) 1, "1", Short.class);
        testConversion((short) 1, true, Short.class);
        testConversion((short) 0, false, Short.class);
        testConversion((short) 1, (byte) 1, Short.class);
        testConversion((short) 1, (short) 1, Short.class);
        testConversion((short) 1, 1, Short.class);
        testConversion((short) 1, 1L, Short.class);
        testConversion((short) 1, 1.0d, Short.class);
        testConversion((short) 1, 1.0f, Short.class);
        testConversion((short) 1, BigInteger.ONE, Short.class);
        testConversion((short) 1, BigDecimal.ONE, Short.class);
        testConversion(null, new Date(0), Short.class);
        testConversion(null, new Time(0), Short.class);
        testConversion(null, new Timestamp(0), Short.class);
    }

    @Test
    public void testToInteger() {
        testConversion(null, null, Integer.class);
        testConversion(0, null, int.class);
        testConversion(1, "1", Integer.class);
        testConversion(1, true, Integer.class);
        testConversion(0, false, Integer.class);
        testConversion(1, (byte) 1, Integer.class);
        testConversion(1, (short) 1, Integer.class);
        testConversion(1, 1, Integer.class);
        testConversion(1, 1L, Integer.class);
        testConversion(1, 1.0d, Integer.class);
        testConversion(1, 1.0f, Integer.class);
        testConversion(1, BigInteger.ONE, Integer.class);
        testConversion(1, BigDecimal.ONE, Integer.class);
        testConversion(null, new Date(0), Integer.class);
        testConversion(null, new Time(0), Integer.class);
        testConversion(null, new Timestamp(0), Integer.class);
    }

    @Test
    public void testToLong() {
        testConversion(null, null, Long.class);
        testConversion(0L, null, long.class);
        testConversion(1L, "1", Long.class);
        testConversion(1L, true, Long.class);
        testConversion(0L, false, Long.class);
        testConversion(1L, (byte) 1, Long.class);
        testConversion(1L, (short) 1, Long.class);
        testConversion(1L, 1, Long.class);
        testConversion(1L, 1L, Long.class);
        testConversion(1L, 1.0d, Long.class);
        testConversion(1L, 1.0f, Long.class);
        testConversion(1L, BigInteger.ONE, Long.class);
        testConversion(1L, BigDecimal.ONE, Long.class);
        testConversion(0L, new Date(0), Long.class);
        testConversion(10L, new Date(10), Long.class);
        testConversion(0L, new Time(0), Long.class);
        testConversion(10L, new Time(10), Long.class);
        testConversion(0L, new Timestamp(0), Long.class);
        testConversion(10L, new Timestamp(10), Long.class);
    }

    @Test
    public void testToBigInteger() {
        testConversion(null, null, BigInteger.class);
        testConversion(BigInteger.ONE, "1", BigInteger.class);
        testConversion(BigInteger.ONE, true, BigInteger.class);
        testConversion(BigInteger.ZERO, false, BigInteger.class);
        testConversion(BigInteger.ONE, (byte) 1, BigInteger.class);
        testConversion(BigInteger.ONE, (short) 1, BigInteger.class);
        testConversion(BigInteger.ONE, 1, BigInteger.class);
        testConversion(BigInteger.ONE, 1L, BigInteger.class);
        testConversion(BigInteger.ONE, 1.0d, BigInteger.class);
        testConversion(BigInteger.ONE, 1.0f, BigInteger.class);
        testConversion(BigInteger.ONE, BigInteger.ONE, BigInteger.class);
        testConversion(BigInteger.ONE, BigDecimal.ONE, BigInteger.class);
        testConversion(null, new Date(0), BigInteger.class);
        testConversion(null, new Time(0), BigInteger.class);
        testConversion(null, new Timestamp(0), BigInteger.class);
    }

    @Test
    public void testToBigDecimal() {
        testConversion(null, null, BigDecimal.class);
        testConversion(BigDecimal.ONE, "1", BigDecimal.class);
        testConversion(BigDecimal.ONE, true, BigDecimal.class);
        testConversion(BigDecimal.ZERO, false, BigDecimal.class);
        testConversion(BigDecimal.ONE, (byte) 1, BigDecimal.class);
        testConversion(BigDecimal.ONE, (short) 1, BigDecimal.class);
        testConversion(BigDecimal.ONE, 1, BigDecimal.class);
        testConversion(BigDecimal.ONE, 1L, BigDecimal.class);
        testConversion(new BigDecimal("1.0"), 1.0d, BigDecimal.class);
        testConversion(new BigDecimal("1.0"), 1.0f, BigDecimal.class);
        testConversion(BigDecimal.ONE, BigInteger.ONE, BigDecimal.class);
        testConversion(BigDecimal.ONE, BigDecimal.ONE, BigDecimal.class);
        testConversion(null, new Date(0), BigDecimal.class);
        testConversion(null, new Time(0), BigDecimal.class);
        testConversion(null, new Timestamp(0), BigDecimal.class);
    }

    @SuppressWarnings("unchecked")
    private static <T, U> void testConversion(U expected, T from, Class<U> toClass) {
        if (from != null) {
            assertEquals(from, Convert.convert(from, Object.class));
            assertEquals(from, Convert.convert(from, from.getClass()));
        }

        U conv1 = Convert.convert(from, toClass);
        assertEquals(expected, conv1);

        if (toClass.isPrimitive()) {
            assertTrue(wrapper(toClass).isInstance(conv1));
            return;
        }
        else if (expected == null) {
            assertNull(conv1);
        }
        else {
            assertTrue(toClass.isInstance(conv1));
        }

        Class<?> toArrayClass = Array.newInstance(toClass, 0).getClass();

        Object[] conv2 = Convert.convert(new Object[] { from, from }, new Class[] { toClass, toClass });
        U[] conv3 = (U[]) Convert.convert(new Object[] { from, from }, toClass);
        U[] conv4 = (U[]) Convert.convertArray(new Object[] { from, from }, toClass);
        U[] conv5 = (U[]) Convert.convertArray(new Object[] { from, from }, toArrayClass);

        assertEquals(2, conv2.length);
        assertEquals(2, conv3.length);
        assertEquals(2, conv4.length);
        assertEquals(2, conv5.length);
        assertEquals(expected, conv2[0]);
        assertEquals(expected, conv2[1]);
        assertEquals(expected, conv3[0]);
        assertEquals(expected, conv3[1]);
        assertEquals(expected, conv4[0]);
        assertEquals(expected, conv4[1]);
        assertEquals(expected, conv5[0]);
        assertEquals(expected, conv5[1]);
        assertTrue(Object[].class.isInstance(conv2));
        assertTrue(toArrayClass.isInstance(conv3));
        assertTrue(toArrayClass.isInstance(conv4));
        assertTrue(toArrayClass.isInstance(conv5));
    }
}
