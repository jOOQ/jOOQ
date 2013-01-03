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
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.jooq.debug.QueryExecutionResult;

/**
 * @author Christopher Deckers
 */
public interface StatementExecutionResultSetResult extends QueryExecutionResult {

    @SuppressWarnings("serial")
    public static class TypeInfo implements Serializable {

        private String columnName;
        private int precision;
        private int scale;
        private int nullable = ResultSetMetaData.columnNullableUnknown;

        TypeInfo(ResultSetMetaData metaData, int column) {
            try {
                columnName = metaData.getColumnTypeName(column + 1);
                precision = metaData.getPrecision(column + 1);
                scale = metaData.getScale(column + 1);
                nullable = metaData.isNullable(column + 1);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(columnName);
            if(precision != 0) {
                sb.append(" (" + precision + (scale != 0? ", " + scale: "") + ")");
            }
            if(nullable != ResultSetMetaData.columnNullableUnknown) {
                sb.append(nullable == ResultSetMetaData.columnNoNulls? " not null": " null");
            }
            return sb.toString();
        }

    }

    public String[] getColumnNames();

    public TypeInfo[] getTypeInfos();

    public Class<?>[] getColumnClasses();

    /**
     * @return the data or an empty array of arrays if <code>rowCount</code> is over <code>retainParsedRSDataRowCountThreshold</code>.
     */
    public Object[][] getRowData();

    public int getRowCount();

    public long getResultSetParsingDuration();

    public int getRetainParsedRSDataRowCountThreshold();

    public boolean deleteRow(int row);

    public boolean setValueAt(Object o, int row, int col);

    public boolean isEditable();

}
