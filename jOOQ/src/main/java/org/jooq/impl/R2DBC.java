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
package org.jooq.impl;



import static org.jooq.impl.Internal.subscriber;
import static org.jooq.tools.jdbc.JDBCUtils.safeClose;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import org.jooq.Cursor;
import org.jooq.Record;
import org.jooq.impl.Tools.ThreadGuard;
import org.jooq.impl.Tools.ThreadGuard.Guard;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;


/**
 * A single namespace for all reactive {@link Subscription} and other implementations.
 */
final class R2DBC {

    // -------------------------------------------------------------------------
    // Utilities to pass the TCK
    // -------------------------------------------------------------------------

    static abstract class AbstractSubscription<T> implements Subscription {

        final AtomicBoolean         completed;
        final AtomicLong            requested;
        final Subscriber<? super T> subscriber;
        final Guard                 guard;

        AbstractSubscription(Subscriber<? super T> subscriber) {
            this.completed = new AtomicBoolean();
            this.requested = new AtomicLong();
            this.guard = new Guard();
            this.subscriber = subscriber(
                subscriber::onSubscribe,
                subscriber::onNext,
                subscriber::onError,
                () -> {
                    completed.set(true);
                    subscriber.onComplete();
                }
            );
        }

        @Override
        public final void request(long n) {
            if (n <= 0) {
                subscriber.onError(new IllegalArgumentException("Rule 3.9 non-positive request signals are illegal"));
            }
            else if (!completed.get()) {
                requested.accumulateAndGet(n, (x, y) -> {
                    long r = x + y;

                    // See Long::addExact
                    if (((x ^ r) & (y ^ r)) < 0)
                        return Long.MAX_VALUE;
                    else
                        return r;
                });

                ThreadGuard.run(guard, this::request0, () -> {});
            }
        }

        @Override
        public final void cancel() {
            if (!completed.getAndSet(true))
                cancel0();
        }

        abstract void request0();
        abstract void cancel0();
    }

    // -------------------------------------------------------------------------
    // XXX: Legacy implementation
    // -------------------------------------------------------------------------

    static final class BlockingRecordSubscription<R extends Record> extends AbstractSubscription<R> {
        private final AbstractResultQuery<R> query;
        private volatile Cursor<R>           c;

        BlockingRecordSubscription(AbstractResultQuery<R> query, Subscriber<? super R> subscriber) {
            super(subscriber);

            this.query = query;
        }

        @Override
        final synchronized void request0() {
            try {
                if (c == null)
                    c = query.fetchLazyNonAutoClosing();

                while (requested.getAndUpdate(l -> Math.max(0, l - 1)) > 0) {
                    R r = c.fetchNext();

                    if (r == null) {
                        subscriber.onComplete();
                        safeClose(c);
                        break;
                    }

                    subscriber.onNext(r);
                }
            }
            catch (Throwable t) {
                subscriber.onError(t);
                safeClose(c);
            }
        }

        @Override
        final void cancel0() {
            safeClose(c);
        }
    }

    static final class BlockingRowCountSubscription extends AbstractSubscription<Integer> {
        final AbstractRowCountQuery query;

        BlockingRowCountSubscription(AbstractRowCountQuery query, Subscriber<? super Integer> subscriber) {
            super(subscriber);

            this.query = query;
        }

        @Override
        final void request0() {
            try {
                subscriber.onNext(query.execute());
                subscriber.onComplete();
            }
            catch (Throwable t) {
                subscriber.onError(t);
            }
        }

        @Override
        final void cancel0() {}
    }
}


