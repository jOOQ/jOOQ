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
import static org.jooq.SQLDialect.H2;
// ...
import static org.jooq.SQLDialect.HSQLDB;
// ...
// ...
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.POSTGRES_9_5;

import javax.annotation.Generated;

/**
 * This type is used for the {@link Update}'s DSL API.
 * <p>
 * Example: <code><pre>
 * using(configuration)
 *       .update(table)
 *       .set(field1, value1)
 *       .set(field2, value2)
 *       .where(field1.greaterThan(100))
 *       .execute();
 * </pre></code>
 *
 * @author Lukas Eder
 */
@Generated("This class was generated using jOOQ-tools")
public interface UpdateSetFirstStep<R extends Record> extends UpdateSetStep<R> {

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     * <p>
     * This is emulated using a subquery for the <code>value</code>, where row
     * value expressions aren't supported.
     */
    @Support({ H2, HSQLDB, POSTGRES })
    <T1> UpdateFromStep<R> set(Row1<T1> row, Row1<T1> value);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     * <p>
     * This is emulated using a subquery for the <code>value</code>, where row
     * value expressions aren't supported.
     */
    @Support({ H2, HSQLDB, POSTGRES })
    <T1, T2> UpdateFromStep<R> set(Row2<T1, T2> row, Row2<T1, T2> value);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     * <p>
     * This is emulated using a subquery for the <code>value</code>, where row
     * value expressions aren't supported.
     */
    @Support({ H2, HSQLDB, POSTGRES })
    <T1, T2, T3> UpdateFromStep<R> set(Row3<T1, T2, T3> row, Row3<T1, T2, T3> value);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     * <p>
     * This is emulated using a subquery for the <code>value</code>, where row
     * value expressions aren't supported.
     */
    @Support({ H2, HSQLDB, POSTGRES })
    <T1, T2, T3, T4> UpdateFromStep<R> set(Row4<T1, T2, T3, T4> row, Row4<T1, T2, T3, T4> value);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     * <p>
     * This is emulated using a subquery for the <code>value</code>, where row
     * value expressions aren't supported.
     */
    @Support({ H2, HSQLDB, POSTGRES })
    <T1, T2, T3, T4, T5> UpdateFromStep<R> set(Row5<T1, T2, T3, T4, T5> row, Row5<T1, T2, T3, T4, T5> value);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     * <p>
     * This is emulated using a subquery for the <code>value</code>, where row
     * value expressions aren't supported.
     */
    @Support({ H2, HSQLDB, POSTGRES })
    <T1, T2, T3, T4, T5, T6> UpdateFromStep<R> set(Row6<T1, T2, T3, T4, T5, T6> row, Row6<T1, T2, T3, T4, T5, T6> value);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     * <p>
     * This is emulated using a subquery for the <code>value</code>, where row
     * value expressions aren't supported.
     */
    @Support({ H2, HSQLDB, POSTGRES })
    <T1, T2, T3, T4, T5, T6, T7> UpdateFromStep<R> set(Row7<T1, T2, T3, T4, T5, T6, T7> row, Row7<T1, T2, T3, T4, T5, T6, T7> value);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     * <p>
     * This is emulated using a subquery for the <code>value</code>, where row
     * value expressions aren't supported.
     */
    @Support({ H2, HSQLDB, POSTGRES })
    <T1, T2, T3, T4, T5, T6, T7, T8> UpdateFromStep<R> set(Row8<T1, T2, T3, T4, T5, T6, T7, T8> row, Row8<T1, T2, T3, T4, T5, T6, T7, T8> value);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     * <p>
     * This is emulated using a subquery for the <code>value</code>, where row
     * value expressions aren't supported.
     */
    @Support({ H2, HSQLDB, POSTGRES })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9> UpdateFromStep<R> set(Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9> row, Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9> value);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     * <p>
     * This is emulated using a subquery for the <code>value</code>, where row
     * value expressions aren't supported.
     */
    @Support({ H2, HSQLDB, POSTGRES })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> UpdateFromStep<R> set(Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> row, Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> value);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     * <p>
     * This is emulated using a subquery for the <code>value</code>, where row
     * value expressions aren't supported.
     */
    @Support({ H2, HSQLDB, POSTGRES })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> UpdateFromStep<R> set(Row11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> row, Row11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> value);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     * <p>
     * This is emulated using a subquery for the <code>value</code>, where row
     * value expressions aren't supported.
     */
    @Support({ H2, HSQLDB, POSTGRES })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> UpdateFromStep<R> set(Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> row, Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> value);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     * <p>
     * This is emulated using a subquery for the <code>value</code>, where row
     * value expressions aren't supported.
     */
    @Support({ H2, HSQLDB, POSTGRES })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> UpdateFromStep<R> set(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> row, Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> value);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     * <p>
     * This is emulated using a subquery for the <code>value</code>, where row
     * value expressions aren't supported.
     */
    @Support({ H2, HSQLDB, POSTGRES })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> UpdateFromStep<R> set(Row14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> row, Row14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> value);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     * <p>
     * This is emulated using a subquery for the <code>value</code>, where row
     * value expressions aren't supported.
     */
    @Support({ H2, HSQLDB, POSTGRES })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> UpdateFromStep<R> set(Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> row, Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> value);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     * <p>
     * This is emulated using a subquery for the <code>value</code>, where row
     * value expressions aren't supported.
     */
    @Support({ H2, HSQLDB, POSTGRES })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> UpdateFromStep<R> set(Row16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> row, Row16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> value);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     * <p>
     * This is emulated using a subquery for the <code>value</code>, where row
     * value expressions aren't supported.
     */
    @Support({ H2, HSQLDB, POSTGRES })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> UpdateFromStep<R> set(Row17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> row, Row17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> value);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     * <p>
     * This is emulated using a subquery for the <code>value</code>, where row
     * value expressions aren't supported.
     */
    @Support({ H2, HSQLDB, POSTGRES })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> UpdateFromStep<R> set(Row18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> row, Row18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> value);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     * <p>
     * This is emulated using a subquery for the <code>value</code>, where row
     * value expressions aren't supported.
     */
    @Support({ H2, HSQLDB, POSTGRES })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> UpdateFromStep<R> set(Row19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> row, Row19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> value);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     * <p>
     * This is emulated using a subquery for the <code>value</code>, where row
     * value expressions aren't supported.
     */
    @Support({ H2, HSQLDB, POSTGRES })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> UpdateFromStep<R> set(Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> row, Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> value);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     * <p>
     * This is emulated using a subquery for the <code>value</code>, where row
     * value expressions aren't supported.
     */
    @Support({ H2, HSQLDB, POSTGRES })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> UpdateFromStep<R> set(Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> row, Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> value);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     * <p>
     * This is emulated using a subquery for the <code>value</code>, where row
     * value expressions aren't supported.
     */
    @Support({ H2, HSQLDB, POSTGRES })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> UpdateFromStep<R> set(Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> row, Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> value);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     * <p>
     * This is emulated using a subquery for the <code>value</code>, where row
     * value expressions aren't supported.
     */
    @Support({ H2, HSQLDB, POSTGRES })
    UpdateFromStep<R> set(RowN row, RowN value);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Support({ H2, HSQLDB, POSTGRES_9_5 })
    <T1> UpdateFromStep<R> set(Row1<T1> row, Select<? extends Record1<T1>> select);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Support({ H2, HSQLDB, POSTGRES_9_5 })
    <T1, T2> UpdateFromStep<R> set(Row2<T1, T2> row, Select<? extends Record2<T1, T2>> select);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Support({ H2, HSQLDB, POSTGRES_9_5 })
    <T1, T2, T3> UpdateFromStep<R> set(Row3<T1, T2, T3> row, Select<? extends Record3<T1, T2, T3>> select);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Support({ H2, HSQLDB, POSTGRES_9_5 })
    <T1, T2, T3, T4> UpdateFromStep<R> set(Row4<T1, T2, T3, T4> row, Select<? extends Record4<T1, T2, T3, T4>> select);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Support({ H2, HSQLDB, POSTGRES_9_5 })
    <T1, T2, T3, T4, T5> UpdateFromStep<R> set(Row5<T1, T2, T3, T4, T5> row, Select<? extends Record5<T1, T2, T3, T4, T5>> select);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Support({ H2, HSQLDB, POSTGRES_9_5 })
    <T1, T2, T3, T4, T5, T6> UpdateFromStep<R> set(Row6<T1, T2, T3, T4, T5, T6> row, Select<? extends Record6<T1, T2, T3, T4, T5, T6>> select);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Support({ H2, HSQLDB, POSTGRES_9_5 })
    <T1, T2, T3, T4, T5, T6, T7> UpdateFromStep<R> set(Row7<T1, T2, T3, T4, T5, T6, T7> row, Select<? extends Record7<T1, T2, T3, T4, T5, T6, T7>> select);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Support({ H2, HSQLDB, POSTGRES_9_5 })
    <T1, T2, T3, T4, T5, T6, T7, T8> UpdateFromStep<R> set(Row8<T1, T2, T3, T4, T5, T6, T7, T8> row, Select<? extends Record8<T1, T2, T3, T4, T5, T6, T7, T8>> select);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Support({ H2, HSQLDB, POSTGRES_9_5 })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9> UpdateFromStep<R> set(Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9> row, Select<? extends Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> select);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Support({ H2, HSQLDB, POSTGRES_9_5 })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> UpdateFromStep<R> set(Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> row, Select<? extends Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>> select);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Support({ H2, HSQLDB, POSTGRES_9_5 })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> UpdateFromStep<R> set(Row11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> row, Select<? extends Record11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>> select);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Support({ H2, HSQLDB, POSTGRES_9_5 })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> UpdateFromStep<R> set(Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> row, Select<? extends Record12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>> select);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Support({ H2, HSQLDB, POSTGRES_9_5 })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> UpdateFromStep<R> set(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> row, Select<? extends Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> select);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Support({ H2, HSQLDB, POSTGRES_9_5 })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> UpdateFromStep<R> set(Row14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> row, Select<? extends Record14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>> select);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Support({ H2, HSQLDB, POSTGRES_9_5 })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> UpdateFromStep<R> set(Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> row, Select<? extends Record15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>> select);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Support({ H2, HSQLDB, POSTGRES_9_5 })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> UpdateFromStep<R> set(Row16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> row, Select<? extends Record16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>> select);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Support({ H2, HSQLDB, POSTGRES_9_5 })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> UpdateFromStep<R> set(Row17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> row, Select<? extends Record17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>> select);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Support({ H2, HSQLDB, POSTGRES_9_5 })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> UpdateFromStep<R> set(Row18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> row, Select<? extends Record18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>> select);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Support({ H2, HSQLDB, POSTGRES_9_5 })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> UpdateFromStep<R> set(Row19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> row, Select<? extends Record19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>> select);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Support({ H2, HSQLDB, POSTGRES_9_5 })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> UpdateFromStep<R> set(Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> row, Select<? extends Record20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>> select);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Support({ H2, HSQLDB, POSTGRES_9_5 })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> UpdateFromStep<R> set(Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> row, Select<? extends Record21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>> select);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Support({ H2, HSQLDB, POSTGRES_9_5 })
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> UpdateFromStep<R> set(Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> row, Select<? extends Record22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>> select);

    /**
     * Specify a multi-column set clause for the <code>UPDATE</code> statement.
     */
    @Support({ H2, HSQLDB, POSTGRES_9_5 })
    UpdateFromStep<R> set(RowN row, Select<?> select);

}
