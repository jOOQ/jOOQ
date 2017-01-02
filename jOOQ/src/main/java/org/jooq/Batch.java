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

import java.io.Serializable;
import java.sql.Statement;

import org.jooq.exception.DataAccessException;

/**
 * A wrapper for a JDBC batch operation. It has two modes:
 * <p>
 * <ol>
 * <li><b>Execute several queries without bind values</b><code><pre>
 * create.batch(query1,
 *              query2,
 *              query3)
 *       .execute();
 * </pre></code></li>
 * <li><b>Execute one query several times with bind values</b><code><pre>
 * create.batch(query)
 *       .bind(valueA1, valueA2)
 *       .bind(valueB1, valueB2)
 *       .execute();
 * </pre></code></li>
 * </ol>
 *
 * @author Lukas Eder
 * @see Statement#executeBatch()
 */
public interface Batch extends Serializable {

    /**
     * Execute the batch operation.
     *
     * @see Statement#executeBatch()
     * @throws DataAccessException if something went wrong executing the query
     */
    int[] execute() throws DataAccessException;

    /**
     * Get the number of executed queries in this batch operation
     */
    int size();
}
