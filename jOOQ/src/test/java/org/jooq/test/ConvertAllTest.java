/**
 * Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)
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
package org.jooq.test;

import static java.time.temporal.ChronoField.INSTANT_SECONDS;
import static java.time.temporal.ChronoField.MILLI_OF_DAY;
import static java.time.temporal.ChronoField.MILLI_OF_SECOND;
import static org.jooq.tools.reflect.Reflect.wrapper;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.time.temporal.Temporal;

import org.jooq.Record;
import org.jooq.Result;
import org.jooq.tools.Convert;

import org.junit.Test;

/**
 * Unit tests for data type conversion
 *
 * @author Lukas Eder
 */
public class ConvertAllTest extends AbstractTest {

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
        testConversion(zeroDate(), new Date(0), String.class);
        testConversion(zeroTime(), new Time(0), String.class);
        testConversion(zeroTimestamp(), new Timestamp(0), String.class);
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
        testConversion(true, "t", Boolean.class);
        testConversion(false, "f", Boolean.class);
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


        Date date = Date.valueOf("2001-02-03");
        testConversion(date.getTime(), date, Long.class);
        testConversion(date.getTime(), date.toLocalDate(), Long.class);


        Time time = Time.valueOf("04:05:06");
        testConversion(time.getTime(), time, Long.class);
        testConversion(time.getTime(), time.toLocalTime(), Long.class);

        OffsetTime t1 = OffsetTime.parse("04:05:06.789Z");
        OffsetTime t2 = OffsetTime.parse("04:05:06.789+02:00");
        testConversion(millis(t1), t1, Long.class);
        testConversion(millis(t2), t2, Long.class);


        Timestamp timestamp = Timestamp.valueOf("2001-02-03 04:05:06.789");
        testConversion(timestamp.getTime(), timestamp, Long.class);
        testConversion(timestamp.getTime(), timestamp.toLocalDateTime(), Long.class);

        OffsetDateTime dt1 = OffsetDateTime.parse("2001-02-03T04:05:06.789Z");
        OffsetDateTime dt2 = OffsetDateTime.parse("2001-02-03T04:05:06.789+02:00");
        testConversion(millis(dt1), dt1, Long.class);
        testConversion(millis(dt2), dt2, Long.class);
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

    @Test
    public void testToJDBCArray() throws SQLException {
        Object[] from1 = null;
        java.sql.Array a1 = Convert.convert(from1, java.sql.Array.class);
        assertNull(a1);

        Object[] from2 = new Object[0];
        java.sql.Array a2 = Convert.convert(from2, java.sql.Array.class);
        Result<Record> r2 = create.fetch(a2.getResultSet());
        assertArrayEquals(from2, (Object[]) a2.getArray());
        assertEquals(0, r2.size());
        assertEquals(2, r2.fields().length);
        assertEquals("INDEX", r2.field(0).getName());
        assertEquals(Long.class, r2.field(0).getType());
        assertEquals("VALUE", r2.field(1).getName());
        assertEquals(Object.class, r2.field(1).getType());

        Object[] from3 = { 1 };
        java.sql.Array a3 = Convert.convert(from3, java.sql.Array.class);
        Result<Record> r3 = create.fetch(a3.getResultSet());
        assertArrayEquals(from3, (Object[]) a3.getArray());
        assertEquals(1, r3.size());
        assertEquals(1L, r3.getValue(0, "INDEX"));
        assertEquals(1, r3.getValue(0, "VALUE"));
        assertEquals(2, r3.fields().length);
        assertEquals("INDEX", r3.field(0).getName());
        assertEquals(Long.class, r3.field(0).getType());
        assertEquals("VALUE", r3.field(1).getName());
        assertEquals(Object.class, r3.field(1).getType());

        String[] from4 = { "A", "B" };
        java.sql.Array a4 = Convert.convert(from4, java.sql.Array.class);
        Result<Record> r4 = create.fetch(a4.getResultSet());
        assertArrayEquals(from4, (String[]) a4.getArray());
        assertEquals(2, r4.size());
        assertEquals(1L, r4.getValue(0, "INDEX"));
        assertEquals("A", r4.getValue(0, "VALUE"));
        assertEquals(2L, r4.getValue(1, "INDEX"));
        assertEquals("B", r4.getValue(1, "VALUE"));
        assertEquals(2, r4.fields().length);
        assertEquals("INDEX", r4.field(0).getName());
        assertEquals(Long.class, r4.field(0).getType());
        assertEquals("VALUE", r4.field(1).getName());
        assertEquals(String.class, r4.field(1).getType());
    }

    @Test
    public void testToDate() {
        Date date = Date.valueOf("2001-02-03");

        testConversion(date, "2001-02-03", Date.class);
        testConversion(date, date.getTime(), Date.class);
        testConversion(date, date.toLocalDate(), Date.class);
    }

    @Test
    public void testToLocalDate() {
        Date date = Date.valueOf("2001-02-03");

        testConversion(date.toLocalDate(), "2001-02-03", LocalDate.class);
        testConversion(date.toLocalDate(), date.getTime(), LocalDate.class);
        testConversion(date.toLocalDate(), date, LocalDate.class);
    }

    @Test
    public void testToTime() {
        Time time = Time.valueOf("04:05:06");

        testConversion(time, "04:05:06", Time.class);
        testConversion(time, time.getTime(), Time.class);
        testConversion(time, time.toLocalTime(), Time.class);

        OffsetTime o1 = OffsetTime.parse("04:05:06.789Z");
        OffsetTime o2 = OffsetTime.parse("04:05:06.789+02:00");
        testConversion(new Time(millis(o1)), o1, Time.class);
        testConversion(new Time(millis(o2)), o2, Time.class);
    }

    @Test
    public void testToLocalTime() {
        Time time = Time.valueOf("04:05:06");

        testConversion(time.toLocalTime(), "04:05:06", LocalTime.class);
        testConversion(time.toLocalTime(), time.getTime(), LocalTime.class);
        testConversion(time.toLocalTime(), time, LocalTime.class);

        OffsetTime o1 = OffsetTime.parse("04:05:06.789Z");
        OffsetTime o2 = OffsetTime.parse("04:05:06.789+02:00");
        testConversion(new Time(millis(o1)).toLocalTime(), o1, LocalTime.class);
        testConversion(new Time(millis(o2)).toLocalTime(), o2, LocalTime.class);
    }

    @Test
    public void testToOffsetTime() {
        Time time = Time.valueOf("04:05:06");
        ZoneOffset offset = OffsetDateTime.now().getOffset();

        testConversion(time.toLocalTime().atOffset(offset), "04:05:06", OffsetTime.class);
        testConversion(time.toLocalTime().atOffset(offset), time.getTime(), OffsetTime.class);
        testConversion(time.toLocalTime().atOffset(offset), time, OffsetTime.class);
    }

    @Test
    public void testToTimestamp() {
        Timestamp t1 = Timestamp.valueOf("2001-02-03 04:05:06");
        Timestamp t2 = Timestamp.valueOf("2001-02-03 04:05:06.7");
        Timestamp t3 = Timestamp.valueOf("2001-02-03 04:05:06.789");

        testConversion(t1, "2001-02-03 04:05:06", Timestamp.class);
        testConversion(t2, "2001-02-03 04:05:06.7", Timestamp.class);
        testConversion(t3, "2001-02-03 04:05:06.789", Timestamp.class);

        testConversion(t1, t1.getTime(), Timestamp.class);
        testConversion(t2, t2.getTime(), Timestamp.class);
        testConversion(t3, t3.getTime(), Timestamp.class);

        testConversion(t1, t1.toLocalDateTime(), Timestamp.class);
        testConversion(t2, t2.toLocalDateTime(), Timestamp.class);
        testConversion(t3, t3.toLocalDateTime(), Timestamp.class);

        OffsetDateTime o1 = OffsetDateTime.parse("2001-02-03T04:05:06.789Z");
        OffsetDateTime o2 = OffsetDateTime.parse("2001-02-03T04:05:06.789+02:00");
        testConversion(new Timestamp(millis(o1)), o1, Timestamp.class);
        testConversion(new Timestamp(millis(o2)), o2, Timestamp.class);
    }

    @Test
    public void testToLocalDateTime() {
        Timestamp t1 = Timestamp.valueOf("2001-02-03 04:05:06");
        Timestamp t2 = Timestamp.valueOf("2001-02-03 04:05:06.7");
        Timestamp t3 = Timestamp.valueOf("2001-02-03 04:05:06.789");

        testConversion(t1.toLocalDateTime(), "2001-02-03 04:05:06", LocalDateTime.class);
        testConversion(t2.toLocalDateTime(), "2001-02-03 04:05:06.7", LocalDateTime.class);
        testConversion(t3.toLocalDateTime(), "2001-02-03 04:05:06.789", LocalDateTime.class);

        testConversion(t1.toLocalDateTime(), t1.getTime(), LocalDateTime.class);
        testConversion(t2.toLocalDateTime(), t2.getTime(), LocalDateTime.class);
        testConversion(t3.toLocalDateTime(), t3.getTime(), LocalDateTime.class);

        testConversion(t1.toLocalDateTime(), t1, LocalDateTime.class);
        testConversion(t2.toLocalDateTime(), t2, LocalDateTime.class);
        testConversion(t3.toLocalDateTime(), t3, LocalDateTime.class);

        OffsetDateTime o1 = OffsetDateTime.parse("2001-02-03T04:05:06.789Z");
        OffsetDateTime o2 = OffsetDateTime.parse("2001-02-03T04:05:06.789+02:00");
        testConversion(new Timestamp(millis(o1)).toLocalDateTime(), o1, LocalDateTime.class);
        testConversion(new Timestamp(millis(o2)).toLocalDateTime(), o2, LocalDateTime.class);
    }

    @Test
    public void testToOffsetDateTime() {
        Timestamp t1 = Timestamp.valueOf("2001-02-03 04:05:06");
        Timestamp t2 = Timestamp.valueOf("2001-02-03 04:05:06.7");
        Timestamp t3 = Timestamp.valueOf("2001-02-03 04:05:06.789");
        ZoneOffset offset = OffsetDateTime.now().getOffset();

        testConversion(t1.toLocalDateTime().atOffset(offset), "2001-02-03 04:05:06", OffsetDateTime.class);
        testConversion(t2.toLocalDateTime().atOffset(offset), "2001-02-03 04:05:06.7", OffsetDateTime.class);
        testConversion(t3.toLocalDateTime().atOffset(offset), "2001-02-03 04:05:06.789", OffsetDateTime.class);

        testConversion(t1.toLocalDateTime().atOffset(offset), t1.getTime(), OffsetDateTime.class);
        testConversion(t2.toLocalDateTime().atOffset(offset), t2.getTime(), OffsetDateTime.class);
        testConversion(t3.toLocalDateTime().atOffset(offset), t3.getTime(), OffsetDateTime.class);

        testConversion(t1.toLocalDateTime().atOffset(offset), t1, OffsetDateTime.class);
        testConversion(t2.toLocalDateTime().atOffset(offset), t2, OffsetDateTime.class);
        testConversion(t3.toLocalDateTime().atOffset(offset), t3, OffsetDateTime.class);
    }

    private Long millis(Temporal t) {
        if (t.isSupported(INSTANT_SECONDS))
            return 1000 * t.getLong(INSTANT_SECONDS) + t.getLong(MILLI_OF_SECOND);
        else
            return t.getLong(MILLI_OF_DAY);
    }
}
