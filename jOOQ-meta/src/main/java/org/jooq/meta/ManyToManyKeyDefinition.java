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
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
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
 * An object holding information about an many to many foreign key relationship.
 * <p>
 * This constraint represents the {@link UniqueKeyDefinition} that enforces the
 * relationship in the relationship table. The {@link UniqueKeyDefinition} is a
 * composite key, whose columns are completely contained in the two outbound
 * {@link ForeignKeyDefinition} specified by {@link #getForeignKey1()} and
 * {@link #getForeignKey2()}. The order of {@link ForeignKeyDefinition} depends
 * on the navigation direction.
 *
 * @author Lukas Eder
 */
public interface ManyToManyKeyDefinition extends ConstraintDefinition {

    /**
     * The list of columns making up the unique key.
     */
    List<ColumnDefinition> getKeyColumns();

    /**
     * The unique key that enforces the relationship.
     */
    UniqueKeyDefinition getUniqueKey();

    /**
     * Get the first foreign key in the relationship.
     */
    ForeignKeyDefinition getForeignKey1();

    /**
     * Get the second foreign key in the relationship.
     */
    ForeignKeyDefinition getForeignKey2();
}
