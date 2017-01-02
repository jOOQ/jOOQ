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

package org.jooq;

import static org.jooq.SQLDialect.MYSQL;
// ...
// ...
// ...


/**
 * The type of join
 *
 * @author Lukas Eder
 */
public enum JoinType {

    /**
     * <code>INNER JOIN</code> two tables.
     */
    @Support
    JOIN("join"),

    /**
     * <code>CROSS JOIN</code> two tables.
     */
    @Support
    CROSS_JOIN("cross join"),

    /**
     * <code>LEFT OUTER JOIN</code> two tables.
     */
    @Support
    LEFT_OUTER_JOIN("left outer join"),

    /**
     * <code>RIGHT OUTER JOIN</code> two tables.
     */
    @Support
    RIGHT_OUTER_JOIN("right outer join"),

    /**
     * <code>FULL OUTER JOIN</code> two tables.
     */
    @Support
    FULL_OUTER_JOIN("full outer join"),

    /**
     * <code>NATURAL INNER JOIN</code> two tables.
     */
    @Support
    NATURAL_JOIN("natural join"),

    /**
     * <code>NATURAL LEFT OUTER JOIN</code> two tables.
     */
    @Support
    NATURAL_LEFT_OUTER_JOIN("natural left outer join"),

    /**
     * <code>NATURAL RIGHT OUTER JOIN</code> two tables.
     */
    @Support
    NATURAL_RIGHT_OUTER_JOIN("natural right outer join"),

    /**
     * <code>CROSS APPLY</code> two tables.
     */
    @Support({})
    CROSS_APPLY("cross apply"),

    /**
     * <code>OUTER APPLY</code> two tables.
     */
    @Support({})
    OUTER_APPLY("outer apply"),

    /**
     * <code>STRAIGHT_JOIN</code> two tables.
     */
    @Support({ MYSQL })
    STRAIGHT_JOIN("straight_join"),

    /**
     * <code>LEFT SEMI JOIN</code> two tables.
     */
    @Support
    LEFT_SEMI_JOIN("left semi join"),

    /**
     * <code>LEFT ANTI JOIN</code> two tables.
     */
    @Support
    LEFT_ANTI_JOIN("left anti join")

    ;

    private final String sql;

    private JoinType(String sql) {
        this.sql = sql;
    }

    public final String toSQL() {
        return sql;
    }
}
