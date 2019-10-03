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

    private final boolean          createSchemaIfNotExists;
    private final boolean          createTableIfNotExists;
    private final boolean          createSequenceIfNotExists;
    private final EnumSet<DDLFlag> flags;

    /**
     * Create a new default export configuration instance.
     */
    public DDLExportConfiguration() {
        this(
            EnumSet.allOf(DDLFlag.class),
            false,
            false,
            false
        );
    }

    private DDLExportConfiguration(
        Collection<DDLFlag> flags,
        boolean createSchemaIfNotExists,
        boolean createTableIfNotExists,
        boolean createSequenceIfNotExists
    ) {
        this.flags = EnumSet.copyOf(flags);
        this.createSchemaIfNotExists = createSchemaIfNotExists;
        this.createTableIfNotExists = createTableIfNotExists;
        this.createSequenceIfNotExists = createSequenceIfNotExists;
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
        return new DDLExportConfiguration(newFlags, createSchemaIfNotExists, createTableIfNotExists, createSequenceIfNotExists);
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
        return new DDLExportConfiguration(flags, newCreateSchemaIfNotExists, createTableIfNotExists, createSequenceIfNotExists);
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
        return new DDLExportConfiguration(flags, createSchemaIfNotExists, newCreateTableIfNotExists, createSequenceIfNotExists);
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
        return new DDLExportConfiguration(flags, createSchemaIfNotExists, createTableIfNotExists, newCreateSequenceIfNotExists);
    }
}
