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
 * The {@link File#type()}.
 * <p>
 * This type describes the semantics of {@link File#content()}. The files
 * contained in {@link Commit#delta()} will be processed in the following order:
 * <p>
 * <ul>
 * <li>{@link ContentType#INCREMENT}</li>
 * <li>{@link ContentType#SCHEMA}</li>
 * </ul>
 */
public enum ContentType {

    /**
     * The file contains partial schema information.
     * <p>
     * Partial schema information could be <code>CREATE TABLE</code> and similar
     * statements, which are not applied as increments in a {@link Migration},
     * but used to create a diff between two {@link Version} using
     * {@link Meta#migrateTo(Meta)}.
     */
    SCHEMA,

    /**
     * The file contains increment information.
     * <p>
     * Increments could be <code>ALTER TABLE</code> or <code>UPDATE</code> and
     * similar statements, which are applied as increments in a migration using
     * {@link Meta#apply(Queries)}.
     * <p>
     * Within the same {@link Commit}.
     */
    INCREMENT,

    /**
     * The file contains a script.
     * <p>
     * Scripts are increments that are not processed by jOOQ, and can thus not
     * be interpreted. They can contain vendor-specific SQL that jOOQ doesn't
     * understand.
     */
    SCRIPT,

    /**
     * A snapshot (or tag?) is a file / set of files that describes the entire
     * schema.
     * <p>
     * In order to restore a database, or install a new one, we don't have to go
     * back any further than the snapshot.
     */
    SNAPSHOT,

}
