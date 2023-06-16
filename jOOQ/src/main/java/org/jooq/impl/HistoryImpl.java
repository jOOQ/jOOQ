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
package org.jooq.impl;

import static java.lang.Boolean.TRUE;
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.noCondition;
import static org.jooq.impl.DSL.schema;
import static org.jooq.impl.History.HISTORY;
import static org.jooq.impl.MigrationImpl.Resolution.OPEN;
import static org.jooq.impl.MigrationImpl.Resolution.RESOLVED;
import static org.jooq.impl.MigrationImpl.Status.SUCCESS;
import static org.jooq.impl.Tools.isEmpty;
import static org.jooq.tools.StringUtils.isBlank;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.jooq.Commit;
import org.jooq.Commits;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.History;
import org.jooq.Schema;
import org.jooq.Version;
import org.jooq.conf.InterpreterSearchSchema;
import org.jooq.conf.MappedCatalog;
import org.jooq.conf.MappedSchema;
import org.jooq.conf.MigrationSchema;
import org.jooq.conf.RenderMapping;
import org.jooq.exception.DataAccessException;
import org.jooq.exception.DataMigrationVerificationException;
import org.jooq.impl.MigrationImpl.Resolution;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Experimental;

/**
 * @author Lukas Eder
 */
class HistoryImpl extends AbstractScope implements History {

    final DSLContext    ctx;
    final DSLContext    historyCtx;
    final Commits       commits;
    final List<Version> versions;

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
    public final Iterator<Version> iterator() {
        return unmodifiableList(versions).iterator();
    }

    @Override
    public final Version root() {
        if (!isEmpty(versions))
            return versions.get(0);
        else
            throw new DataMigrationVerificationException("No versions are available");
    }

    @Override
    public final Version current() {
        if (!isEmpty(versions))
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
            set.addAll(lookup(asList(schema(name(schema.getCatalog(), schema.getSchema())))));
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

        // [#8301] Find a better way to test if our table already exists
        try {
            Configuration c = historyCtx
                .configuration()
                .derive();
            c.data("org.jooq.tools.LoggerListener.exception.mute", true);
            c.dsl().fetchExists(HISTORY);
            return true;
        }
        catch (DataAccessException ignore) {}

        return false;
    }

    private final List<Version> initVersions() {
        List<Version> result = new ArrayList<>();

        if (existsHistory()) {
            result.add(commits.root().version());

            for (HistoryRecord r : historyCtx
                .selectFrom(HISTORY)
                .where(HISTORY.STATUS.eq(inline(SUCCESS)))
                .orderBy(HISTORY.ID.asc())
            ) {
                Commit commit = commits.get(r.getMigratedTo());

                if (commit != null)
                    result.add(commit.version());
                else
                    throw new DataMigrationVerificationException("CommitProvider didn't provide version for ID: " + r.getMigratedTo());
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
        return "History [" + current() + "]";
    }
}
