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

/**
 * The jOOQ <code>IOException</code> is a wrapper for a
 * {@link java.io.IOException}.
 *
 * @author Lukas Eder
 */
public class IOException extends DataAccessException {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 491834858363345767L;

    /**
     * Constructor for DataAccessException.
     *
     * @param message the detail message
     * @param cause the root cause (usually from using a underlying data access
     *            API such as JDBC)
     */
    public IOException(String message, java.io.IOException cause) {
        super(message, cause);
    }

    @Override
    public synchronized java.io.IOException getCause() {
        return (java.io.IOException) super.getCause();
    }

    @Override
    public synchronized Throwable initCause(Throwable cause) {
        if (!(cause instanceof java.io.IOException))
            throw new IllegalArgumentException("Can only wrap java.io.IOException: " + cause);

        return super.initCause(cause);
    }
}
