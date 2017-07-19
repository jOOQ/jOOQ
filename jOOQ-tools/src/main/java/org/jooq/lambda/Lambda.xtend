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
package org.jooq.lambda

import org.jooq.xtend.Generators

/**
 * @author Lukas Eder
 */

class Lambda extends Generators {
    
    def static void main(String[] args) {
        val lambda = new Lambda();
        
        lambda.generateTuple();
        lambda.generateTuples();
        lambda.generateConsumers();
        lambda.generateFunctions();
        lambda.generateCollect();
        lambda.generateWindows();
        lambda.generateZipStatic();
        lambda.generateZipAllStatic();
        lambda.generateCrossApplyStatic();
        lambda.generateOuterApplyStatic();
        lambda.generateCrossJoinStatic();
    }
    
    def max() {
        16;
    }
    
    def copyright() {
        '''
        /**
         * Copyright (c) 2014-2017, Data Geekery GmbH, contact@datageekery.com
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
    
    def generateCollect() {
        val out = new StringBuilder();
        
        for (degree : 2 .. max) {
            out.append('''
            
                /**
                 * Collect this collectable into «degree» {@link Collector}s.
                 */
                @Generated("This method was generated using jOOQ-tools")
                default <«XXXn(degree, "R")», «XXXn(degree, "A")»> Tuple«degree»<«XXXn(degree, "R")»> collect(
                    «FOR d : 1 .. degree»
                    Collector<? super T, A«d», R«d»> collector«d»«IF d < degree»,«ENDIF»
                    «ENDFOR»
                ) {
                    return collect(Tuple.collectors(«XXXn(degree, "collector")»));
                }
            ''')
        }
        
        insert("org.jooq.lambda.Collectable", out, "collect")
    }
    
    def generateWindows() {
        val out = new StringBuilder();
        
        for (degree : 1 .. max) {
            out.append('''
            
                /**
                 * Map this stream to a windowed stream with «degree» distinct windows.
                 */
                @Generated("This method was generated using jOOQ-tools")
                default Seq<Tuple«degree»<«FOR d : 1 .. degree SEPARATOR ', '»Window<T>«ENDFOR»>> window(
                    «FOR d : 1 .. degree»
                    WindowSpecification<T> specification«d»«IF d < degree»,«ENDIF»
                    «ENDFOR»
                ) {
                    List<Tuple2<T, Long>> buffer = zipWithIndex().toList();

                    «FOR d : 1 .. degree»
                    Map<?, Partition<T>> partitions«d» = SeqUtils.partitions(specification«d», buffer);
                    «ENDFOR»

                    return seq(buffer)
                          .map(t -> tuple(
                               «FOR d : 1 .. degree»
                               (Window<T>) new WindowImpl<>(t, partitions«d».get(specification«d».partition().apply(t.v1)), specification«d»)«IF d < degree»,«ENDIF»
                               «ENDFOR»
                          ))
                          .onClose(this::close);
                }
            ''')
        }
        
        insert("org.jooq.lambda.Seq", out, "windows")
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
                static <«TN(degree)»> Seq<Tuple«degree»<«TN(degree)»>> zip(«(1 .. degree).map([d | '''Stream<? extends T«d»> s«d»''']).join(", ")») {
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
                static <«TN(degree)»> Seq<Tuple«degree»<«TN(degree)»>> zip(«(1 .. degree).map([d | '''Iterable<? extends T«d»> i«d»''']).join(", ")») {
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
                static <«TN(degree)»> Seq<Tuple«degree»<«TN(degree)»>> zip(«(1 .. degree).map([d | '''Seq<? extends T«d»> s«d»''']).join(", ")») {
                    return zip(«XXXn(degree, "s")», («XXXn(degree, "t")») -> tuple(«XXXn(degree, "t")»))
                          .onClose(SeqUtils.closeAll(«XXXn(1, degree, "s")»));
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
                static <«TN(degree)», R> Seq<R> zip(«(1 .. degree).map([d | '''Stream<? extends T«d»> s«d»''']).join(", ")», «IF degree == 2»BiFunction«ELSE»Function«degree»«ENDIF»<«XXXn(degree, "? super T")», ? extends R> zipper) {
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
                static <«TN(degree)», R> Seq<R> zip(«(1 .. degree).map([d | '''Iterable<? extends T«d»> i«d»''']).join(", ")», «IF degree == 2»BiFunction«ELSE»Function«degree»«ENDIF»<«XXXn(degree, "? super T")», ? extends R> zipper) {
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
                static <«TN(degree)», R> Seq<R> zip(«(1 .. degree).map([d | '''Seq<? extends T«d»> s«d»''']).join(", ")», «IF degree == 2»BiFunction«ELSE»Function«degree»«ENDIF»<«XXXn(degree, "? super T")», ? extends R> zipper) {
                    «FOR d : (1 .. degree)»
                    final Iterator<? extends T«d»> it«d» = s«d».iterator();
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
    
    def generateZipAllStatic() {
        val out = new StringBuilder();
        
        for (degree : 2 .. max) {
            out.append('''
            
                /**
                 * Zip two streams into one - by storing the corresponding elements from them in a tuple,
                 * when one of streams will end - a default value for that stream will be provided instead -
                 * so the resulting stream will be as long as the longest of the two streams.
                 * <p>
                 * <code><pre>
                 * // (tuple(1, "a"), tuple(2, "x"), tuple(3, "x"))
                 * Seq.zipAll(Seq.of(1, 2, 3), Seq.of("a"), 0, "x")
                 * </pre></code>
                 */
                static <«TN(degree)»> Seq<Tuple«degree»<«TN(degree)»>> zipAll(«(1 .. degree).map([d | '''Stream<? extends T«d»> s«d»''']).join(", ")», «(1 .. degree).map([d | '''T«d» default«d»''']).join(", ")») {
                    return zipAll(«XXXn(degree, "s")», «XXXn(degree, "default")», Tuple::tuple);
                }
            ''')
        }
        
        for (degree : 2 .. max) {
            out.append('''
            
                /**
                 * Zip two streams into one - by storing the corresponding elements from them in a tuple,
                 * when one of streams will end - a default value for that stream will be provided instead -
                 * so the resulting stream will be as long as the longest of the two streams.
                 * <p>
                 * <code><pre>
                 * // (tuple(1, "a"), tuple(2, "x"), tuple(3, "x"))
                 * Seq.zipAll(Seq.of(1, 2, 3), Seq.of("a"), 0, "x")
                 * </pre></code>
                 */
                static <«TN(degree)»> Seq<Tuple«degree»<«TN(degree)»>> zipAll(«(1 .. degree).map([d | '''Iterable<? extends T«d»> s«d»''']).join(", ")», «(1 .. degree).map([d | '''T«d» default«d»''']).join(", ")») {
                    return zipAll(«XXXn(degree, "s")», «XXXn(degree, "default")», Tuple::tuple);
                }
            ''')
        }
        
        for (degree : 2 .. max) {
            out.append('''
            
                /**
                 * Zip two streams into one - by storing the corresponding elements from them in a tuple,
                 * when one of streams will end - a default value for that stream will be provided instead -
                 * so the resulting stream will be as long as the longest of the two streams.
                 * <p>
                 * <code><pre>
                 * // (tuple(1, "a"), tuple(2, "x"), tuple(3, "x"))
                 * Seq.zipAll(Seq.of(1, 2, 3), Seq.of("a"), 0, "x")
                 * </pre></code>
                 */
                static <«TN(degree)»> Seq<Tuple«degree»<«TN(degree)»>> zipAll(«(1 .. degree).map([d | '''Seq<? extends T«d»> s«d»''']).join(", ")», «(1 .. degree).map([d | '''T«d» default«d»''']).join(", ")») {
                    return zipAll(«XXXn(degree, "s")», «XXXn(degree, "default")», Tuple::tuple);
                }
            ''')
        }
        
        for (degree : 2 .. max) {
            out.append('''
            
                /**
                 * Zip two streams into one - by storing the corresponding elements from them in a tuple,
                 * when one of streams will end - a default value for that stream will be provided instead -
                 * so the resulting stream will be as long as the longest of the two streams.
                 * <p>
                 * <code><pre>
                 * // (tuple(1, "a"), tuple(2, "x"), tuple(3, "x"))
                 * Seq.zipAll(Seq.of(1, 2, 3), Seq.of("a"), 0, "x")
                 * </pre></code>
                 */
                static <«TN(degree)», R> Seq<R> zipAll(«(1 .. degree).map([d | '''Stream<? extends T«d»> s«d»''']).join(", ")», «(1 .. degree).map([d | '''T«d» default«d»''']).join(", ")», «IF degree == 2»BiFunction«ELSE»Function«degree»«ENDIF»<«XXXn(degree, "? super T")», ? extends R> zipper) {
                    return zipAll(«(1 .. degree).map([d | '''seq(s«d»)''']).join(", ")», «XXXn(degree, "default")», zipper);
                }
            ''')
        }
                
        for (degree : 2 .. max) {
            out.append('''
            
                /**
                 * Zip two streams into one - by storing the corresponding elements from them in a tuple,
                 * when one of streams will end - a default value for that stream will be provided instead -
                 * so the resulting stream will be as long as the longest of the two streams.
                 * <p>
                 * <code><pre>
                 * // (tuple(1, "a"), tuple(2, "x"), tuple(3, "x"))
                 * Seq.zipAll(Seq.of(1, 2, 3), Seq.of("a"), 0, "x")
                 * </pre></code>
                 */
                static <«TN(degree)», R> Seq<R> zipAll(«(1 .. degree).map([d | '''Iterable<? extends T«d»> s«d»''']).join(", ")», «(1 .. degree).map([d | '''T«d» default«d»''']).join(", ")», «IF degree == 2»BiFunction«ELSE»Function«degree»«ENDIF»<«XXXn(degree, "? super T")», ? extends R> zipper) {
                    return zipAll(«(1 .. degree).map([d | '''seq(s«d»)''']).join(", ")», «XXXn(degree, "default")», zipper);
                }
            ''')
        }
        
        for (degree : 2 .. max) {
            out.append('''

                /**
                 * Zip two streams into one using a {@link BiFunction} to produce resulting values,
                 * when one of streams will end, a default value for that stream will be provided instead -
                 * so the resulting stream will be as long as the longest of the two streams.
                 * <p>
                 * <code><pre>
                 * // ("1:a", "2:x", "3:x")
                 * Seq.zipAll(Seq.of(1, 2, 3), Seq.of("a"), 0, "x", (i, s) -> i + ":" + s)
                 * </pre></code>
                 */
                static <«TN(degree)», R> Seq<R> zipAll(«(1 .. degree).map([d | '''Seq<? extends T«d»> s«d»''']).join(", ")», «(1 .. degree).map([d | '''T«d» default«d»''']).join(", ")», «IF degree == 2»BiFunction«ELSE»Function«degree»«ENDIF»<«XXXn(degree, "? super T")», ? extends R> zipper) {
                    «FOR d : 1 .. degree»
                    final Iterator<? extends T«d»> it«d» = s«d».iterator();
                    «ENDFOR»

                    class ZipAll implements Iterator<R> {
                        
                        @Override
                        public boolean hasNext() {
                            return «(1 .. degree).map([d | '''it«d».hasNext()''']).join(" || ")»;
                        }

                        @Override
                        public R next() {
                            «FOR d : 1 .. degree»
                            boolean b«d» = it«d».hasNext();
                            «ENDFOR»
                            
                            if («(1 .. degree).map([d | '''!b«d»''']).join(" && ")»)
                                throw new NoSuchElementException("next on empty iterator");
                            
                            return zipper.apply(
                                «FOR d : 1 .. degree»
                                b«d» ? it«d».next() : default«d»«IF d < degree»,«ENDIF»
                                «ENDFOR»
                            );
                        }
                    }
            
                    return seq(new ZipAll()).onClose(SeqUtils.closeAll(«XXXn(1, degree, "s")»));
                }            
            ''')
        }
        
        insert("org.jooq.lambda.Seq", out, "zip-all-static")
    }
    
    def generateCrossApplyStatic() {
        val out = new StringBuilder();
        
        for (degree : 2 .. max) {
            out.append('''
            
                /**
                 * Cross apply «degree» functions to a stream.
                 * <p>
                 * <code><pre>
                 * // (tuple(1, 0), tuple(2, 0), tuple(2, 1))
                 * Seq.of(1, 2).crossApply(t -> Seq.range(0, t))
                 * </pre></code>
                 */
                @Generated("This method was generated using jOOQ-tools")
                static <«TN(degree)»> Seq<Tuple«degree»<«TN(degree)»>> crossApply(Stream<? extends T1> stream, «(2 .. degree).map([d | '''Function<? super T«d - 1», ? extends Stream<? extends T«d»>> function«d»''']).join(", ")») {
                    return crossApply(seq(stream), «(2 .. degree).map([d | '''t -> seq(function«d».apply(t))''']).join(", ")»);
                }
            ''')
        }
        
        for (degree : 2 .. max) {
            out.append('''
            
                /**
                 * Cross apply «degree» functions to a stream.
                 * <p>
                 * <code><pre>
                 * // (tuple(1, 0), tuple(2, 0), tuple(2, 1))
                 * Seq.of(1, 2).crossApply(t -> Seq.range(0, t))
                 * </pre></code>
                 */
                @Generated("This method was generated using jOOQ-tools")
                static <«TN(degree)»> Seq<Tuple«degree»<«TN(degree)»>> crossApply(Iterable<? extends T1> iterable, «(2 .. degree).map([d | '''Function<? super T«d - 1», ? extends Iterable<? extends T«d»>> function«d»''']).join(", ")») {
                    return crossApply(seq(iterable), «(2 .. degree).map([d | '''t -> seq(function«d».apply(t))''']).join(", ")»);
                }
            ''')
        }
        
        
        for (degree : 2 .. max) {
            out.append('''
            
                /**
                 * Cross apply «degree» functions to a stream.
                 * <p>
                 * <code><pre>
                 * // (tuple(1, 0), tuple(2, 0), tuple(2, 1))
                 * Seq.of(1, 2).crossApply(t -> Seq.range(0, t))
                 * </pre></code>
                 */
                @Generated("This method was generated using jOOQ-tools")
                static <«TN(degree)»> Seq<Tuple«degree»<«TN(degree)»>> crossApply(Seq<? extends T1> seq, «(2 .. degree).map([d | '''Function<? super T«d - 1», ? extends Seq<? extends T«d»>> function«d»''']).join(", ")») {
                    return seq.flatMap(t1 -> function2.apply(t1).map(t2 -> tuple(t1, t2)))
                              «IF degree > 2»
                              «FOR d : 3 .. degree»
                              .flatMap(t -> function«d».apply(t.v«d - 1»).map(t«d» -> t.concat(t«d»)))
                              «ENDFOR»
                              «ENDIF»
                              .onClose(seq::close);
                }
            ''')
        }
        
        insert("org.jooq.lambda.Seq", out, "crossapply-static")
    }
    
    def generateOuterApplyStatic() {
        val out = new StringBuilder();
        
        for (degree : 2 .. max) {
            out.append('''
            
                /**
                 * Outer apply «degree» functions to a stream.
                 * <p>
                 * <code><pre>
                 * // (tuple(0, null), tuple(1, 0), tuple(2, 0), tuple(2, 1))
                 * Seq.of(0, 1, 2).outerApply(t -> Seq.range(0, t))
                 * </pre></code>
                 */
                @Generated("This method was generated using jOOQ-tools")
                static <«TN(degree)»> Seq<Tuple«degree»<«TN(degree)»>> outerApply(Stream<? extends T1> stream, «(2 .. degree).map([d | '''Function<? super T«d - 1», ? extends Stream<? extends T«d»>> function«d»''']).join(", ")») {
                    return outerApply(seq(stream), «(2 .. degree).map([d | '''t -> seq(function«d».apply(t))''']).join(", ")»);
                }
            ''')
        }
        
        for (degree : 2 .. max) {
            out.append('''
            
                /**
                 * Outer apply «degree» functions to a stream.
                 * <p>
                 * <code><pre>
                 * // (tuple(0, null), tuple(1, 0), tuple(2, 0), tuple(2, 1))
                 * Seq.of(0, 1, 2).outerApply(t -> Seq.range(0, t))
                 * </pre></code>
                 */
                @Generated("This method was generated using jOOQ-tools")
                static <«TN(degree)»> Seq<Tuple«degree»<«TN(degree)»>> outerApply(Iterable<? extends T1> iterable, «(2 .. degree).map([d | '''Function<? super T«d - 1», ? extends Iterable<? extends T«d»>> function«d»''']).join(", ")») {
                    return outerApply(seq(iterable), «(2 .. degree).map([d | '''t -> seq(function«d».apply(t))''']).join(", ")»);
                }
            ''')
        }
        
        
        for (degree : 2 .. max) {
            out.append('''
            
                /**
                 * Outer apply «degree» functions to a stream.
                 * <p>
                 * <code><pre>
                 * // (tuple(0, null), tuple(1, 0), tuple(2, 0), tuple(2, 1))
                 * Seq.of(0, 1, 2).outerApply(t -> Seq.range(0, t))
                 * </pre></code>
                 */
                @Generated("This method was generated using jOOQ-tools")
                static <«TN(degree)»> Seq<Tuple«degree»<«TN(degree)»>> outerApply(Seq<? extends T1> seq, «(2 .. degree).map([d | '''Function<? super T«d - 1», ? extends Seq<? extends T«d»>> function«d»''']).join(", ")») {
                    return seq.flatMap(t1 -> function2.apply(t1).onEmpty(null).map(t2 -> tuple(t1, t2)))
                              «IF degree > 2»
                              «FOR d : 3 .. degree»
                              .flatMap(t -> function«d».apply(t.v«d - 1»).onEmpty(null).map(t«d» -> t.concat(t«d»)))
                              «ENDFOR»
                              «ENDIF»
                              .onClose(seq::close);
                }
            ''')
        }
        
        insert("org.jooq.lambda.Seq", out, "outerapply-static")
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
                static <«TN(degree)»> Seq<Tuple«degree»<«TN(degree)»>> crossJoin(«(1 .. degree).map([d | '''Stream<? extends T«d»> s«d»''']).join(", ")») {
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
                static <«TN(degree)»> Seq<Tuple«degree»<«TN(degree)»>> crossJoin(«(1 .. degree).map([d | '''Iterable<? extends T«d»> i«d»''']).join(", ")») {
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
                static <«TN(degree)»> Seq<Tuple«degree»<«TN(degree)»>> crossJoin(«(1 .. degree).map([d | '''Seq<? extends T«d»> s«d»''']).join(", ")») {
                    «IF degree > 2»
                    List<Tuple«degree - 1»<«TN(2, degree)»>> list = crossJoin(«XXXn(2, degree, "s")»).toList();
                    return s1.flatMap(v1 -> seq(list).map(t -> tuple(v1, «XXXn(1, degree - 1, "t.v")»)))
                             .onClose(SeqUtils.closeAll(«XXXn(2, degree, "s")»));
                    «ELSE»
                    List<? extends T2> list = s2.toList();
                    return seq(s1).flatMap(v1 -> seq(list).map(v2 -> tuple(v1, v2)))
                                  .onClose(SeqUtils.closeAll(s1, s2));
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
            import java.util.Map;
            import java.util.stream.Collector;
            import java.util.function.BiConsumer;
            import java.util.function.BiFunction;
            import java.util.function.Consumer;
            import java.util.function.Function;
            import java.util.function.Supplier;
            
            import org.jooq.lambda.Seq;
            «FOR degree : (0 .. max)»
            import org.jooq.lambda.function.Consumer«degree»;
            «ENDFOR»
            «FOR degree : (0 .. max)»
            import org.jooq.lambda.function.Function«degree»;
            «ENDFOR»
            
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
            «FOR degree : (0 .. max)»
            
                /**
                 * Construct a tuple function of degree «degree».
                 */
                static <«IF degree > 0»«TN(degree)», «ENDIF»R> Function1<Tuple«degree»«IF degree > 0»<«TN(degree)»>«ENDIF», R> function(«IF degree == 0»Supplier«ELSEIF degree == 1»Function«ELSEIF degree == 2»BiFunction«ELSE»Function«degree»«ENDIF»<«IF degree > 0»«TN(degree)», «ENDIF»R> function) {
                    «IF degree == 0»
                    return t -> function.get();
                    «ELSE»
                    return t -> function.apply(«XXXn(degree, "t.v")»);
                    «ENDIF»
                }
            «ENDFOR»
            «FOR degree : (0 .. max)»
            
                /**
                 * Construct a tuple consumer of degree «degree».
                 */
                static «IF degree > 0»<«TN(degree)»> «ENDIF»Consumer1<Tuple«degree»«IF degree > 0»<«TN(degree)»>«ENDIF»> consumer(«IF degree == 0»Runnable«ELSEIF degree == 1»Consumer«ELSEIF degree == 2»BiConsumer«ELSE»Consumer«degree»«ENDIF»«IF degree > 0»<«TN(degree)»>«ENDIF» consumer) {
                    «IF degree == 0»
                    return t -> consumer.run();
                    «ELSE»
                    return t -> consumer.accept(«XXXn(degree, "t.v")»);
                    «ENDIF»
                }
            «ENDFOR»
            «FOR degree : (1 .. max)»
            
                /**
                 * Construct a tuple collector of degree «degree».
                 */
                static <T, «XXXn(degree, "A")», «XXXn(degree, "D")»> Collector<T, Tuple«degree»<«XXXn(degree, "A")»>, Tuple«degree»<«XXXn(degree, "D")»>> collectors(
                  «FOR d : (1 .. degree)»
                  «IF d > 1»,«ELSE» «ENDIF» Collector<? super T, A«d», D«d»> collector«d»
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
                 *
                 * @deprecated - Use {@link #toArray()} instead.
                 */
                @Deprecated
                Object[] array();
            
                /**
                 * Get an array representation of this tuple.
                 */
                Object[] toArray();
            
                /**
                 * Get a list representation of this tuple.
                 *
                 * @deprecated - Use {@link #toList()} instead.
                 */
                @Deprecated
                List<?> list();

                /**
                 * Get a list representation of this tuple.
                 */
                List<?> toList();
            
                /**
                 * Get a Seq representation of this tuple.
                 */
                Seq<?> toSeq();
            
                /**
                 * Get a map representation of this tuple.
                 */
                Map<String, ?> toMap();

                /**
                 * Get a map representation of this tuple.
                 */
                <K> Map<K, ?> toMap(Function<? super Integer, ? extends K> keyMapper);

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
                import java.util.LinkedHashMap;
                import java.util.List;
                import java.util.Map;
                «IF degree > 0»
                import java.util.Objects;
                «ENDIF»
                «IF degree == 2»
                import java.util.Optional;
                import java.util.function.BiFunction;
                «ENDIF»
                import java.util.function.Function;
                «IF degree != 1»
                import java.util.function.Supplier;
                «ENDIF»

                import org.jooq.lambda.Seq;                
                «IF degree > 1»
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
                    public static final <T extends Comparable<? super T>> boolean overlaps(Tuple2<T, T> left, Tuple2<T, T> right) {
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
                    public static final <T extends Comparable<? super T>> Optional<Tuple2<T, T>> intersect(Tuple2<T, T> left, Tuple2<T, T> right) {
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
                    public final <R> R map(«function(degree)»<«IF degree > 0»«XXXn(degree, "? super T")», «ENDIF»? extends R> function) {
                        return function.«apply(degree)»(«IF degree > 0»«XXXn(degree, "v")»«ENDIF»);
                    }
                    «IF degree > 0»
                    «FOR d : 1 .. degree»

                    /**
                     * Apply attribute «d» as argument to a function and return a new tuple with the substituted argument.
                     */
                    public final <U«d»> Tuple«degree»<«TN(1, d - 1)»«IF d > 1», «ENDIF»U«d»«IF d < degree», «ENDIF»«TN(d + 1, degree)»> map«d»(Function<? super T«d», ? extends U«d»> function) {
                        return Tuple.tuple(«vn(1, d - 1)»«IF d > 1», «ENDIF»function.apply(v«d»)«IF d < degree», «ENDIF»«vn(d + 1, degree)»);
                    }
                    «ENDFOR»
                    «ENDIF»

                    @Override
                    @Deprecated
                    public final Object[] array() {
                        return toArray();
                    }

                    @Override
                    public final Object[] toArray() {
                        return new Object[] { «IF degree > 0»«vn(degree)»«ENDIF» };
                    }

                    @Override
                    @Deprecated
                    public final List<?> list() {
                        return toList();
                    }

                    @Override
                    public final List<?> toList() {
                        return Arrays.asList(toArray());
                    }

                    @Override
                    public final Seq<?> toSeq() {
                        return Seq.seq(toList());
                    }

                    @Override
                    public final Map<String, ?> toMap() {
                        return toMap(i -> "v" + (i + 1));
                    }

                    @Override
                    public final <K> Map<K, ?> toMap(Function<? super Integer, ? extends K> keyMapper) {
                        Map<K, Object> result = new LinkedHashMap<>();
                        Object[] array = toArray();

                        for (int i = 0; i < array.length; i++)
                            result.put(keyMapper.apply(i), array[i]);

                        return result;
                    }
                    «IF degree > 1»

                    public final <K> Map<K, ?> toMap(
                        «FOR d : 1 .. degree»
                        Supplier<? extends K> keySupplier«d»«IF d < degree», «ENDIF»
                        «ENDFOR»
                    ) {
                        Map<K, Object> result = new LinkedHashMap<>();
                        
                        «FOR d : 1 .. degree»
                        result.put(keySupplier«d».get(), v«d»);
                        «ENDFOR»
                        
                        return result;
                    }

                    public final <K> Map<K, ?> toMap(
                        «FOR d : 1 .. degree»
                        K key«d»«IF d < degree», «ENDIF»
                        «ENDFOR»
                    ) {
                        Map<K, Object> result = new LinkedHashMap<>();
                        
                        «FOR d : 1 .. degree»
                        result.put(key«d», v«d»);
                        «ENDFOR»
                        
                        return result;
                    }
                    «ENDIF»

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
    
    def generateConsumers() {
        for (degree : 1 .. max) {
            write(
                "org.jooq.lambda.function.Consumer" + degree,
                '''
                «copyright»
                package org.jooq.lambda.function;
                
                «IF degree == 1»
                import java.util.function.Consumer;
                «ENDIF»
                «IF degree == 2»
                import java.util.function.BiConsumer;
                «ENDIF»
                
                «FOR d : (1 .. degree)»
                import org.jooq.lambda.tuple.Tuple«d»;
                «ENDFOR»

                /**
                 * A consumer with «degree» arguments.
                 *
                 * @author Lukas Eder
                 */
                @FunctionalInterface
                public interface Consumer«degree»<«TN(degree)»> «IF degree == 1»extends Consumer<T1> «ELSEIF degree == 2»extends BiConsumer<T1, T2> «ENDIF»{
                
                    /**
                     * Performs this operation on the given argument.
                     *
                     * @param args The arguments as a tuple.
                     */
                    default void accept(Tuple«degree»<«XXXn(degree, "? extends T")»> args) {
                        accept(«XXXn(degree, "args.v")»);
                    }
                
                    /**
                     * Performs this operation on the given argument.
                     */
                    «IF degree <= 2»
                    @Override
                    «ENDIF»
                    void accept(«TN_XXXn(degree, "v")»);
                    «IF degree == 1»

                    /**
                     * Convert this consumer to a {@link java.util.function.Consumer}.
                     */
                    default Consumer<T1> toConsumer() {
                        return this::accept;
                    }

                    /**
                     * Convert to this consumer from a {@link java.util.function.Consumer}.
                     */
                    static <T1> Consumer1<T1> from(Consumer<? super T1> consumer) {
                        return consumer::accept;
                    }
                    «ELSEIF degree == 2»

                    /**
                     * Convert this consumer to a {@link java.util.function.BiConsumer}.
                     */
                    default BiConsumer<T1, T2> toBiConsumer() {
                        return this::accept;
                    }

                    /**
                     * Convert to this consumer to a {@link java.util.function.BiConsumer}.
                     */
                    static <T1, T2> Consumer2<T1, T2> from(BiConsumer<? super T1, ? super T2> consumer) {
                        return consumer::accept;
                    }
                    «ENDIF»
                    «IF degree > 0»
                    «FOR d : (1 .. degree)»

                    /**
                     * Let this consumer partially accept the arguments.
                     */
                    default Consumer«degree - d»«IF degree - d > 0»<«TN(d + 1, degree)»>«ENDIF» acceptPartially(«TN_XXXn(d, "v")») {
                        return («XXXn(d + 1, degree, "v")») -> accept(«XXXn(degree, "v")»);
                    }
                    «ENDFOR»
                    «FOR d : (1 .. degree)»

                    /**
                     * Let this consumer partially accept the arguments.
                     */
                    default Consumer«degree - d»«IF degree - d > 0»<«TN(d + 1, degree)»>«ENDIF» acceptPartially(Tuple«d»<«XXXn(d, "? extends T")»> args) {
                        return («XXXn(d + 1, degree, "v")») -> accept(«XXXn(d, "args.v")»«IF degree - d > 0», «XXXn(d + 1, degree, "v")»«ENDIF»);
                    }
                    «ENDFOR»
                    «ENDIF»
                }
                '''
            )
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
                 * A function with «degree» arguments.
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
                    default R apply(Tuple«degree»<«XXXn(degree, "? extends T")»> args) {
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
                     * Convert this function to a {@link java.util.function.Function}.
                     */
                    default Function<T1, R> toFunction() {
                        return this::apply;
                    }

                    /**
                     * Convert to this function from a {@link java.util.function.Function}.
                     */
                    static <T1, R> Function1<T1, R> from(Function<? super T1, ? extends R> function) {
                        return function::apply;
                    }
                    «ELSEIF degree == 2»

                    /**
                     * Convert this function to a {@link java.util.function.BiFunction}.
                     */
                    default BiFunction<T1, T2, R> toBiFunction() {
                        return this::apply;
                    }

                    /**
                     * Convert to this function to a {@link java.util.function.BiFunction}.
                     */
                    static <T1, T2, R> Function2<T1, T2, R> from(BiFunction<? super T1, ? super T2, ? extends R> function) {
                        return function::apply;
                    }
                    «ENDIF»
                    «IF degree > 0»
                    «FOR d : (1 .. degree)»

                    /**
                     * Partially apply this function to the arguments.
                     */
                    default Function«degree - d»<«IF degree - d > 0»«TN(d + 1, degree)», «ENDIF»R> applyPartially(«TN_XXXn(d, "v")») {
                        return («XXXn(d + 1, degree, "v")») -> apply(«XXXn(degree, "v")»);
                    }
                    «ENDFOR»
                    «FOR d : (1 .. degree)»

                    /**
                     * Partially apply this function to the arguments.
                     */
                    default Function«degree - d»<«IF degree - d > 0»«TN(d + 1, degree)», «ENDIF»R> applyPartially(Tuple«d»<«XXXn(d, "? extends T")»> args) {
                        return («XXXn(d + 1, degree, "v")») -> apply(«XXXn(d, "args.v")»«IF degree - d > 0», «XXXn(d + 1, degree, "v")»«ENDIF»);
                    }
                    «ENDFOR»
                    «FOR d : (1 .. degree)»

                    /**
                     * Partially apply this function to the arguments.
                     *
                     * @deprecated - Use {@link #applyPartially(«(1..d).map["Object"].join(", ")»)} instead.
                     */
                    @Deprecated
                    default Function«degree - d»<«IF degree - d > 0»«TN(d + 1, degree)», «ENDIF»R> curry(«TN_XXXn(d, "v")») {
                        return («XXXn(d + 1, degree, "v")») -> apply(«XXXn(degree, "v")»);
                    }
                    «ENDFOR»
                    «FOR d : (1 .. degree)»

                    /**
                     * Partially apply this function to the arguments.
                     *
                     * @deprecated - Use {@link #applyPartially(Tuple«d»)} instead.
                     */
                    @Deprecated
                    default Function«degree - d»<«IF degree - d > 0»«TN(d + 1, degree)», «ENDIF»R> curry(Tuple«d»<«XXXn(d, "? extends T")»> args) {
                        return («XXXn(d + 1, degree, "v")») -> apply(«XXXn(d, "args.v")»«IF degree - d > 0», «XXXn(d + 1, degree, "v")»«ENDIF»);
                    }
                    «ENDFOR»                    «ENDIF»
                }
                '''
            )
        }
    }
    
    def function(int degree) {
        return if (degree == 0) "Supplier" 
          else if (degree == 1) "Function"
          else if (degree == 2) "BiFunction"
          else                  "Function" + degree;
    }
    
    def apply(int degree) {
        return if (degree == 0) "get"
          else                  "apply";
    }
}