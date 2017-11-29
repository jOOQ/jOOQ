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

/**
 * An enum that specifies the nullability of a {@link DataType}.
 *
 * @author Lukas Eder
 */
public enum Nullability {

    /**
     * The data type is explicitly nullable.
     */
    NULL,

    /**
     * The data type is explicitly not nullable.
     */
    NOT_NULL,

    /**
     * Default behaviour for data types.
     * <p>
     * The interpretation of this value depends on the context in which a
     * {@link DataType} is used:
     * <ul>
     * <li><code>CREATE TABLE</code>: The data type is implicitly nullable (or
     * explicitly, if the underlying database does not support implicit
     * nullability or defaults to implicit non-nullability).</li>
     * <li><code>ALTER TABLE .. ALTER COLUMN .. SET TYPE</code>: The data type's
     * nullability will not be modified by jOOQ (but it may well be modified by
     * the database, e.g. {@link SQLDialect#MYSQL} or
     * {@link SQLDialect#SQLSERVER}).</li>
     * </ul>
     */
    DEFAULT;

    /**
     * Get the explicit {@link Nullability} corresponding to a boolean value.
     */
    public static Nullability of(boolean nullability) {
        return nullability ? NULL : NOT_NULL;
    }

    /**
     * Whether this nullability encodes an explicitly or implicitly nullable
     * type.
     */
    public boolean nullable() {
        return this != NOT_NULL;
    }
}
