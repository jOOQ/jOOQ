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

import java.util.Collection;

import org.jooq.migrations.xml.jaxb.MigrationsType;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A (sub) set of commits.
 * <p>
 * This type is used to group together a set of commits that form a subgraph of
 * the complete commit graph. Like the complete graph, this subgraph will have a
 * single {@link #root()} node.
 *
 * @author Lukas Eder
 */
public interface Commits extends Iterable<Commit> {

    /**
     * The root node.
     */
    @NotNull
    Commit root();

    /**
     * Get a commit from the graph by ID, or <code>null</code>, if the ID was
     * not found.
     */
    @Nullable
    Commit get(String id);

    /**
     * Add a commit to the graph.
     */
    void add(Commit commit);

    /**
     * Add all commits to the graph.
     */
    void addAll(Commit... commits);

    /**
     * Add all commits to the graph.
     */
    void addAll(Collection<? extends Commit> commits);

    /**
     * Load XML content into this commits graph.
     */
    @NotNull
    Commits load(MigrationsType migrations);

    /**
     * Export XML content from this commits graph.
     */
    @NotNull
    MigrationsType export();
}
