/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
 *
 * For more information, please visit: https://www.jooq.org/legal/licensing
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
 *
 *
 *
 */
package org.jooq;

import org.jooq.conf.ParamType;
import org.jooq.conf.Settings;
import org.jooq.exception.DataTypeException;
import org.jooq.impl.DSL;
import org.jooq.impl.QOM;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Experimental;

/**
 * A named parameter and/or bind value.
 * <p>
 * A lot of jOOQ API accepts user input values, such as for example when
 * creating a {@link Condition} using {@link Field#eq(Object)}, where a column
 * expression is being compared with a value.
 * <p>
 * Behind the scenes, jOOQ wraps the value in a bind value expression using
 * {@link DSL#val(Object)}. The generated SQL of such an expression depends on
 * things like {@link Settings#getStatementType()} or {@link ParamType} being
 * passed to configurations or {@link Query#getSQL(ParamType)} calls, etc. By
 * default, a parameter marker <code>?</code> is generated.
 * <p>
 * Users can create parameters explicitly using {@link DSL} API, which is useful
 * in a few cases where the value cannot be passed to jOOQ directly, e.g.
 * <ul>
 * <li>When the value is at the left hand side of an operator</li>
 * <li>When {@link Field} references and {@link Param} values are mixed</li>
 * </ul>
 * <p>
 * <strong>Example:</strong>
 * <p>
 * <pre><code>
 * // Assuming import static org.jooq.impl.DSL.*;
 *
 * // The bind value is the first operand of an expression, in case of which it
 * // needs to be wrapped in a Param explicitly
 * using(configuration)
 *    .select()
 *    .from(RENTALS)
 *    .where(val(LocalDateTime.now()).between(RENTALS.RENTAL_DATE).and(RENTALS.DUE_DATE))
 *    .fetch();
 *
 * // The bind value is mixed with other types of Field expressions in a statement
 * using(configuration)
 *    .insertInto(ACTOR)
 *    .columns(ACTOR.FIRST_NAME, ACTOR.LAST_NAME, ACTOR.LAST_UPDATE)
 *    .values(val("John"), val("Doe"), currentTimestamp())
 *    .execute();
 * </code></pre>
 * <p>
 * Instances can be created using {@link DSL#param(String, Object)},
 * {@link DSL#val(Object)}, {@link DSL#inline(Object)} and respective overloads.
 *
 * @author Lukas Eder
 * @see DSL#param(String, Object)
 */
public interface Param<T> extends ParamOrVariable<T> {

    /**
     * The parameter name. This name is useful for two things:
     * <ul>
     * <li>Named parameters in frameworks that support them, such as Spring's
     * <code>JdbcTemplate</code></li>
     * <li>Accessing the parameter from the {@link Query} API, with
     * {@link Query#getParam(String)}, {@link Query#getParams()}</li>
     * </ul>
     */
    @Nullable
    String getParamName();

    /**
     * Get the parameter's underlying value. This returns <code>null</code> if
     * no value has been set yet.
     */
    @Nullable
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
    @Deprecated(forRemoval = true, since = "3.8")
    void setValue(T value);

    /**
     * Sets a converted value, using this {@link Param}'s underlying
     * {@link DataType}, obtained from {@link #getDataType()}
     *
     * @throws DataTypeException If <code>value</code> cannot be converted into
     *             this parameter's data type.
     * @deprecated - 3.8.0 - [#4991] In jOOQ 4.0, {@link Param} will be made
     *             immutable. Modifying {@link Param} values is strongly
     *             discouraged.
     */
    @Deprecated(forRemoval = true, since = "3.8")
    void setConverted(Object value) throws DataTypeException;

    /**
     * A flag on the bind value to force it to be inlined in rendered SQL
     *
     * @deprecated - 3.8.0 - [#4991] In jOOQ 4.0, {@link Param} will be made
     *             immutable. Modifying {@link Param} values is strongly
     *             discouraged.
     */
    @Deprecated(forRemoval = true, since = "3.8")
    void setInline(boolean inline);

    /**
     * A flag on the bind value to force it to be inlined in rendered SQL.
     * <p>
     * Please note that despite this flag returning <code>false</code>, jOOQ
     * internals may decide to inline a {@link Param} at their own discretion,
     * e.g. because of a limitation in the dialect's syntactic capabilities
     * (e.g. some functions may require literals as arguments), or of the JDBC
     * driver.
     */
    boolean isInline();

    /**
     * The parameter type.
     */
    @NotNull
    ParamType getParamType();

    /**
     * The parameter mode.
     */
    @NotNull
    ParamMode getParamMode();

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    /**
     * Experimental query object model accessor method, see also {@link QOM}.
     * Subject to change in future jOOQ versions, use at your own risk.
     */
    @Experimental
    T $value();

    /**
     * Experimental query object model accessor method, see also {@link QOM}.
     * Subject to change in future jOOQ versions, use at your own risk.
     */
    @Experimental
    @NotNull Param<T> $value(T value);

    /**
     * Experimental query object model accessor method, see also {@link QOM}.
     * Subject to change in future jOOQ versions, use at your own risk.
     */
    @Experimental
    boolean $inline();

    /**
     * Experimental query object model accessor method, see also {@link QOM}.
     * Subject to change in future jOOQ versions, use at your own risk.
     */
    @Experimental
    @NotNull Param<T> $inline(boolean inline);

}
