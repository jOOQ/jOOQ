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

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static org.jooq.Clause.FIELD;
import static org.jooq.Clause.FIELD_REFERENCE;
import static org.jooq.Clause.FIELD_ROW;
import static org.jooq.Clause.FIELD_VALUE;
import static org.jooq.Clause.SELECT_WHERE;
import static org.jooq.Clause.TABLE;
import static org.jooq.Clause.TABLE_REFERENCE;
import static org.jooq.Clause.UPDATE;
import static org.jooq.Clause.UPDATE_SET;
import static org.jooq.Clause.UPDATE_SET_ASSIGNMENT;
import static org.jooq.Clause.UPDATE_UPDATE;
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.using;
import static org.jooq.impl.DSL.val;
import static org.jooq.impl.DefaultVisitListenerProvider.providers;
import static org.jooq.test.data.Table1.FIELD_DATE1;
import static org.jooq.test.data.Table1.FIELD_ID1;
import static org.jooq.test.data.Table1.FIELD_NAME1;
import static org.jooq.test.data.Table1.TABLE1;
import static org.jooq.tools.StringUtils.leftPad;

import java.util.ArrayList;
import java.util.List;

import org.jooq.Clause;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.VisitContext;
import org.jooq.VisitListener;
import org.jooq.impl.DSL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Some common tests related to {@link VisitContext}
 *
 * @author Lukas Eder
 */
@SuppressWarnings("unchecked")
public class VisitContextTest extends AbstractTest {

    private DSLContext ctx;
    private SimpleListener listener;

    @Before
    public void setup() {
        listener = new SimpleListener();
        ctx = DSL.using(
            create.configuration().derive(providers(listener)));
    }

    @After
    public void teardown() {
        ctx = null;
        listener = null;
    }


    @Test
    public void testClauses() {
        Configuration c = create.configuration().derive(providers(new ClausesListener()));

//        String sql =
//        using(c)
//            .select(FIELD_ID1, FIELD_NAME1)
//            .from(select(FIELD_NAME1).from(
//                TABLE1,
//                TABLE2.join(TABLE3).on("1 = 2")
//                      .leftOuterJoin(TABLE3).on("1 = 1")))
//            .where(FIELD_ID1.eq(1))
//            .and(FIELD_NAME1.ne("3"))
//            .and("x = y")
//            .having(FIELD_ID1.eq(1))
//            .getSQL();
        String sql =
        using(c)
            .select(FIELD_ID1, FIELD_NAME1)
            .from(select(FIELD_NAME1).from(TABLE1))
            .getSQL();

        System.out.println();
        System.out.println(sql);
    }

    private static class ClausesListener implements VisitListener {

        Clause clause;
        String where = "where";
        int indent = 0;

        @Override
        public void clauseStart(VisitContext context) {
            if (context.clause() == Clause.DUMMY)
                return;

            clause = context.clause();
            indent += 2;
            System.out.println(leftPad("+-", indent, "| ") + context.clause());
        }

        @Override
        public void clauseEnd(VisitContext context) {
            if (context.clause() == Clause.DUMMY)
                return;

            if (clause == SELECT_WHERE) {
                if (context.renderContext() != null) {
                    context.renderContext()
                           .sql(" ")
                           .keyword(where)
                           .sql(" ")
                           .sql("SecurityCode IN (1, 2, 3)");
                }
            }

            clause = null;
            indent -= 2;
        }

        @Override
        public void visitStart(VisitContext context) {
            if (clause == SELECT_WHERE) {
                where = "and";
            }
        }

        @Override
        public void visitEnd(VisitContext context) {
        }
    }

    @Test
    public void test_UPDATE_SET_simple() {
        ctx.update(TABLE1)
           .set(FIELD_NAME1, "value")
           .getSQL();

        assertEvents(asList(
            asList(UPDATE),
            asList(UPDATE, UPDATE_UPDATE),
            asList(UPDATE, UPDATE_UPDATE, TABLE),
            asList(UPDATE, UPDATE_UPDATE, TABLE, TABLE_REFERENCE),
            asList(UPDATE, UPDATE_SET),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD, FIELD_REFERENCE),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD, FIELD_VALUE)
        ));
    }

    @Test
    public void test_UPDATE_SET_twoValues() {
        ctx.update(TABLE1)
           .set(FIELD_NAME1, "value")
           .set(FIELD_DATE1, FIELD_DATE1)
           .getSQL();

        assertEvents(asList(
            asList(UPDATE),
            asList(UPDATE, UPDATE_UPDATE),
            asList(UPDATE, UPDATE_UPDATE, TABLE),
            asList(UPDATE, UPDATE_UPDATE, TABLE, TABLE_REFERENCE),
            asList(UPDATE, UPDATE_SET),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD, FIELD_REFERENCE),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD, FIELD_VALUE),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD, FIELD_REFERENCE),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD, FIELD_REFERENCE)
        ));
    }

    @Test
    public void test_UPDATE_SET_rowValueExpressions() {

        // Postgres is known to support this syntax particularly well
        ctx.configuration().set(POSTGRES);

        ctx.update(TABLE1)
           .set(row(FIELD_NAME1,  FIELD_DATE1),
                row(val("value"), FIELD_DATE1))
           .getSQL();

        assertEvents(asList(
            asList(UPDATE),
            asList(UPDATE, UPDATE_UPDATE),
            asList(UPDATE, UPDATE_UPDATE, TABLE),
            asList(UPDATE, UPDATE_UPDATE, TABLE, TABLE_REFERENCE),
            asList(UPDATE, UPDATE_SET),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD, FIELD_ROW),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD, FIELD_ROW, FIELD),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD, FIELD_ROW, FIELD, FIELD_REFERENCE),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD, FIELD_ROW, FIELD),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD, FIELD_ROW, FIELD, FIELD_REFERENCE),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD, FIELD_ROW),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD, FIELD_ROW, FIELD),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD, FIELD_ROW, FIELD, FIELD_VALUE),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD, FIELD_ROW, FIELD),
            asList(UPDATE, UPDATE_SET, UPDATE_SET_ASSIGNMENT, FIELD, FIELD_ROW, FIELD, FIELD_REFERENCE)
        ));
    }

    private void assertEvents(List<List<Clause>> expected) {
        // This assertion is a bit more verbose to be able to detect errors more easily
        for (int i = 0; i < expected.size() && i < listener.clauses.size(); i++) {
            assertEquals("Mismatch at position: " + i, expected.get(i), listener.clauses.get(i));
        }

        if (expected.size() != listener.clauses.size()) {
            fail("Size mismatch:\n\tExpected: " + expected + "\n\tActual: " + listener.clauses);
        }
    }

    private static class SimpleListener implements VisitListener {

        List<List<Clause>> clauses = new ArrayList<List<Clause>>();

        @Override
        public void clauseStart(VisitContext context) {
            clauses.add(asList(context.clauses()));
        }

        @Override
        public void clauseEnd(VisitContext context) {}

        @Override
        public void visitStart(VisitContext context) {}

        @Override
        public void visitEnd(VisitContext context) {}

    }
}
