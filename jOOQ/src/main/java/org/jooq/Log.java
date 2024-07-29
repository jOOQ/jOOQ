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
 * Apache-2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
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
package org.jooq;


/**
 * A common logger abstraction API for jOOQ's internal logging.
 *
 * @author Lukas Eder
 */
public interface Log {

    /**
     * Check if {@link Log.Level#TRACE} level logging is enabled.
     */
    boolean isTraceEnabled();

    /**
     * Log a message in {@link Log.Level#TRACE} level.
     *
     * @param message The log message
     */
    void trace(Object message);

    /**
     * Log a message in {@link Log.Level#TRACE} level.
     *
     * @param message The log message
     * @param details The message details (padded to a constant-width message);
     */
    void trace(Object message, Object details);

    /**
     * Log a message in {@link Log.Level#TRACE} level.
     *
     * @param message The log message
     * @param throwable An exception whose stacktrace is logged along with the
     *            message
     */
    void trace(Object message, Throwable throwable);

    /**
     * Log a message in {@link Log.Level#TRACE} level.
     *
     * @param message The log message
     * @param details The message details (padded to a constant-width message);
     * @param throwable An exception whose stacktrace is logged along with the
     *            message
     */
    void trace(Object message, Object details, Throwable throwable);

    /**
     * Check if {@link Log.Level#DEBUG} level logging is enabled.
     */
    boolean isDebugEnabled();

    /**
     * Log a message in {@link Log.Level#DEBUG} level.
     *
     * @param message The log message
     */
    void debug(Object message);

    /**
     * Log a message in {@link Log.Level#DEBUG} level.
     *
     * @param message The log message
     * @param details The message details (padded to a constant-width message);
     */
    void debug(Object message, Object details);

    /**
     * Log a message in {@link Log.Level#DEBUG} level.
     *
     * @param message The log message
     * @param throwable An exception whose stacktrace is logged along with the
     *            message
     */
    void debug(Object message, Throwable throwable);

    /**
     * Log a message in {@link Log.Level#DEBUG} level.
     *
     * @param message The log message
     * @param details The message details (padded to a constant-width message);
     * @param throwable An exception whose stacktrace is logged along with the
     *            message
     */
    void debug(Object message, Object details, Throwable throwable);

    /**
     * Check if {@link Log.Level#INFO} level logging is enabled.
     */
    boolean isInfoEnabled();

    /**
     * Log a message in {@link Log.Level#INFO} level.
     *
     * @param message The log message
     */
    void info(Object message);

    /**
     * Log a message in {@link Log.Level#INFO} level.
     *
     * @param message The log message
     * @param details The message details (padded to a constant-width message);
     */
    void info(Object message, Object details);

    /**
     * Log a message in {@link Log.Level#INFO} level.
     *
     * @param message The log message
     * @param throwable An exception whose stacktrace is logged along with the
     *            message
     */
    void info(Object message, Throwable throwable);

    /**
     * Log a message in {@link Log.Level#INFO} level.
     *
     * @param message The log message
     * @param details The message details (padded to a constant-width message);
     * @param throwable An exception whose stacktrace is logged along with the
     *            message
     */
    void info(Object message, Object details, Throwable throwable);

    /**
     * Check if {@link Log.Level#WARN} level logging is enabled.
     */
    boolean isWarnEnabled();

    /**
     * Log a message in {@link Log.Level#WARN} level.
     *
     * @param message The log message
     */
    void warn(Object message);

    /**
     * Log a message in {@link Log.Level#WARN} level.
     *
     * @param message The log message
     * @param details The message details (padded to a constant-width message);
     */
    void warn(Object message, Object details);

    /**
     * Log a message in {@link Log.Level#WARN} level.
     *
     * @param message The log message
     * @param throwable An exception whose stacktrace is logged along with the
     *            message
     */
    void warn(Object message, Throwable throwable);

    /**
     * Log a message in {@link Log.Level#WARN} level.
     *
     * @param message The log message
     * @param details The message details (padded to a constant-width message);
     * @param throwable An exception whose stacktrace is logged along with the
     *            message
     */
    void warn(Object message, Object details, Throwable throwable);

    /**
     * Check if {@link Log.Level#ERROR} level logging is enabled.
     */
    boolean isErrorEnabled();

    /**
     * Log a message in {@link Log.Level#ERROR} level.
     *
     * @param message The log message
     */
    void error(Object message);

    /**
     * Log a message in {@link Log.Level#ERROR} level.
     *
     * @param message The log message
     * @param details The message details (padded to a constant-width message);
     */
    void error(Object message, Object details);

    /**
     * Log a message in {@link Log.Level#ERROR} level.
     *
     * @param message The log message
     * @param throwable An exception whose stacktrace is logged along with the
     *            message
     */
    void error(Object message, Throwable throwable);

    /**
     * Log a message in {@link Log.Level#ERROR} level.
     *
     * @param message The log message
     * @param details The message details (padded to a constant-width message);
     * @param throwable An exception whose stacktrace is logged along with the
     *            message
     */
    void error(Object message, Object details, Throwable throwable);

    /**
     * Log a message in a given log level.
     *
     * @param level The log level
     * @param message The log message
     */
    void log(Level level, Object message);

    /**
     * Log a message in a given log level.
     *
     * @param level The log level
     * @param message The log message
     * @param details The message details (padded to a constant-width message);
     */
    void log(Level level, Object message, Object details);

    /**
     * Log a message in a given log level.
     *
     * @param level The log level
     * @param message The log message
     * @param throwable An exception whose stacktrace is logged along with the
     *            message
     */
    void log(Level level, Object message, Throwable throwable);

    /**
     * Log a message in a given log level.
     *
     * @param level The log level
     * @param message The log message
     * @param details The message details (padded to a constant-width message);
     * @param throwable An exception whose stacktrace is logged along with the
     *            message
     */
    void log(Level level, Object message, Object details, Throwable throwable);

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

        /**
         * If this level is a lower level than the argument level.
         */
        public boolean supports(Level level) {
            return ordinal() <= level.ordinal();
        }
    }
}
