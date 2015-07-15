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

import static org.jooq.impl.DSL.condition;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.list;
import static org.jooq.impl.DSL.sql;
import static org.jooq.impl.DSL.val;
import static org.jooq.test.data.Table1.TABLE1;

import java.sql.SQLException;

import org.jooq.Condition;
import org.jooq.QueryPart;

import org.jmock.Expectations;
import org.junit.Test;

/**
 * @author Lukas Eder
 */
public class PlainSQLTest extends AbstractTest {

    @Test
    public void testBindVariables() {
        Condition q = condition("a = ? and b = ?", val(1), 2);

        assertEquals("(a = ? and b = ?)", create.render(q));
        assertEquals("(a = 1 and b = 2)", create.renderInlined(q));
        assertEquals("(a = :1 and b = :2)", create.renderNamedParams(q));

        assertEquals("((a = ? and b = ?) and (a = ? and b = ?))", create.render(q.and(q)));
        assertEquals("((a = 1 and b = 2) and (a = 1 and b = 2))", create.renderInlined(q.and(q)));
        assertEquals("((a = :1 and b = :2) and (a = :3 and b = :4))", create.renderNamedParams(q.and(q)));
    }

    @Test
    public void testStringLiterals() {
        Condition q = condition("a = '?' and b = '{0}' and c = ?", 1);

        assertEquals("(a = '?' and b = '{0}' and c = ?)", create.render(q));
        assertEquals("(a = '?' and b = '{0}' and c = 1)", create.renderInlined(q));
        assertEquals("(a = '?' and b = '{0}' and c = :1)", create.renderNamedParams(q));
    }

    @Test
    public void testQuotedIdentifiers() {
        Condition c1 = condition("a = `?` and b = `{0}` and c = ?", 1);
        Condition c2 = condition("a = `?` and b = `{0}` and c = ?", 1);

        assertEquals("(a = `?` and b = `{0}` and c = ?)", create.render(c1));
        assertEquals("(a = `?` and b = `{0}` and c = 1)", create.renderInlined(c1));
        assertEquals("(a = `?` and b = `{0}` and c = :1)", create.renderNamedParams(c1));

        assertEquals("(a = `?` and b = `{0}` and c = ?)", create.render(c2));
        assertEquals("(a = `?` and b = `{0}` and c = 1)", create.renderInlined(c2));
        assertEquals("(a = `?` and b = `{0}` and c = :1)", create.renderNamedParams(c2));

//        assertEquals("(a = `?` and b = `{0}` and c = ?)", DSL.using(POSTGRES).render(c2));
//        assertEquals("(a = `?` and b = `{0}` and c = 1)", DSL.using(POSTGRES).renderInlined(c2));
//        assertEquals("(a = `?` and b = `{0}` and c = :1)", DSL.using(POSTGRES).renderNamedParams(c2));
    }

    @Test
    public void testIndexedParameters() {
        QueryPart q = condition("a = ? and b = ? and ? = ?", 1, 2, "a", "b");

        assertEquals("(a = ? and b = ? and ? = ?)", create.render(q));
        assertEquals("(a = 1 and b = 2 and 'a' = 'b')", create.renderInlined(q));
    }

    @Test
    public void testNamedParameters() {

        // [#2906] TODO: It's not clear how we want to deal with all sorts of named bind variables. There
        // is substantial risk of breaking actual SQL syntax, e.g. PostgreSQL's :: operator

//        QueryPart q = condition("a = :1 and b = :2 and :3 = :4", 1, 2, "a", "b");
//
//        assertEquals("(a = ? and b = ? and ? = ?)", create.render(q));
//        assertEquals("(a = :1 and b = :2 and :3 = :4)", create.renderNamedParams(q));
//        assertEquals("(a = 1 and b = 2 and 'a' = 'b')", create.renderInlined(q));
    }

    @Test
    public void testList() throws SQLException {
        QueryPart list = list(val(1), inline(2), sql("({0})", list(val("a"), TABLE1)));

        assertEquals("?, 2, (?, `TABLE1`)", create.render(list));
        assertEquals("1, 2, ('a', `TABLE1`)", create.renderInlined(list));

        context.checking(new Expectations() {{
            oneOf(statement).setInt(1, 1);
            oneOf(statement).setString(2, "a");
        }});

        int i = b_ref().visit(list).peekIndex();
        assertEquals(3, i);

        context.assertIsSatisfied();
    }

    @Test
    public void testEscapedCurlyBraces() {
        QueryPart q = sql("{abc}, {{xyz}}");

        assertEquals("abc, {xyz}", create.render(q));
        assertEquals("abc, {xyz}", create.renderInlined(q));
    }
}
