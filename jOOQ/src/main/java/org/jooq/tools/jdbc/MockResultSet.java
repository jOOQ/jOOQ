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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
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
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

import org.jooq.Converter;
import org.jooq.Converters;
import org.jooq.Field;
import org.jooq.Result;
import org.jooq.tools.Convert;

/**
 * A mock result set.
 *
 * @author Lukas Eder
 * @see MockConnection
 */
public class MockResultSet extends JDBC41ResultSet implements ResultSet, Serializable {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -2292216936424437750L;

    private final int         maxRows;
    Result<?>                 result;
    private transient int     index;
    private transient boolean wasNull;

    public MockResultSet(Result<?> result) {
        this(result, 0);
    }

    public MockResultSet(Result<?> result, int maxRows) {
        this.result = result;
        this.maxRows = maxRows;
    }

    // -------------------------------------------------------------------------
    // XXX: Unsupported implementations
    // -------------------------------------------------------------------------

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    // -------------------------------------------------------------------------
    // XXX: ResultSet operations
    // -------------------------------------------------------------------------

    private int size() {
        if (maxRows == 0)
            return result.size();
        else
            return Math.min(maxRows, result.size());
    }

    void checkNotClosed() throws SQLException {
        if (result == null)
            throw new SQLException("ResultSet is already closed");
    }

    private void checkInRange() throws SQLException {
        checkNotClosed();

        if (index <= 0 || index > result.size())
            throw new SQLException("ResultSet index is at an illegal position : " + index);
    }

    private Field<?> field(String columnLabel) throws SQLException {
        Field<?> field = result.field(columnLabel);

        if (field == null)
            throw new SQLException("Unknown column label : " + columnLabel);

        return field;
    }

    private Field<?> field(int columnIndex) throws SQLException {
        Field<?> field = result.field(columnIndex - 1);

        if (field == null)
            throw new SQLException("Unknown column index : " + columnIndex);

        return field;
    }

    @Override
    public boolean next() throws SQLException {
        return relative(1);
    }

    @Override
    public boolean previous() throws SQLException {
        return relative(-1);
    }

    @Override
    public boolean absolute(int row) throws SQLException {
        if (size() > 0) {
            if (row > 0) {
                if (row <= size()) {
                    index = row;
                    return true;
                }
                else {
                    afterLast();
                    return false;
                }
            }
            else if (row == 0) {
                beforeFirst();
                return false;
            }
            else {
                if (-row <= size()) {
                    index = size() + 1 + row;
                    return true;
                }
                else {
                    beforeFirst();
                    return false;
                }
            }
        }
        else {
            return false;
        }
    }

    @Override
    public boolean relative(int rows) throws SQLException {
        checkNotClosed();

        index += rows;
        try {
            return (index > 0 && index <= size());
        }

        // Be sure we don't go out of bounds
        finally {
            index = Math.max(index, 0);
            index = Math.min(index, size() + 1);
        }
    }

    @Override
    public int getRow() throws SQLException {
        return (index > size()) ? 0 : index;
    }

    @Override
    public void beforeFirst() throws SQLException {
        checkNotClosed();
        index = 0;
    }

    @Override
    public void afterLast() throws SQLException {
        checkNotClosed();
        index = size() + 1;
    }

    @Override
    public boolean first() throws SQLException {
        return absolute(1);
    }

    @Override
    public boolean last() throws SQLException {
        checkNotClosed();
        return absolute(size());
    }

    @Override
    public boolean isFirst() throws SQLException {
        checkNotClosed();
        return (size() > 0 && index == 1);
    }

    @Override
    public boolean isBeforeFirst() throws SQLException {
        checkNotClosed();
        return (size() > 0 && index == 0);
    }

    @Override
    public boolean isLast() throws SQLException {
        checkNotClosed();
        return (size() > 0 && index == size());
    }

    @Override
    public boolean isAfterLast() throws SQLException {
        checkNotClosed();
        return (size() > 0 && index > size());
    }

    @Override
    public void close() throws SQLException {
        checkNotClosed();

        result = null;
        index = 0;
    }

    @Override
    public boolean isClosed() throws SQLException {
        return result == null;
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        // Warnings are not supported
        return null;
    }

    @Override
    public void clearWarnings() throws SQLException {
        // Warnings are not supported
    }

    @Override
    public String getCursorName() throws SQLException {
        throw new SQLFeatureNotSupportedException("jOOQ ResultSets don't have a cursor name");
    }

    @Override
    public int findColumn(String columnLabel) throws SQLException {
        checkNotClosed();

        Field<?> field = result.field(columnLabel);
        if (field == null)
            throw new SQLException("No such column : " + columnLabel);

        return result.fieldsRow().indexOf(field) + 1;
    }

    @Override
    public void setFetchDirection(int direction) throws SQLException {

        // Fetch direction is not supported
        if (direction != ResultSet.FETCH_FORWARD)
            throw new SQLException("Fetch direction can only be FETCH_FORWARD");
    }

    @Override
    public int getFetchDirection() throws SQLException {

        // Fetch direction is not supported
        return ResultSet.FETCH_FORWARD;
    }

    @Override
    public void setFetchSize(int rows) throws SQLException {
        // Fetch size is not supported
    }

    @Override
    public int getFetchSize() throws SQLException {
        return 0;
    }

    @Override
    public int getType() throws SQLException {
        return ResultSet.TYPE_SCROLL_INSENSITIVE;
    }

    @Override
    public int getConcurrency() throws SQLException {
        return ResultSet.CONCUR_READ_ONLY;
    }

    @Override
    public int getHoldability() throws SQLException {
        return ResultSet.CLOSE_CURSORS_AT_COMMIT;
    }

    // -------------------------------------------------------------------------
    // XXX: Getters
    // -------------------------------------------------------------------------

    @Override
    public boolean wasNull() throws SQLException {
        checkNotClosed();

        return wasNull;
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        return new MockResultSetMetaData(this);
    }

    @Override
    public Statement getStatement() throws SQLException {
        return null;
    }

    private <T> T get(String columnLabel, Class<T> type) throws SQLException {
        checkInRange();

        Converter<?, ?> converter = Converters.inverse(field(columnLabel).getConverter());
        T value = Convert.convert(result.get(index - 1).get(columnLabel, converter), type);
        wasNull = (value == null);
        return value;
    }

    private <T> T get(int columnIndex, Class<T> type) throws SQLException {
        checkInRange();

        Converter<?, ?> converter = Converters.inverse(field(columnIndex).getConverter());
        T value = Convert.convert(result.get(index - 1).get(columnIndex - 1, converter), type);
        wasNull = (value == null);
        return value;
    }

    @Override
    public String getString(int columnIndex) throws SQLException {
        return get(columnIndex, String.class);
    }

    @Override
    public String getString(String columnLabel) throws SQLException {
        return get(columnLabel, String.class);
    }

    @Override
    public String getNString(int columnIndex) throws SQLException {
        return getString(columnIndex);
    }

    @Override
    public String getNString(String columnLabel) throws SQLException {
        return getString(columnLabel);
    }

    @Override
    public boolean getBoolean(int columnIndex) throws SQLException {
        Boolean value = get(columnIndex, Boolean.class);
        return wasNull ? false : value;
    }

    @Override
    public boolean getBoolean(String columnLabel) throws SQLException {
        Boolean value = get(columnLabel, Boolean.class);
        return wasNull ? false : value;
    }

    @Override
    public byte getByte(int columnIndex) throws SQLException {
        Byte value = get(columnIndex, Byte.class);
        return wasNull ? (byte) 0 : value;
    }

    @Override
    public byte getByte(String columnLabel) throws SQLException {
        Byte value = get(columnLabel, Byte.class);
        return wasNull ? (byte) 0 : value;
    }

    @Override
    public short getShort(int columnIndex) throws SQLException {
        Short value = get(columnIndex, Short.class);
        return wasNull ? (short) 0 : value;
    }

    @Override
    public short getShort(String columnLabel) throws SQLException {
        Short value = get(columnLabel, Short.class);
        return wasNull ? (short) 0 : value;
    }

    @Override
    public int getInt(int columnIndex) throws SQLException {
        Integer value = get(columnIndex, Integer.class);
        return wasNull ? 0 : value;
    }

    @Override
    public int getInt(String columnLabel) throws SQLException {
        Integer value = get(columnLabel, Integer.class);
        return wasNull ? 0 : value;
    }

    @Override
    public long getLong(int columnIndex) throws SQLException {
        Long value = get(columnIndex, Long.class);
        return wasNull ? 0L : value;
    }

    @Override
    public long getLong(String columnLabel) throws SQLException {
        Long value = get(columnLabel, Long.class);
        return wasNull ? 0L : value;
    }

    @Override
    public float getFloat(int columnIndex) throws SQLException {
        Float value = get(columnIndex, Float.class);
        return wasNull ? 0.0f : value;
    }

    @Override
    public float getFloat(String columnLabel) throws SQLException {
        Float value = get(columnLabel, Float.class);
        return wasNull ? 0.0f : value;
    }

    @Override
    public double getDouble(int columnIndex) throws SQLException {
        Double value = get(columnIndex, Double.class);
        return wasNull ? 0.0 : value;
    }

    @Override
    public double getDouble(String columnLabel) throws SQLException {
        Double value = get(columnLabel, Double.class);
        return wasNull ? 0.0 : value;
    }

    @Override
    public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
        return get(columnIndex, BigDecimal.class);
    }

    @Override
    @Deprecated
    public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
        return get(columnIndex, BigDecimal.class);
    }

    @Override
    public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
        return get(columnLabel, BigDecimal.class);
    }

    @Override
    @Deprecated
    public BigDecimal getBigDecimal(String columnLabel, int scale) throws SQLException {
        return get(columnLabel, BigDecimal.class);
    }

    @Override
    public byte[] getBytes(int columnIndex) throws SQLException {
        return get(columnIndex, byte[].class);
    }

    @Override
    public byte[] getBytes(String columnLabel) throws SQLException {
        return get(columnLabel, byte[].class);
    }

    @Override
    public Date getDate(int columnIndex) throws SQLException {
        return get(columnIndex, Date.class);
    }

    @Override
    public Date getDate(int columnIndex, Calendar cal) throws SQLException {
        return get(columnIndex, Date.class);
    }

    @Override
    public Date getDate(String columnLabel) throws SQLException {
        return get(columnLabel, Date.class);
    }

    @Override
    public Date getDate(String columnLabel, Calendar cal) throws SQLException {
        return get(columnLabel, Date.class);
    }

    @Override
    public Time getTime(int columnIndex) throws SQLException {
        return get(columnIndex, Time.class);
    }

    @Override
    public Time getTime(int columnIndex, Calendar cal) throws SQLException {
        return get(columnIndex, Time.class);
    }

    @Override
    public Time getTime(String columnLabel) throws SQLException {
        return get(columnLabel, Time.class);
    }

    @Override
    public Time getTime(String columnLabel, Calendar cal) throws SQLException {
        return get(columnLabel, Time.class);
    }

    @Override
    public Timestamp getTimestamp(int columnIndex) throws SQLException {
        return get(columnIndex, Timestamp.class);
    }

    @Override
    public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
        return get(columnIndex, Timestamp.class);
    }

    @Override
    public Timestamp getTimestamp(String columnLabel) throws SQLException {
        return get(columnLabel, Timestamp.class);
    }

    @Override
    public Timestamp getTimestamp(String columnLabel, Calendar cal) throws SQLException {
        return get(columnLabel, Timestamp.class);
    }

    @Override
    public InputStream getAsciiStream(int columnIndex) throws SQLException {
        byte[] bytes = getBytes(columnIndex);
        return wasNull ? null : new ByteArrayInputStream(bytes);
    }

    @Override
    public InputStream getAsciiStream(String columnLabel) throws SQLException {
        byte[] bytes = getBytes(columnLabel);
        return wasNull ? null : new ByteArrayInputStream(bytes);
    }

    @Override
    @Deprecated
    public InputStream getUnicodeStream(int columnIndex) throws SQLException {
        String string = getString(columnIndex);
        return wasNull ? null : new ByteArrayInputStream(string.getBytes());
    }

    @Override
    @Deprecated
    public InputStream getUnicodeStream(String columnLabel) throws SQLException {
        String string = getString(columnLabel);
        return wasNull ? null : new ByteArrayInputStream(string.getBytes());
    }

    @Override
    public Reader getCharacterStream(int columnIndex) throws SQLException {
        String string = getString(columnIndex);
        return wasNull ? null : new StringReader(string);
    }

    @Override
    public Reader getCharacterStream(String columnLabel) throws SQLException {
        String string = getString(columnLabel);
        return wasNull ? null : new StringReader(string);
    }

    @Override
    public Reader getNCharacterStream(int columnIndex) throws SQLException {
        return getCharacterStream(columnIndex);
    }

    @Override
    public Reader getNCharacterStream(String columnLabel) throws SQLException {
        return getCharacterStream(columnLabel);
    }

    @Override
    public InputStream getBinaryStream(int columnIndex) throws SQLException {
        byte[] bytes = getBytes(columnIndex);
        return wasNull ? null : new ByteArrayInputStream(bytes);
    }

    @Override
    public InputStream getBinaryStream(String columnLabel) throws SQLException {
        byte[] bytes = getBytes(columnLabel);
        return wasNull ? null : new ByteArrayInputStream(bytes);
    }

    @Override
    public Ref getRef(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException("Unsupported data type");
    }

    @Override
    public Ref getRef(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException("Unsupported data type");
    }

    @Override
    public RowId getRowId(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException("Unsupported data type");
    }

    @Override
    public RowId getRowId(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException("Unsupported data type");
    }

    @Override
    public SQLXML getSQLXML(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException("Unsupported data type");
    }

    @Override
    public SQLXML getSQLXML(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException("Unsupported data type");
    }

    @Override
    public Blob getBlob(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException("Unsupported data type. Use getBytes() instead");
    }

    @Override
    public Blob getBlob(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException("Unsupported data type. Use getBytes() instead");
    }

    @Override
    public Clob getClob(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException("Unsupported data type. Use getString() instead");
    }

    @Override
    public Clob getClob(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException("Unsupported data type. Use getString() instead");
    }

    @Override
    public NClob getNClob(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException("Unsupported data type. Use getString() instead");
    }

    @Override
    public NClob getNClob(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException("Unsupported data type. Use getString() instead");
    }

    @Override
    public Array getArray(int columnIndex) throws SQLException {
        return get(columnIndex, Array.class);
    }

    @Override
    public Array getArray(String columnLabel) throws SQLException {
        return get(columnLabel, Array.class);
    }

    @Override
    public URL getURL(int columnIndex) throws SQLException {
        return get(columnIndex, URL.class);
    }

    @Override
    public URL getURL(String columnLabel) throws SQLException {
        return get(columnLabel, URL.class);
    }

    @Override
    public Object getObject(int columnIndex) throws SQLException {
        return get(columnIndex, Object.class);
    }

    @Override
    public Object getObject(int columnIndex, Map<String, Class<?>> map) throws SQLException {
        return get(columnIndex, Object.class);
    }

    @Override
    public Object getObject(String columnLabel) throws SQLException {
        return get(columnLabel, Object.class);
    }

    @Override
    public Object getObject(String columnLabel, Map<String, Class<?>> map) throws SQLException {
        return get(columnLabel, Object.class);
    }

    // -------------------------------------------------------------------------
    // XXX: Setters and row update methods
    // -------------------------------------------------------------------------

    @Override
    public boolean rowUpdated() throws SQLException {
        return false;
    }

    @Override
    public boolean rowInserted() throws SQLException {
        return false;
    }

    @Override
    public boolean rowDeleted() throws SQLException {
        return false;
    }

    @Override
    public void updateNull(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public void updateBoolean(int columnIndex, boolean x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public void updateByte(int columnIndex, byte x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public void updateShort(int columnIndex, short x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public void updateInt(int columnIndex, int x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public void updateLong(int columnIndex, long x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public void updateFloat(int columnIndex, float x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public void updateDouble(int columnIndex, double x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public void updateString(int columnIndex, String x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public void updateBytes(int columnIndex, byte[] x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public void updateDate(int columnIndex, Date x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public void updateTime(int columnIndex, Time x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public void updateObject(int columnIndex, Object x, int scaleOrLength) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public void updateObject(int columnIndex, Object x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public void updateNull(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public void updateBoolean(String columnLabel, boolean x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public void updateByte(String columnLabel, byte x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public void updateShort(String columnLabel, short x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public void updateInt(String columnLabel, int x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public void updateLong(String columnLabel, long x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public void updateFloat(String columnLabel, float x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public void updateDouble(String columnLabel, double x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public void updateBigDecimal(String columnLabel, BigDecimal x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public void updateString(String columnLabel, String x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public void updateBytes(String columnLabel, byte[] x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public void updateDate(String columnLabel, Date x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public void updateTime(String columnLabel, Time x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public void updateTimestamp(String columnLabel, Timestamp x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public void updateAsciiStream(String columnLabel, InputStream x, int length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public void updateBinaryStream(String columnLabel, InputStream x, int length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public void updateCharacterStream(String columnLabel, Reader reader, int length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public void updateRef(int columnIndex, Ref x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public void updateRef(String columnLabel, Ref x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public void updateRowId(int columnIndex, RowId x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public void updateRowId(String columnLabel, RowId x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public void updateBlob(int columnIndex, Blob x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public void updateBlob(String columnLabel, Blob x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public void updateClob(int columnIndex, Clob x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public void updateClob(String columnLabel, Clob x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public void updateArray(int columnIndex, Array x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public void updateArray(String columnLabel, Array x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public void updateNString(int columnIndex, String nString) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public void updateNString(String columnLabel, String nString) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public void updateNClob(int columnIndex, NClob nClob) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public void updateNClob(String columnLabel, NClob nClob) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException { throw new SQLFeatureNotSupportedException("Cannot update ResultSet"); }

    @Override
    public void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException { throw new SQLFeatureNotSupportedException("Cannot update ResultSet"); }

    @Override
    public void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException { throw new SQLFeatureNotSupportedException("Cannot update ResultSet"); }

    @Override
    public void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException { throw new SQLFeatureNotSupportedException("Cannot update ResultSet"); }

    @Override
    public void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException { throw new SQLFeatureNotSupportedException("Cannot update ResultSet"); }

    @Override
    public void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException { throw new SQLFeatureNotSupportedException("Cannot update ResultSet"); }

    @Override
    public void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException { throw new SQLFeatureNotSupportedException("Cannot update ResultSet"); }

    @Override
    public void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException { throw new SQLFeatureNotSupportedException("Cannot update ResultSet"); }

    @Override
    public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException { throw new SQLFeatureNotSupportedException("Cannot update ResultSet"); }

    @Override
    public void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException { throw new SQLFeatureNotSupportedException("Cannot update ResultSet"); }

    @Override
    public void updateClob(int columnIndex, Reader reader, long length) throws SQLException { throw new SQLFeatureNotSupportedException("Cannot update ResultSet"); }

    @Override
    public void updateClob(String columnLabel, Reader reader, long length) throws SQLException { throw new SQLFeatureNotSupportedException("Cannot update ResultSet"); }

    @Override
    public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException { throw new SQLFeatureNotSupportedException("Cannot update ResultSet"); }

    @Override
    public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException { throw new SQLFeatureNotSupportedException("Cannot update ResultSet"); }

    @Override
    public void updateNCharacterStream(int columnIndex, Reader x) throws SQLException { throw new SQLFeatureNotSupportedException("Cannot update ResultSet"); }

    @Override
    public void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException { throw new SQLFeatureNotSupportedException("Cannot update ResultSet"); }

    @Override
    public void updateAsciiStream(int columnIndex, InputStream x) throws SQLException { throw new SQLFeatureNotSupportedException("Cannot update ResultSet"); }

    @Override
    public void updateBinaryStream(int columnIndex, InputStream x) throws SQLException { throw new SQLFeatureNotSupportedException("Cannot update ResultSet"); }

    @Override
    public void updateCharacterStream(int columnIndex, Reader x) throws SQLException { throw new SQLFeatureNotSupportedException("Cannot update ResultSet"); }

    @Override
    public void updateAsciiStream(String columnLabel, InputStream x) throws SQLException { throw new SQLFeatureNotSupportedException("Cannot update ResultSet"); }

    @Override
    public void updateBinaryStream(String columnLabel, InputStream x) throws SQLException { throw new SQLFeatureNotSupportedException("Cannot update ResultSet"); }

    @Override
    public void updateCharacterStream(String columnLabel, Reader reader) throws SQLException { throw new SQLFeatureNotSupportedException("Cannot update ResultSet"); }

    @Override
    public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException { throw new SQLFeatureNotSupportedException("Cannot update ResultSet"); }

    @Override
    public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException { throw new SQLFeatureNotSupportedException("Cannot update ResultSet"); }

    @Override
    public void updateClob(int columnIndex, Reader reader) throws SQLException { throw new SQLFeatureNotSupportedException("Cannot update ResultSet"); }

    @Override
    public void updateClob(String columnLabel, Reader reader) throws SQLException { throw new SQLFeatureNotSupportedException("Cannot update ResultSet"); }

    @Override
    public void updateNClob(int columnIndex, Reader reader) throws SQLException { throw new SQLFeatureNotSupportedException("Cannot update ResultSet"); }

    @Override
    public void updateNClob(String columnLabel, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public void updateObject(String columnLabel, Object x, int scaleOrLength) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public void updateObject(String columnLabel, Object x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public void insertRow() throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public void updateRow() throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public void deleteRow() throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public void refreshRow() throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public void cancelRowUpdates() throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public void moveToInsertRow() throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    @Override
    public void moveToCurrentRow() throws SQLException {
        throw new SQLFeatureNotSupportedException("Cannot update ResultSet");
    }

    // -------------------------------------------------------------------------
    // XXX: Object API
    // -------------------------------------------------------------------------

    @Override
    public String toString() {
        return result == null ? "null" : result.toString();
    }
}
