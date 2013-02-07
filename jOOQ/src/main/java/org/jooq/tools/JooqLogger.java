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
