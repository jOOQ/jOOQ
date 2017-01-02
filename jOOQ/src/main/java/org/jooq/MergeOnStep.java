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

import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.FIREBIRD_3_0;
import static org.jooq.SQLDialect.HSQLDB;
// ...
// ...
// ...

import org.jooq.impl.DSL;

/**
 * This type is used for the {@link Merge}'s DSL API.
 * <p>
 * Example: <code><pre>
 * DSLContext create = DSL.using(configuration);
 *
 * create.mergeInto(table)
 *       .using(select)
 *       .on(condition)
 *       .whenMatchedThenUpdate()
 *       .set(field1, value1)
 *       .set(field2, value2)
 *       .whenNotMatchedThenInsert(field1, field2)
 *       .values(value1, value2)
 *       .execute();
 * </pre></code>
 *
 * @author Lukas Eder
 */
public interface MergeOnStep<R extends Record> {

    /**
     * Provide join conditions and proceed to the next step, connecting them
     * with each other with {@link Operator#AND}.
     */
    @Support({ CUBRID, FIREBIRD_3_0, HSQLDB })
    MergeOnConditionStep<R> on(Condition... conditions);

    /**
     * Provide join conditions and proceed to the next step
     */
    @Support({ CUBRID, FIREBIRD_3_0, HSQLDB })
    MergeOnConditionStep<R> on(Field<Boolean> condition);

    /**
     * Provide join conditions and proceed to the next step
     *
     * @deprecated - 3.8.0 - [#4763] - Use {@link #on(Condition...)} or
     *             {@link #on(Field)} instead. Due to ambiguity between
     *             calling this method using {@link Field#equals(Object)}
     *             argument, vs. calling the other method via a
     *             {@link Field#equal(Object)} argument, this method will be
     *             removed in the future.
     */
    @Deprecated
    @Support({ CUBRID, FIREBIRD_3_0, HSQLDB })
    MergeOnConditionStep<R> on(Boolean condition);

    /**
     * Provide join conditions and proceed to the next step
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#condition(SQL)
     * @see SQL
     */
    @Support({ CUBRID, FIREBIRD_3_0, HSQLDB })
    @PlainSQL
    MergeOnConditionStep<R> on(SQL sql);

    /**
     * Provide join conditions and proceed to the next step
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#condition(String)
     * @see SQL
     */
    @Support({ CUBRID, FIREBIRD_3_0, HSQLDB })
    @PlainSQL
    MergeOnConditionStep<R> on(String sql);

    /**
     * Provide join conditions and proceed to the next step
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
    @Support({ CUBRID, FIREBIRD_3_0, HSQLDB })
    @PlainSQL
    MergeOnConditionStep<R> on(String sql, Object... bindings);

    /**
     * Provide join conditions and proceed to the next step
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
    @Support({ CUBRID, FIREBIRD_3_0, HSQLDB })
    @PlainSQL
    MergeOnConditionStep<R> on(String sql, QueryPart... parts);
}
