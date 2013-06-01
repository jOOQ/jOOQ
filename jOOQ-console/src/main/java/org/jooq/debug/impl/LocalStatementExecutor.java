/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
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
package org.jooq.debug.impl;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jooq.SQLDialect;
import org.jooq.debug.QueryExecution;
import org.jooq.debug.QueryExecutionMessageResult;
import org.jooq.debug.QueryExecutionResult;
import org.jooq.debug.QueryExecutor;
import org.jooq.debug.QueryExecutorContext;
import org.jooq.debug.QueryType;
import org.jooq.debug.impl.StatementExecutionResultSetResult.TypeInfo;

/**
 * @author Christopher Deckers
 */
class LocalStatementExecutor implements QueryExecutor {

    private QueryExecutorContext executorContext;

    public LocalStatementExecutor(QueryExecutorContext executorContext) {
        this.executorContext = executorContext;
    }

    private volatile Connection conn;
    private volatile Statement stmt;

    private volatile Thread evaluationThread;

    @Override
    public QueryExecution execute(String sql, int maxRSRowsParsing, int retainParsedRSDataRowCountThreshold, boolean isUpdatable) {
        if(sql.trim().length() == 0) {
            return new QueryExecution(0, new QueryExecutionMessageResult("The statement to evaluate cannot be empty.", true));
        }
        boolean isReadOnly = executorContext.isReadOnly();
        isUpdatable = !isReadOnly && isUpdatable;
        boolean isAllowed = true;
        if(isReadOnly) {
            String simplifiedSql = sql.replaceAll("'[^']*'", "");
            switch(QueryType.detectType(simplifiedSql)) {
                case SELECT:
                    String[] forbiddenWords = new String[] {
                            "INSERT",
                            "UPDATE",
                            "DELETE",
                            "ALTER",
                            "DROP",
                            "CREATE",
                            "EXEC",
                            "EXECUTE",
                    };
                    Matcher matcher = Pattern.compile("[a-zA-Z_0-9\\$]+").matcher(simplifiedSql);
                    while(matcher.find()) {
                        String word = simplifiedSql.substring(matcher.start(), matcher.end()).toUpperCase(Locale.ENGLISH);
                        for(String keyword: forbiddenWords) {
                            if(word.equals(keyword)) {
                                isAllowed = false;
                                break;
                            }
                        }
                    }
                    break;
                default:
                    isAllowed = false;
                    break;
            }
        }
        if(!isAllowed) {
            return new QueryExecution(0, new QueryExecutionMessageResult("The database is not editable but the statement to evaluate is a modification statement!", true));
        }
        stopExecution();
        evaluationThread = Thread.currentThread();
        long start = System.currentTimeMillis();
        try {
            conn = executorContext.getConnection();
            if(isUpdatable) {
                stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            } else {
                stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            }
            // If no error, adjust start to beginning of actual execution.
            start = System.currentTimeMillis();
            if(evaluationThread != Thread.currentThread()) {
                long executionDuration = System.currentTimeMillis() - start;
                return new QueryExecution(executionDuration, new QueryExecutionMessageResult("Interrupted by user after " + Utils.formatDuration(executionDuration), true));
            }
            boolean executeResult;
            try {
                executeResult = stmt.execute(sql);
            } catch(SQLException e) {
                long executionDuration = System.currentTimeMillis() - start;
                if(evaluationThread != Thread.currentThread()) {
                    return new QueryExecution(executionDuration, new QueryExecutionMessageResult("Interrupted by user after " + Utils.formatDuration(executionDuration), true));
                }
                return new QueryExecution(executionDuration, new QueryExecutionMessageResult(e));
            }
            final long executionDuration = System.currentTimeMillis() - start;
            if(evaluationThread != Thread.currentThread()) {
                return new QueryExecution(executionDuration, new QueryExecutionMessageResult("Interrupted by user after " + Utils.formatDuration(executionDuration), true));
            }
            List<QueryExecutionResult> statementExecutionResultList = new ArrayList<QueryExecutionResult>();
            int updateCount = stmt.getUpdateCount();
            do {
                QueryExecutionResult queryExecutionResult;
                if(executeResult) {
                    final ResultSet rs = stmt.getResultSet();
                    ResultSetMetaData metaData = rs.getMetaData();
                    final String[] columnNames = new String[metaData.getColumnCount()];
                    final int[] columnTypes = new int[columnNames.length];
                    final TypeInfo[] typeInfos = new TypeInfo[columnNames.length];
                    final Class<?>[] columnClasses = new Class[columnNames.length];
                    for(int i=0; i<columnNames.length; i++) {
                        columnNames[i] = metaData.getColumnName(i + 1);
                        if(columnNames[i] == null || columnNames[i].length() == 0) {
                            columnNames[i] = " ";
                        }
                        typeInfos[i] = new TypeInfo(metaData, i);
                        int type = metaData.getColumnType(i + 1);
                        columnTypes[i] = type;
                        switch(type) {
                            case Types.CLOB:
                                columnClasses[i] = String.class;
                                break;
                            case Types.BLOB:
                                columnClasses[i] = byte[].class;
                                break;
                            default:
                                String columnClassName = metaData.getColumnClassName(i + 1);
                                if(columnClassName == null) {
                                    System.err.println("Unknown SQL Type for \"" + columnNames[i] + "\" in " + getClass().getSimpleName() + ": " + metaData.getColumnTypeName(i));
                                    columnClasses[i] = Object.class;
                                } else {
                                    if(columnClassName.indexOf('.') == -1) {
                                        boolean isArray;
                                        if(columnClassName.endsWith("[]")) {
                                            // TODO: handle multi dimensional arrays?
                                            columnClassName = columnClassName.substring(0, columnClassName.length() - 2);
                                            isArray = true;
                                        } else {
                                            isArray = false;
                                        }
                                        if("boolean".equals(columnClassName)) {
                                            columnClasses[i] = boolean.class;
                                        } else if("byte".equals(columnClassName)) {
                                            columnClasses[i] = byte.class;
                                        } else if("char".equals(columnClassName)) {
                                            columnClasses[i] = char.class;
                                        } else if("short".equals(columnClassName)) {
                                            columnClasses[i] = short.class;
                                        } else if("int".equals(columnClassName)) {
                                            columnClasses[i] = int.class;
                                        } else if("long".equals(columnClassName)) {
                                            columnClasses[i] = long.class;
                                        } else if("float".equals(columnClassName)) {
                                            columnClasses[i] = float.class;
                                        } else if("double".equals(columnClassName)) {
                                            columnClasses[i] = double.class;
                                        } else {
                                            isArray = false;
                                            columnClasses[i] = Class.forName(columnClassName);
                                        }
                                        if(isArray) {
                                            columnClasses[i] = Array.newInstance(columnClasses[i], 0).getClass();
                                        }
                                    } else {
                                        columnClasses[i] = Class.forName(columnClassName);
                                    }
                                }
                                break;
                        }
                    }
                    if(evaluationThread != Thread.currentThread()) {
                        return new QueryExecution(executionDuration, new QueryExecutionMessageResult("Interrupted by user after " + Utils.formatDuration(executionDuration), true));
                    }
                    final List<Object[]> rowDataList = new ArrayList<Object[]>();
                    int rowCount = 0;
                    long rsStart = System.currentTimeMillis();
                    while(rs.next() && rowCount < maxRSRowsParsing) {
                        if(evaluationThread != Thread.currentThread()) {
                            return new QueryExecution(executionDuration, new QueryExecutionMessageResult("Interrupted by user after " + Utils.formatDuration(executionDuration), true));
                        }
                        rowCount++;
                        Object[] rowData = new Object[columnNames.length];
                        for(int i=0; i<columnNames.length; i++) {
                            switch(columnTypes[i]) {
                                case Types.CLOB: {
                                    Clob clob = rs.getClob(i + 1);
                                    if(clob != null) {
                                        StringWriter stringWriter = new StringWriter();
                                        char[] chars = new char[1024];
                                        Reader reader = new BufferedReader(clob.getCharacterStream());
                                        try {
                                            for(int count; (count=reader.read(chars))>=0; ) {
                                                stringWriter.write(chars, 0, count);
                                            }
                                        } finally {
                                            reader.close();
                                        }
                                        rowData[i] = stringWriter.toString();
                                    } else {
                                        rowData[i] = null;
                                    }
                                    break;
                                }
                                case Types.BLOB: {
                                    Blob blob = rs.getBlob(i + 1);
                                    if(blob != null) {
                                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                        byte[] bytes = new byte[1024];
                                        InputStream in = new BufferedInputStream(blob.getBinaryStream());
                                        try {
                                            for(int count; (count=in.read(bytes))>=0; ) {
                                                baos.write(bytes, 0, count);
                                            }
                                        } finally {
                                            in.close();
                                        }
                                        rowData[i] = baos.toByteArray();
                                    } else {
                                        rowData[i] = null;
                                    }
                                    break;
                                }
                                default:
                                    Object object = rs.getObject(i + 1);
                                    if(object != null) {
                                        String className = object.getClass().getName();
                                        if ("oracle.sql.TIMESTAMP".equals(className) || "oracle.sql.TIMESTAMPTZ".equals(className)) {
                                            object = rs.getTimestamp(i + 1);
                                        }
                                        // Probably something to do for oracle.sql.DATE
                                    }
                                    rowData[i] = object;
                                    break;
                            }
                        }
                        if(rowCount <= retainParsedRSDataRowCountThreshold) {
                            rowDataList.add(rowData);
                        } else if(rowCount == retainParsedRSDataRowCountThreshold + 1) {
                            rowDataList.clear();
                        }
                    }
                    final long resultSetParsingDuration = System.currentTimeMillis() - rsStart;
                    queryExecutionResult = new LocalStatementExecutionResultSetResult(rs, columnNames, typeInfos, columnClasses, rowDataList.toArray(new Object[0][]), rowCount, resultSetParsingDuration, retainParsedRSDataRowCountThreshold, isReadOnly);
                } else {
                    queryExecutionResult = new QueryExecutionMessageResult(Utils.formatDuration(executionDuration) + "> " + updateCount + " row(s) affected.", false);
                }
                if(executorContext.getSQLDialect().family() == SQLDialect.SQLSERVER) {
                    try {
                        executeResult = stmt.getMoreResults(Statement.KEEP_CURRENT_RESULT);
                    } catch(Exception e) {
                        executeResult = stmt.getMoreResults();
                    }
                    updateCount = stmt.getUpdateCount();
                } else {
                    executeResult = false;
                    updateCount = -1;
                }
                statementExecutionResultList.add(queryExecutionResult);
            } while(executeResult || updateCount != -1);
            return new QueryExecution(executionDuration, statementExecutionResultList.toArray(new QueryExecutionResult[0]));
        } catch(Exception e) {
            long executionDuration = System.currentTimeMillis() - start;
            return new QueryExecution(executionDuration, new QueryExecutionMessageResult(e));
        } finally {
            if(isReadOnly) {
                closeConnection();
            }
        }
    }

    private void closeConnection() {
        if (conn != null) {
            if (stmt != null) {
                try {
                    stmt.cancel();
                } catch (Exception e) {
                }
                try {
                    stmt.close();
                } catch (Exception e) {
                }
            }
            stmt = null;
            executorContext.releaseConnection(conn);
            conn = null;
        }
    }

    @Override
    public void stopExecution() {
        if(evaluationThread != null) {
            evaluationThread = null;
            closeConnection();
        }
    }

    @Override
    public String[] getTableNames() {
        return executorContext.getTableNames();
    }

    @Override
    public String[] getTableColumnNames() {
        return executorContext.getTableColumnNames();
    }

}
