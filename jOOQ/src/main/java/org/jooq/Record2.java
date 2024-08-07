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
 * A model type for a records with degree <code>2</code>
 *
 * @see Row2
 * @author Lukas Eder
 */
public interface Record2<T1, T2> extends Record {

    // ------------------------------------------------------------------------
    // Row value expressions
    // ------------------------------------------------------------------------

    /**
     * Get this record's fields as a {@link Row2}.
     */
    @NotNull
    @Override
    Row2<T1, T2> fieldsRow();

    /**
     * Get this record's values as a {@link Row2}.
     */
    @NotNull
    @Override
    Row2<T1, T2> valuesRow();

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
     * Set the first value.
     */
    @NotNull
    Record2<T1, T2> value1(T1 value);

    /**
     * Set the second value.
     */
    @NotNull
    Record2<T1, T2> value2(T2 value);

    /**
     * Set all values.
     */
    @NotNull
    Record2<T1, T2> values(T1 t1, T2 t2);

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    <T> Record2<T1, T2> with(Field<T> field, T value);

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    <T, U> Record2<T1, T2> with(Field<T> field, U value, Converter<? extends T, ? super U> converter);

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
}
