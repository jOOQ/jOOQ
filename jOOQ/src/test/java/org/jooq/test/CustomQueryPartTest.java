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

import static org.jooq.impl.DSL.val;

import org.jooq.Context;
import org.jooq.QueryPart;
import org.jooq.impl.CustomQueryPart;
import org.jooq.impl.DSL;

import org.jmock.Expectations;
import org.junit.Test;

/**
 * Test cases for custom {@link QueryPart}s.
 *
 * @author Lukas Eder
 */
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
