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

import java.util.concurrent.Flow;
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
public interface Publisher<T> extends org.reactivestreams.Publisher<T>, Flow.Publisher<T> {



    @Override
    default void subscribe(Flow.Subscriber<? super T> subscriber) {
        subscribe(FlowAdapters.toSubscriber(subscriber));
    }


}

