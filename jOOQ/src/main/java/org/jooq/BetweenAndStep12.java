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
package org.jooq;

import org.jetbrains.annotations.NotNull;

/**
 * An intermediate DSL type for the construction of a <code>BETWEEN</code>
 * predicate.
 *
 * @author Lukas Eder
 */
public interface BetweenAndStep12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> {

    /**
     * Create a condition to check this field against some bounds
     */
    @NotNull
    @Support
    Condition and(Field<T1> maxValue1, Field<T2> maxValue2, Field<T3> maxValue3, Field<T4> maxValue4, Field<T5> maxValue5, Field<T6> maxValue6, Field<T7> maxValue7, Field<T8> maxValue8, Field<T9> maxValue9, Field<T10> maxValue10, Field<T11> maxValue11, Field<T12> maxValue12);

    /**
     * Create a condition to check this field against some bounds
     */
    @NotNull
    @Support
    Condition and(T1 maxValue1, T2 maxValue2, T3 maxValue3, T4 maxValue4, T5 maxValue5, T6 maxValue6, T7 maxValue7, T8 maxValue8, T9 maxValue9, T10 maxValue10, T11 maxValue11, T12 maxValue12);

    /**
     * Create a condition to check this field against some bounds
     */
    @NotNull
    @Support
    Condition and(Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> maxValue);

    /**
     * Create a condition to check this field against some bounds
     */
    @NotNull
    @Support
    Condition and(Record12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> maxValue);

}
