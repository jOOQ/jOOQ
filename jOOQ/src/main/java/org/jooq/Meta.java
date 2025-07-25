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

// ...
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.DUCKDB;
import static org.jooq.SQLDialect.FIREBIRD;
// ...
import static org.jooq.SQLDialect.H2;
// ...
import static org.jooq.SQLDialect.HSQLDB;
// ...
// ...
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
// ...
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
// ...
import static org.jooq.SQLDialect.YUGABYTEDB;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import org.jooq.conf.Settings;
import org.jooq.exception.DataAccessException;
import org.jooq.util.xml.jaxb.InformationSchema;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A wrapping object for {@link DatabaseMetaData} or for other sources of
 * database meta information (e.g. {@link InformationSchema})
 * <p>
 * This object can be obtained through {@link DSLContext#meta()} in order to
 * provide convenient access to your database meta data. This abstraction has
 * two purposes:
 * <p>
 * <ol>
 * <li>To increase API convenience, as no checked {@link SQLException} is
 * thrown, only the unchecked {@link DataAccessException}</li>
 * <li>To increase API convenience, as the returned objects are always jOOQ
 * objects, not JDBC {@link ResultSet} objects with hard-to-remember API
 * constraints</li>
 * </ol>
 * <p>
 * This type is a {@link Scope} with independent lifecycle and its own
 * {@link #data()} map.
 *
 * @author Lukas Eder
 */
public interface Meta extends Scope {

    /**
     * Get all catalog objects from the underlying meta data source.
     * <p>
     * For those databases that don't really support JDBC meta data catalogs, a
     * single empty catalog (named <code>""</code>) will be returned. In other
     * words, there is always at least one catalog in a database.
     *
     * @throws DataAccessException If something went wrong fetching the meta
     *             objects
     */
    @NotNull
    @Support
    List<Catalog> getCatalogs() throws DataAccessException;

    /**
     * Get a catalog object by name from the underlying meta data source, or
     * <code>null</code> if no such object exists.
     *
     * @throws DataAccessException If something went wrong fetching the meta
     *             objects
     */
    @NotNull
    @Support
    Catalog getCatalog(String name) throws DataAccessException;

    /**
     * Get a catalog object by name from the underlying meta data source, or
     * <code>null</code> if no such object exists.
     *
     * @throws DataAccessException If something went wrong fetching the meta
     *             objects
     */
    @NotNull
    @Support
    Catalog getCatalog(Name name) throws DataAccessException;

    /**
     * Get all schema objects from the underlying meta data source.
     *
     * @throws DataAccessException If something went wrong fetching the meta
     *             objects
     */
    @NotNull
    @Support
    List<Schema> getSchemas() throws DataAccessException;

    /**
     * Get all schema objects by name from the underlying meta data source.
     *
     * @throws DataAccessException If something went wrong fetching the meta
     *             objects
     */
    @NotNull
    @Support
    List<Schema> getSchemas(String name) throws DataAccessException;

    /**
     * Get all schema objects by name from the underlying meta data source.
     *
     * @throws DataAccessException If something went wrong fetching the meta
     *             objects
     */
    @NotNull
    @Support
    List<Schema> getSchemas(Name name) throws DataAccessException;

    /**
     * Get all table objects from the underlying meta data source.
     *
     * @throws DataAccessException If something went wrong fetching the meta
     *             objects
     */
    @NotNull
    @Support
    List<Table<?>> getTables() throws DataAccessException;

    /**
     * Get all table objects by name from the underlying meta data source.
     *
     * @throws DataAccessException If something went wrong fetching the meta
     *             objects
     */
    @NotNull
    @Support
    List<Table<?>> getTables(String name) throws DataAccessException;

    /**
     * Get all table objects by name from the underlying meta data source.
     *
     * @throws DataAccessException If something went wrong fetching the meta
     *             objects
     */
    @NotNull
    @Support
    List<Table<?>> getTables(Name name) throws DataAccessException;

    /**
     * Resolve a {@link Table} by its name according to interpreter
     * {@link Settings}.
     * <p>
     * Unlike {@link #getTables(String)}, which may list multiple tables by
     * their case sensitive name, this method finds the best matching table
     * according to the argument as well as the underlying
     * {@link Settings#getInterpreterNameLookupCaseSensitivity()} and
     * {@link Settings#getInterpreterSearchPath()}, or returns <code>null</code>
     * if no such table was found.
     */
    @Nullable
    @Support
    Table<?> resolveTable(String name) throws DataAccessException;

    /**
     * Resolve a {@link Table} by its name according to interpreter
     * {@link Settings}.
     * <p>
     * Unlike {@link #getTables(Name)}, which may list multiple tables by their
     * case sensitive name, this method finds the best matching table according
     * to the argument {@link Name#qualifier()} as well as the underlying
     * {@link Settings#getInterpreterNameLookupCaseSensitivity()} and
     * {@link Settings#getInterpreterSearchPath()}, or returns <code>null</code>
     * if no such table was found.
     */
    @Nullable
    @Support
    Table<?> resolveTable(Name name) throws DataAccessException;

    /**
     * Get all {@link UDT} objects from the underlying meta data source.
     *
     * @throws DataAccessException If something went wrong fetching the meta
     *             objects
     */
    @NotNull
    @Support({ DUCKDB, POSTGRES, YUGABYTEDB })
    List<UDT<?>> getUDTs() throws DataAccessException;

    /**
     * Get all {@link UDT} objects by name from the underlying meta data source.
     *
     * @throws DataAccessException If something went wrong fetching the meta
     *             objects
     */
    @NotNull
    @Support({ DUCKDB, POSTGRES, YUGABYTEDB })
    List<UDT<?>> getUDTs(String name) throws DataAccessException;

    /**
     * Get all {@link UDT} objects by name from the underlying meta data source.
     *
     * @throws DataAccessException If something went wrong fetching the meta
     *             objects
     */
    @NotNull
    @Support({ DUCKDB, POSTGRES, YUGABYTEDB })
    List<UDT<?>> getUDTs(Name name) throws DataAccessException;

    /**
     * Resolve a {@link UDT} by its name according to interpreter
     * {@link Settings}.
     * <p>
     * Unlike {@link #getUDTs(String)}, which may list multiple UDTs by their
     * case sensitive name, this method finds the best matching UDTs according
     * to the argument as well as the underlying
     * {@link Settings#getInterpreterNameLookupCaseSensitivity()} and
     * {@link Settings#getInterpreterSearchPath()}, or returns <code>null</code>
     * if no such UDTs was found.
     */
    @Nullable
    @Support({ DUCKDB, POSTGRES, YUGABYTEDB })
    UDT<?> resolveUDT(String name) throws DataAccessException;

    /**
     * Resolve a {@link UDT} by its name according to interpreter
     * {@link Settings}.
     * <p>
     * Unlike {@link #getUDTs(Name)}, which may list multiple UDTs by their case
     * sensitive name, this method finds the best matching UDTs according to the
     * argument {@link Name#qualifier()} as well as the underlying
     * {@link Settings#getInterpreterNameLookupCaseSensitivity()} and
     * {@link Settings#getInterpreterSearchPath()}, or returns <code>null</code>
     * if no such UDTs was found.
     */
    @Nullable
    @Support({ DUCKDB, POSTGRES, YUGABYTEDB })
    UDT<?> resolveUDT(Name name) throws DataAccessException;

    /**
     * Get all domain objects from the underlying meta data source.
     *
     * @throws DataAccessException If something went wrong fetching the meta
     *             objects
     */
    @NotNull
    @Support({ FIREBIRD, H2, HSQLDB, POSTGRES, YUGABYTEDB })
    List<Domain<?>> getDomains() throws DataAccessException;

    /**
     * Get all domain objects by name from the underlying meta data source.
     *
     * @throws DataAccessException If something went wrong fetching the meta
     *             objects
     */
    @NotNull
    @Support({ FIREBIRD, H2, HSQLDB, POSTGRES, YUGABYTEDB })
    List<Domain<?>> getDomains(String name) throws DataAccessException;

    /**
     * Get all domain objects by name from the underlying meta data source.
     *
     * @throws DataAccessException If something went wrong fetching the meta
     *             objects
     */
    @NotNull
    @Support({ FIREBIRD, H2, HSQLDB, POSTGRES, YUGABYTEDB })
    List<Domain<?>> getDomains(Name name) throws DataAccessException;

    /**
     * Resolve a {@link Domain} by its name according to interpreter
     * {@link Settings}.
     * <p>
     * Unlike {@link #getDomains(String)}, which may list multiple domains by
     * their case sensitive name, this method finds the best matching domain
     * according to the argument as well as the underlying
     * {@link Settings#getInterpreterNameLookupCaseSensitivity()} and
     * {@link Settings#getInterpreterSearchPath()}, or returns <code>null</code>
     * if no such domain was found.
     */
    @Nullable
    @Support({ H2, POSTGRES })
    Domain<?> resolveDomain(String name) throws DataAccessException;

    /**
     * Resolve a {@link Domain} by its name according to interpreter
     * {@link Settings}.
     * <p>
     * Unlike {@link #getDomains(Name)}, which may list multiple domains by
     * their case sensitive name, this method finds the best matching domain
     * according to the argument {@link Name#qualifier()} as well as the
     * underlying {@link Settings#getInterpreterNameLookupCaseSensitivity()} and
     * {@link Settings#getInterpreterSearchPath()}, or returns <code>null</code>
     * if no such domain was found.
     */
    @Nullable
    @Support({ H2, POSTGRES })
    Domain<?> resolveDomain(Name name) throws DataAccessException;










































































































































































    /**
     * Get all sequence objects from the underlying meta data source.
     *
     * @throws DataAccessException If something went wrong fetching the meta
     *             objects
     */
    @NotNull
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, POSTGRES })
    List<Sequence<?>> getSequences() throws DataAccessException;

    /**
     * Get all sequence objects by name from the underlying meta data source.
     *
     * @throws DataAccessException If something went wrong fetching the meta
     *             objects
     */
    @NotNull
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, POSTGRES })
    List<Sequence<?>> getSequences(String name) throws DataAccessException;

    /**
     * Get all sequence objects by name from the underlying meta data source.
     *
     * @throws DataAccessException If something went wrong fetching the meta
     *             objects
     */
    @NotNull
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, POSTGRES })
    List<Sequence<?>> getSequences(Name name) throws DataAccessException;

    /**
     * Resolve a {@link Sequence} by its name according to interpreter
     * {@link Settings}.
     * <p>
     * Unlike {@link #getSequences(String)}, which may list multiple sequences
     * by their case sensitive name, this method finds the best matching
     * sequence according to the argument as well as the underlying
     * {@link Settings#getInterpreterNameLookupCaseSensitivity()} and
     * {@link Settings#getInterpreterSearchPath()}, or returns <code>null</code>
     * if no such sequence was found.
     */
    @Nullable
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, POSTGRES })
    Sequence<?> resolveSequence(String name) throws DataAccessException;

    /**
     * Resolve a {@link Sequence} by its name according to interpreter
     * {@link Settings}.
     * <p>
     * Unlike {@link #getSequences(Name)}, which may list multiple sequences by
     * their case sensitive name, this method finds the best matching sequence
     * according to the argument {@link Name#qualifier()} as well as the
     * underlying {@link Settings#getInterpreterNameLookupCaseSensitivity()} and
     * {@link Settings#getInterpreterSearchPath()}, or returns <code>null</code>
     * if no such sequence was found.
     */
    @Nullable
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, POSTGRES })
    Sequence<?> resolveSequence(Name name) throws DataAccessException;

    /**
     * Get all primary keys from the underlying meta data source.
     *
     * @throws DataAccessException If something went wrong fetching the meta
     *             objects
     */
    @NotNull
    @Support
    List<UniqueKey<?>> getPrimaryKeys() throws DataAccessException;

    /**
     * Get all primary keys from the underlying meta data source.
     *
     * @throws DataAccessException If something went wrong fetching the meta
     *             objects
     */
    @NotNull
    @Support
    List<UniqueKey<?>> getPrimaryKeys(String name) throws DataAccessException;

    /**
     * Get all primary keys from the underlying meta data source.
     *
     * @throws DataAccessException If something went wrong fetching the meta
     *             objects
     */
    @NotNull
    @Support
    List<UniqueKey<?>> getPrimaryKeys(Name name) throws DataAccessException;

    /**
     * Get all unique keys from the underlying meta data source.
     *
     * @throws DataAccessException If something went wrong fetching the meta
     *             objects
     */
    @NotNull
    @Support
    List<UniqueKey<?>> getUniqueKeys() throws DataAccessException;

    /**
     * Get all unique keys from the underlying meta data source.
     *
     * @throws DataAccessException If something went wrong fetching the meta
     *             objects
     */
    @NotNull
    @Support
    List<UniqueKey<?>> getUniqueKeys(String name) throws DataAccessException;

    /**
     * Get all unique keys from the underlying meta data source.
     *
     * @throws DataAccessException If something went wrong fetching the meta
     *             objects
     */
    @NotNull
    @Support
    List<UniqueKey<?>> getUniqueKeys(Name name) throws DataAccessException;

    /**
     * Get all foreign keys from the underlying meta data source.
     *
     * @throws DataAccessException If something went wrong fetching the meta
     *             objects
     */
    @NotNull
    @Support
    List<ForeignKey<?, ?>> getForeignKeys() throws DataAccessException;

    /**
     * Get all foreign keys from the underlying meta data source.
     *
     * @throws DataAccessException If something went wrong fetching the meta
     *             objects
     */
    @NotNull
    @Support
    List<ForeignKey<?, ?>> getForeignKeys(String name) throws DataAccessException;

    /**
     * Get all foreign keys from the underlying meta data source.
     *
     * @throws DataAccessException If something went wrong fetching the meta
     *             objects
     */
    @NotNull
    @Support
    List<ForeignKey<?, ?>> getForeignKeys(Name name) throws DataAccessException;

    /**
     * Get all indexes from the underlying meta data sources.
     *
     * @throws DataAccessException If something went wrong fetching the meta
     *             objects
     */
    @NotNull
    @Support
    List<Index> getIndexes() throws DataAccessException;

    /**
     * Get all indexes from the underlying meta data sources.
     *
     * @throws DataAccessException If something went wrong fetching the meta
     *             objects
     */
    @NotNull
    @Support
    List<Index> getIndexes(String name) throws DataAccessException;

    /**
     * Get all indexes from the underlying meta data sources.
     *
     * @throws DataAccessException If something went wrong fetching the meta
     *             objects
     */
    @NotNull
    @Support
    List<Index> getIndexes(Name name) throws DataAccessException;

    /**
     * Create a wrapper {@link Meta} instance filtering out some catalogs.
     */
    @NotNull
    Meta filterCatalogs(Predicate<? super Catalog> filter);

    /**
     * Create a wrapper {@link Meta} instance filtering out some schemas.
     */
    @NotNull
    Meta filterSchemas(Predicate<? super Schema> filter);

    /**
     * Create a wrapper {@link Meta} instance filtering out some tables.
     */
    @NotNull
    Meta filterTables(Predicate<? super Table<?>> filter);

    /**
     * Create a wrapper {@link Meta} instance filtering out some UDTs.
     */
    @NotNull
    Meta filterUDTs(Predicate<? super UDT<?>> filter);

    /**
     * Create a wrapper {@link Meta} instance filtering out some domains.
     */
    @NotNull
    Meta filterDomains(Predicate<? super Domain<?>> filter);



















    /**
     * Create a wrapper {@link Meta} instance filtering out some sequences.
     */
    @NotNull
    Meta filterSequences(Predicate<? super Sequence<?>> filter);

    /**
     * Create a wrapper {@link Meta} instance filtering out some primary keys.
     */
    @NotNull
    Meta filterPrimaryKeys(Predicate<? super UniqueKey<?>> filter);

    /**
     * Create a wrapper {@link Meta} instance filtering out some unique keys.
     */
    @NotNull
    Meta filterUniqueKeys(Predicate<? super UniqueKey<?>> filter);

    /**
     * Create a wrapper {@link Meta} instance filtering out some foreign keys.
     */
    @NotNull
    Meta filterForeignKeys(Predicate<? super ForeignKey<?, ?>> filter);

    /**
     * Create a wrapper {@link Meta} instance filtering out some indexes.
     */
    @NotNull
    Meta filterIndexes(Predicate<? super Index> filter);

    /**
     * Eager-create an in-memory copy of this {@link Meta} instance without any
     * connection to the original data source.
     */
    @NotNull
    Meta snapshot() throws DataAccessException;

    /**
     * Collect the object dependencies between the various objects in this {@link Meta}.
     * <p>
     * This is a commercial-only feature.
     */
    @NotNull
    Dependencies dependencies() throws DataAccessException;

    /**
     * Generate a creation script for the entire meta data.
     *
     * @throws DataAccessException If something went wrong fetching the meta
     *             objects
     */
    @NotNull
    Queries ddl() throws DataAccessException;

    /**
     * Generate a creation script for the entire meta data.
     *
     * @throws DataAccessException If something went wrong fetching the meta
     *             objects
     */
    @NotNull
    Queries ddl(DDLExportConfiguration configuration) throws DataAccessException;

    /**
     * Apply a migration to this meta to produce a new {@link Meta}.
     *
     * @see Parser#parse(String)
     * @throws DataAccessException If something went wrong fetching the meta
     *             objects
     */
    @NotNull
    Meta apply(String migration) throws DataAccessException;

    /**
     * Apply a migration to this meta to produce a new {@link Meta}.
     *
     * @throws DataAccessException If something went wrong fetching the meta
     *             objects
     */
    @NotNull
    Meta apply(Query... migration) throws DataAccessException;

    /**
     * Apply a migration to this meta to produce a new {@link Meta}.
     *
     * @throws DataAccessException If something went wrong fetching the meta
     *             objects
     */
    @NotNull
    Meta apply(Collection<? extends Query> migration) throws DataAccessException;

    /**
     * Apply a migration to this meta to produce a new {@link Meta}.
     *
     * @throws DataAccessException If something went wrong fetching the meta
     *             objects
     */
    @NotNull
    Meta apply(Queries migration) throws DataAccessException;

    /**
     * Generate a migration script to get from this meta data to another one.
     * <p>
     * See {@link #migrateTo(Meta, MigrationConfiguration)} for more details.
     *
     * @throws DataAccessException If something went wrong fetching the meta
     *             objects
     * @see #migrateTo(Meta, MigrationConfiguration)
     */
    @NotNull
    Queries migrateTo(Meta other) throws DataAccessException;

    /**
     * Generate a migration script to get from this meta data to another one.
     * <p>
     * To some extent, some database migrations can be generated automatically
     * by comparing two versions of a schema. This is what
     * <code>migrateTo()</code> does. It supports:
     * <p>
     * <ul>
     * <li>Schema additions / removals</li>
     * <li>Table additions / removals</li>
     * <li>Column additions / removals</li>
     * <li>Column data type changes</li>
     * <li>Constraint additions / removals</li>
     * <li>Index additions / removals</li>
     * <li>Sequence additions / removals</li>
     * <li>Comment additions / removals</li>
     * </ul>
     * <p>
     * More complex, structural changes, such as moving some columns from one
     * table to another, or turning a to-one relationship into a to-many
     * relationship, as well as data migrations, can currently not be detected
     * automatically.
     *
     * @throws DataAccessException If something went wrong fetching the meta
     *             objects
     */
    @NotNull
    Queries migrateTo(Meta other, MigrationConfiguration configuration) throws DataAccessException;

    /**
     * Export to the {@link InformationSchema} format.
     * <p>
     * This allows for serialising schema meta information as XML using JAXB.
     * See also {@link Constants#XSD_META} for details.
     */
    @NotNull
    InformationSchema informationSchema() throws DataAccessException;
}
