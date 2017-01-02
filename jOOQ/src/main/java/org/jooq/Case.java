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
     * </pre></code>
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
     * </pre></code>
     *
     * @param <T> The generic field type parameter
     * @param condition A condition to check in the case statement
     * @param result The result if the condition holds true
     * @return An intermediary step for case statement construction
     */
    @Support
    <T> CaseConditionStep<T> when(Condition condition, Field<T> result);

    /**
     * This construct can be used to create expressions of the type <code><pre>
     * CASE WHEN x &lt; 1  THEN 'one'
     *      WHEN x &gt;= 2 THEN 'two'
     *      ELSE            'three'
     * END
     * </pre></code>
     *
     * @param <T> The generic field type parameter
     * @param condition A condition to check in the case statement
     * @param result The result if the condition holds true
     * @return An intermediary step for case statement construction
     */
    @Support
    <T> CaseConditionStep<T> when(Condition condition, Select<? extends Record1<T>> result);
}
