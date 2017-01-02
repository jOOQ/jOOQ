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
package org.jooq;

/**
 * The type of database interaction that is being executed with this context.
 */
public enum ExecuteType {

    /**
     * A <code>SELECT</code> query is being executed.
     * <p>
     * This may also apply to plain SQL <code>WITH .. SELECT</code> queries
     * (selections with common table expressions), <code>FETCH</code> queries
     * and other types of vendor-specific queries.
     */
    READ,

    /**
     * An <code>INSERT</code>, <code>UPDATE</code>, <code>DELETE</code>,
     * <code>MERGE</code> query is being executed.
     * <p>
     * This may also apply to plain SQL <code>REPLACE</code>,
     * <code>UPSERT</code> and other vendor-specific queries.
     */
    WRITE,

    /**
     * A DDL statement is being executed.
     * <p>
     * Currently, this only applies to <code>TRUNCATE</code> statements
     */
    DDL,

    /**
     * A batch statement is being executed.
     */
    BATCH,

    /**
     * A routine (stored procedure or function) is being executed.
     */
    ROUTINE,

    /**
     * An other (unknown) type of database interaction is being executed.
     */
    OTHER,
}
