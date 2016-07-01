/**
 * Copyright (c) 2009-2016, Data Geekery GmbH (http://www.datageekery.com)
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
package org.jooq.impl;

import static org.jooq.impl.Tools.DataKey.DATA_DEFAULT_TRANSACTION_PROVIDER_CONNECTION;

import java.sql.Connection;
import java.sql.Savepoint;
import java.util.ArrayDeque;
import java.util.Deque;

import org.jooq.Configuration;
import org.jooq.ConnectionProvider;
import org.jooq.TransactionContext;
import org.jooq.TransactionProvider;

/**
 * A {@link TransactionProvider} that implements thread-bound transaction
 * semantics.
 * <p>
 * Use this <code>TransactionProvider</code> if your transactions are
 * thread-bound, meaning that a transaction and its underlying
 * {@link Connection} will never leave the thread that started the transaction.
 * <p>
 * When this <code>TransactionProvider</code> is used, users must pass their
 * custom {@link ConnectionProvider} implementations to this
 * <code>TransactionProvider</code>, instead of passing it to the
 * {@link Configuration}.
 *
 * @author Lukas Eder
 */
public class ThreadLocalTransactionProvider implements TransactionProvider {

    final DefaultTransactionProvider        transaction;
    final ThreadLocalConnectionProvider     connection;
    final ThreadLocal<Deque<Configuration>> configuration;

    public ThreadLocalTransactionProvider(ConnectionProvider provider) {
        this(provider, true);
    }

    /**
     * @param nested Whether nested transactions via {@link Savepoint}s are
     *            supported.
     */
    public ThreadLocalTransactionProvider(ConnectionProvider provider, boolean nested) {
        this.connection = new ThreadLocalConnectionProvider(provider);
        this.transaction = new DefaultTransactionProvider(connection, nested);
        this.configuration = new ThreadLocal<Deque<Configuration>>();
    }

    @Override
    public void begin(TransactionContext ctx) {
        transaction.begin(ctx);
        configurations().push(ctx.configuration());
        if (transaction.nestingLevel(ctx.configuration()) == 1)
            connection.tl.set(((DefaultConnectionProvider) ctx.configuration().data(DATA_DEFAULT_TRANSACTION_PROVIDER_CONNECTION)).connection);
    }

    @Override
    public void commit(TransactionContext ctx) {
        if (transaction.nestingLevel(ctx.configuration()) == 1)
            connection.tl.remove();
        configurations().pop();
        transaction.commit(ctx);
    }

    @Override
    public void rollback(TransactionContext ctx) {
        if (transaction.nestingLevel(ctx.configuration()) == 1)
            connection.tl.remove();
        configurations().pop();
        transaction.rollback(ctx);
    }

    Configuration configuration(Configuration fallback) {
        Deque<Configuration> configurations = configurations();
        return configurations.isEmpty() ? fallback : configurations.peek();
    }

    private Deque<Configuration> configurations() {
        Deque<Configuration> result = configuration.get();

        if (result == null) {
            result = new ArrayDeque<Configuration>();
            configuration.set(result);
        }

        return result;
    }
}
