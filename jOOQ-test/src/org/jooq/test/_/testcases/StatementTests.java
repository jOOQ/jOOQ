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
package org.jooq.test._.testcases;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.jooq.SQLDialect.H2;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.val;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Cursor;
import org.jooq.DSLContext;
import org.jooq.ExecuteContext;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record6;
import org.jooq.ResultQuery;
import org.jooq.Select;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.conf.StatementType;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DefaultExecuteListener;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;
import org.jooq.tools.reflect.Reflect;

import org.junit.Test;

public class StatementTests<
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

    public StatementTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, I, IPK, T725, T639, T785> delegate) {
        super(delegate);
    }

    @Test
    public void testKeepStatement() throws Exception {
        DSLContext create = create(new KeepStatementListener());

        // [#385] By default, new statements are created for every execution
        KeepStatementListener.reset();
        ResultQuery<Record2<Integer, Integer>> query = create.select(val(1), inline(2));
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

        Cursor<Record2<Integer, Integer>> cursor = query.fetchLazy();
        assertEquals(3, cursor.fetchOne().getValue(0));
        assertEquals(3, query.fetchOne(0));

        // [#1886] The first underlying statement should've been closed when
        // using StatementType.STATIC_STATEMENT
        assertEquals(4, KeepStatementListener.statements.size());
        assertEquals(0, KeepStatementListener.closed);
        assertEquals(
            create().configuration().settings().getStatementType() == StatementType.PREPARED_STATEMENT,
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

        /**
         * Generated UID
         */
        private static final long serialVersionUID = 7399239846062763212L;

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
        if (dialect() != H2) {
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
