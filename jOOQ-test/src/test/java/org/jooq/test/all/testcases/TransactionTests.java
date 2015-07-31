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
package org.jooq.test.all.testcases;

import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.SQLDialect.HANA;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

import org.jooq.ConnectionProvider;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record6;
import org.jooq.TableRecord;
import org.jooq.TransactionContext;
import org.jooq.TransactionProvider;
import org.jooq.UpdatableRecord;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultTransactionProvider;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;

/**
 * @author Lukas Eder
 */
@SuppressWarnings("serial")
public class TransactionTests<
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
    CS   extends UpdatableRecord<CS>,
    I    extends TableRecord<I>,
    IPK  extends UpdatableRecord<IPK>,
    T725 extends UpdatableRecord<T725>,
    T639 extends UpdatableRecord<T639>,
    T785 extends TableRecord<T785>,
    CASE extends UpdatableRecord<CASE>>
extends BaseTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, CS, I, IPK, T725, T639, T785, CASE> {

    public TransactionTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, CS, I, IPK, T725, T639, T785, CASE> delegate) {
        super(delegate);
    }

    static class MyRuntimeException extends RuntimeException {
        public MyRuntimeException(String message) {
            super(message);
        }
    }

    static class MyCheckedException extends Exception {
        public MyCheckedException(String message) {
            super(message);
        }
    }

    public void testTransactionsWithJDBCSimple() throws Exception {
        DSLContext create = create();
        TransactionalConnectionProvider provider = new TransactionalConnectionProvider(create.configuration().connectionProvider());
        create.configuration().set(provider);

        try {
            create.transaction(c -> {
                assertAutoCommit(c.connectionProvider(), false);

                assertEquals(1,
                DSL.using(c)
                   .insertInto(TAuthor(), TAuthor_ID(), TAuthor_LAST_NAME())
                   .values(3, "Koontz")
                   .execute());

                throw new MyRuntimeException("No");
            });

            fail();
        }
        catch (MyRuntimeException expected) {
            assertEquals(1, provider.acquire.get());
            assertEquals(1, provider.release.get());

            assertEquals("No", expected.getMessage());
            assertEquals(2, create.fetchCount(TAuthor()));
            assertAutoCommit(create.configuration().connectionProvider(), true);
        }

        assertEquals(2, create.fetchCount(TAuthor()));
    }

    public void testTransactionsWithJDBCCheckedException() throws Exception {
        DSLContext create = create();
        TransactionalConnectionProvider provider = new TransactionalConnectionProvider(create.configuration().connectionProvider());
        create.configuration().set(provider);

        try {
            create.transaction(c -> {
                assertAutoCommit(c.connectionProvider(), false);

                assertEquals(1,
                DSL.using(c)
                   .insertInto(TAuthor(), TAuthor_ID(), TAuthor_LAST_NAME())
                   .values(3, "Koontz")
                   .execute());

                throw new MyCheckedException("No");
            });

            fail();
        }
        catch (DataAccessException expected) {
            assertEquals(1, provider.acquire.get());
            assertEquals(1, provider.release.get());

            assertEquals(MyCheckedException.class, expected.getCause().getClass());
            assertEquals("No", expected.getCause().getMessage());
            assertEquals(2, create.fetchCount(TAuthor()));
            assertAutoCommit(create.configuration().connectionProvider(), true);
        }

        assertEquals(2, create.fetchCount(TAuthor()));
    }

    public void testTransactionsWithJDBCNestedWithSavepoints() throws Exception {
        assumeFamilyNotIn(CUBRID, HANA);

        jOOQAbstractTest.reset = false;

        final int[] inserted = new int[1];
        final int[] updated = new int[1];

        DSLContext create = create();
        TransactionalConnectionProvider provider = new TransactionalConnectionProvider(create.configuration().connectionProvider());
        create.configuration().set(provider);

        Integer result =
        create.transactionResult(c1 -> {
            assertAutoCommit(c1.connectionProvider(), false);

            inserted[0] =
            DSL.using(c1)
               .insertInto(TAuthor(), TAuthor_ID(), TAuthor_LAST_NAME())
               .values(3, "Koontz")
               .execute();

            assertEquals(1, inserted[0]);

            // Implicit savepoint here
            try {
                DSL.using(c1).transaction(c2 -> {
                    assertAutoCommit(c2.connectionProvider(), false);

                    updated[0] =
                    DSL.using(c2)
                       .update(TAuthor())
                       .set(TAuthor_FIRST_NAME(), "Dean")
                       .where(TAuthor_ID().eq(3))
                       .execute();

                    assertEquals(1, updated[0]);

                    throw new MyRuntimeException("No");
                });
            }

            // Rollback to savepoint must have happened
            catch (MyRuntimeException expected) {
                assertNull(DSL.using(c1).fetchOne(TAuthor(), TAuthor_ID().eq(3)).getValue(TAuthor_FIRST_NAME()));
                assertEquals(MyRuntimeException.class, expected.getClass());
                assertEquals("No", expected.getMessage());
            }

            return 42;
        });

        assertEquals(1, provider.acquire.get());
        assertEquals(1, provider.release.get());
        assertEquals(3, create.fetchCount(TAuthor()));
        assertEquals(42, (int) result);
    }


    public void testTransactionsWithJDBCNestedWithoutSavepoints() throws Exception {
        assumeFamilyNotIn(HANA);

        final int[] inserted = new int[1];
        final int[] updated = new int[1];

        DSLContext create = create();
        TransactionalConnectionProvider provider = new TransactionalConnectionProvider(create.configuration().connectionProvider());
        create.configuration().set(provider);
        create.configuration().set(new DefaultTransactionProvider(provider, false));

        try {
            create.transactionResult(c1 -> {
                assertAutoCommit(c1.connectionProvider(), false);

                inserted[0] =
                DSL.using(c1)
                   .insertInto(TAuthor(), TAuthor_ID(), TAuthor_LAST_NAME())
                   .values(3, "Koontz")
                   .execute();

                assertEquals(1, inserted[0]);

                // No savepoint here
                DSL.using(c1).transaction(c2 -> {
                    assertAutoCommit(c2.connectionProvider(), false);

                    updated[0] =
                    DSL.using(c2)
                       .update(TAuthor())
                       .set(TAuthor_FIRST_NAME(), "Dean")
                       .where(TAuthor_ID().eq(3))
                       .execute();

                    assertEquals(1, updated[0]);

                    throw new MyRuntimeException("No");
                });

                // This code should never be reached, as exception should propagate
                fail();
                return 42;
            });
        }

        catch (MyRuntimeException expected) {
            assertEquals(1, provider.acquire.get());
            assertEquals(1, provider.release.get());

            assertEquals(2, create.fetchCount(TAuthor()));
            assertEquals(MyRuntimeException.class, expected.getClass());
            assertEquals("No", expected.getMessage());
        }
    }


    public void testTransactionsWithExceptionInRollback() throws Exception {
        final int[] inserted = new int[2];

        DSLContext create = create();
        TransactionalConnectionProvider provider = new TransactionalConnectionProvider(create.configuration().connectionProvider());
        create.configuration().set(provider);
        create.configuration().set(new TransactionProvider() {

            final TransactionProvider trx = new DefaultTransactionProvider(provider);

            @Override
            public void begin(TransactionContext ctx) throws DataAccessException {
                trx.begin(ctx);
            }

            @Override
            public void commit(TransactionContext ctx) throws DataAccessException {
                trx.commit(ctx);
            }

            @Override
            public void rollback(TransactionContext ctx) throws DataAccessException {
                trx.rollback(ctx);
                throw new MyRuntimeException("Suppress this one");
            }
        });

        try {
            create.transaction(c1 -> {
                assertAutoCommit(c1.connectionProvider(), false);

                inserted[0] =
                DSL.using(c1)
                   .insertInto(TAuthor(), TAuthor_ID(), TAuthor_LAST_NAME())
                   .values(3, "Koontz")
                   .execute();

                inserted[1] =
                DSL.using(c1)
                   .insertInto(TAuthor(), TAuthor_ID(), TAuthor_LAST_NAME())
                   .values(3, "Koontz")
                   .execute();

                // This code should never be reached, as exception should propagate
                fail("No exception was thrown. Are constraints supported?");
            });
        }

        catch (DataAccessException expected) {
            assertEquals(1, provider.acquire.get());
            assertEquals(1, provider.release.get());

            assertEquals(2, create.fetchCount(TAuthor()));
            assertEquals(DataAccessException.class, expected.getClass());
            assertEquals(MyRuntimeException.class, expected.getSuppressed()[0].getClass());
            assertEquals("Suppress this one", expected.getSuppressed()[0].getMessage());
        }
    }

    private void assertAutoCommit(ConnectionProvider provider, boolean autoCommit) {
        Connection c = null;

        try {
            c = provider.acquire();
            assertEquals(autoCommit, c.getAutoCommit());
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            if (c != null)
                provider.release(c);
        }
    }

    private static class TransactionalConnectionProvider implements ConnectionProvider {

        private final ConnectionProvider delegate;
        private final AtomicInteger acquire;
        private final AtomicInteger release;

        TransactionalConnectionProvider(ConnectionProvider delegate) {
            this.delegate = delegate;
            this.acquire = new AtomicInteger();
            this.release = new AtomicInteger();
        }

        @Override
        public Connection acquire() throws DataAccessException {
            acquire.incrementAndGet();
            return delegate.acquire();
        }

        @Override
        public void release(Connection connection) throws DataAccessException {
            release.incrementAndGet();
            delegate.release(connection);
        }
    }
}
