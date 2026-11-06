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
 * The step in constructing the {@link Loader} object where you can set the
 * mandatory XML loader options.
 * <p>
 * The expected XML format is the one produced by {@link Formattable#formatXML()}
 * and consumed by {@link DSLContext#fetchFromXML(String)}, e.g.
 *
 * <pre><code>
 * &lt;result xmlns="http://www.jooq.org/xsd/jooq-export-3.10.0.xsd"&gt;
 *   &lt;fields&gt;
 *     &lt;field name="ID" type="INTEGER"/&gt;
 *     &lt;field name="TITLE" type="VARCHAR"/&gt;
 *   &lt;/fields&gt;
 *   &lt;records&gt;
 *     &lt;record&gt;
 *       &lt;value field="ID"&gt;1&lt;/value&gt;
 *       &lt;value field="TITLE"&gt;1984&lt;/value&gt;
 *     &lt;/record&gt;
 *   &lt;/records&gt;
 * &lt;/result&gt;
 * </code></pre>
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
public interface LoaderXMLStep<R extends Record> {

    /**
     * Specify the fields to be loaded into the table in the correct order.
     * <p>
     * The XML column at index <code>i</code> is inserted into the table field
     * at index <code>i</code>. If <code>fields[i] == null</code> or
     * <code>fields.length &lt;= i</code>, then the XML column is skipped.
     */
    @NotNull @CheckReturnValue
    @Support
    LoaderXMLOptionsStep<R> fields(Field<?>... fields);

    /**
     * Specify the fields to be loaded into the table in the correct order.
     * <p>
     * The XML column at index <code>i</code> is inserted into the table field
     * at index <code>i</code>. If
     * <code>new ArrayList(fields).get(i) == null</code> or
     * <code>new ArrayList(fields).size() &lt;= i</code>, then the XML column
     * is skipped.
     */
    @NotNull @CheckReturnValue
    @Support
    LoaderXMLOptionsStep<R> fields(Collection<? extends Field<?>> fields);

    /**
     * Specify a function to apply on each input field to receive the target
     * table's field.
     * <p>
     * The input field obtained from {@link LoaderFieldContext#field()} wraps
     * the XML column name as declared in the <code>&lt;fields/&gt;</code>
     * element. The {@link LoaderFieldContext#index()} property corresponds to
     * the XML column index in declaration order.
     */
    @NotNull @CheckReturnValue
    @Support
    LoaderXMLOptionsStep<R> fields(LoaderFieldMapper mapper);

    /**
     * Indicate that all input fields which have a corresponding field in the
     * target table (with the same name) should be loaded.
     *
     * @throws LoaderConfigurationException When the source data does not expose
     *             field names.
     */
    @NotNull @CheckReturnValue
    @Support
    LoaderXMLOptionsStep<R> fieldsCorresponding();

}
