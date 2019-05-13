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



import java.util.concurrent.Flow;

/**
 * A bridge mapping a JDK 9 {@link Flow.Subscriber} to a reactive streams
 * {@link org.reactivestreams.Subscriber}
 *
 * @author Lukas Eder
 */
final class FlowToReactiveStreamsSubscriberBridge<T> implements org.reactivestreams.Subscriber<T> {

    final Flow.Subscriber<? super T> delegate;

    FlowToReactiveStreamsSubscriberBridge(Flow.Subscriber<? super T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public void onSubscribe(org.reactivestreams.Subscription s) {
        delegate.onSubscribe(new Flow.Subscription() {
            @Override
            public void request(long n) {
                s.request(n);
            }

            @Override
            public void cancel() {
                s.cancel();
            }
        });
    }

    @Override
    public void onNext(T t) {
        delegate.onNext(t);
    }

    @Override
    public void onError(Throwable t) {
        delegate.onError(t);
    }

    @Override
    public void onComplete() {
        delegate.onComplete();
    }
}

