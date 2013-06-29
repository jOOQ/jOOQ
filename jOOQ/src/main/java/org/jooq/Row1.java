/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.jooq;

import org.jooq.Comparator;
import org.jooq.api.annotation.State;

import java.util.Collection;

import javax.annotation.Generated;

/**
 * A model type for a row value expression with degree <code>1</code>.
 * <p>
 * Note: Not all databases support row value expressions, but many row value
 * expression operations can be simulated on all databases. See relevant row
 * value expression method Javadocs for details.
 *
 * @author Lukas Eder
 */
@Generated("This class was generated using jOOQ-tools")
@State
public interface Row1<T1> extends Row {

    // ------------------------------------------------------------------------
    // Field accessors
    // ------------------------------------------------------------------------

    /**
     * Get the first field.
     */
    Field<T1> field1();

    // ------------------------------------------------------------------------
    // Generic comparison predicates
    // ------------------------------------------------------------------------
    
    /**
     * Compare this row value expression with another row value expression
     * using a dynamic comparator.
     * <p>
     * See the explicit comparison methods for details. Note, not all 
     * {@link Comparator} types are supported
     *
     * @see #equal(Row1)
     * @see #notEqual(Row1)
     * @see #lessThan(Row1)
     * @see #lessOrEqual(Row1)
     * @see #greaterThan(Row1)
     * @see #greaterOrEqual(Row1)
     */
    @Support
    Condition compare(Comparator comparator, Row1<T1> row);

    /**
     * Compare this row value expression with a record record
     * using a dynamic comparator.
     * <p>
     * See the explicit comparison methods for details. Note, not all 
     * {@link Comparator} types are supported
     *
     * @see #equal(Row1)
     * @see #notEqual(Row1)
     * @see #lessThan(Row1)
     * @see #lessOrEqual(Row1)
     * @see #greaterThan(Row1)
     * @see #greaterOrEqual(Row1)
     */
    @Support
    Condition compare(Comparator comparator, Record1<T1> record);

    /**
     * Compare this row value expression with another row value expression
     * using a dynamic comparator.
     * <p>
     * See the explicit comparison methods for details. Note, not all 
     * {@link Comparator} types are supported
     *
     * @see #equal(Row1)
     * @see #notEqual(Row1)
     * @see #lessThan(Row1)
     * @see #lessOrEqual(Row1)
     * @see #greaterThan(Row1)
     * @see #greaterOrEqual(Row1)
     */
    @Support
    Condition compare(Comparator comparator, T1 t1);

    /**
     * Compare this row value expression with another row value expression
     * using a dynamic comparator.
     * <p>
     * See the explicit comparison methods for details. Note, not all 
     * {@link Comparator} types are supported
     *
     * @see #equal(Row1)
     * @see #notEqual(Row1)
     * @see #lessThan(Row1)
     * @see #lessOrEqual(Row1)
     * @see #greaterThan(Row1)
     * @see #greaterOrEqual(Row1)
     */
    @Support
    Condition compare(Comparator comparator, Field<T1> t1);

    // ------------------------------------------------------------------------
    // Equal / Not equal comparison predicates
    // ------------------------------------------------------------------------

    /**
     * Compare this row value expression with another row value expression for
     * equality.
     * <p>
     * Row equality comparison predicates can be simulated in those databases
     * that do not support such predicates natively:
     * <code>(A, B) = (1, 2)</code> is equivalent to
     * <code>A = 1 AND B = 2</code>
     */
    @Support
    Condition equal(Row1<T1> row);

    /**
     * Compare this row value expression with a record for equality.
     *
     * @see #equal(Row1)
     */
    @Support
    Condition equal(Record1<T1> record);

    /**
     * Compare this row value expression with another row value expression for
     * equality.
     *
     * @see #equal(Row1)
     */
    @Support
    Condition equal(T1 t1);

    /**
     * Compare this row value expression with another row value expression for
     * equality.
     *
     * @see #equal(Row1)
     */
    @Support
    Condition equal(Field<T1> t1);

    /**
     * Compare this row value expression with a subselect for equality.
     *
     * @see #equal(Row1)
     */
    @Support
    Condition equal(Select<? extends Record1<T1>> select);

    /**
     * Compare this row value expression with another row value expression for
     * equality.
     *
     * @see #equal(Row1)
     */
    @Support
    Condition eq(Row1<T1> row);

    /**
     * Compare this row value expression with a record for equality.
     *
     * @see #equal(Row1)
     */
    @Support
    Condition eq(Record1<T1> record);

    /**
     * Compare this row value expression with another row value expression for
     * equality.
     *
     * @see #equal(Row1)
     */
    @Support
    Condition eq(T1 t1);

    /**
     * Compare this row value expression with another row value expression for
     * equality.
     *
     * @see #equal(Row1)
     */
    @Support
    Condition eq(Field<T1> t1);

    /**
     * Compare this row value expression with a subselect for equality.
     *
     * @see #equal(Row1)
     */
    @Support
    Condition eq(Select<? extends Record1<T1>> select);

    /**
     * Compare this row value expression with another row value expression for
     * non-equality.
     * <p>
     * Row non-equality comparison predicates can be simulated in those
     * databases that do not support such predicates natively:
     * <code>(A, B) <> (1, 2)</code> is equivalent to
     * <code>NOT(A = 1 AND B = 2)</code>
     */
    @Support
    Condition notEqual(Row1<T1> row);

    /**
     * Compare this row value expression with a record for non-equality
     *
     * @see #notEqual(Row1)
     */
    @Support
    Condition notEqual(Record1<T1> record);

    /**
     * Compare this row value expression with another row value expression for.
     * non-equality
     *
     * @see #notEqual(Row1)
     */
    @Support
    Condition notEqual(T1 t1);

    /**
     * Compare this row value expression with another row value expression for
     * non-equality.
     *
     * @see #notEqual(Row1)
     */
    @Support
    Condition notEqual(Field<T1> t1);

    /**
     * Compare this row value expression with a subselect for non-equality.
     *
     * @see #notEqual(Row1)
     */
    @Support
    Condition notEqual(Select<? extends Record1<T1>> select);

    /**
     * Compare this row value expression with another row value expression for
     * non-equality.
     *
     * @see #notEqual(Row1)
     */
    @Support
    Condition ne(Row1<T1> row);

    /**
     * Compare this row value expression with a record for non-equality.
     *
     * @see #notEqual(Row1)
     */
    @Support
    Condition ne(Record1<T1> record);

    /**
     * Compare this row value expression with another row value expression for
     * non-equality.
     *
     * @see #notEqual(Row1)
     */
    @Support
    Condition ne(T1 t1);

    /**
     * Compare this row value expression with another row value expression for
     * non-equality.
     *
     * @see #notEqual(Row1)
     */
    @Support
    Condition ne(Field<T1> t1);

    /**
     * Compare this row value expression with a subselect for non-equality.
     *
     * @see #notEqual(Row1)
     */
    @Support
    Condition ne(Select<? extends Record1<T1>> select);

    // ------------------------------------------------------------------------
    // Ordering comparison predicates
    // ------------------------------------------------------------------------

    /**
     * Compare this row value expression with another row value expression for
     * order.
     * <p>
     * Row order comparison predicates can be simulated in those
     * databases that do not support such predicates natively:
     * <code>(A, B, C) < (1, 2, 3)</code> is equivalent to
     * <code>A < 1 OR (A = 1 AND B < 2) OR (A = 1 AND B = 2 AND C < 3)</code>
     */
    @Support
    Condition lessThan(Row1<T1> row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #lessThan(Row1)
     */
    @Support
    Condition lessThan(Record1<T1> record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessThan(Row1)
     */
    @Support
    Condition lessThan(T1 t1);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessThan(Row1)
     */
    @Support
    Condition lessThan(Field<T1> t1);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #lessThan(Row1)
     */
    @Support
    Condition lessThan(Select<? extends Record1<T1>> select);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessThan(Row1)
     */
    @Support
    Condition lt(Row1<T1> row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #lessThan(Row1)
     */
    @Support
    Condition lt(Record1<T1> record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessThan(Row1)
     */
    @Support
    Condition lt(T1 t1);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessThan(Row1)
     */
    @Support
    Condition lt(Field<T1> t1);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #lessThan(Row1)
     */
    @Support
    Condition lt(Select<? extends Record1<T1>> select);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     * <p>
     * Row order comparison predicates can be simulated in those
     * databases that do not support such predicates natively:
     * <code>(A, B) <= (1, 2)</code> is equivalent to
     * <code>A < 1 OR (A = 1 AND B < 2) OR (A = 1 AND B = 2)</code>
     */
    @Support
    Condition lessOrEqual(Row1<T1> row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #lessOrEqual(Row1)
     */
    @Support
    Condition lessOrEqual(Record1<T1> record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessOrEqual(Row1)
     */
    @Support
    Condition lessOrEqual(T1 t1);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessOrEqual(Row1)
     */
    @Support
    Condition lessOrEqual(Field<T1> t1);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #lessOrEqual(Row1)
     */
    @Support
    Condition lessOrEqual(Select<? extends Record1<T1>> select);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessOrEqual(Row1)
     */
    @Support
    Condition le(Row1<T1> row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #lessOrEqual(Row1)
     */
    @Support
    Condition le(Record1<T1> record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessOrEqual(Row1)
     */
    @Support
    Condition le(T1 t1);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessOrEqual(Row1)
     */
    @Support
    Condition le(Field<T1> t1);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #lessOrEqual(Row1)
     */
    @Support
    Condition le(Select<? extends Record1<T1>> select);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     * <p>
     * Row order comparison predicates can be simulated in those
     * databases that do not support such predicates natively:
     * <code>(A, B, C) > (1, 2, 3)</code> is equivalent to
     * <code>A > 1 OR (A = 1 AND B > 2) OR (A = 1 AND B = 2 AND C > 3)</code>
     */
    @Support
    Condition greaterThan(Row1<T1> row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #greaterThan(Row1)
     */
    @Support
    Condition greaterThan(Record1<T1> record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterThan(Row1)
     */
    @Support
    Condition greaterThan(T1 t1);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterThan(Row1)
     */
    @Support
    Condition greaterThan(Field<T1> t1);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #greaterThan(Row1)
     */
    @Support
    Condition greaterThan(Select<? extends Record1<T1>> select);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterThan(Row1)
     */
    @Support
    Condition gt(Row1<T1> row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #greaterThan(Row1)
     */
    @Support
    Condition gt(Record1<T1> record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterThan(Row1)
     */
    @Support
    Condition gt(T1 t1);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterThan(Row1)
     */
    @Support
    Condition gt(Field<T1> t1);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #greaterThan(Row1)
     */
    @Support
    Condition gt(Select<? extends Record1<T1>> select);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     * <p>
     * Row order comparison predicates can be simulated in those
     * databases that do not support such predicates natively:
     * <code>(A, B) >= (1, 2)</code> is equivalent to
     * <code>A > 1 OR (A = 1 AND B > 2) OR (A = 1 AND B = 2)</code>
     */
    @Support
    Condition greaterOrEqual(Row1<T1> row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #greaterOrEqual(Row1)
     */
    @Support
    Condition greaterOrEqual(Record1<T1> record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterOrEqual(Row1)
     */
    @Support
    Condition greaterOrEqual(T1 t1);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterOrEqual(Row1)
     */
    @Support
    Condition greaterOrEqual(Field<T1> t1);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #greaterOrEqual(Row1)
     */
    @Support
    Condition greaterOrEqual(Select<? extends Record1<T1>> select);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterOrEqual(Row1)
     */
    @Support
    Condition ge(Row1<T1> row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #greaterOrEqual(Row1)
     */
    @Support
    Condition ge(Record1<T1> record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterOrEqual(Row1)
     */
    @Support
    Condition ge(T1 t1);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterOrEqual(Row1)
     */
    @Support
    Condition ge(Field<T1> t1);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #greaterOrEqual(Row1)
     */
    @Support
    Condition ge(Select<? extends Record1<T1>> select);

    // ------------------------------------------------------------------------
    // [NOT] BETWEEN predicates
    // ------------------------------------------------------------------------

    /**
     * Check if this row value expression is within a range of two other row
     * value expressions.
     *
     * @see #between(Row1, Row1)
     */
    @Support
    BetweenAndStep1<T1> between(T1 minValue1);

    /**
     * Check if this row value expression is within a range of two other row
     * value expressions.
     *
     * @see #between(Row1, Row1)
     */
    @Support
    BetweenAndStep1<T1> between(Field<T1> minValue1);

    /**
     * Check if this row value expression is within a range of two other row
     * value expressions.
     *
     * @see #between(Row1, Row1)
     */
    @Support
    BetweenAndStep1<T1> between(Row1<T1> minValue);

    /**
     * Check if this row value expression is within a range of two records.
     *
     * @see #between(Row1, Row1)
     */
    @Support
    BetweenAndStep1<T1> between(Record1<T1> minValue);

    /**
     * Check if this row value expression is within a range of two other row
     * value expressions.
     * <p>
     * This is the same as calling <code>between(minValue).and(maxValue)</code>
     * <p>
     * The expression <code>A BETWEEN B AND C</code> is equivalent to the
     * expression <code>A >= B AND A <= C</code> for those SQL dialects that do
     * not properly support the <code>BETWEEN</code> predicate for row value
     * expressions
     */
    @Support
    Condition between(Row1<T1> minValue,
                      Row1<T1> maxValue);

    /**
     * Check if this row value expression is within a range of two records.
     * <p>
     * This is the same as calling <code>between(minValue).and(maxValue)</code>
     *
     * @see #between(Row1, Row1)
     */
    @Support
    Condition between(Record1<T1> minValue,
                      Record1<T1> maxValue);

    /**
     * Check if this row value expression is within a symmetric range of two
     * other row value expressions.
     *
     * @see #betweenSymmetric(Row1, Row1)
     */
    @Support
    BetweenAndStep1<T1> betweenSymmetric(T1 minValue1);

    /**
     * Check if this row value expression is within a symmetric range of two
     * other row value expressions.
     *
     * @see #betweenSymmetric(Row1, Row1)
     */
    @Support
    BetweenAndStep1<T1> betweenSymmetric(Field<T1> minValue1);

    /**
     * Check if this row value expression is within a symmetric range of two
     * other row value expressions.
     *
     * @see #betweenSymmetric(Row1, Row1)
     */
    @Support
    BetweenAndStep1<T1> betweenSymmetric(Row1<T1> minValue);

    /**
     * Check if this row value expression is within a symmetric range of two
     * records.
     *
     * @see #betweenSymmetric(Row1, Row1)
     */
    @Support
    BetweenAndStep1<T1> betweenSymmetric(Record1<T1> minValue);

    /**
     * Check if this row value expression is within a symmetric range of two
     * other row value expressions.
     * <p>
     * This is the same as calling <code>betweenSymmetric(minValue).and(maxValue)</code>
     * <p>
     * The expression <code>A BETWEEN SYMMETRIC B AND C</code> is equivalent to
     * the expression <code>(A >= B AND A <= C) OR (A >= C AND A <= B)</code>
     * for those SQL dialects that do not properly support the
     * <code>BETWEEN</code> predicate for row value expressions
     */
    @Support
    Condition betweenSymmetric(Row1<T1> minValue,
                               Row1<T1> maxValue);

    /**
     * Check if this row value expression is within a symmetric range of two
     * records.
     * <p>
     * This is the same as calling <code>betweenSymmetric(minValue).and(maxValue)</code>
     *
     * @see #betweenSymmetric(Row1, Row1)
     */
    @Support
    Condition betweenSymmetric(Record1<T1> minValue,
                               Record1<T1> maxValue);

    /**
     * Check if this row value expression is not within a range of two other
     * row value expressions.
     *
     * @see #between(Row1, Row1)
     */
    @Support
    BetweenAndStep1<T1> notBetween(T1 minValue1);

    /**
     * Check if this row value expression is not within a range of two other
     * row value expressions.
     *
     * @see #notBetween(Row1, Row1)
     */
    @Support
    BetweenAndStep1<T1> notBetween(Field<T1> minValue1);

    /**
     * Check if this row value expression is not within a range of two other
     * row value expressions.
     *
     * @see #notBetween(Row1, Row1)
     */
    @Support
    BetweenAndStep1<T1> notBetween(Row1<T1> minValue);

    /**
     * Check if this row value expression is within a range of two records.
     *
     * @see #notBetween(Row1, Row1)
     */
    @Support
    BetweenAndStep1<T1> notBetween(Record1<T1> minValue);

    /**
     * Check if this row value expression is not within a range of two other
     * row value expressions.
     * <p>
     * This is the same as calling <code>notBetween(minValue).and(maxValue)</code>
     * <p>
     * The expression <code>A NOT BETWEEN B AND C</code> is equivalent to the
     * expression <code>A < B OR A > C</code> for those SQL dialects that do
     * not properly support the <code>BETWEEN</code> predicate for row value
     * expressions
     */
    @Support
    Condition notBetween(Row1<T1> minValue,
                         Row1<T1> maxValue);

    /**
     * Check if this row value expression is within a range of two records.
     * <p>
     * This is the same as calling <code>notBetween(minValue).and(maxValue)</code>
     *
     * @see #notBetween(Row1, Row1)
     */
    @Support
    Condition notBetween(Record1<T1> minValue,
                         Record1<T1> maxValue);

    /**
     * Check if this row value expression is not within a symmetric range of two
     * other row value expressions.
     *
     * @see #notBetweenSymmetric(Row1, Row1)
     */
    @Support
    BetweenAndStep1<T1> notBetweenSymmetric(T1 minValue1);

    /**
     * Check if this row value expression is not within a symmetric range of two
     * other row value expressions.
     *
     * @see #notBetweenSymmetric(Row1, Row1)
     */
    @Support
    BetweenAndStep1<T1> notBetweenSymmetric(Field<T1> minValue1);

    /**
     * Check if this row value expression is not within a symmetric range of two
     * other row value expressions.
     *
     * @see #notBetweenSymmetric(Row1, Row1)
     */
    @Support
    BetweenAndStep1<T1> notBetweenSymmetric(Row1<T1> minValue);

    /**
     * Check if this row value expression is not within a symmetric range of two
     * records.
     *
     * @see #notBetweenSymmetric(Row1, Row1)
     */
    @Support
    BetweenAndStep1<T1> notBetweenSymmetric(Record1<T1> minValue);

    /**
     * Check if this row value expression is not within a symmetric range of two
     * other row value expressions.
     * <p>
     * This is the same as calling <code>notBetweenSymmetric(minValue).and(maxValue)</code>
     * <p>
     * The expression <code>A NOT BETWEEN SYMMETRIC B AND C</code> is equivalent
     * to the expression <code>(A < B OR A > C) AND (A < C OR A > B)</code> for
     * those SQL dialects that do not properly support the <code>BETWEEN</code>
     * predicate for row value expressions
     */
    @Support
    Condition notBetweenSymmetric(Row1<T1> minValue,
                                  Row1<T1> maxValue);

    /**
     * Check if this row value expression is not within a symmetric range of two
     * records.
     * <p>
     * This is the same as calling <code>notBetweenSymmetric(minValue).and(maxValue)</code>
     *
     * @see #notBetweenSymmetric(Row1, Row1)
     */
    @Support
    Condition notBetweenSymmetric(Record1<T1> minValue,
                                  Record1<T1> maxValue);

    // ------------------------------------------------------------------------
    // [NOT] DISTINCT predicates
    // ------------------------------------------------------------------------


    // ------------------------------------------------------------------------
    // [NOT] IN predicates
    // ------------------------------------------------------------------------

    /**
     * Compare this row value expression with a set of row value expressions for
     * equality.
     * <p>
     * Row IN predicates can be simulated in those databases that do not support
     * such predicates natively: <code>(A, B) IN ((1, 2), (3, 4))</code> is
     * equivalent to <code>((A, B) = (1, 2)) OR ((A, B) = (3, 4))</code>, which
     * is equivalent to <code>(A = 1 AND B = 2) OR (A = 3 AND B = 4)</code>
     */
    @Support
    Condition in(Collection<? extends Row1<T1>> rows);

    /**
     * Compare this row value expression with a set of row value expressions for
     * equality.
     *
     * @see #in(Collection)
     */
    @Support
    Condition in(Row1<T1>... rows);

    /**
     * Compare this row value expression with a set of records for equality.
     *
     * @see #in(Collection)
     */
    @Support
    Condition in(Record1<T1>... record);

    /**
     * Compare this row value expression with a subselect for equality.
     *
     * @see #in(Collection)
     */
    @Support
    Condition in(Select<? extends Record1<T1>> select);

    /**
     * Compare this row value expression with a set of row value expressions for
     * equality.
     * <p>
     * Row NOT IN predicates can be simulated in those databases that do not
     * support such predicates natively:
     * <code>(A, B) NOT IN ((1, 2), (3, 4))</code> is equivalent to
     * <code>NOT(((A, B) = (1, 2)) OR ((A, B) = (3, 4)))</code>, which is
     * equivalent to <code>NOT((A = 1 AND B = 2) OR (A = 3 AND B = 4))</code>
     */
    @Support
    Condition notIn(Collection<? extends Row1<T1>> rows);

    /**
     * Compare this row value expression with a set of row value expressions for
     * equality.
     *
     * @see #notIn(Collection)
     */
    @Support
    Condition notIn(Row1<T1>... rows);

    /**
     * Compare this row value expression with a set of records for non-equality.
     *
     * @see #notIn(Collection)
     */
    @Support
    Condition notIn(Record1<T1>... record);

    /**
     * Compare this row value expression with a subselect for non-equality.
     *
     * @see #notIn(Collection)
     */
    @Support
    Condition notIn(Select<? extends Record1<T1>> select);

}
