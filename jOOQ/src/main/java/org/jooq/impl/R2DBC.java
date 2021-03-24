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

import static org.jooq.impl.DefaultDataType.getDataType;
import static org.jooq.impl.Tools.recordFactory;
import static org.jooq.tools.StringUtils.defaultIfNull;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Wrapper;
import java.util.ArrayDeque;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

import org.jooq.Configuration;
import org.jooq.Cursor;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Param;
import org.jooq.Record;
import org.jooq.conf.ParamType;
import org.jooq.conf.SettingsTools;
import org.jooq.impl.DefaultRenderContext.Rendered;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.r2dbc.spi.ColumnMetadata;
import io.r2dbc.spi.Connection;
import io.r2dbc.spi.RowMetadata;
import io.r2dbc.spi.Statement;

/**
 * A single namespace for all reactive {@link Subscription} and other implementations.
 */
final class R2DBC {

    static volatile boolean is_0_9 = true;

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
                try {
                    // TODO: Cache this getFields() call
                    Field<?>[] fields = query.getFields(new R2DBCResultSetMetaData(query.configuration(), m));

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
                }

                // TODO: More specific error handling
                catch (Throwable t) {
                    onError(t);
                    return null;
                }
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
            try {
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

            // TODO: More specific error handling
            catch (Throwable t) {
                onError(t);
            }
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

    // -------------------------------------------------------------------------
    // JDBC to R2DBC bridges for better interop, where it doesn't matter
    // -------------------------------------------------------------------------

    static abstract class R2DBCWrapper implements Wrapper {

        @Override
        public final <T> T unwrap(Class<T> iface) throws SQLException {
            throw new SQLFeatureNotSupportedException("R2DBC can't unwrap JDBC types");
        }

        @Override
        public final boolean isWrapperFor(Class<?> iface) throws SQLException {
            return false;
        }
    }

    static final class R2DBCResultSetMetaData extends R2DBCWrapper implements ResultSetMetaData {

        final Configuration     c;
        final RowMetadata       m;

        R2DBCResultSetMetaData(Configuration c, RowMetadata m) {
            this.c = c;
            this.m = m;
        }

        private final ColumnMetadata meta(int column) {
            return m.getColumnMetadata(column - 1);
        }

        @Override
        public final int getColumnCount() throws SQLException {
            return m.getColumnNames().size();
        }

        @Override
        public final int isNullable(int column) throws SQLException {
            switch (meta(column).getNullability()) {
                case NON_NULL:
                    return ResultSetMetaData.columnNoNulls;
                case NULLABLE:
                    return ResultSetMetaData.columnNullable;
                case UNKNOWN:
                    return ResultSetMetaData.columnNullableUnknown;
                default:
                    throw new SQLFeatureNotSupportedException("Nullability: " + meta(column).getNullability().toString());
            }
        }

        @Override
        public final String getCatalogName(int column) throws SQLException {
            return "";
        }

        @Override
        public final String getSchemaName(int column) throws SQLException {
            return "";
        }

        @Override
        public final String getTableName(int column) throws SQLException {
            return "";
        }

        @Override
        public final String getColumnLabel(int column) throws SQLException {
            return getColumnName(column);
        }

        @Override
        public final String getColumnName(int column) throws SQLException {
            return meta(column).getName();
        }

        @Override
        public final int getPrecision(int column) throws SQLException {
            return defaultIfNull(meta(column).getPrecision(), 0);
        }

        @Override
        public final int getScale(int column) throws SQLException {
            return defaultIfNull(meta(column).getScale(), 0);
        }

        private final Class<?> getType(int column) {
            return defaultIfNull(meta(column).getJavaType(), Object.class);
        }

        private final DataType<?> getDataType(int column) {
            return DefaultDataType.getDataType(c.family(), getType(column));
        }

        @Override
        public final int getColumnType(int column) throws SQLException {
            return getDataType(column).getSQLType();
        }

        @Override
        public final String getColumnClassName(int column) throws SQLException {
            return getType(column).getName();
        }

        @Override
        public final String getColumnTypeName(int column) throws SQLException {
            if (is_0_9) {
                try {
                    return meta(column).getType().getName();
                }

                // ColumnMetadata::getType was added in 0.9
                catch (AbstractMethodError e) {
                    is_0_9 = false;
                }
            }

            return getDataType(column).getName();
        }

        @Override
        public final boolean isReadOnly(int column) throws SQLException {
            return false;
        }

        @Override
        public final boolean isWritable(int column) throws SQLException {
            return true;
        }

        @Override
        public final boolean isDefinitelyWritable(int column) throws SQLException {
            return true;
        }

        @Override
        public final boolean isSigned(int column) throws SQLException {
            return false;
        }

        @Override
        public final int getColumnDisplaySize(int column) throws SQLException {
            return 0;
        }

        @Override
        public final boolean isAutoIncrement(int column) throws SQLException {
            return false;
        }

        @Override
        public final boolean isCaseSensitive(int column) throws SQLException {
            return false;
        }

        @Override
        public final boolean isSearchable(int column) throws SQLException {
            return false;
        }

        @Override
        public final boolean isCurrency(int column) throws SQLException {
            return false;
        }
    }
}
