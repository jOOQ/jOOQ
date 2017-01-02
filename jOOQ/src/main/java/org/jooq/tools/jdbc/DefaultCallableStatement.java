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
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

/**
 * A default JDBC CallableStatement implementation delegating all JDBC 4.0 calls
 * to an internal delegate.
 *
 * @author Lukas Eder
 */
public class DefaultCallableStatement extends DefaultPreparedStatement implements CallableStatement {

    public DefaultCallableStatement(CallableStatement delegate) {
        super(delegate);
    }

    protected DefaultCallableStatement(Statement delegate) {
        super(delegate);
    }

    @Override
    public CallableStatement getDelegate() {
        return getDelegateCallableStatement();
    }

    public CallableStatement getDelegateCallableStatement() {
        return (CallableStatement) getDelegateStatement();
    }

    // ------------------------------------------------------------------------
    // XXX Other methods
    // ------------------------------------------------------------------------

    @Override
    public void registerOutParameter(int parameterIndex, int sqlType) throws SQLException {
        getDelegateCallableStatement().registerOutParameter(parameterIndex, sqlType);
    }

    @Override
    public void registerOutParameter(int parameterIndex, int sqlType, int scale) throws SQLException {
        getDelegateCallableStatement().registerOutParameter(parameterIndex, sqlType, scale);
    }

    @Override
    public boolean wasNull() throws SQLException {
        return getDelegateCallableStatement().wasNull();
    }

    @Override
    public String getString(int parameterIndex) throws SQLException {
        return getDelegateCallableStatement().getString(parameterIndex);
    }

    @Override
    public boolean getBoolean(int parameterIndex) throws SQLException {
        return getDelegateCallableStatement().getBoolean(parameterIndex);
    }

    @Override
    public byte getByte(int parameterIndex) throws SQLException {
        return getDelegateCallableStatement().getByte(parameterIndex);
    }

    @Override
    public short getShort(int parameterIndex) throws SQLException {
        return getDelegateCallableStatement().getShort(parameterIndex);
    }

    @Override
    public int getInt(int parameterIndex) throws SQLException {
        return getDelegateCallableStatement().getInt(parameterIndex);
    }

    @Override
    public long getLong(int parameterIndex) throws SQLException {
        return getDelegateCallableStatement().getLong(parameterIndex);
    }

    @Override
    public float getFloat(int parameterIndex) throws SQLException {
        return getDelegateCallableStatement().getFloat(parameterIndex);
    }

    @Override
    public double getDouble(int parameterIndex) throws SQLException {
        return getDelegateCallableStatement().getDouble(parameterIndex);
    }

    @Override
    @Deprecated
    public BigDecimal getBigDecimal(int parameterIndex, int scale) throws SQLException {
        return getDelegateCallableStatement().getBigDecimal(parameterIndex, scale);
    }

    @Override
    public byte[] getBytes(int parameterIndex) throws SQLException {
        return getDelegateCallableStatement().getBytes(parameterIndex);
    }

    @Override
    public Date getDate(int parameterIndex) throws SQLException {
        return getDelegateCallableStatement().getDate(parameterIndex);
    }

    @Override
    public Time getTime(int parameterIndex) throws SQLException {
        return getDelegateCallableStatement().getTime(parameterIndex);
    }

    @Override
    public Timestamp getTimestamp(int parameterIndex) throws SQLException {
        return getDelegateCallableStatement().getTimestamp(parameterIndex);
    }

    @Override
    public Object getObject(int parameterIndex) throws SQLException {
        return getDelegateCallableStatement().getObject(parameterIndex);
    }

    @Override
    public BigDecimal getBigDecimal(int parameterIndex) throws SQLException {
        return getDelegateCallableStatement().getBigDecimal(parameterIndex);
    }

    @Override
    public Object getObject(int parameterIndex, Map<String, Class<?>> map) throws SQLException {
        return getDelegateCallableStatement().getObject(parameterIndex, map);
    }

    @Override
    public Ref getRef(int parameterIndex) throws SQLException {
        return getDelegateCallableStatement().getRef(parameterIndex);
    }

    @Override
    public Blob getBlob(int parameterIndex) throws SQLException {
        return getDelegateCallableStatement().getBlob(parameterIndex);
    }

    @Override
    public Clob getClob(int parameterIndex) throws SQLException {
        return getDelegateCallableStatement().getClob(parameterIndex);
    }

    @Override
    public Array getArray(int parameterIndex) throws SQLException {
        return getDelegateCallableStatement().getArray(parameterIndex);
    }

    @Override
    public Date getDate(int parameterIndex, Calendar cal) throws SQLException {
        return getDelegateCallableStatement().getDate(parameterIndex, cal);
    }

    @Override
    public Time getTime(int parameterIndex, Calendar cal) throws SQLException {
        return getDelegateCallableStatement().getTime(parameterIndex, cal);
    }

    @Override
    public Timestamp getTimestamp(int parameterIndex, Calendar cal) throws SQLException {
        return getDelegateCallableStatement().getTimestamp(parameterIndex, cal);
    }

    @Override
    public void registerOutParameter(int parameterIndex, int sqlType, String typeName) throws SQLException {
        getDelegateCallableStatement().registerOutParameter(parameterIndex, sqlType, typeName);
    }

    @Override
    public void registerOutParameter(String parameterName, int sqlType) throws SQLException {
        getDelegateCallableStatement().registerOutParameter(parameterName, sqlType);
    }

    @Override
    public void registerOutParameter(String parameterName, int sqlType, int scale) throws SQLException {
        getDelegateCallableStatement().registerOutParameter(parameterName, sqlType, scale);
    }

    @Override
    public void registerOutParameter(String parameterName, int sqlType, String typeName) throws SQLException {
        getDelegateCallableStatement().registerOutParameter(parameterName, sqlType, typeName);
    }

    @Override
    public URL getURL(int parameterIndex) throws SQLException {
        return getDelegateCallableStatement().getURL(parameterIndex);
    }

    @Override
    public void setURL(String parameterName, URL val) throws SQLException {
        getDelegateCallableStatement().setURL(parameterName, val);
    }

    @Override
    public void setNull(String parameterName, int sqlType) throws SQLException {
        getDelegateCallableStatement().setNull(parameterName, sqlType);
    }

    @Override
    public void setBoolean(String parameterName, boolean x) throws SQLException {
        getDelegateCallableStatement().setBoolean(parameterName, x);
    }

    @Override
    public void setByte(String parameterName, byte x) throws SQLException {
        getDelegateCallableStatement().setByte(parameterName, x);
    }

    @Override
    public void setShort(String parameterName, short x) throws SQLException {
        getDelegateCallableStatement().setShort(parameterName, x);
    }

    @Override
    public void setInt(String parameterName, int x) throws SQLException {
        getDelegateCallableStatement().setInt(parameterName, x);
    }

    @Override
    public void setLong(String parameterName, long x) throws SQLException {
        getDelegateCallableStatement().setLong(parameterName, x);
    }

    @Override
    public void setFloat(String parameterName, float x) throws SQLException {
        getDelegateCallableStatement().setFloat(parameterName, x);
    }

    @Override
    public void setDouble(String parameterName, double x) throws SQLException {
        getDelegateCallableStatement().setDouble(parameterName, x);
    }

    @Override
    public void setBigDecimal(String parameterName, BigDecimal x) throws SQLException {
        getDelegateCallableStatement().setBigDecimal(parameterName, x);
    }

    @Override
    public void setString(String parameterName, String x) throws SQLException {
        getDelegateCallableStatement().setString(parameterName, x);
    }

    @Override
    public void setBytes(String parameterName, byte[] x) throws SQLException {
        getDelegateCallableStatement().setBytes(parameterName, x);
    }

    @Override
    public void setDate(String parameterName, Date x) throws SQLException {
        getDelegateCallableStatement().setDate(parameterName, x);
    }

    @Override
    public void setTime(String parameterName, Time x) throws SQLException {
        getDelegateCallableStatement().setTime(parameterName, x);
    }

    @Override
    public void setTimestamp(String parameterName, Timestamp x) throws SQLException {
        getDelegateCallableStatement().setTimestamp(parameterName, x);
    }

    @Override
    public void setAsciiStream(String parameterName, InputStream x, int length) throws SQLException {
        getDelegateCallableStatement().setAsciiStream(parameterName, x, length);
    }

    @Override
    public void setBinaryStream(String parameterName, InputStream x, int length) throws SQLException {
        getDelegateCallableStatement().setBinaryStream(parameterName, x, length);
    }

    @Override
    public void setObject(String parameterName, Object x, int targetSqlType, int scale) throws SQLException {
        getDelegateCallableStatement().setObject(parameterName, x, targetSqlType, scale);
    }

    @Override
    public void setObject(String parameterName, Object x, int targetSqlType) throws SQLException {
        getDelegateCallableStatement().setObject(parameterName, x, targetSqlType);
    }

    @Override
    public void setObject(String parameterName, Object x) throws SQLException {
        getDelegateCallableStatement().setObject(parameterName, x);
    }

    @Override
    public void setCharacterStream(String parameterName, Reader reader, int length) throws SQLException {
        getDelegateCallableStatement().setCharacterStream(parameterName, reader, length);
    }

    @Override
    public void setDate(String parameterName, Date x, Calendar cal) throws SQLException {
        getDelegateCallableStatement().setDate(parameterName, x, cal);
    }

    @Override
    public void setTime(String parameterName, Time x, Calendar cal) throws SQLException {
        getDelegateCallableStatement().setTime(parameterName, x, cal);
    }

    @Override
    public void setTimestamp(String parameterName, Timestamp x, Calendar cal) throws SQLException {
        getDelegateCallableStatement().setTimestamp(parameterName, x, cal);
    }

    @Override
    public void setNull(String parameterName, int sqlType, String typeName) throws SQLException {
        getDelegateCallableStatement().setNull(parameterName, sqlType, typeName);
    }

    @Override
    public String getString(String parameterName) throws SQLException {
        return getDelegateCallableStatement().getString(parameterName);
    }

    @Override
    public boolean getBoolean(String parameterName) throws SQLException {
        return getDelegateCallableStatement().getBoolean(parameterName);
    }

    @Override
    public byte getByte(String parameterName) throws SQLException {
        return getDelegateCallableStatement().getByte(parameterName);
    }

    @Override
    public short getShort(String parameterName) throws SQLException {
        return getDelegateCallableStatement().getShort(parameterName);
    }

    @Override
    public int getInt(String parameterName) throws SQLException {
        return getDelegateCallableStatement().getInt(parameterName);
    }

    @Override
    public long getLong(String parameterName) throws SQLException {
        return getDelegateCallableStatement().getLong(parameterName);
    }

    @Override
    public float getFloat(String parameterName) throws SQLException {
        return getDelegateCallableStatement().getFloat(parameterName);
    }

    @Override
    public double getDouble(String parameterName) throws SQLException {
        return getDelegateCallableStatement().getDouble(parameterName);
    }

    @Override
    public byte[] getBytes(String parameterName) throws SQLException {
        return getDelegateCallableStatement().getBytes(parameterName);
    }

    @Override
    public Date getDate(String parameterName) throws SQLException {
        return getDelegateCallableStatement().getDate(parameterName);
    }

    @Override
    public Time getTime(String parameterName) throws SQLException {
        return getDelegateCallableStatement().getTime(parameterName);
    }

    @Override
    public Timestamp getTimestamp(String parameterName) throws SQLException {
        return getDelegateCallableStatement().getTimestamp(parameterName);
    }

    @Override
    public Object getObject(String parameterName) throws SQLException {
        return getDelegateCallableStatement().getObject(parameterName);
    }

    @Override
    public BigDecimal getBigDecimal(String parameterName) throws SQLException {
        return getDelegateCallableStatement().getBigDecimal(parameterName);
    }

    @Override
    public Object getObject(String parameterName, Map<String, Class<?>> map) throws SQLException {
        return getDelegateCallableStatement().getObject(parameterName, map);
    }

    @Override
    public Ref getRef(String parameterName) throws SQLException {
        return getDelegateCallableStatement().getRef(parameterName);
    }

    @Override
    public Blob getBlob(String parameterName) throws SQLException {
        return getDelegateCallableStatement().getBlob(parameterName);
    }

    @Override
    public Clob getClob(String parameterName) throws SQLException {
        return getDelegateCallableStatement().getClob(parameterName);
    }

    @Override
    public Array getArray(String parameterName) throws SQLException {
        return getDelegateCallableStatement().getArray(parameterName);
    }

    @Override
    public Date getDate(String parameterName, Calendar cal) throws SQLException {
        return getDelegateCallableStatement().getDate(parameterName, cal);
    }

    @Override
    public Time getTime(String parameterName, Calendar cal) throws SQLException {
        return getDelegateCallableStatement().getTime(parameterName, cal);
    }

    @Override
    public Timestamp getTimestamp(String parameterName, Calendar cal) throws SQLException {
        return getDelegateCallableStatement().getTimestamp(parameterName, cal);
    }

    @Override
    public URL getURL(String parameterName) throws SQLException {
        return getDelegateCallableStatement().getURL(parameterName);
    }

    @Override
    public RowId getRowId(int parameterIndex) throws SQLException {
        return getDelegateCallableStatement().getRowId(parameterIndex);
    }

    @Override
    public RowId getRowId(String parameterName) throws SQLException {
        return getDelegateCallableStatement().getRowId(parameterName);
    }

    @Override
    public void setRowId(String parameterName, RowId x) throws SQLException {
        getDelegateCallableStatement().setRowId(parameterName, x);
    }

    @Override
    public void setNString(String parameterName, String value) throws SQLException {
        getDelegateCallableStatement().setNString(parameterName, value);
    }

    @Override
    public void setNCharacterStream(String parameterName, Reader value, long length) throws SQLException {
        getDelegateCallableStatement().setNCharacterStream(parameterName, value, length);
    }

    @Override
    public void setNClob(String parameterName, NClob value) throws SQLException {
        getDelegateCallableStatement().setNClob(parameterName, value);
    }

    @Override
    public void setClob(String parameterName, Reader reader, long length) throws SQLException {
        getDelegateCallableStatement().setClob(parameterName, reader, length);
    }

    @Override
    public void setBlob(String parameterName, InputStream inputStream, long length) throws SQLException {
        getDelegateCallableStatement().setBlob(parameterName, inputStream, length);
    }

    @Override
    public void setNClob(String parameterName, Reader reader, long length) throws SQLException {
        getDelegateCallableStatement().setNClob(parameterName, reader, length);
    }

    @Override
    public NClob getNClob(int parameterIndex) throws SQLException {
        return getDelegateCallableStatement().getNClob(parameterIndex);
    }

    @Override
    public NClob getNClob(String parameterName) throws SQLException {
        return getDelegateCallableStatement().getNClob(parameterName);
    }

    @Override
    public void setSQLXML(String parameterName, SQLXML xmlObject) throws SQLException {
        getDelegateCallableStatement().setSQLXML(parameterName, xmlObject);
    }

    @Override
    public SQLXML getSQLXML(int parameterIndex) throws SQLException {
        return getDelegateCallableStatement().getSQLXML(parameterIndex);
    }

    @Override
    public SQLXML getSQLXML(String parameterName) throws SQLException {
        return getDelegateCallableStatement().getSQLXML(parameterName);
    }

    @Override
    public String getNString(int parameterIndex) throws SQLException {
        return getDelegateCallableStatement().getNString(parameterIndex);
    }

    @Override
    public String getNString(String parameterName) throws SQLException {
        return getDelegateCallableStatement().getNString(parameterName);
    }

    @Override
    public Reader getNCharacterStream(int parameterIndex) throws SQLException {
        return getDelegateCallableStatement().getNCharacterStream(parameterIndex);
    }

    @Override
    public Reader getNCharacterStream(String parameterName) throws SQLException {
        return getDelegateCallableStatement().getNCharacterStream(parameterName);
    }

    @Override
    public Reader getCharacterStream(int parameterIndex) throws SQLException {
        return getDelegateCallableStatement().getCharacterStream(parameterIndex);
    }

    @Override
    public Reader getCharacterStream(String parameterName) throws SQLException {
        return getDelegateCallableStatement().getCharacterStream(parameterName);
    }

    @Override
    public void setBlob(String parameterName, Blob x) throws SQLException {
        getDelegateCallableStatement().setBlob(parameterName, x);
    }

    @Override
    public void setClob(String parameterName, Clob x) throws SQLException {
        getDelegateCallableStatement().setClob(parameterName, x);
    }

    @Override
    public void setAsciiStream(String parameterName, InputStream x, long length) throws SQLException {
        getDelegateCallableStatement().setAsciiStream(parameterName, x, length);
    }

    @Override
    public void setBinaryStream(String parameterName, InputStream x, long length) throws SQLException {
        getDelegateCallableStatement().setBinaryStream(parameterName, x, length);
    }

    @Override
    public void setCharacterStream(String parameterName, Reader reader, long length) throws SQLException {
        getDelegateCallableStatement().setCharacterStream(parameterName, reader, length);
    }

    @Override
    public void setAsciiStream(String parameterName, InputStream x) throws SQLException {
        getDelegateCallableStatement().setAsciiStream(parameterName, x);
    }

    @Override
    public void setBinaryStream(String parameterName, InputStream x) throws SQLException {
        getDelegateCallableStatement().setBinaryStream(parameterName, x);
    }

    @Override
    public void setCharacterStream(String parameterName, Reader reader) throws SQLException {
        getDelegateCallableStatement().setCharacterStream(parameterName, reader);
    }

    @Override
    public void setNCharacterStream(String parameterName, Reader value) throws SQLException {
        getDelegateCallableStatement().setNCharacterStream(parameterName, value);
    }

    @Override
    public void setClob(String parameterName, Reader reader) throws SQLException {
        getDelegateCallableStatement().setClob(parameterName, reader);
    }

    @Override
    public void setBlob(String parameterName, InputStream inputStream) throws SQLException {
        getDelegateCallableStatement().setBlob(parameterName, inputStream);
    }

    @Override
    public void setNClob(String parameterName, Reader reader) throws SQLException {
        getDelegateCallableStatement().setNClob(parameterName, reader);
    }
}
