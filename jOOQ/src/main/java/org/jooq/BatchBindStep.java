/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
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
 * For more information, please visit: https://www.jooq.org/legal/licensing
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

import org.jooq.impl.DSL;

import org.jetbrains.annotations.*;


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
     * <li><code>:1</code> -&gt; <code>bindValues[0]</code></li>
     * <li><code>:2</code> -&gt; <code>bindValues[1]</code></li>
     * <li>...</li>
     * <li><code>:N</code> -&gt; <code>bindValues[N - 1]</code></li>
     * </ul>
     * <p>
     * "Unmatched" bind values will be left unmodified:
     * <ul>
     * <li><code>:N+1</code> -&gt; unmodified</li>
     * <li><code>:N+2</code> -&gt; unmodified</li>
     * </ul>
     * <h3>Bind index order</h3> The 1-based parameter index describes a
     * parameter in <em>rendering order</em>, not in input order. For example,
     * if a query contains a {@link DSL#log(Field, Field)} call, where the first
     * argument is the <code>value</code> and the second argument is the
     * <code>base</code>, this may produce different dialect specific
     * renderings:
     * <ul>
     * <li>Db2: <code>ln(value) / ln(base)</code></li>
     * <li>Oracle: <code>log(base, value)</code></li>
     * <li>SQL Server: <code>log(value, base)</code></li>
     * </ul>
     * <p>
     * Some bind values may even be repeated by a dialect specific emulation,
     * leading to duplication and index-shifting.
     * <p>
     * As such, it is usually better to supply bind values directly with the
     * input of an expression, e.g.:
     * <ul>
     * <li>Directly with the {@link DSL} method, such as
     * {@link DSL#log(Field, Field)}, for example.</li>
     * <li>With the plain SQL template constructor, e.g.
     * {@link DSL#field(String, Object...)}</li>
     * <li>With the parser method, e.g.
     * {@link Parser#parseField(String, Object...)}</li>
     * </ul>
     */
    @NotNull @CheckReturnValue
    @Support
    BatchBindStep bind(Object... bindValues);

    /**
     * Set several indexed bind values onto the batch statement.
     * <p>
     * This is the same as calling {@link #bind(Object...)} several times.
     */
    @NotNull @CheckReturnValue
    @Support
    BatchBindStep bind(Object[]... bindValues);

    /**
     * Set named bind values onto the batch statement.
     * <p>
     * The argument map of <code>namedBindValues</code> will be set onto the
     * named bind values of the batch statement:
     * <ul>
     * <li><code>:name1</code> -&gt; <code>bindValues.get("name1")</code></li>
     * <li><code>:name2</code> -&gt; <code>bindValues.get("name2")</code></li>
     * <li>...</li>
     * <li><code>:nameN</code> -&gt; <code>bindValues.get("nameN")</code></li>
     * </ul>
     * <p>
     * "Unmatched" bind values will be left unmodified:
     * <ul>
     * <li><code>:nameN+1</code> -&gt; unmodified</li>
     * <li><code>:nameN+2</code> -&gt; unmodified</li>
     * </ul>
     */
    @NotNull @CheckReturnValue
    @Support
    BatchBindStep bind(Map<String, Object> namedBindValues);

    /**
     * Set several named bind values onto the batch statement.
     * <p>
     * This is the same as calling {@link #bind(Map...)} several times.
     */
    @NotNull @CheckReturnValue
    @Support
    BatchBindStep bind(Map<String, Object>... namedBindValues);
}
