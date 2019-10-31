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
 * A description of the type of a {@link Table}.
 *
 * @author Lukas Eder
 */
public enum TableType {

    /**
     * An ordinary table that is stored in the schema.
     */
    TABLE,

    /**
     * A global temporary table that is stored in the schema and visible to
     * everyone.
     */
    TEMPORARY,

    /**
     * A view that is defined by a {@link Select} statement.
     */
    VIEW,

    /**
     * A materialised view that is defined by a {@link Select} statement, and
     * whose data is materialised in the schema.
     */
    MATERIALIZED_VIEW,

    /**
     * A table valued function that is defined by a {@link Routine}.
     */
    FUNCTION,

    /**
     * A table expression, such as a derived table, a joined table, a common
     * table expression, etc.
     */
    EXPRESSION,

    /**
     * A table type that is unknown to jOOQ.
     */
    UNKNOWN
}
