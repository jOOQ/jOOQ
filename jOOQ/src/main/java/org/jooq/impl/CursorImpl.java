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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
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

import static java.lang.Boolean.TRUE;
import static java.util.Collections.emptyList;
// ...
import static org.jooq.impl.RowAsField.NO_NATIVE_SUPPORT;
import static org.jooq.impl.Tools.embeddedFields;
import static org.jooq.impl.Tools.embeddedRecordType;
import static org.jooq.impl.Tools.recordFactory;
import static org.jooq.impl.Tools.uncoerce;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_MULTISET_CONTENT;

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
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

import org.jooq.Attachable;
import org.jooq.BindingGetResultSetContext;
import org.jooq.ContextConverter;
import org.jooq.ExecuteContext;
import org.jooq.ExecuteListener;
import org.jooq.Field;
// ...
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.Table;
import org.jooq.exception.ControlFlowSignal;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.jdbc.JDBC41ResultSet;
import org.jooq.tools.jdbc.JDBCUtils;

/**
 * @author Lukas Eder
 */
final class CursorImpl<R extends Record> extends AbstractCursor<R> {

    private static final JooqLogger            log = JooqLogger.getLogger(CursorImpl.class);

    final ExecuteContext                       ctx;
    final ExecuteListener                      listener;
    private final boolean                      keepResultSet;
    private final boolean                      keepStatement;
    private final boolean                      autoclosing;
    private final int                          maxRows;
    private final Supplier<? extends R>        factory;

    private volatile transient CursorResultSet rs;
    private volatile transient Iterator<R>     iterator;

    // [#18893] A cursor may be accessed concurrently by blocking subscriptions.
    // The subscriptions must ensure mutex access
    private volatile boolean                   isClosed;
    private volatile int                       rows;

    @SuppressWarnings("unchecked")
    CursorImpl(ExecuteContext ctx, ExecuteListener listener, Field<?>[] fields, boolean keepStatement, boolean keepResultSet) {
        this(ctx, listener, fields, keepStatement, keepResultSet, null, (Class<? extends R>) RecordImplN.class, 0, true);
    }

    CursorImpl(ExecuteContext ctx, ExecuteListener listener, Field<?>[] fields, boolean keepStatement, boolean keepResultSet, Table<? extends R> table, Class<? extends R> type, int maxRows, boolean autoclosing) {
        super(ctx.configuration(), (AbstractRow<R>) Tools.row0(fields));

        this.ctx = ctx;
        this.listener = (listener != null ? listener : ExecuteListeners.getAndStart(ctx));
        this.factory = recordFactory(table, type, this.fields);
        this.keepStatement = keepStatement;
        this.keepResultSet = keepResultSet;
        this.rs = new CursorResultSet();
        this.maxRows = maxRows;
        this.autoclosing = autoclosing;
    }

    // -------------------------------------------------------------------------
    // XXX: Attachable API
    // -------------------------------------------------------------------------

    @Override
    final List<? extends Attachable> getAttachables() {
        return emptyList();
    }

    // -------------------------------------------------------------------------
    // XXX: Cursor API
    // -------------------------------------------------------------------------

    @Override
    public final Iterator<R> iterator() {
        if (iterator == null) {
            iterator = new CursorIterator();
            listener.fetchStart(ctx);
        }

        return iterator;
    }

    @Override
    public final Result<R> fetchNext(int number) {
        // [#1157] This invokes listener.fetchStart(ctx), which has to be called
        // Before listener.resultStart(ctx)
        iterator();
        ResultImpl<R> result = new ResultImpl<>(((DefaultExecuteContext) ctx).originalConfiguration(), fields);

        ctx.result(result);
        listener.resultStart(ctx);

        for (int i = 0; i < number && iterator().hasNext(); i++)
            result.addRecord(iterator().next());

        ctx.result(result);
        listener.resultEnd(ctx);

        return result;
    }

    @Override
    public final void close() {
        JDBCUtils.safeClose(rs);
        rs = null;
        isClosed = true;
    }

    @Override
    public final boolean isClosed() {
        return isClosed;
    }

    @Override
    public final ResultSet resultSet() {
        return rs;
    }

    /**
     * A wrapper for the underlying JDBC {@link ResultSet} and {@link Statement}
     */
    final class CursorResultSet extends JDBC41ResultSet implements ResultSet {

        // ---------------------------------------------------------------------
        // XXX: Wrapper methods
        // ---------------------------------------------------------------------

        @Override
        public final <T> T unwrap(Class<T> iface) throws SQLException {
            return ctx.resultSet().unwrap(iface);
        }

        @Override
        public final boolean isWrapperFor(Class<?> iface) throws SQLException {
            return ctx.resultSet().isWrapperFor(iface);
        }

        // ---------------------------------------------------------------------
        // XXX: Informational methods
        // ---------------------------------------------------------------------

        @Override
        public final Statement getStatement() throws SQLException {
            return ctx.resultSet().getStatement();
        }

        @Override
        public final SQLWarning getWarnings() throws SQLException {
            return ctx.resultSet().getWarnings();
        }

        @Override
        public final void clearWarnings() throws SQLException {
            ctx.resultSet().clearWarnings();
        }

        @Override
        public final String getCursorName() throws SQLException {
            return ctx.resultSet().getCursorName();
        }

        @Override
        public final ResultSetMetaData getMetaData() throws SQLException {
            return ctx.resultSet().getMetaData();
        }

        @Override
        public final int findColumn(String columnLabel) throws SQLException {
            return ctx.resultSet().findColumn(columnLabel);
        }

        @Override
        public final void setFetchDirection(int direction) throws SQLException {
            ctx.resultSet().setFetchDirection(direction);
        }

        @Override
        public final int getFetchDirection() throws SQLException {
            return ctx.resultSet().getFetchDirection();
        }

        @Override
        public final void setFetchSize(int rows) throws SQLException {
            ctx.resultSet().setFetchSize(rows);
        }

        @Override
        public final int getFetchSize() throws SQLException {
            return ctx.resultSet().getFetchSize();
        }

        @Override
        public final int getType() throws SQLException {
            return ctx.resultSet().getType();
        }

        @Override
        public final int getConcurrency() throws SQLException {
            return ctx.resultSet().getConcurrency();
        }

        @Override
        public final int getHoldability() throws SQLException {
            return ctx.resultSet().getHoldability();
        }

        // ---------------------------------------------------------------------
        // XXX: Navigational methods
        // ---------------------------------------------------------------------

        @Override
        public final boolean isBeforeFirst() throws SQLException {
            return ctx.resultSet().isBeforeFirst();
        }

        @Override
        public final boolean isAfterLast() throws SQLException {
            return ctx.resultSet().isAfterLast();
        }

        @Override
        public final boolean isFirst() throws SQLException {
            return ctx.resultSet().isFirst();
        }

        @Override
        public final boolean isLast() throws SQLException {
            return ctx.resultSet().isLast();
        }

        @Override
        public final boolean next() throws SQLException {
            return ctx.resultSet().next();
        }

        @Override
        public final boolean previous() throws SQLException {
            return ctx.resultSet().previous();
        }

        @Override
        public final void beforeFirst() throws SQLException {
            ctx.resultSet().beforeFirst();
        }

        @Override
        public final void afterLast() throws SQLException {
            ctx.resultSet().afterLast();
        }

        @Override
        public final boolean first() throws SQLException {
            return ctx.resultSet().first();
        }

        @Override
        public final boolean last() throws SQLException {
            return ctx.resultSet().last();
        }

        @Override
        public final int getRow() throws SQLException {
            return ctx.resultSet().getRow();
        }

        @Override
        public final boolean absolute(int row) throws SQLException {
            return ctx.resultSet().absolute(row);
        }

        @Override
        public final boolean relative(int r) throws SQLException {
            return ctx.resultSet().relative(r);
        }

        @Override
        public final void moveToInsertRow() throws SQLException {
            ctx.resultSet().moveToInsertRow();
        }

        @Override
        public final void moveToCurrentRow() throws SQLException {
            ctx.resultSet().moveToCurrentRow();
        }

        @Override
        public final void close() throws SQLException {
            ctx.rows(rows);

            // [#12568] Make sure exceptions thrown in fetchEnd() don't produce
            //          connection leaks
            try {
                listener.fetchEnd(ctx);
            }

            // [#1868] If this Result / Cursor was "kept" through a lazy
            // execution, we must assure that the ExecuteListener lifecycle is
            // correctly terminated.
            finally {
                Tools.safeClose(listener, ctx, keepStatement, keepResultSet);
            }
        }

        @Override
        public final boolean isClosed() throws SQLException {
            return ctx.resultSet().isClosed();
        }

        // ---------------------------------------------------------------------
        // XXX: Data retrieval
        // ---------------------------------------------------------------------

        @Override
        public final boolean wasNull() throws SQLException {
            return ctx.resultSet().wasNull();
        }

        @Override
        public final Array getArray(int columnIndex) throws SQLException {
            return ctx.resultSet().getArray(columnIndex);
        }

        @Override
        public final Array getArray(String columnLabel) throws SQLException {
            return ctx.resultSet().getArray(columnLabel);
        }

        @Override
        public final InputStream getAsciiStream(int columnIndex) throws SQLException {
            return ctx.resultSet().getAsciiStream(columnIndex);
        }

        @Override
        public final InputStream getAsciiStream(String columnLabel) throws SQLException {
            return ctx.resultSet().getAsciiStream(columnLabel);
        }

        @Override
        public final BigDecimal getBigDecimal(int columnIndex) throws SQLException {
            return ctx.resultSet().getBigDecimal(columnIndex);
        }

        @Override
        @Deprecated
        public final BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
            return ctx.resultSet().getBigDecimal(columnIndex, scale);
        }

        @Override
        public final BigDecimal getBigDecimal(String columnLabel) throws SQLException {
            return ctx.resultSet().getBigDecimal(columnLabel);
        }

        @Override
        @Deprecated
        public final BigDecimal getBigDecimal(String columnLabel, int scale) throws SQLException {
            return ctx.resultSet().getBigDecimal(columnLabel, scale);
        }

        @Override
        public final InputStream getBinaryStream(int columnIndex) throws SQLException {
            return ctx.resultSet().getBinaryStream(columnIndex);
        }

        @Override
        public final InputStream getBinaryStream(String columnLabel) throws SQLException {
            return ctx.resultSet().getBinaryStream(columnLabel);
        }

        @Override
        public final Blob getBlob(int columnIndex) throws SQLException {
            return ctx.resultSet().getBlob(columnIndex);
        }

        @Override
        public final Blob getBlob(String columnLabel) throws SQLException {
            return ctx.resultSet().getBlob(columnLabel);
        }

        @Override
        public final boolean getBoolean(int columnIndex) throws SQLException {
            return ctx.resultSet().getBoolean(columnIndex);
        }

        @Override
        public final boolean getBoolean(String columnLabel) throws SQLException {
            return ctx.resultSet().getBoolean(columnLabel);
        }

        @Override
        public final byte getByte(int columnIndex) throws SQLException {
            return ctx.resultSet().getByte(columnIndex);
        }

        @Override
        public final byte getByte(String columnLabel) throws SQLException {
            return ctx.resultSet().getByte(columnLabel);
        }

        @Override
        public final byte[] getBytes(int columnIndex) throws SQLException {
            return ctx.resultSet().getBytes(columnIndex);
        }

        @Override
        public final byte[] getBytes(String columnLabel) throws SQLException {
            return ctx.resultSet().getBytes(columnLabel);
        }

        @Override
        public final Reader getCharacterStream(int columnIndex) throws SQLException {
            return ctx.resultSet().getCharacterStream(columnIndex);
        }

        @Override
        public final Reader getCharacterStream(String columnLabel) throws SQLException {
            return ctx.resultSet().getCharacterStream(columnLabel);
        }

        @Override
        public final Clob getClob(int columnIndex) throws SQLException {
            return ctx.resultSet().getClob(columnIndex);
        }

        @Override
        public final Clob getClob(String columnLabel) throws SQLException {
            return ctx.resultSet().getClob(columnLabel);
        }

        @Override
        public final Date getDate(int columnIndex) throws SQLException {
            return ctx.resultSet().getDate(columnIndex);
        }

        @Override
        public final Date getDate(int columnIndex, Calendar cal) throws SQLException {
            return ctx.resultSet().getDate(columnIndex, cal);
        }

        @Override
        public final Date getDate(String columnLabel) throws SQLException {
            return ctx.resultSet().getDate(columnLabel);
        }

        @Override
        public final Date getDate(String columnLabel, Calendar cal) throws SQLException {
            return ctx.resultSet().getDate(columnLabel, cal);
        }

        @Override
        public final double getDouble(int columnIndex) throws SQLException {
            return ctx.resultSet().getDouble(columnIndex);
        }

        @Override
        public final double getDouble(String columnLabel) throws SQLException {
            return ctx.resultSet().getDouble(columnLabel);
        }

        @Override
        public final float getFloat(int columnIndex) throws SQLException {
            return ctx.resultSet().getFloat(columnIndex);
        }

        @Override
        public final float getFloat(String columnLabel) throws SQLException {
            return ctx.resultSet().getFloat(columnLabel);
        }

        @Override
        public final int getInt(int columnIndex) throws SQLException {
            return ctx.resultSet().getInt(columnIndex);
        }

        @Override
        public final int getInt(String columnLabel) throws SQLException {
            return ctx.resultSet().getInt(columnLabel);
        }

        @Override
        public final long getLong(int columnIndex) throws SQLException {
            return ctx.resultSet().getLong(columnIndex);
        }

        @Override
        public final long getLong(String columnLabel) throws SQLException {
            return ctx.resultSet().getLong(columnLabel);
        }

        @Override
        public final Reader getNCharacterStream(int columnIndex) throws SQLException {
            return ctx.resultSet().getNCharacterStream(columnIndex);
        }

        @Override
        public final Reader getNCharacterStream(String columnLabel) throws SQLException {
            return ctx.resultSet().getNCharacterStream(columnLabel);
        }

        @Override
        public final NClob getNClob(int columnIndex) throws SQLException {
            return ctx.resultSet().getNClob(columnIndex);
        }

        @Override
        public final NClob getNClob(String columnLabel) throws SQLException {
            return ctx.resultSet().getNClob(columnLabel);
        }

        @Override
        public final String getNString(int columnIndex) throws SQLException {
            return ctx.resultSet().getNString(columnIndex);
        }

        @Override
        public final String getNString(String columnLabel) throws SQLException {
            return ctx.resultSet().getNString(columnLabel);
        }

        @Override
        public final Object getObject(int columnIndex) throws SQLException {
            return ctx.resultSet().getObject(columnIndex);
        }

        @Override
        public final Object getObject(int columnIndex, Map<String, Class<?>> map) throws SQLException {
            return ctx.resultSet().getObject(columnIndex, map);
        }

        @Override
        public final Object getObject(String columnLabel) throws SQLException {
            return ctx.resultSet().getObject(columnLabel);
        }

        @Override
        public final Object getObject(String columnLabel, Map<String, Class<?>> map) throws SQLException {
            return ctx.resultSet().getObject(columnLabel, map);
        }

        @Override
        public final Ref getRef(int columnIndex) throws SQLException {
            return ctx.resultSet().getRef(columnIndex);
        }

        @Override
        public final Ref getRef(String columnLabel) throws SQLException {
            return ctx.resultSet().getRef(columnLabel);
        }

        @Override
        public final RowId getRowId(int columnIndex) throws SQLException {
            return ctx.resultSet().getRowId(columnIndex);
        }

        @Override
        public final RowId getRowId(String columnLabel) throws SQLException {
            return ctx.resultSet().getRowId(columnLabel);
        }

        @Override
        public final short getShort(int columnIndex) throws SQLException {
            return ctx.resultSet().getShort(columnIndex);
        }

        @Override
        public final short getShort(String columnLabel) throws SQLException {
            return ctx.resultSet().getShort(columnLabel);
        }

        @Override
        public final SQLXML getSQLXML(int columnIndex) throws SQLException {
            return ctx.resultSet().getSQLXML(columnIndex);
        }

        @Override
        public final SQLXML getSQLXML(String columnLabel) throws SQLException {
            return ctx.resultSet().getSQLXML(columnLabel);
        }

        @Override
        public final String getString(int columnIndex) throws SQLException {
            return ctx.resultSet().getString(columnIndex);
        }

        @Override
        public final String getString(String columnLabel) throws SQLException {
            return ctx.resultSet().getString(columnLabel);
        }

        @Override
        public final Time getTime(int columnIndex) throws SQLException {
            return ctx.resultSet().getTime(columnIndex);
        }

        @Override
        public final Time getTime(int columnIndex, Calendar cal) throws SQLException {
            return ctx.resultSet().getTime(columnIndex, cal);
        }

        @Override
        public final Time getTime(String columnLabel) throws SQLException {
            return ctx.resultSet().getTime(columnLabel);
        }

        @Override
        public final Time getTime(String columnLabel, Calendar cal) throws SQLException {
            return ctx.resultSet().getTime(columnLabel, cal);
        }

        @Override
        public final Timestamp getTimestamp(int columnIndex) throws SQLException {
            return ctx.resultSet().getTimestamp(columnIndex);
        }

        @Override
        public final Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
            return ctx.resultSet().getTimestamp(columnIndex, cal);
        }

        @Override
        public final Timestamp getTimestamp(String columnLabel) throws SQLException {
            return ctx.resultSet().getTimestamp(columnLabel);
        }

        @Override
        public final Timestamp getTimestamp(String columnLabel, Calendar cal) throws SQLException {
            return ctx.resultSet().getTimestamp(columnLabel, cal);
        }

        @Override
        @Deprecated
        public final InputStream getUnicodeStream(int columnIndex) throws SQLException {
            return ctx.resultSet().getUnicodeStream(columnIndex);
        }

        @Override
        @Deprecated
        public final InputStream getUnicodeStream(String columnLabel) throws SQLException {
            return ctx.resultSet().getUnicodeStream(columnLabel);
        }

        @Override
        public final URL getURL(int columnIndex) throws SQLException {
            return ctx.resultSet().getURL(columnIndex);
        }

        @Override
        public final URL getURL(String columnLabel) throws SQLException {
            return ctx.resultSet().getURL(columnLabel);
        }

        // ---------------------------------------------------------------------
        // XXX: JDBC 4.1 methods
        // ---------------------------------------------------------------------

        @Override
        public final <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
            return ctx.resultSet().getObject(columnIndex, type);
        }

        @Override
        public final <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
            return ctx.resultSet().getObject(columnLabel, type);
        }

        // ---------------------------------------------------------------------
        // XXX: Data modification
        // ---------------------------------------------------------------------

        private final void logUpdate(int columnIndex, Object x) throws SQLException {
            if (log.isDebugEnabled()) {
                log.debug("Updating Result", "Updating Result position " + getRow() + ":" + columnIndex + " with value " + x);
            }
        }

        private final void logUpdate(String columnLabel, Object x) throws SQLException {
            if (log.isDebugEnabled()) {
                log.debug("Updating Result", "Updating Result position " + getRow() + ":" + columnLabel + " with value " + x);
            }
        }

        @Override
        public final boolean rowUpdated() throws SQLException {
            return ctx.resultSet().rowUpdated();
        }

        @Override
        public final boolean rowInserted() throws SQLException {
            return ctx.resultSet().rowInserted();
        }

        @Override
        public final boolean rowDeleted() throws SQLException {
            return ctx.resultSet().rowDeleted();
        }

        @Override
        public final void insertRow() throws SQLException {
            ctx.resultSet().insertRow();
        }

        @Override
        public final void updateRow() throws SQLException {
            ctx.resultSet().updateRow();
        }

        @Override
        public final void deleteRow() throws SQLException {
            ctx.resultSet().deleteRow();
        }

        @Override
        public final void refreshRow() throws SQLException {
            ctx.resultSet().refreshRow();
        }

        @Override
        public final void cancelRowUpdates() throws SQLException {
            ctx.resultSet().cancelRowUpdates();
        }

        @Override
        public final void updateNull(int columnIndex) throws SQLException {
            logUpdate(columnIndex, null);
            ctx.resultSet().updateNull(columnIndex);
        }

        @Override
        public final void updateNull(String columnLabel) throws SQLException {
            logUpdate(columnLabel, null);
            ctx.resultSet().updateNull(columnLabel);
        }

        @Override
        public final void updateArray(int columnIndex, Array x) throws SQLException {
            logUpdate(columnIndex, x);
            ctx.resultSet().updateArray(columnIndex, x);
        }

        @Override
        public final void updateArray(String columnLabel, Array x) throws SQLException {
            logUpdate(columnLabel, x);
            ctx.resultSet().updateArray(columnLabel, x);
        }

        @Override
        public final void updateAsciiStream(int columnIndex, InputStream x) throws SQLException {
            logUpdate(columnIndex, x);
            ctx.resultSet().updateAsciiStream(columnIndex, x);
        }

        @Override
        public final void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
            logUpdate(columnIndex, x);
            ctx.resultSet().updateAsciiStream(columnIndex, x, length);
        }

        @Override
        public final void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException {
            logUpdate(columnIndex, x);
            ctx.resultSet().updateAsciiStream(columnIndex, x, length);
        }

        @Override
        public final void updateAsciiStream(String columnLabel, InputStream x) throws SQLException {
            logUpdate(columnLabel, x);
            ctx.resultSet().updateAsciiStream(columnLabel, x);
        }

        @Override
        public final void updateAsciiStream(String columnLabel, InputStream x, int length) throws SQLException {
            logUpdate(columnLabel, x);
            ctx.resultSet().updateAsciiStream(columnLabel, x, length);
        }

        @Override
        public final void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException {
            logUpdate(columnLabel, x);
            ctx.resultSet().updateAsciiStream(columnLabel, x, length);
        }

        @Override
        public final void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
            logUpdate(columnIndex, x);
            ctx.resultSet().updateBigDecimal(columnIndex, x);
        }

        @Override
        public final void updateBigDecimal(String columnLabel, BigDecimal x) throws SQLException {
            logUpdate(columnLabel, x);
            ctx.resultSet().updateBigDecimal(columnLabel, x);
        }

        @Override
        public final void updateBinaryStream(int columnIndex, InputStream x) throws SQLException {
            logUpdate(columnIndex, x);
            ctx.resultSet().updateBinaryStream(columnIndex, x);
        }

        @Override
        public final void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
            logUpdate(columnIndex, x);
            ctx.resultSet().updateBinaryStream(columnIndex, x, length);
        }

        @Override
        public final void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException {
            logUpdate(columnIndex, x);
            ctx.resultSet().updateBinaryStream(columnIndex, x, length);
        }

        @Override
        public final void updateBinaryStream(String columnLabel, InputStream x) throws SQLException {
            logUpdate(columnLabel, x);
            ctx.resultSet().updateBinaryStream(columnLabel, x);
        }

        @Override
        public final void updateBinaryStream(String columnLabel, InputStream x, int length) throws SQLException {
            logUpdate(columnLabel, x);
            ctx.resultSet().updateBinaryStream(columnLabel, x, length);
        }

        @Override
        public final void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException {
            logUpdate(columnLabel, x);
            ctx.resultSet().updateBinaryStream(columnLabel, x, length);
        }

        @Override
        public final void updateBlob(int columnIndex, Blob x) throws SQLException {
            logUpdate(columnIndex, x);
            ctx.resultSet().updateBlob(columnIndex, x);
        }

        @Override
        public final void updateBlob(int columnIndex, InputStream x, long length) throws SQLException {
            logUpdate(columnIndex, x);
            ctx.resultSet().updateBlob(columnIndex, x, length);
        }

        @Override
        public final void updateBlob(String columnLabel, Blob x) throws SQLException {
            logUpdate(columnLabel, x);
            ctx.resultSet().updateBlob(columnLabel, x);
        }

        @Override
        public final void updateBlob(int columnIndex, InputStream x) throws SQLException {
            logUpdate(columnIndex, x);
            ctx.resultSet().updateBlob(columnIndex, x);
        }

        @Override
        public final void updateBlob(String columnLabel, InputStream x) throws SQLException {
            logUpdate(columnLabel, x);
            ctx.resultSet().updateBlob(columnLabel, x);
        }

        @Override
        public final void updateBlob(String columnLabel, InputStream x, long length) throws SQLException {
            logUpdate(columnLabel, x);
            ctx.resultSet().updateBlob(columnLabel, x, length);
        }

        @Override
        public final void updateBoolean(int columnIndex, boolean x) throws SQLException {
            logUpdate(columnIndex, x);
            ctx.resultSet().updateBoolean(columnIndex, x);
        }

        @Override
        public final void updateBoolean(String columnLabel, boolean x) throws SQLException {
            logUpdate(columnLabel, x);
            ctx.resultSet().updateBoolean(columnLabel, x);
        }

        @Override
        public final void updateByte(int columnIndex, byte x) throws SQLException {
            logUpdate(columnIndex, x);
            ctx.resultSet().updateByte(columnIndex, x);
        }

        @Override
        public final void updateByte(String columnLabel, byte x) throws SQLException {
            logUpdate(columnLabel, x);
            ctx.resultSet().updateByte(columnLabel, x);
        }

        @Override
        public final void updateBytes(int columnIndex, byte[] x) throws SQLException {
            logUpdate(columnIndex, x);
            ctx.resultSet().updateBytes(columnIndex, x);
        }

        @Override
        public final void updateBytes(String columnLabel, byte[] x) throws SQLException {
            logUpdate(columnLabel, x);
            ctx.resultSet().updateBytes(columnLabel, x);
        }

        @Override
        public final void updateCharacterStream(int columnIndex, Reader x) throws SQLException {
            logUpdate(columnIndex, x);
            ctx.resultSet().updateCharacterStream(columnIndex, x);
        }

        @Override
        public final void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
            logUpdate(columnIndex, x);
            ctx.resultSet().updateCharacterStream(columnIndex, x, length);
        }

        @Override
        public final void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
            logUpdate(columnIndex, x);
            ctx.resultSet().updateCharacterStream(columnIndex, x, length);
        }

        @Override
        public final void updateCharacterStream(String columnLabel, Reader x) throws SQLException {
            logUpdate(columnLabel, x);
            ctx.resultSet().updateCharacterStream(columnLabel, x);
        }

        @Override
        public final void updateCharacterStream(String columnLabel, Reader x, int length) throws SQLException {
            logUpdate(columnLabel, x);
            ctx.resultSet().updateCharacterStream(columnLabel, x, length);
        }

        @Override
        public final void updateCharacterStream(String columnLabel, Reader x, long length) throws SQLException {
            logUpdate(columnLabel, x);
            ctx.resultSet().updateCharacterStream(columnLabel, x, length);
        }

        @Override
        public final void updateClob(int columnIndex, Clob x) throws SQLException {
            logUpdate(columnIndex, x);
            ctx.resultSet().updateClob(columnIndex, x);
        }

        @Override
        public final void updateClob(int columnIndex, Reader x) throws SQLException {
            logUpdate(columnIndex, x);
            ctx.resultSet().updateClob(columnIndex, x);
        }

        @Override
        public final void updateClob(int columnIndex, Reader x, long length) throws SQLException {
            logUpdate(columnIndex, x);
            ctx.resultSet().updateClob(columnIndex, x, length);
        }

        @Override
        public final void updateClob(String columnLabel, Clob x) throws SQLException {
            logUpdate(columnLabel, x);
            ctx.resultSet().updateClob(columnLabel, x);
        }

        @Override
        public final void updateClob(String columnLabel, Reader x) throws SQLException {
            logUpdate(columnLabel, x);
            ctx.resultSet().updateClob(columnLabel, x);
        }

        @Override
        public final void updateClob(String columnLabel, Reader x, long length) throws SQLException {
            logUpdate(columnLabel, x);
            ctx.resultSet().updateClob(columnLabel, x, length);
        }

        @Override
        public final void updateDate(int columnIndex, Date x) throws SQLException {
            logUpdate(columnIndex, x);
            ctx.resultSet().updateDate(columnIndex, x);
        }

        @Override
        public final void updateDate(String columnLabel, Date x) throws SQLException {
            logUpdate(columnLabel, x);
            ctx.resultSet().updateDate(columnLabel, x);
        }

        @Override
        public final void updateDouble(int columnIndex, double x) throws SQLException {
            logUpdate(columnIndex, x);
            ctx.resultSet().updateDouble(columnIndex, x);
        }

        @Override
        public final void updateDouble(String columnLabel, double x) throws SQLException {
            logUpdate(columnLabel, x);
            ctx.resultSet().updateDouble(columnLabel, x);
        }

        @Override
        public final void updateFloat(int columnIndex, float x) throws SQLException {
            logUpdate(columnIndex, x);
            ctx.resultSet().updateFloat(columnIndex, x);
        }

        @Override
        public final void updateFloat(String columnLabel, float x) throws SQLException {
            logUpdate(columnLabel, x);
            ctx.resultSet().updateFloat(columnLabel, x);
        }

        @Override
        public final void updateInt(int columnIndex, int x) throws SQLException {
            logUpdate(columnIndex, x);
            ctx.resultSet().updateInt(columnIndex, x);
        }

        @Override
        public final void updateInt(String columnLabel, int x) throws SQLException {
            logUpdate(columnLabel, x);
            ctx.resultSet().updateInt(columnLabel, x);
        }

        @Override
        public final void updateLong(int columnIndex, long x) throws SQLException {
            logUpdate(columnIndex, x);
            ctx.resultSet().updateLong(columnIndex, x);
        }

        @Override
        public final void updateLong(String columnLabel, long x) throws SQLException {
            logUpdate(columnLabel, x);
            ctx.resultSet().updateLong(columnLabel, x);
        }

        @Override
        public final void updateNCharacterStream(int columnIndex, Reader x) throws SQLException {
            logUpdate(columnIndex, x);
            ctx.resultSet().updateNCharacterStream(columnIndex, x);
        }

        @Override
        public final void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
            logUpdate(columnIndex, x);
            ctx.resultSet().updateNCharacterStream(columnIndex, x, length);
        }

        @Override
        public final void updateNCharacterStream(String columnLabel, Reader x) throws SQLException {
            logUpdate(columnLabel, x);
            ctx.resultSet().updateNCharacterStream(columnLabel, x);
        }

        @Override
        public final void updateNCharacterStream(String columnLabel, Reader x, long length) throws SQLException {
            logUpdate(columnLabel, x);
            ctx.resultSet().updateNCharacterStream(columnLabel, x, length);
        }

        @Override
        public final void updateNClob(int columnIndex, NClob x) throws SQLException {
            logUpdate(columnIndex, x);
            ctx.resultSet().updateNClob(columnIndex, x);
        }

        @Override
        public final void updateNClob(int columnIndex, Reader x, long length) throws SQLException {
            logUpdate(columnIndex, x);
            ctx.resultSet().updateNClob(columnIndex, x, length);
        }

        @Override
        public final void updateNClob(int columnIndex, Reader x) throws SQLException {
            logUpdate(columnIndex, x);
            ctx.resultSet().updateNClob(columnIndex, x);
        }

        @Override
        public final void updateNClob(String columnLabel, NClob x) throws SQLException {
            logUpdate(columnLabel, x);
            ctx.resultSet().updateNClob(columnLabel, x);
        }

        @Override
        public final void updateNClob(String columnLabel, Reader x, long length) throws SQLException {
            logUpdate(columnLabel, x);
            ctx.resultSet().updateNClob(columnLabel, x, length);
        }

        @Override
        public final void updateNClob(String columnLabel, Reader x) throws SQLException {
            logUpdate(columnLabel, x);
            ctx.resultSet().updateNClob(columnLabel, x);
        }

        @Override
        public final void updateNString(int columnIndex, String x) throws SQLException {
            logUpdate(columnIndex, x);
            ctx.resultSet().updateNString(columnIndex, x);
        }

        @Override
        public final void updateNString(String columnLabel, String x) throws SQLException {
            logUpdate(columnLabel, x);
            ctx.resultSet().updateNString(columnLabel, x);
        }

        @Override
        public final void updateObject(int columnIndex, Object x) throws SQLException {
            logUpdate(columnIndex, x);
            ctx.resultSet().updateObject(columnIndex, x);
        }

        @Override
        public final void updateObject(int columnIndex, Object x, int scaleOrLength) throws SQLException {
            logUpdate(columnIndex, x);
            ctx.resultSet().updateObject(columnIndex, x, scaleOrLength);
        }

        @Override
        public final void updateObject(String columnLabel, Object x) throws SQLException {
            logUpdate(columnLabel, x);
            ctx.resultSet().updateObject(columnLabel, x);
        }

        @Override
        public final void updateObject(String columnLabel, Object x, int scaleOrLength) throws SQLException {
            logUpdate(columnLabel, x);
            ctx.resultSet().updateObject(columnLabel, x, scaleOrLength);
        }

        @Override
        public final void updateRef(int columnIndex, Ref x) throws SQLException {
            logUpdate(columnIndex, x);
            ctx.resultSet().updateRef(columnIndex, x);
        }

        @Override
        public final void updateRef(String columnLabel, Ref x) throws SQLException {
            logUpdate(columnLabel, x);
            ctx.resultSet().updateRef(columnLabel, x);
        }

        @Override
        public final void updateRowId(int columnIndex, RowId x) throws SQLException {
            logUpdate(columnIndex, x);
            ctx.resultSet().updateRowId(columnIndex, x);
        }

        @Override
        public final void updateRowId(String columnLabel, RowId x) throws SQLException {
            logUpdate(columnLabel, x);
            ctx.resultSet().updateRowId(columnLabel, x);
        }

        @Override
        public final void updateShort(int columnIndex, short x) throws SQLException {
            logUpdate(columnIndex, x);
            ctx.resultSet().updateShort(columnIndex, x);
        }

        @Override
        public final void updateShort(String columnLabel, short x) throws SQLException {
            logUpdate(columnLabel, x);
            ctx.resultSet().updateShort(columnLabel, x);
        }

        @Override
        public final void updateSQLXML(int columnIndex, SQLXML x) throws SQLException {
            logUpdate(columnIndex, x);
            ctx.resultSet().updateSQLXML(columnIndex, x);
        }

        @Override
        public final void updateSQLXML(String columnLabel, SQLXML x) throws SQLException {
            logUpdate(columnLabel, x);
            ctx.resultSet().updateSQLXML(columnLabel, x);
        }

        @Override
        public final void updateString(int columnIndex, String x) throws SQLException {
            logUpdate(columnIndex, x);
            ctx.resultSet().updateString(columnIndex, x);
        }

        @Override
        public final void updateString(String columnLabel, String x) throws SQLException {
            logUpdate(columnLabel, x);
            ctx.resultSet().updateString(columnLabel, x);
        }

        @Override
        public final void updateTime(int columnIndex, Time x) throws SQLException {
            logUpdate(columnIndex, x);
            ctx.resultSet().updateTime(columnIndex, x);
        }

        @Override
        public final void updateTime(String columnLabel, Time x) throws SQLException {
            logUpdate(columnLabel, x);
            ctx.resultSet().updateTime(columnLabel, x);
        }

        @Override
        public final void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
            logUpdate(columnIndex, x);
            ctx.resultSet().updateTimestamp(columnIndex, x);
        }

        @Override
        public final void updateTimestamp(String columnLabel, Timestamp x) throws SQLException {
            logUpdate(columnLabel, x);
            ctx.resultSet().updateTimestamp(columnLabel, x);
        }

        // ------------------------------------------------------------------------
        // JDBC 4.2
        // ------------------------------------------------------------------------

        @Override
        public void updateObject(int columnIndex, Object x, SQLType targetSqlType, int scaleOrLength) throws SQLException {
            logUpdate(columnIndex, x);
            ctx.resultSet().updateObject(columnIndex, x, targetSqlType, scaleOrLength);
        }

        @Override
        public void updateObject(String columnLabel, Object x, SQLType targetSqlType, int scaleOrLength) throws SQLException {
            logUpdate(columnLabel, x);
            ctx.resultSet().updateObject(columnLabel, x, targetSqlType, scaleOrLength);
        }

        @Override
        public void updateObject(int columnIndex, Object x, SQLType targetSqlType) throws SQLException {
            logUpdate(columnIndex, x);
            ctx.resultSet().updateObject(columnIndex, x, targetSqlType);
        }

        @Override
        public void updateObject(String columnLabel, Object x, SQLType targetSqlType) throws SQLException {
            logUpdate(columnLabel, x);
            ctx.resultSet().updateObject(columnLabel, x, targetSqlType);
        }
    }

    /**
     * An iterator for records fetched by this cursor
     */
    final class CursorIterator implements Iterator<R> {

        /**
         * The (potentially) pre-fetched next record
         */
        private volatile R                           next;

        /**
         * Whether the underlying {@link ResultSet} has a next record. This
         * boolean has three states:
         * <ul>
         * <li>null: it's not known whether there is a next record</li>
         * <li>true: there is a next record, and it has been pre-fetched</li>
         * <li>false: there aren't any next records</li>
         * </ul>
         */
        private volatile Boolean                     hasNext;

        /**
         * [#11099] Cache this instance for the entire cursor.
         */
        private final CursorRecordInitialiser        initialiser    = new CursorRecordInitialiser(
            ctx, listener,
            new DefaultBindingGetResultSetContext<>(ctx, rs, 0),
            fields, 0
        );

        @SuppressWarnings("unchecked")
        private final RecordDelegate<AbstractRecord> recordDelegate = Tools.newRecord(true, ((DefaultExecuteContext) ctx).originalConfiguration(), (Supplier<AbstractRecord>) factory);

        @Override
        public final boolean hasNext() {
            if (hasNext == null) {

                // Some databases (e.g. Redshift) do not implement JDBC's maxRows.
                if (maxRows > 0 && rows >= maxRows)
                    return false;

                next = fetchNext();
                hasNext = (next != null);
            }

            return hasNext;
        }

        @Override
        public final R next() {
            if (!hasNext())
                throw new NoSuchElementException("There are no more records to fetch from this Cursor");

            R result = next;
            hasNext = null;
            next = null;
            return result;
        }

        @SuppressWarnings("unchecked")
        private final R fetchNext() {
            AbstractRecord record = null;

            try {
                if (!isClosed && rs.next()) {
                    record = recordDelegate.operate(initialiser.reset());
                    rows++;
                }
            }

            // [#3427] ControlFlowSignals must not be passed on to ExecuteListners
            catch (ControlFlowSignal e) {
                throw e;
            }
            catch (RuntimeException e) {
                ctx.exception(e);
                listener.exception(ctx);
                throw ctx.exception();
            }
            catch (SQLException e) {
                ctx.sqlException(e);
                listener.exception(ctx);
                throw ctx.exception();
            }

            // [#1868] [#2373] [#2385] [#8544] This calls through to
            // Utils.safeClose() if necessary, lazy-terminating the ExecuteListener
            // lifecycle if the result is not eager-fetched.
            if (record == null && autoclosing) {
                CursorImpl.this.close();
            }

            return (R) record;
        }

        @Override
        public final void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * A utility to initialise records and transfer data in a
     * {@link RecordDelegate}.
     * <p>
     * While {@link CursorImpl} is strictly for blocking execution on JDBC, this
     * initialiser can also be used by the {@link R2DBC} implementation.
     */
    static class CursorRecordInitialiser implements ThrowingFunction<AbstractRecord, AbstractRecord, SQLException> {

        private final ExecuteContext                       ctx;
        private final ExecuteListener                      listener;
        private final AbstractRow<?>                       initialiserFields;
        private volatile int                               offset;







        private final DefaultBindingGetResultSetContext<?> rsContext;

        CursorRecordInitialiser(
            ExecuteContext ctx,
            ExecuteListener listener,
            DefaultBindingGetResultSetContext<?> rsContext,
            AbstractRow<?> initialiserFields,
            int offset
        ) {
















            this.ctx = ctx;
            this.listener = listener;
            this.rsContext = rsContext;
            this.initialiserFields = initialiserFields;
            this.offset = offset;





        }

        CursorRecordInitialiser reset() {
            offset = 0;
            return this;
        }

        @Override
        public AbstractRecord apply(AbstractRecord record) throws SQLException {
            ctx.record(record);
            listener.recordStart(ctx);
            int size = initialiserFields.size();




















            for (int i = 0; i < size; i++)
                setValue(record, initialiserFields.field(i), i);

            ctx.record(record);
            listener.recordEnd(ctx);

            return record;
        }

        /**
         * Utility method to prevent unnecessary unchecked conversions
         */
        @SuppressWarnings("unchecked")
        private final <T> void setValue(AbstractRecord record, Field<T> field, int index) throws SQLException {
            try {
                T value;
                AbstractRow<?> nested = null;
                Class<? extends AbstractRecord> recordType = null;

                // [#7100] TODO: This should be transparent to the CursorImpl
                //         RowField may have a Row[N].mapping(...) applied
                Field<?> f = uncoerce(field);

                // [#13560] Queries may decide themselves to replace the
                //          flattening emulation by the MULTISET emulation
                if (f instanceof AbstractRowAsField
                        && NO_NATIVE_SUPPORT.contains(ctx.dialect())
                        && !TRUE.equals(ctx.data(DATA_MULTISET_CONTENT))) {
                    nested = ((AbstractRowAsField<?>) f).emulatedFields(ctx.configuration());
                    recordType = (Class<? extends AbstractRecord>) ((AbstractRowAsField<?>) f).getRecordType();
                }
                else if (f.getDataType().isEmbeddable()) {
                    nested = Tools.row0(embeddedFields(f));
                    recordType = embeddedRecordType(f);
                }

                int nestedOffset = offset + index;
                if (nested != null) {
                    CursorRecordInitialiser operation = new CursorRecordInitialiser(
                        ctx, listener,
                        rsContext,
                        nested, nestedOffset





                    );
                    value = (T) Tools.newRecord(true, ((DefaultExecuteContext) ctx).originalConfiguration(), (Class<AbstractRecord>) recordType, (AbstractRow<AbstractRecord>) nested)
                                     .operate(operation);

                    // [#7100] TODO: Is there a more elegant way to do this?
                    if (f != field)
                        value = ((ContextConverter<Object, T>) field.getConverter()).from(value, ctx.converterContext());

                    offset += operation.offset - nestedOffset + nested.size() - 1;
                }
                else {
                    rsContext.index(nestedOffset + 1);
                    rsContext.field((Field) field);
                    field.getBinding().get((BindingGetResultSetContext<T>) rsContext);
                    value = (T) rsContext.value();
                }

                record.values[index] = value;
                record.originals[index] = value;
            }

            // [#5901] Improved error logging, mostly useful when there are some data type conversion errors
            catch (Exception e) {
                throw new SQLException("Error while reading field: " + field + ", at JDBC index: " + (offset + index + 1), e);
            }
        }





















































    }
}
