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
    void addConditions(Collection<? extends Condition> conditions);

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
    void addConditions(Operator operator, Collection<? extends Condition> conditions);

}
