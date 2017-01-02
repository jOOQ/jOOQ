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

// ...
// ...
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...
// ...

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
public interface WindowRowsAndStep<T> {

    /**
     * Add a <code>... AND UNBOUNDED PRECEDING</code> frame clause to the window
     * function.
     */
    @Support({ POSTGRES })
    WindowFinalStep<T> andUnboundedPreceding();

    /**
     * Add a <code>... AND [number] PRECEDING</code> frame clause to the window
     * function.
     */
    @Support({ POSTGRES })
    WindowFinalStep<T> andPreceding(int number);

    /**
     * Add a <code>... AND CURRENT ROW</code> frame clause to the window
     * function.
     */
    @Support({ POSTGRES })
    WindowFinalStep<T> andCurrentRow();

    /**
     * Add a <code>... AND UNBOUNDED FOLLOWING</code> frame clause to the window
     * function.
     */
    @Support({ POSTGRES })
    WindowFinalStep<T> andUnboundedFollowing();

    /**
     * Add a <code>... AND [number] FOLLOWING</code> frame clause to the window
     * function.
     */
    @Support({ POSTGRES })
    WindowFinalStep<T> andFollowing(int number);
}
