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
import java.util.ArrayList;
import java.util.Locale;

import org.jooq.tools.JooqLogger;
import org.jooq.util.jaxb.tools.MiniJAXB;

/**
 * Convenience methods for jOOQ runtime settings.
 *
 * @author Lukas Eder
 */
public final class SettingsTools {

    private static final Settings   DEFAULT_SETTINGS;
    private static final JooqLogger log = JooqLogger.getLogger(SettingsTools.class);

    static {
        Settings settings = null;
        String property = System.getProperty("org.jooq.settings");

        if (property != null) {
            log.warn("DEPRECATION", "Loading system wide default settings via org.jooq.settings system properties has been deprecated. Please use explicit Settings in your Configuration references, instead.");

            // Check classpath first
            InputStream in = SettingsTools.class.getResourceAsStream(property);
            if (in != null)
                settings = MiniJAXB.unmarshal(in, Settings.class);
            else
                settings = MiniJAXB.unmarshal(new File(property), Settings.class);
        }

        if (settings == null) {
            InputStream in = SettingsTools.class.getResourceAsStream("/jooq-settings.xml");

            if (in != null) {
                log.warn("DEPRECATION", "Loading system wide default settings via the classpath /jooq-settings.xml resource has been deprecated. Please use explicit Settings in your Configuration references, instead.");
                settings = MiniJAXB.unmarshal(in, Settings.class);
            }
        }

        if (settings == null)
            settings = new Settings();

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

            if (result != null)
                return result;
        }

        return INDEXED;
    }

    /**
     * Get the statement type from the settings.
     */
    public static final StatementType getStatementType(Settings settings) {
        if (settings != null) {
            StatementType result = settings.getStatementType();

            if (result != null)
                return result;
        }

        return PREPARED_STATEMENT;
    }

    /**
     * Get the value BackslashEscaping value.
     */
    public static final BackslashEscaping getBackslashEscaping(Settings settings) {
        if (settings != null) {
            BackslashEscaping result = settings.getBackslashEscaping();

            if (result != null)
                return result;
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
     * The render locale that is applicable, or the default locale if no such
     * locale is configured.
     */
    public static final Locale locale(Settings settings) {
        return defaultIfNull(settings.getLocale(), Locale.getDefault());
    }

    /**
     * The render locale that is applicable, or the default locale if no such
     * locale is configured.
     */
    public static final Locale renderLocale(Settings settings) {
        return defaultIfNull(settings.getRenderLocale(), locale(settings));
    }

    /**
     * The parser locale that is applicable, or the default locale if no such
     * locale is configured.
     */
    public static final Locale parseLocale(Settings settings) {
        return defaultIfNull(settings.getParseLocale(), locale(settings));
    }

    /**
     * The interpreter locale that is applicable, or the default locale if no such
     * locale is configured.
     */
    public static final Locale interpreterLocale(Settings settings) {
        return defaultIfNull(settings.getInterpreterLocale(), locale(settings));
    }

    /**
     * Lazy access to {@link RenderMapping}.
     */
    public static final RenderMapping getRenderMapping(Settings settings) {
        if (settings.getRenderMapping() == null)
            settings.setRenderMapping(new RenderMapping());

        return settings.getRenderMapping();
    }

    /**
     * Backwards compatible access to {@link RenderKeywordCase} and/or
     * {@link RenderKeywordStyle} (the latter being deprecated).
     */
    public static final RenderKeywordCase getRenderKeywordCase(Settings settings) {
        RenderKeywordCase result = settings.getRenderKeywordCase();

        if (result == null || result == RenderKeywordCase.AS_IS) {
            RenderKeywordStyle style = settings.getRenderKeywordStyle();

            if (style != null) {
                switch (style) {
                    case AS_IS:  result = RenderKeywordCase.AS_IS;  break;
                    case LOWER:  result = RenderKeywordCase.LOWER;  break;
                    case UPPER:  result = RenderKeywordCase.UPPER;  break;
                    case PASCAL: result = RenderKeywordCase.PASCAL; break;
                    default:
                        throw new UnsupportedOperationException("Unsupported style: " + style);
                }
            }
            else {
                result = RenderKeywordCase.AS_IS;
            }
        }

        return result;
    }

    /**
     * Backwards compatible access to {@link RenderNameCase} and/or
     * {@link RenderNameStyle} (the latter being deprecated).
     */
    public static final RenderNameCase getRenderNameCase(Settings settings) {
        RenderNameCase result = settings.getRenderNameCase();

        if (result == null || result == RenderNameCase.AS_IS) {
            RenderNameStyle style = settings.getRenderNameStyle();

            if (style == RenderNameStyle.LOWER)
                result = RenderNameCase.LOWER;
            else if (style == RenderNameStyle.UPPER)
                result = RenderNameCase.UPPER;
            else
                result = RenderNameCase.AS_IS;
        }

        return result;
    }

    /**
     * Backwards compatible access to {@link RenderQuotedNames} and/or
     * {@link RenderNameStyle} (the latter being deprecated).
     */
    public static final RenderQuotedNames getRenderQuotedNames(Settings settings) {
        RenderQuotedNames result = settings.getRenderQuotedNames();

        if (result == null || result == RenderQuotedNames.EXPLICIT_DEFAULT_QUOTED) {
            RenderNameStyle style = settings.getRenderNameStyle();

            if (style == null || style == RenderNameStyle.QUOTED)
                result = RenderQuotedNames.EXPLICIT_DEFAULT_QUOTED;
            else
                result = RenderQuotedNames.NEVER;
        }

        return result;
    }

    /**
     * Lazy access to {@link Settings#getExecuteUpdateWithoutWhere()}.
     */
    public static final ExecuteWithoutWhere getExecuteUpdateWithoutWhere(Settings settings) {
        ExecuteWithoutWhere result = settings.getExecuteUpdateWithoutWhere();
        return result == null ? ExecuteWithoutWhere.LOG_DEBUG : result;
    }

    /**
     * Lazy access to {@link Settings#getExecuteDeleteWithoutWhere()}.
     */
    public static final ExecuteWithoutWhere getExecuteDeleteWithoutWhere(Settings settings) {
        ExecuteWithoutWhere result = settings.getExecuteDeleteWithoutWhere();
        return result == null ? ExecuteWithoutWhere.LOG_DEBUG : result;
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
        Settings result = (Settings) settings.clone();

        if (result.renderFormatting != null)
            result.renderFormatting = (RenderFormatting) result.renderFormatting.clone();
        if (result.parseSearchPath != null)
            result.parseSearchPath = new ArrayList<>(result.parseSearchPath);

        return result;
    }

    /**
     * Return <code>timeout</code> if it is not <code>0</code>, or the specified
     * {@link Settings#getQueryTimeout()}.
     */
    public static final int getQueryTimeout(int timeout, Settings settings) {
        return timeout != 0
             ? timeout
             : settings.getQueryTimeout() != null
             ? settings.getQueryTimeout()
             : 0;
    }

    /**
     * Return <code>poolable</code> if it is not <code>null</code>, or the
     * specified {@link Settings#getQueryPoolable()}.
     */
    public static final QueryPoolable getQueryPoolable(QueryPoolable poolable, Settings settings) {
        return poolable != null && poolable != QueryPoolable.DEFAULT
             ? poolable
             : settings.getQueryPoolable() != null
             ? settings.getQueryPoolable()
             : QueryPoolable.DEFAULT;
    }

    /**
     * Return <code>maxRows</code> if it is not <code>0</code>, or the specified
     * {@link Settings#getMaxRows()}.
     */
    public static final int getMaxRows(int maxRows, Settings settings) {
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
    public static final int getFetchSize(int fetchSize, Settings settings) {
        return fetchSize != 0
             ? fetchSize
             : settings.getFetchSize() != null
             ? settings.getFetchSize()
             : 0;
    }

    /**
     * Return <code>fetchServerOutputSize</code> if it is not <code>0</code>, or
     * the specified {@link Settings#getFetchServerOutputSize()}.
     */
    public static final int getFetchServerOutputSize(int fetchServerOutputSize, Settings settings) {
        return fetchServerOutputSize != 0
             ? fetchServerOutputSize
             : settings.getFetchServerOutputSize() != null
             ? settings.getFetchServerOutputSize()
             : 0;
    }
}
