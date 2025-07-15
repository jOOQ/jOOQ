/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
 *
 * For more information, please visit: https://www.jooq.org/legal/licensing
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
package org.jooq.exception;

import org.jooq.Field;
import org.jooq.SQLDialect;
import org.jooq.Table;

/**
 * A feature needs to be emulated for a specific {@link SQLDialect}, but meta
 * data is unavailable, so the emulation won't work.
 * <p>
 * This exception helps users better understand why an emulation can't be made
 * to work when meta data is unavailable to jOOQ internals. For historic
 * reasons, this exception isn't thrown in <em>all</em> such cases in order not
 * to break compatibility with cases where {@link SQLDialect#DEFAULT} SQL is
 * being generated, or dummy meta data is being produced (e.g. an empty list of
 * {@link Field}, when calling {@link Table#fields()} on a plain SQL table).
 *
 * @author Lukas Eder
 */
public class MetaDataUnavailableException extends DataAccessException {

    /**
     * Constructor for MetaDataUnavailableException.
     *
     * @param message the detail message
     */
    public MetaDataUnavailableException(String message) {
        super(message);
    }

    /**
     * Constructor for MetaDataUnavailableException.
     *
     * @param message the detail message
     * @param cause the cause
     */
    public MetaDataUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
