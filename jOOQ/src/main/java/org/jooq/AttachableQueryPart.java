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
 *
 *
 *
 */
package org.jooq;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

import org.jooq.conf.ParamType;
import org.jooq.conf.Settings;
import org.jooq.conf.StatementType;
import org.jooq.impl.DSL;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A type that is both {@link Attachable} and a {@link QueryPart}.
 * <p>
 * In addition to being able to render SQL of possibly some unspecified dialect
 * and with inlined bind values for debugging purposes via the
 * {@link QueryPart#toString()} method, this type offers more fine-grained
 * control over the SQL generation via {@link ParamType},
 * {@link Configuration#dialect()} and various {@link Configuration#settings()}.
 *
 * @author Lukas Eder
 */
public interface AttachableQueryPart extends Attachable, QueryPart {

    /**
     * Retrieve the SQL code rendered by this Query.
     * <p>
     * Use this method, when you want to use jOOQ for object oriented query
     * creation, but execute the query with some other technology, such as
     * <ul>
     * <li>JDBC</li>
     * <li>Spring Templates</li>
     * <li>JPA native queries</li>
     * <li>etc...</li>
     * </ul>
     * <p>
     * Note, this is the same as calling {@link #getSQL(ParamType)}. The
     * parameter will depend on your {@link DSLContext}'s {@link Settings}:
     * <table border="1">
     * <tr>
     * <th><code>StatementType</code></th>
     * <th>boolean parameter</th>
     * <th>effect</th>
     * </tr>
     * <tr>
     * <td>{@link StatementType#PREPARED_STATEMENT}</td>
     * <td><code>false</code> (default)</td>
     * <td>This will render bind variables to be used with a JDBC
     * {@link PreparedStatement}. You can extract bind values from this
     * <code>Query</code> using {@link #getBindValues()}</td>
     * </tr>
     * <tr>
     * <td>{@link StatementType#STATIC_STATEMENT}</td>
     * <td><code>true</code></td>
     * <td>This will inline all bind variables in a statement to be used with a
     * JDBC {@link Statement}</td>
     * </tr>
     * </table>
     * <p>
     * [#1520] Note that the query actually being executed might not contain any
     * bind variables, in case the number of bind variables exceeds your SQL
     * dialect's maximum number of supported bind variables. This is not
     * reflected by this method, which will only use the {@link Settings} to
     * decide whether to render bind values.
     *
     * @see #getSQL(ParamType)
     */
    @NotNull
    String getSQL();

    /**
     * Retrieve the SQL code rendered by this Query.
     * <p>
     * [#1520] Note that the query actually being executed might not contain any
     * bind variables, in case the number of bind variables exceeds your SQL
     * dialect's maximum number of supported bind variables. This is not
     * reflected by this method, which will only use <code>paramType</code>
     * argument to decide whether to render bind values.
     * <p>
     * See {@link #getSQL()} for more details.
     *
     * @param paramType How to render parameters. This overrides values in
     *            {@link Settings#getStatementType()}
     * @return The generated SQL
     */
    @NotNull
    String getSQL(ParamType paramType);

    /**
     * Retrieve the bind values that will be bound by this Query.
     * <p>
     * Unlike {@link #getParams()}, which returns also inlined parameters, this
     * returns only actual bind values that will render an actual bind value as
     * a question mark <code>"?"</code>
     *
     * @see DSLContext#extractBindValues(QueryPart)
     */
    @NotNull
    List<Object> getBindValues();

    /**
     * Get a <code>Map</code> of named parameters. The <code>Map</code> itself
     * cannot be modified, but the {@link Param} elements allow for modifying
     * bind values on an existing {@link Query}.
     * <p>
     * Bind values created with {@link DSL#val(Object)} will have their bind
     * index as name.
     *
     * @see Param
     * @see DSL#param(String, Object)
     * @see DSLContext#extractParams(QueryPart)
     */
    @NotNull
    Map<String, Param<?>> getParams();

    /**
     * Get a named parameter from the {@link Query}, provided its name.
     * <p>
     * Bind values created with {@link DSL#val(Object)} will have their bind
     * index as name.
     *
     * @see Param
     * @see DSL#param(String, Object)
     * @see DSLContext#extractParam(QueryPart, String)
     */
    @Nullable
    Param<?> getParam(String name);

}
