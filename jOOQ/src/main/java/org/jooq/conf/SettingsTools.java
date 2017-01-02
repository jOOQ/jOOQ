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
package org.jooq.conf;

import static org.jooq.conf.ParamType.INDEXED;
import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.conf.StatementType.PREPARED_STATEMENT;
import static org.jooq.conf.StatementType.STATIC_STATEMENT;
import static org.jooq.tools.StringUtils.defaultIfNull;

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
     * Get the value BackslashEscaping value.
     */
    public static final BackslashEscaping getBackslashEscaping(Settings settings) {
        if (settings != null) {
            BackslashEscaping result = settings.getBackslashEscaping();

            if (result != null) {
                return result;
            }
        }

        return BackslashEscaping.DEFAULT;
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
     * Whether primary keys should be updatable.
     */
    public static final boolean updatablePrimaryKeys(Settings settings) {
        return defaultIfNull(settings.isUpdatablePrimaryKeys(), false);
    }

    /**
     * Whether primary keys should be updatable.
     */
    public static final boolean reflectionCaching(Settings settings) {
        return defaultIfNull(settings.isReflectionCaching(), true);
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

    /**
     * Return <code>timeout</code> if it is not <code>0</code>, or the specified
     * {@link Settings#getQueryTimeout()}.
     */
    public static int getQueryTimeout(int timeout, Settings settings) {
        return timeout != 0
             ? timeout
             : settings.getQueryTimeout() != null
             ? settings.getQueryTimeout()
             : 0;
    }

    /**
     * Return <code>maxRows</code> if it is not <code>0</code>, or the specified
     * {@link Settings#getMaxRows()}.
     */
    public static int getMaxRows(int maxRows, Settings settings) {
        return maxRows != 0
             ? maxRows
             : settings.getMaxRows() != null
             ? settings.getMaxRows()
             : 0;
    }

    /**
     * Return <code>fetchSize</code> if it is not <code>0</code>, or the specified
     * {@link Settings#getFetchSize()}.
     */
    public static int getFetchSize(int fetchSize, Settings settings) {
        return fetchSize != 0
             ? fetchSize
             : settings.getFetchSize() != null
             ? settings.getFetchSize()
             : 0;
    }
}
