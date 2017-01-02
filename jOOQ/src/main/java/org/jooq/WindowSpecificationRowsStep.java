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
 * An intermediate step in the construction of a {@link WindowSpecification}.
 * <p>
 * Example: <code><pre>
 * WindowSpecification spec =
 * DSL.partitionBy(BOOK.AUTHOR_ID)
 *    .orderBy(BOOK.ID)
 *    .rowsBetweenUnboundedPreceding()
 *    .andCurrentRow();
 * </pre></code>
 *
 * @author Lukas Eder
 */
public interface WindowSpecificationRowsStep extends WindowSpecificationFinalStep {

    /**
     * Add a <code>ROWS UNBOUNDED PRECEDING</code> frame clause to the window
     * specification.
     */
    @Support({ POSTGRES })
    WindowSpecificationFinalStep rowsUnboundedPreceding();

    /**
     * Add a <code>ROWS [number] PRECEDING</code> frame clause to the window
     * specification.
     */
    @Support({ POSTGRES })
    WindowSpecificationFinalStep rowsPreceding(int number);

    /**
     * Add a <code>ROWS CURRENT ROW</code> frame clause to the window
     * specification.
     */
    @Support({ POSTGRES })
    WindowSpecificationFinalStep rowsCurrentRow();

    /**
     * Add a <code>ROWS UNBOUNDED FOLLOWING</code> frame clause to the window
     * specification.
     */
    @Support({ POSTGRES })
    WindowSpecificationFinalStep rowsUnboundedFollowing();

    /**
     * Add a <code>ROWS [number] FOLLOWING</code> frame clause to the window
     * specification.
     */
    @Support({ POSTGRES })
    WindowSpecificationFinalStep rowsFollowing(int number);

    /**
     * Add a <code>ROWS BETWEEN UNBOUNDED PRECEDING ...</code> frame clause to
     * the window specification.
     */
    @Support({ POSTGRES })
    WindowSpecificationRowsAndStep rowsBetweenUnboundedPreceding();

    /**
     * Add a <code>ROWS BETWEEN [number] PRECEDING ...</code> frame clause to
     * the window specification.
     */
    @Support({ POSTGRES })
    WindowSpecificationRowsAndStep rowsBetweenPreceding(int number);

    /**
     * Add a <code>ROWS BETWEEN CURRENT ROW ...</code> frame clause to the
     * window specification.
     */
    @Support({ POSTGRES })
    WindowSpecificationRowsAndStep rowsBetweenCurrentRow();

    /**
     * Add a <code>ROWS BETWEEN UNBOUNDED FOLLOWING ...</code> frame clause to
     * the window specification.
     */
    @Support({ POSTGRES })
    WindowSpecificationRowsAndStep rowsBetweenUnboundedFollowing();

    /**
     * Add a <code>ROWS BETWEEN [number] FOLLOWING ...</code> frame clause to
     * the window specification.
     */
    @Support({ POSTGRES })
    WindowSpecificationRowsAndStep rowsBetweenFollowing(int number);

    /**
     * Add a <code>RANGE UNBOUNDED PRECEDING</code> frame clause to the window
     * specification.
     */
    @Support({ POSTGRES })
    WindowSpecificationFinalStep rangeUnboundedPreceding();

    /**
     * Add a <code>RANGE [number] PRECEDING</code> frame clause to the window
     * specification.
     */
    @Support({ POSTGRES })
    WindowSpecificationFinalStep rangePreceding(int number);

    /**
     * Add a <code>RANGE CURRENT ROW</code> frame clause to the window
     * specification.
     */
    @Support({ POSTGRES })
    WindowSpecificationFinalStep rangeCurrentRow();

    /**
     * Add a <code>RANGE UNBOUNDED FOLLOWING</code> frame clause to the window
     * specification.
     */
    @Support({ POSTGRES })
    WindowSpecificationFinalStep rangeUnboundedFollowing();

    /**
     * Add a <code>RANGE [number] FOLLOWING</code> frame clause to the window
     * specification.
     */
    @Support({ POSTGRES })
    WindowSpecificationFinalStep rangeFollowing(int number);

    /**
     * Add a <code>RANGE BETWEEN UNBOUNDED PRECEDING ...</code> frame clause to
     * the window specification.
     */
    @Support({ POSTGRES })
    WindowSpecificationRowsAndStep rangeBetweenUnboundedPreceding();

    /**
     * Add a <code>RANGE BETWEEN [number] PRECEDING ...</code> frame clause to
     * the window specification.
     */
    @Support({ POSTGRES })
    WindowSpecificationRowsAndStep rangeBetweenPreceding(int number);

    /**
     * Add a <code>RANGE BETWEEN CURRENT ROW ...</code> frame clause to the
     * window specification.
     */
    @Support({ POSTGRES })
    WindowSpecificationRowsAndStep rangeBetweenCurrentRow();

    /**
     * Add a <code>RANGE BETWEEN UNBOUNDED FOLLOWING ...</code> frame clause to
     * the window specification.
     */
    @Support({ POSTGRES })
    WindowSpecificationRowsAndStep rangeBetweenUnboundedFollowing();

    /**
     * Add a <code>RANGE BETWEEN [number] FOLLOWING ...</code> frame clause to
     * the window specification.
     */
    @Support({ POSTGRES })
    WindowSpecificationRowsAndStep rangeBetweenFollowing(int number);
}
