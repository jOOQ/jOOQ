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
package org.jooq.conf;

import static org.jooq.conf.ParamType.INDEXED;
import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.conf.StatementType.PREPARED_STATEMENT;
import static org.jooq.conf.StatementType.STATIC_STATEMENT;

import java.io.File;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.Statement;

import javax.xml.bind.JAXB;

/**
 * Convenience methods for jOOQ runtime settings.
 *
 * @author Lukas Eder
 */
public final class SettingsTools {

    private static final Settings DEFAULT_SETTINGS;

    static {
        Settings settings = null;
        String property = System.getProperty("org.jooq.settings");

        if (property != null) {

            // Check classpath first
            InputStream in = SettingsTools.class.getResourceAsStream(property);
            if (in != null) {
                settings = JAXB.unmarshal(in, Settings.class);
            }
            else {
                settings = JAXB.unmarshal(new File(property), Settings.class);
            }
        }

        if (settings == null) {
            InputStream in = SettingsTools.class.getResourceAsStream("/jooq-settings.xml");

            if (in != null) {
                settings = JAXB.unmarshal(in, Settings.class);
            }
        }

        if (settings == null) {
            settings = new Settings();
        }

        DEFAULT_SETTINGS = settings;
    }

    /**
     * Get the parameter type from the settings.
     * <p>
     * The {@link ParamType} can be overridden by the {@link StatementType}.
     * If the latter is set to {@link StatementType#STATIC_STATEMENT}, then the
     * former defaults to {@link ParamType#INLINED}.
     */
    public static final ParamType getParamType(Settings settings) {
        if (executeStaticStatements(settings)) {
            return INLINED;
        }
        else if (settings != null) {
            ParamType result = settings.getParamType();

            if (result != null) {
                return result;
            }
        }

        return INDEXED;
    }

    /**
     * Get the statement type from the settings.
     */
    public static final StatementType getStatementType(Settings settings) {
        if (settings != null) {
            StatementType result = settings.getStatementType();

            if (result != null) {
                return result;
            }
        }

        return PREPARED_STATEMENT;
    }

    /**
     * Whether a {@link PreparedStatement} should be executed.
     */
    public static final boolean executePreparedStatements(Settings settings) {
        return getStatementType(settings) == PREPARED_STATEMENT;
    }

    /**
     * Whether static {@link Statement} should be executed.
     */
    public static final boolean executeStaticStatements(Settings settings) {
        return getStatementType(settings) == STATIC_STATEMENT;
    }

    /**
     * Lazy access to {@link RenderMapping}.
     */
    public static final RenderMapping getRenderMapping(Settings settings) {
        if (settings.getRenderMapping() == null) {
            settings.setRenderMapping(new RenderMapping());
        }

        return settings.getRenderMapping();
    }

    /**
     * Retrieve the configured default settings.
     * <p>
     * <ul>
     * <li>If the JVM flag <code>-Dorg.jooq.settings</code> points to a valid
     * settings file on the classpath, this will be loaded</li>
     * <li>If the JVM flag <code>-Dorg.jooq.settings</code> points to a valid
     * settings file on the file system, this will be loaded</li>
     * <li>If a valid settings file is found on the classpath at
     * <code>/jooq-settings.xml</code>, this will be loaded</li>
     * <li>Otherwise, a new <code>Settings</code> object is created with its
     * defaults</li>
     * </ul>
     */
    public static final Settings defaultSettings() {

        // Clone the DEFAULT_SETTINGS to prevent modification
        return clone(DEFAULT_SETTINGS);
    }

    /**
     * Clone some settings.
     */
    public static final Settings clone(Settings settings) {
        return (Settings) settings.clone();
    }
}
