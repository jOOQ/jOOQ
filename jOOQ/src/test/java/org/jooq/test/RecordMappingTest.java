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
import static org.jooq.test.data.Table1.FIELD_DATE1;
import static org.jooq.test.data.Table1.FIELD_ID1;
import static org.jooq.test.data.Table1.FIELD_NAME1;
import static org.jooq.test.data.Table1.TABLE1;
import static org.jooq.tools.reflect.Reflect.accessible;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Id;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record4;
import org.jooq.Result;
import org.jooq.test.data.Table1Record;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;


/**
 * A test suite for jOOQ functionality related to records
 *
 * @author Lukas Eder
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RecordMappingTest extends AbstractTest {

    @Test
    public void testIntoBooleans() throws Exception {
        Field<Boolean> field = field("B", Boolean.class);
        Record1<Boolean> record = create.newRecord(field);
        record.setValue(field, true);

        // [#3101] Make sure this works for both "get" and "is" annotated getters
        BooleansWithAnnotations pojo = record.into(BooleansWithAnnotations.class);
        assertTrue(pojo.oneZero);
        assertTrue(pojo.oneZero1);
        assertTrue(pojo.oneZero2);
    }

    public static class BooleansWithAnnotations {

        @Column(name = "B")
        public boolean oneZero;

        public boolean oneZero1;
        public boolean oneZero2;

        public void setOneZero1(boolean oneZero1) {
            this.oneZero1 = oneZero1;
        }

        @Column(name = "B")
        public boolean getOneZero1() {
            return oneZero1;
        }

        public void setOneZero2(boolean oneZero2) {
            this.oneZero2 = oneZero2;
        }

        @Column(name = "B")
        public boolean isOneZero2() {
            return oneZero2;
        }

        @Override
        public String toString() {
            return "Boolean [oneZero=" + oneZero + ", oneZero1=" + oneZero1 + ", oneZero2=" + oneZero2 + "]";
        }
    }

    @Test
    public void testCaseSensitivity() throws Exception {

        // [#4128] Case sensitive @Column annotations have to be dealt with correctly
        Field<Boolean> field1 = field("a", Boolean.class);
        Field<Boolean> field2 = field("B", Boolean.class);
        Record2<Boolean, Boolean> record = create.newRecord(field1, field2);
        record.setValue(field1, true);
        record.setValue(field2, true);

        CaseSensitiveWithAnnotations pojo = record.into(CaseSensitiveWithAnnotations.class);
        assertTrue(pojo.a1);
        assertTrue(pojo.a2);
        assertFalse(pojo.a3);
        assertTrue(pojo.a4);

        assertTrue(pojo.b1);
        assertTrue(pojo.b2);
        assertTrue(pojo.b3);
        assertFalse(pojo.b4);
    }

    public static class CaseSensitiveWithAnnotations {

        // Insensitive
        @Column(name = "A")
        boolean a1;
        @Column(name = "a")
        boolean a2;

        // Sensitive
        @Column(name = "\"A\"")
        boolean a3;
        @Column(name = "\"a\"")
        boolean a4;

        boolean b1;
        boolean b2;
        boolean b3;
        boolean b4;

        public void setB1(boolean b1) {
            this.b1 = b1;
        }
        public void setB2(boolean b2) {
            this.b2 = b2;
        }
        public void setB3(boolean b3) {
            this.b3 = b3;
        }
        public void setB4(boolean b4) {
            this.b4 = b4;
        }

        @Column(name = "B")
        public boolean getB1() {
            return b1;
        }
        @Column(name = "b")
        public boolean getB2() {
            return b2;
        }
        @Column(name = "\"B\"")
        public boolean getB3() {
            return b3;
        }
        @Column(name = "\"b\"")
        public boolean getB4() {
            return b4;
        }
    }

    @Test
    public void testInheritance() throws Exception {

        // [#3614] [#3643] Check on inheritance of fields
        Field<Boolean> a = field("a", Boolean.class);
        Field<Boolean> b = field("b", Boolean.class);
        Field<Boolean> c = field("c", Boolean.class);
        Field<Boolean> d = field("d", Boolean.class);
        Record4<Boolean, Boolean, Boolean, Boolean> record = create.newRecord(a, b, c, d);
        record.setValue(a, true);
        record.setValue(b, true);
        record.setValue(c, true);
        record.setValue(d, true);

        Sub pojo = record.into(Sub.class);
        assertTrue(pojo.a);
        assertTrue(pojo.b);
        assertTrue(pojo.c);
        assertTrue((Boolean) accessible(Super.class.getDeclaredField("d")).get(pojo));
    }

    static class Sub extends Super {

    }

    static class Super {
        public boolean a;
        protected boolean b;
        boolean c;
        private boolean d;
    }

    @Test
    public void testIdAnnotation() throws Exception {
        Field<Long> field = field("xx", Long.class);
        Record1<Long> record = create.newRecord(field);
        record.setValue(field, 1L);

        IdWithAnnotations1 pojo1 = record.into(IdWithAnnotations1.class);
        assertEquals(1L, pojo1.xx);

        IdWithAnnotations2 pojo2 = record.into(IdWithAnnotations2.class);
        assertEquals(1L, pojo2.xx);
    }

    public static class IdWithAnnotations1 {
        @Id
        long xx;
    }

    public static class IdWithAnnotations2 {
        long xx;

        public void setXx(long xx) {
            this.xx = xx;
        }

        @Id
        public long getXx() {
            return xx;
        }
    }

    @Test
    public void testIntoValueTypes() throws Exception {
        Field<Boolean> field = field("B", Boolean.class);
        Result<Record1<Boolean>> result = create.newResult(field);
        result.add(create.newRecord(field));
        result.add(create.newRecord(field));
        result.add(create.newRecord(field));
        result.get(0).setValue(field, true);
        result.get(1).setValue(field, false);
        result.get(2).setValue(field, null);

        assertEquals(Arrays.asList(true, false, false), result.into(boolean.class));
        assertEquals(Arrays.asList(true, false, null), result.into(Boolean.class));
        assertEquals(Arrays.asList(1, 0, 0), result.into(int.class));
        assertEquals(Arrays.asList(1, 0, null), result.into(Integer.class));
        assertEquals(Arrays.asList("true", "false", null), result.into(String.class));
    }

    @Test
    public void testRecordFrom() throws Exception {
        Table1Record record = create.newRecord(TABLE1);
        assertFalse(record.changed());

        T1 t1 = new T1();
        t1.ID1 = 1;
        t1.NAME1 = "A";
        t1.DUMMY = "X";

        record.from(t1);
        assertTrue(record.changed());
        assertTrue(record.changed(FIELD_ID1));
        assertTrue(record.changed(FIELD_NAME1));
        assertFalse(record.changed(FIELD_DATE1));
        assertEquals(1, (int) record.getValue(FIELD_ID1));
        assertEquals("A", record.getValue(FIELD_NAME1));
        assertNull(record.getValue(FIELD_DATE1));
    }

    static class T1 {
        Integer ID1;
        String NAME1;
        String DUMMY;
    }

}
