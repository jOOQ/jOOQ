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
package org.jooq;

import java.io.IOException;
import java.util.Collection;

import org.jooq.exception.DataMigrationVerificationException;
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
     * The current node.
     * <p>
     * The current node is the node in the graph that has been installed in the
     * database.
     *
     * @throws DataMigrationVerificationException In case there's no current node
     *             in the database, which can happen e.g. when no migration has
     *             been initialised yet, or when the currently installed
     *             {@link Commit#id()} is not part of this graph.
     */
    @NotNull
    Commit current() throws DataMigrationVerificationException;

    /**
     * The latest node.
     * <p>
     * The latest node is the last node in the graph, if all branches have been
     * merged successfully, or the latest node of the default branch, in case a
     * default branch has been specified.
     *
     * @throws DataMigrationVerificationException In case there's no latest node,
     *             i.e. there's no default branch and branches haven't all been
     *             merged yet.
     */
    @NotNull
    Commit latest() throws DataMigrationVerificationException;

    /**
     * Get a commit from the graph by {@link Commit#id()} or {@link Tag#id()}
     *
     * @return The resulting {@link Commit}, or <code>null</code>, if the ID was
     *         not found.
     */
    @Nullable
    Commit get(String id);

    /**
     * Add a commit to the graph.
     *
     * @return The same instance.
     */
    @NotNull
    Commits add(Commit commit);

    /**
     * Add all commits to the graph.
     *
     * @return The same instance.
     */
    @NotNull
    Commits addAll(Commit... commits);

    /**
     * Add all commits to the graph.
     *
     * @return The same instance.
     */
    @NotNull
    Commits addAll(Collection<? extends Commit> commits);

    /**
     * Load directory content into this commits graph.
     *
     * @return The same instance.
     * @throws IOException If anything goes wrong reading the directory
     *             contents.
     * @throws DataMigrationVerificationException If the migration verification
     *             fails.
     */
    @NotNull
    Commits load(java.io.File directory) throws IOException, DataMigrationVerificationException;

    /**
     * Load XML content into this commits graph.
     *
     * @return The same instance.
     * @throws DataMigrationVerificationException If the migration verification
     *             fails.
     */
    @NotNull
    Commits load(MigrationsType migrations) throws DataMigrationVerificationException;

    /**
     * Export XML content from this commits graph.
     */
    @NotNull
    MigrationsType export();
}
