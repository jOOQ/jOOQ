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

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * @author Christopher Deckers
 */
class LocalStatementExecutionResultSetResult implements StatementExecutionResultSetResult {

    private ResultSet rs;
    private String[] columnNames;
    private TypeInfo[] typeInfos;
    private Class<?>[] columnClasses;
    private Object[][] rowData;
    private int rowCount;
    private long resultSetParsingDuration;
    private int retainParsedRSDataRowCountThreshold;
    private boolean isReadOnly;

    public LocalStatementExecutionResultSetResult(ResultSet rs, String[] columnNames, TypeInfo[] typeInfos, Class<?>[] columnClasses, Object[][] rowData, int rowCount, long resultSetParsingDuration, int retainParsedRSDataRowCountThreshold, boolean isReadOnly) {
        this.rs = rs;
        this.columnNames = columnNames;
        this.typeInfos = typeInfos;
        this.columnClasses = columnClasses;
        this.rowData = rowData;
        this.rowCount = rowCount;
        this.resultSetParsingDuration = resultSetParsingDuration;
        this.retainParsedRSDataRowCountThreshold = retainParsedRSDataRowCountThreshold;
        this.isReadOnly = isReadOnly;
    }

    @Override
    public String[] getColumnNames() {
        return columnNames;
    }

    @Override
    public TypeInfo[] getTypeInfos() {
        return typeInfos;
    }

    @Override
    public Class<?>[] getColumnClasses() {
        return columnClasses;
    }

    /**
     * @return the data or an empty array of arrays if <code>rowCount</code> is over <code>retainParsedRSDataRowCountThreshold</code>.
     */
    @Override
    public Object[][] getRowData() {
        return rowData;
    }

    @Override
    public int getRowCount() {
        return rowCount;
    }

    @Override
    public long getResultSetParsingDuration() {
        return resultSetParsingDuration;
    }

    @Override
    public int getRetainParsedRSDataRowCountThreshold() {
        return retainParsedRSDataRowCountThreshold;
    }

    @Override
    public boolean deleteRow(int row) {
        try {
            rs.absolute(row);
            rs.deleteRow();
            Object[][] newRowData = new Object[rowData.length - 1][];
            System.arraycopy(rowData, 0, newRowData, 0, row);
            System.arraycopy(rowData, row + 1, newRowData, row, newRowData.length - row - 1);
            rowData = newRowData;
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean setValueAt(Object o, int row, int col) {
        if(Utils.equals(o, rowData[row][col])) {
            return false;
        }
        try {
            rs.absolute(row + 1);
            rs.updateObject(col + 1, o);
            rs.updateRow();
            rowData[row][col] = o;
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                rs.cancelRowUpdates();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean isEditable() {
        try {
            return !isReadOnly && rs.getConcurrency() == ResultSet.CONCUR_UPDATABLE;
        } catch (SQLException e) {
        }
        return false;
    }

}
