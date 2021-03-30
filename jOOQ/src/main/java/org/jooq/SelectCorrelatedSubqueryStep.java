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

import org.jetbrains.annotations.*;


import org.jooq.conf.Settings;

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
 *      AND T_BOOK.PUBLISHED &gt; '2008-01-01'
 * GROUP BY T_AUTHOR.FIRST_NAME, T_AUTHOR.LAST_NAME
 *   HAVING COUNT(*) &gt; 5
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
public interface SelectCorrelatedSubqueryStep<R extends Record> extends SelectFinalStep<R> {

    /**
     * Compare this subquery with a record using a dynamic comparator.
     * <p>
     * See the explicit comparison methods for details. Note, not all {@link
     * Comparator} types are supported
     *
     * @see #eq(Record)
     * @see #ne(Record)
     * @see #lt(Record)
     * @see #le(Record)
     * @see #gt(Record)
     * @see #ge(Record)
     */
    @NotNull @CheckReturnValue
    @Support
    Condition compare(Comparator comparator, R record);

    /**
     * Compare this subquery with a subquery using a dynamic comparator.
     * <p>
     * See the explicit comparison methods for details. Note, not all {@link
     * Comparator} types are supported
     *
     * @see #eq(Record)
     * @see #ne(Record)
     * @see #lt(Record)
     * @see #le(Record)
     * @see #gt(Record)
     * @see #ge(Record)
     */
    @NotNull @CheckReturnValue
    @Support
    Condition compare(Comparator comparator, Select<? extends R> select);

    /**
     * Compare this subquery with a quantified subquery using a dynamic
     * comparator.
     * <p>
     * See the explicit comparison methods for details. Note, not all {@link
     * Comparator} types are supported
     *
     * @see #eq(Record)
     * @see #ne(Record)
     * @see #lt(Record)
     * @see #le(Record)
     * @see #gt(Record)
     * @see #ge(Record)
     */
    @NotNull @CheckReturnValue
    @Support
    Condition compare(Comparator comparator, QuantifiedSelect<? extends R> select);

    /**
     * Compare this subquery with a record for equality.
     */
    @NotNull @CheckReturnValue
    @Support
    Condition eq(R record);

    /**
     * Compare this subquery with another record for equality.
     */
    @NotNull @CheckReturnValue
    @Support
    Condition eq(Select<? extends R> select);

    /**
     * Compare this subquery with a quanitified subquery for equality.
     */
    @NotNull @CheckReturnValue
    @Support
    Condition eq(QuantifiedSelect<? extends R> select);

    /**
     * Compare this subquery with a record for equality.
     */
    @NotNull @CheckReturnValue
    @Support
    Condition equal(R record);

    /**
     * Compare this subquery with another record for equality.
     */
    @NotNull @CheckReturnValue
    @Support
    Condition equal(Select<? extends R> select);

    /**
     * Compare this subquery with a quanitified subquery for equality.
     */
    @NotNull @CheckReturnValue
    @Support
    Condition equal(QuantifiedSelect<? extends R> select);

    /**
     * Compare this subquery with a record for non-equality.
     */
    @NotNull @CheckReturnValue
    @Support
    Condition ne(R record);

    /**
     * Compare this subquery with another record for non-equality.
     */
    @NotNull @CheckReturnValue
    @Support
    Condition ne(Select<? extends R> select);

    /**
     * Compare this subquery with a quanitified subquery for non-equality.
     */
    @NotNull @CheckReturnValue
    @Support
    Condition ne(QuantifiedSelect<? extends R> select);

    /**
     * Compare this subquery with a record for non-equality.
     */
    @NotNull @CheckReturnValue
    @Support
    Condition notEqual(R record);

    /**
     * Compare this subquery with another record for non-equality.
     */
    @NotNull @CheckReturnValue
    @Support
    Condition notEqual(Select<? extends R> select);

    /**
     * Compare this subquery with a quanitified subquery for non-equality.
     */
    @NotNull @CheckReturnValue
    @Support
    Condition notEqual(QuantifiedSelect<? extends R> select);

    /**
     * Compare this subquery with a record for order.
     */
    @NotNull @CheckReturnValue
    @Support
    Condition lt(R record);

    /**
     * Compare this subquery with another record for order.
     */
    @NotNull @CheckReturnValue
    @Support
    Condition lt(Select<? extends R> select);

    /**
     * Compare this subquery with a quanitified subquery for order.
     */
    @NotNull @CheckReturnValue
    @Support
    Condition lt(QuantifiedSelect<? extends R> select);

    /**
     * Compare this subquery with a record for order.
     */
    @NotNull @CheckReturnValue
    @Support
    Condition lessThan(R record);

    /**
     * Compare this subquery with another record for order.
     */
    @NotNull @CheckReturnValue
    @Support
    Condition lessThan(Select<? extends R> select);

    /**
     * Compare this subquery with a quanitified subquery for order.
     */
    @NotNull @CheckReturnValue
    @Support
    Condition lessThan(QuantifiedSelect<? extends R> select);

    /**
     * Compare this subquery with a record for order.
     */
    @NotNull @CheckReturnValue
    @Support
    Condition le(R record);

    /**
     * Compare this subquery with another record for order.
     */
    @NotNull @CheckReturnValue
    @Support
    Condition le(Select<? extends R> select);

    /**
     * Compare this subquery with a quanitified subquery for order.
     */
    @NotNull @CheckReturnValue
    @Support
    Condition le(QuantifiedSelect<? extends R> select);

    /**
     * Compare this subquery with a record for order.
     */
    @NotNull @CheckReturnValue
    @Support
    Condition lessOrEqual(R record);

    /**
     * Compare this subquery with another record for order.
     */
    @NotNull @CheckReturnValue
    @Support
    Condition lessOrEqual(Select<? extends R> select);

    /**
     * Compare this subquery with a quanitified subquery for order.
     */
    @NotNull @CheckReturnValue
    @Support
    Condition lessOrEqual(QuantifiedSelect<? extends R> select);

    /**
     * Compare this subquery with a record for order.
     */
    @NotNull @CheckReturnValue
    @Support
    Condition gt(R record);

    /**
     * Compare this subquery with another record for order.
     */
    @NotNull @CheckReturnValue
    @Support
    Condition gt(Select<? extends R> select);

    /**
     * Compare this subquery with a quanitified subquery for order.
     */
    @NotNull @CheckReturnValue
    @Support
    Condition gt(QuantifiedSelect<? extends R> select);

    /**
     * Compare this subquery with a record for order.
     */
    @NotNull @CheckReturnValue
    @Support
    Condition greaterThan(R record);

    /**
     * Compare this subquery with another record for order.
     */
    @NotNull @CheckReturnValue
    @Support
    Condition greaterThan(Select<? extends R> select);

    /**
     * Compare this subquery with a quanitified subquery for order.
     */
    @NotNull @CheckReturnValue
    @Support
    Condition greaterThan(QuantifiedSelect<? extends R> select);

    /**
     * Compare this subquery with a record for order.
     */
    @NotNull @CheckReturnValue
    @Support
    Condition ge(R record);

    /**
     * Compare this subquery with another record for order.
     */
    @NotNull @CheckReturnValue
    @Support
    Condition ge(Select<? extends R> select);

    /**
     * Compare this subquery with a quanitified subquery for order.
     */
    @NotNull @CheckReturnValue
    @Support
    Condition ge(QuantifiedSelect<? extends R> select);

    /**
     * Compare this subquery with a record for order.
     */
    @NotNull @CheckReturnValue
    @Support
    Condition greaterOrEqual(R record);

    /**
     * Compare this subquery with another record for order.
     */
    @NotNull @CheckReturnValue
    @Support
    Condition greaterOrEqual(Select<? extends R> select);

    /**
     * Compare this subquery with a quanitified subquery for order.
     */
    @NotNull @CheckReturnValue
    @Support
    Condition greaterOrEqual(QuantifiedSelect<? extends R> select);

    /**
     * Compare this subquery with a set of records for equality.
     * <p>
     * Note that generating dynamic SQL with arbitrary-length
     * <code>IN</code> predicates can cause cursor cache contention in some
     * databases that use unique SQL strings as a statement identifier (e.g.
     * {@link SQLDialect#ORACLE}). In order to prevent such problems, you could
     * use {@link Settings#isInListPadding()} to produce less distinct SQL
     * strings (see also
     * <a href="https://github.com/jOOQ/jOOQ/issues/5600">[#5600]</a>), or you
     * could avoid <code>IN</code> lists, and replace them with:
     * <ul>
     * <li><code>IN</code> predicates on temporary tables</li>
     * <li><code>IN</code> predicates on unnested array bind variables</li>
     * </ul>
     */
    @NotNull @CheckReturnValue
    @Support
    Condition in(R... records);

    /**
     * Compare this subquery with another subquery for equality.
     */
    @NotNull @CheckReturnValue
    @Support
    Condition in(Select<? extends R> select);

    /**
     * Compare this subquery with a set of records for non-equality.
     * <p>
     * Note that generating dynamic SQL with arbitrary-length
     * <code>IN</code> predicates can cause cursor cache contention in some
     * databases that use unique SQL strings as a statement identifier (e.g.
     * {@link SQLDialect#ORACLE}). In order to prevent such problems, you could
     * use {@link Settings#isInListPadding()} to produce less distinct SQL
     * strings (see also
     * <a href="https://github.com/jOOQ/jOOQ/issues/5600">[#5600]</a>), or you
     * could avoid <code>IN</code> lists, and replace them with:
     * <ul>
     * <li><code>IN</code> predicates on temporary tables</li>
     * <li><code>IN</code> predicates on unnested array bind variables</li>
     * </ul>
     */
    @NotNull @CheckReturnValue
    @Support
    Condition notIn(R... records);

    /**
     * Compare this subquery with another subquery for non-equality.
     */
    @NotNull @CheckReturnValue
    @Support
    Condition notIn(Select<? extends R> select);

    /**
     * Compare this subquery with another record for distinctness.
     */
    @NotNull @CheckReturnValue
    @Support
    Condition isDistinctFrom(R record);

    /**
     * Compare this subquery with another record for distinctness.
     */
    @NotNull @CheckReturnValue
    @Support
    Condition isDistinctFrom(Select<? extends R> select);

    /**
     * Compare this subquery with another record for distinctness.
     */
    @NotNull @CheckReturnValue
    @Support
    Condition isDistinctFrom(QuantifiedSelect<? extends R> select);

    /**
     * Compare this subquery with another record for distinctness.
     */
    @NotNull @CheckReturnValue
    @Support
    Condition isNotDistinctFrom(R record);

    /**
     * Compare this subquery with another record for distinctness.
     */
    @NotNull @CheckReturnValue
    @Support
    Condition isNotDistinctFrom(Select<? extends R> select);

    /**
     * Compare this subquery with another record for distinctness.
     */
    @NotNull @CheckReturnValue
    @Support
    Condition isNotDistinctFrom(QuantifiedSelect<? extends R> select);

    /**
     * Check if this subquery is within a range of two records.
     * <p>
     * This is the same as calling <code>between(minValue).and(maxValue)</code>
     */
    @NotNull @CheckReturnValue
    @Support
    BetweenAndStep<R> between(R minValue);

    /**
     * Check if this subquery is within a range of two records.
     * <p>
     * This is the same as calling <code>between(minValue).and(maxValue)</code>
     */
    @NotNull @CheckReturnValue
    @Support
    Condition between(R minValue, R maxValue);

    /**
     * Check if this subquery is within a range of two subqueries.
     * <p>
     * This is the same as calling <code>between(minValue).and(maxValue)</code>
     */
    @NotNull @CheckReturnValue
    @Support
    BetweenAndStep<R> between(Select<? extends R> minValue);

    /**
     * Check if this subquery is within a range of two subqueries.
     * <p>
     * This is the same as calling <code>between(minValue).and(maxValue)</code>
     */
    @NotNull @CheckReturnValue
    @Support
    Condition between(Select<? extends R> minValue, Select<? extends R> maxValue);

    /**
     * Check if this subquery is within a symmetric range of two records.
     */
    @NotNull @CheckReturnValue
    @Support
    BetweenAndStepR<R> betweenSymmetric(R minValue);

    /**
     * Check if this subquery is within a symmetric range of two records.
     * <p>
     * This is the same as calling <code>between(minValue).and(maxValue)</code>
     */
    @NotNull @CheckReturnValue
    @Support
    Condition betweenSymmetric(R minValue, R maxValue);

    /**
     * Check if this subquery is within a symmetric range of two subqueries.
     */
    @NotNull @CheckReturnValue
    @Support
    BetweenAndStepR<R> betweenSymmetric(Select<? extends R> minValue);

    /**
     * Check if this subquery is within a symmetric range of two subqueries.
     * <p>
     * This is the same as calling <code>between(minValue).and(maxValue)</code>
     */
    @NotNull @CheckReturnValue
    @Support
    Condition betweenSymmetric(Select<? extends R> minValue, Select<? extends R> maxValue);

    /**
     * Check if this subquery is not within a range of two records.
     */
    @NotNull @CheckReturnValue
    @Support
    BetweenAndStepR<R> notBetween(R minValue);

    /**
     * Check if this subquery is not within a range of two records.
     * <p>
     * This is the same as calling <code>between(minValue).and(maxValue)</code>
     */
    @NotNull @CheckReturnValue
    @Support
    Condition notBetween(R minValue, R maxValue);

    /**
     * Check if this subquery is not within a range of two subqueries.
     */
    @NotNull @CheckReturnValue
    @Support
    BetweenAndStepR<R> notBetween(Select<? extends R> minValue);

    /**
     * Check if this subquery is not within a range of two subqueries.
     * <p>
     * This is the same as calling <code>between(minValue).and(maxValue)</code>
     */
    @NotNull @CheckReturnValue
    @Support
    Condition notBetween(Select<? extends R> minValue, Select<? extends R> maxValue);

    /**
     * Check if this subquery is not within a symmetric range of two records.
     */
    @NotNull @CheckReturnValue
    @Support
    BetweenAndStepR<R> notBetweenSymmetric(R minValue);

    /**
     * Check if this subquery is not within a symmetric range of two records.
     * <p>
     * This is the same as calling <code>between(minValue).and(maxValue)</code>
     */
    @NotNull @CheckReturnValue
    @Support
    Condition notBetweenSymmetric(R minValue, R maxValue);

    /**
     * Check if this subquery is not within a symmetric range of two subqueries.
     */
    @NotNull @CheckReturnValue
    @Support
    BetweenAndStepR<R> notBetweenSymmetric(Select<? extends R> minValue);

    /**
     * Check if this subquery is not within a symmetric range of two subqueries.
     * <p>
     * This is the same as calling <code>between(minValue).and(maxValue)</code>
     */
    @NotNull @CheckReturnValue
    @Support
    Condition notBetweenSymmetric(Select<? extends R> minValue, Select<? extends R> maxValue);

    /**
     * Check if the result of this subquery <code>IS NULL</code>
     */
    @NotNull @CheckReturnValue
    @Support
    Condition isNull();

    /**
     * Check if the result of this subquery <code>IS NOT NULL</code>
     */
    @NotNull @CheckReturnValue
    @Support
    Condition isNotNull();
}
