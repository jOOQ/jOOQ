/**
 * Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */

package org.jooq.test;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.val;
import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.test.data.Table1;
import org.jooq.types.DayToSecond;
import org.jooq.types.Interval;
import org.jooq.types.YearToMonth;

import org.jmock.Expectations;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;


/**
 * A test suite for jOOQ functionality related to data types
 *
 * @author Lukas Eder
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DataTypeTest extends AbstractTest {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    public void testComparisonPredicateTypeCoercion() throws Exception {
        // This test checks whether automatic type coercion works well for
        // comparison predicates

        Field integer = Table1.FIELD_ID1;
        Field string = Table1.FIELD_NAME1;
        Field object = field(name("ANY"));

        // Check if a correct type was coerced correctly
        // ---------------------------------------------
        {
            Condition int_int = integer.eq(1);
            assertEquals("`TABLE1`.`ID1` = 1", r_refI().render(int_int));
            context.checking(new Expectations() {{
                oneOf(statement).setInt(1, 1);
            }});

            assertEquals(2, b_ref().visit(int_int).peekIndex());
            context.assertIsSatisfied();
        }

        {
            Condition string_string = string.eq("1");
            assertEquals("`TABLE1`.`NAME1` = '1'", r_refI().render(string_string));
            context.checking(new Expectations() {{
                oneOf(statement).setString(1, "1");
            }});

            assertEquals(2, b_ref().visit(string_string).peekIndex());
            context.assertIsSatisfied();
        }

        // Check if a convertible type was coerced correctly
        // -------------------------------------------------
        {
            Condition int_string = integer.eq("1");
            assertEquals("`TABLE1`.`ID1` = 1", r_refI().render(int_string));
            context.checking(new Expectations() {{
                oneOf(statement).setInt(1, 1);
            }});

            assertEquals(2, b_ref().visit(int_string).peekIndex());
            context.assertIsSatisfied();

            Condition string_int = string.eq(1);
            assertEquals("`TABLE1`.`NAME1` = '1'", r_refI().render(string_int));
            context.checking(new Expectations() {{
                oneOf(statement).setString(1, "1");
            }});

            assertEquals(2, b_ref().visit(string_int).peekIndex());
            context.assertIsSatisfied();
        }

        // Check if ...
        // ------------
        {
            Condition object_int = object.eq(1);
            assertEquals("`ANY` = 1", r_refI().render(object_int));
            context.checking(new Expectations() {{
                oneOf(statement).setInt(1, 1);
            }});

            assertEquals(2, b_ref().visit(object_int).peekIndex());
            context.assertIsSatisfied();
        }

        {
            Condition object_string = object.eq("1");
            assertEquals("`ANY` = '1'", r_refI().render(object_string));
            context.checking(new Expectations() {{
                oneOf(statement).setString(1, "1");
            }});

            assertEquals(2, b_ref().visit(object_string).peekIndex());
            context.assertIsSatisfied();
        }

        {
            Condition object_date = object.eq(Timestamp.valueOf("2012-12-21 15:30:00.0"));
            assertEquals("`ANY` = {ts '2012-12-21 15:30:00.0'}", r_refI().render(object_date));
            context.checking(new Expectations() {{
                oneOf(statement).setTimestamp(1, Timestamp.valueOf("2012-12-21 15:30:00.0"));
            }});

            assertEquals(2, b_ref().visit(object_date).peekIndex());
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
        Field object = field(name("ANY"));

        // Check if a correct type was coerced correctly
        // ---------------------------------------------
        {
            Condition int_int = integer.in(1);
            assertEquals("`TABLE1`.`ID1` in (1)", r_refI().render(int_int));
            context.checking(new Expectations() {{
                oneOf(statement).setInt(1, 1);
            }});

            assertEquals(2, b_ref().visit(int_int).peekIndex());
            context.assertIsSatisfied();
        }

        {
            Condition string_string = string.in("1");
            assertEquals("`TABLE1`.`NAME1` in ('1')", r_refI().render(string_string));
            context.checking(new Expectations() {{
                oneOf(statement).setString(1, "1");
            }});

            assertEquals(2, b_ref().visit(string_string).peekIndex());
            context.assertIsSatisfied();
        }

        // Check if a convertible type was coerced correctly
        // -------------------------------------------------
        {
            Condition int_string = integer.in("1");
            assertEquals("`TABLE1`.`ID1` in (1)", r_refI().render(int_string));
            context.checking(new Expectations() {{
                oneOf(statement).setInt(1, 1);
            }});

            assertEquals(2, b_ref().visit(int_string).peekIndex());
            context.assertIsSatisfied();

            Condition string_int = string.in(1);
            assertEquals("`TABLE1`.`NAME1` in ('1')", r_refI().render(string_int));
            context.checking(new Expectations() {{
                oneOf(statement).setString(1, "1");
            }});

            assertEquals(2, b_ref().visit(string_int).peekIndex());
            context.assertIsSatisfied();
        }

        // Check if ...
        // ------------
        {
            Condition object_int = object.in(1);
            assertEquals("`ANY` in (1)", r_refI().render(object_int));
            context.checking(new Expectations() {{
                oneOf(statement).setInt(1, 1);
            }});

            assertEquals(2, b_ref().visit(object_int).peekIndex());
            context.assertIsSatisfied();
        }

        {
            Condition object_string = object.in("1");
            assertEquals("`ANY` in ('1')", r_refI().render(object_string));
            context.checking(new Expectations() {{
                oneOf(statement).setString(1, "1");
            }});

            assertEquals(2, b_ref().visit(object_string).peekIndex());
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

    @Test
    public void testCoercion() throws Exception {
        Field<String> s = val(1).coerce(String.class);
        Field<Integer> i = val("2").coerce(Integer.class);

        assertEquals("?", r_ref().render(s));
        assertEquals("?", r_ref().render(i));
        assertEquals("1", r_refI().render(s));
        assertEquals("'2'", r_refI().render(i));

        context.checking(new Expectations() {{
            oneOf(statement).setInt(1, 1);
            oneOf(statement).setString(2, "2");
        }});

        assertEquals(3, b_ref().visit(s).visit(i).peekIndex());
        context.assertIsSatisfied();
    }
}
