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
package org.jooq.impl;

import java.util.Collections;
import java.util.Set;

import org.jooq.Commit;
import org.jooq.Configuration;
import org.jooq.MigrationContext;
import org.jooq.Queries;
import org.jooq.Query;
import org.jooq.Schema;
import org.jooq.impl.MigrationImpl.Untracked;

/**
 * A default implementation for {@link MigrationContext}.
 *
 * @author Lukas Eder
 */
final class DefaultMigrationContext extends AbstractScope implements MigrationContext {

    final Set<Schema> migratedSchemas;
    final Commit      migrationFrom;
    final Commit      migrationTo;
    final Queries     migrationQueries;
    final Queries     untrackedQueries;
    final Queries     revertUntrackedQueries;
    final boolean     baseline;

    Query             query;

    DefaultMigrationContext(
        Configuration configuration,
        Set<Schema> migratedSchemas,
        Commit migrationFrom,
        Commit migrationTo,
        Queries migrationQueries,
        Untracked untracked,
        boolean baseline
    ) {
        super(configuration);

        this.migratedSchemas = migratedSchemas;
        this.migrationFrom = migrationFrom;
        this.migrationTo = migrationTo;
        this.migrationQueries = migrationQueries;
        this.untrackedQueries = untracked.apply();
        this.revertUntrackedQueries = untracked.revert();
        this.baseline = baseline;
    }

    @Override
    public final Set<Schema> migratedSchemas() {
        return Collections.unmodifiableSet(migratedSchemas);
    }

    @Override
    public final Commit migrationFrom() {
        return migrationFrom;
    }

    @Override
    public final Commit migrationTo() {
        return migrationTo;
    }

    @Override
    public final Queries migrationQueries() {
        return migrationQueries;
    }

    @Override
    public final Commit queriesFrom() {
        return migrationFrom;
    }

    @Override
    public final Commit queriesTo() {
        return migrationTo;
    }

    @Override
    public final Queries queries() {
        return migrationQueries;
    }

    @Override
    public final Query query() {
        return query;
    }

    final void query(Query q) {
        this.query = q;
    }

    @Override
    public final boolean baseline() {
        return baseline;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(
            "MigrationContext for migration from: " + migrationFrom + " to " + migrationTo + "\n"
            + "Migration queries:\n" + migrationQueries + "\n"
            + "Revert untracked queries:\n" + revertUntrackedQueries
        );

        return sb.toString();
    }
}
