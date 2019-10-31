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
package org.jooq.impl;

import static org.jooq.SQLDialect.DEFAULT;
import static org.jooq.impl.Tools.DataKey.DATA_DEFAULT_TRANSACTION_PROVIDER_CONNECTION;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.time.Clock;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

import javax.sql.DataSource;

import org.jooq.Configuration;
import org.jooq.ConnectionProvider;
import org.jooq.ConverterProvider;
import org.jooq.DSLContext;
import org.jooq.DiagnosticsListener;
import org.jooq.DiagnosticsListenerProvider;
import org.jooq.ExecuteListener;
import org.jooq.ExecuteListenerProvider;
import org.jooq.ExecutorProvider;
import org.jooq.MetaProvider;
import org.jooq.Record;
import org.jooq.RecordListener;
import org.jooq.RecordListenerProvider;
import org.jooq.RecordMapper;
import org.jooq.RecordMapperProvider;
import org.jooq.RecordType;
import org.jooq.RecordUnmapper;
import org.jooq.RecordUnmapperProvider;
import org.jooq.SQLDialect;
import org.jooq.TransactionListener;
import org.jooq.TransactionListenerProvider;
import org.jooq.TransactionProvider;
import org.jooq.Unwrapper;
import org.jooq.UnwrapperProvider;
import org.jooq.VisitListener;
import org.jooq.VisitListenerProvider;
import org.jooq.conf.Settings;
import org.jooq.conf.SettingsTools;
import org.jooq.exception.ConfigurationException;
import org.jooq.impl.ThreadLocalTransactionProvider.ThreadLocalConnectionProvider;
import org.jooq.impl.Tools.DataCacheKey;

/**
 * A default implementation for configurations within a {@link DSLContext}, if no
 * custom configuration was supplied to {@link DSL#using(Configuration)}.
 * <p>
 * The <code>DefaultConfiguration</code>
 *
 * @author Lukas Eder
 */
public class DefaultConfiguration implements Configuration {

    /**
     * Serial version UID
     */
    private static final long                           serialVersionUID = 4296981040491238190L;

    // Configuration objects
    private SQLDialect                                  dialect;
    private Settings                                    settings;

    private Clock                                       clock;


    // These objects may be user defined and thus not necessarily serialisable
    private transient ConnectionProvider                connectionProvider;
    private transient ConnectionProvider                interpreterConnectionProvider;
    private transient ConnectionProvider                systemConnectionProvider;
    private transient MetaProvider                      metaProvider;
    private transient ExecutorProvider                  executorProvider;
    private transient TransactionProvider               transactionProvider;
    private transient RecordMapperProvider              recordMapperProvider;
    private transient RecordUnmapperProvider            recordUnmapperProvider;
    private transient RecordListenerProvider[]          recordListenerProviders;
    private transient ExecuteListenerProvider[]         executeListenerProviders;
    private transient VisitListenerProvider[]           visitListenerProviders;
    private transient TransactionListenerProvider[]     transactionListenerProviders;
    private transient DiagnosticsListenerProvider[]     diagnosticsListenerProviders;
    private transient UnwrapperProvider                 unwrapperProvider;
    private transient ConverterProvider                 converterProvider;

    // [#7062] Apart from the possibility of containing user defined objects, the data
    //         map also contains the reflection cache, which isn't serializable (and
    //         should not be serialized anyway).
    private transient ConcurrentHashMap<Object, Object> data;

    // Derived objects
    private org.jooq.SchemaMapping                      mapping;

    // -------------------------------------------------------------------------
    // XXX: Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a new "empty" configuration object.
     * <p>
     * This can be used as is, as a "dummy" configuration object, or as a base
     * implementation for creating more sophisticated "derived" configurations
     * through the various <code>derive()</code> or <code>set()</code> methods.
     */
    public DefaultConfiguration() {
        this(DEFAULT);
    }

    /**
     * Create a new "empty" configuration object given a {@link SQLDialect}.
     * <p>
     * This can be used as is, as a "dummy" configuration object, or as a base
     * implementation for creating more sophisticated "derived" configurations
     * through the various <code>derive()</code> or <code>set()</code> methods.
     *
     * @param dialect The pre-existing {@link SQLDialect}.
     */
    DefaultConfiguration(SQLDialect dialect) {
        this(
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,

            null,

            dialect,
            SettingsTools.defaultSettings(),
            null
        );
    }

    /**
     * Create a new "derived" configuration object from a pre-existing one.
     * <p>
     * This copies all properties from a pre-existing configuration into a new,
     * derived one.
     *
     * @param configuration The pre-existing configuration.
     */
    DefaultConfiguration(DefaultConfiguration configuration) {
        this(
            configuration.connectionProvider,
            configuration.interpreterConnectionProvider,
            configuration.systemConnectionProvider,
            configuration.metaProvider,
            configuration.executorProvider,
            configuration.transactionProvider,
            configuration.recordMapperProvider,
            configuration.recordUnmapperProvider,
            configuration.recordListenerProviders,
            configuration.executeListenerProviders,
            configuration.visitListenerProviders,
            configuration.transactionListenerProviders,
            configuration.diagnosticsListenerProviders,
            configuration.unwrapperProvider,
            configuration.converterProvider,

            configuration.clock,

            configuration.dialect,
            configuration.settings,
            configuration.data
        );
    }

    /**
     * This constructor is maintained for backwards-compatibility reasons.
     * Spring users tend to construct this <code>DefaultConfiguration</code>
     * through reflection.
     *
     * @deprecated Use
     *             {@link #DefaultConfiguration(ConnectionProvider, ExecutorProvider, TransactionProvider, RecordMapperProvider, RecordListenerProvider[], ExecuteListenerProvider[], VisitListenerProvider[], TransactionListenerProvider[], ConverterProvider, SQLDialect, Settings, Map)}
     *             instead. This constructor is maintained to provide jOOQ 3.2,
     *             3.3, 3.7, 3.8 backwards-compatibility if called with reflection
     *             from Spring configurations.
     */
    @Deprecated
    DefaultConfiguration(
        ConnectionProvider connectionProvider,
        ExecuteListenerProvider[] executeListenerProviders,
        SQLDialect dialect,
        Settings settings,
        Map<Object, Object> data)
    {
        this(
            connectionProvider,
            null,
            null,
            null,
            null,
            null,
            null,
            executeListenerProviders,
            null,
            null,
            null,
            null,
            null,

            null,

            dialect,
            settings,
            data
        );
    }

    /**
     * This constructor is maintained for backwards-compatibility reasons.
     * Spring users tend to construct this <code>DefaultConfiguration</code>
     * through reflection.
     *
     * @deprecated Use
     *             {@link #DefaultConfiguration(ConnectionProvider, ExecutorProvider, TransactionProvider, RecordMapperProvider, RecordListenerProvider[], ExecuteListenerProvider[], VisitListenerProvider[], TransactionListenerProvider[], ConverterProvider, SQLDialect, Settings, Map)}
     *             instead. This constructor is maintained to provide jOOQ 3.2,
     *             3.3, 3.7, 3.8 backwards-compatibility if called with reflection
     *             from Spring configurations.
     */
    @Deprecated
    DefaultConfiguration(
        ConnectionProvider connectionProvider,
        RecordMapperProvider recordMapperProvider,
        ExecuteListenerProvider[] executeListenerProviders,
        SQLDialect dialect,
        Settings settings,
        Map<Object, Object> data)
    {
        this(
            connectionProvider,
            null,
            null,
            null,
            recordMapperProvider,
            null,
            null,
            executeListenerProviders,
            null,
            null,
            null,
            null,
            null,

            null,

            dialect,
            settings,
            data
        );
    }

    /**
     * This constructor is maintained for backwards-compatibility reasons.
     * Spring users tend to construct this <code>DefaultConfiguration</code>
     * through reflection.
     *
     * @deprecated Use
     *             {@link #DefaultConfiguration(ConnectionProvider, ExecutorProvider, TransactionProvider, RecordMapperProvider, RecordListenerProvider[], ExecuteListenerProvider[], VisitListenerProvider[], TransactionListenerProvider[], ConverterProvider, SQLDialect, Settings, Map)}
     *             instead. This constructor is maintained to provide jOOQ 3.2,
     *             3.3, 3.7, 3.8 backwards-compatibility if called with reflection
     *             from Spring configurations.
     */
    @Deprecated
    DefaultConfiguration(
        ConnectionProvider connectionProvider,
        RecordMapperProvider recordMapperProvider,
        RecordListenerProvider[] recordListenerProviders,
        ExecuteListenerProvider[] executeListenerProviders,
        VisitListenerProvider[] visitListenerProviders,
        SQLDialect dialect,
        Settings settings,
        Map<Object, Object> data)
    {
        this(
            connectionProvider,
            null,
            null,
            null,
            recordMapperProvider,
            null,
            recordListenerProviders,
            executeListenerProviders,
            visitListenerProviders,
            null,
            null,
            null,
            null,

            null,

            dialect,
            settings,
            data
        );
    }

    /**
     * This constructor is maintained for backwards-compatibility reasons.
     * Spring users tend to construct this <code>DefaultConfiguration</code>
     * through reflection.
     *
     * @deprecated Use
     *             {@link #DefaultConfiguration(ConnectionProvider, ExecutorProvider, TransactionProvider, RecordMapperProvider, RecordListenerProvider[], ExecuteListenerProvider[], VisitListenerProvider[], TransactionListenerProvider[], ConverterProvider, SQLDialect, Settings, Map)}
     *             instead. This constructor is maintained to provide jOOQ 3.2,
     *             3.3, 3.7, 3.8 backwards-compatibility if called with reflection
     *             from Spring configurations.
     */
    @Deprecated
    DefaultConfiguration(
        ConnectionProvider connectionProvider,
        TransactionProvider transactionProvider,
        RecordMapperProvider recordMapperProvider,
        RecordListenerProvider[] recordListenerProviders,
        ExecuteListenerProvider[] executeListenerProviders,
        VisitListenerProvider[] visitListenerProviders,
        SQLDialect dialect,
        Settings settings,
        Map<Object, Object> data)
    {
        this(
            connectionProvider,
            null,
            null,
            transactionProvider,
            recordMapperProvider,
            null,
            recordListenerProviders,
            executeListenerProviders,
            visitListenerProviders,
            null,
            null,
            null,
            null,

            null,

            dialect,
            settings,
            data
        );
    }

    /**
     * This constructor is maintained for backwards-compatibility reasons.
     * Spring users tend to construct this <code>DefaultConfiguration</code>
     * through reflection.
     *
     * @deprecated Use
     *             {@link #DefaultConfiguration(ConnectionProvider, ExecutorProvider, TransactionProvider, RecordMapperProvider, RecordListenerProvider[], ExecuteListenerProvider[], VisitListenerProvider[], TransactionListenerProvider[], ConverterProvider, SQLDialect, Settings, Map)}
     *             instead. This constructor is maintained to provide jOOQ 3.2,
     *             3.3, 3.7, 3.8 backwards-compatibility if called with reflection
     *             from Spring configurations.
     */
    @Deprecated
    DefaultConfiguration(
        ConnectionProvider connectionProvider,
        TransactionProvider transactionProvider,
        RecordMapperProvider recordMapperProvider,
        RecordListenerProvider[] recordListenerProviders,
        ExecuteListenerProvider[] executeListenerProviders,
        VisitListenerProvider[] visitListenerProviders,
        ConverterProvider converterProvider,
        SQLDialect dialect,
        Settings settings,
        Map<Object, Object> data)
    {
        this(
            connectionProvider,
            null,
            null,
            transactionProvider,
            recordMapperProvider,
            null,
            recordListenerProviders,
            executeListenerProviders,
            visitListenerProviders,
            null,
            null,
            null,
            converterProvider,

            null,

            dialect,
            settings,
            data
        );
    }

    /**
     * Create the actual configuration object.
     * <p>
     * This constructor has been made package-private to allow for adding new
     * configuration properties in the future, without breaking client code.
     * Consider creating a configuration by chaining calls to various
     * <code>derive()</code> methods.
     *
     * @deprecated Use
     *             {@link #DefaultConfiguration(ConnectionProvider, ExecutorProvider, TransactionProvider, RecordMapperProvider, RecordListenerProvider[], ExecuteListenerProvider[], VisitListenerProvider[], TransactionListenerProvider[], ConverterProvider, SQLDialect, Settings, Map)}
     *             instead. This constructor is maintained to provide jOOQ 3.2,
     *             3.3, 3.7, 3.8 backwards-compatibility if called with reflection
     *             from Spring configurations.
     */
    @Deprecated
    DefaultConfiguration(
        ConnectionProvider connectionProvider,
        ExecutorProvider executorProvider,
        TransactionProvider transactionProvider,
        RecordMapperProvider recordMapperProvider,
        RecordListenerProvider[] recordListenerProviders,
        ExecuteListenerProvider[] executeListenerProviders,
        VisitListenerProvider[] visitListenerProviders,
        ConverterProvider converterProvider,
        SQLDialect dialect,
        Settings settings,
        Map<Object, Object> data)
    {
        this(
            connectionProvider,
            null,
            executorProvider,
            transactionProvider,
            recordMapperProvider,
            null,
            recordListenerProviders,
            executeListenerProviders,
            visitListenerProviders,
            null,
            null,
            null,
            converterProvider,

            null,

            dialect,
            settings,
            data
        );
    }

    /**
     * Create the actual configuration object.
     * <p>
     * This constructor has been made package-private to allow for adding new
     * configuration properties in the future, without breaking client code.
     * Consider creating a configuration by chaining calls to various
     * <code>derive()</code> methods.
     *
     * @deprecated Use
     *             {@link #DefaultConfiguration(ConnectionProvider, ExecutorProvider, TransactionProvider, RecordMapperProvider, RecordUnmapperProvider, RecordListenerProvider[], ExecuteListenerProvider[], VisitListenerProvider[], TransactionListenerProvider[], ConverterProvider, SQLDialect, Settings, Map)}
     *             instead. This constructor is maintained to provide jOOQ 3.2,
     *             3.3, 3.7, 3.8, 3.9 backwards-compatibility if called with
     *             reflection from Spring configurations.
     */
    @Deprecated
    DefaultConfiguration(
        ConnectionProvider connectionProvider,
        ExecutorProvider executorProvider,
        TransactionProvider transactionProvider,
        RecordMapperProvider recordMapperProvider,
        RecordListenerProvider[] recordListenerProviders,
        ExecuteListenerProvider[] executeListenerProviders,
        VisitListenerProvider[] visitListenerProviders,
        TransactionListenerProvider[] transactionListenerProviders,
        ConverterProvider converterProvider,
        SQLDialect dialect,
        Settings settings,
        Map<Object, Object> data)
    {
        this(
            connectionProvider,
            null,
            executorProvider,
            transactionProvider,
            recordMapperProvider,
            null,
            recordListenerProviders,
            executeListenerProviders,
            visitListenerProviders,
            transactionListenerProviders,
            null,
            null,
            converterProvider,

            null,

            dialect,
            settings,
            data
        );
    }

    /**
     * Create the actual configuration object.
     * <p>
     * This constructor has been made package-private to allow for adding new
     * configuration properties in the future, without breaking client code.
     * Consider creating a configuration by chaining calls to various
     * <code>derive()</code> methods.
     *
     * @deprecated Use
     *             {@link #DefaultConfiguration(ConnectionProvider, ExecutorProvider, TransactionProvider, RecordMapperProvider, RecordUnmapperProvider, RecordListenerProvider[], ExecuteListenerProvider[], VisitListenerProvider[], TransactionListenerProvider[], DiagnosticsListenerProvider[], ConverterProvider, SQLDialect, Settings, Map)}
     *             instead. This constructor is maintained to provide jOOQ 3.2,
     *             3.3, 3.7, 3.8, 3.9, 3.10 backwards-compatibility if called with
     *             reflection from Spring configurations.
     */
    @Deprecated
    DefaultConfiguration(
        ConnectionProvider connectionProvider,
        ExecutorProvider executorProvider,
        TransactionProvider transactionProvider,
        RecordMapperProvider recordMapperProvider,
        RecordUnmapperProvider recordUnmapperProvider,
        RecordListenerProvider[] recordListenerProviders,
        ExecuteListenerProvider[] executeListenerProviders,
        VisitListenerProvider[] visitListenerProviders,
        TransactionListenerProvider[] transactionListenerProviders,
        ConverterProvider converterProvider,

        Clock clock,

        SQLDialect dialect,
        Settings settings,
        Map<Object, Object> data)
    {
        this(
            connectionProvider,
            null,
            executorProvider,
            transactionProvider,
            recordMapperProvider,
            recordUnmapperProvider,
            recordListenerProviders,
            executeListenerProviders,
            visitListenerProviders,
            transactionListenerProviders,
            null,
            null,
            converterProvider,

            clock,

            dialect,
            settings,
            data
        );
    }

    /**
     * Create the actual configuration object.
     * <p>
     * This constructor has been made package-private to allow for adding new
     * configuration properties in the future, without breaking client code.
     * Consider creating a configuration by chaining calls to various
     * <code>derive()</code> methods.
     *
     * @deprecated Use
     *             {@link #DefaultConfiguration(ConnectionProvider, ExecutorProvider, TransactionProvider, RecordMapperProvider, RecordUnmapperProvider, RecordListenerProvider[], ExecuteListenerProvider[], VisitListenerProvider[], TransactionListenerProvider[], DiagnosticsListenerProvider[], UnwrapperProvider, ConverterProvider, SQLDialect, Settings, Map)}
     *             instead. This constructor is maintained to provide jOOQ 3.2,
     *             3.3, 3.7, 3.8, 3.9, 3.10, 3.11 backwards-compatibility if
     *             called with reflection from Spring configurations.
     */
    @Deprecated
    DefaultConfiguration(
        ConnectionProvider connectionProvider,
        MetaProvider metaProvider,
        ExecutorProvider executorProvider,
        TransactionProvider transactionProvider,
        RecordMapperProvider recordMapperProvider,
        RecordUnmapperProvider recordUnmapperProvider,
        RecordListenerProvider[] recordListenerProviders,
        ExecuteListenerProvider[] executeListenerProviders,
        VisitListenerProvider[] visitListenerProviders,
        TransactionListenerProvider[] transactionListenerProviders,
        DiagnosticsListenerProvider[] diagnosticsListenerProviders,
        ConverterProvider converterProvider,

        Clock clock,

        SQLDialect dialect,
        Settings settings,
        Map<Object, Object> data)
    {
        this(
            connectionProvider,
            metaProvider,
            executorProvider,
            transactionProvider,
            recordMapperProvider,
            recordUnmapperProvider,
            recordListenerProviders,
            executeListenerProviders,
            visitListenerProviders,
            transactionListenerProviders,
            diagnosticsListenerProviders,
            null,
            converterProvider,

            clock,

            dialect,
            settings,
            data
        );
    }

    /**
     * Create the actual configuration object.
     * <p>
     * This constructor has been made package-private to allow for adding new
     * configuration properties in the future, without breaking client code.
     * Consider creating a configuration by chaining calls to various
     * <code>derive()</code> methods.
     *
     * @deprecated Use
     *             {@link #DefaultConfiguration(ConnectionProvider, ConnectionProvider, ExecutorProvider, TransactionProvider, RecordMapperProvider, RecordUnmapperProvider, RecordListenerProvider[], ExecuteListenerProvider[], VisitListenerProvider[], TransactionListenerProvider[], DiagnosticsListenerProvider[], UnwrapperProvider, ConverterProvider, SQLDialect, Settings, Map)}
     *             instead. This constructor is maintained to provide jOOQ 3.2,
     *             3.3, 3.7, 3.8, 3.9, 3.10, 3.11, 3.12 backwards-compatibility if
     *             called with reflection from Spring configurations.
     */
    @Deprecated
    DefaultConfiguration(
        ConnectionProvider connectionProvider,
        MetaProvider metaProvider,
        ExecutorProvider executorProvider,
        TransactionProvider transactionProvider,
        RecordMapperProvider recordMapperProvider,
        RecordUnmapperProvider recordUnmapperProvider,
        RecordListenerProvider[] recordListenerProviders,
        ExecuteListenerProvider[] executeListenerProviders,
        VisitListenerProvider[] visitListenerProviders,
        TransactionListenerProvider[] transactionListenerProviders,
        DiagnosticsListenerProvider[] diagnosticsListenerProviders,
        UnwrapperProvider unwrapperProvider,
        ConverterProvider converterProvider,

        Clock clock,

        SQLDialect dialect,
        Settings settings,
        Map<Object, Object> data)
    {
        this(
            connectionProvider,
            null,
            null,
            metaProvider,
            executorProvider,
            transactionProvider,
            recordMapperProvider,
            recordUnmapperProvider,
            recordListenerProviders,
            executeListenerProviders,
            visitListenerProviders,
            transactionListenerProviders,
            diagnosticsListenerProviders,
            unwrapperProvider,
            converterProvider,

            clock,

            dialect,
            settings,
            data
        );
    }

    /**
     * Create the actual configuration object.
     * <p>
     * This constructor has been made package-private to allow for adding new
     * configuration properties in the future, without breaking client code.
     * Consider creating a configuration by chaining calls to various
     * <code>derive()</code> methods.
     */
    DefaultConfiguration(
        ConnectionProvider connectionProvider,
        ConnectionProvider interpreterConnectionProvider,
        ConnectionProvider systemConnectionProvider,
        MetaProvider metaProvider,
        ExecutorProvider executorProvider,
        TransactionProvider transactionProvider,
        RecordMapperProvider recordMapperProvider,
        RecordUnmapperProvider recordUnmapperProvider,
        RecordListenerProvider[] recordListenerProviders,
        ExecuteListenerProvider[] executeListenerProviders,
        VisitListenerProvider[] visitListenerProviders,
        TransactionListenerProvider[] transactionListenerProviders,
        DiagnosticsListenerProvider[] diagnosticsListenerProviders,
        UnwrapperProvider unwrapperProvider,
        ConverterProvider converterProvider,

        Clock clock,

        SQLDialect dialect,
        Settings settings,
        Map<Object, Object> data)
    {
        set(connectionProvider);
        setInterpreterConnectionProvider(interpreterConnectionProvider);
        setSystemConnectionProvider(systemConnectionProvider);
        set(metaProvider);
        set(executorProvider);
        set(transactionProvider);
        set(recordMapperProvider);
        set(recordUnmapperProvider);
        set(recordListenerProviders);
        set(executeListenerProviders);
        set(visitListenerProviders);
        set(transactionListenerProviders);
        set(diagnosticsListenerProviders);
        set(unwrapperProvider);
        set(converterProvider);

        set(clock);

        set(dialect);
        set(settings);

        this.data = data != null
            ? new ConcurrentHashMap<>(data)
            : new ConcurrentHashMap<>();
    }

    // -------------------------------------------------------------------------
    // XXX: Wrap this Configuration
    // -------------------------------------------------------------------------

    @Override
    public final DSLContext dsl() {
        return DSL.using(this);
    }

    // -------------------------------------------------------------------------
    // XXX: Deriving configurations
    // -------------------------------------------------------------------------

    @Override
    public final Configuration derive() {
        return new DefaultConfiguration(this);
    }

    @Override
    public final Configuration derive(Connection newConnection) {
        return derive(new DefaultConnectionProvider(newConnection));
    }

    @Override
    public final Configuration derive(DataSource newDataSource) {
        return derive(new DataSourceConnectionProvider(newDataSource));
    }

    @Override
    public final Configuration derive(ConnectionProvider newConnectionProvider) {
        return new DefaultConfiguration(
            newConnectionProvider,
            interpreterConnectionProvider,
            systemConnectionProvider,
            metaProvider,
            executorProvider,
            transactionProvider,
            recordMapperProvider,
            recordUnmapperProvider,
            recordListenerProviders,
            executeListenerProviders,
            visitListenerProviders,
            transactionListenerProviders,
            diagnosticsListenerProviders,
            unwrapperProvider,
            converterProvider,

            clock,

            dialect,
            settings,
            data
        );
    }

    @Override
    public final Configuration derive(MetaProvider newMetaProvider) {
        return new DefaultConfiguration(
            connectionProvider,
            interpreterConnectionProvider,
            systemConnectionProvider,
            newMetaProvider,
            executorProvider,
            transactionProvider,
            recordMapperProvider,
            recordUnmapperProvider,
            recordListenerProviders,
            executeListenerProviders,
            visitListenerProviders,
            transactionListenerProviders,
            diagnosticsListenerProviders,
            unwrapperProvider,
            converterProvider,

            clock,

            dialect,
            settings,
            data
        );
    }

    @Override
    public final Configuration derive(final Executor newExecutor) {
        return derive(new ExecutorWrapper(newExecutor));
    }

    @Override
    public final Configuration derive(ExecutorProvider newExecutorProvider) {
        return new DefaultConfiguration(
            connectionProvider,
            interpreterConnectionProvider,
            systemConnectionProvider,
            metaProvider,
            newExecutorProvider,
            transactionProvider,
            recordMapperProvider,
            recordUnmapperProvider,
            recordListenerProviders,
            executeListenerProviders,
            visitListenerProviders,
            transactionListenerProviders,
            diagnosticsListenerProviders,
            unwrapperProvider,
            converterProvider,

            clock,

            dialect,
            settings,
            data
        );
    }

    @Override
    public final Configuration derive(TransactionProvider newTransactionProvider) {
        return new DefaultConfiguration(
            connectionProvider,
            interpreterConnectionProvider,
            systemConnectionProvider,
            metaProvider,
            executorProvider,
            newTransactionProvider,
            recordMapperProvider,
            recordUnmapperProvider,
            recordListenerProviders,
            executeListenerProviders,
            visitListenerProviders,
            transactionListenerProviders,
            diagnosticsListenerProviders,
            unwrapperProvider,
            converterProvider,

            clock,

            dialect,
            settings,
            data
        );
    }

    @Override
    public final Configuration derive(final RecordMapper<?, ?> newRecordMapper) {
        return derive(new RecordMapperWrapper(newRecordMapper));
    }

    @Override
    public final Configuration derive(RecordMapperProvider newRecordMapperProvider) {
        return new DefaultConfiguration(
            connectionProvider,
            interpreterConnectionProvider,
            systemConnectionProvider,
            metaProvider,
            executorProvider,
            transactionProvider,
            newRecordMapperProvider,
            recordUnmapperProvider,
            recordListenerProviders,
            executeListenerProviders,
            visitListenerProviders,
            transactionListenerProviders,
            diagnosticsListenerProviders,
            unwrapperProvider,
            converterProvider,

            clock,

            dialect,
            settings,
            data
        );
    }

    @Override
    public final Configuration derive(final RecordUnmapper<?, ?> newRecordUnmapper) {
        return derive(new RecordUnmapperWrapper(newRecordUnmapper));
    }

    @Override
    public final Configuration derive(RecordUnmapperProvider newRecordUnmapperProvider) {
        return new DefaultConfiguration(
            connectionProvider,
            interpreterConnectionProvider,
            systemConnectionProvider,
            metaProvider,
            executorProvider,
            transactionProvider,
            recordMapperProvider,
            newRecordUnmapperProvider,
            recordListenerProviders,
            executeListenerProviders,
            visitListenerProviders,
            transactionListenerProviders,
            diagnosticsListenerProviders,
            unwrapperProvider,
            converterProvider,

            clock,

            dialect,
            settings,
            data
        );
    }

    @Override
    public final Configuration derive(RecordListener... newRecordListeners) {
        return derive(DefaultRecordListenerProvider.providers(newRecordListeners));
    }

    @Override
    public final Configuration derive(RecordListenerProvider... newRecordListenerProviders) {
        return new DefaultConfiguration(
            connectionProvider,
            interpreterConnectionProvider,
            systemConnectionProvider,
            metaProvider,
            executorProvider,
            transactionProvider,
            recordMapperProvider,
            recordUnmapperProvider,
            newRecordListenerProviders,
            executeListenerProviders,
            visitListenerProviders,
            transactionListenerProviders,
            diagnosticsListenerProviders,
            unwrapperProvider,
            converterProvider,

            clock,

            dialect,
            settings,
            data
        );
    }

    @Override
    public final Configuration derive(ExecuteListener... newExecuteListeners) {
        return derive(DefaultExecuteListenerProvider.providers(newExecuteListeners));
    }

    @Override
    public final Configuration derive(ExecuteListenerProvider... newExecuteListenerProviders) {
        return new DefaultConfiguration(
            connectionProvider,
            interpreterConnectionProvider,
            systemConnectionProvider,
            metaProvider,
            executorProvider,
            transactionProvider,
            recordMapperProvider,
            recordUnmapperProvider,
            recordListenerProviders,
            newExecuteListenerProviders,
            visitListenerProviders,
            transactionListenerProviders,
            diagnosticsListenerProviders,
            unwrapperProvider,
            converterProvider,

            clock,

            dialect,
            settings,
            data
        );
    }

    @Override
    public final Configuration derive(VisitListener... newVisitListeners) {
        return derive(DefaultVisitListenerProvider.providers(newVisitListeners));
    }

    @Override
    public final Configuration derive(VisitListenerProvider... newVisitListenerProviders) {
        return new DefaultConfiguration(
            connectionProvider,
            interpreterConnectionProvider,
            systemConnectionProvider,
            metaProvider,
            executorProvider,
            transactionProvider,
            recordMapperProvider,
            recordUnmapperProvider,
            recordListenerProviders,
            executeListenerProviders,
            newVisitListenerProviders,
            transactionListenerProviders,
            diagnosticsListenerProviders,
            unwrapperProvider,
            converterProvider,

            clock,

            dialect,
            settings,
            data
        );
    }

    @Override
    public final Configuration derive(TransactionListener... newTransactionListeners) {
        return derive(DefaultTransactionListenerProvider.providers(newTransactionListeners));
    }

    @Override
    public final Configuration derive(TransactionListenerProvider... newTransactionListenerProviders) {
        return new DefaultConfiguration(
            connectionProvider,
            interpreterConnectionProvider,
            systemConnectionProvider,
            metaProvider,
            executorProvider,
            transactionProvider,
            recordMapperProvider,
            recordUnmapperProvider,
            recordListenerProviders,
            executeListenerProviders,
            visitListenerProviders,
            newTransactionListenerProviders,
            diagnosticsListenerProviders,
            unwrapperProvider,
            converterProvider,

            clock,

            dialect,
            settings,
            data
        );
    }

    @Override
    public final Configuration derive(DiagnosticsListener... newDiagnosticsListeners) {
        return derive(DefaultDiagnosticsListenerProvider.providers(newDiagnosticsListeners));
    }

    @Override
    public final Configuration derive(DiagnosticsListenerProvider... newDiagnosticsListenerProviders) {
        return new DefaultConfiguration(
            connectionProvider,
            interpreterConnectionProvider,
            systemConnectionProvider,
            metaProvider,
            executorProvider,
            transactionProvider,
            recordMapperProvider,
            recordUnmapperProvider,
            recordListenerProviders,
            executeListenerProviders,
            visitListenerProviders,
            transactionListenerProviders,
            newDiagnosticsListenerProviders,
            unwrapperProvider,
            converterProvider,

            clock,

            dialect,
            settings,
            data
        );
    }

    @Override
    public final Configuration derive(Unwrapper newUnwrapper) {
        return derive(new UnwrapperWrapper(newUnwrapper));
    }

    @Override
    public final Configuration derive(UnwrapperProvider newUnwrapperProvider) {
        return new DefaultConfiguration(
            connectionProvider,
            interpreterConnectionProvider,
            systemConnectionProvider,
            metaProvider,
            executorProvider,
            transactionProvider,
            recordMapperProvider,
            recordUnmapperProvider,
            recordListenerProviders,
            executeListenerProviders,
            visitListenerProviders,
            transactionListenerProviders,
            diagnosticsListenerProviders,
            newUnwrapperProvider,
            converterProvider,

            clock,

            dialect,
            settings,
            data
        );
    }

    @Override
    public final Configuration derive(ConverterProvider newConverterProvider) {
        return new DefaultConfiguration(
            connectionProvider,
            interpreterConnectionProvider,
            systemConnectionProvider,
            metaProvider,
            executorProvider,
            transactionProvider,
            recordMapperProvider,
            recordUnmapperProvider,
            recordListenerProviders,
            executeListenerProviders,
            visitListenerProviders,
            transactionListenerProviders,
            diagnosticsListenerProviders,
            unwrapperProvider,
            newConverterProvider,

            clock,

            dialect,
            settings,
            data
        );
    }


    @Override
    public final Configuration derive(Clock newClock) {
        return new DefaultConfiguration(
            connectionProvider,
            interpreterConnectionProvider,
            systemConnectionProvider,
            metaProvider,
            executorProvider,
            transactionProvider,
            recordMapperProvider,
            recordUnmapperProvider,
            recordListenerProviders,
            executeListenerProviders,
            visitListenerProviders,
            transactionListenerProviders,
            diagnosticsListenerProviders,
            unwrapperProvider,
            converterProvider,
            newClock,
            dialect,
            settings,
            data
        );
    }


    @Override
    public final Configuration derive(SQLDialect newDialect) {
        return new DefaultConfiguration(
            connectionProvider,
            interpreterConnectionProvider,
            systemConnectionProvider,
            metaProvider,
            executorProvider,
            transactionProvider,
            recordMapperProvider,
            recordUnmapperProvider,
            recordListenerProviders,
            executeListenerProviders,
            visitListenerProviders,
            transactionListenerProviders,
            diagnosticsListenerProviders,
            unwrapperProvider,
            converterProvider,

            clock,

            newDialect,
            settings,
            data
        );
    }

    @Override
    public final Configuration derive(Settings newSettings) {
        return new DefaultConfiguration(
            connectionProvider,
            interpreterConnectionProvider,
            systemConnectionProvider,
            metaProvider,
            executorProvider,
            transactionProvider,
            recordMapperProvider,
            recordUnmapperProvider,
            recordListenerProviders,
            executeListenerProviders,
            visitListenerProviders,
            transactionListenerProviders,
            diagnosticsListenerProviders,
            unwrapperProvider,
            converterProvider,

            clock,

            dialect,
            newSettings,
            data
        );
    }

    // -------------------------------------------------------------------------
    // XXX: Changing configurations
    // -------------------------------------------------------------------------

    @Override
    public final Configuration set(Connection newConnection) {
        return set(new DefaultConnectionProvider(newConnection));
    }

    @Override
    public final Configuration set(DataSource newDataSource) {
        return set(new DataSourceConnectionProvider(newDataSource));
    }

    @Override
    public final Configuration set(ConnectionProvider newConnectionProvider) {
        if (newConnectionProvider != null) {

            // [#5388] TODO Factor out this API in a more formal contract between TransactionProvider and ConnectionProvider
            if (transactionProvider instanceof ThreadLocalTransactionProvider &&
              !(newConnectionProvider instanceof ThreadLocalConnectionProvider))
                throw new ConfigurationException("Cannot specify custom ConnectionProvider when Configuration contains a ThreadLocalTransactionProvider");

            this.connectionProvider = newConnectionProvider;
        }
        else {
            this.connectionProvider = new NoConnectionProvider();
        }

        return this;
    }

    @Override
    public final Configuration set(MetaProvider newMetaProvider) {
        this.metaProvider = newMetaProvider;
        return this;
    }

    @Override
    public final Configuration set(Executor newExecutor) {
        return set(new ExecutorWrapper(newExecutor));
    }

    @Override
    public final Configuration set(ExecutorProvider newExecutorProvider) {
        this.executorProvider = newExecutorProvider;
        return this;
    }

    @Override
    public final Configuration set(TransactionProvider newTransactionProvider) {
        if (newTransactionProvider != null) {
            this.transactionProvider = newTransactionProvider;

            if (newTransactionProvider instanceof ThreadLocalTransactionProvider)
                this.connectionProvider = ((ThreadLocalTransactionProvider) newTransactionProvider).localConnectionProvider;
        }
        else {
            this.transactionProvider = new NoTransactionProvider();
        }

        return this;
    }

    @Override
    public final Configuration set(RecordMapper<?, ?> newRecordMapper) {
        return set(new RecordMapperWrapper(newRecordMapper));
    }

    @Override
    public final Configuration set(RecordMapperProvider newRecordMapperProvider) {
        this.recordMapperProvider = newRecordMapperProvider;
        return this;
    }

    @Override
    public final Configuration set(RecordUnmapper<?, ?> newRecordUnmapper) {
        return set(new RecordUnmapperWrapper(newRecordUnmapper));
    }

    @Override
    public final Configuration set(RecordUnmapperProvider newRecordUnmapperProvider) {
        this.recordUnmapperProvider = newRecordUnmapperProvider;
        return this;
    }

    @Override
    public final Configuration set(RecordListener... newRecordListeners) {
        return set(DefaultRecordListenerProvider.providers(newRecordListeners));
    }

    @Override
    public final Configuration set(RecordListenerProvider... newRecordListenerProviders) {
        this.recordListenerProviders = newRecordListenerProviders != null
            ? newRecordListenerProviders
            : new RecordListenerProvider[0];

        return this;
    }

    @Override
    public final Configuration set(ExecuteListener... newExecuteListeners) {
        return set(DefaultExecuteListenerProvider.providers(newExecuteListeners));
    }

    @Override
    public final Configuration set(ExecuteListenerProvider... newExecuteListenerProviders) {
        this.executeListenerProviders = newExecuteListenerProviders != null
            ? newExecuteListenerProviders
            : new ExecuteListenerProvider[0];

        return this;
    }

    @Override
    public final Configuration set(VisitListener... newVisitListeners) {
        return set(DefaultVisitListenerProvider.providers(newVisitListeners));
    }

    @Override
    public final Configuration set(VisitListenerProvider... newVisitListenerProviders) {
        this.visitListenerProviders = newVisitListenerProviders != null
            ? newVisitListenerProviders
            : new VisitListenerProvider[0];

        return this;
    }

    @Override
    public final Configuration set(TransactionListener... newTransactionListeners) {
        return set(DefaultTransactionListenerProvider.providers(newTransactionListeners));
    }

    @Override
    public final Configuration set(TransactionListenerProvider... newTransactionListenerProviders) {
        this.transactionListenerProviders = newTransactionListenerProviders != null
            ? newTransactionListenerProviders
            : new TransactionListenerProvider[0];

        return this;
    }

    @Override
    public final Configuration set(DiagnosticsListener... newDiagnosticsListeners) {
        return set(DefaultDiagnosticsListenerProvider.providers(newDiagnosticsListeners));
    }

    @Override
    public final Configuration set(DiagnosticsListenerProvider... newDiagnosticsListenerProviders) {
        this.diagnosticsListenerProviders = newDiagnosticsListenerProviders != null
            ? newDiagnosticsListenerProviders
            : new DiagnosticsListenerProvider[0];

        return this;
    }

    @Override
    public final Configuration set(Unwrapper newUnwrapper) {
        return newUnwrapper != null
             ? set(new UnwrapperWrapper(newUnwrapper))
             : set((UnwrapperProvider) null) ;
    }

    @Override
    public final Configuration set(UnwrapperProvider newUnwrapperProvider) {
        this.unwrapperProvider = newUnwrapperProvider;
        return this;
    }

    @Override
    public final Configuration set(ConverterProvider newConverterProvider) {
        this.converterProvider = newConverterProvider != null
            ? newConverterProvider
            : new DefaultConverterProvider();

        return this;
    }


    @Override
    public final Configuration set(Clock newClock) {

        // [#6447] Defaulting to UTC system time
        this.clock = newClock == null ? Clock.systemUTC() : newClock;
        return this;
    }


    @Override
    public final Configuration set(SQLDialect newDialect) {

        // [#6274] The reported dialect should never be null
        this.dialect = newDialect == null ? SQLDialect.DEFAULT : newDialect;
        return this;
    }

    @Override
    public final Configuration set(Settings newSettings) {
        this.settings = newSettings != null
            ? SettingsTools.clone(newSettings)
            : SettingsTools.defaultSettings();

        this.mapping = new org.jooq.SchemaMapping(this);
        return this;
    }

    // -------------------------------------------------------------------------
    // XXX: Changing configurations via JavaBeans-style setters
    // -------------------------------------------------------------------------

    /**
     * @see #set(Connection)
     */
    public final void setConnection(Connection newConnection) {
        set(newConnection);
    }

    /**
     * @see #set(DataSource)
     */
    public final void setDataSource(DataSource newDataSource) {
        set(newDataSource);
    }

    /**
     * @see #set(ConnectionProvider)
     */
    public final void setConnectionProvider(ConnectionProvider newConnectionProvider) {
        set(newConnectionProvider);
    }

    /**
     * @see #set(ConnectionProvider)
     */
    public final void setInterpreterConnectionProvider(ConnectionProvider newInterpreterConnectionProvider) {
        // TODO: [#9460] Should we have setters in the Configuration API as well?
        //       See https://github.com/jOOQ/jOOQ/issues/9460#issuecomment-547973317
        this.interpreterConnectionProvider = newInterpreterConnectionProvider;
    }

    /**
     * @see #set(ConnectionProvider)
     */
    public final void setSystemConnectionProvider(ConnectionProvider newSystemConnectionProvider) {
        // TODO: [#9460] Should we have setters in the Configuration API as well?
        //       See https://github.com/jOOQ/jOOQ/issues/9460#issuecomment-547973317
        this.systemConnectionProvider = newSystemConnectionProvider;
    }

    /**
     * @see #set(MetaProvider)
     */
    public final void setMetaProvider(MetaProvider newMetaProvider) {
        set(newMetaProvider);
    }

    /**
     * @see #set(Executor)
     */
    public final void setExecutor(Executor newExecutor) {
        set(newExecutor);
    }

    /**
     * @see #set(ExecutorProvider)
     */
    public final void setExecutorProvider(ExecutorProvider newExecutorProvider) {
        set(newExecutorProvider);
    }

    /**
     * @see #set(TransactionProvider)
     */
    public final void setTransactionProvider(TransactionProvider newTransactionProvider) {
        set(newTransactionProvider);
    }

    /**
     * @see #set(RecordMapper)
     */
    public final void setRecordMapper(RecordMapper<?, ?> newRecordMapper) {
        set(newRecordMapper);
    }

    /**
     * @see #set(RecordMapperProvider)
     */
    public final void setRecordMapperProvider(RecordMapperProvider newRecordMapperProvider) {
        set(newRecordMapperProvider);
    }

    /**
     * @see #set(RecordUnmapper)
     */
    public final void setRecordUnmapper(RecordUnmapper<?, ?> newRecordUnmapper) {
        set(newRecordUnmapper);
    }

    /**
     * @see #set(RecordUnmapperProvider)
     */
    public final void setRecordUnmapperProvider(RecordUnmapperProvider newRecordUnmapperProvider) {
        set(newRecordUnmapperProvider);
    }

    /**
     * @see #set(RecordListener[])
     */
    public final void setRecordListener(RecordListener... newRecordListeners) {
        set(newRecordListeners);
    }

    /**
     * @see #set(RecordListenerProvider[])
     */
    public final void setRecordListenerProvider(RecordListenerProvider... newRecordListenerProviders) {
        set(newRecordListenerProviders);
    }

    /**
     * @see #set(ExecuteListener[])
     */
    public final void setExecuteListener(ExecuteListener... newExecuteListeners) {
        set(newExecuteListeners);
    }

    /**
     * @see #set(ExecuteListenerProvider[])
     */
    public final void setExecuteListenerProvider(ExecuteListenerProvider... newExecuteListenerProviders) {
        set(newExecuteListenerProviders);
    }

    /**
     * @see #set(VisitListener[])
     */
    public final void setVisitListener(VisitListener... newVisitListeners) {
        set(newVisitListeners);
    }

    /**
     * @see #set(VisitListenerProvider[])
     */
    public final void setVisitListenerProvider(VisitListenerProvider... newVisitListenerProviders) {
        set(newVisitListenerProviders);
    }

    /**
     * @see #set(TransactionListener[])
     */
    public final void setTransactionListener(TransactionListener... newTransactionListeners) {
        set(newTransactionListeners);
    }

    /**
     * @see #set(TransactionListenerProvider[])
     */
    public final void setTransactionListenerProvider(TransactionListenerProvider... newTransactionListenerProviders) {
        set(newTransactionListenerProviders);
    }

    /**
     * @see #set(DiagnosticsListener[])
     */
    public final void setDiagnosticsListener(DiagnosticsListener... newDiagnosticsListener) {
        set(newDiagnosticsListener);
    }

    /**
     * @see #set(DiagnosticsListenerProvider[])
     */
    public final void setDiagnosticsListenerProvider(DiagnosticsListenerProvider... newDiagnosticsListenerProviders) {
        set(newDiagnosticsListenerProviders);
    }

    /**
     * @see #set(Unwrapper)
     */
    public final void setUnwrapper(Unwrapper newUnwrapper) {
        set(newUnwrapper);
    }

    /**
     * @see #set(UnwrapperProvider)
     */
    public final void setUnwrapperProvider(UnwrapperProvider newUnwrapperProvider) {
        set(newUnwrapperProvider);
    }


    /**
     * @see #set(Clock)
     */
    public final void setClock(Clock newClock) {
        set(newClock);
    }


    /**
     * @see #set(SQLDialect)
     */
    public final void setSQLDialect(SQLDialect newDialect) {
        set(newDialect);
    }

    /**
     * @see #set(Settings)
     */
    public final void setSettings(Settings newSettings) {
        set(newSettings);
    }

    // -------------------------------------------------------------------------
    // XXX: Getters
    // -------------------------------------------------------------------------

    @Override
    public final ConnectionProvider connectionProvider() {

        // [#3229] [#5377] If we're currently in a transaction, return that transaction's
        // local DefaultConnectionProvider, not the one from this configuration
        TransactionProvider tp = transactionProvider();
        ConnectionProvider transactional = tp instanceof ThreadLocalTransactionProvider
            ? ((ThreadLocalTransactionProvider) tp).localConnectionProvider
            : (ConnectionProvider) data(DATA_DEFAULT_TRANSACTION_PROVIDER_CONNECTION);

        return transactional == null ? connectionProvider : transactional;
    }

    @Override
    public final ConnectionProvider interpreterConnectionProvider() {
        return interpreterConnectionProvider != null
             ? interpreterConnectionProvider
             : new DefaultInterpreterConnectionProvider(this);
    }

    @Override
    public final ConnectionProvider systemConnectionProvider() {
        return systemConnectionProvider != null
             ? systemConnectionProvider
             : connectionProvider();
    }

    @Override
    public final MetaProvider metaProvider() {
        return metaProvider != null
             ? metaProvider
             : new DefaultMetaProvider(this);
    }

    @Override
    public final ExecutorProvider executorProvider() {
        return executorProvider != null
             ? executorProvider
             : new DefaultExecutorProvider();
    }

    @Override
    public final TransactionProvider transactionProvider() {

        // [#3229] If transactions are used in client code, the default behaviour
        // is assumed automatically, for convenience.
        if (transactionProvider instanceof NoTransactionProvider) {
            return new DefaultTransactionProvider(connectionProvider);
        }

        return transactionProvider;
    }

    @Override
    public final RecordMapperProvider recordMapperProvider() {

        // [#3915] Avoid permanently referencing such a DefaultRecordMapperProvider from this
        // DefaultConfiguration to prevent memory leaks.
        return recordMapperProvider != null
             ? recordMapperProvider
             : new DefaultRecordMapperProvider(this);
    }

    @Override
    public final RecordUnmapperProvider recordUnmapperProvider() {

        // [#3915] Avoid permanently referencing such a DefaultRecordUnmapperProvider from this
        // DefaultConfiguration to prevent memory leaks.
        return recordUnmapperProvider != null
             ? recordUnmapperProvider
             : new DefaultRecordUnmapperProvider(this);
    }

    @Override
    public final RecordListenerProvider[] recordListenerProviders() {
        return recordListenerProviders;
    }

    @Override
    public final ExecuteListenerProvider[] executeListenerProviders() {
        return executeListenerProviders;
    }

    @Override
    public final VisitListenerProvider[] visitListenerProviders() {
        return visitListenerProviders;
    }

    @Override
    public final TransactionListenerProvider[] transactionListenerProviders() {
        return transactionListenerProviders;
    }

    @Override
    public final DiagnosticsListenerProvider[] diagnosticsListenerProviders() {
        return diagnosticsListenerProviders;
    }

    @Override
    public final UnwrapperProvider unwrapperProvider() {
        return unwrapperProvider != null
             ? unwrapperProvider
             : new DefaultUnwrapperProvider();
    }

    @Override
    public final ConverterProvider converterProvider() {
        return converterProvider;
    }


    @Override
    public final Clock clock() {
        return clock;
    }


    @Override
    public final SQLDialect dialect() {
        return dialect;
    }

    @Override
    public final SQLDialect family() {
        return dialect.family();
    }

    @Override
    public final Settings settings() {
        return settings;
    }

    @Override
    public final ConcurrentHashMap<Object, Object> data() {
        return data;
    }

    @Override
    public final Object data(Object key) {
        return data.get(key);
    }

    @Override
    public final Object data(Object key, Object value) {
        return data.put(key, value);
    }

    @Override
    @Deprecated
    public final org.jooq.SchemaMapping schemaMapping() {
        return mapping;
    }

    @Override
    public String toString() {
        return "DefaultConfiguration " +
            "[\n\tconnected=" + (connectionProvider != null && !(connectionProvider instanceof NoConnectionProvider)) +
            ",\n\ttransactional=" + (transactionProvider != null && !(transactionProvider instanceof NoTransactionProvider)) +
            ",\n\tdialect=" + dialect +
            ",\n\tdata=" + data +
            ",\n\tsettings=\n\t\t" + settings +
            "\n]";
    }

    // -------------------------------------------------------------------------
    // XXX: Serialisation
    // -------------------------------------------------------------------------

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();

        // Allow these objects to be non-serializable
        oos.writeObject(connectionProvider instanceof Serializable
            ? connectionProvider
            : null);
        oos.writeObject(interpreterConnectionProvider instanceof Serializable
            ? interpreterConnectionProvider
            : null);
        oos.writeObject(systemConnectionProvider instanceof Serializable
            ? systemConnectionProvider
            : null);
        oos.writeObject(metaProvider instanceof Serializable
            ? metaProvider
            : null);
        oos.writeObject(transactionProvider instanceof Serializable
            ? transactionProvider
            : null);
        oos.writeObject(recordMapperProvider instanceof Serializable
            ? recordMapperProvider
            : null);
        oos.writeObject(recordUnmapperProvider instanceof Serializable
            ? recordUnmapperProvider
            : null);

        oos.writeObject(cloneSerializables(executeListenerProviders));
        oos.writeObject(cloneSerializables(recordListenerProviders));
        oos.writeObject(cloneSerializables(visitListenerProviders));
        oos.writeObject(cloneSerializables(transactionListenerProviders));
        oos.writeObject(cloneSerializables(diagnosticsListenerProviders));

        oos.writeObject(unwrapperProvider instanceof Serializable
            ? unwrapperProvider
            : null);

        oos.writeObject(converterProvider instanceof Serializable
            ? converterProvider
            : null);

        // [#7062] Exclude reflection cache from serialisation
        for (Entry<Object, Object> entry : data.entrySet()) {
            if (entry.getKey() instanceof DataCacheKey)
                continue;

            oos.writeObject(entry.getKey());
            oos.writeObject(entry.getValue());
        }

        // [#7062] End of Map marker
        oos.writeObject(END_OF_MAP_MARKER);
    }

    private static final String END_OF_MAP_MARKER = "EOM";

    private <E> E[] cloneSerializables(E[] array) {
        E[] clone = array.clone();

        for (int i = 0; i < clone.length; i++) {
            if (!(clone[i] instanceof Serializable)) {
                clone[i] = null;
            }
        }

        return clone;
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();

        connectionProvider = (ConnectionProvider) ois.readObject();
        interpreterConnectionProvider = (ConnectionProvider) ois.readObject();
        systemConnectionProvider = (ConnectionProvider) ois.readObject();
        metaProvider = (MetaProvider) ois.readObject();
        transactionProvider = (TransactionProvider) ois.readObject();
        recordMapperProvider = (RecordMapperProvider) ois.readObject();
        recordUnmapperProvider = (RecordUnmapperProvider) ois.readObject();
        executeListenerProviders = (ExecuteListenerProvider[]) ois.readObject();
        recordListenerProviders = (RecordListenerProvider[]) ois.readObject();
        visitListenerProviders = (VisitListenerProvider[]) ois.readObject();
        transactionListenerProviders = (TransactionListenerProvider[]) ois.readObject();
        diagnosticsListenerProviders = (DiagnosticsListenerProvider[]) ois.readObject();
        unwrapperProvider = (UnwrapperProvider) ois.readObject();
        converterProvider = (ConverterProvider) ois.readObject();
        data = new ConcurrentHashMap<>();

        Object key;
        Object value;

        while (!END_OF_MAP_MARKER.equals(key = ois.readObject())) {
            value = ois.readObject();
            data.put(key, value);
        }
    }

    private final class ExecutorWrapper implements ExecutorProvider {
        private final Executor newExecutor;

        private ExecutorWrapper(Executor newExecutor) {
            this.newExecutor = newExecutor;
        }

        @Override
        public Executor provide() {
            return newExecutor;
        }
    }

    private final class RecordMapperWrapper implements RecordMapperProvider {
        private final RecordMapper<?, ?> newRecordMapper;

        private RecordMapperWrapper(RecordMapper<?, ?> newRecordMapper) {
            this.newRecordMapper = newRecordMapper;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <R extends Record, E> RecordMapper<R, E> provide(RecordType<R> recordType, Class<? extends E> type) {
            return (RecordMapper<R, E>) newRecordMapper;
        }
    }

    private final class RecordUnmapperWrapper implements RecordUnmapperProvider {
        private final RecordUnmapper<?, ?> newRecordUnmapper;

        private RecordUnmapperWrapper(RecordUnmapper<?, ?> newRecordUnmapper) {
            this.newRecordUnmapper = newRecordUnmapper;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <E, R extends Record> RecordUnmapper<E, R> provide(Class<? extends E> type, RecordType<R> recordType) {
            return (RecordUnmapper<E, R>) newRecordUnmapper;
        }
    }

    private final class UnwrapperWrapper implements UnwrapperProvider {
        private final Unwrapper newUnwrapper;

        private UnwrapperWrapper(Unwrapper newUnwrapper) {
            this.newUnwrapper = newUnwrapper;
        }

        @Override
        public Unwrapper provide() {
            return newUnwrapper;
        }
    }
}
