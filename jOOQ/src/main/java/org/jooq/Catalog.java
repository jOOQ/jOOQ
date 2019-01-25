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
 * A catalog.
 * <p>
 * Standard SQL object identifiers come in 3 parts:
 * <code>[catalog].[schema].[object]</code>. The catalog is an object that
 * groups a set of {@link Schema} instances, which again group a set of objects,
 * where objects can be {@link Table}, {@link Sequence}, {@link Routine} and
 * many other types of objects.
 * <p>
 * If your RDBMS supports catalogs, and jOOQ supports using catalogs with your
 * RDBMS, then generated catalog references can be used to fully qualify objects
 * <p>
 * <strong>Example:</strong>
 * <p>
 * <code><pre>
 * // Assuming import static org.jooq.impl.DSL.*;
 *
 * using(configuration)
 *    .select(CATALOG.SCHEMA.ACTOR.FIRST_NAME, CATALOG.SCHEMA.ACTOR.LAST_NAME)
 *    .from(CATALOG.SCHEMA.ACTOR)
 *    .fetch();
 * </pre></code>
 * <p>
 * <strong>Compatibility:</strong>
 * <p>
 * jOOQ supports catalogs in {@link SQLDialect#SQLSERVER} and related dialects,
 * such as {@link SQLDialect#SQLDATAWAREHOUSE}. Database products like
 * {@link SQLDialect#MYSQL} and related dialects, such as
 * {@link SQLDialect#MARIADB} use catalogs ("databases") instead of schemas, and
 * lack schema support. For historic reasons, jOOQ treats MySQL catalogs as
 * schemas and does not support any catalog qualifier in MySQL.
 * <p>
 * Instances can be created using {@link DSL#catalog(Name)} and overloads.
 *
 * @author Lukas Eder
 */
public interface Catalog extends Named {

    /**
     * List all schemas contained in this catalog.
     */
    List<Schema> getSchemas();

    /**
     * Get a schema by its name (case-sensitive) in this catalog, or
     * <code>null</code> if no such schema exists.
     */
    Schema getSchema(String name);


    /**
     * Stream all schemas contained in this catalog.
     */
    Stream<Schema> schemaStream();

}
