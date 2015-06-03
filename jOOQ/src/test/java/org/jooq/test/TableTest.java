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

import static org.jooq.test.data.Table1.FIELD_DATE1;
import static org.jooq.test.data.Table1.FIELD_ID1;
import static org.jooq.test.data.Table1.FIELD_NAME1;
import static org.jooq.test.data.Table1.TABLE1;
import static org.jooq.test.data.Table2.TABLE2;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;


/**
 * A test suite for jOOQ functionality related to tables
 *
 * @author Lukas Eder
 */
public class TableTest extends AbstractTest {

    @Test
    public void testTableFieldAccess() throws Exception {
        assertNotNull(TABLE1.field(FIELD_ID1));
        assertNull(TABLE2.field(FIELD_ID1));

        assertEquals(FIELD_ID1, TABLE1.field(0));
        assertEquals(FIELD_NAME1, TABLE1.field(1));
        assertEquals(FIELD_DATE1, TABLE1.field(2));
        assertNull(TABLE1.field(3));
    }
}
