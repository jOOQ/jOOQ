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

package org.jooq.impl;

import org.jooq.SQLDialect;
import org.jooq.Select;

/**
 * A combine operator is used to combine result sets of two arbitrary
 * {@link Select} queries.
 *
 * @author Lukas Eder
 */
enum CombineOperator {

    /**
     * Unite the sets of rows produced by the two {@link Select}'s (disallowing
     * duplicate records).
     */
    UNION("union"),

    /**
     * Unite the bags of rows produced by the two {@link Select}'s (allowing
     * duplicate records).
     */
    UNION_ALL("union all"),

    /**
     * Remove all rows in the set of rows produced by the second {@link Select}
     * from the set of rows produced by the first {@link Select} (disallowing
     * duplicate records).
     */
    EXCEPT("except"),

    /**
     * Remove all rows in the bag of rows produced by the second {@link Select}
     * from the bag of rows produced by the first {@link Select} (allowing
     * duplicate records).
     */
    EXCEPT_ALL("except all"),

    /**
     * Retain all rows in the sets of rows produced by both {@link Select}'s
     * (disallowing duplicate records).
     */
    INTERSECT("intersect"),

    /**
     * Retain all rows in the bags of rows produced by both {@link Select}'s
     * (allowing duplicate records).
     */
    INTERSECT_ALL("intersect all");

    private final String sql;

    private CombineOperator(String sql) {
        this.sql = sql;
    }

    public String toSQL(SQLDialect dialect) {








        return sql;
    }
}
