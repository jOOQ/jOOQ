/*
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
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: http://www.jooq.org/licenses
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package org.jooq.util;

import java.util.List;

import org.jooq.Record;
import org.jooq.Table;

/**
 * The definition of a table or view.
 *
 * @author Lukas Eder
 */
public interface TableDefinition extends Definition {

    /**
     * All columns in the type, table or view.
     */
    List<ColumnDefinition> getColumns();

    /**
     * Get a column in this type by its name.
     */
    ColumnDefinition getColumn(String columnName);

    /**
     * Get a column in this type by its name.
     */
    ColumnDefinition getColumn(String columnName, boolean ignoreCase);

    /**
     * Get a column in this type by its index (starting at 0).
     */
    ColumnDefinition getColumn(int columnIndex);

    /**
     * Get the primary key for this table.
     */
    UniqueKeyDefinition getPrimaryKey();

    /**
     * Get the unique keys for this table.
     */
    List<UniqueKeyDefinition> getUniqueKeys();

    /**
     * Get the foreign keys for this table.
     */
    List<ForeignKeyDefinition> getForeignKeys();

    /**
     * Get the <code>CHECK</code> constraints for this table.
     */
    List<CheckConstraintDefinition> getCheckConstraints();

    /**
     * Get the <code>IDENTITY</code> column of this table, or <code>null</code>,
     * if no such column exists.
     */
    IdentityDefinition getIdentity();

    /**
     * Get the parent table if table inheritance is applicable.
     */
    TableDefinition getParentTable();

    /**
     * Get the child tables if table inheritance is applicable.
     */
    List<TableDefinition> getChildTables();

    /**
     * This TableDefinition as a {@link Table}.
     */
    Table<Record> getTable();

    /**
     * The parameters of this table if this is a table-valued function.
     */
    List<ParameterDefinition> getParameters();

    /**
     * Whether this table is a table-valued function.
     */
    boolean isTableValuedFunction();

}
