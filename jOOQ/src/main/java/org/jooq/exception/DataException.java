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
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
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

import java.sql.SQLDataException;
import java.sql.SQLException;

import io.r2dbc.spi.R2dbcException;

/**
 * The <code>DataException</code> is jOOQ's equivalent of JDBC's
 * {@link SQLDataException}.
 * <p>
 * It is thrown by jOOQ whenever jOOQ detects
 * {@link SQLStateClass#C22_DATA_EXCEPTION} from the JDBC driver. Whether this
 * SQL state is available is JDBC driver implementation specific.
 *
 * @author Lukas Eder
 */
public class DataException extends DataAccessException {

    /**
     * Constructor for DataException.
     *
     * @param message the detail message
     */
    public DataException(String message) {
        super(message);
    }

    /**
     * Constructor for DataException.
     *
     * @param message the detail message
     * @param cause the root cause (usually from using a underlying data access
     *            API such as JDBC)
     */
    public DataException(String message, Throwable cause) {
        super(message, cause);
    }
}
