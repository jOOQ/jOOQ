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
