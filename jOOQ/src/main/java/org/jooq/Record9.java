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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
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
package org.jooq;

import org.jetbrains.annotations.NotNull;

/**
 * A model type for a records with degree <code>9</code>
 *
 * @see Row9
 * @author Lukas Eder
 */
public interface Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9> extends Record {

    // ------------------------------------------------------------------------
    // Row value expressions
    // ------------------------------------------------------------------------

    /**
     * Get this record's fields as a {@link Row9}.
     */
    @NotNull
    @Override
    Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9> fieldsRow();

    /**
     * Get this record's values as a {@link Row9}.
     */
    @NotNull
    @Override
    Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9> valuesRow();

    // ------------------------------------------------------------------------
    // Field accessors
    // ------------------------------------------------------------------------

    /**
     * Get the first field.
     */
    @NotNull
    Field<T1> field1();

    /**
     * Get the second field.
     */
    @NotNull
    Field<T2> field2();

    /**
     * Get the third field.
     */
    @NotNull
    Field<T3> field3();

    /**
     * Get the fourth field.
     */
    @NotNull
    Field<T4> field4();

    /**
     * Get the fifth field.
     */
    @NotNull
    Field<T5> field5();

    /**
     * Get the sixth field.
     */
    @NotNull
    Field<T6> field6();

    /**
     * Get the seventh field.
     */
    @NotNull
    Field<T7> field7();

    /**
     * Get the eighth field.
     */
    @NotNull
    Field<T8> field8();

    /**
     * Get the ninth field.
     */
    @NotNull
    Field<T9> field9();

    // ------------------------------------------------------------------------
    // Value accessors
    // ------------------------------------------------------------------------

    /**
     * Get the first value.
     */
    T1 value1();

    /**
     * Get the second value.
     */
    T2 value2();

    /**
     * Get the third value.
     */
    T3 value3();

    /**
     * Get the fourth value.
     */
    T4 value4();

    /**
     * Get the fifth value.
     */
    T5 value5();

    /**
     * Get the sixth value.
     */
    T6 value6();

    /**
     * Get the seventh value.
     */
    T7 value7();

    /**
     * Get the eighth value.
     */
    T8 value8();

    /**
     * Get the ninth value.
     */
    T9 value9();

    /**
     * Set the first value.
     */
    @NotNull
    Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9> value1(T1 value);

    /**
     * Set the second value.
     */
    @NotNull
    Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9> value2(T2 value);

    /**
     * Set the third value.
     */
    @NotNull
    Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9> value3(T3 value);

    /**
     * Set the fourth value.
     */
    @NotNull
    Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9> value4(T4 value);

    /**
     * Set the fifth value.
     */
    @NotNull
    Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9> value5(T5 value);

    /**
     * Set the sixth value.
     */
    @NotNull
    Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9> value6(T6 value);

    /**
     * Set the seventh value.
     */
    @NotNull
    Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9> value7(T7 value);

    /**
     * Set the eighth value.
     */
    @NotNull
    Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9> value8(T8 value);

    /**
     * Set the ninth value.
     */
    @NotNull
    Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9> value9(T9 value);

    /**
     * Set all values.
     */
    @NotNull
    Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9> values(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9);

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    <T> Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9> with(Field<T> field, T value);

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    <T, U> Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9> with(Field<T> field, U value, Converter<? extends T, ? super U> converter);

    // ------------------------------------------------------------------------
    // Value accessors for record destructuring in Kotlin
    // ------------------------------------------------------------------------

    /**
     * Get the first value.
     * <p>
     * This is the same as {@link #value1()}.
     */
    T1 component1();

    /**
     * Get the second value.
     * <p>
     * This is the same as {@link #value2()}.
     */
    T2 component2();

    /**
     * Get the third value.
     * <p>
     * This is the same as {@link #value3()}.
     */
    T3 component3();

    /**
     * Get the fourth value.
     * <p>
     * This is the same as {@link #value4()}.
     */
    T4 component4();

    /**
     * Get the fifth value.
     * <p>
     * This is the same as {@link #value5()}.
     */
    T5 component5();

    /**
     * Get the sixth value.
     * <p>
     * This is the same as {@link #value6()}.
     */
    T6 component6();

    /**
     * Get the seventh value.
     * <p>
     * This is the same as {@link #value7()}.
     */
    T7 component7();

    /**
     * Get the eighth value.
     * <p>
     * This is the same as {@link #value8()}.
     */
    T8 component8();

    /**
     * Get the ninth value.
     * <p>
     * This is the same as {@link #value9()}.
     */
    T9 component9();
}
