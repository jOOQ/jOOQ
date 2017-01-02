/*
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
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
     * Split the time and trace log a message, if trace logging is enabled.
     */
    public void splitTrace(String message) {
        if (log.isTraceEnabled())
            log.trace(message, splitMessage(0));
    }

    /**
     * Split the time and trace log a message if the split time exceeds a
     * certain threshold and if trace logging is enabled.
     */
    public void splitTrace(String message, long thresholdNano) {
        if (log.isTraceEnabled()) {
            String splitMessage = splitMessage(thresholdNano);

            if (splitMessage != null)
                log.trace(message, splitMessage);
        }
    }

    /**
     * Split the time and debug log a message, if trace logging is enabled
     */
    public void splitDebug(String message) {
        if (log.isDebugEnabled())
            log.debug(message, splitMessage(0));
    }

    /**
     * Split the time and debug log a message if the split time exceeds a
     * certain threshold and if  trace logging is enabled
     */
    public void splitDebug(String message, long thresholdNano) {
        if (log.isDebugEnabled()) {
            String splitMessage = splitMessage(thresholdNano);

            if (splitMessage != null)
                log.debug(message, splitMessage);
        }
    }

    /**
     * Split the time and info log a message, if trace logging is enabled
     */
    public void splitInfo(String message) {
        if (log.isInfoEnabled())
            log.info(message, splitMessage(0));
    }

    /**
     * Split the time and info log a message if the split time exceeds a
     * certain threshold and if  trace logging is enabled
     */
    public void splitInfo(String message, long thresholdNano) {
        if (log.isInfoEnabled()) {
            String splitMessage = splitMessage(thresholdNano);

            if (splitMessage != null)
                log.info(message, splitMessage);
        }
    }

    /**
     * Split the time and warn log a message, if trace logging is enabled
     */
    public void splitWarn(String message) {
        log.warn(message, splitMessage(0));
    }

    /**
     * Split the time and warn log a message if the split time exceeds a
     * certain threshold and if  trace logging is enabled
     */
    public void splitWarn(String message, long thresholdNano) {
        String splitMessage = splitMessage(thresholdNano);

        if (splitMessage != null)
            log.warn(message, splitMessage);
    }

    public long split() {
        return System.nanoTime() - start;
    }

    private String splitMessage(long thresholdNano) {
        final long temp = split;
        split = System.nanoTime();
        final long inc = split - temp;

        if (thresholdNano > 0 && inc < thresholdNano)
            return null;

        if (temp == start)
            return "Total: " + format(split - start);
        else
            return "Total: " + format(split - start) + ", +" + format(inc);
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
