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
import java.util.List;

import org.jooq.conf.Settings;

/**
 * A version ID attached to a {@link Meta} description of a database.
 *
 * @author Lukas Eder
 */
public interface Version {

    /**
     * The version ID, which is unique in the version graph.
     */
    String id();

    /**
     * The version's {@link Meta} representation of the database.
     */
    Meta meta();

    /**
     * Produce a migration to a new version.
     * <p>
     * In jOOQ's commercial distributions, this method allows for migrating
     * between versions in any direction, regardless of which version was
     * "first" in a version graph, or if the two versions are on different
     * branches. The resulting queries are potentially destructive in such a
     * case. Such destructive queries ("UNDO" migrations) are prevented by
     * default, and can be turned on using
     * {@link Settings#isMigrationAllowsUndo()}.
     * <p>
     * In jOOQ's Open Source Edition, this method only allows for migrating
     * "forward".
     */
    Queries migrateTo(Version version);

    /**
     * Get the root version of this graph.
     */
    Version root();

    /**
     * Get the parent versions of this version.
     */
    List<Version> parents();

    /**
     * Commit a new {@link Meta} representation to the version graph.
     * <p>
     * This calculates a migration path using {@link Meta#migrateTo(Meta)}.
     */
    Version commit(String id, Meta meta);

    /**
     * Commit a new {@link Meta} representation to the version graph.
     *
     * @see #commit(String, Meta)
     */
    Version commit(String id, String... meta);

    /**
     * Commit a new {@link Meta} representation to the version graph.
     *
     * @see #commit(String, Meta)
     */
    Version commit(String id, Source... meta);

    /**
     * Merge versions.
     */
    Version merge(String id, Version with);

    /**
     * Apply a migration to produce a new version.
     */
    Version apply(String id, Queries migration);

    /**
     * Apply a migration to produce a new version.
     *
     * @see #apply(String, Queries)
     */
    Version apply(String id, Query... migration);

    /**
     * Apply a migration to produce a new version.
     *
     * @see #apply(String, Queries)
     */
    Version apply(String id, Collection<? extends Query> migration);

    /**
     * Apply a migration to produce a new version.
     *
     * @see #apply(String, Queries)
     */
    Version apply(String id, String migration);
}
