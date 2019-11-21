CREATE TABLE jooq_migrations_changelog (
  id             BIGINT       NOT NULL IDENTITY,
  migrated_from  VARCHAR(255) NOT NULL,
  migrated_to    VARCHAR(255) NOT NULL,
  migrated_at    TIMESTAMP    NOT NULL,
  migration_time BIGINT           NULL,
  jooq_version   VARCHAR(50)  NOT NULL,
  sql            CLOB             NULL,
  sql_count      INT          NOT NULL,
  status         VARCHAR(10)  NOT NULL,

  CONSTRAINT jooq_migr_pk   PRIMARY KEY (id),
  CONSTRAINT jooq_migr_chk1 CHECK (status IN ('RUNNING', 'SUCCESS', 'FAILED'))
);

CREATE INDEX jooq_migr_i1 ON jooq_migrations_changelog (migrated_at);

COMMENT ON TABLE  jooq_migrations_changelog                 IS 'The migration log of jOOQ Migrations.';
COMMENT ON COLUMN jooq_migrations_changelog.id              IS 'The database version ID.';
COMMENT ON COLUMN jooq_migrations_changelog.migrated_from   IS 'The previous database version ID.';
COMMENT ON COLUMN jooq_migrations_changelog.migrated_at     IS 'The date/time when the database version was migrated to.';
COMMENT ON COLUMN jooq_migrations_changelog.migration_time  IS 'The time in milliseconds it took to migrate to this database version.';
COMMENT ON COLUMN jooq_migrations_changelog.jooq_version    IS 'The jOOQ version used to migrate to this database version.';
COMMENT ON COLUMN jooq_migrations_changelog.sql_count       IS 'The number of SQL statements that were run to install this database version.';
COMMENT ON COLUMN jooq_migrations_changelog.sql             IS 'The SQL statements that were run to install this database version.';
COMMENT ON COLUMN jooq_migrations_changelog.status          IS 'The database version installation status.';
