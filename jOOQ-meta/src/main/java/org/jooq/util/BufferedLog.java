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
package org.jooq.util;

import static org.jooq.Log.Level.ERROR;
import static org.jooq.Log.Level.WARN;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.jooq.Log;
import org.jooq.tools.JooqLogger;

/**
 * A logger that buffers warnings and error messages in order to repeat them in
 * a consolidated manner at the end of a code generation run.
 *
 * @author Lukas Eder
 */
public class BufferedLog implements Log {

    private static final Queue<Message> messages = new ConcurrentLinkedQueue<Message>();
    private final JooqLogger            delegate;

    public static BufferedLog getLogger(Class<?> type) {
        return new BufferedLog(JooqLogger.getLogger(type));
    }

    BufferedLog(JooqLogger delegate) {
        this.delegate = delegate;
    }

    /**
     * Perform the actual logging.
     */
    public static synchronized void flush() {
        JooqLogger delegate = JooqLogger.getLogger(BufferedLog.class);

        if (!messages.isEmpty()) {
            delegate.warn("Buffered warning and error messages:");
            delegate.warn("------------------------------------");
        }

        for (Message m : messages)
            switch (m.level) {
                case DEBUG: delegate.debug(m.message, m.details, m.throwable); break;
                case TRACE: delegate.trace(m.message, m.details, m.throwable); break;
                case INFO:  delegate.info (m.message, m.details, m.throwable); break;
                case WARN:  delegate.warn (m.message, m.details, m.throwable); break;
                case ERROR: delegate.error(m.message, m.details, m.throwable); break;
                case FATAL: delegate.error(m.message, m.details, m.throwable); break;
            }

        messages.clear();
    }

    static Message message(Level level, Object message) {
        return new Message(level, message, null, null);
    }

    static Message message(Level level, Object message, Object details) {
        return new Message(level, message, details, null);
    }

    static Message message(Level level, Object message, Throwable throwable) {
        return new Message(level, message, null, throwable);
    }

    static Message message(Level level, Object message, Object details, Throwable throwable) {
        return new Message(level, message, details, throwable);
    }

    private static class Message {
        final Level     level;
        final Object    message;
        final Object    details;
        final Throwable throwable;

        Message(Level level, Object message, Object details, Throwable throwable) {
            this.level = level;
            this.message = message;
            this.details = details;
            this.throwable = throwable;
        }
    }

    @Override
    public boolean isTraceEnabled() {
        return delegate.isTraceEnabled();
    }

    @Override
    public void trace(Object message) {
        delegate.trace(message);
        // messages.add(message(TRACE, message));
    }

    @Override
    public void trace(Object message, Object details) {
        delegate.trace(message, details);
        // messages.add(message(TRACE, message, details));
    }

    @Override
    public void trace(Object message, Throwable throwable) {
        delegate.trace(message, throwable);
        // messages.add(message(TRACE, message, throwable));
    }

    @Override
    public void trace(Object message, Object details, Throwable throwable) {
        delegate.trace(message, details, throwable);
        // messages.add(message(TRACE, message, details, throwable));
    }

    @Override
    public boolean isDebugEnabled() {
        return delegate.isDebugEnabled();
    }

    @Override
    public void debug(Object message) {
        delegate.debug(message);
        // messages.add(message(DEBUG, message));
    }

    @Override
    public void debug(Object message, Object details) {
        delegate.debug(message, details);
        // messages.add(message(DEBUG, details));
    }

    @Override
    public void debug(Object message, Throwable throwable) {
        delegate.debug(message, throwable);
        // messages.add(message(DEBUG, message, throwable));
    }

    @Override
    public void debug(Object message, Object details, Throwable throwable) {
        delegate.debug(message, details, throwable);
        // messages.add(message(DEBUG, message, details, throwable));
    }

    @Override
    public boolean isInfoEnabled() {
        return delegate.isInfoEnabled();
    }

    @Override
    public void info(Object message) {
        delegate.info(message);
        // messages.add(message(INFO, message));
    }

    @Override
    public void info(Object message, Object details) {
        delegate.info(message, details);
        // messages.add(message(INFO, message, details));
    }

    @Override
    public void info(Object message, Throwable throwable) {
        delegate.info(message, throwable);
        // messages.add(message(INFO, message, throwable));
    }

    @Override
    public void info(Object message, Object details, Throwable throwable) {
        delegate.info(message, details, throwable);
        // messages.add(message(INFO, message, details, throwable));
    }

    @Override
    public void warn(Object message) {
        delegate.warn(message);
        messages.add(message(WARN, message));
    }

    @Override
    public void warn(Object message, Object details) {
        delegate.warn(message, details);
        messages.add(message(WARN, message, details));
    }

    @Override
    public void warn(Object message, Throwable throwable) {
        delegate.warn(message, throwable);
        messages.add(message(WARN, message, throwable));
    }

    @Override
    public void warn(Object message, Object details, Throwable throwable) {
        delegate.warn(message, details, throwable);
        messages.add(message(WARN, message, details, throwable));
    }

    @Override
    public void error(Object message) {
        delegate.error(message);
        messages.add(message(ERROR, message));
    }

    @Override
    public void error(Object message, Object details) {
        delegate.error(message, details);
        messages.add(message(ERROR, message, details));
    }

    @Override
    public void error(Object message, Throwable throwable) {
        delegate.error(message, throwable);
        messages.add(message(ERROR, message, throwable));
    }

    @Override
    public void error(Object message, Object details, Throwable throwable) {
        delegate.error(message, details, throwable);
        messages.add(message(ERROR, message, details, throwable));
    }
}
