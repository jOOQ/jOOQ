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

import static java.util.Collections.singletonList;
import static org.jooq.impl.DSL.val;
import static org.jooq.impl.ParsingConnection.translate;
import static org.jooq.impl.SQLDataType.NVARCHAR;
import static org.jooq.impl.Tools.asInt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLType;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.jooq.Param;
import org.jooq.Source;

/**
 * @author Lukas Eder
 */
final class ParsingStatement implements CallableStatement {

    private final ParsingConnection                                                       connection;
    private final Statement                                                               statement;
    private final ThrowingFunction<List<List<Param<?>>>, PreparedStatement, SQLException> prepared;
    private final List<ThrowingConsumer<Statement, SQLException>>                         flags;
    private final List<Param<?>>                                                          binds;
    private final List<List<Param<?>>>                                                    batch;
    private PreparedStatement                                                             last;

    ParsingStatement(ParsingConnection connection, Statement statement) {
        this.connection = connection;
        this.statement = statement;
        this.prepared = null;
        this.flags = null;
        this.binds = null;
        this.batch = null;
    }

    ParsingStatement(ParsingConnection connection, ThrowingFunction<List<List<Param<?>>>, PreparedStatement, SQLException> prepared) {
        this.connection = connection;
        this.statement = null;
        this.prepared = prepared;
        this.flags = new ArrayList<>();
        this.binds = new ArrayList<>();
        this.batch = new ArrayList<>();
    }

    private final List<Param<?>> bindValues(int index) {
        int size = binds.size();
        int reserve = index - size;

        if (reserve > 0)
            binds.addAll(Collections.nCopies(reserve, null));

        return binds;
    }

    // -------------------------------------------------------------------------
    // XXX: Flags
    // -------------------------------------------------------------------------

    private final SQLException closed() {
        return new SQLException("Statement is closed or not yet prepared.");
    }

    private final Statement statement() throws SQLException {
        if (last != null)
            return last;
        else if (statement != null)
            return statement;
        else
            throw closed();
    }

    private final void setFlag(ThrowingConsumer<Statement, SQLException> set) throws SQLException {
        if (statement != null)
            set.accept(statement);
        else
            flags.add(set);
    }

    @Override
    public final void setPoolable(boolean poolable) throws SQLException {
        setFlag(s -> s.setPoolable(poolable));
    }

    @Override
    public final boolean isPoolable() throws SQLException {
        return statement().isPoolable();
    }

    @Override
    public final void setFetchDirection(int direction) throws SQLException {
        setFlag(s -> s.setFetchDirection(direction));
    }

    @Override
    public final int getFetchDirection() throws SQLException {
        return statement().getFetchDirection();
    }

    @Override
    public final void setFetchSize(int rows) throws SQLException {
        setFlag(s -> s.setFetchSize(rows));
    }

    @Override
    public final int getFetchSize() throws SQLException {
        return statement().getFetchSize();
    }

    @Override
    public final void setMaxFieldSize(int max) throws SQLException {
        setFlag(s -> s.setMaxFieldSize(max));
    }

    @Override
    public final int getMaxFieldSize() throws SQLException {
        return statement().getMaxFieldSize();
    }

    @Override
    public final void setMaxRows(int max) throws SQLException {
        setFlag(s -> s.setMaxRows(max));
    }

    @Override
    public final int getMaxRows() throws SQLException {
        return statement().getMaxRows();
    }

    @Override
    public final void setLargeMaxRows(long max) throws SQLException {
        setFlag(s -> s.setLargeMaxRows(max));
    }

    @Override
    public final long getLargeMaxRows() throws SQLException {
        return statement().getLargeMaxRows();
    }

    @Override
    public final void setQueryTimeout(int seconds) throws SQLException {
        setFlag(s -> s.setQueryTimeout(seconds));
    }

    @Override
    public final int getQueryTimeout() throws SQLException {
        return statement().getQueryTimeout();
    }

    @Override
    public final void setEscapeProcessing(boolean enable) throws SQLException {
        setFlag(s -> s.setEscapeProcessing(enable));
    }

    @Override
    public final void setCursorName(String name) throws SQLException {
        setFlag(s -> s.setCursorName(name));
    }

    @Override
    public final int getResultSetConcurrency() throws SQLException {
        return statement().getResultSetConcurrency();
    }

    @Override
    public final int getResultSetType() throws SQLException {
        return statement().getResultSetType();
    }

    @Override
    public final int getResultSetHoldability() throws SQLException {
        return statement().getResultSetHoldability();
    }

    // -------------------------------------------------------------------------
    // XXX: Static statement execution
    // -------------------------------------------------------------------------

    @Override
    public final ResultSet executeQuery(String sql) throws SQLException {
        return statement.executeQuery(translate(connection.configuration, sql).sql);
    }

    @Override
    public final int executeUpdate(String sql) throws SQLException {
        return statement.executeUpdate(translate(connection.configuration, sql).sql);
    }

    @Override
    public final int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        return statement.executeUpdate(translate(connection.configuration, sql).sql, autoGeneratedKeys);
    }

    @Override
    public final int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
        return statement.executeUpdate(translate(connection.configuration, sql).sql, columnIndexes);
    }

    @Override
    public final int executeUpdate(String sql, String[] columnNames) throws SQLException {
        return statement.executeUpdate(translate(connection.configuration, sql).sql, columnNames);
    }

    @Override
    public final boolean execute(String sql) throws SQLException {
        return statement.execute(translate(connection.configuration, sql).sql);
    }

    @Override
    public final boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        return statement.execute(translate(connection.configuration, sql).sql, autoGeneratedKeys);
    }

    @Override
    public final boolean execute(String sql, int[] columnIndexes) throws SQLException {
        return statement.execute(translate(connection.configuration, sql).sql, columnIndexes);
    }

    @Override
    public final boolean execute(String sql, String[] columnNames) throws SQLException {
        return statement.execute(translate(connection.configuration, sql).sql, columnNames);
    }

    @Override
    public final long executeLargeUpdate(String sql) throws SQLException {
        return statement.executeLargeUpdate(translate(connection.configuration, sql).sql);
    }

    @Override
    public final long executeLargeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        return statement.executeLargeUpdate(translate(connection.configuration, sql).sql, autoGeneratedKeys);
    }

    @Override
    public final long executeLargeUpdate(String sql, int[] columnIndexes) throws SQLException {
        return statement.executeLargeUpdate(translate(connection.configuration, sql).sql, columnIndexes);
    }

    @Override
    public final long executeLargeUpdate(String sql, String[] columnNames) throws SQLException {
        return statement.executeLargeUpdate(translate(connection.configuration, sql).sql, columnNames);
    }

    @Override
    public final void addBatch(String sql) throws SQLException {
        statement.addBatch(translate(connection.configuration, sql).sql);
    }

    @Override
    public final Connection getConnection() throws SQLException {
        return connection;
    }

    // -------------------------------------------------------------------------
    // XXX: Prepared statement execution
    // -------------------------------------------------------------------------

    private final PreparedStatement last() throws SQLException {
        if (last == null)
            throw new SQLException("No PreparedStatement is available yet");
        else
            return last;
    }

    private final PreparedStatement prepareAndBind() throws SQLException {
        last = prepared.apply(batch.isEmpty() ? singletonList(binds) : batch);

        for (ThrowingConsumer<Statement, SQLException> flag : flags)
            flag.accept(last);

        return last;
    }

    @Override
    public final ResultSet executeQuery() throws SQLException {
        return prepareAndBind().executeQuery();
    }

    @Override
    public final int executeUpdate() throws SQLException {
        return prepareAndBind().executeUpdate();
    }

    @Override
    public final boolean execute() throws SQLException {
        return prepareAndBind().execute();
    }

    @Override
    public final long executeLargeUpdate() throws SQLException {
        return prepareAndBind().executeLargeUpdate();
    }

    @Override
    public final void addBatch() throws SQLException {
        batch.add(new ArrayList<>(binds));
    }

    // -------------------------------------------------------------------------
    // XXX: Shared static and prepared statement execution
    // -------------------------------------------------------------------------

    @Override
    public final void clearBatch() throws SQLException {
        statement().clearBatch();

        if (batch != null)
            batch.clear();
    }

    @Override
    public final int[] executeBatch() throws SQLException {
        if (statement != null)
            return statement.executeBatch();
        else
            return prepareAndBind().executeBatch();
    }

    @Override
    public final long[] executeLargeBatch() throws SQLException {
        if (statement != null)
            return statement.executeLargeBatch();
        else
            return prepareAndBind().executeLargeBatch();
    }

    // -------------------------------------------------------------------------
    // XXX: Lifecycle management
    // -------------------------------------------------------------------------

    @Override
    public final void close() throws SQLException {
        if (last != null)
            last.close();
        else if (statement != null)
            statement.close();
    }

    @Override
    public final boolean isClosed() throws SQLException {
        if (last != null)
            return last.isClosed();
        else if (statement != null)
            return statement.isClosed();
        else
            return true;
    }

    @Override
    public final void cancel() throws SQLException {
        statement().cancel();
    }

    // -------------------------------------------------------------------------
    // XXX: Result streaming
    // -------------------------------------------------------------------------

    @Override
    public final ResultSet getResultSet() throws SQLException {
        return last().getResultSet();
    }

    @Override
    public final ResultSetMetaData getMetaData() throws SQLException {
        return last().getMetaData();
    }

    @Override
    public final int getUpdateCount() throws SQLException {
        return last().getUpdateCount();
    }

    @Override
    public final long getLargeUpdateCount() throws SQLException {
        return last().getLargeUpdateCount();
    }

    @Override
    public final boolean getMoreResults() throws SQLException {
        return last().getMoreResults();
    }

    @Override
    public final boolean getMoreResults(int current) throws SQLException {
        return last().getMoreResults(current);
    }

    @Override
    public final ResultSet getGeneratedKeys() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    // -------------------------------------------------------------------------
    // XXX: Indexed Variable binding for PreparedStatement
    // -------------------------------------------------------------------------

    @Override
    public final void clearParameters() throws SQLException {
        binds.clear();
    }

    private final void set(int parameterIndex, Supplier<Param<?>> supplier) {
        bindValues(parameterIndex).set(parameterIndex - 1, supplier.get());
    }

    @Override
    public final void setNull(int parameterIndex, int sqlType) throws SQLException {
        set(parameterIndex, () -> val(null, DefaultDataType.getDataType(connection.configuration.dialect(), sqlType)));
    }

    @Override
    public final void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
        set(parameterIndex, () -> val(null, DefaultDataType.getDataType(connection.configuration.dialect(), sqlType)));
    }

    @Override
    public final void setBoolean(int parameterIndex, boolean x) throws SQLException {
        set(parameterIndex, () -> val(x));
    }

    @Override
    public final void setByte(int parameterIndex, byte x) throws SQLException {
        set(parameterIndex, () -> val(x));
    }

    @Override
    public final void setShort(int parameterIndex, short x) throws SQLException {
        set(parameterIndex, () -> val(x));
    }

    @Override
    public final void setInt(int parameterIndex, int x) throws SQLException {
        set(parameterIndex, () -> val(x));
    }

    @Override
    public final void setLong(int parameterIndex, long x) throws SQLException {
        set(parameterIndex, () -> val(x));
    }

    @Override
    public final void setFloat(int parameterIndex, float x) throws SQLException {
        set(parameterIndex, () -> val(x));
    }

    @Override
    public final void setDouble(int parameterIndex, double x) throws SQLException {
        set(parameterIndex, () -> val(x));
    }

    @Override
    public final void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
        set(parameterIndex, () -> val(x));
    }

    @Override
    public final void setString(int parameterIndex, String x) throws SQLException {
        set(parameterIndex, () -> val(x));
    }

    @Override
    public final void setBytes(int parameterIndex, byte[] x) throws SQLException {
        set(parameterIndex, () -> val(x));
    }

    @Override
    public final void setDate(int parameterIndex, Date x) throws SQLException {
        set(parameterIndex, () -> val(x));
    }

    @Override
    public final void setTime(int parameterIndex, Time x) throws SQLException {
        set(parameterIndex, () -> val(x));
    }

    @Override
    public final void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
        set(parameterIndex, () -> val(x));
    }

    @Override
    public final void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
        set(parameterIndex, () -> val(x, DefaultDataType.getDataType(connection.configuration.dialect(), targetSqlType)));
    }

    @Override
    public final void setObject(int parameterIndex, Object x) throws SQLException {
        set(parameterIndex, () -> val(x));
    }

    @Override
    public final void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
        set(parameterIndex, () -> val(x, DefaultDataType.getDataType(connection.configuration.dialect(), targetSqlType)));
    }

    @Override
    public final void setObject(int parameterIndex, Object x, SQLType targetSqlType, int scaleOrLength) throws SQLException {
        set(parameterIndex, () -> val(x, DefaultDataType.getDataType(connection.configuration.dialect(), targetSqlType)));
    }

    @Override
    public final void setObject(int parameterIndex, Object x, SQLType targetSqlType) throws SQLException {
        set(parameterIndex, () -> val(x, DefaultDataType.getDataType(connection.configuration.dialect(), targetSqlType)));
    }

    @Override
    public final void setNString(int parameterIndex, String value) throws SQLException {
        set(parameterIndex, () -> val(value, NVARCHAR));
    }

    // -------------------------------------------------------------------------
    // XXX: [#12666] Streaming bind values are collected into String or byte[]
    //               More efficient approaches are currently not possible in
    //               jOOQ, which has no built in way to represent streaming bind
    //               values or ResultSet values.
    // -------------------------------------------------------------------------

    private static final byte[] readBytes(InputStream x, int length) {
        try {

            if (true)
                return x.readNBytes(length);


            // Legacy Java 8 implementation
            ByteArrayOutputStream out = new ByteArrayOutputStream(length);

            long total = 0;
            byte[] buffer = new byte[8192];
            int delta;
            while ((delta = x.read(buffer, 0, (int) Math.min(length - total, 8192L))) >= 0) {
                out.write(buffer, 0, delta);
                total += delta;
            }

            return out.toByteArray();
        }
        catch (IOException e) {
            throw new org.jooq.exception.IOException("Could not read source", e);
        }
    }

    @Override
    public final void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
        setString(parameterIndex, Source.of(x, Charset.forName("US-ASCII")).readString());
    }

    @Override
    public final void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
        setString(parameterIndex, Source.of(x, length, Charset.forName("US-ASCII")).readString());
    }

    @Override
    public final void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
        setAsciiStream(parameterIndex, x, asInt(length));
    }

    @Override
    public final void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
        setString(parameterIndex, Source.of(x, length, Charset.forName("UTF-8")).readString());
    }

    @Override
    public final void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
        setString(parameterIndex, Source.of(reader).readString());
    }

    @Override
    public final void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
        setString(parameterIndex, Source.of(reader, length).readString());
    }

    @Override
    public final void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
        setCharacterStream(parameterIndex, reader, asInt(length));
    }

    @Override
    public final void setClob(int parameterIndex, Reader reader) throws SQLException {
        setCharacterStream(parameterIndex, reader);
    }

    @Override
    public final void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
        setCharacterStream(parameterIndex, reader, length);
    }

    @Override
    public final void setClob(int parameterIndex, Clob x) throws SQLException {
        setCharacterStream(parameterIndex, x.getCharacterStream(), asInt(x.length()));
    }

    @Override
    public final void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
        setNString(parameterIndex, Source.of(value).readString());
    }

    @Override
    public final void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
        setNString(parameterIndex, Source.of(value, asInt(length)).readString());
    }

    @Override
    public final void setNClob(int parameterIndex, Reader reader) throws SQLException {
        setNCharacterStream(parameterIndex, reader);
    }

    @Override
    public final void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
        setNCharacterStream(parameterIndex, reader, length);
    }

    @Override
    public final void setNClob(int parameterIndex, NClob value) throws SQLException {
        setNClob(parameterIndex, value.getCharacterStream(), value.length());
    }

    @Override
    public final void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
        setBinaryStream(parameterIndex, x, Integer.MAX_VALUE);
    }

    @Override
    public final void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
        setBytes(parameterIndex, readBytes(x, length));
    }

    @Override
    public final void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
        setBinaryStream(parameterIndex, x, asInt(length));
    }

    @Override
    public final void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
        setBinaryStream(parameterIndex, inputStream);
    }

    @Override
    public final void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
        setBinaryStream(parameterIndex, inputStream, length);
    }

    @Override
    public final void setBlob(int parameterIndex, Blob x) throws SQLException {
        setBlob(parameterIndex, x.getBinaryStream(), x.length());
    }

    // -------------------------------------------------------------------------
    // XXX: Indexed Variable binding (TODO)
    // -------------------------------------------------------------------------

    @Override
    public final void setRef(int parameterIndex, Ref x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void setArray(int parameterIndex, Array x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void setURL(int parameterIndex, URL x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void setRowId(int parameterIndex, RowId x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    // -------------------------------------------------------------------------
    // XXX: Named Variable binding for CallableStatement
    // -------------------------------------------------------------------------

    // TODO: [#11512] What to do about these?

    @Override
    public final void setURL(String parameterName, URL val) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void setNull(String parameterName, int sqlType) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void setBoolean(String parameterName, boolean x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void setByte(String parameterName, byte x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void setShort(String parameterName, short x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void setInt(String parameterName, int x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void setLong(String parameterName, long x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void setFloat(String parameterName, float x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void setDouble(String parameterName, double x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void setBigDecimal(String parameterName, BigDecimal x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void setString(String parameterName, String x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void setBytes(String parameterName, byte[] x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void setDate(String parameterName, Date x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void setTime(String parameterName, Time x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void setTimestamp(String parameterName, Timestamp x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void setAsciiStream(String parameterName, InputStream x, int length) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void setBinaryStream(String parameterName, InputStream x, int length) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void setObject(String parameterName, Object x, int targetSqlType, int scale) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void setObject(String parameterName, Object x, int targetSqlType) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void setObject(String parameterName, Object x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void setCharacterStream(String parameterName, Reader reader, int length) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void setDate(String parameterName, Date x, Calendar cal) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void setTime(String parameterName, Time x, Calendar cal) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void setTimestamp(String parameterName, Timestamp x, Calendar cal) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void setNull(String parameterName, int sqlType, String typeName) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void setRowId(String parameterName, RowId x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void setNString(String parameterName, String value) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void setNCharacterStream(String parameterName, Reader value, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void setNClob(String parameterName, NClob value) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void setClob(String parameterName, Reader reader, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void setBlob(String parameterName, InputStream inputStream, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void setNClob(String parameterName, Reader reader, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void setSQLXML(String parameterName, SQLXML xmlObject) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void setBlob(String parameterName, Blob x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void setClob(String parameterName, Clob x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void setAsciiStream(String parameterName, InputStream x, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void setBinaryStream(String parameterName, InputStream x, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void setCharacterStream(String parameterName, Reader reader, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void setAsciiStream(String parameterName, InputStream x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void setBinaryStream(String parameterName, InputStream x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void setCharacterStream(String parameterName, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void setNCharacterStream(String parameterName, Reader value) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void setClob(String parameterName, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void setBlob(String parameterName, InputStream inputStream) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void setNClob(String parameterName, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void setObject(String parameterName, Object x, SQLType targetSqlType, int scaleOrLength) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void setObject(String parameterName, Object x, SQLType targetSqlType) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    // -------------------------------------------------------------------------
    // XXX: Other CallableStatement API
    // -------------------------------------------------------------------------

    // TODO: [#11512] What to do about these?


    @Override
    public final void registerOutParameter(int parameterIndex, int sqlType) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void registerOutParameter(int parameterIndex, int sqlType, int scale) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final boolean wasNull() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final String getString(int parameterIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final boolean getBoolean(int parameterIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final byte getByte(int parameterIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final short getShort(int parameterIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final int getInt(int parameterIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final long getLong(int parameterIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final float getFloat(int parameterIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final double getDouble(int parameterIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final BigDecimal getBigDecimal(int parameterIndex, int scale) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final byte[] getBytes(int parameterIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final Date getDate(int parameterIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final Time getTime(int parameterIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final Timestamp getTimestamp(int parameterIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final Object getObject(int parameterIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final BigDecimal getBigDecimal(int parameterIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final Object getObject(int parameterIndex, Map<String, Class<?>> map) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final Ref getRef(int parameterIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final Blob getBlob(int parameterIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final Clob getClob(int parameterIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final Array getArray(int parameterIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final Date getDate(int parameterIndex, Calendar cal) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final Time getTime(int parameterIndex, Calendar cal) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final Timestamp getTimestamp(int parameterIndex, Calendar cal) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void registerOutParameter(int parameterIndex, int sqlType, String typeName) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void registerOutParameter(String parameterName, int sqlType) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void registerOutParameter(String parameterName, int sqlType, int scale) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void registerOutParameter(String parameterName, int sqlType, String typeName) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final URL getURL(int parameterIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final String getString(String parameterName) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final boolean getBoolean(String parameterName) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final byte getByte(String parameterName) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final short getShort(String parameterName) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final int getInt(String parameterName) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final long getLong(String parameterName) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final float getFloat(String parameterName) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final double getDouble(String parameterName) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final byte[] getBytes(String parameterName) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final Date getDate(String parameterName) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final Time getTime(String parameterName) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final Timestamp getTimestamp(String parameterName) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final Object getObject(String parameterName) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final BigDecimal getBigDecimal(String parameterName) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final Object getObject(String parameterName, Map<String, Class<?>> map) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final Ref getRef(String parameterName) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final Blob getBlob(String parameterName) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final Clob getClob(String parameterName) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final Array getArray(String parameterName) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final Date getDate(String parameterName, Calendar cal) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final Time getTime(String parameterName, Calendar cal) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final Timestamp getTimestamp(String parameterName, Calendar cal) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final URL getURL(String parameterName) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final RowId getRowId(int parameterIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final RowId getRowId(String parameterName) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final NClob getNClob(int parameterIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final NClob getNClob(String parameterName) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final SQLXML getSQLXML(int parameterIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final SQLXML getSQLXML(String parameterName) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final String getNString(int parameterIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final String getNString(String parameterName) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final Reader getNCharacterStream(int parameterIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final Reader getNCharacterStream(String parameterName) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final Reader getCharacterStream(int parameterIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final Reader getCharacterStream(String parameterName) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final <T> T getObject(int parameterIndex, Class<T> type) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final <T> T getObject(String parameterName, Class<T> type) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void registerOutParameter(int parameterIndex, SQLType sqlType) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void registerOutParameter(int parameterIndex, SQLType sqlType, int scale) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void registerOutParameter(int parameterIndex, SQLType sqlType, String typeName) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void registerOutParameter(String parameterName, SQLType sqlType) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void registerOutParameter(String parameterName, SQLType sqlType, int scale) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void registerOutParameter(String parameterName, SQLType sqlType, String typeName) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    // -------------------------------------------------------------------------
    // XXX: TODO
    // -------------------------------------------------------------------------

    @Override
    public final ParameterMetaData getParameterMetaData() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final SQLWarning getWarnings() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void clearWarnings() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final void closeOnCompletion() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final boolean isCloseOnCompletion() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final <T> T unwrap(Class<T> iface) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public final boolean isWrapperFor(Class<?> iface) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }
}
