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

/**
 * This type is used for the {@link Insert}'s DSL API.
 * <p>
 * Example: <code><pre>
 * DSLContext create = DSL.using(configuration);
 *
 * TableRecord&lt;?&gt; record =
 * create.insertInto(table, field1, field2)
 *       .values(value1, value2)
 *       .returning(field1)
 *       .fetchOne();
 * </pre></code>
 * <p>
 * This implemented differently for every dialect:
 * <ul>
 * <li>Firebird and Postgres have native support for
 * <code>INSERT .. RETURNING</code> clauses</li>
 * <li>DB2 allows to execute
 * <code>SELECT .. FROM FINAL TABLE (INSERT ...)</code></li>
 * <li>HSQLDB, and Oracle JDBC drivers allow for retrieving any table column as
 * "generated key" in one statement</li>
 * <li>Derby, H2, Ingres, MySQL, SQL Server only allow for retrieving IDENTITY
 * column values as "generated key". If other fields are requested, a second
 * statement is issued. Client code must assure transactional integrity between
 * the two statements.</li>
 * <li>Sybase and SQLite allow for retrieving IDENTITY values as
 * <code>@@identity</code> or <code>last_inserted_rowid()</code> values. Those
 * values are fetched in a separate <code>SELECT</code> statement. If other
 * fields are requested, a second statement is issued. Client code must assure
 * transactional integrity between the two statements.</li>
 * </ul>
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
public interface InsertReturningStep<R extends Record> extends InsertFinalStep<R> {

    /**
     * Configure the <code>INSERT</code> statement to return all fields in
     * <code>R</code>.
     * <p>
     * This will return the data <em>after</em> insertion and generation of
     * default values and generation of any values produced by triggers.
     *
     * @see InsertResultStep
     */
    @Support
    InsertResultStep<R> returning();

    /**
     * Configure the <code>INSERT</code> statement to return a list of fields in
     * <code>R</code>.
     * <p>
     * This will return the data <em>after</em> insertion and generation of
     * default values and generation of any values produced by triggers.
     * <p>
     * [#5070] Due to an early API design flaw, this method historically returns
     * the type <code>R</code>, not a more generic type <code>Record</code>.
     * This means that only actual columns in <code>R</code> can be returned.
     * For a more generic set of column expressions, use
     * {@link #returningResult(SelectFieldOrAsterisk...)} instead.
     *
     * @param fields Fields to be returned
     * @see InsertResultStep
     */
    @Support
    InsertResultStep<R> returning(SelectFieldOrAsterisk... fields);

    /**
     * Configure the <code>INSERT</code> statement to return a list of fields in
     * <code>R</code>.
     * <p>
     * This will return the data <em>after</em> insertion and generation of
     * default values and generation of any values produced by triggers.
     * <p>
     * [#5070] Due to an early API design flaw, this method historically returns
     * the type <code>R</code>, not a more generic type <code>Record</code>.
     * This means that only actual columns in <code>R</code> can be returned.
     * For a more generic set of column expressions, use
     * {@link #returningResult(Collection)} instead.
     *
     * @param fields Fields to be returned
     * @see InsertResultStep
     */
    @Support
    InsertResultStep<R> returning(Collection<? extends SelectFieldOrAsterisk> fields);

    /**
     * Configure the <code>INSERT</code> statement to return a list of fields in
     * <code>R</code>.
     * <p>
     * This will return the data <em>after</em> insertion and generation of
     * default values and generation of any values produced by triggers.
     *
     * @param fields Fields to be returned
     * @see InsertResultStep
     */
    @Support
    InsertResultStep<Record> returningResult(SelectFieldOrAsterisk... fields);

    /**
     * Configure the <code>INSERT</code> statement to return a list of fields in
     * <code>R</code>.
     * <p>
     * This will return the data <em>after</em> insertion and generation of
     * default values and generation of any values produced by triggers.
     *
     * @param fields Fields to be returned
     * @see InsertResultStep
     */
    @Support
    InsertResultStep<Record> returningResult(Collection<? extends SelectFieldOrAsterisk> fields);

    // [jooq-tools] START [returning]

    /**
     * Configure the <code>INSERT</code> statement to return a list of fields in
     * <code>R</code>.
     * <p>
     * This will return the data <em>after</em> insertion and generation of
     * default values and generation of any values produced by triggers.
     *
     * @see InsertResultStep
     */
    @Support
    <T1> InsertResultStep<Record1<T1>> returningResult(SelectField<T1> field1);

    /**
     * Configure the <code>INSERT</code> statement to return a list of fields in
     * <code>R</code>.
     * <p>
     * This will return the data <em>after</em> insertion and generation of
     * default values and generation of any values produced by triggers.
     *
     * @see InsertResultStep
     */
    @Support
    <T1, T2> InsertResultStep<Record2<T1, T2>> returningResult(SelectField<T1> field1, SelectField<T2> field2);

    /**
     * Configure the <code>INSERT</code> statement to return a list of fields in
     * <code>R</code>.
     * <p>
     * This will return the data <em>after</em> insertion and generation of
     * default values and generation of any values produced by triggers.
     *
     * @see InsertResultStep
     */
    @Support
    <T1, T2, T3> InsertResultStep<Record3<T1, T2, T3>> returningResult(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3);

    /**
     * Configure the <code>INSERT</code> statement to return a list of fields in
     * <code>R</code>.
     * <p>
     * This will return the data <em>after</em> insertion and generation of
     * default values and generation of any values produced by triggers.
     *
     * @see InsertResultStep
     */
    @Support
    <T1, T2, T3, T4> InsertResultStep<Record4<T1, T2, T3, T4>> returningResult(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4);

    /**
     * Configure the <code>INSERT</code> statement to return a list of fields in
     * <code>R</code>.
     * <p>
     * This will return the data <em>after</em> insertion and generation of
     * default values and generation of any values produced by triggers.
     *
     * @see InsertResultStep
     */
    @Support
    <T1, T2, T3, T4, T5> InsertResultStep<Record5<T1, T2, T3, T4, T5>> returningResult(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5);

    /**
     * Configure the <code>INSERT</code> statement to return a list of fields in
     * <code>R</code>.
     * <p>
     * This will return the data <em>after</em> insertion and generation of
     * default values and generation of any values produced by triggers.
     *
     * @see InsertResultStep
     */
    @Support
    <T1, T2, T3, T4, T5, T6> InsertResultStep<Record6<T1, T2, T3, T4, T5, T6>> returningResult(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6);

    /**
     * Configure the <code>INSERT</code> statement to return a list of fields in
     * <code>R</code>.
     * <p>
     * This will return the data <em>after</em> insertion and generation of
     * default values and generation of any values produced by triggers.
     *
     * @see InsertResultStep
     */
    @Support
    <T1, T2, T3, T4, T5, T6, T7> InsertResultStep<Record7<T1, T2, T3, T4, T5, T6, T7>> returningResult(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7);

    /**
     * Configure the <code>INSERT</code> statement to return a list of fields in
     * <code>R</code>.
     * <p>
     * This will return the data <em>after</em> insertion and generation of
     * default values and generation of any values produced by triggers.
     *
     * @see InsertResultStep
     */
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8> InsertResultStep<Record8<T1, T2, T3, T4, T5, T6, T7, T8>> returningResult(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8);

    /**
     * Configure the <code>INSERT</code> statement to return a list of fields in
     * <code>R</code>.
     * <p>
     * This will return the data <em>after</em> insertion and generation of
     * default values and generation of any values produced by triggers.
     *
     * @see InsertResultStep
     */
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9> InsertResultStep<Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> returningResult(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9);

    /**
     * Configure the <code>INSERT</code> statement to return a list of fields in
     * <code>R</code>.
     * <p>
     * This will return the data <em>after</em> insertion and generation of
     * default values and generation of any values produced by triggers.
     *
     * @see InsertResultStep
     */
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> InsertResultStep<Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>> returningResult(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10);

    /**
     * Configure the <code>INSERT</code> statement to return a list of fields in
     * <code>R</code>.
     * <p>
     * This will return the data <em>after</em> insertion and generation of
     * default values and generation of any values produced by triggers.
     *
     * @see InsertResultStep
     */
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> InsertResultStep<Record11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>> returningResult(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11);

    /**
     * Configure the <code>INSERT</code> statement to return a list of fields in
     * <code>R</code>.
     * <p>
     * This will return the data <em>after</em> insertion and generation of
     * default values and generation of any values produced by triggers.
     *
     * @see InsertResultStep
     */
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> InsertResultStep<Record12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>> returningResult(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12);

    /**
     * Configure the <code>INSERT</code> statement to return a list of fields in
     * <code>R</code>.
     * <p>
     * This will return the data <em>after</em> insertion and generation of
     * default values and generation of any values produced by triggers.
     *
     * @see InsertResultStep
     */
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> InsertResultStep<Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> returningResult(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13);

    /**
     * Configure the <code>INSERT</code> statement to return a list of fields in
     * <code>R</code>.
     * <p>
     * This will return the data <em>after</em> insertion and generation of
     * default values and generation of any values produced by triggers.
     *
     * @see InsertResultStep
     */
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> InsertResultStep<Record14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>> returningResult(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14);

    /**
     * Configure the <code>INSERT</code> statement to return a list of fields in
     * <code>R</code>.
     * <p>
     * This will return the data <em>after</em> insertion and generation of
     * default values and generation of any values produced by triggers.
     *
     * @see InsertResultStep
     */
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> InsertResultStep<Record15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>> returningResult(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15);

    /**
     * Configure the <code>INSERT</code> statement to return a list of fields in
     * <code>R</code>.
     * <p>
     * This will return the data <em>after</em> insertion and generation of
     * default values and generation of any values produced by triggers.
     *
     * @see InsertResultStep
     */
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> InsertResultStep<Record16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>> returningResult(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15, SelectField<T16> field16);

    /**
     * Configure the <code>INSERT</code> statement to return a list of fields in
     * <code>R</code>.
     * <p>
     * This will return the data <em>after</em> insertion and generation of
     * default values and generation of any values produced by triggers.
     *
     * @see InsertResultStep
     */
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> InsertResultStep<Record17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>> returningResult(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15, SelectField<T16> field16, SelectField<T17> field17);

    /**
     * Configure the <code>INSERT</code> statement to return a list of fields in
     * <code>R</code>.
     * <p>
     * This will return the data <em>after</em> insertion and generation of
     * default values and generation of any values produced by triggers.
     *
     * @see InsertResultStep
     */
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> InsertResultStep<Record18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>> returningResult(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15, SelectField<T16> field16, SelectField<T17> field17, SelectField<T18> field18);

    /**
     * Configure the <code>INSERT</code> statement to return a list of fields in
     * <code>R</code>.
     * <p>
     * This will return the data <em>after</em> insertion and generation of
     * default values and generation of any values produced by triggers.
     *
     * @see InsertResultStep
     */
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> InsertResultStep<Record19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>> returningResult(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15, SelectField<T16> field16, SelectField<T17> field17, SelectField<T18> field18, SelectField<T19> field19);

    /**
     * Configure the <code>INSERT</code> statement to return a list of fields in
     * <code>R</code>.
     * <p>
     * This will return the data <em>after</em> insertion and generation of
     * default values and generation of any values produced by triggers.
     *
     * @see InsertResultStep
     */
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> InsertResultStep<Record20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>> returningResult(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15, SelectField<T16> field16, SelectField<T17> field17, SelectField<T18> field18, SelectField<T19> field19, SelectField<T20> field20);

    /**
     * Configure the <code>INSERT</code> statement to return a list of fields in
     * <code>R</code>.
     * <p>
     * This will return the data <em>after</em> insertion and generation of
     * default values and generation of any values produced by triggers.
     *
     * @see InsertResultStep
     */
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> InsertResultStep<Record21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>> returningResult(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15, SelectField<T16> field16, SelectField<T17> field17, SelectField<T18> field18, SelectField<T19> field19, SelectField<T20> field20, SelectField<T21> field21);

    /**
     * Configure the <code>INSERT</code> statement to return a list of fields in
     * <code>R</code>.
     * <p>
     * This will return the data <em>after</em> insertion and generation of
     * default values and generation of any values produced by triggers.
     *
     * @see InsertResultStep
     */
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> InsertResultStep<Record22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>> returningResult(SelectField<T1> field1, SelectField<T2> field2, SelectField<T3> field3, SelectField<T4> field4, SelectField<T5> field5, SelectField<T6> field6, SelectField<T7> field7, SelectField<T8> field8, SelectField<T9> field9, SelectField<T10> field10, SelectField<T11> field11, SelectField<T12> field12, SelectField<T13> field13, SelectField<T14> field14, SelectField<T15> field15, SelectField<T16> field16, SelectField<T17> field17, SelectField<T18> field18, SelectField<T19> field19, SelectField<T20> field20, SelectField<T21> field21, SelectField<T22> field22);

    // [jooq-tools] END [returning]

}
