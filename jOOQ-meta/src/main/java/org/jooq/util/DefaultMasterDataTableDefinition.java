/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
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
package org.jooq.util;

import static org.jooq.impl.Factory.field;

import java.util.Arrays;
import java.util.List;

import org.jooq.Record;
import org.jooq.Result;
import org.jooq.Table;
import org.jooq.exception.DataAccessException;
import org.jooq.tools.JooqLogger;
import org.jooq.util.jaxb.MasterDataTable;

public class DefaultMasterDataTableDefinition extends AbstractDefinition implements MasterDataTableDefinition {

    private static final JooqLogger log = JooqLogger.getLogger(DefaultMasterDataTableDefinition.class);

    private final TableDefinition   delegate;
    private Result<Record>          data;
    private boolean                 dataFetched;

    public DefaultMasterDataTableDefinition(TableDefinition delegate) {
        super(delegate.getDatabase(), delegate.getSchema(), delegate.getName(), delegate.getComment());

        this.delegate = delegate;
    }

    @Override
    public List<Definition> getDefinitionPath() {
        return Arrays.<Definition>asList(getSchema(), this);
    }

    @Override
    public ColumnDefinition getPrimaryKeyColumn() {
        for (ColumnDefinition column : getColumns()) {
            if (getDatabase().getRelations().getPrimaryKey(column) != null) {
                return column;
            }
        }

        return null;
    }

    @Override
    public ColumnDefinition getLiteralColumn() {
        String columnName = getConfiguredMasterDataTable().getLiteral();

        if (columnName == null) {
            columnName = getPrimaryKeyColumn().getName();
        }

        return getColumn(columnName);
    }

    @Override
    public ColumnDefinition getDescriptionColumn() {
        String columnName = getConfiguredMasterDataTable().getDescription();

        if (columnName == null) {
            columnName = getLiteralColumn().getName();
        }

        return getColumn(columnName);
    }

    private final MasterDataTable getConfiguredMasterDataTable() {
        for (MasterDataTable table : getDatabase().getConfiguredMasterDataTables()) {
            if (table.getName().equals(getName())) {
                return table;
            }
        }

        return null;
    }

    @Override
    public Result<Record> getData() {
        if (!dataFetched) {
            dataFetched = true;

            try {
                data = create().select()
                               .from(delegate.getTable())
                               .orderBy(field(getPrimaryKeyColumn().getName()))
                               .fetch();
            }
            catch (DataAccessException e) {
                log.error("Error while initialising master data", e);
            }
        }

        return data;
    }

    @Override
    public UniqueKeyDefinition getMainUniqueKey() {
        return delegate.getMainUniqueKey();
    }

    @Override
    public List<UniqueKeyDefinition> getUniqueKeys() {
        return delegate.getUniqueKeys();
    }

    @Override
    public List<ForeignKeyDefinition> getForeignKeys() {
        return delegate.getForeignKeys();
    }

    @Override
    public ColumnDefinition getIdentity() {
        return delegate.getIdentity();
    }

    @Override
    public List<ColumnDefinition> getColumns() {
        return delegate.getColumns();
    }

    @Override
    public ColumnDefinition getColumn(String columnName) {
        return delegate.getColumn(columnName);
    }

    @Override
    public ColumnDefinition getColumn(String columnName, boolean ignoreCase) {
        return delegate.getColumn(columnName, ignoreCase);
    }

    @Override
    public ColumnDefinition getColumn(int columnIndex) {
        return delegate.getColumn(columnIndex);
    }

    @Override
    public Table<Record> getTable() {
        return delegate.getTable();
    }
}
