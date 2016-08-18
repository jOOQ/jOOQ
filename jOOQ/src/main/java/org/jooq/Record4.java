/**
 * Copyright (c) 2009-2016, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
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

import javax.annotation.Generated;

/**
 * A model type for a records with degree <code>4</code>
 *
 * @see Row4
 * @author Lukas Eder
 */
@Generated("This class was generated using jOOQ-tools")
public interface Record4<T1, T2, T3, T4> extends Record {

    // ------------------------------------------------------------------------
    // Row value expressions
    // ------------------------------------------------------------------------

    /**
     * Get this record's fields as a {@link Row4}.
     */
    @Override
    Row4<T1, T2, T3, T4> fieldsRow();

    /**
     * Get this record's values as a {@link Row4}.
     */
    @Override
    Row4<T1, T2, T3, T4> valuesRow();

    // ------------------------------------------------------------------------
    // Field accessors
    // ------------------------------------------------------------------------

    /**
     * Get the first field.
     */
    Field<T1> field1();

    /**
     * Get the second field.
     */
    Field<T2> field2();

    /**
     * Get the third field.
     */
    Field<T3> field3();

    /**
     * Get the fourth field.
     */
    Field<T4> field4();

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
     * Set the first value.
     */
    Record4<T1, T2, T3, T4> value1(T1 value);

    /**
     * Set the second value.
     */
    Record4<T1, T2, T3, T4> value2(T2 value);

    /**
     * Set the third value.
     */
    Record4<T1, T2, T3, T4> value3(T3 value);

    /**
     * Set the fourth value.
     */
    Record4<T1, T2, T3, T4> value4(T4 value);

    /**
     * Set all values.
     */
    Record4<T1, T2, T3, T4> values(T1 t1, T2 t2, T3 t3, T4 t4);

//  [#4695] [#5501] The following covariant overrides cannot be published yet for implementation reasons (see #4695)
//
//  /**
//   * {@inheritDoc}
//   */
//  @Override
//  <T> Record4<T1, T2, T3, T4> with(Field<T> field, T value);
//
//  /**
//   * {@inheritDoc}
//   */
//  @Override
//  <T, U> Record4<T1, T2, T3, T4> with(Field<T> field, U value, Converter<? extends T, ? super U> converter);
}
