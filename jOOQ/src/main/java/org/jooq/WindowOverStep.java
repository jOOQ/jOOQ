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
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD_3_0;
import static org.jooq.SQLDialect.H2;
// ...
import static org.jooq.SQLDialect.HSQLDB;
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
 * <p>
 * Unlike in {@link WindowBeforeOverStep}, <code>OVER()</code> is a mandatory
 * clause.
 *
 * @param <T> The function return type
 * @author Lukas Eder
 */
public interface WindowOverStep<T> {

    /**
     * Turn this aggregate function into a window function.
     * <p>
     * An example: <code><pre>
     * MAX(id) OVER (PARTITION BY 1)
     * </code>
     * </pre>
     */
    @Support({ CUBRID, DERBY, FIREBIRD_3_0, H2, HSQLDB, POSTGRES })
    WindowPartitionByStep<T> over();

    /**
     * Turn this aggregate function into a window function referencing a window
     * name.
     * <p>
     * An example: <code><pre>
     * MAX(id) OVER my_window
     * </code>
     * </pre>
     * <p>
     * If the <code>WINDOW</code> clause is not supported (see
     * {@link SelectWindowStep#window(WindowDefinition...)}, then referenced
     * windows will be inlined.
     */
    @Support({ CUBRID, FIREBIRD_3_0, POSTGRES })
    WindowFinalStep<T> over(Name name);

    /**
     * Turn this aggregate function into a window function referencing a window
     * name.
     * <p>
     * An example: <code><pre>
     * MAX(id) OVER my_window
     * </code>
     * </pre>
     * <p>
     * If the <code>WINDOW</code> clause is not supported (see
     * {@link SelectWindowStep#window(WindowDefinition...)}, then referenced
     * windows will be inlined.
     */
    @Support({ CUBRID, FIREBIRD_3_0, POSTGRES })
    WindowFinalStep<T> over(String name);

    /**
     * Turn this aggregate function into a window function.
     * <p>
     * An example: <code><pre>
     * MAX(id) OVER (PARTITION BY 1)
     * </code>
     * </pre>
     */
    @Support({ CUBRID, FIREBIRD_3_0, POSTGRES })
    WindowFinalStep<T> over(WindowSpecification specification);

    /**
     * Turn this aggregate function into a window function referencing a window
     * definition.
     * <p>
     * An example: <code><pre>
     * MAX(id) OVER my_window
     * </code>
     * </pre>
     * <p>
     * If the <code>WINDOW</code> clause is not supported (see
     * {@link SelectWindowStep#window(WindowDefinition...)}, then referenced
     * windows will be inlined.
     */
    @Support({ CUBRID, FIREBIRD_3_0, POSTGRES })
    WindowFinalStep<T> over(WindowDefinition definition);

}
