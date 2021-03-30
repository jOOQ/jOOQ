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

// ...
import java.util.function.Consumer;

import org.reactivestreams.FlowAdapters;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;


/**
 * A bridge from {@link java.util.concurrent.Flow.Publisher} to
 * {@link org.reactivestreams.Publisher} for compatibility purposes.
 *
 * @author Lukas Eder
 */
@FunctionalInterface
public interface Publisher<T> extends org.reactivestreams.Publisher<T> {










    /**
     * Create a {@link Subscriber} from a set of lambdas.
     * <p>
     * This is used for internal purposes and thus subject for change.
     */
    @Internal
    static <T> Subscriber<T> subscriber(
        Consumer<? super Subscription> subscription,
        Consumer<? super T> onNext,
        Consumer<? super Throwable> onError,
        Runnable onComplete
    ) {
        return new Subscriber<T>() {
            @Override
            public void onSubscribe(Subscription s) {
                subscription.accept(s);
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

