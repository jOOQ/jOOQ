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

import java.io.File;

import org.jooq.Commit;
import org.jooq.CommitProvider;
import org.jooq.Commits;
import org.jooq.Configuration;
import org.jooq.Migration;
import org.jooq.Migrations;
import org.jooq.exception.DataMigrationVerificationException;
import org.jooq.impl.DefaultCommitProvider;

import org.apache.maven.plugin.MojoExecutionException;

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

        CommitProvider cp = configuration.commitProvider();

        // TODO: We cannot assume this is equivalent to users not providing custom implementations
        if (cp instanceof DefaultCommitProvider) {
            Migrations migrations = configuration.dsl().migrations();
            Commits commits = migrations.commits();
            commits.load(file(directory));
            cp = (CommitProvider) () -> commits;
        }

        Migration migration = configuration
            .derive(cp)
            .dsl()
            .migrations()
            .migrateTo(migrateTo(cp));

        if (getLog().isInfoEnabled())
            getLog().info("Migration loaded from version " + migration.from() + " to version " + migration.to());

        execute1(migration);
    }

    /* non-final */ Commit migrateTo(CommitProvider cp) {
        return cp.provide().latest();
    }

    abstract void execute1(Migration migration) throws Exception;

    // [#9506] TODO: Move this utility into the library
    final File file(String file) {
        getLog().debug("Reading migrations directory: " + file);
        File f = new File(file);

        if (!f.isAbsolute())
            f = new File(project.getBasedir(), file);

        return f;
    }

    static final String fileName(String id, String fileName, String suffix) {
        String result = fileName != null ? fileName : id + "-" + suffix;

        if (!result.endsWith(".sql"))
            result = result + ".sql";

        return result;
    }
}
