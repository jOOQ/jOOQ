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

// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.HSQLDB;
// ...
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
// ...
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...

import java.util.Collection;
import java.util.Map;

import javax.annotation.Generated;

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
public interface InsertSetStep<R extends Record> {

    /**
     * Set the columns for insert.
     */
    @Support
    InsertValuesStepN<R> columns(Field<?>... fields);

    /**
     * Set the columns for insert.
     */
    @Support
    InsertValuesStepN<R> columns(Collection<? extends Field<?>> fields);

    // [jooq-tools] START [columns]

    /**
     * Set the columns for insert.
     */
    @Generated("This class was generated using jOOQ-tools")
    @Support
    <T1> InsertValuesStep1<R, T1> columns(Field<T1> field1);

    /**
     * Set the columns for insert.
     */
    @Generated("This class was generated using jOOQ-tools")
    @Support
    <T1, T2> InsertValuesStep2<R, T1, T2> columns(Field<T1> field1, Field<T2> field2);

    /**
     * Set the columns for insert.
     */
    @Generated("This class was generated using jOOQ-tools")
    @Support
    <T1, T2, T3> InsertValuesStep3<R, T1, T2, T3> columns(Field<T1> field1, Field<T2> field2, Field<T3> field3);

    /**
     * Set the columns for insert.
     */
    @Generated("This class was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4> InsertValuesStep4<R, T1, T2, T3, T4> columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4);

    /**
     * Set the columns for insert.
     */
    @Generated("This class was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5> InsertValuesStep5<R, T1, T2, T3, T4, T5> columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5);

    /**
     * Set the columns for insert.
     */
    @Generated("This class was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6> InsertValuesStep6<R, T1, T2, T3, T4, T5, T6> columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6);

    /**
     * Set the columns for insert.
     */
    @Generated("This class was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7> InsertValuesStep7<R, T1, T2, T3, T4, T5, T6, T7> columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7);

    /**
     * Set the columns for insert.
     */
    @Generated("This class was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8> InsertValuesStep8<R, T1, T2, T3, T4, T5, T6, T7, T8> columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8);

    /**
     * Set the columns for insert.
     */
    @Generated("This class was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9> InsertValuesStep9<R, T1, T2, T3, T4, T5, T6, T7, T8, T9> columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9);

    /**
     * Set the columns for insert.
     */
    @Generated("This class was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> InsertValuesStep10<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10);

    /**
     * Set the columns for insert.
     */
    @Generated("This class was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> InsertValuesStep11<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11);

    /**
     * Set the columns for insert.
     */
    @Generated("This class was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> InsertValuesStep12<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12);

    /**
     * Set the columns for insert.
     */
    @Generated("This class was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> InsertValuesStep13<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13);

    /**
     * Set the columns for insert.
     */
    @Generated("This class was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> InsertValuesStep14<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14);

    /**
     * Set the columns for insert.
     */
    @Generated("This class was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> InsertValuesStep15<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15);

    /**
     * Set the columns for insert.
     */
    @Generated("This class was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> InsertValuesStep16<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16);

    /**
     * Set the columns for insert.
     */
    @Generated("This class was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> InsertValuesStep17<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17);

    /**
     * Set the columns for insert.
     */
    @Generated("This class was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> InsertValuesStep18<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18);

    /**
     * Set the columns for insert.
     */
    @Generated("This class was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> InsertValuesStep19<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19);

    /**
     * Set the columns for insert.
     */
    @Generated("This class was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> InsertValuesStep20<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20);

    /**
     * Set the columns for insert.
     */
    @Generated("This class was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> InsertValuesStep21<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21);

    /**
     * Set the columns for insert.
     */
    @Generated("This class was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> InsertValuesStep22<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21, Field<T22> field22);

// [jooq-tools] END [columns]

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
     * treated as a <code>Map&lt;Field&lt;?>, Object></code>, except that the
     * {@link Record#changed()} flags are taken into consideration in order to
     * update only changed values.
     *
     * @see #set(Map)
     */
    @Support
    InsertSetMoreStep<R> set(Record record);

    /**
     * Add values to the insert statement with implicit field names.
     */
    @Support
    InsertValuesStepN<R> values(Object... values);

    /**
     * Add values to the insert statement with implicit field names.
     */
    @Support
    InsertValuesStepN<R> values(Field<?>... values);

    /**
     * Add values to the insert statement with implicit field names.
     */
    @Support
    InsertValuesStepN<R> values(Collection<?> values);

    /**
     * Add an empty record with default values.
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    InsertReturningStep<R> defaultValues();

    /**
     * Use a <code>SELECT</code> statement as the source of values for the
     * <code>INSERT</code> statement.
     * <p>
     * This variant of the <code>INSERT .. SELECT</code> statement does not
     * allow for specifying a subset of the fields inserted into. It will insert
     * into all fields of the table specified in the <code>INTO</code> clause.
     * Use {@link DSLContext#insertInto(Table, Field...)} or
     * {@link DSLContext#insertInto(Table, Collection)} instead, to
     * define a field set for insertion.
     */
    @Support
    InsertReturningStep<R> select(Select<?> select);
}
