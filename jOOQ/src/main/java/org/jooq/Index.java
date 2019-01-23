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
package org.jooq;

import java.util.List;

import org.jooq.impl.DSL;

/**
 * A DDL index definition.
 * <p>
 * Instances can be created using {@link DSL#index(Name)} and overloads.
 *
 * @author Lukas Eder
 */
public interface Index extends Named {

    /**
     * The table on which this index is defined.
     */
    Table<?> getTable();

    /**
     * The sort field expressions on which this index is defined.
     */
    List<SortField<?>> getFields();

    /**
     * The condition of a filtered / partial index, or <code>null</code>, if
     * this is an ordinary index.
     */
    Condition getWhere();

    /**
     * Whether this is a <code>UNIQUE</code> index.
     */
    boolean getUnique();

}
