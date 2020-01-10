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

import java.util.Collection;

import org.jooq.LoaderFieldMapper.LoaderFieldContext;
import org.jooq.exception.LoaderConfigurationException;

/**
 * The <code>Loader</code> API is used for configuring data loads.
 * <p>
 * The step in constructing the {@link Loader} object where you can set the
 * mandatory row loader options.
 * <p>
 * <h3>Referencing <code>XYZ*Step</code> types directly from client code</h3>
 * <p>
 * It is usually not recommended to reference any <code>XYZ*Step</code> types
 * directly from client code, or assign them to local variables. When writing
 * dynamic SQL, creating a statement's components dynamically, and passing them
 * to the DSL API statically is usually a better choice. See the manual's
 * section about dynamic SQL for details: <a href=
 * "https://www.jooq.org/doc/latest/manual/sql-building/dynamic-sql">https://www.jooq.org/doc/latest/manual/sql-building/dynamic-sql</a>.
 * <p>
 * Drawbacks of referencing the <code>XYZ*Step</code> types directly:
 * <ul>
 * <li>They're operating on mutable implementations (as of jOOQ 3.x)</li>
 * <li>They're less composable and not easy to get right when dynamic SQL gets
 * complex</li>
 * <li>They're less readable</li>
 * <li>They might have binary incompatible changes between minor releases</li>
 * </ul>
 *
 * @author Lukas Eder
 */
public interface LoaderRowsStep<R extends Record> {

    /**
     * Specify the the fields to be loaded into the table in the correct order.
     * <p>
     * The record column at index <code>i</code> is inserted into the table
     * field at index <code>i</code>. If <code>fields[i] == null</code> or
     * <code>fields.length &lt;= i</code>, then the record column is skipped.
     */
    @Support
    LoaderListenerStep<R> fields(Field<?>... fields);

    /**
     * Specify the the fields to be loaded into the table in the correct order.
     * <p>
     * The record column at index <code>i</code> is inserted into the table
     * field at index <code>i</code>. If
     * <code>new ArrayList(fields).get(i) == null</code> or
     * <code>new ArrayList(fields).size() &lt;= i</code>, then the record column
     * is skipped.
     */
    @Support
    LoaderListenerStep<R> fields(Collection<? extends Field<?>> fields);

    /**
     * Specify a function to apply on each input field to receive the target
     * table's field.
     * <p>
     * The input field obtained from {@link LoaderFieldContext#field()}
     * corresponds to the source record field, if any, or an unspecified field
     * enumeration is used. The {@link LoaderFieldContext#index()} property
     * corresponds to the source column index.
     */
    @Support
    LoaderListenerStep<R> fields(LoaderFieldMapper mapper);

    /**
     * Indicate that all input fields which have a corresponding field in the
     * target table (with the same name) should be loaded.
     * <p>
     * When {@link LoaderLoadStep#execute() executing the loader} input fields
     * for which there is no match in the target table will be logged and if no
     * field names can be derived for the input data a
     * {@link LoaderConfigurationException} will be reported.
     */
    @Support
    LoaderListenerStep<R> fieldsFromSource();
}
