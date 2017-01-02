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

import static org.jooq.SQLDialect.POSTGRES_9_4;

/**
 * An ordered-set aggregate function.
 * <p>
 * An ordered-set aggregate function is an aggregate function with a mandatory
 * Oracle-specific <code>WITHIN GROUP (ORDER BY ..)</code> clause. An example is
 * <code>LISTAGG</code>: <code><pre>
 * SELECT   LISTAGG(TITLE, ', ')
 *          WITHIN GROUP (ORDER BY TITLE)
 * FROM     T_BOOK
 * GROUP BY AUTHOR_ID
 * </pre></code> The above function groups books by author and aggregates titles
 * into a concatenated string.
 * <p>
 * Ordered-set aggregate functions can be further converted into window functions
 * using the <code>OVER(PARTITION BY ..)</code> clause. For example: <code><pre>
 * SELECT LISTAGG(TITLE, ', ')
 *        WITHIN GROUP (ORDER BY TITLE)
 *        OVER (PARTITION BY AUTHOR_ID)
 * FROM   T_BOOK
 * </pre></code>
 *
 * @author Lukas Eder
 */
public interface OrderedAggregateFunctionOfDeferredType {

    /**
     * Add an <code>WITHIN GROUP (ORDER BY ..)</code> clause to the ordered
     * aggregate function
     */
    @Support({ POSTGRES_9_4 })
    <T> AggregateFilterStep<T> withinGroupOrderBy(Field<T> field);

    /**
     * Add an <code>WITHIN GROUP (ORDER BY ..)</code> clause to the ordered
     * aggregate function
     */
    @Support({ POSTGRES_9_4 })
    <T> AggregateFilterStep<T> withinGroupOrderBy(SortField<T> field);
}
