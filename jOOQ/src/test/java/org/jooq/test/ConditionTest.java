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
