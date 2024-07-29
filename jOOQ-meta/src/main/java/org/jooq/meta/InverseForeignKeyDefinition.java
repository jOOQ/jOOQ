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

/**
 * An object holding information about an inverse foreign key relationship.
 * <p>
 * This constraint represents the {@link UniqueKeyDefinition} that enforces the
 * uniqueness at the referenced end of the {@link ForeignKeyDefinition}.
 *
 * @author Lukas Eder
 */
public interface InverseForeignKeyDefinition extends ConstraintDefinition {

    /**
     * The list of columns making up the unique key.
     */
    List<ColumnDefinition> getKeyColumns();

    /**
     * The definition of the referencing table.
     */
    TableDefinition getReferencingTable();

    /**
     * The list of columns referencing this unique key from the foreign key.
     */
    List<ColumnDefinition> getReferencingColumns();

    /**
     * Get the foreign key this is an inverse of.
     */
    ForeignKeyDefinition getForeignKey();
}
