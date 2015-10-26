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

import org.jooq.Context;
import org.jooq.QueryPart;
import org.jooq.impl.CustomQueryPart;
import org.jooq.impl.DSL;

import org.jmock.Expectations;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * Test cases for custom {@link QueryPart}s.
 *
 * @author Lukas Eder
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CustomQueryPartTest extends AbstractTest {

    @SuppressWarnings("serial")
    @Test
    public void testCustomQueryPart() throws Exception {
        QueryPart p = new CustomQueryPart() {

            @Override
            public void accept(Context<?> ctx) {
                ctx.visit(val("abc"));
            }
        };

        assertEquals("(x = ?)", r_ref().render(DSL.condition("x = {0}", p)));
        assertEquals("(x = :1)", r_refP().render(DSL.condition("x = {0}", p)));
        assertEquals("(x = 'abc')", r_refI().render(DSL.condition("x = {0}", p)));

        context.checking(new Expectations() {{
            oneOf(statement).setString(1, "abc");
        }});

        int i = b_ref().visit(DSL.condition("x = {0}", p)).peekIndex();
        assertEquals(2, i);

        context.assertIsSatisfied();
    }
}
