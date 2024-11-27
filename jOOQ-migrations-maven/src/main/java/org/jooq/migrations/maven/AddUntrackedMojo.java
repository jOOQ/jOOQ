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
import java.util.Arrays;

import org.jooq.Commit;
import org.jooq.History;
import org.jooq.Migration;
import org.jooq.Queries;

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

    /**
     * The file name to add untracked objects to, defaulting to <code>[version]-added.sql</code>.
     */
    @Parameter(property = "jooq.migrate.addUntracked.fileName")
    String fileName;

    @Override
    final void execute1(Migration migration) throws Exception {
        Queries untracked = migration.untracked();

        if (untracked.queries().length > 0) {
            History history = migration.dsl().migrations().history();
            String id = history.available() ? history.current().version().id() : migration.from().id();
            File file = new File(file(directory), id + "/increments/" + fileName(id, fileName, "added"));
            file.getParentFile().mkdirs();

            StringBuilder sb = new StringBuilder();

            if (getLog().isInfoEnabled())
                getLog().info("Writing untracked objects to: " + file + "\n" + untracked);

            sb.append("-- Untracked objects of version: " + id + "\n");

            if (Commit.ROOT.equals(id)) {
                sb.append("-- Objects that were present before the root version will not be created during migration on any databases.\n");
                sb.append("-- They are added to this file only as a baseline.\n");
            }

            sb.append(untracked);
            sb.append("\n");
            Files.write(file.toPath(), asList(sb.toString()), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        }
    }

}
