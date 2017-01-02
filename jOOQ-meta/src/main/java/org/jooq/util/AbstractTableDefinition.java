/*
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */

package org.jooq.util;

import static org.jooq.impl.DSL.table;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
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

    private List<ParameterDefinition>       parameters;
    private TableDefinition                 parentTable;
    private List<TableDefinition>           childTables;

    public AbstractTableDefinition(SchemaDefinition schema, String name, String comment) {
        super(schema, name, comment);

        this.parentTable = null;
        this.childTables = new ArrayList<TableDefinition>();
    }

    @Override
    public final UniqueKeyDefinition getPrimaryKey() {
        UniqueKeyDefinition primaryKey = null;

        for (ColumnDefinition column : getColumns()) {
            if (column.getPrimaryKey() != null) {
                primaryKey = column.getPrimaryKey();
                return primaryKey;
            }
        }

        return primaryKey;
    }

    @Override
    public final List<UniqueKeyDefinition> getUniqueKeys() {
        return getDatabase().getRelations().getUniqueKeys(this);
    }

    @Override
    public final List<ForeignKeyDefinition> getForeignKeys() {
        return getDatabase().getRelations().getForeignKeys(this);
    }

    @Override
    public final List<CheckConstraintDefinition> getCheckConstraints() {
        return getDatabase().getRelations().getCheckConstraints(this);
    }

    @Override
    public final IdentityDefinition getIdentity() {
        IdentityDefinition identity = null;

        for (ColumnDefinition column : getColumns()) {
            if (column.isIdentity()) {
                identity = new DefaultIdentityDefinition(column);
                break;
            }
        }

        return identity;
    }

    public final void setParentTable(TableDefinition parentTable) {
        this.parentTable = parentTable;
    }

    @Override
    public final TableDefinition getParentTable() {
        return parentTable;
    }

    @Override
    public final List<TableDefinition> getChildTables() {
        return childTables;
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
    public final ColumnDefinition getColumn(int columnIndex) {
        return getElement(columnIndex);
    }

    @Override
    public final List<ParameterDefinition> getParameters() {
        if (parameters == null) {
            parameters = getParameters0();
        }

        return parameters;
    }

    @Override
    public /* non-final */ boolean isTableValuedFunction() {
        return false;
    }

    @Override
    protected List<ColumnDefinition> getElements0() throws SQLException {
        return null;
    }

    protected List<ParameterDefinition> getParameters0() {
        return Collections.emptyList();
    }
}
