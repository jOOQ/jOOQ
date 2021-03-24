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

import static org.jooq.impl.Tools.recordFactory;

import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

import org.jooq.Cursor;
import org.jooq.Field;
import org.jooq.Param;
import org.jooq.Record;
import org.jooq.conf.ParamType;
import org.jooq.conf.SettingsTools;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DefaultRenderContext.Rendered;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.r2dbc.spi.Connection;
import io.r2dbc.spi.Statement;

/**
 * A single namespace for all reactive {@link Subscription} and other implementations.
 */
final class Flow {

    static final class RowSubscriber<R extends Record> implements Subscriber<R> {

        final ResultSubscriber<R> upstream;
        Subscription              subscription;

        public RowSubscriber(ResultSubscriber<R> s) {
            this.upstream = s;
        }

        @Override
        public void onSubscribe(Subscription s) {
            this.subscription = s;
            subscription.request(upstream.upstream.upstream.requested.getAndSet(0L));
        }

        @Override
        public void onNext(R record) {
            upstream.upstream.upstream.subscriber.onNext(record);

            long requested = upstream.upstream.upstream.requested.getAndSet(0L);
            if (requested > 0)
                subscription.request(requested);
        }

        @Override
        public void onError(Throwable t) {
            upstream.onError(t);
        }

        @Override
        public void onComplete() {
            upstream.upstream.upstream.subscriber.onComplete();
        }
    }

    static final class ResultSubscriber<R extends Record> implements Subscriber<io.r2dbc.spi.Result> {

        final AbstractResultQuery<R>  query;
        final ConnectionSubscriber<R> upstream;

        ResultSubscriber(AbstractResultQuery<R> query, ConnectionSubscriber<R> s) {
            this.query = query;
            this.upstream = s;
        }

        @Override
        public void onSubscribe(Subscription s) {
            s.request(1);
        }

        @SuppressWarnings("unchecked")
        @Override
        public void onNext(io.r2dbc.spi.Result r) {
            r.map((row, m) -> {
                // TODO: Cache this getFields() call
                Field<?>[] fields;
                try {
                    fields = query.getFields(null);
                }

                // TODO: Can this happen? If so, trigger onError() correctly
                catch (SQLException ignore) {
                    throw new DataAccessException("", ignore);
                }

                // TODO: This call is duplicated from CursorImpl and related classes.
                // Refactor this call to make sure code is re-used, especially when
                // ExecuteListener lifecycle management is implemented
                RecordDelegate<AbstractRecord> delegate = Tools.newRecord(true, (Supplier<AbstractRecord>) recordFactory(query.getRecordType(), Tools.row0(fields)), query.configuration());

                return (R) delegate.operate(record -> {
                    // TODO: Go through Field.getBinding()
                    // TODO: Make sure all the embeddable records, and other types of nested records are supported
                    for (int i = 0; i < fields.length; i++) {
                        Field<?> f = fields[i];
                        Object value = row.get(i, f.getType());
                        record.values[i] = value;
                        record.originals[i] = value;
                    }

                    return record;
                });
            }).subscribe(new RowSubscriber<R>(this));
        }

        @Override
        public void onError(Throwable t) {
            upstream.onError(t);
        }

        @Override
        public void onComplete() {}
    }

    static final class ConnectionSubscriber<R extends Record> implements Subscriber<Connection> {

        final AbstractResultQuery<R> query;
        final RecordSubscription<R>  upstream;

        ConnectionSubscriber(AbstractResultQuery<R> query, RecordSubscription<R> s) {
            this.query = query;
            this.upstream = s;
        }

        @Override
        public void onSubscribe(Subscription s) {
            s.request(1);
        }

        @Override
        public void onNext(Connection c) {
            DefaultRenderContext render = new DefaultRenderContext(query.configuration().derive(
                SettingsTools.clone(query.configuration().settings()).withParamType(ParamType.NAMED).withRenderNamedParamPrefix("$")
            ));

            Rendered r = new Rendered(render.visit(query).render(), render.bindValues(), render.skipUpdateCounts());
            Statement stmt = c.createStatement(r.sql);

            int i = 0;
            for (Param<?> p : r.bindValues)
                if (p.getValue() == null)
                    stmt.bindNull(i++, p.getType());
                else
                    stmt.bind(i++, p.getValue());

            stmt.execute().subscribe(new ResultSubscriber<>(query, this));
        }

        @Override
        public void onError(Throwable t) {
            upstream.subscriber.onError(t);
        }

        @Override
        public void onComplete() {}
    }

    static final class RecordSubscription<R extends Record> implements Subscription {

        final AbstractResultQuery<R>    query;
        final AtomicLong                requested;
        final Subscriber<? super R>     subscriber;
        Publisher<? extends Connection> connection;

        RecordSubscription(AbstractResultQuery<R> query, Subscriber<? super R> subscriber) {
            this.query = query;
            this.subscriber = subscriber;
            this.requested = new AtomicLong();
        }

        @Override
        public void request(long n) {
            requested.getAndAdd(n);

            if (connection == null) {
                connection = query.configuration().connectionFactory().create();
                connection.subscribe(new ConnectionSubscriber<>(query, this));
            }
        }

        @Override
        public void cancel() {
            subscriber.onComplete();
        }
    }

    static final class BlockingSubscription<R extends Record> implements Subscription {
        private final AbstractResultQuery<R> query;
        private final Subscriber<? super R>  subscriber;
        private Cursor<R>                    c;
        private ArrayDeque<R>                buffer;

        BlockingSubscription(AbstractResultQuery<R> query, Subscriber<? super R> subscriber) {
            this.query = query;
            this.subscriber = subscriber;
        }

        @Override
        public void request(long n) {
            int i = (int) Math.min(n, Integer.MAX_VALUE);

            try {
                if (c == null)
                    c = query.fetchLazyNonAutoClosing();

                if (buffer == null)
                    buffer = new ArrayDeque<>();

                if (buffer.size() < i)
                    buffer.addAll(c.fetchNext(i - buffer.size()));

                boolean complete = buffer.size() < i;
                while (!buffer.isEmpty()) {
                    subscriber.onNext(buffer.pollFirst());
                }

                if (complete)
                    doComplete();
            }
            catch (Throwable t) {
                subscriber.onError(t);
                doComplete();
            }
        }

        private void doComplete() {
            close();
            subscriber.onComplete();
        }

        private void close() {
            if (c != null)
                c.close();
        }

        @Override
        public void cancel() {
            close();
        }
    }
}
