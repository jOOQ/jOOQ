/**
 * This package contains jOOQ's implementation classes.
 * <p>
 * This package provides implementations for the jOOQ API from
 * <code>org.jooq</code>, whose interfaces are constructed mostly through the
 * {@link org.jooq.impl.DSL} and {@link org.jooq.DSLContext} classes, which hide
 * implementation facts from the public API.
 */
package org.jooq.impl;


import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/*
 * Also, because we can, we can place some classes that don't deserve their own
 * file in here:
 * https://docs.oracle.com/javase/specs/jls/se17/html/jls-7.html#jls-7.4.1
 *
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

    default ThrowingPredicate<T, E> negate() {
        return t -> !test(t);
    }
}

/**
 * A checked exception throwing {@link java.util.function.Predicate}.
 */
@FunctionalInterface
interface ThrowingIntPredicate<T, E extends Throwable> {
    boolean test(T t, int i) throws E;

    default ThrowingIntPredicate<T, E> negate() {
        return (t, i) -> !test(t, i);
    }
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

    default <V> ObjIntFunction<T, V> andThen(ObjIntFunction<? super R, ? extends V> after) {
        return (t, i) -> after.apply(apply(t, i), i);
    }
}

/**
 * A missing primitive type {@link BiPredicate} for references and ints.
 */
@FunctionalInterface
interface ObjIntPredicate<T> {
    boolean test(T t, int i);
}
