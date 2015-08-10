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

import static org.jooq.SQLDialect.DEFAULT;
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.POSTGRES_9_3;
import static org.jooq.SQLDialect.POSTGRES_9_4;
import static org.jooq.SQLDialect.POSTGRES_9_5;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jooq.SQLDialect;

import org.junit.Test;


/**
 * Tests related to {@link SQLDialect}
 *
 * @author Lukas Eder
 */
public class DialectTest extends AbstractTest {

    @Test
    public void testPredecessor() throws Exception {
        assertEquals(DEFAULT, DEFAULT.predecessor());
        assertEquals(H2, H2.predecessor());

        assertEquals(POSTGRES_9_3, POSTGRES_9_3.predecessor());
        assertEquals(POSTGRES_9_3, POSTGRES_9_4.predecessor());
        assertEquals(POSTGRES_9_4, POSTGRES_9_5.predecessor());
        assertEquals(POSTGRES_9_5, POSTGRES.predecessor());
    }

    @Test
    public void testPrecedes() throws Exception {
        assertTrue(DEFAULT.precedes(DEFAULT));
        assertFalse(DEFAULT.precedes(H2));

        assertTrue(POSTGRES_9_3.precedes(POSTGRES_9_3));
        assertTrue(POSTGRES_9_3.precedes(POSTGRES_9_4));
        assertTrue(POSTGRES_9_3.precedes(POSTGRES_9_5));
        assertTrue(POSTGRES_9_3.precedes(POSTGRES));

        assertFalse(POSTGRES_9_4.precedes(POSTGRES_9_3));
        assertTrue(POSTGRES_9_4.precedes(POSTGRES_9_4));
        assertTrue(POSTGRES_9_4.precedes(POSTGRES_9_5));
        assertTrue(POSTGRES_9_4.precedes(POSTGRES));

        assertFalse(POSTGRES.precedes(POSTGRES_9_3));
        assertFalse(POSTGRES.precedes(POSTGRES_9_4));
        assertFalse(POSTGRES.precedes(POSTGRES_9_5));
        assertTrue(POSTGRES.precedes(POSTGRES));
    }
}
