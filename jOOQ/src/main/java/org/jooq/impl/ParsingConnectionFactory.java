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

import static java.util.Collections.nCopies;
import static org.jooq.impl.DSL.val;
import static org.jooq.impl.ParsingConnection.translate;
import static org.jooq.impl.R2DBC.setParamType;
import static org.jooq.impl.Tools.EMPTY_PARAM;

import java.util.ArrayList;
import java.util.List;

import org.jooq.Configuration;
import org.jooq.Param;
import org.jooq.exception.DetachedException;
import org.jooq.impl.DefaultRenderContext.Rendered;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.r2dbc.spi.Batch;
import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryMetadata;
import io.r2dbc.spi.ConnectionMetadata;
import io.r2dbc.spi.IsolationLevel;
import io.r2dbc.spi.Result;
import io.r2dbc.spi.Statement;
import io.r2dbc.spi.TransactionDefinition;
import io.r2dbc.spi.ValidationDepth;

/**
 * @author Lukas Eder
 */
final class ParsingConnectionFactory implements ConnectionFactory {
    final Configuration configuration;

    ParsingConnectionFactory(Configuration configuration) {
        if (configuration.connectionFactory() instanceof NoConnectionFactory)
            throw new DetachedException("ConnectionProvider did not provide an R2DBC ConnectionFactory");

        this.configuration = configuration.deriveSettings(s -> setParamType(configuration.dialect(), s));
    }

    @Override
    public final Publisher<? extends Connection> create() {
        return subscriber -> configuration
            .connectionFactory()
            .create()
            .subscribe(new ParsingR2DBCConnectionSubscriber(subscriber));
    }

    @Override
    public final ConnectionFactoryMetadata getMetadata() {
        return configuration.connectionFactory().getMetadata();
    }

    private final class ParsingR2DBCConnectionSubscriber implements Subscriber<Connection> {
        private final Subscriber<? super Connection> subscriber;

        private ParsingR2DBCConnectionSubscriber(Subscriber<? super Connection> subscriber) {
            this.subscriber = subscriber;
        }

        @Override
        public final void onSubscribe(Subscription s) {
            subscriber.onSubscribe(s);
        }

        @Override
        public final void onNext(Connection c) {
            subscriber.onNext(new ParsingR2DBCConnection(c));
        }

        @Override
        public final void onError(Throwable t) {
            subscriber.onError(t);
        }

        @Override
        public final void onComplete() {
            subscriber.onComplete();
        }
    }

    private final class ParsingR2DBCConnection implements Connection {
        private final Connection delegate;

        private ParsingR2DBCConnection(Connection delegate) {
            this.delegate = delegate;
        }

        @Override
        public final Publisher<Void> beginTransaction() {
            return delegate.beginTransaction();
        }

        @Override
        public final Publisher<Void> beginTransaction(TransactionDefinition definition) {
            return delegate.beginTransaction(definition);
        }

        @Override
        public final Publisher<Void> close() {
            return delegate.close();
        }

        @Override
        public final Publisher<Void> commitTransaction() {
            return delegate.commitTransaction();
        }

        @Override
        public final Publisher<Void> createSavepoint(String name) {
            return delegate.createSavepoint(name);
        }

        @Override
        public final boolean isAutoCommit() {
            return delegate.isAutoCommit();
        }

        @Override
        public final ConnectionMetadata getMetadata() {
            return delegate.getMetadata();
        }

        @Override
        public final IsolationLevel getTransactionIsolationLevel() {
            return delegate.getTransactionIsolationLevel();
        }

        @Override
        public final Publisher<Void> releaseSavepoint(String name) {
            return delegate.releaseSavepoint(name);
        }

        @Override
        public final Publisher<Void> rollbackTransaction() {
            return delegate.rollbackTransaction();
        }

        @Override
        public final Publisher<Void> rollbackTransactionToSavepoint(String name) {
            return delegate.rollbackTransactionToSavepoint(name);
        }

        @Override
        public final Publisher<Void> setAutoCommit(boolean autoCommit) {
            return delegate.setAutoCommit(autoCommit);
        }

        @Override
        public final Publisher<Void> setTransactionIsolationLevel(IsolationLevel isolationLevel) {
            return delegate.setTransactionIsolationLevel(isolationLevel);
        }

        @Override
        public final Publisher<Boolean> validate(ValidationDepth depth) {
            return delegate.validate(depth);
        }

        @Override
        public final Batch createBatch() {
            return new ParsingR2DBCBatch(delegate.createBatch());
        }

        @Override
        public final Statement createStatement(String sql) {
            return new ParsingR2DBCStatement(delegate, sql);
        }
    }

    private final class ParsingR2DBCBatch implements Batch {
        private final Batch delegate;

        private ParsingR2DBCBatch(Batch b) {
            this.delegate = b;
        }

        @Override
        public final Batch add(String sql) {
            delegate.add(translate(configuration, sql).sql);
            return this;
        }

        @Override
        public final Publisher<? extends Result> execute() {
            return delegate.execute();
        }
    }

    private final class ParsingR2DBCStatement implements Statement {
        private final Connection           delegate;
        private final String               input;
        private final List<List<Param<?>>> params;

        private ParsingR2DBCStatement(Connection delegate, String input) {
            this.delegate = delegate;
            this.input = input;
            this.params = new ArrayList<>();

            params.add(new ArrayList<>());
        }

        private final List<Param<?>> list(int index) {
            List<Param<?>> list = params.get(params.size() - 1);

            int reserve = index + 1 - list.size();
            if (reserve > 0)
                list.addAll(nCopies(reserve, null));

            return list;
        }

        @Override
        public final Statement add() {
            params.add(new ArrayList<>());
            return this;
        }

        @Override
        public final Statement bind(int index, Object value) {
            list(index).set(index, val(value));
            return this;
        }

        @Override
        public final Statement bind(String name, Object value) {
            // TODO
            throw new UnsupportedOperationException();
        }

        @Override
        public final Statement bindNull(int index, Class<?> type) {
            list(index).set(index, val(null, type));
            return this;
        }

        @Override
        public final Statement bindNull(String name, Class<?> type) {
            // TODO
            throw new UnsupportedOperationException();
        }

        @SuppressWarnings("null")
        @Override
        public final Publisher<? extends Result> execute() {
            Statement statement = null;

            for (List<Param<?>> p : params) {
                if (statement != null)
                    statement.add();

                Rendered rendered = translate(configuration, input, p.toArray(EMPTY_PARAM));

                if (statement == null)
                    statement = delegate.createStatement(rendered.sql);

                int j = 0;
                for (Param<?> o : rendered.bindValues)
                    if (o.getValue() == null)
                        statement.bindNull(j++, o.getType());
                    else
                        statement.bind(j++, o.getValue());
            }

            return statement.execute();
        }
    }
}
