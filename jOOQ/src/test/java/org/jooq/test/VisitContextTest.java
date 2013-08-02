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

import static org.jooq.Clause.SELECT_WHERE;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.using;
import static org.jooq.impl.DefaultVisitListenerProvider.providers;
import static org.jooq.test.data.Table1.FIELD_ID1;
import static org.jooq.test.data.Table1.FIELD_NAME1;
import static org.jooq.test.data.Table1.TABLE1;
import static org.jooq.tools.StringUtils.leftPad;

import org.jooq.Clause;
import org.jooq.Configuration;
import org.jooq.VisitContext;
import org.jooq.VisitListener;

import org.junit.Test;

/**
 * Some common tests related to {@link VisitContext}
 *
 * @author Lukas Eder
 */
public class VisitContextTest extends AbstractTest {

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
}
