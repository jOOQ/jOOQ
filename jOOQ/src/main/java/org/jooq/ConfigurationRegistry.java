/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
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

import org.jooq.tools.JooqLogger;

/**
 * A public static registry that can provide factories ({@link Configuration}'s)
 * to {@link Attachable}'s upon deserialisation. The registry acts as an
 * interface between client code and jOOQ. Client code may register a
 * {@link ConfigurationProvider} using
 * {@link #setProvider(ConfigurationProvider)}
 * <p>
 * This functionality is experimental. It may change again in the future. Use it
 * at your own risk.
 *
 * @author Lukas Eder
 * @see <a
 *      href="http://groups.google.com/group/jooq-user/browse_thread/thread/d33e9a902707d111">http://groups.google.com/group/jooq-user/browse_thread/thread/d33e9a902707d111</a>
 * @deprecated - 2.1.0 [#1191] - Use
 *             {@link ExecuteListener#start(ExecuteContext)} instead to provide
 *             jOOQ with valid connections
 */
@Deprecated
public final class ConfigurationRegistry {

    private static JooqLogger log = JooqLogger.getLogger(ConfigurationRegistry.class);

    /**
     * The provider instance
     */
    private static ConfigurationProvider provider;

    /**
     * Communicate to client-registered {@link ConfigurationProvider}'s
     * <p>
     * This method is used by jOOQ retrieve a client-provided
     * <code>Configuration</code>. Do not call this method directly.
     */
    public static Configuration provideFor(Configuration configuration) {
        if (provider != null) {
            if (log.isTraceEnabled()) {
                log.trace("Providing for", configuration);
            }

            Configuration result = provider.provideFor(configuration);

            if (log.isTraceEnabled()) {
                log.trace("Provided", result);
            }

            return result;
        }

        return null;
    }

    /**
     * Register a client {@link ConfigurationProvider} to provide jOOQ with
     * {@link Configuration}'s upon deserialisation.
     *
     * @param provider The client <code>ConfigurationProvider</code>
     *            implementation, providing jOOQ with
     *            <code>Configurations</code> after deserialisation. This may be
     *            <code>null</code> to reset the provider.
     */
    public static void setProvider(ConfigurationProvider provider) {
        log.warn("Deprecation", "org.jooq.ConfigurationRegistry is deprecated. Use org.jooq.ExecuteListener instead");

        if (provider != null) {
            log.info("Registering provider", provider);
        }
        else {
            log.info("Unregistering provider");
        }
        ConfigurationRegistry.provider = provider;
    }
}
