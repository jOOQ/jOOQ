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

import java.util.Collection;

/**
 * A version ID attached to a {@link Meta} description of a database.
 *
 * @author Lukas Eder
 */
public interface Version {

    /**
     * The version ID, which is unique in the version graph.
     */
    String id();

    /**
     * The version's {@link Meta} representation of the database.
     */
    Meta meta();

    /**
     * Produce a migration from a previous version.
     */
    Queries migrateFrom(Version version);






























    /**
     * Apply a change set to produce a new version.
     */
    Version apply(String id, Queries diff);

    /**
     * Apply a change set to produce a new version.
     *
     * @see #apply(String, Queries)
     */
    Version apply(String id, Query... diff);

    /**
     * Apply a change set to produce a new version.
     *
     * @see #apply(String, Queries)
     */
    Version apply(String id, Collection<? extends Query> diff);

    /**
     * Apply a change set to produce a new version.
     *
     * @see #apply(String, Queries)
     */
    Version apply(String id, String diff);
}
