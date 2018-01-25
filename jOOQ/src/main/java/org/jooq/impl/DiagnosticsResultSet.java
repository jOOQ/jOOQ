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
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Map;

import org.jooq.tools.jdbc.DefaultResultSet;

/**
 * @author Lukas Eder
 */
final class DiagnosticsResultSet extends DefaultResultSet {

    final DiagnosticsConnection connection;
    final String                sql;
    final ResultSetMetaData     meta;
    final BitSet                nullable;
    final BitSet                read;
    final int                   columns;
    int                         current;
    int                         rows;
    int                         wasColumnIndex;
    boolean                     wasPrimitive;
    boolean                     wasNullable;

    DiagnosticsResultSet(ResultSet delegate, String sql, Statement creator, DiagnosticsConnection connection) throws SQLException {
        super(delegate, creator);

        this.connection = connection;
        this.sql = sql;
        this.meta = delegate.getMetaData();
        this.columns = meta.getColumnCount();
        this.read = new BitSet(columns);
        this.nullable = new BitSet(columns);

        for (int i = 0; i < columns; i++)
            nullable.set(i, meta.isNullable(i + 1) == ResultSetMetaData.columnNullable);
    }

    // ------------------------------------------------------------------------
    // XXX Getter methods
    // ------------------------------------------------------------------------

    @Override
    public final String getString(int columnIndex) throws SQLException {
        checkPrimitive();
        read(columnIndex);
        return super.getString(columnIndex);
    }

    @Override
    public final boolean getBoolean(int columnIndex) throws SQLException {
        wasPrimitive(columnIndex);
        read(columnIndex);
        return super.getBoolean(columnIndex);
    }

    @Override
    public final byte getByte(int columnIndex) throws SQLException {
        wasPrimitive(columnIndex);
        read(columnIndex);
        return super.getByte(columnIndex);
    }

    @Override
    public final short getShort(int columnIndex) throws SQLException {
        wasPrimitive(columnIndex);
        read(columnIndex);
        return super.getShort(columnIndex);
    }

    @Override
    public final int getInt(int columnIndex) throws SQLException {
        wasPrimitive(columnIndex);
        read(columnIndex);
        return super.getInt(columnIndex);
    }

    @Override
    public final long getLong(int columnIndex) throws SQLException {
        wasPrimitive(columnIndex);
        read(columnIndex);
        return super.getLong(columnIndex);
    }

    @Override
    public final float getFloat(int columnIndex) throws SQLException {
        wasPrimitive(columnIndex);
        read(columnIndex);
        return super.getFloat(columnIndex);
    }

    @Override
    public final double getDouble(int columnIndex) throws SQLException {
        wasPrimitive(columnIndex);
        read(columnIndex);
        return super.getDouble(columnIndex);
    }

    @Override
    @Deprecated
    public final BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
        checkPrimitive();
        read(columnIndex);
        return super.getBigDecimal(columnIndex, scale);
    }

    @Override
    public final byte[] getBytes(int columnIndex) throws SQLException {
        checkPrimitive();
        read(columnIndex);
        return super.getBytes(columnIndex);
    }

    @Override
    public final Date getDate(int columnIndex) throws SQLException {
        checkPrimitive();
        read(columnIndex);
        return super.getDate(columnIndex);
    }

    @Override
    public final Time getTime(int columnIndex) throws SQLException {
        checkPrimitive();
        read(columnIndex);
        return super.getTime(columnIndex);
    }

    @Override
    public final Timestamp getTimestamp(int columnIndex) throws SQLException {
        checkPrimitive();
        read(columnIndex);
        return super.getTimestamp(columnIndex);
    }

    @Override
    public final InputStream getAsciiStream(int columnIndex) throws SQLException {
        checkPrimitive();
        read(columnIndex);
        return super.getAsciiStream(columnIndex);
    }

    @Override
    @Deprecated
    public final InputStream getUnicodeStream(int columnIndex) throws SQLException {
        checkPrimitive();
        read(columnIndex);
        return super.getUnicodeStream(columnIndex);
    }

    @Override
    public final InputStream getBinaryStream(int columnIndex) throws SQLException {
        checkPrimitive();
        read(columnIndex);
        return super.getBinaryStream(columnIndex);
    }

    @Override
    public final String getString(String columnLabel) throws SQLException {
        checkPrimitive();
        read(columnLabel);
        return super.getString(columnLabel);
    }

    @Override
    public final boolean getBoolean(String columnLabel) throws SQLException {
        wasPrimitive(columnLabel);
        read(columnLabel);
        return super.getBoolean(columnLabel);
    }

    @Override
    public final byte getByte(String columnLabel) throws SQLException {
        wasPrimitive(columnLabel);
        read(columnLabel);
        return super.getByte(columnLabel);
    }

    @Override
    public final short getShort(String columnLabel) throws SQLException {
        wasPrimitive(columnLabel);
        read(columnLabel);
        return super.getShort(columnLabel);
    }

    @Override
    public final int getInt(String columnLabel) throws SQLException {
        wasPrimitive(columnLabel);
        read(columnLabel);
        return super.getInt(columnLabel);
    }

    @Override
    public final long getLong(String columnLabel) throws SQLException {
        wasPrimitive(columnLabel);
        read(columnLabel);
        return super.getLong(columnLabel);
    }

    @Override
    public final float getFloat(String columnLabel) throws SQLException {
        wasPrimitive(columnLabel);
        read(columnLabel);
        return super.getFloat(columnLabel);
    }

    @Override
    public final double getDouble(String columnLabel) throws SQLException {
        wasPrimitive(columnLabel);
        read(columnLabel);
        return super.getDouble(columnLabel);
    }

    @Override
    @Deprecated
    public final BigDecimal getBigDecimal(String columnLabel, int scale) throws SQLException {
        checkPrimitive();
        read(columnLabel);
        return super.getBigDecimal(columnLabel, scale);
    }

    @Override
    public final byte[] getBytes(String columnLabel) throws SQLException {
        checkPrimitive();
        read(columnLabel);
        return super.getBytes(columnLabel);
    }

    @Override
    public final Date getDate(String columnLabel) throws SQLException {
        checkPrimitive();
        read(columnLabel);
        return super.getDate(columnLabel);
    }

    @Override
    public final Time getTime(String columnLabel) throws SQLException {
        checkPrimitive();
        read(columnLabel);
        return super.getTime(columnLabel);
    }

    @Override
    public final Timestamp getTimestamp(String columnLabel) throws SQLException {
        checkPrimitive();
        read(columnLabel);
        return super.getTimestamp(columnLabel);
    }

    @Override
    public final InputStream getAsciiStream(String columnLabel) throws SQLException {
        checkPrimitive();
        read(columnLabel);
        return super.getAsciiStream(columnLabel);
    }

    @Override
    @Deprecated
    public final InputStream getUnicodeStream(String columnLabel) throws SQLException {
        checkPrimitive();
        read(columnLabel);
        return super.getUnicodeStream(columnLabel);
    }

    @Override
    public final InputStream getBinaryStream(String columnLabel) throws SQLException {
        checkPrimitive();
        read(columnLabel);
        return super.getBinaryStream(columnLabel);
    }

    @Override
    public final Object getObject(int columnIndex) throws SQLException {
        checkPrimitive();
        read(columnIndex);
        return super.getObject(columnIndex);
    }

    @Override
    public final Object getObject(String columnLabel) throws SQLException {
        checkPrimitive();
        read(columnLabel);
        return super.getObject(columnLabel);
    }

    @Override
    public final Reader getCharacterStream(int columnIndex) throws SQLException {
        checkPrimitive();
        read(columnIndex);
        return super.getCharacterStream(columnIndex);
    }

    @Override
    public final Reader getCharacterStream(String columnLabel) throws SQLException {
        checkPrimitive();
        read(columnLabel);
        return super.getCharacterStream(columnLabel);
    }

    @Override
    public final BigDecimal getBigDecimal(int columnIndex) throws SQLException {
        checkPrimitive();
        read(columnIndex);
        return super.getBigDecimal(columnIndex);
    }

    @Override
    public final BigDecimal getBigDecimal(String columnLabel) throws SQLException {
        checkPrimitive();
        read(columnLabel);
        return super.getBigDecimal(columnLabel);
    }

    @Override
    public final Object getObject(int columnIndex, Map<String, Class<?>> map) throws SQLException {
        checkPrimitive();
        read(columnIndex);
        return super.getObject(columnIndex, map);
    }

    @Override
    public final Ref getRef(int columnIndex) throws SQLException {
        checkPrimitive();
        read(columnIndex);
        return super.getRef(columnIndex);
    }

    @Override
    public final Blob getBlob(int columnIndex) throws SQLException {
        checkPrimitive();
        read(columnIndex);
        return super.getBlob(columnIndex);
    }

    @Override
    public final Clob getClob(int columnIndex) throws SQLException {
        checkPrimitive();
        read(columnIndex);
        return super.getClob(columnIndex);
    }

    @Override
    public final Array getArray(int columnIndex) throws SQLException {
        checkPrimitive();
        read(columnIndex);
        return super.getArray(columnIndex);
    }

    @Override
    public final Object getObject(String columnLabel, Map<String, Class<?>> map) throws SQLException {
        checkPrimitive();
        read(columnLabel);
        return super.getObject(columnLabel, map);
    }

    @Override
    public final Ref getRef(String columnLabel) throws SQLException {
        checkPrimitive();
        read(columnLabel);
        return super.getRef(columnLabel);
    }

    @Override
    public final Blob getBlob(String columnLabel) throws SQLException {
        checkPrimitive();
        read(columnLabel);
        return super.getBlob(columnLabel);
    }

    @Override
    public final Clob getClob(String columnLabel) throws SQLException {
        checkPrimitive();
        read(columnLabel);
        return super.getClob(columnLabel);
    }

    @Override
    public final Array getArray(String columnLabel) throws SQLException {
        checkPrimitive();
        read(columnLabel);
        return super.getArray(columnLabel);
    }

    @Override
    public final Date getDate(int columnIndex, Calendar cal) throws SQLException {
        checkPrimitive();
        read(columnIndex);
        return super.getDate(columnIndex, cal);
    }

    @Override
    public final Date getDate(String columnLabel, Calendar cal) throws SQLException {
        checkPrimitive();
        read(columnLabel);
        return super.getDate(columnLabel, cal);
    }

    @Override
    public final Time getTime(int columnIndex, Calendar cal) throws SQLException {
        checkPrimitive();
        read(columnIndex);
        return super.getTime(columnIndex, cal);
    }

    @Override
    public final Time getTime(String columnLabel, Calendar cal) throws SQLException {
        checkPrimitive();
        read(columnLabel);
        return super.getTime(columnLabel, cal);
    }

    @Override
    public final Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
        checkPrimitive();
        read(columnIndex);
        return super.getTimestamp(columnIndex, cal);
    }

    @Override
    public final Timestamp getTimestamp(String columnLabel, Calendar cal) throws SQLException {
        checkPrimitive();
        read(columnLabel);
        return super.getTimestamp(columnLabel, cal);
    }

    @Override
    public final URL getURL(int columnIndex) throws SQLException {
        checkPrimitive();
        read(columnIndex);
        return super.getURL(columnIndex);
    }

    @Override
    public final URL getURL(String columnLabel) throws SQLException {
        checkPrimitive();
        read(columnLabel);
        return super.getURL(columnLabel);
    }

    @Override
    public final RowId getRowId(int columnIndex) throws SQLException {
        checkPrimitive();
        read(columnIndex);
        return super.getRowId(columnIndex);
    }

    @Override
    public final RowId getRowId(String columnLabel) throws SQLException {
        checkPrimitive();
        read(columnLabel);
        return super.getRowId(columnLabel);
    }

    @Override
    public final NClob getNClob(int columnIndex) throws SQLException {
        checkPrimitive();
        read(columnIndex);
        return super.getNClob(columnIndex);
    }

    @Override
    public final NClob getNClob(String columnLabel) throws SQLException {
        checkPrimitive();
        read(columnLabel);
        return super.getNClob(columnLabel);
    }

    @Override
    public final SQLXML getSQLXML(int columnIndex) throws SQLException {
        checkPrimitive();
        read(columnIndex);
        return super.getSQLXML(columnIndex);
    }

    @Override
    public final SQLXML getSQLXML(String columnLabel) throws SQLException {
        checkPrimitive();
        read(columnLabel);
        return super.getSQLXML(columnLabel);
    }

    @Override
    public final String getNString(int columnIndex) throws SQLException {
        checkPrimitive();
        read(columnIndex);
        return super.getNString(columnIndex);
    }

    @Override
    public final String getNString(String columnLabel) throws SQLException {
        checkPrimitive();
        read(columnLabel);
        return super.getNString(columnLabel);
    }

    @Override
    public final Reader getNCharacterStream(int columnIndex) throws SQLException {
        checkPrimitive();
        read(columnIndex);
        return super.getNCharacterStream(columnIndex);
    }

    @Override
    public final Reader getNCharacterStream(String columnLabel) throws SQLException {
        checkPrimitive();
        read(columnLabel);
        return super.getNCharacterStream(columnLabel);
    }

    @Override
    public final <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
        checkPrimitive();
        read(columnIndex);
        return super.getObject(columnIndex, type);
    }

    @Override
    public final <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
        checkPrimitive();
        read(columnLabel);
        return super.getObject(columnLabel, type);
    }

    @Override
    public final boolean wasNull() throws SQLException {
        if (!wasPrimitive) {
            DefaultDiagnosticsContext ctx = ctx();
            ctx.resultSetUnnecessaryWasNullCall = true;
            ctx.resultSetColumnIndex = wasColumnIndex;
            connection.listeners.unnecessaryWasNullCall(ctx);
        }

        wasPrimitive = false;
        wasNullable = false;

        return super.wasNull();
    }

    // ------------------------------------------------------------------------
    // XXX Utilities
    // ------------------------------------------------------------------------

    private final void wasPrimitive(int columnIndex) {
        checkPrimitive();

        wasColumnIndex = columnIndex;
        wasPrimitive = true;
        wasNullable = nullable.get(columnIndex - 1);
    }

    private final void wasPrimitive(String columnLabel) throws SQLException {
        wasPrimitive(super.findColumn(columnLabel));
    }

    private final void checkPrimitive() {
        if (wasPrimitive && wasNullable) {
            DefaultDiagnosticsContext ctx = ctx();
            ctx.resultSetMissingWasNullCall = true;
            ctx.resultSetColumnIndex = wasColumnIndex;
            connection.listeners.missingWasNullCall(ctx);
        }

        wasPrimitive = false;
        wasNullable = false;
    }

    private final void read(int columnIndex) {
        read.set(columnIndex - 1);
    }

    private final void read(String columnLabel) throws SQLException {
        read(super.findColumn(columnLabel));
    }

    // ------------------------------------------------------------------------
    // XXX Navigational methods
    // ------------------------------------------------------------------------

    @Override
    public final void beforeFirst() throws SQLException {
        checkPrimitive();
        super.beforeFirst();
        moveAbsolute(true, super.getRow());
    }

    @Override
    public final void afterLast() throws SQLException {
        checkPrimitive();
        super.afterLast();
        moveAbsolute(true, super.getRow());
    }

    @Override
    public final boolean first() throws SQLException {
        checkPrimitive();
        return moveAbsolute(super.first(), super.getRow());
    }

    @Override
    public final boolean last() throws SQLException {
        checkPrimitive();
        return moveAbsolute(super.last(), super.getRow());
    }

    @Override
    public final boolean absolute(int row) throws SQLException {
        checkPrimitive();
        return moveAbsolute(super.absolute(row), super.getRow());
    }

    @Override
    public final boolean relative(int relative) throws SQLException {
        checkPrimitive();
        return moveRelative(super.relative(relative), relative);
    }

    @Override
    public final boolean next() throws SQLException {
        checkPrimitive();
        return moveRelative(super.next(), 1);
    }

    @Override
    public final boolean previous() throws SQLException {
        checkPrimitive();
        return moveRelative(super.previous(), -1);
    }

    private final boolean moveRelative(boolean success, int relative) {
        if (success) {
            current = current + relative;
            rows = Math.max(rows, current);
        }

        return success;
    }

    private final boolean moveAbsolute(boolean success, int absolute) {
        if (success) {
            current = absolute;
            rows = Math.max(rows, current);
        }

        return success;
    }

    @Override
    public final void close() throws SQLException {
        checkPrimitive();

        try {
            if (current < rows)
                super.absolute(current = rows);

            DefaultDiagnosticsContext ctx = ctx();

            if (super.next())
                connection.listeners.tooManyRowsFetched(ctx);

            if (read.cardinality() != columns)
                connection.listeners.tooManyColumnsFetched(ctx);
        }
        catch (SQLException ignore) {}

        super.close();
    }

    private final DefaultDiagnosticsContext ctx() {
        DefaultDiagnosticsContext ctx = new DefaultDiagnosticsContext(sql);

        ctx.resultSet = super.getDelegate();
        ctx.resultSetFetchedColumns = read.cardinality();
        ctx.resultSetActualColumns = columns;
        ctx.resultSetFetchedRows = current;
        ctx.resultSetActualRows = current + 1;

        return ctx;
    }
}
