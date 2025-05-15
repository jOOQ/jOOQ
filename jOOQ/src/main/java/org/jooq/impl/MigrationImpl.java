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
import static org.jooq.ContentType.SCRIPT;
import static org.jooq.impl.DSL.createSchemaIfNotExists;
import static org.jooq.impl.DSL.dropSchemaIfExists;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.History.HISTORY;
import static org.jooq.impl.HistoryImpl.initCtx;
import static org.jooq.impl.HistoryResolution.OPEN;
import static org.jooq.impl.HistoryStatus.FAILURE;
import static org.jooq.impl.HistoryStatus.MIGRATING;
import static org.jooq.impl.HistoryStatus.REVERTING;
import static org.jooq.impl.HistoryStatus.STARTING;
import static org.jooq.impl.HistoryStatus.SUCCESS;
import static org.jooq.impl.SchemaImpl.DEFAULT_SCHEMA;
import static org.jooq.impl.Tools.collect;
import static org.jooq.impl.Tools.map;
import static org.jooq.tools.StringUtils.isEmpty;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jooq.Commit;
import org.jooq.Commits;
import org.jooq.Configuration;
import org.jooq.Constants;
import org.jooq.ContextTransactionalRunnable;
import org.jooq.File;
import org.jooq.Files;
import org.jooq.HistoryVersion;
import org.jooq.Meta;
import org.jooq.Migration;
import org.jooq.MigrationContext;
import org.jooq.MigrationListener;
import org.jooq.Queries;
import org.jooq.Query;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.Tag;
import org.jooq.Version;
import org.jooq.conf.MigrationSchema;
import org.jooq.exception.DataAccessException;
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

    static final JooqLogger log = JooqLogger.getLogger(MigrationImpl.class);
    final HistoryImpl       history;
    final Commit            to;
    CurrentCommit           from;
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

    static final Schema schema(MigrationSchema schema) {
        return new SchemaImpl(name(schema.getCatalog(), schema.getSchema()));
    }

    @Override
    public final Commit from() {
        return from0(null).commit();
    }

    final CurrentCommit from0(Commit baseline) {
        if (baseline != null)
            return new CurrentCommit(baseline, false);

        // TODO: Use pessimistic locking so no one else can migrate in between
        if (from == null)
            from = currentCommit(baseline);

        return from;
    }

    @Override
    public final Commit fromSnapshot() {
        if (configuration().commercial()) {




        }

        return null;
    }

    @Override
    public final Commit to() {
        return to;
    }

    @Override
    public final Queries queries() {
        if (queries == null)
            queries = queries0(from());

        return queries;
    }

    final Queries queries0(Commit baseline) {
        Files files = (baseline != null ? baseline : from()).migrateTo(to());
        return files.from().migrateTo(files.to());
    }

    private final Commits commits() {
        if (commits == null)
            commits = configuration().commitProvider().provide();

        return commits;
    }

    @Override
    public final Queries untracked() {
        return untracked(null, history.schemas()).apply();
    }

    @Override
    public final void verify() {
        verify0(migrationContext(null));
    }

    @Override
    public void baseline() {
        baseline(commits().current());
    }

    @Override
    public void baseline(Commit commit) {
        execute0(commit);
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

        if (!to().valid() && !TRUE.equals(ctx.settings().isMigrationAllowInvalidCommits()))
            throw new DataMigrationVerificationException(
                """
                Commit is not a valid commit to migrate to: {commit}
                Invalid commits include:
                - Uncommitted or untracked changes in the GitCommitProvider
                - Commits leading to inconsistent migration states due to editing of commit paths
                """.replace("{commit}", to().id())
            );
    }

    private final void validateCommitProvider(DefaultMigrationContext ctx, Commit commit) {
        if (commits().get(commit.id()) == null)
            throw new DataMigrationVerificationException("Commit is not available from CommitProvider: " + commit.id());

        for (Schema schema : history.lookup(commit.meta().getSchemas()))
            if (!isEmpty(schema.getName()) && !ctx.migratedSchemas().contains(schema))
                throw new DataMigrationVerificationException(
                    """
                    Schema is referenced from commit, but not configured for migration: {schema}.
                    The commit referencing the schema: {commit}.

                    All schemas that are referenced from commits in a migration must be configured for
                    inclusion in the migration.
                    """.replace("{schema}", schema.toString())
                       .replace("{commit}", commit.toString())
                );
    }

    static final record Untracked(Configuration configuration, Meta current, Meta existing) {
        Queries revert() {
            if (existing() == null)
                return configuration().dsl().queries();
            else
                return existing().migrateTo(current());
        }

        Queries apply() {
            if (current() == null)
                return configuration().dsl().queries();
            else
                return current().migrateTo(existing());
        }
    }

    private final Untracked untracked(Commit baseline, Set<Schema> includedSchemas) {
        if (scriptsOnly())
            return new Untracked(configuration(), null, null);

        MigrationSchema hs = settings().getMigrationHistorySchema();
        MigrationSchema ds = settings().getMigrationDefaultSchema();

        Set<Table<?>> historyTables = new HashSet<>();

        if (hs != null || ds != null)
            historyTables.add(table(schema(hs != null ? hs : ds).getQualifiedName().append(HISTORY.getUnqualifiedName())));
        else
            historyTables.addAll(map(includedSchemas, s -> table(s.getQualifiedName().append(HISTORY.getUnqualifiedName()))));

        Commit currentCommit = currentCommit(baseline).commit();
        Meta currentMeta = currentCommit.meta();
        Meta existingMeta = dsl().meta()
            .filterSchemas(includedSchemas::contains)
            .filterTables(t -> !historyTables.contains(t));

        Set<Schema> expectedSchemas = new HashSet<>();
        expectedSchemas.addAll(history.lookup(from().meta().getSchemas()));
        expectedSchemas.addAll(history.lookup(to().meta().getSchemas()));
        expectedSchemas.retainAll(includedSchemas);

        if (ds != null) {
            Schema d = DEFAULT_SCHEMA.get();

            if (expectedSchemas.contains(d) && includedSchemas.contains(d))
                expectedSchemas.add(schema(ds));
        }

        schemaLoop:
        for (Schema schema : existingMeta.getSchemas()) {
            if (!includedSchemas.contains(schema))
                continue schemaLoop;

            if (!expectedSchemas.contains(schema))
                existingMeta = existingMeta.apply(dropSchemaIfExists(schema).cascade());
            else
                currentMeta = currentMeta.apply(createSchemaIfNotExists(schema));
        }

        return new Untracked(configuration(), currentMeta, existingMeta);
    }

    private final boolean scriptsOnly() {
        for (Commit commit : commits())
            for (File file : commit.delta())
                if (file.type() != SCRIPT)
                    return false;

        return true;
    }

    private final void revertUntracked(DefaultMigrationContext ctx, MigrationListener listener, HistoryRecord currentRecord) {
        if (ctx.revertUntrackedQueries.queries().length > 0)
            if (!TRUE.equals(dsl().settings().isMigrationRevertUntracked())) {
                if (currentRecord == null) {
                    throw new DataMigrationVerificationException(
                        """
                        Non-empty difference between actual schema and migration from schema:
                        {queries}

                        Possible remedies:
                        - Use Settings.migrationAutoBaseline or the baseline command to automatically set a baseline.
                        """.replace("{queries}", "" + ctx.revertUntrackedQueries)
                    );
                }
                else if (!ctx.migrationFrom.fromHistory) {
                    throw new DataMigrationVerificationException(
                        """
                        Non-empty difference between actual schema and migration from schema:
                        {queries}.

                        Setting a baseline can fail for at least 3 reasons:
                        1) The migration specification of a version that has already been installed has been modified.
                        2) The baseline version {from} does not correspond to the actual database version.
                        3) The database schemas contain untracked objects.
                        4) There's a false positive reported by the database / org.jooq.Meta. Please consider reporting
                           it here: https://jooq.org/bug

                        Possible remedies if 1):
                        - Revert changes to the migration specification and move those changes to a new version.

                        Possible remedies if 2):
                        - Specify the correct baseline version that corresponds to the actual database version.

                        Possible remedies if 3):
                        - Use Settings.migrationRevertUntracked to automatically drop unknown objects (at your own risk!)
                        - Manually drop or move unknown objects outside of managed schemas.
                        - Update migration scripts to track missing objects (including adding them automatically).
                        """.replace("{queries}", "" + ctx.revertUntrackedQueries)
                           .replace("{from}", "" + ctx.migrationFrom.commit().id())
                    );
                }
                else {
                    throw new DataMigrationVerificationException(
                        """
                        Non-empty difference between actual schema and migration from schema:
                        {queries}.

                        This can happen for at least 3 reasons:
                        1) The migration specification of a version that has already been installed has been modified.
                        2) The database schemas contain untracked objects.
                        3) There's a false positive reported by the database / org.jooq.Meta. Please consider reporting
                           it here: https://jooq.org/bug

                        Possible remedies if 1):
                        - Revert changes to the migration specification and move those changes to a new version.

                        Possible remedies if 2):
                        - Use Settings.migrationRevertUntracked to automatically drop unknown objects (at your own risk!)
                        - Manually drop or move unknown objects outside of managed schemas.
                        - Update migration scripts to track missing objects (including adding them automatically).
                        """.replace("{queries}", "" + ctx.revertUntrackedQueries)
                    );
                }
            }
            else if (listener != null)
                execute(ctx, listener, ctx.revertUntrackedQueries);
    }

    final DefaultMigrationContext migrationContext(Commit baseline) {
        Set<Schema> schemas = history.schemas();

        return new DefaultMigrationContext(
            configuration(),
            schemas,
            from0(baseline),
            to(),
            queries0(baseline),
            untracked(baseline, schemas).revert()
        );
    }

    @Override
    public final void execute() {
        execute0(null);
    }

    void execute0(Commit baseline) {

        // TODO: Transactions don't really make sense in most dialects. In some, they do
        //       e.g. PostgreSQL supports transactional DDL. Check if we're getting this right.
        run(() -> {
            DefaultMigrationContext ctx = migrationContext(baseline);
            Commit from0 = ctx.migrationFrom.commit();
            MigrationListener listener = new MigrationListeners(configuration);

            if (!FALSE.equals(dsl().settings().isMigrationAutoVerification()))
                verify0(ctx);

            init();

            try {
                listener.migrationStart(ctx);

                // [#9506] Can't use baseline here, because it can be null when Settings.migrationAutoBaseline is set
                if (!ctx.migrationFrom.fromHistory) {
                    if (history.available() && history.current().version().id().equals(from0.id())) {
                        if (log.isInfoEnabled())
                            log.info("Current version is already set to baseline version: " + from0.id());

                        return;
                    }

                    if (log.isInfoEnabled())
                        log.info("Setting baseline to " + from0.id());

                    createRecord(SUCCESS, commits().root(), from0, "New baseline");
                    return;
                }

                if (from0.equals(to())) {
                    if (!ctx.migrationFrom.fromHistory && log.isInfoEnabled())
                        log.info("Version " + to().id() + " is already installed as the current version.");

                    return;
                }

                // TODO: Implement preconditions
                // TODO: Implement a listener with a variety of pro / oss features
                // TODO: Implement additional out-of-the-box sanity checks
                // TODO: Add some migration settings, e.g. whether HISTORY.SQL should be filled
                // TODO: Migrate the HISTORY table with the Migration API

                if (log.isInfoEnabled()) {
                    Commit snapshot = fromSnapshot();
                    log.info("Version " + from0.id() + " is being migrated to " + to().id() + (snapshot != null ? " (from snapshot: " + snapshot.id() + ")" : ""));
                }

                StopWatch watch = new StopWatch();

                // TODO: Make logging configurable
                if (log.isDebugEnabled())
                    for (Query query : queries())
                        log.debug(dsl().renderInlined(query));

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

                    if (log.isErrorEnabled()) {
                        log.error("Version " + from0.id() + " migration to " + to().id() + " failed: " + e.getMessage());

                        if (!ctx.migrationFrom.fromHistory)
                            log.error("Couldn't migrate from baseline version: " + ctx.migrationFrom.commit().id() + ". Consider specifying an alternative baseline version, instead.");
                    }

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
        return createRecord(status, from(), to());
    }

    private final HistoryRecord createRecord(HistoryStatus status, Commit from0, Commit to0) {
        return createRecord(status, from0, to0, null);
    }

    private final HistoryRecord createRecord(HistoryStatus status, Commit from0, Commit to0, String message) {
        HistoryRecord record = history.historyCtx.newRecord(HISTORY);
        String hostName;

        try {
            hostName = InetAddress.getLocalHost().getHostName();
        }
        catch (UnknownHostException e) {
            hostName = "unknown";
        }

        record
            .setJooqVersion(Constants.VERSION)
            .setMigratedAt(new Timestamp(dsl().configuration().clock().instant().toEpochMilli()))
            .setMigratedFrom(from0.id())
            .setMigratedTo(to0.id())
            .setMigratedToMessage(to0.message())
            .setMigratedToTags(new JSONArray(map(to0.tags(), Tag::id)).toString())
            .setMigrationTime(0L)
            .setClientUserName(System.getProperty("user.name"))
            .setClientHostName(hostName)
            .setSql(queries().toString())
            .setSqlCount(queries().queries().length)
            .setStatus(status)
            .setStatusMessage(message)
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

        MigrationContext ctx = migrationContext(null);
        if (TRUE.equals(ctx.settings().isMigrationSchemataCreateSchemaIfNotExists()))
            for (Schema schema : ctx.migratedSchemas())
                dsl().createSchemaIfNotExists(schema).execute();
    }

    static final record CurrentCommit(Commit commit, boolean fromHistory) {}

    final CurrentCommit currentCommit(Commit baseline) {
        if (baseline != null)
            return new CurrentCommit(baseline, false);

        HistoryRecord currentRecord = history.currentHistoryRecord(true);

        if (currentRecord == null) {
            CurrentCommit result = TRUE.equals(settings().isMigrationAutoBaseline())
                ? new CurrentCommit(to(), false)
                : new CurrentCommit(to().root(), true);

            if (result.commit() == null)
                throw new DataMigrationVerificationException("CommitProvider did not provide a current version for " + to().id());

            return result;
        }
        else {
            Commit result = commits().get(currentRecord.getMigratedTo());

            if (result == null)
                throw new DataMigrationVerificationException("CommitProvider did not provide a current version for " + currentRecord.getMigratedTo());

            return new CurrentCommit(result, true);
        }
    }

    private final void run(final ContextTransactionalRunnable runnable) {
        try {
            dsl().transaction(runnable);
        }
        catch (DataMigrationRedoLogException e) {
            try {

                // [#9506] Make sure history record is re-created in case it was rolled back.
                HistoryRecord record = history.currentHistoryRecord(false);

                if (record == null || !StringUtils.equals(e.record.getId(), record.getId())) {
                    e.record.touched(true);
                    e.record.insert();
                }
            }
            catch (DataAccessException s) {
                e.addSuppressed(s);
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

    @Override
    public final void logHistory() {
        List<HistoryVersion> versions = collect(() -> history.iterator());

        if (versions.isEmpty()) {
            log.info("No migration history available yet");
        }
        else {
            log.info("Migration history");

            for (HistoryVersion version : versions.subList(
                Math.max(0, versions.size() - 5),
                versions.size()
            )) {
                log(version);
            }
        }
    }

    static final void log(HistoryVersion version) {
        log.info("  " + string(version.migratedAt()) + " - Version: " + string(version.version()));

        if (version.version().parents().size() > 1) {
            log.info("  Merged parents: ");

            for (Version p : version.version().parents())
                log.info("  - " + string(p));
        }
    }

    private static final String string(Instant instant) {
        if (instant == null)
            return "0000-00-00T00:00:00.000Z";
        else
            return StringUtils.rightPad(instant.toString(), 24);
    }

    private static final String string(Version version) {
        return version.id() + (!isEmpty(version.message()) ? " (" + version.message() + ")" : "");
    }

    @Override
    public final void logPending() {
        Query[] q = queries().queries();
        log.info("Pending queries from " + from().id() + " to " + to().id() + ": " + (q.length == 0 ? "none" : ""));
        log(q);
    }

    @Override
    public final void logUntracked() {
        Query[] q = untracked().queries();
        log.info("Untracked changes at " + from().id() + ": " + (q.length == 0 ? "none" : ""));
        log(q);
    }

    static final void log(Query[] queries) {
        for (int i = 0; i < queries.length; i++)
            log.info(queries[i]);
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
