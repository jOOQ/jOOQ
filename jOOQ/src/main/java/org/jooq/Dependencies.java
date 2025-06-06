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
package org.jooq;

import java.util.Set;

import org.jooq.conf.Settings;

import org.jetbrains.annotations.NotNull;

/**
 * A representation of the object dependencies between the various objects in a
 * {@link Meta} object collection.
 * <p>
 * Unqualified table references are looked up using
 * {@link Settings#getInterpreterSearchPath()}.
 * <p>
 * This is a commercial jOOQ edition only feature.
 *
 * @author Lukas Eder
 */
public interface Dependencies {

    /**
     * The direct table dependencies of a view.
     */
    @NotNull
    Set<Table<?>> viewTables(Table<?> view);

    /**
     * The and transitive table dependencies of a view.
     */
    @NotNull
    Set<Table<?>> viewTablesTransitive(Table<?> view);
}
