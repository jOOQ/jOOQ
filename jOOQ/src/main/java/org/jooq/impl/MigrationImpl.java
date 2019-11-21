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

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jooq.Configuration;
import org.jooq.Constants;
import org.jooq.ContextTransactionalCallable;
import org.jooq.Field;
import org.jooq.Identity;
import org.jooq.Migration;
import org.jooq.MigrationListener;
import org.jooq.MigrationResult;
import org.jooq.Name;
import org.jooq.Queries;
import org.jooq.Query;
import org.jooq.Record1;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.Version;
import org.jooq.exception.DataAccessException;
import org.jooq.exception.DataDefinitionException;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.StopWatch;

/**
 * @author Lukas Eder
 */
final class MigrationImpl extends AbstractScope implements Migration {

    private static final JooqLogger              log       = JooqLogger.getLogger(Migration.class);

    // TODO: Make this table and its schema configurable
    private static final JooqMigrationsChangelog CHANGELOG = JooqMigrationsChangelog.JOOQ_MIGRATIONS_CHANGELOG;
    private final Version                        to;
    private Version                              from;
    private Queries                              queries;
    private Map<String, Version>                 versions;

    MigrationImpl(Configuration configuration, Version to) {
        super(configuration.derive(new ThreadLocalTransactionProvider(configuration.systemConnectionProvider())));

        this.to = to;
    }

    @Override
    public final Version from() {
        if (from == null) {

            // TODO: Use pessimistic locking so no one else can migrate in between
            JooqMigrationsChangelogRecord currentRecord =
            dsl().selectFrom(CHANGELOG)
                 .orderBy(CHANGELOG.MIGRATED_AT.desc(), CHANGELOG.ID.desc())
                 .limit(1)
                 .fetchOne();

            from = currentRecord == null ? to().root() : versions().get(currentRecord.getMigratedTo());
        }

        return from;
    }

    @Override
    public final Version to() {
        return to;
    }

    @Override
    public final Queries queries() {
        if (queries == null)
            queries = to().migrateFrom(from());

        return queries;
    }

    private final Map<String, Version> versions() {
        if (versions == null) {
            versions = new HashMap<>();

            for (Version version : configuration().versionProvider().provide())
                versions.put(version.id(), version);
        }

        return versions;
    }

    private static final MigrationResult MIGRATION_RESULT = new MigrationResult() {};

    @Override
    public final MigrationResult migrate() throws DataDefinitionException {

        // TODO: Transactions don't really make sense in most dialects. In some, they do
        //       e.g. PostgreSQL supports transactional DDL. Check if we're getting this right.
        return run(new ContextTransactionalCallable<MigrationResult>() {
            @Override
            public MigrationResult run() {
                DefaultMigrationContext ctx = new DefaultMigrationContext(configuration(), from(), to(), queries());
                MigrationListener listener = new MigrationListeners(configuration);

                try {
                    listener.migrationStart(ctx);

                    if (from().equals(to())) {
                        log.info("jOOQ Migrations", "Version " + to().id() + " is already installed as the current version.");
                        return MIGRATION_RESULT;
                    }

                    // TODO: Implement preconditions
                    // TODO: Implement a listener with a variety of pro / oss features
                    // TODO: Implement additional out-of-the-box sanity checks
                    // TODO: Allow undo migrations only if enabled explicitly
                    // TODO: Add some migration settings, e.g. whether CHANGELOG.SQL should be filled
                    // TODO: Migrate the CHANGELOG table with the Migration API
                    // TODO: Create an Enum for CHANGELOG.STATUS

                    log.info("jOOQ Migrations", "Version " + from().id() + " is migrated to " + to().id());

                    StopWatch watch = new StopWatch();

                    // TODO: Make logging configurable
                    if (log.isDebugEnabled())
                        for (Query query : queries())
                            log.debug("jOOQ Migrations", dsl().renderInlined(query));

                    JooqMigrationsChangelogRecord newRecord = dsl().newRecord(CHANGELOG);

                    newRecord
                        .setJooqVersion(Constants.VERSION)
                        .setMigratedAt(new Timestamp(System.currentTimeMillis()))
                        .setMigratedFrom(from().id())
                        .setMigratedTo(to().id())
                        .setMigrationTime(0L)
                        .setSql(queries().toString())
                        .setSqlCount(queries().queries().length)
                        .setStatus("PENDING")
                        .insert();

                    try {

                        // TODO: Can we access the individual Queries from Version, if applicable?
                        // TODO: Set the ctx.queriesFrom(), ctx.queriesTo(), and ctx.queries() values
                        listener.queriesStart(ctx);

                        // TODO: Make batching an option: queries().executeBatch();
                        for (Query query : queries().queries()) {
                            ctx.query(query);
                            listener.queryStart(ctx);
                            query.execute();
                            listener.queryEnd(ctx);
                            ctx.query(null);
                        }

                        listener.queriesEnd(ctx);

                        newRecord
                            .setMigrationTime(watch.split() / 1000000L)
                            .setStatus("SUCCESS")
                            .update();
                    }
                    catch (DataAccessException e) {

                        // TODO: Make sure this is committed, given that we're re-throwing the exception.
                        // TODO: How can we recover from failure?
                        newRecord
                            .setMigrationTime(watch.split() / 1000000L)
                            .setStatus("FAILURE")
                            .update();

                        throw e;
                    }

                    return MIGRATION_RESULT;
                }
                finally {
                    listener.migrationEnd(ctx);
                }
            }
        });
    }

    /**
     * Initialise the underlying {@link Configuration} with the jOOQ Migrations
     * Changelog.
     */
    public final void init() {

        // TODO: What to do when initialising jOOQ-migrations on an existing database?
        //       - Should there be init() commands that can be run explicitl by the user?
        //       - Will we reverse engineer the production Meta snapshot first?
        if (!existsChangelog())
            dsl().meta(CHANGELOG).ddl().executeBatch();
    }

    private final boolean existsChangelog() {

        // [#8301] Find a better way to test if our table already exists
        try {
            dsl().fetchExists(CHANGELOG);
            return true;
        }
        catch (DataAccessException ignore) {}

        return false;
    }

    private final <T> T run(final ContextTransactionalCallable<T> runnable) {
        init();
        return dsl().transactionResult(runnable);
    }

    // -------------------------------------------------------------------------
    // XXX: Generated code
    // -------------------------------------------------------------------------

    // TODO These classes have been generated and copied here. It would be desirable:
    // - [#6948] To be able to generate package private classes directly inside of other classes
    // - [#7444] Alternatively, have a simple public API replacing TableImpl
    // -         If the above cannot be implemented, generate these in the org.jooq.impl package
    //           and make them package private or @Internal

    /**
     * The migration log of jOOQ Migrations.
     */
    @SuppressWarnings({ "all", "unchecked", "rawtypes" })
    static class JooqMigrationsChangelog extends TableImpl<JooqMigrationsChangelogRecord> {

        private static final long serialVersionUID = 1147896779;

        /**
         * The reference instance of <code>JOOQ_MIGRATIONS_CHANGELOG</code>
         */
        public static final JooqMigrationsChangelog JOOQ_MIGRATIONS_CHANGELOG = new JooqMigrationsChangelog();

        /**
         * The class holding records for this type
         */
        @Override
        public Class<JooqMigrationsChangelogRecord> getRecordType() {
            return JooqMigrationsChangelogRecord.class;
        }

        /**
         * The column <code>JOOQ_MIGRATIONS_CHANGELOG.ID</code>. The database version ID.
         */
        public final TableField<JooqMigrationsChangelogRecord, Long> ID = createField(DSL.name("ID"), org.jooq.impl.SQLDataType.BIGINT.nullable(false).identity(true), this, "The database version ID.");

        /**
         * The column <code>JOOQ_MIGRATIONS_CHANGELOG.MIGRATED_FROM</code>. The previous database version ID.
         */
        public final TableField<JooqMigrationsChangelogRecord, String> MIGRATED_FROM = createField(DSL.name("MIGRATED_FROM"), org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false), this, "The previous database version ID.");

        /**
         * The column <code>JOOQ_MIGRATIONS_CHANGELOG.MIGRATED_TO</code>.
         */
        public final TableField<JooqMigrationsChangelogRecord, String> MIGRATED_TO = createField(DSL.name("MIGRATED_TO"), org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false), this, "");

        /**
         * The column <code>JOOQ_MIGRATIONS_CHANGELOG.MIGRATED_AT</code>. The date/time when the database version was migrated to.
         */
        public final TableField<JooqMigrationsChangelogRecord, Timestamp> MIGRATED_AT = createField(DSL.name("MIGRATED_AT"), org.jooq.impl.SQLDataType.TIMESTAMP.precision(6).nullable(false), this, "The date/time when the database version was migrated to.");

        /**
         * The column <code>JOOQ_MIGRATIONS_CHANGELOG.MIGRATION_TIME</code>. The time in milliseconds it took to migrate to this database version.
         */
        public final TableField<JooqMigrationsChangelogRecord, Long> MIGRATION_TIME = createField(DSL.name("MIGRATION_TIME"), org.jooq.impl.SQLDataType.BIGINT, this, "The time in milliseconds it took to migrate to this database version.");

        /**
         * The column <code>JOOQ_MIGRATIONS_CHANGELOG.JOOQ_VERSION</code>. The jOOQ version used to migrate to this database version.
         */
        public final TableField<JooqMigrationsChangelogRecord, String> JOOQ_VERSION = createField(DSL.name("JOOQ_VERSION"), org.jooq.impl.SQLDataType.VARCHAR(50).nullable(false), this, "The jOOQ version used to migrate to this database version.");

        /**
         * The column <code>JOOQ_MIGRATIONS_CHANGELOG.SQL</code>. The jOOQ version used to migrate to this database version.
         */
        public final TableField<JooqMigrationsChangelogRecord, String> SQL = createField(DSL.name("SQL"), org.jooq.impl.SQLDataType.CLOB, this, "The SQL statements that were run to install this database version.");

        /**
         * The column <code>JOOQ_MIGRATIONS_CHANGELOG.SQL_COUNT</code>. The number of SQL statements that were run to install this database version.
         */
        public final TableField<JooqMigrationsChangelogRecord, Integer> SQL_COUNT = createField(DSL.name("SQL_COUNT"), org.jooq.impl.SQLDataType.INTEGER, this, "The number of SQL statements that were run to install this database version.");

        /**
         * The column <code>JOOQ_MIGRATIONS_CHANGELOG.JOOQ_VERSION</code>. The jOOQ version used to migrate to this database version.
         */
        public final TableField<JooqMigrationsChangelogRecord, String> STATUS = createField(DSL.name("STATUS"), org.jooq.impl.SQLDataType.VARCHAR(10).nullable(false), this, "The database version installation status.");

        /**
         * Create a <code>JOOQ_MIGRATIONS_CHANGELOG</code> table reference
         */
        public JooqMigrationsChangelog() {
            this(DSL.name("JOOQ_MIGRATIONS_CHANGELOG"), null);
        }

        /**
         * Create an aliased <code>JOOQ_MIGRATIONS_CHANGELOG</code> table reference
         */
        public JooqMigrationsChangelog(String alias) {
            this(DSL.name(alias), JOOQ_MIGRATIONS_CHANGELOG);
        }

        /**
         * Create an aliased <code>JOOQ_MIGRATIONS_CHANGELOG</code> table reference
         */
        public JooqMigrationsChangelog(Name alias) {
            this(alias, JOOQ_MIGRATIONS_CHANGELOG);
        }

        private JooqMigrationsChangelog(Name alias, Table<JooqMigrationsChangelogRecord> aliased) {
            this(alias, aliased, null);
        }

        private JooqMigrationsChangelog(Name alias, Table<JooqMigrationsChangelogRecord> aliased, Field<?>[] parameters) {
            super(alias, null, aliased, parameters, DSL.comment("The migration log of jOOQ Migrations."));
        }

        @Override
        public Schema getSchema() {
            return new SchemaImpl("");
        }

        @Override
        public Identity<JooqMigrationsChangelogRecord, Long> getIdentity() {
            return Internal.createIdentity(JOOQ_MIGRATIONS_CHANGELOG, JOOQ_MIGRATIONS_CHANGELOG.ID);
        }

        @Override
        public UniqueKey<JooqMigrationsChangelogRecord> getPrimaryKey() {
            return Internal.createUniqueKey(JOOQ_MIGRATIONS_CHANGELOG, "JOOQ_MIGRATIONS_CHANGELOG_PK", JOOQ_MIGRATIONS_CHANGELOG.ID);
        }

        @Override
        public List<UniqueKey<JooqMigrationsChangelogRecord>> getKeys() {
            return Arrays.<UniqueKey<JooqMigrationsChangelogRecord>>asList(
                  Internal.createUniqueKey(JOOQ_MIGRATIONS_CHANGELOG, "JOOQ_MIGRATIONS_CHANGELOG_PK", JOOQ_MIGRATIONS_CHANGELOG.ID)
            );
        }
    }

    /**
     * The migration log of jOOQ Migrations.
     */
    @SuppressWarnings({ "all", "unchecked", "rawtypes" })
    static class JooqMigrationsChangelogRecord extends UpdatableRecordImpl<JooqMigrationsChangelogRecord> {

        private static final long serialVersionUID = 2016380678;

        /**
         * Setter for <code>JOOQ_MIGRATIONS_CHANGELOG.ID</code>. The database version ID.
         */
        public JooqMigrationsChangelogRecord setId(Long value) {
            set(0, value);
            return this;
        }

        /**
         * Getter for <code>JOOQ_MIGRATIONS_CHANGELOG.ID</code>. The database version ID.
         */
        public Long getId() {
            return (Long) get(0);
        }

        /**
         * Setter for <code>JOOQ_MIGRATIONS_CHANGELOG.MIGRATED_FROM</code>. The previous database version ID.
         */
        public JooqMigrationsChangelogRecord setMigratedFrom(String value) {
            set(1, value);
            return this;
        }

        /**
         * Getter for <code>JOOQ_MIGRATIONS_CHANGELOG.MIGRATED_FROM</code>. The previous database version ID.
         */
        public String getMigratedFrom() {
            return (String) get(1);
        }

        /**
         * Setter for <code>JOOQ_MIGRATIONS_CHANGELOG.MIGRATED_TO</code>.
         */
        public JooqMigrationsChangelogRecord setMigratedTo(String value) {
            set(2, value);
            return this;
        }

        /**
         * Getter for <code>JOOQ_MIGRATIONS_CHANGELOG.MIGRATED_TO</code>.
         */
        public String getMigratedTo() {
            return (String) get(2);
        }

        /**
         * Setter for <code>JOOQ_MIGRATIONS_CHANGELOG.MIGRATED_AT</code>. The date/time when the database version was migrated to.
         */
        public JooqMigrationsChangelogRecord setMigratedAt(Timestamp value) {
            set(3, value);
            return this;
        }

        /**
         * Getter for <code>JOOQ_MIGRATIONS_CHANGELOG.MIGRATED_AT</code>. The date/time when the database version was migrated to.
         */
        public Timestamp getMigratedAt() {
            return (Timestamp) get(3);
        }

        /**
         * Setter for <code>JOOQ_MIGRATIONS_CHANGELOG.MIGRATION_TIME</code>. The time in milliseconds it took to migrate to this database version.
         */
        public JooqMigrationsChangelogRecord setMigrationTime(Long value) {
            set(4, value);
            return this;
        }

        /**
         * Getter for <code>JOOQ_MIGRATIONS_CHANGELOG.MIGRATION_TIME</code>. The time in milliseconds it took to migrate to this database version.
         */
        public Long getMigrationTime() {
            return (Long) get(4);
        }

        /**
         * Setter for <code>JOOQ_MIGRATIONS_CHANGELOG.JOOQ_VERSION</code>. The jOOQ version used to migrate to this database version.
         */
        public JooqMigrationsChangelogRecord setJooqVersion(String value) {
            set(5, value);
            return this;
        }

        /**
         * Getter for <code>JOOQ_MIGRATIONS_CHANGELOG.JOOQ_VERSION</code>. The jOOQ version used to migrate to this database version.
         */
        public String getJooqVersion() {
            return (String) get(5);
        }

        /**
         * Setter for <code>JOOQ_MIGRATIONS_CHANGELOG.SQL</code>. The SQL statements that were run to install this database version.
         */
        public JooqMigrationsChangelogRecord setSql(String value) {
            set(6, value);
            return this;
        }

        /**
         * Getter for <code>JOOQ_MIGRATIONS_CHANGELOG.SQL</code>. The SQL statements that were run to install this database version.
         */
        public String getSql() {
            return (String) get(6);
        }

        /**
         * Setter for <code>JOOQ_MIGRATIONS_CHANGELOG.SQL_COUNT</code>. The number of SQL statements that were run to install this database version.
         */
        public JooqMigrationsChangelogRecord setSqlCount(Integer value) {
            set(7, value);
            return this;
        }

        /**
         * Getter for <code>JOOQ_MIGRATIONS_CHANGELOG.SQL_COUNT</code>. The number of SQL statements that were run to install this database version.
         */
        public Integer getSqlCount() {
            return (Integer) get(7);
        }

        /**
         * Setter for <code>JOOQ_MIGRATIONS_CHANGELOG.STATUS</code>. The database version installation status.
         */
        public JooqMigrationsChangelogRecord setStatus(String value) {
            set(8, value);
            return this;
        }

        /**
         * Getter for <code>JOOQ_MIGRATIONS_CHANGELOG.STATUS</code>. The database version installation status.
         */
        public String getStatus() {
            return (String) get(8);
        }

        // -------------------------------------------------------------------------
        // Primary key information
        // -------------------------------------------------------------------------

        @Override
        public Record1<Long> key() {
            return (Record1) super.key();
        }

        // -------------------------------------------------------------------------
        // Constructors
        // -------------------------------------------------------------------------

        /**
         * Create a detached JooqMigrationsChangelogRecord
         */
        public JooqMigrationsChangelogRecord() {
            super(JooqMigrationsChangelog.JOOQ_MIGRATIONS_CHANGELOG);
        }

        /**
         * Create a detached, initialised JooqMigrationsChangelogRecord
         */
        public JooqMigrationsChangelogRecord(Long id, String migratedFrom, String migratedTo, Timestamp migratedAt, Long migrationTime, String jooqVersion, String sql, String status) {
            super(JooqMigrationsChangelog.JOOQ_MIGRATIONS_CHANGELOG);

            set(0, id);
            set(1, migratedFrom);
            set(2, migratedTo);
            set(3, migratedAt);
            set(4, migrationTime);
            set(5, jooqVersion);
            set(6, sql);
            set(7, status);
        }
    }
}
