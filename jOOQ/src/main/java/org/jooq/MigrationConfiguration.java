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

/**
 * A configuration type for use with the various {@link Meta#migrateTo(Meta)}
 * methods.
 *
 * @author Lukas Eder
 */
public final class MigrationConfiguration {

    private final boolean alterTableAddMultiple;
    private final boolean alterTableDropMultiple;
    private final boolean dropSchemaCascade;
    private final boolean dropTableCascade;
    private final boolean alterTableDropCascade;
    private final boolean createOrReplaceView;
    private final boolean respectColumnOrder;

    /**
     * Create a new default export configuration instance.
     */
    public MigrationConfiguration() {
        this(
            false,
            false,
            false,
            false,
            false,
            false,
            false
        );
    }

    private MigrationConfiguration(
        boolean alterTableAddMultiple,
        boolean alterTableDropMultiple,
        boolean dropSchemaCascade,
        boolean dropTableCascade,
        boolean alterTableDropCascade,
        boolean createOrReplaceView,
        boolean respectColumnOrder
    ) {
        this.alterTableAddMultiple = alterTableAddMultiple;
        this.alterTableDropMultiple = alterTableDropMultiple;
        this.dropSchemaCascade = dropSchemaCascade;
        this.dropTableCascade = dropTableCascade;
        this.alterTableDropCascade = alterTableDropCascade;
        this.createOrReplaceView = createOrReplaceView;
        this.respectColumnOrder = respectColumnOrder;
    }

    /**
     * Whether <code>ALTER TABLE</code> statements should add multiple columns
     * and constraints in a single statement where supported.
     */
    public final boolean alterTableAddMultiple() {
        return alterTableAddMultiple;
    }

    /**
     * Whether <code>ALTER TABLE</code> statements should add multiple columns
     * and constraints in a single statement where supported.
     */
    public final MigrationConfiguration alterTableAddMultiple(boolean newAlterTableAddMultiple) {
        return new MigrationConfiguration(
            newAlterTableAddMultiple,
            alterTableDropMultiple,
            dropSchemaCascade,
            dropTableCascade,
            alterTableDropCascade,
            createOrReplaceView,
            respectColumnOrder
        );
    }

    /**
     * Whether <code>ALTER TABLE</code> statements should add multiple columns
     * and constraints in a single statement where supported.
     */
    public final boolean alterTableDropMultiple() {
        return alterTableDropMultiple;
    }

    /**
     * Whether <code>ALTER TABLE</code> statements should drop multiple columns
     * and constraints in a single statement where supported.
     */
    public final MigrationConfiguration alterTableDropMultiple(boolean newAlterTableDropMultiple) {
        return new MigrationConfiguration(
            alterTableAddMultiple,
            newAlterTableDropMultiple,
            dropSchemaCascade,
            dropTableCascade,
            alterTableDropCascade,
            createOrReplaceView,
            respectColumnOrder
        );
    }

    /**
     * Whether <code>DROP SCHEMA</code> statements should have a
     * <code>CASCADE</code> clause where supported.
     */
    public final boolean dropSchemaCascade() {
        return dropSchemaCascade;
    }

    /**
     * Whether <code>DROP SCHEMA</code> statements should have a
     * <code>CASCADE</code> clause where supported.
     */
    public final MigrationConfiguration dropSchemaCascade(boolean newDropSchemaCascade) {
        return new MigrationConfiguration(
            alterTableAddMultiple,
            alterTableDropMultiple,
            newDropSchemaCascade,
            dropTableCascade,
            alterTableDropCascade,
            createOrReplaceView,
            respectColumnOrder
        );
    }

    /**
     * Whether <code>DROP TABLE</code> statements should have a
     * <code>CASCADE [ CONSTRAINTS ]</code> clause where supported.
     */
    public final boolean dropTableCascade() {
        return dropTableCascade;
    }

    /**
     * Whether <code>DROP TABLE</code> statements should have a
     * <code>CASCADE [ CONSTRAINTS ]</code> clause where supported.
     */
    public final MigrationConfiguration dropTableCascade(boolean newDropTableCascade) {
        return new MigrationConfiguration(
            alterTableAddMultiple,
            alterTableDropMultiple,
            dropSchemaCascade,
            newDropTableCascade,
            alterTableDropCascade,
            createOrReplaceView,
            respectColumnOrder
        );
    }

    /**
     * Whether <code>ALTER TABLE .. DROP</code> statements should have a
     * <code>CASCADE [ CONSTRAINTS ]</code> clause where supported.
     */
    public final boolean alterTableDropCascade() {
        return alterTableDropCascade;
    }

    /**
     * Whether <code>ALTER TABLE .. DROP</code> statements should have a
     * <code>CASCADE [ CONSTRAINTS ]</code> clause where supported.
     */
    public final MigrationConfiguration alterTableDropCascade(boolean newAlterTableDropCascade) {
        return new MigrationConfiguration(
            alterTableAddMultiple,
            alterTableDropMultiple,
            dropSchemaCascade,
            dropTableCascade,
            newAlterTableDropCascade,
            createOrReplaceView,
            respectColumnOrder
        );
    }

    /**
     * Whether the views should be (create-or-)replaced or dropped and re-created.
     */
    public final boolean createOrReplaceView() {
        return createOrReplaceView;
    }

    /**
     * Whether the views should be (create-or-)replaced or dropped and re-created.
     */
    public final MigrationConfiguration createOrReplaceView(boolean newCreateOrReplaceView) {
        return new MigrationConfiguration(
            alterTableAddMultiple,
            alterTableDropMultiple,
            dropSchemaCascade,
            dropTableCascade,
            alterTableDropCascade,
            newCreateOrReplaceView,
            respectColumnOrder
        );
    }

    /**
     * Whether the column order should be respected in a migration.
     */
    public final boolean respectColumnOrder() {
        return respectColumnOrder;
    }

    /**
     * Whether the column order should be respected in a migration.
     */
    public final MigrationConfiguration respectColumnOrder(boolean newRespectColumnOrder) {
        return new MigrationConfiguration(
            alterTableAddMultiple,
            alterTableDropMultiple,
            dropSchemaCascade,
            dropTableCascade,
            alterTableDropCascade,
            createOrReplaceView,
            newRespectColumnOrder
        );
    }
}
