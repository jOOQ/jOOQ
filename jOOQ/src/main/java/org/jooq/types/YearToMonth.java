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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
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
package org.jooq.types;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jooq.Field;
import org.jooq.SQLDialect;

/**
 * An implementation for the SQL standard <code>INTERVAL YEAR TO MONTH</code>
 * data type.
 * <p>
 * <code>YearToMonth</code> is a {@link Number} whose {@link Number#intValue()}
 * represents the number of months of the interval.
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
 * <li> {@link SQLDialect#MARIADB}</li>
 * <li> {@link SQLDialect#MYSQL}</li>
 * </ul>
 *
 * @author Lukas Eder
 * @see Interval
 */
public final class YearToMonth extends Number implements Interval, Comparable<YearToMonth> {

    private static final Pattern PATTERN_SQL = Pattern.compile("^([+-])?(\\d+)-(\\d+)$");
    private static final Pattern PATTERN_ISO = Pattern.compile("^([+-])?P(?:([+-]?\\d+)Y)?(?:([+-]?\\d+)M)?(?:0D)?$", Pattern.CASE_INSENSITIVE);

    private final boolean        negative;
    private final int            years;
    private final int            months;

    /**
     * Create a new year-month interval.
     */
    public YearToMonth() {
        this(0, 0, false);
    }

    /**
     * Create a new year-month interval.
     */
    public YearToMonth(int years) {
        this(years, 0, false);
    }

    /**
     * Create a new year-month interval.
     */
    public YearToMonth(int years, int months) {
        this(years, months, false);
    }

    YearToMonth(int years, int months, boolean negative) {

        // Perform normalisation. Specifically, Postgres may return intervals
        // such as 0-13
        if (Math.abs(months) >= 12) {
            years += (months / 12);
            months %= 12;
        }

        this.negative = negative;
        this.years = years;
        this.months = months;
    }

    /**
     * Parse a string representation of a <code>INTERVAL YEAR TO MONTH</code>.
     *
     * @param string A string representation of the form
     *            <code>[+|-][years]-[months]</code>
     * @return The parsed <code>YEAR TO MONTH</code> object, or
     *         <code>null</code> if the string could not be parsed.
     */
    public static YearToMonth valueOf(String string) {
        if (string != null) {
            Matcher matcher;

            if ((matcher = PATTERN_SQL.matcher(string)).find())
                return YearToSecond.parseYM(matcher, 0);

            if ((matcher = PATTERN_ISO.matcher(string)).find()) {
                boolean negative = "-".equals(matcher.group(1));

                String group2 = matcher.group(2);
                String group3 = matcher.group(3);

                int years = group2 == null ? 0 : Integer.parseInt(group2);
                int months = group3 == null ? 0 : Integer.parseInt(group3);

                return new YearToMonth(years, months, negative);
            }

            return yearToMonth(string);
        }

        return null;
    }

    /**
     * Parse a standard SQL string representation of a
     * <code>INTERVAL YEAR</code>.
     *
     * @param string A string representation of the form
     *            <code>[+|-][years]</code>
     * @return The parsed <code>YEAR</code> object, or <code>null</code> if the
     *         string could not be parsed.
     */
    public static YearToMonth year(String string) {
        try {
            return string == null ? null : new YearToMonth(Integer.parseInt(string));
        }
        catch (NumberFormatException ignore) {
            return null;
        }
    }

    /**
     * Parse a standard SQL string representation of a
     * <code>INTERVAL YEAR TO MONTH</code>.
     *
     * @param string A string representation of the form
     *            <code>[+|-][years]-[months]</code>
     * @return The parsed <code>YEAR TO MONTH</code> object, or
     *         <code>null</code> if the string could not be parsed.
     */
    public static YearToMonth yearToMonth(String string) {
        if (string != null) {
            Matcher matcher;

            if ((matcher = PATTERN_SQL.matcher(string)).find())
                return YearToSecond.parseYM(matcher, 0);
        }

        return null;
    }

    /**
     * Parse a standard SQL string representation of a
     * <code>INTERVAL MONTH</code>.
     *
     * @param string A string representation of the form
     *            <code>[+|-][months]</code>
     * @return The parsed <code>MONTH</code> object, or <code>null</code> if the
     *         string could not be parsed.
     */
    public static YearToMonth month(String string) {
        try {
            return string == null ? null : new YearToMonth(0, Integer.parseInt(string));
        }
        catch (NumberFormatException ignore) {
            return null;
        }
    }

    @Override
    public final Duration toDuration() {
        long hours = years * 8766L  // 365.25 * 24
                   + months * 720L; // 30 * 24

        if (negative)
            hours = -hours;

        return Duration.ofHours(hours);
    }

    // -------------------------------------------------------------------------
    // XXX Interval API
    // -------------------------------------------------------------------------

    @Override
    public final YearToMonth neg() {
        return new YearToMonth(years, months, !negative);
    }

    @Override
    public final YearToMonth abs() {
        return new YearToMonth(years, months, false);
    }

    public final int getYears() {
        return years;
    }

    public final int getMonths() {
        return months;
    }

    @Override
    public final int getSign() {
        return negative ? -1 : 1;
    }

    // -------------------------------------------------------------------------
    // XXX Number API
    // -------------------------------------------------------------------------

    @Override
    public final int intValue() {
        return (negative ? -1 : 1) * (12 * years + months);
    }

    @Override
    public final long longValue() {
        return intValue();
    }

    @Override
    public final float floatValue() {
        return intValue();
    }

    @Override
    public final double doubleValue() {
        return intValue();
    }

    // -------------------------------------------------------------------------
    // XXX Comparable and Object API
    // -------------------------------------------------------------------------

    @Override
    public final int compareTo(YearToMonth that) {
        if (years < that.years) {
            return -1;
        }
        if (years > that.years) {
            return 1;
        }

        if (months < that.months) {
            return -1;
        }
        if (months > that.months) {
            return 1;
        }

        return 0;
    }

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 0;
        if (months != 0)
            result = prime * result + months;
        if (years != 0)
            result = prime * result + years;
        return result;
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() == obj.getClass()) {
            YearToMonth other = (YearToMonth) obj;
            if (months != other.months)
                return false;
            if (years != other.years)
                return false;
            if (negative != other.negative && intValue() != 0)
                return false;
            return true;
        }
        else if (obj instanceof YearToSecond)
            return obj.equals(this);
        else
            return false;
    }

    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(negative ? "-" : "+");
        sb.append(years);
        sb.append("-");
        sb.append(months);

        return sb.toString();
    }
}
