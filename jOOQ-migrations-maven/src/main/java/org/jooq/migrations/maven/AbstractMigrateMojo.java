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
package org.jooq.migrations.maven;

import static org.apache.maven.plugins.annotations.LifecyclePhase.GENERATE_SOURCES;
import static org.apache.maven.plugins.annotations.ResolutionScope.TEST;

import java.io.File;

import org.jooq.CommitProvider;
import org.jooq.Commits;
import org.jooq.Configuration;
import org.jooq.Migration;
import org.jooq.Migrations;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * A base class for {@link MigrateMojo},
 *
 * @author Lukas Eder
 */
public abstract class AbstractMigrateMojo extends AbstractMigrationsMojo {

    @Override
    final void execute0(Configuration configuration) throws Exception {
        if (directory == null)
            throw new MojoExecutionException("Directory was not provided");

        Migrations migrations = configuration.dsl().migrations();
        Commits commits = migrations.commits();

        // [#9506] TODO: Support loading directories recursively
        // [#9506] TODO: Support loading **/*.sql style paths
        // [#9506] TODO: Support relative paths, absolute paths, etc.
        commits.load(file(directory));

        // [#9506] TODO: Having to use this CommitsProvider "trick" isn't really
        //               user friendly. There must be a better way
        Migration migration = configuration
            .derive((CommitProvider) () -> commits)
            .dsl()
            .migrations()
            .migrateTo(commits.latest());

        if (getLog().isInfoEnabled())
            getLog().info(
                  "Migration loaded from version " + migration.from() + " to version " + migration.to()
                + " (number of queries: " + migration.queries().queries().length + ")"
            );

        execute1(migration);
    }

    abstract void execute1(Migration migration) throws Exception;

    // [#9506] TODO: Move this utility into the library
    private File file(String file) {
        getLog().info("Reading migrations directory: " + file);
        File f = new File(file);

        if (!f.isAbsolute())
            f = new File(project.getBasedir(), file);

        return f;
    }
}
