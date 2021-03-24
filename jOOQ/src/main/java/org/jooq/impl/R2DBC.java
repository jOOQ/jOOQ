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

import static org.jooq.conf.ParamType.NAMED;
import static org.jooq.impl.Tools.recordFactory;
import static org.jooq.tools.StringUtils.defaultIfNull;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLType;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.sql.Wrapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayDeque;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.function.Supplier;

import org.jooq.Configuration;
import org.jooq.Cursor;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.conf.SettingsTools;
import org.jooq.impl.DefaultRenderContext.Rendered;
import org.jooq.tools.jdbc.DefaultPreparedStatement;

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

        RowSubscriber(ResultSubscriber<R> s) {
            this.upstream = s;
        }

        @Override
        public final void onSubscribe(Subscription s) {
            this.subscription = s;
            subscription.request(upstream.upstream.upstream.requested.getAndSet(0L));
        }

        @Override
        public final void onNext(R record) {
            upstream.upstream.upstream.subscriber.onNext(record);

            long requested = upstream.upstream.upstream.requested.getAndSet(0L);
            if (requested > 0)
                subscription.request(requested);
        }

        @Override
        public final void onError(Throwable t) {
            upstream.onError(t);
        }

        @Override
        public final void onComplete() {
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
        public final void onSubscribe(Subscription s) {
            s.request(1);
        }

        @SuppressWarnings("unchecked")
        @Override
        public final void onNext(io.r2dbc.spi.Result r) {
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
        public final void onError(Throwable t) {
            upstream.onError(t);
        }

        @Override
        public final void onComplete() {}
    }

    static final class ConnectionSubscriber<R extends Record> implements Subscriber<Connection> {

        final AbstractResultQuery<R> query;
        final RecordSubscription<R>  upstream;

        ConnectionSubscriber(AbstractResultQuery<R> query, RecordSubscription<R> s) {
            this.query = query;
            this.upstream = s;
        }

        @Override
        public final void onSubscribe(Subscription s) {
            s.request(1);
        }

        @Override
        public final void onNext(Connection c) {
            try {
                DefaultRenderContext render = new DefaultRenderContext(query.configuration().derive(
                    SettingsTools.clone(query.configuration().settings()).withParamType(NAMED).withRenderNamedParamPrefix("$")
                ));

                Rendered r = new Rendered(render.paramType(NAMED).visit(query).render(), render.bindValues(), render.skipUpdateCounts());
                Statement stmt = c.createStatement(r.sql);
                new DefaultBindContext(query.configuration(), new R2DBCPreparedStatement(query.configuration(), stmt)).visit(r.bindValues);
//                ;
//                int i = 0;
//                for (Param<?> p : r.bindValues)
//                    if (p.getValue() == null)
//                        stmt.bindNull(i++, p.getType());
//                    else
//                        stmt.bind(i++, p.getValue());

                stmt.execute().subscribe(new ResultSubscriber<>(query, this));
            }

            // TODO: More specific error handling
            catch (Throwable t) {
                onError(t);
            }
        }

        @Override
        public final void onError(Throwable t) {
            upstream.subscriber.onError(t);
        }

        @Override
        public final void onComplete() {}
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
        public final void request(long n) {
            requested.getAndAdd(n);

            if (connection == null) {
                connection = query.configuration().connectionFactory().create();
                connection.subscribe(new ConnectionSubscriber<>(query, this));
            }
        }

        @Override
        public final void cancel() {
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

    static final class R2DBCPreparedStatement extends DefaultPreparedStatement {

        final Configuration c;
        final Statement     s;

        R2DBCPreparedStatement(Configuration c, Statement s) {

            // TODO: Refactor super class to throw a custom exception if trying to dereference this null pointer.
            super(null);

            this.c = c;
            this.s = s;
        }

        private final <T> void bindNull(int parameterIndex, Class<T> type) {
            s.bindNull(parameterIndex - 1, type);
        }

        private final <T> void bindNullable(int parameterIndex, T x, Class<T> type) {
            bindNullable(parameterIndex, x, type, t -> t);
        }

        private final <T, U> void bindNullable(int parameterIndex, T x, Class<U> type, Function<? super T, ? extends U> conversion) {
            if (x == null)
                s.bindNull(parameterIndex - 1, type);
            else
                bindNonNull(parameterIndex - 1, conversion.apply(x));
        }

        private final void bindNonNull(int parameterIndex, Object x) {
            s.bind(parameterIndex - 1, x);
        }

        private final Class<?> type(int sqlType) {
            return DefaultDataType.getDataType(c.family(), sqlType).getType();
        }

        @Override
        public final void setNull(int parameterIndex, int sqlType) throws SQLException {
            bindNull(parameterIndex, type(sqlType));
        }

        @Override
        public final void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
            bindNull(parameterIndex, type(sqlType));
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

        @Override
        public final void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
            throw new SQLFeatureNotSupportedException("The JDBC to R2DBC bridge doesn't support this data type");
        }

        @Override
        public final void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
            throw new SQLFeatureNotSupportedException("The JDBC to R2DBC bridge doesn't support this data type");
        }

        @Override
        public final void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
            throw new SQLFeatureNotSupportedException("The JDBC to R2DBC bridge doesn't support this data type");
        }

        @Override
        public final void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
            throw new SQLFeatureNotSupportedException("The JDBC to R2DBC bridge doesn't support this data type");
        }

        @Override
        public final void setRef(int parameterIndex, Ref x) throws SQLException {
            throw new SQLFeatureNotSupportedException("The JDBC to R2DBC bridge doesn't support this data type");
        }

        @Override
        public final void setBlob(int parameterIndex, Blob x) throws SQLException {
            throw new SQLFeatureNotSupportedException("The JDBC to R2DBC bridge doesn't support this data type");
        }

        @Override
        public final void setClob(int parameterIndex, Clob x) throws SQLException {
            throw new SQLFeatureNotSupportedException("The JDBC to R2DBC bridge doesn't support this data type");
        }

        @Override
        public final void setArray(int parameterIndex, Array x) throws SQLException {
            throw new SQLFeatureNotSupportedException("The JDBC to R2DBC bridge doesn't support this data type");
        }

        @Override
        public final void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
            throw new SQLFeatureNotSupportedException("The JDBC to R2DBC bridge doesn't support this data type");
        }

        @Override
        public final void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
            throw new SQLFeatureNotSupportedException("The JDBC to R2DBC bridge doesn't support this data type");
        }

        @Override
        public final void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
            throw new SQLFeatureNotSupportedException("The JDBC to R2DBC bridge doesn't support this data type");
        }

        @Override
        public final void setURL(int parameterIndex, URL x) throws SQLException {
            throw new SQLFeatureNotSupportedException("The JDBC to R2DBC bridge doesn't support this data type");
        }

        @Override
        public final void setRowId(int parameterIndex, RowId x) throws SQLException {
            throw new SQLFeatureNotSupportedException("The JDBC to R2DBC bridge doesn't support this data type");
        }

        @Override
        public final void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
            throw new SQLFeatureNotSupportedException("The JDBC to R2DBC bridge doesn't support this data type");
        }

        @Override
        public final void setNClob(int parameterIndex, NClob value) throws SQLException {
            throw new SQLFeatureNotSupportedException("The JDBC to R2DBC bridge doesn't support this data type");
        }

        @Override
        public final void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
            throw new SQLFeatureNotSupportedException("The JDBC to R2DBC bridge doesn't support this data type");
        }

        @Override
        public final void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
            throw new SQLFeatureNotSupportedException("The JDBC to R2DBC bridge doesn't support this data type");
        }

        @Override
        public final void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
            throw new SQLFeatureNotSupportedException("The JDBC to R2DBC bridge doesn't support this data type");
        }

        @Override
        public final void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
            throw new SQLFeatureNotSupportedException("The JDBC to R2DBC bridge doesn't support this data type");
        }

        @Override
        public final void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
            throw new SQLFeatureNotSupportedException("The JDBC to R2DBC bridge doesn't support this data type");
        }

        @Override
        public final void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
            throw new SQLFeatureNotSupportedException("The JDBC to R2DBC bridge doesn't support this data type");
        }

        @Override
        public final void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
            throw new SQLFeatureNotSupportedException("The JDBC to R2DBC bridge doesn't support this data type");
        }

        @Override
        public final void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
            throw new SQLFeatureNotSupportedException("The JDBC to R2DBC bridge doesn't support this data type");
        }

        @Override
        public final void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
            throw new SQLFeatureNotSupportedException("The JDBC to R2DBC bridge doesn't support this data type");
        }

        @Override
        public final void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
            throw new SQLFeatureNotSupportedException("The JDBC to R2DBC bridge doesn't support this data type");
        }

        @Override
        public final void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
            throw new SQLFeatureNotSupportedException("The JDBC to R2DBC bridge doesn't support this data type");
        }

        @Override
        public final void setClob(int parameterIndex, Reader reader) throws SQLException {
            throw new SQLFeatureNotSupportedException("The JDBC to R2DBC bridge doesn't support this data type");
        }

        @Override
        public final void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
            throw new SQLFeatureNotSupportedException("The JDBC to R2DBC bridge doesn't support this data type");
        }

        @Override
        public final void setNClob(int parameterIndex, Reader reader) throws SQLException {
            throw new SQLFeatureNotSupportedException("The JDBC to R2DBC bridge doesn't support this data type");
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
