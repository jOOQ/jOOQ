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



import java.util.LongSummaryStatistics;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalLong;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.LongBinaryOperator;
import java.util.function.LongConsumer;
import java.util.function.LongFunction;
import java.util.function.LongPredicate;
import java.util.function.LongToDoubleFunction;
import java.util.function.LongToIntFunction;
import java.util.function.LongUnaryOperator;
import java.util.function.ObjLongConsumer;
import java.util.function.Supplier;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * A {@link LongStream} wrapper that auto-closes itself:
 * <ul>
 * <li>Upon failure</li>
 * <li>Upon a terminal operation</li>
 * </ul>
 *
 * @author Lukas Eder
 */
class AutoClosingLongStream extends AutoClosingBaseStream<Long, LongStream, AutoClosingLongStream> implements LongStream {

    AutoClosingLongStream(LongStream delegate, Consumer<Optional<Throwable>> onComplete) {
        super(delegate, onComplete);
    }

    @Override
    final LongStream delegateOf(LongStream newDelegate) {
        return delegate(newDelegate);
    }

    // -------------------------------------------------------------------------
    // These methods break the auto-closing semantics
    // -------------------------------------------------------------------------

    @Override
    public final PrimitiveIterator.OfLong iterator() {
        return delegate.iterator();
    }

    @Override
    public final Spliterator.OfLong spliterator() {
        return delegate.spliterator();
    }

    // -------------------------------------------------------------------------
    // These methods forward to delegate stream, and wrap afresh
    // -------------------------------------------------------------------------

    @Override
    public final LongStream filter(LongPredicate predicate) {
        return delegate(delegate.filter(predicate));
    }

    @Override
    public final LongStream map(LongUnaryOperator mapper) {
        return delegate(delegate.map(mapper));
    }

    @Override
    public final <U> Stream<U> mapToObj(LongFunction<? extends U> mapper) {
        return delegate(delegate.mapToObj(mapper));
    }

    @Override
    public final IntStream mapToInt(LongToIntFunction mapper) {
        return delegate(delegate.mapToInt(mapper));
    }

    @Override
    public final DoubleStream mapToDouble(LongToDoubleFunction mapper) {
        return delegate(delegate.mapToDouble(mapper));
    }

    @Override
    public final LongStream flatMap(LongFunction<? extends LongStream> mapper) {
        return delegate(delegate.flatMap(mapper));
    }

    @Override
    public final LongStream distinct() {
        return delegate(delegate.distinct());
    }

    @Override
    public final LongStream sorted() {
        return delegate(delegate.sorted());
    }

    @Override
    public final LongStream peek(LongConsumer action) {
        return delegate.peek(action);
    }

    @Override
    public final LongStream limit(long maxSize) {
        return delegate(delegate.limit(maxSize));
    }

    @Override
    public final LongStream skip(long n) {
        return delegate(delegate.skip(n));
    }

    @Override
    public final DoubleStream asDoubleStream() {
        return delegate(delegate.asDoubleStream());
    }

    @Override
    public final Stream<Long> boxed() {
        return delegate(delegate.boxed());
    }

    // -------------------------------------------------------------------------
    // These methods implement terminal op semantics, and implement auto-closing
    // -------------------------------------------------------------------------

    @Override
    public final void forEach(LongConsumer action) {
        terminalOp(() -> delegate.forEach(action));
    }

    @Override
    public final void forEachOrdered(LongConsumer action) {
        terminalOp(() -> delegate.forEachOrdered(action));
    }

    @Override
    public final long[] toArray() {
        return terminalOp(() -> delegate.toArray());
    }

    @Override
    public final long reduce(long identity, LongBinaryOperator op) {
        return terminalOp(() -> delegate.reduce(identity, op));
    }

    @Override
    public final OptionalLong reduce(LongBinaryOperator op) {
        return terminalOp(() -> delegate.reduce(op));
    }

    @Override
    public final <R> R collect(Supplier<R> supplier, ObjLongConsumer<R> accumulator, BiConsumer<R, R> combiner) {
        return terminalOp(() -> delegate.collect(supplier, accumulator, combiner));
    }

    @Override
    public final long sum() {
        return terminalOp(() -> delegate.sum());
    }

    @Override
    public final OptionalLong min() {
        return terminalOp(() -> delegate.min());
    }

    @Override
    public final OptionalLong max() {
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
    public final LongSummaryStatistics summaryStatistics() {
        return terminalOp(() -> delegate.summaryStatistics());
    }

    @Override
    public final boolean anyMatch(LongPredicate predicate) {
        return terminalOp(() -> delegate.anyMatch(predicate));
    }

    @Override
    public final boolean allMatch(LongPredicate predicate) {
        return terminalOp(() -> delegate.allMatch(predicate));
    }

    @Override
    public final boolean noneMatch(LongPredicate predicate) {
        return terminalOp(() -> delegate.noneMatch(predicate));
    }

    @Override
    public final OptionalLong findFirst() {
        return terminalOp(() -> delegate.findFirst());
    }

    @Override
    public final OptionalLong findAny() {
        return terminalOp(() -> delegate.findAny());
    }
}

