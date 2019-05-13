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
 * A <code>MERGE</code> statement.
 * <p>
 * <strong>Example:</strong>
 * <p>
 * <code><pre>
 * // Assuming import static org.jooq.impl.DSL.*;
 *
 * using(configuration)
 *    .mergeInto(CUSTOMER)
 *    .using(selectFrom(CUSTOMER_IMPORT))
 *    .on(CUSTOMER.ID.eq(CUSTOMER_IMPORT.ID))
 *    .whenMatchedThenUpdate()
 *    .set(CUSTOMER.FIRST_NAME, CUSTOMER_IMPORT.FIRST_NAME)
 *    .set(CUSTOMER.LAST_NAME, CUSTOMER_IMPORT.LAST_NAME)
 *    .whenNotMatchedThenInsert(CUSTOMER.FIRST_NAME, CUSTOMER.LAST_NAME)
 *    .values(CUSTOMER_IMPORT.FIRST_NAME, CUSTOMER_IMPORT.LAST_NAME)
 *    .execute();
 * </pre></code>
 * <p>
 * Instances can be created using {@link DSL#mergeInto(Table)} and overloads.
 *
 * @author Lukas Eder
 */
public interface Merge<R extends Record> extends RowCountQuery {

}
