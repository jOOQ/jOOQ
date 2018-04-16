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

import org.jooq.SortOrder;

/**
 * An interface defining a column of an index.
 *
 * @author Lukas Eder
 */
public interface IndexColumnDefinition extends TypedElementDefinition<IndexDefinition> {

    /**
     * The column position in the index.
     */
    int getPosition();

    /**
     * The <code>ASC</code> or <code>DESC</code> sort order
     */
    SortOrder getSortOrder();

    /**
     * The table column definition that this index column definition is backed
     * by.
     * <p>
     * This may be a virtual and/or invisible column, e.g. in case this index is
     * a function-based index.
     */
    ColumnDefinition getColumn();
}
