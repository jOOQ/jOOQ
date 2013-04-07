/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
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
import org.jooq.SQLDialect;
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
    private static final long                       serialVersionUID = 8193158984283234708L;

    // Configuration objects
    private final SQLDialect                        dialect;
    private final Settings                          settings;
    private final ConcurrentHashMap<Object, Object> data;

    // Non-serializable Configuration objects
    private transient ConnectionProvider            connectionProvider;
    private transient ExecuteListenerProvider       listenerProvider;

    // Derived objects
    private final org.jooq.SchemaMapping            mapping;

    // -------------------------------------------------------------------------
    // XXX: Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a new "empty" configuration object.
     * <p>
     * This can be used as is, as a "dummy" configuration object, or as a base
     * implementation for creating more sophisticated "derived" configurations
     * through the various <code>derive()</code> methods.
     */
    public DefaultConfiguration() {
        this(
            new NoConnectionProvider(),
            new DefaultExecuteListenerProvider(),
            SQL99,
            SettingsTools.defaultSettings(),
            null);
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
            configuration.getConnectionProvider(),
            configuration.getExecuteListenerProvider(),
            configuration.getDialect(),
            configuration.getSettings(),
            configuration.getData()
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
            ExecuteListenerProvider listenerProvider,
            SQLDialect dialect,
            Settings settings,
            Map<Object, Object> data)
    {
        this.connectionProvider = connectionProvider;
        this.listenerProvider = listenerProvider != null
            ? listenerProvider
            : new DefaultExecuteListenerProvider();

        this.dialect = dialect;
        this.settings = settings != null
            ? SettingsTools.clone(settings)
            : SettingsTools.defaultSettings();

        this.data = data != null
            ? new ConcurrentHashMap<Object, Object>(data)
            : new ConcurrentHashMap<Object, Object>();

        this.mapping = new org.jooq.SchemaMapping(this);
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
    public final Configuration derive(SQLDialect newDialect) {
        return new DefaultConfiguration(connectionProvider, listenerProvider, newDialect, settings, data);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Configuration derive(ConnectionProvider newConnectionProvider) {
        return new DefaultConfiguration(newConnectionProvider, listenerProvider, dialect, settings, data);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Configuration derive(Settings newSettings) {
        return new DefaultConfiguration(connectionProvider, listenerProvider, dialect, newSettings, data);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Configuration derive(ExecuteListenerProvider newExecuteListenerProvider) {
        return new DefaultConfiguration(connectionProvider, newExecuteListenerProvider, dialect, settings, data);
    }

    // -------------------------------------------------------------------------
    // XXX: Getters
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public final SQLDialect getDialect() {
        return dialect;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final ConnectionProvider getConnectionProvider() {
        return connectionProvider;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Deprecated
    public final org.jooq.SchemaMapping getSchemaMapping() {
        return mapping;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Settings getSettings() {
        return settings;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final ConcurrentHashMap<Object, Object> getData() {
        return data;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Object getData(Object key) {
        return data.get(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Object setData(Object key, Object value) {
        return data.put(key, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final ExecuteListenerProvider getExecuteListenerProvider() {
        return listenerProvider;
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
        oos.writeObject(listenerProvider instanceof Serializable
            ? listenerProvider
            : null);
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();

        connectionProvider = (ConnectionProvider) ois.readObject();
        listenerProvider = (ExecuteListenerProvider) ois.readObject();
    }
}