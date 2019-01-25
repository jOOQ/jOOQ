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
 * A sort specification.
 * <p>
 * The SQL <code>ORDER BY</code> clause accepts expressions based on
 * {@link Field}, which may be enhanced by <code>ASC</code> or <code>DESC</code>
 * as well as <code>NULLS FIRST</code> or <code>NULLS LAST</code>.
 * <p>
 * <strong>Example:</strong>
 * <p>
 * <code><pre>
 * // Assuming import static org.jooq.impl.DSL.*;
 *
 * using(configuration)
 *    .select(ACTOR.FIRST_NAME, ACTOR.LAST_NAME)
 *    .from(ACTOR)
 *    .orderBy(ACTOR.LAST_UPDATE.desc().nullsLast())
 *    .fetch();
 * </pre></code>
 * <p>
 * Instances can be created using {@link Field#asc()}, {@link Field#desc()} and
 * related methods.
 *
 * @param <T> The field type
 * @author Lukas Eder
 * @see Field#asc()
 * @see Field#desc()
 */
public interface SortField<T> extends OrderField<T> {

    /**
     * The name of this sort field
     */
    String getName();

    /**
     * Get the underlying sort order of this sort field
     */
    SortOrder getOrder();

    /**
     * Add a <code>NULLS FIRST</code> clause to this sort field
     */
    @Support
    SortField<T> nullsFirst();

    /**
     * Add a <code>NULLS LAST</code> clause to this sort field
     */
    @Support
    SortField<T> nullsLast();

}
