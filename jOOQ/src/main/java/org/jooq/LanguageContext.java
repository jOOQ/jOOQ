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
 * The current language context of some {@link ParseContext} or
 * {@link RenderContext}.
 * <p>
 * This can be useful to help distinguish between ways to generate SQL, e.g.:
 * <ul>
 * <li>whether <code>CAST</code> expressions are allowed to generate length,
 * precision, and scale</li>
 * <li>whether <code>DELETING</code> and similar "magic" identifiers are from a
 * <code>TRIGGER</code>, or not</li>
 * </ul>
 *
 * @author Lukas Eder
 */
public enum LanguageContext {

    /**
     * A SQL query, including DDL to create procedural elements.
     */
    QUERY,

    /**
     * The body of a stored procedure.
     */
    PROCEDURE,

    /**
     * The body of a stored function.
     */
    FUNCTION,

    /**
     * The body of a trigger.
     */
    TRIGGER,

    /**
     * The body of an anonymous block.
     */
    BLOCK
}
