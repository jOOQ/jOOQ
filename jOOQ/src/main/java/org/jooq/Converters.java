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

import java.io.Serializable;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.jooq.exception.DataTypeException;
import org.jooq.impl.AbstractConverter;
import org.jooq.impl.AbstractScopedConverter;
import org.jooq.impl.IdentityConverter;
import org.jooq.impl.SQLDataType;

import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;

/**
 * A chain of converters.
 *
 * @param <T> The database type - i.e. any type available from
 *            {@link SQLDataType}
 * @param <U> The user type
 * @author Lukas Eder
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public final class Converters<T, U> extends AbstractScopedConverter<T, U> {

    final ScopedConverter[] chain;

    /**
     * Create an identity converter.
     */
    @NotNull
    public static <T> ScopedConverter<T, T> identity(final Class<T> type) {
        return new IdentityConverter<T>(type);
    }

    /**
     * Create an identity converter.
     *
     * @deprecated - [#10689] - 3.14.0 - This converter does not work. Do not
     *             use this method, use {@link #identity(Class)} instead.
     */
    @Deprecated(forRemoval = true, since = "3.14")
    @NotNull
    public static <T, U> ScopedConverter<T, U> of() {
        return new Converters();
    }

    /**
     * Create a single converter.
     *
     * @deprecated - [#10689] - 3.14.0 - This method does not provide any useful
     *             functionality and will be removed in the future.
     */
    @Deprecated(forRemoval = true, since = "3.14")
    @NotNull
    public static <T, U> ScopedConverter<T, U> of(Converter<T, U> converter) {
        return new Converters(ScopedConverter.scoped(converter));
    }

    /**
     * Chain two converters.
     */
    @NotNull
    public static <T, X1, U> ScopedConverter<T, U> of(Converter<T, ? extends X1> c1, Converter<? super X1, U> c2) {
        return new Converters(
            ScopedConverter.scoped(c1),
            ScopedConverter.scoped(c2)
        );
    }

    /**
     * Chain three converters.
     */
    @NotNull
    public static <T, X1, X2, U> ScopedConverter<T, U> of(Converter<T, ? extends X1> c1, Converter<? super X1, ? extends X2> c2, Converter<? super X2, U> c3) {
        return new Converters(
            ScopedConverter.scoped(c1),
            ScopedConverter.scoped(c2),
            ScopedConverter.scoped(c3)
        );
    }

    /**
     * Chain four converters.
     */
    @NotNull
    public static <T, X1, X2, X3, U> ScopedConverter<T, U> of(Converter<T, ? extends X1> c1, Converter<? super X1, ? extends X2> c2, Converter<? super X2, ? extends X3> c3, Converter<? super X3, U> c4) {
        return new Converters(
            ScopedConverter.scoped(c1),
            ScopedConverter.scoped(c2),
            ScopedConverter.scoped(c3),
            ScopedConverter.scoped(c4)
        );
    }

    /**
     * Inverse a converter.
     */
    public static <T, U> Converter<U, T> inverse(final Converter<T, U> converter) {
        return inverse(ScopedConverter.scoped(converter));
    }

    /**
     * Inverse a converter.
     */
    public static <T, U> ScopedConverter<U, T> inverse(final ScopedConverter<T, U> converter) {

        // [#11099] Allow instanceof checks on IdentityConverter for performance reasons
        if (converter instanceof IdentityConverter)
            return (ScopedConverter<U, T>) converter;
        else
            return ScopedConverter.of(converter.toType(), converter.fromType(), converter::to, converter::from);
    }

    /**
     * Create a converter that can convert arrays with the component types being
     * the argument converter's types.
     */
    public static <T, U> Converter<T[], U[]> forArrays(Converter<T, U> converter) {
        return forArrays(ScopedConverter.scoped(converter));
    }

    /**
     * Create a converter that can convert arrays with the component types being
     * the argument converter's types.
     */
    public static <T, U> ScopedConverter<T[], U[]> forArrays(ScopedConverter<T, U> converter) {
        if (converter instanceof ArrayComponentConverter<T, U> a)
            return a.converter;
        else
            return new ArrayConverter<>(converter);
    }

    /**
     * Create a converter that can convert component types based on the argument
     * converter, which converts array types.
     */
    public static <T, U> Converter<T, U> forArrayComponents(Converter<T[], U[]> converter) {
        return forArrayComponents(ScopedConverter.scoped(converter));
    }

    /**
     * Create a converter that can convert component types based on the argument
     * converter, which converts array types.
     */
    public static <T, U> Converter<T, U> forArrayComponents(ScopedConverter<T[], U[]> converter) {
        if (converter instanceof ArrayConverter<T, U> a)
            return a.converter;
        else
            return new ArrayComponentConverter<>(converter);
    }

    Converters(ScopedConverter... chain) {
        super(chain[0].fromType(), chain[chain.length - 1].toType());

        this.chain = chain;
    }

    @Override
    public final U from(T t, ConverterScope scope) {
        Object result = t;

        for (int i = 0; i < chain.length; i++)
            result = chain[i].from(result, scope);

        return (U) result;
    }

    @Override
    public final T to(U u, ConverterScope scope) {
        Object result = u;

        for (int i = chain.length - 1; i >= 0; i--)
            result = chain[i].to(result, scope);

        return (T) result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String separator = " -> ";

        sb.append("Converters [ ");
        sb.append(fromType().getName());

        for (Converter<?, ?> converter : chain) {
            sb.append(separator);
            sb.append(converter.toType().getName());
        }

        sb.append(" ]");
        return sb.toString();
    }

    static final <T, U> Function<T, U> nullable(Function<? super T, ? extends U> f) {
        return f instanceof Serializable
            ? (Function<T, U> & Serializable) t -> t == null ? null
            : f.apply(t) : t -> t == null ? null : f.apply(t);
    }

    static final <T, U> BiFunction<T, ConverterScope, U> nullable(BiFunction<? super T, ? super ConverterScope, ? extends U> f) {
        return f instanceof Serializable
            ? (BiFunction<T, ConverterScope, U> & Serializable) (t, x) -> t == null ? null
            : f.apply(t, x) : (t, x) -> t == null ? null : f.apply(t, x);
    }

    static final <T, U> Function<T, U> notImplemented() {
        return t -> { throw new DataTypeException("Conversion function not implemented"); };
    }

    static final <T, U> BiFunction<T, ConverterScope, U> notImplementedBiFunction() {
        return (t, x) -> { throw new DataTypeException("Conversion function not implemented"); };
    }

    /**
     * An unknown type that is used when users do not provide any explicit user
     * type {@link Class} reference e.g. in
     * {@link DataType#asConvertedDataTypeFrom(Function)} or
     * {@link DataType#asConvertedDataTypeTo(Function)}
     */
    @Internal
    public static final class UnknownType {
        private UnknownType() {}
    }
}
