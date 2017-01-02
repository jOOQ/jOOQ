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

import javax.annotation.Generated;

/**
 * A model type for a records with degree <code>1</code>
 *
 * @see Row1
 * @author Lukas Eder
 */
@Generated("This class was generated using jOOQ-tools")
public interface Record1<T1> extends Record {

    // ------------------------------------------------------------------------
    // Row value expressions
    // ------------------------------------------------------------------------

    /**
     * Get this record's fields as a {@link Row1}.
     */
    @Override
    Row1<T1> fieldsRow();

    /**
     * Get this record's values as a {@link Row1}.
     */
    @Override
    Row1<T1> valuesRow();

    // ------------------------------------------------------------------------
    // Field accessors
    // ------------------------------------------------------------------------

    /**
     * Get the first field.
     */
    Field<T1> field1();

    // ------------------------------------------------------------------------
    // Value accessors
    // ------------------------------------------------------------------------

    /**
     * Get the first value.
     */
    T1 value1();

    /**
     * Set the first value.
     */
    Record1<T1> value1(T1 value);

    /**
     * Set all values.
     */
    Record1<T1> values(T1 t1);

//  [#4695] [#5501] The following covariant overrides cannot be published yet for implementation reasons (see #4695)
//
//  /**
//   * {@inheritDoc}
//   */
//  @Override
//  <T> Record1<T1> with(Field<T> field, T value);
//
//  /**
//   * {@inheritDoc}
//   */
//  @Override
//  <T, U> Record1<T1> with(Field<T> field, U value, Converter<? extends T, ? super U> converter);
}
