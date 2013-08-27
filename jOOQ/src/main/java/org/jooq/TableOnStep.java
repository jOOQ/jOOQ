/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 * 
 * This work is dual-licensed Open Source, under LGPL and jOOQ EULA
 * =============================================================================
 * You may freely choose which license applies to you. For more information 
 * about licensing, please visit http://www.jooq.org/licenses
 * 
 * LGPL:  
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 * 
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 * 
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details.
 * 
 * You should have received a copy of the jOOQ End User License Agreement
 * along with this library.
 * If not, see http://www.jooq.org/eula
 */
package org.jooq;

import java.util.Collection;

import org.jooq.api.annotation.State;
import org.jooq.api.annotation.Transition;
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
@State
public interface TableOnStep {

    /**
     * Add an <code>ON</code> clause to the <code>JOIN</code>.
     */
    @Support
    @Transition(
        name = "ON",
        args = "Condition+",
        to = "JoinedTable"
    )
    TableOnConditionStep on(Condition... conditions);

    /**
     * Add an <code>ON</code> clause to the <code>JOIN</code>.
     */
    @Support
    @Transition(
        name = "ON",
        args = "Condition",
        to = "JoinedTable"
    )
    TableOnConditionStep on(Field<Boolean> condition);

    /**
     * Add an <code>ON</code> clause to the <code>JOIN</code>.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#condition(String)
     */
    @Support
    TableOnConditionStep on(String sql);

    /**
     * Add an <code>ON</code> clause to the <code>JOIN</code>.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#condition(String, Object...)
     */
    @Support
    TableOnConditionStep on(String sql, Object... bindings);

    /**
     * Add an <code>ON</code> clause to the <code>JOIN</code>.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#condition(String, QueryPart...)
     */
    @Support
    TableOnConditionStep on(String sql, QueryPart... parts);

    /**
     * Join a table with the <code>USING(column [, column...])</code> syntax.
     * <p>
     * If this is not supported by your RDBMS, then jOOQ will try to simulate
     * this behaviour using the information provided in this query.
     */
    @Support
    @Transition(
        name = "USING",
        args = "Field+",
        to = "JoinedTable"
    )
    Table<Record> using(Field<?>... fields);

    /**
     * Join a table with the <code>USING(column [, column...])</code> syntax.
     * <p>
     * If this is not supported by your RDBMS, then jOOQ will try to simulate
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
    TableOnConditionStep onKey() throws DataAccessException;

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
    TableOnConditionStep onKey(TableField<?, ?>... keyFields) throws DataAccessException;

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
    TableOnConditionStep onKey(ForeignKey<?, ?> key);
}
