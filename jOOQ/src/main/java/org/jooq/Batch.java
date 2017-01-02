/*
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
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
