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

import static org.jooq.SQLDialect.DEFAULT;
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.POSTGRES_9_3;
import static org.jooq.SQLDialect.POSTGRES_9_4;
import static org.jooq.SQLDialect.POSTGRES_9_5;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jooq.SQLDialect;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;


/**
 * Tests related to {@link SQLDialect}
 *
 * @author Lukas Eder
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
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
