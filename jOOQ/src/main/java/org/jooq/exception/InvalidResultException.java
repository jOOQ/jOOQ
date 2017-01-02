/*
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
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
