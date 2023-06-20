/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
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
 * For more information, please visit: https://www.jooq.org/legal/licensing
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

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

import java.lang.reflect.Array;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.jooq.exception.InvalidResultException;
import org.jooq.impl.Internal;

/**
 * Common utilities related to {@link Record} types and constructing
 * {@link RecordMapper}.
 * <p>
 * The various <code>mapping()</code> methods can be used e.g. to map between
 * {@link Record} types and constructors of known degree, such as in this
 * example:
 * <p>
 * <pre><code>
 * record Actor (int id, String firstName, String lastName) {}
 *
 * List&lt;Actor&gt; actors =
 * ctx.select(ACTOR.ID, ACTOR.FIRST_NAME, ACTOR.LAST_NAME)
 *    .from(ACTOR)
 *    .fetch(mapping(Actor::new));
 * </code></pre>
 *
 * @author Lukas Eder
 */
public final class Records {

    /**
     * Create a collector that can collect {@link Record1} resulting from a
     * single column {@link ResultQuery} into an array of that column's type.
     * <p>
     * For example:
     * <p>
     * <pre><code>
     * String[] titles =
     * ctx.select(BOOK.TITLE)
     *    .from(BOOK)
     *    .collect(intoArray(new String[0]));
     * </code></pre>
     * <p>
     * This is the same as the following, but allows for omitting repeating the
     * <code>BOOK.TITLE</code> column:
     * <p>
     * <pre><code>
     * String[] titles =
     * ctx.select(BOOK.TITLE)
     *    .from(BOOK)
     *    .fetchArray(BOOK.TITLE);
     * </code></pre>
     */
    public static final <E, R extends Record1<E>> Collector<R, ?, E[]> intoArray(E[] a) {
        return intoArray(a, Record1::value1);
    }

    /**
     * Create a collector that can collect {@link Record} resulting from a
     * {@link ResultQuery} into an array of a mapped type.
     * <p>
     * For example:
     * <p>
     * <pre><code>
     * String[] titles =
     * ctx.select(BOOK.TITLE)
     *    .from(BOOK)
     *    .collect(intoArray(new String[0], r -&gt; r.get(BOOK.TITLE)));
     * </code></pre>
     * <p>
     * This is the same as the following:
     * <p>
     * <pre><code>
     * String[] titles =
     * ctx.select(BOOK.TITLE)
     *    .from(BOOK)
     *    .fetchArray(BOOK.TITLE);
     * </code></pre>
     */
    public static final <E, R extends Record1<E>> Collector<R, ?, E[]> intoArray(Class<? extends E> componentType) {
        return intoArray(componentType, Record1::value1);
    }

    /**
     * Create a collector that can collect {@link Record} resulting from a
     * {@link ResultQuery} into an array of a mapped type.
     * <p>
     * For example:
     * <p>
     * <pre><code>
     * String[] titles =
     * ctx.select(BOOK.TITLE)
     *    .from(BOOK)
     *    .collect(intoArray(new String[0], r -&gt; r.get(BOOK.TITLE)));
     * </code></pre>
     * <p>
     * This is the same as the following:
     * <p>
     * <pre><code>
     * String[] titles =
     * ctx.select(BOOK.TITLE)
     *    .from(BOOK)
     *    .fetchArray(BOOK.TITLE);
     * </code></pre>
     */
    public static final <E, R extends Record> Collector<R, ?, E[]> intoArray(E[] a, Function<? super R, ? extends E> function) {
        return collectingAndThen(Collectors.mapping(function, toCollection(ArrayList::new)), l -> l.toArray(a));
    }

    /**
     * Create a collector that can collect {@link Record} resulting from a
     * {@link ResultQuery} into an array of a mapped type.
     * <p>
     * For example:
     * <p>
     * <pre><code>
     * String[] titles =
     * ctx.select(BOOK.TITLE)
     *    .from(BOOK)
     *    .collect(intoArray(new String[0], r -&gt; r.get(BOOK.TITLE)));
     * </code></pre>
     * <p>
     * This is the same as the following:
     * <p>
     * <pre><code>
     * String[] titles =
     * ctx.select(BOOK.TITLE)
     *    .from(BOOK)
     *    .fetchArray(BOOK.TITLE);
     * </code></pre>
     */
    @SuppressWarnings("unchecked")
    public static final <E, R extends Record> Collector<R, ?, E[]> intoArray(Class<? extends E> componentType, Function<? super R, ? extends E> function) {
        return collectingAndThen(Collectors.mapping(function, toCollection(ArrayList::new)), l -> l.toArray((E[]) Array.newInstance(componentType, l.size())));
    }

    /**
     * Create a collector that can collect {@link Record1} resulting from a
     * single column {@link ResultQuery} into a {@link List} of that column's
     * type.
     * <p>
     * For example:
     * <p>
     * <pre><code>
     * List&lt;String&gt; titles =
     * ctx.select(BOOK.TITLE)
     *    .from(BOOK)
     *    .collect(intoList());
     * </code></pre>
     * <p>
     * This is the same as the following, but allows for omitting repeating the
     * <code>BOOK.TITLE</code> column:
     * <p>
     * <pre><code>
     * List&lt;String&gt; titles =
     * ctx.select(BOOK.TITLE)
     *    .from(BOOK)
     *    .fetch(BOOK.TITLE);
     * </code></pre>
     */
    public static final <E, R extends Record1<E>> Collector<R, ?, List<E>> intoList() {
        return Collectors.mapping(Record1::value1, Collectors.toCollection(ArrayList::new));
    }

    /**
     * Create a collector that can collect {@link Record} resulting from a
     * {@link ResultQuery} into a {@link List} of a mapped type.
     * <p>
     * For example:
     * <p>
     * <pre><code>
     * List&lt;String&gt; titles =
     * ctx.select(BOOK.TITLE)
     *    .from(BOOK)
     *    .collect(intoList(r -&gt; r.get(BOOK.TITLE)));
     * </code></pre>
     * <p>
     * This is the same as the following:
     * <p>
     * <pre><code>
     * List&lt;String&gt; titles =
     * ctx.select(BOOK.TITLE)
     *    .from(BOOK)
     *    .fetch(BOOK.TITLE);
     * </code></pre>
     */
    public static final <E, R extends Record> Collector<R, ?, List<E>> intoList(Function<? super R, ? extends E> function) {
        return Collectors.mapping(function, Collectors.toCollection(ArrayList::new));
    }

    /**
     * Create a collector that can collect {@link Record1} resulting from a
     * single column {@link ResultQuery} into a {@link Set} of that column's
     * type.
     * <p>
     * For example:
     * <p>
     * <pre><code>
     * Set&lt;String&gt; titles =
     * ctx.select(BOOK.TITLE)
     *    .from(BOOK)
     *    .collect(intoSet());
     * </code></pre>
     * <p>
     * This is the same as the following, but allows for omitting repeating the
     * <code>BOOK.TITLE</code> column:
     * <p>
     * <pre><code>
     * List&lt;String&gt; titles =
     * ctx.select(BOOK.TITLE)
     *    .from(BOOK)
     *    .fetchSet(BOOK.TITLE);
     * </code></pre>
     */
    public static final <E, R extends Record1<E>> Collector<R, ?, Set<E>> intoSet() {
        return intoSet(Record1::value1);
    }

    /**
     * Create a collector that can collect {@link Record} resulting from a
     * {@link ResultQuery} into a {@link Set} of a mapped type.
     * <p>
     * For example:
     * <p>
     * <pre><code>
     * Set&lt;String&gt; titles =
     * ctx.select(BOOK.TITLE)
     *    .from(BOOK)
     *    .collect(intoSet(r -&gt; r.get(BOOK.TITLE)));
     * </code></pre>
     * <p>
     * This is the same as the following:
     * <p>
     * <pre><code>
     * List&lt;String&gt; titles =
     * ctx.select(BOOK.TITLE)
     *    .from(BOOK)
     *    .fetchSet(BOOK.TITLE);
     * </code></pre>
     */
    public static final <E, R extends Record> Collector<R, ?, Set<E>> intoSet(Function<? super R, ? extends E> function) {
        return Collectors.mapping(function, Collectors.toCollection(LinkedHashSet::new));
    }

    /**
     * Create a collector that can collect {@link Record2} resulting from a
     * 2-column {@link ResultQuery} into a {@link Map} using the first column as
     * key and the second column as value.
     * <p>
     * Collection throws {@link InvalidResultException} if a key is encountered
     * more than once.
     * <p>
     * For example:
     * <p>
     * <pre><code>
     * Map&lt;Integer, String&gt; books =
     * ctx.select(BOOK.ID, BOOK.TITLE)
     *    .from(BOOK)
     *    .collect(intoMap());
     * </code></pre>
     * <p>
     * This is the same as the following, but allows for omitting repeating the
     * <code>BOOK.ID</code> and <code>BOOK.TITLE</code> columns:
     * <p>
     * <pre><code>
     * Map&lt;Integer, String&gt; books =
     * ctx.select(BOOK.ID, BOOK.TITLE)
     *    .from(BOOK)
     *    .fetchMap(BOOK.ID, BOOK.TITLE);
     * </code></pre>
     */
    public static final <K, V, R extends Record2<K, V>> Collector<R, ?, Map<K, V>> intoMap() {
        return intoMap(Record2::value1, Record2::value2);
    }

    /**
     * Create a collector that can collect {@link Record} resulting from a
     * {@link ResultQuery} into a {@link Map} using the result of the argument
     * {@link RecordMapper} as key and the record itself as value.
     * <p>
     * Collection throws {@link InvalidResultException} if a key is encountered
     * more than once.
     * <p>
     * For example:
     * <p>
     * <pre><code>
     * Map&lt;Integer, Record2&lt;Integer, String&gt;&gt; books =
     * ctx.select(BOOK.ID, BOOK.TITLE)
     *    .from(BOOK)
     *    .collect(intoMap(r -&gt; r.get(BOOK.ID)));
     * </code></pre>
     * <p>
     * This is the same as the following:
     * <p>
     * <pre><code>
     * Map&lt;Integer, Record2&lt;Integer, String&gt;&gt; books =
     * ctx.select(BOOK.ID, BOOK.TITLE)
     *    .from(BOOK)
     *    .fetchMap(BOOK.ID);
     * </code></pre>
     */
    public static final <K, R extends Record> Collector<R, ?, Map<K, R>> intoMap(Function<? super R, ? extends K> keyMapper) {
        return intoMap(keyMapper, r -> r);
    }

    /**
     * Create a collector that can collect {@link Record} resulting from a
     * {@link ResultQuery} into a {@link Map} using the result of the argument
     * {@link RecordMapper} as key and the result of another argument
     * {@link RecordMapper} as value.
     * <p>
     * Collection throws {@link InvalidResultException} if a key is encountered
     * more than once.
     * <p>
     * For example:
     * <p>
     * <pre><code>
     * Map&lt;Integer, String&gt; books =
     * ctx.select(BOOK.ID, BOOK.TITLE)
     *    .from(BOOK)
     *    .collect(intoMap(r -&gt; r.get(BOOK.ID), r -&gt; r.get(BOOK.TITLE)));
     * </code></pre>
     * <p>
     * This is the same as the following:
     * <p>
     * <pre><code>
     * Map&lt;Integer, String&gt; books =
     * ctx.select(BOOK.ID, BOOK.TITLE)
     *    .from(BOOK)
     *    .fetchMap(BOOK.ID, BOOK.TITLE);
     * </code></pre>
     */
    public static final <K, V, R extends Record> Collector<R, ?, Map<K, V>> intoMap(
        Function<? super R, ? extends K> keyMapper,
        Function<? super R, ? extends V> valueMapper
    ) {

        // [#12123] Can't use Collectors.toMap() with nulls
        return Collector.of(
            LinkedHashMap::new,
            (m, e) -> {
                K k = keyMapper.apply(e);

                if (m.containsKey(k))
                    throw new InvalidResultException("Key " + k + " is not unique in Result");
                else
                    m.put(k, valueMapper.apply(e));
            },
            (m1, m2) -> {
                m1.putAll(m2);
                return m1;
            }
        );
    }

    /**
     * Create a collector that can collect {@link Record2} resulting from a
     * 2-column {@link ResultQuery} into a {@link Map} using the first column as
     * key collecting values of the second column into a list of values.
     * <p>
     * For example:
     * <p>
     * <pre><code>
     * Map&lt;Integer, List&lt;String&gt;&gt; books =
     * ctx.select(BOOK.ID, BOOK.TITLE)
     *    .from(BOOK)
     *    .collect(intoGroups());
     * </code></pre>
     * <p>
     * This is the same as the following, but allows for omitting repeating the
     * <code>BOOK.ID</code> and <code>BOOK.TITLE</code> columns:
     * <p>
     * <pre><code>
     * Map&lt;Integer, List&lt;String&gt;&gt; books =
     * ctx.select(BOOK.ID, BOOK.TITLE)
     *    .from(BOOK)
     *    .fetchGroups(BOOK.ID, BOOK.TITLE);
     * </code></pre>
     */
    public static final <K, V, R extends Record2<K, V>> Collector<R, ?, Map<K, List<V>>> intoGroups() {
        return intoGroups(Record2::value1, Record2::value2);
    }

    /**
     * Create a collector that can collect {@link Record} resulting from a
     * {@link ResultQuery} into a {@link Map} using the result of the argument
     * {@link RecordMapper} as key collecting the records themselves into a list
     * of values.
     * <p>
     * For example:
     * <p>
     * <pre><code>
     * Map&lt;Integer, List&lt;Record2&lt;Integer, String&gt;&gt;&gt; books =
     * ctx.select(BOOK.ID, BOOK.TITLE)
     *    .from(BOOK)
     *    .collect(intoGroups(r -&gt; r.get(BOOK.ID)));
     * </code></pre>
     * <p>
     * This is the same as the following:
     * <p>
     * <pre><code>
     * Map&lt;Integer, List&lt;Record2&lt;Integer, String&gt;&gt;&gt; books =
     * ctx.select(BOOK.ID, BOOK.TITLE)
     *    .from(BOOK)
     *    .fetchGroups(BOOK.ID);
     * </code></pre>
     */
    public static final <K, R extends Record> Collector<R, ?, Map<K, List<R>>> intoGroups(Function<? super R, ? extends K> keyMapper) {
        return intoGroups(keyMapper, r -> r);
    }

    /**
     * Create a collector that can collect {@link Record} resulting from a
     * {@link ResultQuery} into a {@link Map} using the result of the argument
     * {@link RecordMapper} as key collecting the result of another argument
     * {@link RecordMapper} into a list of values.
     * <p>
     * For example:
     * <p>
     * <pre><code>
     * Map&lt;Integer, List&lt;String&gt;&gt; books =
     * ctx.select(BOOK.ID, BOOK.TITLE)
     *    .from(BOOK)
     *    .collect(intoGroups(r -&gt; r.get(BOOK.ID), r -&gt; r.get(BOOK.TITLE)));
     * </code></pre>
     * <p>
     * This is the same as the following:
     * <p>
     * <pre><code>
     * Map&lt;Integer, List&lt;String&gt;&gt; books =
     * ctx.select(BOOK.ID, BOOK.TITLE)
     *    .from(BOOK)
     *    .fetchGroups(BOOK.ID, BOOK.TITLE);
     * </code></pre>
     */
    public static final <K, V, R extends Record> Collector<R, ?, Map<K, List<V>>> intoGroups(
        Function<? super R, ? extends K> keyMapper,
        Function<? super R, ? extends V> valueMapper
    ) {
        return collectingAndThen(
            Collectors.groupingBy(
                keyMapper.andThen(wrapNulls()),
                LinkedHashMap::new,
                Collectors.mapping(valueMapper, Collectors.toList())
            ),
            unwrapNulls()
        );
    }

    /**
     * Create a collector that can collect {@link Record} resulting from a
     * {@link ResultQuery} into a {@link Map} using the result of the argument
     * {@link RecordMapper} as key collecting the records themselves into a
     * {@link Result}.
     * <p>
     * For example:
     * <p>
     * <pre><code>
     * Map&lt;Integer, Result&lt;Record2&lt;Integer, String&gt;&gt;&gt; books =
     * ctx.select(BOOK.ID, BOOK.TITLE)
     *    .from(BOOK)
     *    .collect(intoResultGroups(r -&gt; r.get(BOOK.ID)));
     * </code></pre>
     * <p>
     * This is the same as the following:
     * <p>
     * <pre><code>
     * Map&lt;Integer, Result&lt;Record2&lt;Integer, String&gt;&gt;&gt; books =
     * ctx.select(BOOK.ID, BOOK.TITLE)
     *    .from(BOOK)
     *    .fetchGroups(BOOK.ID);
     * </code></pre>
     */
    public static final <K, R extends Record> Collector<R, ?, Map<K, Result<R>>> intoResultGroups(Function<? super R, ? extends K> keyMapper) {
        return intoResultGroups(keyMapper, r -> r);
    }

    /**
     * Create a collector that can collect {@link Record} resulting from a
     * {@link ResultQuery} into a {@link Map} using the result of the argument
     * {@link RecordMapper} as key collecting the result of another argument
     * {@link RecordMapper} into a {@link Result} of values.
     * <p>
     * For example:
     * <p>
     * <pre><code>
     * Map&lt;Integer, Result&lt;Record1&lt;String&gt;&gt;&gt; books =
     * ctx.select(BOOK.ID, BOOK.TITLE)
     *    .from(BOOK)
     *    .collect(intoResultGroups(r -&gt; r.get(BOOK.ID), r -&gt; r.get(BOOK.TITLE)));
     * </code></pre>
     * <p>
     * This is the same as the following:
     * <p>
     * <pre><code>
     * Map&lt;Integer, Result&lt;Record1&lt;String&gt;&gt;&gt; books =
     * ctx.select(BOOK.ID, BOOK.TITLE)
     *    .from(BOOK)
     *    .fetchGroups(BOOK.ID, BOOK.TITLE);
     * </code></pre>
     */
    @SuppressWarnings("unchecked")
    public static final <K, V extends Record, R extends Record> Collector<R, ?, Map<K, Result<V>>> intoResultGroups(
        Function<? super R, ? extends K> keyMapper,
        Function<? super R, ? extends V> valueMapper
    ) {
        return Collectors.groupingBy(
            keyMapper,
            LinkedHashMap::new,
            Collector.<R, Result<V>[], Result<V>>of(
                () -> (Result<V>[]) new Result[1],
                (x, r) -> {
                    V v = valueMapper.apply(r);

                    if (x[0] == null)
                        x[0] = Internal.result(v);

                    x[0].add(v);
                },
                (r1, r2) -> {
                    r1[0].addAll(r2[0]);
                    return r1;
                },
                r -> r[0]
            )
        );
    }

    /**
     * Create a collector that can collect {@link Record} resulting from a
     * {@link ResultQuery} into a hierarchy of custom data types.
     * <p>
     * For example:
     * <p>
     *
     * <pre>
     * <code>
     * record File(String name, List&lt;File&gt; contents) {}
     *
     * List&lt;File&gt; files =
     * ctx.select(FILE.ID, FILE.PARENT_ID, FILE.NAME)
     *    .from(FILE)
     *    .collect(intoHierarchy(
     *        r -&gt; r.value1(),
     *        r -&gt; r.value2(),
     *        r -&gt; new File(r.value3(), new ArrayList<>()),
     *        (p, c) -&gt; p.contents().add(c)
     *    ));
     * </code>
     * </pre>
     *
     * @param <K> The key type (e.g. an <code>ID</code>)
     * @param <E> The value type (e.g. a POJO)
     * @param <R> The record type
     * @param keyMapper A function that extract a key from a record
     * @param parentKeyMapper A function that extracts the parent key from a
     *            record.
     * @param nodeMapper A function that maps a record to a new value type.
     * @param parentChildAppender A (parent, child) consumer that adds the child
     *            to its parent.
     */
    public static final <K, E, R extends Record> Collector<R, ?, List<E>> intoHierarchy(
        Function<? super R, ? extends K> keyMapper,
        Function<? super R, ? extends K> parentKeyMapper,
        Function<? super R, ? extends E> nodeMapper,
        BiConsumer<? super E, ? super E> parentChildAppender
    ) {
        return collectingAndThen(
            intoMap(keyMapper, r -> new SimpleImmutableEntry<R, E>(r, nodeMapper.apply(r))),
            m -> {
                List<E> r = new ArrayList<>();

                m.forEach((k, v) -> {
                    Entry<R, E> parent = m.get(parentKeyMapper.apply(v.getKey()));

                    if (parent != null)
                        parentChildAppender.accept(parent.getValue(), v.getValue());
                    else
                        r.add(v.getValue());
                });

                return r;
            }
        );
    }



    /**
     * Create a {@link RecordMapper} that can map from {@link Record1} to a user
     * type in a type safe way.
     * <p>
     * Combine this with e.g. {@link Functions#nullOnAllNull(Function1)} or
     * {@link Functions#nullOnAnyNull(Function1)} to achieve <code>null</code>
     * safety when mapping nested rows from <code>LEFT JOIN</code> etc.
     */
    public static final <T1, R extends Record1<T1>, U> RecordMapper<R, U> mapping(
        Function1<? super T1, ? extends U> function
    ) {
        return r -> function.apply(r.value1());
    }

    /**
     * Create a {@link RecordMapper} that can map from {@link Record2} to a user
     * type in a type safe way.
     * <p>
     * Combine this with e.g. {@link Functions#nullOnAllNull(Function2)} or
     * {@link Functions#nullOnAnyNull(Function2)} to achieve <code>null</code>
     * safety when mapping nested rows from <code>LEFT JOIN</code> etc.
     */
    public static final <T1, T2, R extends Record2<T1, T2>, U> RecordMapper<R, U> mapping(
        Function2<? super T1, ? super T2, ? extends U> function
    ) {
        return r -> function.apply(r.value1(), r.value2());
    }

    /**
     * Create a {@link RecordMapper} that can map from {@link Record3} to a user
     * type in a type safe way.
     * <p>
     * Combine this with e.g. {@link Functions#nullOnAllNull(Function3)} or
     * {@link Functions#nullOnAnyNull(Function3)} to achieve <code>null</code>
     * safety when mapping nested rows from <code>LEFT JOIN</code> etc.
     */
    public static final <T1, T2, T3, R extends Record3<T1, T2, T3>, U> RecordMapper<R, U> mapping(
        Function3<? super T1, ? super T2, ? super T3, ? extends U> function
    ) {
        return r -> function.apply(r.value1(), r.value2(), r.value3());
    }

    /**
     * Create a {@link RecordMapper} that can map from {@link Record4} to a user
     * type in a type safe way.
     * <p>
     * Combine this with e.g. {@link Functions#nullOnAllNull(Function4)} or
     * {@link Functions#nullOnAnyNull(Function4)} to achieve <code>null</code>
     * safety when mapping nested rows from <code>LEFT JOIN</code> etc.
     */
    public static final <T1, T2, T3, T4, R extends Record4<T1, T2, T3, T4>, U> RecordMapper<R, U> mapping(
        Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends U> function
    ) {
        return r -> function.apply(r.value1(), r.value2(), r.value3(), r.value4());
    }

    /**
     * Create a {@link RecordMapper} that can map from {@link Record5} to a user
     * type in a type safe way.
     * <p>
     * Combine this with e.g. {@link Functions#nullOnAllNull(Function5)} or
     * {@link Functions#nullOnAnyNull(Function5)} to achieve <code>null</code>
     * safety when mapping nested rows from <code>LEFT JOIN</code> etc.
     */
    public static final <T1, T2, T3, T4, T5, R extends Record5<T1, T2, T3, T4, T5>, U> RecordMapper<R, U> mapping(
        Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends U> function
    ) {
        return r -> function.apply(r.value1(), r.value2(), r.value3(), r.value4(), r.value5());
    }

    /**
     * Create a {@link RecordMapper} that can map from {@link Record6} to a user
     * type in a type safe way.
     * <p>
     * Combine this with e.g. {@link Functions#nullOnAllNull(Function6)} or
     * {@link Functions#nullOnAnyNull(Function6)} to achieve <code>null</code>
     * safety when mapping nested rows from <code>LEFT JOIN</code> etc.
     */
    public static final <T1, T2, T3, T4, T5, T6, R extends Record6<T1, T2, T3, T4, T5, T6>, U> RecordMapper<R, U> mapping(
        Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends U> function
    ) {
        return r -> function.apply(r.value1(), r.value2(), r.value3(), r.value4(), r.value5(), r.value6());
    }

    /**
     * Create a {@link RecordMapper} that can map from {@link Record7} to a user
     * type in a type safe way.
     * <p>
     * Combine this with e.g. {@link Functions#nullOnAllNull(Function7)} or
     * {@link Functions#nullOnAnyNull(Function7)} to achieve <code>null</code>
     * safety when mapping nested rows from <code>LEFT JOIN</code> etc.
     */
    public static final <T1, T2, T3, T4, T5, T6, T7, R extends Record7<T1, T2, T3, T4, T5, T6, T7>, U> RecordMapper<R, U> mapping(
        Function7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends U> function
    ) {
        return r -> function.apply(r.value1(), r.value2(), r.value3(), r.value4(), r.value5(), r.value6(), r.value7());
    }

    /**
     * Create a {@link RecordMapper} that can map from {@link Record8} to a user
     * type in a type safe way.
     * <p>
     * Combine this with e.g. {@link Functions#nullOnAllNull(Function8)} or
     * {@link Functions#nullOnAnyNull(Function8)} to achieve <code>null</code>
     * safety when mapping nested rows from <code>LEFT JOIN</code> etc.
     */
    public static final <T1, T2, T3, T4, T5, T6, T7, T8, R extends Record8<T1, T2, T3, T4, T5, T6, T7, T8>, U> RecordMapper<R, U> mapping(
        Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends U> function
    ) {
        return r -> function.apply(r.value1(), r.value2(), r.value3(), r.value4(), r.value5(), r.value6(), r.value7(), r.value8());
    }

    /**
     * Create a {@link RecordMapper} that can map from {@link Record9} to a user
     * type in a type safe way.
     * <p>
     * Combine this with e.g. {@link Functions#nullOnAllNull(Function9)} or
     * {@link Functions#nullOnAnyNull(Function9)} to achieve <code>null</code>
     * safety when mapping nested rows from <code>LEFT JOIN</code> etc.
     */
    public static final <T1, T2, T3, T4, T5, T6, T7, T8, T9, R extends Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9>, U> RecordMapper<R, U> mapping(
        Function9<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? extends U> function
    ) {
        return r -> function.apply(r.value1(), r.value2(), r.value3(), r.value4(), r.value5(), r.value6(), r.value7(), r.value8(), r.value9());
    }

    /**
     * Create a {@link RecordMapper} that can map from {@link Record10} to a user
     * type in a type safe way.
     * <p>
     * Combine this with e.g. {@link Functions#nullOnAllNull(Function10)} or
     * {@link Functions#nullOnAnyNull(Function10)} to achieve <code>null</code>
     * safety when mapping nested rows from <code>LEFT JOIN</code> etc.
     */
    public static final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R extends Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>, U> RecordMapper<R, U> mapping(
        Function10<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? extends U> function
    ) {
        return r -> function.apply(r.value1(), r.value2(), r.value3(), r.value4(), r.value5(), r.value6(), r.value7(), r.value8(), r.value9(), r.value10());
    }

    /**
     * Create a {@link RecordMapper} that can map from {@link Record11} to a user
     * type in a type safe way.
     * <p>
     * Combine this with e.g. {@link Functions#nullOnAllNull(Function11)} or
     * {@link Functions#nullOnAnyNull(Function11)} to achieve <code>null</code>
     * safety when mapping nested rows from <code>LEFT JOIN</code> etc.
     */
    public static final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R extends Record11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>, U> RecordMapper<R, U> mapping(
        Function11<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? extends U> function
    ) {
        return r -> function.apply(r.value1(), r.value2(), r.value3(), r.value4(), r.value5(), r.value6(), r.value7(), r.value8(), r.value9(), r.value10(), r.value11());
    }

    /**
     * Create a {@link RecordMapper} that can map from {@link Record12} to a user
     * type in a type safe way.
     * <p>
     * Combine this with e.g. {@link Functions#nullOnAllNull(Function12)} or
     * {@link Functions#nullOnAnyNull(Function12)} to achieve <code>null</code>
     * safety when mapping nested rows from <code>LEFT JOIN</code> etc.
     */
    public static final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R extends Record12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>, U> RecordMapper<R, U> mapping(
        Function12<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? super T12, ? extends U> function
    ) {
        return r -> function.apply(r.value1(), r.value2(), r.value3(), r.value4(), r.value5(), r.value6(), r.value7(), r.value8(), r.value9(), r.value10(), r.value11(), r.value12());
    }

    /**
     * Create a {@link RecordMapper} that can map from {@link Record13} to a user
     * type in a type safe way.
     * <p>
     * Combine this with e.g. {@link Functions#nullOnAllNull(Function13)} or
     * {@link Functions#nullOnAnyNull(Function13)} to achieve <code>null</code>
     * safety when mapping nested rows from <code>LEFT JOIN</code> etc.
     */
    public static final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R extends Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>, U> RecordMapper<R, U> mapping(
        Function13<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? super T12, ? super T13, ? extends U> function
    ) {
        return r -> function.apply(r.value1(), r.value2(), r.value3(), r.value4(), r.value5(), r.value6(), r.value7(), r.value8(), r.value9(), r.value10(), r.value11(), r.value12(), r.value13());
    }

    /**
     * Create a {@link RecordMapper} that can map from {@link Record14} to a user
     * type in a type safe way.
     * <p>
     * Combine this with e.g. {@link Functions#nullOnAllNull(Function14)} or
     * {@link Functions#nullOnAnyNull(Function14)} to achieve <code>null</code>
     * safety when mapping nested rows from <code>LEFT JOIN</code> etc.
     */
    public static final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R extends Record14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>, U> RecordMapper<R, U> mapping(
        Function14<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? super T12, ? super T13, ? super T14, ? extends U> function
    ) {
        return r -> function.apply(r.value1(), r.value2(), r.value3(), r.value4(), r.value5(), r.value6(), r.value7(), r.value8(), r.value9(), r.value10(), r.value11(), r.value12(), r.value13(), r.value14());
    }

    /**
     * Create a {@link RecordMapper} that can map from {@link Record15} to a user
     * type in a type safe way.
     * <p>
     * Combine this with e.g. {@link Functions#nullOnAllNull(Function15)} or
     * {@link Functions#nullOnAnyNull(Function15)} to achieve <code>null</code>
     * safety when mapping nested rows from <code>LEFT JOIN</code> etc.
     */
    public static final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R extends Record15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>, U> RecordMapper<R, U> mapping(
        Function15<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? super T12, ? super T13, ? super T14, ? super T15, ? extends U> function
    ) {
        return r -> function.apply(r.value1(), r.value2(), r.value3(), r.value4(), r.value5(), r.value6(), r.value7(), r.value8(), r.value9(), r.value10(), r.value11(), r.value12(), r.value13(), r.value14(), r.value15());
    }

    /**
     * Create a {@link RecordMapper} that can map from {@link Record16} to a user
     * type in a type safe way.
     * <p>
     * Combine this with e.g. {@link Functions#nullOnAllNull(Function16)} or
     * {@link Functions#nullOnAnyNull(Function16)} to achieve <code>null</code>
     * safety when mapping nested rows from <code>LEFT JOIN</code> etc.
     */
    public static final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R extends Record16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>, U> RecordMapper<R, U> mapping(
        Function16<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? super T12, ? super T13, ? super T14, ? super T15, ? super T16, ? extends U> function
    ) {
        return r -> function.apply(r.value1(), r.value2(), r.value3(), r.value4(), r.value5(), r.value6(), r.value7(), r.value8(), r.value9(), r.value10(), r.value11(), r.value12(), r.value13(), r.value14(), r.value15(), r.value16());
    }

    /**
     * Create a {@link RecordMapper} that can map from {@link Record17} to a user
     * type in a type safe way.
     * <p>
     * Combine this with e.g. {@link Functions#nullOnAllNull(Function17)} or
     * {@link Functions#nullOnAnyNull(Function17)} to achieve <code>null</code>
     * safety when mapping nested rows from <code>LEFT JOIN</code> etc.
     */
    public static final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, R extends Record17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>, U> RecordMapper<R, U> mapping(
        Function17<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? super T12, ? super T13, ? super T14, ? super T15, ? super T16, ? super T17, ? extends U> function
    ) {
        return r -> function.apply(r.value1(), r.value2(), r.value3(), r.value4(), r.value5(), r.value6(), r.value7(), r.value8(), r.value9(), r.value10(), r.value11(), r.value12(), r.value13(), r.value14(), r.value15(), r.value16(), r.value17());
    }

    /**
     * Create a {@link RecordMapper} that can map from {@link Record18} to a user
     * type in a type safe way.
     * <p>
     * Combine this with e.g. {@link Functions#nullOnAllNull(Function18)} or
     * {@link Functions#nullOnAnyNull(Function18)} to achieve <code>null</code>
     * safety when mapping nested rows from <code>LEFT JOIN</code> etc.
     */
    public static final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, R extends Record18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>, U> RecordMapper<R, U> mapping(
        Function18<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? super T12, ? super T13, ? super T14, ? super T15, ? super T16, ? super T17, ? super T18, ? extends U> function
    ) {
        return r -> function.apply(r.value1(), r.value2(), r.value3(), r.value4(), r.value5(), r.value6(), r.value7(), r.value8(), r.value9(), r.value10(), r.value11(), r.value12(), r.value13(), r.value14(), r.value15(), r.value16(), r.value17(), r.value18());
    }

    /**
     * Create a {@link RecordMapper} that can map from {@link Record19} to a user
     * type in a type safe way.
     * <p>
     * Combine this with e.g. {@link Functions#nullOnAllNull(Function19)} or
     * {@link Functions#nullOnAnyNull(Function19)} to achieve <code>null</code>
     * safety when mapping nested rows from <code>LEFT JOIN</code> etc.
     */
    public static final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, R extends Record19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>, U> RecordMapper<R, U> mapping(
        Function19<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? super T12, ? super T13, ? super T14, ? super T15, ? super T16, ? super T17, ? super T18, ? super T19, ? extends U> function
    ) {
        return r -> function.apply(r.value1(), r.value2(), r.value3(), r.value4(), r.value5(), r.value6(), r.value7(), r.value8(), r.value9(), r.value10(), r.value11(), r.value12(), r.value13(), r.value14(), r.value15(), r.value16(), r.value17(), r.value18(), r.value19());
    }

    /**
     * Create a {@link RecordMapper} that can map from {@link Record20} to a user
     * type in a type safe way.
     * <p>
     * Combine this with e.g. {@link Functions#nullOnAllNull(Function20)} or
     * {@link Functions#nullOnAnyNull(Function20)} to achieve <code>null</code>
     * safety when mapping nested rows from <code>LEFT JOIN</code> etc.
     */
    public static final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, R extends Record20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>, U> RecordMapper<R, U> mapping(
        Function20<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? super T12, ? super T13, ? super T14, ? super T15, ? super T16, ? super T17, ? super T18, ? super T19, ? super T20, ? extends U> function
    ) {
        return r -> function.apply(r.value1(), r.value2(), r.value3(), r.value4(), r.value5(), r.value6(), r.value7(), r.value8(), r.value9(), r.value10(), r.value11(), r.value12(), r.value13(), r.value14(), r.value15(), r.value16(), r.value17(), r.value18(), r.value19(), r.value20());
    }

    /**
     * Create a {@link RecordMapper} that can map from {@link Record21} to a user
     * type in a type safe way.
     * <p>
     * Combine this with e.g. {@link Functions#nullOnAllNull(Function21)} or
     * {@link Functions#nullOnAnyNull(Function21)} to achieve <code>null</code>
     * safety when mapping nested rows from <code>LEFT JOIN</code> etc.
     */
    public static final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, R extends Record21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>, U> RecordMapper<R, U> mapping(
        Function21<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? super T12, ? super T13, ? super T14, ? super T15, ? super T16, ? super T17, ? super T18, ? super T19, ? super T20, ? super T21, ? extends U> function
    ) {
        return r -> function.apply(r.value1(), r.value2(), r.value3(), r.value4(), r.value5(), r.value6(), r.value7(), r.value8(), r.value9(), r.value10(), r.value11(), r.value12(), r.value13(), r.value14(), r.value15(), r.value16(), r.value17(), r.value18(), r.value19(), r.value20(), r.value21());
    }

    /**
     * Create a {@link RecordMapper} that can map from {@link Record22} to a user
     * type in a type safe way.
     * <p>
     * Combine this with e.g. {@link Functions#nullOnAllNull(Function22)} or
     * {@link Functions#nullOnAnyNull(Function22)} to achieve <code>null</code>
     * safety when mapping nested rows from <code>LEFT JOIN</code> etc.
     */
    public static final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, R extends Record22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>, U> RecordMapper<R, U> mapping(
        Function22<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? super T12, ? super T13, ? super T14, ? super T15, ? super T16, ? super T17, ? super T18, ? super T19, ? super T20, ? super T21, ? super T22, ? extends U> function
    ) {
        return r -> function.apply(r.value1(), r.value2(), r.value3(), r.value4(), r.value5(), r.value6(), r.value7(), r.value8(), r.value9(), r.value10(), r.value11(), r.value12(), r.value13(), r.value14(), r.value15(), r.value16(), r.value17(), r.value18(), r.value19(), r.value20(), r.value21(), r.value22());
    }



    private static final Object NULL = new Object();

    private static final <T> Function<T, Object> wrapNulls() {
        return t -> t == null ? NULL : t;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static final <K, V> Function<Map<Object, V>, Map<K, V>> unwrapNulls() {
        return map -> {
            if (map.containsKey(NULL))
                map.put(null, map.remove(NULL));

            return (Map) map;
        };
    }
}
