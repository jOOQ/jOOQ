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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * A configuration type for use with the various {@link DSLContext#ddl(Catalog)}
 * methods.
 *
 * @author Lukas Eder
 */
public final class DDLExportConfiguration {

    private final EnumSet<DDLFlag> flags;

    private final boolean          createSchemaIfNotExists;
    private final boolean          createTableIfNotExists;
    private final boolean          createIndexIfNotExists;
    private final boolean          createSequenceIfNotExists;
    private final boolean          createViewIfNotExists;
    private final boolean          createOrReplaceView;

    private final boolean          respectCatalogOrder;
    private final boolean          respectSchemaOrder;
    private final boolean          respectTableOrder;
    private final boolean          respectColumnOrder;
    private final boolean          respectConstraintOrder;
    private final boolean          respectIndexOrder;
    private final boolean          respectSequenceOrder;

    private final boolean          defaultSequenceFlags;

    /**
     * Create a new default export configuration instance.
     */
    public DDLExportConfiguration() {
        this(
            EnumSet.allOf(DDLFlag.class),

            false,
            false,
            false,
            false,
            false,
            false,

            false,
            false,
            false,
            true,
            false,
            false,
            false,

            false
        );
    }

    private DDLExportConfiguration(
        Collection<DDLFlag> flags,

        boolean createSchemaIfNotExists,
        boolean createTableIfNotExists,
        boolean createIndexIfNotExists,
        boolean createSequenceIfNotExists,
        boolean createViewIfNotExists,
        boolean createOrReplaceView,

        boolean respectCatalogOrder,
        boolean respectSchemaOrder,
        boolean respectTableOrder,
        boolean respectColumnOrder,
        boolean respectConstraintOrder,
        boolean respectIndexOrder,
        boolean respectSequenceOrder,

        boolean defaultSequenceFlags
    ) {
        this.flags = EnumSet.copyOf(flags);

        this.createSchemaIfNotExists = createSchemaIfNotExists;
        this.createTableIfNotExists = createTableIfNotExists;
        this.createIndexIfNotExists = createIndexIfNotExists;
        this.createSequenceIfNotExists = createSequenceIfNotExists;
        this.createViewIfNotExists = createViewIfNotExists;
        this.createOrReplaceView = createOrReplaceView;

        this.respectCatalogOrder = respectCatalogOrder;
        this.respectSchemaOrder = respectSchemaOrder;
        this.respectTableOrder = respectTableOrder;
        this.respectColumnOrder = respectColumnOrder;
        this.respectConstraintOrder = respectConstraintOrder;
        this.respectIndexOrder = respectIndexOrder;
        this.respectSequenceOrder = respectSequenceOrder;
        this.defaultSequenceFlags = defaultSequenceFlags;
    }

    /**
     * The {@link DDLFlag} that are enabled on this configuration.
     */
    public final Set<DDLFlag> flags() {
        return Collections.unmodifiableSet(flags);
    }

    /**
     * The {@link DDLFlag} that are enabled on this configuration.
     */
    public final DDLExportConfiguration flags(DDLFlag... newFlags) {
        return flags(Arrays.asList(newFlags));
    }

    /**
     * The {@link DDLFlag} that are enabled on this configuration.
     */
    public final DDLExportConfiguration flags(Collection<DDLFlag> newFlags) {
        return new DDLExportConfiguration(
            newFlags,
            createSchemaIfNotExists,
            createTableIfNotExists,
            createIndexIfNotExists,
            createSequenceIfNotExists,
            createViewIfNotExists,
            createOrReplaceView,
            respectCatalogOrder,
            respectSchemaOrder,
            respectTableOrder,
            respectColumnOrder,
            respectConstraintOrder,
            respectIndexOrder,
            respectSequenceOrder,
            defaultSequenceFlags
        );
    }

    /**
     * Whether to generate <code>CREATE SCHEMA IF NOT EXISTS</code> statements.
     * <p>
     * Not all RDBMS support this flag. Check
     * {@link DSLContext#createSchemaIfNotExists(Schema)} to see if your
     * {@link SQLDialect} supports the clause.
     */
    public final boolean createSchemaIfNotExists() {
        return createSchemaIfNotExists;
    }

    /**
     * Whether to generate <code>CREATE SCHEMA IF NOT EXISTS</code> statements.
     */
    public final DDLExportConfiguration createSchemaIfNotExists(boolean newCreateSchemaIfNotExists) {
        return new DDLExportConfiguration(
            flags,
            newCreateSchemaIfNotExists,
            createTableIfNotExists,
            createIndexIfNotExists,
            createSequenceIfNotExists,
            createViewIfNotExists,
            createOrReplaceView,
            respectCatalogOrder,
            respectSchemaOrder,
            respectTableOrder,
            respectColumnOrder,
            respectConstraintOrder,
            respectIndexOrder,
            respectSequenceOrder,
            defaultSequenceFlags
        );
    }

    /**
     * Whether to generate <code>CREATE TABLE IF NOT EXISTS</code> statements.
     * <p>
     * Not all RDBMS support this flag. Check
     * {@link DSLContext#createTableIfNotExists(Table)} to see if your
     * {@link SQLDialect} supports the clause.
     */
    public final boolean createTableIfNotExists() {
        return createTableIfNotExists;
    }

    /**
     * Whether to generate <code>CREATE TABLE IF NOT EXISTS</code> statements.
     */
    public final DDLExportConfiguration createTableIfNotExists(boolean newCreateTableIfNotExists) {
        return new DDLExportConfiguration(
            flags,
            createSchemaIfNotExists,
            newCreateTableIfNotExists,
            createIndexIfNotExists,
            createSequenceIfNotExists,
            createViewIfNotExists,
            createOrReplaceView,
            respectCatalogOrder,
            respectSchemaOrder,
            respectTableOrder,
            respectColumnOrder,
            respectConstraintOrder,
            respectIndexOrder,
            respectSequenceOrder,
            defaultSequenceFlags
        );
    }

    /**
     * Whether to generate <code>CREATE INDEX IF NOT EXISTS</code> statements.
     * <p>
     * Not all RDBMS support this flag. Check
     * {@link DSLContext#createIndexIfNotExists(Index)} to see if your
     * {@link SQLDialect} supports the clause.
     */
    public final boolean createIndexIfNotExists() {
        return createIndexIfNotExists;
    }

    /**
     * Whether to generate <code>CREATE INDEX IF NOT EXISTS</code> statements.
     */
    public final DDLExportConfiguration createIndexIfNotExists(boolean newCreateIndexIfNotExists) {
        return new DDLExportConfiguration(
            flags,
            createSchemaIfNotExists,
            createTableIfNotExists,
            newCreateIndexIfNotExists,
            createSequenceIfNotExists,
            createViewIfNotExists,
            createOrReplaceView,
            respectCatalogOrder,
            respectSchemaOrder,
            respectTableOrder,
            respectColumnOrder,
            respectConstraintOrder,
            respectIndexOrder,
            respectSequenceOrder,
            defaultSequenceFlags
        );
    }

    /**
     * Whether to generate <code>CREATE SEQUENCE IF NOT EXISTS</code> statements.
     * <p>
     * Not all RDBMS support this flag. Check
     * {@link DSLContext#createSequenceIfNotExists(Sequence)} to see if your
     * {@link SQLDialect} supports the clause.
     */
    public final boolean createSequenceIfNotExists() {
        return createSequenceIfNotExists;
    }

    /**
     * Whether to generate <code>CREATE SEQUENCE IF NOT EXISTS</code> statements.
     */
    public final DDLExportConfiguration createSequenceIfNotExists(boolean newCreateSequenceIfNotExists) {
        return new DDLExportConfiguration(
            flags,
            createSchemaIfNotExists,
            createTableIfNotExists,
            createIndexIfNotExists,
            newCreateSequenceIfNotExists,
            createViewIfNotExists,
            createOrReplaceView,
            respectCatalogOrder,
            respectSchemaOrder,
            respectTableOrder,
            respectColumnOrder,
            respectConstraintOrder,
            respectIndexOrder,
            respectSequenceOrder,
            defaultSequenceFlags
        );
    }

    /**
     * Whether to generate <code>CREATE VIEW IF NOT EXISTS</code> statements.
     * <p>
     * Not all RDBMS support this flag. Check
     * {@link DSLContext#createViewIfNotExists(Table, Field...)} to see if your
     * {@link SQLDialect} supports the clause.
     */
    public final boolean createViewIfNotExists() {
        return createViewIfNotExists;
    }

    /**
     * Whether to generate <code>CREATE VIEW IF NOT EXISTS</code> statements.
     */
    public final DDLExportConfiguration createViewIfNotExists(boolean newCreateViewIfNotExists) {
        return new DDLExportConfiguration(
            flags,
            createSchemaIfNotExists,
            createTableIfNotExists,
            createIndexIfNotExists,
            createSequenceIfNotExists,
            newCreateViewIfNotExists,
            createOrReplaceView,
            respectCatalogOrder,
            respectSchemaOrder,
            respectTableOrder,
            respectColumnOrder,
            respectConstraintOrder,
            respectIndexOrder,
            respectSequenceOrder,
            defaultSequenceFlags
        );
    }

    /**
     * Whether to generate <code>CREATE OR REPLACE VIEW</code> statements.
     * <p>
     * Not all RDBMS support this flag. Check
     * {@link DSLContext#createOrReplaceView(Table, Field...)} to see if your
     * {@link SQLDialect} supports the clause.
     */
    public final boolean createOrReplaceView() {
        return createOrReplaceView;
    }

    /**
     * Whether to generate <code>CREATE OR REPLACE VIEW</code> statements.
     */
    public final DDLExportConfiguration createOrReplaceView(boolean newCreateOrReplaceView) {
        return new DDLExportConfiguration(
            flags,
            createSchemaIfNotExists,
            createTableIfNotExists,
            createIndexIfNotExists,
            createSequenceIfNotExists,
            createViewIfNotExists,
            newCreateOrReplaceView,
            respectCatalogOrder,
            respectSchemaOrder,
            respectTableOrder,
            respectColumnOrder,
            respectConstraintOrder,
            respectIndexOrder,
            respectSequenceOrder,
            defaultSequenceFlags
        );
    }

    /**
     * Whether to respect the catalog order produced by the {@link Meta} source
     * when generated catalog DDL.
     */
    public final boolean respectCatalogOrder() {
        return respectCatalogOrder;
    }

    /**
     * Whether to respect the catalog order produced by the {@link Meta} source
     * when generated catalog DDL.
     */
    public final DDLExportConfiguration respectCatalogOrder(boolean newRespectCatalogOrder) {
        return new DDLExportConfiguration(
            flags,
            createSchemaIfNotExists,
            createTableIfNotExists,
            createIndexIfNotExists,
            createSequenceIfNotExists,
            createViewIfNotExists,
            createOrReplaceView,
            newRespectCatalogOrder,
            respectSchemaOrder,
            respectTableOrder,
            respectColumnOrder,
            respectConstraintOrder,
            respectIndexOrder,
            respectSequenceOrder,
            defaultSequenceFlags
        );
    }

    /**
     * Whether to respect the schema order produced by the {@link Meta} source
     * when generated schema DDL.
     */
    public final boolean respectSchemaOrder() {
        return respectSchemaOrder;
    }

    /**
     * Whether to respect the schema order produced by the {@link Meta} source
     * when generated schema DDL.
     */
    public final DDLExportConfiguration respectSchemaOrder(boolean newRespectSchemaOrder) {
        return new DDLExportConfiguration(
            flags,
            createSchemaIfNotExists,
            createTableIfNotExists,
            createIndexIfNotExists,
            createSequenceIfNotExists,
            createViewIfNotExists,
            createOrReplaceView,
            respectCatalogOrder,
            newRespectSchemaOrder,
            respectTableOrder,
            respectColumnOrder,
            respectConstraintOrder,
            respectIndexOrder,
            respectSequenceOrder,
            defaultSequenceFlags
        );
    }

    /**
     * Whether to respect the table order produced by the {@link Meta} source
     * when generated table DDL.
     */
    public final boolean respectTableOrder() {
        return respectTableOrder;
    }

    /**
     * Whether to respect the table order produced by the {@link Meta} source
     * when generated table DDL.
     */
    public final DDLExportConfiguration respectTableOrder(boolean newRespectTableOrder) {
        return new DDLExportConfiguration(
            flags,
            createSchemaIfNotExists,
            createTableIfNotExists,
            createIndexIfNotExists,
            createSequenceIfNotExists,
            createViewIfNotExists,
            createOrReplaceView,
            respectCatalogOrder,
            respectSchemaOrder,
            newRespectTableOrder,
            respectColumnOrder,
            respectConstraintOrder,
            respectIndexOrder,
            respectSequenceOrder,
            defaultSequenceFlags
        );
    }

    /**
     * Whether to respect the column order produced by the {@link Meta} source
     * when generated column DDL.
     */
    public final boolean respectColumnOrder() {
        return respectColumnOrder;
    }

    /**
     * Whether to respect the column order produced by the {@link Meta} source
     * when generated column DDL.
     */
    public final DDLExportConfiguration respectColumnOrder(boolean newRespectColumnOrder) {
        return new DDLExportConfiguration(
            flags,
            createSchemaIfNotExists,
            createTableIfNotExists,
            createIndexIfNotExists,
            createSequenceIfNotExists,
            createViewIfNotExists,
            createOrReplaceView,
            respectCatalogOrder,
            respectSchemaOrder,
            respectTableOrder,
            newRespectColumnOrder,
            respectConstraintOrder,
            respectIndexOrder,
            respectSequenceOrder,
            defaultSequenceFlags
        );
    }

    /**
     * Whether to respect the constraint order produced by the {@link Meta} source
     * when generated constraint DDL.
     */
    public final boolean respectConstraintOrder() {
        return respectConstraintOrder;
    }

    /**
     * Whether to respect the constraint order produced by the {@link Meta} source
     * when generated constraint DDL.
     */
    public final DDLExportConfiguration respectConstraintOrder(boolean newRespectConstraintOrder) {
        return new DDLExportConfiguration(
            flags,
            createSchemaIfNotExists,
            createTableIfNotExists,
            createIndexIfNotExists,
            createSequenceIfNotExists,
            createViewIfNotExists,
            createOrReplaceView,
            respectCatalogOrder,
            respectSchemaOrder,
            respectTableOrder,
            respectColumnOrder,
            newRespectConstraintOrder,
            respectIndexOrder,
            respectSequenceOrder,
            defaultSequenceFlags
        );
    }

    /**
     * Whether to respect the index order produced by the {@link Meta} source
     * when generated index DDL.
     */
    public final boolean respectIndexOrder() {
        return respectIndexOrder;
    }

    /**
     * Whether to respect the index order produced by the {@link Meta} source
     * when generated index DDL.
     */
    public final DDLExportConfiguration respectIndexOrder(boolean newRespectIndexOrder) {
        return new DDLExportConfiguration(
            flags,
            createSchemaIfNotExists,
            createTableIfNotExists,
            createIndexIfNotExists,
            createSequenceIfNotExists,
            createViewIfNotExists,
            createOrReplaceView,
            respectCatalogOrder,
            respectSchemaOrder,
            respectTableOrder,
            respectColumnOrder,
            respectConstraintOrder,
            newRespectIndexOrder,
            respectSequenceOrder,
            defaultSequenceFlags
        );
    }

    /**
     * Whether to respect the sequence order produced by the {@link Meta} source
     * when generated sequence DDL.
     */
    public final boolean respectSequenceOrder() {
        return respectSequenceOrder;
    }

    /**
     * Whether to respect the sequence order produced by the {@link Meta} source
     * when generated sequence DDL.
     */
    public final DDLExportConfiguration respectSequenceOrder(boolean newRespectSequenceOrder) {
        return new DDLExportConfiguration(
            flags,
            createSchemaIfNotExists,
            createTableIfNotExists,
            createIndexIfNotExists,
            createSequenceIfNotExists,
            createViewIfNotExists,
            createOrReplaceView,
            respectCatalogOrder,
            respectSchemaOrder,
            respectTableOrder,
            respectColumnOrder,
            respectConstraintOrder,
            respectIndexOrder,
            newRespectSequenceOrder,
            defaultSequenceFlags
        );
    }

    /**
     * Whether to explicitly produce defaults for all sequence flags, when
     * they're not defined explicitly.
     */
    public final boolean defaultSequenceFlags() {
        return defaultSequenceFlags;
    }

    /**
     * Whether to explicitly produce defaults for all sequence flags, when
     * they're not defined explicitly.
     */
    public final DDLExportConfiguration defaultSequenceFlags(boolean newDefaultSequenceFlags) {
        return new DDLExportConfiguration(
            flags,
            createSchemaIfNotExists,
            createTableIfNotExists,
            createIndexIfNotExists,
            createSequenceIfNotExists,
            createViewIfNotExists,
            createOrReplaceView,
            respectCatalogOrder,
            respectSchemaOrder,
            respectTableOrder,
            respectColumnOrder,
            respectConstraintOrder,
            respectIndexOrder,
            respectSequenceOrder,
            newDefaultSequenceFlags
        );
    }
}
