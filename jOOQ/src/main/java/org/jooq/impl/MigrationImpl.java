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

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.jooq.impl.DSL.createSchemaIfNotExists;
import static org.jooq.impl.DSL.dropSchemaIfExists;
import static org.jooq.impl.DSL.dropTableIfExists;
import static org.jooq.impl.History.HISTORY;
import static org.jooq.impl.HistoryImpl.initCtx;
import static org.jooq.impl.HistoryResolution.OPEN;
import static org.jooq.impl.HistoryStatus.FAILURE;
import static org.jooq.impl.HistoryStatus.MIGRATING;
import static org.jooq.impl.HistoryStatus.REVERTING;
import static org.jooq.impl.HistoryStatus.STARTING;
import static org.jooq.impl.HistoryStatus.SUCCESS;
import static org.jooq.impl.Tools.map;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import org.jooq.Commit;
import org.jooq.Commits;
import org.jooq.Configuration;
import org.jooq.Constants;
import org.jooq.ContextTransactionalRunnable;
import org.jooq.Files;
import org.jooq.Meta;
import org.jooq.Migration;
import org.jooq.MigrationContext;
import org.jooq.MigrationListener;
import org.jooq.Queries;
import org.jooq.Query;
import org.jooq.Schema;
import org.jooq.Tag;
import org.jooq.exception.DataMigrationException;
import org.jooq.exception.DataMigrationVerificationException;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.StopWatch;
import org.jooq.tools.StringUtils;
import org.jooq.tools.json.JSONArray;

/**
 * @author Lukas Eder
 */
final class MigrationImpl extends AbstractScope implements Migration {

    static final JooqLogger log = JooqLogger.getLogger(Migration.class);
    final HistoryImpl       history;
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
        this.history = new HistoryImpl(configuration());
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
    public final void verify() {
        verify0(migrationContext());
    }

    private final void verify0(DefaultMigrationContext ctx) {
        HistoryRecord currentRecord = history.currentHistoryRecord(false);

        if (currentRecord != null) {
            switch (currentRecord.getStatus()) {
                case FAILURE:
                    throw new DataMigrationVerificationException("Previous migration attempt from " + currentRecord.getMigratedFrom() + " to " + currentRecord.getMigratedTo() + " has failed. Please resolve before migrating.");

                case STARTING:
                case REVERTING:
                case MIGRATING:
                    throw new DataMigrationVerificationException("Ongoing migration from " + currentRecord.getMigratedFrom() + " to " + currentRecord.getMigratedTo() + ". Please wait until it has finished.");
            }

            Commit currentCommit = commits().get(currentRecord.getMigratedTo());

            if (currentCommit == null)
                throw new DataMigrationVerificationException("Version currently installed is not available from CommitProvider: " + currentRecord.getMigratedTo());
        }

        validateCommitProvider(ctx, from());
        validateCommitProvider(ctx, to());
        revertUntracked(ctx, null, currentRecord);
    }

    private final void validateCommitProvider(DefaultMigrationContext ctx, Commit commit) {
        if (commits().get(commit.id()) == null)
            throw new DataMigrationVerificationException("Commit is not available from CommitProvider: " + commit.id());

        for (Schema schema : history.lookup(commit.meta().getSchemas()))
            if (!ctx.migratedSchemas().contains(schema))
                throw new DataMigrationVerificationException(
                    """
                    Schema is referenced from commit, but not configured for migration: {schema}.
                    The commit referencing the schema: {commit}.

                    All schemas that are referenced from commits in a migration must be configured for
                    inclusion in the migration

                    TODO doclink
                    """.replace("{schema}", schema.toString())
                       .replace("{commit}", commit.toString())
                );
    }

    private final Queries revertUntrackedQueries(Set<Schema> includedSchemas) {
        Commit currentCommit = currentCommit();
        Meta currentMeta = currentCommit.meta();
        Meta existingMeta = dsl().meta().filterSchemas(includedSchemas::contains);

        Set<Schema> expectedSchemas = new HashSet<>();
        expectedSchemas.addAll(history.lookup(from().meta().getSchemas()));
        expectedSchemas.addAll(history.lookup(to().meta().getSchemas()));
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
            if (!TRUE.equals(dsl().settings().isMigrationRevertUntracked())) {
                if (currentRecord == null) {
                    throw new DataMigrationVerificationException(
                        """
                        Non-empty difference between actual schema and migration from schema: {queries}.

                        Possible remedies:
                        - Use Settings.migrationAutoBaseline to automatically set a baseline.
                        """.replace("{queries}", "" + ctx.revertUntrackedQueries)
                    );
                }
                else {
                    throw new DataMigrationVerificationException(
                        """
                        Non-empty difference between actual schema and migration from schema: {queries}.

                        This can happen for two reasons:
                        1) The migration specification of a version that has already been installed has been modified.
                        2) The database schemas contain untracked objects.

                        Possible remedies if 1):
                        - Revert changes to the migration specification and move those changes to a new version.

                        Possible remedies if 2):
                        - Use Settings.migrationRevertUntracked to automatically drop unknown objects (at your own risk!)
                        - Manually drop or move unknown objects outside of managed schemas.
                        - Update migration scripts to track missing objects.
                        """.replace("{queries}", "" + ctx.revertUntrackedQueries)
                    );
                }
            }
            else if (listener != null)
                execute(ctx, listener, ctx.revertUntrackedQueries);
    }

    final DefaultMigrationContext migrationContext() {
        Set<Schema> schemas = history.schemas();

        return new DefaultMigrationContext(
            configuration(),
            schemas,
            from(),
            to(),
            queries(),
            revertUntrackedQueries(schemas)
        );
    }

    @Override
    public final void execute() {

        // TODO: Transactions don't really make sense in most dialects. In some, they do
        //       e.g. PostgreSQL supports transactional DDL. Check if we're getting this right.
        run(() -> {
            DefaultMigrationContext ctx = migrationContext();
            MigrationListener listener = new MigrationListeners(configuration);

            if (!FALSE.equals(dsl().settings().isMigrationAutoVerification()))
                verify0(ctx);

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

                log.info("jOOQ Migrations", "Version " + from().id() + " is being migrated to " + to().id());

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
                catch (Exception e) {
                    StringWriter s = new StringWriter();
                    e.printStackTrace(new PrintWriter(s));

                    log.error("jOOQ Migrations", "Version " + from().id() + " migration to " + to().id() + " failed: " + e.getMessage());
                    log(watch, record, FAILURE, OPEN, s.toString());
                    throw new DataMigrationRedoLogException(record, e);
                }
            }
            finally {
                listener.migrationEnd(ctx);
            }
        });
    }

    /**
     * An internal wrapper class for exceptions that allows for re-creating the
     * {@link HistoryRecord} in case it was rolled back.
     */
    static final class DataMigrationRedoLogException extends DataMigrationException {

        final HistoryRecord record;

        public DataMigrationRedoLogException(HistoryRecord record, Exception cause) {
            super("Redo log", cause);

            this.record = record;
        }
    }

    private final HistoryRecord createRecord(HistoryStatus status) {
        HistoryRecord record = history.historyCtx.newRecord(HISTORY);

        record
            .setJooqVersion(Constants.VERSION)
            .setMigratedAt(new Timestamp(dsl().configuration().clock().instant().toEpochMilli()))
            .setMigratedFrom(from().id())
            .setMigratedTo(to().id())
            .setMigratedToTags(new JSONArray(map(to().tags(), Tag::id)).toString())
            .setMigrationTime(0L)
            .setSql(queries().toString())
            .setSqlCount(queries().queries().length)
            .setStatus(status)
            .insert();

        return record;
    }

    private final void log(StopWatch watch, HistoryRecord record, HistoryStatus status) {
        log(watch, record, status, null, null);
    }

    private final void log(StopWatch watch, HistoryRecord record, HistoryStatus status, HistoryResolution resolution, String message) {
        record.setMigrationTime(watch.split() / 1000000L)
              .setStatus(status)
              .setStatusMessage(message)
              .setResolution(resolution)
              .update();
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
        history.init();

        MigrationContext ctx = migrationContext();
        if (TRUE.equals(ctx.settings().isMigrationSchemataCreateSchemaIfNotExists()))
            for (Schema schema : ctx.migratedSchemas())
                dsl().createSchemaIfNotExists(schema).execute();
    }

    final Commit currentCommit() {
        HistoryRecord currentRecord = history.currentHistoryRecord(true);

        if (currentRecord == null) {
            Commit result = TRUE.equals(settings().isMigrationAutoBaseline()) ? to() : to().root();

            if (result == null)
                throw new DataMigrationVerificationException("CommitProvider did not provide a root version for " + to().id());

            return result;
        }
        else {
            Commit result = commits().get(currentRecord.getMigratedTo());

            if (result == null)
                throw new DataMigrationVerificationException("CommitProvider did not provide a version for " + currentRecord.getMigratedTo());

            return result;
        }
    }

    private final void run(final ContextTransactionalRunnable runnable) {
        try {
            init();
            dsl().transaction(runnable);
        }
        catch (DataMigrationRedoLogException e) {

            // [#9506] Make sure history record is re-created in case it was rolled back.
            HistoryRecord record = history.currentHistoryRecord(false);

            if (record == null || !StringUtils.equals(e.record.getId(), record.getId())) {
                e.record.touched(true);
                e.record.insert();
            }

            if (e.getCause() instanceof DataMigrationException r)
                throw r;
            else
                throw new DataMigrationException("Exception during migration", e);
        }
        catch (DataMigrationException e) {
            throw e;
        }
        catch (Exception e) {
            throw new DataMigrationException("Exception during migration", e);
        }
    }

    // -------------------------------------------------------------------------
    // The Object API
    // -------------------------------------------------------------------------

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("-- Migration\n--   From: ").append(from().id()).append("\n")
          .append("--   To  : ").append(to().id()).append("\n")
          .append(queries());

        return sb.toString();
    }
}
