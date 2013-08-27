/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 * 
 * This work is dual-licensed Open Source, under LGPL and jOOQ EULA
 * =============================================================================
 * You may freely choose which license applies to you. For more information 
 * about licensing, please visit http://www.jooq.org/licenses
 * 
 * LGPL:  
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 * 
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 * 
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details.
 * 
 * You should have received a copy of the jOOQ End User License Agreement
 * along with this library.
 * If not, see http://www.jooq.org/eula
 */
package org.jooq.tools;

import java.util.logging.Level;

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
 * <code>JooqLogger</code> tries to instanciate any of the above loggers,
 * catching potential {@link NoClassDefFoundError}'s in case any logger API
 * cannot be found on the classpath.
 *
 * @author Lukas Eder
 */
public final class JooqLogger {

    /**
     * The SLF4j Logger instance, if available.
     */
    private org.slf4j.Logger         slf4j;

    /**
     * The log4j Logger instance, if available.
     */
    private org.apache.log4j.Logger  log4j;

    /**
     * The JDK Logger instance, if available.
     */
    private java.util.logging.Logger util;

    /**
     * Whether calls to {@link #trace(Object)} are possible.
     */
    private boolean                  supportsTrace = true;

    /**
     * Whether calls to {@link #debug(Object)} are possible.
     */
    private boolean                  supportsDebug = true;

    /**
     * Whether calls to {@link #info(Object)} are possible.
     */
    private boolean                  supportsInfo  = true;

    /**
     * Get a logger wrapper for a class.
     */
    public static JooqLogger getLogger(Class<?> clazz) {
        JooqLogger result = new JooqLogger();

        // Prioritise slf4j
        try {
            result.slf4j = org.slf4j.LoggerFactory.getLogger(clazz);
        }

        // If that's not on the classpath, try log4j instead
        catch (Throwable e1) {
            try {
                result.log4j = org.apache.log4j.Logger.getLogger(clazz);
            }

            // If that's not on the classpath either, ignore most of logging
            catch (Throwable e2) {
                result.util = java.util.logging.Logger.getLogger(clazz.getName());
            }
        }

        // [#2085] Check if any of the INFO, DEBUG, TRACE levels might be
        // unavailable, e.g. because client code isn't using the latest version
        // of log4j or any other logger

        try {
            result.isInfoEnabled();
        }
        catch (Throwable e) {
            result.supportsInfo = false;
        }

        try {
            result.isDebugEnabled();
        }
        catch (Throwable e) {
            result.supportsDebug = false;
        }

        try {
            result.isTraceEnabled();
        }
        catch (Throwable e) {
            result.supportsTrace = false;
        }

        return result;
    }

    /**
     * Check if <code>TRACE</code> level logging is enabled.
     */
    public boolean isTraceEnabled() {
        if (!supportsTrace) {
            return false;
        }
        else if (slf4j != null) {
            return slf4j.isTraceEnabled();
        }
        else if (log4j != null) {
            return log4j.isTraceEnabled();
        }
        else {
            return util.isLoggable(Level.FINER);
        }
    }

    /**
     * Log a message in <code>TRACE</code> level.
     *
     * @param message The log message
     */
    public void trace(Object message) {
        trace(message, (Object) null);
    }

    /**
     * Log a message in <code>TRACE</code> level.
     *
     * @param message The log message
     * @param details The message details (padded to a constant-width message)
     */
    public void trace(Object message, Object details) {
        if (slf4j != null) {
            slf4j.trace(getMessage(message, details));
        }
        else if (log4j != null) {
            log4j.trace(getMessage(message, details));
        }
        else {
            util.finer("" + getMessage(message, details));
        }
    }

    /**
     * Log a message in <code>TRACE</code> level.
     *
     * @param message The log message
     * @param throwable An exception whose stacktrace is logged along with the
     *            message
     */
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
    public void trace(Object message, Object details, Throwable throwable) {
        if (slf4j != null) {
            slf4j.trace(getMessage(message, details), throwable);
        }
        else if (log4j != null) {
            log4j.trace(getMessage(message, details), throwable);
        }
        else {
            util.log(Level.FINER, "" + getMessage(message, details), throwable);
        }
    }

    /**
     * Check if <code>DEBUG</code> level logging is enabled.
     */
    public boolean isDebugEnabled() {
        if (!supportsDebug) {
            return false;
        }
        else if (slf4j != null) {
            return slf4j.isDebugEnabled();
        }
        else if (log4j != null) {
            return log4j.isDebugEnabled();
        }
        else {
            return util.isLoggable(Level.FINE);
        }
    }

    /**
     * Log a message in <code>DEBUG</code> level.
     *
     * @param message The log message
     */
    public void debug(Object message) {
        debug(message, (Object) null);
    }

    /**
     * Log a message in <code>DEBUG</code> level.
     *
     * @param message The log message
     * @param details The message details (padded to a constant-width message)
     */
    public void debug(Object message, Object details) {
        if (slf4j != null) {
            slf4j.debug(getMessage(message, details));
        }
        else if (log4j != null) {
            log4j.debug(getMessage(message, details));
        }
        else {
            util.fine("" + getMessage(message, details));
        }
    }

    /**
     * Log a message in <code>DEBUG</code> level.
     *
     * @param message The log message
     * @param throwable An exception whose stacktrace is logged along with the
     *            message
     */
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
    public void debug(Object message, Object details, Throwable throwable) {
        if (slf4j != null) {
            slf4j.debug(getMessage(message, details), throwable);
        }
        else if (log4j != null) {
            log4j.debug(getMessage(message, details), throwable);
        }
        else {
            util.log(Level.FINE, "" + getMessage(message, details), throwable);
        }
    }

    /**
     * Check if <code>INFO</code> level logging is enabled.
     */
    public boolean isInfoEnabled() {
        if (!supportsInfo) {
            return false;
        }
        else if (slf4j != null) {
            return slf4j.isInfoEnabled();
        }
        else if (log4j != null) {
            return log4j.isInfoEnabled();
        }
        else {
            return util.isLoggable(Level.INFO);
        }
    }

    /**
     * Log a message in <code>INFO</code> level.
     *
     * @param message The log message
     */
    public void info(Object message) {
        info(message, (Object) null);
    }

    /**
     * Log a message in <code>INFO</code> level.
     *
     * @param message The log message
     * @param details The message details (padded to a constant-width message)
     */
    public void info(Object message, Object details) {
        if (slf4j != null) {
            slf4j.info(getMessage(message, details));
        }
        else if (log4j != null) {
            log4j.info(getMessage(message, details));
        }
        else {
            util.info("" + getMessage(message, details));
        }
    }

    /**
     * Log a message in <code>INFO</code> level.
     *
     * @param message The log message
     * @param throwable An exception whose stacktrace is logged along with the
     *            message
     */
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
    public void info(Object message, Object details, Throwable throwable) {
        if (slf4j != null) {
            slf4j.info(getMessage(message, details), throwable);
        }
        else if (log4j != null) {
            log4j.info(getMessage(message, details), throwable);
        }
        else {
            util.log(Level.INFO, "" + getMessage(message, details), throwable);
        }
    }

    /**
     * Log a message in <code>WARN</code> level.
     *
     * @param message The log message
     */
    public void warn(Object message) {
        warn(message, (Object) null);
    }

    /**
     * Log a message in <code>WARN</code> level.
     *
     * @param message The log message
     * @param details The message details (padded to a constant-width message)
     */
    public void warn(Object message, Object details) {
        if (slf4j != null) {
            slf4j.warn(getMessage(message, details));
        }
        else if (log4j != null) {
            log4j.warn(getMessage(message, details));
        }
        else {
            util.warning("" + getMessage(message, details));
        }
    }

    /**
     * Log a message in <code>WARN</code> level.
     *
     * @param message The log message
     * @param throwable An exception whose stacktrace is logged along with the
     *            message
     */
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
    public void warn(Object message, Object details, Throwable throwable) {
        if (slf4j != null) {
            slf4j.warn(getMessage(message, details), throwable);
        }
        else if (log4j != null) {
            log4j.warn(getMessage(message, details), throwable);
        }
        else {
            util.log(Level.WARNING, "" + getMessage(message, details), throwable);
        }
    }

    /**
     * Log a message in <code>ERROR</code> level.
     *
     * @param message The log message
     */
    public void error(Object message) {
        error(message, (Object) null);
    }

    /**
     * Log a message in <code>ERROR</code> level.
     *
     * @param message The log message
     * @param details The message details (padded to a constant-width message)
     */
    public void error(Object message, Object details) {
        if (slf4j != null) {
            slf4j.error(getMessage(message, details));
        }
        else if (log4j != null) {
            log4j.error(getMessage(message, details));
        }
        else {
            util.severe("" + getMessage(message, details));
        }
    }

    /**
     * Log a message in <code>ERROR</code> level.
     *
     * @param message The log message
     * @param throwable An exception whose stacktrace is logged along with the
     *            message
     */
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
    public void error(Object message, Object details, Throwable throwable) {
        if (slf4j != null) {
            slf4j.error(getMessage(message, details), throwable);
        }
        else if (log4j != null) {
            log4j.error(getMessage(message, details), throwable);
        }
        else {
            util.log(Level.SEVERE, "" + getMessage(message, details), throwable);
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
}
