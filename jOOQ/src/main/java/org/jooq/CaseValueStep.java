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
package org.jooq;

import java.util.Map;

/**
 * An intermediary step in creating a case statement of the type <code><pre>
 * CASE x WHEN 1 THEN 'one'
 *        WHEN 2 THEN 'two'
 *        ELSE        'three'
 * END
 * </pre></code>
 *
 * @param <V> The type of values being compared in this case statement
 * @author Lukas Eder
 * @see Case
 */
public interface CaseValueStep<V> {

    /**
     * Compare a value to the already constructed case statement, return result
     * if values are equal.
     *
     * @param <T> The generic result field type parameter
     * @param compareValue The value to compare with the already constructed
     *            case statement
     * @param result The result value if values are equal
     * @return An intermediary step for case statement construction
     */
    @Support
    <T> CaseWhenStep<V, T> when(V compareValue, T result);

    /**
     * Compare a value to the already constructed case statement, return result
     * if values are equal.
     *
     * @param <T> The generic result field type parameter
     * @param compareValue The value to compare with the already constructed
     *            case statement
     * @param result The result value if values are equal
     * @return An intermediary step for case statement construction
     */
    @Support
    <T> CaseWhenStep<V, T> when(V compareValue, Field<T> result);

    /**
     * Compare a value to the already constructed case statement, return result
     * if values are equal.
     *
     * @param <T> The generic result field type parameter
     * @param compareValue The value to compare with the already constructed
     *            case statement
     * @param result The result value if values are equal
     * @return An intermediary step for case statement construction
     */
    @Support
    <T> CaseWhenStep<V, T> when(V compareValue, Select<? extends Record1<T>> result);

    /**
     * Compare a value to the already constructed case statement, return result
     * if values are equal.
     *
     * @param <T> The generic result field type parameter
     * @param compareValue The value to compare with the already constructed
     *            case statement
     * @param result The result value if values are equal
     * @return An intermediary step for case statement construction
     */
    @Support
    <T> CaseWhenStep<V, T> when(Field<V> compareValue, T result);

    /**
     * Compare a value to the already constructed case statement, return result
     * if values are equal.
     *
     * @param <T> The generic result field type parameter
     * @param compareValue The value to compare with the already constructed
     *            case statement
     * @param result The result value if values are equal
     * @return An intermediary step for case statement construction
     */
    @Support
    <T> CaseWhenStep<V, T> when(Field<V> compareValue, Field<T> result);

    /**
     * Compare a value to the already constructed case statement, return result
     * if values are equal.
     *
     * @param <T> The generic result field type parameter
     * @param compareValue The value to compare with the already constructed
     *            case statement
     * @param result The result value if values are equal
     * @return An intermediary step for case statement construction
     */
    @Support
    <T> CaseWhenStep<V, T> when(Field<V> compareValue, Select<? extends Record1<T>> result);

    /**
     * Create <code>WHEN .. THEN</code> expressions from a {@link Map}.
     * <p>
     * This will iterate over the map's entries to create individual
     * <code>WHEN .. THEN</code> expressions for each map entry.
     */
    <T> CaseWhenStep<V, T> mapValues(Map<V, T> values);

    /**
     * Create <code>WHEN .. THEN</code> expressions from a {@link Map}.
     * <p>
     * This will iterate over the map's entries to create individual
     * <code>WHEN .. THEN</code> expressions for each map entry.
     */
    <T> CaseWhenStep<V, T> mapFields(Map<? extends Field<V>, ? extends Field<T>> fields);

}
