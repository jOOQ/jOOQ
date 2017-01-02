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

import org.jooq.conf.ParamType;
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
     * @deprecated - 3.8.0 - [#4991] In jOOQ 4.0, {@link Param} will be made
     *             immutable. Modifying {@link Param} values is strongly
     *             discouraged.
     */
    @Deprecated
    void setValue(T value);

    /**
     * Sets a converted value, using this {@link Param}'s underlying
     * {@link DataType}, obtained from {@link #getDataType()}
     *
     * @see DataType#convert(Object)
     * @see Convert#convert(Object, Class)
     * @throws DataTypeException If <code>value</code> cannot be converted into
     *             this parameter's data type.
     * @deprecated - 3.8.0 - [#4991] In jOOQ 4.0, {@link Param} will be made
     *             immutable. Modifying {@link Param} values is strongly
     *             discouraged.
     */
    @Deprecated
    void setConverted(Object value) throws DataTypeException;

    /**
     * A flag on the bind value to force it to be inlined in rendered SQL
     *
     * @deprecated - 3.8.0 - [#4991] In jOOQ 4.0, {@link Param} will be made
     *             immutable. Modifying {@link Param} values is strongly
     *             discouraged.
     */
    @Deprecated
    void setInline(boolean inline);

    /**
     * A flag on the bind value to force it to be inlined in rendered SQL
     */
    boolean isInline();

    /**
     * The parameter type.
     */
    ParamType getParamType();

    /**
     * The parameter mode.
     */
    ParamMode getParamMode();
}
