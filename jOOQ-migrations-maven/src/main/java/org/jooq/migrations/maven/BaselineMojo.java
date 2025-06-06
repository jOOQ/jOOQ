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

import org.jooq.Commit;
import org.jooq.CommitProvider;
import org.jooq.Migration;

import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Create a baseline from the current schema using the pending changes, if they
 * match.
 *
 * @author Lukas Eder
 */
@Mojo(
    name = "baseline",
    defaultPhase = GENERATE_SOURCES,
    requiresDependencyResolution = TEST,
    threadSafe = true
)
public class BaselineMojo extends AbstractMigrateMojo {

    static final String P_VERSION = "jooq.migrate.baseline.version";

    /**
     * The version to set the baseline to, defaulting to <code>root</code>.
     */
    @Parameter(property = P_VERSION)
    String version;

    @Override
    final Commit migrateTo(CommitProvider cp) {
        return version != null ? cp.provide().get(version) : super.migrateTo(cp);
    }

    @Override
    final void execute1(Migration migration) throws Exception {
        migration.baseline();
    }
}
