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
package org.jooq.xtend

import org.jooq.Constants

/**
 * @author Lukas Eder
 */
class Rows extends Generators {

    def static void main(String[] args) {
        val rows = new Rows();
        rows.generateRowClasses();
        rows.generateRowImpl();
    }

    def generateRowClasses() {
        for (degree : (0..Constants::MAX_ROW_DEGREE)) {
            val out = new StringBuilder();
            
            val typeSuffix = typeSuffix(degree)
            val typeSuffixRaw = typeSuffixRaw(degree)
            val recTypeSuffix = recTypeSuffix(degree)

            out.append('''
            «classHeader»
            package org.jooq;

            import org.jooq.Comparator;
            import org.jooq.api.annotation.State;
            import org.jooq.api.annotation.Transition;

            import java.util.Collection;

            import javax.annotation.Generated;

            /**
             * A model type for a row value expression with degree <code>«IF degree > 0»«degree»«ELSE»N > «Constants::MAX_ROW_DEGREE»«ENDIF»</code>.
             * <p>
             * Note: Not all databases support row value expressions, but many row value
             * expression operations can be simulated on all databases. See relevant row
             * value expression method Javadocs for details.
             *
             * @author Lukas Eder
             */
            «generatedAnnotation»
            @State
            public interface Row«typeSuffix» extends Row {
            «IF degree > 0»
            
                // ------------------------------------------------------------------------
                // Field accessors
                // ------------------------------------------------------------------------
                «FOR d : (1..degree)»

                /**
                 * Get the «first(d)» field.
                 */
                Field<T«d»> field«d»();
                «ENDFOR»
            «ENDIF»
            
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
                 * @see #equal(Row«typeSuffixRaw»)
                 * @see #notEqual(Row«typeSuffixRaw»)
                 * @see #lessThan(Row«typeSuffixRaw»)
                 * @see #lessOrEqual(Row«typeSuffixRaw»)
                 * @see #greaterThan(Row«typeSuffixRaw»)
                 * @see #greaterOrEqual(Row«typeSuffixRaw»)
                 */
                @Support
                Condition compare(Comparator comparator, Row«typeSuffix» row);
            
                /**
                 * Compare this row value expression with a record record
                 * using a dynamic comparator.
                 * <p>
                 * See the explicit comparison methods for details. Note, not all 
                 * {@link Comparator} types are supported
                 *
                 * @see #equal(Row«typeSuffixRaw»)
                 * @see #notEqual(Row«typeSuffixRaw»)
                 * @see #lessThan(Row«typeSuffixRaw»)
                 * @see #lessOrEqual(Row«typeSuffixRaw»)
                 * @see #greaterThan(Row«typeSuffixRaw»)
                 * @see #greaterOrEqual(Row«typeSuffixRaw»)
                 */
                @Support
                Condition compare(Comparator comparator, Record«recTypeSuffix» record);
            
                /**
                 * Compare this row value expression with another row value expression
                 * using a dynamic comparator.
                 * <p>
                 * See the explicit comparison methods for details. Note, not all 
                 * {@link Comparator} types are supported
                 *
                 * @see #equal(Row«typeSuffixRaw»)
                 * @see #notEqual(Row«typeSuffixRaw»)
                 * @see #lessThan(Row«typeSuffixRaw»)
                 * @see #lessOrEqual(Row«typeSuffixRaw»)
                 * @see #greaterThan(Row«typeSuffixRaw»)
                 * @see #greaterOrEqual(Row«typeSuffixRaw»)
                 */
                @Support
                Condition compare(Comparator comparator, «TN_tn(degree)»);
            
                /**
                 * Compare this row value expression with another row value expression
                 * using a dynamic comparator.
                 * <p>
                 * See the explicit comparison methods for details. Note, not all 
                 * {@link Comparator} types are supported
                 *
                 * @see #equal(Row«typeSuffixRaw»)
                 * @see #notEqual(Row«typeSuffixRaw»)
                 * @see #lessThan(Row«typeSuffixRaw»)
                 * @see #lessOrEqual(Row«typeSuffixRaw»)
                 * @see #greaterThan(Row«typeSuffixRaw»)
                 * @see #greaterOrEqual(Row«typeSuffixRaw»)
                 */
                @Support
                Condition compare(Comparator comparator, «Field_TN_tn(degree)»);
            
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
                Condition equal(Row«typeSuffix» row);

                /**
                 * Compare this row value expression with a record for equality.
                 *
                 * @see #equal(Row«typeSuffixRaw»)
                 */
                @Support
                Condition equal(Record«recTypeSuffix» record);

                /**
                 * Compare this row value expression with another row value expression for
                 * equality.
                 *
                 * @see #equal(Row«typeSuffixRaw»)
                 */
                @Support
                Condition equal(«TN_tn(degree)»);

                /**
                 * Compare this row value expression with another row value expression for
                 * equality.
                 *
                 * @see #equal(Row«typeSuffixRaw»)
                 */
                @Support
                Condition equal(«Field_TN_tn(degree)»);

                /**
                 * Compare this row value expression with a subselect for equality.
                 *
                 * @see #equal(Row«typeSuffixRaw»)
                 */
                @Support
                Condition equal(Select<? extends Record«recTypeSuffix»> select);

                /**
                 * Compare this row value expression with another row value expression for
                 * equality.
                 *
                 * @see #equal(Row«typeSuffixRaw»)
                 */
                @Support
                Condition eq(Row«typeSuffix» row);

                /**
                 * Compare this row value expression with a record for equality.
                 *
                 * @see #equal(Row«typeSuffixRaw»)
                 */
                @Support
                Condition eq(Record«recTypeSuffix» record);

                /**
                 * Compare this row value expression with another row value expression for
                 * equality.
                 *
                 * @see #equal(Row«typeSuffixRaw»)
                 */
                @Support
                Condition eq(«TN_tn(degree)»);

                /**
                 * Compare this row value expression with another row value expression for
                 * equality.
                 *
                 * @see #equal(Row«typeSuffixRaw»)
                 */
                @Support
                Condition eq(«Field_TN_tn(degree)»);

                /**
                 * Compare this row value expression with a subselect for equality.
                 *
                 * @see #equal(Row«typeSuffixRaw»)
                 */
                @Support
                Condition eq(Select<? extends Record«recTypeSuffix»> select);

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
                Condition notEqual(Row«typeSuffix» row);

                /**
                 * Compare this row value expression with a record for non-equality
                 *
                 * @see #notEqual(Row«typeSuffixRaw»)
                 */
                @Support
                Condition notEqual(Record«recTypeSuffix» record);

                /**
                 * Compare this row value expression with another row value expression for.
                 * non-equality
                 *
                 * @see #notEqual(Row«typeSuffixRaw»)
                 */
                @Support
                Condition notEqual(«TN_tn(degree)»);

                /**
                 * Compare this row value expression with another row value expression for
                 * non-equality.
                 *
                 * @see #notEqual(Row«typeSuffixRaw»)
                 */
                @Support
                Condition notEqual(«Field_TN_tn(degree)»);

                /**
                 * Compare this row value expression with a subselect for non-equality.
                 *
                 * @see #notEqual(Row«typeSuffixRaw»)
                 */
                @Support
                Condition notEqual(Select<? extends Record«recTypeSuffix»> select);

                /**
                 * Compare this row value expression with another row value expression for
                 * non-equality.
                 *
                 * @see #notEqual(Row«typeSuffixRaw»)
                 */
                @Support
                Condition ne(Row«typeSuffix» row);

                /**
                 * Compare this row value expression with a record for non-equality.
                 *
                 * @see #notEqual(Row«typeSuffixRaw»)
                 */
                @Support
                Condition ne(Record«recTypeSuffix» record);

                /**
                 * Compare this row value expression with another row value expression for
                 * non-equality.
                 *
                 * @see #notEqual(Row«typeSuffixRaw»)
                 */
                @Support
                Condition ne(«TN_tn(degree)»);

                /**
                 * Compare this row value expression with another row value expression for
                 * non-equality.
                 *
                 * @see #notEqual(Row«typeSuffixRaw»)
                 */
                @Support
                Condition ne(«Field_TN_tn(degree)»);

                /**
                 * Compare this row value expression with a subselect for non-equality.
                 *
                 * @see #notEqual(Row«typeSuffixRaw»)
                 */
                @Support
                Condition ne(Select<? extends Record«recTypeSuffix»> select);

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
                Condition lessThan(Row«typeSuffix» row);

                /**
                 * Compare this row value expression with a record for order.
                 *
                 * @see #lessThan(Row«typeSuffixRaw»)
                 */
                @Support
                Condition lessThan(Record«recTypeSuffix» record);

                /**
                 * Compare this row value expression with another row value expression for
                 * order.
                 *
                 * @see #lessThan(Row«typeSuffixRaw»)
                 */
                @Support
                Condition lessThan(«TN_tn(degree)»);

                /**
                 * Compare this row value expression with another row value expression for
                 * order.
                 *
                 * @see #lessThan(Row«typeSuffixRaw»)
                 */
                @Support
                Condition lessThan(«Field_TN_tn(degree)»);

                /**
                 * Compare this row value expression with a subselect for order.
                 *
                 * @see #lessThan(Row«typeSuffixRaw»)
                 */
                @Support
                Condition lessThan(Select<? extends Record«recTypeSuffix»> select);

                /**
                 * Compare this row value expression with another row value expression for
                 * order.
                 *
                 * @see #lessThan(Row«typeSuffixRaw»)
                 */
                @Support
                Condition lt(Row«typeSuffix» row);

                /**
                 * Compare this row value expression with a record for order.
                 *
                 * @see #lessThan(Row«typeSuffixRaw»)
                 */
                @Support
                Condition lt(Record«recTypeSuffix» record);

                /**
                 * Compare this row value expression with another row value expression for
                 * order.
                 *
                 * @see #lessThan(Row«typeSuffixRaw»)
                 */
                @Support
                Condition lt(«TN_tn(degree)»);

                /**
                 * Compare this row value expression with another row value expression for
                 * order.
                 *
                 * @see #lessThan(Row«typeSuffixRaw»)
                 */
                @Support
                Condition lt(«Field_TN_tn(degree)»);

                /**
                 * Compare this row value expression with a subselect for order.
                 *
                 * @see #lessThan(Row«typeSuffixRaw»)
                 */
                @Support
                Condition lt(Select<? extends Record«recTypeSuffix»> select);

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
                Condition lessOrEqual(Row«typeSuffix» row);

                /**
                 * Compare this row value expression with a record for order.
                 *
                 * @see #lessOrEqual(Row«typeSuffixRaw»)
                 */
                @Support
                Condition lessOrEqual(Record«recTypeSuffix» record);

                /**
                 * Compare this row value expression with another row value expression for
                 * order.
                 *
                 * @see #lessOrEqual(Row«typeSuffixRaw»)
                 */
                @Support
                Condition lessOrEqual(«TN_tn(degree)»);

                /**
                 * Compare this row value expression with another row value expression for
                 * order.
                 *
                 * @see #lessOrEqual(Row«typeSuffixRaw»)
                 */
                @Support
                Condition lessOrEqual(«Field_TN_tn(degree)»);

                /**
                 * Compare this row value expression with a subselect for order.
                 *
                 * @see #lessOrEqual(Row«typeSuffixRaw»)
                 */
                @Support
                Condition lessOrEqual(Select<? extends Record«recTypeSuffix»> select);

                /**
                 * Compare this row value expression with another row value expression for
                 * order.
                 *
                 * @see #lessOrEqual(Row«typeSuffixRaw»)
                 */
                @Support
                Condition le(Row«typeSuffix» row);

                /**
                 * Compare this row value expression with a record for order.
                 *
                 * @see #lessOrEqual(Row«typeSuffixRaw»)
                 */
                @Support
                Condition le(Record«recTypeSuffix» record);

                /**
                 * Compare this row value expression with another row value expression for
                 * order.
                 *
                 * @see #lessOrEqual(Row«typeSuffixRaw»)
                 */
                @Support
                Condition le(«TN_tn(degree)»);

                /**
                 * Compare this row value expression with another row value expression for
                 * order.
                 *
                 * @see #lessOrEqual(Row«typeSuffixRaw»)
                 */
                @Support
                Condition le(«Field_TN_tn(degree)»);

                /**
                 * Compare this row value expression with a subselect for order.
                 *
                 * @see #lessOrEqual(Row«typeSuffixRaw»)
                 */
                @Support
                Condition le(Select<? extends Record«recTypeSuffix»> select);

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
                Condition greaterThan(Row«typeSuffix» row);

                /**
                 * Compare this row value expression with a record for order.
                 *
                 * @see #greaterThan(Row«typeSuffixRaw»)
                 */
                @Support
                Condition greaterThan(Record«recTypeSuffix» record);

                /**
                 * Compare this row value expression with another row value expression for
                 * order.
                 *
                 * @see #greaterThan(Row«typeSuffixRaw»)
                 */
                @Support
                Condition greaterThan(«TN_tn(degree)»);

                /**
                 * Compare this row value expression with another row value expression for
                 * order.
                 *
                 * @see #greaterThan(Row«typeSuffixRaw»)
                 */
                @Support
                Condition greaterThan(«Field_TN_tn(degree)»);

                /**
                 * Compare this row value expression with a subselect for order.
                 *
                 * @see #greaterThan(Row«typeSuffixRaw»)
                 */
                @Support
                Condition greaterThan(Select<? extends Record«recTypeSuffix»> select);

                /**
                 * Compare this row value expression with another row value expression for
                 * order.
                 *
                 * @see #greaterThan(Row«typeSuffixRaw»)
                 */
                @Support
                Condition gt(Row«typeSuffix» row);

                /**
                 * Compare this row value expression with a record for order.
                 *
                 * @see #greaterThan(Row«typeSuffixRaw»)
                 */
                @Support
                Condition gt(Record«recTypeSuffix» record);

                /**
                 * Compare this row value expression with another row value expression for
                 * order.
                 *
                 * @see #greaterThan(Row«typeSuffixRaw»)
                 */
                @Support
                Condition gt(«TN_tn(degree)»);

                /**
                 * Compare this row value expression with another row value expression for
                 * order.
                 *
                 * @see #greaterThan(Row«typeSuffixRaw»)
                 */
                @Support
                Condition gt(«Field_TN_tn(degree)»);

                /**
                 * Compare this row value expression with a subselect for order.
                 *
                 * @see #greaterThan(Row«typeSuffixRaw»)
                 */
                @Support
                Condition gt(Select<? extends Record«recTypeSuffix»> select);

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
                Condition greaterOrEqual(Row«typeSuffix» row);

                /**
                 * Compare this row value expression with a record for order.
                 *
                 * @see #greaterOrEqual(Row«typeSuffixRaw»)
                 */
                @Support
                Condition greaterOrEqual(Record«recTypeSuffix» record);

                /**
                 * Compare this row value expression with another row value expression for
                 * order.
                 *
                 * @see #greaterOrEqual(Row«typeSuffixRaw»)
                 */
                @Support
                Condition greaterOrEqual(«TN_tn(degree)»);

                /**
                 * Compare this row value expression with another row value expression for
                 * order.
                 *
                 * @see #greaterOrEqual(Row«typeSuffixRaw»)
                 */
                @Support
                Condition greaterOrEqual(«Field_TN_tn(degree)»);

                /**
                 * Compare this row value expression with a subselect for order.
                 *
                 * @see #greaterOrEqual(Row«typeSuffixRaw»)
                 */
                @Support
                Condition greaterOrEqual(Select<? extends Record«recTypeSuffix»> select);

                /**
                 * Compare this row value expression with another row value expression for
                 * order.
                 *
                 * @see #greaterOrEqual(Row«typeSuffixRaw»)
                 */
                @Support
                Condition ge(Row«typeSuffix» row);

                /**
                 * Compare this row value expression with a record for order.
                 *
                 * @see #greaterOrEqual(Row«typeSuffixRaw»)
                 */
                @Support
                Condition ge(Record«recTypeSuffix» record);

                /**
                 * Compare this row value expression with another row value expression for
                 * order.
                 *
                 * @see #greaterOrEqual(Row«typeSuffixRaw»)
                 */
                @Support
                Condition ge(«TN_tn(degree)»);

                /**
                 * Compare this row value expression with another row value expression for
                 * order.
                 *
                 * @see #greaterOrEqual(Row«typeSuffixRaw»)
                 */
                @Support
                Condition ge(«Field_TN_tn(degree)»);

                /**
                 * Compare this row value expression with a subselect for order.
                 *
                 * @see #greaterOrEqual(Row«typeSuffixRaw»)
                 */
                @Support
                Condition ge(Select<? extends Record«recTypeSuffix»> select);

                // ------------------------------------------------------------------------
                // [NOT] BETWEEN predicates
                // ------------------------------------------------------------------------

                /**
                 * Check if this row value expression is within a range of two other row
                 * value expressions.
                 *
                 * @see #between(Row«typeSuffixRaw», Row«typeSuffixRaw»)
                 */
                @Support
                BetweenAndStep«typeSuffix» between(«TN_XXXn(degree, "minValue")»);

                /**
                 * Check if this row value expression is within a range of two other row
                 * value expressions.
                 *
                 * @see #between(Row«typeSuffixRaw», Row«typeSuffixRaw»)
                 */
                @Support
                BetweenAndStep«typeSuffix» between(«Field_TN_XXXn(degree, "minValue")»);

                /**
                 * Check if this row value expression is within a range of two other row
                 * value expressions.
                 *
                 * @see #between(Row«typeSuffixRaw», Row«typeSuffixRaw»)
                 */
                @Support
                BetweenAndStep«typeSuffix» between(Row«typeSuffix» minValue);

                /**
                 * Check if this row value expression is within a range of two records.
                 *
                 * @see #between(Row«typeSuffixRaw», Row«typeSuffixRaw»)
                 */
                @Support
                BetweenAndStep«typeSuffix» between(Record«recTypeSuffix» minValue);

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
                Condition between(Row«typeSuffix» minValue,
                                  Row«typeSuffix» maxValue);

                /**
                 * Check if this row value expression is within a range of two records.
                 * <p>
                 * This is the same as calling <code>between(minValue).and(maxValue)</code>
                 *
                 * @see #between(Row«typeSuffixRaw», Row«typeSuffixRaw»)
                 */
                @Support
                Condition between(Record«recTypeSuffix» minValue,
                                  Record«recTypeSuffix» maxValue);

                /**
                 * Check if this row value expression is within a symmetric range of two
                 * other row value expressions.
                 *
                 * @see #betweenSymmetric(Row«typeSuffixRaw», Row«typeSuffixRaw»)
                 */
                @Support
                BetweenAndStep«typeSuffix» betweenSymmetric(«TN_XXXn(degree, "minValue")»);

                /**
                 * Check if this row value expression is within a symmetric range of two
                 * other row value expressions.
                 *
                 * @see #betweenSymmetric(Row«typeSuffixRaw», Row«typeSuffixRaw»)
                 */
                @Support
                BetweenAndStep«typeSuffix» betweenSymmetric(«Field_TN_XXXn(degree, "minValue")»);

                /**
                 * Check if this row value expression is within a symmetric range of two
                 * other row value expressions.
                 *
                 * @see #betweenSymmetric(Row«typeSuffixRaw», Row«typeSuffixRaw»)
                 */
                @Support
                BetweenAndStep«typeSuffix» betweenSymmetric(Row«typeSuffix» minValue);

                /**
                 * Check if this row value expression is within a symmetric range of two
                 * records.
                 *
                 * @see #betweenSymmetric(Row«typeSuffixRaw», Row«typeSuffixRaw»)
                 */
                @Support
                BetweenAndStep«typeSuffix» betweenSymmetric(Record«recTypeSuffix» minValue);

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
                Condition betweenSymmetric(Row«typeSuffix» minValue,
                                           Row«typeSuffix» maxValue);

                /**
                 * Check if this row value expression is within a symmetric range of two
                 * records.
                 * <p>
                 * This is the same as calling <code>betweenSymmetric(minValue).and(maxValue)</code>
                 *
                 * @see #betweenSymmetric(Row«typeSuffixRaw», Row«typeSuffixRaw»)
                 */
                @Support
                Condition betweenSymmetric(Record«recTypeSuffix» minValue,
                                           Record«recTypeSuffix» maxValue);

                /**
                 * Check if this row value expression is not within a range of two other
                 * row value expressions.
                 *
                 * @see #between(Row«typeSuffixRaw», Row«typeSuffixRaw»)
                 */
                @Support
                BetweenAndStep«typeSuffix» notBetween(«TN_XXXn(degree, "minValue")»);

                /**
                 * Check if this row value expression is not within a range of two other
                 * row value expressions.
                 *
                 * @see #notBetween(Row«typeSuffixRaw», Row«typeSuffixRaw»)
                 */
                @Support
                BetweenAndStep«typeSuffix» notBetween(«Field_TN_XXXn(degree, "minValue")»);

                /**
                 * Check if this row value expression is not within a range of two other
                 * row value expressions.
                 *
                 * @see #notBetween(Row«typeSuffixRaw», Row«typeSuffixRaw»)
                 */
                @Support
                BetweenAndStep«typeSuffix» notBetween(Row«typeSuffix» minValue);

                /**
                 * Check if this row value expression is within a range of two records.
                 *
                 * @see #notBetween(Row«typeSuffixRaw», Row«typeSuffixRaw»)
                 */
                @Support
                BetweenAndStep«typeSuffix» notBetween(Record«recTypeSuffix» minValue);

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
                Condition notBetween(Row«typeSuffix» minValue,
                                     Row«typeSuffix» maxValue);

                /**
                 * Check if this row value expression is within a range of two records.
                 * <p>
                 * This is the same as calling <code>notBetween(minValue).and(maxValue)</code>
                 *
                 * @see #notBetween(Row«typeSuffixRaw», Row«typeSuffixRaw»)
                 */
                @Support
                Condition notBetween(Record«recTypeSuffix» minValue,
                                     Record«recTypeSuffix» maxValue);

                /**
                 * Check if this row value expression is not within a symmetric range of two
                 * other row value expressions.
                 *
                 * @see #notBetweenSymmetric(Row«typeSuffixRaw», Row«typeSuffixRaw»)
                 */
                @Support
                BetweenAndStep«typeSuffix» notBetweenSymmetric(«TN_XXXn(degree, "minValue")»);

                /**
                 * Check if this row value expression is not within a symmetric range of two
                 * other row value expressions.
                 *
                 * @see #notBetweenSymmetric(Row«typeSuffixRaw», Row«typeSuffixRaw»)
                 */
                @Support
                BetweenAndStep«typeSuffix» notBetweenSymmetric(«Field_TN_XXXn(degree, "minValue")»);

                /**
                 * Check if this row value expression is not within a symmetric range of two
                 * other row value expressions.
                 *
                 * @see #notBetweenSymmetric(Row«typeSuffixRaw», Row«typeSuffixRaw»)
                 */
                @Support
                BetweenAndStep«typeSuffix» notBetweenSymmetric(Row«typeSuffix» minValue);

                /**
                 * Check if this row value expression is not within a symmetric range of two
                 * records.
                 *
                 * @see #notBetweenSymmetric(Row«typeSuffixRaw», Row«typeSuffixRaw»)
                 */
                @Support
                BetweenAndStep«typeSuffix» notBetweenSymmetric(Record«recTypeSuffix» minValue);

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
                Condition notBetweenSymmetric(Row«typeSuffix» minValue,
                                              Row«typeSuffix» maxValue);

                /**
                 * Check if this row value expression is not within a symmetric range of two
                 * records.
                 * <p>
                 * This is the same as calling <code>notBetweenSymmetric(minValue).and(maxValue)</code>
                 *
                 * @see #notBetweenSymmetric(Row«typeSuffixRaw», Row«typeSuffixRaw»)
                 */
                @Support
                Condition notBetweenSymmetric(Record«recTypeSuffix» minValue,
                                              Record«recTypeSuffix» maxValue);

                // ------------------------------------------------------------------------
                // [NOT] DISTINCT predicates
                // ------------------------------------------------------------------------

«««                /**
«««                 * Compare this row value expression with another row value expression for
«««                 * distinctness.
«««                 */
«««                @Support
«««                Condition isDistinctFrom(Row«typeSuffix» row);
«««
«««                /**
«««                 * Compare this row value expression with a record for distinctness.
«««                 *
«««                 * @see #isDistinctFrom(Row«typeSuffixRaw»)
«««                 */
«««                @Support
«««                Condition isDistinctFrom(Record«recTypeSuffix» record);
«««
«««                /**
«««                 * Compare this row value expression with another row value expression for
«««                 * distinctness.
«««                 *
«««                 * @see #isDistinctFrom(Row«typeSuffixRaw»)
«««                 */
«««                @Support
«««                Condition isDistinctFrom(«TN_tn(degree)»);
«««
«««                /**
«««                 * Compare this row value expression with another row value expression for
«««                 * distinctness.
«««                 *
«««                 * @see #isDistinctFrom(Row«typeSuffixRaw»)
«««                 */
«««                @Support
«««                Condition isDistinctFrom(«Field_TN_tn(degree)»);
«««
«««                /**
«««                 * Compare this row value expression with another row value expression for
«««                 * non-distinctness.
«««                 */
«««                @Support
«««                Condition isNotDistinctFrom(Row«typeSuffix» row);
«««
«««                /**
«««                 * Compare this row value expression with a record for non-distinctness.
«««                 *
«««                 * @see #isNotDistinctFrom(Row«typeSuffixRaw»)
«««                 */
«««                @Support
«««                Condition isNotDistinctFrom(Record«recTypeSuffix» record);
«««
«««                /**
«««                 * Compare this row value expression with another row value expression for
«««                 * non-distinctness.
«««                 *
«««                 * @see #isNotDistinctFrom(Row«typeSuffixRaw»)
«««                 */
«««                @Support
«««                Condition isNotDistinctFrom(«TN_tn(degree)»);
«««
«««                /**
«««                 * Compare this row value expression with another row value expression for
«««                 * non-distinctness.
«««                 *
«««                 * @see #isNotDistinctFrom(Row«typeSuffixRaw»)
«««                 */
«««                @Support
«««                Condition isNotDistinctFrom(«Field_TN_tn(degree)»);

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
                Condition in(Collection<? extends Row«typeSuffix»> rows);

                /**
                 * Compare this row value expression with a set of row value expressions for
                 * equality.
                 *
                 * @see #in(Collection)
                 */
                @Support
                Condition in(Row«typeSuffix»... rows);

                /**
                 * Compare this row value expression with a set of records for equality.
                 *
                 * @see #in(Collection)
                 */
                @Support
                Condition in(Record«recTypeSuffix»... record);

                /**
                 * Compare this row value expression with a subselect for equality.
                 *
                 * @see #in(Collection)
                 */
                @Support
                Condition in(Select<? extends Record«recTypeSuffix»> select);

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
                Condition notIn(Collection<? extends Row«typeSuffix»> rows);

                /**
                 * Compare this row value expression with a set of row value expressions for
                 * equality.
                 *
                 * @see #notIn(Collection)
                 */
                @Support
                Condition notIn(Row«typeSuffix»... rows);

                /**
                 * Compare this row value expression with a set of records for non-equality.
                 *
                 * @see #notIn(Collection)
                 */
                @Support
                Condition notIn(Record«recTypeSuffix»... record);

                /**
                 * Compare this row value expression with a subselect for non-equality.
                 *
                 * @see #notIn(Collection)
                 */
                @Support
                Condition notIn(Select<? extends Record«recTypeSuffix»> select);

                «IF degree == 2»
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

                «ENDIF»
            }
            ''');

            write("org.jooq.Row" + degreeOrN(degree), out);
        }
    }

    def generateRowImpl() {
        val out = new StringBuilder();

        out.append('''
        «classHeader»
        package org.jooq.impl;

        import static org.jooq.impl.DSL.row;

        import java.util.Arrays;
        import java.util.Collection;

        import javax.annotation.Generated;

        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»
        import org.jooq.BetweenAndStep«typeSuffixRaw(degree)»;
        «ENDFOR»
        import org.jooq.BindContext;
        import org.jooq.Comparator;
        import org.jooq.Condition;
        import org.jooq.DataType;
        import org.jooq.Field;
        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»
        import org.jooq.Record«recTypeSuffixRaw(degree)»;
        «ENDFOR»
        import org.jooq.RenderContext;
        import org.jooq.Row;
        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»
        import org.jooq.Row«typeSuffixRaw(degree)»;
        «ENDFOR»
        import org.jooq.Select;

        /**
         * @author Lukas Eder
         */
        «generatedAnnotation»
        @SuppressWarnings({ "rawtypes", "unchecked" })
        class RowImpl<«TN(Constants::MAX_ROW_DEGREE)»> extends AbstractQueryPart
        implements

            // This row implementation implements all row types. Type-safety is
            // being checked through the type-safe API. No need for further checks here
            «FOR degree : (0..Constants::MAX_ROW_DEGREE) SEPARATOR ','»
            Row«typeSuffix(degree)»
            «ENDFOR» 
        {

            /**
             * Generated UID
             */
            private static final long serialVersionUID = -929427349071556318L;

            final Fields              fields;

            RowImpl(Field<?>... fields) {
                this(new Fields(fields));
            }

            RowImpl(Collection<? extends Field<?>> fields) {
                this(new Fields(fields));
            }

            RowImpl(Fields fields) {
                super();

                this.fields = fields;
            }

            // ------------------------------------------------------------------------
            // XXX: QueryPart API
            // ------------------------------------------------------------------------

            @Override
            public final void toSQL(RenderContext context) {
                context.sql("(");

                String separator = "";
                for (Field<?> field : fields.fields) {
                    context.sql(separator);
                    context.sql(field);

                    separator = ", ";
                }

                context.sql(")");
            }

            @Override
            public final void bind(BindContext context) {
                context.bind(fields);
            }

            // ------------------------------------------------------------------------
            // XXX: Row accessor API
            // ------------------------------------------------------------------------

            @Override
            public final int size() {
                return fields.fields.length;
            }

            @Override
            public final <T> Field<T> field(Field<T> field) {
                return fields.field(field);
            }

            @Override
            public final Field<?> field(String name) {
                return fields.field(name);
            }

            @Override
            public final Field<?> field(int index) {
                return fields.field(index);
            }

            @Override
            public final Field<?>[] fields() {
                return fields.fields();
            }

            @Override
            public final int indexOf(Field<?> field) {
                return fields.indexOf(field);
            }

            @Override
            public final int indexOf(String fieldName) {
                return fields.indexOf(fieldName);
            }

            @Override
            public final Class<?>[] types() {
                int size = fields.fields.length;
                Class<?>[] result = new Class[size];
        
                for (int i = 0; i < size; i++) {
                    result[i] = fields.field(i).getType();
                }
        
                return result;
            }

            @Override
            public final Class<?> type(int fieldIndex) {
                return fieldIndex >= 0 && fieldIndex < size() ? fields.field(fieldIndex).getType() : null;
            }

            @Override
            public final Class<?> type(String fieldName) {
                return type(indexOf(fieldName));
            }

            @Override
            public final DataType<?>[] dataTypes() {
                int size = fields.fields.length;
                DataType<?>[] result = new DataType[size];
                
                for (int i = 0; i < size; i++) {
                    result[i] = fields.field(i).getDataType();
                }
                
                return result;
            }

            @Override
            public final DataType<?> dataType(int fieldIndex) {
                return fieldIndex >= 0 && fieldIndex < size() ? fields.field(fieldIndex).getDataType() : null;
            }

            @Override
            public final DataType<?> dataType(String fieldName) {
                return dataType(indexOf(fieldName));
            }
            «FOR degree : (1..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Field<T«degree»> field«degree»() {
                return (Field<T«degree»>) fields.field(«degree - 1»);
            }
            «ENDFOR»

            // ------------------------------------------------------------------------
            // [NOT] NULL predicates
            // ------------------------------------------------------------------------

            @Override
            public final Condition isNull() {
                return new RowIsNull(this, true);
            }

            @Override
            public final Condition isNotNull() {
                return new RowIsNull(this, false);
            }

            // ------------------------------------------------------------------------
            // Generic comparison predicates
            // ------------------------------------------------------------------------
            «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Condition compare(Comparator comparator, Row«typeSuffix(degree)» row) {
                return new RowCondition(this, row, comparator);
            }
            «ENDFOR»
            «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Condition compare(Comparator comparator, Record«recTypeSuffix(degree)» record) {
                return new RowCondition(this, record.valuesRow(), comparator);
            }
            «ENDFOR»
            «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Condition compare(Comparator comparator, «TN_tn(degree)») {
                return compare(comparator, row(«tn(degree)»));
            }
            «ENDFOR»
            «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Condition compare(Comparator comparator, «Field_TN_tn(degree)») {
                return compare(comparator, row(«tn(degree)»));
            }
            «ENDFOR»

            // ------------------------------------------------------------------------
            // Equal / Not equal comparison predicates
            // ------------------------------------------------------------------------
            «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Condition equal(Row«typeSuffix(degree)» row) {
                return compare(Comparator.EQUALS, row);
            }
            «ENDFOR»
            «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Condition equal(Record«recTypeSuffix(degree)» record) {
                return compare(Comparator.EQUALS, record);
            }
            «ENDFOR»
            «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Condition equal(«TN_tn(degree)») {
                return compare(Comparator.EQUALS, «tn(degree)»);
            }
            «ENDFOR»
            «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Condition equal(«Field_TN_tn(degree)») {
                return compare(Comparator.EQUALS, «tn(degree)»);
            }
            «ENDFOR»
            «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Condition eq(Row«typeSuffix(degree)» row) {
                return equal(row);
            }
            «ENDFOR»
            «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Condition eq(Record«recTypeSuffix(degree)» record) {
                return equal(record);
            }
            «ENDFOR»
            «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Condition eq(«TN_tn(degree)») {
                return equal(«tn(degree)»);
            }
            «ENDFOR»
            «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Condition eq(«Field_TN_tn(degree)») {
                return equal(«tn(degree)»);
            }
            «ENDFOR»
            «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Condition notEqual(Row«typeSuffix(degree)» row) {
                return compare(Comparator.NOT_EQUALS, row);
            }
            «ENDFOR»
            «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Condition notEqual(Record«recTypeSuffix(degree)» record) {
                return compare(Comparator.NOT_EQUALS, record);
            }
            «ENDFOR»
            «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Condition notEqual(«TN_tn(degree)») {
                return compare(Comparator.NOT_EQUALS, «tn(degree)»);
            }
            «ENDFOR»
            «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Condition notEqual(«Field_TN_tn(degree)») {
                return compare(Comparator.NOT_EQUALS, «tn(degree)»);
            }
            «ENDFOR»
            «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Condition ne(Row«typeSuffix(degree)» row) {
                return notEqual(row);
            }
            «ENDFOR»
            «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Condition ne(Record«recTypeSuffix(degree)» record) {
                return notEqual(record);
            }
            «ENDFOR»
            «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Condition ne(«TN_tn(degree)») {
                return notEqual(«tn(degree)»);
            }
            «ENDFOR»
            «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Condition ne(«Field_TN_tn(degree)») {
                return notEqual(«tn(degree)»);
            }
            «ENDFOR»

            // ------------------------------------------------------------------------
            // Ordering comparison predicates
            // ------------------------------------------------------------------------
            «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Condition lessThan(Row«typeSuffix(degree)» row) {
                return compare(Comparator.LESS, row);
            }
            «ENDFOR»
            «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Condition lessThan(Record«recTypeSuffix(degree)» record) {
                return compare(Comparator.LESS, record);
            }
            «ENDFOR»
            «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Condition lessThan(«TN_tn(degree)») {
                return compare(Comparator.LESS, «tn(degree)»);
            }
            «ENDFOR»
            «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Condition lessThan(«Field_TN_tn(degree)») {
                return compare(Comparator.LESS, «tn(degree)»);
            }
            «ENDFOR»
            «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Condition lt(Row«typeSuffix(degree)» row) {
                return lessThan(row);
            }
            «ENDFOR»
            «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Condition lt(Record«recTypeSuffix(degree)» record) {
                return lessThan(record);
            }
            «ENDFOR»
            «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Condition lt(«TN_tn(degree)») {
                return lessThan(«tn(degree)»);
            }
            «ENDFOR»
            «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Condition lt(«Field_TN_tn(degree)») {
                return lessThan(«tn(degree)»);
            }
            «ENDFOR»
            «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Condition lessOrEqual(Row«typeSuffix(degree)» row) {
                return compare(Comparator.LESS_OR_EQUAL, row);
            }
            «ENDFOR»
            «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Condition lessOrEqual(Record«recTypeSuffix(degree)» record) {
                return compare(Comparator.LESS_OR_EQUAL, record);
            }
            «ENDFOR»
            «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Condition lessOrEqual(«TN_tn(degree)») {
                return compare(Comparator.LESS_OR_EQUAL, «tn(degree)»);
            }
            «ENDFOR»
            «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Condition lessOrEqual(«Field_TN_tn(degree)») {
                return compare(Comparator.LESS_OR_EQUAL, «tn(degree)»);
            }
            «ENDFOR»
            «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Condition le(Row«typeSuffix(degree)» row) {
                return lessOrEqual(row);
            }
            «ENDFOR»
            «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Condition le(Record«recTypeSuffix(degree)» record) {
                return lessOrEqual(record);
            }
            «ENDFOR»
            «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Condition le(«TN_tn(degree)») {
                return lessOrEqual(«tn(degree)»);
            }
            «ENDFOR»
            «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Condition le(«Field_TN_tn(degree)») {
                return lessOrEqual(«tn(degree)»);
            }
            «ENDFOR»
            «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Condition greaterThan(Row«typeSuffix(degree)» row) {
                return compare(Comparator.GREATER, row);
            }
            «ENDFOR»
            «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Condition greaterThan(Record«recTypeSuffix(degree)» record) {
                return compare(Comparator.GREATER, record);
            }
            «ENDFOR»
            «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Condition greaterThan(«TN_tn(degree)») {
                return compare(Comparator.GREATER, «tn(degree)»);
            }
            «ENDFOR»
            «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Condition greaterThan(«Field_TN_tn(degree)») {
                return compare(Comparator.GREATER, «tn(degree)»);
            }
            «ENDFOR»
            «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Condition gt(Row«typeSuffix(degree)» row) {
                return greaterThan(row);
            }
            «ENDFOR»
            «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Condition gt(Record«recTypeSuffix(degree)» record) {
                return greaterThan(record);
            }
            «ENDFOR»
            «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Condition gt(«TN_tn(degree)») {
                return greaterThan(«tn(degree)»);
            }
            «ENDFOR»
            «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Condition gt(«Field_TN_tn(degree)») {
                return greaterThan(«tn(degree)»);
            }
            «ENDFOR»
            «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Condition greaterOrEqual(Row«typeSuffix(degree)» row) {
                return compare(Comparator.GREATER_OR_EQUAL, row);
            }
            «ENDFOR»
            «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Condition greaterOrEqual(Record«recTypeSuffix(degree)» record) {
                return compare(Comparator.GREATER_OR_EQUAL, record);
            }
            «ENDFOR»
            «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Condition greaterOrEqual(«TN_tn(degree)») {
                return compare(Comparator.GREATER_OR_EQUAL, «tn(degree)»);
            }
            «ENDFOR»
            «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Condition greaterOrEqual(«Field_TN_tn(degree)») {
                return compare(Comparator.GREATER_OR_EQUAL, «tn(degree)»);
            }
            «ENDFOR»
            «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Condition ge(Row«typeSuffix(degree)» row) {
                return greaterOrEqual(row);
            }
            «ENDFOR»
            «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Condition ge(Record«recTypeSuffix(degree)» record) {
                return greaterOrEqual(record);
            }
            «ENDFOR»
            «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Condition ge(«TN_tn(degree)») {
                return greaterOrEqual(«tn(degree)»);
            }
            «ENDFOR»
            «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Condition ge(«Field_TN_tn(degree)») {
                return greaterOrEqual(«tn(degree)»);
            }
            «ENDFOR»

            // ------------------------------------------------------------------------
            // [NOT] BETWEEN predicates
            // ------------------------------------------------------------------------
            «FOR keyword : newArrayList("between", "betweenSymmetric", "notBetween", "notBetweenSymmetric")»
            «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

            @Override
            public final BetweenAndStep«typeSuffix(degree)» «keyword»(«TN_tn(degree)») {
                return «keyword»(row(«tn(degree)»));
            }
            «ENDFOR»
            «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

            @Override
            public final BetweenAndStep«typeSuffix(degree)» «keyword»(«Field_TN_tn(degree)») {
                return «keyword»(row(«tn(degree)»));
            }
            «ENDFOR»
            «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

            @Override
            public final BetweenAndStep«typeSuffix(degree)» «keyword»(Row«typeSuffix(degree)» row) {
                return new RowBetweenCondition(this, row, «keyword.startsWith("not")», «keyword.endsWith("Symmetric")»);
            }
            «ENDFOR»
            «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

            @Override
            public final BetweenAndStep«typeSuffix(degree)» «keyword»(Record«recTypeSuffix(degree)» record) {
                return «keyword»(record.valuesRow());
            }
            «ENDFOR»
            «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Condition «keyword»(Row«typeSuffix(degree)» minValue, Row«typeSuffix(degree)» maxValue) {
                return «keyword»(minValue).and(maxValue);
            }
            «ENDFOR»
            «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Condition «keyword»(Record«recTypeSuffix(degree)» minValue, Record«recTypeSuffix(degree)» maxValue) {
                return «keyword»(minValue).and(maxValue);
            }
            «ENDFOR»
            «ENDFOR»

            // ------------------------------------------------------------------------
            // [NOT] DISTINCT predicates
            // ------------------------------------------------------------------------

            // ------------------------------------------------------------------------
            // [NOT] IN predicates
            // ------------------------------------------------------------------------
            «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Condition in(Row«typeSuffix(degree)»... rows) {
                return in(Arrays.asList(rows));
            }
            «ENDFOR»
            «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Condition in(Record«recTypeSuffix(degree)»... records) {
                Row«typeSuffix(degree)»[] rows = new Row«typeSuffixRaw(degree)»[records.length];

                for (int i = 0; i < records.length; i++) {
                    rows[i] = «IF degree == 0»(RowN) «ENDIF»records[i].valuesRow();
                }

                return in(rows);
            }
            «ENDFOR»
            «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Condition notIn(Row«typeSuffix(degree)»... rows) {
                return notIn(Arrays.asList(rows));
            }
            «ENDFOR»
            «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Condition notIn(Record«recTypeSuffix(degree)»... records) {
                Row«typeSuffix(degree)»[] rows = new Row«typeSuffixRaw(degree)»[records.length];

                for (int i = 0; i < records.length; i++) {
                    rows[i] = «IF degree == 0»(RowN) «ENDIF»records[i].valuesRow();
                }

                return notIn(rows);
            }
            «ENDFOR»

            @Override
            public final Condition in(Collection rows) {
                QueryPartList<Row> list = new QueryPartList<Row>(rows);
                return new RowInCondition(this, list, Comparator.IN);
            }

            @Override
            public final Condition notIn(Collection rows) {
                QueryPartList<Row> list = new QueryPartList<Row>(rows);
                return new RowInCondition(this, list, Comparator.NOT_IN);
            }

            // ------------------------------------------------------------------------
            // Predicates involving subqueries
            // ------------------------------------------------------------------------

            @Override
            public final Condition equal(Select select) {
                return new RowSubqueryCondition(this, select, Comparator.EQUALS);
            }

            @Override
            public final Condition eq(Select select) {
                return equal(select);
            }

            @Override
            public final Condition notEqual(Select select) {
                return new RowSubqueryCondition(this, select, Comparator.NOT_EQUALS);
            }

            @Override
            public final Condition ne(Select select) {
                return notEqual(select);
            }

            @Override
            public final Condition greaterThan(Select select) {
                return new RowSubqueryCondition(this, select, Comparator.GREATER);
            }

            @Override
            public final Condition gt(Select select) {
                return greaterThan(select);
            }

            @Override
            public final Condition greaterOrEqual(Select select) {
                return new RowSubqueryCondition(this, select, Comparator.GREATER_OR_EQUAL);
            }

            @Override
            public final Condition ge(Select select) {
                return greaterOrEqual(select);
            }

            @Override
            public final Condition lessThan(Select select) {
                return new RowSubqueryCondition(this, select, Comparator.LESS);
            }

            @Override
            public final Condition lt(Select select) {
                return lessThan(select);
            }

            @Override
            public final Condition lessOrEqual(Select select) {
                return new RowSubqueryCondition(this, select, Comparator.LESS_OR_EQUAL);
            }

            @Override
            public final Condition le(Select select) {
                return lessOrEqual(select);
            }

            @Override
            public final Condition in(Select select) {
                return new RowSubqueryCondition(this, select, Comparator.IN);
            }

            @Override
            public final Condition notIn(Select select) {
                return new RowSubqueryCondition(this, select, Comparator.NOT_IN);
            }

            // ------------------------------------------------------------------------
            // XXX: Row2 API
            // ------------------------------------------------------------------------

            @Override
            public final Condition overlaps(T1 t1, T2 t2) {
                return overlaps(row(t1, t2));
            }

            @Override
            public final Condition overlaps(Field<T1> t1, Field<T2> t2) {
                return overlaps(row(t1, t2));
            }

            @Override
            public final Condition overlaps(Row2<T1, T2> row) {
                return new RowOverlapsCondition(this, row);
            }
        }
        ''');

        write("org.jooq.impl.RowImpl", out);
    }
}
