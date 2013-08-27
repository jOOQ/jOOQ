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
