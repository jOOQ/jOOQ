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
 *
 *
 *
 */
package org.jooq.types;

import java.time.Duration;
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jooq.tools.Convert;
import org.jooq.tools.StringUtils;

/**
 * A combined {@link YearToMonth} / {@link DayToSecond} interval.
 * <p>
 * Some databases (e.g. PostgreSQL) allow for mixing <code>YEAR TO MONTH</code>
 * and <code>DAY TO SECOND</code> intervals, despite the many questions such a
 * combination raises. The <code>YearToSecond</code> type intends to model such
 * vendor specific intervals.
 * <p>
 * The numeric value of this interval corresponds to its "context free" number
 * of milliseconds. While the {@link DayToSecond} interval component can provide
 * such a value easily (being independent of time zones, daylight saving times,
 * leap years, or leap seconds), the {@link YearToMonth} component cannot. The
 * implemented rules are those of PostgreSQL:
 * <p>
 * <ul>
 * <li>A day has 86400 seconds</li>
 * <li>A month has 30 days</li>
 * <li>A year has 365.25 days</li>
 * <li>A year has 12 months</li>
 * </ul>
 * <p>
 * Examples:
 * <p>
 * <ul>
 * <li><code>P11M</code> has 330 days</li>
 * <li><code>P1Y-1M</code> has 330 days</li>
 * <li><code>P1Y</code> has 365.25 days</li>
 * <li><code>P1Y1M</code> has 396.25 days</li>
 * </ul>
 *
 * @author Lukas Eder
 */
public final class YearToSecond extends Number implements Interval, Comparable<YearToSecond> {

    /**
     * Generated UID
     */
    private static final long    serialVersionUID = 6051372712676019640L;
    private static final Pattern PATTERN          = Pattern.compile("^([+-])?(\\d+)-(\\d+) ([+-])?(?:(\\d+) )?(\\d+):(\\d+):(\\d+)(?:\\.(\\d+))?$");

    private final YearToMonth yearToMonth;
    private final DayToSecond dayToSecond;

    public YearToSecond(YearToMonth yearToMonth, DayToSecond dayToSecond) {
        this.yearToMonth = yearToMonth == null ? new YearToMonth(0) : yearToMonth;
        this.dayToSecond = dayToSecond == null ? new DayToSecond(0) : dayToSecond;
    }

    /**
     * Load a {@link Double} representation of a
     * <code>INTERVAL YEAR TO SECOND</code> by assuming standard 24 hour days and
     * 60 second minutes.
     *
     * @param milli The number of milliseconds as a fractional number
     * @return The loaded <code>INTERVAL DAY TO SECOND</code> object
     */
    public static YearToSecond valueOf(double milli) {
        double abs = Math.abs(milli);

        int y = (int) (abs / (365.25 * 86400000.0)); abs = abs % (365.25 * 86400000.0);
        int m = (int) (abs / (30.0 * 86400000.0)); abs = abs % (30.0 * 86400000.0);

        YearToSecond result = new YearToSecond(new YearToMonth(y, m), DayToSecond.valueOf(abs));

        if (milli < 0)
            result = result.neg();

        return result;
    }


    /**
     * Transform a {@link Duration} into a {@link YearToSecond} interval by
     * taking its number of milliseconds.
     */
    public static YearToSecond valueOf(Duration duration) {
        return duration == null ? null : valueOf(duration.toMillis());
    }


    @Override
    public final Duration toDuration() {
        return yearToMonth.toDuration().plus(dayToSecond.toDuration());
    }


    /**
     * Transform a {@link Period} into a {@link YearToSecond} interval.
     */
    public static YearToSecond valueOf(Period period) {
        return period == null ? null : new YearToSecond(
            new YearToMonth(period.getYears(), period.getMonths()),
            new DayToSecond(period.getDays())
        );
    }


    /**
     * Parse a string representation of a <code>INTERVAL YEAR TO SECOND</code>
     *
     * @param string A string representation of the form
     *            <code>[+|-][years]-[months] [+|-][days] [hours]:[minutes]:[seconds].[fractional seconds]</code>
     * @return The parsed <code>YEAR TO SECOND</code> object, or
     *         <code>null</code> if the string could not be parsed.
     */
    public static YearToSecond valueOf(String string) {
        if (string != null) {

            // Accept also doubles as the number of milliseconds
            try {
                return valueOf(Double.valueOf(string));
            }
            catch (NumberFormatException e) {
                Matcher matcher = PATTERN.matcher(string);

                if (matcher.find()) {
                    return new YearToSecond(parseYM(matcher, 0), parseDS(matcher, 3));
                }


                else {
                    try {
                        return YearToSecond.valueOf(Duration.parse(string));
                    }
                    catch (DateTimeParseException ignore) {}
                }

            }
        }

        return null;
    }

    static YearToMonth parseYM(Matcher matcher, int groupOffset) {
        boolean negativeYM = "-".equals(matcher.group(groupOffset + 1));
        int years = Integer.parseInt(matcher.group(groupOffset + 2));
        int months = Integer.parseInt(matcher.group(groupOffset + 3));

        return new YearToMonth(years, months, negativeYM);
    }

    static DayToSecond parseDS(Matcher matcher, int groupOffset) {
        boolean negativeDS = "-".equals(matcher.group(groupOffset + 1));

        int days = Convert.convert(matcher.group(groupOffset + 2), int.class);
        int hours = Convert.convert(matcher.group(groupOffset + 3), int.class);
        int minutes = Convert.convert(matcher.group(groupOffset + 4), int.class);
        int seconds = Convert.convert(matcher.group(groupOffset + 5), int.class);
        int nano = Convert.convert(StringUtils.rightPad(matcher.group(groupOffset + 6), 9, "0"), int.class);

        return new DayToSecond(days, hours, minutes, seconds, nano, negativeDS);
    }

    // -------------------------------------------------------------------------
    // XXX Interval API
    // -------------------------------------------------------------------------

    @Override
    public final YearToSecond neg() {
        return new YearToSecond(yearToMonth.neg(), dayToSecond.neg());
    }

    @Override
    public final YearToSecond abs() {
        return new YearToSecond(yearToMonth.abs(), dayToSecond.abs());
    }

    public final YearToMonth getYearToMonth() {
        return yearToMonth;
    }

    public final DayToSecond getDayToSecond() {
        return dayToSecond;
    }

    public final int getYears() {
        return yearToMonth.getYears();
    }

    public final int getMonths() {
        return yearToMonth.getMonths();
    }

    /**
     * Get the day-part of this interval
     */
    public final int getDays() {
        return dayToSecond.getDays();
    }

    /**
     * Get the hour-part of this interval
     */
    public final int getHours() {
        return dayToSecond.getHours();
    }

    /**
     * Get the minute-part of this interval
     */
    public final int getMinutes() {
        return dayToSecond.getMinutes();
    }

    /**
     * Get the second-part of this interval
     */
    public final int getSeconds() {
        return dayToSecond.getSeconds();
    }

    /**
     * Get the (truncated) milli-part of this interval
     */
    public final int getMilli() {
        return dayToSecond.getMilli();
    }

    /**
     * Get the (truncated) micro-part of this interval
     */
    public final int getMicro() {
        return dayToSecond.getMicro();
    }

    /**
     * Get the nano-part of this interval
     */
    public final int getNano() {
        return dayToSecond.getNano();
    }

    @Override
    public final int getSign() {
        double value = doubleValue();

        return value > 0
             ? 1
             : value < 0
             ? -1
             : 0;
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
        return (yearToMonth.getYears() * 365.25
              + yearToMonth.getMonths() * 30) * 86400000 * yearToMonth.getSign()
              + dayToSecond.doubleValue();
    }

    // -------------------------------------------------------------------------
    // XXX Comparable and Object API
    // -------------------------------------------------------------------------

    @Override
    public final int compareTo(YearToSecond that) {
        double i1 = doubleValue();
        double i2 = that.doubleValue();

        return i1 > i2
             ? 1
             : i1 < i2
             ? -1
             : 0;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dayToSecond == null) ? 0 : dayToSecond.hashCode());
        result = prime * result + ((yearToMonth == null) ? 0 : yearToMonth.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        YearToSecond other = (YearToSecond) obj;
        if (dayToSecond == null) {
            if (other.dayToSecond != null)
                return false;
        }
        else if (!dayToSecond.equals(other.dayToSecond))
            return false;
        if (yearToMonth == null) {
            if (other.yearToMonth != null)
                return false;
        }
        else if (!yearToMonth.equals(other.yearToMonth))
            return false;
        return true;
    }

    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(yearToMonth);
        sb.append(' ');
        sb.append(dayToSecond);

        return sb.toString();
    }
}
