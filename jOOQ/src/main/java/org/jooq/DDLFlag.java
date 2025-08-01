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
package org.jooq;


/**
 * @author Lukas Eder
 */
public enum DDLFlag {

    /**
     * Whether <code>CREATE SCHEMA</code> statements should be generated.
     */
    SCHEMA,

    /**
     * Whether <code>CREATE TABLE</code> statements should be generated.
     */
    TABLE,

    /**
     * Whether <code>PRIMARY KEY</code> constraints should be generated.
     */
    PRIMARY_KEY,

    /**
     * Whether <code>UNIQUE</code> constraints should be generated.
     */
    UNIQUE,

    /**
     * Whether <code>FOREIGN KEY</code> constraints should be generated.
     */
    FOREIGN_KEY,

    /**
     * Whether <code>CHECK</code> constraints should be generated.
     */
    CHECK,

    /**
     * Whether <code>INDEX</code> definitions should be generated.
     */
    INDEX,

    /**
     * Whether <code>UDT</code> statements should be generated.
     */
    UDT,

    /**
     * Whether <code>DOMAIN</code> statements should be generated.
     */
    DOMAIN,

    /**
     * Whether <code>SEQUENCE</code> statements should be generated.
     */
    SEQUENCE,

    /**
     * Whether <code>COMMENT</code> statements should be generated.
     */
    COMMENT,














}
