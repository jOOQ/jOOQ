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



import java.util.IntSummaryStatistics;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.IntBinaryOperator;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntToDoubleFunction;
import java.util.function.IntToLongFunction;
import java.util.function.IntUnaryOperator;
import java.util.function.ObjIntConsumer;
import java.util.function.Supplier;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * An {@link IntStream} wrapper that auto-closes itself:
 * <ul>
 * <li>Upon failure</li>
 * <li>Upon a terminal operation</li>
 * </ul>
 *
 * @author Lukas Eder
 */
class AutoClosingIntStream extends AutoClosingBaseStream<Integer, IntStream, AutoClosingIntStream> implements IntStream {

    AutoClosingIntStream(IntStream delegate, Consumer<Optional<Throwable>> onComplete) {
        super(delegate, onComplete);
    }

    @Override
    final IntStream delegateOf(IntStream newDelegate) {
        return delegate(newDelegate);
    }

    // -------------------------------------------------------------------------
    // These methods break the auto-closing semantics
    // -------------------------------------------------------------------------

    @Override
    public final PrimitiveIterator.OfInt iterator() {
        return delegate.iterator();
    }

    @Override
    public final Spliterator.OfInt spliterator() {
        return delegate.spliterator();
    }

    // -------------------------------------------------------------------------
    // These methods forward to delegate stream, and wrap afresh
    // -------------------------------------------------------------------------

    @Override
    public final IntStream filter(IntPredicate predicate) {
        return delegate(delegate.filter(predicate));
    }

    @Override
    public final IntStream map(IntUnaryOperator mapper) {
        return delegate(delegate.map(mapper));
    }

    @Override
    public final <U> Stream<U> mapToObj(IntFunction<? extends U> mapper) {
        return delegate(delegate.mapToObj(mapper));
    }

    @Override
    public final LongStream mapToLong(IntToLongFunction mapper) {
        return delegate(delegate.mapToLong(mapper));
    }

    @Override
    public final DoubleStream mapToDouble(IntToDoubleFunction mapper) {
        return delegate(delegate.mapToDouble(mapper));
    }

    @Override
    public final IntStream flatMap(IntFunction<? extends IntStream> mapper) {
        return delegate(delegate.flatMap(mapper));
    }

    @Override
    public final IntStream distinct() {
        return delegate(delegate.distinct());
    }

    @Override
    public final IntStream sorted() {
        return delegate(delegate.sorted());
    }

    @Override
    public final IntStream peek(IntConsumer action) {
        return delegate.peek(action);
    }

    @Override
    public final IntStream limit(long maxSize) {
        return delegate(delegate.limit(maxSize));
    }

    @Override
    public final IntStream skip(long n) {
        return delegate(delegate.skip(n));
    }

    @Override
    public final LongStream asLongStream() {
        return delegate(delegate.asLongStream());
    }

    @Override
    public final DoubleStream asDoubleStream() {
        return delegate(delegate.asDoubleStream());
    }

    @Override
    public final Stream<Integer> boxed() {
        return delegate(delegate.boxed());
    }

    // -------------------------------------------------------------------------
    // These methods implement terminal op semantics, and implement auto-closing
    // -------------------------------------------------------------------------

    @Override
    public final void forEach(IntConsumer action) {
        terminalOp(() -> delegate.forEach(action));
    }

    @Override
    public final void forEachOrdered(IntConsumer action) {
        terminalOp(() -> delegate.forEachOrdered(action));
    }

    @Override
    public final int[] toArray() {
        return terminalOp(() -> delegate.toArray());
    }

    @Override
    public final int reduce(int identity, IntBinaryOperator op) {
        return terminalOp(() -> delegate.reduce(identity, op));
    }

    @Override
    public final OptionalInt reduce(IntBinaryOperator op) {
        return terminalOp(() -> delegate.reduce(op));
    }

    @Override
    public final <R> R collect(Supplier<R> supplier, ObjIntConsumer<R> accumulator, BiConsumer<R, R> combiner) {
        return terminalOp(() -> delegate.collect(supplier, accumulator, combiner));
    }

    @Override
    public final int sum() {
        return terminalOp(() -> delegate.sum());
    }

    @Override
    public final OptionalInt min() {
        return terminalOp(() -> delegate.min());
    }

    @Override
    public final OptionalInt max() {
        return terminalOp(() -> delegate.max());
    }

    @Override
    public final long count() {
        return terminalOp(() -> delegate.count());
    }

    @Override
    public final OptionalDouble average() {
        return terminalOp(() -> delegate.average());
    }

    @Override
    public final IntSummaryStatistics summaryStatistics() {
        return terminalOp(() -> delegate.summaryStatistics());
    }

    @Override
    public final boolean anyMatch(IntPredicate predicate) {
        return terminalOp(() -> delegate.anyMatch(predicate));
    }

    @Override
    public final boolean allMatch(IntPredicate predicate) {
        return terminalOp(() -> delegate.allMatch(predicate));
    }

    @Override
    public final boolean noneMatch(IntPredicate predicate) {
        return terminalOp(() -> delegate.noneMatch(predicate));
    }

    @Override
    public final OptionalInt findFirst() {
        return terminalOp(() -> delegate.findFirst());
    }

    @Override
    public final OptionalInt findAny() {
        return terminalOp(() -> delegate.findAny());
    }
}

