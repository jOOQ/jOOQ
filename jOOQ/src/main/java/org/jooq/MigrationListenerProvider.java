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

import org.jooq.impl.DefaultMigrationListenerProvider;

/**
 * A provider for {@link MigrationListener} instances.
 * <p>
 * In order to facilitate the lifecycle management of
 * <code>MigrationListener</code> instances that are provided to a jOOQ
 * {@link Configuration}, clients can implement this API. To jOOQ, it is thus
 * irrelevant, if migration listeners are stateful or stateless, local to an
 * execution, or global to an application.
 * <p>
 * This is EXPERIMENTAL functionality and subject to change in future jOOQ
 * versions.
 *
 * @author Lukas Eder
 * @see MigrationListener
 * @see Configuration
 */
@FunctionalInterface
@Internal
public interface MigrationListenerProvider {

    /**
     * Provide an <code>MigrationListener</code> instance.
     * <p>
     * Implementations are free to choose whether this method returns new
     * instances at every call or whether the same instance is returned
     * repetitively.
     * <p>
     * An <code>MigrationListener</code> shall be provided exactly once per {@link Migration}
     * execution lifecycle, i.e. per <code>MigrationContext</code>.
     *
     * @return An <code>MigrationListener</code> instance.
     * @see MigrationListener
     * @see MigrationContext
     * @see DefaultMigrationListenerProvider
     */
    MigrationListener provide();
}
