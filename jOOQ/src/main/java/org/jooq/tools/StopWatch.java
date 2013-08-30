/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is triple-licensed under ASL 2.0, AGPL 3.0, and jOOQ EULA
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   ASL 2.0 or jOOQ EULA.
 * - If you're using this work with at least one commercial database, you may
 *   choose AGPL 3.0 or jOOQ EULA.
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * AGPL 3.0
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 *
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details: http://www.jooq.org/eula
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
