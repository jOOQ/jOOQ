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
public interface WindowRowsStep<T> extends WindowFinalStep<T> {

    /**
     * Add a <code>ROWS UNBOUNDED PRECEDING</code> frame clause to the window
     * function.
     */
    @Support({ POSTGRES })
    WindowFinalStep<T> rowsUnboundedPreceding();

    /**
     * Add a <code>ROWS [number] PRECEDING</code> frame clause to the window
     * function.
     */
    @Support({ POSTGRES })
    WindowFinalStep<T> rowsPreceding(int number);

    /**
     * Add a <code>ROWS CURRENT ROW</code> frame clause to the window function.
     */
    @Support({ POSTGRES })
    WindowFinalStep<T> rowsCurrentRow();

    /**
     * Add a <code>ROWS UNBOUNDED FOLLOWING</code> frame clause to the window
     * function.
     */
    @Support({ POSTGRES })
    WindowFinalStep<T> rowsUnboundedFollowing();

    /**
     * Add a <code>ROWS [number] FOLLOWING</code> frame clause to the window
     * function.
     */
    @Support({ POSTGRES })
    WindowFinalStep<T> rowsFollowing(int number);

    /**
     * Add a <code>ROWS BETWEEN UNBOUNDED PRECEDING ...</code> frame clause to
     * the window function.
     */
    @Support({ POSTGRES })
    WindowRowsAndStep<T> rowsBetweenUnboundedPreceding();

    /**
     * Add a <code>ROWS BETWEEN [number] PRECEDING ...</code> frame clause to
     * the window function.
     */
    @Support({ POSTGRES })
    WindowRowsAndStep<T> rowsBetweenPreceding(int number);

    /**
     * Add a <code>ROWS BETWEEN CURRENT ROW ...</code> frame clause to
     * the window function.
     */
    @Support({ POSTGRES })
    WindowRowsAndStep<T> rowsBetweenCurrentRow();

    /**
     * Add a <code>ROWS BETWEEN UNBOUNDED FOLLOWING ...</code> frame clause to
     * the window function.
     */
    @Support({ POSTGRES })
    WindowRowsAndStep<T> rowsBetweenUnboundedFollowing();

    /**
     * Add a <code>ROWS BETWEEN [number] FOLLOWING ...</code> frame clause to
     * the window function.
     */
    @Support({ POSTGRES })
    WindowRowsAndStep<T> rowsBetweenFollowing(int number);

    /**
     * Add a <code>RANGE UNBOUNDED PRECEDING</code> frame clause to the window
     * function.
     */
    @Support({ POSTGRES })
    WindowFinalStep<T> rangeUnboundedPreceding();

    /**
     * Add a <code>RANGE [number] PRECEDING</code> frame clause to the window
     * function.
     */
    @Support({ POSTGRES })
    WindowFinalStep<T> rangePreceding(int number);

    /**
     * Add a <code>RANGE CURRENT ROW</code> frame clause to the window function.
     */
    @Support({ POSTGRES })
    WindowFinalStep<T> rangeCurrentRow();

    /**
     * Add a <code>RANGE UNBOUNDED FOLLOWING</code> frame clause to the window
     * function.
     */
    @Support({ POSTGRES })
    WindowFinalStep<T> rangeUnboundedFollowing();

    /**
     * Add a <code>RANGE [number] FOLLOWING</code> frame clause to the window
     * function.
     */
    @Support({ POSTGRES })
    WindowFinalStep<T> rangeFollowing(int number);

    /**
     * Add a <code>RANGE BETWEEN UNBOUNDED PRECEDING ...</code> frame clause to
     * the window function.
     */
    @Support({ POSTGRES })
    WindowRowsAndStep<T> rangeBetweenUnboundedPreceding();

    /**
     * Add a <code>RANGE BETWEEN [number] PRECEDING ...</code> frame clause to
     * the window function.
     */
    @Support({ POSTGRES })
    WindowRowsAndStep<T> rangeBetweenPreceding(int number);

    /**
     * Add a <code>RANGE BETWEEN CURRENT ROW ...</code> frame clause to
     * the window function.
     */
    @Support({ POSTGRES })
    WindowRowsAndStep<T> rangeBetweenCurrentRow();

    /**
     * Add a <code>RANGE BETWEEN UNBOUNDED FOLLOWING ...</code> frame clause to
     * the window function.
     */
    @Support({ POSTGRES })
    WindowRowsAndStep<T> rangeBetweenUnboundedFollowing();

    /**
     * Add a <code>RANGE BETWEEN [number] FOLLOWING ...</code> frame clause to
     * the window function.
     */
    @Support({ POSTGRES })
    WindowRowsAndStep<T> rangeBetweenFollowing(int number);
}
