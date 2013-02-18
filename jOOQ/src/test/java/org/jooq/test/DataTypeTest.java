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

package org.jooq.test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.jooq.impl.Factory.fieldByName;

import java.sql.Timestamp;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.test.data.Table1;
import org.jooq.types.DayToSecond;
import org.jooq.types.Interval;
import org.jooq.types.YearToMonth;

import org.jmock.Expectations;
import org.junit.Test;


/**
 * A test suite for jOOQ functionality related to data types
 *
 * @author Lukas Eder
 */
public class DataTypeTest extends AbstractTest {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    public void testComparisonPredicateTypeCoercion() throws Exception {
        // This test checks whether automatic type coercion works well for
        // comparison predicates

        Field integer = Table1.FIELD_ID1;
        Field string = Table1.FIELD_NAME1;
        Field object = fieldByName("ANY");

        // Check if a correct type was coerced correctly
        // ---------------------------------------------
        {
            Condition int_int = integer.eq(1);
            assertEquals("\"TABLE1\".\"ID1\" = 1", r_refI().render(int_int));
            context.checking(new Expectations() {{
                oneOf(statement).setInt(1, 1);
            }});

            assertEquals(2, b_ref().bind(int_int).peekIndex());
            context.assertIsSatisfied();
        }

        {
            Condition string_string = string.eq("1");
            assertEquals("\"TABLE1\".\"NAME1\" = '1'", r_refI().render(string_string));
            context.checking(new Expectations() {{
                oneOf(statement).setString(1, "1");
            }});

            assertEquals(2, b_ref().bind(string_string).peekIndex());
            context.assertIsSatisfied();
        }

        // Check if a convertible type was coerced correctly
        // -------------------------------------------------
        {
            Condition int_string = integer.eq("1");
            assertEquals("\"TABLE1\".\"ID1\" = 1", r_refI().render(int_string));
            context.checking(new Expectations() {{
                oneOf(statement).setInt(1, 1);
            }});

            assertEquals(2, b_ref().bind(int_string).peekIndex());
            context.assertIsSatisfied();

            Condition string_int = string.eq(1);
            assertEquals("\"TABLE1\".\"NAME1\" = '1'", r_refI().render(string_int));
            context.checking(new Expectations() {{
                oneOf(statement).setString(1, "1");
            }});

            assertEquals(2, b_ref().bind(string_int).peekIndex());
            context.assertIsSatisfied();
        }

        // Check if ...
        // ------------
        {
            Condition object_int = object.eq(1);
            assertEquals("\"ANY\" = 1", r_refI().render(object_int));
            context.checking(new Expectations() {{
                oneOf(statement).setInt(1, 1);
            }});

            assertEquals(2, b_ref().bind(object_int).peekIndex());
            context.assertIsSatisfied();
        }

        {
            Condition object_string = object.eq("1");
            assertEquals("\"ANY\" = '1'", r_refI().render(object_string));
            context.checking(new Expectations() {{
                oneOf(statement).setString(1, "1");
            }});

            assertEquals(2, b_ref().bind(object_string).peekIndex());
            context.assertIsSatisfied();
        }

        {
            Condition object_date = object.eq(Timestamp.valueOf("2012-12-21 15:30:00.0"));
            assertEquals("\"ANY\" = timestamp '2012-12-21 15:30:00.0'", r_refI().render(object_date));
            context.checking(new Expectations() {{
                oneOf(statement).setTimestamp(1, Timestamp.valueOf("2012-12-21 15:30:00.0"));
            }});

            assertEquals(2, b_ref().bind(object_date).peekIndex());
            context.assertIsSatisfied();
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    public void testInPredicateTypeCoercion() throws Exception {
        // [#2227] This test checks whether automatic type coercion works well for
        // IN predicates

        Field integer = Table1.FIELD_ID1;
        Field string = Table1.FIELD_NAME1;
        Field object = fieldByName("ANY");

        // Check if a correct type was coerced correctly
        // ---------------------------------------------
        {
            Condition int_int = integer.in(1);
            assertEquals("\"TABLE1\".\"ID1\" in (1)", r_refI().render(int_int));
            context.checking(new Expectations() {{
                oneOf(statement).setInt(1, 1);
            }});

            assertEquals(2, b_ref().bind(int_int).peekIndex());
            context.assertIsSatisfied();
        }

        {
            Condition string_string = string.in("1");
            assertEquals("\"TABLE1\".\"NAME1\" in ('1')", r_refI().render(string_string));
            context.checking(new Expectations() {{
                oneOf(statement).setString(1, "1");
            }});

            assertEquals(2, b_ref().bind(string_string).peekIndex());
            context.assertIsSatisfied();
        }

        // Check if a convertible type was coerced correctly
        // -------------------------------------------------
        {
            Condition int_string = integer.in("1");
            assertEquals("\"TABLE1\".\"ID1\" in (1)", r_refI().render(int_string));
            context.checking(new Expectations() {{
                oneOf(statement).setInt(1, 1);
            }});

            assertEquals(2, b_ref().bind(int_string).peekIndex());
            context.assertIsSatisfied();

            Condition string_int = string.in(1);
            assertEquals("\"TABLE1\".\"NAME1\" in ('1')", r_refI().render(string_int));
            context.checking(new Expectations() {{
                oneOf(statement).setString(1, "1");
            }});

            assertEquals(2, b_ref().bind(string_int).peekIndex());
            context.assertIsSatisfied();
        }

        // Check if ...
        // ------------
        {
            Condition object_int = object.in(1);
            assertEquals("\"ANY\" in (1)", r_refI().render(object_int));
            context.checking(new Expectations() {{
                oneOf(statement).setInt(1, 1);
            }});

            assertEquals(2, b_ref().bind(object_int).peekIndex());
            context.assertIsSatisfied();
        }

        {
            Condition object_string = object.in("1");
            assertEquals("\"ANY\" in ('1')", r_refI().render(object_string));
            context.checking(new Expectations() {{
                oneOf(statement).setString(1, "1");
            }});

            assertEquals(2, b_ref().bind(object_string).peekIndex());
            context.assertIsSatisfied();
        }
    }

    @Test
    public void testYearToMonth() {
        for (int i = 0; i <= 5; i++) {
            intervalChecks(i * 12, new YearToMonth(i));
            intervalChecks(i * -12, new YearToMonth(i).neg());
            intervalChecks(i, new YearToMonth(0, i));
        }
    }

    @Test
    public void testDayToSecond() {
        for (double i = -1394892834972.0; i <= 23487289374987.0; i += 283749827.3839293) {
            intervalChecks(i, DayToSecond.valueOf(i));
        }

        for (int i = 0; i <= 5; i++) {
            intervalChecks(i * 1000 * 86400.0, new DayToSecond(i));
            intervalChecks(i * 1000 * 3600.0, new DayToSecond(0, i));
            intervalChecks(i * 1000 * 60.0, new DayToSecond(0, 0, i));
            intervalChecks(i * 1000, new DayToSecond(0, 0, 0, i));
            intervalChecks(i / 1000000.0, new DayToSecond(0, 0, 0, 0, i));
        }
    }

    private <I extends Number & Interval> void intervalChecks(Number expected, I interval) {
        // Allow some floating point arithmetic inaccuracy
        assertTrue(Math.abs(Double.doubleToLongBits(expected.doubleValue()) - Double.doubleToLongBits(interval.doubleValue())) < 50);
        assertTrue(Math.abs(Float.floatToIntBits(expected.floatValue()) - Float.floatToIntBits(interval.floatValue())) < 5);

        assertEquals(expected.byteValue(), interval.byteValue());
        assertEquals(expected.shortValue(), interval.shortValue());
        assertEquals(expected.intValue(), interval.intValue());
        assertEquals(expected.longValue(), interval.longValue());

        if (interval instanceof YearToMonth) {
            YearToMonth y = YearToMonth.valueOf(interval.toString());
            assertEquals(interval, y);
        }
        else {
            DayToSecond m = DayToSecond.valueOf(interval.toString());
            assertEquals(interval, m);
            assertEquals(m.getDays(),
                m.getSign() * (int) m.getTotalDays());
            assertEquals(m.getDays() * 24 + m.getHours(),
                m.getSign() * (int) m.getTotalHours());
            assertEquals(m.getDays() * 24 * 60 + m.getHours() * 60 + m.getMinutes(),
                m.getSign() * (int) m.getTotalMinutes());
        }
    }
}
