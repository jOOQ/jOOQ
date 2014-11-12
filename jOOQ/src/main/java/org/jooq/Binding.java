/**
 * Copyright (c) 2009-2014, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */
package org.jooq;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;
import java.sql.Timestamp;

import org.jooq.exception.DataAccessException;
import org.jooq.impl.DefaultBinding;
import org.jooq.impl.SQLDataType;

/**
 * An SPI (Service Provider Interface) that exposes all low-level interactions
 * with JDBC bind variables.
 * <p>
 * This SPI is used by jOOQ users to implement support for custom data types
 * that would otherwise not be supported by jOOQ and/or JDBC. All of jOOQ's
 * internal support for bind variable types is implemented in
 * {@link DefaultBinding}.
 *
 * @param <T> The database type - i.e. any type available from
 *            {@link SQLDataType}
 * @param <U> The user type
 * @author Lukas Eder
 */
public interface Binding<T, U> extends Serializable {
	
    /**
     * A converter that can convert between the database type and the custom
     * type.
     */
    Converter<T, U> converter();

    /**
     * Generate SQL code for the bind variable.
     * <p>
     * Implementations should generate SQL code onto
     * {@link BindingSQLContext#render()}, given the context's bind variable
     * located at {@link BindingSQLContext#value()}. Examples of such SQL code
     * are:
     * <ul>
     * <li><code>"?"</code>: Default implementations can simply generate a
     * question mark.<br>
     * <br>
     * </li>
     * <li><code>"123"</code>: Implementations may choose to inline bind
     * variables to influence execution plan generation.<br>
     * <br>
     * {@link RenderContext#paramType()} contains information whether inlined
     * bind variables are expected in the current context.<br>
     * <br>
     * </li>
     * <li><code>"CAST(? AS DATE)"</code>: Cast a database to a more specific
     * type. This can be useful in databases like Oracle, which map both
     * <code>DATE</code> and <code>TIMESTAMP</code> SQL types to
     * {@link Timestamp}.<br>
     * <br>
     * {@link RenderContext#castMode()} may contain some hints about whether
     * casting is suggested in the current context.<br>
     * <br>
     * </li>
     * <li><code>"?::json"</code>: Vendor-specific bind variables can be
     * supported, e.g. {@link SQLDialect#POSTGRES}'s JSON data type.</li>
     * </ul>
     * <p>
     * Implementations must provide consistent behaviour between
     * {@link #sql(BindingSQLContext)} and
     * {@link #set(BindingSetStatementContext)}, i.e. when bind variables are
     * inlined, then they must not be bound to the {@link PreparedStatement} in
     * {@link #set(BindingSetStatementContext)}
     *
     * @param ctx The context object containing all argument objects.
     * @throws SQLException Implementations are allowed to pass on all
     *             {@link SQLException}s to the caller to be wrapped in
     *             {@link DataAccessException}s.
     */
    void sql(BindingSQLContext<U> ctx) throws SQLException;

    /**
     * Register a {@link CallableStatement}'s <code>OUT</code> parameter.
     *
     * @param ctx The context object containing all argument objects.
     * @throws SQLException Implementations are allowed to pass on all
     *             {@link SQLException}s to the caller to be wrapped in
     *             {@link DataAccessException}s.
     */
    void register(BindingRegisterContext<U> ctx) throws SQLException;

    /**
     * Set a {@link PreparedStatement}'s <code>IN</code> parameter.
     *
     * @param ctx The context object containing all argument objects.
     * @throws SQLException Implementations are allowed to pass on all
     *             {@link SQLException}s to the caller to be wrapped in
     *             {@link DataAccessException}s.
     */
    void set(BindingSetStatementContext<U> ctx) throws SQLException;

    /**
     * Set a {@link SQLOutput}'s <code>IN</code> parameter.
     *
     * @param ctx The context object containing all argument objects.
     * @throws SQLException Implementations are allowed to pass on all
     *             {@link SQLException}s to the caller to be wrapped in
     *             {@link DataAccessException}s.
     */
    void set(BindingSetSQLOutputContext<U> ctx) throws SQLException;

    /**
     * Get a {@link ResultSet}'s <code>OUT</code> value.
     * <p>
     * Implementations are expected to produce a value by calling
     * {@link BindingGetResultSetContext#value(Object)}, passing the resulting
     * value to the method.
     *
     * @param ctx The context object containing all argument objects.
     * @throws SQLException Implementations are allowed to pass on all
     *             {@link SQLException}s to the caller to be wrapped in
     *             {@link DataAccessException}s.
     */
    void get(BindingGetResultSetContext<U> ctx) throws SQLException;

    /**
     * Get a {@link CallableStatement}'s <code>OUT</code> value.
     * <p>
     * Implementations are expected to produce a value by calling
     * {@link BindingGetStatementContext#value(Object)}, passing the resulting
     * value to the method.
     *
     * @param ctx The context object containing all argument objects.
     * @throws SQLException Implementations are allowed to pass on all
     *             {@link SQLException}s to the caller to be wrapped in
     *             {@link DataAccessException}s.
     */
    void get(BindingGetStatementContext<U> ctx) throws SQLException;

    /**
     * Get a {@link SQLInput}'s <code>OUT</code> value.
     * <p>
     * Implementations are expected to produce a value by calling
     * {@link BindingGetSQLInputContext#value(Object)}, passing the resulting
     * value to the method.
     *
     * @param ctx The context object containing all argument objects.
     * @throws SQLException Implementations are allowed to pass on all
     *             {@link SQLException}s to the caller to be wrapped in
     *             {@link DataAccessException}s.
     */
    void get(BindingGetSQLInputContext<U> ctx) throws SQLException;
}
