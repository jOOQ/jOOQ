/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
 *                          Christopher Deckers, chrriis@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.jooq.debug;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
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
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author Christopher Deckers
 */
public class UsageTrackingPreparedStatement implements PreparedStatement {

	private PreparedStatement stmt;

    public UsageTrackingPreparedStatement(PreparedStatement stmt) {
        this.stmt = stmt;
    }

    @Override
    public void addBatch() throws SQLException {
        stmt.addBatch();
        commitParameterDescription();
    }

    @Override
    public void clearParameters() throws SQLException {
        stmt.clearParameters();
    	paramList.clear();
    }

    @Override
    public boolean execute() throws SQLException {
        return stmt.execute();
    }

    @Override
    public ResultSet executeQuery() throws SQLException {
        return stmt.executeQuery();
    }

    @Override
    public int executeUpdate() throws SQLException {
        return stmt.executeUpdate();
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        return stmt.getMetaData();
    }

    @Override
    public ParameterMetaData getParameterMetaData() throws SQLException {
        return stmt.getParameterMetaData();
    }

    @Override
    public void setArray(int parameterIndex, Array x) throws SQLException {
        stmt.setArray(parameterIndex, x);
        logValue(parameterIndex, x == null? null: "<Array>");
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
        stmt.setAsciiStream(parameterIndex, x);
        logValue(parameterIndex, x == null? null: "<AsciiStream>");
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
        stmt.setAsciiStream(parameterIndex, x, length);
        logValue(parameterIndex, x == null? null: "<AsciiStream>");
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
        stmt.setAsciiStream(parameterIndex, x, length);
        logValue(parameterIndex, x == null? null: "<AsciiStream>");
    }

    @Override
    public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
        stmt.setBigDecimal(parameterIndex, x);
        logValue(parameterIndex, x);
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
        stmt.setBinaryStream(parameterIndex, x);
        logValue(parameterIndex, x == null? null: "<BinaryStream>");
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
        stmt.setBinaryStream(parameterIndex, x, length);
        logValue(parameterIndex, x == null? null: "<BinaryStream>");
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
        stmt.setBinaryStream(parameterIndex, x, length);
        logValue(parameterIndex, x == null? null: "<BinaryStream>");
    }

    @Override
    public void setBlob(int parameterIndex, Blob x) throws SQLException {
        stmt.setBlob(parameterIndex, x);
        logValue(parameterIndex, x == null? null: "<Blob>");
    }

    @Override
    public void setBlob(int parameterIndex, InputStream x) throws SQLException {
        stmt.setBlob(parameterIndex, x);
        logValue(parameterIndex, x == null? null: "<Blob>");
    }

    @Override
    public void setBlob(int parameterIndex, InputStream x, long length) throws SQLException {
        stmt.setBlob(parameterIndex, x, length);
        logValue(parameterIndex, x == null? null: "<Blob>");
    }

    @Override
    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
        stmt.setBoolean(parameterIndex, x);
        logValue(parameterIndex, x);
    }

    @Override
    public void setByte(int parameterIndex, byte x) throws SQLException {
        stmt.setByte(parameterIndex, x);
        logValue(parameterIndex, x);
    }

    @Override
    public void setBytes(int parameterIndex, byte[] x) throws SQLException {
        stmt.setBytes(parameterIndex, x);
        logValue(parameterIndex, x);
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader x) throws SQLException {
        stmt.setCharacterStream(parameterIndex, x);
        logValue(parameterIndex, x == null? null: "<CharacterStream>");
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader x, int length) throws SQLException {
        stmt.setCharacterStream(parameterIndex, x, length);
        logValue(parameterIndex, x == null? null: "<CharacterStream>");
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader x, long length) throws SQLException {
        stmt.setCharacterStream(parameterIndex, x, length);
        logValue(parameterIndex, x == null? null: "<CharacterStream>");
    }

    @Override
    public void setClob(int parameterIndex, Clob x) throws SQLException {
        stmt.setClob(parameterIndex, x);
        logValue(parameterIndex, x == null? null: "<Clob>");
    }

    @Override
    public void setClob(int parameterIndex, Reader x) throws SQLException {
        stmt.setCharacterStream(parameterIndex, x);
        logValue(parameterIndex, x == null? null: "<Clob>");
    }

    @Override
    public void setClob(int parameterIndex, Reader x, long length) throws SQLException {
        stmt.setCharacterStream(parameterIndex, x, length);
        logValue(parameterIndex, x == null? null: "<Clob>");
    }

    @Override
    public void setDate(int parameterIndex, Date x) throws SQLException {
        stmt.setDate(parameterIndex, x);
        logValue(parameterIndex, x);
    }

    @Override
    public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
        stmt.setDate(parameterIndex, x, cal);
        logValue(parameterIndex, x);
    }

    @Override
    public void setDouble(int parameterIndex, double x) throws SQLException {
        stmt.setDouble(parameterIndex, x);
        logValue(parameterIndex, x);
    }

    @Override
    public void setFloat(int parameterIndex, float x) throws SQLException {
        stmt.setFloat(parameterIndex, x);
        logValue(parameterIndex, x);
    }

    @Override
    public void setInt(int parameterIndex, int x) throws SQLException {
        stmt.setInt(parameterIndex, x);
        logValue(parameterIndex, x);
    }

    @Override
    public void setLong(int parameterIndex, long x) throws SQLException {
        stmt.setLong(parameterIndex, x);
        logValue(parameterIndex, x);
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader x) throws SQLException {
        stmt.setNCharacterStream(parameterIndex, x);
        logValue(parameterIndex, x == null? null: "<NCharacterStream>");
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader x, long length) throws SQLException {
        stmt.setNCharacterStream(parameterIndex, x, length);
        logValue(parameterIndex, x == null? null: "<NCharacterStream>");
    }

    @Override
    public void setNClob(int parameterIndex, NClob x) throws SQLException {
        stmt.setNClob(parameterIndex, x);
        logValue(parameterIndex, x == null? null: "<NClob>");
    }

    @Override
    public void setNClob(int parameterIndex, Reader x) throws SQLException {
        stmt.setNClob(parameterIndex, x);
        logValue(parameterIndex, x == null? null: "<NClob>");
    }

    @Override
    public void setNClob(int parameterIndex, Reader x, long length) throws SQLException {
        stmt.setNClob(parameterIndex, x, length);
        logValue(parameterIndex, x == null? null: "<NClob>");
    }

    @Override
    public void setNString(int parameterIndex, String x) throws SQLException {
        stmt.setNString(parameterIndex, x);
        logValue(parameterIndex, x == null? null: '\'' + x.replace("'", "''") + '\'');
    }

    @Override
    public void setNull(int parameterIndex, int sqlType) throws SQLException {
        stmt.setNull(parameterIndex, sqlType);
        logValue(parameterIndex, null);
    }

    @Override
    public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
        stmt.setNull(parameterIndex, sqlType, typeName);
        logValue(parameterIndex, null);
    }

    @Override
    public void setObject(int parameterIndex, Object x) throws SQLException {
        stmt.setObject(parameterIndex, x);
        logValue(parameterIndex, x);
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
        stmt.setObject(parameterIndex, x, targetSqlType);
        logValue(parameterIndex, x);
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
        stmt.setObject(parameterIndex, x, targetSqlType, scaleOrLength);
        logValue(parameterIndex, x);
    }

    @Override
    public void setRef(int parameterIndex, Ref x) throws SQLException {
        stmt.setRef(parameterIndex, x);
        logValue(parameterIndex, x == null? null: "<Ref>");
    }

    @Override
    public void setRowId(int parameterIndex, RowId x) throws SQLException {
        stmt.setRowId(parameterIndex, x);
        logValue(parameterIndex, x == null? null: "<RowId>");
    }

    @Override
    public void setSQLXML(int parameterIndex, SQLXML x) throws SQLException {
        stmt.setSQLXML(parameterIndex, x);
        logValue(parameterIndex, x == null? null: "<SQLXML>");
    }

    @Override
    public void setShort(int parameterIndex, short x) throws SQLException {
        stmt.setShort(parameterIndex, x);
        logValue(parameterIndex, x);
    }

    @Override
    public void setString(int parameterIndex, String x) throws SQLException {
        stmt.setString(parameterIndex, x);
        logValue(parameterIndex, x == null? null: '\'' + x.replace("'", "''") + '\'');
    }

    @Override
    public void setTime(int parameterIndex, Time x) throws SQLException {
        stmt.setTime(parameterIndex, x);
        logValue(parameterIndex, x);
    }

    @Override
    public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
        stmt.setTime(parameterIndex, x, cal);
        logValue(parameterIndex, x);
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
        stmt.setTimestamp(parameterIndex, x);
        logValue(parameterIndex, x);
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
        stmt.setTimestamp(parameterIndex, x, cal);
        logValue(parameterIndex, x);
    }

    @Override
    public void setURL(int parameterIndex, URL x) throws SQLException {
        stmt.setURL(parameterIndex, x);
        logValue(parameterIndex, x);
    }

    @Override
    @Deprecated
    public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
        stmt.setUnicodeStream(parameterIndex, x, length);
        logValue(parameterIndex, x == null? null: "<UnicodeStream>");
    }

    @Override
    public void addBatch(String sql) throws SQLException {
        stmt.addBatch(sql);
        commitParameterDescription();
    }

    @Override
    public void cancel() throws SQLException {
        stmt.cancel();
    }

    @Override
    public void clearBatch() throws SQLException {
        stmt.clearBatch();
    }

    @Override
    public void clearWarnings() throws SQLException {
        stmt.clearWarnings();
    }

    @Override
    public void close() throws SQLException {
        stmt.close();
    }

    @Override
    public boolean execute(String sql) throws SQLException {
        return stmt.execute(sql);
    }

    @Override
    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        return stmt.execute(sql, autoGeneratedKeys);
    }

    @Override
    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        return stmt.execute(sql, columnIndexes);
    }

    @Override
    public boolean execute(String sql, String[] columnNames) throws SQLException {
        return stmt.execute(sql, columnNames);
    }

    @Override
    public int[] executeBatch() throws SQLException {
        return stmt.executeBatch();
    }

    @Override
    public ResultSet executeQuery(String sql) throws SQLException {
        return stmt.executeQuery(sql);
    }

    @Override
    public int executeUpdate(String sql) throws SQLException {
        return stmt.executeUpdate(sql);
    }

    @Override
    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        return stmt.executeUpdate(sql, autoGeneratedKeys);
    }

    @Override
    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
        return stmt.executeUpdate(sql, columnIndexes);
    }

    @Override
    public int executeUpdate(String sql, String[] columnNames) throws SQLException {
        return stmt.executeUpdate(sql, columnNames);
    }

//    @Override
//    public void closeOnCompletion() throws SQLException {
//        stmt.closeOnCompletion();
//    }
//
//    @Override
//    public boolean isCloseOnCompletion() throws SQLException {
//        return stmt.isCloseOnCompletion();
//    }

    @Override
    public Connection getConnection() throws SQLException {
        return stmt.getConnection();
    }

    @Override
    public int getFetchDirection() throws SQLException {
        return stmt.getFetchDirection();
    }

    @Override
    public int getFetchSize() throws SQLException {
        return stmt.getFetchDirection();
    }

    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        return stmt.getGeneratedKeys();
    }

    @Override
    public int getMaxFieldSize() throws SQLException {
        return stmt.getMaxFieldSize();
    }

    @Override
    public int getMaxRows() throws SQLException {
        return stmt.getMaxRows();
    }

    @Override
    public boolean getMoreResults() throws SQLException {
        return stmt.getMoreResults();
    }

    @Override
    public boolean getMoreResults(int current) throws SQLException {
        return stmt.getMoreResults(current);
    }

    @Override
    public int getQueryTimeout() throws SQLException {
        return stmt.getQueryTimeout();
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        return stmt.getResultSet();
    }

    @Override
    public int getResultSetConcurrency() throws SQLException {
        return stmt.getResultSetConcurrency();
    }

    @Override
    public int getResultSetHoldability() throws SQLException {
        return stmt.getResultSetHoldability();
    }

    @Override
    public int getResultSetType() throws SQLException {
        return stmt.getResultSetType();
    }

    @Override
    public int getUpdateCount() throws SQLException {
        return stmt.getUpdateCount();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return stmt.getWarnings();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return stmt.isClosed();
    }

    @Override
    public boolean isPoolable() throws SQLException {
        return stmt.isPoolable();
    }

    @Override
    public void setCursorName(String name) throws SQLException {
        stmt.setCursorName(name);
    }

    @Override
    public void setEscapeProcessing(boolean enable) throws SQLException {
        stmt.setEscapeProcessing(enable);
    }

    @Override
    public void setFetchDirection(int direction) throws SQLException {
        stmt.setFetchDirection(direction);
    }

    @Override
    public void setFetchSize(int rows) throws SQLException {
        stmt.setFetchSize(rows);
    }

    @Override
    public void setMaxFieldSize(int max) throws SQLException {
        stmt.setMaxFieldSize(max);
    }

    @Override
    public void setMaxRows(int max) throws SQLException {
        stmt.setMaxRows(max);
    }

    @Override
    public void setPoolable(boolean poolable) throws SQLException {
        stmt.setPoolable(poolable);
    }

    @Override
    public void setQueryTimeout(int seconds) throws SQLException {
        stmt.setQueryTimeout(seconds);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return stmt.isWrapperFor(iface);
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return stmt.unwrap(iface);
    }

    @Override
    public String toString() {
    	return stmt.toString();
    }

    PreparedStatement getWrappedStatement() {
        return stmt;
    }

	private StringBuilder sb = new StringBuilder();
	private List<String> paramList = new ArrayList<String>();

    private void commitParameterDescription() {
    	sb.append("[");
    	int size = paramList.size();
    	for(int i=0; i<size; i++) {
    		if(i > 0) {
    			sb.append(", ");
    		}
    		String param = paramList.get(i);
    		if(param != null) {
    			sb.append(param);
    		}
    	}
    	sb.append("]");
    }

    protected void logValue(int parameterIndex, Object o) {
    	// parameters in SQL are 1-based
		int missingLength = parameterIndex - paramList.size();
    	for(int i=0; i<missingLength; i++) {
    		paramList.add(null);
    	}
    	paramList.set(parameterIndex-1, String.valueOf(o));
	}

    public String getParameterDescription() {
    	if(paramList.isEmpty() && sb.length() == 0) {
    	    return null;
    	}
    	commitParameterDescription();
    	return sb.toString();
    }

}
