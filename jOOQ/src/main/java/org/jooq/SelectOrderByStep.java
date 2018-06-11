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

import static org.jooq.SQLDialect.CUBRID;
// ...
// ...

import java.util.Collection;

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
public interface SelectOrderByStep<R extends Record> extends SelectLimitStep<R> {

// [jooq-tools] START [order-by-orderfield-array]

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Support
    <T1> SelectSeekStep1<R, T1> orderBy(OrderField<T1> field1);

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Support
    <T1, T2> SelectSeekStep2<R, T1, T2> orderBy(OrderField<T1> field1, OrderField<T2> field2);

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Support
    <T1, T2, T3> SelectSeekStep3<R, T1, T2, T3> orderBy(OrderField<T1> field1, OrderField<T2> field2, OrderField<T3> field3);

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Support
    <T1, T2, T3, T4> SelectSeekStep4<R, T1, T2, T3, T4> orderBy(OrderField<T1> field1, OrderField<T2> field2, OrderField<T3> field3, OrderField<T4> field4);

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Support
    <T1, T2, T3, T4, T5> SelectSeekStep5<R, T1, T2, T3, T4, T5> orderBy(OrderField<T1> field1, OrderField<T2> field2, OrderField<T3> field3, OrderField<T4> field4, OrderField<T5> field5);

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Support
    <T1, T2, T3, T4, T5, T6> SelectSeekStep6<R, T1, T2, T3, T4, T5, T6> orderBy(OrderField<T1> field1, OrderField<T2> field2, OrderField<T3> field3, OrderField<T4> field4, OrderField<T5> field5, OrderField<T6> field6);

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Support
    <T1, T2, T3, T4, T5, T6, T7> SelectSeekStep7<R, T1, T2, T3, T4, T5, T6, T7> orderBy(OrderField<T1> field1, OrderField<T2> field2, OrderField<T3> field3, OrderField<T4> field4, OrderField<T5> field5, OrderField<T6> field6, OrderField<T7> field7);

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8> SelectSeekStep8<R, T1, T2, T3, T4, T5, T6, T7, T8> orderBy(OrderField<T1> field1, OrderField<T2> field2, OrderField<T3> field3, OrderField<T4> field4, OrderField<T5> field5, OrderField<T6> field6, OrderField<T7> field7, OrderField<T8> field8);

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9> SelectSeekStep9<R, T1, T2, T3, T4, T5, T6, T7, T8, T9> orderBy(OrderField<T1> field1, OrderField<T2> field2, OrderField<T3> field3, OrderField<T4> field4, OrderField<T5> field5, OrderField<T6> field6, OrderField<T7> field7, OrderField<T8> field8, OrderField<T9> field9);

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> SelectSeekStep10<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> orderBy(OrderField<T1> field1, OrderField<T2> field2, OrderField<T3> field3, OrderField<T4> field4, OrderField<T5> field5, OrderField<T6> field6, OrderField<T7> field7, OrderField<T8> field8, OrderField<T9> field9, OrderField<T10> field10);

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> SelectSeekStep11<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> orderBy(OrderField<T1> field1, OrderField<T2> field2, OrderField<T3> field3, OrderField<T4> field4, OrderField<T5> field5, OrderField<T6> field6, OrderField<T7> field7, OrderField<T8> field8, OrderField<T9> field9, OrderField<T10> field10, OrderField<T11> field11);

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> SelectSeekStep12<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> orderBy(OrderField<T1> field1, OrderField<T2> field2, OrderField<T3> field3, OrderField<T4> field4, OrderField<T5> field5, OrderField<T6> field6, OrderField<T7> field7, OrderField<T8> field8, OrderField<T9> field9, OrderField<T10> field10, OrderField<T11> field11, OrderField<T12> field12);

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> SelectSeekStep13<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> orderBy(OrderField<T1> field1, OrderField<T2> field2, OrderField<T3> field3, OrderField<T4> field4, OrderField<T5> field5, OrderField<T6> field6, OrderField<T7> field7, OrderField<T8> field8, OrderField<T9> field9, OrderField<T10> field10, OrderField<T11> field11, OrderField<T12> field12, OrderField<T13> field13);

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> SelectSeekStep14<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> orderBy(OrderField<T1> field1, OrderField<T2> field2, OrderField<T3> field3, OrderField<T4> field4, OrderField<T5> field5, OrderField<T6> field6, OrderField<T7> field7, OrderField<T8> field8, OrderField<T9> field9, OrderField<T10> field10, OrderField<T11> field11, OrderField<T12> field12, OrderField<T13> field13, OrderField<T14> field14);

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> SelectSeekStep15<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> orderBy(OrderField<T1> field1, OrderField<T2> field2, OrderField<T3> field3, OrderField<T4> field4, OrderField<T5> field5, OrderField<T6> field6, OrderField<T7> field7, OrderField<T8> field8, OrderField<T9> field9, OrderField<T10> field10, OrderField<T11> field11, OrderField<T12> field12, OrderField<T13> field13, OrderField<T14> field14, OrderField<T15> field15);

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> SelectSeekStep16<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> orderBy(OrderField<T1> field1, OrderField<T2> field2, OrderField<T3> field3, OrderField<T4> field4, OrderField<T5> field5, OrderField<T6> field6, OrderField<T7> field7, OrderField<T8> field8, OrderField<T9> field9, OrderField<T10> field10, OrderField<T11> field11, OrderField<T12> field12, OrderField<T13> field13, OrderField<T14> field14, OrderField<T15> field15, OrderField<T16> field16);

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> SelectSeekStep17<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> orderBy(OrderField<T1> field1, OrderField<T2> field2, OrderField<T3> field3, OrderField<T4> field4, OrderField<T5> field5, OrderField<T6> field6, OrderField<T7> field7, OrderField<T8> field8, OrderField<T9> field9, OrderField<T10> field10, OrderField<T11> field11, OrderField<T12> field12, OrderField<T13> field13, OrderField<T14> field14, OrderField<T15> field15, OrderField<T16> field16, OrderField<T17> field17);

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> SelectSeekStep18<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> orderBy(OrderField<T1> field1, OrderField<T2> field2, OrderField<T3> field3, OrderField<T4> field4, OrderField<T5> field5, OrderField<T6> field6, OrderField<T7> field7, OrderField<T8> field8, OrderField<T9> field9, OrderField<T10> field10, OrderField<T11> field11, OrderField<T12> field12, OrderField<T13> field13, OrderField<T14> field14, OrderField<T15> field15, OrderField<T16> field16, OrderField<T17> field17, OrderField<T18> field18);

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> SelectSeekStep19<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> orderBy(OrderField<T1> field1, OrderField<T2> field2, OrderField<T3> field3, OrderField<T4> field4, OrderField<T5> field5, OrderField<T6> field6, OrderField<T7> field7, OrderField<T8> field8, OrderField<T9> field9, OrderField<T10> field10, OrderField<T11> field11, OrderField<T12> field12, OrderField<T13> field13, OrderField<T14> field14, OrderField<T15> field15, OrderField<T16> field16, OrderField<T17> field17, OrderField<T18> field18, OrderField<T19> field19);

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> SelectSeekStep20<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> orderBy(OrderField<T1> field1, OrderField<T2> field2, OrderField<T3> field3, OrderField<T4> field4, OrderField<T5> field5, OrderField<T6> field6, OrderField<T7> field7, OrderField<T8> field8, OrderField<T9> field9, OrderField<T10> field10, OrderField<T11> field11, OrderField<T12> field12, OrderField<T13> field13, OrderField<T14> field14, OrderField<T15> field15, OrderField<T16> field16, OrderField<T17> field17, OrderField<T18> field18, OrderField<T19> field19, OrderField<T20> field20);

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> SelectSeekStep21<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> orderBy(OrderField<T1> field1, OrderField<T2> field2, OrderField<T3> field3, OrderField<T4> field4, OrderField<T5> field5, OrderField<T6> field6, OrderField<T7> field7, OrderField<T8> field8, OrderField<T9> field9, OrderField<T10> field10, OrderField<T11> field11, OrderField<T12> field12, OrderField<T13> field13, OrderField<T14> field14, OrderField<T15> field15, OrderField<T16> field16, OrderField<T17> field17, OrderField<T18> field18, OrderField<T19> field19, OrderField<T20> field20, OrderField<T21> field21);

    /**
     * Add an <code>ORDER BY</code> clause to the query.
     */
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> SelectSeekStep22<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> orderBy(OrderField<T1> field1, OrderField<T2> field2, OrderField<T3> field3, OrderField<T4> field4, OrderField<T5> field5, OrderField<T6> field6, OrderField<T7> field7, OrderField<T8> field8, OrderField<T9> field9, OrderField<T10> field10, OrderField<T11> field11, OrderField<T12> field12, OrderField<T13> field13, OrderField<T14> field14, OrderField<T15> field15, OrderField<T16> field16, OrderField<T17> field17, OrderField<T18> field18, OrderField<T19> field19, OrderField<T20> field20, OrderField<T21> field21, OrderField<T22> field22);

// [jooq-tools] END [order-by-orderfield-array]


    /**
     * Add an <code>ORDER BY</code> clause to the query
     */
    @Support
    SelectSeekStepN<R> orderBy(OrderField<?>... fields);

    /**
     * Add an <code>ORDER BY</code> clause to the query
     */
    @Support
    SelectSeekStepN<R> orderBy(Collection<? extends OrderField<?>> fields);

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
    SelectLimitStep<R> orderSiblingsBy(OrderField<?>... fields);

    /**
     * Add an <code>ORDER SIBLINGS BY</code> clause to the query
     * <p>
     * This clause can be used only along with Oracle's <code>CONNECT BY</code>
     * clause, to indicate that the hierarchical ordering should be preserved
     * and elements of each hierarchy should be ordered among themselves.
     */
    @Support({ CUBRID })
    SelectLimitStep<R> orderSiblingsBy(Collection<? extends OrderField<?>> fields);

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
