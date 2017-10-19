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

import org.jooq.impl.DSL;

/**
 * A {@link Query} that can create tables.
 *
 * @author Lukas Eder
 */
public interface CreateTableStorageStep extends CreateTableFinalStep {

    /**
     * Add vendor-specific storage clauses to the <code>CREATE TABLE</code> statement.
     * <p>
     * Storage clauses will always be appended to the <em>end</em> of everything
     * else that jOOQ renders, including possibly other storage clauses, such as
     * {@link CreateTableOnCommitStep#onCommitDeleteRows()} or similar clauses.
     * If custom storage clauses should be mixed with jOOQ-provided storage
     * clauses, it is recommended not to use the jOOQ API and use the custom
     * clause API for all storage clauses instead.
     * <p>
     * Storage clauses will be separated from previous elements by a separator
     * (whitespace or newline) to ensure syntactic integrity.
     * <p>
     * Example usage:
     * <p>
     * <code><pre>
     * DSL.using(configuration)
     *    .createTable("t")
     *    .column(field("i", SQLDataType.INTEGER))
     *    .storage("TABLESPACE my_tablespace")
     *    .execute();
     * </pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses! One way to escape
     * literals is to use {@link DSL#name(String...)} and similar methods
     *
     * @param sql The SQL
     */
    @Support
    @PlainSQL
    CreateTableFinalStep storage(SQL sql);

    /**
     * Add vendor-specific storage clauses to the <code>CREATE TABLE</code> statement.
     * <p>
     * Storage clauses will always be appended to the <em>end</em> of everything
     * else that jOOQ renders, including possibly other storage clauses, such as
     * {@link CreateTableOnCommitStep#onCommitDeleteRows()} or similar clauses.
     * If custom storage clauses should be mixed with jOOQ-provided storage
     * clauses, it is recommended not to use the jOOQ API and use the custom
     * clause API for all storage clauses instead.
     * <p>
     * Storage clauses will be separated from previous elements by a separator
     * (whitespace or newline) to ensure syntactic integrity.
     * <p>
     * Example usage:
     * <p>
     * <code><pre>
     * DSL.using(configuration)
     *    .createTable("t")
     *    .column(field("i", SQLDataType.INTEGER))
     *    .storage("TABLESPACE my_tablespace")
     *    .execute();
     * </pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses! One way to escape
     * literals is to use {@link DSL#name(String...)} and similar methods
     *
     * @param sql The SQL
     */
    @Support
    @PlainSQL
    CreateTableFinalStep storage(String sql);

    /**
     * Add vendor-specific storage clauses to the <code>CREATE TABLE</code> statement.
     * <p>
     * Storage clauses will always be appended to the <em>end</em> of everything
     * else that jOOQ renders, including possibly other storage clauses, such as
     * {@link CreateTableOnCommitStep#onCommitDeleteRows()} or similar clauses.
     * If custom storage clauses should be mixed with jOOQ-provided storage
     * clauses, it is recommended not to use the jOOQ API and use the custom
     * clause API for all storage clauses instead.
     * <p>
     * Storage clauses will be separated from previous elements by a separator
     * (whitespace or newline) to ensure syntactic integrity.
     * <p>
     * Example usage:
     * <p>
     * <code><pre>
     * DSL.using(configuration)
     *    .createTable("t")
     *    .column(field("i", SQLDataType.INTEGER))
     *    .storage("TABLESPACE my_tablespace")
     *    .execute();
     * </pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses! One way to escape
     * literals is to use {@link DSL#name(String...)} and similar methods
     *
     * @param sql The SQL
     * @param bindings The bindings
     */
    @Support
    @PlainSQL
    CreateTableFinalStep storage(String sql, Object... bindings);

    /**
     * Add vendor-specific storage clauses to the <code>CREATE TABLE</code>
     * statement.
     * <p>
     * Storage clauses will always be appended to the <em>end</em> of everything
     * else that jOOQ renders, including possibly other storage clauses, such as
     * {@link CreateTableOnCommitStep#onCommitDeleteRows()} or similar clauses.
     * If custom storage clauses should be mixed with jOOQ-provided storage
     * clauses, it is recommended not to use the jOOQ API and use the custom
     * clause API for all storage clauses instead.
     * <p>
     * Storage clauses will be separated from previous elements by a separator
     * (whitespace or newline) to ensure syntactic integrity.
     * <p>
     * Example usage:
     * <p>
     * <code><pre>
     * DSL.using(configuration)
     *    .createTable("t")
     *    .column(field("i", SQLDataType.INTEGER))
     *    .storage("TABLESPACE my_tablespace")
     *    .execute();
     * </pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses! One way to escape
     * literals is to use {@link DSL#name(String...)} and similar methods
     *
     * @param sql The SQL
     * @param parts The {@link QueryPart} objects that are rendered at the
     *            {numbered placeholder} locations
     */
    @Support
    @PlainSQL
    CreateTableFinalStep storage(String sql, QueryPart... parts);
}
