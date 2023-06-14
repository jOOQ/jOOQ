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

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Arrays.asList;
import static org.jooq.impl.DSL.createSchemaIfNotExists;
import static org.jooq.impl.DSL.dropSchemaIfExists;
import static org.jooq.impl.DSL.dropTableIfExists;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.schema;
import static org.jooq.impl.History.HISTORY;
import static org.jooq.impl.MigrationImpl.Status.FAILURE;
import static org.jooq.impl.MigrationImpl.Status.MIGRATING;
import static org.jooq.impl.MigrationImpl.Status.REVERTING;
import static org.jooq.impl.MigrationImpl.Status.STARTING;
import static org.jooq.impl.MigrationImpl.Status.SUCCESS;
import static org.jooq.tools.StringUtils.isBlank;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.jooq.Commit;
import org.jooq.Commits;
import org.jooq.Configuration;
import org.jooq.Constants;
import org.jooq.ContextTransactionalRunnable;
import org.jooq.DSLContext;
import org.jooq.Files;
import org.jooq.Meta;
import org.jooq.Migration;
import org.jooq.MigrationContext;
import org.jooq.MigrationListener;
import org.jooq.Queries;
import org.jooq.Query;
import org.jooq.Schema;
import org.jooq.conf.InterpreterSearchSchema;
import org.jooq.conf.MappedCatalog;
import org.jooq.conf.MappedSchema;
import org.jooq.conf.MigrationSchema;
import org.jooq.conf.RenderMapping;
import org.jooq.exception.DataAccessException;
import org.jooq.exception.DataMigrationException;
import org.jooq.exception.DataMigrationValidationException;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.StopWatch;

/**
 * @author Lukas Eder
 */
final class MigrationImpl extends AbstractScope implements Migration {

    static final JooqLogger log       = JooqLogger.getLogger(Migration.class);
    final DSLContext        historyCtx;
    final Commit            to;
    Commit                  from;
    Queries                 queries;
    Commits                 commits;

    MigrationImpl(Configuration configuration, Commit to) {
        super(initCtx(
            configuration.derive(new ThreadLocalTransactionProvider(configuration.systemConnectionProvider())),
            configuration.settings().getMigrationDefaultSchema()
        ));

        this.to = to;
        this.historyCtx = initCtx(configuration(), configuration.settings().getMigrationHistorySchema()).dsl();
    }

    private static final Configuration initCtx(Configuration configuration, MigrationSchema defaultSchema) {
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

    @Override
    public final Commit from() {
        if (from == null)

            // TODO: Use pessimistic locking so no one else can migrate in between
            from = currentCommit();

        return from;
    }

    @Override
    public final Commit to() {
        return to;
    }

    @Override
    public final Queries queries() {
        if (queries == null) {
            Files files = from().migrateTo(to());
            queries = files.from().migrateTo(files.to());
        }

        return queries;
    }

    private final Commits commits() {
        if (commits == null)
            commits = configuration().commitProvider().provide();

        return commits;
    }

    @Override
    public final void validate() {
        validate0(migrationContext());
    }

    private final void validate0(DefaultMigrationContext ctx) {
        HistoryRecord currentRecord = currentHistoryRecord();

        if (currentRecord != null) {
            Commit currentCommit = commits().get(currentRecord.getMigratedTo());

            if (currentCommit == null)
                throw new DataMigrationValidationException("Version currently installed is not available from CommitProvider: " + currentRecord.getMigratedTo());
        }

        validateCommitProvider(ctx, from());
        validateCommitProvider(ctx, to());
        revertUntracked(ctx, null, currentRecord);
    }

    private final void validateCommitProvider(DefaultMigrationContext ctx, Commit commit) {
        if (commits().get(commit.id()) == null)
            throw new DataMigrationValidationException("Commit is not available from CommitProvider: " + commit.id());

        for (Schema schema : lookup(commit.meta().getSchemas()))
            if (!ctx.migratedSchemas().contains(schema))
                throw new DataMigrationValidationException("Schema is referenced from commit, but not configured for migration: " + schema);
    }

    private final Collection<Schema> lookup(List<Schema> schemas) {

        // TODO: Refactor usages of getInterpreterSearchPath()
        Collection<Schema> result = schemas;
        List<InterpreterSearchSchema> searchPath = dsl().settings().getInterpreterSearchPath();

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

    private final Queries revertUntrackedQueries(Set<Schema> includedSchemas) {
        Commit currentCommit = currentCommit();
        Meta currentMeta = currentCommit.meta();
        Meta existingMeta = dsl().meta().filterSchemas(includedSchemas::contains);

        Set<Schema> expectedSchemas = new HashSet<>();
        expectedSchemas.addAll(lookup(from().meta().getSchemas()));
        expectedSchemas.addAll(lookup(to().meta().getSchemas()));
        expectedSchemas.retainAll(includedSchemas);

        schemaLoop:
        for (Schema schema : existingMeta.getSchemas()) {
            if (!includedSchemas.contains(schema))
                continue schemaLoop;

            // TODO Why is this qualification necessary?
            existingMeta = existingMeta.apply(dropTableIfExists(schema.getQualifiedName().append(HISTORY.getUnqualifiedName())).cascade());

            if (!expectedSchemas.contains(schema))
                existingMeta = existingMeta.apply(dropSchemaIfExists(schema).cascade());
            else
                currentMeta = currentMeta.apply(createSchemaIfNotExists(schema));
        }

        return existingMeta.migrateTo(currentMeta);
    }

    private final void revertUntracked(DefaultMigrationContext ctx, MigrationListener listener, HistoryRecord currentRecord) {
        if (ctx.revertUntrackedQueries.queries().length > 0)
            if (!TRUE.equals(dsl().settings().isMigrationRevertUntracked()))
                throw new DataMigrationValidationException(
                    "Non-empty difference between actual schema and migration from schema: " + ctx.revertUntrackedQueries +
                    (currentRecord == null ? ("\n\nUse Settings.migrationAutoBaseline to automatically set a baseline") : "")
                );
            else if (listener != null)
                execute(ctx, listener, ctx.revertUntrackedQueries);
    }

    private final DefaultMigrationContext migrationContext() {
        Set<Schema> schemas = schemas();

        return new DefaultMigrationContext(
            configuration(),
            schemas,
            from(),
            to(),
            queries(),
            revertUntrackedQueries(schemas)
        );
    }

    private final Set<Schema> schemas() {
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

    @Override
    public final void execute() {

        // TODO: Transactions don't really make sense in most dialects. In some, they do
        //       e.g. PostgreSQL supports transactional DDL. Check if we're getting this right.
        run(new ContextTransactionalRunnable() {
            @Override
            public void run() {
                DefaultMigrationContext ctx = migrationContext();
                MigrationListener listener = new MigrationListeners(configuration);

                if (!FALSE.equals(dsl().settings().isMigrationAutoValidation()))
                    validate0(ctx);

                try {
                    listener.migrationStart(ctx);

                    if (from().equals(to())) {
                        log.info("jOOQ Migrations", "Version " + to().id() + " is already installed as the current version.");
                        return;
                    }

                    // TODO: Implement preconditions
                    // TODO: Implement a listener with a variety of pro / oss features
                    // TODO: Implement additional out-of-the-box sanity checks
                    // TODO: Allow undo migrations only if enabled explicitly
                    // TODO: Add some migration settings, e.g. whether HISTORY.SQL should be filled
                    // TODO: Migrate the HISTORY table with the Migration API
                    // TODO: Create an Enum for HISTORY.STATUS
                    // TODO: Add HISTORY.USERNAME and HOSTNAME columns
                    // TODO: Add HISTORY.COMMENTS column
                    // TODO: Replace (MIGRATED_AT, MIGRATION_TIME) by (MIGRATION_START, MIGRATION_END)

                    log.info("jOOQ Migrations", "Version " + from().id() + " is migrated to " + to().id());

                    StopWatch watch = new StopWatch();

                    // TODO: Make logging configurable
                    if (log.isDebugEnabled())
                        for (Query query : queries())
                            log.debug("jOOQ Migrations", dsl().renderInlined(query));

                    HistoryRecord record = createRecord(STARTING);

                    try {
                        log(watch, record, REVERTING);
                        revertUntracked(ctx, listener, record);
                        log(watch, record, MIGRATING);
                        execute(ctx, listener, queries());
                        log(watch, record, SUCCESS);
                    }
                    catch (DataAccessException e) {

                        // TODO: Make sure this is committed, given that we're re-throwing the exception.
                        // TODO: How can we recover from failure?
                        log(watch, record, FAILURE);
                        throw e;
                    }
                }
                finally {
                    listener.migrationEnd(ctx);
                }
            }

            private final HistoryRecord createRecord(Status status) {
                HistoryRecord record = historyCtx.newRecord(HISTORY);

                record
                    .setJooqVersion(Constants.VERSION)
                    .setMigratedAt(new Timestamp(dsl().configuration().clock().instant().toEpochMilli()))
                    .setMigratedFrom(from().id())
                    .setMigratedTo(to().id())
                    .setMigrationTime(0L)
                    .setSql(queries().toString())
                    .setSqlCount(queries().queries().length)
                    .setStatus(status)
                    .insert();

                return record;
            }

            private final void log(StopWatch watch, HistoryRecord record, Status status) {
                record.setMigrationTime(watch.split() / 1000000L)
                      .setStatus(status)
                      .update();
            }
        });
    }

    private final void execute(DefaultMigrationContext ctx, MigrationListener listener, Queries q) {
        // TODO: Can we access the individual Queries from Version, if applicable?
        // TODO: Set the ctx.queriesFrom(), ctx.queriesTo(), and ctx.queries() values
        listener.queriesStart(ctx);

        // TODO: Make batching an option: queries().executeBatch();
        for (Query query : q.queries()) {
            ctx.query(query);
            listener.queryStart(ctx);
            query.execute();
            listener.queryEnd(ctx);
            ctx.query(null);
        }

        listener.queriesEnd(ctx);
    }

    /**
     * Initialise the underlying {@link Configuration} with the jOOQ Migrations
     * History.
     */
    final void init() {

        // TODO: What to do when initialising jOOQ-migrations on an existing database?
        //       - Should there be init() commands that can be run explicitly by the user?
        //       - Will we reverse engineer the production Meta snapshot first?
        if (!existsHistory()) {

            // TODO: [#9506] Make this schema creation vendor agnostic
            // TODO: [#15225] This CREATE SCHEMA statement should never be necessary.
            if (TRUE.equals(historyCtx.settings().isMigrationHistorySchemaCreateSchemaIfNotExists())
                && historyCtx.settings().getMigrationHistorySchema() != null)
                historyCtx.createSchemaIfNotExists("").execute();

            historyCtx.meta(HISTORY).ddl().executeBatch();
        }

        MigrationContext ctx = migrationContext();
        if (TRUE.equals(ctx.settings().isMigrationSchemataCreateSchemaIfNotExists()))
            for (Schema schema : ctx.migratedSchemas())
                dsl().createSchemaIfNotExists(schema).execute();
    }

    private final boolean existsHistory() {

        // [#8301] Find a better way to test if our table already exists
        try {
            historyCtx.fetchExists(HISTORY);
            return true;
        }
        catch (DataAccessException ignore) {}

        return false;
    }

    private final HistoryRecord currentHistoryRecord() {
        return existsHistory()
            ? historyCtx.selectFrom(HISTORY)

                   // TODO: How to recover from failure?
                   .where(HISTORY.STATUS.eq(inline(SUCCESS)))
                   .orderBy(HISTORY.MIGRATED_AT.desc(), HISTORY.ID.desc())
                   .limit(1)
                   .fetchOne()
            : null;
    }

    final Commit currentCommit() {
        HistoryRecord currentRecord = currentHistoryRecord();

        if (currentRecord == null) {
            Commit result = TRUE.equals(settings().isMigrationAutoBaseline()) ? to() : to().root();

            if (result == null)
                throw new DataMigrationValidationException("CommitProvider did not provide a root version for " + to().id());

            return result;
        }
        else {
            Commit result = commits().get(currentRecord.getMigratedTo());

            if (result == null)
                throw new DataMigrationValidationException("CommitProvider did not provide a version for " + currentRecord.getMigratedTo());

            return result;
        }
    }

    private final void run(final ContextTransactionalRunnable runnable) {
        try {
            init();
            dsl().transaction(runnable);
        }
        catch (DataMigrationException e) {
            throw e;
        }
        catch (Exception e) {
            throw new DataMigrationException("Exception during migration", e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("-- Migration\n--   From: ").append(from().id()).append("\n")
          .append("--   To  : ").append(to().id()).append("\n")
          .append(queries());

        return sb.toString();
    }

    enum Status {
        STARTING,
        REVERTING,
        MIGRATING,
        SUCCESS,
        FAILURE
    }
}
