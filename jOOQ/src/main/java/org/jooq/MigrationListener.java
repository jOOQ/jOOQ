/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
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
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: http://www.jooq.org/licenses
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
package org.jooq;

import java.util.EventListener;

/**
 * A listener for {@link Migration} lifecycles.
 *
 * @author Lukas Eder
 */
public interface MigrationListener extends EventListener {

    /**
     * Invoked at the start of a {@link Migration}.
     */
    void migrationStart(MigrationContext ctx);

    /**
     * Invoked at the end of a {@link Migration}.
     */
    void migrationEnd(MigrationContext ctx);

    /**
     * Invoked at the start of a set of {@link Queries} that describe a single version increment.
     */
    void queriesStart(MigrationContext ctx);

    /**
     * Invoked at the end of a set of {@link Queries} that describe a single version increment.
     */
    void queriesEnd(MigrationContext ctx);

    /**
     * Invoked at the start of an individual {@link Query}.
     */
    void queryStart(MigrationContext ctx);

    /**
     * Invoked at the start of an individual {@link Query}.
     */
    void queryEnd(MigrationContext ctx);
}
