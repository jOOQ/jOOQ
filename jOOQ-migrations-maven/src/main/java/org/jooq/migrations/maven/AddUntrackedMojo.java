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

import static java.util.Arrays.asList;
import static org.apache.maven.plugins.annotations.LifecyclePhase.GENERATE_SOURCES;
import static org.apache.maven.plugins.annotations.ResolutionScope.TEST;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import org.jooq.Commit;
import org.jooq.History;
import org.jooq.Migration;
import org.jooq.Queries;
import org.jooq.exception.DataMigrationVerificationException;

import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Add objects from the configured database schemas to the migration.
 *
 * @author Lukas Eder
 */
@Mojo(
    name = "addUntracked",
    defaultPhase = GENERATE_SOURCES,
    requiresDependencyResolution = TEST,
    threadSafe = true
)
public class AddUntrackedMojo extends AbstractMigrateMojo {

    static final String P_FILE_NAME = "jooq.migrate.addUntracked.fileName";
    static final String P_VERSION   = "jooq.migrate.addUntracked.version";

    /**
     * The file name to add untracked objects to, defaulting to <code>[version]-added.sql</code>.
     */
    @Parameter(property = P_FILE_NAME)
    String fileName;

    /**
     * The version to add untracked objects to, defaulting to the last migrated version.
     */
    @Parameter(property = P_VERSION)
    String version;

    @Override
    final void execute1(Migration migration) throws Exception {
        Queries untracked = migration.untracked();

        if (untracked.queries().length > 0) {
            History history = migration.dsl().migrations().history();
            String id = version != null
                ? version
                : migration.to().id();

            if (history.available() && history.contains(id))
                if (version != null)
                    throw new DataMigrationVerificationException("Cannot add untracked files to a version that has already been applied to the history: " + id);
                else
                    throw new DataMigrationVerificationException("Cannot add untracked files to a version that has already been applied to the history: " + id + ". Specify the version property explicitly.");

            if (Commit.ROOT.equals(id))
                throw new DataMigrationVerificationException("Cannot add untracked files to root version. Specify the version property explicitly.");

            File file = new File(file(directory), id + "/increments/" + fileName(id, fileName, "untracked"));
            file.getParentFile().mkdirs();

            StringBuilder sb = new StringBuilder();

            if (getLog().isInfoEnabled())
                getLog().info("Writing untracked objects to: " + file + "\n" + untracked);

            sb.append("-- Untracked objects of version: " + id + "\n");

            sb.append(untracked);
            sb.append("\n");
            Files.write(file.toPath(), asList(sb.toString()), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        }
    }

}
