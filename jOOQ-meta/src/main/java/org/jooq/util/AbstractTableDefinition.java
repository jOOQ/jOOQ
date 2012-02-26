/**
 * Copyright (c) 2009-2011, Lukas Eder, lukas.eder@gmail.com
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

import static org.jooq.impl.Factory.table;

import java.util.List;

import org.jooq.Record;
import org.jooq.Table;

/**
 * A base implementation for table definitions.
 *
 * @author Lukas Eder
 */
public abstract class AbstractTableDefinition
extends AbstractElementContainerDefinition<ColumnDefinition>
implements TableDefinition {

    private List<UniqueKeyDefinition>  uniqueKeys;
    private List<ForeignKeyDefinition> foreignKeys;
    private boolean                    mainUniqueKeyLoaded;
    private UniqueKeyDefinition        mainUniqueKey;
    private boolean                    identityLoaded;
    private ColumnDefinition           identity;

    public AbstractTableDefinition(SchemaDefinition schema, String name, String comment) {
        super(schema, name, comment);
    }

    @Override
    public final UniqueKeyDefinition getMainUniqueKey() {
        if (!mainUniqueKeyLoaded) {
            mainUniqueKeyLoaded = true;

            // Try finding a primary key first
            for (ColumnDefinition column : getColumns()) {
                if (column.getPrimaryKey() != null) {
                    mainUniqueKey = column.getPrimaryKey();
                    return mainUniqueKey;
                }
            }

            // Find best matching unique key. Matching algorithm:
            // 1. Prefer single-column indexes
            // 2. Prefer scalar-type indexes
            for (ColumnDefinition column : getColumns()) {
                for (UniqueKeyDefinition uniqueKey : column.getUniqueKeys()) {
                    mainUniqueKey = uniqueKey;
                    return mainUniqueKey;
                }
            }
        }

        return mainUniqueKey;
    }

    @Override
    public final List<UniqueKeyDefinition> getUniqueKeys() {
        if (uniqueKeys == null) {
            uniqueKeys = getDatabase().getRelations().getUniqueKeys(this);
        }

        return uniqueKeys;
    }

    @Override
    public final List<ForeignKeyDefinition> getForeignKeys() {
        if (foreignKeys == null) {
            foreignKeys = getDatabase().getRelations().getForeignKeys(this);
        }

        return foreignKeys;
    }

    @Override
    public final ColumnDefinition getIdentity() {
        if (!identityLoaded) {
            identityLoaded = true;

            for (ColumnDefinition column : getColumns()) {
                if (column.isIdentity()) {
                    identity = column;
                    break;
                }
            }
        }

        return identity;
    }

    @Override
    public final Table<Record> getTable() {
        return table(getQualifiedName());
    }

    @Override
    public final List<ColumnDefinition> getColumns() {
        return getElements();
    }

    @Override
    public final ColumnDefinition getColumn(String columnName) {
        return getElement(columnName);
    }

    @Override
    public final ColumnDefinition getColumn(int columnIndex) {
        return getElement(columnIndex);
    }
}
