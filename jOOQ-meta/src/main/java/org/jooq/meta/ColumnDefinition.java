/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
 *
 * For more information, please visit: https://www.jooq.org/legal/licensing
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

// ...

/**
 * An interface defining a column of a table.
 *
 * @author Lukas Eder
 */
public interface ColumnDefinition extends TypedElementDefinition<TableDefinition>, PositionedDefinition {

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
     * All definitions of unique and primary keys that this column is part of.
     */
    List<UniqueKeyDefinition> getKeys();

    /**
     * All definitions of foreign keys that this column is part of.
     */
    List<ForeignKeyDefinition> getForeignKeys();

    /**
     * Whether this column is the table's <code>IDENTITY</code> column.
     */
    boolean isIdentity();

    /**
     * Whether this column is hidden.
     */
    boolean isHidden();

    /**
     * Whether this column is redacted.
     */
    boolean isRedacted();

    /**
     * Whether this column is readonly.
     */
    boolean isReadonly();










}
