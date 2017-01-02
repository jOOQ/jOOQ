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
package org.jooq.exception;

import org.jooq.Query;
import org.jooq.ResultQuery;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;

/**
 * An unexpected result was encountered after executing a {@link Query}. This
 * exception indicates wrong usage of jOOQ's various fetch methods, or an
 * integrity problem in your data.
 * <p>
 * This is typically the case in the following situations:
 * <ul>
 * <li>When you call methods such as {@link ResultQuery#fetchOne()} and the
 * database returns more than one record.</li>
 * <li>When you call methods such as
 * {@link ResultQuery#fetchMap(org.jooq.Field)} and the database returns several
 * records per key.</li>
 * <li>When you refresh a {@link TableRecord} using
 * {@link UpdatableRecord#refresh()}, and the record does not exist anymore in
 * the database.</li>
 * </ul>
 *
 * @author Lukas Eder
 */
public class InvalidResultException extends DataAccessException {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -6460945824599280420L;

    /**
     * Constructor for InvalidResultException.
     *
     * @param message the detail message
     */
    public InvalidResultException(String message) {
        super(message);
    }
}
