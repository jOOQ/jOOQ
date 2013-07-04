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
