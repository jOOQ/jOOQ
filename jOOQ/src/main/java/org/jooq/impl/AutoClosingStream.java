/**
 * Copyright (c) 2009-2016, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
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
package org.jooq.impl;



import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Collector;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * A {@link Stream} wrapper that auto-closes itself:
 * <ul>
 * <li>Upon failure</li>
 * <li>Upon a terminal operation</li>
 * </ul>
 *
 * @author Lukas Eder
 */
class AutoClosingStream<T> extends AutoClosingBaseStream<T, Stream<T>, AutoClosingStream<T>> implements Stream<T> {

    AutoClosingStream(Stream<T> delegate, Consumer<Optional<Throwable>> onComplete) {
        super(delegate, onComplete);
    }

    @Override
    final Stream<T> delegateOf(Stream<T> newDelegate) {
        return delegate(newDelegate);
    }

    // -------------------------------------------------------------------------
    // These methods break the auto-closing semantics
    // -------------------------------------------------------------------------

    @Override
    public final Iterator<T> iterator() {
        return delegate.iterator();
    }

    @Override
    public final Spliterator<T> spliterator() {
        return delegate.spliterator();
    }

    // -------------------------------------------------------------------------
    // These methods forward to delegate stream, and wrap afresh
    // -------------------------------------------------------------------------

    @Override
    public final Stream<T> filter(Predicate<? super T> predicate) {
        return delegate(delegate.filter(predicate));
    }

    @Override
    public final <R> Stream<R> map(Function<? super T, ? extends R> mapper) {
        return delegate(delegate.map(mapper));
    }

    @Override
    public final IntStream mapToInt(ToIntFunction<? super T> mapper) {
        return delegate(delegate.mapToInt(mapper));
    }

    @Override
    public final LongStream mapToLong(ToLongFunction<? super T> mapper) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final DoubleStream mapToDouble(ToDoubleFunction<? super T> mapper) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final <R> Stream<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper) {
        return delegate(delegate.flatMap(mapper));
    }

    @Override
    public final IntStream flatMapToInt(Function<? super T, ? extends IntStream> mapper) {
        return delegate(delegate.flatMapToInt(mapper));
    }

    @Override
    public final LongStream flatMapToLong(Function<? super T, ? extends LongStream> mapper) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final DoubleStream flatMapToDouble(Function<? super T, ? extends DoubleStream> mapper) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final Stream<T> distinct() {
        return delegate(delegate.distinct());
    }

    @Override
    public final Stream<T> sorted() {
        return delegate(delegate.sorted());
    }

    @Override
    public final Stream<T> sorted(Comparator<? super T> comparator) {
        return delegate(delegate.sorted(comparator));
    }

    @Override
    public final Stream<T> peek(Consumer<? super T> action) {
        return delegate(delegate.peek(action));
    }

    @Override
    public final Stream<T> limit(long maxSize) {
        return delegate(delegate.limit(maxSize));
    }

    @Override
    public final Stream<T> skip(long n) {
        return delegate(delegate.skip(n));
    }

    // -------------------------------------------------------------------------
    // These methods implement terminal op semantics, and implement auto-closing
    // -------------------------------------------------------------------------

    @Override
    public final void forEach(Consumer<? super T> action) {
        terminalOp(() -> delegate.forEach(action));
    }

    @Override
    public final void forEachOrdered(Consumer<? super T> action) {
        terminalOp(() -> delegate.forEachOrdered(action));
    }

    @Override
    public final Object[] toArray() {
        return terminalOp(() -> delegate.toArray());
    }

    @Override
    public final <A> A[] toArray(IntFunction<A[]> generator) {
        return terminalOp(() -> delegate.toArray(generator));
    }

    @Override
    public final T reduce(T identity, BinaryOperator<T> accumulator) {
        return terminalOp(() -> delegate.reduce(identity, accumulator));
    }

    @Override
    public final Optional<T> reduce(BinaryOperator<T> accumulator) {
        return terminalOp(() -> delegate.reduce(accumulator));
    }

    @Override
    public final <U> U reduce(U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner) {
        return terminalOp(() -> delegate.reduce(identity, accumulator, combiner));
    }

    @Override
    public final <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> accumulator, BiConsumer<R, R> combiner) {
        return terminalOp(() -> delegate.collect(supplier, accumulator, combiner));
    }

    @Override
    public final <R, A> R collect(Collector<? super T, A, R> collector) {
        return terminalOp(() -> delegate.collect(collector));
    }

    @Override
    public final Optional<T> min(Comparator<? super T> comparator) {
        return terminalOp(() -> delegate.min(comparator));
    }

    @Override
    public final Optional<T> max(Comparator<? super T> comparator) {
        return terminalOp(() -> delegate.max(comparator));
    }

    @Override
    public final long count() {
        return terminalOp(() -> delegate.count());
    }

    @Override
    public final boolean anyMatch(Predicate<? super T> predicate) {
        return terminalOp(() -> delegate.anyMatch(predicate));
    }

    @Override
    public final boolean allMatch(Predicate<? super T> predicate) {
        return terminalOp(() -> delegate.allMatch(predicate));
    }

    @Override
    public final boolean noneMatch(Predicate<? super T> predicate) {
        return terminalOp(() -> delegate.noneMatch(predicate));
    }

    @Override
    public final Optional<T> findFirst() {
        return terminalOp(() -> delegate.findFirst());
    }

    @Override
    public final Optional<T> findAny() {
        return terminalOp(() -> delegate.findAny());
    }
}

