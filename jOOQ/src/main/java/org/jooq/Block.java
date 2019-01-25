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
 * A procedural block.
 * <p>
 * Many RDBMS support procedural languages and in those languages, blocks are an
 * essential means of grouping logic and creating scope. Some databases support
 * executing anonymous blocks, in case of which the jOOQ <code>Block</code> can
 * be executed like any other {@link Query}. This works in a similar way as a
 * {@link Batch} containing multiple queries, but unlike a {@link Batch}, a
 * {@link Block} can contain procedural code as well.
 * <p>
 * <strong>Example:</strong>
 * <p>
 * <code><pre>
 * // Assuming import static org.jooq.impl.DSL.*;
 *
 * // Wrapping SQL statements only
 * using(configuration)
 *    .begin(
 *        insertInto(TABLE1).columns(TABLE1.COL).values(1),
 *        insertInto(TABLE2).columns(TABLE2.COL).values(2),
 *        insertInto(TABLE3).columns(TABLE3.COL).values(3)
 *    )
 *    .execute();
 *
 * // Wrapping procedural code
 * Variable&lt;Integer> i = var("i", SQLDataType.INTEGER);
 * using(configuration)
 *    .begin(
 *        for_(i).in(1, 3).loop(
 *            insertInto(TABLE1).columns(TABLE1.COL).values(i)
 *        )
 *    )
 *    .execute();
 * </pre></code>
 * <p>
 * Instances can be created using {@link DSL#begin(Statement...)} and overloads.
 *
 * @author Lukas Eder
 */
public interface Block extends Query {

}
