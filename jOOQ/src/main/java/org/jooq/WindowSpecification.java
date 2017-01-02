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
 * A window specification.
 * <p>
 * Window specifications are the syntactic clauses that can be passed to both
 * window definitions in <code>WINDOW</code> clauses, as well as to the
 * <code>OVER</code> clause of window functions. This makes window
 * specifications highly reusable across several queries.
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
public interface WindowSpecification extends QueryPart {

}
