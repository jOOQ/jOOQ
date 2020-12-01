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
 *
 *
 *
 */

package org.jooq.meta;

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
     * Whether this table is a synthetic table (e.g. a synthetic view).
     */
    boolean isSynthetic();

    /**
     * All embeddable types in this defining table.
     */
    List<EmbeddableDefinition> getEmbeddables();

    /**
     * All embeddable types in this referencing table.
     */
    List<EmbeddableDefinition> getReferencedEmbeddables();

    /**
     * Get the indexes for this table.
     */
    List<IndexDefinition> getIndexes();

    /**
     * Get the primary key for this table.
     */
    UniqueKeyDefinition getPrimaryKey();

    /**
     * Get the unique keys for this table.
     */
    List<UniqueKeyDefinition> getUniqueKeys();

    /**
     * Get the unique key for this table by name.
     */
    UniqueKeyDefinition getUniqueKey(String name);

    /**
     * Get the unique keys and primary keys for this table.
     */
    List<UniqueKeyDefinition> getKeys();

    /**
     * Get the unique key or primary key for this table by name.
     */
    UniqueKeyDefinition getKey(String name);

    /**
     * Get the foreign keys for this table.
     */
    List<ForeignKeyDefinition> getForeignKeys();

    /**
     * Get the foreign keys for this table referencing a specific table.
     */
    List<ForeignKeyDefinition> getForeignKeys(TableDefinition referenced);

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
     * Whether this table is a temporary table.
     */
    boolean isTemporary();

    /**
     * Whether this table is a view.
     */
    boolean isView();

    /**
     * Whether this table is a materialized view.
     */
    boolean isMaterializedView();

    /**
     * Whether this table is a table-valued function.
     */
    boolean isTableValuedFunction();

}
