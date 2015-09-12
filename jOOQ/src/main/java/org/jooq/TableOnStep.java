/**
 * Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)
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

import java.util.Collection;

import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;

/**
 * An intermediate type for the construction of a <code>JOIN</code> clause,
 * where there must be a join criteria added using an <code>ON</code> clause
 * (with a {@link Condition}), or using a <code>USING</code> clause (with a list
 * of {@link Field}).
 *
 * @author Lukas Eder
 */
public interface TableOnStep<R extends Record> {

    /**
     * Add an <code>ON</code> clause to the <code>JOIN</code>, connecting them
     * with each other with {@link Operator#AND}.
     */
    @Support
    TableOnConditionStep<R> on(Condition... conditions);

    /**
     * Add an <code>ON</code> clause to the <code>JOIN</code>.
     */
    @Support
    TableOnConditionStep<R> on(Field<Boolean> condition);

    /**
     * Add an <code>ON</code> clause to the <code>JOIN</code>.
     */
    @Support
    TableOnConditionStep<R> on(Boolean condition);

    /**
     * Add an <code>ON</code> clause to the <code>JOIN</code>.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#condition(String)
     * @see SQL
     */
    @Support
    @PlainSQL
    TableOnConditionStep<R> on(String sql);

    /**
     * Add an <code>ON</code> clause to the <code>JOIN</code>.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#condition(String, Object...)
     * @see SQL
     */
    @Support
    @PlainSQL
    TableOnConditionStep<R> on(String sql, Object... bindings);

    /**
     * Add an <code>ON</code> clause to the <code>JOIN</code>.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#condition(String, QueryPart...)
     * @see SQL
     */
    @Support
    @PlainSQL
    TableOnConditionStep<R> on(String sql, QueryPart... parts);

    /**
     * Join a table with the <code>USING(column [, column...])</code> syntax.
     * <p>
     * If this is not supported by your RDBMS, then jOOQ will try to emulate
     * this behaviour using the information provided in this query.
     */
    @Support
    Table<Record> using(Field<?>... fields);

    /**
     * Join a table with the <code>USING(column [, column...])</code> syntax.
     * <p>
     * If this is not supported by your RDBMS, then jOOQ will try to emulate
     * this behaviour using the information provided in this query.
     */
    @Support
    Table<Record> using(Collection<? extends Field<?>> fields);

    /**
     * Join the table on a non-ambiguous foreign key relationship between the
     * two joined tables.
     * <p>
     * See {@link #onKey(ForeignKey)} for examples.
     *
     * @see #onKey(ForeignKey)
     * @throws DataAccessException If there is no non-ambiguous key definition
     *             known to jOOQ
     */
    @Support
    TableOnConditionStep<R> onKey() throws DataAccessException;

    /**
     * Join the table on a non-ambiguous foreign key relationship between the
     * two joined tables.
     * <p>
     * See {@link #onKey(ForeignKey)} for examples.
     *
     * @see #onKey(ForeignKey)
     * @throws DataAccessException If there is no non-ambiguous key definition
     *             known to jOOQ
     */
    @Support
    TableOnConditionStep<R> onKey(TableField<?, ?>... keyFields) throws DataAccessException;

    /**
     * Join the table on a non-ambiguous foreign key relationship between the
     * two joined tables.
     * <p>
     * An example: <code><pre>
     * // There is a single foreign key relationship between A and B and it can
     * // be obtained by A.getReferencesTo(B) or vice versa. The order of A and
     * // B is not important
     * A.join(B).onKey();
     *
     * // There are several foreign key relationships between A and B. In order
     * // to disambiguate, you can provide a formal org.jooq.Key reference from
     * // the generated Keys class
     * A.join(B).onKey(key);
     *
     * // There are several foreign key relationships between A and B. In order
     * // to disambiguate, you can provide any non-ambiguous foreign key column
     * A.join(B).onKey(B.A_ID);
     * </pre></code>
     */
    @Support
    TableOnConditionStep<R> onKey(ForeignKey<?, ?> key);
}
