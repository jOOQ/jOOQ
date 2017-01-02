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
