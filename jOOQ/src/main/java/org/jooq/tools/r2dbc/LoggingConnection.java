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

import org.jooq.tools.JooqLogger;

import org.reactivestreams.Publisher;

import io.r2dbc.spi.Batch;
import io.r2dbc.spi.Connection;
import io.r2dbc.spi.IsolationLevel;
import io.r2dbc.spi.Statement;
import io.r2dbc.spi.TransactionDefinition;
import io.r2dbc.spi.ValidationDepth;

/**
 * An R2DBC {@link Connection} proxy that logs all statements that are prepared
 * or executed using it.
 *
 * @author Lukas Eder
 */
public class LoggingConnection extends DefaultConnection {

    private static final JooqLogger log = JooqLogger.getLogger(LoggingConnection.class);

    public LoggingConnection(Connection delegate) {
        super(delegate);
    }

    @Override
    public Publisher<Void> beginTransaction() {
        return s -> {
            if (log.isDebugEnabled())
                log.debug("Connection::beginTransaction");

            getDelegate().beginTransaction().subscribe(s);
        };
    }

    @Override
    public Publisher<Void> beginTransaction(TransactionDefinition definition) {
        return s -> {
            if (log.isDebugEnabled())
                log.debug("Connection::beginTransaction", definition);

            getDelegate().beginTransaction(definition).subscribe(s);
        };
    }

    @Override
    public Publisher<Void> close() {
        return s -> {
            if (log.isDebugEnabled())
                log.debug("Connection::close");

            getDelegate().close().subscribe(s);
        };
    }

    @Override
    public Publisher<Void> commitTransaction() {
        return s -> {
            if (log.isDebugEnabled())
                log.debug("Connection::commitTransaction");

            getDelegate().commitTransaction().subscribe(s);
        };
    }

    @Override
    public Batch createBatch() {
        return new LoggingBatch(getDelegate().createBatch());
    }

    @Override
    public Publisher<Void> createSavepoint(String name) {
        return s -> {
            if (log.isDebugEnabled())
                log.debug("Connection::createSavepoint", name);

            getDelegate().createSavepoint(name).subscribe(s);
        };
    }

    @Override
    public Statement createStatement(String sql) {
        if (log.isDebugEnabled())
            log.debug("Connection::createStatement", sql);

        return new LoggingStatement(getDelegate().createStatement(sql));
    }

    @Override
    public Publisher<Void> releaseSavepoint(String name) {
        return s -> {
            if (log.isDebugEnabled())
                log.debug("Connection::releaseSavepoint", name);

            getDelegate().releaseSavepoint(name).subscribe(s);
        };
    }

    @Override
    public Publisher<Void> rollbackTransaction() {
        return s -> {
            if (log.isDebugEnabled())
                log.debug("Connection::rollbackTransaction");

            getDelegate().rollbackTransaction().subscribe(s);
        };
    }

    @Override
    public Publisher<Void> rollbackTransactionToSavepoint(String name) {
        return s -> {
            if (log.isDebugEnabled())
                log.debug("Connection::rollbackTransactionToSavepoint", name);

            getDelegate().rollbackTransactionToSavepoint(name).subscribe(s);
        };
    }

    @Override
    public Publisher<Void> setAutoCommit(boolean autoCommit) {
        return s -> {
            if (log.isDebugEnabled())
                log.debug("Connection::setAutoCommit", autoCommit);

            getDelegate().setAutoCommit(autoCommit).subscribe(s);
        };
    }

    @Override
    public Publisher<Void> setLockWaitTimeout(Duration timeout) {
        return s -> {
            if (log.isDebugEnabled())
                log.debug("Connection::setLockWaitTimeout", timeout);

            getDelegate().setLockWaitTimeout(timeout).subscribe(s);
        };
    }

    @Override
    public Publisher<Void> setStatementTimeout(Duration timeout) {
        return s -> {
            if (log.isDebugEnabled())
                log.debug("Connection::setStatementTimeout", timeout);

            getDelegate().setStatementTimeout(timeout).subscribe(s);
        };
    }

    @Override
    public Publisher<Void> setTransactionIsolationLevel(IsolationLevel isolationLevel) {
        return s -> {
            if (log.isDebugEnabled())
                log.debug("Connection::setTransactionIsolationLevel", isolationLevel);

            getDelegate().setTransactionIsolationLevel(isolationLevel).subscribe(s);
        };
    }

    @Override
    public Publisher<Boolean> validate(ValidationDepth depth) {
        return s -> {
            if (log.isDebugEnabled())
                log.debug("Connection::validate", depth);

            getDelegate().validate(depth).subscribe(s);
        };
    }
}
