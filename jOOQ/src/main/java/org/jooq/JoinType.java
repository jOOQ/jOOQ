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

// ...
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
// ...
import static org.jooq.SQLDialect.HSQLDB;
// ...
// ...
import static org.jooq.SQLDialect.MARIADB;
// ...
import static org.jooq.SQLDialect.MYSQL;
// ...
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...
// ...
// ...
// ...
// ...

import org.jooq.impl.DSL;

/**
 * The type of join.
 *
 * @author Lukas Eder
 */
public enum JoinType {

    /**
     * <code>INNER JOIN</code> two tables.
     */
    @Support
    JOIN("join", "inner join", "join", true),

    /**
     * <code>CROSS JOIN</code> two tables.
     */
    @Support
    CROSS_JOIN("cross join", false),

    /**
     * <code>LEFT OUTER JOIN</code> two tables.
     */
    @Support
    LEFT_OUTER_JOIN("left outer join", "left outer join", "left join", true),

    /**
     * <code>RIGHT OUTER JOIN</code> two tables.
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    RIGHT_OUTER_JOIN("right outer join", "right outer join", "right join", true),

    /**
     * <code>FULL OUTER JOIN</code> two tables.
     */
    @Support({ FIREBIRD, HSQLDB, POSTGRES })
    FULL_OUTER_JOIN("full outer join", "full outer join", "full join", true),

    /**
     * <code>NATURAL INNER JOIN</code> two tables.
     */
    @Support
    NATURAL_JOIN("natural join", "natural inner join", "natural join", false),

    /**
     * <code>NATURAL LEFT OUTER JOIN</code> two tables.
     */
    @Support
    NATURAL_LEFT_OUTER_JOIN("natural left outer join", "natural left outer join", "natural left join", false),

    /**
     * <code>NATURAL RIGHT OUTER JOIN</code> two tables.
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    NATURAL_RIGHT_OUTER_JOIN("natural right outer join", "natural right outer join", "natural right join", false),

    /**
     * <code>NATURAL FULL OUTER JOIN</code> two tables.
     */
    @Support({ FIREBIRD, HSQLDB, POSTGRES })
    NATURAL_FULL_OUTER_JOIN("natural full outer join", "natural full outer join", "natural full join", false),

    /**
     * <code>CROSS APPLY</code> two tables.
     */
    @Support({ POSTGRES })
    CROSS_APPLY("cross apply", false),

    /**
     * <code>OUTER APPLY</code> two tables.
     */
    @Support({ POSTGRES })
    OUTER_APPLY("outer apply", false),

    /**
     * <code>STRAIGHT_JOIN</code> two tables.
     */
    @Support({ MARIADB, MYSQL })
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

    private final String  defaultSql;
    private final Keyword defaultKeyword;
    private final Keyword includingOptionalKeywords;
    private final Keyword excludingOptionalKeywords;
    private final boolean qualified;

    private JoinType(String sql, boolean qualified) {
        this(sql, sql, sql, qualified);
    }

    private JoinType(String defaultSql, String includingOptionalKeywords, String excludingOptionalKeywords, boolean qualified) {
        this.defaultSql = defaultSql;
        this.includingOptionalKeywords = DSL.keyword(includingOptionalKeywords);
        this.excludingOptionalKeywords = DSL.keyword(excludingOptionalKeywords);
        this.defaultKeyword = DSL.keyword(defaultSql);
        this.qualified = qualified;
    }

    public final String toSQL() {
        return defaultSql;
    }

    public final Keyword toKeyword() {
        return defaultKeyword;
    }

    public final Keyword toKeyword(boolean includeOptionalKeywords) {
        return includeOptionalKeywords ? includingOptionalKeywords : excludingOptionalKeywords;
    }

    /**
     * Whether a <code>JOIN</code> operation of this type must be qualified with
     * <code>ON</code> or <code>USING</code>.
     */
    public final boolean qualified() {
        return qualified;
    }
}
