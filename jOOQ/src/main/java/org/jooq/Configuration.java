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

import java.io.Serializable;
import java.sql.Connection;
import java.sql.Savepoint;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;

import javax.sql.DataSource;

import org.jooq.conf.Settings;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConnectionProvider;
import org.jooq.impl.DefaultExecuteListenerProvider;
import org.jooq.impl.DefaultExecutorProvider;
import org.jooq.impl.DefaultRecordListenerProvider;
import org.jooq.impl.DefaultRecordMapper;
import org.jooq.impl.DefaultRecordMapperProvider;
import org.jooq.impl.DefaultTransactionListenerProvider;
import org.jooq.impl.DefaultTransactionProvider;
import org.jooq.impl.DefaultVisitListenerProvider;
import org.jooq.tools.LoggerListener;
import org.jooq.tools.StopWatchListener;

/**
 * A <code>Configuration</code> configures a {@link DSLContext}, providing it
 * with information for query rendering and execution.
 * <p>
 * A <code>Configuration</code> wraps all information elements that are
 * needed...
 * <ul>
 * <li>by a {@link RenderContext} to render {@link Query} objects and
 * {@link QueryPart}s</li>
 * <li>by a {@link BindContext} to bind values to {@link Query} objects and
 * {@link QueryPart}s</li>
 * <li>by a {@link Query} or {@link Routine} object to execute themselves</li>
 * </ul>
 * <p>
 * The simplest usage of a <code>Configuration</code> instance is to use it
 * exactly for a single <code>Query</code> execution, disposing it immediately.
 * This will make it very simple to implement thread-safe behaviour.
 * <p>
 * At the same time, jOOQ does not require <code>Configuration</code> instances
 * to be that short-lived. Thread-safety will then be delegated to component
 * objects, such as the {@link ConnectionProvider}, the {@link ExecuteListener}
 * list, etc.
 * <p>
 * A <code>Configuration</code> is composed of types composing its state and of
 * SPIs:
 * <h3>Types composing its state:</h3>
 * <ul>
 * <li>{@link #dialect()}: The {@link SQLDialect} that defines the underlying
 * database's behaviour when generating SQL syntax, or binding variables, or
 * when executing the query</li>
 * <li>{@link #settings()}: The {@link Settings} that define general jOOQ
 * behaviour</li>
 * <li>{@link #data()}: A {@link Map} containing user-defined data for the
 * {@link Scope} of this configuration.</li>
 * </ul>
 * <h3>SPIs:</h3>
 * <ul>
 * <li>{@link #connectionProvider()}: The {@link ConnectionProvider} that
 * defines the semantics of {@link ConnectionProvider#acquire()} and
 * {@link ConnectionProvider#release(Connection)} for all queries executed in
 * the context of this <code>Configuration</code>. <br/>
 * <br/>
 * jOOQ-provided default implementations include:
 * <ul>
 * <li>{@link DefaultConnectionProvider}: a non-thread-safe implementation that
 * wraps a single JDBC {@link Connection}. Ideal for batch processing.</li>
 * <li>{@link DataSourceConnectionProvider}: a possibly thread-safe
 * implementation that wraps a JDBC {@link DataSource}. Ideal for use with
 * connection pools, Java EE, or Spring.</li>
 * </ul>
 * </li>
 * <li>{@link #transactionProvider()}: The {@link TransactionProvider} that
 * defines and implements the behaviour of the
 * {@link DSLContext#transaction(TransactionalRunnable)} and
 * {@link DSLContext#transactionResult(TransactionalCallable)} methods.<br/>
 * <br/>
 * jOOQ-provided default implementations include:
 * <ul>
 * <li>{@link DefaultTransactionProvider}: an implementation backed by JDBC
 * directly, via {@link Connection#commit()}, {@link Connection#rollback()}, and
 * {@link Connection#rollback(Savepoint)} for nested transactions.</li>
 * </ul>
 * </li>
 * <li>{@link #recordMapperProvider()}: The {@link RecordMapperProvider} that
 * defines and implements the behaviour of {@link Record#into(Class)},
 * {@link ResultQuery#fetchInto(Class)}, {@link Cursor#fetchInto(Class)}, and
 * various related methods. <br/>
 * <br/>
 * jOOQ-provided default implementations include:
 * <ul>
 * <li>{@link DefaultRecordMapperProvider}: an implementation delegating to the
 * {@link DefaultRecordMapper}, which implements the most common mapping
 * use-cases.</li>
 * </ul>
 * </li>
 * <li>{@link #recordListenerProviders()}: A set of
 * {@link RecordListenerProvider} that implement {@link Record} fetching and
 * storing lifecycle management, specifically for use with
 * {@link UpdatableRecord}.<br/>
 * <br/>
 * jOOQ does not provide any implementations.</li>
 * <li>{@link #executeListenerProviders()}: A set of
 * {@link ExecuteListenerProvider} that implement {@link Query} execution
 * lifecycle management.<br/>
 * <br/>
 * jOOQ-provided example implementations include:
 * <ul>
 * <li>{@link LoggerListener}: generating default query execution log output</li>
 * <li>{@link StopWatchListener}: generating default query execution speed log
 * output</li>
 * </ul>
 * <li>{@link #visitListenerProviders()}: A set of {@link VisitListenerProvider}
 * that implement {@link Query} rendering and variable binding lifecycle
 * management, and that are allowed to implement query transformation - e.g. to
 * implement row-level security, or multi-tenancy.<br/>
 * <br/>
 * jOOQ does not provide any implementations.</li>
 * </ul>
 * </li> </ul>
 *
 * @author Lukas Eder
 */
public interface Configuration extends Serializable {

    // -------------------------------------------------------------------------
    // Custom data
    // -------------------------------------------------------------------------

    /**
     * Get all custom data from this <code>Configuration</code>.
     * <p>
     * This is custom data that was previously set to the configuration using
     * {@link #data(Object, Object)}. Use custom data if you want to pass data
     * to your custom {@link QueryPart} or {@link ExecuteListener} objects to be
     * made available at render, bind, execution, fetch time.
     * <p>
     * See {@link ExecuteListener} for more details.
     *
     * @return The custom data. This is never <code>null</code>
     * @see ExecuteListener
     */
    Map<Object, Object> data();

    /**
     * Get some custom data from this <code>Configuration</code>.
     * <p>
     * This is custom data that was previously set to the configuration using
     * {@link #data(Object, Object)}. Use custom data if you want to pass data
     * to your custom {@link QueryPart} or {@link ExecuteListener} objects to be
     * made available at render, bind, execution, fetch time.
     * <p>
     * See {@link ExecuteListener} for more details.
     *
     * @param key A key to identify the custom data
     * @return The custom data or <code>null</code> if no such data is contained
     *         in this <code>Configuration</code>
     * @see ExecuteListener
     */
    Object data(Object key);

    /**
     * Set some custom data to this <code>Configuration</code>.
     * <p>
     * Use custom data if you want to pass data to your custom {@link QueryPart}
     * or {@link ExecuteListener} objects to be made available at render, bind,
     * execution, fetch time.
     * <p>
     * Be sure that your custom data implements {@link Serializable} if you want
     * to serialise this <code>Configuration</code> or objects referencing this
     * <code>Configuration</code>, e.g. your {@link Record} types.
     * <p>
     * See {@link ExecuteListener} for more details.
     *
     * @param key A key to identify the custom data
     * @param value The custom data
     * @return The previously set custom data or <code>null</code> if no data
     *         was previously set for the given key
     * @see ExecuteListener
     */
    Object data(Object key, Object value);

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    /**
     * Get this configuration's underlying connection provider.
     */
    ConnectionProvider connectionProvider();

    /**
     * Get this configuration's underlying executor provider.
     * <p>
     * Asynchronous operations will call back to this SPI to obtain an executor.
     * This applies, for example, to {@link ResultQuery#fetchAsync()}.
     * <p>
     * The following logic is applied when resolving the appropriate
     * <code>executor</code>:
     * <ol>
     * <li>If {@link Configuration#executorProvider()} does not return
     * <code>null</code>, then {@link ExecutorProvider#provide()} is called to
     * obtain an <code>Executor</code> for the asynchronous task.</li>
     * <li>In the jOOQ Java 8 distribution, {@link ForkJoinPool#commonPool()} is
     * used if <code>{@link ForkJoinPool#getCommonPoolParallelism()} > 1</code>
     * </li>
     * <li>A new "one thread per call" <code>Executor</code> is used in any
     * other case.</li>
     * </ol>
     * <p>
     * The SPI will not be called if an asynchronous operation explicitly
     * overrides the {@link Executor}, e.g. as is the case for
     * {@link ResultQuery#fetchAsync(Executor)}.
     */
    ExecutorProvider executorProvider();

    /**
     * Get this configuration's underlying transaction provider.
     * <p>
     * If no explicit transaction provider was specified, and if
     * {@link #connectionProvider()} is a {@link DefaultConnectionProvider},
     * then this will return a {@link DefaultTransactionProvider}.
     */
    TransactionProvider transactionProvider();

    /**
     * Get the configured <code>TransactionListenerProvider</code>s from this
     * configuration.
     */
    TransactionListenerProvider[] transactionListenerProviders();

    /**
     * Get this configuration's underlying record mapper provider.
     */
    RecordMapperProvider recordMapperProvider();

    /**
     * Get the configured <code>RecordListenerProvider</code>s from this
     * configuration.
     * <p>
     * This method allows for retrieving the configured
     * <code>RecordListenerProvider</code> from this configuration. The
     * providers will provide jOOQ with {@link RecordListener} instances. These
     * instances receive record manipulation notification events every time jOOQ
     * executes queries. jOOQ makes no assumptions about the internal state of
     * these listeners, i.e. listener instances may
     * <ul>
     * <li>share this <code>Configuration</code>'s lifecycle (i.e. that of a
     * JDBC <code>Connection</code>, or that of a transaction)</li>
     * <li>share the lifecycle of an <code>RecordContext</code> (i.e. that of a
     * single record manipulation)</li>
     * <li>follow an entirely different lifecycle.</li>
     * </ul>
     *
     * @return The configured set of record listeners.
     * @see RecordListenerProvider
     * @see RecordListener
     * @see RecordContext
     */
    RecordListenerProvider[] recordListenerProviders();

    /**
     * Get the configured <code>ExecuteListenerProvider</code>s from this
     * configuration.
     * <p>
     * This method allows for retrieving the configured
     * <code>ExecuteListenerProvider</code> from this configuration. The
     * providers will provide jOOQ with {@link ExecuteListener} instances. These
     * instances receive execution lifecycle notification events every time jOOQ
     * executes queries. jOOQ makes no assumptions about the internal state of
     * these listeners, i.e. listener instances may
     * <ul>
     * <li>share this <code>Configuration</code>'s lifecycle (i.e. that of a
     * JDBC <code>Connection</code>, or that of a transaction)</li>
     * <li>share the lifecycle of an <code>ExecuteContext</code> (i.e. that of a
     * single query execution)</li>
     * <li>follow an entirely different lifecycle.</li>
     * </ul>
     * <p>
     * Note, depending on your {@link Settings#isExecuteLogging()}, some
     * additional listeners may be prepended to this list, internally. Those
     * listeners will never be exposed through this method, though.
     *
     * @return The configured set of execute listeners.
     * @see ExecuteListenerProvider
     * @see ExecuteListener
     * @see ExecuteContext
     */
    ExecuteListenerProvider[] executeListenerProviders();

    /**
     * TODO [#2667]
     */
    VisitListenerProvider[] visitListenerProviders();

    /**
     * Get the configured <code>ConverterProvider</code> from this
     * configuration.
     *
     * @deprecated - This API is still EXPERIMENTAL. Do not use yet
     */
    @Deprecated
    ConverterProvider converterProvider();

    /**
     * Retrieve the configured schema mapping.
     *
     * @deprecated - 2.0.5 - Use {@link #settings()} instead
     */
    @Deprecated
    SchemaMapping schemaMapping();

    /**
     * Retrieve the configured dialect.
     */
    SQLDialect dialect();

    /**
     * Retrieve the family of the configured dialect.
     */
    SQLDialect family();

    /**
     * Retrieve the runtime configuration settings.
     */
    Settings settings();

    // -------------------------------------------------------------------------
    // Setters
    // -------------------------------------------------------------------------

    /**
     * Change this configuration to hold a new connection provider.
     * <p>
     * This method is not thread-safe and should not be used in globally
     * available <code>Configuration</code> objects.
     *
     * @param newConnectionProvider The new connection provider to be contained
     *            in the changed configuration.
     * @return The changed configuration.
     */
    Configuration set(ConnectionProvider newConnectionProvider);

    /**
     * Change this configuration to hold a new executor provider.
     * <p>
     * This method is not thread-safe and should not be used in globally
     * available <code>Configuration</code> objects.
     *
     * @param newExecutorProvider The new executor provider to be contained in
     *            the changed configuration.
     * @return The changed configuration.
     */
    Configuration set(ExecutorProvider newExecutorProvider);

    /**
     * Change this configuration to hold a new executor.
     * <p>
     * This will wrap the argument {@link Executor} in a
     * {@link DefaultExecutorProvider} for convenience.
     * <p>
     * This method is not thread-safe and should not be used in globally
     * available <code>Configuration</code> objects.
     *
     * @param newExecutor The new executor to be contained in the changed
     *            configuration.
     * @return The changed configuration.
     */
    Configuration set(Executor newExecutor);

    /**
     * Change this configuration to hold a new connection wrapped in a
     * {@link DefaultConnectionProvider}.
     * <p>
     * This method is not thread-safe and should not be used in globally
     * available <code>Configuration</code> objects.
     *
     * @param newConnection The new connection to be contained in the changed
     *            configuration.
     * @return The changed configuration.
     */
    Configuration set(Connection newConnection);

    /**
     * Change this configuration to hold a new data source wrapped in a
     * {@link DataSourceConnectionProvider}.
     * <p>
     * This method is not thread-safe and should not be used in globally
     * available <code>Configuration</code> objects.
     *
     * @param newDataSource The new data source to be contained in the changed
     *            configuration.
     * @return The changed configuration.
     */
    Configuration set(DataSource newDataSource);

    /**
     * Change this configuration to hold a new transaction provider.
     * <p>
     * This method is not thread-safe and should not be used in globally
     * available <code>Configuration</code> objects.
     *
     * @param newTransactionProvider The new transaction provider to be
     *            contained in the changed configuration.
     * @return The changed configuration.
     */
    Configuration set(TransactionProvider newTransactionProvider);

    /**
     * Change this configuration to hold a new record mapper.
     * <p>
     * This will wrap the argument {@link RecordMapper} in a
     * {@link DefaultRecordMapperProvider} for convenience.
     * <p>
     * This method is not thread-safe and should not be used in globally
     * available <code>Configuration</code> objects.
     *
     * @param newRecordMapper The new record mapper to be contained in the
     *            changed configuration.
     * @return The changed configuration.
     */
    Configuration set(RecordMapper<?, ?> newRecordMapper);

    /**
     * Change this configuration to hold a new record mapper provider.
     * <p>
     * This method is not thread-safe and should not be used in globally
     * available <code>Configuration</code> objects.
     *
     * @param newRecordMapperProvider The new record mapper provider to be
     *            contained in the changed configuration.
     * @return The changed configuration.
     */
    Configuration set(RecordMapperProvider newRecordMapperProvider);

    /**
     * Change this configuration to hold a new record listeners.
     * <p>
     * This will wrap the argument {@link RecordListener} in a
     * {@link DefaultRecordListenerProvider} for convenience.
     * <p>
     * This method is not thread-safe and should not be used in globally
     * available <code>Configuration</code> objects.
     *
     * @param newRecordListeners The new record listener to be contained
     *            in the changed configuration.
     * @return The changed configuration.
     */
    Configuration set(RecordListener... newRecordListeners);

    /**
     * Change this configuration to hold a new record listener providers.
     * <p>
     * This method is not thread-safe and should not be used in globally
     * available <code>Configuration</code> objects.
     *
     * @param newRecordListenerProviders The new record listener providers to
     *            be contained in the changed configuration.
     * @return The changed configuration.
     */
    Configuration set(RecordListenerProvider... newRecordListenerProviders);

    /**
     * Change this configuration to hold a new execute listeners.
     * <p>
     * This will wrap the argument {@link ExecuteListener} in a
     * {@link DefaultExecuteListenerProvider} for convenience.
     * <p>
     * This method is not thread-safe and should not be used in globally
     * available <code>Configuration</code> objects.
     *
     * @param newExecuteListeners The new execute listeners to be contained in
     *            the changed configuration.
     * @return The changed configuration.
     */
    Configuration set(ExecuteListener... newExecuteListeners);

    /**
     * Change this configuration to hold a new execute listener providers.
     * <p>
     * This method is not thread-safe and should not be used in globally
     * available <code>Configuration</code> objects.
     *
     * @param newExecuteListenerProviders The new execute listener providers to
     *            be contained in the changed configuration.
     * @return The changed configuration.
     */
    Configuration set(ExecuteListenerProvider... newExecuteListenerProviders);

    /**
     * Change this configuration to hold a new visit listeners.
     * <p>
     * This will wrap the argument {@link VisitListener} in a
     * {@link DefaultVisitListenerProvider} for convenience.
     * <p>
     * This method is not thread-safe and should not be used in globally
     * available <code>Configuration</code> objects.
     *
     * @param newVisitListeners The new visit listeners to be contained
     *            in the changed configuration.
     * @return The changed configuration.
     */
    Configuration set(VisitListener... newVisitListeners);

    /**
     * Change this configuration to hold a new visit listener providers.
     * <p>
     * This method is not thread-safe and should not be used in globally
     * available <code>Configuration</code> objects.
     *
     * @param newVisitListenerProviders The new visit listener providers to
     *            be contained in the changed configuration.
     * @return The changed configuration.
     */
    Configuration set(VisitListenerProvider... newVisitListenerProviders);

    /**
     * Change this configuration to hold a new transaction listeners.
     * <p>
     * This will wrap the argument {@link TransactionListener} in a
     * {@link DefaultTransactionListenerProvider} for convenience.
     * <p>
     * This method is not thread-safe and should not be used in globally
     * available <code>Configuration</code> objects.
     *
     * @param newTransactionListeners The new transaction listeners to be
     *            contained in the changed configuration.
     * @return The changed configuration.
     */
    Configuration set(TransactionListener... newTransactionListeners);

    /**
     * Change this configuration to hold a new transaction listener providers.
     * <p>
     * This method is not thread-safe and should not be used in globally
     * available <code>Configuration</code> objects.
     *
     * @param newTransactionListenerProviders The new transaction listener
     *            providers to be contained in the changed configuration.
     * @return The changed configuration.
     */
    Configuration set(TransactionListenerProvider... newTransactionListenerProviders);

    /**
     * Change this configuration to hold a new converter provider.
     * <p>
     * This method is not thread-safe and should not be used in globally
     * available <code>Configuration</code> objects.
     *
     * @param newConverterProvider The new converter provider to be contained in
     *            the changed configuration.
     * @return The changed configuration.
     * @deprecated - This API is still EXPERIMENTAL. Do not use yet
     */
    @Deprecated
    Configuration set(ConverterProvider newConverterProvider);

    /**
     * Change this configuration to hold a new dialect.
     * <p>
     * This method is not thread-safe and should not be used in globally
     * available <code>Configuration</code> objects.
     *
     * @param newDialect The new dialect to be contained in the changed
     *            configuration.
     * @return The changed configuration.
     */
    Configuration set(SQLDialect newDialect);

    /**
     * Change this configuration to hold a new settings.
     * <p>
     * This method is not thread-safe and should not be used in globally
     * available <code>Configuration</code> objects.
     *
     * @param newSettings The new settings to be contained in the changed
     *            configuration.
     * @return The changed configuration.
     */
    Configuration set(Settings newSettings);

    // -------------------------------------------------------------------------
    // Derivation methods
    // -------------------------------------------------------------------------

    /**
     * Create a derived configuration from this one, without changing any
     * properties.
     *
     * @return The derived configuration.
     */
    Configuration derive();

    /**
     * Create a derived configuration from this one, with a new connection
     * wrapped in a {@link DefaultConnectionProvider}.
     *
     * @param newConnection The new connection to be contained in the derived
     *            configuration.
     * @return The derived configuration.
     */
    Configuration derive(Connection newConnection);

    /**
     * Create a derived configuration from this one, with a new data source
     * wrapped in a {@link DataSourceConnectionProvider}.
     *
     * @param newDataSource The new data source to be contained in the derived
     *            configuration.
     * @return The derived configuration.
     */
    Configuration derive(DataSource newDataSource);

    /**
     * Create a derived configuration from this one, with a new connection
     * provider.
     *
     * @param newConnectionProvider The new connection provider to be contained
     *            in the derived configuration.
     * @return The derived configuration.
     */
    Configuration derive(ConnectionProvider newConnectionProvider);

    /**
     * Create a derived configuration from this one, with a new executor.
     * <p>
     * This will wrap the argument {@link Executor} in a
     * {@link DefaultExecutorProvider} for convenience.
     *
     * @param newExecutor The new executor to be contained in the derived
     *            configuration.
     * @return The derived configuration.
     */
    Configuration derive(Executor newExecutor);

    /**
     * Create a derived configuration from this one, with a new executor
     * provider.
     *
     * @param newExecutorProvider The new executor provider to be contained in
     *            the derived configuration.
     * @return The derived configuration.
     */
    Configuration derive(ExecutorProvider newExecutorProvider);

    /**
     * Create a derived configuration from this one, with a new transaction
     * provider.
     *
     * @param newTransactionProvider The new transaction provider to be
     *            contained in the derived configuration.
     * @return The derived configuration.
     */
    Configuration derive(TransactionProvider newTransactionProvider);

    /**
     * Create a derived configuration from this one, with a new record mapper.
     * <p>
     * This will wrap the argument {@link RecordMapper} in a
     * {@link DefaultRecordMapperProvider} for convenience.
     *
     * @param newRecordMapper The new record mapper to be contained in the
     *            derived configuration.
     * @return The derived configuration.
     */
    Configuration derive(RecordMapper<?, ?> newRecordMapper);

    /**
     * Create a derived configuration from this one, with a new record mapper
     * provider.
     *
     * @param newRecordMapperProvider The new record mapper provider to be
     *            contained in the derived configuration.
     * @return The derived configuration.
     */
    Configuration derive(RecordMapperProvider newRecordMapperProvider);

    /**
     * Create a derived configuration from this one, with new record listeners.
     * <p>
     * This will wrap the argument {@link RecordListener} in a
     * {@link DefaultRecordListenerProvider} for convenience.
     *
     * @param newRecordListeners The new record listeners to be contained in the
     *            derived configuration.
     * @return The derived configuration.
     */
    Configuration derive(RecordListener... newRecordListeners);

    /**
     * Create a derived configuration from this one, with new record listener
     * providers.
     *
     * @param newRecordListenerProviders The new record listener providers to
     *            be contained in the derived configuration.
     * @return The derived configuration.
     */
    Configuration derive(RecordListenerProvider... newRecordListenerProviders);

    /**
     * Create a derived configuration from this one, with new execute listeners.
     * <p>
     * This will wrap the argument {@link ExecuteListener} in a
     * {@link DefaultExecuteListenerProvider} for convenience.
     *
     * @param newExecuteListeners The new execute listener to be contained in
     *            the derived configuration.
     * @return The derived configuration.
     */
    Configuration derive(ExecuteListener... newExecuteListeners);

    /**
     * Create a derived configuration from this one, with new execute listener
     * providers.
     *
     * @param newExecuteListenerProviders The new execute listener providers to
     *            be contained in the derived configuration.
     * @return The derived configuration.
     */
    Configuration derive(ExecuteListenerProvider... newExecuteListenerProviders);

    /**
     * Create a derived configuration from this one, with new visit listeners.
     * <p>
     * This will wrap the argument {@link VisitListener} in a
     * {@link DefaultVisitListenerProvider} for convenience.
     *
     * @param newVisitListeners The new visit listeners to be contained in the
     *            derived configuration.
     * @return The derived configuration.
     */
    Configuration derive(VisitListener... newVisitListeners);

    /**
     * Create a derived configuration from this one, with new visit listener
     * providers.
     *
     * @param newVisitListenerProviders The new visit listener providers to
     *            be contained in the derived configuration.
     * @return The derived configuration.
     */
    Configuration derive(VisitListenerProvider... newVisitListenerProviders);

    /**
     * Create a derived configuration from this one, with new transaction
     * listeners.
     * <p>
     * This will wrap the argument {@link TransactionListener} in a
     * {@link DefaultTransactionListenerProvider} for convenience.
     *
     * @param newTransactionListeners The new transaction listeners to be
     *            contained in the derived configuration.
     * @return The derived configuration.
     */
    Configuration derive(TransactionListener... newTransactionListeners);

    /**
     * Create a derived configuration from this one, with new transaction
     * listener providers.
     *
     * @param newTransactionListenerProviders The new transaction listener
     *            providers to be contained in the derived configuration.
     * @return The derived configuration.
     */
    Configuration derive(TransactionListenerProvider... newTransactionListenerProviders);

    /**
     * Create a derived configuration from this one, with new converter
     * provider.
     *
     * @param newConverterProvider The new converter provider to be contained in
     *            the derived configuration.
     * @return The derived configuration.
     * @deprecated - This API is still EXPERIMENTAL. Do not use yet
     */
    @Deprecated
    Configuration derive(ConverterProvider newConverterProvider);

    /**
     * Create a derived configuration from this one, with a new dialect.
     *
     * @param newDialect The new dialect to be contained in the derived
     *            configuration.
     * @return The derived configuration.
     */
    Configuration derive(SQLDialect newDialect);

    /**
     * Create a derived configuration from this one, with new settings.
     *
     * @param newSettings The new settings to be contained in the derived
     *            configuration.
     * @return The derived configuration.
     */
    Configuration derive(Settings newSettings);
}
