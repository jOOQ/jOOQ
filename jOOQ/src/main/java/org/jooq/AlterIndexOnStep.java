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
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...

/**
 * The step in the <code>ALTER INDEX</code> where the table can be specified for
 * the index.
 *
 * @author Lukas Eder
 */
public interface AlterIndexOnStep extends AlterIndexStep {

    /**
     * Specify the table expression on which to alter an index.
     * <p>
     * {@link SQLDialect#MYSQL}, {@link SQLDialect#MARIADB}, and
     * {@link SQLDialect#SQLSERVER} use table-scoped index names, not
     * schema-scoped names. This means that in these databases, the
     * <code>ON</code> clause is mandatory in order to unambiguously identify an
     * index. In all other databases, the <code>ON</code> clause will simply be
     * ignored for compatibility reasons.
     */
    @Support({ H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    AlterIndexStep on(Table<?> table);

    /**
     * Specify the table expression on which to alter an index.
     * <p>
     * {@link SQLDialect#MYSQL}, {@link SQLDialect#MARIADB}, and
     * {@link SQLDialect#SQLSERVER} use table-scoped index names, not
     * schema-scoped names. This means that in these databases, the
     * <code>ON</code> clause is mandatory in order to unambiguously identify an
     * index. In all other databases, the <code>ON</code> clause will simply be
     * ignored for compatibility reasons.
     */
    @Support({ H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    AlterIndexStep on(String tableName);

    /**
     * Specify the table expression on which to alter an index.
     * <p>
     * {@link SQLDialect#MYSQL}, {@link SQLDialect#MARIADB}, and
     * {@link SQLDialect#SQLSERVER} use table-scoped index names, not
     * schema-scoped names. This means that in these databases, the
     * <code>ON</code> clause is mandatory in order to unambiguously identify an
     * index. In all other databases, the <code>ON</code> clause will simply be
     * ignored for compatibility reasons.
     */
    @Support({ H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    AlterIndexStep on(Name tableName);

}
