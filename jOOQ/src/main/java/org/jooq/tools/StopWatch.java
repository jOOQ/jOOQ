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



/**
 * A time measuring device
 *
 * @author Lukas Eder
 */
public final class StopWatch {

    private static final JooqLogger log = JooqLogger.getLogger(StopWatch.class);
    private long             start;
    private long             split;

    /**
     * Initialise the stop watch
     */
    public StopWatch() {
        this.start = System.nanoTime();
        this.split = start;
    }

    /**
     * Split the time and trace log a message, if trace logging is enabled
     */
    public void splitTrace(String message) {
        if (log.isTraceEnabled()) {
            log.trace(message, splitMessage());
        }
    }

    /**
     * Split the time and debug log a message, if trace logging is enabled
     */
    public void splitDebug(String message) {
        if (log.isDebugEnabled()) {
            log.debug(message, splitMessage());
        }
    }

    /**
     * Split the time and info log a message, if trace logging is enabled
     */
    public void splitInfo(String message) {
        if (log.isInfoEnabled()) {
            log.info(message, splitMessage());
        }
    }

    private String splitMessage() {
        final long temp = split;
        split = System.nanoTime();

        if (temp == start) {
            return "Total: " + format(split - start);
        }
        else {
            return "Total: " + format(split - start) + ", +" + format(split - temp);
        }
    }

    public static String format(long nanoTime) {

        // If more than one minute, format in HH:mm:ss
        if (nanoTime > (60L * 1000L * 1000000L)) {
            return formatHours(nanoTime / (1000L * 1000000L));
        }

        // If more than one second, display seconds with milliseconds
        else if (nanoTime > (1000L * 1000000L)) {
            return ((nanoTime / 1000000L) / 1000.0) + "s";
        }

        // If less than one second, display milliseconds with microseconds
        else {
            return ((nanoTime / 1000L) / 1000.0) + "ms";
        }
    }

    public static String formatHours(long seconds) {
        long s = seconds % 60L;
        long m = (seconds / 60L) % 60L;
        long h = (seconds / 3600L);

        StringBuilder sb = new StringBuilder();

        if (h == 0) {
            // nop
        }
        else if (h < 10) {
            sb.append("0");
            sb.append(h);
            sb.append(":");
        }
        else {
            sb.append(h);
            sb.append(":");
        }

        if (m < 10) {
            sb.append("0");
            sb.append(m);
            sb.append(":");
        } else {
            sb.append(m);
            sb.append(":");
        }

        if (s < 10) {
            sb.append("0");
            sb.append(s);
        } else {
            sb.append(s);
        }

        return sb.toString();
    }
}
