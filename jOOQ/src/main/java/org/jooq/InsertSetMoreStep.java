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

import java.util.Map;

/**
 * This type is used for the {@link Insert}'s alternative DSL API.
 * <p>
 * Example: <code><pre>
 * DSLContext create = DSL.using(configuration);
 *
 * create.insertInto(table)
 *       .set(field1, value1)
 *       .set(field2, value2)
 *       .newRecord()
 *       .set(field1, value3)
 *       .set(field2, value4)
 *       .onDuplicateKeyUpdate()
 *       .set(field1, value1)
 *       .set(field2, value2)
 *       .execute();
 * </pre></code>
 *
 * @author Lukas Eder
 */
public interface InsertSetMoreStep<R extends Record> extends InsertOnDuplicateStep<R> {

    /**
     * Set a value for a field in the <code>INSERT</code> statement.
     */
    @Support
    <T> InsertSetMoreStep<R> set(Field<T> field, T value);

    /**
     * Set a value for a field in the <code>INSERT</code> statement.
     */
    @Support
    <T> InsertSetMoreStep<R> set(Field<T> field, Field<T> value);

    /**
     * Set a value for a field in the <code>INSERT</code> statement.
     */
    @Support
    <T> InsertSetMoreStep<R> set(Field<T> field, Select<? extends Record1<T>> value);

    /**
     * Set values in the <code>INSERT</code> statement.
     * <p>
     * Values can either be of type <code>&lt;T&gt;</code> or
     * <code>Field&lt;T&gt;</code>. jOOQ will attempt to convert values to their
     * corresponding field's type.
     */
    @Support
    InsertSetMoreStep<R> set(Map<? extends Field<?>, ?> map);

    /**
     * Set values in the <code>INSERT</code> statement.
     * <p>
     * This is the same as calling {@link #set(Map)} with the argument record
     * treated as a <code>Map&lt;Field&lt;?>, Object></code>.
     *
     * @see #set(Map)
     */
    @Support
    InsertSetMoreStep<R> set(Record record);

    /**
     * Add an additional record to the <code>INSERT</code> statement
     *
     * @see InsertQuery#newRecord()
     */
    @Support
    InsertSetStep<R> newRecord();
}
