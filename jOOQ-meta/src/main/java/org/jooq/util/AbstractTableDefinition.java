/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 * 
 * This work is dual-licensed Open Source, under AGPL and jOOQ EULA
 * =============================================================================
 * You may freely choose which license applies to you. For more information 
 * about licensing, please visit http://www.jooq.org/licenses
 *
 * AGPL:  
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public 
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 * 
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it and/or
 * modify it.
 * 
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details.
 * 
 * You should have received a copy of the jOOQ End User License Agreement
 * along with this library.
 * If not, see http://www.jooq.org/eula
 */

package org.jooq.util;

import static org.jooq.impl.DSL.table;

import java.sql.SQLException;
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

    private List<UniqueKeyDefinition>       uniqueKeys;
    private List<ForeignKeyDefinition>      foreignKeys;
    private List<CheckConstraintDefinition> checkConstraints;
    private boolean                         primaryKeyLoaded;
    private UniqueKeyDefinition             primaryKey;
    private boolean                         identityLoaded;
    private IdentityDefinition              identity;

    public AbstractTableDefinition(SchemaDefinition schema, String name, String comment) {
        super(schema, name, comment);
    }

    @Override
    public final UniqueKeyDefinition getPrimaryKey() {
        if (!primaryKeyLoaded) {
            primaryKeyLoaded = true;

            // Try finding a primary key first
            for (ColumnDefinition column : getColumns()) {
                if (column.getPrimaryKey() != null) {
                    primaryKey = column.getPrimaryKey();
                    return primaryKey;
                }
            }
        }

        return primaryKey;
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
    public final List<CheckConstraintDefinition> getCheckConstraints() {
        if (checkConstraints == null) {
            checkConstraints = getDatabase().getRelations().getCheckConstraints(this);
        }

        return checkConstraints;
    }

    @Override
    public final IdentityDefinition getIdentity() {
        if (!identityLoaded) {
            identityLoaded = true;

            for (ColumnDefinition column : getColumns()) {
                if (column.isIdentity()) {
                    identity = new DefaultIdentityDefinition(column);
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
    public final ColumnDefinition getColumn(String columnName, boolean ignoreCase) {
        return getElement(columnName, ignoreCase);
    }

    @Override
    protected List<ColumnDefinition> getElements0() throws SQLException {
        return null;
    }

    @Override
    public final ColumnDefinition getColumn(int columnIndex) {
        return getElement(columnIndex);
    }
}
