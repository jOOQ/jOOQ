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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jooq.impl.DSL;

/**
 * An auxiliary class for constructing {@link Row} collections.
 *
 * @author Dmitry Baev
 * @author Lukas Eder
 */
public final class Rows {



    /**
     * Create a collector that can collect into an array of {@link RowN}.
     */
    @SafeVarargs
    public static <T> Collector<? super T, ?, RowN[]> collectingArray(
        Function<? super T, ?>... functions
    ) {
        return Collectors.collectingAndThen(collecting(functions), l -> l.toArray(RowN[]::new));
    }

    /**
     * Create a collector that can collect into a list of {@link RowN}.
     */
    @SafeVarargs
    public static <T, T1> Collector<? super T, ?, List<RowN>> collecting(
        Function<? super T, ?>... functions
    ) {
        return Collector.of(
            ArrayList::new,
            (l, t) -> l.add(DSL.row(Stream.of(functions).map(f -> f.apply(t)).toArray())),
            (t1, t2) -> {
                t1.addAll(t2);
                return t1;
            }
        );
    }



    /**
     * Create a collector that can collect into an array of {@link Row1}.
     */
    @SuppressWarnings("unchecked")
    public static <T, T1> Collector<? super T, ?, Row1<T1>[]> collectingArray(
        Function<? super T, ? extends T1> f1
    ) {
        return Collectors.collectingAndThen(collecting(f1), l -> l.toArray(Row1[]::new));
    }

    /**
     * Create a collector that can collect into a list of {@link Row1}.
     */
    public static <T, T1> Collector<? super T, ?, List<Row1<T1>>> collecting(
        Function<? super T, ? extends T1> f1
    ) {
        return Collector.of(
            ArrayList::new,
            (l, t) -> l.add(DSL.row(f1.apply(t))),
            (t1, t2) -> {
                t1.addAll(t2);
                return t1;
            }
        );
    }

    /**
     * Create a collector that can collect into an array of {@link Row2}.
     */
    @SuppressWarnings("unchecked")
    public static <T, T1, T2> Collector<? super T, ?, Row2<T1, T2>[]> collectingArray(
        Function<? super T, ? extends T1> f1,
        Function<? super T, ? extends T2> f2
    ) {
        return Collectors.collectingAndThen(collecting(f1, f2), l -> l.toArray(Row2[]::new));
    }

    /**
     * Create a collector that can collect into a list of {@link Row2}.
     */
    public static <T, T1, T2> Collector<? super T, ?, List<Row2<T1, T2>>> collecting(
        Function<? super T, ? extends T1> f1,
        Function<? super T, ? extends T2> f2
    ) {
        return Collector.of(
            ArrayList::new,
            (l, t) -> l.add(DSL.row(f1.apply(t), f2.apply(t))),
            (t1, t2) -> {
                t1.addAll(t2);
                return t1;
            }
        );
    }

    /**
     * Create a collector that can collect into an array of {@link Row3}.
     */
    @SuppressWarnings("unchecked")
    public static <T, T1, T2, T3> Collector<? super T, ?, Row3<T1, T2, T3>[]> collectingArray(
        Function<? super T, ? extends T1> f1,
        Function<? super T, ? extends T2> f2,
        Function<? super T, ? extends T3> f3
    ) {
        return Collectors.collectingAndThen(collecting(f1, f2, f3), l -> l.toArray(Row3[]::new));
    }

    /**
     * Create a collector that can collect into a list of {@link Row3}.
     */
    public static <T, T1, T2, T3> Collector<? super T, ?, List<Row3<T1, T2, T3>>> collecting(
        Function<? super T, ? extends T1> f1,
        Function<? super T, ? extends T2> f2,
        Function<? super T, ? extends T3> f3
    ) {
        return Collector.of(
            ArrayList::new,
            (l, t) -> l.add(DSL.row(f1.apply(t), f2.apply(t), f3.apply(t))),
            (t1, t2) -> {
                t1.addAll(t2);
                return t1;
            }
        );
    }

    /**
     * Create a collector that can collect into an array of {@link Row4}.
     */
    @SuppressWarnings("unchecked")
    public static <T, T1, T2, T3, T4> Collector<? super T, ?, Row4<T1, T2, T3, T4>[]> collectingArray(
        Function<? super T, ? extends T1> f1,
        Function<? super T, ? extends T2> f2,
        Function<? super T, ? extends T3> f3,
        Function<? super T, ? extends T4> f4
    ) {
        return Collectors.collectingAndThen(collecting(f1, f2, f3, f4), l -> l.toArray(Row4[]::new));
    }

    /**
     * Create a collector that can collect into a list of {@link Row4}.
     */
    public static <T, T1, T2, T3, T4> Collector<? super T, ?, List<Row4<T1, T2, T3, T4>>> collecting(
        Function<? super T, ? extends T1> f1,
        Function<? super T, ? extends T2> f2,
        Function<? super T, ? extends T3> f3,
        Function<? super T, ? extends T4> f4
    ) {
        return Collector.of(
            ArrayList::new,
            (l, t) -> l.add(DSL.row(f1.apply(t), f2.apply(t), f3.apply(t), f4.apply(t))),
            (t1, t2) -> {
                t1.addAll(t2);
                return t1;
            }
        );
    }

    /**
     * Create a collector that can collect into an array of {@link Row5}.
     */
    @SuppressWarnings("unchecked")
    public static <T, T1, T2, T3, T4, T5> Collector<? super T, ?, Row5<T1, T2, T3, T4, T5>[]> collectingArray(
        Function<? super T, ? extends T1> f1,
        Function<? super T, ? extends T2> f2,
        Function<? super T, ? extends T3> f3,
        Function<? super T, ? extends T4> f4,
        Function<? super T, ? extends T5> f5
    ) {
        return Collectors.collectingAndThen(collecting(f1, f2, f3, f4, f5), l -> l.toArray(Row5[]::new));
    }

    /**
     * Create a collector that can collect into a list of {@link Row5}.
     */
    public static <T, T1, T2, T3, T4, T5> Collector<? super T, ?, List<Row5<T1, T2, T3, T4, T5>>> collecting(
        Function<? super T, ? extends T1> f1,
        Function<? super T, ? extends T2> f2,
        Function<? super T, ? extends T3> f3,
        Function<? super T, ? extends T4> f4,
        Function<? super T, ? extends T5> f5
    ) {
        return Collector.of(
            ArrayList::new,
            (l, t) -> l.add(DSL.row(f1.apply(t), f2.apply(t), f3.apply(t), f4.apply(t), f5.apply(t))),
            (t1, t2) -> {
                t1.addAll(t2);
                return t1;
            }
        );
    }

    /**
     * Create a collector that can collect into an array of {@link Row6}.
     */
    @SuppressWarnings("unchecked")
    public static <T, T1, T2, T3, T4, T5, T6> Collector<? super T, ?, Row6<T1, T2, T3, T4, T5, T6>[]> collectingArray(
        Function<? super T, ? extends T1> f1,
        Function<? super T, ? extends T2> f2,
        Function<? super T, ? extends T3> f3,
        Function<? super T, ? extends T4> f4,
        Function<? super T, ? extends T5> f5,
        Function<? super T, ? extends T6> f6
    ) {
        return Collectors.collectingAndThen(collecting(f1, f2, f3, f4, f5, f6), l -> l.toArray(Row6[]::new));
    }

    /**
     * Create a collector that can collect into a list of {@link Row6}.
     */
    public static <T, T1, T2, T3, T4, T5, T6> Collector<? super T, ?, List<Row6<T1, T2, T3, T4, T5, T6>>> collecting(
        Function<? super T, ? extends T1> f1,
        Function<? super T, ? extends T2> f2,
        Function<? super T, ? extends T3> f3,
        Function<? super T, ? extends T4> f4,
        Function<? super T, ? extends T5> f5,
        Function<? super T, ? extends T6> f6
    ) {
        return Collector.of(
            ArrayList::new,
            (l, t) -> l.add(DSL.row(f1.apply(t), f2.apply(t), f3.apply(t), f4.apply(t), f5.apply(t), f6.apply(t))),
            (t1, t2) -> {
                t1.addAll(t2);
                return t1;
            }
        );
    }

    /**
     * Create a collector that can collect into an array of {@link Row7}.
     */
    @SuppressWarnings("unchecked")
    public static <T, T1, T2, T3, T4, T5, T6, T7> Collector<? super T, ?, Row7<T1, T2, T3, T4, T5, T6, T7>[]> collectingArray(
        Function<? super T, ? extends T1> f1,
        Function<? super T, ? extends T2> f2,
        Function<? super T, ? extends T3> f3,
        Function<? super T, ? extends T4> f4,
        Function<? super T, ? extends T5> f5,
        Function<? super T, ? extends T6> f6,
        Function<? super T, ? extends T7> f7
    ) {
        return Collectors.collectingAndThen(collecting(f1, f2, f3, f4, f5, f6, f7), l -> l.toArray(Row7[]::new));
    }

    /**
     * Create a collector that can collect into a list of {@link Row7}.
     */
    public static <T, T1, T2, T3, T4, T5, T6, T7> Collector<? super T, ?, List<Row7<T1, T2, T3, T4, T5, T6, T7>>> collecting(
        Function<? super T, ? extends T1> f1,
        Function<? super T, ? extends T2> f2,
        Function<? super T, ? extends T3> f3,
        Function<? super T, ? extends T4> f4,
        Function<? super T, ? extends T5> f5,
        Function<? super T, ? extends T6> f6,
        Function<? super T, ? extends T7> f7
    ) {
        return Collector.of(
            ArrayList::new,
            (l, t) -> l.add(DSL.row(f1.apply(t), f2.apply(t), f3.apply(t), f4.apply(t), f5.apply(t), f6.apply(t), f7.apply(t))),
            (t1, t2) -> {
                t1.addAll(t2);
                return t1;
            }
        );
    }

    /**
     * Create a collector that can collect into an array of {@link Row8}.
     */
    @SuppressWarnings("unchecked")
    public static <T, T1, T2, T3, T4, T5, T6, T7, T8> Collector<? super T, ?, Row8<T1, T2, T3, T4, T5, T6, T7, T8>[]> collectingArray(
        Function<? super T, ? extends T1> f1,
        Function<? super T, ? extends T2> f2,
        Function<? super T, ? extends T3> f3,
        Function<? super T, ? extends T4> f4,
        Function<? super T, ? extends T5> f5,
        Function<? super T, ? extends T6> f6,
        Function<? super T, ? extends T7> f7,
        Function<? super T, ? extends T8> f8
    ) {
        return Collectors.collectingAndThen(collecting(f1, f2, f3, f4, f5, f6, f7, f8), l -> l.toArray(Row8[]::new));
    }

    /**
     * Create a collector that can collect into a list of {@link Row8}.
     */
    public static <T, T1, T2, T3, T4, T5, T6, T7, T8> Collector<? super T, ?, List<Row8<T1, T2, T3, T4, T5, T6, T7, T8>>> collecting(
        Function<? super T, ? extends T1> f1,
        Function<? super T, ? extends T2> f2,
        Function<? super T, ? extends T3> f3,
        Function<? super T, ? extends T4> f4,
        Function<? super T, ? extends T5> f5,
        Function<? super T, ? extends T6> f6,
        Function<? super T, ? extends T7> f7,
        Function<? super T, ? extends T8> f8
    ) {
        return Collector.of(
            ArrayList::new,
            (l, t) -> l.add(DSL.row(f1.apply(t), f2.apply(t), f3.apply(t), f4.apply(t), f5.apply(t), f6.apply(t), f7.apply(t), f8.apply(t))),
            (t1, t2) -> {
                t1.addAll(t2);
                return t1;
            }
        );
    }

    /**
     * Create a collector that can collect into an array of {@link Row9}.
     */
    @SuppressWarnings("unchecked")
    public static <T, T1, T2, T3, T4, T5, T6, T7, T8, T9> Collector<? super T, ?, Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9>[]> collectingArray(
        Function<? super T, ? extends T1> f1,
        Function<? super T, ? extends T2> f2,
        Function<? super T, ? extends T3> f3,
        Function<? super T, ? extends T4> f4,
        Function<? super T, ? extends T5> f5,
        Function<? super T, ? extends T6> f6,
        Function<? super T, ? extends T7> f7,
        Function<? super T, ? extends T8> f8,
        Function<? super T, ? extends T9> f9
    ) {
        return Collectors.collectingAndThen(collecting(f1, f2, f3, f4, f5, f6, f7, f8, f9), l -> l.toArray(Row9[]::new));
    }

    /**
     * Create a collector that can collect into a list of {@link Row9}.
     */
    public static <T, T1, T2, T3, T4, T5, T6, T7, T8, T9> Collector<? super T, ?, List<Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9>>> collecting(
        Function<? super T, ? extends T1> f1,
        Function<? super T, ? extends T2> f2,
        Function<? super T, ? extends T3> f3,
        Function<? super T, ? extends T4> f4,
        Function<? super T, ? extends T5> f5,
        Function<? super T, ? extends T6> f6,
        Function<? super T, ? extends T7> f7,
        Function<? super T, ? extends T8> f8,
        Function<? super T, ? extends T9> f9
    ) {
        return Collector.of(
            ArrayList::new,
            (l, t) -> l.add(DSL.row(f1.apply(t), f2.apply(t), f3.apply(t), f4.apply(t), f5.apply(t), f6.apply(t), f7.apply(t), f8.apply(t), f9.apply(t))),
            (t1, t2) -> {
                t1.addAll(t2);
                return t1;
            }
        );
    }

    /**
     * Create a collector that can collect into an array of {@link Row10}.
     */
    @SuppressWarnings("unchecked")
    public static <T, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> Collector<? super T, ?, Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>[]> collectingArray(
        Function<? super T, ? extends T1> f1,
        Function<? super T, ? extends T2> f2,
        Function<? super T, ? extends T3> f3,
        Function<? super T, ? extends T4> f4,
        Function<? super T, ? extends T5> f5,
        Function<? super T, ? extends T6> f6,
        Function<? super T, ? extends T7> f7,
        Function<? super T, ? extends T8> f8,
        Function<? super T, ? extends T9> f9,
        Function<? super T, ? extends T10> f10
    ) {
        return Collectors.collectingAndThen(collecting(f1, f2, f3, f4, f5, f6, f7, f8, f9, f10), l -> l.toArray(Row10[]::new));
    }

    /**
     * Create a collector that can collect into a list of {@link Row10}.
     */
    public static <T, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> Collector<? super T, ?, List<Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>>> collecting(
        Function<? super T, ? extends T1> f1,
        Function<? super T, ? extends T2> f2,
        Function<? super T, ? extends T3> f3,
        Function<? super T, ? extends T4> f4,
        Function<? super T, ? extends T5> f5,
        Function<? super T, ? extends T6> f6,
        Function<? super T, ? extends T7> f7,
        Function<? super T, ? extends T8> f8,
        Function<? super T, ? extends T9> f9,
        Function<? super T, ? extends T10> f10
    ) {
        return Collector.of(
            ArrayList::new,
            (l, t) -> l.add(DSL.row(f1.apply(t), f2.apply(t), f3.apply(t), f4.apply(t), f5.apply(t), f6.apply(t), f7.apply(t), f8.apply(t), f9.apply(t), f10.apply(t))),
            (t1, t2) -> {
                t1.addAll(t2);
                return t1;
            }
        );
    }

    /**
     * Create a collector that can collect into an array of {@link Row11}.
     */
    @SuppressWarnings("unchecked")
    public static <T, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> Collector<? super T, ?, Row11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>[]> collectingArray(
        Function<? super T, ? extends T1> f1,
        Function<? super T, ? extends T2> f2,
        Function<? super T, ? extends T3> f3,
        Function<? super T, ? extends T4> f4,
        Function<? super T, ? extends T5> f5,
        Function<? super T, ? extends T6> f6,
        Function<? super T, ? extends T7> f7,
        Function<? super T, ? extends T8> f8,
        Function<? super T, ? extends T9> f9,
        Function<? super T, ? extends T10> f10,
        Function<? super T, ? extends T11> f11
    ) {
        return Collectors.collectingAndThen(collecting(f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11), l -> l.toArray(Row11[]::new));
    }

    /**
     * Create a collector that can collect into a list of {@link Row11}.
     */
    public static <T, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> Collector<? super T, ?, List<Row11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>>> collecting(
        Function<? super T, ? extends T1> f1,
        Function<? super T, ? extends T2> f2,
        Function<? super T, ? extends T3> f3,
        Function<? super T, ? extends T4> f4,
        Function<? super T, ? extends T5> f5,
        Function<? super T, ? extends T6> f6,
        Function<? super T, ? extends T7> f7,
        Function<? super T, ? extends T8> f8,
        Function<? super T, ? extends T9> f9,
        Function<? super T, ? extends T10> f10,
        Function<? super T, ? extends T11> f11
    ) {
        return Collector.of(
            ArrayList::new,
            (l, t) -> l.add(DSL.row(f1.apply(t), f2.apply(t), f3.apply(t), f4.apply(t), f5.apply(t), f6.apply(t), f7.apply(t), f8.apply(t), f9.apply(t), f10.apply(t), f11.apply(t))),
            (t1, t2) -> {
                t1.addAll(t2);
                return t1;
            }
        );
    }

    /**
     * Create a collector that can collect into an array of {@link Row12}.
     */
    @SuppressWarnings("unchecked")
    public static <T, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> Collector<? super T, ?, Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>[]> collectingArray(
        Function<? super T, ? extends T1> f1,
        Function<? super T, ? extends T2> f2,
        Function<? super T, ? extends T3> f3,
        Function<? super T, ? extends T4> f4,
        Function<? super T, ? extends T5> f5,
        Function<? super T, ? extends T6> f6,
        Function<? super T, ? extends T7> f7,
        Function<? super T, ? extends T8> f8,
        Function<? super T, ? extends T9> f9,
        Function<? super T, ? extends T10> f10,
        Function<? super T, ? extends T11> f11,
        Function<? super T, ? extends T12> f12
    ) {
        return Collectors.collectingAndThen(collecting(f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12), l -> l.toArray(Row12[]::new));
    }

    /**
     * Create a collector that can collect into a list of {@link Row12}.
     */
    public static <T, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> Collector<? super T, ?, List<Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>>> collecting(
        Function<? super T, ? extends T1> f1,
        Function<? super T, ? extends T2> f2,
        Function<? super T, ? extends T3> f3,
        Function<? super T, ? extends T4> f4,
        Function<? super T, ? extends T5> f5,
        Function<? super T, ? extends T6> f6,
        Function<? super T, ? extends T7> f7,
        Function<? super T, ? extends T8> f8,
        Function<? super T, ? extends T9> f9,
        Function<? super T, ? extends T10> f10,
        Function<? super T, ? extends T11> f11,
        Function<? super T, ? extends T12> f12
    ) {
        return Collector.of(
            ArrayList::new,
            (l, t) -> l.add(DSL.row(f1.apply(t), f2.apply(t), f3.apply(t), f4.apply(t), f5.apply(t), f6.apply(t), f7.apply(t), f8.apply(t), f9.apply(t), f10.apply(t), f11.apply(t), f12.apply(t))),
            (t1, t2) -> {
                t1.addAll(t2);
                return t1;
            }
        );
    }

    /**
     * Create a collector that can collect into an array of {@link Row13}.
     */
    @SuppressWarnings("unchecked")
    public static <T, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> Collector<? super T, ?, Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>[]> collectingArray(
        Function<? super T, ? extends T1> f1,
        Function<? super T, ? extends T2> f2,
        Function<? super T, ? extends T3> f3,
        Function<? super T, ? extends T4> f4,
        Function<? super T, ? extends T5> f5,
        Function<? super T, ? extends T6> f6,
        Function<? super T, ? extends T7> f7,
        Function<? super T, ? extends T8> f8,
        Function<? super T, ? extends T9> f9,
        Function<? super T, ? extends T10> f10,
        Function<? super T, ? extends T11> f11,
        Function<? super T, ? extends T12> f12,
        Function<? super T, ? extends T13> f13
    ) {
        return Collectors.collectingAndThen(collecting(f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13), l -> l.toArray(Row13[]::new));
    }

    /**
     * Create a collector that can collect into a list of {@link Row13}.
     */
    public static <T, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> Collector<? super T, ?, List<Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>>> collecting(
        Function<? super T, ? extends T1> f1,
        Function<? super T, ? extends T2> f2,
        Function<? super T, ? extends T3> f3,
        Function<? super T, ? extends T4> f4,
        Function<? super T, ? extends T5> f5,
        Function<? super T, ? extends T6> f6,
        Function<? super T, ? extends T7> f7,
        Function<? super T, ? extends T8> f8,
        Function<? super T, ? extends T9> f9,
        Function<? super T, ? extends T10> f10,
        Function<? super T, ? extends T11> f11,
        Function<? super T, ? extends T12> f12,
        Function<? super T, ? extends T13> f13
    ) {
        return Collector.of(
            ArrayList::new,
            (l, t) -> l.add(DSL.row(f1.apply(t), f2.apply(t), f3.apply(t), f4.apply(t), f5.apply(t), f6.apply(t), f7.apply(t), f8.apply(t), f9.apply(t), f10.apply(t), f11.apply(t), f12.apply(t), f13.apply(t))),
            (t1, t2) -> {
                t1.addAll(t2);
                return t1;
            }
        );
    }

    /**
     * Create a collector that can collect into an array of {@link Row14}.
     */
    @SuppressWarnings("unchecked")
    public static <T, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> Collector<? super T, ?, Row14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>[]> collectingArray(
        Function<? super T, ? extends T1> f1,
        Function<? super T, ? extends T2> f2,
        Function<? super T, ? extends T3> f3,
        Function<? super T, ? extends T4> f4,
        Function<? super T, ? extends T5> f5,
        Function<? super T, ? extends T6> f6,
        Function<? super T, ? extends T7> f7,
        Function<? super T, ? extends T8> f8,
        Function<? super T, ? extends T9> f9,
        Function<? super T, ? extends T10> f10,
        Function<? super T, ? extends T11> f11,
        Function<? super T, ? extends T12> f12,
        Function<? super T, ? extends T13> f13,
        Function<? super T, ? extends T14> f14
    ) {
        return Collectors.collectingAndThen(collecting(f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14), l -> l.toArray(Row14[]::new));
    }

    /**
     * Create a collector that can collect into a list of {@link Row14}.
     */
    public static <T, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> Collector<? super T, ?, List<Row14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>>> collecting(
        Function<? super T, ? extends T1> f1,
        Function<? super T, ? extends T2> f2,
        Function<? super T, ? extends T3> f3,
        Function<? super T, ? extends T4> f4,
        Function<? super T, ? extends T5> f5,
        Function<? super T, ? extends T6> f6,
        Function<? super T, ? extends T7> f7,
        Function<? super T, ? extends T8> f8,
        Function<? super T, ? extends T9> f9,
        Function<? super T, ? extends T10> f10,
        Function<? super T, ? extends T11> f11,
        Function<? super T, ? extends T12> f12,
        Function<? super T, ? extends T13> f13,
        Function<? super T, ? extends T14> f14
    ) {
        return Collector.of(
            ArrayList::new,
            (l, t) -> l.add(DSL.row(f1.apply(t), f2.apply(t), f3.apply(t), f4.apply(t), f5.apply(t), f6.apply(t), f7.apply(t), f8.apply(t), f9.apply(t), f10.apply(t), f11.apply(t), f12.apply(t), f13.apply(t), f14.apply(t))),
            (t1, t2) -> {
                t1.addAll(t2);
                return t1;
            }
        );
    }

    /**
     * Create a collector that can collect into an array of {@link Row15}.
     */
    @SuppressWarnings("unchecked")
    public static <T, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> Collector<? super T, ?, Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>[]> collectingArray(
        Function<? super T, ? extends T1> f1,
        Function<? super T, ? extends T2> f2,
        Function<? super T, ? extends T3> f3,
        Function<? super T, ? extends T4> f4,
        Function<? super T, ? extends T5> f5,
        Function<? super T, ? extends T6> f6,
        Function<? super T, ? extends T7> f7,
        Function<? super T, ? extends T8> f8,
        Function<? super T, ? extends T9> f9,
        Function<? super T, ? extends T10> f10,
        Function<? super T, ? extends T11> f11,
        Function<? super T, ? extends T12> f12,
        Function<? super T, ? extends T13> f13,
        Function<? super T, ? extends T14> f14,
        Function<? super T, ? extends T15> f15
    ) {
        return Collectors.collectingAndThen(collecting(f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15), l -> l.toArray(Row15[]::new));
    }

    /**
     * Create a collector that can collect into a list of {@link Row15}.
     */
    public static <T, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> Collector<? super T, ?, List<Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>>> collecting(
        Function<? super T, ? extends T1> f1,
        Function<? super T, ? extends T2> f2,
        Function<? super T, ? extends T3> f3,
        Function<? super T, ? extends T4> f4,
        Function<? super T, ? extends T5> f5,
        Function<? super T, ? extends T6> f6,
        Function<? super T, ? extends T7> f7,
        Function<? super T, ? extends T8> f8,
        Function<? super T, ? extends T9> f9,
        Function<? super T, ? extends T10> f10,
        Function<? super T, ? extends T11> f11,
        Function<? super T, ? extends T12> f12,
        Function<? super T, ? extends T13> f13,
        Function<? super T, ? extends T14> f14,
        Function<? super T, ? extends T15> f15
    ) {
        return Collector.of(
            ArrayList::new,
            (l, t) -> l.add(DSL.row(f1.apply(t), f2.apply(t), f3.apply(t), f4.apply(t), f5.apply(t), f6.apply(t), f7.apply(t), f8.apply(t), f9.apply(t), f10.apply(t), f11.apply(t), f12.apply(t), f13.apply(t), f14.apply(t), f15.apply(t))),
            (t1, t2) -> {
                t1.addAll(t2);
                return t1;
            }
        );
    }

    /**
     * Create a collector that can collect into an array of {@link Row16}.
     */
    @SuppressWarnings("unchecked")
    public static <T, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> Collector<? super T, ?, Row16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>[]> collectingArray(
        Function<? super T, ? extends T1> f1,
        Function<? super T, ? extends T2> f2,
        Function<? super T, ? extends T3> f3,
        Function<? super T, ? extends T4> f4,
        Function<? super T, ? extends T5> f5,
        Function<? super T, ? extends T6> f6,
        Function<? super T, ? extends T7> f7,
        Function<? super T, ? extends T8> f8,
        Function<? super T, ? extends T9> f9,
        Function<? super T, ? extends T10> f10,
        Function<? super T, ? extends T11> f11,
        Function<? super T, ? extends T12> f12,
        Function<? super T, ? extends T13> f13,
        Function<? super T, ? extends T14> f14,
        Function<? super T, ? extends T15> f15,
        Function<? super T, ? extends T16> f16
    ) {
        return Collectors.collectingAndThen(collecting(f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15, f16), l -> l.toArray(Row16[]::new));
    }

    /**
     * Create a collector that can collect into a list of {@link Row16}.
     */
    public static <T, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> Collector<? super T, ?, List<Row16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>>> collecting(
        Function<? super T, ? extends T1> f1,
        Function<? super T, ? extends T2> f2,
        Function<? super T, ? extends T3> f3,
        Function<? super T, ? extends T4> f4,
        Function<? super T, ? extends T5> f5,
        Function<? super T, ? extends T6> f6,
        Function<? super T, ? extends T7> f7,
        Function<? super T, ? extends T8> f8,
        Function<? super T, ? extends T9> f9,
        Function<? super T, ? extends T10> f10,
        Function<? super T, ? extends T11> f11,
        Function<? super T, ? extends T12> f12,
        Function<? super T, ? extends T13> f13,
        Function<? super T, ? extends T14> f14,
        Function<? super T, ? extends T15> f15,
        Function<? super T, ? extends T16> f16
    ) {
        return Collector.of(
            ArrayList::new,
            (l, t) -> l.add(DSL.row(f1.apply(t), f2.apply(t), f3.apply(t), f4.apply(t), f5.apply(t), f6.apply(t), f7.apply(t), f8.apply(t), f9.apply(t), f10.apply(t), f11.apply(t), f12.apply(t), f13.apply(t), f14.apply(t), f15.apply(t), f16.apply(t))),
            (t1, t2) -> {
                t1.addAll(t2);
                return t1;
            }
        );
    }

    /**
     * Create a collector that can collect into an array of {@link Row17}.
     */
    @SuppressWarnings("unchecked")
    public static <T, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> Collector<? super T, ?, Row17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>[]> collectingArray(
        Function<? super T, ? extends T1> f1,
        Function<? super T, ? extends T2> f2,
        Function<? super T, ? extends T3> f3,
        Function<? super T, ? extends T4> f4,
        Function<? super T, ? extends T5> f5,
        Function<? super T, ? extends T6> f6,
        Function<? super T, ? extends T7> f7,
        Function<? super T, ? extends T8> f8,
        Function<? super T, ? extends T9> f9,
        Function<? super T, ? extends T10> f10,
        Function<? super T, ? extends T11> f11,
        Function<? super T, ? extends T12> f12,
        Function<? super T, ? extends T13> f13,
        Function<? super T, ? extends T14> f14,
        Function<? super T, ? extends T15> f15,
        Function<? super T, ? extends T16> f16,
        Function<? super T, ? extends T17> f17
    ) {
        return Collectors.collectingAndThen(collecting(f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15, f16, f17), l -> l.toArray(Row17[]::new));
    }

    /**
     * Create a collector that can collect into a list of {@link Row17}.
     */
    public static <T, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> Collector<? super T, ?, List<Row17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>>> collecting(
        Function<? super T, ? extends T1> f1,
        Function<? super T, ? extends T2> f2,
        Function<? super T, ? extends T3> f3,
        Function<? super T, ? extends T4> f4,
        Function<? super T, ? extends T5> f5,
        Function<? super T, ? extends T6> f6,
        Function<? super T, ? extends T7> f7,
        Function<? super T, ? extends T8> f8,
        Function<? super T, ? extends T9> f9,
        Function<? super T, ? extends T10> f10,
        Function<? super T, ? extends T11> f11,
        Function<? super T, ? extends T12> f12,
        Function<? super T, ? extends T13> f13,
        Function<? super T, ? extends T14> f14,
        Function<? super T, ? extends T15> f15,
        Function<? super T, ? extends T16> f16,
        Function<? super T, ? extends T17> f17
    ) {
        return Collector.of(
            ArrayList::new,
            (l, t) -> l.add(DSL.row(f1.apply(t), f2.apply(t), f3.apply(t), f4.apply(t), f5.apply(t), f6.apply(t), f7.apply(t), f8.apply(t), f9.apply(t), f10.apply(t), f11.apply(t), f12.apply(t), f13.apply(t), f14.apply(t), f15.apply(t), f16.apply(t), f17.apply(t))),
            (t1, t2) -> {
                t1.addAll(t2);
                return t1;
            }
        );
    }

    /**
     * Create a collector that can collect into an array of {@link Row18}.
     */
    @SuppressWarnings("unchecked")
    public static <T, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> Collector<? super T, ?, Row18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>[]> collectingArray(
        Function<? super T, ? extends T1> f1,
        Function<? super T, ? extends T2> f2,
        Function<? super T, ? extends T3> f3,
        Function<? super T, ? extends T4> f4,
        Function<? super T, ? extends T5> f5,
        Function<? super T, ? extends T6> f6,
        Function<? super T, ? extends T7> f7,
        Function<? super T, ? extends T8> f8,
        Function<? super T, ? extends T9> f9,
        Function<? super T, ? extends T10> f10,
        Function<? super T, ? extends T11> f11,
        Function<? super T, ? extends T12> f12,
        Function<? super T, ? extends T13> f13,
        Function<? super T, ? extends T14> f14,
        Function<? super T, ? extends T15> f15,
        Function<? super T, ? extends T16> f16,
        Function<? super T, ? extends T17> f17,
        Function<? super T, ? extends T18> f18
    ) {
        return Collectors.collectingAndThen(collecting(f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15, f16, f17, f18), l -> l.toArray(Row18[]::new));
    }

    /**
     * Create a collector that can collect into a list of {@link Row18}.
     */
    public static <T, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> Collector<? super T, ?, List<Row18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>>> collecting(
        Function<? super T, ? extends T1> f1,
        Function<? super T, ? extends T2> f2,
        Function<? super T, ? extends T3> f3,
        Function<? super T, ? extends T4> f4,
        Function<? super T, ? extends T5> f5,
        Function<? super T, ? extends T6> f6,
        Function<? super T, ? extends T7> f7,
        Function<? super T, ? extends T8> f8,
        Function<? super T, ? extends T9> f9,
        Function<? super T, ? extends T10> f10,
        Function<? super T, ? extends T11> f11,
        Function<? super T, ? extends T12> f12,
        Function<? super T, ? extends T13> f13,
        Function<? super T, ? extends T14> f14,
        Function<? super T, ? extends T15> f15,
        Function<? super T, ? extends T16> f16,
        Function<? super T, ? extends T17> f17,
        Function<? super T, ? extends T18> f18
    ) {
        return Collector.of(
            ArrayList::new,
            (l, t) -> l.add(DSL.row(f1.apply(t), f2.apply(t), f3.apply(t), f4.apply(t), f5.apply(t), f6.apply(t), f7.apply(t), f8.apply(t), f9.apply(t), f10.apply(t), f11.apply(t), f12.apply(t), f13.apply(t), f14.apply(t), f15.apply(t), f16.apply(t), f17.apply(t), f18.apply(t))),
            (t1, t2) -> {
                t1.addAll(t2);
                return t1;
            }
        );
    }

    /**
     * Create a collector that can collect into an array of {@link Row19}.
     */
    @SuppressWarnings("unchecked")
    public static <T, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> Collector<? super T, ?, Row19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>[]> collectingArray(
        Function<? super T, ? extends T1> f1,
        Function<? super T, ? extends T2> f2,
        Function<? super T, ? extends T3> f3,
        Function<? super T, ? extends T4> f4,
        Function<? super T, ? extends T5> f5,
        Function<? super T, ? extends T6> f6,
        Function<? super T, ? extends T7> f7,
        Function<? super T, ? extends T8> f8,
        Function<? super T, ? extends T9> f9,
        Function<? super T, ? extends T10> f10,
        Function<? super T, ? extends T11> f11,
        Function<? super T, ? extends T12> f12,
        Function<? super T, ? extends T13> f13,
        Function<? super T, ? extends T14> f14,
        Function<? super T, ? extends T15> f15,
        Function<? super T, ? extends T16> f16,
        Function<? super T, ? extends T17> f17,
        Function<? super T, ? extends T18> f18,
        Function<? super T, ? extends T19> f19
    ) {
        return Collectors.collectingAndThen(collecting(f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15, f16, f17, f18, f19), l -> l.toArray(Row19[]::new));
    }

    /**
     * Create a collector that can collect into a list of {@link Row19}.
     */
    public static <T, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> Collector<? super T, ?, List<Row19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>>> collecting(
        Function<? super T, ? extends T1> f1,
        Function<? super T, ? extends T2> f2,
        Function<? super T, ? extends T3> f3,
        Function<? super T, ? extends T4> f4,
        Function<? super T, ? extends T5> f5,
        Function<? super T, ? extends T6> f6,
        Function<? super T, ? extends T7> f7,
        Function<? super T, ? extends T8> f8,
        Function<? super T, ? extends T9> f9,
        Function<? super T, ? extends T10> f10,
        Function<? super T, ? extends T11> f11,
        Function<? super T, ? extends T12> f12,
        Function<? super T, ? extends T13> f13,
        Function<? super T, ? extends T14> f14,
        Function<? super T, ? extends T15> f15,
        Function<? super T, ? extends T16> f16,
        Function<? super T, ? extends T17> f17,
        Function<? super T, ? extends T18> f18,
        Function<? super T, ? extends T19> f19
    ) {
        return Collector.of(
            ArrayList::new,
            (l, t) -> l.add(DSL.row(f1.apply(t), f2.apply(t), f3.apply(t), f4.apply(t), f5.apply(t), f6.apply(t), f7.apply(t), f8.apply(t), f9.apply(t), f10.apply(t), f11.apply(t), f12.apply(t), f13.apply(t), f14.apply(t), f15.apply(t), f16.apply(t), f17.apply(t), f18.apply(t), f19.apply(t))),
            (t1, t2) -> {
                t1.addAll(t2);
                return t1;
            }
        );
    }

    /**
     * Create a collector that can collect into an array of {@link Row20}.
     */
    @SuppressWarnings("unchecked")
    public static <T, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> Collector<? super T, ?, Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>[]> collectingArray(
        Function<? super T, ? extends T1> f1,
        Function<? super T, ? extends T2> f2,
        Function<? super T, ? extends T3> f3,
        Function<? super T, ? extends T4> f4,
        Function<? super T, ? extends T5> f5,
        Function<? super T, ? extends T6> f6,
        Function<? super T, ? extends T7> f7,
        Function<? super T, ? extends T8> f8,
        Function<? super T, ? extends T9> f9,
        Function<? super T, ? extends T10> f10,
        Function<? super T, ? extends T11> f11,
        Function<? super T, ? extends T12> f12,
        Function<? super T, ? extends T13> f13,
        Function<? super T, ? extends T14> f14,
        Function<? super T, ? extends T15> f15,
        Function<? super T, ? extends T16> f16,
        Function<? super T, ? extends T17> f17,
        Function<? super T, ? extends T18> f18,
        Function<? super T, ? extends T19> f19,
        Function<? super T, ? extends T20> f20
    ) {
        return Collectors.collectingAndThen(collecting(f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15, f16, f17, f18, f19, f20), l -> l.toArray(Row20[]::new));
    }

    /**
     * Create a collector that can collect into a list of {@link Row20}.
     */
    public static <T, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> Collector<? super T, ?, List<Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>>> collecting(
        Function<? super T, ? extends T1> f1,
        Function<? super T, ? extends T2> f2,
        Function<? super T, ? extends T3> f3,
        Function<? super T, ? extends T4> f4,
        Function<? super T, ? extends T5> f5,
        Function<? super T, ? extends T6> f6,
        Function<? super T, ? extends T7> f7,
        Function<? super T, ? extends T8> f8,
        Function<? super T, ? extends T9> f9,
        Function<? super T, ? extends T10> f10,
        Function<? super T, ? extends T11> f11,
        Function<? super T, ? extends T12> f12,
        Function<? super T, ? extends T13> f13,
        Function<? super T, ? extends T14> f14,
        Function<? super T, ? extends T15> f15,
        Function<? super T, ? extends T16> f16,
        Function<? super T, ? extends T17> f17,
        Function<? super T, ? extends T18> f18,
        Function<? super T, ? extends T19> f19,
        Function<? super T, ? extends T20> f20
    ) {
        return Collector.of(
            ArrayList::new,
            (l, t) -> l.add(DSL.row(f1.apply(t), f2.apply(t), f3.apply(t), f4.apply(t), f5.apply(t), f6.apply(t), f7.apply(t), f8.apply(t), f9.apply(t), f10.apply(t), f11.apply(t), f12.apply(t), f13.apply(t), f14.apply(t), f15.apply(t), f16.apply(t), f17.apply(t), f18.apply(t), f19.apply(t), f20.apply(t))),
            (t1, t2) -> {
                t1.addAll(t2);
                return t1;
            }
        );
    }

    /**
     * Create a collector that can collect into an array of {@link Row21}.
     */
    @SuppressWarnings("unchecked")
    public static <T, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> Collector<? super T, ?, Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>[]> collectingArray(
        Function<? super T, ? extends T1> f1,
        Function<? super T, ? extends T2> f2,
        Function<? super T, ? extends T3> f3,
        Function<? super T, ? extends T4> f4,
        Function<? super T, ? extends T5> f5,
        Function<? super T, ? extends T6> f6,
        Function<? super T, ? extends T7> f7,
        Function<? super T, ? extends T8> f8,
        Function<? super T, ? extends T9> f9,
        Function<? super T, ? extends T10> f10,
        Function<? super T, ? extends T11> f11,
        Function<? super T, ? extends T12> f12,
        Function<? super T, ? extends T13> f13,
        Function<? super T, ? extends T14> f14,
        Function<? super T, ? extends T15> f15,
        Function<? super T, ? extends T16> f16,
        Function<? super T, ? extends T17> f17,
        Function<? super T, ? extends T18> f18,
        Function<? super T, ? extends T19> f19,
        Function<? super T, ? extends T20> f20,
        Function<? super T, ? extends T21> f21
    ) {
        return Collectors.collectingAndThen(collecting(f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15, f16, f17, f18, f19, f20, f21), l -> l.toArray(Row21[]::new));
    }

    /**
     * Create a collector that can collect into a list of {@link Row21}.
     */
    public static <T, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> Collector<? super T, ?, List<Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>>> collecting(
        Function<? super T, ? extends T1> f1,
        Function<? super T, ? extends T2> f2,
        Function<? super T, ? extends T3> f3,
        Function<? super T, ? extends T4> f4,
        Function<? super T, ? extends T5> f5,
        Function<? super T, ? extends T6> f6,
        Function<? super T, ? extends T7> f7,
        Function<? super T, ? extends T8> f8,
        Function<? super T, ? extends T9> f9,
        Function<? super T, ? extends T10> f10,
        Function<? super T, ? extends T11> f11,
        Function<? super T, ? extends T12> f12,
        Function<? super T, ? extends T13> f13,
        Function<? super T, ? extends T14> f14,
        Function<? super T, ? extends T15> f15,
        Function<? super T, ? extends T16> f16,
        Function<? super T, ? extends T17> f17,
        Function<? super T, ? extends T18> f18,
        Function<? super T, ? extends T19> f19,
        Function<? super T, ? extends T20> f20,
        Function<? super T, ? extends T21> f21
    ) {
        return Collector.of(
            ArrayList::new,
            (l, t) -> l.add(DSL.row(f1.apply(t), f2.apply(t), f3.apply(t), f4.apply(t), f5.apply(t), f6.apply(t), f7.apply(t), f8.apply(t), f9.apply(t), f10.apply(t), f11.apply(t), f12.apply(t), f13.apply(t), f14.apply(t), f15.apply(t), f16.apply(t), f17.apply(t), f18.apply(t), f19.apply(t), f20.apply(t), f21.apply(t))),
            (t1, t2) -> {
                t1.addAll(t2);
                return t1;
            }
        );
    }

    /**
     * Create a collector that can collect into an array of {@link Row22}.
     */
    @SuppressWarnings("unchecked")
    public static <T, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> Collector<? super T, ?, Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>[]> collectingArray(
        Function<? super T, ? extends T1> f1,
        Function<? super T, ? extends T2> f2,
        Function<? super T, ? extends T3> f3,
        Function<? super T, ? extends T4> f4,
        Function<? super T, ? extends T5> f5,
        Function<? super T, ? extends T6> f6,
        Function<? super T, ? extends T7> f7,
        Function<? super T, ? extends T8> f8,
        Function<? super T, ? extends T9> f9,
        Function<? super T, ? extends T10> f10,
        Function<? super T, ? extends T11> f11,
        Function<? super T, ? extends T12> f12,
        Function<? super T, ? extends T13> f13,
        Function<? super T, ? extends T14> f14,
        Function<? super T, ? extends T15> f15,
        Function<? super T, ? extends T16> f16,
        Function<? super T, ? extends T17> f17,
        Function<? super T, ? extends T18> f18,
        Function<? super T, ? extends T19> f19,
        Function<? super T, ? extends T20> f20,
        Function<? super T, ? extends T21> f21,
        Function<? super T, ? extends T22> f22
    ) {
        return Collectors.collectingAndThen(collecting(f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15, f16, f17, f18, f19, f20, f21, f22), l -> l.toArray(Row22[]::new));
    }

    /**
     * Create a collector that can collect into a list of {@link Row22}.
     */
    public static <T, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> Collector<? super T, ?, List<Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>>> collecting(
        Function<? super T, ? extends T1> f1,
        Function<? super T, ? extends T2> f2,
        Function<? super T, ? extends T3> f3,
        Function<? super T, ? extends T4> f4,
        Function<? super T, ? extends T5> f5,
        Function<? super T, ? extends T6> f6,
        Function<? super T, ? extends T7> f7,
        Function<? super T, ? extends T8> f8,
        Function<? super T, ? extends T9> f9,
        Function<? super T, ? extends T10> f10,
        Function<? super T, ? extends T11> f11,
        Function<? super T, ? extends T12> f12,
        Function<? super T, ? extends T13> f13,
        Function<? super T, ? extends T14> f14,
        Function<? super T, ? extends T15> f15,
        Function<? super T, ? extends T16> f16,
        Function<? super T, ? extends T17> f17,
        Function<? super T, ? extends T18> f18,
        Function<? super T, ? extends T19> f19,
        Function<? super T, ? extends T20> f20,
        Function<? super T, ? extends T21> f21,
        Function<? super T, ? extends T22> f22
    ) {
        return Collector.of(
            ArrayList::new,
            (l, t) -> l.add(DSL.row(f1.apply(t), f2.apply(t), f3.apply(t), f4.apply(t), f5.apply(t), f6.apply(t), f7.apply(t), f8.apply(t), f9.apply(t), f10.apply(t), f11.apply(t), f12.apply(t), f13.apply(t), f14.apply(t), f15.apply(t), f16.apply(t), f17.apply(t), f18.apply(t), f19.apply(t), f20.apply(t), f21.apply(t), f22.apply(t))),
            (t1, t2) -> {
                t1.addAll(t2);
                return t1;
            }
        );
    }




}
