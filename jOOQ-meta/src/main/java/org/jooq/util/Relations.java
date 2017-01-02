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

/**
 * A model for all known Referenced Key / Referencing Key relations in the
 * {@link Database}'s schema.
 *
 * @author Lukas Eder
 */
public interface Relations {

    /**
     * Get the the primary for a given column, or <code>null</code> if that
     * column is not part of the primary key.
     */
    UniqueKeyDefinition getPrimaryKey(ColumnDefinition column);

    /**
     * Get a list of referenced keys (primary or unique) for a given table, that
     * the column participates in. Returns an empty list if the given column is
     * not part of any primary key or unique key.
     */
    List<UniqueKeyDefinition> getUniqueKeys(ColumnDefinition column);

    /**
     * Get a list of referenced keys (primary or unique) for a given table.
     * Returns an empty list if the given table has no primary or unique keys.
     */
    List<UniqueKeyDefinition> getUniqueKeys(TableDefinition table);

    /**
     * Get a list of all referenced keys (primary or unique) for a given schema.
     * Returns an empty list if the given schema has no primary or unique keys.
     */
    List<UniqueKeyDefinition> getUniqueKeys(SchemaDefinition schema);

    /**
     * Get a list of all referenced keys (primary or unique). Returns an empty
     * list if there are no primary or unique keys.
     */
    List<UniqueKeyDefinition> getUniqueKeys();

    /**
     * Get a list of foreign keys for a given table, that the column
     * participates in. Returns an empty list if the given column is not part of
     * any foreign key.
     */
    List<ForeignKeyDefinition> getForeignKeys(ColumnDefinition column);

    /**
     * Get a list of foreign keys for a given table. Returns an empty list if
     * the given table has no foreign keys.
     */
    List<ForeignKeyDefinition> getForeignKeys(TableDefinition table);

    /**
     * Get a list of <code>CHECK</code> constraints for a given table. Returns
     * an empty list if the given table has no <code>CHECK</code> constraints.
     */
    List<CheckConstraintDefinition> getCheckConstraints(TableDefinition table);
}
