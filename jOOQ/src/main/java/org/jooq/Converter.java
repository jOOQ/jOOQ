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

import static org.jooq.Converters.notImplemented;
import static org.jooq.Converters.nullable;

import java.io.Serializable;
import java.util.function.Function;

import org.jooq.exception.DataTypeException;
import org.jooq.impl.AbstractConverter;
import org.jooq.impl.SQLDataType;

import org.jetbrains.annotations.NotNull;

/**
 * A <code>Converter</code> for data types.
 * <p>
 * A general data type conversion interface that can be provided to jOOQ at
 * various places in order to perform custom data type conversion. Conversion is
 * directed, this means that the <code>Converter</code> is used
 * <ul>
 * <li>to load database types converting them to user types "FROM" the database.
 * Hence, {@link #fromType()} is the type as defined in the database. Think of
 * "FROM" = "reading".</li>
 * <li>to store user types converting them to database types "TO" the database.
 * Think of "TO" = "writing". Hence, {@link #toType()} is the user-defined
 * type</li>
 * </ul>
 * <p>
 * Note: In order to avoid unwanted side-effects, it is highly recommended (yet
 * not required) for {@link #from(Object)} and {@link #to(Object)} to be
 * <strong>reciprocal</strong>. The two methods are reciprocal, if for all
 * <code>X and Y</code>, it can be said that
 * <ul>
 * <li>if <code>Y.equals(converter.from(X))</code>, then
 * <code>X.equals(converter.to(Y))</code>.</li>
 * <li><code>X.equals(converter.from(converter.to(X)))</code></li>
 * <li><code>X.equals(converter.to(converter.from(X)))</code></li>
 * </ul>
 * <p>
 * Furthermore, it is recommended (yet not required) that
 * <ul>
 * <li><code>converter.from(null) == null</code></li>
 * <li><code>converter.to(null) == null</code></li>
 * </ul>
 * <p>
 * <h3>Creating user defined {@link DataType}s</h3>
 * <p>
 * jOOQ provides built in data types through {@link SQLDataType}. Users can
 * define their own data types programmatically by calling
 * {@link DataType#asConvertedDataType(Converter)} or
 * {@link DataType#asConvertedDataType(Binding)}, for example. Custom data types
 * can also be defined on generated code using the
 * <code>&lt;forcedType/&gt;</code> configuration, see <a href=
 * "https://www.jooq.org/doc/latest/manual/code-generation/codegen-advanced/codegen-config-database/codegen-database-forced-types/">the
 * manual for more details</a>
 *
 * @author Lukas Eder
 * @param <T> The database type - i.e. any type available from
 *            {@link SQLDataType}
 * @param <U> The user type
 */
public interface Converter<T, U> extends Serializable {

    /**
     * Read and convert a database object to a user object.
     *
     * @param databaseObject The database object.
     * @return The user object.
     */
    U from(T databaseObject);

    /**
     * Convert and write a user object to a database object.
     *
     * @param userObject The user object.
     * @return The database object.
     */
    T to(U userObject);

    /**
     * The database type.
     */
    @NotNull
    Class<T> fromType();

    /**
     * The user type.
     */
    @NotNull
    Class<U> toType();

    /**
     * Inverse this converter.
     */
    @NotNull
    default Converter<U, T> inverse() {
        return Converters.inverse(this);
    }

    /**
     * Chain a converter to this converter.
     */
    @NotNull
    default <X> Converter<T, X> andThen(Converter<? super U, X> converter) {
        return Converters.of(this, converter);
    }

    /**
     * Turn this converter into a converter for arrays.
     */
    @NotNull
    default Converter<T[], U[]> forArrays() {
        return Converters.forArrays(this);
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
    static <T, U> Converter<T, U> of(
        Class<T> fromType,
        Class<U> toType,
        Function<? super T, ? extends U> from,
        Function<? super U, ? extends T> to
    ) {
        return new AbstractConverter<T, U>(fromType, toType) {

            @Override
            public final U from(T t) {
                return from.apply(t);
            }

            @Override
            public final T to(U u) {
                return to.apply(u);
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
    static <T, U> Converter<T, U> from(
        Class<T> fromType,
        Class<U> toType,
        Function<? super T, ? extends U> from
    ) {
        return of(fromType, toType, from, notImplemented());
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
    static <T, U> Converter<T, U> to(
        Class<T> fromType,
        Class<U> toType,
        Function<? super U, ? extends T> to
    ) {
        return of(fromType, toType, notImplemented(), to);
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
     * <code><pre>
     * Converter&lt;String, Integer&gt; converter =
     *   Converter.ofNullable(String.class, Integer.class, Integer::parseInt, Object::toString);
     *
     * // No exceptions thrown
     * assertNull(converter.from(null));
     * assertNull(converter.to(null));
     * </pre></code>
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
    static <T, U> Converter<T, U> ofNullable(
        Class<T> fromType,
        Class<U> toType,
        Function<? super T, ? extends U> from,
        Function<? super U, ? extends T> to
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
     * <code><pre>
     * Converter&lt;String, Integer&gt; converter =
     *   Converter.fromNullable(String.class, Integer.class, Integer::parseInt);
     *
     * // No exceptions thrown
     * assertNull(converter.from(null));
     * </pre></code>
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
        Function<? super T, ? extends U> from
    ) {
        return of(fromType, toType, nullable(from), notImplemented());
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
     * <code><pre>
     * Converter&lt;String, Integer&gt; converter =
     *   Converter.toNullable(String.class, Integer.class, Object::toString);
     *
     * // No exceptions thrown
     * assertNull(converter.to(null));
     * </pre></code>
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
        Function<? super U, ? extends T> to
    ) {
        return of(fromType, toType, notImplemented(), nullable(to));
    }
}
