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

import java.util.LinkedHashMap;
import java.util.Map;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;

import org.junit.Test;

/**
 * Unit tests for data type conversion
 *
 * @author Lukas Eder
 */
public class ConditionTest extends AbstractTest {

    @Test
    public void testMapCondition() {
        Map<Field<?>, Object> m1 = new LinkedHashMap<>();
        Map<Field<?>, Object> m2 = new LinkedHashMap<>(m1);
        m2.put(FIELD_ID1, 1);

        Map<Field<?>, Object> m3 = new LinkedHashMap<>(m2);
        m3.put(FIELD_ID2, "2");

        Map<Field<?>, Object> m4 = new LinkedHashMap<>(m3);
        m4.put(FIELD_ID3, FIELD_ID1);
        System.out.println(m4);

        Condition c1 = DSL.condition(m1);
        Condition c2 = DSL.condition(m2);
        Condition c3 = DSL.condition(m3);
        Condition c4 = DSL.condition(m4);

        assertEquals("1 = 1", create.render(c1));
        assertEquals("1 = 1", create.renderInlined(c1));

        assertEquals("`TABLE1`.`ID1` = ?", create.render(c2));
        assertEquals("`TABLE1`.`ID1` = 1", create.renderInlined(c2));
        assertEquals("(`TABLE1`.`ID1` = ? and `TABLE2`.`ID2` = ?)", create.render(c3));
        assertEquals("(`TABLE1`.`ID1` = 1 and `TABLE2`.`ID2` = 2)", create.renderInlined(c3));
        assertEquals("(`TABLE1`.`ID1` = ? and `TABLE2`.`ID2` = ? and `TABLE3`.`ID3` = `TABLE1`.`ID1`)", create.render(c4));
        assertEquals("(`TABLE1`.`ID1` = 1 and `TABLE2`.`ID2` = 2 and `TABLE3`.`ID3` = `TABLE1`.`ID1`)", create.renderInlined(c4));
    }
}
