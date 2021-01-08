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

import java.util.List;
import java.util.stream.Stream;

import org.jooq.impl.DSL;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A schema.
 * <p>
 * Standard SQL object identifiers come in 3 parts:
 * <code>[catalog].[schema].[object]</code>. The schema is an object that groups
 * a set of objects, where objects can be {@link Table}, {@link Sequence},
 * {@link Routine} and many other types of objects.
 * <p>
 * If your RDBMS supports schemas, and jOOQ supports using schemas with your
 * RDBMS, then generated schemas references can be used to qualify objects
 * <p>
 * <strong>Example:</strong>
 * <p>
 * <code><pre>
 * // Assuming import static org.jooq.impl.DSL.*;
 *
 * using(configuration)
 *    .select(SCHEMA.ACTOR.FIRST_NAME, SCHEMA.ACTOR.LAST_NAME)
 *    .from(SCHEMA.ACTOR)
 *    .fetch();
 * </pre></code>
 * <p>
 * <strong>Compatibility:</strong>
 * <p>
 * Database products like {@link SQLDialect#MYSQL} and related dialects, such as
 * {@link SQLDialect#MARIADB} use catalogs ("databases") instead of schemas, and
 * lack schema support. For historic reasons, jOOQ treats MySQL catalogs as
 * schemas and does not support any catalog qualifier in MySQL.
 * <p>
 * Instances can be created using {@link DSL#schema(Name)} and overloads.
 *
 * @author Lukas Eder
 */
public interface Schema extends Named {

    /**
     * The catalog of this schema.
     */
    @Nullable
    Catalog getCatalog();

    /**
     * Stream all tables contained in this schema.
     */
    @NotNull
    Stream<Table<?>> tableStream();

    /**
     * List all tables contained in this schema.
     */
    @NotNull
    List<Table<?>> getTables();

    /**
     * Get a table by its name (case-sensitive) in this schema, or
     * <code>null</code> if no such table exists.
     */
    @Nullable
    Table<?> getTable(String name);

    /**
     * Get a table by its qualified or unqualified name in this schema, or
     * <code>null</code> if no such table exists.
     */
    @Nullable
    Table<?> getTable(Name name);

    /**
     * Stream all primary keys contained in this schema.
     */
    @NotNull
    Stream<UniqueKey<?>> primaryKeyStream();

    /**
     * List all primary keys contained in this schema.
     */
    @NotNull
    List<UniqueKey<?>> getPrimaryKeys();

    /**
     * Get primary keys by their name (case-sensitive) in this schema.
     */
    @NotNull
    List<UniqueKey<?>> getPrimaryKeys(String name);

    /**
     * Get primary keys by their qualified or unqualified name in this schema.
     */
    @NotNull
    List<UniqueKey<?>> getPrimaryKeys(Name name);

    /**
     * Stream all unique keys (including primary keys) contained in this schema.
     */
    @NotNull
    Stream<UniqueKey<?>> uniqueKeyStream();

    /**
     * List all unique keys (including primary keys) contained in this schema.
     */
    @NotNull
    List<UniqueKey<?>> getUniqueKeys();

    /**
     * Get unique keys (including primary keys) by their name (case-sensitive)
     * in this schema.
     */
    @NotNull
    List<UniqueKey<?>> getUniqueKeys(String name);

    /**
     * Get unique keys (including primary keys) by their qualified or
     * unqualified name in this schema.
     */
    @NotNull
    List<UniqueKey<?>> getUniqueKeys(Name name);

    /**
     * Stream all foreign keys contained in this schema.
     */
    @NotNull
    Stream<ForeignKey<?, ?>> foreignKeyStream();

    /**
     * List all foreign keys contained in this schema.
     */
    @NotNull
    List<ForeignKey<?, ?>> getForeignKeys();

    /**
     * Get foreign keys by their name (case-sensitive) in this schema.
     */
    @NotNull
    List<ForeignKey<?, ?>> getForeignKeys(String name);

    /**
     * Get foreign keys by their qualified or unqualified name in this schema.
     */
    @NotNull
    List<ForeignKey<?, ?>> getForeignKeys(Name name);

    /**
     * Stream all indexes contained in this schema.
     */
    @NotNull
    Stream<Index> indexStream();

    /**
     * List all indexes contained in this schema.
     */
    @NotNull
    List<Index> getIndexes();

    /**
     * Get indexes by their name (case-sensitive) in this schema.
     */
    @NotNull
    List<Index> getIndexes(String name);

    /**
     * Get indexes by their qualified or unqualified name in this schema.
     */
    @NotNull
    List<Index> getIndexes(Name name);

    /**
     * Stream all UDTs contained in this schema.
     */
    @NotNull
    Stream<UDT<?>> udtStream();

    /**
     * List all UDTs contained in this schema.
     */
    @NotNull
    List<UDT<?>> getUDTs();

    /**
     * Get a UDT by its name (case-sensitive) in this schema, or
     * <code>null</code> if no such UDT exists.
     */
    @Nullable
    UDT<?> getUDT(String name);

    /**
     * Get a UDT by its qualified or unqualified name in this schema, or
     * <code>null</code> if no such UDT exists.
     */
    @Nullable
    UDT<?> getUDT(Name name);

    /**
     * Stream all domains contained in this schema.
     */
    @NotNull
    Stream<Domain<?>> domainStream();

    /**
     * List all domains contained in this schema.
     */
    @NotNull
    List<Domain<?>> getDomains();

    /**
     * Get a domain by its name (case-sensitive) in this schema, or
     * <code>null</code> if no such domain exists.
     */
    @Nullable
    Domain<?> getDomain(String name);

    /**
     * Get a domain by its qualified or unqualified name in this schema, or
     * <code>null</code> if no such domain exists.
     */
    @Nullable
    Domain<?> getDomain(Name name);

    /**
     * Stream all sequences contained in this schema.
     */
    @NotNull
    Stream<Sequence<?>> sequenceStream();

    /**
     * List all sequences contained in this schema.
     */
    @NotNull
    List<Sequence<?>> getSequences();

    /**
     * Get a sequence by its name (case-sensitive) in this schema, or
     * <code>null</code> if no such sequence exists.
     */
    @Nullable
    Sequence<?> getSequence(String name);

    /**
     * Get a sequence by its qualified or unqualified name in this schema, or
     * <code>null</code> if no such sequence exists.
     */
    @Nullable
    Sequence<?> getSequence(Name name);
}
