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
import static org.jooq.tools.StringUtils.isEmpty;

import java.time.Instant;

import org.jooq.HistoryVersion;
import org.jooq.Migration;
import org.jooq.Version;
import org.jooq.tools.StringUtils;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * Show the history of currently installed versions on the connected database.
 *
 * @author Lukas Eder
 */
@Mojo(
    name = "history",
    defaultPhase = GENERATE_SOURCES,
    requiresDependencyResolution = TEST,
    threadSafe = true
)
public class HistoryMojo extends AbstractMigrateMojo {

    @Override
    final void execute1(Migration migration) throws Exception {
        if (getLog().isInfoEnabled())
            for (HistoryVersion version : migration.dsl().migrations().history())
                log(getLog(), version);
    }

    static final void log(Log log, HistoryVersion version) {
        log.info("  " + string(version.migratedAt()) + " - Version: " + string(version.version()));

        if (version.version().parents().size() > 1) {
            log.info("  Merged parents: ");

            for (Version p : version.version().parents())
                log.info("  - " + string(p));
        }
    }

    private static final String string(Instant instant) {
        if (instant == null)
            return "0000-00-00T00:00:00.000Z";
        else
            return StringUtils.rightPad(instant.toString(), 24);
    }

    private static final String string(Version version) {
        return version.id() + (!isEmpty(version.message()) ? " (" + version.message() + ")" : "");
    }
}
