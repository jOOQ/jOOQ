/**
 * Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
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
package org.jooq.lambda


/**
 * @author Lukas Eder
 */

import org.jooq.xtend.Generators
import javax.annotation.Generated

class Lambda extends Generators {
    
    def static void main(String[] args) {
        val lambda = new Lambda();
        
        lambda.generateTuple();
        lambda.generateTuples();
        lambda.generateFunctions();
        lambda.generateZipStatic();
        lambda.generateCrossJoinStatic();
    }
    
    def max() {
        16;
    }
    
    def copyright() {
        '''
        /**
         * Copyright (c) 2014-2015, Data Geekery GmbH, contact@datageekery.com
         *
         * Licensed under the Apache License, Version 2.0 (the "License");
         * you may not use this file except in compliance with the License.
         * You may obtain a copy of the License at
         *
         *     http://www.apache.org/licenses/LICENSE-2.0
         *
         * Unless required by applicable law or agreed to in writing, software
         * distributed under the License is distributed on an "AS IS" BASIS,
         * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
         * See the License for the specific language governing permissions and
         * limitations under the License.
         */
        '''
    }
    
    def generateZipStatic() {
        val out = new StringBuilder();
        
        for (degree : 2 .. max) {
            out.append('''
            
                /**
                 * Zip «degree» streams into one.
                 * <p>
                 * <code><pre>
                 * // (tuple(1, "a"), tuple(2, "b"), tuple(3, "c"))
                 * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"))
                 * </pre></code>
                 */
                @Generated("This method was generated using jOOQ-tools")
                static <«TN(degree)»> Seq<Tuple«degree»<«TN(degree)»>> zip(«(1 .. degree).map([d | '''Stream<T«d»> s«d»''']).join(", ")») {
                    return zip(«(1 .. degree).map([d | '''seq(s«d»)''']).join(", ")»);
                }
            ''')
        }
        
        for (degree : 2 .. max) {
            out.append('''
            
                /**
                 * Zip «degree» streams into one.
                 * <p>
                 * <code><pre>
                 * // (tuple(1, "a"), tuple(2, "b"), tuple(3, "c"))
                 * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"))
                 * </pre></code>
                 */
                @Generated("This method was generated using jOOQ-tools")
                static <«TN(degree)»> Seq<Tuple«degree»<«TN(degree)»>> zip(«(1 .. degree).map([d | '''Iterable<T«d»> i«d»''']).join(", ")») {
                    return zip(«(1 .. degree).map([d | '''seq(i«d»)''']).join(", ")»);
                }
            ''')
        }
        
        for (degree : 2 .. max) {
            out.append('''
            
                /**
                 * Zip «degree» streams into one.
                 * <p>
                 * <code><pre>
                 * // (tuple(1, "a"), tuple(2, "b"), tuple(3, "c"))
                 * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"))
                 * </pre></code>
                 */
                @Generated("This method was generated using jOOQ-tools")
                static <«TN(degree)»> Seq<Tuple«degree»<«TN(degree)»>> zip(«(1 .. degree).map([d | '''Seq<T«d»> s«d»''']).join(", ")») {
                    return zip(«XXXn(degree, "s")», («XXXn(degree, "t")») -> tuple(«XXXn(degree, "t")»));
                }
            ''')
        }
        
        for (degree : 2 .. max) {
            out.append('''
            
                /**
                 * Zip «degree» streams into one using a «IF degree == 2»{@link BiFunction}«ELSE»{@link Function«degree»}«ENDIF» to produce resulting values.
                 * <p>
                 * <code><pre>
                 * // ("1:a", "2:b", "3:c")
                 * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"), (i, s) -> i + ":" + s)
                 * </pre></code>
                 */
                @Generated("This method was generated using jOOQ-tools")
                static <«TN(degree)», R> Seq<R> zip(«(1 .. degree).map([d | '''Stream<T«d»> s«d»''']).join(", ")», «IF degree == 2»BiFunction«ELSE»Function«degree»«ENDIF»<«TN(degree)», R> zipper) {
                    return zip(«(1 .. degree).map([d | '''seq(s«d»)''']).join(", ")», zipper);
                }
            ''')
        }
        
        for (degree : 2 .. max) {
            out.append('''
            
                /**
                 * Zip «degree» streams into one using a «IF degree == 2»{@link BiFunction}«ELSE»{@link Function«degree»}«ENDIF» to produce resulting values.
                 * <p>
                 * <code><pre>
                 * // ("1:a", "2:b", "3:c")
                 * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"), (i, s) -> i + ":" + s)
                 * </pre></code>
                 */
                @Generated("This method was generated using jOOQ-tools")
                static <«TN(degree)», R> Seq<R> zip(«(1 .. degree).map([d | '''Iterable<T«d»> i«d»''']).join(", ")», «IF degree == 2»BiFunction«ELSE»Function«degree»«ENDIF»<«TN(degree)», R> zipper) {
                    return zip(«(1 .. degree).map([d | '''seq(i«d»)''']).join(", ")», zipper);
                }
            ''')
        }
        
        for (degree : 2 .. max) {
            out.append('''
            
                /**
                 * Zip «degree» streams into one using a «IF degree == 2»{@link BiFunction}«ELSE»{@link Function«degree»}«ENDIF» to produce resulting values.
                 * <p>
                 * <code><pre>
                 * // ("1:a", "2:b", "3:c")
                 * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"), (i, s) -> i + ":" + s)
                 * </pre></code>
                 */
                @Generated("This method was generated using jOOQ-tools")
                static <«TN(degree)», R> Seq<R> zip(«(1 .. degree).map([d | '''Seq<T«d»> s«d»''']).join(", ")», «IF degree == 2»BiFunction«ELSE»Function«degree»«ENDIF»<«TN(degree)», R> zipper) {
                    «FOR d : (1 .. degree)»
                    final Iterator<T«d»> it«d» = s«d».iterator();
                    «ENDFOR»

                    class Zip implements Iterator<R> {
                        @Override
                        public boolean hasNext() {
                            return «FOR d : (1 .. degree) SEPARATOR " && "»it«d».hasNext()«ENDFOR»;
                        }

                        @Override
                        public R next() {
                            return zipper.apply(«FOR d : (1 .. degree) SEPARATOR ", "»it«d».next()«ENDFOR»);
                        }
                    }

                    return seq(new Zip());
                }
            ''')
        }
        
        insert("org.jooq.lambda.Seq", out, "zip-static")
    }
    
    def generateCrossJoinStatic() {
        val out = new StringBuilder();
        
        for (degree : 2 .. max) {
            out.append('''
            
                /**
                 * Cross join «degree» streams into one.
                 * <p>
                 * <code><pre>
                 * // (tuple(1, "a"), tuple(1, "b"), tuple(2, "a"), tuple(2, "b"))
                 * Seq.of(1, 2).crossJoin(Seq.of("a", "b"))
                 * </pre></code>
                 */
                @Generated("This method was generated using jOOQ-tools")
                static <«TN(degree)»> Seq<Tuple«degree»<«TN(degree)»>> crossJoin(«(1 .. degree).map([d | '''Stream<T«d»> s«d»''']).join(", ")») {
                    return crossJoin(«(1 .. degree).map([d | '''seq(s«d»)''']).join(", ")»);
                }
            ''')
        }
        
        for (degree : 2 .. max) {
            out.append('''
            
                /**
                 * Cross join «degree» streams into one.
                 * <p>
                 * <code><pre>
                 * // (tuple(1, "a"), tuple(1, "b"), tuple(2, "a"), tuple(2, "b"))
                 * Seq.of(1, 2).crossJoin(Seq.of("a", "b"))
                 * </pre></code>
                 */
                @Generated("This method was generated using jOOQ-tools")
                static <«TN(degree)»> Seq<Tuple«degree»<«TN(degree)»>> crossJoin(«(1 .. degree).map([d | '''Iterable<T«d»> i«d»''']).join(", ")») {
                    return crossJoin(«(1 .. degree).map([d | '''seq(i«d»)''']).join(", ")»);
                }
            ''')
        }
        
        
        for (degree : 2 .. max) {
            out.append('''
            
                /**
                 * Cross join «degree» streams into one.
                 * <p>
                 * <code><pre>
                 * // (tuple(1, "a"), tuple(1, "b"), tuple(2, "a"), tuple(2, "b"))
                 * Seq.of(1, 2).crossJoin(Seq.of("a", "b"))
                 * </pre></code>
                 */
                @Generated("This method was generated using jOOQ-tools")
                static <«TN(degree)»> Seq<Tuple«degree»<«TN(degree)»>> crossJoin(«(1 .. degree).map([d | '''Seq<T«d»> s«d»''']).join(", ")») {
                    «IF degree > 2»
                    List<Tuple«degree - 1»<«TN(2, degree)»>> list = crossJoin(«XXXn(2, degree, "s")»).toList();
                    return s1.flatMap(v1 -> seq(list).map(t -> tuple(v1, «XXXn(1, degree - 1, "t.v")»)));
                    «ELSE»
                    List<T2> list = s2.toList();
                    return seq(s1).flatMap(v1 -> seq(list).map(v2 -> tuple(v1, v2)));
                    «ENDIF»
                }
            ''')
        }
        
        insert("org.jooq.lambda.Seq", out, "crossjoin-static")
    }
    
    def generateTuple() {
        write("org.jooq.lambda.tuple.Tuple", 
            '''
            «copyright»
            package org.jooq.lambda.tuple;
            
            import java.util.List;
            import java.util.stream.Collector;
            
            /**
             * A tuple.
             *
             * @author Lukas Eder
             */
            public interface Tuple extends Iterable<Object> {
            
                /**
                 * Construct a tuple of degree 0.
                 */
                static Tuple0 tuple() {
                    return new Tuple0();
                }
            «FOR degree : (1 .. max)»
            
                /**
                 * Construct a tuple of degree «degree».
                 */
                static <«TN(degree)»> Tuple«degree»<«TN(degree)»> tuple(«TN_XXXn(degree, "v")») {
                    return new Tuple«degree»<>(«vn(degree)»);
                }
            «ENDFOR»
            «FOR degree : (1 .. max)»
            
                /**
                 * Construct a tuple collector of degree «degree».
                 */
                static <T, «XXXn(degree, "A")», «XXXn(degree, "D")»> Collector<T, Tuple«degree»<«XXXn(degree, "A")»>, Tuple«degree»<«XXXn(degree, "D")»>> collectors(
                  «FOR d : (1 .. degree)»
                  «IF d > 1»,«ELSE» «ENDIF» Collector<T, A«d», D«d»> collector«d»
                  «ENDFOR»
                ) {
                    return Collector.<T, Tuple«degree»<«XXXn(degree, "A")»>, Tuple«degree»<«XXXn(degree, "D")»>>of(
                        () -> tuple(
                          «FOR d : (1 .. degree)»
                          «IF d > 1»,«ELSE» «ENDIF» collector«d».supplier().get()
                          «ENDFOR»
                        ),
                        (a, t) -> {
                            «FOR d : (1 .. degree)»
                            collector«d».accumulator().accept(a.v«d», t);
                            «ENDFOR»
                        },
                        (a1, a2) -> tuple(
                          «FOR d : (1 .. degree)»
                          «IF d > 1»,«ELSE» «ENDIF» collector«d».combiner().apply(a1.v«d», a2.v«d»)
                          «ENDFOR»
                        ),
                        a -> tuple(
                          «FOR d : (1 .. degree)»
                          «IF d > 1»,«ELSE» «ENDIF» collector«d».finisher().apply(a.v«d»)
                          «ENDFOR»
                        )
                    );
                }
            «ENDFOR»
            
                /**
                 * Create a new range.
                 */
                static <T extends Comparable<T>> Range<T> range(T t1, T t2) {
                    return new Range<>(t1, t2);
                }

                /**
                 * Get an array representation of this tuple.
                 */
                Object[] array();
            
                /**
                 * Get a list representation of this tuple.
                 */
                List<?> list();
            
                /**
                 * The degree of this tuple.
                 */
                int degree();
            }
            '''
        );
    }
    
    def generateTuples() {
        for (degree : 0 .. max) {
            write(
                "org.jooq.lambda.tuple.Tuple" + degree,
                '''
                «copyright»
                package org.jooq.lambda.tuple;
                
                import java.io.Serializable;
                import java.util.Arrays;
                import java.util.Iterator;
                import java.util.List;
                import java.util.Objects;
                «IF degree == 2»
                import java.util.Optional;
                «ENDIF»
                
                «IF degree != 1»
                import org.jooq.lambda.function.Function1;
                «ENDIF»
                import org.jooq.lambda.function.Function«degree»;
                
                /**
                 * A tuple of degree «degree».
                 *
                 * @author Lukas Eder
                 */
                public class Tuple«degree»«IF degree > 0»<«TN(degree)»>«ENDIF» implements Tuple, Comparable<Tuple«degree»«IF degree > 0»<«TN(degree)»>«ENDIF»>, Serializable, Cloneable {
                
                    private static final long serialVersionUID = 1L;
                    «IF degree > 0»

                    «FOR d : 1 .. degree»
                    public final T«d» v«d»;
                    «ENDFOR»
                    «FOR d : 1 .. degree»

                    public T«d» v«d»() {
                        return v«d»;
                    }
                    «ENDFOR»
                    «ENDIF»
                    «IF degree > 0»

                    public Tuple«degree»(Tuple«degree»<«TN(degree)»> tuple) {
                        «FOR d : 1 .. degree»
                        this.v«d» = tuple.v«d»;
                        «ENDFOR»
                    }
                
                    public Tuple«degree»(«TN_XXXn(degree, "v")») {
                        «FOR d : 1 .. degree»
                        this.v«d» = v«d»;
                        «ENDFOR»
                    }
                    «ELSE»

                    public Tuple«degree»(Tuple0 tuple) {
                    }

                    public Tuple«degree»() {
                    }
                    «ENDIF»
                    «IF degree < max»

                    /**
                     * Concatenate a value to this tuple.
                     */
                    public final <T«degree + 1»> Tuple«degree + 1»<«TN(degree + 1)»> concat(T«degree + 1» value) {
                        return new Tuple«degree + 1»<>(«IF degree > 0»«XXXn(degree, "v")», «ENDIF»value);
                    }
                    «FOR d : (degree + 1 .. max)»

                    /**
                     * Concatenate a tuple to this tuple.
                     */
                    public final <«TN(degree + 1, d)»> Tuple«d»<«TN(d)»> concat(Tuple«d - degree»<«TN(degree + 1, d)»> tuple) {
                        return new Tuple«d»<>(«IF degree > 0»«XXXn(degree, "v")», «ENDIF»«XXXn(d - degree, "tuple.v")»);
                    }
                    «ENDFOR»
                    «ENDIF»
                    «FOR d : (0 .. degree)»

                    /**
                     * Split this tuple into two tuples of degree «d» and «degree - d».
                     */
                    public final Tuple2<Tuple«d»«IF d > 0»<«TN(d)»>«ENDIF», Tuple«degree - d»«IF degree - d > 0»<«TN(d + 1, degree)»>«ENDIF»> split«d»() {
                        return new Tuple2<>(limit«d»(), skip«d»());
                    }
                    «ENDFOR»
                    «IF degree > 0»
                    «FOR d : (0 .. degree - 1)»

                    /**
                     * Limit this tuple to degree «d».
                     */
                    public final Tuple«d»«IF d > 0»<«TN(d)»>«ENDIF» limit«d»() {
                        return new Tuple«d»«IF d > 0»<>(«XXXn(d, "v")»)«ELSE»()«ENDIF»;
                    }
                    «ENDFOR»
                    «ENDIF»

                    /**
                     * Limit this tuple to degree «degree».
                     */
                    public final Tuple«degree»«IF degree > 0»<«TN(degree)»>«ENDIF» limit«degree»() {
                        return this;
                    }

                    /**
                     * Skip 0 degrees from this tuple.
                     */
                    public final Tuple«degree»«IF degree > 0»<«TN(degree)»>«ENDIF» skip0() {
                        return this;
                    }
                    «IF degree > 0»
                    «FOR d : (1 .. degree)»

                    /**
                     * Skip «d» degrees from this tuple.
                     */
                    public final Tuple«degree - d»«IF degree - d > 0»<«TN(d + 1, degree)»>«ENDIF» skip«d»() {
                        return new Tuple«degree - d»«IF degree - d > 0»<>(«XXXn(d + 1, degree, "v")»)«ELSE»()«ENDIF»;
                    }
                    «ENDFOR»
                    «ENDIF»
                    «IF degree == 2»

                    /**
                     * Get a tuple with the two attributes swapped.
                     */
                    public final Tuple2<T2, T1> swap() {
                        return new Tuple2<>(v2, v1);
                    }

                    /**
                     * Whether two tuples overlap.
                     * <p>
                     * <code><pre>
                     * // true
                     * range(1, 3).overlaps(range(2, 4))
                     *
                     * // false
                     * range(1, 3).overlaps(range(5, 8))
                     * </pre></code>
                     */
                    public static final <T extends Comparable<T>> boolean overlaps(Tuple2<T, T> left, Tuple2<T, T> right) {
                        return left.v1.compareTo(right.v2) <= 0
                            && left.v2.compareTo(right.v1) >= 0;
                    }

                    /**
                     * The intersection of two ranges.
                     * <p>
                     * <code><pre>
                     * // (2, 3)
                     * range(1, 3).intersect(range(2, 4))
                     *
                     * // none
                     * range(1, 3).intersect(range(5, 8))
                     * </pre></code>
                     */
                    public static final <T extends Comparable<T>> Optional<Tuple2<T, T>> intersect(Tuple2<T, T> left, Tuple2<T, T> right) {
                        if (overlaps(left, right))
                            return Optional.of(new Tuple2<>(
                                left.v1.compareTo(right.v1) >= 0 ? left.v1 : right.v1,
                                left.v2.compareTo(right.v2) <= 0 ? left.v2 : right.v2
                            ));
                        else
                            return Optional.empty();
                    }
                    «ENDIF»

                    /**
                     * Apply this tuple as arguments to a function.
                     */
                    public final <R> R map(Function«degree»<«IF degree > 0»«TN(degree)», «ENDIF»R> function) {
                        return function.apply(this);
                    }
                    «IF degree > 0»
                    «FOR d : 1 .. degree»

                    /**
                     * Apply attribute «d» as argument to a function and return a new tuple with the substituted argument.
                     */
                    public final <U«d»> Tuple«degree»<«TN(1, d - 1)»«IF d > 1», «ENDIF»U«d»«IF d < degree», «ENDIF»«TN(d + 1, degree)»> map«d»(Function1<? super T«d», ? extends U«d»> function) {
                        return Tuple.tuple(«vn(1, d - 1)»«IF d > 1», «ENDIF»function.apply(v«d»)«IF d < degree», «ENDIF»«vn(d + 1, degree)»);
                    }
                    «ENDFOR»
                    «ENDIF»

                    @Override
                    public final Object[] array() {
                        return new Object[] { «IF degree > 0»«vn(degree)»«ENDIF» };
                    }

                    @Override
                    public final List<?> list() {
                        return Arrays.asList(array());
                    }

                    /**
                     * The degree of this tuple: «degree».
                     */
                    @Override
                    public final int degree() {
                        return «degree»;
                    }
                
                    @Override
                    @SuppressWarnings("unchecked")
                    public final Iterator<Object> iterator() {
                        return (Iterator<Object>) list().iterator();
                    }
                
                    @Override
                    public int compareTo(Tuple«degree»«IF degree > 0»<«TN(degree)»>«ENDIF» other) {
                        int result = 0;
                        «IF degree > 0»

                        «FOR d : 1 .. degree»
                        result = Tuples.compare(v«d», other.v«d»); if (result != 0) return result;
                        «ENDFOR»

                        «ENDIF»
                        return result;
                    }
                
                    @Override
                    public boolean equals(Object o) {
                        if (this == o)
                            return true;
                        if (!(o instanceof Tuple«degree»))
                            return false;
                        «IF degree > 0»

                        @SuppressWarnings({ "unchecked", "rawtypes" })
                        final Tuple«degree»<«TN(degree)»> that = (Tuple«degree») o;

                        «FOR d : 1 .. degree»
                        if (!Objects.equals(v«d», that.v«d»)) return false;
                        «ENDFOR»
                        «ENDIF»
                
                        return true;
                    }
                
                    @Override
                    public int hashCode() {
                        final int prime = 31;
                        int result = 1;
                        «IF degree > 0»

                        «FOR d : 1 .. degree»
                        result = prime * result + ((v«d» == null) ? 0 : v«d».hashCode());
                        «ENDFOR»

                        «ENDIF»
                        return result;
                    }
                
                    @Override
                    public String toString() {
                        «IF degree == 0»
                        return "()";
                        «ELSE»
                        return "("
                             «FOR d : 1 .. degree»
                             + «IF d > 1»", " + «ELSE»       «ENDIF»v«d»
                             «ENDFOR»
                             + ")";
                        «ENDIF»
                    }
                
                    @Override
                    public Tuple«degree»«IF degree > 0»<«TN(degree)»>«ENDIF» clone() {
                        return new Tuple«degree»«IF degree > 0»<>«ENDIF»(this);
                    }
                }
                '''  
            );
        }
    }
    
    def generateFunctions() {
        for (degree : 1 .. max) {
            write(
                "org.jooq.lambda.function.Function" + degree,
                '''
                «copyright»
                package org.jooq.lambda.function;
                
                «IF degree == 1»
                import java.util.function.Function;
                «ENDIF»
                «IF degree == 2»
                import java.util.function.BiFunction;
                «ENDIF»
                
                «FOR d : (1 .. degree)»
                import org.jooq.lambda.tuple.Tuple«d»;
                «ENDFOR»

                /**
                 * A function with «degree» arguments
                 *
                 * @author Lukas Eder
                 */
                @FunctionalInterface
                public interface Function«degree»<«TN(degree)», R> «IF degree == 1»extends Function<T1, R> «ELSEIF degree == 2»extends BiFunction<T1, T2, R> «ENDIF»{
                
                    /**
                     * Apply this function to the arguments.
                     *
                     * @param args The arguments as a tuple.
                     */
                    default R apply(Tuple«degree»<«TN(degree)»> args) {
                        return apply(«XXXn(degree, "args.v")»);
                    }
                
                    /**
                     * Apply this function to the arguments.
                     */
                    «IF degree <= 2»
                    @Override
                    «ENDIF»
                    R apply(«TN_XXXn(degree, "v")»);
                    «IF degree == 1»

                    /**
                     * Convert this function to a {@link java.util.function.Function}
                     */
                    default Function<T1, R> toFunction() {
                        return this::apply;
                    }

                    /**
                     * Convert to this function from a {@link java.util.function.Function}
                     */
                    static <T1, R> Function1<T1, R> from(Function<T1, R> function) {
                        return function::apply;
                    }
                    «ELSEIF degree == 2»

                    /**
                     * Convert this function to a {@link java.util.function.BiFunction}
                     */
                    default BiFunction<T1, T2, R> toBiFunction() {
                        return this::apply;
                    }

                    /**
                     * Convert to this function to a {@link java.util.function.BiFunction}
                     */
                    static <T1, T2, R> Function2<T1, T2, R> from(BiFunction<T1, T2, R> function) {
                        return function::apply;
                    }
                    «ENDIF»
                    «IF degree > 0»
                    «FOR d : (1 .. degree)»

                    /**
                     * Partially apply this function to the arguments.
                     */
                    default Function«degree - d»<«IF degree - d > 0»«TN(d + 1, degree)», «ENDIF»R> curry(«TN_XXXn(d, "v")») {
                        return («XXXn(d + 1, degree, "v")») -> apply(«XXXn(degree, "v")»);
                    }
                    «ENDFOR»
                    «FOR d : (1 .. degree)»

                    /**
                     * Partially apply this function to the arguments.
                     */
                    default Function«degree - d»<«IF degree - d > 0»«TN(d + 1, degree)», «ENDIF»R> curry(Tuple«d»<«TN(d)»> args) {
                        return («XXXn(d + 1, degree, "v")») -> apply(«XXXn(d, "args.v")»«IF degree - d > 0», «XXXn(d + 1, degree, "v")»«ENDIF»);
                    }
                    «ENDFOR»
                    «ENDIF»
                }
                '''
            )
        }
    }
}