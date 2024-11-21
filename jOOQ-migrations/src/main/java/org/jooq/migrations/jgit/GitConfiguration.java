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
package org.jooq.migrations.jgit;

import static org.jooq.tools.StringUtils.defaultIfNull;

import java.io.File;

import org.jooq.Commit;
import org.jooq.tools.StringUtils;

import org.eclipse.jgit.lib.Repository;
import org.jetbrains.annotations.NotNull;

/**
 * A configuration for the {@link GitCommitProvider}.
 */
public class GitConfiguration {

    private final File    repository;
    private final String  basedir;
    private final String  schemaFilePattern;
    private final String  incrementFilePattern;
    private final String  scriptFilePattern;
    private final String  snapshotFilePattern;
    private final boolean includeUncommitted;

    public GitConfiguration() {
        this(
            null,
            null,
            null,
            null,
            null,
            null,
            true
        );
    }

    private GitConfiguration(
        File repository,
        String basedir,
        String schemaFilePattern,
        String incrementFilePattern,
        String scriptFilePattern,
        String snapshotFilePattern,
        boolean includeUncommitted
    ) {
        this.repository = repository != null ? repository : new File(".");
        this.basedir = basedir != null ? basedir : "src/main/resources";
        this.schemaFilePattern = defaultIfNull(schemaFilePattern, "migrations/schemas/**");
        this.incrementFilePattern = defaultIfNull(incrementFilePattern, "migrations/increments/**");
        this.scriptFilePattern = defaultIfNull(scriptFilePattern, "migrations/scripts/**");
        this.snapshotFilePattern = defaultIfNull(snapshotFilePattern, "migrations/snapshots/**");
        this.includeUncommitted = includeUncommitted;
    }

    /**
     * The {@link Repository#getDirectory()}.
     */
    @NotNull
    public final GitConfiguration repository(File newRepository) {
        return new GitConfiguration(
            newRepository,
            basedir,
            schemaFilePattern,
            incrementFilePattern,
            scriptFilePattern,
            snapshotFilePattern,
            includeUncommitted
        );
    }

    /**
     * The {@link Repository#getDirectory()}.
     */
    @NotNull
    public final File repository() {
        return repository;
    }

    /**
     * The base directory of the migration scripts within the {@link #repository()}.
     */
    @NotNull
    public final GitConfiguration basedir(String newBasedir) {
        return new GitConfiguration(
            repository,
            newBasedir,
            schemaFilePattern,
            incrementFilePattern,
            scriptFilePattern,
            snapshotFilePattern,
            includeUncommitted
        );
    }

    /**
     * The base directory of the migration scripts within the {@link #repository()}.
     */
    @NotNull
    public final String basedir() {
        return basedir;
    }

    /**
     * The patterns of files in the repository to be searched for schema
     * definition files.
     */
    @NotNull
    public final GitConfiguration schemaFilePattern(String newSchemaFilePattern) {
        return new GitConfiguration(
            repository,
            basedir,
            newSchemaFilePattern,
            incrementFilePattern,
            scriptFilePattern,
            snapshotFilePattern,
            includeUncommitted
        );
    }

    /**
     * The patterns of files in the repository to be searched for schema
     * definition files.
     */
    @NotNull
    public final String schemaFilePattern() {
        return schemaFilePattern;
    }

    /**
     * The patterns of files in the repository to be searched for increment
     * definition files.
     */
    @NotNull
    public final GitConfiguration incrementFilePattern(String newIncrementFilePattern) {
        return new GitConfiguration(
            repository,
            basedir,
            schemaFilePattern,
            newIncrementFilePattern,
            scriptFilePattern,
            snapshotFilePattern,
            includeUncommitted
        );
    }

    /**
     * The patterns of files in the repository to be searched for increment
     * definition files.
     */
    @NotNull
    public final String incrementFilePattern() {
        return incrementFilePattern;
    }

    /**
     * Whether the uncommitted (and untracked) changes in the index should be
     * included as virtual {@link Commit}.
     */
    @NotNull
    public final GitConfiguration includeUncommitted(boolean newIncludeUncommitted) {
        return new GitConfiguration(
            repository,
            basedir,
            schemaFilePattern,
            incrementFilePattern,
            scriptFilePattern,
            snapshotFilePattern,
            newIncludeUncommitted);
    }

    /**
     * Whether the uncommitted (and untracked) changes in the index should be
     * included as virtual {@link Commit}.
     */
    @NotNull
    public final boolean includeUncommitted() {
        return includeUncommitted;
    }
}
