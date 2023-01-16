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
package org.jooq.tools.r2dbc;

import java.time.Duration;

import org.reactivestreams.Publisher;

import io.r2dbc.spi.Batch;
import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionMetadata;
import io.r2dbc.spi.IsolationLevel;
import io.r2dbc.spi.Statement;
import io.r2dbc.spi.TransactionDefinition;
import io.r2dbc.spi.ValidationDepth;

/**
 * A default R2DBC {@link Connection} implementation delegating all R2DBC
 * calls to an internal getDelegate().
 *
 * @author Lukas Eder
 */
public class DefaultConnection implements Connection {

    private final Connection delegate;

    public DefaultConnection(Connection delegate) {
        this.delegate = delegate;
    }

    public Connection getDelegate() {
        return delegate;
    }

    @Override
    public Publisher<Void> beginTransaction() {
        return getDelegate().beginTransaction();
    }

    @Override
    public Publisher<Void> beginTransaction(TransactionDefinition definition) {
        return getDelegate().beginTransaction(definition);
    }

    @Override
    public Publisher<Void> close() {
        return getDelegate().close();
    }

    @Override
    public Publisher<Void> commitTransaction() {
        return getDelegate().commitTransaction();
    }

    @Override
    public Batch createBatch() {
        return getDelegate().createBatch();
    }

    @Override
    public Publisher<Void> createSavepoint(String name) {
        return getDelegate().createSavepoint(name);
    }

    @Override
    public Statement createStatement(String sql) {
        return getDelegate().createStatement(sql);
    }

    @Override
    public boolean isAutoCommit() {
        return getDelegate().isAutoCommit();
    }

    @Override
    public ConnectionMetadata getMetadata() {
        return getDelegate().getMetadata();
    }

    @Override
    public IsolationLevel getTransactionIsolationLevel() {
        return getDelegate().getTransactionIsolationLevel();
    }

    @Override
    public Publisher<Void> releaseSavepoint(String name) {
        return getDelegate().releaseSavepoint(name);
    }

    @Override
    public Publisher<Void> rollbackTransaction() {
        return getDelegate().rollbackTransaction();
    }

    @Override
    public Publisher<Void> rollbackTransactionToSavepoint(String name) {
        return getDelegate().rollbackTransactionToSavepoint(name);
    }

    @Override
    public Publisher<Void> setAutoCommit(boolean autoCommit) {
        return getDelegate().setAutoCommit(autoCommit);
    }

    @Override
    public Publisher<Void> setLockWaitTimeout(Duration timeout) {
        return getDelegate().setLockWaitTimeout(timeout);
    }

    @Override
    public Publisher<Void> setStatementTimeout(Duration timeout) {
        return getDelegate().setStatementTimeout(timeout);
    }

    @Override
    public Publisher<Void> setTransactionIsolationLevel(IsolationLevel isolationLevel) {
        return getDelegate().setTransactionIsolationLevel(isolationLevel);
    }

    @Override
    public Publisher<Boolean> validate(ValidationDepth depth) {
        return getDelegate().validate(depth);
    }
}
