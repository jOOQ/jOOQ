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

import static org.jooq.SQLDialect.ORACLE;

import java.util.Collection;

import org.jooq.api.annotation.State;
import org.jooq.api.annotation.Transition;

/**
 * An intermediate type for the construction of a partitioned
 * {@link SQLDialect#ORACLE} <code>OUTER JOIN</code> clause.
 * <p>
 * This step allows for adding Oracle-specific <code>PARTITION BY</code> clauses
 * to the right of an <code>OUTER JOIN</code> keyword. See the Oracle
 * documentation for more details here: <a href=
 * "http://docs.oracle.com/cd/B28359_01/server.111/b28286/queries006.htm#i2054062"
 * >http://docs.oracle.com/cd/B28359_01/server.111/b28286/queries006.htm#
 * i2054062</a>
 *
 * @author Lukas Eder
 */
@State
public interface TablePartitionByStep extends TableOnStep {

    /**
     * Add a <code>PARTITION BY</code> clause to the right hand side of the
     * <code>OUTER JOIN</code> keywords
     */
    @Support(ORACLE)
    @Transition(
        name = "PARTITION BY",
        args = "Field+"
    )
    TableOnStep partitionBy(Field<?>... fields);

    /**
     * Add a <code>PARTITION BY</code> clause to the right hand side of the
     * <code>OUTER JOIN</code> keywords
     */
    @Support(ORACLE)
    @Transition(
        name = "PARTITION BY",
        args = "Field+"
    )
    TableOnStep partitionBy(Collection<? extends Field<?>> fields);
}
