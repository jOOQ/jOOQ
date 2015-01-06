/**
 * Copyright (c) 2009-2014, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
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

import java.util.EnumSet;
import java.util.Set;

/**
 * Dialects and dialect families as supported by jOOQ.
 * <p>
 * jOOQ supports a variety of dialects, which are grouped into dialect families.
 * For instance, the SQL Server dialect family {@link #POSTGRES} is specialised
 * by its dialects
 * <ul>
 * <li> {@link #POSTGRES_9_3}</li>
 * <li> {@link #POSTGRES_9_4}</li>
 * </ul>
 *
 * @author Lukas Eder
 */
public enum SQLDialect {

    /**
     * The standard SQL dialect family.
     *
     * @deprecated - [#3844] - 3.6.0 - {@link #DEFAULT} will replace this
     *             pseudo-dialect.
     */
    @Deprecated
    SQL99(null, false),

    /**
     * The default SQL dialect.
     * <p>
     * This dialect is chosen in the absence of a more explicit dialect. It is
     * not intended to be used with any actual database as it may combined
     * dialect-specific things from various dialects.
     */
    DEFAULT(null, false),

    // -------------------------------------------------------------------------
    // SQL dialects for free usage
    // -------------------------------------------------------------------------

    /**
     * The CUBRID SQL dialect family.
     */
    CUBRID("CUBRID", false),

    /**
     * The Apache Derby SQL dialect family.
     */
    DERBY("Derby", false),

    /**
     * The Firebird SQL dialect family.
     */
    FIREBIRD("Firebird", false),

    /**
     * The H2 SQL dialect family.
     */
    H2("H2", false),

    /**
     * The Hypersonic SQL dialect family.
     */
    HSQLDB("HSQLDB", false),

    /**
     * The MariaDB dialect family.
     */
    MARIADB("MariaDB", false),

    /**
     * The MySQL dialect family.
     */
    MYSQL("MySQL", false),

    /**
     * The PostgreSQL dialect family.
     */
    POSTGRES("Postgres", false),

    /**
     * The PostgreSQL dialect family.
     */
    POSTGRES_9_3("Postgres", false, POSTGRES, null),

    /**
     * The PostgreSQL dialect family.
     */
    POSTGRES_9_4("Postgres", false, POSTGRES, POSTGRES_9_3),

    /**
     * The SQLite dialect family.
     */
    SQLITE("SQLite", false),

    // -------------------------------------------------------------------------
    // SQL dialects for commercial usage
    // -------------------------------------------------------------------------

    /* [pro] xx

    xxx
     x xxx xx xxxxxx xxx xxxxxxx xxxxxxx
     x xxx
     x xxxx xxxxxxx xx xxxxxxxxx xx xxxxxxxxxx xxxx xxxxxxxxxxxxxx xxxxx
     xx
    xxxxxxxxxxxxxxxx xxxxxx

    xxx
     x xxx xx xxxxxx xxxx xxx xxxxxxxx
     x xxx
     x xxxx xxxxxxx xx xxxxxxxxx xx xxxxxxxxxx xxxx xxxxxxxxxxxxxx xxxxx
     xx
    xxxxxxxxxxxxxxxxxxxx xxxxx xxxxxxxx

    xxx
     x xxx xxxxxx xxxxxxxx xxxxxx xxx xxxxxxx xxxxxxx
     x xxx
     x xxxx xxxxxxx xx xxxxxxxxx xx xxxxxxxxxx xxxx xxxxxxxxxxxxxx xxxxx
     xx
    xxxxxxxxxx xxxxxx

    xxx
     x xxx xxx xxx xxx xxxxxxx xxxxxxx
     x xxx
     x xxxx xxxxxxx xx xxxxxxxxx xx xxxxxxxxxx xxxx xxxxxxxxxxxxxx xxxxx
     xx
    xxxxxxxxxx xxxxxx

    xxx
     x xxx xxx xxx xxx xxx xxxxxxxx
     x xxx
     x xxxx xxxxxxx xx xxxxxxxxx xx xxxxxxxxxx xxxx xxxxxxxxxxxxxx xxxxx
     xx
    xxxxxxxxxxxx xxxxx xxxxx

    xxx
     x xxx xxx xxx xxxx xxx xxxxxxxx
     x xxx
     x xxxx xxxxxxx xx xxxxxxxxx xx xxxxxxxxxx xxxx xxxxxxxxxxxxxx xxxxx
     xx
    xxxxxxxxxxxxx xxxxx xxxx xxxxxxx

    xxx
     x xxx xxx xxxx xxx xxxxxxxx
     x xxx
     x xxxx xxxxxxx xx xxxxxxxxx xx xxxxxxxxxx xxxx xxxxxxxxxxxxxx xxxxx
     xx
    xxxxxxxxxxxx xxxxxx

    xxx
     x xxx xxxxxxxx xxx xxxxxxxx
     x xxx
     x xxxx xxxxxxx xx xxxxxxxxx xx xxxxxxxxxx xxxx xxxxxxxxxxxxxx xxxxx
     xx
    xxxxxxxxxxxxxxxxxxxx xxxxxx

    xxx
     x xxx xxxxxx xxxxxxx xxxxxxx
     x xxx
     x xxxx xxxxxxx xx xxxxxxxxx xx xxxxxxxxxx xxxx xxxxxxxxxxxxxx xxxxx
     xx
    xxxxxxxxxxxxxxxx xxxxxx

    xxx
     x xxx xxxxxx xxxxxxx xxxxxxx
     x xxx
     x xxxx xxxxxxx xx xxxxxxxxx xx xxxxxxxxxx xxxx xxxxxxxxxxxxxx xxxxx
     xx
    xxxxxxxxxxxxxxxx xxxxxx

    xxx
     x xxx xxxxxx xxx xxxxxxxx
     x xxx
     x xxxx xxxxxxx xx xxxxxxxxx xx xxxxxxxxxx xxxx xxxxxxxxxxxxxx xxxxx
     xx
    xxxxxxxxxxxxxxxxxxx xxxxx xxxxxxxx

    xxx
     x xxx xxxxxx xxx xxxxxxxx
     x xxx
     x xxxx xxxxxxx xx xxxxxxxxx xx xxxxxxxxxx xxxx xxxxxxxxxxxxxx xxxxx
     xx
    xxxxxxxxxxxxxxxxxxx xxxxx xxxxxxx xxxxxxxxxxx

    xxx
     x xxx xxxxxx xxx xxxxxxxx
     x xxx
     x xxxx xxxxxxx xx xxxxxxxxx xx xxxxxxxxxx xxxx xxxxxxxxxxxxxx xxxxx
     xx
    xxxxxxxxxxxxxxxxxxx xxxxx xxxxxxx xxxxxxxxxxx

    xxx
     x xxx xxx xxxxxx xxxxxxx xxxxxxx
     x xxx
     x xxxx xxxxxxx xx xxxxxxxxx xx xxxxxxxxxx xxxx xxxxxxxxxxxxxx xxxxx
     xx
    xxxxxxxxxxxxxxxxxxxxxx xxxxxx

    xxx
     x xxx xxx xxxxxx xxxx xxxxxxxx
     x xxx
     x xxxx xxxxxxx xx xxxxxxxxx xx xxxxxxxxxx xxxx xxxxxxxxxxxxxx xxxxx
     xx
    xxxxxxxxxxxxxxxxxxxxxxxxxx xxxxx xxxxxxxxxxx

    xxx
     x xxx xxx xxxxxx xxxx xxxxxxxx
     x xxx
     x xxxx xxxxxxx xx xxxxxxxxx xx xxxxxxxxxx xxxx xxxxxxxxxxxxxx xxxxx
     xx
    xxxxxxxxxxxxxxxxxxxxxxxxxx xxxxx xxxxxxxxxx xxxxxxxxxxxxxxx

    xxx
     x xxx xxx xxxxxx xxxx xxxxxxxx
     x xxx
     x xxxx xxxxxxx xx xxxxxxxxx xx xxxxxxxxxx xxxx xxxxxxxxxxxxxx xxxxx
     xx
    xxxxxxxxxxxxxxxxxxxxxxxxxx xxxxx xxxxxxxxxx xxxxxxxxxxxxxxx

    xxx
     x xxx xxxxxx xxx xxxxxxxx xxxxxxx xxxxxxx
     x xxx
     x xxxx xxxxxxx xx xxxxxxxxx xx xxxxxxxxxx xxxx xxxxxxxxxxxxxx xxxxx
     xx
    xxxxxxxxxxxxxxxx xxxxxx

    xx [/pro] */

    ;

    private static final SQLDialect[] FAMILIES;

    static {
        Set<SQLDialect> set = EnumSet.noneOf(SQLDialect.class);

        for (SQLDialect dialect : values()) {
            set.add(dialect.family());
        }

        FAMILIES = set.toArray(new SQLDialect[set.size()]);
    }

    private final String              name;
    private final boolean             commercial;
    private final SQLDialect          family;
    private SQLDialect                predecessor;

    private SQLDialect(String name, boolean commercial) {
        this(name, commercial, null, null);
    }

    private SQLDialect(String name, boolean commercial, SQLDialect family) {
        this(name, commercial, family, null);
    }

    private SQLDialect(String name, boolean commercial, SQLDialect family, SQLDialect predecessor) {
        this.name = name;
        this.commercial = commercial;
        this.family = family == null ? this : family;
        this.predecessor = predecessor == null ? this : predecessor;

        if (family != null)
            family.predecessor = this;
    }

    /**
     * Whether this dialect is supported with the jOOQ commercial license only.
     */
    public final boolean commercial() {
        return commercial;
    }

    /**
     * The dialect family.
     * <p>
     * This returns the dialect itself, if it has no "parent family". E.g.
     * <code><pre>
     * SQLSERVER == SQLSERVER2012.family();
     * SQLSERVER == SQLSERVER2008.family();
     * SQLSERVER == SQLSERVER.family();
     * </pre></code>
     */
    public final SQLDialect family() {
        return family;
    }

    /**
     * The predecessor dialect.
     * <p>
     * If this is a dialect version (e.g. {@link #POSTGRES_9_4}) within a family
     * (e.g. {@link #POSTGRES}), then the predecessor will point to the
     * historically previous dialect version (e.g. {@link #POSTGRES_9_3}) within
     * the same family, or to the dialect itself if there was no predecessor
     * explicitly supported by jOOQ.
     */
    public final SQLDialect predecessor() {
        return predecessor;
    }

    /**
     * Whether this dialect precedes an other dialect from the same family.
     * <p>
     * This returns:
     * <ul>
     * <li><code>true</code> if this dialect is the same as the other dialect</li>
     * <li><code>true</code> if this dialect precedes the other dialect via any
     * number of calls to {@link #predecessor()}</li>
     * </ul>
     * The above also implies that:
     * <ul>
     * <li><code>false</code> if the two dialects do not belong to the same
     * family</li>
     * </ul>
     * <p>
     * This is useful to see if some feature is supported by <em>"at least"</em>
     * a given dialect version. Example: <code><pre>
     * // Do this block only if the chosen dialect supports PostgreSQL 9.4+ features
     * if (POSTGRES_9_4.precedes(dialect)) {
     * }
     *
     * // Do this block only if the chosen dialect supports PostgreSQL 9.3+ features
     * else if (POSTGRES_9_3.precedes(dialect)) {
     * }
     *
     * // Fall back to pre-PostgreSQL 9.3 behaviour
     * else {
     * }
     * </pre></code>
     */
    public final boolean precedes(SQLDialect other) {
        if (family != other.family)
            return false;

        SQLDialect candidate = other;
        while (candidate != null) {
            if (this == candidate)
                return true;

            if (candidate == candidate.predecessor())
                return false;

            candidate = candidate.predecessor();
        }

        return false;
    }

    /**
     * The name of this dialect as it appears in related class names.
     */
    public final String getName() {
        return name;
    }

    /**
     * The name of this dialect as it appears in related package names.
     */
    public final String getNameLC() {
        return name == null ? null : name.toLowerCase();
    }

    /**
     * The name of this dialect as it appears in related enum values.
     */
    public final String getNameUC() {
        return name == null ? null : name.toUpperCase();
    }

    /**
     * Get a list of all {@link SQLDialect#family()} values.
     */
    public static final SQLDialect[] families() {
        return FAMILIES.clone();
    }
}
