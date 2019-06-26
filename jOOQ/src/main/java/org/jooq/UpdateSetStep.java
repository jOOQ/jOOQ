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

import java.util.Map;

/**
 * This type is used for the {@link Update}'s DSL API.
 * <p>
 * Example: <code><pre>
 * DSLContext create = DSL.using(configuration);
 *
 * create.update(table)
 *       .set(field1, value1)
 *       .set(field2, value2)
 *       .where(field1.greaterThan(100))
 *       .execute();
 * </pre></code>
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
public interface UpdateSetStep<R extends Record> {

    /**
     * Set a value for a field in the <code>UPDATE</code> statement.
     */
    @Support
    <T> UpdateSetMoreStep<R> set(Field<T> field, T value);

    /**
     * Set a value for a field in the <code>UPDATE</code> statement.
     */
    @Support
    <T> UpdateSetMoreStep<R> set(Field<T> field, Field<T> value);

    /**
     * Set a value for a field in the <code>UPDATE</code> statement.
     */
    @Support
    <T> UpdateSetMoreStep<R> set(Field<T> field, Select<? extends Record1<T>> value);

    /**
     * Set a <code>null</code> value for a field in the <code>UPDATE</code>
     * statement.
     * <p>
     * This method is convenience for calling {@link #set(Field, Object)},
     * without the necessity of casting the Java <code>null</code> literal to
     * <code>(T)</code>.
     */
    @Support
    <T> UpdateSetMoreStep<R> setNull(Field<T> field);

    /**
     * Set a value for a field in the <code>UPDATE</code> statement.
     * <p>
     * Keys can either be of type {@link String}, {@link Name}, or
     * {@link Field}.
     * <p>
     * Values can either be of type <code>&lt;T&gt;</code> or
     * <code>Field&lt;T&gt;</code>. jOOQ will attempt to convert values to their
     * corresponding field's type.
     */
    @Support
    UpdateSetMoreStep<R> set(Map<?, ?> map);

    /**
     * Set a value for a field in the <code>UPDATE</code> statement.
     * <p>
     * This is the same as calling {@link #set(Map)} with the argument record
     * treated as a <code>Map&lt;Field&lt;?&gt;, Object&gt;</code>, except that the
     * {@link Record#changed()} flags are taken into consideration in order to
     * update only changed values.
     *
     * @see #set(Map)
     */
    @Support
    UpdateSetMoreStep<R> set(Record record);
}
