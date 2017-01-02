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
 * An interface defining a column of a table.
 *
 * @author Lukas Eder
 */
public interface ColumnDefinition extends TypedElementDefinition<TableDefinition> {

    /**
     * The column position in the table.
     */
    int getPosition();

    /**
     * A definition for the primary key that this column is part of, or
     * <code>null</code> if this column is not part of a primary key.
     */
    UniqueKeyDefinition getPrimaryKey();

    /**
     * All definitions of unique keys that this column is part of.
     */
    List<UniqueKeyDefinition> getUniqueKeys();

    /**
     * All definitions of foreign keys that this column is part of.
     */
    List<ForeignKeyDefinition> getForeignKeys();

    /**
     * Whether this column is the table's <code>IDENTITY</code> column.
     */
    boolean isIdentity();

    /**
     * Whether the column is nullable.
     *
     * @deprecated - [#2699] - 3.2.0 - Use
     *             {@link DataTypeDefinition#isNullable()} instead.
     */
    @Deprecated
    boolean isNullable();
}
