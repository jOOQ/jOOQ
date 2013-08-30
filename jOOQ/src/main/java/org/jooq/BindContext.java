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

import java.sql.PreparedStatement;
import java.util.Collection;

import org.jooq.exception.DataAccessException;

/**
 * The bind context is used for binding {@link QueryPart}'s and their contained
 * values to a {@link PreparedStatement}'s bind variables. A new bind context is
 * instanciated every time a {@link Query} is bound. <code>QueryPart</code>'s
 * will then pass the same context to their components
 * <p>
 * This interface is for JOOQ INTERNAL USE only. Do not reference directly
 *
 * @author Lukas Eder
 * @see RenderContext
 */
public interface BindContext extends Context<BindContext> {

    /**
     * Retrieve the context's underlying {@link PreparedStatement}
     */
    PreparedStatement statement();

    /**
     * Bind values from a {@link QueryPart}. This will also increment the
     * internal counter.
     *
     * @throws DataAccessException If something went wrong while binding a
     *             variable
     * @deprecated - 3.2.0 - [#2666] - Use {@link #visit(QueryPart)} instead
     */
    @Deprecated
    BindContext bind(QueryPart part) throws DataAccessException;

    /**
     * Bind values from several {@link QueryPart}'s. This will also increment
     * the internal counter.
     *
     * @throws DataAccessException If something went wrong while binding a
     *             variable
     * @deprecated - 3.2.0 - [#2666] - Use {@link #visit(QueryPart)} instead
     */
    @Deprecated
    BindContext bind(Collection<? extends QueryPart> parts) throws DataAccessException;

    /**
     * Bind values from several {@link QueryPart}'s. This will also increment
     * the internal counter.
     *
     * @throws DataAccessException If something went wrong while binding a
     *             variable
     * @deprecated - 3.2.0 - [#2666] - Use {@link #visit(QueryPart)} instead
     */
    @Deprecated
    BindContext bind(QueryPart[] parts) throws DataAccessException;

    /**
     * Bind a value using a specific type. This will also increment the internal
     * counter.
     *
     * @throws DataAccessException If something went wrong while binding a
     *             variable
     */
    BindContext bindValue(Object value, Class<?> type) throws DataAccessException;

    /**
     * Bind several values. This will also increment the internal counter.
     *
     * @throws DataAccessException If something went wrong while binding a
     *             variable
     */
    BindContext bindValues(Object... values) throws DataAccessException;
}
