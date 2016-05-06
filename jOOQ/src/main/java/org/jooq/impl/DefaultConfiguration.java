/**
 * Copyright (c) 2009-2016, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
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
import java.io.StringWriter;
import java.sql.Connection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;
import javax.xml.bind.JAXB;

import org.jooq.Configuration;
import org.jooq.ConnectionProvider;
import org.jooq.ConverterProvider;
import org.jooq.DSLContext;
import org.jooq.ExecuteListenerProvider;
import org.jooq.RecordListenerProvider;
import org.jooq.RecordMapperProvider;
import org.jooq.SQLDialect;
import org.jooq.TransactionProvider;
import org.jooq.VisitListenerProvider;
import org.jooq.conf.Settings;
import org.jooq.conf.SettingsTools;

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
    private static final long                   serialVersionUID = 8193158984283234708L;

    // Configuration objects
    private SQLDialect                          dialect;
    private Settings                            settings;
    private ConcurrentHashMap<Object, Object>   data;

    // Non-serializable Configuration objects
    private transient ConnectionProvider        connectionProvider;
    private transient TransactionProvider       transactionProvider;
    private transient RecordMapperProvider      recordMapperProvider;
    private transient RecordListenerProvider[]  recordListenerProviders;
    private transient ExecuteListenerProvider[] executeListenerProviders;
    private transient VisitListenerProvider[]   visitListenerProviders;
    private transient ConverterProvider         converterProvider;

    // Derived objects
    private org.jooq.SchemaMapping              mapping;

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
            configuration.transactionProvider(),
            configuration.recordMapperProvider(),
            configuration.recordListenerProviders(),
            configuration.executeListenerProviders(),
            configuration.visitListenerProviders(),
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
     *             {@link #DefaultConfiguration(ConnectionProvider, TransactionProvider, RecordMapperProvider, RecordListenerProvider[], ExecuteListenerProvider[], VisitListenerProvider[], SQLDialect, Settings, Map)}
     *             instead. This constructor is maintained to provide jOOQ 3.0 backwards-compatibility if called with reflection from Spring configurations.
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
            executeListenerProviders,
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
     *             {@link #DefaultConfiguration(ConnectionProvider, TransactionProvider, RecordMapperProvider, RecordListenerProvider[], ExecuteListenerProvider[], VisitListenerProvider[], SQLDialect, Settings, Map)}
     *             instead. This constructor is maintained to provide jOOQ 3.1 backwards-compatibility if called with reflection from Spring configurations.
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
            recordMapperProvider,
            null,
            executeListenerProviders,
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
     *             {@link #DefaultConfiguration(ConnectionProvider, TransactionProvider, RecordMapperProvider, RecordListenerProvider[], ExecuteListenerProvider[], VisitListenerProvider[], SQLDialect, Settings, Map)}
     *             instead. This constructor is maintained to provide jOOQ 3.2, 3.3 backwards-compatibility if called with reflection from Spring configurations.
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
            recordMapperProvider,
            recordListenerProviders,
            executeListenerProviders,
            visitListenerProviders,
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
     *             {@link #DefaultConfiguration(ConnectionProvider, TransactionProvider, RecordMapperProvider, RecordListenerProvider[], ExecuteListenerProvider[], VisitListenerProvider[], ConverterProvider, SQLDialect, Settings, Map)}
     *             instead. This constructor is maintained to provide jOOQ 3.2, 3.3 backwards-compatibility if called with reflection from Spring configurations.
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
            transactionProvider,
            recordMapperProvider,
            recordListenerProviders,
            executeListenerProviders,
            visitListenerProviders,
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
     */
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
        set(connectionProvider);
        set(transactionProvider);
        set(recordMapperProvider);
        set(recordListenerProviders);
        set(executeListenerProviders);
        set(visitListenerProviders);
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

    /**
     * {@inheritDoc}
     */
    @Override
    public final Configuration derive() {
        return new DefaultConfiguration(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Configuration derive(Connection newConnection) {
        return derive(new DefaultConnectionProvider(newConnection));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Configuration derive(DataSource newDataSource) {
        return derive(new DataSourceConnectionProvider(newDataSource));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Configuration derive(ConnectionProvider newConnectionProvider) {
        return new DefaultConfiguration(
            newConnectionProvider,
            transactionProvider,
            recordMapperProvider,
            recordListenerProviders,
            executeListenerProviders,
            visitListenerProviders,
            converterProvider,
            dialect,
            settings,
            data
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Configuration derive(TransactionProvider newTransactionProvider) {
        return new DefaultConfiguration(
            connectionProvider,
            newTransactionProvider,
            recordMapperProvider,
            recordListenerProviders,
            executeListenerProviders,
            visitListenerProviders,
            converterProvider,
            dialect,
            settings,
            data
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Configuration derive(RecordMapperProvider newRecordMapperProvider) {
        return new DefaultConfiguration(
            connectionProvider,
            transactionProvider,
            newRecordMapperProvider,
            recordListenerProviders,
            executeListenerProviders,
            visitListenerProviders,
            converterProvider,
            dialect,
            settings,
            data
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Configuration derive(RecordListenerProvider... newRecordListenerProviders) {
        return new DefaultConfiguration(
            connectionProvider,
            transactionProvider,
            recordMapperProvider,
            newRecordListenerProviders,
            executeListenerProviders,
            visitListenerProviders,
            converterProvider,
            dialect,
            settings,
            data
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Configuration derive(ExecuteListenerProvider... newExecuteListenerProviders) {
        return new DefaultConfiguration(
            connectionProvider,
            transactionProvider,
            recordMapperProvider,
            recordListenerProviders,
            newExecuteListenerProviders,
            visitListenerProviders,
            converterProvider,
            dialect,
            settings,
            data
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Configuration derive(VisitListenerProvider... newVisitListenerProviders) {
        return new DefaultConfiguration(
            connectionProvider,
            transactionProvider,
            recordMapperProvider,
            recordListenerProviders,
            executeListenerProviders,
            newVisitListenerProviders,
            converterProvider,
            dialect,
            settings,
            data
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Configuration derive(ConverterProvider newConverterProvider) {
        return new DefaultConfiguration(
            connectionProvider,
            transactionProvider,
            recordMapperProvider,
            recordListenerProviders,
            executeListenerProviders,
            visitListenerProviders,
            newConverterProvider,
            dialect,
            settings,
            data
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Configuration derive(SQLDialect newDialect) {
        return new DefaultConfiguration(
            connectionProvider,
            transactionProvider,
            recordMapperProvider,
            recordListenerProviders,
            executeListenerProviders,
            visitListenerProviders,
            converterProvider,
            newDialect,
            settings,
            data
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Configuration derive(Settings newSettings) {
        return new DefaultConfiguration(
            connectionProvider,
            transactionProvider,
            recordMapperProvider,
            recordListenerProviders,
            executeListenerProviders,
            visitListenerProviders,
            converterProvider,
            dialect,
            newSettings,
            data
        );
    }

    // -------------------------------------------------------------------------
    // XXX: Changing configurations
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public final Configuration set(Connection newConnection) {
        return set(new DefaultConnectionProvider(newConnection));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Configuration set(DataSource newDataSource) {
        return set(new DataSourceConnectionProvider(newDataSource));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Configuration set(ConnectionProvider newConnectionProvider) {
        this.connectionProvider = newConnectionProvider != null
            ? newConnectionProvider
            : new NoConnectionProvider();

        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Configuration set(TransactionProvider newTransactionProvider) {
        this.transactionProvider = newTransactionProvider != null
            ? newTransactionProvider
            : new NoTransactionProvider();

        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Configuration set(RecordMapperProvider newRecordMapperProvider) {
        this.recordMapperProvider = newRecordMapperProvider;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Configuration set(RecordListenerProvider... newRecordListenerProviders) {
        this.recordListenerProviders = newRecordListenerProviders != null
            ? newRecordListenerProviders
            : new RecordListenerProvider[0];

        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Configuration set(ExecuteListenerProvider... newExecuteListenerProviders) {
        this.executeListenerProviders = newExecuteListenerProviders != null
            ? newExecuteListenerProviders
            : new ExecuteListenerProvider[0];

        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Configuration set(VisitListenerProvider... newVisitListenerProviders) {
        this.visitListenerProviders = newVisitListenerProviders != null
            ? newVisitListenerProviders
            : new VisitListenerProvider[0];

        return this;
    }

    @Override
    public final Configuration set(ConverterProvider newConverterProvider) {
        this.converterProvider = newConverterProvider != null
            ? newConverterProvider
            : new DefaultConverterProvider();

        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Configuration set(SQLDialect newDialect) {
        this.dialect = newDialect;
        return this;
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public final ConnectionProvider connectionProvider() {

        // [#3229] If we're currently in a transaction, return that transaction's
        // local DefaultConnectionProvider, not the one from this configuration
        ConnectionProvider transactional = (ConnectionProvider) data(DATA_DEFAULT_TRANSACTION_PROVIDER_CONNECTION);
        return transactional == null ? connectionProvider : transactional;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final TransactionProvider transactionProvider() {

        // [#3229] If transactions are used in client code, the default behaviour
        // is assumed automatically, for convenience.
        if (transactionProvider instanceof NoTransactionProvider) {
            return new DefaultTransactionProvider(connectionProvider);
        }

        return transactionProvider;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final RecordMapperProvider recordMapperProvider() {

        // [#3915] Avoid permanently referencing such a DefaultRecordMapperProvider from this
        // DefaultConfiguration to prevent memory leaks.
        return recordMapperProvider != null
             ? recordMapperProvider
             : new DefaultRecordMapperProvider(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final RecordListenerProvider[] recordListenerProviders() {
        return recordListenerProviders;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final ExecuteListenerProvider[] executeListenerProviders() {
        return executeListenerProviders;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final VisitListenerProvider[] visitListenerProviders() {
        return visitListenerProviders;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final ConverterProvider converterProvider() {
        return converterProvider;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final SQLDialect dialect() {
        return dialect;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final SQLDialect family() {
        return dialect.family();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Settings settings() {
        return settings;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final ConcurrentHashMap<Object, Object> data() {
        return data;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Object data(Object key) {
        return data.get(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Object data(Object key, Object value) {
        return data.put(key, value);
    }

    /**
     * {@inheritDoc}
     */
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
        converterProvider = (ConverterProvider) ois.readObject();
    }
}
