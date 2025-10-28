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

import static org.jooq.SQLDialect.TRINO;
import static org.jooq.impl.R2DBC.mapping;
import static org.jooq.impl.Tools.blocking;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.function.Function;

import org.jooq.Batch;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.Publisher;
import org.jooq.SQLDialect;
import org.jooq.impl.R2DBC.RowCount;

import org.reactivestreams.Subscriber;

/**
 * @author Lukas Eder
 */
abstract class AbstractBatch implements Batch {

    static final Set<SQLDialect> NO_SUPPORT_BATCH = SQLDialect.supportedBy(TRINO);
    final Configuration          configuration;
    final DSLContext             dsl;

    AbstractBatch(Configuration configuration) {
        this.configuration = configuration;
        this.dsl = DSL.using(configuration);
    }

    @Override
    public final CompletionStage<int[]> executeAsync() {
        return executeAsync(configuration.executorProvider().provide());
    }

    @Override
    public final CompletionStage<int[]> executeAsync(Executor executor) {
        return ExecutorProviderCompletionStage.of(CompletableFuture.supplyAsync(blocking(this::execute), executor), () -> executor);
    }

    @Override
    public final CompletionStage<long[]> executeLargeAsync() {
        return executeLargeAsync(configuration.executorProvider().provide());
    }

    @Override
    public final CompletionStage<long[]> executeLargeAsync(Executor executor) {
        return ExecutorProviderCompletionStage.of(CompletableFuture.supplyAsync(blocking(this::executeLarge), executor), () -> executor);
    }

    @Override
    public final void subscribe(Subscriber<? super Integer> subscriber) {
        subscribe0(subscriber, RowCount::rows, false);
    }

    @Override
    public Publisher<Long> largePublisher() {
        return s -> subscribe0(s, RowCount::largeRows, true);
    }

    final <R> void subscribe0(
        Subscriber<? super R> subscriber,
        Function<? super RowCount, ? extends R> extract,
        boolean large
    ) {
        subscribe0(mapping(subscriber, extract, dsl.configuration().subscriberProvider()));
    }

    abstract void subscribe0(
        Subscriber<? super RowCount> subscriber
    );
}
