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

import org.jetbrains.annotations.*;


import java.util.Collection;

import org.jooq.LoaderFieldMapper.LoaderFieldContext;
import org.jooq.exception.LoaderConfigurationException;

/**
 * The <code>Loader</code> API is used for configuring data loads.
 * <p>
 * The step in constructing the {@link org.jooq.Loader} object where you can set
 * the mandatory JSON loader options.
 *
 * @author Lukas Eder
 * @author Johannes Bühler
 */
public interface LoaderJSONStep<R extends Record> {

    /**
     * Specify the fields to be loaded into the table in the correct order.
     * <p>
     * The JSON column at index <code>i</code> is inserted into the table field
     * at index <code>i</code>. If <code>fields[i] == null</code> or
     * <code>fields.length &lt;= i</code>, then the JSON
     * column is skipped.
     */
    @NotNull @CheckReturnValue
    @Support
    LoaderJSONOptionsStep<R> fields(Field<?>... fields);

    /**
     * Specify the fields to be loaded into the table in the correct order.
     * <p>
     * The JSON column at index <code>i</code> is inserted into the table field
     * at index <code>i</code>. If
     * <code>new ArrayList(fields).get(i) == null</code> or
     * <code>new ArrayList(fields).size() &lt;= i</code>, then the JSON column
     * is skipped.
     */
    @NotNull @CheckReturnValue
    @Support
    LoaderJSONOptionsStep<R> fields(Collection<? extends Field<?>> fields);

    /**
     * Specify a function to apply on each input field to receive the target
     * table's field.
     * <p>
     * The input field obtained from {@link LoaderFieldContext#field()} wraps
     * the JSON column name if any, or an unspecified field enumeration is used.
     * The {@link LoaderFieldContext#index()} property corresponds to the JSON
     * column index in enumeration order.
     */
    @NotNull @CheckReturnValue
    @Support
    LoaderJSONOptionsStep<R> fields(LoaderFieldMapper mapper);

    /**
     * Indicate that all input fields which have a corresponding field in the
     * target table (with the same name) should be loaded.
     *
     * @throws LoaderConfigurationException When the source data does not expose
     *             field names.
     * @deprecated - 3.14.0 - [#10010] - Use {@link #fieldsCorresponding()}
     *             instead.
     */
    @Deprecated(forRemoval = true, since = "3.14")
    @NotNull @CheckReturnValue
    @Support
    LoaderCSVOptionsStep<R> fieldsFromSource();

    /**
     * Indicate that all input fields which have a corresponding field in the
     * target table (with the same name) should be loaded.
     *
     * @throws LoaderConfigurationException When the source data does not expose
     *             field names.
     */
    @NotNull @CheckReturnValue
    @Support
    LoaderCSVOptionsStep<R> fieldsCorresponding();

}
