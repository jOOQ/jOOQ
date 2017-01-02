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


/**
 * A participant of a quantified comparison predicate
 * <p>
 * A <code>QuantifiedSelect</code> models the right hand side of a quantified
 * comparison predicate. Examples of such predicates:
 * <ul>
 * <li><code>ANY (SELECT 1 FROM DUAL)</code></li>
 * <li><code>ALL (SELECT 1 FROM DUAL)</code></li>
 * </ul>
 * These predicates can be used exclusively with methods, such as {@link Field}
 *
 * @author Lukas Eder
 */
public interface QuantifiedSelect<R extends Record> extends QueryPart {

}
