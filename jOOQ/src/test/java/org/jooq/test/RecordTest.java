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

package org.jooq.test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

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
}
