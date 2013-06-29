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
package org.jooq;

import java.io.Serializable;
import java.util.Map;

import org.jooq.conf.Settings;

/**
 * A <code>Configuration</code> configures a {@link DSLContext}, providing it
 * with information for query construction, rendering and execution.
 * <p>
 * A <code>Configuration</code> wraps all information elements that are
 * needed...
 * <ul>
 * <li>by a {@link DSLContext} to construct {@link Query} objects</li>
 * <li>by a {@link RenderContext} to render {@link Query} objects and
 * {@link QueryPart}s</li>
 * <li>by a {@link BindContext} to bind values to {@link Query} objects and
 * {@link QueryPart}s</li>
 * <li>by a {@link Query} or {@link Routine} object to execute itself</li>
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
 *
 * @author Lukas Eder
 */
public interface Configuration extends Serializable {

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    /**
     * Retrieve the configured dialect.
     */
    SQLDialect dialect();

    /**
     * Get this configuration's underlying connection provider.
     */
    ConnectionProvider connectionProvider();

    /**
     * Get this configuration's underlying record mapper provider.
     */
    RecordMapperProvider recordMapperProvider();

    /**
     * Retrieve the configured schema mapping.
     *
     * @deprecated - 2.0.5 - Use {@link #settings()} instead
     */
    @Deprecated
    SchemaMapping schemaMapping();

    /**
     * Retrieve the runtime configuration settings.
     */
    Settings settings();

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
     * @param value The custom data or <code>null</code> to unset the custom
     *            data
     * @return The previously set custom data or <code>null</code> if no data
     *         was previously set for the given key
     * @see ExecuteListener
     */
    Object data(Object key, Object value);

    /**
     * Get the configured <code>ExecuteListenerProvider</code> from this
     * configuration.
     * <p>
     * This method allows for retrieving the configured
     * <code>ExecuteListenerProvider</code> from this configuration. The
     * provider will provide jOOQ with {@link ExecuteListener} instances. These
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

    // -------------------------------------------------------------------------
    // Setters
    // -------------------------------------------------------------------------

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
     * Create a derived configuration from this one, with a new dialect.
     *
     * @param newDialect The new dialect to be contained in the derived
     *            configuration.
     * @return The derived configuration.
     */
    Configuration derive(SQLDialect newDialect);

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
     * Create a derived configuration from this one, with a new record mapper
     * provider.
     *
     * @param newRecordMapperProvider The new record mapper provider to be
     *            contained in the derived configuration.
     * @return The derived configuration.
     */
    Configuration derive(RecordMapperProvider newRecordMapperProvider);

    /**
     * Create a derived configuration from this one, with new settings.
     *
     * @param newSettings The new settings to be contained in the derived
     *            configuration.
     * @return The derived configuration.
     */
    Configuration derive(Settings newSettings);

    /**
     * Create a derived configuration from this one, with a new execute listener
     * providers.
     *
     * @param newExecuteListenerProviders The new execute listener providers to
     *            be contained in the derived configuration.
     * @return The derived configuration.
     */
    Configuration derive(ExecuteListenerProvider... newExecuteListenerProviders);

}
