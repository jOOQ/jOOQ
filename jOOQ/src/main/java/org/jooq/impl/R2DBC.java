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

import static org.jooq.Publisher.subscriber;
// ...
import static org.jooq.conf.ParamType.NAMED;
import static org.jooq.impl.Tools.recordFactory;
import static org.jooq.tools.StringUtils.defaultIfNull;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.sql.Array;
import java.sql.Date;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLType;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayDeque;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import org.jooq.BindingGetResultSetContext;
import org.jooq.Configuration;
import org.jooq.Cursor;
import org.jooq.DataType;
import org.jooq.Field;
// ...
import org.jooq.Query;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.conf.Settings;
import org.jooq.conf.SettingsTools;
import org.jooq.impl.DefaultRenderContext.Rendered;
import org.jooq.tools.Convert;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.jdbc.DefaultPreparedStatement;
import org.jooq.tools.jdbc.DefaultResultSet;
import org.jooq.tools.jdbc.MockArray;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.r2dbc.spi.Batch;
import io.r2dbc.spi.ColumnMetadata;
import io.r2dbc.spi.Connection;
import io.r2dbc.spi.Result;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import io.r2dbc.spi.Statement;

/**
 * A single namespace for all reactive {@link Subscription} and other implementations.
 */
final class R2DBC {

    private static final JooqLogger log    = JooqLogger.getLogger(R2DBC.class);
    static volatile boolean         is_0_9 = true;

    static final class Forwarding<T> implements Subscriber<T> {

        final AbstractSubscription<T> downstream;
        final AtomicLong              expected;

        Forwarding(AbstractSubscription<T> downstream, long expected) {
            this.downstream = downstream;
            this.expected = new AtomicLong(expected);
        }

        @Override
        public final void onSubscribe(Subscription s) {
            s.request(Long.MAX_VALUE);
        }

        @Override
        public final void onNext(T value) {
            expected.decrementAndGet();
            downstream.subscriber.onNext(value);
        }

        @Override
        public final void onError(Throwable t) {
            downstream.subscriber.onError(t);
        }

        @Override
        public final void onComplete() {
            if (expected.get() <= 0)
                downstream.cancel();
        }
    }

    static abstract class AbstractResultSubscriber<T> implements Subscriber<Result> {

        final AbstractSubscription<? super T> downstream;

        AbstractResultSubscriber(AbstractSubscription<? super T> downstream) {
            this.downstream = downstream;
        }

        @Override
        public final void onError(Throwable t) {
            downstream.subscriber.onError(t);
        }

        @Override
        public final void onComplete() {}
    }

    static final class RowCountSubscriber extends AbstractResultSubscriber<Integer> {

        final Forwarding<? super Integer> forwarding;

        RowCountSubscriber(AbstractSubscription<? super Integer> downstream, long expected) {
            super(downstream);

            this.forwarding = new Forwarding<>(downstream, expected);
        }

        @Override
        public void onSubscribe(Subscription s) {
            s.request(forwarding.expected.get());
        }

        @Override
        public void onNext(Result r) {
            r.getRowsUpdated().subscribe(forwarding);
        }
    }

    static final class ResultSubscriber<R extends Record, Q extends ResultQueryTrait<R>> extends AbstractResultSubscriber<R> {

        final Q query;

        ResultSubscriber(Q query, AbstractSubscription<? super R> downstream) {
            super(downstream);

            this.query = query;
        }

        @Override
        public final void onSubscribe(Subscription s) {
            s.request(Long.MAX_VALUE);
        }

        @SuppressWarnings("unchecked")
        @Override
        public final void onNext(Result r) {
            r.map((row, m) -> {
                try {
                    // TODO: Cache this getFields() call
                    Field<?>[] fields = query.getFields(new R2DBCResultSetMetaData(query.configuration(), m));

                    // TODO: This call is duplicated from CursorImpl and related classes.
                    // Refactor this call to make sure code is re-used, especially when
                    // ExecuteListener lifecycle management is implemented
                    RecordDelegate<AbstractRecord> delegate = Tools.newRecord(true, (Supplier<AbstractRecord>) recordFactory(query.getRecordType(), Tools.row0(fields)), query.configuration());

                    return (R) delegate.operate(record -> {

                        // TODO: What data to pass here?
                        DefaultBindingGetResultSetContext<?> ctx = new DefaultBindingGetResultSetContext<>(
                            query.configuration(),
                            query.configuration().data(),
                            new R2DBCResultSet(query.configuration(), row),
                            0
                        );

                        // TODO: Make sure all the embeddable records, and other types of nested records are supported
                        for (int i = 0; i < fields.length; i++) {
                            ctx.index(i + 1);
                            fields[i].getBinding().get((BindingGetResultSetContext) ctx);
                            record.values[i] = ctx.value();
                            record.originals[i] = ctx.value();
                        }

                        return record;
                    });
                }

                // TODO: More specific error handling
                catch (Throwable t) {
                    onError(t);
                    return null;
                }
            }).subscribe(new Forwarding<>(downstream, 1));
        }
    }

    static abstract class ConnectionSubscriber<T> implements Subscriber<Connection> {

        final AbstractSubscription<T>     downstream;
        final AtomicReference<Connection> connection;

        ConnectionSubscriber(AbstractSubscription<T> downstream) {
            this.downstream = downstream;
            this.connection = new AtomicReference<>();
        }

        @Override
        public final void onSubscribe(Subscription s) {
            s.request(1);
        }

        @Override
        public final void onNext(Connection c) {
            connection.set(c);
            onNext0(c);
        }

        abstract void onNext0(Connection c);

        @Override
        public final void onError(Throwable t) {
            downstream.subscriber.onError(t);
        }

        @Override
        public final void onComplete() {}
    }

    static final class QueryExecutionSubscriber<T, Q extends Query> extends ConnectionSubscriber<T> {

        final Q                                                          query;
        final BiFunction<Q, AbstractSubscription<T>, Subscriber<Result>> resultSubscriber;

        QueryExecutionSubscriber(
            Q query,
            QuerySubscription<T, Q> downstream,
            BiFunction<Q, AbstractSubscription<T>, Subscriber<Result>> resultSubscriber
        ) {
            super(downstream);

            this.query = query;
            this.resultSubscriber = resultSubscriber;
        }

        @Override
        final void onNext0(Connection c) {
            try {
                DefaultRenderContext render = new DefaultRenderContext(query.configuration().derive(
                    setParamType(query.configuration().dialect(), query.configuration().settings())
                ));

                Rendered rendered = new Rendered(render.paramType(NAMED).visit(query).render(), render.bindValues(), render.skipUpdateCounts());
                Statement stmt = c.createStatement(rendered.sql);
                new DefaultBindContext(query.configuration(), new R2DBCPreparedStatement(query.configuration(), stmt)).visit(rendered.bindValues);

                // TODO: Reuse org.jooq.impl.Tools.setFetchSize(ExecuteContext ctx, int fetchSize)
                if (query instanceof AbstractResultQuery) {
                    int f = SettingsTools.getFetchSize(((AbstractResultQuery<?>) query).fetchSize(), render.settings());
                    if (f != 0) {
                        if (log.isDebugEnabled())
                            log.debug("Setting fetch size", f);

                        stmt.fetchSize(f);
                    }
                }

                stmt.execute().subscribe(resultSubscriber.apply(query, downstream));
            }

            // TODO: More specific error handling
            catch (Throwable t) {
                onError(t);
            }
        }
    }

    static final class BatchMultipleSubscriber extends ConnectionSubscriber<Integer> {

        final BatchMultiple                                                                batch;
        final BiFunction<BatchMultiple, AbstractSubscription<Integer>, Subscriber<Result>> resultSubscriber;

        BatchMultipleSubscriber(
            BatchMultiple batch,
            BatchMultipleSubscription downstream,
            BiFunction<BatchMultiple, AbstractSubscription<Integer>, Subscriber<Result>> resultSubscriber
        ) {
            super(downstream);

            this.batch = batch;
            this.resultSubscriber = resultSubscriber;
        }

        @Override
        final void onNext0(Connection c) {
            try {
                Batch b = c.createBatch();

                for (int i = 0; i < batch.queries.length; i++)
                    b = b.add(DSL.using(batch.configuration).renderInlined(batch.queries[i]));

                b.execute().subscribe(resultSubscriber.apply(batch, downstream));
            }

            // TODO: More specific error handling
            catch (Throwable t) {
                onError(t);
            }
        }
    }

    static abstract class AbstractSubscription<T> implements Subscription {

        final Subscriber<? super T>           subscriber;
        final AtomicBoolean                   subscribed;
        final AtomicLong                      requested;
        final Publisher<? extends Connection> connection;

        AbstractSubscription(
            Configuration configuration,
            Subscriber<? super T> subscriber
        ) {
            this.subscriber = subscriber;
            this.subscribed = new AtomicBoolean();
            this.requested = new AtomicLong();
            this.connection = configuration.connectionFactory().create();
        }

        @Override
        public final void request(long n) {
            requested.getAndAdd(n);

            if (!subscribed.getAndSet(true))
                connection.subscribe(delegate());
        }

        @Override
        public final void cancel() {
            delegate().connection.updateAndGet(c -> {

                // close() calls on already closed resources have no effect, so
                // the side-effect is OK with the AtomicReference contract
                if (c != null)
                    c.close().subscribe(subscriber(s -> s.request(Long.MAX_VALUE), t -> {}, t -> {}, () -> {}));

                return null;
            });
            subscriber.onComplete();
        }

        abstract ConnectionSubscriber<T> delegate();
    }

    static final class QuerySubscription<T, Q extends Query> extends AbstractSubscription<T> {

        final QueryExecutionSubscriber<T, Q> queryExecutionSubscriber;

        QuerySubscription(
            Q query,
            Subscriber<? super T> subscriber,
            BiFunction<Q, AbstractSubscription<T>, Subscriber<Result>> resultSubscriber
        ) {
            super(query.configuration(), subscriber);

            this.queryExecutionSubscriber = new QueryExecutionSubscriber<>(query, this, resultSubscriber);
        }

        @Override
        final QueryExecutionSubscriber<T, Q> delegate() {
            return queryExecutionSubscriber;
        }
    }

    static final class BatchMultipleSubscription extends AbstractSubscription<Integer> {

        final BatchMultipleSubscriber batchMultipleSubscriber;

        BatchMultipleSubscription(
            BatchMultiple batch,
            Subscriber<? super Integer> subscriber,
            BiFunction<BatchMultiple, AbstractSubscription<Integer>, Subscriber<Result>> resultSubscriber
        ) {
            super(batch.configuration, subscriber);

            this.batchMultipleSubscriber = new BatchMultipleSubscriber(batch, this, resultSubscriber);
        }

        @Override
        final BatchMultipleSubscriber delegate() {
            return batchMultipleSubscriber;
        }
    }

    // -------------------------------------------------------------------------
    // JDBC to R2DBC bridges for better interop, where it doesn't matter
    // -------------------------------------------------------------------------

    static final class R2DBCPreparedStatement extends DefaultPreparedStatement {

        final Configuration c;
        final Statement     s;

        R2DBCPreparedStatement(Configuration c, Statement s) {
            super(null, null, () -> new SQLFeatureNotSupportedException("Unsupported operation of the JDBC to R2DBC bridge."));

            this.c = c;
            this.s = s;
        }

        private final void bindNonNull(int parameterIndex, Object x) {
            switch (c.family()) {







                default:
                    s.bind(parameterIndex - 1, x);
                    break;
            }
        }

        private final <T> void bindNull(int parameterIndex, Class<T> type) {
            switch (c.family()) {







                default:
                    s.bindNull(parameterIndex - 1, type);
                    break;
            }
        }

        private final <T> void bindNullable(int parameterIndex, T x, Class<T> type) {
            bindNullable(parameterIndex, x, type, t -> t);
        }

        private final <T, U> void bindNullable(int parameterIndex, T x, Class<U> type, Function<? super T, ? extends U> conversion) {
            if (x == null)
                bindNull(parameterIndex, type);
            else
                bindNonNull(parameterIndex, conversion.apply(x));
        }

        private final Class<?> type(int sqlType) {

            // [#11700] Intercept JDBC temporal types, which aren't supported by R2DBC
            switch (sqlType) {
                case Types.DATE:
                    return LocalDate.class;
                case Types.TIME:
                    return LocalTime.class;
                case Types.TIMESTAMP:
                    return LocalDateTime.class;
                default:
                    return DefaultDataType.getDataType(c.family(), sqlType).getType();
            }
        }

        @Override
        public final void setNull(int parameterIndex, int sqlType) throws SQLException {
            bindNull(parameterIndex, type(sqlType));
        }

        @Override
        public final void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
            bindNull(parameterIndex, type(sqlType));
        }

        public final void setNull(int parameterIndex, DataType<?> dataType) {
            bindNull(parameterIndex, dataType.getType());
        }

        @Override
        public final void setBoolean(int parameterIndex, boolean x) throws SQLException {
            bindNonNull(parameterIndex, x);
        }

        @Override
        public final void setByte(int parameterIndex, byte x) throws SQLException {
            bindNonNull(parameterIndex, x);
        }

        @Override
        public final void setShort(int parameterIndex, short x) throws SQLException {
            bindNonNull(parameterIndex, x);
        }

        @Override
        public final void setInt(int parameterIndex, int x) throws SQLException {
            bindNonNull(parameterIndex, x);
        }

        @Override
        public final void setLong(int parameterIndex, long x) throws SQLException {
            bindNonNull(parameterIndex, x);
        }

        @Override
        public final void setFloat(int parameterIndex, float x) throws SQLException {
            bindNonNull(parameterIndex, x);
        }

        @Override
        public final void setDouble(int parameterIndex, double x) throws SQLException {
            bindNonNull(parameterIndex, x);
        }

        @Override
        public final void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
            bindNullable(parameterIndex, x, BigDecimal.class);
        }

        @Override
        public final void setString(int parameterIndex, String x) throws SQLException {
            bindNullable(parameterIndex, x, String.class);
        }

        @Override
        public final void setNString(int parameterIndex, String value) throws SQLException {
            bindNullable(parameterIndex, value, String.class);
        }

        @Override
        public final void setBytes(int parameterIndex, byte[] x) throws SQLException {
            bindNullable(parameterIndex, x, byte[].class);
        }

        @Override
        public final void setDate(int parameterIndex, Date x) throws SQLException {
            bindNullable(parameterIndex, x, LocalDate.class, Date::toLocalDate);
        }

        @Override
        public final void setTime(int parameterIndex, Time x) throws SQLException {
            bindNullable(parameterIndex, x, LocalTime.class, Time::toLocalTime);
        }

        @Override
        public final void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
            bindNullable(parameterIndex, x, LocalDateTime.class, Timestamp::toLocalDateTime);
        }

        @SuppressWarnings("unchecked")
        @Override
        public final void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
            bindNullable(parameterIndex, x, (Class<Object>) type(targetSqlType));
        }

        @Override
        public final void setObject(int parameterIndex, Object x) throws SQLException {
            bindNullable(parameterIndex, x, Object.class);
        }

        @Override
        public final void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
            setObject(parameterIndex, x, targetSqlType);
        }

        @Override
        public final void setObject(int parameterIndex, Object x, SQLType targetSqlType, int scaleOrLength) throws SQLException {
            setObject(parameterIndex, x, defaultIfNull(targetSqlType.getVendorTypeNumber(), Types.OTHER));
        }

        @Override
        public final void setObject(int parameterIndex, Object x, SQLType targetSqlType) throws SQLException {
            setObject(parameterIndex, x, defaultIfNull(targetSqlType.getVendorTypeNumber(), Types.OTHER));
        }


















    }

    static final class R2DBCResultSet extends DefaultResultSet {

        final Configuration c;
        final Row           r;
        boolean             wasNull;

        R2DBCResultSet(Configuration c, Row r) {
            super(null, null, () -> new SQLFeatureNotSupportedException("Unsupported operation of the JDBC to R2DBC bridge."));

            this.c = c;
            this.r = r;
        }

        private final <T> T wasNull(T nullable) {
            wasNull = nullable == null;
            return nullable;
        }

        private final <T> T nullable(int columnIndex, Class<T> type) {
            return nullable(columnIndex, type, t -> t);
        }

        private final <T, U> U nullable(int columnIndex, Class<T> type, Function<? super T, ? extends U> conversion) {
            T t = wasNull(r.get(columnIndex - 1, type));
            return wasNull ? null : conversion.apply(t);
        }

        private final <U> U nullable(int columnIndex, Function<? super Object, ? extends U> conversion) {
            Object t = wasNull(r.get(columnIndex - 1));
            return wasNull ? null : conversion.apply(t);
        }

        private final <T> T nonNull(int columnIndex, Class<T> type, T nullValue) {
            T t = wasNull(r.get(columnIndex - 1, type));
            return wasNull ? nullValue : t;
        }

        @Override
        public final boolean wasNull() throws SQLException {
            return wasNull;
        }

        @Override
        public final boolean getBoolean(int columnIndex) throws SQLException {
            return nonNull(columnIndex, Boolean.class, false);
        }

        @Override
        public final byte getByte(int columnIndex) throws SQLException {
            return nonNull(columnIndex, Byte.class, (byte) 0);
        }

        @Override
        public final short getShort(int columnIndex) throws SQLException {
            return nonNull(columnIndex, Short.class, (short) 0);
        }

        @Override
        public final int getInt(int columnIndex) throws SQLException {
            return nonNull(columnIndex, Integer.class, 0);
        }

        @Override
        public final long getLong(int columnIndex) throws SQLException {
            return nonNull(columnIndex, Long.class, 0L);
        }

        @Override
        public final float getFloat(int columnIndex) throws SQLException {
            return nonNull(columnIndex, Float.class, 0.0f);
        }

        @Override
        public final double getDouble(int columnIndex) throws SQLException {
            return nonNull(columnIndex, Double.class, 0.0);
        }

        @Override
        public final BigDecimal getBigDecimal(int columnIndex) throws SQLException {
            return nullable(columnIndex, BigDecimal.class);
        }

        @Override
        public final String getString(int columnIndex) throws SQLException {
            return nullable(columnIndex, String.class);
        }

        @Override
        public final byte[] getBytes(int columnIndex) throws SQLException {







            return nullable(columnIndex, byte[].class);
        }

        @Override
        public final Date getDate(int columnIndex) throws SQLException {
            return nullable(columnIndex, LocalDate.class, Date::valueOf);
        }

        @Override
        public final Time getTime(int columnIndex) throws SQLException {
            return nullable(columnIndex, LocalTime.class, Time::valueOf);
        }

        @Override
        public final Timestamp getTimestamp(int columnIndex) throws SQLException {
            return nullable(columnIndex, LocalDateTime.class, Timestamp::valueOf);
        }

        @Override
        public final Object getObject(int columnIndex) throws SQLException {
            return getObject(columnIndex, Object.class);
        }

        @Override
        public final <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
            return nullable(columnIndex, type);
        }

        @Override
        public final Array getArray(int columnIndex) throws SQLException {
            return new MockArray<>(c.dialect(), (Object[]) nullable(columnIndex, Object.class), Object[].class);
        }
    }

    static final class R2DBCResultSetMetaData implements ResultSetMetaData {

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
        public final <T> T unwrap(Class<T> iface) throws SQLException {
            throw new SQLFeatureNotSupportedException("R2DBC can't unwrap JDBC types");
        }

        @Override
        public final boolean isWrapperFor(Class<?> iface) throws SQLException {
            return false;
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

    static final Settings setParamType(SQLDialect dialect, Settings settings) {
        settings = SettingsTools.clone(settings);

        switch (dialect.family()) {
            case MARIADB:
            case MYSQL:

                return settings.withParamType(NAMED);









            default:
                return settings
                    .withParamType(NAMED)
                    .withRenderNamedParamPrefix("$")
                    .withParseNamedParamPrefix("$");
        }
    }

    // -------------------------------------------------------------------------
    // XXX: Legacy implementation
    // -------------------------------------------------------------------------

    static final class BlockingRecordSubscription<R extends Record> implements Subscription {
        private final ResultQueryTrait<R>  query;
        private final Subscriber<? super R> subscriber;
        private Cursor<R>                   c;
        private ArrayDeque<R>               buffer;

        BlockingRecordSubscription(ResultQueryTrait<R> query, Subscriber<? super R> subscriber) {
            this.query = query;
            this.subscriber = subscriber;
        }

        @Override
        public final void request(long n) {
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
        public final void cancel() {
            close();
        }
    }

    static final class BlockingRowCountSubscription implements Subscription {
        final AbstractRowCountQuery       query;
        final Subscriber<? super Integer> subscriber;
        Integer                           rows;

        BlockingRowCountSubscription(AbstractRowCountQuery query, Subscriber<? super Integer> subscriber) {
            this.query = query;
            this.subscriber = subscriber;
        }

        @Override
        public void request(long n) {
            try {
                if (rows == null)
                    subscriber.onNext(rows = query.execute());
            }
            catch (Throwable t) {
                subscriber.onError(t);
            }

            subscriber.onComplete();
        }

        @Override
        public void cancel() {
        }
    }
}
