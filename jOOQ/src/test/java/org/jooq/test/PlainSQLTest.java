/**
 * Copyright (c) 2009-2014, Data Geekery GmbH (http://www.datageekery.com)
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

import static org.jooq.impl.DSL.condition;
import static org.jooq.impl.DSL.val;

import org.jooq.QueryPart;

import org.junit.Test;

/**
 * @author Lukas Eder
 */
public class PlainSQLTest extends AbstractTest {

    @Test
    public void testBindVariables() {
        QueryPart q = condition("a = ? and b = ?", val(1), 2);

        assertEquals("(a = ? and b = ?)", create.render(q));
        assertEquals("(a = 1 and b = 2)", create.renderInlined(q));
        assertEquals("(a = :1 and b = :2)", create.renderNamedParams(q));
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
}
