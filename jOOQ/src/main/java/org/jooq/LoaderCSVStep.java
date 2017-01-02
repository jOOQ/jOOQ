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

import java.util.Collection;

import org.jooq.LoaderFieldMapper.LoaderFieldContext;

/**
 * The <code>Loader</code> API is used for configuring data loads.
 * <p>
 * The step in constructing the {@link Loader} object where you can set the
 * mandatory CSV loader options.
 *
 * @author Lukas Eder
 */
public interface LoaderCSVStep<R extends Record> {

    /**
     * Specify the the fields to be loaded into the table in the correct order.
     * <p>
     * The CSV column at index <code>i</code> is inserted into the table field
     * at index <code>i</code>. If <code>fields[i] == null</code> or
     * <code>fields.length &lt;= i</code>, then the CSV column is skipped.
     */
    @Support
    LoaderCSVOptionsStep<R> fields(Field<?>... fields);

    /**
     * Specify the the fields to be loaded into the table in the correct order.
     * <p>
     * The CSV column at index <code>i</code> is inserted into the table field
     * at index <code>i</code>. If
     * <code>new ArrayList(fields).get(i) == null</code> or
     * <code>new ArrayList(fields).size() &lt;= i</code>, then the CSV column is
     * skipped.
     */
    @Support
    LoaderCSVOptionsStep<R> fields(Collection<? extends Field<?>> fields);

    /**
     * Specify a function to apply on each input field to receive the target
     * table's field.
     * <p>
     * The input field obtained from {@link LoaderFieldContext#field()} wraps
     * the CSV header column name if any, or an unspecified field enumeration is
     * used. The {@link LoaderFieldContext#index()} property corresponds to the
     * CSV column index.
     */
    @Support
    LoaderListenerStep<R> fields(LoaderFieldMapper mapper);
}
