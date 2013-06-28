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

import java.io.Serializable;

import org.jooq.Field;
import org.jooq.SQLDialect;

/**
 * A substitute for JDBC's missing <code>java.sql.Interval</code> data type.
 * <p>
 * JDBC lacks an important data type that is present in most SQL databases:
 * <code>INTERVAL</code>. The SQL:2008 standard states that there are two types
 * of intervals: <blockquote> 4.6.3 Intervals
 * <p>
 * There are two classes of intervals. One class, called year-month intervals,
 * has an express or implied datetime precision that includes no fields other
 * than YEAR and MONTH, though not both are required. The other class, called
 * day-time intervals, has an express or implied interval precision that can
 * include any fields other than YEAR or MONTH. </blockquote>
 * <p>
 * <code>INTERVAL</code> can be combined with date time data types according to
 * the following operation table:
 * <table border="1">
 * <tr>
 * <th>Operand 1</th>
 * <th>Operator</th>
 * <th>Operand 2</th>
 * <th>Result Type</th>
 * </tr>
 * <tr>
 * <td>Datetime</td>
 * <td>–</td>
 * <td>Datetime</td>
 * <td>Interval</td>
 * </tr>
 * <tr>
 * <td>Datetime</td>
 * <td>+ or –</td>
 * <td>Interval</td>
 * <td>Datetime</td>
 * </tr>
 * <tr>
 * <td>Interval</td>
 * <td>+</td>
 * <td>Datetime</td>
 * <td>Datetime</td>
 * </tr>
 * <tr>
 * <td>Interval</td>
 * <td>+ or –</td>
 * <td>Interval</td>
 * <td>Interval</td>
 * </tr>
 * <tr>
 * <td>Interval</td>
 * <td>* or /</td>
 * <td>Numeric</td>
 * <td>Interval</td>
 * </tr>
 * <tr>
 * <td>Numeric</td>
 * <td>*</td>
 * <td>Interval</td>
 * <td>Interval</td>
 * </tr>
 * </table>
 * <p>
 * Interval implementations can be expected to also also extend {@link Number}.
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
 */
public interface Interval extends Serializable {

    /**
     * Negate the interval (change its sign)
     */
    Interval neg();

    /**
     * Get the absolute value of the interval (set its sign to positive)
     */
    Interval abs();

    /**
     * The sign of the interval
     *
     * @return <code>1</code> for positive or zero, <code>-1</code> for negative
     */
    int getSign();

    /**
     * @see Number#doubleValue()
     */
    double doubleValue();

    /**
     * @see Number#floatValue()
     */
    float floatValue();

    /**
     * @see Number#longValue()
     */
    long longValue();

    /**
     * @see Number#intValue()
     */
    int intValue();

    /**
     * @see Number#byteValue()
     */
    byte byteValue();

    /**
     * @see Number#shortValue()
     */
    short shortValue();
}
