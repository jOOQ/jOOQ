/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is triple-licensed under ASL 2.0, AGPL 3.0, and jOOQ EULA
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   ASL 2.0 or jOOQ EULA.
 * - If you're using this work with at least one commercial database, you may
 *   choose AGPL 3.0 or jOOQ EULA.
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
 * AGPL 3.0
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 *
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details: http://www.jooq.org/eula
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
