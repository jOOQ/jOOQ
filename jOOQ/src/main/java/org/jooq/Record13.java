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
 *
 *
 *
 */
package org.jooq;

import org.jetbrains.annotations.NotNull;

/**
 * A model type for a records with degree <code>13</code>
 *
 * @see Row13
 * @author Lukas Eder
 */
public interface Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> extends Record {

    // ------------------------------------------------------------------------
    // Row value expressions
    // ------------------------------------------------------------------------

    /**
     * Get this record's fields as a {@link Row13}.
     */
    @NotNull
    @Override
    Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> fieldsRow();

    /**
     * Get this record's values as a {@link Row13}.
     */
    @NotNull
    @Override
    Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> valuesRow();

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

    /**
     * Get the tenth field.
     */
    @NotNull
    Field<T10> field10();

    /**
     * Get the eleventh field.
     */
    @NotNull
    Field<T11> field11();

    /**
     * Get the twelfth field.
     */
    @NotNull
    Field<T12> field12();

    /**
     * Get the thirteenth field.
     */
    @NotNull
    Field<T13> field13();

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
     * Get the tenth value.
     */
    T10 value10();

    /**
     * Get the eleventh value.
     */
    T11 value11();

    /**
     * Get the twelfth value.
     */
    T12 value12();

    /**
     * Get the thirteenth value.
     */
    T13 value13();

    /**
     * Set the first value.
     */
    @NotNull
    Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> value1(T1 value);

    /**
     * Set the second value.
     */
    @NotNull
    Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> value2(T2 value);

    /**
     * Set the third value.
     */
    @NotNull
    Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> value3(T3 value);

    /**
     * Set the fourth value.
     */
    @NotNull
    Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> value4(T4 value);

    /**
     * Set the fifth value.
     */
    @NotNull
    Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> value5(T5 value);

    /**
     * Set the sixth value.
     */
    @NotNull
    Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> value6(T6 value);

    /**
     * Set the seventh value.
     */
    @NotNull
    Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> value7(T7 value);

    /**
     * Set the eighth value.
     */
    @NotNull
    Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> value8(T8 value);

    /**
     * Set the ninth value.
     */
    @NotNull
    Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> value9(T9 value);

    /**
     * Set the tenth value.
     */
    @NotNull
    Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> value10(T10 value);

    /**
     * Set the eleventh value.
     */
    @NotNull
    Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> value11(T11 value);

    /**
     * Set the twelfth value.
     */
    @NotNull
    Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> value12(T12 value);

    /**
     * Set the thirteenth value.
     */
    @NotNull
    Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> value13(T13 value);

    /**
     * Set all values.
     */
    @NotNull
    Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> values(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13);

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    <T> Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> with(Field<T> field, T value);

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    <T, U> Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> with(Field<T> field, U value, Converter<? extends T, ? super U> converter);

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

    /**
     * Get the tenth value.
     * <p>
     * This is the same as {@link #value10()}.
     */
    T10 component10();

    /**
     * Get the eleventh value.
     * <p>
     * This is the same as {@link #value11()}.
     */
    T11 component11();

    /**
     * Get the twelfth value.
     * <p>
     * This is the same as {@link #value12()}.
     */
    T12 component12();

    /**
     * Get the thirteenth value.
     * <p>
     * This is the same as {@link #value13()}.
     */
    T13 component13();
}
