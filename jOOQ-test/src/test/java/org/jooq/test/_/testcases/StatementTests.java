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
package org.jooq.test._.testcases;

import static org.jooq.SQLDialect.H2;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.param;
import static org.jooq.impl.DSL.val;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.jooq.Configuration;
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
import org.jooq.impl.DSL;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultExecuteListener;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;
import org.jooq.tools.jdbc.DefaultConnection;
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
    T785 extends TableRecord<T785>,
    CASE extends UpdatableRecord<CASE>>
extends BaseTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, I, IPK, T725, T639, T785, CASE> {

    public StatementTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, I, IPK, T725, T639, T785, CASE> delegate) {
        super(delegate);
    }

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

    public void testKeepStatementWithConnectionPool() throws Exception {

        // "Normal" DataSource behaviour, without keeping an open statement
        CountingDataSource ds1 = new CountingDataSource(getConnection());

        Configuration c1 =
            create().configuration().derive(new DataSourceConnectionProvider(ds1));

        for (int i = 0; i < 5; i++) {
            ResultQuery<Record1<Integer>> query =
            DSL.using(c1)
               .select(TBook_ID())
               .from(TBook())
               .where(TBook_ID().eq(param("p", 0)));

            query.bind("p", 1);
            assertEquals(1, (int) query.fetchOne().getValue(TBook_ID()));
            query.bind("p", 2);
            assertEquals(2, (int) query.fetchOne().getValue(TBook_ID()));

            assertEquals((i + 1) * 2, ds1.open);
            assertEquals((i + 1) * 2, ds1.close);
        }

        assertEquals(10, ds1.open);
        assertEquals(10, ds1.close);

        // Keeping an open statement [#3191]
        CountingDataSource ds2 = new CountingDataSource(getConnection());

        Configuration c2 =
            create().configuration().derive(new DataSourceConnectionProvider(ds2));

        for (int i = 0; i < 5; i++) {
            ResultQuery<Record1<Integer>> query =
            DSL.using(c2)
               .select(TBook_ID())
               .from(TBook())
               .where(TBook_ID().eq(param("p", 0)))
               .keepStatement(true);

            query.bind("p", 1);
            assertEquals(1, (int) query.fetchOne().getValue(TBook_ID()));
            query.bind("p", 2);
            assertEquals(2, (int) query.fetchOne().getValue(TBook_ID()));

            assertEquals(i + 1, ds2.open);
            assertEquals(i    , ds2.close);

            query.close();

            assertEquals(i + 1, ds2.open);
            assertEquals(i + 1, ds2.close);
        }

        assertEquals(5, ds1.open);
        assertEquals(5, ds1.close);
    }

    private static class CountingDataSource implements DataSource {

        final Connection connection;
        int              open;
        int              close;

        public CountingDataSource(Connection connection) {
            this.connection = connection;
        }

        @Override
        public PrintWriter getLogWriter() throws SQLException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setLogWriter(PrintWriter out) throws SQLException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setLoginTimeout(int seconds) throws SQLException {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getLoginTimeout() throws SQLException {
            throw new UnsupportedOperationException();
        }

        @Override
        public Logger getParentLogger() throws SQLFeatureNotSupportedException {
            throw new UnsupportedOperationException();
        }

        @Override
        public <T> T unwrap(Class<T> iface) throws SQLException {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isWrapperFor(Class<?> iface) throws SQLException {
            throw new UnsupportedOperationException();
        }

        @Override
        public Connection getConnection() throws SQLException {
            open++;

            return new DefaultConnection(connection) {

                @Override
                public void close() throws SQLException {
                    close++;
                }
            };
        }

        @Override
        public Connection getConnection(String username, String password) throws SQLException {
            throw new UnsupportedOperationException();
        }
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
