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

// ...
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.CLICKHOUSE;
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.DUCKDB;
// ...
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
// ...
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.IGNITE;
// ...
// ...
import static org.jooq.SQLDialect.MARIADB;
// ...
import static org.jooq.SQLDialect.MYSQL;
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.YUGABYTEDB;

import java.util.Collection;
import java.util.Map;

import org.jooq.conf.Settings;

import org.jetbrains.annotations.NotNull;

/**
 * This type is used for the {@link Insert}'s alternative DSL API.
 * <p>
 * Example: <pre><code>
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
public interface InsertSetStep<R extends Record> {

    /**
     * Set the columns for insert.
     */
    @NotNull @CheckReturnValue
    @Support
    InsertValuesStepN<R> columns(Field<?>... fields);

    /**
     * Set the columns for insert.
     */
    @NotNull @CheckReturnValue
    @Support
    InsertValuesStepN<R> columns(Collection<? extends Field<?>> fields);



    /**
     * Set the columns for insert.
     */
    @NotNull @CheckReturnValue
    @Support
    <T1> InsertValuesStep1<R, T1> columns(Field<T1> field1);

    /**
     * Set the columns for insert.
     */
    @NotNull @CheckReturnValue
    @Support
    <T1, T2> InsertValuesStep2<R, T1, T2> columns(Field<T1> field1, Field<T2> field2);

    /**
     * Set the columns for insert.
     */
    @NotNull @CheckReturnValue
    @Support
    <T1, T2, T3> InsertValuesStep3<R, T1, T2, T3> columns(Field<T1> field1, Field<T2> field2, Field<T3> field3);

    /**
     * Set the columns for insert.
     */
    @NotNull @CheckReturnValue
    @Support
    <T1, T2, T3, T4> InsertValuesStep4<R, T1, T2, T3, T4> columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4);

    /**
     * Set the columns for insert.
     */
    @NotNull @CheckReturnValue
    @Support
    <T1, T2, T3, T4, T5> InsertValuesStep5<R, T1, T2, T3, T4, T5> columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5);

    /**
     * Set the columns for insert.
     */
    @NotNull @CheckReturnValue
    @Support
    <T1, T2, T3, T4, T5, T6> InsertValuesStep6<R, T1, T2, T3, T4, T5, T6> columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6);

    /**
     * Set the columns for insert.
     */
    @NotNull @CheckReturnValue
    @Support
    <T1, T2, T3, T4, T5, T6, T7> InsertValuesStep7<R, T1, T2, T3, T4, T5, T6, T7> columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7);

    /**
     * Set the columns for insert.
     */
    @NotNull @CheckReturnValue
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8> InsertValuesStep8<R, T1, T2, T3, T4, T5, T6, T7, T8> columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8);

    /**
     * Set the columns for insert.
     */
    @NotNull @CheckReturnValue
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9> InsertValuesStep9<R, T1, T2, T3, T4, T5, T6, T7, T8, T9> columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9);

    /**
     * Set the columns for insert.
     */
    @NotNull @CheckReturnValue
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> InsertValuesStep10<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10);

    /**
     * Set the columns for insert.
     */
    @NotNull @CheckReturnValue
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> InsertValuesStep11<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11);

    /**
     * Set the columns for insert.
     */
    @NotNull @CheckReturnValue
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> InsertValuesStep12<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12);

    /**
     * Set the columns for insert.
     */
    @NotNull @CheckReturnValue
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> InsertValuesStep13<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13);

    /**
     * Set the columns for insert.
     */
    @NotNull @CheckReturnValue
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> InsertValuesStep14<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14);

    /**
     * Set the columns for insert.
     */
    @NotNull @CheckReturnValue
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> InsertValuesStep15<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15);

    /**
     * Set the columns for insert.
     */
    @NotNull @CheckReturnValue
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> InsertValuesStep16<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16);

    /**
     * Set the columns for insert.
     */
    @NotNull @CheckReturnValue
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> InsertValuesStep17<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17);

    /**
     * Set the columns for insert.
     */
    @NotNull @CheckReturnValue
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> InsertValuesStep18<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18);

    /**
     * Set the columns for insert.
     */
    @NotNull @CheckReturnValue
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> InsertValuesStep19<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19);

    /**
     * Set the columns for insert.
     */
    @NotNull @CheckReturnValue
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> InsertValuesStep20<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20);

    /**
     * Set the columns for insert.
     */
    @NotNull @CheckReturnValue
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> InsertValuesStep21<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21);

    /**
     * Set the columns for insert.
     */
    @NotNull @CheckReturnValue
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> InsertValuesStep22<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21, Field<T22> field22);



    /**
     * Set a value for a field in the <code>INSERT</code> statement.
     */
    @NotNull @CheckReturnValue
    @Support
    <T> InsertSetMoreStep<R> set(Field<T> field, T value);

    /**
     * Set a value for a field in the <code>INSERT</code> statement.
     */
    @NotNull @CheckReturnValue
    @Support
    <T> InsertSetMoreStep<R> set(Field<T> field, Field<T> value);

    /**
     * Set a value for a field in the <code>INSERT</code> statement.
     */
    @NotNull @CheckReturnValue
    @Support
    <T> InsertSetMoreStep<R> set(Field<T> field, Select<? extends Record1<T>> value);

    /**
     * Set a <code>null</code> value for a field in the <code>INSERT</code>
     * statement.
     * <p>
     * This method is convenience for calling {@link #set(Field, Object)},
     * without the necessity of casting the Java <code>null</code> literal to
     * <code>(T)</code>.
     */
    @NotNull @CheckReturnValue
    @Support
    <T> InsertSetMoreStep<R> setNull(Field<T> field);

    /**
     * Set values in the <code>INSERT</code> statement.
     * <p>
     * Keys can either be of type {@link String}, {@link Name}, or
     * {@link Field}.
     * <p>
     * Values can either be of type <code>&lt;T&gt;</code> or
     * <code>Field&lt;T&gt;</code>. jOOQ will attempt to convert values to their
     * corresponding field's type.
     */
    @NotNull @CheckReturnValue
    @Support
    InsertSetMoreStep<R> set(Map<?, ?> map);

    /**
     * Set values in the <code>INSERT</code> statement.
     * <p>
     * This is the same as calling {@link #set(Map)} with the argument record
     * treated as a <code>Map&lt;Field&lt;?&gt;, Object&gt;</code>, except that
     * the {@link Record#touched()} flags (or {@link Record#modified()} flags,
     * depending on the query's {@link Settings#getRecordDirtyTracking()}
     * configuration) are taken into consideration in order to update only
     * touched (or modified) values.
     *
     * @see #set(Map)
     */
    @NotNull @CheckReturnValue
    @Support
    InsertSetMoreStep<R> set(Record record);

    /**
     * Set values in the <code>INSERT</code> statement.
     * <p>
     * This is convenience for multiple calls to {@link #set(Record)} and
     * {@link InsertSetMoreStep#newRecord()}.
     *
     * @see #set(Record)
     */
    @NotNull @CheckReturnValue
    @Support
    InsertSetMoreStep<R> set(Record... records);

    /**
     * Set values in the <code>INSERT</code> statement.
     * <p>
     * This is convenience for multiple calls to {@link #set(Record)} and
     * {@link InsertSetMoreStep#newRecord()}.
     *
     * @see #set(Record)
     */
    @NotNull @CheckReturnValue
    @Support
    InsertSetMoreStep<R> set(Collection<? extends Record> records);

    /**
     * Add values to the insert statement with implicit field names.
     */
    @NotNull @CheckReturnValue
    @Support
    InsertValuesStepN<R> values(Object... values);

    /**
     * Add values to the insert statement with implicit field names.
     */
    @NotNull @CheckReturnValue
    @Support
    InsertValuesStepN<R> values(Field<?>... values);

    /**
     * Add values to the insert statement with implicit field names.
     */
    @NotNull @CheckReturnValue
    @Support
    InsertValuesStepN<R> values(Collection<?> values);

    /**
     * Add an empty record with default values.
     */
    @NotNull @CheckReturnValue
    @Support({ CLICKHOUSE, CUBRID, DERBY, DUCKDB, FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB })
    InsertOnDuplicateStep<R> defaultValues();

    /**
     * Use a <code>SELECT</code> statement as the source of values for the
     * <code>INSERT</code> statement.
     * <p>
     * This variant of the <code>INSERT … SELECT</code> statement does not
     * allow for specifying a subset of the fields inserted into. It will insert
     * into all fields of the table specified in the <code>INTO</code> clause.
     * Use {@link DSLContext#insertInto(Table, Field...)} or
     * {@link DSLContext#insertInto(Table, Collection)} instead, to
     * define a field set for insertion.
     */
    @NotNull @CheckReturnValue
    @Support
    InsertOnDuplicateStep<R> select(Select<?> select);
}
