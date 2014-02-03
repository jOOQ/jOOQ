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

/**
 * Dialects and dialect families as supported by jOOQ.
 * <p>
 * jOOQ supports a variety of dialects, which are grouped into dialect families.
 * For instance, the SQL Server dialect family {@link #SQLSERVER} is specialised
 * by its dialects
 * <ul>
 * <li> {@link #SQLSERVER2008}</li>
 * <li> {@link #SQLSERVER2012}</li>
 * </ul>
 *
 * @author Lukas Eder
 */
public enum SQLDialect {

    /**
     * The standard SQL dialect family.
     *
     * @deprecated - Do not reference this pseudo-dialect. It is only used for
     *             unit testing
     */
    @Deprecated
    SQL99(null, false),

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
    xxxxxxxxxxxxx xxxxx xxxxx

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
    xxxxxxxxxxxxxxxxxxx xxxxx xxxxxxxx

    xxx
     x xxx xxxxxx xxx xxxxxxxx
     x xxx
     x xxxx xxxxxxx xx xxxxxxxxx xx xxxxxxxxxx xxxx xxxxxxxxxxxxxx xxxxx
     xx
    xxxxxxxxxxxxxxxxxxx xxxxx xxxxxxxx

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
    xxxxxxxxxxxxxxxxxxxxxxxxxx xxxxx xxxxxxxxxxx

    xxx
     x xxx xxxxxx xxx xxxxxxxx xxxxxxx xxxxxxx
     x xxx
     x xxxx xxxxxxx xx xxxxxxxxx xx xxxxxxxxxx xxxx xxxxxxxxxxxxxx xxxxx
     xx
    xxxxxxxxxxxxxxxx xxxxxx

    xx [/pro] */

    ;

    private final String     name;
    private final boolean    commercial;
    private final SQLDialect family;

    private SQLDialect(String name, boolean commercial) {
        this(name, commercial, null);
    }

    private SQLDialect(String name, boolean commercial, SQLDialect family) {
        this.name = name;
        this.commercial = commercial;
        this.family = family;
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
        return family == null ? this : family;
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
}
