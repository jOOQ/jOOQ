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
 */
package org.jooq;

import java.util.List;
import java.util.stream.Stream;

/**
 * An object representing a database catalog.
 * <p>
 * NOTE: Catalogs are experimental in jOOQ 3.0
 *
 * @author Lukas Eder
 */
public interface Catalog extends QueryPart {

    /**
     * The name of this schema.
     */
    String getName();

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
