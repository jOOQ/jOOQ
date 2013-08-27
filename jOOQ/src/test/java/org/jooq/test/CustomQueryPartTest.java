/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 * 
 * This work is dual-licensed Open Source, under LGPL and jOOQ EULA
 * =============================================================================
 * You may freely choose which license applies to you. For more information 
 * about licensing, please visit http://www.jooq.org/licenses
 * 
 * LGPL:  
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 * 
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 * 
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details.
 * 
 * You should have received a copy of the jOOQ End User License Agreement
 * along with this library.
 * If not, see http://www.jooq.org/eula
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
