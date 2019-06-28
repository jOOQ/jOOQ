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

// ...
// ...
import static org.jooq.SQLDialect.H2;
// ...
import static org.jooq.SQLDialect.MARIADB;
// ...
import static org.jooq.SQLDialect.MYSQL;
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...
// ...
import static org.jooq.SQLDialect.SQLITE;
// ...
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
 * <p>
 * <h3>Referencing <code>XYZ*Step</code> types directly from client code</h3>
 * <p>
 * It is usually not recommended to reference any <code>XYZ*Step</code> types
 * directly from client code, or assign them to local variables. When writing
 * dynamic SQL, creating a statement's components dynamically, and passing them
 * to the DSL API statically is usually a better choice. See the manual's
 * section about dynamic SQL for details: <a href=
 * "https://www.jooq.org/doc/latest/manual/sql-building/dynamic-sql">https://www.jooq.org/doc/latest/manual/sql-building/dynamic-sql</a>.
 * <p>
 * Drawbacks of referencing the <code>XYZ*Step</code> types directly:
 * <ul>
 * <li>They're operating on mutable implementations (as of jOOQ 3.x)</li>
 * <li>They're less composable and not easy to get right when dynamic SQL gets
 * complex</li>
 * <li>They're less readable</li>
 * <li>They might have binary incompatible changes between minor releases</li>
 * </ul>
 *
 * @author Lukas Eder
 */
public interface WindowSpecificationRowsStep extends WindowSpecificationFinalStep {

    /**
     * Add a <code>ROWS UNBOUNDED PRECEDING</code> frame clause to the window
     * specification.
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES, SQLITE })
    WindowSpecificationExcludeStep rowsUnboundedPreceding();

    /**
     * Add a <code>ROWS [number] PRECEDING</code> frame clause to the window
     * specification.
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES, SQLITE })
    WindowSpecificationExcludeStep rowsPreceding(int number);

    /**
     * Add a <code>ROWS CURRENT ROW</code> frame clause to the window
     * specification.
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES, SQLITE })
    WindowSpecificationExcludeStep rowsCurrentRow();

    /**
     * Add a <code>ROWS UNBOUNDED FOLLOWING</code> frame clause to the window
     * specification.
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES, SQLITE })
    WindowSpecificationExcludeStep rowsUnboundedFollowing();

    /**
     * Add a <code>ROWS [number] FOLLOWING</code> frame clause to the window
     * specification.
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES, SQLITE })
    WindowSpecificationExcludeStep rowsFollowing(int number);

    /**
     * Add a <code>ROWS BETWEEN UNBOUNDED PRECEDING ...</code> frame clause to
     * the window specification.
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES, SQLITE })
    WindowSpecificationRowsAndStep rowsBetweenUnboundedPreceding();

    /**
     * Add a <code>ROWS BETWEEN [number] PRECEDING ...</code> frame clause to
     * the window specification.
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES, SQLITE })
    WindowSpecificationRowsAndStep rowsBetweenPreceding(int number);

    /**
     * Add a <code>ROWS BETWEEN CURRENT ROW ...</code> frame clause to the
     * window specification.
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES, SQLITE })
    WindowSpecificationRowsAndStep rowsBetweenCurrentRow();

    /**
     * Add a <code>ROWS BETWEEN UNBOUNDED FOLLOWING ...</code> frame clause to
     * the window specification.
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES, SQLITE })
    WindowSpecificationRowsAndStep rowsBetweenUnboundedFollowing();

    /**
     * Add a <code>ROWS BETWEEN [number] FOLLOWING ...</code> frame clause to
     * the window specification.
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES, SQLITE })
    WindowSpecificationRowsAndStep rowsBetweenFollowing(int number);

    /**
     * Add a <code>RANGE UNBOUNDED PRECEDING</code> frame clause to the window
     * specification.
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES, SQLITE })
    WindowSpecificationExcludeStep rangeUnboundedPreceding();

    /**
     * Add a <code>RANGE [number] PRECEDING</code> frame clause to the window
     * specification.
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES, SQLITE })
    WindowSpecificationExcludeStep rangePreceding(int number);

    /**
     * Add a <code>RANGE CURRENT ROW</code> frame clause to the window
     * specification.
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES, SQLITE })
    WindowSpecificationExcludeStep rangeCurrentRow();

    /**
     * Add a <code>RANGE UNBOUNDED FOLLOWING</code> frame clause to the window
     * specification.
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES, SQLITE })
    WindowSpecificationExcludeStep rangeUnboundedFollowing();

    /**
     * Add a <code>RANGE [number] FOLLOWING</code> frame clause to the window
     * specification.
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES, SQLITE })
    WindowSpecificationExcludeStep rangeFollowing(int number);

    /**
     * Add a <code>RANGE BETWEEN UNBOUNDED PRECEDING ...</code> frame clause to
     * the window specification.
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES, SQLITE })
    WindowSpecificationRowsAndStep rangeBetweenUnboundedPreceding();

    /**
     * Add a <code>RANGE BETWEEN [number] PRECEDING ...</code> frame clause to
     * the window specification.
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES, SQLITE })
    WindowSpecificationRowsAndStep rangeBetweenPreceding(int number);

    /**
     * Add a <code>RANGE BETWEEN CURRENT ROW ...</code> frame clause to the
     * window specification.
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES, SQLITE })
    WindowSpecificationRowsAndStep rangeBetweenCurrentRow();

    /**
     * Add a <code>RANGE BETWEEN UNBOUNDED FOLLOWING ...</code> frame clause to
     * the window specification.
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES, SQLITE })
    WindowSpecificationRowsAndStep rangeBetweenUnboundedFollowing();

    /**
     * Add a <code>RANGE BETWEEN [number] FOLLOWING ...</code> frame clause to
     * the window specification.
     */
    @Support({ H2, MARIADB, MYSQL, POSTGRES, SQLITE })
    WindowSpecificationRowsAndStep rangeBetweenFollowing(int number);

    /**
     * Add a <code>GROUPS UNBOUNDED PRECEDING</code> frame clause to the window
     * specification.
     */
    @Support({ H2, POSTGRES, SQLITE })
    WindowSpecificationExcludeStep groupsUnboundedPreceding();

    /**
     * Add a <code>GROUPS [number] PRECEDING</code> frame clause to the window
     * specification.
     */
    @Support({ H2, POSTGRES, SQLITE })
    WindowSpecificationExcludeStep groupsPreceding(int number);

    /**
     * Add a <code>GROUPS CURRENT ROW</code> frame clause to the window
     * specification.
     */
    @Support({ H2, POSTGRES, SQLITE })
    WindowSpecificationExcludeStep groupsCurrentRow();

    /**
     * Add a <code>GROUPS UNBOUNDED FOLLOWING</code> frame clause to the window
     * specification.
     */
    @Support({ H2, POSTGRES, SQLITE })
    WindowSpecificationExcludeStep groupsUnboundedFollowing();

    /**
     * Add a <code>GROUPS [number] FOLLOWING</code> frame clause to the window
     * specification.
     */
    @Support({ H2, POSTGRES, SQLITE })
    WindowSpecificationExcludeStep groupsFollowing(int number);

    /**
     * Add a <code>GROUPS BETWEEN UNBOUNDED PRECEDING ...</code> frame clause to
     * the window specification.
     */
    @Support({ H2, POSTGRES, SQLITE })
    WindowSpecificationRowsAndStep groupsBetweenUnboundedPreceding();

    /**
     * Add a <code>GROUPS BETWEEN [number] PRECEDING ...</code> frame clause to
     * the window specification.
     */
    @Support({ H2, POSTGRES, SQLITE })
    WindowSpecificationRowsAndStep groupsBetweenPreceding(int number);

    /**
     * Add a <code>GROUPS BETWEEN CURRENT ROW ...</code> frame clause to the
     * window specification.
     */
    @Support({ H2, POSTGRES, SQLITE })
    WindowSpecificationRowsAndStep groupsBetweenCurrentRow();

    /**
     * Add a <code>GROUPS BETWEEN UNBOUNDED FOLLOWING ...</code> frame clause to
     * the window specification.
     */
    @Support({ H2, POSTGRES, SQLITE })
    WindowSpecificationRowsAndStep groupsBetweenUnboundedFollowing();

    /**
     * Add a <code>GROUPS BETWEEN [number] FOLLOWING ...</code> frame clause to
     * the window specification.
     */
    @Support({ H2, POSTGRES, SQLITE })
    WindowSpecificationRowsAndStep groupsBetweenFollowing(int number);
}
