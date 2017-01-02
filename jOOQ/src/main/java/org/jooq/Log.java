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
package org.jooq;

/**
 * A common logger abstraction API for jOOQ's internal logging.
 *
 * @author Lukas Eder
 */
public interface Log {

    /**
     * Check if <code>TRACE</code> level logging is enabled.
     */
    boolean isTraceEnabled();

    /**
     * Log a message in <code>TRACE</code> level.
     *
     * @param message The log message
     */
    void trace(Object message);

    /**
     * Log a message in <code>TRACE</code> level.
     *
     * @param message The log message
     * @param details The message details (padded to a constant-width message);
     */
    void trace(Object message, Object details);

    /**
     * Log a message in <code>TRACE</code> level.
     *
     * @param message The log message
     * @param throwable An exception whose stacktrace is logged along with the
     *            message
     */
    void trace(Object message, Throwable throwable);

    /**
     * Log a message in <code>TRACE</code> level.
     *
     * @param message The log message
     * @param details The message details (padded to a constant-width message);
     * @param throwable An exception whose stacktrace is logged along with the
     *            message
     */
    void trace(Object message, Object details, Throwable throwable);

    /**
     * Check if <code>DEBUG</code> level logging is enabled.
     */
    boolean isDebugEnabled();

    /**
     * Log a message in <code>DEBUG</code> level.
     *
     * @param message The log message
     */
    void debug(Object message);

    /**
     * Log a message in <code>DEBUG</code> level.
     *
     * @param message The log message
     * @param details The message details (padded to a constant-width message);
     */
    void debug(Object message, Object details);

    /**
     * Log a message in <code>DEBUG</code> level.
     *
     * @param message The log message
     * @param throwable An exception whose stacktrace is logged along with the
     *            message
     */
    void debug(Object message, Throwable throwable);

    /**
     * Log a message in <code>DEBUG</code> level.
     *
     * @param message The log message
     * @param details The message details (padded to a constant-width message);
     * @param throwable An exception whose stacktrace is logged along with the
     *            message
     */
    void debug(Object message, Object details, Throwable throwable);

    /**
     * Check if <code>INFO</code> level logging is enabled.
     */
    boolean isInfoEnabled();

    /**
     * Log a message in <code>INFO</code> level.
     *
     * @param message The log message
     */
    void info(Object message);

    /**
     * Log a message in <code>INFO</code> level.
     *
     * @param message The log message
     * @param details The message details (padded to a constant-width message);
     */
    void info(Object message, Object details);

    /**
     * Log a message in <code>INFO</code> level.
     *
     * @param message The log message
     * @param throwable An exception whose stacktrace is logged along with the
     *            message
     */
    void info(Object message, Throwable throwable);

    /**
     * Log a message in <code>INFO</code> level.
     *
     * @param message The log message
     * @param details The message details (padded to a constant-width message);
     * @param throwable An exception whose stacktrace is logged along with the
     *            message
     */
    void info(Object message, Object details, Throwable throwable);

    /**
     * Log a message in <code>WARN</code> level.
     *
     * @param message The log message
     */
    void warn(Object message);

    /**
     * Log a message in <code>WARN</code> level.
     *
     * @param message The log message
     * @param details The message details (padded to a constant-width message);
     */
    void warn(Object message, Object details);

    /**
     * Log a message in <code>WARN</code> level.
     *
     * @param message The log message
     * @param throwable An exception whose stacktrace is logged along with the
     *            message
     */
    void warn(Object message, Throwable throwable);

    /**
     * Log a message in <code>WARN</code> level.
     *
     * @param message The log message
     * @param details The message details (padded to a constant-width message);
     * @param throwable An exception whose stacktrace is logged along with the
     *            message
     */
    void warn(Object message, Object details, Throwable throwable);

    /**
     * Log a message in <code>ERROR</code> level.
     *
     * @param message The log message
     */
    void error(Object message);

    /**
     * Log a message in <code>ERROR</code> level.
     *
     * @param message The log message
     * @param details The message details (padded to a constant-width message);
     */
    void error(Object message, Object details);

    /**
     * Log a message in <code>ERROR</code> level.
     *
     * @param message The log message
     * @param throwable An exception whose stacktrace is logged along with the
     *            message
     */
    void error(Object message, Throwable throwable);

    /**
     * Log a message in <code>ERROR</code> level.
     *
     * @param message The log message
     * @param details The message details (padded to a constant-width message);
     * @param throwable An exception whose stacktrace is logged along with the
     *            message
     */
    void error(Object message, Object details, Throwable throwable);

    /**
     * The log level.
     */
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
}
