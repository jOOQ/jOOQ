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
package org.jooq;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * A configuration type for use with the various {@link Meta#ddl()} methods.
 *
 * @author Lukas Eder
 */
public final class DDLExportConfiguration {

    /**
     * Whether to inline foreign key constraint definitions with the table
     * definition.
     */
    public enum InlineForeignKeyConstraints {

        /**
         * Always inline the foreign key constraint definitions.
         */
        ALWAYS,

        /**
         * Inline foreign key constraint definitions only when needed.
         * <p>
         * This can be necessary if {@link AlterTableStep#add(Constraint)} isn't
         * supported, such as in SQLite, see [#16470]. This is the default.
         */
        WHEN_NEEDED,

        /**
         * Never inline foreign key constraint definitions.
         */
        NEVER
    }

    private final EnumSet<DDLFlag>            flags;

    private final boolean                     createSchemaIfNotExists;
    private final boolean                     createTableIfNotExists;
    private final boolean                     createIndexIfNotExists;
    private final boolean                     createUDTIfNotExists;
    private final boolean                     createDomainIfNotExists;
    private final boolean                     createSequenceIfNotExists;
    private final boolean                     createViewIfNotExists;
    private final boolean                     createMaterializedViewIfNotExists;
    private final boolean                     createOrReplaceView;
    private final boolean                     createOrReplaceMaterializedView;





    private final boolean                     respectCatalogOrder;
    private final boolean                     respectSchemaOrder;
    private final boolean                     respectTableOrder;
    private final boolean                     respectColumnOrder;
    private final boolean                     respectConstraintOrder;
    private final boolean                     respectIndexOrder;
    private final boolean                     respectUDTOrder;
    private final boolean                     respectDomainOrder;
    private final boolean                     respectSequenceOrder;





    private final boolean                     defaultSequenceFlags;

    private final boolean                     includeConstraintsOnViews;
    private final boolean                     inlinePrimaryKeyConstraints;
    private final boolean                     inlineUniqueConstraints;
    private final boolean                     inlineCheckConstraints;
    private final InlineForeignKeyConstraints inlineForeignKeyConstraints;

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
            false,





            false,
            false,
            false,
            true,
            false,
            false,
            false,
            false,
            false,





            false,

            false,
            true,
            true,
            true,
            InlineForeignKeyConstraints.WHEN_NEEDED
        );
    }

    private DDLExportConfiguration(
        Collection<DDLFlag> flags,

        boolean createSchemaIfNotExists,
        boolean createTableIfNotExists,
        boolean createIndexIfNotExists,
        boolean createUDTIfNotExists,
        boolean createDomainIfNotExists,
        boolean createSequenceIfNotExists,
        boolean createViewIfNotExists,
        boolean createMaterializedViewIfNotExists,
        boolean createOrReplaceView,
        boolean createOrReplaceMaterializedView,





        boolean respectCatalogOrder,
        boolean respectSchemaOrder,
        boolean respectTableOrder,
        boolean respectColumnOrder,
        boolean respectConstraintOrder,
        boolean respectIndexOrder,
        boolean respectUDTOrder,
        boolean respectDomainOrder,
        boolean respectSequenceOrder,





        boolean defaultSequenceFlags,

        boolean includeConstraintsOnViews,
        boolean inlinePrimaryKeyConstraints,
        boolean inlineUniqueConstraints,
        boolean inlineCheckConstraints,
        InlineForeignKeyConstraints inlineForeignKeyConstraints
    ) {
        this.flags = EnumSet.copyOf(flags);

        this.createSchemaIfNotExists = createSchemaIfNotExists;
        this.createTableIfNotExists = createTableIfNotExists;
        this.createIndexIfNotExists = createIndexIfNotExists;
        this.createUDTIfNotExists = createUDTIfNotExists;
        this.createDomainIfNotExists = createDomainIfNotExists;
        this.createSequenceIfNotExists = createSequenceIfNotExists;
        this.createViewIfNotExists = createViewIfNotExists;
        this.createMaterializedViewIfNotExists = createMaterializedViewIfNotExists;
        this.createOrReplaceView = createOrReplaceView;
        this.createOrReplaceMaterializedView = createOrReplaceMaterializedView;





        this.respectCatalogOrder = respectCatalogOrder;
        this.respectSchemaOrder = respectSchemaOrder;
        this.respectTableOrder = respectTableOrder;
        this.respectColumnOrder = respectColumnOrder;
        this.respectConstraintOrder = respectConstraintOrder;
        this.respectIndexOrder = respectIndexOrder;
        this.respectUDTOrder = respectUDTOrder;
        this.respectDomainOrder = respectDomainOrder;
        this.respectSequenceOrder = respectSequenceOrder;





        this.defaultSequenceFlags = defaultSequenceFlags;

        this.includeConstraintsOnViews = includeConstraintsOnViews;
        this.inlinePrimaryKeyConstraints = inlinePrimaryKeyConstraints;
        this.inlineUniqueConstraints = inlineUniqueConstraints;
        this.inlineCheckConstraints = inlineCheckConstraints;
        this.inlineForeignKeyConstraints = inlineForeignKeyConstraints;
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
            createUDTIfNotExists,
            createDomainIfNotExists,
            createSequenceIfNotExists,
            createViewIfNotExists,
            createMaterializedViewIfNotExists,
            createOrReplaceView,
            createOrReplaceMaterializedView,




            respectCatalogOrder,
            respectSchemaOrder,
            respectTableOrder,
            respectColumnOrder,
            respectConstraintOrder,
            respectIndexOrder,
            respectUDTOrder,
            respectDomainOrder,
            respectSequenceOrder,




            defaultSequenceFlags,
            includeConstraintsOnViews,
            inlinePrimaryKeyConstraints,
            inlineUniqueConstraints,
            inlineCheckConstraints,
            inlineForeignKeyConstraints
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
            createUDTIfNotExists,
            createDomainIfNotExists,
            createSequenceIfNotExists,
            createViewIfNotExists,
            createMaterializedViewIfNotExists,
            createOrReplaceView,
            createOrReplaceMaterializedView,




            respectCatalogOrder,
            respectSchemaOrder,
            respectTableOrder,
            respectColumnOrder,
            respectConstraintOrder,
            respectIndexOrder,
            respectUDTOrder,
            respectDomainOrder,
            respectSequenceOrder,




            defaultSequenceFlags,
            includeConstraintsOnViews,
            inlinePrimaryKeyConstraints,
            inlineUniqueConstraints,
            inlineCheckConstraints,
            inlineForeignKeyConstraints
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
            createUDTIfNotExists,
            createDomainIfNotExists,
            createSequenceIfNotExists,
            createViewIfNotExists,
            createMaterializedViewIfNotExists,
            createOrReplaceView,
            createOrReplaceMaterializedView,




            respectCatalogOrder,
            respectSchemaOrder,
            respectTableOrder,
            respectColumnOrder,
            respectConstraintOrder,
            respectIndexOrder,
            respectUDTOrder,
            respectDomainOrder,
            respectSequenceOrder,




            defaultSequenceFlags,
            includeConstraintsOnViews,
            inlinePrimaryKeyConstraints,
            inlineUniqueConstraints,
            inlineCheckConstraints,
            inlineForeignKeyConstraints
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
            createUDTIfNotExists,
            createDomainIfNotExists,
            createSequenceIfNotExists,
            createViewIfNotExists,
            createMaterializedViewIfNotExists,
            createOrReplaceView,
            createOrReplaceMaterializedView,




            respectCatalogOrder,
            respectSchemaOrder,
            respectTableOrder,
            respectColumnOrder,
            respectConstraintOrder,
            respectIndexOrder,
            respectUDTOrder,
            respectDomainOrder,
            respectSequenceOrder,




            defaultSequenceFlags,
            includeConstraintsOnViews,
            inlinePrimaryKeyConstraints,
            inlineUniqueConstraints,
            inlineCheckConstraints,
            inlineForeignKeyConstraints
        );
    }

    /**
     * Whether to generate <code>CREATE TYPE IF NOT EXISTS</code> statements.
     * <p>
     * Not all RDBMS support this flag. Check
     * {@link DSLContext#createTypeIfNotExists(Type)} to see if your
     * {@link SQLDialect} supports the clause.
     */
    public final boolean createUDTIfNotExists() {
        return createUDTIfNotExists;
    }

    /**
     * Whether to generate <code>CREATE TYPE IF NOT EXISTS</code> statements.
     */
    public final DDLExportConfiguration createUDTIfNotExists(boolean newCreateUDTIfNotExists) {
        return new DDLExportConfiguration(
            flags,
            createSchemaIfNotExists,
            createTableIfNotExists,
            createIndexIfNotExists,
            newCreateUDTIfNotExists,
            createDomainIfNotExists,
            createSequenceIfNotExists,
            createViewIfNotExists,
            createMaterializedViewIfNotExists,
            createOrReplaceView,
            createOrReplaceMaterializedView,




            respectCatalogOrder,
            respectSchemaOrder,
            respectTableOrder,
            respectColumnOrder,
            respectConstraintOrder,
            respectIndexOrder,
            respectUDTOrder,
            respectDomainOrder,
            respectSequenceOrder,




            defaultSequenceFlags,
            includeConstraintsOnViews,
            inlinePrimaryKeyConstraints,
            inlineUniqueConstraints,
            inlineCheckConstraints,
            inlineForeignKeyConstraints
        );
    }

    /**
     * Whether to generate <code>CREATE DOMAIN IF NOT EXISTS</code> statements.
     * <p>
     * Not all RDBMS support this flag. Check
     * {@link DSLContext#createDomainIfNotExists(Domain)} to see if your
     * {@link SQLDialect} supports the clause.
     */
    public final boolean createDomainIfNotExists() {
        return createDomainIfNotExists;
    }

    /**
     * Whether to generate <code>CREATE DOMAIN IF NOT EXISTS</code> statements.
     */
    public final DDLExportConfiguration createDomainIfNotExists(boolean newCreateDomainIfNotExists) {
        return new DDLExportConfiguration(
            flags,
            createSchemaIfNotExists,
            createTableIfNotExists,
            createIndexIfNotExists,
            createUDTIfNotExists,
            newCreateDomainIfNotExists,
            createSequenceIfNotExists,
            createViewIfNotExists,
            createMaterializedViewIfNotExists,
            createOrReplaceView,
            createOrReplaceMaterializedView,




            respectCatalogOrder,
            respectSchemaOrder,
            respectTableOrder,
            respectColumnOrder,
            respectConstraintOrder,
            respectIndexOrder,
            respectUDTOrder,
            respectDomainOrder,
            respectSequenceOrder,




            defaultSequenceFlags,
            includeConstraintsOnViews,
            inlinePrimaryKeyConstraints,
            inlineUniqueConstraints,
            inlineCheckConstraints,
            inlineForeignKeyConstraints
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
            createUDTIfNotExists,
            createDomainIfNotExists,
            newCreateSequenceIfNotExists,
            createViewIfNotExists,
            createMaterializedViewIfNotExists,
            createOrReplaceView,
            createOrReplaceMaterializedView,




            respectCatalogOrder,
            respectSchemaOrder,
            respectTableOrder,
            respectColumnOrder,
            respectConstraintOrder,
            respectIndexOrder,
            respectUDTOrder,
            respectDomainOrder,
            respectSequenceOrder,




            defaultSequenceFlags,
            includeConstraintsOnViews,
            inlinePrimaryKeyConstraints,
            inlineUniqueConstraints,
            inlineCheckConstraints,
            inlineForeignKeyConstraints
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
            createUDTIfNotExists,
            createDomainIfNotExists,
            createSequenceIfNotExists,
            newCreateViewIfNotExists,
            createMaterializedViewIfNotExists,
            createOrReplaceView,
            createOrReplaceMaterializedView,




            respectCatalogOrder,
            respectSchemaOrder,
            respectTableOrder,
            respectColumnOrder,
            respectConstraintOrder,
            respectIndexOrder,
            respectUDTOrder,
            respectDomainOrder,
            respectSequenceOrder,




            defaultSequenceFlags,
            includeConstraintsOnViews,
            inlinePrimaryKeyConstraints,
            inlineUniqueConstraints,
            inlineCheckConstraints,
            inlineForeignKeyConstraints
        );
    }

    /**
     * Whether to generate <code>CREATE MATERIALIZED VIEW IF NOT EXISTS</code> statements.
     * <p>
     * Not all RDBMS support this flag. Check
     * {@link DSLContext#createMaterializedViewIfNotExists(Table, Field...)} to see if your
     * {@link SQLDialect} supports the clause.
     */
    public final boolean createMaterializedViewIfNotExists() {
        return createMaterializedViewIfNotExists;
    }

    /**
     * Whether to generate <code>CREATE MATERIALIZED VIEW IF NOT EXISTS</code> statements.
     */
    public final DDLExportConfiguration createMaterializedViewIfNotExists(boolean newCreateMaterializedViewIfNotExists) {
        return new DDLExportConfiguration(
            flags,
            createSchemaIfNotExists,
            createTableIfNotExists,
            createIndexIfNotExists,
            createUDTIfNotExists,
            createDomainIfNotExists,
            createSequenceIfNotExists,
            createViewIfNotExists,
            newCreateMaterializedViewIfNotExists,
            createOrReplaceView,
            createOrReplaceMaterializedView,




            respectCatalogOrder,
            respectSchemaOrder,
            respectTableOrder,
            respectColumnOrder,
            respectConstraintOrder,
            respectIndexOrder,
            respectUDTOrder,
            respectDomainOrder,
            respectSequenceOrder,




            defaultSequenceFlags,
            includeConstraintsOnViews,
            inlinePrimaryKeyConstraints,
            inlineUniqueConstraints,
            inlineCheckConstraints,
            inlineForeignKeyConstraints
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
            createUDTIfNotExists,
            createDomainIfNotExists,
            createSequenceIfNotExists,
            createViewIfNotExists,
            createMaterializedViewIfNotExists,
            newCreateOrReplaceView,
            createOrReplaceMaterializedView,




            respectCatalogOrder,
            respectSchemaOrder,
            respectTableOrder,
            respectColumnOrder,
            respectConstraintOrder,
            respectIndexOrder,
            respectUDTOrder,
            respectDomainOrder,
            respectSequenceOrder,




            defaultSequenceFlags,
            includeConstraintsOnViews,
            inlinePrimaryKeyConstraints,
            inlineUniqueConstraints,
            inlineCheckConstraints,
            inlineForeignKeyConstraints
        );
    }

    /**
     * Whether to generate <code>CREATE OR REPLACE MATERIALIZED VIEW</code> statements.
     * <p>
     * Not all RDBMS support this flag. Check
     * {@link DSLContext#createOrReplaceMaterializedView(Table, Field...)} to see if your
     * {@link SQLDialect} supports the clause.
     */
    public final boolean createOrReplaceMaterializedView() {
        return createOrReplaceMaterializedView;
    }

    /**
     * Whether to generate <code>CREATE OR REPLACE MATERIALIZED VIEW</code> statements.
     */
    public final DDLExportConfiguration createOrReplaceMaterializedView(boolean newCreateOrReplaceMaterializedView) {
        return new DDLExportConfiguration(
            flags,
            createSchemaIfNotExists,
            createTableIfNotExists,
            createIndexIfNotExists,
            createUDTIfNotExists,
            createDomainIfNotExists,
            createSequenceIfNotExists,
            createViewIfNotExists,
            createMaterializedViewIfNotExists,
            createOrReplaceView,
            newCreateOrReplaceMaterializedView,




            respectCatalogOrder,
            respectSchemaOrder,
            respectTableOrder,
            respectColumnOrder,
            respectConstraintOrder,
            respectIndexOrder,
            respectUDTOrder,
            respectDomainOrder,
            respectSequenceOrder,




            defaultSequenceFlags,
            includeConstraintsOnViews,
            inlinePrimaryKeyConstraints,
            inlineUniqueConstraints,
            inlineCheckConstraints,
            inlineForeignKeyConstraints
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
            createUDTIfNotExists,
            createDomainIfNotExists,
            createSequenceIfNotExists,
            createViewIfNotExists,
            createMaterializedViewIfNotExists,
            createOrReplaceView,
            createOrReplaceMaterializedView,




            newRespectCatalogOrder,
            respectSchemaOrder,
            respectTableOrder,
            respectColumnOrder,
            respectConstraintOrder,
            respectIndexOrder,
            respectUDTOrder,
            respectDomainOrder,
            respectSequenceOrder,




            defaultSequenceFlags,
            includeConstraintsOnViews,
            inlinePrimaryKeyConstraints,
            inlineUniqueConstraints,
            inlineCheckConstraints,
            inlineForeignKeyConstraints
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
            createUDTIfNotExists,
            createDomainIfNotExists,
            createSequenceIfNotExists,
            createViewIfNotExists,
            createMaterializedViewIfNotExists,
            createOrReplaceView,
            createOrReplaceMaterializedView,




            respectCatalogOrder,
            newRespectSchemaOrder,
            respectTableOrder,
            respectColumnOrder,
            respectConstraintOrder,
            respectIndexOrder,
            respectUDTOrder,
            respectDomainOrder,
            respectSequenceOrder,




            defaultSequenceFlags,
            includeConstraintsOnViews,
            inlinePrimaryKeyConstraints,
            inlineUniqueConstraints,
            inlineCheckConstraints,
            inlineForeignKeyConstraints
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
            createUDTIfNotExists,
            createDomainIfNotExists,
            createSequenceIfNotExists,
            createViewIfNotExists,
            createMaterializedViewIfNotExists,
            createOrReplaceView,
            createOrReplaceMaterializedView,




            respectCatalogOrder,
            respectSchemaOrder,
            newRespectTableOrder,
            respectColumnOrder,
            respectConstraintOrder,
            respectIndexOrder,
            respectUDTOrder,
            respectDomainOrder,
            respectSequenceOrder,




            defaultSequenceFlags,
            includeConstraintsOnViews,
            inlinePrimaryKeyConstraints,
            inlineUniqueConstraints,
            inlineCheckConstraints,
            inlineForeignKeyConstraints
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
            createUDTIfNotExists,
            createDomainIfNotExists,
            createSequenceIfNotExists,
            createViewIfNotExists,
            createMaterializedViewIfNotExists,
            createOrReplaceView,
            createOrReplaceMaterializedView,




            respectCatalogOrder,
            respectSchemaOrder,
            respectTableOrder,
            newRespectColumnOrder,
            respectConstraintOrder,
            respectIndexOrder,
            respectUDTOrder,
            respectDomainOrder,
            respectSequenceOrder,




            defaultSequenceFlags,
            includeConstraintsOnViews,
            inlinePrimaryKeyConstraints,
            inlineUniqueConstraints,
            inlineCheckConstraints,
            inlineForeignKeyConstraints
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
            createUDTIfNotExists,
            createDomainIfNotExists,
            createSequenceIfNotExists,
            createViewIfNotExists,
            createMaterializedViewIfNotExists,
            createOrReplaceView,
            createOrReplaceMaterializedView,




            respectCatalogOrder,
            respectSchemaOrder,
            respectTableOrder,
            respectColumnOrder,
            newRespectConstraintOrder,
            respectIndexOrder,
            respectUDTOrder,
            respectDomainOrder,
            respectSequenceOrder,




            defaultSequenceFlags,
            includeConstraintsOnViews,
            inlinePrimaryKeyConstraints,
            inlineUniqueConstraints,
            inlineCheckConstraints,
            inlineForeignKeyConstraints
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
            createUDTIfNotExists,
            createDomainIfNotExists,
            createSequenceIfNotExists,
            createViewIfNotExists,
            createMaterializedViewIfNotExists,
            createOrReplaceView,
            createOrReplaceMaterializedView,




            respectCatalogOrder,
            respectSchemaOrder,
            respectTableOrder,
            respectColumnOrder,
            respectConstraintOrder,
            newRespectIndexOrder,
            respectUDTOrder,
            respectDomainOrder,
            respectSequenceOrder,




            defaultSequenceFlags,
            includeConstraintsOnViews,
            inlinePrimaryKeyConstraints,
            inlineUniqueConstraints,
            inlineCheckConstraints,
            inlineForeignKeyConstraints
        );
    }

    /**
     * Whether to respect the UDT order produced by the {@link Meta} source when
     * generated domain DDL.
     */
    public final boolean respectUDTOrder() {
        return respectUDTOrder;
    }

    /**
     * Whether to respect the UDT order produced by the {@link Meta} source
     * when generated sequence DDL.
     */
    public final DDLExportConfiguration respectUDTOrder(boolean newRespectUDTOrder) {
        return new DDLExportConfiguration(
            flags,
            createSchemaIfNotExists,
            createTableIfNotExists,
            createIndexIfNotExists,
            createUDTIfNotExists,
            createDomainIfNotExists,
            createSequenceIfNotExists,
            createViewIfNotExists,
            createMaterializedViewIfNotExists,
            createOrReplaceView,
            createOrReplaceMaterializedView,




            respectCatalogOrder,
            respectSchemaOrder,
            respectTableOrder,
            respectColumnOrder,
            respectConstraintOrder,
            respectIndexOrder,
            newRespectUDTOrder,
            respectDomainOrder,
            respectSequenceOrder,




            defaultSequenceFlags,
            includeConstraintsOnViews,
            inlinePrimaryKeyConstraints,
            inlineUniqueConstraints,
            inlineCheckConstraints,
            inlineForeignKeyConstraints
        );
    }

    /**
     * Whether to respect the domain order produced by the {@link Meta} source
     * when generated domain DDL.
     */
    public final boolean respectDomainOrder() {
        return respectDomainOrder;
    }

    /**
     * Whether to respect the domain order produced by the {@link Meta} source
     * when generated sequence DDL.
     */
    public final DDLExportConfiguration respectDomainOrder(boolean newRespectDomainOrder) {
        return new DDLExportConfiguration(
            flags,
            createSchemaIfNotExists,
            createTableIfNotExists,
            createIndexIfNotExists,
            createUDTIfNotExists,
            createDomainIfNotExists,
            createSequenceIfNotExists,
            createViewIfNotExists,
            createMaterializedViewIfNotExists,
            createOrReplaceView,
            createOrReplaceMaterializedView,




            respectCatalogOrder,
            respectSchemaOrder,
            respectTableOrder,
            respectColumnOrder,
            respectConstraintOrder,
            respectIndexOrder,
            respectUDTOrder,
            newRespectDomainOrder,
            respectSequenceOrder,




            defaultSequenceFlags,
            includeConstraintsOnViews,
            inlinePrimaryKeyConstraints,
            inlineUniqueConstraints,
            inlineCheckConstraints,
            inlineForeignKeyConstraints
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
            createUDTIfNotExists,
            createDomainIfNotExists,
            createSequenceIfNotExists,
            createViewIfNotExists,
            createMaterializedViewIfNotExists,
            createOrReplaceView,
            createOrReplaceMaterializedView,




            respectCatalogOrder,
            respectSchemaOrder,
            respectTableOrder,
            respectColumnOrder,
            respectConstraintOrder,
            respectIndexOrder,
            respectUDTOrder,
            respectDomainOrder,
            newRespectSequenceOrder,




            defaultSequenceFlags,
            includeConstraintsOnViews,
            inlinePrimaryKeyConstraints,
            inlineUniqueConstraints,
            inlineCheckConstraints,
            inlineForeignKeyConstraints
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
            createUDTIfNotExists,
            createDomainIfNotExists,
            createSequenceIfNotExists,
            createViewIfNotExists,
            createMaterializedViewIfNotExists,
            createOrReplaceView,
            createOrReplaceMaterializedView,




            respectCatalogOrder,
            respectSchemaOrder,
            respectTableOrder,
            respectColumnOrder,
            respectConstraintOrder,
            respectIndexOrder,
            respectUDTOrder,
            respectDomainOrder,
            respectSequenceOrder,




            newDefaultSequenceFlags,
            includeConstraintsOnViews,
            inlinePrimaryKeyConstraints,
            inlineUniqueConstraints,
            inlineCheckConstraints,
            inlineForeignKeyConstraints
        );
    }

    /**
     * Whether to include constraints on views.
     */
    public final boolean includeConstraintsOnViews() {
        return includeConstraintsOnViews;
    }

    /**
     * Whether to include constraints on views.
     */
    public final DDLExportConfiguration includeConstraintsOnViews(boolean newIncludeConstraintsOnViews) {
        return new DDLExportConfiguration(
            flags,
            createSchemaIfNotExists,
            createTableIfNotExists,
            createIndexIfNotExists,
            createUDTIfNotExists,
            createDomainIfNotExists,
            createSequenceIfNotExists,
            createViewIfNotExists,
            createMaterializedViewIfNotExists,
            createOrReplaceView,
            createOrReplaceMaterializedView,




            respectCatalogOrder,
            respectSchemaOrder,
            respectTableOrder,
            respectColumnOrder,
            respectConstraintOrder,
            respectIndexOrder,
            respectUDTOrder,
            respectDomainOrder,
            respectSequenceOrder,




            defaultSequenceFlags,
            newIncludeConstraintsOnViews,
            inlinePrimaryKeyConstraints,
            inlineUniqueConstraints,
            inlineCheckConstraints,
            inlineForeignKeyConstraints
        );
    }

    /**
     * Whether to inline primary key constraint definitions with the table
     * definition.
     */
    public final boolean inlinePrimaryKeyConstraints() {
        return inlinePrimaryKeyConstraints;
    }

    /**
     * Whether to inline foreign key constraint definitions with the table
     * definition.
     */
    public final DDLExportConfiguration inlinePrimaryKeyConstraints(boolean newInlinePrimaryKeyConstraints) {
        return new DDLExportConfiguration(
            flags,
            createSchemaIfNotExists,
            createTableIfNotExists,
            createIndexIfNotExists,
            createUDTIfNotExists,
            createDomainIfNotExists,
            createSequenceIfNotExists,
            createViewIfNotExists,
            createMaterializedViewIfNotExists,
            createOrReplaceView,
            createOrReplaceMaterializedView,




            respectCatalogOrder,
            respectSchemaOrder,
            respectTableOrder,
            respectColumnOrder,
            respectConstraintOrder,
            respectIndexOrder,
            respectUDTOrder,
            respectDomainOrder,
            respectSequenceOrder,




            defaultSequenceFlags,
            includeConstraintsOnViews,
            newInlinePrimaryKeyConstraints,
            inlineUniqueConstraints,
            inlineCheckConstraints,
            inlineForeignKeyConstraints
        );
    }

    /**
     * Whether to inline unique constraint definitions with the table
     * definition.
     */
    public final boolean inlineUniqueConstraints() {
        return inlineUniqueConstraints;
    }

    /**
     * Whether to inline unique constraint definitions with the table
     * definition.
     */
    public final DDLExportConfiguration inlineUniqueConstraints(boolean newInlineUniqueConstraints) {
        return new DDLExportConfiguration(
            flags,
            createSchemaIfNotExists,
            createTableIfNotExists,
            createIndexIfNotExists,
            createUDTIfNotExists,
            createDomainIfNotExists,
            createSequenceIfNotExists,
            createViewIfNotExists,
            createMaterializedViewIfNotExists,
            createOrReplaceView,
            createOrReplaceMaterializedView,




            respectCatalogOrder,
            respectSchemaOrder,
            respectTableOrder,
            respectColumnOrder,
            respectConstraintOrder,
            respectIndexOrder,
            respectUDTOrder,
            respectDomainOrder,
            respectSequenceOrder,




            defaultSequenceFlags,
            includeConstraintsOnViews,
            inlinePrimaryKeyConstraints,
            newInlineUniqueConstraints,
            inlineCheckConstraints,
            inlineForeignKeyConstraints
        );
    }

    /**
     * Whether to inline check constraint definitions with the table
     * definition.
     */
    public final boolean inlineCheckConstraints() {
        return inlineCheckConstraints;
    }

    /**
     * Whether to inline check constraint definitions with the table
     * definition.
     */
    public final DDLExportConfiguration inlineCheckConstraints(boolean newInlineCheckConstraints) {
        return new DDLExportConfiguration(
            flags,
            createSchemaIfNotExists,
            createTableIfNotExists,
            createIndexIfNotExists,
            createUDTIfNotExists,
            createDomainIfNotExists,
            createSequenceIfNotExists,
            createViewIfNotExists,
            createMaterializedViewIfNotExists,
            createOrReplaceView,
            createOrReplaceMaterializedView,




            respectCatalogOrder,
            respectSchemaOrder,
            respectTableOrder,
            respectColumnOrder,
            respectConstraintOrder,
            respectIndexOrder,
            respectUDTOrder,
            respectDomainOrder,
            respectSequenceOrder,




            defaultSequenceFlags,
            includeConstraintsOnViews,
            inlinePrimaryKeyConstraints,
            inlineUniqueConstraints,
            newInlineCheckConstraints,
            inlineForeignKeyConstraints
        );
    }

    /**
     * Whether to inline foreign key constraint definitions with the table
     * definition.
     */
    public final InlineForeignKeyConstraints inlineForeignKeyConstraints() {
        return inlineForeignKeyConstraints;
    }

    /**
     * Whether to inline foreign key constraint definitions with the table
     * definition.
     */
    public final DDLExportConfiguration inlineForeignKeyConstraints(InlineForeignKeyConstraints newInlineForeignKeyConstraints) {
        return new DDLExportConfiguration(
            flags,
            createSchemaIfNotExists,
            createTableIfNotExists,
            createIndexIfNotExists,
            createUDTIfNotExists,
            createDomainIfNotExists,
            createSequenceIfNotExists,
            createViewIfNotExists,
            createMaterializedViewIfNotExists,
            createOrReplaceView,
            createOrReplaceMaterializedView,




            respectCatalogOrder,
            respectSchemaOrder,
            respectTableOrder,
            respectColumnOrder,
            respectConstraintOrder,
            respectIndexOrder,
            respectUDTOrder,
            respectDomainOrder,
            respectSequenceOrder,




            defaultSequenceFlags,
            includeConstraintsOnViews,
            inlinePrimaryKeyConstraints,
            inlineUniqueConstraints,
            inlineCheckConstraints,
            newInlineForeignKeyConstraints
        );
    }
}
