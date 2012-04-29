/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
 *                             Christopher Deckers, chrriis@gmail.com
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.debug.console.DatabaseDescriptor;


public class LocalDebugger implements Debugger {

    private DatabaseDescriptor databaseDescriptor;

    public LocalDebugger(DatabaseDescriptor databaseDescriptor) {
        this.databaseDescriptor = databaseDescriptor;
    }

    private LoggingListener loggingListener;

    @Override
    public void setLoggingListener(LoggingListener loggingListener) {
        this.loggingListener = loggingListener;
    }

    @Override
    public LoggingListener getLoggingListener() {
        return loggingListener;
    }

    @Override
    public boolean isExecutionSupported() {
        return databaseDescriptor != null;
    }

    @Override
    public StatementExecutor createStatementExecutor(String sql, int maxRSRowsParsing, int retainParsedRSDataRowCountThreshold) {
        return new StatementExecutorImpl(databaseDescriptor, sql, maxRSRowsParsing, retainParsedRSDataRowCountThreshold);
    }

    @Override
    public String[] getTableNames() {
        List<Table<?>> tableList = databaseDescriptor.getSchema().getTables();
        List<String> tableNameList = new ArrayList<String>();
        for(Table<? extends Record> table: tableList) {
            String tableName = table.getName();
            tableNameList.add(tableName);
        }
        Collections.sort(tableNameList, String.CASE_INSENSITIVE_ORDER);
        return tableNameList.toArray(new String[0]);
    }

    @Override
    public String[] getTableColumnNames() {
        Set<String> columnNameSet = new HashSet<String>();
        for(Table<?> table: databaseDescriptor.getSchema().getTables()) {
            for(Field<?> field: table.getFields()) {
                String columnName = field.getName();
                columnNameSet.add(columnName);
            }
        }
        String[] columnNames = columnNameSet.toArray(new String[0]);
        Arrays.sort(columnNames, String.CASE_INSENSITIVE_ORDER);
        return columnNames;
    }

    @Override
    public boolean isReadOnly() {
        return databaseDescriptor.isReadOnly();
    }

}
