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
package org.jooq.test._.testcases;

import static java.util.Arrays.asList;
import static org.jooq.Clause.SELECT;
import static org.jooq.Clause.SELECT_WHERE;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectFrom;
import static org.jooq.impl.DSL.selectOne;
import static org.junit.Assert.assertEquals;

import java.sql.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jooq.Clause;
import org.jooq.Condition;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record6;
import org.jooq.Result;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.VisitContext;
import org.jooq.impl.DefaultVisitListener;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;

import org.junit.Test;

public class VisitListenerTests<
    A    extends UpdatableRecord<A> & Record6<Integer, String, String, Date, Integer, ?>,
    AP,
    B    extends UpdatableRecord<B>,
    S    extends UpdatableRecord<S> & Record1<String>,
    B2S  extends UpdatableRecord<B2S> & Record3<String, Integer, Integer>,
    BS   extends UpdatableRecord<BS>,
    L    extends TableRecord<L> & Record2<String, String>,
    X    extends TableRecord<X>,
    DATE extends UpdatableRecord<DATE>,
    BOOL extends UpdatableRecord<BOOL>,
    D    extends UpdatableRecord<D>,
    T    extends UpdatableRecord<T>,
    U    extends TableRecord<U>,
    UU   extends UpdatableRecord<UU>,
    I    extends TableRecord<I>,
    IPK  extends UpdatableRecord<IPK>,
    T725 extends UpdatableRecord<T725>,
    T639 extends UpdatableRecord<T639>,
    T785 extends TableRecord<T785>>
extends BaseTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, I, IPK, T725, T639, T785> {

    public VisitListenerTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, I, IPK, T725, T639, T785> delegate) {
        super(delegate);
    }

    @Test
    public void testVisitListener() throws Exception {

        // No join with author table
        Result<?> result1 =
        create(new OnlyAuthorIDEqual1())
            .select(TBook_ID())
            .from(TBook())
            .orderBy(TBook_ID())
            .fetch();

        assertEquals(2, result1.size());
        assertEquals(asList(1, 2), result1.getValues(TBook_ID()));

        // Additional predicates
        Result<?> result2 =
        create(new OnlyAuthorIDEqual1())
            .select(TBook_ID())
            .from(TBook())
            .where(TBook_ID().in(BOOK_IDS))
            .orderBy(TBook_ID())
            .fetch();

        assertEquals(2, result2.size());
        assertEquals(asList(1, 2), result2.getValues(TBook_ID()));

        // Join with author table
        Result<?> result3 =
        create(new OnlyAuthorIDEqual1())
            .select(TBook_ID())
            .from(TBook().join(TAuthor())
                         .on(TBook_AUTHOR_ID().eq(TAuthor_ID())))
            .orderBy(TBook_ID())
            .fetch();

        assertEquals(2, result3.size());
        assertEquals(asList(1, 2), result3.getValues(TBook_ID()));

        // Create a union of authors
        Result<?> result4 =
        create(new OnlyAuthorIDEqual1())
            .select(TAuthor_ID())
            .from(TAuthor())
            .where(TAuthor_ID().eq(1))
            .union(
             select(TAuthor_ID())
            .from(TAuthor())
            .where(TAuthor_ID().ne(1)))
            .union(
             select(TAuthor_ID())
            .from(TAuthor()))
            .fetch();

        assertEquals(1, result4.size());
        assertEquals(1, (int) result4.getValue(0, TAuthor_ID()));

        // Use nested selects
        Result<?> result5 =
        create(new OnlyAuthorIDEqual1())
            .select(inline(1).as("value"))
            .where(inline(2).in(select(TAuthor_ID()).from(TAuthor()).where(TAuthor_ID().eq(2))))
            .union(
             select(inline(2))
            .whereExists(selectOne().from(TAuthor()).where(TAuthor_ID().eq(2))))
            .union(
             select(inline(3))
            .from(selectFrom(TAuthor()).where(TAuthor_ID().eq(2))))
            .fetch();

        assertEquals(0, result5.size());
    }

    private enum Key {
        NESTING_LEVEL,
        SUBSELECT_VALUES
    }

    private enum Value {
        SUBSELECT_SELECTS_FROM_AUTHOR,
        SUBSELECT_SELECTS_FROM_BOOK,
        SUBSELECT_HAS_WHERE_CLAUSE_PREDICATES
    }

    /**
     * This sample visit listener restricts T_AUTHOR.ID = 1 and T_BOOK.AUTHOR_ID
     * = 1.
     */
    private class OnlyAuthorIDEqual1 extends DefaultVisitListener {

        private int nestingLevel(VisitContext context, int increase) {
            Integer level = (Integer) context.data(Key.NESTING_LEVEL);
            if (level == null) {
                level = 0;
            }

            if (increase == -1)
                subselectValueMap(context).remove(level);

            level += increase;

            if (increase != 0)
                context.data(Key.NESTING_LEVEL, level);
            if (increase == 1)
                subselectValueMap(context).put(level, EnumSet.noneOf(Value.class));

            return level;
        }

        @SuppressWarnings("unchecked")
        private Map<Integer, EnumSet<Value>> subselectValueMap(VisitContext context) {
            Map<Integer, EnumSet<Value>> data = (Map<Integer, EnumSet<Value>>) context.data(Key.SUBSELECT_VALUES);
            if (data == null) {
                data = new HashMap<Integer, EnumSet<Value>>();
                context.data(Key.SUBSELECT_VALUES, data);
            }
            return data;
        }

        private EnumSet<Value> subselectValues(VisitContext context) {
            return subselectValueMap(context).get(nestingLevel(context, 0));
        }

        private List<Clause> subselectClauses(VisitContext context) {
            List<Clause> result = asList(context.clauses());
            return result.subList(result.lastIndexOf(SELECT), result.size() - 1);
        }

        @Override
        public void clauseStart(VisitContext context) {
            if (context.renderContext() == null)
                return;

            if (context.clause() == SELECT) {
                nestingLevel(context, 1);
            }
        }

        @Override
        public void clauseEnd(VisitContext context) {
            if (context.renderContext() == null)
                return;

            if (context.clause() == SELECT_WHERE) {
                if (subselectValues(context).contains(Value.SUBSELECT_SELECTS_FROM_BOOK) ||
                    subselectValues(context).contains(Value.SUBSELECT_SELECTS_FROM_AUTHOR)) {
                    context.renderContext()
                           .sql(" ")
                           .keyword(subselectValues(context).contains(Value.SUBSELECT_HAS_WHERE_CLAUSE_PREDICATES) ? "and" : "where")
                           .sql(" ");

                    if (subselectValues(context).contains(Value.SUBSELECT_SELECTS_FROM_AUTHOR)) {
                        context.renderContext().visit(TAuthor_ID().eq(inline(1)));
                    }
                    else {
                        context.renderContext().visit(TBook_AUTHOR_ID().eq(inline(1)));
                    }
                }
            }

            if (context.clause() == SELECT) {
                nestingLevel(context, -1);
            }
        }

        @Override
        public void visitEnd(VisitContext context) {
            if (context.visiting() == TBook()) {
                if (subselectClauses(context).contains(Clause.SELECT_FROM)) {
                    subselectValues(context).add(Value.SUBSELECT_SELECTS_FROM_BOOK);
                }
            }

            if (context.visiting() == TAuthor()) {
                if (subselectClauses(context).contains(Clause.SELECT_FROM)) {
                    subselectValues(context).add(Value.SUBSELECT_SELECTS_FROM_AUTHOR);
                }
            }

            if (context.visiting() instanceof Condition) {
                if (subselectClauses(context).contains(Clause.SELECT_WHERE)) {
                    subselectValues(context).add(Value.SUBSELECT_HAS_WHERE_CLAUSE_PREDICATES);
                }
            }
        }
    }
}