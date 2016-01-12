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



import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.BaseStream;
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
abstract class AutoClosingBaseStream<T, S extends BaseStream<T, S>, U extends AutoClosingBaseStream<T, S, U>> implements BaseStream<T, S> {

    final S                             delegate;
    final Consumer<Optional<Throwable>> onComplete;

    AutoClosingBaseStream(S delegate, Consumer<Optional<Throwable>> onComplete) {
        this.delegate = delegate;
        this.onComplete = onComplete;
    }

    abstract S delegateOf(S newDelegate);

    final <X> Stream<X> delegate(Stream<X> newDelegate) {
        return new AutoClosingStream<>(newDelegate, onComplete);
    }

    final IntStream delegate(IntStream newDelegate) {
        return new AutoClosingIntStream(newDelegate, onComplete);
    }

    final LongStream delegate(LongStream newDelegate) {
        return new AutoClosingLongStream(newDelegate, onComplete);
    }

    final DoubleStream delegate(DoubleStream newDelegate) {
        return new AutoClosingDoubleStream(newDelegate, onComplete);
    }

    final void terminalOp(Runnable runnable) {
        terminalOp(() -> {
            runnable.run();
            return null;
        });
    }

    final <R1> R1 terminalOp(Supplier<R1> supplier) {
        R1 result = null;

        try {
            result = supplier.get();
            close();
        }
        catch (Throwable e) {
            close();
            throw e;
        }

        return result;
    }

    // -------------------------------------------------------------------------
    // These methods are not affected by the auto-closing semantics
    // -------------------------------------------------------------------------

    @Override
    public final boolean isParallel() {
        return delegate.isParallel();
    }

    @Override
    public final void close() {
        delegate.close();
    }

    // -------------------------------------------------------------------------
    // These methods forward to delegate stream, and wrap afresh
    // -------------------------------------------------------------------------

    @Override
    public final S sequential() {
        return delegateOf(delegate.sequential());
    }

    @Override
    public final S parallel() {
        return delegateOf(delegate.parallel());
    }

    @Override
    public final S unordered() {
        return delegateOf(delegate.unordered());
    }

    @Override
    public final S onClose(Runnable closeHandler) {
        return delegateOf(delegate.onClose(closeHandler));
    }
}

