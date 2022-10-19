/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
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
 * For more information, please visit: https://www.jooq.org/legal/licensing
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

import static org.jooq.impl.R2DBC.AbstractSubscription.onRequest;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntFunction;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

import org.jooq.exception.DetachedException;

import org.reactivestreams.Publisher;

import io.r2dbc.spi.Batch;
import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryMetadata;
import io.r2dbc.spi.ConnectionMetadata;
import io.r2dbc.spi.IsolationLevel;
import io.r2dbc.spi.Statement;
import io.r2dbc.spi.TransactionDefinition;
import io.r2dbc.spi.ValidationDepth;

/**
 * A {@link ConnectionFactory} wrapper for a single connection, which cannot be
 * closed via this connection factory.
 */
final class DefaultConnectionFactory implements ConnectionFactory {

    Connection          connection;
    final boolean       finalize;
    final boolean       nested;
    final AtomicInteger savepoints = new AtomicInteger();

    DefaultConnectionFactory(Connection connection) {
        this(connection, false, true);
    }

    DefaultConnectionFactory(Connection connection, boolean finalize, boolean nested) {
        this.connection = connection;
        this.finalize = finalize;
        this.nested = nested;
    }

    final Connection connectionOrThrow() {
        if (connection == null)
            throw new DetachedException("R2DBC Connection not available or already closed");
        else
            return connection;
    }

    @Override
    public final Publisher<? extends Connection> create() {
        return s -> s.onSubscribe(onRequest(s, x -> {
            x.onNext(new NonClosingConnection());
            x.onComplete();
        }));
    }

    @Override
    public final ConnectionFactoryMetadata getMetadata() {
        return () -> connectionOrThrow().getMetadata().getDatabaseProductName();
    }

    final class NonClosingConnection implements Connection {

        // ---------------------------------------------------------------------
        // 0.9.0.M1 API
        // ---------------------------------------------------------------------

        private <T> T nest(IntSupplier level, Supplier<T> toplevelAction, IntFunction<T> nestedAction) {
            if (nested)
                return nestedAction.apply(level.getAsInt());
            else
                return toplevelAction.get();
        }

        private String savepoint(int i) {
            return "S" + i;
        }

        @Override
        public Publisher<Void> beginTransaction() {
            return nest(
                savepoints::getAndIncrement,
                () -> connectionOrThrow().beginTransaction(),
                i -> connectionOrThrow().createSavepoint(savepoint(i))
            );
        }

        @Override
        public Publisher<Void> beginTransaction(TransactionDefinition definition) {
            return nest(
                savepoints::getAndIncrement,
                () -> connectionOrThrow().beginTransaction(definition),
                i -> connectionOrThrow().createSavepoint(savepoint(i))
            );
        }

        @Override
        public Publisher<Void> close() {
            return s -> s.onSubscribe(onRequest(s, x -> x.onComplete()));
        }

        @Override
        public Publisher<Void> commitTransaction() {
            return nest(
                savepoints::decrementAndGet,
                () -> connectionOrThrow().commitTransaction(),
                i -> connectionOrThrow().releaseSavepoint(savepoint(i))
            );
        }

        @Override
        public Batch createBatch() {
            return connectionOrThrow().createBatch();
        }

        @Override
        public Publisher<Void> createSavepoint(String name) {
            return connectionOrThrow().createSavepoint(name);
        }

        @Override
        public Statement createStatement(String sql) {
            return connectionOrThrow().createStatement(sql);
        }

        @Override
        public boolean isAutoCommit() {
            return connectionOrThrow().isAutoCommit();
        }

        @Override
        public ConnectionMetadata getMetadata() {
            return connectionOrThrow().getMetadata();
        }

        @Override
        public IsolationLevel getTransactionIsolationLevel() {
            return connectionOrThrow().getTransactionIsolationLevel();
        }

        @Override
        public Publisher<Void> releaseSavepoint(String name) {
            return connectionOrThrow().releaseSavepoint(name);
        }

        @Override
        public Publisher<Void> rollbackTransaction() {
            return nest(
                savepoints::decrementAndGet,
                () -> connectionOrThrow().rollbackTransaction(),
                i -> connectionOrThrow().rollbackTransactionToSavepoint(savepoint(i)));
        }

        @Override
        public Publisher<Void> rollbackTransactionToSavepoint(String name) {
            return connectionOrThrow().rollbackTransactionToSavepoint(name);
        }

        @Override
        public Publisher<Void> setAutoCommit(boolean autoCommit) {
            return connectionOrThrow().setAutoCommit(autoCommit);
        }

        @Override
        public Publisher<Void> setTransactionIsolationLevel(IsolationLevel isolationLevel) {
            return connectionOrThrow().setTransactionIsolationLevel(isolationLevel);
        }

        @Override
        public Publisher<Boolean> validate(ValidationDepth depth) {
            return connectionOrThrow().validate(depth);
        }

        // ---------------------------------------------------------------------
        // 0.9.0.M2 API
        // ---------------------------------------------------------------------

        @Override
        public Publisher<Void> setLockWaitTimeout(Duration timeout) {
            return connectionOrThrow().setLockWaitTimeout(timeout);
        }

        @Override
        public Publisher<Void> setStatementTimeout(Duration timeout) {
            return connectionOrThrow().setStatementTimeout(timeout);
        }
    }
}
