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
public interface WindowSpecificationPartitionByStep extends WindowSpecificationOrderByStep {

    /**
     * Add a <code>PARTITION BY</code> clause to the window specification.
     */
    @Support({ CUBRID, FIREBIRD_3_0, POSTGRES })
    WindowSpecificationOrderByStep partitionBy(Field<?>... fields);

    /**
     * Add a <code>PARTITION BY</code> clause to the window specification.
     */
    @Support({ CUBRID, FIREBIRD_3_0, POSTGRES })
    WindowSpecificationOrderByStep partitionBy(Collection<? extends Field<?>> fields);

    /**
     * Add a <code>PARTITION BY 1</code> clause to the window specification,
     * where such a clause is required by the syntax of an RDBMS.
     * <p>
     * This clause is not supported as such in the CUBRID and Sybase dialects.
     * If you use it, jOOQ will simply ignore it.
     */
    @Support({ CUBRID, FIREBIRD_3_0, POSTGRES })
    WindowSpecificationOrderByStep partitionByOne();
}
