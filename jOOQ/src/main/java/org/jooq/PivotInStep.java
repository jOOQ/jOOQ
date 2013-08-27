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

import static org.jooq.SQLDialect.ORACLE11G;
import static org.jooq.SQLDialect.ORACLE12C;

import java.util.Collection;

import org.jooq.api.annotation.State;
import org.jooq.api.annotation.Transition;

/**
 * This type is used for the Oracle <code>PIVOT</code> clause DSL API, pivoting
 * {@link Table} objects to new tables.
 *
 * @author Lukas Eder
 */
@State(
    name = "PivotInStep"
)
public interface PivotInStep<T> {

    /**
     * Specify the acceptable values for pivoting
     *
     * @param values The pivoting values
     * @return A new pivoted table
     */
    @Support({ ORACLE11G, ORACLE12C })
    Table<Record> in(T... values);

    /**
     * Specify the acceptable values for pivoting
     * <p>
     * This clause is generally only supported by {@link SQLDialect#ORACLE}.
     * {@link SQLDialect#SQLSERVER} accepts only literals, use
     * {@link #in(Object...)} instead.
     *
     * @param fields The pivoting values
     * @return A new pivoted table
     */
    @Support({ ORACLE11G, ORACLE12C })
    @Transition(
        name = "IN",
        args = "Field+",
        to = "PivotTable"
    )
    Table<Record> in(Field<?>... fields);

    /**
     * Specify the acceptable values for pivoting
     * <p>
     * This clause is generally only supported by {@link SQLDialect#ORACLE}.
     * {@link SQLDialect#SQLSERVER} accepts only literals, use
     * {@link #in(Object...)} instead.
     *
     * @param fields The pivoting values
     * @return A new pivoted table
     */
    @Support({ ORACLE11G, ORACLE12C })
    Table<Record> in(Collection<? extends Field<T>> fields);
}
