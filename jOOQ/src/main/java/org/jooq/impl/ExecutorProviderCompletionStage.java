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
 */
package org.jooq.impl;



import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import org.jooq.ExecutorProvider;

/**
 * A {@link CompletionStage} that defaults to an {@link ExecutorProvider} for
 * all async methods that do not have an explicit {@link Executor}.
 * <p>
 * In jOOQ, the {@link ExecutorProvider} SPI is used for all types of
 * asynchronous tasks. It overrides standard {@link CompletableFuture} behaviour
 * in case users prefer not to use the {@link ForkJoinPool#commonPool()}.
 *
 * @author Lukas Eder
 */
final class ExecutorProviderCompletionStage<T> implements CompletionStage<T> {

    private final CompletionStage<T> delegate;
    private final ExecutorProvider   provider;

    static final <T> ExecutorProviderCompletionStage<T> of(CompletionStage<T> delegate, ExecutorProvider provider) {
        return new ExecutorProviderCompletionStage<>(delegate, provider);
    }

    ExecutorProviderCompletionStage(CompletionStage<T> delegate, ExecutorProvider provider) {
        this.delegate = delegate;
        this.provider = provider;
    }

    @Override
    public final <U> CompletionStage<U> thenApply(Function<? super T, ? extends U> fn) {
        return of(delegate.thenApply(fn), provider);
    }

    @Override
    public final <U> CompletionStage<U> thenApplyAsync(Function<? super T, ? extends U> fn) {
        return of(delegate.thenApplyAsync(fn, provider.provide()), provider);
    }

    @Override
    public final <U> CompletionStage<U> thenApplyAsync(Function<? super T, ? extends U> fn, Executor executor) {
        return of(delegate.thenApplyAsync(fn, executor), provider);
    }

    @Override
    public final CompletionStage<Void> thenAccept(Consumer<? super T> action) {
        return of(delegate.thenAccept(action), provider);
    }

    @Override
    public final CompletionStage<Void> thenAcceptAsync(Consumer<? super T> action) {
        return of(delegate.thenAcceptAsync(action, provider.provide()), provider);
    }

    @Override
    public final CompletionStage<Void> thenAcceptAsync(Consumer<? super T> action, Executor executor) {
        return of(delegate.thenAcceptAsync(action, executor), provider);
    }

    @Override
    public final CompletionStage<Void> thenRun(Runnable action) {
        return of(delegate.thenRun(action), provider);
    }

    @Override
    public final CompletionStage<Void> thenRunAsync(Runnable action) {
        return of(delegate.thenRunAsync(action, provider.provide()), provider);
    }

    @Override
    public final CompletionStage<Void> thenRunAsync(Runnable action, Executor executor) {
        return of(delegate.thenRunAsync(action, executor), provider);
    }

    @Override
    public final <U, V> CompletionStage<V> thenCombine(CompletionStage<? extends U> other, BiFunction<? super T, ? super U, ? extends V> fn) {
        return of(delegate.thenCombine(other, fn), provider);
    }

    @Override
    public final <U, V> CompletionStage<V> thenCombineAsync(CompletionStage<? extends U> other, BiFunction<? super T, ? super U, ? extends V> fn) {
        return of(delegate.thenCombineAsync(other, fn, provider.provide()), provider);
    }

    @Override
    public final <U, V> CompletionStage<V> thenCombineAsync(CompletionStage<? extends U> other, BiFunction<? super T, ? super U, ? extends V> fn, Executor executor) {
        return of(delegate.thenCombineAsync(other, fn, executor), provider);
    }

    @Override
    public final <U> CompletionStage<Void> thenAcceptBoth(CompletionStage<? extends U> other, BiConsumer<? super T, ? super U> action) {
        return of(delegate.thenAcceptBoth(other, action), provider);
    }

    @Override
    public final <U> CompletionStage<Void> thenAcceptBothAsync(CompletionStage<? extends U> other, BiConsumer<? super T, ? super U> action) {
        return of(delegate.thenAcceptBothAsync(other, action, provider.provide()), provider);
    }

    @Override
    public final <U> CompletionStage<Void> thenAcceptBothAsync(CompletionStage<? extends U> other, BiConsumer<? super T, ? super U> action, Executor executor) {
        return of(delegate.thenAcceptBothAsync(other, action, executor), provider);
    }

    @Override
    public final CompletionStage<Void> runAfterBoth(CompletionStage<?> other, Runnable action) {
        return of(delegate.runAfterBoth(other, action), provider);
    }

    @Override
    public final CompletionStage<Void> runAfterBothAsync(CompletionStage<?> other, Runnable action) {
        return of(delegate.runAfterBothAsync(other, action, provider.provide()), provider);
    }

    @Override
    public final CompletionStage<Void> runAfterBothAsync(CompletionStage<?> other, Runnable action, Executor executor) {
        return of(delegate.runAfterBothAsync(other, action, executor), provider);
    }

    @Override
    public final <U> CompletionStage<U> applyToEither(CompletionStage<? extends T> other, Function<? super T, U> fn) {
        return of(delegate.applyToEither(other, fn), provider);
    }

    @Override
    public final <U> CompletionStage<U> applyToEitherAsync(CompletionStage<? extends T> other, Function<? super T, U> fn) {
        return of(delegate.applyToEitherAsync(other, fn, provider.provide()), provider);
    }

    @Override
    public final <U> CompletionStage<U> applyToEitherAsync(CompletionStage<? extends T> other, Function<? super T, U> fn, Executor executor) {
        return of(delegate.applyToEitherAsync(other, fn, executor), provider);
    }

    @Override
    public final CompletionStage<Void> acceptEither(CompletionStage<? extends T> other, Consumer<? super T> action) {
        return of(delegate.acceptEither(other, action), provider);
    }

    @Override
    public final CompletionStage<Void> acceptEitherAsync(CompletionStage<? extends T> other, Consumer<? super T> action) {
        return of(delegate.acceptEitherAsync(other, action, provider.provide()), provider);
    }

    @Override
    public final CompletionStage<Void> acceptEitherAsync(CompletionStage<? extends T> other, Consumer<? super T> action, Executor executor) {
        return of(delegate.acceptEitherAsync(other, action, executor), provider);
    }

    @Override
    public final CompletionStage<Void> runAfterEither(CompletionStage<?> other, Runnable action) {
        return of(delegate.runAfterEither(other, action), provider);
    }

    @Override
    public final CompletionStage<Void> runAfterEitherAsync(CompletionStage<?> other, Runnable action) {
        return of(delegate.runAfterEitherAsync(other, action, provider.provide()), provider);
    }

    @Override
    public final CompletionStage<Void> runAfterEitherAsync(CompletionStage<?> other, Runnable action, Executor executor) {
        return of(delegate.runAfterEitherAsync(other, action, executor), provider);
    }

    @Override
    public final <U> CompletionStage<U> thenCompose(Function<? super T, ? extends CompletionStage<U>> fn) {
        return of(delegate.thenCompose(fn), provider);
    }

    @Override
    public final <U> CompletionStage<U> thenComposeAsync(Function<? super T, ? extends CompletionStage<U>> fn) {
        return of(delegate.thenComposeAsync(fn, provider.provide()), provider);
    }

    @Override
    public final <U> CompletionStage<U> thenComposeAsync(Function<? super T, ? extends CompletionStage<U>> fn,
        Executor executor) {
        return of(delegate.thenComposeAsync(fn, executor), provider);
    }

    @Override
    public final CompletionStage<T> exceptionally(Function<Throwable, ? extends T> fn) {
        return of(delegate.exceptionally(fn), provider);
    }

    @Override
    public final CompletionStage<T> whenComplete(BiConsumer<? super T, ? super Throwable> action) {
        return of(delegate.whenComplete(action), provider);
    }

    @Override
    public final CompletionStage<T> whenCompleteAsync(BiConsumer<? super T, ? super Throwable> action) {
        return of(delegate.whenCompleteAsync(action, provider.provide()), provider);
    }

    @Override
    public final CompletionStage<T> whenCompleteAsync(BiConsumer<? super T, ? super Throwable> action, Executor executor) {
        return of(delegate.whenCompleteAsync(action, executor), provider);
    }

    @Override
    public final <U> CompletionStage<U> handle(BiFunction<? super T, Throwable, ? extends U> fn) {
        return of(delegate.handle(fn), provider);
    }

    @Override
    public final <U> CompletionStage<U> handleAsync(BiFunction<? super T, Throwable, ? extends U> fn) {
        return of(delegate.handleAsync(fn, provider.provide()), provider);
    }

    @Override
    public final <U> CompletionStage<U> handleAsync(BiFunction<? super T, Throwable, ? extends U> fn, Executor executor) {
        return of(delegate.handleAsync(fn, executor), provider);
    }

    @Override
    public final CompletableFuture<T> toCompletableFuture() {
        return delegate.toCompletableFuture();
    }
}


