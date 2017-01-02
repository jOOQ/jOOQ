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

import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.FIREBIRD_3_0;
// ...
// ...
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...
// ...
// ...

import java.util.Collection;

/**
 * This type is used for the window function DSL API.
 * <p>
 * Example: <code><pre>
 * field.firstValue()
 *      .ignoreNulls()
 *      .over()
 *      .partitionBy(AUTHOR_ID)
 *      .orderBy(PUBLISHED_IN.asc())
 *      .rowsBetweenUnboundedPreceding()
 *      .andUnboundedFollowing()
 * </pre></code>
 *
 * @param <T> The function return type
 * @author Lukas Eder
 */
public interface WindowOrderByStep<T> extends WindowFinalStep<T> {

    /**
     * Add an <code>ORDER BY</code> clause to the window function.
     */
    @Support({ CUBRID, FIREBIRD_3_0, POSTGRES })
    WindowRowsStep<T> orderBy(Field<?>... fields);

    /**
     * Add an <code>ORDER BY</code> clause to the window function.
     */
    @Support({ CUBRID, FIREBIRD_3_0, POSTGRES })
    WindowRowsStep<T> orderBy(SortField<?>... fields);

    /**
     * Add an <code>ORDER BY</code> clause to the window function.
     */
    @Support({ CUBRID, FIREBIRD_3_0, POSTGRES })
    WindowRowsStep<T> orderBy(Collection<? extends SortField<?>> fields);
}
