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
package org.jooq.util;

/**
 * An SPI that can be used to provide a schema version to the jOOQ code
 * generator.
 * <p>
 * If between subsequent meta data accesses, at least one
 * {@link SchemaDefinition}'s version changes, that schema's
 * {@link CatalogDefinition}'s version must change as well. In other words, it
 * can be safely assumed that when between two subsequent schema meta data
 * accesses, the {@link CatalogDefinition}'s version stays the same, all
 * {@link SchemaDefinition}'s versions have stayed the same as well.
 * <p>
 * A {@link SchemaDefinition} is said to be unversioned if
 * {@link #version(SchemaDefinition)} returns <code>null</code>.
 *
 * @author Lukas Eder
 * @see CatalogVersionProvider
 */
public interface SchemaVersionProvider {

    /**
     * Get a custom schema version.
     */
    String version(SchemaDefinition schema);
}
