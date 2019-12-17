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
package org.jooq.impl;

import org.jooq.Configuration;
import org.jooq.MigrationContext;
import org.jooq.Queries;
import org.jooq.Query;
import org.jooq.Version;

/**
 * A default implementation for {@link MigrationContext}.
 *
 * @author Lukas Eder
 */
final class DefaultMigrationContext extends AbstractScope implements MigrationContext {

    final Version migrationFrom;
    final Version migrationTo;
    final Queries migrationQueries;
    final Queries revertUntrackedQueries;

    Query         query;

    DefaultMigrationContext(Configuration configuration, Version migrationFrom, Version migrationTo, Queries migrationQueries, Queries revertUntrackedQueries) {
        super(configuration);

        this.migrationFrom = migrationFrom;
        this.migrationTo = migrationTo;
        this.migrationQueries = migrationQueries;
        this.revertUntrackedQueries = revertUntrackedQueries;
    }

    @Override
    public Version migrationFrom() {
        return migrationFrom;
    }

    @Override
    public Version migrationTo() {
        return migrationTo;
    }

    @Override
    public Queries migrationQueries() {
        return migrationQueries;
    }

    @Override
    public Version queriesFrom() {
        return migrationFrom;
    }

    @Override
    public Version queriesTo() {
        return migrationTo;
    }

    @Override
    public Queries queries() {
        return migrationQueries;
    }

    @Override
    public Query query() {
        return query;
    }

    void query(Query q) {
        this.query = q;
    }
}
