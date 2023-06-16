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
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
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
package org.jooq;

import org.jooq.exception.DataMigrationVerificationException;

import org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.NotNull;

/**
 * The History of {@link Version} elements as installed by previous
 * {@link Migrations}.
 * <p>
 * This is EXPERIMENTAL functionality and subject to change in future jOOQ
 * versions.
 *
 * @author Lukas Eder
 */
@Experimental
public interface History extends Iterable<Version> {

    /**
     * The root {@link Version}.
     * <p>
     * This corresponds to the {@link Configuration#commitProvider()}'s
     * {@link Commits#root()} if migrations have been initialised on the
     * configured database.
     * <p>
     * This is EXPERIMENTAL functionality and subject to change in future jOOQ
     * versions.
     *
     * @throws DataMigrationVerificationException If no root version is
     *             available (e.g. because no migration has happened yet).
     */
    @NotNull
    @Experimental
    Version root() throws DataMigrationVerificationException;

    /**
     * The currently installed {@link Version}.
     * <p>
     * This is EXPERIMENTAL functionality and subject to change in future jOOQ
     * versions.
     *
     * @throws DataMigrationVerificationException If no root version is
     *             available (e.g. because no migration has happened yet).
     */
    @NotNull
    @Experimental
    Version current();

    /**
     * Resolve any previous failures in the {@link History}.
     * <p>
     * This is EXPERIMENTAL functionality and subject to change in future jOOQ
     * versions.
     */
    @Experimental
    void resolve(String message);

}
