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
public interface WindowRowsStep<T> extends WindowFinalStep<T> {

    /**
     * Add a <code>ROWS UNBOUNDED PRECEDING</code> frame clause to the window
     * function.
     */
    @Support({ DB2, POSTGRES, ORACLE, SQLSERVER2012, SYBASE })
    @Transition(
        name = "ROWS UNBOUNDED PRECEDING"
    )
    WindowFinalStep<T> rowsUnboundedPreceding();

    /**
     * Add a <code>ROWS [number] PRECEDING</code> frame clause to the window
     * function.
     */
    @Support({ DB2, POSTGRES, ORACLE, SQLSERVER2012, SYBASE })
    @Transition(
        name = "ROWS PRECEDING",
        args = "Integer"
    )
    WindowFinalStep<T> rowsPreceding(int number);

    /**
     * Add a <code>ROWS CURRENT ROW</code> frame clause to the window function.
     */
    @Support({ DB2, POSTGRES, ORACLE, SQLSERVER2012, SYBASE })
    @Transition(
        name = "ROWS CURRENT ROW"
    )
    WindowFinalStep<T> rowsCurrentRow();

    /**
     * Add a <code>ROWS UNBOUNDED FOLLOWING</code> frame clause to the window
     * function.
     */
    @Support({ DB2, POSTGRES, ORACLE, SQLSERVER2012, SYBASE })
    @Transition(
        name = "ROWS UNBOUNDED FOLLOWING"
    )
    WindowFinalStep<T> rowsUnboundedFollowing();

    /**
     * Add a <code>ROWS [number] FOLLOWING</code> frame clause to the window
     * function.
     */
    @Support({ DB2, POSTGRES, ORACLE, SQLSERVER2012, SYBASE })
    @Transition(
        name = "ROWS FOLLOWING",
        args = "Integer"
    )
    WindowFinalStep<T> rowsFollowing(int number);

    /**
     * Add a <code>ROWS BETWEEN UNBOUNDED PRECEDING ...</code> frame clause to
     * the window function.
     */
    @Support({ DB2, POSTGRES, ORACLE, SQLSERVER2012, SYBASE })
    @Transition(
        name = "ROWS BETWEEN UNBOUNDED PRECEDING"
    )
    WindowRowsAndStep<T> rowsBetweenUnboundedPreceding();

    /**
     * Add a <code>ROWS BETWEEN [number] PRECEDING ...</code> frame clause to
     * the window function.
     */
    @Support({ DB2, POSTGRES, ORACLE, SQLSERVER2012, SYBASE })
    @Transition(
        name = "ROWS BETWEEN PRECEDING",
        args = "Integer"
    )
    WindowRowsAndStep<T> rowsBetweenPreceding(int number);

    /**
     * Add a <code>ROWS BETWEEN CURRENT ROW ...</code> frame clause to
     * the window function.
     */
    @Support({ DB2, POSTGRES, ORACLE, SQLSERVER2012, SYBASE })
    @Transition(
        name = "ROWS BETWEEN CURRENT ROW"
    )
    WindowRowsAndStep<T> rowsBetweenCurrentRow();

    /**
     * Add a <code>ROWS BETWEEN UNBOUNDED FOLLOWING ...</code> frame clause to
     * the window function.
     */
    @Support({ DB2, POSTGRES, ORACLE, SQLSERVER2012, SYBASE })
    @Transition(
        name = "ROWS BETWEEN UNBOUNDED FOLLOWING"
    )
    WindowRowsAndStep<T> rowsBetweenUnboundedFollowing();

    /**
     * Add a <code>ROWS BETWEEN [number] FOLLOWING ...</code> frame clause to
     * the window function.
     */
    @Support({ DB2, POSTGRES, ORACLE, SQLSERVER2012, SYBASE })
    @Transition(
        name = "ROWS BETWEEN FOLLOWING",
        args = "Integer"
    )
    WindowRowsAndStep<T> rowsBetweenFollowing(int number);
}
