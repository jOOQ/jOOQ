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
package org.jooq.types;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jooq.Field;
import org.jooq.SQLDialect;
import org.jooq.tools.Convert;
import org.jooq.tools.StringUtils;

/**
 * An implementation for the SQL standard <code>INTERVAL DAY TO SECOND</code>
 * data type.
 * <p>
 * <code>DayToSecond</code> is a {@link Number} whose {@link Number#intValue()}
 * represents the (truncated) number of milliseconds of the interval,
 * {@link Number#doubleValue()} represents the approximative number of
 * milliseconds (including hours, minutes, seconds, nanoseconds) of the
 * interval.
 * <p>
 * Note: only a few databases actually support this data type on its own. You
 * can still use it for date time arithmetic in other databases, though, through
 * {@link Field#add(Field)} and {@link Field#sub(Field)} Databases that have
 * been observed to natively support <code>INTERVAL</code> data types are:
 * <ul>
 * <li> {@link SQLDialect#HSQLDB}</li>
 * <li> {@link SQLDialect#INGRES}</li>
 * <li> {@link SQLDialect#ORACLE}</li>
 * <li> {@link SQLDialect#POSTGRES}</li>
 * </ul>
 * <p>
 * These dialects have been observed to partially support <code>INTERVAL</code>
 * data types in date time arithmetic functions, such as
 * <code>TIMESTAMPADD</code>, and <code>TIMESTAMPDIFF</code>:
 * <ul>
 * <li> {@link SQLDialect#CUBRID}</li>
 * <li> {@link SQLDialect#MARIADB}</li>
 * <li> {@link SQLDialect#MYSQL}</li>
 * </ul>
 *
 * @author Lukas Eder
 * @see Interval
 */
public final class DayToSecond extends Number implements Interval, Comparable<DayToSecond> {

    /**
     * Generated UID
     */
    private static final long    serialVersionUID = -3853596481984643811L;
    private static final Pattern PATTERN          = Pattern.compile("(\\+|-)?(?:(\\d+) )?(\\d+):(\\d+):(\\d+)(?:\\.(\\d+))?");

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

        // Perform normalisation. Specifically, Postgres may return intervals
        // such as 24:00:00, 25:13:15, etc...
        if (nano >= 1000000000) {
            seconds += (nano / 1000000000);
            nano %= 1000000000;
        }
        if (seconds >= 60) {
            minutes += (seconds / 60);
            seconds %= 60;
        }
        if (minutes >= 60) {
            hours += (minutes / 60);
            minutes %= 60;
        }
        if (hours >= 24) {
            days += (hours / 24);
            hours %= 24;
        }

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
            // Accept also doubles as the number of days
            try {
                return valueOf(Double.valueOf(string));
            }
            catch (NumberFormatException e) {
                Matcher matcher = PATTERN.matcher(string);

                if (matcher.find()) {
                    boolean negative = "-".equals(matcher.group(1));

                    int days = Convert.convert(matcher.group(2), int.class);
                    int hours = Convert.convert(matcher.group(3), int.class);
                    int minutes = Convert.convert(matcher.group(4), int.class);
                    int seconds = Convert.convert(matcher.group(5), int.class);
                    int nano = Convert.convert(StringUtils.rightPad(matcher.group(6), 9, "0"), int.class);

                    return new DayToSecond(days, hours, minutes, seconds, nano, negative);
                }
            }
        }

        return null;
    }

    /**
     * Load a {@link Double} representation of a <code>INTERVAL DAY TO SECOND</code>
     *
     * @param milli The number of milliseconds as a fractional number
     * @return The loaded <code>INTERVAL DAY TO SECOND</code> object
     */
    public static DayToSecond valueOf(double milli) {
        double abs = Math.abs(milli);

        int n = (int) ((abs % 1000) * 1000000.0); abs = Math.floor(abs / 1000);
        int s = (int) (abs % 60); abs = Math.floor(abs / 60);
        int m = (int) (abs % 60); abs = Math.floor(abs / 60);
        int h = (int) (abs % 24); abs = Math.floor(abs / 24);
        int d = (int) abs;

        DayToSecond result = new DayToSecond(d, h, m, s, n);

        if (milli < 0) {
            result = result.neg();
        }

        return result;
    }

    // -------------------------------------------------------------------------
    // XXX Number API
    // -------------------------------------------------------------------------

    @Override
    public final int intValue() {
        return (int) doubleValue();
    }

    @Override
    public final long longValue() {
        return (long) doubleValue();
    }

    @Override
    public final float floatValue() {
        return (float) doubleValue();
    }

    @Override
    public final double doubleValue() {
        return getTotalMilli();
    }

    // -------------------------------------------------------------------------
    // XXX Interval API
    // -------------------------------------------------------------------------

    @Override
    public final DayToSecond neg() {
        return new DayToSecond(days, hours, minutes, seconds, nano, !negative);
    }

    @Override
    public final DayToSecond abs() {
        return new DayToSecond(days, hours, minutes, seconds, nano, false);
    }

    /**
     * Get the day-part of this interval
     */
    public final int getDays() {
        return days;
    }

    /**
     * Get the hour-part of this interval
     */
    public final int getHours() {
        return hours;
    }

    /**
     * Get the minute-part of this interval
     */
    public final int getMinutes() {
        return minutes;
    }

    /**
     * Get the second-part of this interval
     */
    public final int getSeconds() {
        return seconds;
    }

    /**
     * Get the (truncated) milli-part of this interval
     */
    public final int getMilli() {
        return nano / 1000000;
    }

    /**
     * Get the (truncated) micro-part of this interval
     */
    public final int getMicro() {
        return nano / 1000;
    }

    /**
     * Get the nano-part of this interval
     */
    public final int getNano() {
        return nano;
    }

    /**
     * Get the whole interval in days
     */
    public final double getTotalDays() {
        return getSign() * (
            nano / (24.0 * 3600.0 * 1000000000.0) +
            seconds / (24.0 * 3600.0) +
            minutes / (24.0 * 60.0) +
            hours / 24.0 +
            days);
    }

    /**
     * Get the whole interval in hours
     */
    public final double getTotalHours() {
        return getSign() * (
            nano / (3600.0 * 1000000000.0) +
            seconds / 3600.0 +
            minutes / 60.0 +
            hours +
            24.0 * days);
    }

    /**
     * Get the whole interval in minutes
     */
    public final double getTotalMinutes() {
        return getSign() * (
            nano / (60.0 * 1000000000.0) +
            seconds / 60.0 +
            minutes +
            60.0 * hours +
            60.0 * 24.0 * days);
    }

    /**
     * Get the whole interval in seconds
     */
    public final double getTotalSeconds() {
        return getSign() * (
            nano / 1000000000.0 +
            seconds +
            60.0 * minutes +
            3600.0 * hours +
            3600.0 * 24.0 * days);
     }

    /**
     * Get the whole interval in milli-seconds
     */
    public final double getTotalMilli() {
        return getSign() * (
            nano / 1000000.0 +
            1000.0 * seconds +
            1000.0 * 60.0 * minutes +
            1000.0 * 3600.0 * hours +
            1000.0 * 3600.0 * 24.0 * days);
    }

    /**
     * Get the whole interval in micro-seconds
     */
    public final double getTotalMicro() {
        return getSign() * (
            nano / 1000.0 +
            1000000.0 * seconds +
            1000000.0 * 60.0 * minutes +
            1000000.0 * 3600.0 * hours +
            1000000.0 * 3600.0 * 24.0 * days);
    }

    /**
     * Get the whole interval in nano-seconds
     */
    public final double getTotalNano() {
        return getSign() * (
            nano +
            1000000000.0 * seconds +
            1000000000.0 * 60.0 * minutes +
            1000000000.0 * 3600.0 * hours +
            1000000000.0 * 3600.0 * 24.0 * days);
    }

    @Override
    public final int getSign() {
        return negative ? -1 : 1;
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
        sb.append(StringUtils.leftPad("" + nano, 9, "0"));

        return sb.toString();
    }
}
