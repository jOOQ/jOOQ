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

import static org.jooq.SQLDialect.MYSQL;
import static org.jooq.conf.ParamType.INDEXED;
import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.conf.ParamType.NAMED;
import static org.jooq.conf.ParamType.NAMED_OR_INLINED;
import static org.jooq.conf.RenderKeywordStyle.AS_IS;
import static org.jooq.conf.RenderKeywordStyle.LOWER;
import static org.jooq.conf.RenderKeywordStyle.UPPER;
import static org.jooq.conf.StatementType.STATIC_STATEMENT;
import static org.jooq.impl.DSL.param;
import static org.jooq.impl.DSL.val;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Keyword;
import org.jooq.Query;
import org.jooq.SQLDialect;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * Some common tests related to rendering.
 *
 * @author Lukas Eder
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RenderTest extends AbstractTest {

    @SuppressWarnings("deprecation")
    private void testGetSQL0(Query q, String defaultSQL) {
        assertEquals(defaultSQL, q.getSQL());
        assertEquals("select ?, ? from dual", q.getSQL(false));
        assertEquals("select 1, 'A' from dual", q.getSQL(true));
        assertEquals("select ?, ? from dual", q.getSQL(INDEXED));
        assertEquals("select :var, :2 from dual", q.getSQL(NAMED));
        assertEquals("select :var, 'A' from dual", q.getSQL(NAMED_OR_INLINED));
        assertEquals("select 1, 'A' from dual", q.getSQL(INLINED));
    }

    @Test
    public void testGetSQL() {
        Query q = create.select(param("var", 1), val("A"));
        testGetSQL0(q, "select ?, ? from dual");
    }

    @Test
    public void testGetSQLWithParamTypeINDEXED() {
        Query q =
        DSL.using(SQLDialect.MYSQL, new Settings().withParamType(INDEXED))
           .select(param("var", 1), val("A"));

        testGetSQL0(q, "select ?, ? from dual");
    }

    @Test
    public void testGetSQLWithParamTypeINDEXEDandStatementTypeSTATIC() {
        Query q =
        DSL.using(SQLDialect.MYSQL, new Settings().withParamType(INDEXED)
                                                   .withStatementType(STATIC_STATEMENT))
           .select(param("var", 1), val("A"));

        testGetSQL0(q, "select 1, 'A' from dual");
    }

    @Test
    public void testGetSQLWithParamTypeNAMED() {
        Query q =
        DSL.using(SQLDialect.MYSQL, new Settings().withParamType(NAMED))
           .select(param("var", 1), val("A"));

        testGetSQL0(q, "select :var, :2 from dual");
    }

    @Test
    public void testGetSQLWithParamTypeNAMEDandStatementTypeSTATIC() {
        Query q =
        DSL.using(SQLDialect.MYSQL, new Settings().withParamType(NAMED)
                                                  .withStatementType(STATIC_STATEMENT))
           .select(param("var", 1), val("A"));

        testGetSQL0(q, "select 1, 'A' from dual");
    }

    @Test
    public void testGetSQLWithParamTypeNAMED_OR_INLINED() {
        Query q =
        DSL.using(SQLDialect.MYSQL, new Settings().withParamType(NAMED_OR_INLINED))
           .select(param("var", 1), val("A"));

        testGetSQL0(q, "select :var, 'A' from dual");
    }

    @Test
    public void testGetSQLWithParamTypeNAMED_OR_INLINEDandStatementTypeSTATIC() {
        Query q =
        DSL.using(SQLDialect.MYSQL, new Settings().withParamType(NAMED_OR_INLINED)
                                                  .withStatementType(STATIC_STATEMENT))
           .select(param("var", 1), val("A"));

        testGetSQL0(q, "select 1, 'A' from dual");
    }

    @Test
    public void testGetSQLWithParamTypeINLINED() {
        Query q =
        DSL.using(SQLDialect.MYSQL, new Settings().withParamType(INLINED))
           .select(param("var", 1), val("A"));

        testGetSQL0(q, "select 1, 'A' from dual");
    }

    @Test
    public void testGetSQLWithParamTypeINLINEDandStatementTypeSTATIC() {
        Query q =
        DSL.using(SQLDialect.MYSQL, new Settings().withParamType(INLINED)
                                                  .withStatementType(STATIC_STATEMENT))
           .select(param("var", 1), val("A"));

        testGetSQL0(q, "select 1, 'A' from dual");
    }

    @Test
    public void testKeywords() {
        Keyword keyword = DSL.keyword("Abc");
        Field<?> f = DSL.field("{0} Untouched {Xx} Untouched {1}", keyword, keyword);

        DSLContext def = DSL.using(MYSQL);
        DSLContext as_is = DSL.using(MYSQL, new Settings().withRenderKeywordStyle(AS_IS));
        DSLContext lower = DSL.using(MYSQL, new Settings().withRenderKeywordStyle(LOWER));
        DSLContext upper = DSL.using(MYSQL, new Settings().withRenderKeywordStyle(UPPER));

        assertEquals("Abc Untouched Xx Untouched Abc", def.render(f));
        assertEquals("Abc Untouched Xx Untouched Abc", as_is.render(f));
        assertEquals("abc Untouched xx Untouched abc", lower.render(f));
        assertEquals("ABC Untouched XX Untouched ABC", upper.render(f));
    }
}
