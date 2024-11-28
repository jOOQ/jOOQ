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

import static java.lang.Boolean.TRUE;
import static java.util.Arrays.asList;
import static org.apache.maven.plugins.annotations.LifecyclePhase.GENERATE_SOURCES;
import static org.apache.maven.plugins.annotations.ResolutionScope.TEST;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.EnumSet;

import org.jooq.DDLExportConfiguration;
import org.jooq.DDLFlag;
import org.jooq.History;
import org.jooq.HistoryVersion;
import org.jooq.Meta;
import org.jooq.Migration;
import org.jooq.Queries;

import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Create a snapshot of the current version and add it to the migration.
 *
 * @author Lukas Eder
 */
@Mojo(
    name = "addSnapshot",
    defaultPhase = GENERATE_SOURCES,
    requiresDependencyResolution = TEST,
    threadSafe = true
)
public class AddSnapshotMojo extends AbstractMigrateMojo {

    /**
     * The file name to add untracked objects to, defaulting to <code>[version]-added.sql</code>.
     */
    @Parameter(property = "jooq.migrate.addSnapshot.fileName")
    String fileName;

    @Override
    final void execute1(Migration migration) throws Exception {
        migration.configuration().requireCommercial(() -> "Snapshots are a commercial only feature. Please upgrade to the jOOQ Professional Edition or jOOQ Enterprise Edition.");





























    }
}
