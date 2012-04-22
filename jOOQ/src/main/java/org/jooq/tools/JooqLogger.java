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
package org.jooq.tools;

import java.util.logging.Level;




/**
 * The jOOQ logger abstraction
 *
 * @author Lukas Eder
 */
public final class JooqLogger {

    private org.slf4j.Logger slf4j;
    private org.apache.log4j.Logger log4j;
    private java.util.logging.Logger util;

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

        return result;
    }

    public boolean isTraceEnabled() {
        if (slf4j != null) {
            return slf4j.isTraceEnabled();
        }
        else if (log4j != null) {
            return log4j.isTraceEnabled();
        }
        else {
            return util.isLoggable(Level.FINER);
        }
    }

    public void trace(Object message) {
        trace(message, (Object) null);
    }

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

    public void trace(Object message, Throwable t) {
        trace(message, null, t);
    }

    public void trace(Object message, Object details, Throwable t) {
        if (slf4j != null) {
            slf4j.trace(getMessage(message, details), t);
        }
        else if (log4j != null) {
            log4j.trace(getMessage(message, details), t);
        }
        else {
            util.log(Level.FINER, "" + getMessage(message, details), t);
        }
    }


    public boolean isDebugEnabled() {
        if (slf4j != null) {
            return slf4j.isDebugEnabled();
        }
        else if (log4j != null) {
            return log4j.isDebugEnabled();
        }
        else {
            return util.isLoggable(Level.FINE);
        }
    }

    public void debug(Object message) {
        debug(message, (Object) null);
    }

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

    public void debug(Object message, Throwable t) {
        debug(message, null, t);
    }

    public void debug(Object message, Object details, Throwable t) {
        if (slf4j != null) {
            slf4j.debug(getMessage(message, details), t);
        }
        else if (log4j != null) {
            log4j.debug(getMessage(message, details), t);
        }
        else {
            util.log(Level.FINE, "" + getMessage(message, details), t);
        }
    }


    public boolean isInfoEnabled() {
        if (slf4j != null) {
            return slf4j.isInfoEnabled();
        }
        else if (log4j != null) {
            return log4j.isInfoEnabled();
        }
        else {
            return util.isLoggable(Level.INFO);
        }
    }

    public void info(Object message) {
        info(message, (Object) null);
    }

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

    public void info(Object message, Throwable t) {
        info(message, null, t);
    }

    public void info(Object message, Object details, Throwable t) {
        if (slf4j != null) {
            slf4j.info(getMessage(message, details), t);
        }
        else if (log4j != null) {
            log4j.info(getMessage(message, details), t);
        }
        else {
            util.log(Level.INFO, "" + getMessage(message, details), t);
        }
    }

    public void warn(Object message) {
        warn(message, (Object) null);
    }

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

    public void warn(Object message, Throwable t) {
        warn(message, null, t);
    }

    public void warn(Object message, Object details, Throwable t) {
        if (slf4j != null) {
            slf4j.warn(getMessage(message, details), t);
        }
        else if (log4j != null) {
            log4j.warn(getMessage(message, details), t);
        }
        else {
            util.log(Level.WARNING, "" + getMessage(message, details), t);
        }
    }


    public void error(Object message) {
        error(message, (Object) null);
    }

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

    public void error(Object message, Throwable t) {
        error(message, null, t);
    }

    public void error(Object message, Object details, Throwable t) {
        if (slf4j != null) {
            slf4j.error(getMessage(message, details), t);
        }
        else if (log4j != null) {
            log4j.error(getMessage(message, details), t);
        }
        else {
            util.log(Level.SEVERE, "" + getMessage(message, details), t);
        }
    }

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
