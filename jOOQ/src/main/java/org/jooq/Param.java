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

import org.jooq.exception.DataTypeException;
import org.jooq.impl.DSL;
import org.jooq.tools.Convert;

/**
 * A named parameter and/or bind value.
 *
 * @author Lukas Eder
 * @see DSL#param(String, Object)
 */
public interface Param<T> extends Field<T> {

    /**
     * {@inheritDoc}
     * <hr/>
     * The <code>Param</code>'s value for {@link #getName()} coincides with
     * {@link #getParamName()}
     */
    @Override
    String getName();

    /**
     * The parameter name. This name is useful for two things:
     * <ul>
     * <li>Named parameters in frameworks that support them, such as Spring's
     * <code>JdbcTemplate</code></li>
     * <li>Accessing the parameter from the {@link Query} API, with
     * {@link Query#getParam(String)}, {@link Query#getParams()}</li>
     * </ul>
     */
    String getParamName();

    /**
     * Get the parameter's underlying value. This returns <code>null</code> if
     * no value has been set yet.
     */
    T getValue();

    /**
     * Set the parameter's underlying value. This is the same as
     * {@link #setConverted(Object)}, but ensures generic type-safety.
     *
     * @see #setConverted(Object)
     */
    void setValue(T value);

    /**
     * Sets a converted value, using this {@link Param}'s underlying
     * {@link DataType}, obtained from {@link #getDataType()}
     *
     * @see DataType#convert(Object)
     * @see Convert#convert(Object, Class)
     * @throws DataTypeException If <code>value</code> cannot be converted into
     *             this parameter's data type.
     */
    void setConverted(Object value) throws DataTypeException;

    /**
     * A flag on the bind value to force it to be inlined in rendered SQL
     */
    void setInline(boolean inline);

    /**
     * A flag on the bind value to force it to be inlined in rendered SQL
     */
    boolean isInline();
}
