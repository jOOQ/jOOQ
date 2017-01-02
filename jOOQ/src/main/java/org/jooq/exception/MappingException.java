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

import org.jooq.Cursor;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.ResultQuery;

/**
 * An error occurred while fetching data into a user defined Java object with
 * any of these methods:
 * <ul>
 * <li> {@link ResultQuery#fetchInto(Class)}</li>
 * <li> {@link Cursor#fetchInto(Class)}</li>
 * <li> {@link Result#into(Class)}</li>
 * <li> {@link Record#into(Class)}</li>
 * </ul>
 * ... or when copying data into a {@link Record} with any of these methods
 * <ul>
 * <li> {@link DSLContext#newRecord(org.jooq.Table, Object)}</li>
 * <li> {@link Record#from(Object)}</li>
 * </ul>
 *
 * @author Lukas Eder
 */
public class MappingException extends DataAccessException {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -6460945824599280420L;

    /**
     * Constructor for MappingException.
     *
     * @param message the detail message
     */
    public MappingException(String message) {
        super(message);
    }

    /**
     * Constructor for MappingException.
     *
     * @param message the detail message
     * @param cause the root cause
     */
    public MappingException(String message, Throwable cause) {
        super(message, cause);
    }
}
