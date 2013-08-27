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

import static org.jooq.SQLDialect.ORACLE;
import static org.jooq.conf.ParamType.INDEXED;
import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.conf.ParamType.NAMED;
import static org.jooq.conf.RenderKeywordStyle.LOWER;
import static org.jooq.conf.RenderKeywordStyle.UPPER;
import static org.jooq.conf.StatementType.STATIC_STATEMENT;
import static org.jooq.impl.DSL.val;
import static org.junit.Assert.assertEquals;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Keyword;
import org.jooq.Query;
import org.jooq.SQLDialect;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;

import org.junit.Test;

/**
 * Some common tests related to rendering.
 *
 * @author Lukas Eder
 */
public class RenderTest extends AbstractTest {

    @SuppressWarnings("deprecation")
    private void testGetSQL0(Query q, String defaultSQL) {
        assertEquals(defaultSQL, q.getSQL());
        assertEquals("select ?, ? from dual", q.getSQL(false));
        assertEquals("select 1, 'A' from dual", q.getSQL(true));
        assertEquals("select ?, ? from dual", q.getSQL(INDEXED));
        assertEquals("select :1, :2 from dual", q.getSQL(NAMED));
        assertEquals("select 1, 'A' from dual", q.getSQL(INLINED));
    }

    @Test
    public void testGetSQL() {
        Query q = create.select(val(1), val("A"));
        testGetSQL0(q, "select ?, ? from dual");
    }

    @Test
    public void testGetSQLWithParamTypeINDEXED() {
        Query q =
        DSL.using(SQLDialect.ORACLE, new Settings().withParamType(INDEXED))
           .select(val(1), val("A"));

        testGetSQL0(q, "select ?, ? from dual");
    }

    @Test
    public void testGetSQLWithParamTypeINDEXEDandStatementTypeSTATIC() {
        Query q =
        DSL.using(SQLDialect.ORACLE, new Settings().withParamType(INDEXED)
                                                   .withStatementType(STATIC_STATEMENT))
           .select(val(1), val("A"));

        testGetSQL0(q, "select 1, 'A' from dual");
    }

    @Test
    public void testGetSQLWithParamTypeNAMED() {
        Query q =
        DSL.using(SQLDialect.ORACLE, new Settings().withParamType(NAMED))
           .select(val(1), val("A"));

        testGetSQL0(q, "select :1, :2 from dual");
    }

    @Test
    public void testGetSQLWithParamTypeNAMEDandStatementTypeSTATIC() {
        Query q =
        DSL.using(SQLDialect.ORACLE, new Settings().withParamType(NAMED)
                                                   .withStatementType(STATIC_STATEMENT))
           .select(val(1), val("A"));

        testGetSQL0(q, "select 1, 'A' from dual");
    }

    @Test
    public void testGetSQLWithParamTypeINLINED() {
        Query q =
        DSL.using(SQLDialect.ORACLE, new Settings().withParamType(INLINED))
           .select(val(1), val("A"));

        testGetSQL0(q, "select 1, 'A' from dual");
    }

    @Test
    public void testGetSQLWithParamTypeINLINEDandStatementTypeSTATIC() {
        Query q =
        DSL.using(SQLDialect.ORACLE, new Settings().withParamType(INLINED)
                                                   .withStatementType(STATIC_STATEMENT))
           .select(val(1), val("A"));

        testGetSQL0(q, "select 1, 'A' from dual");
    }

    @Test
    public void testKeywords() {
        Keyword keyword = DSL.keyword("Abc");
        Field<?> f = DSL.field("{0} Untouched {Xx} Untouched {1}", keyword, keyword);

        DSLContext def = DSL.using(ORACLE);
        DSLContext lower = DSL.using(ORACLE, new Settings().withRenderKeywordStyle(LOWER));
        DSLContext upper = DSL.using(ORACLE, new Settings().withRenderKeywordStyle(UPPER));

        assertEquals("abc Untouched xx Untouched abc", def.render(f));
        assertEquals("abc Untouched xx Untouched abc", lower.render(f));
        assertEquals("ABC Untouched XX Untouched ABC", upper.render(f));
    }
}
