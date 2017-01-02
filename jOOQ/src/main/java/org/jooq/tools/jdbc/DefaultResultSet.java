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
 */
package org.jooq.tools.jdbc;

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
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

/**
 * A default JDBC ResultSet implementation delegating all JDBC 4.0 calls to an
 * internal delegate.
 *
 * @author Lukas Eder
 */
public class DefaultResultSet extends JDBC41ResultSet implements ResultSet {

    private final ResultSet delegate;
    private final Statement creator;

    public DefaultResultSet(ResultSet delegate) {
        this(delegate, null);
    }

    public DefaultResultSet(ResultSet delegate, Statement creator) {
        this.delegate = delegate;
        this.creator = creator;
    }

    public ResultSet getDelegate() {
        return delegate;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return getDelegate().unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return getDelegate().isWrapperFor(iface);
    }

    @Override
    public boolean next() throws SQLException {
        return getDelegate().next();
    }

    @Override
    public void close() throws SQLException {
        getDelegate().close();
    }

    @Override
    public boolean wasNull() throws SQLException {
        return getDelegate().wasNull();
    }

    @Override
    public String getString(int columnIndex) throws SQLException {
        return getDelegate().getString(columnIndex);
    }

    @Override
    public boolean getBoolean(int columnIndex) throws SQLException {
        return getDelegate().getBoolean(columnIndex);
    }

    @Override
    public byte getByte(int columnIndex) throws SQLException {
        return getDelegate().getByte(columnIndex);
    }

    @Override
    public short getShort(int columnIndex) throws SQLException {
        return getDelegate().getShort(columnIndex);
    }

    @Override
    public int getInt(int columnIndex) throws SQLException {
        return getDelegate().getInt(columnIndex);
    }

    @Override
    public long getLong(int columnIndex) throws SQLException {
        return getDelegate().getLong(columnIndex);
    }

    @Override
    public float getFloat(int columnIndex) throws SQLException {
        return getDelegate().getFloat(columnIndex);
    }

    @Override
    public double getDouble(int columnIndex) throws SQLException {
        return getDelegate().getDouble(columnIndex);
    }

    @Override
    @Deprecated
    public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
        return getDelegate().getBigDecimal(columnIndex, scale);
    }

    @Override
    public byte[] getBytes(int columnIndex) throws SQLException {
        return getDelegate().getBytes(columnIndex);
    }

    @Override
    public Date getDate(int columnIndex) throws SQLException {
        return getDelegate().getDate(columnIndex);
    }

    @Override
    public Time getTime(int columnIndex) throws SQLException {
        return getDelegate().getTime(columnIndex);
    }

    @Override
    public Timestamp getTimestamp(int columnIndex) throws SQLException {
        return getDelegate().getTimestamp(columnIndex);
    }

    @Override
    public InputStream getAsciiStream(int columnIndex) throws SQLException {
        return getDelegate().getAsciiStream(columnIndex);
    }

    @Override
    @Deprecated
    public InputStream getUnicodeStream(int columnIndex) throws SQLException {
        return getDelegate().getUnicodeStream(columnIndex);
    }

    @Override
    public InputStream getBinaryStream(int columnIndex) throws SQLException {
        return getDelegate().getBinaryStream(columnIndex);
    }

    @Override
    public String getString(String columnLabel) throws SQLException {
        return getDelegate().getString(columnLabel);
    }

    @Override
    public boolean getBoolean(String columnLabel) throws SQLException {
        return getDelegate().getBoolean(columnLabel);
    }

    @Override
    public byte getByte(String columnLabel) throws SQLException {
        return getDelegate().getByte(columnLabel);
    }

    @Override
    public short getShort(String columnLabel) throws SQLException {
        return getDelegate().getShort(columnLabel);
    }

    @Override
    public int getInt(String columnLabel) throws SQLException {
        return getDelegate().getInt(columnLabel);
    }

    @Override
    public long getLong(String columnLabel) throws SQLException {
        return getDelegate().getLong(columnLabel);
    }

    @Override
    public float getFloat(String columnLabel) throws SQLException {
        return getDelegate().getFloat(columnLabel);
    }

    @Override
    public double getDouble(String columnLabel) throws SQLException {
        return getDelegate().getDouble(columnLabel);
    }

    @Override
    @Deprecated
    public BigDecimal getBigDecimal(String columnLabel, int scale) throws SQLException {
        return getDelegate().getBigDecimal(columnLabel, scale);
    }

    @Override
    public byte[] getBytes(String columnLabel) throws SQLException {
        return getDelegate().getBytes(columnLabel);
    }

    @Override
    public Date getDate(String columnLabel) throws SQLException {
        return getDelegate().getDate(columnLabel);
    }

    @Override
    public Time getTime(String columnLabel) throws SQLException {
        return getDelegate().getTime(columnLabel);
    }

    @Override
    public Timestamp getTimestamp(String columnLabel) throws SQLException {
        return getDelegate().getTimestamp(columnLabel);
    }

    @Override
    public InputStream getAsciiStream(String columnLabel) throws SQLException {
        return getDelegate().getAsciiStream(columnLabel);
    }

    @Override
    @Deprecated
    public InputStream getUnicodeStream(String columnLabel) throws SQLException {
        return getDelegate().getUnicodeStream(columnLabel);
    }

    @Override
    public InputStream getBinaryStream(String columnLabel) throws SQLException {
        return getDelegate().getBinaryStream(columnLabel);
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return getDelegate().getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        getDelegate().clearWarnings();
    }

    @Override
    public String getCursorName() throws SQLException {
        return getDelegate().getCursorName();
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        return getDelegate().getMetaData();
    }

    @Override
    public Object getObject(int columnIndex) throws SQLException {
        return getDelegate().getObject(columnIndex);
    }

    @Override
    public Object getObject(String columnLabel) throws SQLException {
        return getDelegate().getObject(columnLabel);
    }

    @Override
    public int findColumn(String columnLabel) throws SQLException {
        return getDelegate().findColumn(columnLabel);
    }

    @Override
    public Reader getCharacterStream(int columnIndex) throws SQLException {
        return getDelegate().getCharacterStream(columnIndex);
    }

    @Override
    public Reader getCharacterStream(String columnLabel) throws SQLException {
        return getDelegate().getCharacterStream(columnLabel);
    }

    @Override
    public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
        return getDelegate().getBigDecimal(columnIndex);
    }

    @Override
    public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
        return getDelegate().getBigDecimal(columnLabel);
    }

    @Override
    public boolean isBeforeFirst() throws SQLException {
        return getDelegate().isBeforeFirst();
    }

    @Override
    public boolean isAfterLast() throws SQLException {
        return getDelegate().isAfterLast();
    }

    @Override
    public boolean isFirst() throws SQLException {
        return getDelegate().isFirst();
    }

    @Override
    public boolean isLast() throws SQLException {
        return getDelegate().isLast();
    }

    @Override
    public void beforeFirst() throws SQLException {
        getDelegate().beforeFirst();
    }

    @Override
    public void afterLast() throws SQLException {
        getDelegate().afterLast();
    }

    @Override
    public boolean first() throws SQLException {
        return getDelegate().first();
    }

    @Override
    public boolean last() throws SQLException {
        return getDelegate().last();
    }

    @Override
    public int getRow() throws SQLException {
        return getDelegate().getRow();
    }

    @Override
    public boolean absolute(int row) throws SQLException {
        return getDelegate().absolute(row);
    }

    @Override
    public boolean relative(int rows) throws SQLException {
        return getDelegate().relative(rows);
    }

    @Override
    public boolean previous() throws SQLException {
        return getDelegate().previous();
    }

    @Override
    public void setFetchDirection(int direction) throws SQLException {
        getDelegate().setFetchDirection(direction);
    }

    @Override
    public int getFetchDirection() throws SQLException {
        return getDelegate().getFetchDirection();
    }

    @Override
    public void setFetchSize(int rows) throws SQLException {
        getDelegate().setFetchSize(rows);
    }

    @Override
    public int getFetchSize() throws SQLException {
        return getDelegate().getFetchSize();
    }

    @Override
    public int getType() throws SQLException {
        return getDelegate().getType();
    }

    @Override
    public int getConcurrency() throws SQLException {
        return getDelegate().getConcurrency();
    }

    @Override
    public boolean rowUpdated() throws SQLException {
        return getDelegate().rowUpdated();
    }

    @Override
    public boolean rowInserted() throws SQLException {
        return getDelegate().rowInserted();
    }

    @Override
    public boolean rowDeleted() throws SQLException {
        return getDelegate().rowDeleted();
    }

    @Override
    public void updateNull(int columnIndex) throws SQLException {
        getDelegate().updateNull(columnIndex);
    }

    @Override
    public void updateBoolean(int columnIndex, boolean x) throws SQLException {
        getDelegate().updateBoolean(columnIndex, x);
    }

    @Override
    public void updateByte(int columnIndex, byte x) throws SQLException {
        getDelegate().updateByte(columnIndex, x);
    }

    @Override
    public void updateShort(int columnIndex, short x) throws SQLException {
        getDelegate().updateShort(columnIndex, x);
    }

    @Override
    public void updateInt(int columnIndex, int x) throws SQLException {
        getDelegate().updateInt(columnIndex, x);
    }

    @Override
    public void updateLong(int columnIndex, long x) throws SQLException {
        getDelegate().updateLong(columnIndex, x);
    }

    @Override
    public void updateFloat(int columnIndex, float x) throws SQLException {
        getDelegate().updateFloat(columnIndex, x);
    }

    @Override
    public void updateDouble(int columnIndex, double x) throws SQLException {
        getDelegate().updateDouble(columnIndex, x);
    }

    @Override
    public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
        getDelegate().updateBigDecimal(columnIndex, x);
    }

    @Override
    public void updateString(int columnIndex, String x) throws SQLException {
        getDelegate().updateString(columnIndex, x);
    }

    @Override
    public void updateBytes(int columnIndex, byte[] x) throws SQLException {
        getDelegate().updateBytes(columnIndex, x);
    }

    @Override
    public void updateDate(int columnIndex, Date x) throws SQLException {
        getDelegate().updateDate(columnIndex, x);
    }

    @Override
    public void updateTime(int columnIndex, Time x) throws SQLException {
        getDelegate().updateTime(columnIndex, x);
    }

    @Override
    public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
        getDelegate().updateTimestamp(columnIndex, x);
    }

    @Override
    public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
        getDelegate().updateAsciiStream(columnIndex, x, length);
    }

    @Override
    public void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
        getDelegate().updateBinaryStream(columnIndex, x, length);
    }

    @Override
    public void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
        getDelegate().updateCharacterStream(columnIndex, x, length);
    }

    @Override
    public void updateObject(int columnIndex, Object x, int scaleOrLength) throws SQLException {
        getDelegate().updateObject(columnIndex, x, scaleOrLength);
    }

    @Override
    public void updateObject(int columnIndex, Object x) throws SQLException {
        getDelegate().updateObject(columnIndex, x);
    }

    @Override
    public void updateNull(String columnLabel) throws SQLException {
        getDelegate().updateNull(columnLabel);
    }

    @Override
    public void updateBoolean(String columnLabel, boolean x) throws SQLException {
        getDelegate().updateBoolean(columnLabel, x);
    }

    @Override
    public void updateByte(String columnLabel, byte x) throws SQLException {
        getDelegate().updateByte(columnLabel, x);
    }

    @Override
    public void updateShort(String columnLabel, short x) throws SQLException {
        getDelegate().updateShort(columnLabel, x);
    }

    @Override
    public void updateInt(String columnLabel, int x) throws SQLException {
        getDelegate().updateInt(columnLabel, x);
    }

    @Override
    public void updateLong(String columnLabel, long x) throws SQLException {
        getDelegate().updateLong(columnLabel, x);
    }

    @Override
    public void updateFloat(String columnLabel, float x) throws SQLException {
        getDelegate().updateFloat(columnLabel, x);
    }

    @Override
    public void updateDouble(String columnLabel, double x) throws SQLException {
        getDelegate().updateDouble(columnLabel, x);
    }

    @Override
    public void updateBigDecimal(String columnLabel, BigDecimal x) throws SQLException {
        getDelegate().updateBigDecimal(columnLabel, x);
    }

    @Override
    public void updateString(String columnLabel, String x) throws SQLException {
        getDelegate().updateString(columnLabel, x);
    }

    @Override
    public void updateBytes(String columnLabel, byte[] x) throws SQLException {
        getDelegate().updateBytes(columnLabel, x);
    }

    @Override
    public void updateDate(String columnLabel, Date x) throws SQLException {
        getDelegate().updateDate(columnLabel, x);
    }

    @Override
    public void updateTime(String columnLabel, Time x) throws SQLException {
        getDelegate().updateTime(columnLabel, x);
    }

    @Override
    public void updateTimestamp(String columnLabel, Timestamp x) throws SQLException {
        getDelegate().updateTimestamp(columnLabel, x);
    }

    @Override
    public void updateAsciiStream(String columnLabel, InputStream x, int length) throws SQLException {
        getDelegate().updateAsciiStream(columnLabel, x, length);
    }

    @Override
    public void updateBinaryStream(String columnLabel, InputStream x, int length) throws SQLException {
        getDelegate().updateBinaryStream(columnLabel, x, length);
    }

    @Override
    public void updateCharacterStream(String columnLabel, Reader reader, int length) throws SQLException {
        getDelegate().updateCharacterStream(columnLabel, reader, length);
    }

    @Override
    public void updateObject(String columnLabel, Object x, int scaleOrLength) throws SQLException {
        getDelegate().updateObject(columnLabel, x, scaleOrLength);
    }

    @Override
    public void updateObject(String columnLabel, Object x) throws SQLException {
        getDelegate().updateObject(columnLabel, x);
    }

    @Override
    public void insertRow() throws SQLException {
        getDelegate().insertRow();
    }

    @Override
    public void updateRow() throws SQLException {
        getDelegate().updateRow();
    }

    @Override
    public void deleteRow() throws SQLException {
        getDelegate().deleteRow();
    }

    @Override
    public void refreshRow() throws SQLException {
        getDelegate().refreshRow();
    }

    @Override
    public void cancelRowUpdates() throws SQLException {
        getDelegate().cancelRowUpdates();
    }

    @Override
    public void moveToInsertRow() throws SQLException {
        getDelegate().moveToInsertRow();
    }

    @Override
    public void moveToCurrentRow() throws SQLException {
        getDelegate().moveToCurrentRow();
    }

    @Override
    public Statement getStatement() throws SQLException {
        return creator != null ? creator : getDelegate().getStatement();
    }

    @Override
    public Object getObject(int columnIndex, Map<String, Class<?>> map) throws SQLException {
        return getDelegate().getObject(columnIndex, map);
    }

    @Override
    public Ref getRef(int columnIndex) throws SQLException {
        return getDelegate().getRef(columnIndex);
    }

    @Override
    public Blob getBlob(int columnIndex) throws SQLException {
        return getDelegate().getBlob(columnIndex);
    }

    @Override
    public Clob getClob(int columnIndex) throws SQLException {
        return getDelegate().getClob(columnIndex);
    }

    @Override
    public Array getArray(int columnIndex) throws SQLException {
        return getDelegate().getArray(columnIndex);
    }

    @Override
    public Object getObject(String columnLabel, Map<String, Class<?>> map) throws SQLException {
        return getDelegate().getObject(columnLabel, map);
    }

    @Override
    public Ref getRef(String columnLabel) throws SQLException {
        return getDelegate().getRef(columnLabel);
    }

    @Override
    public Blob getBlob(String columnLabel) throws SQLException {
        return getDelegate().getBlob(columnLabel);
    }

    @Override
    public Clob getClob(String columnLabel) throws SQLException {
        return getDelegate().getClob(columnLabel);
    }

    @Override
    public Array getArray(String columnLabel) throws SQLException {
        return getDelegate().getArray(columnLabel);
    }

    @Override
    public Date getDate(int columnIndex, Calendar cal) throws SQLException {
        return getDelegate().getDate(columnIndex, cal);
    }

    @Override
    public Date getDate(String columnLabel, Calendar cal) throws SQLException {
        return getDelegate().getDate(columnLabel, cal);
    }

    @Override
    public Time getTime(int columnIndex, Calendar cal) throws SQLException {
        return getDelegate().getTime(columnIndex, cal);
    }

    @Override
    public Time getTime(String columnLabel, Calendar cal) throws SQLException {
        return getDelegate().getTime(columnLabel, cal);
    }

    @Override
    public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
        return getDelegate().getTimestamp(columnIndex, cal);
    }

    @Override
    public Timestamp getTimestamp(String columnLabel, Calendar cal) throws SQLException {
        return getDelegate().getTimestamp(columnLabel, cal);
    }

    @Override
    public URL getURL(int columnIndex) throws SQLException {
        return getDelegate().getURL(columnIndex);
    }

    @Override
    public URL getURL(String columnLabel) throws SQLException {
        return getDelegate().getURL(columnLabel);
    }

    @Override
    public void updateRef(int columnIndex, Ref x) throws SQLException {
        getDelegate().updateRef(columnIndex, x);
    }

    @Override
    public void updateRef(String columnLabel, Ref x) throws SQLException {
        getDelegate().updateRef(columnLabel, x);
    }

    @Override
    public void updateBlob(int columnIndex, Blob x) throws SQLException {
        getDelegate().updateBlob(columnIndex, x);
    }

    @Override
    public void updateBlob(String columnLabel, Blob x) throws SQLException {
        getDelegate().updateBlob(columnLabel, x);
    }

    @Override
    public void updateClob(int columnIndex, Clob x) throws SQLException {
        getDelegate().updateClob(columnIndex, x);
    }

    @Override
    public void updateClob(String columnLabel, Clob x) throws SQLException {
        getDelegate().updateClob(columnLabel, x);
    }

    @Override
    public void updateArray(int columnIndex, Array x) throws SQLException {
        getDelegate().updateArray(columnIndex, x);
    }

    @Override
    public void updateArray(String columnLabel, Array x) throws SQLException {
        getDelegate().updateArray(columnLabel, x);
    }

    @Override
    public RowId getRowId(int columnIndex) throws SQLException {
        return getDelegate().getRowId(columnIndex);
    }

    @Override
    public RowId getRowId(String columnLabel) throws SQLException {
        return getDelegate().getRowId(columnLabel);
    }

    @Override
    public void updateRowId(int columnIndex, RowId x) throws SQLException {
        getDelegate().updateRowId(columnIndex, x);
    }

    @Override
    public void updateRowId(String columnLabel, RowId x) throws SQLException {
        getDelegate().updateRowId(columnLabel, x);
    }

    @Override
    public int getHoldability() throws SQLException {
        return getDelegate().getHoldability();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return getDelegate().isClosed();
    }

    @Override
    public void updateNString(int columnIndex, String nString) throws SQLException {
        getDelegate().updateNString(columnIndex, nString);
    }

    @Override
    public void updateNString(String columnLabel, String nString) throws SQLException {
        getDelegate().updateNString(columnLabel, nString);
    }

    @Override
    public void updateNClob(int columnIndex, NClob nClob) throws SQLException {
        getDelegate().updateNClob(columnIndex, nClob);
    }

    @Override
    public void updateNClob(String columnLabel, NClob nClob) throws SQLException {
        getDelegate().updateNClob(columnLabel, nClob);
    }

    @Override
    public NClob getNClob(int columnIndex) throws SQLException {
        return getDelegate().getNClob(columnIndex);
    }

    @Override
    public NClob getNClob(String columnLabel) throws SQLException {
        return getDelegate().getNClob(columnLabel);
    }

    @Override
    public SQLXML getSQLXML(int columnIndex) throws SQLException {
        return getDelegate().getSQLXML(columnIndex);
    }

    @Override
    public SQLXML getSQLXML(String columnLabel) throws SQLException {
        return getDelegate().getSQLXML(columnLabel);
    }

    @Override
    public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {
        getDelegate().updateSQLXML(columnIndex, xmlObject);
    }

    @Override
    public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {
        getDelegate().updateSQLXML(columnLabel, xmlObject);
    }

    @Override
    public String getNString(int columnIndex) throws SQLException {
        return getDelegate().getNString(columnIndex);
    }

    @Override
    public String getNString(String columnLabel) throws SQLException {
        return getDelegate().getNString(columnLabel);
    }

    @Override
    public Reader getNCharacterStream(int columnIndex) throws SQLException {
        return getDelegate().getNCharacterStream(columnIndex);
    }

    @Override
    public Reader getNCharacterStream(String columnLabel) throws SQLException {
        return getDelegate().getNCharacterStream(columnLabel);
    }

    @Override
    public void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
        getDelegate().updateNCharacterStream(columnIndex, x, length);
    }

    @Override
    public void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
        getDelegate().updateNCharacterStream(columnLabel, reader, length);
    }

    @Override
    public void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException {
        getDelegate().updateAsciiStream(columnIndex, x, length);
    }

    @Override
    public void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException {
        getDelegate().updateBinaryStream(columnIndex, x, length);
    }

    @Override
    public void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
        getDelegate().updateCharacterStream(columnIndex, x, length);
    }

    @Override
    public void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException {
        getDelegate().updateAsciiStream(columnLabel, x, length);
    }

    @Override
    public void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException {
        getDelegate().updateBinaryStream(columnLabel, x, length);
    }

    @Override
    public void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
        getDelegate().updateCharacterStream(columnLabel, reader, length);
    }

    @Override
    public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException {
        getDelegate().updateBlob(columnIndex, inputStream, length);
    }

    @Override
    public void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException {
        getDelegate().updateBlob(columnLabel, inputStream, length);
    }

    @Override
    public void updateClob(int columnIndex, Reader reader, long length) throws SQLException {
        getDelegate().updateClob(columnIndex, reader, length);
    }

    @Override
    public void updateClob(String columnLabel, Reader reader, long length) throws SQLException {
        getDelegate().updateClob(columnLabel, reader, length);
    }

    @Override
    public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException {
        getDelegate().updateNClob(columnIndex, reader, length);
    }

    @Override
    public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException {
        getDelegate().updateNClob(columnLabel, reader, length);
    }

    @Override
    public void updateNCharacterStream(int columnIndex, Reader x) throws SQLException {
        getDelegate().updateNCharacterStream(columnIndex, x);
    }

    @Override
    public void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException {
        getDelegate().updateNCharacterStream(columnLabel, reader);
    }

    @Override
    public void updateAsciiStream(int columnIndex, InputStream x) throws SQLException {
        getDelegate().updateAsciiStream(columnIndex, x);
    }

    @Override
    public void updateBinaryStream(int columnIndex, InputStream x) throws SQLException {
        getDelegate().updateBinaryStream(columnIndex, x);
    }

    @Override
    public void updateCharacterStream(int columnIndex, Reader x) throws SQLException {
        getDelegate().updateCharacterStream(columnIndex, x);
    }

    @Override
    public void updateAsciiStream(String columnLabel, InputStream x) throws SQLException {
        getDelegate().updateAsciiStream(columnLabel, x);
    }

    @Override
    public void updateBinaryStream(String columnLabel, InputStream x) throws SQLException {
        getDelegate().updateBinaryStream(columnLabel, x);
    }

    @Override
    public void updateCharacterStream(String columnLabel, Reader reader) throws SQLException {
        getDelegate().updateCharacterStream(columnLabel, reader);
    }

    @Override
    public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {
        getDelegate().updateBlob(columnIndex, inputStream);
    }

    @Override
    public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException {
        getDelegate().updateBlob(columnLabel, inputStream);
    }

    @Override
    public void updateClob(int columnIndex, Reader reader) throws SQLException {
        getDelegate().updateClob(columnIndex, reader);
    }

    @Override
    public void updateClob(String columnLabel, Reader reader) throws SQLException {
        getDelegate().updateClob(columnLabel, reader);
    }

    @Override
    public void updateNClob(int columnIndex, Reader reader) throws SQLException {
        getDelegate().updateNClob(columnIndex, reader);
    }

    @Override
    public void updateNClob(String columnLabel, Reader reader) throws SQLException {
        getDelegate().updateNClob(columnLabel, reader);
    }
}
