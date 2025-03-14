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

import org.jooq.impl.QOM.ForeignKeyRule;

/**
 * An object holding information about a foreign key relationship.
 *
 * @author Lukas Eder
 */
public interface ForeignKeyDefinition extends ConstraintDefinition {

    /**
     * The definition of the referencing table
     *
     * @deprecated - [#9672] - jOOQ 3.13 - Use {@link ConstraintDefinition#getTable()} instead.
     */
    @Deprecated
    TableDefinition getKeyTable();

    /**
     * The list of columns making up the foreign key.
     */
    List<ColumnDefinition> getKeyColumns();

    /**
     * The referenced key.
     */
    UniqueKeyDefinition getReferencedKey();

    /**
     * Resolve a referenced key.
     * <p>
     * If {@link #getReferencedKey()} coincides itself with a foreign key,
     * resolve that foreign key recursively. In case of ambiguity (two foreign
     * keys coinciding with a single unique key), this returns
     * <code>null</code>.
     */
    UniqueKeyDefinition resolveReferencedKey();

    /**
     * The definition of the referenced table.
     */
    TableDefinition getReferencedTable();

    /**
     * The list of columns referenced by this foreign key
     */
    List<ColumnDefinition> getReferencedColumns();

    /**
     * Count the number of references between referencing and referenced tables.
     */
    int countSimilarReferences();

    /**
     * Get the inverse key.
     */
    InverseForeignKeyDefinition getInverse();

    /**
     * The <code>ON DELETE</code> rule or <code>null</code>, if unspecified.
     */
    ForeignKeyRule getDeleteRule();

    /**
     * The <code>ON UPDATE</code> rule or <code>null</code>, if unspecified.
     */
    ForeignKeyRule getUpdateRule();
}
