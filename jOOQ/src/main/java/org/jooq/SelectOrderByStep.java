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
// ...

import java.util.Collection;

import javax.annotation.Generated;

/**
 * This type is used for the {@link Select}'s DSL API when selecting generic
 * {@link Record} types.
 * <p>
 * Example: <code><pre>
 * -- get all authors' first and last names, and the number
 * -- of books they've written in German, if they have written
 * -- more than five books in German in the last three years
 * -- (from 2011), and sort those authors by last names
 * -- limiting results to the second and third row
 *
 *   SELECT T_AUTHOR.FIRST_NAME, T_AUTHOR.LAST_NAME, COUNT(*)
 *     FROM T_AUTHOR
 *     JOIN T_BOOK ON T_AUTHOR.ID = T_BOOK.AUTHOR_ID
 *    WHERE T_BOOK.LANGUAGE = 'DE'
 *      AND T_BOOK.PUBLISHED > '2008-01-01'
 * GROUP BY T_AUTHOR.FIRST_NAME, T_AUTHOR.LAST_NAME
 *   HAVING COUNT(*) > 5
 * ORDER BY T_AUTHOR.LAST_NAME ASC NULLS FIRST
 *    LIMIT 2
 *   OFFSET 1
 *      FOR UPDATE
 *       OF FIRST_NAME, LAST_NAME
 *       NO WAIT
 * </pre></code> Its equivalent in jOOQ <code><pre>
 * create.select(TAuthor.FIRST_NAME, TAuthor.LAST_NAME, create.count())
 *       .from(T_AUTHOR)
 *       .join(T_BOOK).on(TBook.AUTHOR_ID.equal(TAuthor.ID))
 *       .where(TBook.LANGUAGE.equal("DE"))
 *       .and(TBook.PUBLISHED.greaterThan(parseDate('2008-01-01')))
 *       .groupBy(TAuthor.FIRST_NAME, TAuthor.LAST_NAME)
 *       .having(create.count().greaterThan(5))
 *       .orderBy(TAuthor.LAST_NAME.asc().nullsFirst())
 *       .limit(2)
 *       .offset(1)
 *       .forUpdate()
 *       .of(TAuthor.FIRST_NAME, TAuthor.LAST_NAME)
 *       .noWait();
 * </pre></code> Refer to the manual for more details
 *
 * @author Lukas Eder
 */
public interface SelectOrderByStep<R extends Record> extends SelectLimitStep<R> {

// [jooq-tools] START [order-by-field-array]

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1> SelectSeekStep1<R, T1> orderBy(Field<T1> field1);

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2> SelectSeekStep2<R, T1, T2> orderBy(Field<T1> field1, Field<T2> field2);

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3> SelectSeekStep3<R, T1, T2, T3> orderBy(Field<T1> field1, Field<T2> field2, Field<T3> field3);

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4> SelectSeekStep4<R, T1, T2, T3, T4> orderBy(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4);

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5> SelectSeekStep5<R, T1, T2, T3, T4, T5> orderBy(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5);

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6> SelectSeekStep6<R, T1, T2, T3, T4, T5, T6> orderBy(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6);

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7> SelectSeekStep7<R, T1, T2, T3, T4, T5, T6, T7> orderBy(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7);

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8> SelectSeekStep8<R, T1, T2, T3, T4, T5, T6, T7, T8> orderBy(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8);

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9> SelectSeekStep9<R, T1, T2, T3, T4, T5, T6, T7, T8, T9> orderBy(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9);

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> SelectSeekStep10<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> orderBy(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10);

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> SelectSeekStep11<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> orderBy(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11);

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> SelectSeekStep12<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> orderBy(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12);

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> SelectSeekStep13<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> orderBy(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13);

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> SelectSeekStep14<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> orderBy(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14);

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> SelectSeekStep15<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> orderBy(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15);

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> SelectSeekStep16<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> orderBy(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16);

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> SelectSeekStep17<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> orderBy(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17);

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> SelectSeekStep18<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> orderBy(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18);

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> SelectSeekStep19<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> orderBy(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19);

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> SelectSeekStep20<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> orderBy(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20);

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> SelectSeekStep21<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> orderBy(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21);

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> SelectSeekStep22<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> orderBy(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21, Field<T22> field22);

// [jooq-tools] END [order-by-field-array]

    /**
     * Add an <code>ORDER BY</code> clause to the query
     */
    @Support
    SelectSeekStepN<R> orderBy(Field<?>... fields);

// [jooq-tools] START [order-by-sortfield-array]

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1> SelectSeekStep1<R, T1> orderBy(SortField<T1> field1);

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2> SelectSeekStep2<R, T1, T2> orderBy(SortField<T1> field1, SortField<T2> field2);

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3> SelectSeekStep3<R, T1, T2, T3> orderBy(SortField<T1> field1, SortField<T2> field2, SortField<T3> field3);

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4> SelectSeekStep4<R, T1, T2, T3, T4> orderBy(SortField<T1> field1, SortField<T2> field2, SortField<T3> field3, SortField<T4> field4);

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5> SelectSeekStep5<R, T1, T2, T3, T4, T5> orderBy(SortField<T1> field1, SortField<T2> field2, SortField<T3> field3, SortField<T4> field4, SortField<T5> field5);

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6> SelectSeekStep6<R, T1, T2, T3, T4, T5, T6> orderBy(SortField<T1> field1, SortField<T2> field2, SortField<T3> field3, SortField<T4> field4, SortField<T5> field5, SortField<T6> field6);

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7> SelectSeekStep7<R, T1, T2, T3, T4, T5, T6, T7> orderBy(SortField<T1> field1, SortField<T2> field2, SortField<T3> field3, SortField<T4> field4, SortField<T5> field5, SortField<T6> field6, SortField<T7> field7);

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8> SelectSeekStep8<R, T1, T2, T3, T4, T5, T6, T7, T8> orderBy(SortField<T1> field1, SortField<T2> field2, SortField<T3> field3, SortField<T4> field4, SortField<T5> field5, SortField<T6> field6, SortField<T7> field7, SortField<T8> field8);

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9> SelectSeekStep9<R, T1, T2, T3, T4, T5, T6, T7, T8, T9> orderBy(SortField<T1> field1, SortField<T2> field2, SortField<T3> field3, SortField<T4> field4, SortField<T5> field5, SortField<T6> field6, SortField<T7> field7, SortField<T8> field8, SortField<T9> field9);

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> SelectSeekStep10<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> orderBy(SortField<T1> field1, SortField<T2> field2, SortField<T3> field3, SortField<T4> field4, SortField<T5> field5, SortField<T6> field6, SortField<T7> field7, SortField<T8> field8, SortField<T9> field9, SortField<T10> field10);

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> SelectSeekStep11<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> orderBy(SortField<T1> field1, SortField<T2> field2, SortField<T3> field3, SortField<T4> field4, SortField<T5> field5, SortField<T6> field6, SortField<T7> field7, SortField<T8> field8, SortField<T9> field9, SortField<T10> field10, SortField<T11> field11);

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> SelectSeekStep12<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> orderBy(SortField<T1> field1, SortField<T2> field2, SortField<T3> field3, SortField<T4> field4, SortField<T5> field5, SortField<T6> field6, SortField<T7> field7, SortField<T8> field8, SortField<T9> field9, SortField<T10> field10, SortField<T11> field11, SortField<T12> field12);

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> SelectSeekStep13<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> orderBy(SortField<T1> field1, SortField<T2> field2, SortField<T3> field3, SortField<T4> field4, SortField<T5> field5, SortField<T6> field6, SortField<T7> field7, SortField<T8> field8, SortField<T9> field9, SortField<T10> field10, SortField<T11> field11, SortField<T12> field12, SortField<T13> field13);

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> SelectSeekStep14<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> orderBy(SortField<T1> field1, SortField<T2> field2, SortField<T3> field3, SortField<T4> field4, SortField<T5> field5, SortField<T6> field6, SortField<T7> field7, SortField<T8> field8, SortField<T9> field9, SortField<T10> field10, SortField<T11> field11, SortField<T12> field12, SortField<T13> field13, SortField<T14> field14);

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> SelectSeekStep15<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> orderBy(SortField<T1> field1, SortField<T2> field2, SortField<T3> field3, SortField<T4> field4, SortField<T5> field5, SortField<T6> field6, SortField<T7> field7, SortField<T8> field8, SortField<T9> field9, SortField<T10> field10, SortField<T11> field11, SortField<T12> field12, SortField<T13> field13, SortField<T14> field14, SortField<T15> field15);

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> SelectSeekStep16<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> orderBy(SortField<T1> field1, SortField<T2> field2, SortField<T3> field3, SortField<T4> field4, SortField<T5> field5, SortField<T6> field6, SortField<T7> field7, SortField<T8> field8, SortField<T9> field9, SortField<T10> field10, SortField<T11> field11, SortField<T12> field12, SortField<T13> field13, SortField<T14> field14, SortField<T15> field15, SortField<T16> field16);

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> SelectSeekStep17<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> orderBy(SortField<T1> field1, SortField<T2> field2, SortField<T3> field3, SortField<T4> field4, SortField<T5> field5, SortField<T6> field6, SortField<T7> field7, SortField<T8> field8, SortField<T9> field9, SortField<T10> field10, SortField<T11> field11, SortField<T12> field12, SortField<T13> field13, SortField<T14> field14, SortField<T15> field15, SortField<T16> field16, SortField<T17> field17);

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> SelectSeekStep18<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> orderBy(SortField<T1> field1, SortField<T2> field2, SortField<T3> field3, SortField<T4> field4, SortField<T5> field5, SortField<T6> field6, SortField<T7> field7, SortField<T8> field8, SortField<T9> field9, SortField<T10> field10, SortField<T11> field11, SortField<T12> field12, SortField<T13> field13, SortField<T14> field14, SortField<T15> field15, SortField<T16> field16, SortField<T17> field17, SortField<T18> field18);

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> SelectSeekStep19<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> orderBy(SortField<T1> field1, SortField<T2> field2, SortField<T3> field3, SortField<T4> field4, SortField<T5> field5, SortField<T6> field6, SortField<T7> field7, SortField<T8> field8, SortField<T9> field9, SortField<T10> field10, SortField<T11> field11, SortField<T12> field12, SortField<T13> field13, SortField<T14> field14, SortField<T15> field15, SortField<T16> field16, SortField<T17> field17, SortField<T18> field18, SortField<T19> field19);

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> SelectSeekStep20<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> orderBy(SortField<T1> field1, SortField<T2> field2, SortField<T3> field3, SortField<T4> field4, SortField<T5> field5, SortField<T6> field6, SortField<T7> field7, SortField<T8> field8, SortField<T9> field9, SortField<T10> field10, SortField<T11> field11, SortField<T12> field12, SortField<T13> field13, SortField<T14> field14, SortField<T15> field15, SortField<T16> field16, SortField<T17> field17, SortField<T18> field18, SortField<T19> field19, SortField<T20> field20);

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> SelectSeekStep21<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> orderBy(SortField<T1> field1, SortField<T2> field2, SortField<T3> field3, SortField<T4> field4, SortField<T5> field5, SortField<T6> field6, SortField<T7> field7, SortField<T8> field8, SortField<T9> field9, SortField<T10> field10, SortField<T11> field11, SortField<T12> field12, SortField<T13> field13, SortField<T14> field14, SortField<T15> field15, SortField<T16> field16, SortField<T17> field17, SortField<T18> field18, SortField<T19> field19, SortField<T20> field20, SortField<T21> field21);

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> SelectSeekStep22<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> orderBy(SortField<T1> field1, SortField<T2> field2, SortField<T3> field3, SortField<T4> field4, SortField<T5> field5, SortField<T6> field6, SortField<T7> field7, SortField<T8> field8, SortField<T9> field9, SortField<T10> field10, SortField<T11> field11, SortField<T12> field12, SortField<T13> field13, SortField<T14> field14, SortField<T15> field15, SortField<T16> field16, SortField<T17> field17, SortField<T18> field18, SortField<T19> field19, SortField<T20> field20, SortField<T21> field21, SortField<T22> field22);

// [jooq-tools] END [order-by-sortfield-array]


    /**
     * Add an <code>ORDER BY</code> clause to the query
     */
    @Support
    SelectSeekStepN<R> orderBy(SortField<?>... fields);

    /**
     * Add an <code>ORDER BY</code> clause to the query
     */
    @Support
    SelectSeekStepN<R> orderBy(Collection<? extends SortField<?>> fields);

    /**
     * Add an <code>ORDER BY</code> clause to the query
     * <p>
     * Indexes start at <code>1</code> in SQL!
     * <p>
     * Note, you can use <code>orderBy(DSL.val(1).desc())</code> or
     * <code>orderBy(DSL.literal(1).desc())</code> to apply descending
     * ordering
     */
    @Support
    SelectLimitStep<R> orderBy(int... fieldIndexes);

    /**
     * Add an <code>ORDER SIBLINGS BY</code> clause to the query
     * <p>
     * This clause can be used only along with Oracle's <code>CONNECT BY</code>
     * clause, to indicate that the hierarchical ordering should be preserved
     * and elements of each hierarchy should be ordered among themselves.
     */
    @Support({ CUBRID })
    SelectLimitStep<R> orderSiblingsBy(Field<?>... fields);

    /**
     * Add an <code>ORDER SIBLINGS BY</code> clause to the query
     * <p>
     * This clause can be used only along with Oracle's <code>CONNECT BY</code>
     * clause, to indicate that the hierarchical ordering should be preserved
     * and elements of each hierarchy should be ordered among themselves.
     */
    @Support({ CUBRID })
    SelectLimitStep<R> orderSiblingsBy(SortField<?>... fields);

    /**
     * Add an <code>ORDER SIBLINGS BY</code> clause to the query
     * <p>
     * This clause can be used only along with Oracle's <code>CONNECT BY</code>
     * clause, to indicate that the hierarchical ordering should be preserved
     * and elements of each hierarchy should be ordered among themselves.
     */
    @Support({ CUBRID })
    SelectLimitStep<R> orderSiblingsBy(Collection<? extends SortField<?>> fields);

    /**
     * Add an <code>ORDER SIBLINGS BY</code> clause to the query
     * <p>
     * This clause can be used only along with Oracle's <code>CONNECT BY</code>
     * clause, to indicate that the hierarchical ordering should be preserved
     * and elements of each hierarchy should be ordered among themselves.
     * <p>
     * Indexes start at <code>1</code> in SQL!
     * <p>
     * Note, you can use <code>orderSiblingsBy(DSL.val(1).desc())</code> or
     * <code>orderBy(DSL.literal(1).desc())</code> to apply descending
     * ordering
     */
    @Support({ CUBRID })
    SelectLimitStep<R> orderSiblingsBy(int... fieldIndexes);
}
