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

import org.jetbrains.annotations.NotNull;

/**
 * A commit in a version control system.
 *
 * @author Lukas Eder
 */
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
