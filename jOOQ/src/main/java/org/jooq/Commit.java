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

import java.util.Collection;

import org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.NotNull;

/**
 * A change set describing the exact migration path between the
 * {@link #parents()} versions and the {@link #version()} represented by this
 * change.
 *
 * @author Lukas Eder
 */
@Experimental
public interface Commit extends Node<Commit> {

    /**
     * The files affected by this commit, in no particular order.
     */
    @NotNull
    Collection<File> delta();

    /**
     * The files after this commit, in no particular order.
     */
    @NotNull
    Collection<File> files();

    /**
     * The sources after this commit, in no particular order.
     */
    @NotNull
    Collection<Source> sources();

    /**
     * The tags associated with this commit, in no particular order.
     */
    @NotNull
    Collection<Tag> tags();

    /**
     * Add a tag to this commit, returning a copy of the commit itself.
     */
    @NotNull
    Commit tag(String id);

    /**
     * Add a tag to this commit, returning a copy of the commit itself.
     */
    @NotNull
    Commit tag(String id, String message);

    /**
     * Get the version representing this commit.
     */
    @NotNull
    Version version();

    /**
     * Get the meta data representing this commit.
     */
    @NotNull
    Meta meta();

    /**
     * Create a version graph when migrating between two commits.
     */
    @NotNull
    Files migrateTo(Commit commit);

    /**
     * Create a new commit on top of this one.
     */
    @NotNull
    Commit commit(String id, File... delta);

    /**
     * Create a new commit on top of this one.
     */
    @NotNull
    Commit commit(String id, Collection<? extends File> delta);

    /**
     * Create a new commit on top of this one.
     */
    @NotNull
    Commit commit(String id, String message, File... delta);

    /**
     * Create a new commit on top of this one.
     */
    @NotNull
    Commit commit(String id, String message, Collection<? extends File> delta);

    /**
     * Merge two commits.
     */
    @NotNull
    Commit merge(String id, Commit with, File... delta);

    /**
     * Merge two commits.
     */
    @NotNull
    Commit merge(String id, Commit with, Collection<? extends File> delta);

    /**
     * Merge two commits.
     */
    @NotNull
    Commit merge(String id, String message, Commit with, File... delta);

    /**
     * Merge two commits.
     */
    @NotNull
    Commit merge(String id, String message, Commit with, Collection<? extends File> delta);
}
