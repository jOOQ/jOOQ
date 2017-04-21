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
package org.jooq;

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
import static org.jooq.SQLDialect.MYSQL;
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...
// ...

import org.jooq.impl.DSL;

/**
 * An intermediate type for the construction of a partitioned
 * {@link SQLDialect#ORACLE} <code>OUTER JOIN</code> clause.
 * <p>
 * This step allows for adding Oracle-specific <code>PARTITION BY</code> clauses
 * to the left of an <code>OUTER JOIN</code> keyword. See the Oracle
 * documentation for more details here: <a href=
 * "https://docs.oracle.com/database/121/SQLRF/statements_10002.htm#BABBCHJA"
 * >https://docs.oracle.com/database/121/SQLRF/statements_10002.htm#BABBCHJA</a>
 *
 * @author Lukas Eder
 */
public interface TableOuterJoinStep<R extends Record> {

    /**
     * Join a table to this table using a {@link JoinType}.
     * <p>
     * Only {@link JoinType#LEFT_OUTER_JOIN}, {@link JoinType#RIGHT_OUTER_JOIN},
     * and {@link JoinType#FULL_OUTER_JOIN} are allowed.
     */
    @Support
    TableOnStep<Record> join(TableLike<?> table, JoinType type);

    /**
     * <code>LEFT OUTER JOIN</code> a table to this table.
     * <p>
     * A synonym for {@link #leftOuterJoin(TableLike)}.
     *
     * @see #leftOuterJoin(TableLike)
     */
    @Support
    TableOnStep<Record> leftJoin(TableLike<?> table);

    /**
     * <code>LEFT OUTER JOIN</code> a table to this table.
     * <p>
     * A synonym for {@link #leftOuterJoin(String)}.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(SQL)
     * @see #leftOuterJoin(SQL)
     * @see SQL
     */
    @Support
    @PlainSQL
    TableOnStep<Record> leftJoin(SQL sql);

    /**
     * <code>LEFT OUTER JOIN</code> a table to this table.
     * <p>
     * A synonym for {@link #leftOuterJoin(String)}.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String)
     * @see #leftOuterJoin(String)
     * @see SQL
     */
    @Support
    @PlainSQL
    TableOnStep<Record> leftJoin(String sql);

    /**
     * <code>LEFT OUTER JOIN</code> a table to this table.
     * <p>
     * A synonym for {@link #leftOuterJoin(String, Object...)}.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, Object...)
     * @see DSL#sql(String, Object...)
     * @see #leftOuterJoin(String, Object...)
     * @see SQL
     */
    @Support
    @PlainSQL
    TableOnStep<Record> leftJoin(String sql, Object... bindings);

    /**
     * <code>LEFT OUTER JOIN</code> a table to this table.
     * <p>
     * A synonym for {@link #leftOuterJoin(String, QueryPart...)}.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, QueryPart...)
     * @see DSL#sql(String, QueryPart...)
     * @see #leftOuterJoin(String, QueryPart...)
     * @see SQL
     */
    @Support
    @PlainSQL
    TableOnStep<Record> leftJoin(String sql, QueryPart... parts);

    /**
     * <code>LEFT OUTER JOIN</code> a table to this table.
     * <p>
     * A synonym for {@link #leftOuterJoin(Name)}.
     *
     * @see DSL#table(Name)
     * @see #leftOuterJoin(Name)
     */
    @Support
    TableOnStep<Record> leftJoin(Name name);

    /**
     * <code>LEFT OUTER JOIN</code> a table to this table.
     */
    @Support
    TableOnStep<Record> leftOuterJoin(TableLike<?> table);

    /**
     * <code>LEFT OUTER JOIN</code> a table to this table.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(SQL)
     * @see SQL
     */
    @Support
    @PlainSQL
    TableOnStep<Record> leftOuterJoin(SQL sql);

    /**
     * <code>LEFT OUTER JOIN</code> a table to this table.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String)
     * @see SQL
     */
    @Support
    @PlainSQL
    TableOnStep<Record> leftOuterJoin(String sql);

    /**
     * <code>LEFT OUTER JOIN</code> a table to this table.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, Object...)
     * @see DSL#sql(String, Object...)
     * @see SQL
     */
    @Support
    @PlainSQL
    TableOnStep<Record> leftOuterJoin(String sql, Object... bindings);

    /**
     * <code>LEFT OUTER JOIN</code> a table to this table.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, QueryPart...)
     * @see DSL#sql(String, QueryPart...)
     * @see SQL
     */
    @Support
    @PlainSQL
    TableOnStep<Record> leftOuterJoin(String sql, QueryPart... parts);

    /**
     * <code>LEFT OUTER JOIN</code> a table to this table.
     *
     * @see DSL#table(Name)
     * @see SQL
     */
    @Support
    TableOnStep<Record> leftOuterJoin(Name name);

    /**
     * <code>RIGHT OUTER JOIN</code> a table to this table.
     * <p>
     * A synonym for {@link #rightOuterJoin(TableLike)}.
     * <p>
     * This is only possible where the underlying RDBMS supports it.
     *
     * @see #rightOuterJoin(TableLike)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    TableOnStep<Record> rightJoin(TableLike<?> table);

    /**
     * <code>RIGHT OUTER JOIN</code> a table to this table.
     * <p>
     * A synonym for {@link #rightOuterJoin(String)}.
     * <p>
     * This is only possible where the underlying RDBMS supports it.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(SQL)
     * @see #rightOuterJoin(SQL)
     * @see SQL
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    @PlainSQL
    TableOnStep<Record> rightJoin(SQL sql);

    /**
     * <code>RIGHT OUTER JOIN</code> a table to this table.
     * <p>
     * A synonym for {@link #rightOuterJoin(String)}.
     * <p>
     * This is only possible where the underlying RDBMS supports it.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String)
     * @see #rightOuterJoin(String)
     * @see SQL
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    @PlainSQL
    TableOnStep<Record> rightJoin(String sql);

    /**
     * <code>RIGHT OUTER JOIN</code> a table to this table.
     * <p>
     * A synonym for {@link #rightOuterJoin(String, Object...)}.
     * <p>
     * This is only possible where the underlying RDBMS supports it.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, Object...)
     * @see DSL#sql(String, Object...)
     * @see #rightOuterJoin(String, Object...)
     * @see SQL
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    @PlainSQL
    TableOnStep<Record> rightJoin(String sql, Object... bindings);

    /**
     * <code>RIGHT OUTER JOIN</code> a table to this table.
     * <p>
     * A synonym for {@link #rightOuterJoin(String, QueryPart...)}.
     * <p>
     * This is only possible where the underlying RDBMS supports it
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, QueryPart...)
     * @see DSL#sql(String, QueryPart...)
     * @see #rightOuterJoin(String, QueryPart...)
     * @see SQL
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    @PlainSQL
    TableOnStep<Record> rightJoin(String sql, QueryPart... parts);

    /**
     * <code>RIGHT OUTER JOIN</code> a table to this table.
     * <p>
     * A synonym for {@link #rightOuterJoin(Name)}.
     * <p>
     * This is only possible where the underlying RDBMS supports it
     *
     * @see DSL#table(Name)
     * @see #rightOuterJoin(Name)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    TableOnStep<Record> rightJoin(Name name);

    /**
     * <code>RIGHT OUTER JOIN</code> a table to this table.
     * <p>
     * This is only possible where the underlying RDBMS supports it
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    TableOnStep<Record> rightOuterJoin(TableLike<?> table);

    /**
     * <code>RIGHT OUTER JOIN</code> a table to this table.
     * <p>
     * This is only possible where the underlying RDBMS supports it
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(SQL)
     * @see SQL
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    @PlainSQL
    TableOnStep<Record> rightOuterJoin(SQL sql);

    /**
     * <code>RIGHT OUTER JOIN</code> a table to this table.
     * <p>
     * This is only possible where the underlying RDBMS supports it
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String)
     * @see SQL
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    @PlainSQL
    TableOnStep<Record> rightOuterJoin(String sql);

    /**
     * <code>RIGHT OUTER JOIN</code> a table to this table.
     * <p>
     * This is only possible where the underlying RDBMS supports it
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, Object...)
     * @see DSL#sql(String, Object...)
     * @see SQL
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    @PlainSQL
    TableOnStep<Record> rightOuterJoin(String sql, Object... bindings);

    /**
     * <code>RIGHT OUTER JOIN</code> a table to this table.
     * <p>
     * This is only possible where the underlying RDBMS supports it
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, QueryPart...)
     * @see DSL#sql(String, QueryPart...)
     * @see SQL
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    @PlainSQL
    TableOnStep<Record> rightOuterJoin(String sql, QueryPart... parts);

    /**
     * <code>RIGHT OUTER JOIN</code> a table to this table.
     * <p>
     * This is only possible where the underlying RDBMS supports it
     *
     * @see DSL#table(Name)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    TableOnStep<Record> rightOuterJoin(Name name);

    /**
     * <code>FULL OUTER JOIN</code> a table to this table.
     * <p>
     * A synonym for {@link #fullOuterJoin(TableLike)}.
     */
    @Support({ FIREBIRD, HSQLDB, POSTGRES })
    TableOnStep<Record> fullJoin(TableLike<?> table);

    /**
     * <code>FULL OUTER JOIN</code> a table to this table.
     * <p>
     * A synonym for {@link #fullOuterJoin(SQL)}.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     */
    @Support({ FIREBIRD, HSQLDB, POSTGRES })
    @PlainSQL
    TableOnStep<Record> fullJoin(SQL sql);

    /**
     * <code>FULL OUTER JOIN</code> a table to this table.
     * <p>
     * A synonym for {@link #fullOuterJoin(String)}.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     */
    @Support({ FIREBIRD, HSQLDB, POSTGRES })
    @PlainSQL
    TableOnStep<Record> fullJoin(String sql);

    /**
     * <code>FULL OUTER JOIN</code> a table to this table.
     * <p>
     * A synonym for {@link #fullOuterJoin(String, Object...)}.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     */
    @Support({ FIREBIRD, HSQLDB, POSTGRES })
    @PlainSQL
    TableOnStep<Record> fullJoin(String sql, Object... bindings);

    /**
     * <code>FULL OUTER JOIN</code> a table to this table.
     * <p>
     * A synonym for {@link #fullOuterJoin(String, QueryPart...)}.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     */
    @Support({ FIREBIRD, HSQLDB, POSTGRES })
    @PlainSQL
    TableOnStep<Record> fullJoin(String sql, QueryPart... parts);

    /**
     * <code>FULL OUTER JOIN</code> a table to this table.
     * <p>
     * A synonym for {@link #fullOuterJoin(Name)}.
     */
    @Support({ FIREBIRD, HSQLDB, POSTGRES })
    TableOnStep<Record> fullJoin(Name name);

    /**
     * <code>FULL OUTER JOIN</code> a table to this table.
     * <p>
     * This is only possible where the underlying RDBMS supports it
     */
    @Support({ FIREBIRD, HSQLDB, POSTGRES })
    TableOnStep<Record> fullOuterJoin(TableLike<?> table);

    /**
     * <code>FULL OUTER JOIN</code> a table to this table.
     * <p>
     * This is only possible where the underlying RDBMS supports it
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(SQL)
     * @see SQL
     */
    @Support({ FIREBIRD, HSQLDB, POSTGRES })
    @PlainSQL
    TableOnStep<Record> fullOuterJoin(SQL sql);

    /**
     * <code>FULL OUTER JOIN</code> a table to this table.
     * <p>
     * This is only possible where the underlying RDBMS supports it
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String)
     * @see SQL
     */
    @Support({ FIREBIRD, HSQLDB, POSTGRES })
    @PlainSQL
    TableOnStep<Record> fullOuterJoin(String sql);

    /**
     * <code>FULL OUTER JOIN</code> a table to this table.
     * <p>
     * This is only possible where the underlying RDBMS supports it
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, Object...)
     * @see DSL#sql(String, Object...)
     * @see SQL
     */
    @Support({ FIREBIRD, HSQLDB, POSTGRES })
    @PlainSQL
    TableOnStep<Record> fullOuterJoin(String sql, Object... bindings);

    /**
     * <code>FULL OUTER JOIN</code> a table to this table.
     * <p>
     * This is only possible where the underlying RDBMS supports it
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, QueryPart...)
     * @see DSL#sql(String, QueryPart...)
     * @see SQL
     */
    @Support({ FIREBIRD, HSQLDB, POSTGRES })
    @PlainSQL
    TableOnStep<Record> fullOuterJoin(String sql, QueryPart... parts);

    /**
     * <code>FULL OUTER JOIN</code> a table to this table.
     * <p>
     * This is only possible where the underlying RDBMS supports it
     *
     * @see DSL#table(Name)
     */
    @Support({ FIREBIRD, HSQLDB, POSTGRES })
    TableOnStep<Record> fullOuterJoin(Name name);
}
