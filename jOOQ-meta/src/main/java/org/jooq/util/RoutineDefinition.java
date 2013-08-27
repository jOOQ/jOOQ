/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 * 
 * This work is dual-licensed Open Source, under AGPL and jOOQ EULA
 * =============================================================================
 * You may freely choose which license applies to you. For more information 
 * about licensing, please visit http://www.jooq.org/licenses
 *
 * AGPL:  
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public 
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 * 
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it and/or
 * modify it.
 * 
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details.
 * 
 * You should have received a copy of the jOOQ End User License Agreement
 * along with this library.
 * If not, see http://www.jooq.org/eula
 */

package org.jooq.util;

import java.util.List;

/**
 * An interface defining a stored routine in a database.
 *
 * @author Lukas Eder
 */
public interface RoutineDefinition extends Definition {

    /**
     * @return The routine's package. <code>null</code> if the routine is not in
     *         a package
     */
    PackageDefinition getPackage();

    /**
     * A list of IN or INOUT parameter column definitions
     */
    List<ParameterDefinition> getInParameters();

    /**
     * A list of OUT or INOUT parameter column definitions
     */
    List<ParameterDefinition> getOutParameters();

    /**
     * A list of all IN, OUT, and INOUT parameter column definitions
     */
    List<ParameterDefinition> getAllParameters();

    /**
     * @return The return value column definition
     */
    ParameterDefinition getReturnValue();

    /**
     * @return The return value simple Java type
     */
    DataTypeDefinition getReturnType();

    /**
     * @return Whether this routine can be used in SQL (a function without OUT
     *         parameters)
     */
    boolean isSQLUsable();

    /**
     * @return Whether this routine is an aggregate function
     */
    boolean isAggregate();

}
