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
import java.io.FileWriter;
import java.io.PrintWriter;

import org.jooq.History;
import org.jooq.HistoryVersion;
import org.jooq.Migration;
import org.jooq.Queries;
import org.jooq.exception.DataMigrationVerificationException;

import org.apache.maven.plugins.annotations.Mojo;

/**
 * Create a snapshot of the current version.
 *
 * @author Lukas Eder
 */
@Mojo(
    name = "snapshot",
    defaultPhase = GENERATE_SOURCES,
    requiresDependencyResolution = TEST,
    threadSafe = true
)
public class SnapshotMojo extends AbstractMigrateMojo {

    @Override
    final void execute1(Migration migration) throws Exception {
        History history = migration.dsl().migrations().history();
        Queries queries = migration.queries();

        if (queries.queries().length > 0) {
            Queries queries2 = migration.queries();
            getLog().warn("There are outstanding changes that have not been migrated yet, which are not in the snapshot:\n"
                + queries2);
        }

        HistoryVersion current = history.current();
        File file = new File(file(directory), current.version().id() + "/snapshots/snapshot.sql");
        file.getParentFile().mkdirs();

        try (FileWriter f = new FileWriter(file);
            PrintWriter w = new PrintWriter(f)
        ) {
            getLog().info("Writing snapshot to: " + file + "\n" + current.version().meta());
            w.println(current.version().meta().toString());
        }
    }
}
