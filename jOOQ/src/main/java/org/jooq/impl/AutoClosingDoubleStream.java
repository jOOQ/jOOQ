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



import java.util.DoubleSummaryStatistics;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleFunction;
import java.util.function.DoublePredicate;
import java.util.function.DoubleToIntFunction;
import java.util.function.DoubleToLongFunction;
import java.util.function.DoubleUnaryOperator;
import java.util.function.ObjDoubleConsumer;
import java.util.function.Supplier;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * A {@link DoubleStream} wrapper that auto-closes itself:
 * <ul>
 * <li>Upon failure</li>
 * <li>Upon a terminal operation</li>
 * </ul>
 *
 * @author Lukas Eder
 */
class AutoClosingDoubleStream extends AutoClosingBaseStream<Double, DoubleStream, AutoClosingDoubleStream> implements DoubleStream {

    AutoClosingDoubleStream(DoubleStream delegate, Consumer<Optional<Throwable>> onComplete) {
        super(delegate, onComplete);
    }

    @Override
    final DoubleStream delegateOf(DoubleStream newDelegate) {
        return delegate(newDelegate);
    }

    // -------------------------------------------------------------------------
    // These methods break the auto-closing semantics
    // -------------------------------------------------------------------------

    @Override
    public final PrimitiveIterator.OfDouble iterator() {
        return delegate.iterator();
    }

    @Override
    public final Spliterator.OfDouble spliterator() {
        return delegate.spliterator();
    }

    // -------------------------------------------------------------------------
    // These methods forward to delegate stream, and wrap afresh
    // -------------------------------------------------------------------------

    @Override
    public final DoubleStream filter(DoublePredicate predicate) {
        return delegate(delegate.filter(predicate));
    }

    @Override
    public final DoubleStream map(DoubleUnaryOperator mapper) {
        return delegate(delegate.map(mapper));
    }

    @Override
    public final <U> Stream<U> mapToObj(DoubleFunction<? extends U> mapper) {
        return delegate(delegate.mapToObj(mapper));
    }

    @Override
    public final IntStream mapToInt(DoubleToIntFunction mapper) {
        return delegate(delegate.mapToInt(mapper));
    }

    @Override
    public final LongStream mapToLong(DoubleToLongFunction mapper) {
        return delegate(delegate.mapToLong(mapper));
    }

    @Override
    public final DoubleStream flatMap(DoubleFunction<? extends DoubleStream> mapper) {
        return delegate(delegate.flatMap(mapper));
    }

    @Override
    public final DoubleStream distinct() {
        return delegate(delegate.distinct());
    }

    @Override
    public final DoubleStream sorted() {
        return delegate(delegate.sorted());
    }

    @Override
    public final DoubleStream peek(DoubleConsumer action) {
        return delegate.peek(action);
    }

    @Override
    public final DoubleStream limit(long maxSize) {
        return delegate(delegate.limit(maxSize));
    }

    @Override
    public final DoubleStream skip(long n) {
        return delegate(delegate.skip(n));
    }

    @Override
    public final Stream<Double> boxed() {
        return delegate(delegate.boxed());
    }

    // -------------------------------------------------------------------------
    // These methods implement terminal op semantics, and implement auto-closing
    // -------------------------------------------------------------------------

    @Override
    public final void forEach(DoubleConsumer action) {
        terminalOp(() -> delegate.forEach(action));
    }

    @Override
    public final void forEachOrdered(DoubleConsumer action) {
        terminalOp(() -> delegate.forEachOrdered(action));
    }

    @Override
    public final double[] toArray() {
        return terminalOp(() -> delegate.toArray());
    }

    @Override
    public final double reduce(double identity, DoubleBinaryOperator op) {
        return terminalOp(() -> delegate.reduce(identity, op));
    }

    @Override
    public final OptionalDouble reduce(DoubleBinaryOperator op) {
        return terminalOp(() -> delegate.reduce(op));
    }

    @Override
    public final <R> R collect(Supplier<R> supplier, ObjDoubleConsumer<R> accumulator, BiConsumer<R, R> combiner) {
        return terminalOp(() -> delegate.collect(supplier, accumulator, combiner));
    }

    @Override
    public final double sum() {
        return terminalOp(() -> delegate.sum());
    }

    @Override
    public final OptionalDouble min() {
        return terminalOp(() -> delegate.min());
    }

    @Override
    public final OptionalDouble max() {
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
    public final DoubleSummaryStatistics summaryStatistics() {
        return terminalOp(() -> delegate.summaryStatistics());
    }

    @Override
    public final boolean anyMatch(DoublePredicate predicate) {
        return terminalOp(() -> delegate.anyMatch(predicate));
    }

    @Override
    public final boolean allMatch(DoublePredicate predicate) {
        return terminalOp(() -> delegate.allMatch(predicate));
    }

    @Override
    public final boolean noneMatch(DoublePredicate predicate) {
        return terminalOp(() -> delegate.noneMatch(predicate));
    }

    @Override
    public final OptionalDouble findFirst() {
        return terminalOp(() -> delegate.findFirst());
    }

    @Override
    public final OptionalDouble findAny() {
        return terminalOp(() -> delegate.findAny());
    }
}

