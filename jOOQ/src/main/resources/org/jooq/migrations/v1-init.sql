CREATE TABLE jooq_migration_history (
  id                 BIGINT       NOT NULL IDENTITY,
  migrated_from      VARCHAR(255) NOT NULL,
  migrated_to        VARCHAR(255) NOT NULL,
  migrated_at        TIMESTAMP    NOT NULL,
  migration_time     BIGINT           NULL,
  jooq_version       VARCHAR(50)  NOT NULL,
  sql                CLOB             NULL,
  sql_count          INT          NOT NULL,
  status             VARCHAR(10)  NOT NULL,
  status_message     CLOB             NULL,
  resolution         VARCHAR(10)      NULL,
  resolution_message CLOB             NULL,
  
  CONSTRAINT jooq_migr_hist_pk   PRIMARY KEY (id),
  CONSTRAINT jooq_migr_hist_chk1 CHECK (status IN ('STARTING', 'REVERTING', 'MIGRATING', 'SUCCESS', 'FAILURE')),
  CONSTRAINT jooq_migr_hist_chk2 CHECK (resolution IN ('OPEN', 'RESOLVED', 'IGNORED'))
);

CREATE INDEX jooq_migr_hist_i1 ON jooq_migration_history (migrated_at);

COMMENT ON TABLE  jooq_migration_history                    IS 'The migration history of jOOQ Migrations.';
COMMENT ON COLUMN jooq_migration_history.id                 IS 'The database version ID.';
COMMENT ON COLUMN jooq_migration_history.migrated_from      IS 'The previous database version ID.';
COMMENT ON COLUMN jooq_migration_history.migrated_at        IS 'The date/time when the database version was migrated to.';
COMMENT ON COLUMN jooq_migration_history.migration_time     IS 'The time in milliseconds it took to migrate to this database version.';
COMMENT ON COLUMN jooq_migration_history.jooq_version       IS 'The jOOQ version used to migrate to this database version.';
COMMENT ON COLUMN jooq_migration_history.sql_count          IS 'The number of SQL statements that were run to install this database version.';
COMMENT ON COLUMN jooq_migration_history.sql                IS 'The SQL statements that were run to install this database version.';
COMMENT ON COLUMN jooq_migration_history.status             IS 'The database version installation status.';
COMMENT ON COLUMN jooq_migration_history.status_message     IS 'Any info or error message explaining the status.';
COMMENT ON COLUMN jooq_migration_history.resolution         IS 'The error resolution, if any.';
COMMENT ON COLUMN jooq_migration_history.resolution_message IS 'Any info or error message explaining the resolution.';