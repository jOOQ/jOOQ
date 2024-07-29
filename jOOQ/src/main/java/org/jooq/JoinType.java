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

// ...
// ...
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.DERBY;
// ...
import static org.jooq.SQLDialect.FIREBIRD;
// ...
import static org.jooq.SQLDialect.H2;
// ...
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.IGNITE;
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
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.TRINO;
// ...
import static org.jooq.SQLDialect.YUGABYTEDB;

import org.jooq.impl.DSL;

import org.jetbrains.annotations.NotNull;

/**
 * The type of join.
 *
 * @author Lukas Eder
 */
public enum JoinType {

    /**
     * <code>INNER JOIN</code> two tables.
     */
    @NotNull
    @Support
    JOIN("join", "inner join", "join", true, false),

    /**
     * <code>CROSS JOIN</code> two tables.
     */
    @NotNull
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    CROSS_JOIN("cross join", false, false),

    /**
     * <code>LEFT OUTER JOIN</code> two tables.
     */
    @NotNull
    @Support
    LEFT_OUTER_JOIN("left outer join", "left outer join", "left join", true, false),

    /**
     * <code>RIGHT OUTER JOIN</code> two tables.
     */
    @NotNull
    @Support
    RIGHT_OUTER_JOIN("right outer join", "right outer join", "right join", true, false),

    /**
     * <code>FULL OUTER JOIN</code> two tables.
     */
    @NotNull
    @Support({ FIREBIRD, HSQLDB, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    FULL_OUTER_JOIN("full outer join", "full outer join", "full join", true, false),

    /**
     * <code>NATURAL INNER JOIN</code> two tables.
     */
    @NotNull
    @Support
    NATURAL_JOIN("natural join", "natural inner join", "natural join", false, false),

    /**
     * <code>NATURAL LEFT OUTER JOIN</code> two tables.
     */
    @NotNull
    @Support
    NATURAL_LEFT_OUTER_JOIN("natural left outer join", "natural left outer join", "natural left join", false, false),

    /**
     * <code>NATURAL RIGHT OUTER JOIN</code> two tables.
     */
    @NotNull
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    NATURAL_RIGHT_OUTER_JOIN("natural right outer join", "natural right outer join", "natural right join", false, false),

    /**
     * <code>NATURAL FULL OUTER JOIN</code> two tables.
     */
    @NotNull
    @Support({ FIREBIRD, HSQLDB, POSTGRES, SQLITE, TRINO, YUGABYTEDB })
    NATURAL_FULL_OUTER_JOIN("natural full outer join", "natural full outer join", "natural full join", false, false),

    /**
     * <code>CROSS APPLY</code> two tables.
     */
    @NotNull
    @Support({ FIREBIRD, POSTGRES, TRINO, YUGABYTEDB })
    CROSS_APPLY("cross apply", false, true),

    /**
     * <code>OUTER APPLY</code> two tables.
     */
    @NotNull
    @Support({ FIREBIRD, POSTGRES, TRINO, YUGABYTEDB })
    OUTER_APPLY("outer apply", false, true),

    /**
     * <code>STRAIGHT_JOIN</code> two tables.
     */
    @NotNull
    @Support({ MARIADB, MYSQL })
    STRAIGHT_JOIN("straight_join", true, false),

    /**
     * <code>LEFT SEMI JOIN</code> two tables.
     */
    @NotNull
    @Support
    LEFT_SEMI_JOIN("left semi join", true, false),

    /**
     * <code>LEFT ANTI JOIN</code> two tables.
     */
    @NotNull
    @Support
    LEFT_ANTI_JOIN("left anti join", true, false)

    ;

    private final String  defaultSql;
    private final Keyword defaultKeyword;
    private final Keyword includingOptionalKeywords;
    private final Keyword excludingOptionalKeywords;
    private final boolean qualified;
    private final boolean correlated;

    private JoinType(String sql, boolean qualified, boolean correlated) {
        this(sql, sql, sql, qualified, correlated);
    }

    private JoinType(
        String defaultSql,
        String includingOptionalKeywords,
        String excludingOptionalKeywords,
        boolean qualified,
        boolean correlated
    ) {
        this.defaultSql = defaultSql;
        this.includingOptionalKeywords = DSL.keyword(includingOptionalKeywords);
        this.excludingOptionalKeywords = DSL.keyword(excludingOptionalKeywords);
        this.defaultKeyword = DSL.keyword(defaultSql);
        this.qualified = qualified;
        this.correlated = correlated;
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

    /**
     * Whether a <code>JOIN</code> operation of this type can be correlated.
     */
    public final boolean correlated() {
        return correlated;
    }
}
