/**
 * Copyright (c) 2009-2016, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
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
package org.jooq.exception;


/**
 * The <code>DataAccessException</code> is a generic {@link RuntimeException}
 * indicating that something went wrong while executing a SQL statement from
 * jOOQ. The idea behind this unchecked exception is borrowed from Spring's
 * JDBC's DataAccessException
 *
 * @author Sergey Epik - Merged into jOOQ from Spring JDBC Support
 * @author Lukas Eder
 */
public class DataAccessException extends RuntimeException {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 491834858363345767L;

    /**
     * Constructor for DataAccessException.
     *
     * @param message the detail message
     */
    public DataAccessException(String message) {
        super(message);
    }

    /**
     * Constructor for DataAccessException.
     *
     * @param message the detail message
     * @param cause the root cause (usually from using a underlying data access
     *            API such as JDBC)
     */
    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
