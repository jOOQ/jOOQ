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
package org.jooq;

import static org.jooq.Converters.notImplemented;
import static org.jooq.Converters.nullable;

import java.io.Serializable;
import java.util.function.Function;

import org.jooq.impl.AbstractContextConverter;
import org.jooq.impl.DSL;
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
 * <h3>Reciprocity</h3>
 * <p>
 * In order to avoid unwanted side-effects, it is highly recommended (yet not
 * required) for {@link #from(Object)} and {@link #to(Object)} to be
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
 * Irrespective of the <code>Converter</code>'s encoding of <code>null</code>
 * values above, an implementation must be able to handle <code>null</code>
 * values.
 * <p>
 * <h3>When <code>Converter</code> is invoked</h3>
 * <p>
 * Unlike {@link Binding}, which is limited to JDBC interactions, a
 * {@link Converter} can be invoked also outside of the context of reading /
 * writing data from / to the JDBC driver. This may include converting nested
 * data structures, such as {@link DSL#multiset(TableLike)} or
 * {@link DSL#row(SelectField...)}, recursively. These two particular expression
 * types are special cases. With other nested (but opaque to jOOQ) data
 * structures, it is not possible to recursively apply a {@link Converter}. For
 * example, a {@link DSL#jsonObject(JSONEntry...)}, while constructed with jOOQ,
 * produces "opaque" contents and thus cannot recursively apply
 * {@link Converter}.
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
 * <p>
 * <a href=
 * "https://www.jooq.org/doc/latest/manual/sql-execution/fetching/ad-hoc-converter/">Ad-hoc
 * converters</a> allow for attaching a converter directly to a SQL expression
 * in order to keep related logic close together. E.g.:
 *
 * <pre>
 * <code>
 * Result&lt;Record1&lt;BookId>> result =
 * ctx.select(BOOK.ID.convertFrom(BookId::new))
 *    .from(BOOK)
 *    .fetch();
 * </code>
 * </pre>
 * <p>
 * In the above example, a one-way only converter (only implementing
 * {@link #from(Object)}, not {@link #to(Object)}) is attached to the
 * <code>BOOK.ID</code> {@link Field} expression in order to convert between
 * e.g. {@link Long} and <code>BookId</code>. While visually embedded in the
 * query itself, the {@link Converter} is still only applied when reading the
 * {@link Result}, and like any other kind of {@link Converter} (including those
 * attached to {@link Field} by the code generator) thus has no effect on the
 * generated SQL or the contents reported by the database.
 *
 * @author Lukas Eder
 * @param <T> The database type - i.e. any type available from
 *            {@link SQLDataType}
 * @param <U> The user type
 */
public interface Converter<T, U> extends Serializable {

    /**
     * Read and convert a database object to a user object.
     * <p>
     * Implementations that don't support this conversion are expected to
     * override {@link #fromSupported()} to indicate lack of support.
     *
     * @param databaseObject The database object.
     * @return The user object.
     */
    U from(T databaseObject);

    /**
     * Convert and write a user object to a database object.
     * <p>
     * Implementations that don't support this conversion are expected to
     * override {@link #toSupported()} to indicate lack of support.
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
     * Whether this is a write only converter.
     * <p>
     * A write only converter implements only {@link #to(Object)} but not
     * {@link #from(Object)}.
     */
    default boolean fromSupported() {
        return true;
    }

    /**
     * Whether this is a read only converter.
     * <p>
     * A read only converter implements only {@link #from(Object)} but not
     * {@link #to(Object)}.
     */
    default boolean toSupported() {
        return true;
    }

    /**
     * Construct a new converter from functions.
     * <p>
     * The resulting {@link Converter} is expected to return <code>true</code>
     * on both {@link Converter#fromSupported()} and
     * {@link Converter#toSupported()}.
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
        return of(fromType, toType, from, to, true, true);
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
     * @param fromSupported Whether the from function is supported.
     * @param toSupported Whether the to function is supported.
     * @return The converter.
     * @see Converter
     */
    @NotNull
    static <T, U> Converter<T, U> of(
        Class<T> fromType,
        Class<U> toType,
        Function<? super T, ? extends U> from,
        Function<? super U, ? extends T> to,
        boolean fromSupported,
        boolean toSupported
    ) {
        return new AbstractContextConverter<T, U>(fromType, toType) {

            @Override
            public final boolean fromSupported() {
                return fromSupported;
            }

            @Override
            public final boolean toSupported() {
                return toSupported;
            }

            @Override
            public final U from(T t, ConverterContext scope) {
                return from.apply(t);
            }

            @Override
            public final T to(U u, ConverterContext scope) {
                return to.apply(u);
            }
        };
    }

    /**
     * Construct a new read-only converter from a function.
     * <p>
     * The resulting {@link Converter} returns false on
     * {@link Converter#toSupported()}.
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
        return of(fromType, toType, from, notImplemented(), true, false);
    }

    /**
     * Construct a new write-only converter from a function.
     * <p>
     * The resulting {@link Converter} returns false on
     * {@link Converter#fromSupported()}.
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
        return of(fromType, toType, notImplemented(), to, false, true);
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
     * <p>
     * The resulting {@link Converter} is expected to return <code>true</code>
     * on both {@link Converter#fromSupported()} and
     * {@link Converter#toSupported()}.
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
     * <p>
     * The resulting {@link Converter} is expected to return <code>true</code>
     * on both {@link Converter#fromSupported()} and
     * {@link Converter#toSupported()}.
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
        Function<? super U, ? extends T> to,
        boolean fromSupported,
        boolean toSupported
    ) {
        return of(fromType, toType, nullable(from), nullable(to), fromSupported, toSupported);
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
     * <p>
     * The resulting {@link Converter} returns false on
     * {@link Converter#toSupported()}.
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
        return of(fromType, toType, nullable(from), notImplemented(), true, false);
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
     * The resulting {@link Converter} returns false on
     * {@link Converter#fromSupported()}.
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
        return of(fromType, toType, notImplemented(), nullable(to), false, true);
    }
}
