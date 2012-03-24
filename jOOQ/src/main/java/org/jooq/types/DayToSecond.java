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
package org.jooq.types;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jooq.tools.StringUtils;

/**
 * An implementation for the SQL standard <code>INTERVAL YEAR TO MONTH</code>
 * data type.
 *
 * @author Lukas Eder
 * @see Interval
 */
public final class DayToSecond implements Interval<DayToSecond> {

    /**
     * Generated UID
     */
    private static final long    serialVersionUID = -3853596481984643811L;
    private static final Pattern PATTERN          = Pattern.compile("(\\+|-)?(\\d+) (\\d+):(\\d+):(\\d+)\\.(\\d+)");

    private final boolean        negative;
    private final int            days;
    private final int            hours;
    private final int            minutes;
    private final int            seconds;
    private final int            nano;

    /**
     * Create a new day-hour interval.
     */
    public DayToSecond(int days) {
        this(days, 0, 0, 0, 0, false);
    }

    /**
     * Create a new day-hour interval.
     */
    public DayToSecond(int days, int hours) {
        this(days, hours, 0, 0, 0, false);
    }

    /**
     * Create a new day-hour interval.
     */
    public DayToSecond(int days, int hours, int minutes) {
        this(days, hours, minutes, 0, 0, false);
    }

    /**
     * Create a new day-hour interval.
     */
    public DayToSecond(int days, int hours, int minutes, int seconds) {
        this(days, hours, minutes, seconds, 0, false);
    }

    /**
     * Create a new day-hour interval.
     */
    public DayToSecond(int days, int hours, int minutes, int seconds, int nano) {
        this(days, hours, minutes, seconds, nano, false);
    }

    private DayToSecond(int days, int hours, int minutes, int seconds, int nano, boolean negative) {
        this.negative = negative;
        this.days = days;
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
        this.nano = nano;
    }

    /**
     * Parse a string representation of a <code>INTERVAL DAY TO SECOND</code>
     *
     * @param string A string representation of the form
     *            <code>[+|-][days] [hours]:[minutes]:[seconds].[fractional seconds]</code>
     * @return The parsed <code>INTERVAL DAY TO SECOND</code> object, or
     *         <code>null</code> if the string could not be parsed.
     */
    public static DayToSecond valueOf(String string) {
        if (string != null) {
            Matcher matcher = PATTERN.matcher(string);

            if (matcher.find()) {
                boolean negative = "-".equals(matcher.group(1));
                int days = Integer.parseInt(matcher.group(2));
                int hours = Integer.parseInt(matcher.group(3));
                int minutes = Integer.parseInt(matcher.group(4));
                int seconds = Integer.parseInt(matcher.group(5));
                int nano = Integer.parseInt(StringUtils.rightPad(matcher.group(6), 9, "0"));

                return new DayToSecond(days, hours, minutes, seconds, nano, negative);
            }
        }

        return null;
    }

    // -------------------------------------------------------------------------
    // XXX Inteval API
    // -------------------------------------------------------------------------

    @Override
    public final DayToSecond neg() {
        return new DayToSecond(days, hours, minutes, seconds, nano, !negative);
    }

    @Override
    public final DayToSecond abs() {
        return new DayToSecond(days, hours, minutes, seconds, nano, false);
    }

    // -------------------------------------------------------------------------
    // XXX Comparable and Object API
    // -------------------------------------------------------------------------

    @Override
    public final int compareTo(DayToSecond that) {
        if (days < that.days) {
            return -1;
        }
        if (days > that.days) {
            return 1;
        }

        if (hours < that.hours) {
            return -1;
        }
        if (hours > that.hours) {
            return 1;
        }

        if (minutes < that.minutes) {
            return -1;
        }
        if (minutes > that.minutes) {
            return 1;
        }

        if (seconds < that.seconds) {
            return -1;
        }
        if (seconds > that.seconds) {
            return 1;
        }

        if (nano < that.nano) {
            return -1;
        }
        if (nano > that.nano) {
            return 1;
        }

        return 0;
    }

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + days;
        result = prime * result + hours;
        result = prime * result + minutes;
        result = prime * result + nano;
        result = prime * result + seconds;
        return result;
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DayToSecond other = (DayToSecond) obj;
        if (days != other.days)
            return false;
        if (hours != other.hours)
            return false;
        if (minutes != other.minutes)
            return false;
        if (nano != other.nano)
            return false;
        if (seconds != other.seconds)
            return false;
        return true;
    }

    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(negative ? "-" : "+");
        sb.append(days);
        sb.append(" ");

        if (hours < 10)
            sb.append("0");
        sb.append(hours);
        sb.append(":");

        if (minutes < 10)
            sb.append("0");
        sb.append(minutes);
        sb.append(":");

        if (seconds < 10)
            sb.append("0");
        sb.append(seconds);
        sb.append(".");
        sb.append(nano);

        return sb.toString();
    }
}
