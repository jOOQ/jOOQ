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

import org.jooq.exception.DataMigrationException;
import org.jooq.exception.DataMigrationValidationException;

/**
 * An executable migration between two {@link Version} instances.
 *
 * @author Lukas Eder
 */
public interface Migration extends Scope {

    /**
     * The version that is being migrated from.
     */
    Version from();

    /**
     * The version that is being migrated to.
     */
    Version to();

    /**
     * The queries that are executed by the migration.
     */
    Queries queries();

    /**
     * Validate a migration.
     *
     * @throws DataMigrationValidationException When something went wrong during
     *             the validation of the migration.
     */
    void validate() throws DataMigrationValidationException;

    /**
     * Apply the migration.
     *
     * @throws DataMigrationException When something went wrong during the
     *             application of the migration.
     */
    void execute() throws DataMigrationException;
}
