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

/**
 * A common interface for all objects holding conditions (e.g. queries)
 *
 * @author Lukas Eder
 * @deprecated - 2.6.0 [#1881] - This type will be removed from the public API,
 *             soon. Its methods will be pushed down into extending interfaces.
 *             Do not reference this type directly.
 */
@Deprecated
public interface ConditionProvider {

    /**
     * Adds new conditions to the query, connecting them to existing
     * conditions with {@link Operator#AND}
     *
     * @param conditions The condition
     */
    @Support
    void addConditions(Condition... conditions);

    /**
     * Adds new conditions to the query, connecting them to existing
     * conditions with {@link Operator#AND}
     *
     * @param conditions The condition
     */
    @Support
    void addConditions(Collection<Condition> conditions);

    /**
     * Adds new conditions to the query, connecting them to existing
     * conditions with the provided operator
     *
     * @param conditions The condition
     */
    @Support
    void addConditions(Operator operator, Condition... conditions);

    /**
     * Adds new conditions to the query, connecting them to existing
     * conditions with the provided operator
     *
     * @param conditions The condition
     */
    @Support
    void addConditions(Operator operator, Collection<Condition> conditions);

}
