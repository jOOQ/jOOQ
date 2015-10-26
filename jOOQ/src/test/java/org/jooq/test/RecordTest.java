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

import static org.jooq.impl.DSL.row;
import static org.jooq.test.data.Table1.FIELD_DATE1;
import static org.jooq.test.data.Table1.FIELD_ID1;
import static org.jooq.test.data.Table1.FIELD_NAME1;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Date;

import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Row1;
import org.jooq.Row2;
import org.jooq.Row3;
import org.jooq.test.data.Table1;
import org.jooq.test.data.Table1Record;
import org.jooq.test.data.Table2;
import org.jooq.test.data.Table2Record;
import org.jooq.test.data.Table4;
import org.jooq.test.data.Table4Record;

import org.junit.Test;


/**
 * A test suite for jOOQ functionality related to records
 *
 * @author Lukas Eder
 */
public class RecordTest extends AbstractTest {

    @Test
    public void testComparableRecords() throws Exception {
        Table1Record r1 = create.newRecord(Table1.TABLE1);
        Table2Record r2 = create.newRecord(Table2.TABLE2);
        Table4Record r4a = create.newRecord(Table4.TABLE4);
        Table4Record r4b = create.newRecord(Table4.TABLE4);

        // Incomparable (incompatible) records
        try {
            r1.compareTo(r4a);
            fail();
        }
        catch (ClassCastException expected) {}

        // Comparable records
        assertEquals(0, r1.compareTo(r2));
        assertEquals(0, r4a.compareTo(r4b));

        r2.setValue(Table2.FIELD_ID2, 1);
        assertEquals(1, r1.compareTo(r2));

        r2.setValue(Table2.FIELD_NAME2, "1");
        assertEquals(1, r1.compareTo(r2));

        r1.setValue(Table1.FIELD_ID1, 1);
        assertEquals(1, r1.compareTo(r2));

        r1.setValue(Table1.FIELD_NAME1, "1");
        assertEquals(0, r1.compareTo(r2));

        r2.setValue(Table2.FIELD_ID2, null);
        assertEquals(-1, r1.compareTo(r2));

        // Arrays
        r4b.setValue(Table4.FIELD_ARRAY4, new Object[] { 1 });
        assertEquals(1, r4a.compareTo(r4b));

        r4a.setValue(Table4.FIELD_ARRAY4, new Object[] {});
        assertEquals(-1, r4a.compareTo(r4b));

        r4a.setValue(Table4.FIELD_ARRAY4, new Object[] { 1 });
        assertEquals(0, r4a.compareTo(r4b));

        r4a.setValue(Table4.FIELD_ARRAY4, new Object[] { 1, 0 });
        assertEquals(1, r4a.compareTo(r4b));

        r4b.setValue(Table4.FIELD_ARRAY4, new Object[] { 1, 0 });
        assertEquals(0, r4a.compareTo(r4b));

        r4b.setValue(Table4.FIELD_ARRAY4, new Object[] { 1, 1 });
        assertEquals(-1, r4a.compareTo(r4b));
    }

    @Test
    public void testRecordInto() {
        Table1Record r1 = create.newRecord(Table1.TABLE1);
        r1.setValue(Table1.FIELD_ID1, 1);
        r1.setValue(Table1.FIELD_NAME1, "x");

        Record1<Integer> r2 = r1.into(Table1.FIELD_ID1);
        assertEquals(1, r2.size());
        assertEquals(Table1.FIELD_ID1, r2.field(0));
        assertEquals(1, (int) r2.value1());
        assertTrue(r2.changed());

        r1.changed(false);
        r2 = r1.into(Table1.FIELD_ID1);
        assertEquals(1, r2.size());
        assertEquals(Table1.FIELD_ID1, r2.field(0));
        assertEquals(1, (int) r2.value1());
        assertFalse(r2.changed());
    }

    @SuppressWarnings("cast")
    @Test
    public void testRecordTypes() {
        Record1<Integer> r1 = create.newRecord(FIELD_ID1);
        Record2<Integer, String> r2 = create.newRecord(FIELD_ID1, FIELD_NAME1);
        Record3<Integer, String, Date> r3 = create.newRecord(FIELD_ID1, FIELD_NAME1, FIELD_DATE1);

        assertTrue(r1 instanceof Record1);
        assertFalse(r1 instanceof Record2);
        assertFalse(r1 instanceof Record3);

        assertFalse(r2 instanceof Record1);
        assertTrue(r2 instanceof Record2);
        assertFalse(r2 instanceof Record3);

        assertFalse(r3 instanceof Record1);
        assertFalse(r3 instanceof Record2);
        assertTrue(r3 instanceof Record3);
    }

    @SuppressWarnings("cast")
    @Test
    public void testRowTypes() {
        Row1<Integer> r1 = row(FIELD_ID1);
        Row2<Integer, String> r2 = row(FIELD_ID1, FIELD_NAME1);
        Row3<Integer, String, Date> r3 = row(FIELD_ID1, FIELD_NAME1, FIELD_DATE1);

        assertTrue(r1 instanceof Row1);
        assertFalse(r1 instanceof Row2);
        assertFalse(r1 instanceof Row3);

        assertFalse(r2 instanceof Row1);
        assertTrue(r2 instanceof Row2);
        assertFalse(r2 instanceof Row3);

        assertFalse(r3 instanceof Row1);
        assertFalse(r3 instanceof Row2);
        assertTrue(r3 instanceof Row3);
    }
}
