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
 * A window definition.
 * <p>
 * Window definitions can be
 * <ul>
 * <li>declared in the <code>WINDOW</code> clause (see
 * {@link SelectWindowStep#window(WindowDefinition...)}</li>
 * <li>referenced from the <code>OVER</code> clause (see
 * {@link AggregateFunction#over(WindowDefinition)}</li>
 * </ul>
 * <p>
 * The <code>WINDOW</code> clause is only natively supported by
 * <ul>
 * <li> {@link SQLDialect#POSTGRES}</li>
 * <li> {@link SQLDialect#SYBASE}</li>
 * </ul>
 * <p>
 * If your database supports window functions, but not the <code>WINDOW</code>
 * clause, jOOQ will inline window definitions into their respective window
 * functions.
 *
 * @author Lukas Eder
 */
public interface WindowDefinition extends QueryPart {

}
