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

import static org.jooq.impl.DSL.val;

import java.util.Arrays;
import java.util.List;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Select;
import org.jooq.impl.DSL;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * Test cases in this class check if the jOOQ API implementation defends itself against "rogue API usage".
 * <p>
 * By "rogue API usage", we mean that for some reason (e.g. API flaws, such as [#3347], or raw types, etc.), a "wrong" overloaded method is invoked
 * leading to misbehaviour that is not immediately visible from the call-site's API usage.
 *
 * @author Lukas Eder
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RogueAPIUsageTest extends AbstractTest {

    @Test
    public void testInPredicateWithCollection() {
        Field<Object> a = DSL.field("a");
        List<String> values = Arrays.asList("a", "b");

        Condition c = a.in(new Object[] { values });

        assertEquals("a in (?, ?)", create.render(c));
        assertEquals("a in ('a', 'b')", create.renderInlined(c));
        assertEquals("a in (:1, :2)", create.renderNamedParams(c));
    }

    @Test
    public void testInPredicateWithSelect() {
        Field<Object> a = DSL.field("a");
        Select<?> values = DSL.select(val(1));

        Condition c = a.in(values);

        assertEquals("a in (select ? from dual)", create.render(c));
        assertEquals("a in (select 1 from dual)", create.renderInlined(c));
        assertEquals("a in (select :1 from dual)", create.renderNamedParams(c));
    }
}
