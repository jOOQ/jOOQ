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

import static java.lang.Boolean.TRUE;
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.schema;
import static org.jooq.impl.History.HISTORY;
import static org.jooq.impl.HistoryResolution.OPEN;
import static org.jooq.impl.HistoryResolution.RESOLVED;
import static org.jooq.impl.HistoryStatus.SUCCESS;
import static org.jooq.impl.Tools.isEmpty;
import static org.jooq.tools.StringUtils.defaultIfNull;
import static org.jooq.tools.StringUtils.isBlank;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.jooq.Commit;
import org.jooq.Commits;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.History;
import org.jooq.HistoryVersion;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.Version;
import org.jooq.conf.InterpreterSearchSchema;
import org.jooq.conf.MappedCatalog;
import org.jooq.conf.MappedSchema;
import org.jooq.conf.MigrationSchema;
import org.jooq.conf.RenderMapping;
import org.jooq.exception.DataMigrationVerificationException;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.StringUtils;

import org.jetbrains.annotations.Nullable;

/**
 * @author Lukas Eder
 */
class HistoryImpl extends AbstractScope implements History {

    private static final JooqLogger log = JooqLogger.getLogger(HistoryImpl.class);

    final DSLContext                ctx;
    final DSLContext                historyCtx;
    final Commits                   commits;
    final List<HistoryVersion>      versions;

    HistoryImpl(Configuration configuration) {
        super(configuration);

        // [#9506] TODO: What's the best way to spawn a new JDBC connection from an existing one?
        //         The history interactions should run in an autonomous transaction of the system connection provider
        this.ctx = configuration.dsl();
        this.historyCtx = initCtx(configuration, configuration.settings().getMigrationHistorySchema()).dsl();
        this.commits = configuration.commitProvider().provide();
        this.versions = initVersions();
    }

    @Override
    public final boolean contains(String id) {
        return Tools.anyMatch(versions, v -> v.version().id().equals(id));
    }

    @Override
    public final Iterator<HistoryVersion> iterator() {
        return unmodifiableList(versions).iterator();
    }

    @Override
    public final boolean available() {
        return !isEmpty(versions);
    }

    @Override
    public final HistoryVersion root() {
        if (available())
            return versions.get(0);
        else
            throw new DataMigrationVerificationException("No versions are available");
    }

    @Override
    public final HistoryVersion current() {
        if (available())
            return versions.get(versions.size() - 1);
        else
            throw new DataMigrationVerificationException("No versions are available");
    }

    final Set<Schema> schemas() {
        Set<Schema> set = new LinkedHashSet<>();

        for (MigrationSchema schema : configuration.settings().getMigrationSchemata())
            addSchema(set, schema);

        if (configuration.settings().getMigrationDefaultSchema() != null) {
            addSchema(set, configuration.settings().getMigrationDefaultSchema());
            set.add(DSL.schema(""));
        }

        return set;
    }

    private final void addSchema(Set<Schema> set, MigrationSchema schema) {
        if (schema != null)
            set.addAll(lookup(asList(MigrationImpl.schema(schema))));
    }

    final Collection<Schema> lookup(List<Schema> schemas) {

        // TODO: Refactor usages of getInterpreterSearchPath()
        Collection<Schema> result = schemas;
        List<InterpreterSearchSchema> searchPath = configuration().settings().getInterpreterSearchPath();

        if (!searchPath.isEmpty()) {
            result = new HashSet<>();
            Schema defaultSchema = schema(name(searchPath.get(0).getCatalog(), searchPath.get(0).getSchema()));

            for (Schema schema : schemas)
                if (schema.getQualifiedName().empty())
                    result.add(defaultSchema);
                else
                    result.add(schema);
        }

        return result;
    }

    static final Configuration initCtx(Configuration configuration, MigrationSchema defaultSchema) {
        if (defaultSchema != null) {
            Configuration result = configuration.derive();

            if (!isBlank(defaultSchema.getCatalog())) {
                result.settings().withRenderMapping(new RenderMapping()
                    .withCatalogs(new MappedCatalog()
                        .withInput("")
                        .withOutput(defaultSchema.getCatalog())
                        .withSchemata(new MappedSchema()
                            .withInput("")
                            .withOutput(defaultSchema.getSchema())
                        )
                    )
                );
            }
            else if (!isBlank(defaultSchema.getSchema())) {
                result.settings().withRenderMapping(new RenderMapping()
                    .withSchemata(new MappedSchema()
                        .withInput("")
                        .withOutput(defaultSchema.getSchema())
                    )
                );
            }

            result.settings().withInterpreterSearchPath(new InterpreterSearchSchema()
                .withCatalog(defaultSchema.getCatalog())
                .withSchema(defaultSchema.getSchema())
            );

            return result;
        }
        else
            return configuration;
    }

    @Nullable
    final HistoryRecord currentHistoryRecord(boolean successOnly) {
        return existsHistory()
            ? historyCtx.selectFrom(HISTORY)

                   // TODO: How to recover from failure?
                   .where(successOnly
                       ? HISTORY.STATUS.eq(inline(SUCCESS))
                       : HISTORY.STATUS.eq(inline(SUCCESS)).or(HISTORY.RESOLUTION.eq(OPEN)))
                   .orderBy(HISTORY.MIGRATED_AT.desc(), HISTORY.ID.desc())
                   .limit(1)
                   .fetchOne()
            : null;
    }

    final boolean existsHistory() {
        Table<HistoryRecord> h = historyCtx.map(HISTORY);

        // [#8301] Find a better way to test if our table already exists
        // [#9506] Cannot query table and catch exceptions as that would roll back migration transactions
        return !historyCtx
            .meta()
            .filterSchemas(s -> h.getSchema() == null || s.getName().equals(h.getSchema().getName()))
            .filterTables(t -> t.getName().equals(h.getName()))
            .getTables()
            .isEmpty();
    }

    private final List<HistoryVersion> initVersions() {
        List<HistoryVersion> result = new ArrayList<>();

        if (existsHistory()) {
            result.add(new HistoryVersionImpl(
                this,
                commits.root().version(),
                null
            ));

            for (HistoryRecord r : historyCtx
                .selectFrom(HISTORY)
                .where(HISTORY.STATUS.eq(inline(SUCCESS)))
                .orderBy(HISTORY.ID.asc())
            ) {
                Commit commit = commits.get(r.getMigratedTo());

                if (commit != null)
                    result.add(new HistoryVersionImpl(
                        this,
                        commit.version(),
                        r.getMigratedAt().toInstant()
                    ));
                else
                    throw new DataMigrationVerificationException(
                        """
                        CommitProvider didn't provide version for ID: {id}

                        This may happen if a successful migration has happened in a database, but the source
                        for this migration is not available.
                        """.replace("{id}", r.getMigratedTo())
                    );
            }
        }

        return result;
    }

    final void init() {

        // TODO: What to do when initialising jOOQ-migrations on an existing database?
        //       - Should there be init() commands that can be run explicitly by the user?
        //       - Will we reverse engineer the production Meta snapshot first?
        if (!existsHistory()) {

            // TODO: [#9506] Make this schema creation vendor agnostic
            // TODO: [#15225] This CREATE SCHEMA statement should never be necessary.
            if (TRUE.equals(historyCtx.settings().isMigrationHistorySchemaCreateSchemaIfNotExists())
                && historyCtx.settings().getMigrationHistorySchema() != null
                || TRUE.equals(historyCtx.settings().isMigrationSchemataCreateSchemaIfNotExists())
                && historyCtx.settings().getMigrationDefaultSchema() != null)
                historyCtx.createSchemaIfNotExists("").execute();

            log.info("Initialising history table: " + historyCtx.map(HISTORY));
            historyCtx.meta(HISTORY).ddl().executeBatch();
        }
    }

    @Override
    public final void resolve(String message) {
        HistoryRecord h = currentHistoryRecord(false);

        if (h != null)
            h.setResolution(RESOLVED)
             .setResolutionMessage(message)
             .update();
        else
            throw new DataMigrationVerificationException("No current history record found to resolve");
    }

    static final record HistoryVersionImpl(History history, Version version, Instant migratedAt) implements HistoryVersion {

        @Override
        public int hashCode() {
            return Objects.hash(version);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            HistoryVersionImpl other = (HistoryVersionImpl) obj;
            return Objects.equals(version, other.version);
        }

        @Override
        public String toString() {
            return "HistoryVersion [version=" + version + ", migratedAt=" + migratedAt + "]";
        }
    }

    // -------------------------------------------------------------------------
    // The Object API
    // -------------------------------------------------------------------------

    @Override
    public int hashCode() {
        return versions.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof HistoryImpl h) {
            return versions.equals(h.versions);
        }

        return false;
    }

    @Override
    public String toString() {
        if (available())
            return "History [" + current() + "]";
        else
            return "History []";
    }
}
