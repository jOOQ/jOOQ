/*
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
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: http://www.jooq.org/licenses
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
package org.jooq;

import java.sql.PreparedStatement;
import java.util.Collection;

import org.jooq.exception.DataAccessException;

/**
 * The bind context is used for binding {@link QueryPart}'s and their contained
 * values to a {@link PreparedStatement}'s bind variables. A new bind context is
 * instantiated every time a {@link Query} is bound. <code>QueryPart</code>'s
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
    @Override
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
     * @deprecated - 3.4.0 - [#3114] - Use {@link #bindValue(Object, Field)} instead
     */
    @Deprecated
    BindContext bindValue(Object value, Class<?> type) throws DataAccessException;

    /**
     * Bind several values. This will also increment the internal counter.
     *
     * @throws DataAccessException If something went wrong while binding a
     *             variable
     * @deprecated - 3.4.0 - [#3114] - Use {@link #bindValue(Object, Field)} instead
     */
    @Deprecated
    BindContext bindValues(Object... values) throws DataAccessException;

    /**
     * Bind a value using a specific type. This will also increment the internal
     * counter.
     *
     * @throws DataAccessException If something went wrong while binding a
     *             variable
     */
    @Override
    BindContext bindValue(Object value, Field<?> field) throws DataAccessException;

}
