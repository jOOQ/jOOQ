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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
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
package org.jooq.impl;

import java.util.function.Consumer;

import org.jooq.SubscriberProvider;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 * A default, context unaware implementation of the {@link SubscriberProvider}
 * SPI.
 *
 * @author Lukas Eder
 */
public class DefaultSubscriberProvider<C> implements SubscriberProvider<C> {

    @Override
    public final C context() {
        return null;
    }

    @Override
    public final C context(Subscriber<?> subscriber) {
        return null;
    }

    @Override
    public final <T> Subscriber<T> subscriber(
        Consumer<? super Subscription> onSubscribe,
        Consumer<? super T> onNext,
        Consumer<? super Throwable> onError,
        Runnable onComplete,
        C context
    ) {
        return new Subscriber<T>() {
            @Override
            public void onSubscribe(Subscription s) {
                onSubscribe.accept(s);
            }

            @Override
            public void onNext(T t) {
                onNext.accept(t);
            }

            @Override
            public void onError(Throwable t) {
                onError.accept(t);
            }

            @Override
            public void onComplete() {
                onComplete.run();
            }
        };
    }
}
