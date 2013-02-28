/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
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
package org.jooq.liquibase;

import static org.jooq.impl.Factory.tableByName;
import static org.jooq.liquibase.Adapters.dialect;

import java.util.List;

import liquibase.database.structure.Column;
import liquibase.database.structure.Table;
import liquibase.database.structure.View;

import org.jooq.DataType;
import org.jooq.impl.CustomTable;
import org.jooq.impl.DefaultDataType;

/**
 * @author Lukas Eder
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
class LiquibaseTable extends CustomTable {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -280756564537037447L;

    LiquibaseTable(Table table) {
        super(table.getName(), new LiquibaseSchema(table.getSchema()));
        init(table.getColumns());
    }

    LiquibaseTable(View view) {
        super(view.getName(), new LiquibaseSchema(view.getSchema()));
        init(view.getColumns());
    }

    private void init(List<Column> columns) {
        for (Column column : columns) {
            DataType<?> type = DefaultDataType.getDataType(
                dialect(column),
                column.getTypeName(),
                column.getColumnSize(),
                column.getDecimalDigits());

            column.getDataType();
            createField(column.getName(), type, this);
        }
    }

    @Override
    public Class getRecordType() {

        // Trick access to RecordImpl
        return tableByName("dummy").getRecordType();
    }
}
