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
