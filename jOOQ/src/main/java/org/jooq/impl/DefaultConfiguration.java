/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is triple-licensed under ASL 2.0, AGPL 3.0, and jOOQ EULA
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   ASL 2.0 or jOOQ EULA.
 * - If you're using this work with at least one commercial database, you may
 *   choose AGPL 3.0 or jOOQ EULA.
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
 * AGPL 3.0
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 *
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details: http://www.jooq.org/eula
 */
package org.jooq.impl;

import static org.jooq.SQLDialect.SQL99;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.bind.JAXB;

import org.jooq.Configuration;
import org.jooq.ConnectionProvider;
import org.jooq.DSLContext;
import org.jooq.ExecuteListenerProvider;
import org.jooq.RecordListenerProvider;
import org.jooq.RecordMapperProvider;
import org.jooq.SQLDialect;
import org.jooq.SchemaMapping;
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
    private transient RecordMapperProvider      recordMapperProvider;
    private transient RecordListenerProvider[]  recordListenerProviders;
    private transient ExecuteListenerProvider[] executeListenerProviders;
    private transient VisitListenerProvider[]   visitListenerProviders;

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
        this(SQL99);
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
            new NoConnectionProvider(),
            new DefaultRecordMapperProvider(),
            new RecordListenerProvider[0],
            new ExecuteListenerProvider[0],
            new VisitListenerProvider[0],
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
            configuration.recordMapperProvider(),
            configuration.recordListenerProviders(),
            configuration.executeListenerProviders(),
            configuration.visitListenerProviders(),
            configuration.dialect(),
            configuration.settings(),
            configuration.data()
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
            RecordMapperProvider recordMapperProvider,
            RecordListenerProvider[] recordListenerProviders,
            ExecuteListenerProvider[] executeListenerProviders,
            VisitListenerProvider[] visitListenerProviders,
            SQLDialect dialect,
            Settings settings,
            Map<Object, Object> data)
    {
        set(connectionProvider);
        set(recordMapperProvider);
        set(recordListenerProviders);
        set(executeListenerProviders);
        set(visitListenerProviders);
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
    public final Configuration derive(ConnectionProvider newConnectionProvider) {
        return new DefaultConfiguration(
            newConnectionProvider,
            recordMapperProvider,
            recordListenerProviders,
            executeListenerProviders,
            visitListenerProviders,
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
            newRecordMapperProvider,
            recordListenerProviders,
            executeListenerProviders,
            visitListenerProviders,
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
            recordMapperProvider,
            newRecordListenerProviders,
            executeListenerProviders,
            visitListenerProviders,
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
            recordMapperProvider,
            recordListenerProviders,
            newExecuteListenerProviders,
            visitListenerProviders,
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
            recordMapperProvider,
            recordListenerProviders,
            executeListenerProviders,
            newVisitListenerProviders,
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
            recordMapperProvider,
            recordListenerProviders,
            executeListenerProviders,
            visitListenerProviders,
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
            recordMapperProvider,
            recordListenerProviders,
            executeListenerProviders,
            visitListenerProviders,
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
    public final Configuration set(ConnectionProvider newConnectionProvider) {
        this.connectionProvider = newConnectionProvider;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Configuration set(RecordMapperProvider newRecordMapperProvider) {
        this.recordMapperProvider = newRecordMapperProvider != null
            ? newRecordMapperProvider
            : new DefaultRecordMapperProvider();

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

        this.mapping = new SchemaMapping(this);
        return this;
    }

    // -------------------------------------------------------------------------
    // XXX: Getters
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public final ConnectionProvider connectionProvider() {
        return connectionProvider;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final RecordMapperProvider recordMapperProvider() {
        return recordMapperProvider;
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
    public final SQLDialect dialect() {
        return dialect;
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

        return "DefaultConfiguration [\n\tconnected=" + (connectionProvider != null && !(connectionProvider instanceof NoConnectionProvider)) +
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
        oos.writeObject(recordMapperProvider instanceof Serializable
            ? recordMapperProvider
            : null);

        oos.writeObject(cloneSerializables(executeListenerProviders));
        oos.writeObject(cloneSerializables(recordListenerProviders));
        oos.writeObject(cloneSerializables(visitListenerProviders));
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
        recordMapperProvider = (RecordMapperProvider) ois.readObject();
        executeListenerProviders = (ExecuteListenerProvider[]) ois.readObject();
        recordListenerProviders = (RecordListenerProvider[]) ois.readObject();
        visitListenerProviders = (VisitListenerProvider[]) ois.readObject();
    }
}
