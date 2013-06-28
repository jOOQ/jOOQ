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
package org.jooq.types;

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
 * <li> {@link SQLDialect#CUBRID}</li>
 * <li> {@link SQLDialect#MARIADB}</li>
 * <li> {@link SQLDialect#MYSQL}</li>
 * </ul>
 *
 * @author Lukas Eder
 * @see Interval
 */
public final class YearToMonth extends Number implements Interval, Comparable<YearToMonth> {

    /**
     * Generated UID
     */
    private static final long    serialVersionUID = 1308553645456594273L;
    private static final Pattern PATTERN          = Pattern.compile("(\\+|-)?(\\d+)-(\\d+)");

    private final boolean        negative;
    private final int            years;
    private final int            months;

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

    private YearToMonth(int years, int months, boolean negative) {

        // Perform normalisation. Specifically, Postgres may return intervals
        // such as 0-13
        if (months >= 12) {
            years += (months / 12);
            months %= 12;
        }

        this.negative = negative;
        this.years = years;
        this.months = months;
    }

    /**
     * Parse a string representation of a <code>INTERVAL YEAR TO MONTH</code>
     *
     * @param string A string representation of the form
     *            <code>[+|-][years]-[months]</code>
     * @return The parsed <code>YEAR TO MONTH</code> object, or
     *         <code>null</code> if the string could not be parsed.
     */
    public static YearToMonth valueOf(String string) {
        if (string != null) {
            Matcher matcher = PATTERN.matcher(string);

            if (matcher.find()) {
                boolean negative = "-".equals(matcher.group(1));
                int years = Integer.parseInt(matcher.group(2));
                int months = Integer.parseInt(matcher.group(3));

                return new YearToMonth(years, months, negative);
            }
        }

        return null;
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
        int result = 1;
        result = prime * result + months;
        result = prime * result + years;
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
        YearToMonth other = (YearToMonth) obj;
        if (months != other.months)
            return false;
        if (years != other.years)
            return false;
        return true;
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
