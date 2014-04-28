/**
 * Copyright (c) 2009-2014, Data Geekery GmbH (http://www.datageekery.com)
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
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import javax.persistence.Column;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Result;

import org.junit.Test;


/**
 * A test suite for jOOQ functionality related to records
 *
 * @author Lukas Eder
 */
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

}
