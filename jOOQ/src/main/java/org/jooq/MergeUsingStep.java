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

import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.FIREBIRD_3_0;
import static org.jooq.SQLDialect.H2;
// ...
import static org.jooq.SQLDialect.HSQLDB;
// ...
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
// ...
import static org.jooq.SQLDialect.POSTGRES_9_5;
// ...
// ...

import java.util.Collection;

import javax.annotation.Generated;

/**
 * This type is used for the {@link Merge}'s DSL API.
 * <p>
 * Example: <code><pre>
 * DSLContext create = DSL.using(configuration);
 *
 * create.mergeInto(table)
 *       .using(select)
 *       .on(condition)
 *       .whenMatchedThenUpdate()
 *       .set(field1, value1)
 *       .set(field2, value2)
 *       .whenNotMatchedThenInsert(field1, field2)
 *       .values(value1, value2)
 *       .execute();
 * </pre></code>
 *
 * @author Lukas Eder
 */
public interface MergeUsingStep<R extends Record> extends MergeKeyStepN<R> {

    /**
     * Add the <code>USING</code> clause to the SQL standard <code>MERGE</code>
     * statement
     */
    @Support({ CUBRID, FIREBIRD_3_0, HSQLDB })
    MergeOnStep<R> using(TableLike<?> table);

    /**
     * Add a dummy <code>USING</code> clause to the SQL standard
     * <code>MERGE</code> statement
     * <p>
     * This results in <code>USING(SELECT 1 FROM DUAL)</code> for most RDBMS, or
     * in <code>USING(SELECT 1) AS [dummy_table(dummy_field)]</code> in SQL
     * Server, where derived tables need to be aliased.
     */
    @Support({ CUBRID, FIREBIRD_3_0, HSQLDB })
    MergeOnStep<R> usingDual();

    /**
     * Set the columns for merge (H2-specific syntax).
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href= "www.h2database.com/html/grammar.html#merge"
     * >www.h2database.com/html/grammar.html#merge</a></td>
     * </tr>
     * <tr>
     * <td>HANA</td>
     * <td>HANA natively supports this syntax</td>
     * <td><a href="http://help.sap.com/saphelp_hanaplatform/helpdata/en/20/fc06a7751910149892c0d09be21a38/content.htm">http://help.sap.com/saphelp_hanaplatform/helpdata/en/20/fc06a7751910149892c0d09be21a38/content.htm</a></td>
     * </tr>
     * <tr>
     * <td>PostgreSQL</td>
     * <td>This database can emulate the H2-specific MERGE statement via
     * <code>INSERT .. ON CONFLICT DO UPDATE</code></td>
     * <td><a href="http://www.postgresql.org/docs/9.5/static/sql-insert.html">http://www.postgresql.org/docs/9.5/static/sql-insert.html</a></td>
     * </tr>
     * <tr>
     * <td>DB2, Firebird, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can emulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link DSLContext#mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     */
    @Support({ CUBRID, FIREBIRD_3_0, H2, HSQLDB, MARIADB, MYSQL })
    MergeKeyStepN<R> columns(Field<?>... fields);

    /**
     * Set the columns for merge (H2-specific syntax).
     *
     * @see #columns(Field...)
     */
    @Support({ CUBRID, FIREBIRD_3_0, H2, HSQLDB, MARIADB, MYSQL })
    MergeKeyStepN<R> columns(Collection<? extends Field<?>> fields);

    // [jooq-tools] START [columns]
    /**
     * Create a new DSL UPSERT statement ({@link SQLDialect#H2}
     * <code>MERGE</code>) or {@link SQLDialect#HANA} <code>UPSERT</code>).
     *
     * @see DSLContext#mergeInto(Table, Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, FIREBIRD_3_0, H2, HSQLDB, MARIADB, MYSQL, POSTGRES_9_5 })
    <T1> MergeKeyStep1<R, T1> columns(Field<T1> field1);

    /**
     * Create a new DSL UPSERT statement ({@link SQLDialect#H2}
     * <code>MERGE</code>) or {@link SQLDialect#HANA} <code>UPSERT</code>).
     *
     * @see DSLContext#mergeInto(Table, Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, FIREBIRD_3_0, H2, HSQLDB, MARIADB, MYSQL, POSTGRES_9_5 })
    <T1, T2> MergeKeyStep2<R, T1, T2> columns(Field<T1> field1, Field<T2> field2);

    /**
     * Create a new DSL UPSERT statement ({@link SQLDialect#H2}
     * <code>MERGE</code>) or {@link SQLDialect#HANA} <code>UPSERT</code>).
     *
     * @see DSLContext#mergeInto(Table, Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, FIREBIRD_3_0, H2, HSQLDB, MARIADB, MYSQL, POSTGRES_9_5 })
    <T1, T2, T3> MergeKeyStep3<R, T1, T2, T3> columns(Field<T1> field1, Field<T2> field2, Field<T3> field3);

    /**
     * Create a new DSL UPSERT statement ({@link SQLDialect#H2}
     * <code>MERGE</code>) or {@link SQLDialect#HANA} <code>UPSERT</code>).
     *
     * @see DSLContext#mergeInto(Table, Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, FIREBIRD_3_0, H2, HSQLDB, MARIADB, MYSQL, POSTGRES_9_5 })
    <T1, T2, T3, T4> MergeKeyStep4<R, T1, T2, T3, T4> columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4);

    /**
     * Create a new DSL UPSERT statement ({@link SQLDialect#H2}
     * <code>MERGE</code>) or {@link SQLDialect#HANA} <code>UPSERT</code>).
     *
     * @see DSLContext#mergeInto(Table, Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, FIREBIRD_3_0, H2, HSQLDB, MARIADB, MYSQL, POSTGRES_9_5 })
    <T1, T2, T3, T4, T5> MergeKeyStep5<R, T1, T2, T3, T4, T5> columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5);

    /**
     * Create a new DSL UPSERT statement ({@link SQLDialect#H2}
     * <code>MERGE</code>) or {@link SQLDialect#HANA} <code>UPSERT</code>).
     *
     * @see DSLContext#mergeInto(Table, Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, FIREBIRD_3_0, H2, HSQLDB, MARIADB, MYSQL, POSTGRES_9_5 })
    <T1, T2, T3, T4, T5, T6> MergeKeyStep6<R, T1, T2, T3, T4, T5, T6> columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6);

    /**
     * Create a new DSL UPSERT statement ({@link SQLDialect#H2}
     * <code>MERGE</code>) or {@link SQLDialect#HANA} <code>UPSERT</code>).
     *
     * @see DSLContext#mergeInto(Table, Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, FIREBIRD_3_0, H2, HSQLDB, MARIADB, MYSQL, POSTGRES_9_5 })
    <T1, T2, T3, T4, T5, T6, T7> MergeKeyStep7<R, T1, T2, T3, T4, T5, T6, T7> columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7);

    /**
     * Create a new DSL UPSERT statement ({@link SQLDialect#H2}
     * <code>MERGE</code>) or {@link SQLDialect#HANA} <code>UPSERT</code>).
     *
     * @see DSLContext#mergeInto(Table, Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, FIREBIRD_3_0, H2, HSQLDB, MARIADB, MYSQL, POSTGRES_9_5 })
    <T1, T2, T3, T4, T5, T6, T7, T8> MergeKeyStep8<R, T1, T2, T3, T4, T5, T6, T7, T8> columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8);

    /**
     * Create a new DSL UPSERT statement ({@link SQLDialect#H2}
     * <code>MERGE</code>) or {@link SQLDialect#HANA} <code>UPSERT</code>).
     *
     * @see DSLContext#mergeInto(Table, Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, FIREBIRD_3_0, H2, HSQLDB, MARIADB, MYSQL, POSTGRES_9_5 })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9> MergeKeyStep9<R, T1, T2, T3, T4, T5, T6, T7, T8, T9> columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9);

    /**
     * Create a new DSL UPSERT statement ({@link SQLDialect#H2}
     * <code>MERGE</code>) or {@link SQLDialect#HANA} <code>UPSERT</code>).
     *
     * @see DSLContext#mergeInto(Table, Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, FIREBIRD_3_0, H2, HSQLDB, MARIADB, MYSQL, POSTGRES_9_5 })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> MergeKeyStep10<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10);

    /**
     * Create a new DSL UPSERT statement ({@link SQLDialect#H2}
     * <code>MERGE</code>) or {@link SQLDialect#HANA} <code>UPSERT</code>).
     *
     * @see DSLContext#mergeInto(Table, Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, FIREBIRD_3_0, H2, HSQLDB, MARIADB, MYSQL, POSTGRES_9_5 })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> MergeKeyStep11<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11);

    /**
     * Create a new DSL UPSERT statement ({@link SQLDialect#H2}
     * <code>MERGE</code>) or {@link SQLDialect#HANA} <code>UPSERT</code>).
     *
     * @see DSLContext#mergeInto(Table, Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, FIREBIRD_3_0, H2, HSQLDB, MARIADB, MYSQL, POSTGRES_9_5 })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> MergeKeyStep12<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12);

    /**
     * Create a new DSL UPSERT statement ({@link SQLDialect#H2}
     * <code>MERGE</code>) or {@link SQLDialect#HANA} <code>UPSERT</code>).
     *
     * @see DSLContext#mergeInto(Table, Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, FIREBIRD_3_0, H2, HSQLDB, MARIADB, MYSQL, POSTGRES_9_5 })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> MergeKeyStep13<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13);

    /**
     * Create a new DSL UPSERT statement ({@link SQLDialect#H2}
     * <code>MERGE</code>) or {@link SQLDialect#HANA} <code>UPSERT</code>).
     *
     * @see DSLContext#mergeInto(Table, Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, FIREBIRD_3_0, H2, HSQLDB, MARIADB, MYSQL, POSTGRES_9_5 })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> MergeKeyStep14<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14);

    /**
     * Create a new DSL UPSERT statement ({@link SQLDialect#H2}
     * <code>MERGE</code>) or {@link SQLDialect#HANA} <code>UPSERT</code>).
     *
     * @see DSLContext#mergeInto(Table, Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, FIREBIRD_3_0, H2, HSQLDB, MARIADB, MYSQL, POSTGRES_9_5 })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> MergeKeyStep15<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15);

    /**
     * Create a new DSL UPSERT statement ({@link SQLDialect#H2}
     * <code>MERGE</code>) or {@link SQLDialect#HANA} <code>UPSERT</code>).
     *
     * @see DSLContext#mergeInto(Table, Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, FIREBIRD_3_0, H2, HSQLDB, MARIADB, MYSQL, POSTGRES_9_5 })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> MergeKeyStep16<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16);

    /**
     * Create a new DSL UPSERT statement ({@link SQLDialect#H2}
     * <code>MERGE</code>) or {@link SQLDialect#HANA} <code>UPSERT</code>).
     *
     * @see DSLContext#mergeInto(Table, Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, FIREBIRD_3_0, H2, HSQLDB, MARIADB, MYSQL, POSTGRES_9_5 })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> MergeKeyStep17<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17);

    /**
     * Create a new DSL UPSERT statement ({@link SQLDialect#H2}
     * <code>MERGE</code>) or {@link SQLDialect#HANA} <code>UPSERT</code>).
     *
     * @see DSLContext#mergeInto(Table, Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, FIREBIRD_3_0, H2, HSQLDB, MARIADB, MYSQL, POSTGRES_9_5 })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> MergeKeyStep18<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18);

    /**
     * Create a new DSL UPSERT statement ({@link SQLDialect#H2}
     * <code>MERGE</code>) or {@link SQLDialect#HANA} <code>UPSERT</code>).
     *
     * @see DSLContext#mergeInto(Table, Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, FIREBIRD_3_0, H2, HSQLDB, MARIADB, MYSQL, POSTGRES_9_5 })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> MergeKeyStep19<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19);

    /**
     * Create a new DSL UPSERT statement ({@link SQLDialect#H2}
     * <code>MERGE</code>) or {@link SQLDialect#HANA} <code>UPSERT</code>).
     *
     * @see DSLContext#mergeInto(Table, Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, FIREBIRD_3_0, H2, HSQLDB, MARIADB, MYSQL, POSTGRES_9_5 })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> MergeKeyStep20<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20);

    /**
     * Create a new DSL UPSERT statement ({@link SQLDialect#H2}
     * <code>MERGE</code>) or {@link SQLDialect#HANA} <code>UPSERT</code>).
     *
     * @see DSLContext#mergeInto(Table, Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, FIREBIRD_3_0, H2, HSQLDB, MARIADB, MYSQL, POSTGRES_9_5 })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> MergeKeyStep21<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21);

    /**
     * Create a new DSL UPSERT statement ({@link SQLDialect#H2}
     * <code>MERGE</code>) or {@link SQLDialect#HANA} <code>UPSERT</code>).
     *
     * @see DSLContext#mergeInto(Table, Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, FIREBIRD_3_0, H2, HSQLDB, MARIADB, MYSQL, POSTGRES_9_5 })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> MergeKeyStep22<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> columns(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21, Field<T22> field22);

// [jooq-tools] END [columns]

}
