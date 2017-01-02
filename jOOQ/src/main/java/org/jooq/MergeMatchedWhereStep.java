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
public interface MergeMatchedWhereStep<R extends Record> extends MergeNotMatchedStep<R> {

    /**
     * Add an additional <code>WHERE</code> clause to the preceding
     * <code>WHEN MATCHED THEN UPDATE</code> clause.
     * <p>
     * <b>Note:</b> This syntax is only available for the
     * {@link SQLDialect#CUBRID} and {@link SQLDialect#ORACLE} databases!
     * <p>
     * See <a href=
     * "http://docs.oracle.com/cd/B19306_01/server.102/b14200/statements_9016.htm"
     * >http://docs.oracle.com/cd/B19306_01/server.102/b14200/statements_9016.
     * htm</a> for a full definition of the Oracle <code>MERGE</code> statement
     */
    @Support({ CUBRID })
    MergeMatchedDeleteStep<R> where(Condition condition);

    /**
     * Add an additional <code>WHERE</code> clause to the preceding
     * <code>WHEN MATCHED THEN UPDATE</code> clause.
     * <p>
     * <b>Note:</b> This syntax is only available for the
     * {@link SQLDialect#CUBRID} and {@link SQLDialect#ORACLE} databases!
     * <p>
     * See <a href=
     * "http://docs.oracle.com/cd/B19306_01/server.102/b14200/statements_9016.htm"
     * >http://docs.oracle.com/cd/B19306_01/server.102/b14200/statements_9016.
     * htm</a> for a full definition of the Oracle <code>MERGE</code> statement
     */
    @Support({ CUBRID })
    MergeMatchedDeleteStep<R> where(Field<Boolean> condition);

    /**
     * Add an additional <code>WHERE</code> clause to the preceding
     * <code>WHEN MATCHED THEN UPDATE</code> clause.
     * <p>
     * <b>Note:</b> This syntax is only available for the
     * {@link SQLDialect#CUBRID} and {@link SQLDialect#ORACLE} databases!
     * <p>
     * See <a href=
     * "http://docs.oracle.com/cd/B19306_01/server.102/b14200/statements_9016.htm"
     * >http://docs.oracle.com/cd/B19306_01/server.102/b14200/statements_9016.
     * htm</a> for a full definition of the Oracle <code>MERGE</code> statement
     *
     * @deprecated - 3.8.0 - [#4763] - Use {@link #where(Condition)} or
     *             {@link #where(Field)} instead. Due to ambiguity between
     *             calling this method using {@link Field#equals(Object)}
     *             argument, vs. calling the other method via a
     *             {@link Field#equal(Object)} argument, this method will be
     *             removed in the future.
     */
    @Deprecated
    @Support({ CUBRID })
    MergeMatchedDeleteStep<R> where(Boolean condition);
}
