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
 * A model type for a row value expression with degree <code>2</code>.
 * <p>
 * Note: Not all databases support row value expressions, but many row value
 * expression operations can be simulated on all databases. See relevant row
 * value expression method Javadocs for details.
 *
 * @author Lukas Eder
 */
@Generated("This class was generated using jOOQ-tools")
@State
public interface Row2<T1, T2> extends Row {

    // ------------------------------------------------------------------------
    // Field accessors
    // ------------------------------------------------------------------------

    /**
     * Get the first field.
     */
    Field<T1> field1();

    /**
     * Get the second field.
     */
    Field<T2> field2();

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
     * @see #equal(Row2)
     * @see #notEqual(Row2)
     * @see #lessThan(Row2)
     * @see #lessOrEqual(Row2)
     * @see #greaterThan(Row2)
     * @see #greaterOrEqual(Row2)
     */
    @Support
    Condition compare(Comparator comparator, Row2<T1, T2> row);

    /**
     * Compare this row value expression with a record record
     * using a dynamic comparator.
     * <p>
     * See the explicit comparison methods for details. Note, not all 
     * {@link Comparator} types are supported
     *
     * @see #equal(Row2)
     * @see #notEqual(Row2)
     * @see #lessThan(Row2)
     * @see #lessOrEqual(Row2)
     * @see #greaterThan(Row2)
     * @see #greaterOrEqual(Row2)
     */
    @Support
    Condition compare(Comparator comparator, Record2<T1, T2> record);

    /**
     * Compare this row value expression with another row value expression
     * using a dynamic comparator.
     * <p>
     * See the explicit comparison methods for details. Note, not all 
     * {@link Comparator} types are supported
     *
     * @see #equal(Row2)
     * @see #notEqual(Row2)
     * @see #lessThan(Row2)
     * @see #lessOrEqual(Row2)
     * @see #greaterThan(Row2)
     * @see #greaterOrEqual(Row2)
     */
    @Support
    Condition compare(Comparator comparator, T1 t1, T2 t2);

    /**
     * Compare this row value expression with another row value expression
     * using a dynamic comparator.
     * <p>
     * See the explicit comparison methods for details. Note, not all 
     * {@link Comparator} types are supported
     *
     * @see #equal(Row2)
     * @see #notEqual(Row2)
     * @see #lessThan(Row2)
     * @see #lessOrEqual(Row2)
     * @see #greaterThan(Row2)
     * @see #greaterOrEqual(Row2)
     */
    @Support
    Condition compare(Comparator comparator, Field<T1> t1, Field<T2> t2);

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
    Condition equal(Row2<T1, T2> row);

    /**
     * Compare this row value expression with a record for equality.
     *
     * @see #equal(Row2)
     */
    @Support
    Condition equal(Record2<T1, T2> record);

    /**
     * Compare this row value expression with another row value expression for
     * equality.
     *
     * @see #equal(Row2)
     */
    @Support
    Condition equal(T1 t1, T2 t2);

    /**
     * Compare this row value expression with another row value expression for
     * equality.
     *
     * @see #equal(Row2)
     */
    @Support
    Condition equal(Field<T1> t1, Field<T2> t2);

    /**
     * Compare this row value expression with a subselect for equality.
     *
     * @see #equal(Row2)
     */
    @Support
    Condition equal(Select<? extends Record2<T1, T2>> select);

    /**
     * Compare this row value expression with another row value expression for
     * equality.
     *
     * @see #equal(Row2)
     */
    @Support
    Condition eq(Row2<T1, T2> row);

    /**
     * Compare this row value expression with a record for equality.
     *
     * @see #equal(Row2)
     */
    @Support
    Condition eq(Record2<T1, T2> record);

    /**
     * Compare this row value expression with another row value expression for
     * equality.
     *
     * @see #equal(Row2)
     */
    @Support
    Condition eq(T1 t1, T2 t2);

    /**
     * Compare this row value expression with another row value expression for
     * equality.
     *
     * @see #equal(Row2)
     */
    @Support
    Condition eq(Field<T1> t1, Field<T2> t2);

    /**
     * Compare this row value expression with a subselect for equality.
     *
     * @see #equal(Row2)
     */
    @Support
    Condition eq(Select<? extends Record2<T1, T2>> select);

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
    Condition notEqual(Row2<T1, T2> row);

    /**
     * Compare this row value expression with a record for non-equality
     *
     * @see #notEqual(Row2)
     */
    @Support
    Condition notEqual(Record2<T1, T2> record);

    /**
     * Compare this row value expression with another row value expression for.
     * non-equality
     *
     * @see #notEqual(Row2)
     */
    @Support
    Condition notEqual(T1 t1, T2 t2);

    /**
     * Compare this row value expression with another row value expression for
     * non-equality.
     *
     * @see #notEqual(Row2)
     */
    @Support
    Condition notEqual(Field<T1> t1, Field<T2> t2);

    /**
     * Compare this row value expression with a subselect for non-equality.
     *
     * @see #notEqual(Row2)
     */
    @Support
    Condition notEqual(Select<? extends Record2<T1, T2>> select);

    /**
     * Compare this row value expression with another row value expression for
     * non-equality.
     *
     * @see #notEqual(Row2)
     */
    @Support
    Condition ne(Row2<T1, T2> row);

    /**
     * Compare this row value expression with a record for non-equality.
     *
     * @see #notEqual(Row2)
     */
    @Support
    Condition ne(Record2<T1, T2> record);

    /**
     * Compare this row value expression with another row value expression for
     * non-equality.
     *
     * @see #notEqual(Row2)
     */
    @Support
    Condition ne(T1 t1, T2 t2);

    /**
     * Compare this row value expression with another row value expression for
     * non-equality.
     *
     * @see #notEqual(Row2)
     */
    @Support
    Condition ne(Field<T1> t1, Field<T2> t2);

    /**
     * Compare this row value expression with a subselect for non-equality.
     *
     * @see #notEqual(Row2)
     */
    @Support
    Condition ne(Select<? extends Record2<T1, T2>> select);

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
    Condition lessThan(Row2<T1, T2> row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #lessThan(Row2)
     */
    @Support
    Condition lessThan(Record2<T1, T2> record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessThan(Row2)
     */
    @Support
    Condition lessThan(T1 t1, T2 t2);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessThan(Row2)
     */
    @Support
    Condition lessThan(Field<T1> t1, Field<T2> t2);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #lessThan(Row2)
     */
    @Support
    Condition lessThan(Select<? extends Record2<T1, T2>> select);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessThan(Row2)
     */
    @Support
    Condition lt(Row2<T1, T2> row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #lessThan(Row2)
     */
    @Support
    Condition lt(Record2<T1, T2> record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessThan(Row2)
     */
    @Support
    Condition lt(T1 t1, T2 t2);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessThan(Row2)
     */
    @Support
    Condition lt(Field<T1> t1, Field<T2> t2);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #lessThan(Row2)
     */
    @Support
    Condition lt(Select<? extends Record2<T1, T2>> select);

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
    Condition lessOrEqual(Row2<T1, T2> row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #lessOrEqual(Row2)
     */
    @Support
    Condition lessOrEqual(Record2<T1, T2> record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessOrEqual(Row2)
     */
    @Support
    Condition lessOrEqual(T1 t1, T2 t2);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessOrEqual(Row2)
     */
    @Support
    Condition lessOrEqual(Field<T1> t1, Field<T2> t2);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #lessOrEqual(Row2)
     */
    @Support
    Condition lessOrEqual(Select<? extends Record2<T1, T2>> select);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessOrEqual(Row2)
     */
    @Support
    Condition le(Row2<T1, T2> row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #lessOrEqual(Row2)
     */
    @Support
    Condition le(Record2<T1, T2> record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessOrEqual(Row2)
     */
    @Support
    Condition le(T1 t1, T2 t2);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #lessOrEqual(Row2)
     */
    @Support
    Condition le(Field<T1> t1, Field<T2> t2);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #lessOrEqual(Row2)
     */
    @Support
    Condition le(Select<? extends Record2<T1, T2>> select);

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
    Condition greaterThan(Row2<T1, T2> row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #greaterThan(Row2)
     */
    @Support
    Condition greaterThan(Record2<T1, T2> record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterThan(Row2)
     */
    @Support
    Condition greaterThan(T1 t1, T2 t2);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterThan(Row2)
     */
    @Support
    Condition greaterThan(Field<T1> t1, Field<T2> t2);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #greaterThan(Row2)
     */
    @Support
    Condition greaterThan(Select<? extends Record2<T1, T2>> select);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterThan(Row2)
     */
    @Support
    Condition gt(Row2<T1, T2> row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #greaterThan(Row2)
     */
    @Support
    Condition gt(Record2<T1, T2> record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterThan(Row2)
     */
    @Support
    Condition gt(T1 t1, T2 t2);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterThan(Row2)
     */
    @Support
    Condition gt(Field<T1> t1, Field<T2> t2);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #greaterThan(Row2)
     */
    @Support
    Condition gt(Select<? extends Record2<T1, T2>> select);

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
    Condition greaterOrEqual(Row2<T1, T2> row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #greaterOrEqual(Row2)
     */
    @Support
    Condition greaterOrEqual(Record2<T1, T2> record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterOrEqual(Row2)
     */
    @Support
    Condition greaterOrEqual(T1 t1, T2 t2);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterOrEqual(Row2)
     */
    @Support
    Condition greaterOrEqual(Field<T1> t1, Field<T2> t2);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #greaterOrEqual(Row2)
     */
    @Support
    Condition greaterOrEqual(Select<? extends Record2<T1, T2>> select);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterOrEqual(Row2)
     */
    @Support
    Condition ge(Row2<T1, T2> row);

    /**
     * Compare this row value expression with a record for order.
     *
     * @see #greaterOrEqual(Row2)
     */
    @Support
    Condition ge(Record2<T1, T2> record);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterOrEqual(Row2)
     */
    @Support
    Condition ge(T1 t1, T2 t2);

    /**
     * Compare this row value expression with another row value expression for
     * order.
     *
     * @see #greaterOrEqual(Row2)
     */
    @Support
    Condition ge(Field<T1> t1, Field<T2> t2);

    /**
     * Compare this row value expression with a subselect for order.
     *
     * @see #greaterOrEqual(Row2)
     */
    @Support
    Condition ge(Select<? extends Record2<T1, T2>> select);

    // ------------------------------------------------------------------------
    // [NOT] BETWEEN predicates
    // ------------------------------------------------------------------------

    /**
     * Check if this row value expression is within a range of two other row
     * value expressions.
     *
     * @see #between(Row2, Row2)
     */
    @Support
    BetweenAndStep2<T1, T2> between(T1 minValue1, T2 minValue2);

    /**
     * Check if this row value expression is within a range of two other row
     * value expressions.
     *
     * @see #between(Row2, Row2)
     */
    @Support
    BetweenAndStep2<T1, T2> between(Field<T1> minValue1, Field<T2> minValue2);

    /**
     * Check if this row value expression is within a range of two other row
     * value expressions.
     *
     * @see #between(Row2, Row2)
     */
    @Support
    BetweenAndStep2<T1, T2> between(Row2<T1, T2> minValue);

    /**
     * Check if this row value expression is within a range of two records.
     *
     * @see #between(Row2, Row2)
     */
    @Support
    BetweenAndStep2<T1, T2> between(Record2<T1, T2> minValue);

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
    Condition between(Row2<T1, T2> minValue,
                      Row2<T1, T2> maxValue);

    /**
     * Check if this row value expression is within a range of two records.
     * <p>
     * This is the same as calling <code>between(minValue).and(maxValue)</code>
     *
     * @see #between(Row2, Row2)
     */
    @Support
    Condition between(Record2<T1, T2> minValue,
                      Record2<T1, T2> maxValue);

    /**
     * Check if this row value expression is within a symmetric range of two
     * other row value expressions.
     *
     * @see #betweenSymmetric(Row2, Row2)
     */
    @Support
    BetweenAndStep2<T1, T2> betweenSymmetric(T1 minValue1, T2 minValue2);

    /**
     * Check if this row value expression is within a symmetric range of two
     * other row value expressions.
     *
     * @see #betweenSymmetric(Row2, Row2)
     */
    @Support
    BetweenAndStep2<T1, T2> betweenSymmetric(Field<T1> minValue1, Field<T2> minValue2);

    /**
     * Check if this row value expression is within a symmetric range of two
     * other row value expressions.
     *
     * @see #betweenSymmetric(Row2, Row2)
     */
    @Support
    BetweenAndStep2<T1, T2> betweenSymmetric(Row2<T1, T2> minValue);

    /**
     * Check if this row value expression is within a symmetric range of two
     * records.
     *
     * @see #betweenSymmetric(Row2, Row2)
     */
    @Support
    BetweenAndStep2<T1, T2> betweenSymmetric(Record2<T1, T2> minValue);

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
    Condition betweenSymmetric(Row2<T1, T2> minValue,
                               Row2<T1, T2> maxValue);

    /**
     * Check if this row value expression is within a symmetric range of two
     * records.
     * <p>
     * This is the same as calling <code>betweenSymmetric(minValue).and(maxValue)</code>
     *
     * @see #betweenSymmetric(Row2, Row2)
     */
    @Support
    Condition betweenSymmetric(Record2<T1, T2> minValue,
                               Record2<T1, T2> maxValue);

    /**
     * Check if this row value expression is not within a range of two other
     * row value expressions.
     *
     * @see #between(Row2, Row2)
     */
    @Support
    BetweenAndStep2<T1, T2> notBetween(T1 minValue1, T2 minValue2);

    /**
     * Check if this row value expression is not within a range of two other
     * row value expressions.
     *
     * @see #notBetween(Row2, Row2)
     */
    @Support
    BetweenAndStep2<T1, T2> notBetween(Field<T1> minValue1, Field<T2> minValue2);

    /**
     * Check if this row value expression is not within a range of two other
     * row value expressions.
     *
     * @see #notBetween(Row2, Row2)
     */
    @Support
    BetweenAndStep2<T1, T2> notBetween(Row2<T1, T2> minValue);

    /**
     * Check if this row value expression is within a range of two records.
     *
     * @see #notBetween(Row2, Row2)
     */
    @Support
    BetweenAndStep2<T1, T2> notBetween(Record2<T1, T2> minValue);

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
    Condition notBetween(Row2<T1, T2> minValue,
                         Row2<T1, T2> maxValue);

    /**
     * Check if this row value expression is within a range of two records.
     * <p>
     * This is the same as calling <code>notBetween(minValue).and(maxValue)</code>
     *
     * @see #notBetween(Row2, Row2)
     */
    @Support
    Condition notBetween(Record2<T1, T2> minValue,
                         Record2<T1, T2> maxValue);

    /**
     * Check if this row value expression is not within a symmetric range of two
     * other row value expressions.
     *
     * @see #notBetweenSymmetric(Row2, Row2)
     */
    @Support
    BetweenAndStep2<T1, T2> notBetweenSymmetric(T1 minValue1, T2 minValue2);

    /**
     * Check if this row value expression is not within a symmetric range of two
     * other row value expressions.
     *
     * @see #notBetweenSymmetric(Row2, Row2)
     */
    @Support
    BetweenAndStep2<T1, T2> notBetweenSymmetric(Field<T1> minValue1, Field<T2> minValue2);

    /**
     * Check if this row value expression is not within a symmetric range of two
     * other row value expressions.
     *
     * @see #notBetweenSymmetric(Row2, Row2)
     */
    @Support
    BetweenAndStep2<T1, T2> notBetweenSymmetric(Row2<T1, T2> minValue);

    /**
     * Check if this row value expression is not within a symmetric range of two
     * records.
     *
     * @see #notBetweenSymmetric(Row2, Row2)
     */
    @Support
    BetweenAndStep2<T1, T2> notBetweenSymmetric(Record2<T1, T2> minValue);

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
    Condition notBetweenSymmetric(Row2<T1, T2> minValue,
                                  Row2<T1, T2> maxValue);

    /**
     * Check if this row value expression is not within a symmetric range of two
     * records.
     * <p>
     * This is the same as calling <code>notBetweenSymmetric(minValue).and(maxValue)</code>
     *
     * @see #notBetweenSymmetric(Row2, Row2)
     */
    @Support
    Condition notBetweenSymmetric(Record2<T1, T2> minValue,
                                  Record2<T1, T2> maxValue);

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
    Condition in(Collection<? extends Row2<T1, T2>> rows);

    /**
     * Compare this row value expression with a set of row value expressions for
     * equality.
     *
     * @see #in(Collection)
     */
    @Support
    Condition in(Row2<T1, T2>... rows);

    /**
     * Compare this row value expression with a set of records for equality.
     *
     * @see #in(Collection)
     */
    @Support
    Condition in(Record2<T1, T2>... record);

    /**
     * Compare this row value expression with a subselect for equality.
     *
     * @see #in(Collection)
     */
    @Support
    Condition in(Select<? extends Record2<T1, T2>> select);

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
    Condition notIn(Collection<? extends Row2<T1, T2>> rows);

    /**
     * Compare this row value expression with a set of row value expressions for
     * equality.
     *
     * @see #notIn(Collection)
     */
    @Support
    Condition notIn(Row2<T1, T2>... rows);

    /**
     * Compare this row value expression with a set of records for non-equality.
     *
     * @see #notIn(Collection)
     */
    @Support
    Condition notIn(Record2<T1, T2>... record);

    /**
     * Compare this row value expression with a subselect for non-equality.
     *
     * @see #notIn(Collection)
     */
    @Support
    Condition notIn(Select<? extends Record2<T1, T2>> select);

    // ------------------------------------------------------------------------
    // Row2-specific OVERLAPS predicate
    // ------------------------------------------------------------------------

    /**
     * Check if this row value expression overlaps another row value expression.
     * <p>
     * The SQL standard specifies a temporal <code>OVERLAPS</code> predicate,
     * which comes in two flavours:
     * <ul>
     * <li><code>(DATE, DATE) OVERLAPS (DATE, DATE)</code></li>
     * <li><code>(DATE, INTERVAL) OVERLAPS (DATE, INTERVAL)</code></li>
     * </ul>
     * <p>
     * jOOQ also supports arbitrary 2-degree row value expression comparisons,
     * by simulating them as such <code><pre>
     * -- This predicate
     * (A, B) OVERLAPS (C, D)
     *
     * -- can be simulated as such
     * (C &lt;= B) AND (A &lt;= D)
     * </pre></code>
     */
    @Support
    Condition overlaps(T1 t1, T2 t2);

    /**
     * Check if this row value expression overlaps another row value expression.
     * <p>
     * The SQL standard specifies a temporal <code>OVERLAPS</code> predicate,
     * which comes in two flavours:
     * <ul>
     * <li><code>(DATE, DATE) OVERLAPS (DATE, DATE)</code></li>
     * <li><code>(DATE, INTERVAL) OVERLAPS (DATE, INTERVAL)</code></li>
     * </ul>
     * <p>
     * jOOQ also supports arbitrary 2-degree row value expression comparisons,
     * by simulating them as such <code><pre>
     * -- This predicate
     * (A, B) OVERLAPS (C, D)
     *
     * -- can be simulated as such
     * (C &lt;= B) AND (A &lt;= D)
     * </pre></code>
     */
    @Support
    Condition overlaps(Field<T1> t1, Field<T2> t2);

    /**
     * Check if this row value expression overlaps another row value expression.
     * <p>
     * The SQL standard specifies a temporal <code>OVERLAPS</code> predicate,
     * which comes in two flavours:
     * <ul>
     * <li><code>(DATE, DATE) OVERLAPS (DATE, DATE)</code></li>
     * <li><code>(DATE, INTERVAL) OVERLAPS (DATE, INTERVAL)</code></li>
     * </ul>
     * <p>
     * jOOQ also supports arbitrary 2-degree row value expression comparisons,
     * by simulating them as such <code><pre>
     * -- This predicate
     * (A, B) OVERLAPS (C, D)
     *
     * -- can be simulated as such
     * (C &lt;= B) AND (A &lt;= D)
     * </pre></code>
     */
    @Support
    Condition overlaps(Row2<T1, T2> row);

}
