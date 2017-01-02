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

import java.sql.Statement;
import java.util.Map;

/**
 * This type is used for the {@link Batch}'s DSL API.
 * <p>
 * Use it to add bind values to a single operation in the batch statement.
 *
 * @author Lukas Eder
 * @see Batch
 * @see Statement#executeBatch()
 */
public interface BatchBindStep extends Batch {

    /**
     * Set indexed bind values onto the batch statement.
     * <p>
     * The argument array of <code>bindValues</code> will be set onto the
     * indexed bind values of the batch statement:
     * <ul>
     * <li><code>:1</code> -> <code>bindValues[0]</code></li>
     * <li><code>:2</code> -> <code>bindValues[1]</code></li>
     * <li>...</li>
     * <li><code>:N</code> -> <code>bindValues[N - 1]</code></li>
     * </ul>
     * <p>
     * "Unmatched" bind values will be left unmodified:
     * <ul>
     * <li><code>:N+1</code> -> unmodified</li>
     * <li><code>:N+2</code> -> unmodified</li>
     * </ul>
     */
    BatchBindStep bind(Object... bindValues);

    /**
     * Set several indexed bind values onto the batch statement.
     * <p>
     * This is the same as calling {@link #bind(Object...)} several times.
     */
    BatchBindStep bind(Object[]... bindValues);

    /**
     * Set named bind values onto the batch statement.
     * <p>
     * The argument map of <code>namedBindValues</code> will be set onto the
     * named bind values of the batch statement:
     * <ul>
     * <li><code>:name1</code> -> <code>bindValues.get("name1")</code></li>
     * <li><code>:name2</code> -> <code>bindValues.get("name2")</code></li>
     * <li>...</li>
     * <li><code>:nameN</code> -> <code>bindValues.get("nameN")</code></li>
     * </ul>
     * <p>
     * "Unmatched" bind values will be left unmodified:
     * <ul>
     * <li><code>:nameN+1</code> -> unmodified</li>
     * <li><code>:nameN+2</code> -> unmodified</li>
     * </ul>
     */
    BatchBindStep bind(Map<String, Object> namedBindValues);

    /**
     * Set several named bind values onto the batch statement.
     * <p>
     * This is the same as calling {@link #bind(Map...)} several times.
     */
    BatchBindStep bind(Map<String, Object>... namedBindValues);
}
