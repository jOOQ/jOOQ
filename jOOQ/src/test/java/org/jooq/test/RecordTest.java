/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is triple-licensed under ASL 2.0, AGPL 3.0, and jOOQ EULA
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   ASL 2.0 or jOOQ EULA.
 * - If you're using this work with at least one commercial database, you may
 *   choose AGPL 3.0 or jOOQ EULA.
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
 * AGPL 3.0
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 *
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details: http://www.jooq.org/eula
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
