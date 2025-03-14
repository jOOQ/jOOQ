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

import org.jetbrains.annotations.ApiStatus.Experimental;

/**
 * The {@link File#type()}.
 * <p>
 * This type describes the semantics of {@link File#content()}. The files
 * contained in {@link Commit#delta()} will be processed in the following order:
 * <p>
 * <ul>
 * <li>{@link #SNAPSHOT} (replacing all other contents of this {@link Commit} as
 * well as previous commits, if migrating from {@link Commit#root()})</li>
 * <li>{@link #INCREMENT}</li>
 * <li>{@link #SCRIPT}</li>
 * <li>{@link #SCHEMA}</li>
 * </ul>
 * <p>
 * When undoing a migration, the order is:
 * <ul>
 * <li>{@link #SCHEMA}</li>
 * <li>{@link #DECREMENT}</li>
 * </ul>
 */
@Experimental
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
     * Within the same {@link Commit}, increments are sorted according to their
     * {@link File#path()}.
     */
    INCREMENT,

    /**
     * The file contains decrement information.
     * <p>
     * Decrements work like {@link #INCREMENT} typed files, but are applied only
     * when downgrading to a previous version, decrements are sorted in reverse
     * order according to their {@link File#path()}.
     * <p>
     * This API is part of a commercial only feature. To use this feature,
     * please use the jOOQ Professional Edition or the jOOQ Enterprise Edition.
     */
    DECREMENT,

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
     * <p>
     * This API is part of a commercial only feature. To use this feature,
     * please use the jOOQ Professional Edition or the jOOQ Enterprise Edition.
     */
    SNAPSHOT,

}
