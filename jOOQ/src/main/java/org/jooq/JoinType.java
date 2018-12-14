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

import static org.jooq.SQLDialect.MYSQL;
// ...
// ...
// ...

import org.jooq.impl.DSL;

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
    JOIN("join", true),

    /**
     * <code>CROSS JOIN</code> two tables.
     */
    @Support
    CROSS_JOIN("cross join", false),

    /**
     * <code>LEFT OUTER JOIN</code> two tables.
     */
    @Support
    LEFT_OUTER_JOIN("left outer join", true),

    /**
     * <code>RIGHT OUTER JOIN</code> two tables.
     */
    @Support
    RIGHT_OUTER_JOIN("right outer join", true),

    /**
     * <code>FULL OUTER JOIN</code> two tables.
     */
    @Support
    FULL_OUTER_JOIN("full outer join", true),

    /**
     * <code>NATURAL INNER JOIN</code> two tables.
     */
    @Support
    NATURAL_JOIN("natural join", false),

    /**
     * <code>NATURAL LEFT OUTER JOIN</code> two tables.
     */
    @Support
    NATURAL_LEFT_OUTER_JOIN("natural left outer join", false),

    /**
     * <code>NATURAL RIGHT OUTER JOIN</code> two tables.
     */
    @Support
    NATURAL_RIGHT_OUTER_JOIN("natural right outer join", false),

    /**
     * <code>CROSS APPLY</code> two tables.
     */
    @Support({})
    CROSS_APPLY("cross apply", false),

    /**
     * <code>OUTER APPLY</code> two tables.
     */
    @Support({})
    OUTER_APPLY("outer apply", false),

    /**
     * <code>STRAIGHT_JOIN</code> two tables.
     */
    @Support({ MYSQL })
    STRAIGHT_JOIN("straight_join", true),

    /**
     * <code>LEFT SEMI JOIN</code> two tables.
     */
    @Support
    LEFT_SEMI_JOIN("left semi join", true),

    /**
     * <code>LEFT ANTI JOIN</code> two tables.
     */
    @Support
    LEFT_ANTI_JOIN("left anti join", true)

    ;

    private final String  sql;
    private final Keyword keyword;
    private final boolean qualified;

    private JoinType(String sql, boolean qualified) {
        this.sql = sql;
        this.keyword = DSL.keyword(sql);
        this.qualified = qualified;
    }

    public final String toSQL() {
        return sql;
    }

    public final Keyword toKeyword() {
        return keyword;
    }

    /**
     * Whether a <code>JOIN</code> operation of this type must be qualified with
     * <code>ON</code> or <code>USING</code>.
     */
    public final boolean qualified() {
        return qualified;
    }
}
