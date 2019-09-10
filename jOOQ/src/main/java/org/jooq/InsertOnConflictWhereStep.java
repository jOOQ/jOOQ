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
import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.MARIADB;
// ...
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.SQLITE;
// ...

import java.util.Collection;

import org.jooq.impl.DSL;

/**
 * This type is used for the {@link Insert}'s DSL API.
 * <p>
 * Example: <code><pre>
 * DSLContext create = DSL.using(configuration);
 *
 * create.insertInto(table, field1, field2)
 *       .values(value1, value2)
 *       .values(value3, value4)
 *       .onConflict(field1)
 *       .doUpdate()
 *       .set(field2, value2)
 *       .where(field3.eq(value5))
 *       .execute();
 * </pre></code>
 * <p>
 * <h3>Referencing <code>XYZ*Step</code> types directly from client code</h3>
 * <p>
 * It is usually not recommended to reference any <code>XYZ*Step</code> types
 * directly from client code, or assign them to local variables. When writing
 * dynamic SQL, creating a statement's components dynamically, and passing them
 * to the DSL API statically is usually a better choice. See the manual's
 * section about dynamic SQL for details: <a href=
 * "https://www.jooq.org/doc/latest/manual/sql-building/dynamic-sql">https://www.jooq.org/doc/latest/manual/sql-building/dynamic-sql</a>.
 * <p>
 * Drawbacks of referencing the <code>XYZ*Step</code> types directly:
 * <ul>
 * <li>They're operating on mutable implementations (as of jOOQ 3.x)</li>
 * <li>They're less composable and not easy to get right when dynamic SQL gets
 * complex</li>
 * <li>They're less readable</li>
 * <li>They might have binary incompatible changes between minor releases</li>
 * </ul>
 *
 * @author Lukas Eder
 */
public interface InsertOnConflictWhereStep<R extends Record> extends InsertReturningStep<R> {

    /**
     * Add a <code>WHERE</code> clause to the <code>INSERT</code> statement's
     * <code>ON DUPLICATE KEY UPDATE</code> or <code>ON CONFLICT ... DO UPDATE</code> clause.
     */
    @Support({ CUBRID, DERBY, H2, MARIADB, POSTGRES, SQLITE })
    InsertOnConflictConditionStep<R> where(Condition condition);

    /**
     * Add a <code>WHERE</code> clause to the <code>INSERT</code> statement's
     * <code>ON DUPLICATE KEY UPDATE</code> or <code>ON CONFLICT ... DO UPDATE</code> clause,
     * connecting them with each other using {@link Operator#AND}.
     */
    @Support({ CUBRID, DERBY, H2, MARIADB, POSTGRES, SQLITE })
    InsertOnConflictConditionStep<R> where(Condition... conditions);

    /**
     * Add a <code>WHERE</code> clause to the <code>INSERT</code> statement's
     * <code>ON DUPLICATE KEY UPDATE</code> or <code>ON CONFLICT ... DO UPDATE</code> clause,
     * connecting them with each other using {@link Operator#AND}.
     */
    @Support({ CUBRID, DERBY, H2, MARIADB, POSTGRES, SQLITE })
    InsertOnConflictConditionStep<R> where(Collection<? extends Condition> conditions);

    /**
     * Add a <code>WHERE</code> clause to the <code>INSERT</code> statement's
     * <code>ON DUPLICATE KEY UPDATE</code> or <code>ON CONFLICT ... DO UPDATE</code> clause.
     */
    @Support({ CUBRID, DERBY, H2, MARIADB, POSTGRES, SQLITE })
    InsertOnConflictConditionStep<R> where(Field<Boolean> field);

    /**
     * Add a <code>WHERE</code> clause to the <code>INSERT</code> statement's
     * <code>ON DUPLICATE KEY UPDATE</code> or <code>ON CONFLICT ... DO UPDATE</code> clause.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#condition(SQL)
     * @see SQL
     */
    @Support({ CUBRID, DERBY, H2, MARIADB, POSTGRES, SQLITE })
    @PlainSQL
    InsertOnConflictConditionStep<R> where(SQL sql);

    /**
     * Add a <code>WHERE</code> clause to the <code>INSERT</code> statement's
     * <code>ON DUPLICATE KEY UPDATE</code> or <code>ON CONFLICT ... DO UPDATE</code> clause.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#condition(String)
     * @see SQL
     */
    @Support({ CUBRID, DERBY, H2, MARIADB, POSTGRES, SQLITE })
    @PlainSQL
    InsertOnConflictConditionStep<R> where(String sql);

    /**
     * Add a <code>WHERE</code> clause to the <code>INSERT</code> statement's
     * <code>ON DUPLICATE KEY UPDATE</code> or <code>ON CONFLICT ... DO UPDATE</code> clause.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#condition(String, Object...)
     * @see DSL#sql(String, Object...)
     * @see SQL
     */
    @Support({ CUBRID, DERBY, H2, MARIADB, POSTGRES, SQLITE })
    @PlainSQL
    InsertOnConflictConditionStep<R> where(String sql, Object... bindings);

    /**
     * Add a <code>WHERE</code> clause to the <code>INSERT</code> statement's
     * <code>ON DUPLICATE KEY UPDATE</code> or <code>ON CONFLICT ... DO UPDATE</code> clause.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#condition(String, QueryPart...)
     * @see DSL#sql(String, QueryPart...)
     * @see SQL
     */
    @Support({ CUBRID, DERBY, H2, MARIADB, POSTGRES, SQLITE })
    @PlainSQL
    InsertOnConflictConditionStep<R> where(String sql, QueryPart... parts);

    /**
     * Add a <code>WHERE EXISTS</code> clause to the <code>INSERT</code> statement's
     * <code>ON DUPLICATE KEY UPDATE</code> or <code>ON CONFLICT ... DO UPDATE</code> clause.
     */
    @Support({ CUBRID, DERBY, POSTGRES, SQLITE })
    InsertOnConflictConditionStep<R> whereExists(Select<?> select);

    /**
     * Add a <code>WHERE NOT EXISTS</code> clause to the <code>INSERT</code> statement's
     * <code>ON DUPLICATE KEY UPDATE</code> or <code>ON CONFLICT ... DO UPDATE</code> clause.
     */
    @Support({ CUBRID, DERBY, POSTGRES, SQLITE })
    InsertOnConflictConditionStep<R> whereNotExists(Select<?> select);
}
