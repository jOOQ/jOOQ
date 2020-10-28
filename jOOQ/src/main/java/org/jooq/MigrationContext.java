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

import java.util.Set;

import org.jooq.conf.Settings;

import org.jetbrains.annotations.NotNull;

/**
 * The context in which a {@link Migration} is executed.
 * <p>
 * This is EXPERIMENTAL functionality and subject to change in future jOOQ
 * versions.
 *
 * @see MigrationListener
 * @author Lukas Eder
 */
@Internal
public interface MigrationContext extends Scope {

    /**
     * The set of schemas that are being migrated, as specified in
     * {@link Settings#getMigration()}.
     * <p>
     * This is available on all {@link MigrationListener} events.
     */
    @NotNull
    Set<Schema> migratedSchemas();

    /**
     * The {@link Commit} from which a {@link Migration} has started.
     * <p>
     * {@link #migrationFrom()} and {@link #migrationTo()} versions need not be
     * consecutive versions for any given migration. If a migration jumps a few
     * versions, these two methods will only return the endpoints.
     * <p>
     * This is available on all {@link MigrationListener} events.
     */
    @NotNull
    Commit migrationFrom();

    /**
     * The {@link Commit} to which a {@link Migration} is headed.
     * <p>
     * {@link #migrationFrom()} and {@link #migrationTo()} versions need not be
     * consecutive versions for any given migration. If a migration jumps a few
     * versions, these two methods will only return the endpoints.
     * <p>
     * This is available on all {@link MigrationListener} events.
     */
    @NotNull
    Commit migrationTo();

    /**
     * The complete set of {@link Queries} that are executed between
     * {@link #migrationFrom()} and {@link #migrationTo()}.
     * <p>
     * This is available on all {@link MigrationListener} events.
     */
    @NotNull
    Queries migrationQueries();

    /**
     * The {@link Version} from which an individual set of {@link Queries} has
     * started.
     * <p>
     * {@link #queriesFrom()} and {@link #queriesTo()} versions are consecutive
     * versions in a migration. If a migration jumps a few versions, these two
     * methods might return those intermediate versions on these events:
     * <p>
     * <ul>
     * <li>{@link MigrationListener#queriesStart(MigrationContext)}</li>
     * <li>{@link MigrationListener#queriesEnd(MigrationContext)}</li>
     * <li>{@link MigrationListener#queryStart(MigrationContext)}</li>
     * <li>{@link MigrationListener#queryEnd(MigrationContext)}</li>
     * </ul>
     */
    @NotNull
    Commit queriesFrom();

    /**
     * The {@link Version} to which an individual set of {@link Queries} is
     * headed.
     * <p>
     * {@link #queriesFrom()} and {@link #queriesTo()} versions are consecutive
     * versions in a migration. If a migration jumps a few versions, these two
     * methods might return those intermediate versions on these events:
     * <p>
     * <ul>
     * <li>{@link MigrationListener#queriesStart(MigrationContext)}</li>
     * <li>{@link MigrationListener#queriesEnd(MigrationContext)}</li>
     * <li>{@link MigrationListener#queryStart(MigrationContext)}</li>
     * <li>{@link MigrationListener#queryEnd(MigrationContext)}</li>
     * </ul>
     */
    @NotNull
    Commit queriesTo();

    /**
     * The complete set of {@link Queries} that are executed between
     * {@link #queriesFrom()} and {@link #queriesTo()}.
     * <p>
     * This is available on the same {@link MigrationListener} events as
     * {@link #queriesFrom()} and {@link #queriesTo()}.
     */
    @NotNull
    Queries queries();

    /**
     * The current {@link Query} that is being executed.
     * <p>
     * This is available on
     * {@link MigrationListener#queryStart(MigrationContext)} and
     * {@link MigrationListener#queryEnd(MigrationContext)}.
     */
    @NotNull
    Query query();
}
