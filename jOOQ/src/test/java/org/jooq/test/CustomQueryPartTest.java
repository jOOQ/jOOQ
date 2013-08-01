/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.jooq.test;

import static junit.framework.Assert.assertEquals;
import static org.jooq.impl.DSL.val;

import org.jooq.BindContext;
import org.jooq.QueryPart;
import org.jooq.RenderContext;
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
            public void toSQL(RenderContext ctx) {
                ctx.visit(val("abc"));
            }

            @Override
            public void bind(BindContext ctx) {
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
