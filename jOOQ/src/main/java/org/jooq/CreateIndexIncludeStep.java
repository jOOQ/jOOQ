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
 * A {@link Query} that can create indexes.
 *
 * @author Lukas Eder
 */
public interface CreateIndexIncludeStep extends CreateIndexWhereStep {

    /**
     * Add an <code>INCLUDE</code> clause to include columns in an index.
     * <p>
     * If this is not supported by any given database, then the included columns
     * will simply be put in the index as ordinary columns, making the index a
     * composite index.
     */
    @Support
    CreateIndexWhereStep include(Field<?>... fields);

    /**
     * Add an <code>INCLUDE</code> clause to include columns in an index.
     * <p>
     * If this is not supported by any given database, then the included columns
     * will simply be put in the index as ordinary columns, making the index a
     * composite index.
     */
    @Support
    CreateIndexWhereStep include(Name... fields);

    /**
     * Add an <code>INCLUDE</code> clause to include columns in an index.
     * <p>
     * If this is not supported by any given database, then the included columns
     * will simply be put in the index as ordinary columns, making the index a
     * composite index.
     */
    @Support
    CreateIndexWhereStep include(String... fields);

    /**
     * Add an <code>INCLUDE</code> clause to include columns in an index.
     * <p>
     * If this is not supported by any given database, then the included columns
     * will simply be put in the index as ordinary columns, making the index a
     * composite index.
     */
    @Support
    CreateIndexWhereStep include(Collection<? extends Field<?>> fields);

}
