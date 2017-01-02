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
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
// ...
import static org.jooq.SQLDialect.HSQLDB;
// ...
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...

import java.util.Collection;

import javax.annotation.Generated;

/**
 * A query for data updating
 *
 * @param <R> The record type of the table being updated
 * @author Lukas Eder
 */
@SuppressWarnings("deprecation")
public interface UpdateQuery<R extends Record> extends StoreQuery<R>, ConditionProvider, Update<R> {

// [jooq-tools] START [addValues]

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ H2, HSQLDB, POSTGRES })
    <T1> void addValues(Row1<T1> row, Row1<T1> value);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ H2, HSQLDB, POSTGRES })
    <T1, T2> void addValues(Row2<T1, T2> row, Row2<T1, T2> value);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ H2, HSQLDB, POSTGRES })
    <T1, T2, T3> void addValues(Row3<T1, T2, T3> row, Row3<T1, T2, T3> value);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ H2, HSQLDB, POSTGRES })
    <T1, T2, T3, T4> void addValues(Row4<T1, T2, T3, T4> row, Row4<T1, T2, T3, T4> value);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ H2, HSQLDB, POSTGRES })
    <T1, T2, T3, T4, T5> void addValues(Row5<T1, T2, T3, T4, T5> row, Row5<T1, T2, T3, T4, T5> value);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ H2, HSQLDB, POSTGRES })
    <T1, T2, T3, T4, T5, T6> void addValues(Row6<T1, T2, T3, T4, T5, T6> row, Row6<T1, T2, T3, T4, T5, T6> value);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ H2, HSQLDB, POSTGRES })
    <T1, T2, T3, T4, T5, T6, T7> void addValues(Row7<T1, T2, T3, T4, T5, T6, T7> row, Row7<T1, T2, T3, T4, T5, T6, T7> value);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ H2, HSQLDB, POSTGRES })
    <T1, T2, T3, T4, T5, T6, T7, T8> void addValues(Row8<T1, T2, T3, T4, T5, T6, T7, T8> row, Row8<T1, T2, T3, T4, T5, T6, T7, T8> value);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ H2, HSQLDB, POSTGRES })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9> void addValues(Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9> row, Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9> value);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ H2, HSQLDB, POSTGRES })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> void addValues(Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> row, Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> value);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ H2, HSQLDB, POSTGRES })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> void addValues(Row11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> row, Row11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> value);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ H2, HSQLDB, POSTGRES })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> void addValues(Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> row, Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> value);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ H2, HSQLDB, POSTGRES })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> void addValues(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> row, Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> value);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ H2, HSQLDB, POSTGRES })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> void addValues(Row14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> row, Row14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> value);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ H2, HSQLDB, POSTGRES })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> void addValues(Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> row, Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> value);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ H2, HSQLDB, POSTGRES })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> void addValues(Row16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> row, Row16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> value);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ H2, HSQLDB, POSTGRES })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> void addValues(Row17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> row, Row17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> value);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ H2, HSQLDB, POSTGRES })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> void addValues(Row18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> row, Row18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> value);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ H2, HSQLDB, POSTGRES })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> void addValues(Row19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> row, Row19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> value);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ H2, HSQLDB, POSTGRES })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> void addValues(Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> row, Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> value);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ H2, HSQLDB, POSTGRES })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> void addValues(Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> row, Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> value);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ H2, HSQLDB, POSTGRES })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> void addValues(Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> row, Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> value);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ H2, HSQLDB, POSTGRES })
    void addValues(RowN row, RowN value);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ H2, HSQLDB })
    <T1> void addValues(Row1<T1> row, Select<? extends Record1<T1>> select);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ H2, HSQLDB })
    <T1, T2> void addValues(Row2<T1, T2> row, Select<? extends Record2<T1, T2>> select);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ H2, HSQLDB })
    <T1, T2, T3> void addValues(Row3<T1, T2, T3> row, Select<? extends Record3<T1, T2, T3>> select);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ H2, HSQLDB })
    <T1, T2, T3, T4> void addValues(Row4<T1, T2, T3, T4> row, Select<? extends Record4<T1, T2, T3, T4>> select);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ H2, HSQLDB })
    <T1, T2, T3, T4, T5> void addValues(Row5<T1, T2, T3, T4, T5> row, Select<? extends Record5<T1, T2, T3, T4, T5>> select);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ H2, HSQLDB })
    <T1, T2, T3, T4, T5, T6> void addValues(Row6<T1, T2, T3, T4, T5, T6> row, Select<? extends Record6<T1, T2, T3, T4, T5, T6>> select);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ H2, HSQLDB })
    <T1, T2, T3, T4, T5, T6, T7> void addValues(Row7<T1, T2, T3, T4, T5, T6, T7> row, Select<? extends Record7<T1, T2, T3, T4, T5, T6, T7>> select);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ H2, HSQLDB })
    <T1, T2, T3, T4, T5, T6, T7, T8> void addValues(Row8<T1, T2, T3, T4, T5, T6, T7, T8> row, Select<? extends Record8<T1, T2, T3, T4, T5, T6, T7, T8>> select);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ H2, HSQLDB })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9> void addValues(Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9> row, Select<? extends Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> select);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ H2, HSQLDB })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> void addValues(Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> row, Select<? extends Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>> select);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ H2, HSQLDB })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> void addValues(Row11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> row, Select<? extends Record11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>> select);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ H2, HSQLDB })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> void addValues(Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> row, Select<? extends Record12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>> select);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ H2, HSQLDB })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> void addValues(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> row, Select<? extends Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> select);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ H2, HSQLDB })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> void addValues(Row14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> row, Select<? extends Record14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>> select);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ H2, HSQLDB })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> void addValues(Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> row, Select<? extends Record15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>> select);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ H2, HSQLDB })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> void addValues(Row16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> row, Select<? extends Record16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>> select);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ H2, HSQLDB })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> void addValues(Row17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> row, Select<? extends Record17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>> select);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ H2, HSQLDB })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> void addValues(Row18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> row, Select<? extends Record18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>> select);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ H2, HSQLDB })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> void addValues(Row19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> row, Select<? extends Record19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>> select);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ H2, HSQLDB })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> void addValues(Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> row, Select<? extends Record20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>> select);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ H2, HSQLDB })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> void addValues(Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> row, Select<? extends Record21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>> select);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ H2, HSQLDB })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> void addValues(Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> row, Select<? extends Record22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>> select);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ H2, HSQLDB })
    void addValues(RowN row, Select<?> select);

// [jooq-tools] END [addValues]

    /**
     * Add tables to the table product.
     *
     * @param from The added tables
     */
    @Support({ POSTGRES })
    void addFrom(TableLike<?> from);

    /**
     * Add tables to the table product.
     *
     * @param from The added tables
     */
    @Support({ POSTGRES })
    void addFrom(TableLike<?>... from);

    /**
     * Add tables to the table product.
     *
     * @param from The added tables
     */
    @Support({ POSTGRES })
    void addFrom(Collection<? extends TableLike<?>> from);

    // ------------------------------------------------------------------------
    // XXX: Methods from ConditionProvider
    // ------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    @Support
    void addConditions(Condition... conditions);

    /**
     * {@inheritDoc}
     */
    @Override
    @Support
    void addConditions(Collection<? extends Condition> conditions);

    /**
     * {@inheritDoc}
     */
    @Override
    @Support
    void addConditions(Operator operator, Condition... conditions);

    /**
     * {@inheritDoc}
     */
    @Override
    @Support
    void addConditions(Operator operator, Collection<? extends Condition> conditions);

    // ------------------------------------------------------------------------
    // XXX: Methods for the UPDATE .. RETURNING syntax
    // ------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     * <p>
     * This feature works with <code>UPDATE</code> statements for a subset of
     * SQL dialects
     */
    @Override
    @Support({ FIREBIRD, POSTGRES })
    void setReturning();

    /**
     * {@inheritDoc}
     * <p>
     * This feature works with <code>UPDATE</code> statements for a subset of
     * SQL dialects
     */
    @Override
    @Support({ FIREBIRD, POSTGRES })
    void setReturning(Identity<R, ?> identity);

    /**
     * {@inheritDoc}
     * <p>
     * This feature works with <code>UPDATE</code> statements for a subset of
     * SQL dialects
     */
    @Override
    @Support({ FIREBIRD, POSTGRES })
    void setReturning(Field<?>... fields);

    /**
     * {@inheritDoc}
     * <p>
     * This feature works with <code>UPDATE</code> statements for a subset of
     * SQL dialects
     */
    @Override
    @Support({ FIREBIRD, POSTGRES })
    void setReturning(Collection<? extends Field<?>> fields);

    /**
     * {@inheritDoc}
     * <p>
     * This feature works with <code>UPDATE</code> statements for a subset of
     * SQL dialects
     */
    @Override
    @Support({ FIREBIRD, POSTGRES })
    R getReturnedRecord();

    /**
     * {@inheritDoc}
     * <p>
     * This feature works with <code>UPDATE</code> statements for a subset of
     * SQL dialects
     */
    @Override
    @Support({ FIREBIRD, POSTGRES })
    Result<R> getReturnedRecords();

}
