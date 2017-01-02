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

import org.jooq.tools.jdbc.MockDataProvider;

/**
 * An unexpected result can be encountered while loading a file-based
 * {@link MockDataProvider}.
 * <p>
 * This exception may for instance indicate that the number of rows is not in
 * accordance with the specified number of rows. I.e. it not be allowed for the
 * file-based {@link MockDataProvider} to have this content:
 * <p>
 * <code><pre>
 * select "TABLE2"."ID2", "TABLE2"."NAME2" from "TABLE2"
 * > +---+-----+
 * > |ID2|NAME2|
 * > +---+-----+
 * > |1  |X    |
 * > |2  |Y    |
 * > +---+-----+
 * &#64; rows: 1000
 * </pre></code>
 *
 * @author Samy Deghou
 * @author Lukas Eder
 */
public class MockFileDatabaseException extends DataAccessException {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -6460945824599280420L;

    /**
     * Constructor for MockFileDatabaseException.
     *
     * @param message the detail message
     */
    public MockFileDatabaseException(String message) {
        super(message);
    }

    /**
     * Constructor for MockFileDatabaseException.
     *
     * @param message the detail message
     * @param cause the root cause
     */
    public MockFileDatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
