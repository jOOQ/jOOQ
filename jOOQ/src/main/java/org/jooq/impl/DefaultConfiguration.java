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
package org.jooq.impl;

import static org.jooq.SQLDialect.DEFAULT;
import static org.jooq.impl.Tools.DataKey.DATA_DEFAULT_TRANSACTION_PROVIDER_CONNECTION;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.sql.Connection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

import javax.sql.DataSource;
import javax.xml.bind.JAXB;

import org.jooq.Configuration;
import org.jooq.ConnectionProvider;
import org.jooq.ConverterProvider;
import org.jooq.DSLContext;
import org.jooq.ExecuteListener;
import org.jooq.ExecuteListenerProvider;
import org.jooq.ExecutorProvider;
import org.jooq.Record;
import org.jooq.RecordListener;
import org.jooq.RecordListenerProvider;
import org.jooq.RecordMapper;
import org.jooq.RecordMapperProvider;
import org.jooq.RecordType;
import org.jooq.SQLDialect;
import org.jooq.TransactionListener;
import org.jooq.TransactionListenerProvider;
import org.jooq.TransactionProvider;
import org.jooq.VisitListener;
import org.jooq.VisitListenerProvider;
import org.jooq.conf.Settings;
import org.jooq.conf.SettingsTools;
import org.jooq.exception.ConfigurationException;
import org.jooq.impl.ThreadLocalTransactionProvider.ThreadLocalConnectionProvider;

/**
 * A default implementation for configurations within a {@link DSLContext}, if no
 * custom configuration was supplied to {@link DSL#using(Configuration)}.
 * <p>
 * The <code>DefaultConfiguration</code>
 *
 * @author Lukas Eder
 */
@SuppressWarnings("deprecation")
public class DefaultConfiguration implements Configuration {

    /**
     * Serial version UID
     */
    private static final long                       serialVersionUID = 8193158984283234708L;

    // Configuration objects
    private SQLDialect                              dialect;
    private Settings                                settings;
    private ConcurrentHashMap<Object, Object>       data;

    // Non-serializable Configuration objects
    private transient ConnectionProvider            connectionProvider;
    private transient ExecutorProvider              executorProvider;
    private transient TransactionProvider           transactionProvider;
    private transient RecordMapperProvider          recordMapperProvider;
    private transient RecordListenerProvider[]      recordListenerProviders;
    private transient ExecuteListenerProvider[]     executeListenerProviders;
    private transient VisitListenerProvider[]       visitListenerProviders;
    private transient TransactionListenerProvider[] transactionListenerProviders;
    private transient ConverterProvider             converterProvider;

    // Derived objects
    private org.jooq.SchemaMapping                  mapping;

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
    DefaultConfiguration(Configuration configuration) {
        this(
            configuration.connectionProvider(),
            configuration.executorProvider(),
            configuration.transactionProvider(),
            configuration.recordMapperProvider(),
            configuration.recordListenerProviders(),
            configuration.executeListenerProviders(),
            configuration.visitListenerProviders(),
            configuration.transactionListenerProviders(),
            configuration.converterProvider(),
            configuration.dialect(),
            configuration.settings(),
            configuration.data()
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
            executeListenerProviders,
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
            recordMapperProvider,
            null,
            executeListenerProviders,
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
            recordMapperProvider,
            recordListenerProviders,
            executeListenerProviders,
            visitListenerProviders,
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
            transactionProvider,
            recordMapperProvider,
            recordListenerProviders,
            executeListenerProviders,
            visitListenerProviders,
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
            transactionProvider,
            recordMapperProvider,
            recordListenerProviders,
            executeListenerProviders,
            visitListenerProviders,
            null,
            converterProvider,
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
            executorProvider,
            transactionProvider,
            recordMapperProvider,
            recordListenerProviders,
            executeListenerProviders,
            visitListenerProviders,
            null,
            converterProvider,
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
        set(connectionProvider);
        set(executorProvider);
        set(transactionProvider);
        set(recordMapperProvider);
        set(recordListenerProviders);
        set(executeListenerProviders);
        set(visitListenerProviders);
        set(transactionListenerProviders);
        set(converterProvider);
        set(dialect);
        set(settings);

        this.data = data != null
            ? new ConcurrentHashMap<Object, Object>(data)
            : new ConcurrentHashMap<Object, Object>();
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
            executorProvider,
            transactionProvider,
            recordMapperProvider,
            recordListenerProviders,
            executeListenerProviders,
            visitListenerProviders,
            transactionListenerProviders,
            converterProvider,
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
            newExecutorProvider,
            transactionProvider,
            recordMapperProvider,
            recordListenerProviders,
            executeListenerProviders,
            visitListenerProviders,
            transactionListenerProviders,
            converterProvider,
            dialect,
            settings,
            data
        );
    }

    @Override
    public final Configuration derive(TransactionProvider newTransactionProvider) {
        return new DefaultConfiguration(
            connectionProvider,
            executorProvider,
            newTransactionProvider,
            recordMapperProvider,
            recordListenerProviders,
            executeListenerProviders,
            visitListenerProviders,
            transactionListenerProviders,
            converterProvider,
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
            executorProvider,
            transactionProvider,
            newRecordMapperProvider,
            recordListenerProviders,
            executeListenerProviders,
            visitListenerProviders,
            transactionListenerProviders,
            converterProvider,
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
            executorProvider,
            transactionProvider,
            recordMapperProvider,
            newRecordListenerProviders,
            executeListenerProviders,
            visitListenerProviders,
            transactionListenerProviders,
            converterProvider,
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
            executorProvider,
            transactionProvider,
            recordMapperProvider,
            recordListenerProviders,
            newExecuteListenerProviders,
            visitListenerProviders,
            transactionListenerProviders,
            converterProvider,
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
            executorProvider,
            transactionProvider,
            recordMapperProvider,
            recordListenerProviders,
            executeListenerProviders,
            newVisitListenerProviders,
            transactionListenerProviders,
            converterProvider,
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
            executorProvider,
            transactionProvider,
            recordMapperProvider,
            recordListenerProviders,
            executeListenerProviders,
            visitListenerProviders,
            newTransactionListenerProviders,
            converterProvider,
            dialect,
            settings,
            data
        );
    }

    @Override
    public final Configuration derive(ConverterProvider newConverterProvider) {
        return new DefaultConfiguration(
            connectionProvider,
            executorProvider,
            transactionProvider,
            recordMapperProvider,
            recordListenerProviders,
            executeListenerProviders,
            visitListenerProviders,
            transactionListenerProviders,
            newConverterProvider,
            dialect,
            settings,
            data
        );
    }

    @Override
    public final Configuration derive(SQLDialect newDialect) {
        return new DefaultConfiguration(
            connectionProvider,
            executorProvider,
            transactionProvider,
            recordMapperProvider,
            recordListenerProviders,
            executeListenerProviders,
            visitListenerProviders,
            transactionListenerProviders,
            converterProvider,
            newDialect,
            settings,
            data
        );
    }

    @Override
    public final Configuration derive(Settings newSettings) {
        return new DefaultConfiguration(
            connectionProvider,
            executorProvider,
            transactionProvider,
            recordMapperProvider,
            recordListenerProviders,
            executeListenerProviders,
            visitListenerProviders,
            transactionListenerProviders,
            converterProvider,
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
    public Configuration set(RecordMapper<?, ?> newRecordMapper) {
        return set(new RecordMapperWrapper(newRecordMapper));
    }

    @Override
    public final Configuration set(RecordMapperProvider newRecordMapperProvider) {
        this.recordMapperProvider = newRecordMapperProvider;
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
    public final Configuration set(ConverterProvider newConverterProvider) {
        this.converterProvider = newConverterProvider != null
            ? newConverterProvider
            : new DefaultConverterProvider();

        return this;
    }

    @Override
    public final Configuration set(SQLDialect newDialect) {
        this.dialect = newDialect;
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
     * @see #set(RecordMapperProvider)
     */
    public final void setRecordMapperProvider(RecordMapperProvider newRecordMapperProvider) {
        set(newRecordMapperProvider);
    }

    /**
     * @see #set(RecordListenerProvider[])
     */
    public final void setRecordListenerProvider(RecordListenerProvider... newRecordListenerProviders) {
        set(newRecordListenerProviders);
    }

    /**
     * @see #set(ExecuteListenerProvider[])
     */
    public final void setExecuteListenerProvider(ExecuteListenerProvider... newExecuteListenerProviders) {
        set(newExecuteListenerProviders);
    }

    /**
     * @see #set(VisitListenerProvider[])
     */
    public final void setVisitListenerProvider(VisitListenerProvider... newVisitListenerProviders) {
        set(newVisitListenerProviders);
    }

    /**
     * @see #set(TransactionListenerProvider[])
     */
    public final void setTransactionListenerProvider(TransactionListenerProvider... newTransactionListenerProviders) {
        set(newTransactionListenerProviders);
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
    public final ConverterProvider converterProvider() {
        return converterProvider;
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
        StringWriter writer = new StringWriter();
        JAXB.marshal(settings, writer);

        return "DefaultConfiguration " +
            "[\n\tconnected=" + (connectionProvider != null && !(connectionProvider instanceof NoConnectionProvider)) +
            ",\n\ttransactional=" + (transactionProvider != null && !(transactionProvider instanceof NoTransactionProvider)) +
            ",\n\tdialect=" + dialect +
            ",\n\tdata=" + data +
            ",\n\tsettings=\n\t\t" + writer.toString().trim().replace("\n", "\n\t\t") +
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
        oos.writeObject(transactionProvider instanceof Serializable
            ? transactionProvider
            : null);
        oos.writeObject(recordMapperProvider instanceof Serializable
            ? recordMapperProvider
            : null);

        oos.writeObject(cloneSerializables(executeListenerProviders));
        oos.writeObject(cloneSerializables(recordListenerProviders));
        oos.writeObject(cloneSerializables(visitListenerProviders));
        oos.writeObject(cloneSerializables(transactionListenerProviders));

        oos.writeObject(converterProvider instanceof Serializable
            ? converterProvider
            : null);
    }

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
        transactionProvider = (TransactionProvider) ois.readObject();
        recordMapperProvider = (RecordMapperProvider) ois.readObject();
        executeListenerProviders = (ExecuteListenerProvider[]) ois.readObject();
        recordListenerProviders = (RecordListenerProvider[]) ois.readObject();
        visitListenerProviders = (VisitListenerProvider[]) ois.readObject();
        transactionListenerProviders = (TransactionListenerProvider[]) ois.readObject();
        converterProvider = (ConverterProvider) ois.readObject();
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
}
