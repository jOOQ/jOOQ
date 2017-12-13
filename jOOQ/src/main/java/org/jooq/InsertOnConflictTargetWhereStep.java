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

import org.jooq.impl.DSL;

import java.util.Collection;

import static org.jooq.SQLDialect.POSTGRES_9_5;

/**
 * This type is used for the {@link Insert}'s DSL API.
 * <p>
 * Example: <code><pre>
 * DSLContext create = DSL.using(configuration);
 *
 * create.insertInto(table, field1, field2)
 *       .values(value1, value2)
 *       .values(value3, value4)
 *       .onConflict(field1, collation, opclass)
 *       .whereIndex("field < 10")
 *       .set(field1, value1)
 *       .set(field2, value2)
 *       .execute();
 * </pre></code>
 *
 * @author Timur Shaidullin
 */
public interface InsertOnConflictTargetWhereStep<R extends Record> {

    /**
     * Add a <code>WHERE</code> clause to specify a partial index, connecting
     * them with each other with {@link Operator#AND}.
     */
    @Support({ POSTGRES_9_5 })
    InsertOnConflictDoUpdateStep whereIndex(Condition condition);

    /**
     * Add a <code>WHERE</code> clause to specify a partial index, connecting
     * them with each other with {@link Operator#AND}.
     */
    @Support({ POSTGRES_9_5 })
    InsertOnConflictDoUpdateStep whereIndex(Condition... conditions);

    /**
     * Add a <code>WHERE</code> clause to specify a partial index, connecting
     * them with each other with {@link Operator#AND}.
     */
    @Support({ POSTGRES_9_5 })
    InsertOnConflictDoUpdateStep whereIndex(Collection<? extends Condition> conditions);

    /**
     * Add a <code>WHERE</code> clause to specify a partial index.
     */
    @Support({ POSTGRES_9_5 })
    InsertOnConflictDoUpdateStep whereIndex(Field<Boolean> field);

    /**
     * Add a <code>WHERE</code> clause to specify a partial index.
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
    InsertOnConflictDoUpdateStep whereIndex(SQL sql);

    /**
     * Add a <code>WHERE</code> clause to specify a partial index.
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
    InsertOnConflictDoUpdateStep whereIndex(String sql);

    /**
     * Add a <code>WHERE</code> clause to specify a partial index.
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
    InsertOnConflictDoUpdateStep whereIndex(String sql, Object... bindings);

    /**
     * Add a <code>WHERE</code> clause to specify a partial index.
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
    InsertOnConflictDoUpdateStep whereIndex(String sql, QueryPart... parts);
}
