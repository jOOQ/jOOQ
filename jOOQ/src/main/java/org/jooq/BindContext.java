/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
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
     */
    BindContext bind(QueryPart part) throws DataAccessException;

    /**
     * Bind values from several {@link QueryPart}'s. This will also increment
     * the internal counter.
     *
     * @throws DataAccessException If something went wrong while binding a
     *             variable
     */
    BindContext bind(Collection<? extends QueryPart> parts) throws DataAccessException;

    /**
     * Bind values from several {@link QueryPart}'s. This will also increment
     * the internal counter.
     *
     * @throws DataAccessException If something went wrong while binding a
     *             variable
     */
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
