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

import java.io.Serializable;


/**
 * @author Christopher Deckers
 */
@SuppressWarnings("serial")
class ClientStatementExecutionResultSetResult implements StatementExecutionResultSetResult, Serializable {

    private String[]   columnNames;
    private TypeInfo[] typeInfos;
    private Class<?>[] columnClasses;
    private Object[][] rowData;
    private int        rowCount;
    private long       resultSetParsingDuration;
    private int        retainParsedRSDataRowCountThreshold;

    public ClientStatementExecutionResultSetResult(LocalStatementExecutionResultSetResult rsResult) {
        columnNames = rsResult.getColumnNames();
        typeInfos = rsResult.getTypeInfos();
        columnClasses = rsResult.getColumnClasses();
        Object[][] rowData_ = rsResult.getRowData();
        rowData = new Object[rowData_.length][];
        for(int i=0; i<rowData_.length; i++) {
            Object[] columnData_ = rowData_[i];
            Object[] columnData = new Object[columnData_.length];
            for(int j=0; j<columnData_.length; j++) {
                Object o = columnData_[j];
                if(o != null && !(o instanceof Serializable)) {
                    // Best effort conversion because we are in remote mode...
                    o = String.valueOf(o);
                }
                columnData[j] = o;
            }
            rowData[i] = columnData;
        }
        rowCount = rsResult.getRowCount();
        resultSetParsingDuration = rsResult.getResultSetParsingDuration();
        retainParsedRSDataRowCountThreshold = rsResult.getRetainParsedRSDataRowCountThreshold();
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
        // TODO: implement
        return false;
    }

    @Override
    public boolean setValueAt(Object o, int row, int col) {
        // TODO: implement
        return false;
    }

    @Override
    public boolean isEditable() {
        // TODO: implement
        return false;
    }



}
