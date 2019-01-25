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
    Catalog getCatalog();

    /**
     * Stream all tables contained in this schema.
     */

    Stream<Table<?>> tableStream();


    /**
     * List all tables contained in this schema.
     */
    List<Table<?>> getTables();

    /**
     * Get a table by its name (case-sensitive) in this schema, or
     * <code>null</code> if no such table exists.
     */
    Table<?> getTable(String name);


    /**
     * Stream all UDTs contained in this schema.
     */
    Stream<UDT<?>> udtStream();


    /**
     * List all UDTs contained in this schema.
     */
    List<UDT<?>> getUDTs();

    /**
     * Get a UDT by its name (case-sensitive) in this schema, or
     * <code>null</code> if no such UDT exists.
     */
    UDT<?> getUDT(String name);

    /**
     * Stream all sequences contained in this schema.
     */

    Stream<Sequence<?>> sequenceStream();


    /**
     * List all sequences contained in this schema.
     */
    List<Sequence<?>> getSequences();

    /**
     * Get a sequence by its name (case-sensitive) in this schema, or
     * <code>null</code> if no such sequence exists.
     */
    Sequence<?> getSequence(String name);
}
