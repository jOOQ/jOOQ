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

import static org.jooq.test.data.Table1.FIELD_ID1;
import static org.jooq.test.data.Table2.FIELD_ID2;
import static org.jooq.test.data.Table3.FIELD_ID3;

import org.jooq.Record3;
import org.jooq.Result;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;


/**
 * A test suite for sorting functionality of {@link Result}.
 *
 * @author Lukas Eder
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ResultSortTest extends AbstractTest {

    @Test
    public void testSortStability() throws Exception {
        Result<Record3<Integer, Integer, Integer>> original = result();
        Result<Record3<Integer, Integer, Integer>> result = result();

        assertEquals(original, result);

        assertEquals(original, result.sortAsc(FIELD_ID1));
        assertEquals(original, result.sortDesc(FIELD_ID1).sortAsc(FIELD_ID1));
        assertEquals(original, result.sortAsc(FIELD_ID3).sortAsc(FIELD_ID2).sortAsc(FIELD_ID1));
    }

    private Result<Record3<Integer, Integer, Integer>> result() {
        Result<Record3<Integer, Integer, Integer>> result = create.newResult(FIELD_ID1, FIELD_ID2, FIELD_ID3);

        result.add(record(1, 1, 1));
        result.add(record(1, 1, 2));
        result.add(record(1, 1, 3));
        result.add(record(1, 2, 1));
        result.add(record(1, 2, 2));
        result.add(record(1, 2, 3));
        result.add(record(2, 1, 1));

        return result;
    }

    private Record3<Integer, Integer, Integer> record(int i, int j, int k) {
        Record3<Integer, Integer, Integer> record = create.newRecord(FIELD_ID1, FIELD_ID2, FIELD_ID3);
        record.fromArray(i, j, k);
        return record;
    }
}
