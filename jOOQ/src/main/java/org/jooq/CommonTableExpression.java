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

/**
 * A common table expression.
 * <p>
 * A common table expression is a table that can be supplied to
 * <code>WITH</code> clauses. It may or may not be defined recursively.
 * <p>
 * <strong>Example:</strong>
 * <p>
 * <code><pre>
 * // Assuming import static org.jooq.impl.DSL.*;
 *
 * Table&lt;?&gt; t = name("t").fields("v").as(select(one()));
 *
 * using(configuration)
 *    .select()
 *    .from(t)
 *    .fetch();
 * </pre></code>
 * <p>
 * Instances can be created using {@link Name#as(Select)}.
 *
 * @author Lukas Eder
 */
public interface CommonTableExpression<R extends Record> extends Table<R> {

}
