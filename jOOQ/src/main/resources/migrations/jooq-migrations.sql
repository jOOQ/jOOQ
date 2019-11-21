CREATE TABLE jooq_migrations_changelog (
  id             BIGINT       NOT NULL IDENTITY,
  migrated_from  VARCHAR(255) NOT NULL,
  migrated_to    VARCHAR(255) NOT NULL,
  migrated_at    TIMESTAMP    NOT NULL,
  migration_time BIGINT           NULL,
  jooq_version   VARCHAR(50)  NOT NULL,

  CONSTRAINT jooq_migrations_changelog_pk PRIMARY KEY (id)
);

CREATE INDEX jooq_migrations_changelog_i1 ON jooq_migrations_changelog (migrated_at);

COMMENT ON TABLE jooq_migrations_changelog IS 'The migration log of jOOQ Migrations.';
COMMENT ON COLUMN jooq_migrations_changelog.id IS 'The database version ID.';
COMMENT ON COLUMN jooq_migrations_changelog.migrated_from IS 'The previous database version ID.';
COMMENT ON COLUMN jooq_migrations_changelog.migrated_at IS 'The date/time when the database version was migrated to.';
COMMENT ON COLUMN jooq_migrations_changelog.migration_time IS 'The time in milliseconds it took to migrate to this database version.';
COMMENT ON COLUMN jooq_migrations_changelog.jooq_version IS 'The jOOQ version used to migrate to this database version.'