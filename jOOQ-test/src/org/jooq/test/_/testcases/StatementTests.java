/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.jooq.SQLDialect.H2;
import static org.jooq.impl.Factory.inline;
import static org.jooq.impl.Factory.val;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Cursor;
import org.jooq.ExecuteContext;
import org.jooq.Record;
import org.jooq.ResultQuery;
import org.jooq.Select;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.conf.Settings;
import org.jooq.conf.StatementType;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DefaultExecuteListener;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;
import org.jooq.tools.reflect.Reflect;

import org.junit.Test;

public class StatementTests<
    A    extends UpdatableRecord<A>,
    AP,
    B    extends UpdatableRecord<B>,
    S    extends UpdatableRecord<S>,
    B2S  extends UpdatableRecord<B2S>,
    BS   extends UpdatableRecord<BS>,
    L    extends TableRecord<L>,
    X    extends TableRecord<X>,
    DATE extends UpdatableRecord<DATE>,
    BOOL extends UpdatableRecord<BOOL>,
    D    extends UpdatableRecord<D>,
    T    extends UpdatableRecord<T>,
    U    extends TableRecord<U>,
    I    extends TableRecord<I>,
    IPK  extends UpdatableRecord<IPK>,
    T658 extends TableRecord<T658>,
    T725 extends UpdatableRecord<T725>,
    T639 extends UpdatableRecord<T639>,
    T785 extends TableRecord<T785>>
extends BaseTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, I, IPK, T658, T725, T639, T785> {

    public StatementTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, I, IPK, T658, T725, T639, T785> delegate) {
        super(delegate);
    }

    @Test
    public void testKeepStatement() throws Exception {
        Settings settings = new Settings().withExecuteListeners(KeepStatementListener.class.getName());

        // [#385] By default, new statements are created for every execution
        KeepStatementListener.reset();
        ResultQuery<Record> query = create(settings).select(val(1), inline(2));
        assertEquals(1, query.fetchOne(0));
        assertEquals(2, query.bind(1, 2).fetchOne(0));

        assertEquals(2, KeepStatementListener.statements.size());
        assertTrue(KeepStatementListener.closed > 0);
        assertFalse(
            KeepStatementListener.statements.get(0) ==
            KeepStatementListener.statements.get(1));

        // [#385] Queries should keep open statements, if explicitly requested
        KeepStatementListener.reset();
        query.keepStatement(true);
        assertEquals(2, query.fetchOne(0));
        assertEquals(3, query.bind(1, 3).fetchOne(0));

        Cursor<Record> cursor = query.fetchLazy();
        assertEquals(3, cursor.fetchOne().getValue(0));
        assertEquals(3, query.fetchOne(0));

        // [#1886] The first underlying statement should've been closed when
        // using StatementType.STATIC_STATEMENT
        assertEquals(4, KeepStatementListener.statements.size());
        assertEquals(0, KeepStatementListener.closed);
        assertEquals(
            create().getSettings().getStatementType() == StatementType.PREPARED_STATEMENT,
            KeepStatementListener.statements.get(0) ==
            KeepStatementListener.statements.get(1));

        // Statements #2, #3, #4 should be identical
        assertTrue(
            KeepStatementListener.statements.get(1) ==
            KeepStatementListener.statements.get(2));
        assertTrue(
            KeepStatementListener.statements.get(2) ==
            KeepStatementListener.statements.get(3));

        // [#1886] Check if inline bind values are correctly changed
        KeepStatementListener.reset();
        assertEquals(3, query.fetchOne(0));
        assertEquals(3, query.bind(2, 4).fetchOne(0));
        assertEquals(4, query.bind(2, 4).fetchOne(1));

        // All statements should be closed, as the inline bind value was changed
        assertEquals(3, KeepStatementListener.statements.size());
        assertEquals(0, KeepStatementListener.closed);
        assertTrue(
            KeepStatementListener.statements.get(0) !=
            KeepStatementListener.statements.get(1));
        assertTrue(
            KeepStatementListener.statements.get(1) !=
            KeepStatementListener.statements.get(2));

        cursor.close();
        query.close();
    }

    public static class KeepStatementListener extends DefaultExecuteListener {

        static void reset() {
            statements.clear();
            closed = 0;
        }

        static List<PreparedStatement> statements = new ArrayList<PreparedStatement>();
        static int closed = 0;

        @Override
        public void executeStart(ExecuteContext ctx) {
            final PreparedStatement delegate = ctx.statement();
            final PreparedStatement s = (PreparedStatement)
            Proxy.newProxyInstance(
                PreparedStatement.class.getClassLoader(),
                new Class[] { PreparedStatement.class },
                new InvocationHandler() {

                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        if (method.getName().equals("close")) {
                            closed++;
                        }

                        return Reflect.on(delegate).call(method.getName(), args).get();
                    }
                });

            if (!delegate.getClass().getName().toLowerCase().contains("proxy")) {
                ctx.statement(s);
            }

            statements.add(delegate);
        }
    }

    @Test
    public void testCancelStatement() throws Exception {

        // Some dialects do not really implement the cancelation well. In those
        // dialects, this query will run forever
        if (getDialect() != H2) {
            log.info("SKIPPING", "Dangerous timeout query");
            return;
        }

        // [#1855] The below query is *likely* to run for a long time, and can
        // thus be cancelled
        final Select<?> select =
        create().selectOne()
                .from(
                    TBook(), TBook(), TBook(), TBook(),
                    TBook(), TBook(), TBook(), TBook(),
                    TBook(), TBook(), TBook(), TBook(),
                    TBook(), TBook(), TBook(), TBook(),
                    TBook(), TBook(), TBook(), TBook(),
                    TBook(), TBook(), TBook(), TBook(),
                    TBook(), TBook(), TBook(), TBook());

        try {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                    }
                    catch (InterruptedException ignore) {}
                    select.cancel();
                }
            }).start();

            // The fetch should never terminate, as the above thread should cancel it
            select.fetch();
            fail();
        }
        catch (DataAccessException expected) {}
    }
}
