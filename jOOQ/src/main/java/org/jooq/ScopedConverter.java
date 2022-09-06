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

import static org.jooq.Converters.notImplementedBiFunction;
import static org.jooq.Converters.nullable;
import static org.jooq.impl.Internal.converterContext;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.jooq.impl.AbstractScopedConverter;

import org.jetbrains.annotations.NotNull;

/**
 * A special type of {@link Converter} with alternative
 * {@link #from(Object, ConverterContext)} and
 * {@link #to(Object, ConverterContext)} methods.
 * <p>
 * This special converter type can be used wherever an ordinary
 * {@link Converter} is used. jOOQ internal call sites will call the alternative
 * {@link #from(Object, ConverterContext)} and
 * {@link #to(Object, ConverterContext)} methods, instead of
 * {@link #from(Object)} and {@link #to(Object)}, allowing for accessing global
 * {@link Configuration#data()} content.
 */
public interface ScopedConverter<T, U> extends Converter<T, U> {

    /**
     * Read and convert a database object to a user object.
     *
     * @param databaseObject The database object.
     * @param ctx The context of this conversion.
     * @return The user object.
     */
    U from(T databaseObject, ConverterContext ctx);

    /**
     * Convert and write a user object to a database object.
     *
     * @param userObject The user object.
     * @param ctx The context of this conversion.
     * @return The database object.
     */
    T to(U userObject, ConverterContext ctx);

    @Override
    default T to(U userObject) {
        return to(userObject, converterContext());
    }

    @Override
    default U from(T databaseObject) {
        return from(databaseObject, converterContext());
    }

    /**
     * Inverse this converter.
     */
    @Override
    @NotNull
    default ScopedConverter<U, T> inverse() {
        return Converters.inverse(this);
    }

    /**
     * Chain a converter to this converter.
     */
    @Override
    @NotNull
    default <X> ScopedConverter<T, X> andThen(Converter<? super U, X> converter) {
        return Converters.of(this, converter);
    }

    /**
     * Turn this converter into a converter for arrays.
     */
    @Override
    @NotNull
    default ScopedConverter<T[], U[]> forArrays() {
        return Converters.forArrays(this);
    }

    /**
     * Turn a {@link Converter} into a {@link ScopedConverter}.
     */
    @NotNull
    static <T, U> ScopedConverter<T, U> scoped(Converter<T, U> converter) {
        if (converter instanceof ScopedConverter<T, U> s)
            return s;
        else
            return new AbstractScopedConverter<T, U>(converter.fromType(), converter.toType()) {
                @Override
                public U from(T t, ConverterContext scope) {
                    return converter.from(t);
                }

                @Override
                public T to(U u, ConverterContext scope) {
                    return converter.to(u);
                }
            };
    }

    /**
     * Construct a new read-only converter from a function.
     *
     * @param <T> the database type
     * @param <U> the user type
     * @param fromType The database type
     * @param toType The user type
     * @param from A function converting from T to U when reading from the
     *            database.
     * @param to A function converting from U to T when writing to the database.
     * @return The converter.
     * @see Converter
     */
    @NotNull
    static <T, U> ScopedConverter<T, U> from(
        Class<T> fromType,
        Class<U> toType,
        BiFunction<? super T, ? super ConverterContext, ? extends U> from
    ) {
        return of(fromType, toType, from, notImplementedBiFunction());
    }

    /**
     * Construct a new write-only converter from a function.
     *
     * @param <T> the database type
     * @param <U> the user type
     * @param fromType The database type
     * @param toType The user type
     * @param from A function converting from T to U when reading from the
     *            database.
     * @param to A function converting from U to T when writing to the database.
     * @return The converter.
     * @see Converter
     */
    @NotNull
    static <T, U> ScopedConverter<T, U> to(
        Class<T> fromType,
        Class<U> toType,
        BiFunction<? super U, ? super ConverterContext, ? extends T> to
    ) {
        return of(fromType, toType, notImplementedBiFunction(), to);
    }

    /**
     * Construct a new converter from functions.
     *
     * @param <T> the database type.
     * @param <U> the user type.
     * @param fromType The database type.
     * @param toType The user type.
     * @param from A function converting from T to U when reading from the
     *            database.
     * @param to A function converting from U to T when writing to the database.
     * @return The converter.
     * @see Converter
     */
    @NotNull
    static <T, U> ScopedConverter<T, U> of(
        Class<T> fromType,
        Class<U> toType,
        BiFunction<? super T, ? super ConverterContext, ? extends U> from,
        BiFunction<? super U, ? super ConverterContext, ? extends T> to
    ) {
        return new AbstractScopedConverter<T, U>(fromType, toType) {

            @Override
            public final U from(T t, ConverterContext scope) {
                return from.apply(t, scope);
            }

            @Override
            public final T to(U u, ConverterContext scope) {
                return to.apply(u, scope);
            }
        };
    }

    /**
     * Construct a new converter from functions.
     * <p>
     * This works like {@link Converter#of(Class, Class, Function, Function)},
     * except that both conversion {@link Function}s are decorated with a
     * function that always returns <code>null</code> for <code>null</code>
     * inputs.
     * <p>
     * Example:
     * <p>
     * <pre><code>
     * Converter&lt;String, Integer&gt; converter =
     *   Converter.ofNullable(String.class, Integer.class, Integer::parseInt, Object::toString);
     *
     * // No exceptions thrown
     * assertNull(converter.from(null));
     * assertNull(converter.to(null));
     * </code></pre>
     *
     * @param <T> the database type
     * @param <U> the user type
     * @param fromType The database type
     * @param toType The user type
     * @param from A function converting from T to U when reading from the
     *            database.
     * @param to A function converting from U to T when writing to the database.
     * @return The converter.
     * @see Converter
     */
    @NotNull
    static <T, U> ScopedConverter<T, U> ofNullable(
        Class<T> fromType,
        Class<U> toType,
        BiFunction<? super T, ? super ConverterContext, ? extends U> from,
        BiFunction<? super U, ? super ConverterContext, ? extends T> to
    ) {
        return of(fromType, toType, nullable(from), nullable(to));
    }

    /**
     * Construct a new read-only converter from a function.
     * <p>
     * This works like {@link Converter#from(Class, Class, Function)}, except
     * that the conversion {@link Function} is decorated with a function that
     * always returns <code>null</code> for <code>null</code> inputs.
     * <p>
     * Example:
     * <p>
     * <pre><code>
     * Converter&lt;String, Integer&gt; converter =
     *   Converter.fromNullable(String.class, Integer.class, Integer::parseInt);
     *
     * // No exceptions thrown
     * assertNull(converter.from(null));
     * </code></pre>
     *
     * @param <T> the database type.
     * @param <U> the user type.
     * @param fromType The database type.
     * @param toType The user type.
     * @param from A function converting from T to U when reading from the
     *            database.
     * @return The converter.
     * @see Converter
     */
    @NotNull
    static <T, U> Converter<T, U> fromNullable(
        Class<T> fromType,
        Class<U> toType,
        BiFunction<? super T, ? super ConverterContext, ? extends U> from
    ) {
        return of(fromType, toType, nullable(from), notImplementedBiFunction());
    }

    /**
     * Construct a new write-only converter from a function.
     * <p>
     * This works like {@link Converter#to(Class, Class, Function)}, except that
     * the conversion {@link Function} is decorated with a function that always
     * returns <code>null</code> for <code>null</code> inputs.
     * <p>
     * Example:
     * <p>
     * <pre><code>
     * Converter&lt;String, Integer&gt; converter =
     *   Converter.toNullable(String.class, Integer.class, Object::toString);
     *
     * // No exceptions thrown
     * assertNull(converter.to(null));
     * </code></pre>
     *
     * @param <T> the database type
     * @param <U> the user type
     * @param fromType The database type
     * @param toType The user type
     * @param to A function converting from U to T when writing to the database.
     * @return The converter.
     * @see Converter
     */
    @NotNull
    static <T, U> Converter<T, U> toNullable(
        Class<T> fromType,
        Class<U> toType,
        BiFunction<? super U, ? super ConverterContext, ? extends T> to
    ) {
        return of(fromType, toType, notImplementedBiFunction(), nullable(to));
    }
}
