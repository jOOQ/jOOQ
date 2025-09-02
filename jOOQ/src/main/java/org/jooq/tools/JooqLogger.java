/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
 *
 * For more information, please visit: https://www.jooq.org/legal/licensing
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
package org.jooq.tools;

import static java.util.Arrays.asList;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntConsumer;
import java.util.logging.SimpleFormatter;

import org.jooq.Log;

/**
 * The jOOQ logger abstraction.
 * <p>
 * This logger provides abstraction over the three logger APIs supported
 * optionally by jOOQ. These are (in order of preference):
 * <ul>
 * <li>slf4j</li>
 * <li>log4j</li>
 * <li>jav.util.logging</li>
 * </ul>
 * <code>JooqLogger</code> tries to instantiate any of the above loggers,
 * catching potential {@link NoClassDefFoundError}'s in case any logger API
 * cannot be found on the classpath.
 *
 * @author Lukas Eder
 */
public final class JooqLogger implements Log {

    private static volatile Log.Level globalThreshold = Log.Level.TRACE;
    private org.slf4j.Logger          slf4j;
    private java.util.logging.Logger  util;
    private volatile Log.Level        threshold       = Log.Level.TRACE;
    private final String              name;
    private final String              propertyName;
    private final AtomicInteger       limitMessages;

    /**
     * @deprecated - [#15050] - 3.19.0 - Do not construct your own logger. Use
     *             {@link #getLogger(Class)} methods instead.
     */
    @Deprecated
    public JooqLogger(int limitMessages) {
        this(UUID.randomUUID().toString(), limitMessages);
    }

    JooqLogger(String name, int limitMessages) {
        this.name = name;
        this.propertyName = "org.jooq.log." + name;
        this.limitMessages = limitMessages >= 0 ? new AtomicInteger(limitMessages) : null;
    }

    /**
     * Get a logger wrapper for a class.
     */
    public static JooqLogger getLogger(Class<?> clazz) {
        return getLogger(clazz, null, -1);
    }

    /**
     * Get a logger wrapper for a class.
     */
    public static JooqLogger getLogger(String name) {
        return getLogger(null, name, -1);
    }

    /**
     * Get a logger wrapper for a class, which logs at most a certain number of
     * messages.
     */
    public static JooqLogger getLogger(Class<?> clazz, int limitMessages) {
        return getLogger(clazz, null, limitMessages);
    }

    /**
     * Get a logger wrapper for a class, which logs at most a certain number of
     * messages.
     */
    public static JooqLogger getLogger(String name, int limitMessages) {
        return getLogger(null, name, limitMessages);
    }

    /**
     * Get a logger wrapper for a class, which logs at most a certain number of
     * messages.
     */
    public static JooqLogger getLogger(Class<?> clazz, String nameSuffix, int limitMessages) {
        String name = clazz != null && nameSuffix != null
            ? (clazz.getName() + "." + nameSuffix)
            : clazz != null
            ? clazz.getName()
            : nameSuffix;

        JooqLogger result = new JooqLogger(name, limitMessages);

        // Prioritise slf4j
        try {
            result.slf4j = org.slf4j.LoggerFactory.getLogger(name);
        }

        // If that's not on the classpath, log using the JDK logger
        catch (Throwable e2) {
            result.util = java.util.logging.Logger.getLogger(name);
        }

        // [#2085] Check if any of the INFO, DEBUG, TRACE levels might be
        // unavailable, e.g. because client code isn't using the latest version
        // of log4j or any other logger

        try {
            result.isTraceEnabled();
        }
        catch (Throwable e) {
            result.threshold = Log.Level.DEBUG;
        }

        try {
            result.isDebugEnabled();
        }
        catch (Throwable e) {
            result.threshold = Log.Level.INFO;
        }

        try {
            result.isInfoEnabled();
        }
        catch (Throwable e) {
            result.threshold = Log.Level.WARN;
        }

        return result;
    }

    private final Log.Level threshold() {

        Log.Level global = globalThreshold;
        Log.Level local = threshold;

        // [#15050] Turn off logging if specified by system properties
        String p = System.getProperty(propertyName);
        if (p != null) {
            try {
                threshold = local = Log.Level.valueOf(p.toUpperCase());
            }
            catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Unsupported log level for org.jooq.log." + name + ": " + p + ". Supported levels include: " + asList(Log.Level.values()), e);
            }
        }

        return local.supports(global) ? global : local;
    }

    private final void decrementLimitAndDo(IntConsumer runnable) {
        runnable.accept(limitMessages != null
            ? limitMessages.getAndUpdate(i -> Math.max(i - 1, 0))
            : 1
        );
    }

    /**
     * Check if <code>TRACE</code> level logging is enabled.
     */
    @Override
    public boolean isTraceEnabled() {
        return isTraceEnabled0(limitMessages != null && limitMessages.get() == 0);
    }

    private boolean isTraceEnabled0(boolean limitReached) {
        if (!threshold().supports(Log.Level.TRACE))
            return false;
        else if (limitReached)
            return false;
        else if (slf4j != null)
            return slf4j.isTraceEnabled();
        else
            return util.isLoggable(java.util.logging.Level.FINER);
    }

    /**
     * Log a message in <code>TRACE</code> level.
     *
     * @param message The log message
     */
    @Override
    public void trace(Object message) {
        trace(message, (Object) null);
    }

    /**
     * Log a message in <code>TRACE</code> level.
     *
     * @param message The log message
     * @param details The message details (padded to a constant-width message)
     */
    @Override
    public void trace(Object message, Object details) {
        decrementLimitAndDo(i -> {
            if (!isTraceEnabled0(i == 0))
                return;
            else if (slf4j != null)
                slf4j.trace(getMessage(message, details));
            else
                util.finer(getMessage(message, details));
        });
    }

    /**
     * Log a message in <code>TRACE</code> level.
     *
     * @param message The log message
     * @param throwable An exception whose stacktrace is logged along with the
     *            message
     */
    @Override
    public void trace(Object message, Throwable throwable) {
        trace(message, null, throwable);
    }

    /**
     * Log a message in <code>TRACE</code> level.
     *
     * @param message The log message
     * @param details The message details (padded to a constant-width message)
     * @param throwable An exception whose stacktrace is logged along with the
     *            message
     */
    @Override
    public void trace(Object message, Object details, Throwable throwable) {
        decrementLimitAndDo(i -> {
            if (!isTraceEnabled0(i == 0))
                return;
            else if (slf4j != null)
                slf4j.trace(getMessage(message, details), throwable);
            else
                util.log(java.util.logging.Level.FINER, getMessage(message, details), throwable);
        });
    }

    /**
     * Check if <code>DEBUG</code> level logging is enabled.
     */
    @Override
    public boolean isDebugEnabled() {
        return isDebugEnabled0(limitMessages != null && limitMessages.get() == 0);
    }

    private boolean isDebugEnabled0(boolean limitReached) {
        if (!threshold().supports(Log.Level.DEBUG))
            return false;
        else if (limitReached)
            return false;
        else if (slf4j != null)
            return slf4j.isDebugEnabled();
        else
            return util.isLoggable(java.util.logging.Level.FINE);
    }

    /**
     * Log a message in <code>DEBUG</code> level.
     *
     * @param message The log message
     */
    @Override
    public void debug(Object message) {
        debug(message, (Object) null);
    }

    /**
     * Log a message in <code>DEBUG</code> level.
     *
     * @param message The log message
     * @param details The message details (padded to a constant-width message)
     */
    @Override
    public void debug(Object message, Object details) {
        decrementLimitAndDo(i -> {
            if (!isDebugEnabled0(i == 0))
                return;
            else if (slf4j != null)
                slf4j.debug(getMessage(message, details));
            else
                util.fine(getMessage(message, details));
        });
    }

    /**
     * Log a message in <code>DEBUG</code> level.
     *
     * @param message The log message
     * @param throwable An exception whose stacktrace is logged along with the
     *            message
     */
    @Override
    public void debug(Object message, Throwable throwable) {
        debug(message, null, throwable);
    }

    /**
     * Log a message in <code>DEBUG</code> level.
     *
     * @param message The log message
     * @param details The message details (padded to a constant-width message)
     * @param throwable An exception whose stacktrace is logged along with the
     *            message
     */
    @Override
    public void debug(Object message, Object details, Throwable throwable) {
        decrementLimitAndDo(i -> {
            if (!isDebugEnabled0(i == 0))
                return;
            else if (slf4j != null)
                slf4j.debug(getMessage(message, details), throwable);
            else
                util.log(java.util.logging.Level.FINE, getMessage(message, details), throwable);
        });
    }

    /**
     * Check if <code>INFO</code> level logging is enabled.
     */
    @Override
    public boolean isInfoEnabled() {
        return isInfoEnabled0(limitMessages != null && limitMessages.get() == 0);
    }

    private boolean isInfoEnabled0(boolean limitReached) {
        if (!threshold().supports(Log.Level.INFO))
            return false;
        else if (limitReached)
            return false;
        else if (slf4j != null)
            return slf4j.isInfoEnabled();
        else
            return util.isLoggable(java.util.logging.Level.INFO);
    }

    /**
     * Log a message in <code>INFO</code> level.
     *
     * @param message The log message
     */
    @Override
    public void info(Object message) {
        info(message, (Object) null);
    }

    /**
     * Log a message in <code>INFO</code> level.
     *
     * @param message The log message
     * @param details The message details (padded to a constant-width message)
     */
    @Override
    public void info(Object message, Object details) {
        decrementLimitAndDo(i -> {
            if (!isInfoEnabled0(i == 0))
                return;
            else if (slf4j != null)
                slf4j.info(getMessage(message, details));
            else
                util.info(getMessage(message, details));
        });
    }

    /**
     * Log a message in <code>INFO</code> level.
     *
     * @param message The log message
     * @param throwable An exception whose stacktrace is logged along with the
     *            message
     */
    @Override
    public void info(Object message, Throwable throwable) {
        info(message, null, throwable);
    }

    /**
     * Log a message in <code>INFO</code> level.
     *
     * @param message The log message
     * @param details The message details (padded to a constant-width message)
     * @param throwable An exception whose stacktrace is logged along with the
     *            message
     */
    @Override
    public void info(Object message, Object details, Throwable throwable) {
        decrementLimitAndDo(i -> {
            if (!isInfoEnabled0(i == 0))
                return;
            else if (slf4j != null)
                slf4j.info(getMessage(message, details), throwable);
            else
                util.log(java.util.logging.Level.INFO, getMessage(message, details), throwable);
        });
    }

    /**
     * Check if <code>WARN</code> level logging is enabled.
     */
    @Override
    public boolean isWarnEnabled() {
        return isWarnEnabled0(limitMessages != null && limitMessages.get() == 0);
    }

    private boolean isWarnEnabled0(boolean limitReached) {
        if (!threshold().supports(Log.Level.WARN))
            return false;
        else if (limitReached)
            return false;
        else if (slf4j != null)
            return slf4j.isWarnEnabled();
        else
            return util.isLoggable(java.util.logging.Level.WARNING);
    }

    /**
     * Log a message in <code>WARN</code> level.
     *
     * @param message The log message
     */
    @Override
    public void warn(Object message) {
        warn(message, (Object) null);
    }

    /**
     * Log a message in <code>WARN</code> level.
     *
     * @param message The log message
     * @param details The message details (padded to a constant-width message)
     */
    @Override
    public void warn(Object message, Object details) {
        decrementLimitAndDo(i -> {
            if (!isWarnEnabled0(i == 0))
                return;
            else if (slf4j != null)
                slf4j.warn(getMessage(message, details));
            else
                util.warning(getMessage(message, details));
        });
    }

    /**
     * Log a message in <code>WARN</code> level.
     *
     * @param message The log message
     * @param throwable An exception whose stacktrace is logged along with the
     *            message
     */
    @Override
    public void warn(Object message, Throwable throwable) {
        warn(message, null, throwable);
    }

    /**
     * Log a message in <code>WARN</code> level.
     *
     * @param message The log message
     * @param details The message details (padded to a constant-width message)
     * @param throwable An exception whose stacktrace is logged along with the
     *            message
     */
    @Override
    public void warn(Object message, Object details, Throwable throwable) {
        decrementLimitAndDo(i -> {
            if (!isWarnEnabled0(i == 0))
                return;
            else if (slf4j != null)
                slf4j.warn(getMessage(message, details), throwable);
            else
                util.log(java.util.logging.Level.WARNING, getMessage(message, details), throwable);
        });
    }

    /**
     * Check if <code>ERROR</code> level logging is enabled.
     */
    @Override
    public boolean isErrorEnabled() {
        return isErrorEnabled0(limitMessages != null && limitMessages.get() == 0);
    }

    private boolean isErrorEnabled0(boolean limitReached) {
        if (!threshold().supports(Log.Level.ERROR))
            return false;
        else if (limitReached)
            return false;
        else if (slf4j != null)
            return slf4j.isErrorEnabled();
        else
            return util.isLoggable(java.util.logging.Level.SEVERE);
    }

    /**
     * Log a message in <code>ERROR</code> level.
     *
     * @param message The log message
     */
    @Override
    public void error(Object message) {
        error(message, (Object) null);
    }

    /**
     * Log a message in <code>ERROR</code> level.
     *
     * @param message The log message
     * @param details The message details (padded to a constant-width message)
     */
    @Override
    public void error(Object message, Object details) {
        decrementLimitAndDo(i -> {
            if (!isErrorEnabled0(i == 0))
                return;
            else if (slf4j != null)
                slf4j.error(getMessage(message, details));
            else
                util.severe(getMessage(message, details));
        });
    }

    /**
     * Log a message in <code>ERROR</code> level.
     *
     * @param message The log message
     * @param throwable An exception whose stacktrace is logged along with the
     *            message
     */
    @Override
    public void error(Object message, Throwable throwable) {
        error(message, null, throwable);
    }

    /**
     * Log a message in <code>ERROR</code> level.
     *
     * @param message The log message
     * @param details The message details (padded to a constant-width message)
     * @param throwable An exception whose stacktrace is logged along with the
     *            message
     */
    @Override
    public void error(Object message, Object details, Throwable throwable) {
        decrementLimitAndDo(i -> {
            if (!isErrorEnabled0(i == 0))
                return;
            else if (slf4j != null)
                slf4j.error(getMessage(message, details), throwable);
            else
                util.log(java.util.logging.Level.SEVERE, getMessage(message, details), throwable);
        });
    }

    /**
     * Log a message in a given log level.
     *
     * @param level The log level
     * @param message The log message
     */
    @Override
    public void log(Log.Level level, Object message) {
        log(level, message, (Object) null);
    }

    /**
     * Log a message in a given log level.
     *
     * @param level The log level
     * @param message The log message
     * @param details The message details (padded to a constant-width message)
     */
    @Override
    public void log(Log.Level level, Object message, Object details) {
        switch (level) {
            case TRACE: trace(message, details); break;
            case DEBUG: debug(message, details); break;
            case INFO:  info (message, details); break;
            case WARN:  warn (message, details); break;
            case ERROR:
            case FATAL: error(message, details); break;
        }
    }

    /**
     * Log a message in a given log level.
     *
     * @param level The log level
     * @param message The log message
     * @param throwable An exception whose stacktrace is logged along with the
     *            message
     */
    @Override
    public void log(Log.Level level, Object message, Throwable throwable) {
        log(level, message, null, throwable);
    }

    /**
     * Log a message in a given log level.
     *
     * @param level The log level
     * @param message The log message
     * @param details The message details (padded to a constant-width message)
     * @param throwable An exception whose stacktrace is logged along with the
     *            message
     */
    @Override
    public void log(Log.Level level, Object message, Object details, Throwable throwable) {
        switch (level) {
            case TRACE: trace(message, details, throwable); break;
            case DEBUG: debug(message, details, throwable); break;
            case INFO:  info (message, details, throwable); break;
            case WARN:  warn (message, details, throwable); break;
            case ERROR:
            case FATAL: error(message, details, throwable); break;
        }
    }

    /**
     * Get a formatted message.
     */
    private String getMessage(Object message, Object details) {
        StringBuilder sb = new StringBuilder();

        sb.append(StringUtils.rightPad("" + message, 25));

        if (details != null) {
            sb.append(": ");
            sb.append(details);
        }

        return sb.toString();
    }

    /**
     * Set a global level threshold to all JooqLoggers.
     */
    public static void globalThreshold(Level level) {
        switch (level) {
            case TRACE: globalThreshold(Log.Level.TRACE); break;
            case DEBUG: globalThreshold(Log.Level.DEBUG); break;
            case INFO:  globalThreshold(Log.Level.INFO);  break;
            case WARN:  globalThreshold(Log.Level.WARN);  break;
            case ERROR: globalThreshold(Log.Level.ERROR); break;
            case FATAL: globalThreshold(Log.Level.FATAL); break;
        }
    }

    /**
     * Set a global level threshold to all JooqLoggers.
     */
    public static void globalThreshold(Log.Level level) {
        globalThreshold = level;
    }

    /**
     * The log level.
     *
     * @deprecated - Use {@link org.jooq.Log.Level} instead
     */
    @Deprecated
    public static enum Level {

        TRACE,
        DEBUG,
        INFO,
        WARN,
        ERROR,
        FATAL;

        public boolean supports(Level level) {
            return ordinal() <= level.ordinal();
        }
    }

    /**
     * [#12405] The common {@link SimpleFormatter} format to be set in all of
     * jOOQ's CLIs.
     */
    public static void initSimpleFormatter() {
        if (System.getProperty("java.util.logging.SimpleFormatter.format") == null)
            System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tH:%1$tM:%1$tS %4$s %5$s%6$s%n");
    }
}
