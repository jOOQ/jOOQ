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
package org.jooq;

import org.jooq.impl.DSL;


/**
 * The SQL case statement.
 * <p>
 * This construct can be used to create expressions of the type <code><pre>
 * CASE x WHEN 1 THEN 'one'
 *        WHEN 2 THEN 'two'
 *        ELSE        'three'
 * END
 * </pre></code> or of the type <code><pre>
 * CASE WHEN x &lt; 1  THEN 'one'
 *      WHEN x &gt;= 2 THEN 'two'
 *      ELSE            'three'
 * END
 * </pre></code> Instances of Case are created through the
 * {@link DSL#decode()} method
 *
 * @author Lukas Eder
 */
public interface Case {

    /**
     * This construct can be used to create expressions of the type <code><pre>
     * CASE value WHEN 1 THEN 'one'
     *            WHEN 2 THEN 'two'
     *            ELSE        'three'
     * END
     * </pre></code>
     *
     * @param <V> The generic value type parameter
     * @param value The value to do the case statement on
     * @return An intermediary step for case statement construction
     */
    @Support
    <V> CaseValueStep<V> value(V value);

    /**
     * This construct can be used to create expressions of the type <code><pre>
     * CASE value WHEN 1 THEN 'one'
     *            WHEN 2 THEN 'two'
     *            ELSE        'three'
     * END
     * </pre></code>
     *
     * @param <V> The generic value type parameter
     * @param value The value to do the case statement on
     * @return An intermediary step for case statement construction
     */
    @Support
    <V> CaseValueStep<V> value(Field<V> value);

    /**
     * This construct can be used to create expressions of the type <code><pre>
     * CASE WHEN x &lt; 1  THEN 'one'
     *      WHEN x &gt;= 2 THEN 'two'
     *      ELSE            'three'
     * END
     * </pre></code> Instances of Case are created through the
     *
     * @param <T> The generic field type parameter
     * @param condition A condition to check in the case statement
     * @param result The result if the condition holds true
     * @return An intermediary step for case statement construction
     */
    @Support
    <T> CaseConditionStep<T> when(Condition condition, T result);

    /**
     * This construct can be used to create expressions of the type <code><pre>
     * CASE WHEN x &lt; 1  THEN 'one'
     *      WHEN x &gt;= 2 THEN 'two'
     *      ELSE            'three'
     * END
     * </pre></code> Instances of Case are created through the
     *
     * @param <T> The generic field type parameter
     * @param condition A condition to check in the case statement
     * @param result The result if the condition holds true
     * @return An intermediary step for case statement construction
     */
    @Support
    <T> CaseConditionStep<T> when(Condition condition, Field<T> result);
}
