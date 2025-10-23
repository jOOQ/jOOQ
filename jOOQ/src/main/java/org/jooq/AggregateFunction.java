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

// ...

import java.util.Collection;

import org.jooq.impl.DSL;
import org.jooq.impl.QOM;

import org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * An aggregate function.
 * <p>
 * An aggregate function is a special field that is usually used in a
 * <code>GROUP BY</code> context. It is also the base for window function
 * construction.
 * <p>
 * <strong>Example:</strong>
 * <p>
 * <pre><code>
 * // Assuming import static org.jooq.impl.DSL.*;
 *
 * using(configuration)
 *    .select(ACTOR.LAST_NAME, count())
 *    .from(ACTOR)
 *    .groupBy(ACTOR.LAST_NAME)
 *    .orderBy(count().desc())
 *    .fetch();
 * </code></pre>
 * <p>
 * Instances can be created using various {@link DSL} methods and their
 * overloads, such as {@link DSL#count()} or {@link DSL#countDistinct(Field)}.
 *
 * @author Lukas Eder
 */
public interface AggregateFunction<T> extends AggregateFilterStep<T> {




















































    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    /**
     * Experimental query object model accessor method, see also {@link QOM}.
     * Subject to change in future jOOQ versions, use at your own risk.
     */
    @Experimental
    @Nullable Condition $filterWhere();
}
