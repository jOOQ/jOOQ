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
 * A definition for a UDT
 * <p>
 * This extends {@link PackageDefinition} because Oracle internally models UDT's
 * in similar ways as packages. This is especially true for the way, member
 * procedures and functions are called.
 *
 * @author Lukas Eder
 */
public interface UDTDefinition extends PackageDefinition {

    /**
     * All attributes in the UDT
     */
    List<AttributeDefinition> getAttributes();

    /**
     * Get an attribute in this UDT by its name
     */
    AttributeDefinition getAttribute(String attributeName);

    /**
     * Get an attribute in this UDT by its index (starting at 0)
     */
    AttributeDefinition getAttribute(int attributeIndex);

    /**
     * All routines in the UDT
     */
    @Override
    List<RoutineDefinition> getRoutines();
}
