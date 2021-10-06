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

import org.jooq.impl.DSL;

/**
 * A collation.
 * <p>
 * In SQL, a collation is a set of objects defining character encoding and/or
 * sort order of character data. jOOQ supports the collation type in various SQL
 * clauses of DDL and DML statements.
 * <p>
 * <strong>Example:</strong>
 * <p>
 * <code><pre>
 * // Assuming import static org.jooq.impl.DSL.*;
 *
 * using(configuration)
 *    .select(ACTOR.FIRST_NAME, ACTOR.LAST_NAME)
 *    .from(ACTOR)
 *    .orderBy(
 *        ACTOR.FIRST_NAME.collate(collation("utf8_german2_ci")),
 *        ACTOR.LAST_NAME.collate(collation("utf8_german2_ci")))
 *    .fetch();
 * </pre></code>
 * <p>
 * Instances can be created using {@link DSL#collation(Name)} and overloads.
 *
 * @author Lukas Eder
 */
public interface Collation extends QueryPart {

    /**
     * The name of the collation.
     */
    String getName();
}
