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

import static org.jooq.SQLDialect.DB2;
import static org.jooq.SQLDialect.ORACLE;
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.SQLSERVER2012;
import static org.jooq.SQLDialect.SYBASE;

import org.jooq.api.annotation.State;
import org.jooq.api.annotation.Transition;

/**
 * This type is used for the window function DSL API.
 * <p>
 * Example: <code><pre>
 * field.firstValue()
 *      .ignoreNulls()
 *      .over()
 *      .partitionBy(AUTHOR_ID)
 *      .orderBy(PUBLISHED_IN.asc())
 *      .rowsBetweenUnboundedPreceding()
 *      .andUnboundedFollowing()
 * </pre></code>
 *
 * @param <T> The function return type
 * @author Lukas Eder
 */
@State
public interface WindowRowsAndStep<T> {

    /**
     * Add a <code>... AND UNBOUNDED PRECEDING</code> frame clause to the window
     * function.
     */
    @Support({ DB2, POSTGRES, ORACLE, SQLSERVER2012, SYBASE })
    @Transition(
        name = "AND UNBOUNDED PRECEDING"
    )
    WindowFinalStep<T> andUnboundedPreceding();

    /**
     * Add a <code>... AND [number] PRECEDING</code> frame clause to the window
     * function.
     */
    @Support({ DB2, POSTGRES, ORACLE, SQLSERVER2012, SYBASE })
    @Transition(
        name = "AND PRECEDING",
        args = "Integer"
    )
    WindowFinalStep<T> andPreceding(int number);

    /**
     * Add a <code>... AND CURRENT ROW</code> frame clause to the window
     * function.
     */
    @Support({ DB2, POSTGRES, ORACLE, SQLSERVER2012, SYBASE })
    @Transition(
        name = "AND CURRENT ROW"
    )
    WindowFinalStep<T> andCurrentRow();

    /**
     * Add a <code>... AND UNBOUNDED FOLLOWING</code> frame clause to the window
     * function.
     */
    @Support({ DB2, POSTGRES, ORACLE, SQLSERVER2012, SYBASE })
    @Transition(
        name = "AND UNBOUNDED FOLLOWING"
    )
    WindowFinalStep<T> andUnboundedFollowing();

    /**
     * Add a <code>... AND [number] FOLLOWING</code> frame clause to the window
     * function.
     */
    @Support({ DB2, POSTGRES, ORACLE, SQLSERVER2012, SYBASE })
    @Transition(
        name = "AND FOLLOWING",
        args = "Integer"
    )
    WindowFinalStep<T> andFollowing(int number);
}
