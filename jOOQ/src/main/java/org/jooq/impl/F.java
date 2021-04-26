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

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;


/*
 * This file contains a few functional interfaces that are missing from the JDK.
 */

/**
 * A checked exception throwing {@link Runnable}.
 */
@FunctionalInterface
interface ThrowingRunnable<E extends Throwable> {
    void run() throws E;
}

/**
 * A checked exception throwing {@link Consumer}.
 */
@FunctionalInterface
interface ThrowingConsumer<T, E extends Throwable> {
    void accept(T t) throws E;
}

/**
 * A checked exception throwing {@link Supplier}.
 */
@FunctionalInterface
interface ThrowingSupplier<T, E extends Throwable> {
    T get() throws E;
}

/**
 * A checked exception throwing {@link Function}.
 */
@FunctionalInterface
interface ThrowingFunction<T, R, E extends Throwable> {
    R apply(T t) throws E;
}

/**
 * A checked exception throwing {@link Predicate}.
 */
@FunctionalInterface
interface ThrowingPredicate<T, E extends Throwable> {
    boolean test(T t) throws E;
}

/**
 * A checked exception throwing {@link Predicate}.
 */
@FunctionalInterface
interface ThrowingIntPredicate<T, E extends Throwable> {
    boolean test(T t, int i) throws E;
}

/**
 * A checked exception throwing {@link IntFunction}.
 */
@FunctionalInterface
interface ThrowingIntFunction<R, E extends Throwable> {
    R apply(int t) throws E;
}

/**
 * A checked exception throwing {@link BiFunction}.
 */
@FunctionalInterface
interface ThrowingBiFunction<T1, T2, R, E extends Throwable> {
    R apply(T1 t1, T2 t2) throws E;
}

/**
 * A checked exception throwing {@link IntIntFunction}.
 */
@FunctionalInterface
interface ThrowingIntIntFunction<R, E extends Throwable> {
    R apply(int i, int j) throws E;
}

/**
 * A checked exception throwing {@link ObjIntFunction}.
 */
@FunctionalInterface
interface ThrowingObjIntFunction<T, R, E extends Throwable> {
    R apply(T t, int i) throws E;
}

/**
 * A missing primitive type {@link Consumer} for booleans.
 */
@FunctionalInterface
interface BooleanConsumer {
    void accept(boolean b);
}

/**
 * A missing primitive type {@link BiFunction} for references and ints.
 */
@FunctionalInterface
interface ObjIntFunction<T, R> {
    R apply(T t, int i);
}
