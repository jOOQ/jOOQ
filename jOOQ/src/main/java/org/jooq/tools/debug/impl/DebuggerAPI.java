/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
 *                          Christopher Deckers, chrriis@gmail.com
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
package org.jooq.tools.debug.impl;

import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.jooq.Configuration;
import org.jooq.Schema;
import org.jooq.tools.debug.Debugger;
import org.jooq.tools.debug.Executor;

/**
 * A factory object for {@link Debugger} types.
 *
 * @author Lukas Eder
 * @author Christopher Deckers
 */
public class DebuggerAPI {

    private static final Map<String, Executor> EXECUTORS = new LinkedHashMap<String, Executor>();

    private static volatile boolean            DEBUG     = true;

    /**
     * Activate/Deactivate debugging (default is true)
     */
    // Should this be synchronized?
    public static void debug(boolean debug) {
        DebuggerAPI.DEBUG = debug;
    }

    /**
     * Whether debugging is active (default is true)
     */
    // Should this be synchronized?
    public static boolean debug() {
        return DebuggerAPI.DEBUG;
    }

    /**
     * Start a server locally, to which remote debuggers can connect to
     *
     * @param port The port on which to listen for incoming debugging
     *            conncetions.
     * @see #remoteDebugger(String, int)
     */
    public static synchronized void startServer(int port) {
        Server.start(port);
    }

    /**
     * Stop a previously started server
     */
    public static synchronized void stopServer() {
        Server.stop();
    }

    /**
     * Get a registered executor by its name
     */
    static Executor executor(String name) {
        synchronized (EXECUTORS) {
            return EXECUTORS.get(name);
        }
    }

    /**
     * Get all registered executors
     */
    static Executor[] executors() {
        synchronized (EXECUTORS) {
            return EXECUTORS.values().toArray(new Executor[0]);
        }
    }

    /**
     * Register a new {@link Executor}
     * <p>
     * Registered executors are made available globally (statically, within the
     * same <code>ClassLoader</code>) to all {@link Debugger} objects. The
     * {@link Configuration} used within an <code>Executor</code> should be
     * chosen as such that it is independent of any other application
     * transactions. Both <code>Configurations</code> using standalone JDBC
     * {@link Connection} or pooled {@link DataSource} objects are possible.
     * <p>
     * Client code is responsible for removing executors again using
     * {@link #removeExecutor(String)}
     *
     * @param name The executor's registered name, as returned by
     *            {@link Executor#getName()}
     * @param configuration The executor's underlying configuration
     * @param schemata The generated <code>Schema</code> objects, that provide
     *            meta-information about an executor.
     * @return The newly registered executor.
     * @see #removeExecutor(String)
     */
    public static Executor setExecutor(String name, Configuration configuration, Schema... schemata) {
        ExecutorImpl result = new ExecutorImpl(name, configuration, schemata);

        synchronized (EXECUTORS) {
            EXECUTORS.put(name, result);
        }

        return result;
    }

    /**
     * Remove a previously registered executor
     *
     * @param name The executor's name
     */
    public static void removeExecutor(String name) {
        synchronized (EXECUTORS) {
            EXECUTORS.remove(name);
        }
    }

    /**
     * Get a reference of a "local" {@link Debugger}
     * <p>
     * Local debuggers are debuggers that run in the same process as the
     * application executing SQL through jOOQ.
     *
     * @see #remoteDebugger(String, int)
     */
    public static Debugger localDebugger() {
        return new LocalDebugger();
    }

    /**
     * Get a reference to a "remote" {@link Debugger}
     * <p>
     * Remote debuggers are debuggers that run in an other process than the
     * application executing SQL through jOOQ. Note that the application being
     * debugged must have a running server at <code>ip:port</code>. You can
     * start such a server using {@link #startServer(int)}
     *
     * @param ip The IP (or DNS-resolvable name) to connect to
     * @param port The port to connect to
     * @see #startServer(int)
     * @see #stopServer()
     */
    public static Debugger remoteDebugger(String ip, int port) {
        return new ClientDebugger(ip, port);
    }

    /**
     * No instances
     */
    private DebuggerAPI() {}
}
