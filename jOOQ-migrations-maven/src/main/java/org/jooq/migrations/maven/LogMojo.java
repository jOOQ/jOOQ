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

import static java.util.stream.Collectors.toList;
import static org.apache.maven.plugins.annotations.LifecyclePhase.GENERATE_SOURCES;
import static org.apache.maven.plugins.annotations.ResolutionScope.TEST;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.jooq.History;
import org.jooq.HistoryVersion;
import org.jooq.Migration;
import org.jooq.Query;
import org.jooq.tools.StringUtils;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * Log the queries of the outstanding migration.
 *
 * @author Lukas Eder
 */
@Mojo(
    name = "log",
    defaultPhase = GENERATE_SOURCES,
    requiresDependencyResolution = TEST,
    threadSafe = true
)
public class LogMojo extends AbstractMigrateMojo {

    @Override
    final void execute1(Migration migration) throws Exception {
        if (getLog().isInfoEnabled()) {
            History history = migration.dsl().migrations().history();
            List<HistoryVersion> versions = StreamSupport
                .stream(history.spliterator(), false)
                .collect(toList());

            if (versions.isEmpty()) {
                getLog().info("No migration history available yet");
            }
            else {
                getLog().info("Migration history");

                for (HistoryVersion version : versions.subList(
                    Math.max(0, versions.size() - 5),
                    versions.size()
                )) {
                    HistoryMojo.log(getLog(), version);
                }
            }

            Query[] queries = migration.queries().queries();

            getLog().info("Outstanding queries from " + migration.from() + " to " + migration.to() + ": "
                + (queries.length == 0 ? "none" : "")
            );

            log(getLog(), queries);
        }
    }

    static final void log(Log log, Query[] queries) {
        int pad = ("" + queries.length).length();

        for (int i = 0; i < queries.length; i++)
            log.info("  Query " + StringUtils.leftPad("" + (i + 1), pad) + ": " + queries[i]);
    }
}
