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
import static org.jooq.SQLDialect.POSTGRES_9_5;

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
 *       .onDuplicateKeyUpdate()
 *       .set(field1, value1)
 *       .set(field2, value2)
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
public interface InsertOnConflictConditionStep<R extends Record> extends InsertReturningStep<R> {

    /**
     * Combine the currently assembled conditions with another one using the
     * {@link Operator#AND} operator and proceed to the next step.
     */
    @Support({ POSTGRES_9_5 })
    InsertOnConflictConditionStep<R> and(Condition condition);

    /**
     * Combine the currently assembled conditions with another one using the
     * {@link Operator#AND} operator and proceed to the next step.
     */
    @Support({ POSTGRES_9_5 })
    InsertOnConflictConditionStep<R> and(Field<Boolean> condition);

    /**
     * Combine the currently assembled conditions with another one using the
     * {@link Operator#AND} operator and proceed to the next step.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#condition(SQL)
     * @see SQL
     */
    @Support({ POSTGRES_9_5 })
    @PlainSQL
    InsertOnConflictConditionStep<R> and(SQL sql);

    /**
     * Combine the currently assembled conditions with another one using the
     * {@link Operator#AND} operator and proceed to the next step.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#condition(String)
     * @see SQL
     */
    @Support({ POSTGRES_9_5 })
    @PlainSQL
    InsertOnConflictConditionStep<R> and(String sql);

    /**
     * Combine the currently assembled conditions with another one using the
     * {@link Operator#AND} operator and proceed to the next step.
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
    @Support({ POSTGRES_9_5 })
    @PlainSQL
    InsertOnConflictConditionStep<R> and(String sql, Object... bindings);

    /**
     * Combine the currently assembled conditions with another one using the
     * {@link Operator#AND} operator and proceed to the next step.
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
    @Support({ POSTGRES_9_5 })
    @PlainSQL
    InsertOnConflictConditionStep<R> and(String sql, QueryPart... parts);

    /**
     * Combine the currently assembled conditions with a negated other one using
     * the {@link Operator#AND} operator and proceed to the next step.
     */
    @Support({ POSTGRES_9_5 })
    InsertOnConflictConditionStep<R> andNot(Condition condition);

    /**
     * Combine the currently assembled conditions with a negated other one using
     * the {@link Operator#AND} operator and proceed to the next step.
     */
    @Support({ POSTGRES_9_5 })
    InsertOnConflictConditionStep<R> andNot(Field<Boolean> condition);

    /**
     * Combine the currently assembled conditions with an EXISTS clause using
     * the {@link Operator#AND} operator and proceed to the next step.
     */
    @Support({ POSTGRES_9_5 })
    InsertOnConflictConditionStep<R> andExists(Select<?> select);

    /**
     * Combine the currently assembled conditions with a NOT EXISTS clause using
     * the {@link Operator#AND} operator and proceed to the next step.
     */
    @Support({ POSTGRES_9_5 })
    InsertOnConflictConditionStep<R> andNotExists(Select<?> select);

    /**
     * Combine the currently assembled conditions with another one using the
     * {@link Operator#OR} operator and proceed to the next step.
     */
    @Support({ POSTGRES_9_5 })
    InsertOnConflictConditionStep<R> or(Condition condition);

    /**
     * Combine the currently assembled conditions with another one using the
     * {@link Operator#OR} operator and proceed to the next step.
     */
    @Support({ POSTGRES_9_5 })
    InsertOnConflictConditionStep<R> or(Field<Boolean> condition);

    /**
     * Combine the currently assembled conditions with another one using the
     * {@link Operator#OR} operator and proceed to the next step.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#condition(SQL)
     * @see SQL
     */
    @Support({ POSTGRES_9_5 })
    @PlainSQL
    InsertOnConflictConditionStep<R> or(SQL sql);

    /**
     * Combine the currently assembled conditions with another one using the
     * {@link Operator#OR} operator and proceed to the next step.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#condition(String)
     * @see SQL
     */
    @Support({ POSTGRES_9_5 })
    @PlainSQL
    InsertOnConflictConditionStep<R> or(String sql);

    /**
     * Combine the currently assembled conditions with another one using the
     * {@link Operator#OR} operator and proceed to the next step.
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
    @Support({ POSTGRES_9_5 })
    @PlainSQL
    InsertOnConflictConditionStep<R> or(String sql, Object... bindings);

    /**
     * Combine the currently assembled conditions with another one using the
     * {@link Operator#OR} operator and proceed to the next step.
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
    @Support({ POSTGRES_9_5 })
    @PlainSQL
    InsertOnConflictConditionStep<R> or(String sql, QueryPart... parts);

    /**
     * Combine the currently assembled conditions with a negated other one using
     * the {@link Operator#OR} operator and proceed to the next step.
     */
    @Support({ POSTGRES_9_5 })
    InsertOnConflictConditionStep<R> orNot(Condition condition);

    /**
     * Combine the currently assembled conditions with a negated other one using
     * the {@link Operator#OR} operator and proceed to the next step.
     */
    @Support({ POSTGRES_9_5 })
    InsertOnConflictConditionStep<R> orNot(Field<Boolean> condition);

    /**
     * Combine the currently assembled conditions with an EXISTS clause using
     * the {@link Operator#OR} operator and proceed to the next step.
     */
    @Support({ POSTGRES_9_5 })
    InsertOnConflictConditionStep<R> orExists(Select<?> select);

    /**
     * Combine the currently assembled conditions with a NOT EXISTS clause using
     * the {@link Operator#OR} operator and proceed to the next step.
     */
    @Support({ POSTGRES_9_5 })
    InsertOnConflictConditionStep<R> orNotExists(Select<?> select);
}
