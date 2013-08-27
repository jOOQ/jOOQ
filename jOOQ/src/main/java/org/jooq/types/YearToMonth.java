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
