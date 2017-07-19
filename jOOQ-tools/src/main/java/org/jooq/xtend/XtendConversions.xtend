/**
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */
package org.jooq.xtend

import org.jooq.Constants

/**
 * @author Lukas Eder
 */
class XtendConversions extends Generators {
    
    def static void main(String[] args) {
        val conversions = new XtendConversions();
        conversions.generateConversions();
    }
    
    def generateConversions() {
        val out = new StringBuilder();
        
        out.append('''
            «classHeader»
            package org.jooq.xtend
            
            import javax.annotation.Generated

            import org.jooq.Condition
            import org.jooq.Field
            import org.jooq.QuantifiedSelect
            «FOR degree : (0..Constants::MAX_ROW_DEGREE)»
            import org.jooq.Record«recTypeSuffixRaw(degree)»
            «ENDFOR»
            import org.jooq.Row
            «FOR degree : (0..Constants::MAX_ROW_DEGREE)»
            import org.jooq.Row«typeSuffixRaw(degree)»
            «ENDFOR»
            import org.jooq.Select
            
            import org.jooq.impl.DSL
            
            /**
             * jOOQ type conversions used to enhance the jOOQ Java API with Xtend operators.
             * 
             * @author Lukas Eder
             * @see <a href="http://www.eclipse.org/xtend/documentation.html#operators">http://www.eclipse.org/xtend/documentation.html#operators</a>
             */
            «generatedAnnotation»
            class Conversions {
            
                def static <T> operator_or(Condition c1, Condition c2) {
                    c1.or(c2);
                }
            
                def static <T> operator_and(Condition c1, Condition c2) {
                    c1.and(c2);
                }
            
                def static <T> operator_tripleEquals(Field<T> f1, T f2) {
                    f1.eq(f2)
                }
            
                def static <T> operator_tripleEquals(Field<T> f1, Field<T> f2) {
                    f1.eq(f2)
                }
            
                def static <T> operator_tripleEquals(Field<T> f1, Select<? extends Record1<T>> f2) {
                    f1.eq(f2)
                }
            
                def static <T> operator_tripleEquals(Field<T> f1, QuantifiedSelect<? extends Record1<T>> f2) {
                    f1.eq(f2)
                }

                def static operator_tripleEquals(RowN r1, RowN r2) {
                    r1.eq(r2)
                }

                def static operator_tripleEquals(RowN r1, Record r2) {
                    r1.eq(r2)
                }

                def static operator_tripleEquals(RowN r1, Select<? extends Record> r2) {
                    r1.eq(r2)
                }
                «FOR degree : (1..Constants::MAX_ROW_DEGREE)»

                def static <«TN(degree)»> operator_tripleEquals(Row«typeSuffix(degree)» r1, Row«typeSuffix(degree)» r2) {
                    r1.eq(r2)
                }

                def static <«TN(degree)»> operator_tripleEquals(Row«typeSuffix(degree)» r1, Record«typeSuffix(degree)» r2) {
                    r1.eq(r2)
                }

                def static <«TN(degree)»> operator_tripleEquals(Row«typeSuffix(degree)» r1, Select<? extends Record«typeSuffix(degree)»> r2) {
                    r1.eq(r2)
                }
                «ENDFOR»
            
                def static <T> operator_tripleNotEquals(Field<T> f1, T f2) {
                    f1.ne(f2)
                }
            
                def static <T> operator_tripleNotEquals(Field<T> f1, Field<T> f2) {
                    f1.ne(f2)
                }
            
                def static <T> operator_tripleNotEquals(Field<T> f1, Select<? extends Record1<T>> f2) {
                    f1.ne(f2)
                }
            
                def static <T> operator_tripleNotEquals(Field<T> f1, QuantifiedSelect<? extends Record1<T>> f2) {
                    f1.ne(f2)
                }
                «FOR degree : (1..Constants::MAX_ROW_DEGREE)»

                def static <«TN(degree)»> operator_tripleNotEquals(Row«typeSuffix(degree)» r1, Row«typeSuffix(degree)» r2) {
                    r1.ne(r2)
                }

                def static <«TN(degree)»> operator_tripleNotEquals(Row«typeSuffix(degree)» r1, Record«typeSuffix(degree)» r2) {
                    r1.ne(r2)
                }

                def static <«TN(degree)»> operator_tripleNotEquals(Row«typeSuffix(degree)» r1, Select<? extends Record«typeSuffix(degree)»> r2) {
                    r1.ne(r2)
                }
                «ENDFOR»
            
                def static <T> operator_lessThan(Field<T> f1, T f2) {
                    f1.lt(f2)
                }
            
                def static <T> operator_lessThan(Field<T> f1, Field<T> f2) {
                    f1.lt(f2)
                }
            
                def static <T> operator_lessThan(Field<T> f1, Select<? extends Record1<T>> f2) {
                    f1.lt(f2)
                }
            
                def static <T> operator_lessThan(Field<T> f1, QuantifiedSelect<? extends Record1<T>> f2) {
                    f1.lt(f2)
                }
                «FOR degree : (1..Constants::MAX_ROW_DEGREE)»

                def static <«TN(degree)»> operator_lessThan(Row«typeSuffix(degree)» r1, Row«typeSuffix(degree)» r2) {
                    r1.lt(r2)
                }

                def static <«TN(degree)»> operator_lessThan(Row«typeSuffix(degree)» r1, Record«typeSuffix(degree)» r2) {
                    r1.lt(r2)
                }

                def static <«TN(degree)»> operator_lessThan(Row«typeSuffix(degree)» r1, Select<? extends Record«typeSuffix(degree)»> r2) {
                    r1.lt(r2)
                }
                «ENDFOR»
            
                def static <T> operator_greaterThan(Field<T> f1, T f2) {
                    f1.gt(f2)
                }
            
                def static <T> operator_greaterThan(Field<T> f1, Field<T> f2) {
                    f1.gt(f2)
                }
            
                def static <T> operator_greaterThan(Field<T> f1, Select<? extends Record1<T>> f2) {
                    f1.gt(f2)
                }
            
                def static <T> operator_greaterThan(Field<T> f1, QuantifiedSelect<? extends Record1<T>> f2) {
                    f1.gt(f2)
                }
                «FOR degree : (1..Constants::MAX_ROW_DEGREE)»

                def static <«TN(degree)»> operator_greaterThan(Row«typeSuffix(degree)» r1, Row«typeSuffix(degree)» r2) {
                    r1.gt(r2)
                }

                def static <«TN(degree)»> operator_greaterThan(Row«typeSuffix(degree)» r1, Record«typeSuffix(degree)» r2) {
                    r1.gt(r2)
                }

                def static <«TN(degree)»> operator_greaterThan(Row«typeSuffix(degree)» r1, Select<? extends Record«typeSuffix(degree)»> r2) {
                    r1.gt(r2)
                }
                «ENDFOR»
            
                def static <T> operator_lessEqualsThan(Field<T> f1, T f2) {
                    f1.le(f2)
                }
            
                def static <T> operator_lessEqualsThan(Field<T> f1, Field<T> f2) {
                    f1.le(f2)
                }
            
                def static <T> operator_lessEqualsThan(Field<T> f1, Select<? extends Record1<T>> f2) {
                    f1.le(f2)
                }
            
                def static <T> operator_lessEqualsThan(Field<T> f1, QuantifiedSelect<? extends Record1<T>> f2) {
                    f1.le(f2)
                }
                «FOR degree : (1..Constants::MAX_ROW_DEGREE)»

                def static <«TN(degree)»> operator_lessEqualsThan(Row«typeSuffix(degree)» r1, Row«typeSuffix(degree)» r2) {
                    r1.le(r2)
                }

                def static <«TN(degree)»> operator_lessEqualsThan(Row«typeSuffix(degree)» r1, Record«typeSuffix(degree)» r2) {
                    r1.le(r2)
                }

                def static <«TN(degree)»> operator_lessEqualsThan(Row«typeSuffix(degree)» r1, Select<? extends Record«typeSuffix(degree)»> r2) {
                    r1.le(r2)
                }
                «ENDFOR»
            
                def static <T> operator_greaterEqualsThan(Field<T> f1, T f2) {
                    f1.ge(f2)
                }
            
                def static <T> operator_greaterEqualsThan(Field<T> f1, Field<T> f2) {
                    f1.ge(f2)
                }
            
                def static <T> operator_greaterEqualsThan(Field<T> f1, Select<? extends Record1<T>> f2) {
                    f1.ge(f2)
                }
            
                def static <T> operator_greaterEqualsThan(Field<T> f1, QuantifiedSelect<? extends Record1<T>> f2) {
                    f1.ge(f2)
                }
                «FOR degree : (1..Constants::MAX_ROW_DEGREE)»

                def static <«TN(degree)»> operator_greaterEqualsThan(Row«typeSuffix(degree)» r1, Row«typeSuffix(degree)» r2) {
                    r1.ge(r2)
                }

                def static <«TN(degree)»> operator_greaterEqualsThan(Row«typeSuffix(degree)» r1, Record«typeSuffix(degree)» r2) {
                    r1.ge(r2)
                }

                def static <«TN(degree)»> operator_greaterEqualsThan(Row«typeSuffix(degree)» r1, Select<? extends Record«typeSuffix(degree)»> r2) {
                    r1.ge(r2)
                }
                «ENDFOR»
            
                def static operator_upTo(Field<Integer> f1, Integer f2) {
                    DSL::generateSeries(f1, DSL::value(f2))
                }
            
                def static operator_upTo(Field<Integer> f1, Field<Integer> f2) {
                    DSL::generateSeries(f1, f2)
                }
            
                def static <T extends Number> operator_doubleLessThan(Field<T> f1, T f2) {
                    DSL::shl(f1, f2)
                }
            
                def static <T extends Number> operator_doubleLessThan(Field<T> f1, Field<T> f2) {
                    DSL::shl(f1, f2)
                }
            
                def static <T extends Number> operator_doubleGreaterThan(Field<T> f1, T f2) {
                    DSL::shr(f1, f2)
                }
            
                def static <T extends Number> operator_doubleGreaterThan(Field<T> f1, Field<T> f2) {
                    DSL::shr(f1, f2)
                }
            
                def static <T> operator_diamond(Field<T> f1, T f2) {
                    f1.ne(f2)
                }
            
                def static <T> operator_elvis(Field<T> f1, T f2) {
                    DSL::nvl(f1, f2)
                }
            
                def static <T> operator_elvis(Field<T> f1, Field<T> f2) {
                    DSL::nvl(f1, f2)
                }
            
                def static <T> operator_spaceship(Field<T> f1, T f2) {
                    f1.isNotDistinctFrom(f2)
                }
            
                def static <T> operator_spaceship(Field<T> f1, Field<T> f2) {
                    f1.isNotDistinctFrom(f2)
                }
            
                def static <T extends Number> operator_plus(Field<T> f1, T f2) {
                    f1.add(f2)
                }
            
                def static <T extends Number> operator_plus(Field<T> f1, Field<T> f2) {
                    f1.add(f2)
                }
            
                def static <T extends Number> operator_minus(Field<T> f1, T f2) {
                    f1.sub(f2)
                }
            
                def static <T extends Number> operator_minus(Field<T> f1, Field<T> f2) {
                    f1.sub(f2)
                }
            
                def static <T extends Number> operator_multiply(Field<T> f1, T f2) {
                    f1.mul(f2)
                }
            
                def static <T extends Number> operator_multiply(Field<T> f1, Field<T> f2) {
                    f1.mul(f2)
                }
            
                def static <T extends Number> operator_divide(Field<T> f1, T f2) {
                    f1.div(f2)
                }
            
                def static <T extends Number> operator_divide(Field<T> f1, Field<T> f2) {
                    f1.div(f2)
                }
            
                def static <T extends Number> operator_modulo(Field<T> f1, T f2) {
                    f1.mod(f2)
                }
            
                def static <T extends Number> operator_modulo(Field<T> f1, Field<T> f2) {
                    f1.mod(f2)
                }
            
                def static <T extends Number> operator_power(Field<T> f1, T f2) {
                    DSL::power(f1, f2)
                }
            
                def static <T extends Number> operator_power(Field<T> f1, Field<T> f2) {
                    DSL::power(f1, f2)
                }
            
                def static operator_not(Condition c) {
                    c.not()
                }
            
                def static <T> operator_minus(Field<T> f) {
                    f.neg();
                }
            }
        ''');
         
        write("org.jooq.xtend.Conversions", out);
    }
}