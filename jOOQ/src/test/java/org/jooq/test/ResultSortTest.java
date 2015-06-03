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

import static org.jooq.test.data.Table1.FIELD_ID1;
import static org.jooq.test.data.Table2.FIELD_ID2;
import static org.jooq.test.data.Table3.FIELD_ID3;

import org.jooq.Record3;
import org.jooq.Result;

import org.junit.Test;


/**
 * A test suite for sorting functionality of {@link Result}.
 *
 * @author Lukas Eder
 */
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
