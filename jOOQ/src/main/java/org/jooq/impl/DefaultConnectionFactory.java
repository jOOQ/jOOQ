/*
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

import static org.jooq.impl.R2DBC.AbstractSubscription.onRequest;

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

    Connection    connection;
    final boolean finalize;

    DefaultConnectionFactory(Connection connection) {
        this(connection, false);
    }

    DefaultConnectionFactory(Connection connection, boolean finalize) {
        this.connection = connection;
        this.finalize = finalize;
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

    private final class NonClosingConnection implements Connection {
        @Override
        public Publisher<Void> beginTransaction() {
            return connectionOrThrow().beginTransaction();
        }

        @Override
        public Publisher<Void> beginTransaction(TransactionDefinition definition) {
            return connectionOrThrow().beginTransaction(definition);
        }

        @Override
        public Publisher<Void> close() {
            return s -> s.onSubscribe(onRequest(s, x -> x.onComplete()));
        }

        @Override
        public Publisher<Void> commitTransaction() {
            return connectionOrThrow().commitTransaction();
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
            return connectionOrThrow().rollbackTransaction();
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
    }
}
