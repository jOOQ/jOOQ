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
