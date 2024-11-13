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
 * Apache-2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
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

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;
import java.sql.Timestamp;
import java.util.function.Consumer;

import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultBinding;
import org.jooq.impl.SQLDataType;

import org.jetbrains.annotations.NotNull;

/**
 * An SPI (Service Provider Interface) that exposes all low-level interactions
 * with JDBC bind variables.
 * <p>
 * This SPI is used by jOOQ users to implement support for custom data types
 * that would otherwise not be supported by jOOQ and/or JDBC. All of jOOQ's
 * internal support for bind variable types is implemented in
 * {@link DefaultBinding}.
 * <h3>When <code>Binding</code> is invoked</h3>
 * <p>
 * A <code>Binding</code> is invoked whenever jOOQ interacts with JDBC's various
 * types, including:
 * <ul>
 * <li>{@link #get(BindingGetResultSetContext)}: To read from a JDBC
 * {@link ResultSet}</li>
 * <li>{@link #get(BindingGetStatementContext)}: To read from a JDBC
 * {@link CallableStatement} (<code>OUT</code> parameters).
 * {@link #register(BindingRegisterContext)} may be needed to declare
 * <code>OUT</code> parameters beforehand.</li>
 * <li>{@link #get(BindingGetSQLInputContext)}: To read from a JDBC
 * {@link SQLInput} (UDT attributes)</li>
 * <li>{@link #set(BindingSetStatementContext)}: To write to a JDBC
 * {@link PreparedStatement} ({@link #sql(BindingSQLContext)} may be needed to
 * specify the bind value syntax)</li>
 * <li>{@link #set(BindingSetSQLOutputContext)}: To read from a JDBC
 * {@link SQLOutput} (UDT attributes)</li>
 * </ul>
 * <p>
 * Unlike a {@link Converter}, a <code>Binding</code> applies <em>only</em> to
 * JDBC interactions. It does not influence how nested data structures (such as
 * {@link DSL#multiset(TableLike)} or {@link DSL#row(SelectField...)}) are
 * mapped to Java objects, for example.
 * <p>
 * <h3>Creating user defined {@link DataType}s</h3>
 * <p>
 * jOOQ provides built in data types through {@link SQLDataType}. Users can
 * define their own data types programmatically by calling
 * {@link DataType#asConvertedDataType(Converter)} or
 * {@link DataType#asConvertedDataType(Binding)}, for example. Custom data types
 * can also be defined on generated code using the
 * <code>&lt;forcedType/&gt;</code> configuration, see <a href=
 * "https://www.jooq.org/doc/latest/manual/code-generation/codegen-advanced/codegen-config-database/codegen-database-forced-types/">the
 * manual for more details</a>.
 * <p>
 * <a href=
 * "https://www.jooq.org/doc/latest/manual/sql-execution/fetching/ad-hoc-converter/">Ad-hoc
 * converters</a> allow for attaching a converter directly to a SQL expression
 * in order to keep related logic close together, see the {@link Converter}
 * Javadoc for more details.
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
     * <p>
     * While the {@link Converter} property of a binding is not optional
     * ({@link Converter#fromType()} and {@link Converter#toType()} are needed
     * by jOOQ's internals), the conversion implementation (i.e.
     * {@link Converter#from(Object)} and {@link Converter#to(Object)}) isn't
     * strictly required if implementations don't rely on it. If these
     * conversion implementations are non-functional, it is advised to hint at
     * this fact in {@link Converter#fromSupported()} and
     * {@link Converter#toSupported()}.
     */
    @NotNull
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

    /**
     * Construct a binding from functions.
     */
    @NotNull
    static <T, U> Binding<T, U> of(
        Converter<T, U>                                 converter,
        Consumer<? super BindingSQLContext<U>>          sqlContext,
        Consumer<? super BindingGetResultSetContext<U>> getResultSetContext,
        Consumer<? super BindingSetStatementContext<U>> setStatementContext
    ) {
        return of(
            converter,
            sqlContext,
            getResultSetContext,
            setStatementContext,
            ctx -> { throw new UnsupportedOperationException(); },
            ctx -> { throw new UnsupportedOperationException(); },
            ctx -> { throw new UnsupportedOperationException(); },
            ctx -> { throw new UnsupportedOperationException(); }
        );
    }

    /**
     * Construct a binding from functions.
     */
    @NotNull
    static <T, U> Binding<T, U> of(
        Converter<T, U>                                 converter,
        Consumer<? super BindingSQLContext<U>>          sqlContext,
        Consumer<? super BindingGetResultSetContext<U>> getResultSetContext,
        Consumer<? super BindingSetStatementContext<U>> setStatementContext,
        Consumer<? super BindingRegisterContext<U>>     registerContext,
        Consumer<? super BindingGetStatementContext<U>> getStatementContext
    ) {
        return of(
            converter,
            sqlContext,
            getResultSetContext,
            setStatementContext,
            registerContext,
            getStatementContext,
            ctx -> { throw new UnsupportedOperationException(); },
            ctx -> { throw new UnsupportedOperationException(); }
        );
    }

    /**
     * Construct a binding from functions.
     */
    @NotNull
    static <T, U> Binding<T, U> of(
        Converter<T, U>                                 converter,
        Consumer<? super BindingSQLContext<U>>          sqlContext,
        Consumer<? super BindingGetResultSetContext<U>> getResultSetContext,
        Consumer<? super BindingSetStatementContext<U>> setStatementContext,
        Consumer<? super BindingRegisterContext<U>>     registerContext,
        Consumer<? super BindingGetStatementContext<U>> getStatementContext,
        Consumer<? super BindingGetSQLInputContext<U>>  getSqlInputContext,
        Consumer<? super BindingSetSQLOutputContext<U>> setSqlOutputContext
    ) {
        return new Binding<T, U>() {

            @Override
            public final Converter<T, U> converter() {
                return converter;
            }

            @Override
            public final void sql(BindingSQLContext<U> ctx) throws SQLException {
                sqlContext.accept(ctx);
            }

            @Override
            public final void register(BindingRegisterContext<U> ctx) throws SQLException {
                registerContext.accept(ctx);
            }

            @Override
            public final void set(BindingSetStatementContext<U> ctx) throws SQLException {
                setStatementContext.accept(ctx);
            }

            @Override
            public final void set(BindingSetSQLOutputContext<U> ctx) throws SQLException {
                setSqlOutputContext.accept(ctx);
            }

            @Override
            public final void get(BindingGetResultSetContext<U> ctx) throws SQLException {
                getResultSetContext.accept(ctx);
            }

            @Override
            public final void get(BindingGetStatementContext<U> ctx) throws SQLException {
                getStatementContext.accept(ctx);
            }

            @Override
            public final void get(BindingGetSQLInputContext<U> ctx) throws SQLException {
                getSqlInputContext.accept(ctx);
            }
        };
    }
}
