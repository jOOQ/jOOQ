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

import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * Dialects and dialect families as supported by jOOQ.
 * <p>
 * The commercial jOOQ distributions support a variety of dialects, which are
 * grouped into dialect families. For instance, the SQL Server dialect family
 * {@link #POSTGRES} is specialised by its dialects
 * <ul>
 * <li>{@link #POSTGRES_9_3}</li>
 * <li>{@link #POSTGRES_9_4}</li>
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
    SQL99("", false, false),

    /**
     * The default SQL dialect.
     * <p>
     * This dialect is chosen in the absence of a more explicit dialect. It is
     * not intended to be used with any actual database as it may combined
     * dialect-specific things from various dialects.
     */
    DEFAULT("", false, false),

    // -------------------------------------------------------------------------
    // SQL dialects for free usage
    // -------------------------------------------------------------------------

    /**
     * The CUBRID dialect family.
     *
     * @deprecated - [#9403] - 3.13.0 - This dialect is hardly used by anyone
     *             with jOOQ or without jOOQ and will be removed in the near
     *             future.
     */
    @Deprecated
    CUBRID("CUBRID", false, true),

    /**
     * The Apache Derby dialect family.
     */
    DERBY("Derby", false, true),

    /**
     * The Firebird dialect family.
     * <p>
     * This family behaves like the versioned dialect {@link #FIREBIRD_3_0}.
     */
    FIREBIRD("Firebird", false, true),



















    /**
     * The H2 dialect family.
     */
    H2("H2", false, true),

    /**
     * The Hypersonic dialect family.
     */
    HSQLDB("HSQLDB", false, true),

    /**
     * The MariaDB dialect family.
     * <p>
     * This family behaves like the versioned dialect {@link #MARIADB_10_5}.
     */
    MARIADB("MariaDB", false, true),





















































    /**
     * The MySQL dialect family.
     * <p>
     * This family behaves like the versioned dialect {@link #MYSQL_8_0_19}.
     */
    MYSQL("MySQL", false, true),



























    /**
     * The PostgreSQL dialect family.
     * <p>
     * This family behaves like the versioned dialect {@link #POSTGRES_11}.
     * <p>
     * While this family (and its dialects) have been observed to work to some
     * extent on Amazon RedShift as well, we strongly suggest you use the
     * official {@link #REDSHIFT} support, instead.
     */
    POSTGRES("Postgres", false, true),











































































    /**
     * The SQLite dialect family.
     * <p>
     * This family behaves like the versioned dialect {@link #SQLITE_3_30}.
     */
    SQLITE("SQLite", false, true),




























    // -------------------------------------------------------------------------
    // SQL dialects for commercial usage
    // -------------------------------------------------------------------------




















































































































































































































































































    ;

    private static final SQLDialect[] FAMILIES;

    static {
        Set<SQLDialect> set = EnumSet.noneOf(SQLDialect.class);

        for (SQLDialect dialect : values()) {
            set.add(dialect.family());
        }

        FAMILIES = set.toArray(new SQLDialect[0]);
    }

    private final String                  name;
    private final boolean                 commercial;
    private final boolean                 supported;
    private final SQLDialect              family;
    private SQLDialect                    predecessor;
    private transient EnumSet<SQLDialect> predecessors;
    private final ThirdParty              thirdParty;

    /**
     * Get a list of all {@link SQLDialect#family()} values.
     */
    public static final SQLDialect[] families() {
        return FAMILIES.clone();
    }

    /**
     * Get a set of dialects preceding a given set of dialects.
     * <p>
     * The resulting set of dialects contain all the families and dialect
     * versions that precede the argument dialects.
     */
    public static final Set<SQLDialect> predecessors(SQLDialect... dialects) {
        EnumSet<SQLDialect> result = EnumSet.noneOf(SQLDialect.class);

        for (SQLDialect dialect : dialects)
            result.addAll(dialect.predecessors());

        return Collections.unmodifiableSet(result);
    }

    /**
     * Get a set of supported dialect versions and predecessors given a dialect
     * version.
     * <p>
     * The resulting set of dialects contain all the families and dialect
     * versions that precede the argument dialect.
     */
    public static final Set<SQLDialect> supportedUntil(SQLDialect dialect) {
        return predecessors(dialect);
    }

    /**
     * Get a set of supported dialect versions and predecessors given a dialect
     * version.
     * <p>
     * The resulting set of dialects contain all the families and dialect
     * versions that precede the argument dialect.
     */
    public static final Set<SQLDialect> supportedUntil(SQLDialect... dialects) {
        return predecessors(dialects);
    }

    /**
     * Get a set of supported dialect versions and successors given a dialect
     * version.
     * <p>
     * The resulting set of dialects contain all the families and dialect
     * versions that support the argument dialect, i.e. that succeed it.
     */
    public static final Set<SQLDialect> supportedBy(SQLDialect dialect) {
        EnumSet<SQLDialect> result = EnumSet.noneOf(SQLDialect.class);
        addSupportedBy(dialect, result);
        return Collections.unmodifiableSet(result);
    }

    /**
     * Get a set of supported dialect versions and successors given a set of
     * dialect versions.
     * <p>
     * The resulting set of dialects contain all the families and dialect
     * versions that support the argument dialects, i.e. that succeed them.
     */
    public static final Set<SQLDialect> supportedBy(SQLDialect... dialects) {
        EnumSet<SQLDialect> result = EnumSet.noneOf(SQLDialect.class);

        for (SQLDialect dialect : dialects)
            addSupportedBy(dialect, result);

        return Collections.unmodifiableSet(result);
    }

    private static final void addSupportedBy(SQLDialect dialect, EnumSet<SQLDialect> supported) {
        supported.add(dialect);

        if (dialect.isFamily())
            supported.addAll(dialect.predecessors());
        else
            for (SQLDialect candidate = dialect.family(); candidate != dialect; candidate = candidate.predecessor())
                supported.add(candidate);
    }

    private SQLDialect(String name, boolean commercial, boolean supported) {
        this(name, commercial, supported, null, null);
    }

    private SQLDialect(String name, boolean commercial, boolean supported, SQLDialect family) {
        this(name, commercial, supported, family, null);
    }

    private SQLDialect(String name, boolean commercial, boolean supported, SQLDialect family, SQLDialect predecessor) {
        this.name = name;
        this.commercial = commercial;
        this.supported = supported;
        this.family = family == null ? this : family;
        this.predecessor = predecessor == null ? this : predecessor;

        if (family != null)
            family.predecessor = this;

        this.thirdParty = new ThirdParty();
    }

    /**
     * Whether this dialect is supported with the jOOQ commercial license only.
     */
    public final boolean commercial() {
        return commercial;
    }

    /**
     * Whether this dialect is supported by jOOQ as an output dialect.
     * <p>
     * Unsupported, non-output dialects include:
     * <ul>
     * <li>{@link #DEFAULT}: A hypothetical dialect used for
     * {@link QueryPart#toString()} calls of unattached query parts.</li>
     * <li>{@link #SQL99}: A legacy version of {@link #DEFAULT}.</li>
     * <li>{@link #POSTGRESPLUS}: A not yet supported dialect.</li>
     * </ul>
     */
    public final boolean supported() {
        return supported;
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
     * Whether this dialect is a {@link #family()}.
     */
    public final boolean isFamily() {
        return this == family;
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
     * The predecessor dialects.
     * <p>
     * Recursively calls {@link #predecessor()} and finds all the preceding
     * dialects to this one, including this one.
     */
    public final Set<SQLDialect> predecessors() {
        if (predecessors == null) {
            SQLDialect curr = this;

            EnumSet<SQLDialect> result = EnumSet.of(curr);
            for (;;) {
                SQLDialect pred = curr.predecessor();
                result.add(pred);

                if (curr == pred)
                    break;

                curr = pred;
            }

            predecessors = result;
        }

        return Collections.unmodifiableSet(predecessors);
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

        return other.predecessors().contains(this);
    }

    /**
     * Check whether this dialect supports another one.
     * <p>
     * This is:
     * <ul>
     * <li><code>false</code> if dialects don't belong to the same family</li>
     * <li><code>true</code> if either dialect {@link #isFamily()}</li>
     * <li><code>true</code> if <code>other</code> dialect precedes this
     * dialect</li>
     * </ul>
     * <p>
     * The <code>other</code> argument dialect is typically referenced from a
     * {@link Support} annotation, whereas this dialect is the user dialect.
     */
    public final boolean supports(SQLDialect other) {
        if (family != other.family)
            return false;

        if (isFamily() || other.isFamily())
            return true;

        return other.precedes(this);
    }

    /**
     * Check whether this dialect supports any dialect from the argument
     * collection.
     *
     * @deprecated - [#9882] - 3.14.0 - Use {@link #supportedBy(SQLDialect...)} instead
     */
    @Deprecated
    public final boolean supports(Collection<SQLDialect> other) {
        if (other.contains(family))
            return true;

        SQLDialect candidate = family.predecessor();
        boolean successor = this == family;
        for (;;) {
            successor = successor || this == candidate;
            if (other.contains(candidate))
                return successor;

            if (candidate == (candidate = candidate.predecessor()))
                return false;
        }
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
     * Get access to third party representations of this {@link SQLDialect}.
     */
    public final ThirdParty thirdParty() {
        return thirdParty;
    }

    /**
     * Third party representations of the enclosing {@link SQLDialect}.
     */
    public final class ThirdParty {

        /**
         * "Guess" the JDBC driver from a {@link SQLDialect}.
         *
         * @return The appropriate JDBC driver class or
         *         <code>"java.sql.Driver"</code> if no driver class could be
         *         derived from the URL. Never <code>null</code>.
         */
        public final String driver() {

            // jOOQ build tools need this class without its dependencies, which
            // is why we are using reflection here.
            try {
                Class<?> utils = Class.forName("org.jooq.tools.jdbc.JDBCUtils");
                return (String) utils.getMethod("driver", SQLDialect.class).invoke(utils, SQLDialect.this);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * The Spring DB name or <code>null</code>, if the db name is not
         * supported by Spring.
         * <p>
         * The name returned by this method corresponds to the DB id as
         * referenced in
         * <code>org/springframework/jdbc/support/sql-error-codes.xml</code>
         */
        public final String springDbName() {
            switch (SQLDialect.this.family) {













                case DERBY:       return "Derby";
                case H2:          return "H2";
                case HSQLDB:      return "HSQL";
                case MARIADB:




                case MYSQL:       return "MySQL";
                case POSTGRES:    return "PostgreSQL";

                default:          return null;
            }
        }

        /**
         * The Hibernate dialect name or <code>null</code>, if the dialect is
         * not supported by Hibernate.
         * <p>
         *
         * @see <a href=
         *      "http://docs.jboss.org/hibernate/orm/5.0/javadocs/org/hibernate/dialect/package-summary.html">
         *      http://docs.jboss.org/hibernate/orm/5.0/javadocs/org/hibernate/
         *      dialect/package-summary.html</a>
         */
        public final String hibernateDialect() {
            switch (SQLDialect.this) {































                case CUBRID:        return "org.hibernate.dialect.CUBRIDDialect";
                case DERBY:         return "org.hibernate.dialect.DerbyTenSevenDialect";
                case FIREBIRD:      return "org.hibernate.dialect.FirebirdDialect";
                case H2:            return "org.hibernate.dialect.H2Dialect";
                case HSQLDB:        return "org.hibernate.dialect.HSQLDialect";
                case MARIADB:











                case MYSQL:         return "org.hibernate.dialect.MySQL5Dialect";






                case POSTGRES:      return "org.hibernate.dialect.PostgreSQL94Dialect";
                case SQLITE:        return null;

                default:            return null;
            }
        }
    }
}
